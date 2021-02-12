package com.pronin.mrtestingtask.presenter;

import android.util.Log;
import com.pronin.mrtestingtask.App;
import com.pronin.mrtestingtask.database.Staff;
import com.pronin.mrtestingtask.database.StaffDao;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableCompletableObserver;
import io.reactivex.schedulers.Schedulers;

public class StaffActivityPresenter implements StaffActivityContract.Presenter {

    private static final String TAG = StaffActivityPresenter.class.getSimpleName();
    private StaffActivityContract.View view;
    private final StaffDao staffDao;
    private Disposable subscribe;

    public StaffActivityPresenter() {
        staffDao = App.getInstance().getDatabase().getStaffDao();
    }

    @Override
    public void addStaff(Staff staff) {
        subscribe = staffDao.insertStaff(staff)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableCompletableObserver() {
                    @Override
                    public void onComplete() {
                        Log.d(TAG, "Added record: " + staff.toString());
                        view.showAddedStaff();
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Log.e(TAG, "Error on adding record: " + staff.toString());
                        view.showError();
                    }
                });
    }

    @Override
    public void updateStaff(Staff staff) {
        subscribe = staffDao.updateStaff(staff)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableCompletableObserver() {
                    @Override
                    public void onComplete() {
                        Log.d(TAG, "Updated record: " + staff.toString());
                        view.showAddedStaff();
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Log.e(TAG, "Error on updating record: " + staff.toString());
                        view.showError();
                    }
                });
    }


    @Override
    public void attachView(StaffActivityContract.View view) {
        this.view = view;
    }

    @Override
    public void detachView() {
        view = null;
    }

    @Override
    public void onDestroy() {
        if (subscribe != null)
            subscribe.dispose();
    }
}
