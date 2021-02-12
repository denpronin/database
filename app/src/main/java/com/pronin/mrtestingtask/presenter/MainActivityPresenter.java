package com.pronin.mrtestingtask.presenter;

import android.util.Log;
import com.pronin.mrtestingtask.App;
import com.pronin.mrtestingtask.database.Staff;
import com.pronin.mrtestingtask.database.StaffDao;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import io.reactivex.functions.Consumer;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

public class MainActivityPresenter implements MainActivityContract.Presenter{

    private static final String TAG = MainActivityPresenter.class.getSimpleName();
    private static final int DEFAULT_STAFF_COUNT = 20;
    private static final int COEFF = 5;
    private MainActivityContract.View view;
    private final StaffDao staffDao;
    private Disposable subscribe;

    public MainActivityPresenter() {
        staffDao = App.getInstance().getDatabase().getStaffDao();
    }

    @Override
    public void initDataBase() {
        subscribe = Observable.fromCallable(new CallableDefaultStaff())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean aBoolean) {
                        view.onFirstRun();
                    }
                });
    }

    @Override
    public void loadStaff(String search, String sort) {
        subscribe = staffDao.getStaffList(search, sort)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<List<Staff>>() {
                    @Override
                    public void onSuccess(@NonNull List<Staff> staffList) {
                        Log.d(TAG, "Load staff list");
                        view.showStaff(staffList);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Log.e(TAG, "Error on loading staff list");
                        e.printStackTrace();
                        view.showError();
                    }
                });
    }

    @Override
    public void attachView(MainActivityContract.View view) {
        this.view = view;
    }

    @Override
    public void detachView() {
        view = null;
    }

    @Override
    public void onDestroy() {
        subscribe.dispose();
    }

    public boolean getDefaultStaff() {
        List<Staff> list = new ArrayList<>();
        String[] firstNames = {"Иван", "Андрей", "Алексей", "Виктор", "Петр"};
        String[] lastNames = {"Иванов", "Петров", "Сидоров", "Прохоров", "Морозов"};
        String[] positions = {"Начальник отдела", "Специалист", "Ведущий специалист", "Главный специалист", "Замначальника отдела"};
        String[] offices = {"Бухгалтерия", "Финансовый отдел", "Отдел ИТ", "Администрация", "Отдел рекламы"};
        String[] cities = {"Минск", "Гродно", "Витебск", "Гомель", "Брест"};
        for (int i=1; i<=DEFAULT_STAFF_COUNT; i++ ) {
            list.add(new Staff(i, firstNames[(int) (Math.random() * COEFF)],
                    lastNames[(int) (Math.random() * COEFF)], positions[(int) (Math.random() * COEFF)],
                    offices[(int) (Math.random() * COEFF)], cities[(int) (Math.random() * COEFF)]));
        }
        staffDao.insertStaffList(list);
        return true;
    }

    class CallableDefaultStaff implements Callable<Boolean> {
        @Override
        public Boolean call() {
            return getDefaultStaff();
        }
    }
}
