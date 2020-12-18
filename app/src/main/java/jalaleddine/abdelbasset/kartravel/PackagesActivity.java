package jalaleddine.abdelbasset.kartravel;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import dmax.dialog.SpotsDialog;

public class PackagesActivity extends AppCompatActivity {

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

    AlertDialog spotsDialog;
    BottomNavigationView navigationView;

    ArrayList<ContactInformation> contactInformationArrayList;

    ListAdapter adapter;
    ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_packages);
        spotsDialog = new SpotsDialog.Builder().setContext(PackagesActivity.this).
                setMessage("Loading Information...").build();
        spotsDialog.show();
        BottomNavigation();
        GetInfo();
        contactInformationArrayList = new ArrayList<>();
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
            navigationView.setSelectedItemId(R.id.navigation_packages);
        }
        else if (number.contains("61657756")){
            //TODO: Fix for User

            navigationView.inflateMenu(R.menu.bottom_navigation_user);
            navigationView.setSelectedItemId(R.id.navigation_packages);
        }
        else{
            //TODO: Fix for Admin
            navigationView.inflateMenu(R.menu.bottom_navigation_admin);
            navigationView.setSelectedItemId(R.id.navigation_packages);
        }
        spotsDialog.dismiss();
    }
    private void GetFromDatabase() {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy
                .Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        contactInformationArrayList.clear();


        try {
            Class.forName(forgy);
            connection = DriverManager.getConnection(url,username
                    ,pass);
            Statement statement = connection.createStatement();

            ResultSet rs = statement.executeQuery(" SELECT * FROM PACKAGE where packageID = packageID");
            while(rs.next())
            {
                String packageName = rs.getString(2);
                String Capacity = rs.getString(3);


                contactInformationArrayList.add(new ContactInformation (packageName,packageName,Capacity));

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

                Toast.makeText(PackagesActivity.this, "Clicked!", Toast.LENGTH_SHORT).show();
            }
        });
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onResume() {
        navigationView.setSelectedItemId(R.id.navigation_packages);
        GetFromDatabase();
        super.onResume();
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
                    case R.id.navigation_pending:
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
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_add) {
            startActivity(new Intent(getApplicationContext(), AddPackageActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.adder, menu);
        return true;
    }

}