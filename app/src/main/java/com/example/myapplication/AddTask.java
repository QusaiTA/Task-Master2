package com.example.myapplication;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toolbar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import androidx.room.Room;

import com.amplifyframework.api.graphql.model.ModelMutation;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.generated.model.Task;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;


public class AddTask extends AppCompatActivity {

    private TaskDao taskDao;
    private AppDataBase appDataBase;

    String img = "";
    public Uri imgLink;
    String imageKey = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);

        appDataBase= Room.databaseBuilder(getApplicationContext(),AppDataBase.class,"task").allowMainThreadQueries().build();
        taskDao=appDataBase.taskDao();
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        TextView textView = findViewById(R.id.textView6);


        Button upload =findViewById(R.id.SaveImage);
        upload.setOnClickListener(view -> {
            fileChoose();
            uploadInputStream();
        });
        Intent intent = getIntent();
        String action = intent.getAction();
        String type = intent.getType();
        ImageView image = findViewById(R.id.NewImageView);
        if (Intent.ACTION_SEND.equals(action) && type != null) {
            if (type.startsWith("image/")) {
                Uri imageUri = (Uri) intent.getParcelableExtra(Intent.EXTRA_STREAM);
                if (imageUri != null) {
                    image.setImageURI(imageUri);
                    image.setVisibility(View.VISIBLE);

                }
            }
        }
        Button button = findViewById(R.id.button3);
        button.setOnClickListener(new View.OnClickListener() {
            int count =1;



            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View view) {

                EditText taskTitle =findViewById(R.id.editTextTextPersonName);
                EditText taskBody  =findViewById(R.id.editTextTextPersonName2);
                EditText taskState =findViewById(R.id.editTextTextPersonName3);

                String taskTitleVal =taskTitle.getText().toString();
                String taskBodyVal  =taskBody.getText().toString();
                String taskStateVal =taskState.getText().toString();
                String imageURl = sharedPreferences.getString("FileUrlForReal", "no files");




                textView.setText("Task Count :" + count++);
                com.amplifyframework.datastore.generated.model.Task todo = Task.builder()
                        .title(taskTitleVal)
                        .body(taskBodyVal)
                        .state(taskStateVal)
                        .img(imageURl)
                        .build();

                Amplify.API.mutate(
                        ModelMutation.create(todo),
                        response -> Log.i("MyAmplifyApp", "Added Todo with id: " + response.getData().getId()),
                        error -> Log.e("MyAmplifyApp", "Create failed", error)
                );




            }
        });

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_baseline_arrow_back_24);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent   = new Intent(AddTask.this, MainActivity.class);
                startActivity(intent);
            }
        });





    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
        String fileName = sdf.format(new Date());
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        File uploadFile = new File(getApplicationContext().getFilesDir(), fileName);
        try {

            InputStream exampleInputStream = getContentResolver().openInputStream(data.getData());
            OutputStream outputStream = new FileOutputStream(uploadFile);
            img = data.getData().toString();
            byte[] buff = new byte[1024];
            int length;
            while ((length = exampleInputStream.read(buff)) > 0) {
                outputStream.write(buff, 0, length);
            }
            exampleInputStream.close();
            outputStream.close();
            Amplify.Storage.uploadFile(
                    fileName + ".jpg",
                    uploadFile,
                    result -> {
                        Log.i("MyAmplifyAppUpload", "Successfully uploaded: " + result.getKey());
                        Amplify.Storage.getUrl(result.getKey(), urlResult -> {
                            sharedPreferences.edit().putString("FileUrlForReal", urlResult.getUrl().toString()).apply();
                        }, urlError -> {
                            Log.e("TAG", "onActivityResult: Error please dont be mad");
                        });
                    },
                    storageFailure -> Log.e("MyAmplifyApp", "Upload failed", storageFailure)
            );
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void uploadInputStream() {
        if (imgLink != null) {
            try {
                InputStream exampleInputStream = getContentResolver().openInputStream(imgLink);

                Amplify.Storage.uploadInputStream(
                        img,
                        exampleInputStream,
                        result -> Log.i("MyAmplifyApp", "Successfully uploaded: " + result.getKey()),
                        storageFailure -> Log.e("MyAmplifyApp", "Upload failed", storageFailure)
                );
            } catch (FileNotFoundException error) {
                Log.e("MyAmplifyApp", "Could not find file to open for input stream.", error);
            }
        }
    }

    public void fileChoose() {
        Intent fileChoose = new Intent(Intent.ACTION_GET_CONTENT);
        fileChoose.setType("*/*");
        fileChoose = Intent.createChooser(fileChoose, "choose file");
        startActivityForResult(fileChoose, 1234);
    }

}