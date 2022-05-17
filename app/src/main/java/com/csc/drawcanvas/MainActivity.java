package com.csc.drawcanvas;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private ImageView imageView;
    private TextView backOff;
    private TextView forward;
    private TextView clear_tv;

    private DrawView drawView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageView = findViewById(R.id.img);
        RelativeLayout layout = (RelativeLayout) findViewById(R.id.ldd);

        drawView = new DrawView(this);
        layout.addView(drawView);

        backOff = findViewById(R.id.backOff);
        backOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawView.backOff();
            }
        });
        forward = findViewById(R.id.forward);
        forward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawView.forward();
            }
        });

        clear_tv = findViewById(R.id.clear_tv);
        clear_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawView.clear();
            }
        });
    }
}