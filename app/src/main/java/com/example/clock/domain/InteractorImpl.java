package com.example.clock.domain;

import io.reactivex.Observable;

public class InteractorImpl implements Interactor {
    @Override
    public Observable<Boolean> isSubmit(String timeText, Float hourHand, Float minuteHand) {
        return Observable.just(checkAnswer(timeText, hourHand, minuteHand));
    }

    private Boolean checkAnswer(String timeText, Float hourHand, Float minuteHand) {
        // Hard code
        return true;
    }
}
