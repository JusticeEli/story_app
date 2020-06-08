package com.justice.storyapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.flaviofaria.kenburnsview.KenBurnsView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity {
    private FloatingActionButton fob;
    private KenBurnsView kenBurnsView;
    public static int genre;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initWidgets();
        setOnClickListeners();
    }

    private void setOnClickListeners() {
        fob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ApplicationClass.documentSnapshot=null;
                startActivity(new Intent(MainActivity.this,AddActivity.class));
            }
        });
    }

    private void initWidgets() {
        kenBurnsView = findViewById(R.id.kenBurnsView);
        fob=findViewById(R.id.fob);
    }

    public void onBtnClick(View view) {
        kenBurnsView.pause();

        switch (view.getId()) {
            case R.id.comedyBtn:
                genre = 0;
                break;
            case R.id.fantacyBtn:
                genre = 1;
                break;
            case R.id.realLifeBtn:
                genre = 2;
                break;
            case R.id.motivationBtn:
                genre = 3;
                break;

        }
        startActivity(new Intent(this, StoriesActivity.class));


    }

    @Override
    protected void onResume() {
        super.onResume();
        kenBurnsView.resume();
    }
}
