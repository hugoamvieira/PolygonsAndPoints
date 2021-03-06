package io.github.hugoamvieira.polygonsandpoints;

import android.content.Intent;
import android.os.Bundle;
import android.os.Debug;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ListView mPointsListView;
    private ListView mPolygonsListView;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.nav_points:
                    mPointsListView.setVisibility(View.VISIBLE);
                    mPolygonsListView.setVisibility(View.INVISIBLE);
                    return true;
                case R.id.nav_polygons:
                    mPointsListView.setVisibility(View.INVISIBLE);
                    mPolygonsListView.setVisibility(View.VISIBLE);
                    return true;
            }
            return false;
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        // Get data.json in assets folder
        String jsonData = null;

        try {
            // Create input stream to read the file onto a buffer and
            // then dump the contents into a String variable
            InputStream inpStr = getAssets().open("data.json");
            int s = inpStr.available();

            byte[] readBuffer = new byte[s];
            inpStr.read(readBuffer);
            inpStr.close();

            jsonData = new String(readBuffer, "UTF-8");

        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        // ArrayList to store all the points in the JSON file
        ArrayList<Point> pointsList = new ArrayList<>();
        ArrayList<Polygon> polygonsList = new ArrayList<>();

        if (!jsonData.isEmpty()) {
            try {
                // Read main object
                JSONObject obj = new JSONObject(jsonData);

                for (int i = 0; i < obj.names().length(); i++) {
                    if (i == 0) {
                        // Only points here.
                        JSONArray pointsJSONArray = obj.getJSONArray(obj.names().getString(i));

                        for (int j = 0; j < pointsJSONArray.length(); j++) {
                            JSONObject jsonPoint = pointsJSONArray.getJSONObject(j);

                            double latitude = jsonPoint.getDouble("latitude");
                            double longitude = jsonPoint.getDouble("longitude");
                            String name = jsonPoint.getString("name");

                            Point p = new Point(latitude, longitude, name);
                            pointsList.add(p);
                        }

                    } else {
                        // It's time for polygons.
                        JSONArray polygonPointsJSONArray = obj.getJSONArray(obj.names().getString(i));

                        // Add all polygon points to point array
                        Point[] polygonPoints = new Point[polygonPointsJSONArray.length()];

                        for (int j = 0; j < polygonPointsJSONArray.length(); j++) {
                            JSONObject jsonPoint = polygonPointsJSONArray.getJSONObject(j);

                            double latitude = jsonPoint.getDouble("latitude");
                            double longitude = jsonPoint.getDouble("longitude");
                            String name = jsonPoint.getString("name");

                            polygonPoints[j] = new Point(latitude, longitude, name);
                        }

                        // Create new Polygon object with the Points array.
                        // JSON polygon naming convention shall be "polygon-<NAME-IN-APP>
                        Polygon polygon = new Polygon(polygonPoints, obj.names().getString(i).split("-")[1] + " Polygon");
                        polygonsList.add(polygon);
                    }
                }


            } catch (Exception e) {
                e.printStackTrace();
                return;
            }
        }


        // Get stuff in the UI
        mPointsListView = (ListView) findViewById(R.id.points_list_view);
        mPolygonsListView = (ListView) findViewById(R.id.polygons_list_view);

        // Set invisible as switch statement is not triggered in the first run
        mPolygonsListView.setVisibility(View.INVISIBLE);

        final ArrayAdapter<Point> mPointsAdapter = new ArrayAdapter<>(MainActivity.this,
                android.R.layout.simple_list_item_1, android.R.id.text1, pointsList);
        mPointsListView.setAdapter(mPointsAdapter);

        mPointsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(getBaseContext(), MapsActivity.class);
                i.putExtra("Point", mPointsAdapter.getItem(position));
                startActivity(i);
            }
        });


        final ArrayAdapter<Polygon> mPolygonsAdapter = new ArrayAdapter<>(MainActivity.this,
                android.R.layout.simple_list_item_1, android.R.id.text1, polygonsList);
        mPolygonsListView.setAdapter(mPolygonsAdapter);

        mPolygonsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(getBaseContext(), MapsActivity.class);
                i.putExtra("Polygon", mPolygonsAdapter.getItem(position));
                startActivity(i);
            }
        });
    }
}
