package com.swiss4ward.swissapp;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import com.swiss4ward.swissapp.data.UsersSQLiteHelper;
import com.swiss4ward.swissapp.models.Address;
import com.swiss4ward.swissapp.models.Company;
import com.swiss4ward.swissapp.models.User;
import com.swiss4ward.swissapp.ui.main.DetailFragment;
import com.swiss4ward.swissapp.ui.main.MainFragment;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public class MainActivity extends AppCompatActivity implements MainFragment.OnItemSelectedListener {

    MainFragment mmainFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        if (findViewById(R.id.container) != null) {
            if (savedInstanceState != null) {
                return;
            }
            mmainFragment = new MainFragment();
            mmainFragment.setArguments(getIntent().getExtras());
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, mmainFragment)
                    .commit();
        }

        UsersSQLiteHelper usersDB = new UsersSQLiteHelper(this, "DBUsers", null, 2);
        SQLiteDatabase db = usersDB.getWritableDatabase();
        String[] cols = new String[]{"id", "name", "username", "email", "phone", "website",
                "street", "suite", "city", "zipcode", "x", "y",
                "companyName", "catchPhrase", "bs"};
        Cursor cursor = db.query("Users", cols, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(0);
                String name = cursor.getString(1);
                String username = cursor.getString(2);
                String email = cursor.getString(3);
                String phone = cursor.getString(4);
                String website = cursor.getString(5);
                String street = cursor.getString(6);
                String suite = cursor.getString(7);
                String city = cursor.getString(8);
                String zipcode = cursor.getString(9);
                String lat = cursor.getString(10);
                String lng = cursor.getString(11);
                String companyName = cursor.getString(12);
                String catchPhrase = cursor.getString(13);
                String bs = cursor.getString(14);
                Address address = new Address(street, suite, city, zipcode, lat, lng);
                Company company = new Company(companyName, catchPhrase, bs);
                User user = new User(id, name, username, email, website, phone, address, company);
                Skeleton.getInstance().addUser(user);
            } while (cursor.moveToNext());
            cursor.close();
            mmainFragment.onUserAdded();
            db.close();
        } else {
            new myDataTask().execute();
        }


    }

    @Override
    public void onItemSelectedListener(int position) {
        DetailFragment detailFragment = (DetailFragment)
                getSupportFragmentManager().findFragmentById(R.id.detail_fragment);

        if (detailFragment != null) {
            detailFragment.setDetailItem(position);
        } else {
            detailFragment = new DetailFragment();
            Bundle args = new Bundle();
            args.putInt("Position", position);
            detailFragment.setArguments(args);

            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.container, detailFragment);
            transaction.addToBackStack(null);

            transaction.commit();
        }
    }


    protected class myDataTask extends AsyncTask<Void, Void, JSONArray> {
        @Override
        protected JSONArray doInBackground(Void... params) {

            String str = "https://jsonplaceholder.typicode.com/users";
            URLConnection urlConn = null;
            BufferedReader bufferedReader = null;
            try {
                URL url = new URL(str);
                urlConn = url.openConnection();
                bufferedReader = new BufferedReader(new InputStreamReader(urlConn.getInputStream()));

                StringBuffer stringBuffer = new StringBuffer();
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    stringBuffer.append(line);
                }

                return new JSONArray(stringBuffer.toString());
            } catch (Exception ex) {
                Log.e("App", "yourDataTask", ex);
                return null;
            } finally {
                if (bufferedReader != null) {
                    try {
                        bufferedReader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        @Override
        protected void onPostExecute(JSONArray response) {
            if (response != null) {
                try {
                    UsersSQLiteHelper usersDB = new UsersSQLiteHelper(getBaseContext(), "DBUsers", null, 2);
                    SQLiteDatabase db = usersDB.getWritableDatabase();
                    for (int i = 0; i < response.length(); i++) {
                        User user = new User(response.getJSONObject(i));
                        Skeleton.getInstance().addUser(user);

                        ContentValues newRegister = new ContentValues();
                        newRegister.put("id", user.getId());
                        newRegister.put("name", user.getName());
                        newRegister.put("username", user.getUsername());
                        newRegister.put("email", user.getEmail());
                        newRegister.put("phone", user.getPhone());
                        newRegister.put("website", user.getWebsite());
                        newRegister.put("street", user.getAddress().getStreet());
                        newRegister.put("suite", user.getAddress().getCity());
                        newRegister.put("city", user.getAddress().getCity());
                        newRegister.put("zipcode", user.getAddress().getZipCode());
                        newRegister.put("x", user.getAddress().getLat());
                        newRegister.put("y", user.getAddress().getLng());
                        newRegister.put("companyName", user.getCompany().getName());
                        newRegister.put("catchPhrase", user.getCompany().getCatchPhrase());
                        newRegister.put("bs", user.getCompany().getBs());

                        db.insert("Users", null, newRegister);

                    }
                    db.close();
                    mmainFragment.onUserAdded();
                } catch (JSONException ex) {
                    Log.e("App", "Failure", ex);
                }
            }
        }
    }
}