package com.example.clock.presentation;

import androidx.lifecycle.MutableLiveData;
import com.example.clock.domain.Interactor;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;

public class Presenter {

    protected InterfaceView view;

    private Interactor interactor;

    private MutableLiveData<Boolean> isSubmit = new MutableLiveData<>();

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

    private void isSubmit() {
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
    }

    public MutableLiveData<Boolean> isSubmit(String timeText, Float hourHand, Float minuteHand){
        this.timeText = timeText;
        this.hourHand = hourHand;
        this.minuteHand = minuteHand;
        isSubmit();
        return isSubmit;
    }
}
