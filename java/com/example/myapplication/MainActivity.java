package com.example.myapplication;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Random;

public class MainActivity extends AppCompatActivity {
    private EditText inputNumber;
    private TextView infoText;
    private LinearLayout linearLayout;
    private TextView textView;
    private Button restart;
    private Button submit;
    private int random;
    private final Random rand = new Random();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        inputNumber = findViewById(R.id.inputNumber);
        infoText = findViewById(R.id.infoText);
        linearLayout = findViewById(R.id.linearLayout);
        textView = findViewById(R.id.textView);
        restart = findViewById(R.id.restart);
        submit = findViewById(R.id.submit);
        textView.setText("");
        random = rand.nextInt(10);
        View.OnClickListener listenerSubmit = v -> {
            String info;
            if (inputNumber.getText().toString().equals(String.valueOf(random))){
                info = "Tebrikler doğru tahmin!";
            }else {
                info = "Yanlış tahmin!";
                if (Integer.parseInt(inputNumber.getText().toString()) > random){
                    info += "\nDaha küçük bir sayı giriniz.";
                }else {
                    info += "\nDaha büyük bir sayı giriniz";
                }
                if (linearLayout.getChildCount() > 0){
                    linearLayout.removeViewAt(linearLayout.getChildCount() - 1);
                }else{
                    inputNumber.setEnabled(false);
                }
            }
            infoText.setText(info);
        };
        View.OnClickListener listenerRestart = v -> {
            @SuppressLint("UnsafeIntentLaunch") Intent intent = getIntent();

            finish();

            startActivity(intent);
        };
        restart.setOnClickListener(listenerRestart);
        submit.setOnClickListener(listenerSubmit);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    public void onRestoreInstanceState(@Nullable Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }
}