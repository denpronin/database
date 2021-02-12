package com.pronin.mrtestingtask;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import com.pronin.mrtestingtask.database.Staff;
import com.pronin.mrtestingtask.presenter.StaffActivityContract;
import java.util.Objects;
import javax.inject.Inject;

public class StaffActivity extends AppCompatActivity implements StaffActivityContract.View {
    public static final String MESSAGE_IS_NEW = "isNew";
    public static final String MESSAGE_ID = "id";
    public static final String MESSAGE_FIRST_NAME = "first_name";
    public static final String MESSAGE_LAST_NAME = "last_name";
    public static final String MESSAGE_POSITION = "position";
    public static final String MESSAGE_OFFICE = "office";
    public static final String MESSAGE_CITY = "city";
    private EditText first_name;
    private EditText last_name;
    private EditText position;
    private EditText office;
    private EditText city;
    private boolean isNew;
    private int idStaff;
    @Inject
    StaffActivityContract.Presenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff);
        App.getComponent().injectsMainActivity(this);
        first_name = findViewById(R.id.first_name);
        last_name = findViewById(R.id.last_name);
        position = findViewById(R.id.position);
        office = findViewById(R.id.office);
        city = findViewById(R.id.city);
        Intent intent = getIntent();
        isNew = intent.getBooleanExtra(MESSAGE_IS_NEW, false);
        if (isNew)
            idStaff = intent.getIntExtra(MESSAGE_ID, 0);
        else {
            idStaff = intent.getIntExtra(MESSAGE_ID, 0);
            first_name.setText(intent.getStringExtra(MESSAGE_FIRST_NAME));
            last_name.setText(intent.getStringExtra(MESSAGE_LAST_NAME));
            position.setText(intent.getStringExtra(MESSAGE_POSITION));
            office.setText(intent.getStringExtra(MESSAGE_OFFICE));
            city.setText(intent.getStringExtra(MESSAGE_CITY));
        }
        if (savedInstanceState != null) {
            isNew = savedInstanceState.getBoolean(MESSAGE_IS_NEW);
            idStaff = savedInstanceState.getInt(MESSAGE_ID);
            first_name.setText(savedInstanceState.getString(MESSAGE_FIRST_NAME));
            last_name.setText(savedInstanceState.getString(MESSAGE_LAST_NAME));
            position.setText(savedInstanceState.getString(MESSAGE_POSITION));
            office.setText(savedInstanceState.getString(MESSAGE_OFFICE));
            city.setText(savedInstanceState.getString(MESSAGE_CITY));
        }
        presenter.attachView(this);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(MESSAGE_IS_NEW, isNew);
        outState.putInt(MESSAGE_ID, idStaff);
        outState.putString(MESSAGE_FIRST_NAME, first_name.getText().toString());
        outState.putString(MESSAGE_LAST_NAME, last_name.getText().toString());
        outState.putString(MESSAGE_POSITION, position.getText().toString());
        outState.putString(MESSAGE_OFFICE, office.getText().toString());
        outState.putString(MESSAGE_CITY, city.getText().toString());
    }

    public void onClickDone(View view) {
        Staff staff = new Staff(idStaff, first_name.getText().toString(), last_name.getText().toString(),
                position.getText().toString(), office.getText().toString(), city.getText().toString());
        if (isNew)
            presenter.addStaff(staff);
        else
            presenter.updateStaff(staff);
    }

    @Override
    public void showAddedStaff() {
        Toast.makeText(this, getString(R.string.record_added), Toast.LENGTH_SHORT).show();
        finish();
    }

    @Override
    public void showError() {
        Toast.makeText(this, getString(R.string.record_added_error), Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.onDestroy();
        presenter.detachView();
    }
}