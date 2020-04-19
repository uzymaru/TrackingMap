package com.projek.trackingmap.maps;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.projek.trackingmap.R;

import org.oscim.android.cache.TileCache;
import org.oscim.core.GeoPoint;
import org.oscim.layers.tile.buildings.BuildingLayer;
import org.oscim.layers.tile.vector.VectorTileLayer;
import org.oscim.layers.tile.vector.labeling.LabelLayer;
import org.oscim.map.Map;
import org.oscim.theme.ThemeLoader;
import org.oscim.theme.VtmThemes;
import org.oscim.tiling.source.oscimap4.OSciMap4TileSource;

import com.ujuizi.ramani.mapsapi.MapView;
import com.ujuizi.ramani.mapsapi.account.RMAccountManager;
import com.ujuizi.ramani.mapsapi.input.Marker;

import java.io.InputStream;
import java.util.ArrayList;

public class peta extends AppCompatActivity implements RMAccountManager.RMAccountManagerListener{

    private Map mMap;
    private MapView mMapView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_peta);
//        mapViewSS = findViewById(R.id.ss);

        // authentication before using our services
        RMAccountManager.init(this, this, "269c67b242bc0b77004fcee546ac2e5c");
    }
    private void setupMap() {

        OSciMap4TileSource tileSource = new OSciMap4TileSource();
        mMapView.setMapPosition(new GeoPoint(51.221297, 4.399128), 7);

        // for map cache
        TileCache mCache = new TileCache(this, null, "opensciencemap-tiles.db");
        mCache.setCacheSize(512 * (1 << 10));
        tileSource.setCache(mCache);


        // your base map with building
        VectorTileLayer mBaseLayer = mMap.setBaseMap(tileSource);
        mMap.layers().add(new BuildingLayer(mMap, mBaseLayer));
        mMap.layers().add(new LabelLayer(mMap, mBaseLayer));
        mMap.setTheme(ThemeLoader.load(VtmThemes.DEFAULT), true);

    }
    public void setUpMapObject() {
        mMapView.setScaleBar(true);
        //add marker, simply like this
        Marker mc1 = new Marker(mMapView, peta.this);
        mc1.add("Frascati, Italy", "", new GeoPoint(41.806830, 12.675612));

        //or you can add more parameter
        Marker mc2 = new Marker(mMapView, peta.this);
        mc2.setTitle("City Hall");
        mc2.setDescription("Antwerp, Belgium");
        mc2.add(new GeoPoint(51.221297, 4.399128));
    }

    @Override
    public void onMapAuthDone() {
        mMapView = findViewById(R.id.mapView);
        mMap = mMapView.map();
        setupMap();
        setUpMapObject();
//        RMMapTileLayer rmTileLayer = new RMMapTileLayer(mMap, new RMMapTileSource(layerID));
//        rmTileLayer.tileRenderer().setBitmapAlpha(0.5f); // layer opacity, range 0 - 1
//        mMap.layers(); //index hierarchy, 0 is basemap
//        mMap.render();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null)
            actionBar.setDisplayHomeAsUpEnabled(true);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        onBackPressed();
        return super.onOptionsItemSelected(item);
    }

}
