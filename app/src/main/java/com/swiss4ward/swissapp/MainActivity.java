package com.swiss4ward.swissapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

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
import com.swiss4ward.swissapp.ui.main.DetailFragment;
import com.swiss4ward.swissapp.ui.main.MainFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements MainFragment.OnItemSelectedListener{

    MainFragment mmainFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        if (findViewById(R.id.container) != null) {
            if (savedInstanceState != null){
                return;
            }
            mmainFragment = new MainFragment();
            mmainFragment.setArguments(getIntent().getExtras());
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, mmainFragment)
                    .commit();
        }

        UsersSQLiteHelper usersDB = new UsersSQLiteHelper(this, "DBUsers", null, 1);
        SQLiteDatabase db = usersDB.getWritableDatabase();
        String[] cols = new String[] {"id","name","username"};
        Cursor cursor = db.query("Users", cols, null, null, null, null, null);
        if(cursor.moveToFirst()){
            do{
                int id = cursor.getInt(0);
                String name = cursor.getString(1);
                String username = cursor.getString(2);
                User user = new User(id,name,username);
                Skeleton.getInstance().addUser(user);
            }while(cursor.moveToNext());
            mmainFragment.onUserAdded();
        }else{
            new myDataTask().execute();
        }

        db.close();

    }

    @Override
    public void onItemSelectedListener(int position) {
        DetailFragment detailFragment = (DetailFragment)
                getSupportFragmentManager().findFragmentById(R.id.detail_fragment);

        if(detailFragment != null ){
            detailFragment.setDetailItem(position);
        }else{
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
                    for (int i=0; i<response.length();i++) {
                        Skeleton.getInstance().addUser(new User(response.getJSONObject(i)));
                    }
                    mmainFragment.onUserAdded();
                    //Log.e("App", "Success: " + response.getString("yourJsonElement") );
                } catch (JSONException ex) {
                    Log.e("App", "Failure", ex);
                }
            }
        }
    }
}