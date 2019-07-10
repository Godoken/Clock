package com.example.clock.domain;

import io.reactivex.Observable;

import java.util.concurrent.TimeUnit;

public class InteractorImpl implements Interactor {
    @Override
    public Observable<Boolean> isSubmit(String timeText, Float hourHand, Float minuteHand) {
        return Observable.just(checkAnswer(timeText, hourHand, minuteHand));
    }

    @Override
    public Observable<String> createRandomTask() {
        return Observable.just(randomTask()).delay(5, TimeUnit.SECONDS);
    }

    private Boolean checkAnswer(String timeText, Float hourHand, Float minuteHand) {

        int hour = formatHour(hourHand);
        int minute = formatMinute(minuteHand);

        String hourString = String.valueOf(hour);
        String minuteString = formatMinuteInt(minute);

        return isCorrect(hourString, minuteString, timeText);
    }

    private int formatHour(Float hourFloat){
        int hour = hourFloat.intValue()/30;
        if (hour >= 12) {
            hour = hour % 12;
        }
        return hour;
    }

    private int formatMinute(Float minuteFloat){
        int minute = minuteFloat.intValue()/30*5;
        if (minute >= 60) {
            minute = minute % 60;
        }
        return minute;
    }

    private String formatMinuteInt(int minute){
        String minuteString = String.valueOf(minute);
        if ((minute / 10) == 0) {
            minuteString = "0" + minuteString;
        }
        return minuteString;
    }

    private boolean isCorrect(String hour, String minute, String timeText){
        String[] strings = timeText.split(":");
        return (isCorrectHour(hour, strings[0]) & isCorrectMinute(minute, strings[1]));
    }

    private boolean isCorrectHour(String hour, String timeHour){
        boolean b;
        switch (hour) {
            case "0":
                b = (timeHour.equals("00")) | (timeHour.equals("12"));
                break;

            case "1":
                b = (timeHour.equals("01")) | (timeHour.equals("13"));
                break;

            case "2":
                b = (timeHour.equals("02")) | (timeHour.equals("14"));
                break;

            case "3":
                b = (timeHour.equals("03")) | (timeHour.equals("15"));
                break;

            case "4":
                b = (timeHour.equals("04")) | (timeHour.equals("16"));
                break;

            case "5":
                b = (timeHour.equals("05")) | (timeHour.equals("17"));
                break;

            case "6":
                b = (timeHour.equals("06")) | (timeHour.equals("18"));
                break;

            case "7":
                b = (timeHour.equals("07")) | (timeHour.equals("19"));
                break;

            case "8":
                b = (timeHour.equals("08")) | (timeHour.equals("20"));
                break;

            case "9":
                b = (timeHour.equals("09")) | (timeHour.equals("21"));
                break;

            case "10":
                b = (timeHour.equals("10")) | (timeHour.equals("22"));
                break;

            case "11":
                b = (timeHour.equals("11")) | (timeHour.equals("23"));
                break;

            default:
                b = false;
        }
        return b;
    }

    private boolean isCorrectMinute(String minute, String timeMinute){
        return minute.equals(timeMinute);
    }

    private String randomTask() {
        //Hard code
        return "21:30";
    }
}
