package com.jrspencer00.imtoosoberforthissht;

import android.widget.EditText;
import android.widget.RadioButton;

import java.util.Calendar;

public class Utils extends MainActivity {

    public static double round(double Value) {
        double roundedValue;
        roundedValue = Math.round(Value * 100);
        roundedValue /= 100;
        return roundedValue;
    }

    public static double calculateAlcoholInGrams(Drinks drink) {
        return drink.getDrinkSize() * (drink.getDrinkPercentage() / 100) * 0.789;
    }

    public static int timeToDeparture(EditText selectedHour, EditText selectedMinute, RadioButton timePM, RadioButton timeAM) {
        Calendar cal = Calendar.getInstance();
        int currentHour = cal.get(Calendar.HOUR_OF_DAY);
        int currentMinute = cal.get(Calendar.MINUTE);

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
                    hourDifference = hour - currentHour + 24;
                } else {
                    hourDifference = hour - currentMinute;
                }

                int minuteDifference = Integer.parseInt(selectedMinute.getText().toString()) - currentMinute;
                return hourDifference * 60 + minuteDifference;
            } else {
//                TODO Add error mesage
//                Toast.makeText(this, "Invalid Minutes", Toast.LENGTH_SHORT);
            }
        } else {
//            TODO Add error mesage
//            Toast.makeText(this, "Invalid Hours", Toast.LENGTH_SHORT);
        }
        return 0;
    }
}
