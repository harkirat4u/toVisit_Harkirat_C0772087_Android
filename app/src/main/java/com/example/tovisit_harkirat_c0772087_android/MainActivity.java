package com.example.tovisit_harkirat_c0772087_android;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {


    SwipeMenuListView LV_places;
    List<Place> placeList;
    DatabaseHelper mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        mDatabase = new DatabaseHelper(this);
        LV_places = findViewById(R.id.locationList);

        SwipeMenuCreator creator = new SwipeMenuCreator() {

            @Override
            public void create(SwipeMenu menu) {

                SwipeMenuItem del_item = new SwipeMenuItem(getApplicationContext());
                del_item.setWidth(350);
                del_item.setBackground(new ColorDrawable(Color.RED));
                del_item.setTitle("Delete");
                del_item.setTitleSize(15);
                del_item.setTitleColor(Color.WHITE);
                menu.addMenuItem(del_item);

                SwipeMenuItem update_item = new SwipeMenuItem(getApplicationContext());
                update_item.setWidth(350);
                update_item.setBackground(new ColorDrawable(Color.GREEN));
                update_item.setTitle("Update");
                update_item.setTitleSize(15);
                update_item.setTitleColor(Color.WHITE);
                menu.addMenuItem(update_item);


            }
        };
        LV_places.setMenuCreator(creator);
        LV_places.setSwipeDirection(SwipeMenuListView.DIRECTION_LEFT);

        LV_places.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {

                switch(index){

                    case 0:
                        mDatabase.removePlace(placeList.get(position).getId());
                        loadPlaces();
                        break;
                    case 1:
                        Intent editI = new Intent(MainActivity.this, Map.class);
                        editI.putExtra("selectedPlace", placeList.get(position));
                        editI.putExtra("EDIT", true);
                        startActivity(editI);
                        break;
                    default:
                        break;
                }
                return false;
            }
        });
        LV_places.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent mapI = new Intent(MainActivity.this, Map.class);
                mapI.putExtra("selectedPlace", placeList.get(position));
                startActivity(mapI);
            }
        });
    }
    @Override
    protected void onStart() {
        super.onStart();
        loadPlaces();
    }
    public void showMap(View view) {
        Intent mapI = new Intent(this, Map.class);
        startActivity(mapI);
    }
    private void loadPlaces() {
        placeList = new ArrayList<>();
        Cursor cursor = mDatabase.getAllPlaces();
        if (cursor.moveToFirst()) {
            do {
                placeList.add(new Place(
                        cursor.getInt(0),
                        cursor.getString(1),
                        cursor.getString(2).equals("1"),
                        cursor.getDouble(3),
                        cursor.getDouble(4)
                ));
            } while (cursor.moveToNext());
            cursor.close();
            PlaceAdaptor adaptor = new PlaceAdaptor(this, R.layout.place_cell, placeList, mDatabase);
            LV_places.setAdapter(adaptor);

        }
    }

}
