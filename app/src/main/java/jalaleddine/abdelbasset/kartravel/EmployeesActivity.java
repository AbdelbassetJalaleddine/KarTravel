package jalaleddine.abdelbasset.kartravel;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import dmax.dialog.SpotsDialog;

public class EmployeesActivity extends AppCompatActivity {

    AlertDialog spotsDialog;
    BottomNavigationView navigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employees);
        spotsDialog = new SpotsDialog.Builder().setContext(EmployeesActivity.this).
                setMessage("Loading Information...").build();
        spotsDialog.show();
        BottomNavigation();
    }
    @Override
    protected void onResume() {
        navigationView.setSelectedItemId(R.id.navigation_employees);
        super.onResume();
    }

    private void BottomNavigation() {
        navigationView = findViewById(R.id.bottom_navigation_employee);
        navigationView.setSelectedItemId(R.id.navigation_employees);
        navigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_profile:
                        startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
                        return true;
                    case R.id.navigation_packages:
                        startActivity(new Intent(getApplicationContext(), PackagesActivity.class));
                        return true;
                    case R.id.navigation_pending:
                        startActivity(new Intent(getApplicationContext(), TrackerActivity.class));
                        return true;
                    case R.id.navigation_verify:
                        startActivity(new Intent(getApplicationContext(), VerifyActivity.class));
                        return true;
                    case R.id.navigation_employees:
                        return true;
                }
                return false;
            }
        });
        navigationView.inflateMenu(R.menu.bottom_navigation_admin);
        spotsDialog.dismiss();
    }
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_add) {
            startActivity(new Intent(getApplicationContext(), AddEmployeeActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.adder, menu);
        return true;
    }
}