package com.ferrarib.explorer.presentation.countrydetails

import android.util.Log
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.ferrarib.explorer.R

@Composable
fun CountryWebMapView(
    googleMapsUrl: String?,
    openStreetMapsUrl: String?,
    latitude: Double? = null,
    longitude: Double? = null,
    modifier: Modifier = Modifier
) {
    var isLoading by remember { mutableStateOf(true) }
    var useGoogleMaps by remember { mutableStateOf(true) }
    
    // Create HTML content with iframe for proper embedding
    fun createMapHtml(url: String?, isGoogleMaps: Boolean): String {
        val embedUrl = when {
            url == null && latitude != null && longitude != null -> {
                // Create coordinate-based URL as fallback
                if (isGoogleMaps) {
                    "https://maps.google.com/maps?q=$latitude,$longitude&z=6&output=embed"
                } else {
                    "https://www.openstreetmap.org/export/embed.html?bbox=${longitude-2},${latitude-2},${longitude+2},${latitude+2}&layer=mapnik&marker=$latitude,$longitude"
                }
            }
            url == null -> return "<html><body><div style='text-align:center; padding:50px; font-family:Arial;'>No map available</div></body></html>"
            isGoogleMaps -> {
                // Convert Google Maps URL to embeddable format
                if (url.contains("goo.gl") || url.contains("maps.google.com")) {
                    // For goo.gl links, try coordinate fallback if available
                    if (latitude != null && longitude != null) {
                        "https://maps.google.com/maps?q=$latitude,$longitude&z=6&output=embed"
                    } else {
                        // Try to extract the map ID or use original URL
                        url.replace("goo.gl/maps/", "maps.google.com/maps?mid=").let { processedUrl ->
                            if (processedUrl != url) {
                                "$processedUrl&output=embed"
                            } else {
                                // Fallback to original URL but add embed parameter
                                "$url&output=embed"
                            }
                        }
                    }
                } else {
                    url
                }
            }
            else -> {
                // Convert OpenStreetMap relation URL to embeddable map
                if (url.contains("openstreetmap.org/relation/")) {
                    val relationId = url.substringAfterLast("/")
                    "https://www.openstreetmap.org/export/embed.html?relation=$relationId&layer=mapnik"
                } else {
                    url
                }
            }
        }
        
        return """
            <!DOCTYPE html>
            <html>
            <head>
                <meta name="viewport" content="width=device-width, initial-scale=1.0">
                <style>
                    body { margin: 0; padding: 0; }
                    iframe { width: 100%; height: 100%; border: none; }
                </style>
            </head>
            <body>
                <iframe src="$embedUrl" allowfullscreen></iframe>
            </body>
            </html>
        """.trimIndent()
    }
    
    // Determine which HTML content to show
    val htmlContent = when {
        useGoogleMaps && (googleMapsUrl != null || (latitude != null && longitude != null)) -> 
            createMapHtml(googleMapsUrl, true)
        openStreetMapsUrl != null || (latitude != null && longitude != null) -> 
            createMapHtml(openStreetMapsUrl, false)
        googleMapsUrl != null -> 
            createMapHtml(googleMapsUrl, true)
        else -> null
    }
    
    // Only show if we have content to display
    htmlContent?.let { html ->
        Log.d("CountryWebMapView", "Loading HTML content with map")
        
        Box(modifier = modifier) {
            AndroidView(
                factory = { context ->
                    WebView(context).apply {
                        settings.apply {
                            javaScriptEnabled = true
                            domStorageEnabled = true
                            builtInZoomControls = true
                            displayZoomControls = false
                            loadWithOverviewMode = true
                            useWideViewPort = true
                            mixedContentMode = android.webkit.WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
                            userAgentString = settings.userAgentString + " AndroidExplorerApp"
                        }
                        webViewClient = object : WebViewClient() {
                            override fun onPageStarted(view: WebView?, url: String?, favicon: android.graphics.Bitmap?) {
                                super.onPageStarted(view, url, favicon)
                                Log.d("CountryWebMapView", "Page started loading: $url")
                                isLoading = true
                            }
                            
                            override fun onPageFinished(view: WebView?, url: String?) {
                                super.onPageFinished(view, url)
                                Log.d("CountryWebMapView", "Page finished loading: $url")
                                isLoading = false
                            }
                            
                            override fun onReceivedError(view: WebView?, request: android.webkit.WebResourceRequest?, error: android.webkit.WebResourceError?) {
                                super.onReceivedError(view, request, error)
                                Log.e("CountryWebMapView", "WebView error: ${error?.description}")
                                isLoading = false
                            }
                        }
                        webChromeClient = object : WebChromeClient() {
                            override fun onConsoleMessage(message: android.webkit.ConsoleMessage?): Boolean {
                                Log.d("CountryWebMapView", "Console: ${message?.message()}")
                                return super.onConsoleMessage(message)
                            }
                        }
                        loadData(html, "text/html", "UTF-8")
                    }
                },
                update = { webView ->
                    // Reload if content changed
                    webView.loadData(html, "text/html", "UTF-8")
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp)
            )
            
            // Loading indicator
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center)
                )
            }
            
            // Map provider toggle (only show if both URLs are available)
            if (googleMapsUrl != null && openStreetMapsUrl != null) {
                Row(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(8.dp)
                ) {
                    FilterChip(
                        selected = useGoogleMaps,
                        onClick = { 
                            useGoogleMaps = true
                            isLoading = true
                        },
                        label = { Text("Google Maps") },
                        modifier = Modifier.padding(end = 4.dp)
                    )
                    FilterChip(
                        selected = !useGoogleMaps,
                        onClick = { 
                            useGoogleMaps = false
                            isLoading = true
                        },
                        label = { Text("OpenStreetMap") }
                    )
                }
            }
        }
    }
}