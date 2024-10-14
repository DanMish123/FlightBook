package com.example.dn05;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static android.graphics.Color.RED;

public class Rezervacija extends AppCompatActivity {

    Spinner destinacija;
    RadioButton enosmerna;
    RadioButton dvosmerna;
    RadioButton prvi;
    RadioButton ekonomski;
    RadioButton poslovni;
    EditText datumPrihoda;
    EditText datumOdhoda;
    EditText steviloOdraslih;
    EditText steviloOtrok;
    EditText steviloDojenckov;
    EditText cena;
    Button nadaljujNaPlacilo;
    Button izpolniPodatke;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rezervacija);

        datumPrihoda = findViewById(R.id.datumPrihoda);
        datumOdhoda = findViewById(R.id.datumOdhoda);
        destinacija = findViewById(R.id.destinacija);
        prvi = findViewById(R.id.razredPrvi);
        ekonomski = findViewById(R.id.razredEkonomski);
        poslovni = findViewById(R.id.razredPoslovni);
        steviloOdraslih = findViewById(R.id.steviloOdraslih);
        steviloOtrok = findViewById(R.id.steviloOtrok);
        steviloDojenckov = findViewById(R.id.steviloDojenckov);
        enosmerna = findViewById(R.id.enosmerna);
        dvosmerna = findViewById(R.id.dvosmerna);
        cena = findViewById(R.id.editTextNumberDecimal);
        nadaljujNaPlacilo = findViewById(R.id.nadaljujNaPlacilo);
        izpolniPodatke = findViewById(R.id.izpolniPodatke);

        nadaljujNaPlacilo.setEnabled(false);

        enosmerna.setOnClickListener(view -> dvosmerna.setChecked(false));
        dvosmerna.setOnClickListener(view -> enosmerna.setChecked(false));

        prvi.setOnClickListener(view -> {
            ekonomski.setChecked(false);
            poslovni.setChecked(false);
            calculateAndShowPrice();
        });
        ekonomski.setOnClickListener(view -> {
            prvi.setChecked(false);
            poslovni.setChecked(false);
            calculateAndShowPrice();
        });
        poslovni.setOnClickListener(view -> {
            prvi.setChecked(false);
            ekonomski.setChecked(false);
            calculateAndShowPrice();
        });

        steviloOdraslih.addTextChangedListener(priceTextWatcher);
        steviloOtrok.addTextChangedListener(priceTextWatcher);
        steviloDojenckov.addTextChangedListener(priceTextWatcher);

        if (savedInstanceState != null) {
            restoreState(savedInstanceState);
        }

    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("datumPrihoda", datumPrihoda.getText().toString());
        outState.putString("datumOdhoda", datumOdhoda.getText().toString());
        outState.putInt("destinacija", destinacija.getSelectedItemPosition());
        outState.putBoolean("prvi", prvi.isChecked());
        outState.putBoolean("ekonomski", ekonomski.isChecked());
        outState.putBoolean("poslovni", poslovni.isChecked());
        outState.putString("steviloOdraslih", steviloOdraslih.getText().toString());
        outState.putString("steviloOtrok", steviloOtrok.getText().toString());
        outState.putString("steviloDojenckov", steviloDojenckov.getText().toString());
        outState.putBoolean("enosmerna", enosmerna.isChecked());
        outState.putBoolean("dvosmerna", dvosmerna.isChecked());
    }

    private void restoreState(Bundle savedInstanceState) {
        datumPrihoda.setText(savedInstanceState.getString("datumPrihoda"));
        datumOdhoda.setText(savedInstanceState.getString("datumOdhoda"));
        destinacija.setSelection(savedInstanceState.getInt("destinacija"));
        prvi.setChecked(savedInstanceState.getBoolean("prvi"));
        ekonomski.setChecked(savedInstanceState.getBoolean("ekonomski"));
        poslovni.setChecked(savedInstanceState.getBoolean("poslovni"));
        steviloOdraslih.setText(savedInstanceState.getString("steviloOdraslih"));
        steviloOtrok.setText(savedInstanceState.getString("steviloOtrok"));
        steviloDojenckov.setText(savedInstanceState.getString("steviloDojenckov"));
        enosmerna.setChecked(savedInstanceState.getBoolean("enosmerna"));
        dvosmerna.setChecked(savedInstanceState.getBoolean("dvosmerna"));
    }

    private TextWatcher priceTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) { }

        @Override
        public void afterTextChanged(Editable s) {
            calculateAndShowPrice();
        }
    };

    private void calculateAndShowPrice() {
        int basePrice = 100;
        int classMultiplier = 1;

        if (prvi.isChecked()) {
            classMultiplier = 3;
        } else if (poslovni.isChecked()) {
            classMultiplier = 2;
        }

        int adults = parseIntegerOrDefault(steviloOdraslih.getText().toString(), 0);
        int children = parseIntegerOrDefault(steviloOtrok.getText().toString(), 0);
        int babies = parseIntegerOrDefault(steviloDojenckov.getText().toString(), 0);

        int totalPrice = basePrice * classMultiplier * (adults + (children / 2));
        cena.setText(String.valueOf(totalPrice));
    }

    private int parseIntegerOrDefault(String value, int defaultValue) {
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    public boolean validateDates(EditText datum) {
        String regex = "^\\d{2}/\\d{2}/\\d{4}$";
        Pattern pattern = Pattern.compile(regex);
        String date = datum.getText().toString();
        Matcher matcher = pattern.matcher(date);
        return matcher.matches();
    }

    private boolean allFieldsInputted() {
        return !datumOdhoda.getText().toString().isEmpty() &&
                !datumPrihoda.getText().toString().isEmpty() &&
                !steviloOdraslih.getText().toString().isEmpty() &&
                !steviloOtrok.getText().toString().isEmpty() &&
                !steviloDojenckov.getText().toString().isEmpty() &&
                !destinacija.getSelectedItem().equals("Izberi destinacijo") &&
                (prvi.isChecked() || ekonomski.isChecked() || poslovni.isChecked());
    }

    public void placiloCB(View view) {
        if (!allFieldsInputted()) {
            Toast.makeText(this, "Prosimo izpolnite vsa polja.", Toast.LENGTH_SHORT).show();
        } else if (!validateDates(datumPrihoda)) {
            datumPrihoda.setBackgroundResource(R.drawable.edit_text_error);
            Toast.makeText(this, "Datum prihoda mora biti v formatu DD/MM/YYYY.", Toast.LENGTH_SHORT).show();
        } else if (!validateDates(datumOdhoda)) {
            datumOdhoda.setBackgroundResource(R.drawable.edit_text_error);
            Toast.makeText(this, "Datum odhoda mora biti v formatu DD/MM/YYYY.", Toast.LENGTH_SHORT).show();
        }else {
            Intent intent = new Intent(this, Placilo.class);
            startActivityForResult(intent, 2);
        }
    }

    public void izpolniPodatkeCB(View view) {
        if (!allFieldsInputted()) {
            Toast.makeText(this, "Prosimo izpolnite vsa polja.", Toast.LENGTH_SHORT).show();
        } else if (!validateDates(datumPrihoda)) {
            datumPrihoda.setBackgroundResource(R.drawable.edit_text_error);
            Toast.makeText(this, "Datum prihoda mora biti v formatu DD/MM/YYYY.", Toast.LENGTH_SHORT).show();
        } else if (!validateDates(datumOdhoda)) {
            datumOdhoda.setBackgroundResource(R.drawable.edit_text_error);
            Toast.makeText(this, "Datum odhoda mora biti v formatu DD/MM/YYYY.", Toast.LENGTH_SHORT).show();
        } else {
            int totalPassengers = calculateTotalPassengers();
            Intent intent = new Intent(this, Potnik.class);
            intent.putExtra("totalPassengers", totalPassengers);
            startActivityForResult(intent, 1);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            nadaljujNaPlacilo.setEnabled(true);
        }
    }

    private int calculateTotalPassengers() {
        int adults = parseIntegerOrDefault(steviloOdraslih.getText().toString(), 0);
        int children = parseIntegerOrDefault(steviloOtrok.getText().toString(), 0);
        int babies = parseIntegerOrDefault(steviloDojenckov.getText().toString(), 0);
        return adults + children + babies;
    }
}
