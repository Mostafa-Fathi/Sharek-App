package com.example.khalid.sharektest.tour;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.example.khalid.sharektest.HomePage;
import com.example.khalid.sharektest.R;

/**
 * Created by Abdelrahman on 6/17/2017.
 */

public class tour_finish extends AppCompatActivity implements View.OnClickListener {

    TextView textView;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_tourview_finish);
        textView = (TextView) findViewById(R.id.Tour_circle_Finish);
        textView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(getApplicationContext(), HomePage.class);
        intent.putExtra("newAuthentication", true);
        startActivity(intent);
    }
}