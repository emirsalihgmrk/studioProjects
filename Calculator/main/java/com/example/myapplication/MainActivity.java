package com.example.myapplication;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {
    private EditText newNumber;
    private EditText result;
    private TextView displayOperation;

    private Double operand1;
    private String pendingOperation = "=";

    private static final String STATE_PENDING_OPERATION = "PendingOperation";
    private static final String STATE_OPERAND1 = "Operand1";

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

        newNumber = findViewById(R.id.newNumber);
        result = findViewById(R.id.result);
        displayOperation = findViewById(R.id.operation);

        Button btn_0 = findViewById(R.id.btn_0);
        Button btn_1 = findViewById(R.id.btn_1);
        Button btn_2 = findViewById(R.id.btn_2);
        Button btn_3 = findViewById(R.id.btn_3);
        Button btn_4 = findViewById(R.id.btn_4);
        Button btn_5 = findViewById(R.id.btn_5);
        Button btn_6 = findViewById(R.id.btn_6);
        Button btn_7 = findViewById(R.id.btn_7);
        Button btn_8 = findViewById(R.id.btn_8);
        Button btn_9 = findViewById(R.id.btn_9);
        Button btn_dot = findViewById(R.id.btn_dot);
        Button btn_equals = findViewById(R.id.btn_equals);
        Button btn_plus = findViewById(R.id.btn_plus);
        Button btn_minus = findViewById(R.id.btn_minus);
        Button btn_multiply = findViewById(R.id.btn_multiply);
        Button btn_slash = findViewById(R.id.btn_slash);
        Button btn_neg = findViewById(R.id.btn_neg);

        View.OnClickListener listener = v -> {
            Button b = (Button) v;
            newNumber.append(b.getText().toString());
        };
        btn_0.setOnClickListener(listener);
        btn_1.setOnClickListener(listener);
        btn_2.setOnClickListener(listener);
        btn_3.setOnClickListener(listener);
        btn_4.setOnClickListener(listener);
        btn_5.setOnClickListener(listener);
        btn_6.setOnClickListener(listener);
        btn_7.setOnClickListener(listener);
        btn_8.setOnClickListener(listener);
        btn_9.setOnClickListener(listener);
        btn_dot.setOnClickListener(listener);

        View.OnClickListener opListener = v -> {
            Button b = (Button) v;
            String op = b.getText().toString();
            String value = newNumber.getText().toString();
            try {
                Double doubleValue = Double.valueOf(value);
                performOperation(doubleValue,op);
            }catch (NumberFormatException e){
                newNumber.setText("");
            }
            pendingOperation = op;
            displayOperation.setText(pendingOperation);
        };

        btn_equals.setOnClickListener(opListener);
        btn_plus.setOnClickListener(opListener);
        btn_minus.setOnClickListener(opListener);
        btn_multiply.setOnClickListener(opListener);
        btn_slash.setOnClickListener(opListener);

        btn_neg.setOnClickListener(view -> {
            String value = newNumber.getText().toString();
            if (value.isEmpty()) {
                newNumber.setText("-");
            }else{
                try {
                    Double doubleValue = Double.valueOf(value);
                    doubleValue *= -1;
                    newNumber.setText(String.valueOf(doubleValue));
                }catch (NumberFormatException e){
                    newNumber.setText("");
                }
            }
        });
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putString(STATE_PENDING_OPERATION,pendingOperation);
        if (operand1 != null){
            outState.putDouble(STATE_OPERAND1,operand1);
        }
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        pendingOperation = savedInstanceState.getString(STATE_PENDING_OPERATION);
        operand1 = savedInstanceState.getDouble(STATE_OPERAND1);
        displayOperation.setText(pendingOperation);
    }

    private void performOperation(Double value, String operation){
        if(operand1==null){
            operand1 = value;
        }else{
            if (pendingOperation.equals("=")) {
                pendingOperation = operation;
            }

            switch (pendingOperation){
                case "=":
                    operand1 = value;
                    break;
                case "/":
                    if (value == 0) {
                        operand1 = 0.0;
                    }else {
                        operand1 /= value;
                    }
                    break;
                case "*":
                    operand1 *= value;
                    break;
                case "+":
                    operand1 += value;
                    break;
                case "-":
                    operand1 -= value;
                    break;
            }
            result.setText(String.valueOf(operand1));
            newNumber.setText("");
        }
    }
}