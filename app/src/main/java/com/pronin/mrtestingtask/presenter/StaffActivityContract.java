package com.pronin.mrtestingtask.presenter;

import com.pronin.mrtestingtask.database.Staff;

public interface StaffActivityContract {
    interface View {
        void showAddedStaff();

        void showError();
    }

    interface Presenter {
        void addStaff(Staff staff);

        void updateStaff(Staff staff);

        void attachView(StaffActivityContract.View view);

        void detachView();

        void onDestroy();
    }
}
