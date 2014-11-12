package me.anuraag.lunch;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;
import android.content.*;

import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseUser;

import java.text.ParseException;


public class LoginActivity extends Activity {
private EditText email,password;
private Intent mainScreen;
private Button submit;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
//        ParseUser currentUser = ParseUser.getCurrentUser();
//        if (currentUser != null) {
//            Intent mainScreen = new Intent(getApplicationContext(), MainActivity.class);
//            startActivity(mainScreen);
//        }
        Parse.initialize(this, "BPDyYZu9VR7X6DKdP2k7SmA2DmO3ziNqXAjnR6oW", "VfugZOJG4d8hpRmUVWCTQrWiisgDsMbjITCRAS8U");
        ActionBar actionBar = getActionBar();
        actionBar.hide();
        try {
            ParseUser currentUser = ParseUser.getCurrentUser();
            if(currentUser!= null) {
                Intent mainScreen = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(mainScreen);
            }
        }catch (NullPointerException n){

        }
        email = (EditText)findViewById(R.id.email);
        password = (EditText)findViewById(R.id.password);
        submit = (Button)findViewById(R.id.button);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login();
            }
        });

    }

    private void login(){
        mainScreen = new Intent(this,MainActivity.class);
        String myEmail = email.getText().toString();
        String myPassword= password.getText().toString();

        ParseUser.logInInBackground(myEmail, myPassword, new LogInCallback() {

            public void done(ParseUser parseUser, com.parse.ParseException e) {
                if (parseUser != null) {
                    if(parseUser.getBoolean("emailVerified")) {
                        Log.i("Worked", parseUser.getEmail());
                        startActivity(mainScreen);
                    }else{
                        verifyAlert();
                    }

                } else {
                    exceptionAlert(e.toString().substring(25));

                }
            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.login, menu);
        return true;
    }
    private void exceptionAlert(String title){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(title)
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //do things
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }
    private void verifyAlert(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Please verify your email!")
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //do things
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
