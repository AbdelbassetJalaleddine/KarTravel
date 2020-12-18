package jalaleddine.abdelbasset.kartravel;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import jalaleddine.abdelbasset.kartravel.ListAdapter;


import dmax.dialog.SpotsDialog;

public class EmployeesActivity extends AppCompatActivity {

    AlertDialog spotsDialog;
    BottomNavigationView navigationView;

    // Connect to MSSQL
    private static String ip = "192.168.10.101";
    private static String port = "1433";
    private static String forgy = "net.sourceforge.jtds.jdbc.Driver";
    private static String database = "KARTRAVEL";
    private static String username = "sa";
    private static String pass = "SmartGym";
    private static String url = "jdbc:jtds:sqlserver://" + ip
            + ":" + port + "/" + database;
    private Connection connection = null;


    ArrayList<ContactInformation> contactInformationArrayList;

    ListAdapter adapter;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employees);
        spotsDialog = new SpotsDialog.Builder().setContext(EmployeesActivity.this).
                setMessage("Loading Information...").build();
        spotsDialog.show();
        BottomNavigation();
        contactInformationArrayList = new ArrayList<>();
        GetFromDatabase();
    }

    private void GetFromDatabase() {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy
                .Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        contactInformationArrayList.clear();

        //String GetIT = "SELECT employeeType, phoneNumber, employeeGender FROM EMPLOYEE;";
        String GetIT = "SELECT * FROM EMPLOYEE;";

        try {
            Class.forName(forgy);
            connection = DriverManager.getConnection(url,username
                    ,pass);
            System.out.println("Done " + GetIT);
            Statement statement = connection.createStatement();

            ResultSet rs = statement.executeQuery(" SELECT * FROM EMPLOYEE where employeeType = employeeType");
            while(rs.next())
            {
                String FName = rs.getString(2);
                String MName = rs.getString(3);
                String LName = rs.getString(4);

                String position = rs.getString(7);

                String FullName = FName + MName + LName;
                contactInformationArrayList.add(new ContactInformation (FullName,position,null));

            }
            MakeList();

        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }


    private void MakeList() {

        listView =  findViewById(R.id.listview);
        // Getting a reference to listview of main.xml layout file
        // Setting the adapter to the listView
        adapter = new ListAdapter(this, R.layout.listview_layout, contactInformationArrayList);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Toast.makeText(EmployeesActivity.this, "Clicked!", Toast.LENGTH_SHORT).show();
            }
        });
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onResume() {
        navigationView.setSelectedItemId(R.id.navigation_employees);
        GetFromDatabase();
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