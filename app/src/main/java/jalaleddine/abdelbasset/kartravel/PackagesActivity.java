package jalaleddine.abdelbasset.kartravel;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import dmax.dialog.SpotsDialog;

public class PackagesActivity extends AppCompatActivity {

    AlertDialog spotsDialog;
    BottomNavigationView navigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_packages);
        spotsDialog = new SpotsDialog.Builder().setContext(PackagesActivity.this).
                setMessage("Loading Information...").build();
        spotsDialog.show();
        BottomNavigation();
        GetInfo();
    }
    private void GetInfo() {
        SharedPreferences editor = getSharedPreferences("UsersData", MODE_PRIVATE);
        String number = editor.getString("phonenumber", "1");
        String name = editor.getString("name", "Aboud");
        String gender = editor.getString("gender", "male");

        if(number.contains("71657756")){
            //adder
            //TODO: Fix it for Employees

            navigationView.inflateMenu(R.menu.bottom_navigation_employee);
        }
        else if (number.contains("61657756")){
            //TODO: Fix for User

            navigationView.inflateMenu(R.menu.bottom_navigation_user);
        }
        else{
            //TODO: Fix for Admin
            navigationView.inflateMenu(R.menu.bottom_navigation_admin);
        }
        spotsDialog.dismiss();
    }

    private void BottomNavigation() {
        navigationView = findViewById(R.id.bottom_navigation_employee);
        navigationView.setSelectedItemId(R.id.navigation_packages);
        navigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_profile:
                        startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
                        return true;
                    case R.id.navigation_packages:
                        return true;
                    case R.id.navigation_track:
                        startActivity(new Intent(getApplicationContext(), TrackerActivity.class));
                        return true;
                    case R.id.navigation_verify:
                        startActivity(new Intent(getApplicationContext(), VerifyActivity.class));
                        return true;
                    case R.id.navigation_employees:
                        startActivity(new Intent(getApplicationContext(), EmployeesActivity.class));
                        return true;
                }
                return false;
            }
        });
    }

}