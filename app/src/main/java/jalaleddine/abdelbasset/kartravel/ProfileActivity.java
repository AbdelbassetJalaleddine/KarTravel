package jalaleddine.abdelbasset.kartravel;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;

import java.util.HashMap;
import java.util.Map;

import dmax.dialog.SpotsDialog;
import mehdi.sakout.fancybuttons.FancyButton;

public class ProfileActivity extends AppCompatActivity implements ForceUpdateChecker.OnUpdateNeededListener {

    ImageView iv;
    EditText nameeditText;
    EditText phoneeditText;
    AlertDialog spotsDialog;
    FancyButton emergencybutton;
    FancyButton statisticsbutton;
    FancyButton contactslistbutton;
    BottomNavigationView navigationView;
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        iv = findViewById(R.id.imageView);
        nameeditText = findViewById(R.id.NameeditText);
        phoneeditText = findViewById(R.id.PhoneNumbereditText);
        /*emergencybutton = findViewById(R.id.Emergencybutton);
        //emergencybutton.setIconResource(R.drawable.emergency);
        emergencybutton.setIconPosition(FancyButton.POSITION_BOTTOM);

        statisticsbutton = findViewById(R.id.Statisticsbutton);
        //statisticsbutton.setIconResource(R.drawable.statistics);
        statisticsbutton.setIconPosition(FancyButton.POSITION_BOTTOM);

        contactslistbutton = findViewById(R.id.ContactsListButton);
        //contactslistbutton.setIconResource(R.drawable.contacts_list);
        contactslistbutton.setIconPosition(FancyButton.POSITION_BOTTOM);
*/
        spotsDialog = new SpotsDialog.Builder().setContext(ProfileActivity.this).
        setMessage("Loading Information...").build();
        spotsDialog.show();
        BottomNavigation();
        GetInfo();
        FirebaseChecker(); // check if the app is up to date or if it needs an update
        // then send the maleuser to the store
       // checkLocationPermission();
        /*Intent intent = new Intent(this, MyService.class);
        intent.putExtra("Phone Number",phoneeditText.getText().toString());
        startService(intent);*/
    }

    @Override
    protected void onResume() {
        navigationView.setSelectedItemId(R.id.navigation_profile);
        super.onResume();
    }

    private void GetInfo() {
        SharedPreferences editor = getSharedPreferences("UsersData", MODE_PRIVATE);
        String number = editor.getString("phonenumber", "1");
        String name = editor.getString("name", "Aboud");
        String gender = editor.getString("gender", "male");

                                if(number.contains("71657756")){
                                    //adder
                                    //TODO: Fix it for Employees
                                    if(gender.toLowerCase().equals("male")){
                                        iv.setImageResource(R.drawable.maleengineer);
                                    }
                                    else{
                                        iv.setImageResource(R.drawable.femaleengineer);
                                    }
                                    navigationView.inflateMenu(R.menu.bottom_navigation_employee);
                                    navigationView.setSelectedItemId(R.id.navigation_profile);

                                }
                                else if (number.contains("81657756")){
                                    //TODO: Fix for User
                                    if(gender.toLowerCase().equals("male")){
                                        iv.setImageResource(R.drawable.maleuser);
                                    }
                                    else{
                                        iv.setImageResource(R.drawable.femaleuser);
                                    }
                                   navigationView.inflateMenu(R.menu.bottom_navigation_user);
                                    navigationView.setSelectedItemId(R.id.navigation_profile);

                                }
                                else{
                                    //TODO: Fix for Admin
                                    iv.setImageResource(R.drawable.admin);
                                    navigationView.inflateMenu(R.menu.bottom_navigation_admin);
                                    navigationView.setSelectedItemId(R.id.navigation_profile);

                                }
                                nameeditText.setText(name);
                                phoneeditText.setText(number);
                                spotsDialog.dismiss();
            }

    private void BottomNavigation() {
        navigationView = findViewById(R.id.bottom_navigation_employee);
        navigationView.setSelectedItemId(R.id.navigation_profile);
        navigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_profile:
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
        if (id == R.id.action_logout) {
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(ProfileActivity.this, RegistrationActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);        }
        /*if(id == R.id.action_credits){
            Intent intent = new Intent(ProfileActivity.this, CreditsActivity.class);
            startActivity(intent);
        }*/
        /*if(id == R.id.action_emergency){
            Intent intent = new Intent(ProfileActivity.this, EmergencyActivity.class);
            startActivity(intent);*/
        //}

        return super.onOptionsItemSelected(item);
    }
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.profile, menu);
        return true;
    }

    public void FirebaseChecker(){

        final FirebaseRemoteConfig firebaseRemoteConfig = FirebaseRemoteConfig.getInstance();

        // set in-app defaults
        Map<String, Object> remoteConfigDefaults = new HashMap();
        remoteConfigDefaults.put(ForceUpdateChecker.KEY_UPDATE_REQUIRED, false);
        remoteConfigDefaults.put(ForceUpdateChecker.KEY_CURRENT_VERSION, "1.0");
        remoteConfigDefaults.put(ForceUpdateChecker.KEY_UPDATE_URL,
                "https://dowanghami.en.aptoide.com/?store_name=abdelbassetj&app_id=46985875");

        firebaseRemoteConfig.setDefaultsAsync(remoteConfigDefaults);
        firebaseRemoteConfig.fetch(60) // fetch every minutes
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d("Firebase", "remote config is fetched.");
                            firebaseRemoteConfig.fetchAndActivate();
                        }
                    }
                });
        ForceUpdateChecker.with(this).onUpdateNeeded(this).check();
        final FirebaseRemoteConfig remoteConfig = FirebaseRemoteConfig.getInstance();
    }

    @Override
    public void onUpdateNeeded(final String updateUrl) {
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("New version available")
                .setMessage("Please, update the app to the newest version.")
                .setPositiveButton("Update",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                redirectStore(updateUrl);
                            }
                        }).setNegativeButton("No, thanks",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //finish(); if i want to force the update i just put this
                                // so that the maleuser either updates the app
                                // or he gets kicked out

                            }
                        }).create();
        dialog.show();
    }
    private void redirectStore(String updateUrl) {
        final Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(updateUrl));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    public void ContactsActivity(View view) {
       /* Intent intent = new Intent(this, ContactsActivity.class);
        startActivity(intent);*/
    }

    public void StatisticsActivity(View view) {
        /*Intent intent = new Intent(this, StatisticsActivity.class);
        startActivity(intent);*/
    }

    public void EmergencyActivity(View view) {
       /* Intent intent = new Intent(this, EmergencyActivity.class);
        startActivity(intent);*/
    }

   /* public boolean checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

               // Alert Dialog to show why i want the maleuser's location
                new AlertDialog.Builder(this)
                        .setTitle(R.string.title_location_permission)
                        .setMessage(R.string.text_location_permission)
                        .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //permission for maleuser location
                                ActivityCompat.requestPermissions(ProfileActivity.this,
                                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                        MY_PERMISSIONS_REQUEST_LOCATION);
                            }
                        })
                        .create()
                        .show();


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        } else {
            return true;
        }
    }*/

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {

    }



}
