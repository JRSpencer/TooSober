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

    private EditText userInputFeet;
    private EditText userInputInches;
    private EditText userInputWeight;
    private TextView outputHeight;
    private TextView displayDrinksNeeded;
    private Button calculate;
    private RadioButton genderMale;
    private RadioButton genderFemale;
    private Spinner drunkLevel;
    private EditText drinkSize;
    private EditText percentageAlcohol;
    private TextView largeDrinksDisplay;
    private EditText selectedHour;
    private EditText selectedMinute;
    private TextView timeUntilDeparture;

    private RadioButton timeAM;
    private RadioButton timePM;

    double[] BAC = {0.03,
            0.06,
            0.10,
            0.15,
            0.20,
            0.25,
            0.3};


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
//        outputHeight = (TextView) findViewById(R.id.sum);
        calculate = (Button) findViewById(R.id.calculate);
        genderMale = (RadioButton) findViewById(R.id.genderMale);
        genderFemale = (RadioButton) findViewById(R.id.genderFemale);
//        displayDrinksNeeded = (TextView) findViewById(R.id.drinksNeeded);
        drunkLevel = (Spinner) findViewById(R.id.drunkLevel);
        percentageAlcohol = (EditText) findViewById(R.id.percentAlcohol);
        drinkSize = (EditText) findViewById(R.id.drinkSize);
        largeDrinksDisplay = (TextView) findViewById(R.id.drinksDisplay);
        selectedHour = (EditText) findViewById(R.id.leavingHour);
        selectedMinute = (EditText) findViewById(R.id.leavingMinute);
        timeUntilDeparture = (TextView) findViewById(R.id.timeRemaining);
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
//                    Toast.makeText(MainActivity.this, "weight in grams is " + weightInGrams, Toast.LENGTH_LONG).show();
//                    outputHeight.setText(Double.toString(weightInGrams));

                    if (genderMale.isChecked()) {
                        genderConstant = 0.68;
                    } else if (genderFemale.isChecked()) {
                        genderConstant = 0.55;
                    } else{
                        Toast.makeText(MainActivity.this, "Please select a gender", Toast.LENGTH_LONG).show();
                        genderConstant = 0;
                    }
//                    Toast.makeText(MainActivity.this, "gender Constant is " + genderConstant, Toast.LENGTH_LONG).show();
//                    double mlConversion = 29.5735;
                    int drinkSizeValue = Integer.parseInt(drinkSize.getText().toString());
                    double alcoholContent = Double.parseDouble(percentageAlcohol.getText().toString());
                    double drinksNeeded = (desiredBAC / 100) * weightInGrams * genderConstant;
                    double alcoholInGrams = drinkSizeValue * (alcoholContent / 100) * 0.789;
                    Toast.makeText(MainActivity.this, "Alcohol in Gram is " + alcoholInGrams, Toast.LENGTH_LONG).show();
                    drinksNeeded = Math.round(drinksNeeded * 100 / alcoholInGrams);
                    drinksNeeded /= 100;
//                    displayDrinksNeeded.setText(Double.toString(drinksNeeded));
                    largeDrinksDisplay.setText(Double.toString(drinksNeeded) + " drinks needed to reach desired level of drunkenness");
                    int hour = Integer.parseInt(selectedHour.getText().toString());
                    if(Integer.parseInt(selectedHour.getText().toString()) > 0 &&
                            Integer.parseInt(selectedHour.getText().toString()) < 13 ){
                        if(Integer.parseInt(selectedMinute.getText().toString()) >= 0 &&
                                Integer.parseInt(selectedMinute.getText().toString()) < 60 ){
                            Calendar cal = Calendar.getInstance();
                            if(timePM.isChecked()){
                                hour += 12;
                            }
                            int hourDifference;
                            if(hour < cal.get(Calendar.HOUR_OF_DAY) && timeAM.isChecked()){
                                hourDifference = hour - cal.get(Calendar.HOUR_OF_DAY) + 24;
                            }else if (hour < cal.get(Calendar.HOUR_OF_DAY) && timePM.isChecked()){
                                hourDifference = hour  - cal.get(Calendar.HOUR_OF_DAY);
                            }else{
                                hourDifference = hour  - cal.get(Calendar.HOUR_OF_DAY);
                            }

                            int minuteDifference = Integer.parseInt(selectedMinute.getText().toString()) - cal.get(Calendar.MINUTE);
                            timeUntilDeparture.setText(Integer.toString(hourDifference * 60 + minuteDifference) + " Minutes until you want to leave.");
                        }else{
                            Toast.makeText(MainActivity.this, "INVALID MINUTES", Toast.LENGTH_LONG).show();
                        }
                    }else{
                        Toast.makeText(MainActivity.this, "INVALID HOUR", Toast.LENGTH_LONG).show();
                    }

                } else {
                    Toast.makeText(MainActivity.this, "Please Fill in the Content", Toast.LENGTH_LONG).show();
                }
            }
        });


//        int totalHeightInches = (12 * Integer.parseInt(userInputFeet.getText().toString())) + Integer.parseInt(userInputInches.getText().toString());
//        outputHeight.setText(Integer.toString(totalHeightInches));
//        int userWeight = Integer.parseInt(userInputWeight.getText().toString());
//        Toast.makeText(MainActivity.this, "weight is " + userWeight, Toast.LENGTH_LONG).show();

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
}
