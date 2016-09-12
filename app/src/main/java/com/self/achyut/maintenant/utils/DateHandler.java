package com.self.achyut.maintenant.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by boonkui on 11-09-2016.
 */
public class DateHandler {

    public static String dateToString(Date date){
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        String dateStr = sdf.format(date);
        return dateStr;
    }

    public static Date stringToDate(String datestring){
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        Date d = null;
        try {
            d = dateFormat.parse(datestring);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return d;
    }
}
