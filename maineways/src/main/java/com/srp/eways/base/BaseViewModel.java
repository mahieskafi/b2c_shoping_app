package com.srp.eways.base;

import android.view.View;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.srp.eways.util.rx.SchedulerProvider;

import io.reactivex.Scheduler;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Arcane on 7/16/2019.
 */
public class BaseViewModel extends ViewModel {

    private SchedulerProvider mSchedulerProvider;
    private CompositeDisposable mCompositeDisposable;

    private MutableLiveData<Boolean> mIsLoading = new MutableLiveData<>();
    private MutableLiveData<Boolean> mPermissionAccessed = new MutableLiveData<>();
    private MutableLiveData<Boolean> mIsNeededToLogin = new MutableLiveData<>();


    public BaseViewModel(SchedulerProvider schedulerProvider) {

        mSchedulerProvider = schedulerProvider;
        mCompositeDisposable = new CompositeDisposable();
    }

    public BaseViewModel() {
        mSchedulerProvider = new SchedulerProvider() {
            @Override
            public Scheduler computation() {
                return Schedulers.computation();
            }

            @Override
            public Scheduler io() {
                return Schedulers.io();
            }

            @Override
            public Scheduler ui() {
                return Schedulers.single();
            }
        };

        mCompositeDisposable = new CompositeDisposable();
    }

    public LifecycleOwner getLifecycleOwner(View view) {

        //TODO should send lifecycleowner to adapter
        if (view.getTag() instanceof LifecycleOwner) {

            return (LifecycleOwner) (view.getTag());
        } else {

            return getLifecycleOwner((View) view.getParent());
        }
    }

    public SchedulerProvider getSchedulerProvider() {

        return mSchedulerProvider;
    }

    public CompositeDisposable getCompositeDisposable() {

        return mCompositeDisposable;
    }

    @Override
    protected void onCleared() {

        mCompositeDisposable.dispose();
        super.onCleared();
    }

    public MutableLiveData<Boolean> isLoading() {
        return mIsLoading;
    }

    public void setLoading(Boolean isLoading) {
        mIsLoading.setValue(isLoading);
    }

    public void setLoadingConsumed() {
        mIsLoading.setValue(null);
    }

    public MutableLiveData<Boolean> isPermissionAccessed() {
        return mPermissionAccessed;
    }

    public void setPermissionAccessed(Boolean permissionAccessed) {
        mPermissionAccessed.setValue(permissionAccessed);
    }

    public void setPermissionAccessedConsumed() {
        mPermissionAccessed.setValue(null);
    }

    public MutableLiveData<Boolean> isNeededToLogin() {
        return mIsNeededToLogin;
    }

    public void consumeNeededToLogin() {
        mIsNeededToLogin.setValue(null);
    }

    public void setIsNeededToLogin(Boolean isNeededToLogin) {
        mIsNeededToLogin.setValue(isNeededToLogin);
    }

    public void makeInstanceNull() {
    }
}
