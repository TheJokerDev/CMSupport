package net.coralmc.cmsupport.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Utils {

    public static String getFormattedDate(Date var1){
        String dateFormat = "EEE, d MMM yyyy HH:mm:ss";
        if (var1 != null) {
            try {
                Date date = new SimpleDateFormat(dateFormat).parse(var1.toString());
                return date.toString();
            } catch (ParseException ignored) {
            }
        }
        return "N/A";
    }
}
