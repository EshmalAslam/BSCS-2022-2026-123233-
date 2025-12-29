package com.example.tasky;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Is line se activity_main.xml wala layout screen par show hota hai
        setContentView(R.layout.activity_main);

        // Get Started Button ko XML se link kar rahe hain
        Button btnGetStarted = findViewById(R.id.btnGetStarted);

        // Button Click hone par kya hoga:
        btnGetStarted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Intent ke zariye hum TaskActivity (Task Manager Page) par jayenge
                Intent intent = new Intent(MainActivity.this, TaskActivity.class);
                startActivity(intent);
            }
        });
    }
}