package com.example.clock.domain;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;

import java.util.Random;
import java.util.concurrent.TimeUnit;

public class InteractorImpl implements Interactor {
    @Override
    public Observable<Boolean> isSubmit(String timeText, Float hourHand, Float minuteHand) {
        return Observable.just(checkAnswer(timeText, hourHand, minuteHand))
                .subscribeOn(Schedulers.io());
    }

    @Override
    public Observable<String> createRandomTask() {
        return Observable.just(randomTask())
                .subscribeOn(Schedulers.io())
                .delay(5, TimeUnit.SECONDS);
    }

    @Override
    public int chooseKindRotation(float x, float y, float previousX, float previousY) {
        int rotation;
        if ((previousX == x) & (previousY == y)) {
            rotation = 0;
        } else {
            if (chooseVector(x, y, previousX, previousY)) {
                rotation = 30;
            } else {
                rotation = -30;
            }
        }
        return rotation;
    }

    private boolean chooseVector(float x, float y, float previousX, float previousY) {
        boolean b;
        if (x >= previousX) {
            if (y >= previousY) {
                b = (y >= 0);
            } else {
                b = (y <= 0);
            }
        } else {
            if (y >= previousY) {
                b = (y <= 0);
            } else {
                b = (y >= 0);
            }
        }
        return b;
    }

    private Boolean checkAnswer(String timeText, Float hourHand, Float minuteHand) {

        int hour = formatHour(hourHand);
        int minute = formatMinute(minuteHand);

        String hourString = String.valueOf(hour);
        String minuteString = formatInt(minute);

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

    private String formatInt(int integer){
        String string = String.valueOf(integer);
        if ((integer / 10) == 0) {
            string = "0" + string;
        }
        return string;
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
        return (randomHour() + ":" + randomMinute());
    }

    private String randomHour(){
        Random random = new Random(System.currentTimeMillis());
        return formatInt(formatRandom(random.nextInt(24)));
    }

    private String randomMinute(){
        Random random = new Random(System.currentTimeMillis());
        return formatInt(formatRandom(random.nextInt(60)));
    }

    private int formatRandom(int random){
        int correctRandom;
        if (random % 5 == 0) {
            correctRandom = random;
        } else {
            correctRandom = random - (random % 5);
        }
        return correctRandom;
    }
}
