package com.example.clock.domain;

import io.reactivex.Observable;

public interface Interactor {
    Observable<Boolean> isSubmit(String timeText, Float hourHand, Float minuteHand);

    Observable<String> createRandomTask();
}
