package com.jrspencer00.imtoosoberforthissht;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
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

import static com.jrspencer00.imtoosoberforthissht.Utils.calculateAlcoholInGrams;
import static com.jrspencer00.imtoosoberforthissht.Utils.round;

public class MainActivity extends AppCompatActivity {

    NotificationCompat.Builder mBuilder =
            (NotificationCompat.Builder) new NotificationCompat.Builder(this);

    private AlarmManager alarmMgr;
    private PendingIntent pendingIntent;

    private TextView currentBAC;
    private TextView BACGoal;
    private TextView drinksLeft;

    private EditText userInputFeet;
    private EditText userInputInches;
    private EditText userInputWeight;
    private EditText drinkSizeTextbox;
    private EditText percentageAlcoholTextbox;
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

    List<Drinks> drinksArray = new ArrayList<>();

    double alcoholInGrams = 0;
    //    int minutesUntilDeparture;
    int initialMinutesUntilDeparture;
    boolean firstTime = false;
    int initialHour;
    int initialMinute;
    double drinksNeeded;
    double minutesPerDrink;
    int timeRemaining;


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        android.support.v7.widget.Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
        userInputFeet = findViewById(R.id.heightFeet);
        userInputInches = findViewById(R.id.heightInches);
        userInputWeight = findViewById(R.id.weight);
        selectedHour = findViewById(R.id.leavingHour);
        selectedMinute = findViewById(R.id.leavingMinute);
        drinkSizeTextbox = findViewById(R.id.drinkSize);
        percentageAlcoholTextbox = findViewById(R.id.percentAlcohol);

//        calculate = (Button) findViewById(R.id.calculate);
        addDrink = findViewById(R.id.addDrink);
        subtractDrink = findViewById(R.id.subtractDrink);

        drunkLevel = findViewById(R.id.drunkLevel);
        volumeSizes = findViewById(R.id.volumeSizes);

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

                if (drinkSizeTextbox.getText().length() > 0 && percentageAlcoholTextbox.getText().length() > 0) {
                    double drinkSize = (Double.parseDouble(drinkSizeTextbox.getText().toString()));
                    double drinkPercent = Double.parseDouble(percentageAlcoholTextbox.getText().toString());
                    if (volumeSizes.getSelectedItemId() == 1) {
                        drinkSize = drinkSize * Constants.OZ2ML;
                    }

                    drinksArray.add(Drinks.builder()
                            .drinkPercentage(drinkPercent)
                            .drinkSize(drinkSize)
                            .build());

                    alcoholInGrams = alcoholInGrams + calculateAlcoholInGrams(drinksArray.get(drinksArray.size() - 1));
                    double drinksPerDrink = round(alcoholInGrams / 14);
                    currentBAC.setText(Double.toString(drinksPerDrink));
                    double drinksRemaining = drinksNeeded - drinksPerDrink;
                    drinksLeft.setText(Double.toString(round(drinksRemaining)));

                    if (selectedHour.getText().length() > 0 && selectedMinute.getText().length() > 0 &&
                            (timeAM.isChecked() || timePM.isChecked())) {

                        timeRemaining = Utils.timeToDeparture(selectedHour, selectedMinute, timePM, timeAM);
//                    Toast.makeText(MainActivity.this, "Time remaining" + timeRemaining, Toast.LENGTH_LONG).show();
//                    Toast.makeText(MainActivity.this, "Drinks remaining" + drinksRemaining, Toast.LENGTH_LONG).show();
//                    Toast.makeText(MainActivity.this, "Drinks per time unit" + minutesPerDrink, Toast.LENGTH_LONG).show();
//                    Toast.makeText(MainActivity.this, "Val " + (drinksRemaining * minutesPerDrink), Toast.LENGTH_LONG).show();

                        if (timeRemaining <= (drinksRemaining * minutesPerDrink) + Constants.BUFFER && timeRemaining >= (drinksRemaining * minutesPerDrink) - Constants.BUFFER) {
                            Log.d("Testing", "Drinking Rate " + drinksRemaining * minutesPerDrink);
                            Log.d("Testing", "Time Remaining " + timeRemaining);
                            Log.d("Testing", "" + (timeRemaining <= (drinksRemaining * minutesPerDrink) + Constants.BUFFER));
                            Log.d("Testing", "" + (timeRemaining >= (drinksRemaining * minutesPerDrink) - Constants.BUFFER));
                            mBuilder.setSmallIcon(R.mipmap.ic_launcher);
                            mBuilder.setContentTitle("On Pace");
                            mBuilder.setContentText("Keep it up, you are on track");
                        } else if (timeRemaining < drinksRemaining * minutesPerDrink) {
                            Log.d("Testing", "Drinking Rate " + drinksRemaining * minutesPerDrink);
                            Log.d("Testing", "Time Remaining " + timeRemaining);
                            Log.d("Testing", "" + (timeRemaining <= (drinksRemaining * minutesPerDrink) + Constants.BUFFER));
                            Log.d("Testing", "" + (timeRemaining >= (drinksRemaining * minutesPerDrink) - Constants.BUFFER));
                            mBuilder.setSmallIcon(R.mipmap.ic_launcher);
                            mBuilder.setContentTitle("Drink Faster");
                            mBuilder.setContentText("You are falling behind pace");
                        } else {
                            Log.d("Testing", "Drinking Rate " + drinksRemaining * minutesPerDrink);
                            Log.d("Testing", "Time Remaining " + timeRemaining);
                            Log.d("Testing", "" + (timeRemaining <= (drinksRemaining * minutesPerDrink) + Constants.BUFFER));
                            Log.d("Testing", "" + (timeRemaining >= (drinksRemaining * minutesPerDrink) - Constants.BUFFER));
                            mBuilder.setSmallIcon(R.mipmap.ic_launcher);
                            mBuilder.setContentTitle("Drink Slower");
                            mBuilder.setContentText("You are drinking too fast");
                        }
                        // Gets an instance of the NotificationManager service
                        NotificationManager mNotifyMgr =
                                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                        // Builds the notification and issues it.
                        mNotifyMgr.notify(101, mBuilder.build());
                    }

                } else {
                    Toast.makeText(MainActivity.this, "Please fill out drink size then click + to add a drink", Toast.LENGTH_LONG).show();
                }
            }
        });

        subtractDrink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (drinkSizeTextbox.getText().length() > 0 && percentageAlcoholTextbox.getText().length() > 0 && drinksArray.size() > 0) {
                    alcoholInGrams = alcoholInGrams - calculateAlcoholInGrams(drinksArray.get(drinksArray.size() - 1));
                    drinksArray.remove(drinksArray.size() - 1);
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
        if (id == R.id.calculate) {

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
                double desiredBAC = Constants.BAC[(int) drunkLevelValue];
                double weightInGrams = userWeight * Constants.LBS2GRAMS;

                if (genderMale.isChecked()) {
                    genderConstant = Constants.MALEGENDERCONSTANT;
                } else if (genderFemale.isChecked()) {
                    genderConstant = Constants.FEMALEGENDERCONSTANT;
                } else {
                    //Should never enter this case
                    Toast.makeText(MainActivity.this, "Please select a gender", Toast.LENGTH_LONG).show();
                    genderConstant = 0;
                }

//                    alcoholInGrams = calculateAlcoholInGrams(drinkSizeValue, alcoholContent);   //drinkSizeValue * (alcoholContent / 100) * 0.789;
                //Toast.makeText(MainActivity.this, "Alcohol in Gram is " + alcoholInGrams, Toast.LENGTH_LONG).show();

                double hoursUntilDeparture = (double) (initialMinutesUntilDeparture) / 60;
                drinksNeeded = ((desiredBAC + (Constants.ALCOHOLMETABOLIZEDPERHOUR * hoursUntilDeparture)) / 100) * weightInGrams * genderConstant;
//                    alcoholInGrams = 14; //Set to standard drinks, can be changed to cope with drink size but would be unreliable.
                drinksNeeded = round((drinksNeeded / Constants.GRAMSALCOHOLPERDRINK));
//                    displayDrinksNeeded.setText(Double.toString(drinksNeeded));
//                largeDrinksDisplay.setText(Double.toString(drinksNeeded) + " drinks needed to reach desired level of drunkenness");
                BACGoal.setText(Double.toString(drinksNeeded));
                drinksLeft.setText(Double.toString(round(drinksNeeded - round(alcoholInGrams / Constants.GRAMSALCOHOLPERDRINK))));
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
}
