package com.jrspencer00.imtoosoberforthissht;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private EditText userInputFeet;
    private EditText userInputInches;
    private EditText userInputWeight;
    private TextView outputHeight;
    private TextView displayDrinksNeeded;
    private Button calculate;
    private RadioButton genderMale;
    private RadioButton genderFemale;


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
        outputHeight = (TextView) findViewById(R.id.sum);
        calculate = (Button) findViewById(R.id.calculate);
        genderFemale = (RadioButton) findViewById(R.id.genderFemale);
        genderMale = (RadioButton) findViewById(R.id.genderMale);
        displayDrinksNeeded = (TextView) findViewById(R.id.drinksNeeded);




        calculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (userInputFeet.getText().length() > 0 && userInputInches.getText().length() > 0 && userInputWeight.getText().length() > 0) {
                    double genderConstant;
                    String drunkLevel;

                    double BAC = 0.186;
                    Toast.makeText(MainActivity.this, "BAC is " + BAC, Toast.LENGTH_LONG).show();
                    int totalHeightInches = (12 * Integer.parseInt(userInputFeet.getText().toString())) + Integer.parseInt(userInputInches.getText().toString());
                    outputHeight.setText(Integer.toString(totalHeightInches));
                    int userWeight = Integer.parseInt(userInputWeight.getText().toString());

                    Toast.makeText(MainActivity.this, "weight is " + userWeight, Toast.LENGTH_LONG).show();
                    double weightInGrams = userWeight * 453.592;
                    Toast.makeText(MainActivity.this, "weight in grams is " + weightInGrams, Toast.LENGTH_LONG).show();
                    outputHeight.setText(Double.toString(weightInGrams));

                    if (genderMale.isChecked()) {
                        genderConstant = 0.68;
                    } else {
                        genderConstant = 0.55;
                    }
                    Toast.makeText(MainActivity.this, "gender Constant is " + genderConstant, Toast.LENGTH_LONG).show();

                    double drinksNeeded = (BAC / 100) * weightInGrams * genderConstant;
                    displayDrinksNeeded.setText(Double.toString(drinksNeeded));
                } else {
                    Toast.makeText(MainActivity.this, "Please Filling the Content", Toast.LENGTH_LONG).show();
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
