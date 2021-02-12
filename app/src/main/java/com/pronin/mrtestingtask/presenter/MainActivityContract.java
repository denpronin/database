package com.pronin.mrtestingtask.presenter;

import com.pronin.mrtestingtask.database.Staff;
import java.util.List;

public interface MainActivityContract {

    interface View {
        void onFirstRun();
        void showStaff(List<Staff> staffList);
        void showError();
    }

    interface Presenter {
        void initDataBase();
        void loadStaff(String search, String sort);
        void attachView(MainActivityContract.View view);
        void detachView();
        void onDestroy();
    }
}
