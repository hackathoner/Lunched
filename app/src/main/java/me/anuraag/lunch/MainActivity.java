package me.anuraag.lunch;

import android.app.Activity;
import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;
import android.content.*;
import android.widget.*;

import com.mapbox.mapboxsdk.views.MapView;
import com.parse.Parse;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import org.w3c.dom.Text;


public class MainActivity extends Activity {
//    private ParseUser user = ParseUser.getCurrentUser();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Parse.initialize(this, "BPDyYZu9VR7X6DKdP2k7SmA2DmO3ziNqXAjnR6oW", "VfugZOJG4d8hpRmUVWCTQrWiisgDsMbjITCRAS8U");

        ParseUser currentUser = ParseUser.getCurrentUser();
            if(currentUser==null){
                Intent login = new Intent(this, LoginActivity.class);
                startActivity(login);
            }
        ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        actionBar.setDisplayShowTitleEnabled(false);


        ActionBar.Tab lunchTab = actionBar.newTab().setText("Lunch").setTabListener(new TabListener<PlaceholderFragment>(this, "Lunch", PlaceholderFragment.class));
        actionBar.addTab(lunchTab);

        ActionBar.Tab historyTab = actionBar.newTab().setText("Profile").setTabListener(new TabListener<ProfileFragment>(this, "Profile", ProfileFragment.class));
        actionBar.addTab(historyTab);

        ActionBar.Tab accountTab = actionBar.newTab().setText("Account").setTabListener(new TabListener<SettingsFragment>(this, "Account", SettingsFragment.class));
        actionBar.addTab(accountTab);
////
//
//        if (savedInstanceState == null) {
//            getFragmentManager().beginTransaction()
//                    .add(R.id.container, new PlaceholderFragment())
//                    .commit();
//        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
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


    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        private ParseUser user;
        private Button logout;
        private TextView myView;

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            ParseUser currentUser = ParseUser.getCurrentUser();
            if(currentUser==null){
                Intent login = new Intent(getActivity(), LoginActivity.class);
                startActivity(login);
            }
//                MapView mapView = new MapView(rootView, "examples.map-zr0njcqy");
//           getActivity().setContentView(mapView);
            myView = (TextView)rootView.findViewById(R.id.textView);
            logout = (Button)rootView.findViewById(R.id.button);
//            MapView mapView = new MapView(this,swagid);
            user = ParseUser.getCurrentUser();
            if(user!=null) {
                Log.i("User", user.getEmail());
                myView.setText(user.getEmail());
                logout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ParseUser.logOut();
                        getActivity().finish();
                        startActivity(getActivity().getIntent());
//                        Intent login = new Intent(getActivity(), LoginActivity.class);
//                        startActivity(login);
                    }
                });
            }else{
                Intent login = new Intent(getActivity(), LoginActivity.class);
                startActivity(login);
            }
            return rootView;
        }
    }
    public static class SettingsFragment extends Fragment {
        private ParseUser user;
        private TextView name, email, password, interests;
        private Button submit;

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.accountsettings, container, false);

            user = ParseUser.getCurrentUser();
            if(user!=null) {
                Log.i("User", user.getEmail());
            }else{
                Intent login = new Intent(getActivity(), LoginActivity.class);
                startActivity(login);
            }
            name = (TextView)rootView.findViewById(R.id.name);
            email = (TextView)rootView.findViewById(R.id.email);
            password = (TextView)rootView.findViewById(R.id.password);
            interests = (TextView)rootView.findViewById(R.id.interests);
            submit = (Button)rootView.findViewById(R.id.button);

            name.setText(user.getString("name"));
            email.setText(user.getEmail());
            interests.setText(user.getString("Interets"));
            try {
                interests.setText(user.getString("interests"));
            }catch (NullPointerException n){

            }
            submit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    verifyAlert();
                    Context context = getActivity();
                    CharSequence text = "Edits Completed";
                    int duration = Toast.LENGTH_SHORT;
                    Toast.makeText(context, text, duration).show();
                }
            });



            return rootView;
        }
        private void verifyAlert(){
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage("Are you sure you want to edit your info?")
                    .setCancelable(false)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            if(!email.getText().toString().isEmpty()) {
                                user.setEmail(email.getText().toString());
                                user.setUsername(email.getText().toString());

                            }
                            if(!password.getText().toString().isEmpty()) {
                                user.setPassword(password.getText().toString());
                            }
                            if(!name.getText().toString().isEmpty()) {
                                user.put("Name",name.getText().toString());
                            }
                            if(!interests.getText().toString().isEmpty()) {
                                user.put("Interests", interests.getText().toString());
                            }
                            if(!password.getText().toString().isEmpty()) {
                                user.setPassword(password.getText().toString());
                            }
                            user.saveInBackground(new SaveCallback() {
                                public void done(com.parse.ParseException e) {
                                    // TODO Auto-generated method stub
                                    if (e == null) {
                                      Log.i("Worked","Worked");
                                       String s = user.getString("Interests");
                                      Log.i("Interests",s);
                                    } else {
                                      Log.i("Ooops",e.toString());
                                    }
                                }
                            });
                        }
                    });
            AlertDialog alert = builder.create();
            alert.show();
        }
    }
    public static class ProfileFragment extends Fragment {
        private ParseUser user;
        private TextView name, interests;

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.profile, container, false);

            user = ParseUser.getCurrentUser();
            if(user!=null) {
                Log.i("User", user.getEmail());
            }else{
                Intent login = new Intent(getActivity(), LoginActivity.class);
                startActivity(login);
            }
            name = (TextView)rootView.findViewById(R.id.name);
            interests = (TextView)rootView.findViewById(R.id.Sample);

            name.setText(user.getString("Name"));
            interests.setText(user.getString("Interests"));

            return rootView;
        }
    }
    public static class TabListener<T extends Fragment> implements ActionBar.TabListener {
        private Fragment mFragment;
        private final Activity mActivity;
        private final String mTag;
        private final Class<T> mClass;

        /** Constructor used each time a new tab is created.
         * @param activity  The host Activity, used to instantiate the fragment
         * @param tag  The identifier tag for the fragment
         * @param clz  The fragment's Class, used to instantiate the fragment
         */
        public TabListener(Activity activity, String tag, Class<T> clz) {
            mActivity = activity;
            mTag = tag;
            mClass = clz;
        }

    /* The following are each of the ActionBar.TabListener callbacks */

        public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
            // Check if the fragment is already initialized
            if (mFragment == null) {
                // If not, instantiate and add it to the activity
                mFragment = Fragment.instantiate(mActivity, mClass.getName());
                ft.add(android.R.id.content, mFragment, mTag);
            } else {
                // If it exists, simply attach it in order to show it
                ft.attach(mFragment);
            }
        }

        public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {
            if (mFragment != null) {
                // Detach the fragment, because another one is being attached
                ft.detach(mFragment);

//                ft.detach(mFragment);
            }
        }

        public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {
            // User selected the already selected tab. Usually do nothing.
        }
    }
    }
