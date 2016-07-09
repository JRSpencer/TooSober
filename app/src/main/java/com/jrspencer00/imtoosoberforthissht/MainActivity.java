package com.jrspencer00.imtoosoberforthissht;

import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    private TextView outputHeight;
    private TextView displayDrinksNeeded;
    private TextView largeDrinksDisplay;
    private TextView timeUntilDeparture;
    private TextView currentBAC;
    private TextView desiredBAC;

    private EditText userInputFeet;
    private EditText userInputInches;
    private EditText userInputWeight;
    private EditText drinkSize;
    private EditText percentageAlcohol;
    private EditText selectedHour;
    private EditText selectedMinute;

    private Button calculate;
    private Button addDrink;

    private RadioButton genderMale;
    private RadioButton genderFemale;
    private RadioButton timeAM;
    private RadioButton timePM;

    private Spinner drunkLevel;

    double[] BAC = {0.03,
            0.06,
            0.10,
            0.15,
            0.20,
            0.25,
            0.3};

    double alcoholInGrams;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
        userInputFeet = (EditText) findViewById(R.id.heightFeet);
        userInputInches = (EditText) findViewById(R.id.heightInches);
        userInputWeight = (EditText) findViewById(R.id.weight);
        selectedHour = (EditText) findViewById(R.id.leavingHour);
        selectedMinute = (EditText) findViewById(R.id.leavingMinute);
        drinkSize = (EditText) findViewById(R.id.drinkSize);
        percentageAlcohol = (EditText) findViewById(R.id.percentAlcohol);

        calculate = (Button) findViewById(R.id.calculate);
        addDrink = (Button) findViewById(R.id.addDrink);

        drunkLevel = (Spinner) findViewById(R.id.drunkLevel);

        largeDrinksDisplay = (TextView) findViewById(R.id.drinksDisplay);
        timeUntilDeparture = (TextView) findViewById(R.id.timeRemaining);
        currentBAC = (TextView) findViewById(R.id.currentBAC);
        desiredBAC = (TextView) findViewById(R.id.desiredBAC);

        genderMale = (RadioButton) findViewById(R.id.genderMale);
        genderFemale = (RadioButton) findViewById(R.id.genderFemale);
        timeAM = (RadioButton) findViewById(R.id.amTime);
        timePM = (RadioButton) findViewById(R.id.pmTime);


               calculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (userInputFeet.getText().length() > 0 && userInputInches.getText().length() > 0 &&
                        userInputWeight.getText().length() > 0 && (genderFemale.isChecked() || genderMale.isChecked()) &&
                        selectedHour.getText().length() > 0 && selectedMinute.getText().length() > 0 &&
                        (timeAM.isChecked() || timePM.isChecked())) {
                    double genderConstant;
                    long drunkLevelValue = drunkLevel.getSelectedItemId();
                    double desiredBAC = BAC[(int) drunkLevelValue];
//                    Toast.makeText(MainActivity.this, "Desired BAC is " + desiredBAC, Toast.LENGTH_LONG).show();
                    int totalHeightInches = (12 * Integer.parseInt(userInputFeet.getText().toString())) + Integer.parseInt(userInputInches.getText().toString());
//                    outputHeight.setText(Integer.toString(totalHeightInches));
                    int userWeight = Integer.parseInt(userInputWeight.getText().toString());

//                    Toast.makeText(MainActivity.this, "weight is " + userWeight, Toast.LENGTH_LONG).show();
                    double weightInGrams = userWeight * 453.592;

                    if (genderMale.isChecked()) {
                        genderConstant = 0.68;
                    } else if (genderFemale.isChecked()) {
                        genderConstant = 0.55;
                    } else{
                        Toast.makeText(MainActivity.this, "Please select a gender", Toast.LENGTH_LONG).show();
                        genderConstant = 0;
                    }

                    int drinkSizeValue = Integer.parseInt(drinkSize.getText().toString());
                    double alcoholContent = Double.parseDouble(percentageAlcohol.getText().toString());
                    double drinksNeeded = (desiredBAC / 100) * weightInGrams * genderConstant;
                    alcoholInGrams = calculateAlcoholInGrams(drinkSizeValue, alcoholContent);   //drinkSizeValue * (alcoholContent / 100) * 0.789;
                    Toast.makeText(MainActivity.this, "Alcohol in Gram is " + alcoholInGrams, Toast.LENGTH_LONG).show();
//                    drinksNeeded = Math.round(drinksNeeded * 100 / alcoholInGrams);
//                    drinksNeeded /= 100;
                    drinksNeeded = round(drinksNeeded / alcoholInGrams);
//                    displayDrinksNeeded.setText(Double.toString(drinksNeeded));
                    largeDrinksDisplay.setText(Double.toString(drinksNeeded) + " drinks needed to reach desired level of drunkenness");
                    int hour = Integer.parseInt(selectedHour.getText().toString());
                    if(Integer.parseInt(selectedHour.getText().toString()) > 0 &&
                            Integer.parseInt(selectedHour.getText().toString()) < 13 ){
                        if(Integer.parseInt(selectedMinute.getText().toString()) >= 0 &&
                                Integer.parseInt(selectedMinute.getText().toString()) < 60 ){
                            Calendar cal = Calendar.getInstance();
                            if (timePM.isChecked() && hour < 12) {
                                hour += 12;
                            }
                            int hourDifference;
                            if (hour < cal.get(Calendar.HOUR_OF_DAY)) {
                                hourDifference = hour - cal.get(Calendar.HOUR_OF_DAY) + 24;
                            }else{
                                hourDifference = hour  - cal.get(Calendar.HOUR_OF_DAY);
                            }

                            int minuteDifference = Integer.parseInt(selectedMinute.getText().toString()) - cal.get(Calendar.MINUTE);
                            int minutesUntilDeparture = hourDifference * 60 + minuteDifference;
                            timeUntilDeparture.setText(Integer.toString(minutesUntilDeparture) + " Minutes until you want to leave.");
                            double minutesPerDrink = round(minutesUntilDeparture / drinksNeeded);
                            timeUntilDeparture.setText("You need to have a drink every " + Double.toString(minutesPerDrink) + " minutes");
                        }else{
                            Toast.makeText(MainActivity.this, "INVALID MINUTES", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Toast.makeText(MainActivity.this, "INVALID HOUR", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(MainActivity.this, "Please Fill in the Content", Toast.LENGTH_LONG).show();
                }

            }
        });

        addDrink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (drinkSize.getText().length() > 0 && percentageAlcohol.getText().length() > 0) {
                    int drinkSizeValue = Integer.parseInt(drinkSize.getText().toString());
                    double alcoholContent = Double.parseDouble(percentageAlcohol.getText().toString());
                    alcoholInGrams = calculateAlcoholInGrams(drinkSizeValue, alcoholContent);
                    double drinksPerDrink = round(alcoholInGrams / 14);
                    currentBAC.setText(Double.toString(drinksPerDrink));
                } else {
                    Toast.makeText(MainActivity.this, "Please fill out drink size", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public static double calculateAlcoholInGrams(int drinkSize, double percentAlcohol) {
        double alcoholInGrams = drinkSize * (percentAlcohol / 100) * 0.789;
        return alcoholInGrams;
    }

    public static double round(double Value) {
        double roundedValue;
        roundedValue = Math.round(Value * 100);
        roundedValue /= 100;
        return roundedValue;
    }
}
