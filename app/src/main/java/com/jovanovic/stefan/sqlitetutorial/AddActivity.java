package com.jovanovic.stefan.sqlitetutorial;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class AddActivity extends AppCompatActivity {
    ImageView mImageView, mColorView;
    TextView mResultTV;
    String hexa, hex;
    Bitmap bitmap;
    EditText title_input, type_input;
    Button add_button;
    int pixel;
    SharePref sharePref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        sharePref = new SharePref(this);
        if(sharePref.loadNightModeState()){
            setTheme(R.style.DarkTheme);
        }else setTheme(R.style.AppTheme);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        mImageView = findViewById(R.id.imageView);
        mResultTV = findViewById(R.id.textView);
        mColorView = findViewById(R.id.colorView);

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


        title_input = findViewById(R.id.title_input);
        type_input = findViewById(R.id.type_input);
        add_button = findViewById(R.id.add_button);
        add_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyDatabaseHelper myDB = new MyDatabaseHelper(AddActivity.this);
                myDB.addBook(title_input.getText().toString().trim(),
                        type_input.getText().toString().trim(),
                        hexa.trim());

            }
        });
        if(hexa == null){
            Toast.makeText(this, "Please choose the color too ", Toast.LENGTH_SHORT).show();}

    }

}
