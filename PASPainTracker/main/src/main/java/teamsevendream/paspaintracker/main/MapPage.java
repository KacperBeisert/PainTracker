package teamsevendream.paspaintracker.main;

import androidx.appcompat.app.ActionBar;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.mapbox.geojson.Feature;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Style;
import com.mapbox.mapboxsdk.style.layers.PropertyFactory;
import com.mapbox.mapboxsdk.style.layers.SymbolLayer;
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource;

public class MapPage extends AppCompatActivity {

    private static String TAG = "MapPage";
    private MapView mapView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        String lat = intent.getExtras().getString("lat");
        String lon = intent.getExtras().getString("lon");

        Log.d(TAG, lat + lon);

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.abs_layout);

        TextView tvTitle = findViewById(R.id.tvTitle);
        tvTitle.setText(R.string.app_name);

        Mapbox.getInstance(this, "pk.eyJ1Ijoiandhcm5vY2s2NTciLCJhIjoiY2pwOXBv" +
                "eGc3MjEzaTNwcHg1OGVpY2FpMiJ9.zT7riHkIfWZdYDOmrwX1_A");
        setContentView(R.layout.activity_map_page);
        mapView = (MapView) findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(@NonNull final MapboxMap mapboxMap) {

                mapboxMap.setCameraPosition(new CameraPosition.Builder()
                        .target(new LatLng(Double.parseDouble(lat), Double.parseDouble(lon)))
                        .build());

                mapboxMap.setStyle(Style.MAPBOX_STREETS, new Style.OnStyleLoaded() {
                    @Override
                    public void onStyleLoaded(@NonNull Style style) {
                        // Add the marker image to map
                        style.addImage("marker-icon-id",
                                BitmapFactory.decodeResource(
                                        MapPage.this.getResources(),
                                        R.drawable.mapbox_marker_icon_default));

                        GeoJsonSource geoJsonSource =
                                new GeoJsonSource("source-id", Feature.fromGeometry(
                                Point.fromLngLat(Float.parseFloat(lon), Float.parseFloat(lat))));
                        style.addSource(geoJsonSource);

                        SymbolLayer symbolLayer =
                                new SymbolLayer("layer-id", "source-id");
                        symbolLayer.withProperties(
                                PropertyFactory.iconImage("marker-icon-id")
                        );
                        style.addLayer(symbolLayer);

                    }
                });
            }
        });
    }

    // Add the mapView's own lifecycle methods to the activity's lifecycle methods
    @Override
    public void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }
}

