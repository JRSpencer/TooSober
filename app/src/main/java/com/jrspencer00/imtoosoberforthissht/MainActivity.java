package com.jrspencer00.imtoosoberforthissht;

import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private TextView outputHeight;
    private TextView displayDrinksNeeded;
    private TextView largeDrinksDisplay;
    private TextView timeUntilDeparture;
    private TextView currentBAC;
    private TextView BACGoal;

    private EditText userInputFeet;
    private EditText userInputInches;
    private EditText userInputWeight;
    private EditText drinkSize;
    private EditText percentageAlcohol;
    private EditText selectedHour;
    private EditText selectedMinute;

    //    private Button calculate;
    private Button addDrink;
    private Button subtractDrink;

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

    List<Integer> drinkSizeArray = new ArrayList<Integer>();
    List<Double> drinkPercentArray = new ArrayList<Double>();

    double alcoholInGrams = 0;
    int minutesUntilDeparture;


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

//        calculate = (Button) findViewById(R.id.calculate);
        addDrink = (Button) findViewById(R.id.addDrink);
        subtractDrink = (Button) findViewById(R.id.substractDrink);

        drunkLevel = (Spinner) findViewById(R.id.drunkLevel);

        largeDrinksDisplay = (TextView) findViewById(R.id.drinksDisplay);
        timeUntilDeparture = (TextView) findViewById(R.id.timeRemaining);
        currentBAC = (TextView) findViewById(R.id.currentBAC);
        BACGoal = (TextView) findViewById(R.id.desiredBAC);

        genderMale = (RadioButton) findViewById(R.id.genderMale);
        genderFemale = (RadioButton) findViewById(R.id.genderFemale);
        timeAM = (RadioButton) findViewById(R.id.amTime);
        timePM = (RadioButton) findViewById(R.id.pmTime);


//        calculate.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//        });

        addDrink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (drinkSize.getText().length() > 0 && percentageAlcohol.getText().length() > 0) {
                    drinkSizeArray.add(Integer.parseInt(drinkSize.getText().toString()));
                    drinkPercentArray.add(Double.parseDouble(percentageAlcohol.getText().toString()));
                    //int drinkSizeValue = Integer.parseInt(drinkSize.getText().toString());
                    //double alcoholContent = Double.parseDouble(percentageAlcohol.getText().toString());
                    //alcoholInGrams = calculateAlcoholInGrams(drinkSizeValue, alcoholContent);

//                    alcoholInGrams = 0;
//                    for (int i = 0; i < drinkPercentArray.size(); i++) {
                    alcoholInGrams = alcoholInGrams + calculateAlcoholInGrams(drinkSizeArray.get(drinkSizeArray.size() - 1),
                            drinkPercentArray.get(drinkPercentArray.size() - 1));
                    double drinksPerDrink = round(alcoholInGrams / 14);
                    currentBAC.setText(Double.toString(drinksPerDrink));
//                    }

                } else {
                    Toast.makeText(MainActivity.this, "Please fill out drink size", Toast.LENGTH_LONG).show();
                }
            }
        });

        subtractDrink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (drinkSize.getText().length() > 0 && percentageAlcohol.getText().length() > 0 && drinkSizeArray.size() > 0) {
//                    drinkSizeArray.add(Integer.parseInt(drinkSize.getText().toString()));
//                    drinkPercentArray.add(Double.parseDouble(percentageAlcohol.getText().toString()));
                    alcoholInGrams = alcoholInGrams - calculateAlcoholInGrams(drinkSizeArray.get(drinkSizeArray.size() - 1),
                            drinkPercentArray.get(drinkPercentArray.size() - 1));
                    drinkSizeArray.remove(drinkSizeArray.size() - 1);
                    drinkPercentArray.remove(drinkPercentArray.size() - 1);
                    double drinksPerDrink = round(alcoholInGrams / 14);
                    currentBAC.setText(Double.toString(drinksPerDrink));

                } else {
                    Toast.makeText(MainActivity.this, "No drinks to remove", Toast.LENGTH_LONG).show();
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
            if (userInputFeet.getText().length() > 0 && userInputInches.getText().length() > 0 &&
                    userInputWeight.getText().length() > 0 && (genderFemale.isChecked() || genderMale.isChecked()) &&
                    selectedHour.getText().length() > 0 && selectedMinute.getText().length() > 0 &&
                    (timeAM.isChecked() || timePM.isChecked())) {


                int hour = Integer.parseInt(selectedHour.getText().toString());
                if (Integer.parseInt(selectedHour.getText().toString()) > 0 &&
                        Integer.parseInt(selectedHour.getText().toString()) < 13) {
                    if (Integer.parseInt(selectedMinute.getText().toString()) >= 0 &&
                            Integer.parseInt(selectedMinute.getText().toString()) < 60) {
                        Calendar cal = Calendar.getInstance();
                        if (timePM.isChecked() && hour < 12) {
                            hour += 12;
                        } else if (timeAM.isChecked() && hour == 12) {
                            hour -= 12;
                        }
                        int hourDifference;
                        if (hour < cal.get(Calendar.HOUR_OF_DAY)) {
                            hourDifference = hour - cal.get(Calendar.HOUR_OF_DAY) + 24;
                        } else {
                            hourDifference = hour - cal.get(Calendar.HOUR_OF_DAY);
                        }

                        int minuteDifference = Integer.parseInt(selectedMinute.getText().toString()) - cal.get(Calendar.MINUTE);
                        minutesUntilDeparture = hourDifference * 60 + minuteDifference;
//                            System.out.println(minutesUntilDeparture);
                        timeUntilDeparture.setText(Integer.toString(minutesUntilDeparture) + " Minutes until you want to leave.");
                        //double minutesPerDrink = round(minutesUntilDeparture / drinksNeeded);
                        //timeUntilDeparture.setText("You need to have a drink every " + Double.toString(minutesPerDrink) + " minutes");
                    } else {
                        Toast.makeText(MainActivity.this, "INVALID MINUTES", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(MainActivity.this, "INVALID HOUR", Toast.LENGTH_LONG).show();
                }

                long drunkLevelValue = drunkLevel.getSelectedItemId();

                int totalHeightInches = (12 * Integer.parseInt(userInputFeet.getText().toString())) + Integer.parseInt(userInputInches.getText().toString());
                int userWeight = Integer.parseInt(userInputWeight.getText().toString());
                int drinkSizeValue = Integer.parseInt(drinkSize.getText().toString());

                double genderConstant;
                double desiredBAC = BAC[(int) drunkLevelValue];
                double alcoholContent = Double.parseDouble(percentageAlcohol.getText().toString());
                double weightInGrams = userWeight * 453.592;

                if (genderMale.isChecked()) {
                    genderConstant = 0.68;
                } else if (genderFemale.isChecked()) {
                    genderConstant = 0.55;
                } else {
                    Toast.makeText(MainActivity.this, "Please select a gender", Toast.LENGTH_LONG).show();
                    genderConstant = 0;
                }

//                    alcoholInGrams = calculateAlcoholInGrams(drinkSizeValue, alcoholContent);   //drinkSizeValue * (alcoholContent / 100) * 0.789;
                Toast.makeText(MainActivity.this, "Alcohol in Gram is " + alcoholInGrams, Toast.LENGTH_LONG).show();

                double hoursUntilDeparture = minutesUntilDeparture / 60;
                double drinksNeeded = (desiredBAC / 100) * weightInGrams * genderConstant;
//                    alcoholInGrams = 14; //Set to standard drinks, can be changed to cope with drink size but would be unreliable.
                drinksNeeded = round((drinksNeeded / 14) + 0.015 * hoursUntilDeparture);
//                    displayDrinksNeeded.setText(Double.toString(drinksNeeded));
                largeDrinksDisplay.setText(Double.toString(drinksNeeded) + " drinks needed to reach desired level of drunkenness");
                BACGoal.setText(Double.toString(drinksNeeded));


            } else {
                Toast.makeText(MainActivity.this, "Please Fill in the Content", Toast.LENGTH_LONG).show();
            }

        }

        return true;
//        return super.onOptionsItemSelected(item);
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

    public static int getHoursRemaining(int hours, int minutes) {
        int timeRemaining = 0;

        timeRemaining = hours + minutes / 60;

        return timeRemaining;
    }
}
