package com.jovanovic.stefan.sqlitetutorial;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.widget.Toast;


public class NameList extends AppCompatActivity {
    SharePref sharePref;
    String id, title,color;

    private SQLiteDatabase mDatabase;
    private GroceryAdapter mAdaptor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        sharePref = new SharePref(this);
        if(sharePref.loadNightModeState()){
            setTheme(R.style.DarkTheme);
        }else setTheme(R.style.AppTheme);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_name_list);

       getAndSetIntentData();

        ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor(color));
        ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setTitle(title);
            ab.setBackgroundDrawable(colorDrawable);
        }

        MyDatabaseHelper dbHelper = new MyDatabaseHelper(this);
        mDatabase = dbHelper.getWritableDatabase();
        RecyclerView recyclerView;
        recyclerView = findViewById(R.id.recyclerView2);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdaptor = new GroceryAdapter(this, getAllList());
        recyclerView.setAdapter(mAdaptor);

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }
            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                removeItem((long) viewHolder.itemView.getTag());
            }
        }).attachToRecyclerView(recyclerView);
    }

    void getAndSetIntentData(){
            if(getIntent().hasExtra("id") && getIntent().hasExtra("title")
                    && getIntent().hasExtra("color")){
                //Getting Data from Intent
                id = getIntent().getStringExtra("id");
                title = getIntent().getStringExtra("title");
                color = getIntent().getStringExtra("color");
            }else{
                Toast.makeText(this, "Empty", Toast.LENGTH_SHORT).show();
            }
    }

    private void removeItem(long id) {
        mDatabase.delete(GroceryContract.GroceryEntry.TABLE_NAME,
                GroceryContract.GroceryEntry._ID + "=" + id, null);
        mAdaptor.swapCursor(getAllList());
    }

    private Cursor getAllList() {
        return mDatabase.rawQuery("SELECT * FROM " +
                GroceryContract.GroceryEntry.TABLE_NAME +
                " WHERE " + GroceryContract.GroceryEntry.COLUMN_LIST_ID +
                " = " + id, null);
    }

}