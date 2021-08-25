package net.coralmc.cmsupport.utils;

import net.coralmc.cmsupport.Main;
import net.coralmc.cmsupport.storage.Partner;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Utils {

    public static String getFormattedDate(Date var1){
        String dateFormat = "EEE, d MMM yyyy HH:mm:ss";


        if (var1 != null) {
            SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);
            return formatter.format(var1);
        }

        return "N/A";
    }

    public static String getReimainingTime(Partner partner){
        long remaining = partner.getResetDate().getTime() - new Date().getTime();
        int remainingTime = (int)(remaining/1000);
        if (remainingTime <= 0){
            Main.plugin.getUserStorage().resetVotesOfPartner(partner);
            partner.setResetDate(getResetDate(new Date()));
            Main.plugin.getPartnerStorage().save(partner, true);
            remaining = partner.getResetDate().getTime() - new Date().getTime();
            remainingTime = (int)(remaining/1000);
        }
        return getFormattedTimer(remainingTime);
    }

    public static String getFormattedTimer(int var4){
        int var5 = var4 % 86400 % 3600 % 60;
        int var6 = var4 % 86400 % 3600 / 60;
        int var7 = var4 % 86400 / 3600;
        int var8 = var4 / 86400;
        boolean var9 = true;
        boolean var10 = true;
        boolean var11 = true;
        boolean var12 = true;
        if (var5 == 1) {
            var9 = false;
        }

        if (var6 == 1) {
            var10 = false;
        }

        if (var7 == 1) {
            var11 = false;
        }

        if (var8 == 1) {
            var12 = false;
        }

        String var13 = var9 ? "§f%s §asegs." : "§f%s §aseg.";
        String var14 = String.format(var13, var5);
        String var15 = var10 ? "§f%s §amins, " : "§f%s §amin, ";
        String var16 = String.format(var15, var6);
        String var17 = var11 ? "§f%s §ahrs, " : "§f%s §ah, ";
        String var18 = String.format(var17, var7);
        String var19 = var12 ? "§f%s §ads, " : "§f%s §ad, ";
        String var20 = String.format(var19, var8);
        if (var8 == 0) {
            var20 = "";
        }

        if (var7 == 0) {
            var18 = "";
        }

        if (var6 == 0) {
            var16 = "";
        }

        String var21 = var20+var18+var16+var14;
        return var21;
    }

    public static Date getResetDate(Date date){
        String resetDate = Main.plugin.getSettingsStorage().getConfig().getString("Settings.ResetVoteTime").toLowerCase().replaceAll(" ", "");
        int years = 0;
        int months = 0;
        int weeks = 0;
        int days = 0;
        int hours = 0;
        int minutes = 0;
        int seconds = 0;
        if (resetDate.contains(",")){
            for (String s : resetDate.split(",")){
                if (s.contains("y")){
                    s = s.replace("y", "");
                    years+=Integer.parseInt(s);
                } else if (s.contains("m")){
                    s = s.replace("m", "");
                    months+=Integer.parseInt(s);
                } else if (s.contains("w")){
                    s = s.replace("w", "");
                    weeks+=Integer.parseInt(s);
                } else if (s.contains("d")){
                    s = s.replace("d", "");
                    days+=Integer.parseInt(s);
                } else if (s.contains("h")){
                    s = s.replace("h", "");
                    hours+=Integer.parseInt(s);
                } else if (s.contains("min")){
                    s = s.replace("min", "");
                    minutes+=Integer.parseInt(s);
                } else if (s.contains("s")){
                    s = s.replace("s", "");
                    seconds+=Integer.parseInt(s);
                }
            }
            return getDate(date, years, months, weeks, days, hours, minutes, seconds);
        }
        return new Date();
    }

    private static Date getDate(Date date, int years, int months, int weeks, int days, int hours, int minutes, int seconds){
        years = date.getYear()+years;
        months = date.getMonth()+months;
        if (months > 12){
            months = months-12;
        }
        weeks = date.getDay()+(weeks*7);
        days = date.getDate()+days;
        hours = date.getHours()+hours;
        minutes = date.getMinutes()+minutes;
        seconds = date.getSeconds()+seconds;

        date.setYear(years);
        date.setMonth(months);
        date.setDate(weeks+days);
        date.setHours(hours);
        date.setMinutes(minutes);
        date.setSeconds(seconds);

        return date;
    }

    private static int getInt(String str){
        str = str.replaceAll("[ymwdhins]", "");
        if (ItemsUtil.isNumeric(str)){
            return Integer.parseInt(str);
        } else {
            return 0;
        }
    }
}
