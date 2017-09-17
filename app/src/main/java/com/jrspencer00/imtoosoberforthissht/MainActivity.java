package com.jrspencer00.imtoosoberforthissht;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.NotificationCompat;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    NotificationCompat.Builder mBuilder =
            (NotificationCompat.Builder) new NotificationCompat.Builder(this);

    private AlarmManager alarmMgr;
    private PendingIntent pendingIntent;

    private TextView outputHeight;
    private TextView displayDrinksNeeded;
    private TextView largeDrinksDisplay;
    private TextView timeUntilDeparture;
    private TextView currentBAC;
    private TextView BACGoal;
    private TextView drinksLeft;

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
    private Spinner volumeSizes;

    double[] BAC = {0.03,
            0.06,
            0.10,
            0.15,
            0.20,
            0.25,
            0.3};

    List<Double> drinkSizeArray = new ArrayList<Double>();
    List<Double> drinkPercentArray = new ArrayList<Double>();

    double alcoholInGrams = 0;
    int minutesUntilDeparture;
    int initialMinutesUntilDeparture;
    boolean firstTime = false;
    int initialHour;
    int initialMinute;
    double drinksNeeded;
    double minutesPerDrink;
    int timeRemaining;


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
        subtractDrink = (Button) findViewById(R.id.subtractDrink);

        drunkLevel = (Spinner) findViewById(R.id.drunkLevel);
        volumeSizes = (Spinner) findViewById(R.id.volumeSizes);

//        largeDrinksDisplay = (TextView) findViewById(R.id.drinksDisplay);
//        timeUntilDeparture = (TextView) findViewById(R.id.timeRemaining);
        currentBAC = (TextView) findViewById(R.id.currentBAC);
        BACGoal = (TextView) findViewById(R.id.desiredBAC);
        drinksLeft = (TextView) findViewById(R.id.drinksLeft);

        genderMale = (RadioButton) findViewById(R.id.genderMale);
        genderFemale = (RadioButton) findViewById(R.id.genderFemale);
        timeAM = (RadioButton) findViewById(R.id.amTime);
        timePM = (RadioButton) findViewById(R.id.pmTime);


        addDrink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (drinkSize.getText().length() > 0 && percentageAlcohol.getText().length() > 0) {
                    if (volumeSizes.getSelectedItemId() == 1) {
                        double oz2ml = 29.5735;
                        double ozDrinkSize = (Double.parseDouble(drinkSize.getText().toString()) * oz2ml);
                        double ozDrinkPercent = Double.parseDouble(percentageAlcohol.getText().toString());

                        drinkSizeArray.add(ozDrinkSize);
                        drinkPercentArray.add(ozDrinkPercent);
                    } else {
                        drinkSizeArray.add(Double.parseDouble(drinkSize.getText().toString()));
                        drinkPercentArray.add(Double.parseDouble(percentageAlcohol.getText().toString()));
                    }
                    //int drinkSizeValue = Integer.parseInt(drinkSize.getText().toString());
                    //double alcoholContent = Double.parseDouble(percentageAlcohol.getText().toString());
                    //alcoholInGrams = calculateAlcoholInGrams(drinkSizeValue, alcoholContent);

//                    alcoholInGrams = 0;
//                    for (int i = 0; i < drinkPercentArray.size(); i++) {
                    alcoholInGrams = alcoholInGrams + calculateAlcoholInGrams(drinkSizeArray.get(drinkSizeArray.size() - 1),
                            drinkPercentArray.get(drinkPercentArray.size() - 1));
                    double drinksPerDrink = round(alcoholInGrams / 14);
                    currentBAC.setText(Double.toString(drinksPerDrink));
                    drinksLeft.setText(Double.toString(round(drinksNeeded - drinksPerDrink)));
//                    }
                    Calendar cal = Calendar.getInstance();
                    int currentHour = cal.get(Calendar.HOUR_OF_DAY);
                    int currentMinute = cal.get(Calendar.MINUTE);
                    timeRemaining = timeToDeparture(currentHour, currentMinute);
//                    Toast.makeText(MainActivity.this, "Time remaining" + timeRemaining, Toast.LENGTH_LONG).show();
                    double  drinksRemaining = Double.parseDouble(drinksLeft.getText().toString());
//                    Toast.makeText(MainActivity.this, "Drinks remaining" + drinksRemaining, Toast.LENGTH_LONG).show();
//                    Toast.makeText(MainActivity.this, "Drinks per time unit" + minutesPerDrink, Toast.LENGTH_LONG).show();
//                    Toast.makeText(MainActivity.this, "Val " + (drinksRemaining * minutesPerDrink), Toast.LENGTH_LONG).show();
                    if(timeRemaining <= (drinksRemaining * minutesPerDrink) - 5 || timeRemaining >= (drinksRemaining * minutesPerDrink) + 5){
                        mBuilder.setSmallIcon(R.mipmap.ic_launcher);
                        mBuilder.setContentTitle("On Pace");
                        mBuilder.setContentText("Keep it up, you are on track");
                    }
                    else if(timeRemaining < drinksRemaining * minutesPerDrink){
                        mBuilder.setSmallIcon(R.mipmap.ic_launcher);
                        mBuilder.setContentTitle("Drink Faster");
                        mBuilder.setContentText("You are falling behind pace");
                    }
                    else{
                        mBuilder.setSmallIcon(R.mipmap.ic_launcher);
                        mBuilder.setContentTitle("Drink Slower");
                        mBuilder.setContentText("You are drinking too fast");
                    }
                    // Gets an instance of the NotificationManager service
                    NotificationManager mNotifyMgr =
                            (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
// Builds the notification and issues it.
                    mNotifyMgr.notify(101, mBuilder.build());

                } else {
                    Toast.makeText(MainActivity.this, "Please fill out drink size then click + to add a drink", Toast.LENGTH_LONG).show();
                }
            }
        });

        subtractDrink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (drinkSize.getText().length() > 0 && percentageAlcohol.getText().length() > 0 && drinkSizeArray.size() > 0) {
                    alcoholInGrams = alcoholInGrams - calculateAlcoholInGrams(drinkSizeArray.get(drinkSizeArray.size() - 1),
                            drinkPercentArray.get(drinkPercentArray.size() - 1));
                    drinkSizeArray.remove(drinkSizeArray.size() - 1);
                    drinkPercentArray.remove(drinkPercentArray.size() - 1);
                    double drinksPerDrink = round(alcoholInGrams / 14);
                    currentBAC.setText(Double.toString(drinksPerDrink));
                    drinksLeft.setText(Double.toString(round(drinksNeeded - drinksPerDrink)));

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
    protected void onSaveInstanceState(Bundle outState) {

        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {

        super.onRestoreInstanceState(savedInstanceState);
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

                Calendar cal = Calendar.getInstance();

                if (firstTime == false) {
                    initialHour = cal.get(Calendar.HOUR_OF_DAY);
                    initialMinute = cal.get(Calendar.MINUTE);
                    firstTime = true;
                }

                int hour = Integer.parseInt(selectedHour.getText().toString());
                if (Integer.parseInt(selectedHour.getText().toString()) > 0 &&
                        Integer.parseInt(selectedHour.getText().toString()) < 13) {
                    if (Integer.parseInt(selectedMinute.getText().toString()) >= 0 &&
                            Integer.parseInt(selectedMinute.getText().toString()) < 60) {

                        if (timePM.isChecked() && hour < 12) {
                            hour += 12;
                        } else if (timeAM.isChecked() && hour == 12) {
                            hour -= 12;
                        }
                        int hourDifference;
                        if (hour < cal.get(Calendar.HOUR_OF_DAY)) {
                            hourDifference = hour - initialHour + 24;
                        } else {
                            hourDifference = hour - initialHour;
                        }

                        int minuteDifference = Integer.parseInt(selectedMinute.getText().toString()) - initialMinute;
                        initialMinutesUntilDeparture = hourDifference * 60 + minuteDifference;
//                            System.out.println(minutesUntilDeparture);
//                        timeUntilDeparture.setText(Integer.toString(minutesUntilDeparture) + " Minutes until you want to leave.");
                        //double minutesPerDrink = round(minutesUntilDeparture / drinksNeeded);
                        //timeUntilDeparture.setText("You need to have a drink every " + Double.toString(minutesPerDrink) + " minutes");
                    } else {
                        Toast.makeText(MainActivity.this, "INVALID MINUTES", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(MainActivity.this, "INVALID HOUR", Toast.LENGTH_LONG).show();
                }


                long drunkLevelValue = drunkLevel.getSelectedItemId();

                //int totalHeightInches = (12 * Integer.parseInt(userInputFeet.getText().toString())) + Integer.parseInt(userInputInches.getText().toString());
                int userWeight = Integer.parseInt(userInputWeight.getText().toString());

                double genderConstant;
                double desiredBAC = BAC[(int) drunkLevelValue];
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
                //Toast.makeText(MainActivity.this, "Alcohol in Gram is " + alcoholInGrams, Toast.LENGTH_LONG).show();

                double hoursUntilDeparture = (double) (initialMinutesUntilDeparture) / 60;
                drinksNeeded = ((desiredBAC + (0.015 * hoursUntilDeparture)) / 100) * weightInGrams * genderConstant;
//                    alcoholInGrams = 14; //Set to standard drinks, can be changed to cope with drink size but would be unreliable.
                drinksNeeded = round((drinksNeeded / 14));
//                    displayDrinksNeeded.setText(Double.toString(drinksNeeded));
//                largeDrinksDisplay.setText(Double.toString(drinksNeeded) + " drinks needed to reach desired level of drunkenness");
                BACGoal.setText(Double.toString(drinksNeeded));
                drinksLeft.setText(Double.toString(round(drinksNeeded - round(alcoholInGrams / 14))));
                minutesPerDrink = initialMinutesUntilDeparture / drinksNeeded;

//                if(minutesUntilDeparture % drinksPerTimeUnit == 0){
//
//                }
                //
                //If time until departure % average time per drink == 0
                //set notification

                //Set notification method should see if you are ahead, behind or on pace and notify you appropriately


            } else {
                Toast.makeText(MainActivity.this, "Please Fill in the Content", Toast.LENGTH_LONG).show();
            }

        }
        if (id == R.id.reset) {
            firstTime = false;
        }

        return true;
//        return super.onOptionsItemSelected(item);
    }

    public static double calculateAlcoholInGrams(double drinkSize, double percentAlcohol) {
        double alcoholInGrams = drinkSize * (percentAlcohol / 100) * 0.789;
        return alcoholInGrams;
    }

    public static double round(double Value) {
        double roundedValue;
        roundedValue = Math.round(Value * 100);
        roundedValue /= 100;
        return roundedValue;
    }

    public int timeToDeparture(int givenHour, int givenMinute) {
        Calendar cal = Calendar.getInstance();

        int hour = Integer.parseInt(selectedHour.getText().toString());
        if (Integer.parseInt(selectedHour.getText().toString()) > 0 &&
                Integer.parseInt(selectedHour.getText().toString()) < 13) {
            if (Integer.parseInt(selectedMinute.getText().toString()) >= 0 &&
                    Integer.parseInt(selectedMinute.getText().toString()) < 60) {

                if (timePM.isChecked() && hour < 12) {
                    hour += 12;
                } else if (timeAM.isChecked() && hour == 12) {
                    hour -= 12;
                }
                int hourDifference;
                if (hour < cal.get(Calendar.HOUR_OF_DAY)) {
                    hourDifference = hour - givenHour + 24;
                } else {
                    hourDifference = hour - givenHour;
                }

                int minuteDifference = Integer.parseInt(selectedMinute.getText().toString()) - givenMinute;
                minutesUntilDeparture = hourDifference * 60 + minuteDifference;
            }
        }
        return minutesUntilDeparture;
    }
}
