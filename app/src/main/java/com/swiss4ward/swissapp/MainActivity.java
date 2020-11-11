package com.swiss4ward.swissapp;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Point;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import com.swiss4ward.swissapp.data.UsersSQLiteHelper;
import com.swiss4ward.swissapp.models.Address;
import com.swiss4ward.swissapp.models.Company;
import com.swiss4ward.swissapp.models.User;
import com.swiss4ward.swissapp.ui.main.MainFragment;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, MainFragment.newInstance())
                    .commitNow();
        }
        UsersSQLiteHelper usersDB = new UsersSQLiteHelper(this, "DBUsers", null, 1);
        SQLiteDatabase db = usersDB.getReadableDatabase();
        String[] cols = new String[] {"id","name","username"};
        Cursor cursor = db.query("Users", cols, null, null, null, null, null);
        if(cursor.moveToFirst()){
            List<User> users = new ArrayList<>();
            do{
                int id = cursor.getInt(0);
                String name = cursor.getString(1);
                String username = cursor.getString(2);
                User user = new User(id,name,username);
                users.add(user);
            }while(cursor.moveToNext());
            Skeleton.getInstance().setUsers(users);
        }else{
            new myDataTask().execute();
        }

        db.close();
    }

    protected class myDataTask extends AsyncTask<Void, Void, JSONArray>
    {
        @Override
        protected JSONArray doInBackground(Void... params)
        {

            String str="https://jsonplaceholder.typicode.com/users";
            URLConnection urlConn = null;
            BufferedReader bufferedReader = null;
            try
            {
                URL url = new URL(str);
                urlConn = url.openConnection();
                bufferedReader = new BufferedReader(new InputStreamReader(urlConn.getInputStream()));

                StringBuffer stringBuffer = new StringBuffer();
                String line;
                while ((line = bufferedReader.readLine()) != null)
                {
                    stringBuffer.append(line);
                }

                return new JSONArray(stringBuffer.toString());
            }
            catch(Exception ex)
            {
                Log.e("App", "yourDataTask", ex);
                return null;
            }
            finally
            {
                if(bufferedReader != null)
                {
                    try {
                        bufferedReader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        @Override
        protected void onPostExecute(JSONArray response)
        {
            if(response != null)
            {
                try {


                    User user = new User(response.getJSONObject(0));
                    //Log.e("App", "Success: " + response.getString("yourJsonElement") );
                } catch (JSONException ex) {
                    Log.e("App", "Failure", ex);
                }
            }
        }
    }
}