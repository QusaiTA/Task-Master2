package com.example.myapplication;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.Toolbar;

import com.amplifyframework.core.Amplify;

import java.io.File;

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
        

//        ImageView myimg=findViewById(R.id.imageViewImage);
//
        if (intent.getExtras().getString("img") != null) {
            Amplify.Storage.downloadFile(
                    intent.getExtras().getString("img"),
                    new File(getApplicationContext().getFilesDir() + "/" + intent.getExtras().getString("img") + ".jpg"),
                    result -> {

                        ImageView myimg=findViewById(R.id.imageViewImage);
                        Bitmap bitmap = BitmapFactory.decodeFile(result.getFile().getPath());
                        myimg.setImageBitmap(bitmap);
                        Log.i("MyAmplifyApp", "Successfully downloaded: " + result.getFile().getName());
                    },
                    error -> Log.e("MyAmplifyApp", "Download Failure", error)
            );
        }

//        String img= intent.getExtras().getString("img");
////                if (intent.getExtras().getString("img") != null) {
//                    Amplify.Storage.downloadFile(
//                            "image",
//                            new File(getApplicationContext().getFilesDir() + "/download.jpg"),
//                            result -> {
//                                ImageView image = findViewById(R.id.imageViewImage);
//                                intent.getExtras().getString("img");
//                                image.setImageBitmap(BitmapFactory.decodeFile(result.getFile().getPath()));
//
//                                Log.i("MyAmplifyApp", "Successfully downloaded: " + result.getFile());
//                            },
//                            error -> Log.e("MyAmplifyApp", "Download Failure", error)
//                    );
////                }

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