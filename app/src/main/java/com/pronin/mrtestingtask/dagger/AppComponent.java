package com.pronin.mrtestingtask.dagger;

import com.pronin.mrtestingtask.MainActivity;
import com.pronin.mrtestingtask.StaffActivity;
import javax.inject.Singleton;
import dagger.Component;

@Singleton
@Component(modules = { DaggerModules.class })
public interface AppComponent {

    void injectsMainActivity(MainActivity mainActivity);
    void injectsMainActivity(StaffActivity staffActivity);

}
