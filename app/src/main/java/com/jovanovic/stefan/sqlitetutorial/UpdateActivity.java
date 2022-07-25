package com.jovanovic.stefan.sqlitetutorial;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.r0adkll.slidr.Slidr;
import com.r0adkll.slidr.model.SlidrInterface;

public class UpdateActivity extends AppCompatActivity {
    ImageView mImageView, mColorView;
    TextView mResultTV;
    EditText title_input, type_input, person_input;
    Button update_button, delete_button;
    Bitmap bitmap;
    String hexa,hex;
    String id, title, type, person, color;
    int pixel;
    SharePref sharePref;
    SlidrInterface slidrInterface;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        sharePref = new SharePref(this);
        if(sharePref.loadNightModeState()){
            setTheme(R.style.DarkTheme);
        }else setTheme(R.style.AppTheme);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);
        slidrInterface = Slidr.attach(this);
        mImageView = findViewById(R.id.imageView2);
        mResultTV = findViewById(R.id.textView2);
        mColorView = findViewById(R.id.colorView2);

        mImageView.setDrawingCacheEnabled(true);
        mImageView.buildDrawingCache(true);

        mImageView.setOnTouchListener(new View.OnTouchListener(){

            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN || motionEvent.getAction() == MotionEvent.ACTION_MOVE){
                    bitmap = mImageView.getDrawingCache();
                    try {
                        pixel = bitmap.getPixel((int)motionEvent.getX(), (int)motionEvent.getY());
                    } catch (Exception e) {
                        pixel = 0;
                    }

                    int r = Color.red(pixel);
                    int g = Color.green(pixel);
                    int b = Color.blue(pixel);

                    if (pixel == 0 ){
                        hex = "#000000";
                    } else {
                        hex = "#" + Integer.toHexString(pixel);
                    }
                    hexa = hex;

                    mColorView.setBackgroundColor(Color.rgb(r,g,b));
                    mResultTV.setText("RGB: "+ r +":"+ g +":"+ b +"\nHEX: " +hex);
                }
                return true;
            }
        });

        title_input = findViewById(R.id.title_input2);
        type_input = findViewById(R.id.type_input2);
        update_button = findViewById(R.id.update_button);
        delete_button = findViewById(R.id.delete_button);

        //First we call this
        getAndSetIntentData();
        mResultTV.setText("HEX : " + color);

        //Set actionbar title after getAndSetIntentData method
        ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor(color));
        ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setTitle(title);
            ab.setBackgroundDrawable(colorDrawable);
        }


            update_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    MyDatabaseHelper myDB = new MyDatabaseHelper(UpdateActivity.this);
                    title = title_input.getText().toString().trim();
                    type = type_input.getText().toString().trim();
                    color = hexa.trim();
                    myDB.updateData(id, title, type,color);

                }

            });
        if(hexa == null){Toast.makeText(this, "Please update the color too ", Toast.LENGTH_SHORT).show();}
        delete_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                confirmDialog();
            }
        });

    }

    void getAndSetIntentData(){
        if(getIntent().hasExtra("id") && getIntent().hasExtra("title") &&
                getIntent().hasExtra("type") && getIntent().hasExtra("color")){
            //Getting Data from Intent
            id = getIntent().getStringExtra("id");
            title = getIntent().getStringExtra("title");
            type = getIntent().getStringExtra("type");
            color = getIntent().getStringExtra("color");

            //Setting Intent Data
            title_input.setText(title);
            type_input.setText(type);
            mColorView.setBackgroundColor(Color.parseColor(color));
        }else{
            Toast.makeText(this, "Empty", Toast.LENGTH_SHORT).show();
        }
    }

    void confirmDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete " + title + " ?");
        builder.setMessage("Are you sure you want to delete " + title + " ?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                MyDatabaseHelper myDB = new MyDatabaseHelper(UpdateActivity.this);
                myDB.deleteOneRow(id);
                finish();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        builder.create().show();
    }
}
