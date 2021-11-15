package com.example.myapplication;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import java.io.FileWriter;
import java.io.InputStream;


public class AddTask extends AppCompatActivity {

    private TaskDao taskDao;
    private AppDataBase appDataBase;

    public Intent pickFile;
    public String imgname;
    public Uri imgLink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);

        appDataBase= Room.databaseBuilder(getApplicationContext(),AppDataBase.class,"task").allowMainThreadQueries().build();
        taskDao=appDataBase.taskDao();

        TextView textView = findViewById(R.id.textView6);

        Button button = findViewById(R.id.button3);
        button.setOnClickListener(new View.OnClickListener() {
            int count =1;
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View view) {
                if (imgLink != null) {
                    try {
                        InputStream exampleInputStream = getContentResolver().openInputStream(imgLink);
                        Amplify.Storage.uploadInputStream(
                                imgname,
                                exampleInputStream,
                                result -> Log.i("MyAmplifyApp", "Successfully uploaded: " + result.getKey()),
                                storageFailure -> Log.e("MyAmplifyApp", "Upload failed", storageFailure)
                        );
                    } catch (FileNotFoundException error) {
                        Log.e("MyAmplifyApp", "Could not find file to open for input stream.", error);
                    }
                    System.out.println("yesssssssssssss");
                }
                EditText taskTitle =findViewById(R.id.editTextTextPersonName);
                EditText taskBody  =findViewById(R.id.editTextTextPersonName2);
                EditText taskState =findViewById(R.id.editTextTextPersonName3);

                String taskTitleVal =taskTitle.getText().toString();
                String taskBodyVal  =taskBody.getText().toString();
                String taskStateVal =taskState.getText().toString();



                textView.setText("Task Count :" + count++);
                com.amplifyframework.datastore.generated.model.Task todo = Task.builder()
                        .title(taskTitleVal)
                        .body(taskBodyVal)
                        .state(taskStateVal)
                        .img(imgname)
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


//        Button upload = findViewById(R.id.SaveImage);
//        upload.setOnClickListener(view -> {
//            uploadFile();
//        });
        Button upload =findViewById(R.id.SaveImage);
        upload.setOnClickListener(view -> {
            pickFile=new Intent(Intent.ACTION_GET_CONTENT);
            pickFile.setType("*/*");
            pickFile=Intent.createChooser(pickFile,"pickFile");
            startActivityForResult(pickFile,1234);
        });


    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        File file = new File(data.getData().getPath());
        imgname = file.getName();
        imgLink = data.getData();
    }

//    private void uploadFile() {
//        File exampleFile = new File(getApplicationContext().getFilesDir(), "ExampleKey");
//
//        try {
//            BufferedWriter writer = new BufferedWriter(new FileWriter(exampleFile));
//            writer.append("Example file contents");
//            writer.close();
//        } catch (Exception exception) {
//            Log.e("MyAmplifyApp", "Upload failed", exception);
//        }
//
//        Amplify.Storage.uploadFile(
//                "ExampleKey",
//                exampleFile,
//                result -> Log.i("MyAmplifyApp", "Successfully uploaded: " + result.getKey()),
//                storageFailure -> Log.e("MyAmplifyApp", "Upload failed", storageFailure)
//        );
//    }
}