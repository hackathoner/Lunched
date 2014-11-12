package me.anuraag.lunch;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.format.Time;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.*;
import android.content.*;
//import com.parse.Parse;
//import com.parse.ParseAnaleytics;
//import com.parse.ParseObject;
//import com.parse.ParseUser;
//import com.parse.SignUpCallback;
import com.parse.*;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.text.ParseException;


public class MyActivity extends Activity {
    private EditText name, email, pass, repass, interests;
    private String myName, myEmail, myPass, myRepass, myInterests;
    private Button submit,login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_my);
        ActionBar actionBar = getActionBar();
        actionBar.hide();
        Parse.initialize(this, "BPDyYZu9VR7X6DKdP2k7SmA2DmO3ziNqXAjnR6oW", "VfugZOJG4d8hpRmUVWCTQrWiisgDsMbjITCRAS8U");
        try {
            ParseUser currentUser = ParseUser.getCurrentUser();
            Intent mainScreen = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(mainScreen);
        }catch (NullPointerException n){

        }


//        ParseObject testObject = new ParseObject("TestObject");
//        testObject.put("foo", "bar");
//        testObject.saveInBackground();
        // Declare all the front end fields
        name = (EditText) findViewById(R.id.name);
        email = (EditText) findViewById(R.id.email);
        pass = (EditText) findViewById(R.id.password);
        repass = (EditText) findViewById(R.id.reenter);
        submit = (Button) findViewById(R.id.button);
        login = (Button) findViewById(R.id.button2);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mainScreen = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(mainScreen);
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signUp();


            }
        });


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.my, menu);
        return true;
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

    public void signUp() {

            myName = name.getText().toString();
            myEmail = email.getText().toString();
            myPass = pass.getText().toString();
            myRepass = repass.getText().toString();

            if(myName.isEmpty() || myEmail.isEmpty() || myPass.isEmpty() || myRepass.isEmpty()) {
                unfilled();
            }else{
                legitSignup(true);
            }

    }
    private void legitSignup(boolean worked){
        if (myPass.compareTo(myRepass) == 0) {
            if (myPass.length() >= 6) {
                ParseUser user = new ParseUser();
                user.put("Name", myName);
                user.setUsername(myEmail);
                user.setPassword(myPass);
                user.setEmail(myEmail);
                user.signUpInBackground(new SignUpCallback() {
                    public void done(com.parse.ParseException e) {
                        if (e == null)
                        {
                            Intent mainScreen = new Intent(getApplicationContext(), LoginActivity.class);
                            startActivity(mainScreen);
                            Log.d("Parse", "User save in background worked... but not really.");
                        }else{
                            String error= (e.toString().substring(26));
                            Log.i("Exception", error );
                            exceptionAlert(error);

                        }

                    }
                });
            }else{
                showShortAlert();
            }
        }else{
            misMatch();
        }
    }
    private void misMatch() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Please make sure your passwords match")
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //do things
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    private void showShortAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Please makes sure your password is at least 6 characters long ")
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //do things
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }
    private void unfilled() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Please complete all the fields")
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //do things
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
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



}
