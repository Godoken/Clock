package com.example.clock.presentation;

import com.example.clock.domain.Interactor;
import com.example.clock.domain.InteractorImpl;

public class PresenterFactory {
    public static Presenter createPresenter(){
        final Interactor interactor = new InteractorImpl();
        return new Presenter(interactor);
    }
}
