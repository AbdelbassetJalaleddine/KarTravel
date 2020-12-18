package jalaleddine.abdelbasset.kartravel;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.DatePickerDialog;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.hbb20.CountryCodePicker;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;

import static android.os.Build.VERSION_CODES.N;

public class AddEmployeeActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    DatePickerDialog datePickerDialog;
    static String LastSeen = 0 + "Day(s) ago";
    boolean pickedDate = false;
    boolean pickedDate2 = false;
    EditText editTextFirstName;
    EditText editTextMiddleName;
    EditText editTextLastName;
    EditText editTextPhone;
    CountryCodePicker countryCodePicker;
    Spinner spinnerGender;
    Spinner positionSpinner;

    EditText editTextDOB;
    EditText editTextStartingDate;
    String PickedStartingDate;
    String PickedDOB;

    EditText editTextMonthlySalary;
    EditText editTextAddress;

    EditText editTextIBANNumber;



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


    String do_it = "meow";

    String FirstName;
    String MiddleName;
    String LastName;
    String gender;
    String number;
    String code;
    String position;
    String salary;
    String address;
    String IBAN;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_employee);
        datePickerDialog = new DatePickerDialog(
                this, AddEmployeeActivity.this, 2020, 0, 1);
        editTextFirstName = findViewById(R.id.editTextFirstName);
        editTextMiddleName = findViewById(R.id.editTextMiddleName);
        editTextLastName = findViewById(R.id.editTextLastName);
        editTextDOB = findViewById(R.id.editTextDOB);
        editTextStartingDate = findViewById(R.id.editTextStartingDate);
        editTextPhone = findViewById(R.id.editTextPhone);
        countryCodePicker = findViewById(R.id.ccp);
        countryCodePicker.setCountryForPhoneCode(961);
        spinnerGender = findViewById(R.id.genderSpinner);
        positionSpinner = findViewById(R.id.positionSpinner);
        editTextMonthlySalary = findViewById(R.id.editTextMonthlySalary);
        editTextAddress = findViewById(R.id.editTextAddress);
        editTextIBANNumber = findViewById(R.id.editTextIBANNumber);
    }

    public void AddEmployee(View view) {
        FirstName = editTextFirstName.getText().toString().trim();
        MiddleName = editTextMiddleName.getText().toString().trim();
        LastName = editTextLastName.getText().toString().trim();
        gender = spinnerGender.getSelectedItem().toString();
        number = editTextPhone.getText().toString().trim();
        code = countryCodePicker.getSelectedCountryCode() + number;
        position = positionSpinner.getSelectedItem().toString();
        salary = editTextMonthlySalary.getText().toString().trim();
        address = editTextAddress.getText().toString().trim();
        IBAN = editTextIBANNumber.getText().toString().trim();
        if(!pickedDate || !pickedDate2){
            Toast.makeText(this, "Please fill all the information", Toast.LENGTH_SHORT).show();
            return;
        }
        else{
            SharedPreferences editor = getSharedPreferences("EmployeeID", MODE_PRIVATE);
            int ID = editor.getInt("ID",101);

            int realID = ID + 1;
            SharedPreferences.Editor editor2 = getSharedPreferences("EmployeeID", MODE_PRIVATE).edit();
            editor2.putInt("ID", realID);
            editor2.apply();

           do_it = "insert into dbo.EMPLOYEE " +
                    "(employeeID,fName,mName,lName,phoneNumber,dob,position,startingDate,monthlySalary,address,ibanNumber,employeeType,employeeGender) " +
                    "VALUES("  + realID  + "," + "'" + FirstName + "'" + "," + "'" + MiddleName + "'" + "," + "'" + LastName  + "'" + "," +
                    "'" +code + "'" +",'" + PickedDOB + "'," +
                   "'" + position + "'" + ",'" + PickedStartingDate + "'," + salary + ",'" +address + "'," + "'" + IBAN + "'" + "," +
                    "'Employee'" + "," +  "'" + gender  + "'" +");";
            AddtoDatabase();
        }
    }
    public void AddDateLastSeen(View view) {
        if(Build.VERSION.SDK_INT > N){
            datePickerDialog.show();
            datePickerDialog.setOnDateSetListener(new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                    String monthy = String.valueOf(month + 1);
                    if(monthy.length() == 1){
                        monthy = 0 + monthy;
                    }
                    String dayy = String.valueOf(dayOfMonth);
                    if(dayy.length() == 1){
                        dayy = 0 + dayy;
                    }
                    PickedDOB = year + "-" + monthy + "-" + dayy;
                    editTextDOB.setText(PickedDOB);
                    // LastSeenEditText.setText(milliseconds(PickedCalenderDate));
                    pickedDate = true;
                }
            });
        }
        else{
            Toast.makeText(this, "Saved the Date!", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

    }
    private void AddtoDatabase() {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy
                .Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        try {
            Class.forName(forgy);
            Connection connection2 = DriverManager.getConnection(url,username,pass);
            System.out.println("Done " + do_it);
            Statement statement = connection2.createStatement();
            statement.execute(do_it);
            Toast.makeText(this, "Added to Database!", Toast.LENGTH_SHORT).show();

        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    public void addStartingDate(View view) {
        if(Build.VERSION.SDK_INT > N){
            datePickerDialog.show();
            datePickerDialog.setOnDateSetListener(new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                    String monthy = String.valueOf(month + 1);
                    if(monthy.length() == 1){
                        monthy = 0 + monthy;
                    }
                    String dayy = String.valueOf(dayOfMonth);
                    if(dayy.length() == 1){
                        dayy = 0 + dayy;
                    }
                    PickedStartingDate =  year + "-" + monthy + "-" + dayy ;
                    editTextStartingDate.setText(PickedStartingDate);
                    // LastSeenEditText.setText(milliseconds(PickedCalenderDate));
                    pickedDate2 = true;
                }
            });
        }
        else{
            Toast.makeText(this, "Saved the Date!", Toast.LENGTH_SHORT).show();
        }

    }
}