package com.example.myapplication;


import android.content.Intent;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.Toolbar;

import com.squareup.picasso.Picasso;


public class TaskDetails extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_details);

        Intent intent=getIntent();
        String title=intent.getStringExtra("title");
        String body=intent.getStringExtra("body");
        String state=intent.getStringExtra("state");

        TextView textView= findViewById(R.id.titleFinish);
        textView.setText(title);
        TextView textView2= findViewById(R.id.bodyFinish);
        textView2.setText(body);
        TextView textView3= findViewById(R.id.stateFinish);
        textView3.setText(state);


        String url = intent.getExtras().getString("img");
        Log.i("url", "onCreate: "+ url);
        ImageView image = findViewById(R.id.imageViewImage);
        Picasso.get().load(url).into(image);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_baseline_arrow_back_24);


        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent   = new Intent(TaskDetails.this, MainActivity.class);
                startActivity(intent);
            }
        });


    }
}