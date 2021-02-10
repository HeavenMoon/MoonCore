package fr.heavenmoon.core.common.utils.time;

import java.text.SimpleDateFormat;
import java.util.Date;

public class CustomDate {

    private Date currentDate;

    public CustomDate() {
        this.currentDate = new Date(System.currentTimeMillis());
    }

    public CustomDate(long millis) {
        this.currentDate = new Date(millis);
    }

    public long between(long time) {
        return Math.abs(this.currentDate.getTime() - time);
    }

    public CustomDate addTime(DateUnity dateUnity, Double unityValue) {
        this.currentDate = new Date((long) (currentDate.getTime() + unityValue
                * dateUnity.getMillis()));
        return this;
    }

    public CustomDate removeTime(DateUnity dateUnity, Double unityValue) {
        this.currentDate = new Date((long) (currentDate.getTime() - unityValue
                * dateUnity.getMillis()));
        return this;
    }

    public String getYear() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
                "yyyy");
        return simpleDateFormat.format(this.currentDate);
    }

    public String getMonth() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
                "MM");
        return simpleDateFormat.format(this.currentDate);
    }

    public String getWrittenMonth() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
                "MMM");
        return simpleDateFormat.format(this.currentDate);
    }

    public String getDay() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
                "dd");
        return simpleDateFormat.format(this.currentDate);
    }

    public String getWrittenDay() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
                "EEE");
        return simpleDateFormat.format(this.currentDate);
    }

    public String getHours() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
                "HH");
        return simpleDateFormat.format(this.currentDate);
    }

    public String getMinutes() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
                "mm");
        return simpleDateFormat.format(this.currentDate);
    }

    public String getSeconds() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
                "ss");
        return simpleDateFormat.format(this.currentDate);
    }

    public String getCompleteFormat() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.YYYY à HH:mm:ss");
        return simpleDateFormat.format(this.currentDate);
    }

    public String getDurationUntil(long until) {
        long time = between(until);
        Date date = new Date(time);

        SimpleDateFormat years = new SimpleDateFormat("yyyy");
        SimpleDateFormat months = new SimpleDateFormat("MM");
        SimpleDateFormat days = new SimpleDateFormat("dd");
        SimpleDateFormat hours = new SimpleDateFormat("HH");
        SimpleDateFormat minutes = new SimpleDateFormat("mm");
        SimpleDateFormat seconds = new SimpleDateFormat("ss");

        StringBuilder string = new StringBuilder();
        if (Integer.valueOf(years.format(date)) > 1970)
            string.append((Integer.valueOf(years.format(date)) - 1970) + " année(s), ");
        if (Integer.valueOf(months.format(date)) > 01) string.append(months.format(date) + " mois, ");
        if (Integer.valueOf(days.format(date)) > 01) string.append(days.format(date) + " jour(s), ");
        if (Integer.valueOf(hours.format(date)) > 01) string.append(hours.format(date) + " heure(s), ");
        if (Integer.valueOf(minutes.format(date)) > 0) string.append(minutes.format(date) + " minute(s), ");
        if (Integer.valueOf(seconds.format(date)) > 0) string.append(seconds.format(date) + " seconde(s), ");

        int length = string.toString().length() - 2;
        return string.toString().substring(0, length);
    }

    public String getCleanFormat(long time) {
        String str = "";
        str = String.format("%02d J, %02d h, %02d min, %02d s", time / 86400, time / 3600, (time % 3600) / 60, time % 60);
        if (time / 86400 < 1) {
            str = String.format("%02d h, %02d min, %02d s", time / 3600, (time % 3600) / 60, time % 60);
        }
        if (time / 3600 < 1) {
            String.format("%02d minute(s), %02d seconde(s)", (time % 3600) / 60, time % 60);
        }
        if ((time % 3600) / 60 < 1) {
            String.format("%02d seconde(s)", time % 60);
        }
        return str;
    }

    public String getFormat(String format) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        return simpleDateFormat.format(this.currentDate);
    }

    public long getMillis() {
        return this.currentDate.getTime();
    }

    public Date getCurrentDate() {
        return currentDate;
    }

    public CustomDate setCurrentDate(Date currentDate) {
        this.currentDate = currentDate;
        return this;
    }

}
