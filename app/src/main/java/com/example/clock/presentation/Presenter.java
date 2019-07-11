package com.example.clock.presentation;

import androidx.lifecycle.MutableLiveData;
import com.example.clock.domain.Interactor;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;

public class Presenter {

    private InterfaceView view;
    private Interactor interactor;

    private MutableLiveData<Boolean> isSubmit = new MutableLiveData<>();
    private MutableLiveData<String> randomTask = new MutableLiveData<>();

    private String timeText;
    private Float hourHand;
    private Float minuteHand;

    public Presenter(Interactor interactor){
        this.interactor = interactor;
    }

    public void attachView(InterfaceView view) {
        this.view = view;
    }

    public void detachView() {
        this.view = null;
    }

    private MutableLiveData<Boolean> isSubmit() {
        isSubmit = new MutableLiveData<>();
        Observable<Boolean> observable = interactor.isSubmit(timeText, hourHand, minuteHand);
        Observer<Boolean> observer = new Observer<Boolean>() {
            @Override
            public void onSubscribe(Disposable d) {
                view.showProgress();
            }

            @Override
            public void onNext(Boolean success) {
                isSubmit.postValue(success);
            }

            @Override
            public void onError(Throwable e) {
                view.hideProgress();
            }

            @Override
            public void onComplete() {
                view.hideProgress();
            }
        };
        observable
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
        return isSubmit;
    }

    public MutableLiveData<Boolean> isSubmit(String timeText, Float hourHand, Float minuteHand){
        this.timeText = timeText;
        this.hourHand = hourHand;
        this.minuteHand = minuteHand;
        return isSubmit();
    }

    private MutableLiveData<String> randomTask() {
        randomTask = new MutableLiveData<>();
        Observable<String> observable = interactor.createRandomTask();
        Observer<String> observer = new Observer<String>() {
            @Override
            public void onSubscribe(Disposable d) {
                view.showProgress();
            }

            @Override
            public void onNext(String task) {
                randomTask.postValue(task);
            }

            @Override
            public void onError(Throwable e) {
                view.hideProgress();
            }

            @Override
            public void onComplete() {
                view.hideProgress();
            }
        };
        observable
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
        return randomTask;
    }

    public MutableLiveData<String> beginNewTask() {
        return randomTask();
    }

    public int chooseKindRotation(float x, float y, float previousX, float previousY) {
        return interactor.chooseKindRotation(x, y, previousX, previousY);
    }
}
