package minh.project.multishop.utils;

import android.annotation.SuppressLint;
import androidx.room.TypeConverter;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateConverter {
    @TypeConverter
    public static Date toDate(Long dateLong){
        return dateLong == null ? null: new Date(dateLong);
    }

    @TypeConverter
    public static Long fromDate(Date date){
        return date == null ? null : date.getTime();
    }

    public static String DateTimeFormat(Date DateTime){
        @SuppressLint("SimpleDateFormat")
        DateFormat dateFormat = new SimpleDateFormat("d/M/y");
        @SuppressLint("SimpleDateFormat")
        DateFormat hourFormat = new SimpleDateFormat("h:mm a");

        String date = dateFormat.format(DateTime.getTime());
        String time = hourFormat.format(DateTime.getTime());
        return time + "\t" + date;
    }

    public static String AppDateFormat(Date DateTime){
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat sdf = new SimpleDateFormat("y-M-d");

        return sdf.format(DateTime.getTime());
    }
}