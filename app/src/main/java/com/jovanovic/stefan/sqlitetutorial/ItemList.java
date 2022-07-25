package com.jovanovic.stefan.sqlitetutorial;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class ItemList extends AppCompatActivity {
    SharePref sharePref;
    String id, title, color;
    TextView parent_id;
    private SQLiteDatabase mDatabase;
    private GroceryAdapter mAdaptor;
    private EditText mEditTextName;
    private TextView mTextViewAmount ;
    private int mAmount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        sharePref = new SharePref(this);
        if(sharePref.loadNightModeState()){
            setTheme(R.style.DarkTheme);
        }else setTheme(R.style.AppTheme);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_list);

        getAndSetData();
        ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor(color));
        ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setTitle(title);
            ab.setBackgroundDrawable(colorDrawable);
        }

        MyDatabaseHelper dbHelper = new MyDatabaseHelper(this);
        mDatabase = dbHelper.getWritableDatabase();
        RecyclerView recyclerView;
        recyclerView = findViewById(R.id.recyclerViewS);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        try {
            mAdaptor = new GroceryAdapter(this, getAllList());
        } catch (Exception e) {
            Toast.makeText(this, "Error Awal", Toast.LENGTH_SHORT).show();
        }
        recyclerView.setAdapter(mAdaptor);
        try {
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
        } catch (Exception e) {
            Toast.makeText(this, "Error Delete", Toast.LENGTH_SHORT).show();
        }


        mEditTextName = findViewById(R.id.edt_Items);
        mTextViewAmount = findViewById(R.id.txt_amount);
        Button buttonIncrease = findViewById(R.id.plus_btn);
        Button buttonDecrease = findViewById(R.id.minus_btn);
        Button buttonAdd = findViewById(R.id.input_btn);
        Button buttonR = findViewById(R.id.save_btn);
        buttonR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentE = new Intent(ItemList.this,MainActivity.class);
                startActivity(intentE);
                finish();
            }
        });
        buttonIncrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                increase();
            }
        });
        buttonDecrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                decrease();
            }
        });
        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    addItem();
            }
        });

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1 ){
            recreate();
            finish();
        }
    }

    public void getAndSetData(){
        if(getIntent().hasExtra("id") && getIntent().hasExtra("title") && getIntent().hasExtra("color")){
            //Getting Data from Intent
            id = getIntent().getStringExtra("id");
            title = getIntent().getStringExtra("title");
            color = getIntent().getStringExtra("color");
            parent_id = findViewById(R.id.txt_parentID);
            parent_id.setText(id);
            Log.d("stev", id+" "+title+" "+color);
        }else{
            Toast.makeText(this, "Empty", Toast.LENGTH_SHORT).show();
        }
    }


    private void increase() {
        mAmount++;
        mTextViewAmount.setText(String.valueOf(mAmount));
    }
    private void decrease() {
        if (mAmount > 0) {
            mAmount--;
            mTextViewAmount.setText(String.valueOf(mAmount));
        }
    }
    private void addItem() {
        try {
            if (mEditTextName.getText().toString().trim().length() == 0) {
                return;
            }
            String name = mEditTextName.getText().toString();
            ContentValues cv = new ContentValues();
            cv.put(GroceryContract.GroceryEntry.COLUMN_NAME, name);
            cv.put(GroceryContract.GroceryEntry.COLUMN_AMOUNT, mAmount);
            cv.put(GroceryContract.GroceryEntry.COLUMN_LIST_ID, Integer.parseInt(id));
            mDatabase.insert(GroceryContract.GroceryEntry.TABLE_NAME, null, cv);
            mAdaptor.swapCursor(getAllList());
            mEditTextName.getText().clear();
        } catch (Exception e) {
            Toast.makeText(this, "Error Add", Toast.LENGTH_SHORT).show();
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