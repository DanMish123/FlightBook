package com.example.dn05;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Potnik extends AppCompatActivity {

    EditText ime;
    EditText priimek;
    EditText datumRojstva;
    RadioButton moski;
    RadioButton zenski;

    int currentPassengerIndex = 0;
    int totalPassengers;

    List<Passenger> passengersData = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_potnik);

        ime = findViewById(R.id.imePotnik);
        priimek = findViewById(R.id.priimekPotnik);
        datumRojstva = findViewById(R.id.datumRojstva);
        moski = findViewById(R.id.spolMoski);
        zenski = findViewById(R.id.spolZenski);

        totalPassengers = getIntent().getIntExtra("totalPassengers", 0);

        for (int i = 0; i < totalPassengers; i++) {
            passengersData.add(new Passenger());
        }

        moski.setOnClickListener(view -> zenski.setChecked(false));
        zenski.setOnClickListener(view -> moski.setChecked(false));

        resetErrors();
    }

    private void resetErrors() {
        ime.setBackgroundResource(android.R.drawable.edit_text);
        priimek.setBackgroundResource(android.R.drawable.edit_text);
        datumRojstva.setBackgroundResource(android.R.drawable.edit_text);
    }

    private boolean allFieldsInputted() {
        return !ime.getText().toString().isEmpty() &&
                !priimek.getText().toString().isEmpty() &&
                !datumRojstva.getText().toString().isEmpty();
    }

    public boolean validateDates(EditText datum) {
        String regex = "^\\d{2}/\\d{2}/\\d{4}$";
        Pattern pattern = Pattern.compile(regex);
        String date = datum.getText().toString();
        Matcher matcher = pattern.matcher(date);
        return matcher.matches();
    }

    public boolean validateName(EditText name) {
        String regex = "^[a-zA-Z\\s]+$";
        Pattern pattern = Pattern.compile(regex);
        String nameText = name.getText().toString();
        Matcher matcher = pattern.matcher(nameText);
        return matcher.matches();
    }

    public void naprejCB(View view) {
        resetErrors();
        boolean valid = true;

        if (!allFieldsInputted()) {
            Toast.makeText(this, "Prosimo vnesite vsa polja.", Toast.LENGTH_SHORT).show();
            valid = false;
        } else {
            if (!validateDates(datumRojstva)){
                datumRojstva.setBackgroundResource(R.drawable.edit_text_error);
                Toast.makeText(this, "Datum rojstva mora biti v formatu DD/MM/YYYY", Toast.LENGTH_SHORT).show();
                valid = false;
            }
            if (!validateName(ime)){
                ime.setBackgroundResource(R.drawable.edit_text_error);
                Toast.makeText(this, "Ime mora vsebovati samo črke.", Toast.LENGTH_SHORT).show();
                valid = false;
            }
            if (!validateName(priimek)){
                priimek.setBackgroundResource(R.drawable.edit_text_error);
                Toast.makeText(this, "Priimek mora vsebovati samo črke.", Toast.LENGTH_SHORT).show();
                valid = false;
            }
        }

        if (valid) {
            saveCurrentPassengerData();
            if (currentPassengerIndex < totalPassengers - 1) {
                currentPassengerIndex++;
                showPassengerView(currentPassengerIndex);
            } else {
                Intent intent = new Intent();
                setResult(RESULT_OK, intent);
                finish();
            }
        }
    }

    public void nazajCB(View view) {
        if (currentPassengerIndex > 0) {
            saveCurrentPassengerData();
            currentPassengerIndex--;
            showPassengerView(currentPassengerIndex);
        }
    }

    private void saveCurrentPassengerData() {
        Passenger passenger = passengersData.get(currentPassengerIndex);
        passenger.setIme(ime.getText().toString());
        passenger.setPriimek(priimek.getText().toString());
        passenger.setDatumRojstva(datumRojstva.getText().toString());
        if (moski.isChecked())
            passenger.setSpol("Moski");
        else if (zenski.isChecked())
            passenger.setSpol("Zenski");
    }

    private void showPassengerView(int index) {
        resetErrors();
        Passenger passenger = passengersData.get(index);
        ime.setText(passenger.getIme());
        priimek.setText(passenger.getPriimek());
        datumRojstva.setText(passenger.getDatumRojstva());
        if ("Moski".equals(passenger.getSpol())) {
            moski.setChecked(true);
            zenski.setChecked(false);
        } else {
            moski.setChecked(false);
            zenski.setChecked(true);
        }
    }
}

class Passenger {
    private String ime;
    private String priimek;
    private String datumRojstva;
    private String spol;

    public String getIme() {
        return ime;
    }

    public void setIme(String ime) {
        this.ime = ime;
    }

    public String getPriimek() {
        return priimek;
    }

    public void setPriimek(String priimek) {
        this.priimek = priimek;
    }

    public String getDatumRojstva() {
        return datumRojstva;
    }

    public void setDatumRojstva(String datumRojstva) {
        this.datumRojstva = datumRojstva;
    }

    public String getSpol() {
        return spol;
    }

    public void setSpol(String spol) {
        this.spol = spol;
    }
}
