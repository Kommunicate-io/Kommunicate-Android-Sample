package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import io.kommunicate.KmConversationBuilder;
import io.kommunicate.Kommunicate;
import io.kommunicate.callbacks.KmCallback;

public class TaskActivity extends AppCompatActivity {

    Button supportButton;
    Button homeButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);

        supportButton = findViewById(R.id.supportButton);
        homeButton=findViewById(R.id.homeButton);

        supportButton.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {

                new KmConversationBuilder(TaskActivity.this)
                        .setAppId("20194e45736ec11ebe1c249bd198b9fbd")
                        .launchConversation(new KmCallback() {
                            @Override
                            public void onSuccess(Object message) {
                                Log.d("Conversation", "Success : " + message);
                            }

                            @Override
                            public void onFailure(Object error) {
                                Log.d("Conversation", "Failure : " + error);
                            }
                        });
               // Toast.makeText(TaskActivity.this, "Clicked Support button", Toast.LENGTH_SHORT).show();
            }

        });
        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent intent = new Intent(TaskActivity.this, MainActivity.class);
                startActivity(intent);
            }

        });


        //Navigating from one activity to another
    }

   /* void openSupportChat(View view) {
        Kommunicate.init(getApplicationContext(), "20194e45736ec11ebe1c249bd198b9fbd");

    }

    void openHomeScreen(View view) {
        TextView text = new TextView(this);
        text.setText("Hello World, TESTING SUCCESS");
        setContentView(text);
    }*/
}
