package com.ferrarib.explorer.core.ui

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.ferrarib.explorer.R
import com.ferrarib.explorer.domain.models.Coordinates
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker

@Composable
fun CountryMapView(
    coordinates: Coordinates,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    
    DisposableEffect(coordinates) {
        Configuration.getInstance().load(context, context.getSharedPreferences("osmdroid", 0))
        onDispose { }
    }
    
    AndroidView(
        modifier = modifier.clip(RoundedCornerShape(8.dp)),
        factory = { ctx ->
            MapView(ctx).apply {
                setTileSource(TileSourceFactory.MAPNIK)
                setMultiTouchControls(true)
                isHorizontalMapRepetitionEnabled = false
                isVerticalMapRepetitionEnabled = false
                setScrollableAreaLimitDouble(null)
                
                zoomController.setVisibility(org.osmdroid.views.CustomZoomButtonsController.Visibility.ALWAYS)
                
                layoutParams = android.view.ViewGroup.LayoutParams(
                    android.view.ViewGroup.LayoutParams.MATCH_PARENT,
                    android.view.ViewGroup.LayoutParams.MATCH_PARENT
                )
                
                val geoPoint = GeoPoint(coordinates.latitude, coordinates.longitude)
                controller.setZoom(5.0)
                controller.animateTo(geoPoint)
                
                val marker = Marker(this).apply {
                    position = geoPoint
                    setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
                    title = context.getString(R.string.country_location)
                }
                overlays.add(marker)
            }
        },
        update = { mapView ->
            val geoPoint = GeoPoint(coordinates.latitude, coordinates.longitude)
            mapView.controller.animateTo(geoPoint)
            mapView.overlays.clear()
            val marker = Marker(mapView).apply {
                position = geoPoint
                setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
                title = context.getString(R.string.country_location)
            }
            mapView.overlays.add(marker)
        },
        onRelease = { mapView ->
            mapView.onPause()
        }
    )
}
