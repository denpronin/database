package com.pronin.mrtestingtask.dagger;

import com.pronin.mrtestingtask.presenter.MainActivityContract;
import com.pronin.mrtestingtask.presenter.MainActivityPresenter;
import com.pronin.mrtestingtask.presenter.StaffActivityContract;
import com.pronin.mrtestingtask.presenter.StaffActivityPresenter;
import javax.inject.Singleton;
import dagger.Module;
import dagger.Provides;

@Module
public class DaggerModules {

    @Provides
    @Singleton
    MainActivityContract.Presenter provideMainActivityPresenter() {
        return new MainActivityPresenter();
    }

    @Provides
    @Singleton
    StaffActivityContract.Presenter provideStaffActivityPresenter() {
        return new StaffActivityPresenter();
    }
}
