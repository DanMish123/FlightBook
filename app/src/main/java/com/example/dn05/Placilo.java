package com.example.dn05;

import android.content.DialogInterface;
import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static android.graphics.Color.RED;

class MyTextWatcher implements TextWatcher {
    private EditText editText;
    private int maxLength;

    public MyTextWatcher(EditText editText, int maxLength) {
        this.editText = editText;
        this.maxLength = maxLength;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) { }

    @Override
    public void afterTextChanged(Editable s) {
        if (s.length() > maxLength) {
            editText.setText(s.subSequence(0, maxLength));
            editText.setSelection(maxLength);
        }
    }
}

public class Placilo extends AppCompatActivity {

    EditText ime;
    EditText priimek;
    EditText stevilka;
    EditText cvv;
    EditText datumIzteka;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_placilo);

        ime = findViewById(R.id.imeKreditnaKartica);
        priimek = findViewById(R.id.priimekKreditnaKartica);
        stevilka = findViewById(R.id.stevilkaKreditnaKartica);
        cvv = findViewById(R.id.cvvKreditnaKartica);
        datumIzteka = findViewById(R.id.editTextDate);

        stevilka.addTextChangedListener(new MyTextWatcher(stevilka, 16));
        cvv.addTextChangedListener(new MyTextWatcher(cvv, 3));
    }

    private boolean allFieldsInputted() {
        return !ime.getText().toString().isEmpty() &&
                !priimek.getText().toString().isEmpty() &&
                !datumIzteka.getText().toString().isEmpty() &&
                !cvv.getText().toString().isEmpty() &&
                !stevilka.getText().toString().isEmpty();
    }

    public boolean validateDates(EditText datum) {
        String regex = "^\\d{2}/\\d{2}/\\d{4}$";
        Pattern pattern = Pattern.compile(regex);
        String date = datum.getText().toString();
        Matcher matcher = pattern.matcher(date);
        return matcher.matches();
    }

    public boolean validateName(EditText name) {
        String regex = "^[a-zA-Z\\s]+";
        Pattern pattern = Pattern.compile(regex);
        String date = name.getText().toString();
        Matcher matcher = pattern.matcher(date);
        return matcher.matches();
    }

    public void potrdiPlacilo(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Potrditev")
                .setMessage("Ali ste prepricani da zelite nadaljevati?")
                .setPositiveButton("Da", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (allFieldsInputted()) {
                            if (!validateDates(datumIzteka)){
                                datumIzteka.setBackgroundResource(R.drawable.edit_text_error);
                                Toast.makeText(Placilo.this, "Datum izteka mora biti v formatu DD/MM/YYYY.", Toast.LENGTH_SHORT).show();
                            } else if (!validateName(ime)){
                                ime.setBackgroundResource(R.drawable.edit_text_error);
                                Toast.makeText(Placilo.this, "Ime mora vsebovati samo črke.", Toast.LENGTH_SHORT).show();
                            } else if (!validateName(priimek)){
                                priimek.setBackgroundResource(R.drawable.edit_text_error);
                                Toast.makeText(Placilo.this, "Priimek mora vsebovati samo črke.", Toast.LENGTH_SHORT).show();
                            } else {
                                Intent intent = new Intent(Placilo.this, Potrditev.class);
                                startActivity(intent);
                            }
                        } else {
                            Toast.makeText(Placilo.this, "Prosimo vnesite vsa polja.", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .setNegativeButton("Ne", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .show();
    }

    public void nazajCB(View view) {
        setResult(RESULT_OK);
        finish();
    }
}
