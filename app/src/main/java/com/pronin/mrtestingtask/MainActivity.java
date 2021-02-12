package com.pronin.mrtestingtask;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;
import com.pronin.mrtestingtask.database.Staff;
import com.pronin.mrtestingtask.presenter.MainActivityContract;
import java.util.List;
import javax.inject.Inject;

public class MainActivity extends AppCompatActivity implements MainActivityContract.View, SearchView.OnQueryTextListener {

    private static final String SEARCH_KEY = "search";
    private static final String SORT_KEY = "sort";
    private static final String APP_SETTINGS = "mSettings";
    private static final String FIRST_RUN = "firstRun";
    private SharedPreferences preferences;
    private StaffListAdapter staffListAdapter;

    @Inject
    MainActivityContract.Presenter presenter;

    private final StringBuffer searchText = new StringBuffer();
    private String sortItem;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        preferences = getSharedPreferences(APP_SETTINGS, Context.MODE_PRIVATE);
        if (savedInstanceState != null) {
            searchText.replace(0, searchText.length(),savedInstanceState.getString(SEARCH_KEY));
            sortItem = savedInstanceState.getString(SORT_KEY);
        }
        App.getComponent().injectsMainActivity(this);
        presenter.attachView(this);
        initRecyclerView();
    }

    @Override
    protected void onStart() {
        super.onStart();
        boolean isFirstRun = preferences.getBoolean(FIRST_RUN, true);
        if (isFirstRun)
            presenter.initDataBase();
        else
            initSortSpinner();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(SEARCH_KEY, searchText.toString());
        outState.putString(SORT_KEY, sortItem);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        MenuItem searchItem = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setOnQueryTextListener(this);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add_staff:
                Intent intent = new Intent(this, StaffActivity.class);
                intent.putExtra(StaffActivity.MESSAGE_IS_NEW, true);
                intent.putExtra(StaffActivity.MESSAGE_ID, 0);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onFirstRun() {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(FIRST_RUN, false);
        editor.apply();
        initSortSpinner();
    }

    @Override
    public void showStaff(List<Staff> staff) {
        staffListAdapter.clearItems();
        staffListAdapter.setItems(staff);
    }


    @Override
    public void showError() {
        Toast.makeText(this, getString(R.string.error), Toast.LENGTH_LONG).show();
    }

    private void initRecyclerView() {
        RecyclerView recyclerView = findViewById(R.id.staff_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        staffListAdapter = new StaffListAdapter(new StaffListAdapter.OnStaffClickListener() {
            @Override
            public void onStaffClick(Staff staff) {
                Intent intent = new Intent(MainActivity.this, StaffActivity.class);
                intent.putExtra(StaffActivity.MESSAGE_IS_NEW, false);
                intent.putExtra(StaffActivity.MESSAGE_ID, staff.getId());
                intent.putExtra(StaffActivity.MESSAGE_FIRST_NAME, staff.getFirstName());
                intent.putExtra(StaffActivity.MESSAGE_LAST_NAME, staff.getLastName());
                intent.putExtra(StaffActivity.MESSAGE_POSITION, staff.getPosition());
                intent.putExtra(StaffActivity.MESSAGE_OFFICE, staff.getOffice());
                intent.putExtra(StaffActivity.MESSAGE_CITY, staff.getCity());
                startActivity(intent);
            }
        });
        recyclerView.setAdapter(staffListAdapter);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        searchText.replace(0, searchText.length(), query);
        presenter.loadStaff(searchText.toString(), sortItem);
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        searchText.replace(0, searchText.length(), newText);
        presenter.loadStaff(searchText.toString(), sortItem);
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.onDestroy();
        presenter.detachView();
    }

    private void initSortSpinner() {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.sortItems));
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Spinner spinner = findViewById(R.id.sorting);
        spinner.setAdapter(adapter);
        spinner.setSelection(1);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0 :
                        sortItem = "Staff.first_name";
                        break;
                    case 1 :
                        sortItem = "Staff.last_name";
                        break;
                    case 2 :
                        sortItem = "Staff.position";
                        break;
                    case 3 :
                        sortItem = "Staff.office";
                        break;
                    case 4 :
                        sortItem = "Staff.city";
                        break;
                    default:
                        sortItem = "Staff.id";
                        break;
                }
                presenter.loadStaff(searchText.toString(), sortItem);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
}