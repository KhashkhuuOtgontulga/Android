package com.example.converter;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private EditText input;
    private TextView output;
    private TextView history;
    private RadioButton rb;
    private RadioButton rb2;
    private TextView txt;
    private TextView txt2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rb = findViewById(R.id.radioButton);
        rb2 = findViewById(R.id.radioButton2);
        input = findViewById(R.id.fahrenheitText);
        output = findViewById(R.id.celsiusText);
        history = findViewById(R.id.historyText);
        history.setMovementMethod(new ScrollingMovementMethod());
        txt = findViewById(R.id.textView2);
        txt2 = findViewById(R.id.textView3);
    }

    public void convertTemp(View w) {
        // if there is input entered
        if (input.getText().length() != 0) {
            double d = Double.parseDouble(input.getText().toString());
            double res = 0;
            // fahrenheit to celsius
            if (rb.isChecked()) {
                res = (d - 32.0) / 1.8;
                txt.setText("Fahrenheit Degrees:");
                txt2.setText("Celsius Degrees:");
                history.append(String.format("%,.1f F ==> %,.1f C", d, res));
            }
            // celsius to fahrenheit
            if (rb2.isChecked()) {
                res = ((d * 1.8) + 32);
                txt.setText("Celsius Degrees:");
                txt2.setText("Fahrenheit Degrees:");
                history.append(String.format("%,.1f C ==> %,.1f F", d, res));
            }
            output.setText(String.format("%,.1f", res));
            history.append("\n");
        }
    }


    public void clearHistory(View w) {
        input.setText(null);
        output.setText(null);
        history.setText(null);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString("INPUT", input.getText().toString());
        outState.putString("OUTPUT", output.getText().toString());
        outState.putString("HISTORY", history.getText().toString());

        // call super last
        super.onSaveInstanceState(outState);

    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        // call super first
        super.onRestoreInstanceState(savedInstanceState);

        input.setText(savedInstanceState.getString("INPUT"));
        output.setText(savedInstanceState.getString("OUTPUT"));
        history.setText(savedInstanceState.getString("HISTORY"));

    }
}
