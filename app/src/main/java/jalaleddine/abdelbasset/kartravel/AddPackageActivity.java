package jalaleddine.abdelbasset.kartravel;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class AddPackageActivity extends AppCompatActivity {

    // Connect to MSSQL
    private static String ip = "192.168.10.102";
    private static String port = "1433";
    private static String forgy = "net.sourceforge.jtds.jdbc.Driver";
    private static String database = "KARTRAVEL";
    private static String username = "sa";
    private static String pass = "SmartGym";
    private static String url = "jdbc:jtds:sqlserver://" + ip
            + ":" + port + "/" + database;
    private Connection connection = null;
    String do_it;

    EditText editTextLocation;
    EditText editTextCapacity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_package);
        editTextLocation = findViewById(R.id.editTextLocation);
        editTextCapacity = findViewById(R.id.editTextCapacity);
    }
    public void AddPackage(View view) {
        String capacity = editTextCapacity.getText().toString().trim();
        String location= editTextLocation.getText().toString().trim();

        if(capacity.isEmpty() || location.isEmpty()){
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

            do_it = "insert into dbo.PACKAGE " +
                    "(packageID,packageName,packageCapacity) " +
                    "VALUES("  + realID  + "," + "'" + location + "'" + ","  + capacity  +");";
            System.out.println("Done " + do_it);
            AddtoDatabase();
        }
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
}