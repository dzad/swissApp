package com.swiss4ward.swissapp.ui.main;

import androidx.lifecycle.ViewModelProvider;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.swiss4ward.swissapp.R;
import com.swiss4ward.swissapp.Skeleton;
import com.swiss4ward.swissapp.adapters.UserAdapter;
import com.swiss4ward.swissapp.data.UsersSQLiteHelper;
import com.swiss4ward.swissapp.models.User;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

public class MainFragment extends Fragment {

    private MainViewModel mViewModel;
    public ListView myListView;
    public UserAdapter adapter;

    public static MainFragment newInstance() {
        return new MainFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.main_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(MainViewModel.class);
        // TODO: Use the ViewModel
        myListView = this.getView().findViewById(R.id.users_list);
        adapter = new UserAdapter(getContext(), R.layout.item_layout, Skeleton.getInstance().getUsers());
        myListView.setAdapter(adapter);

        UsersSQLiteHelper usersDB = new UsersSQLiteHelper(getContext(), "DBUsers", null, 1);
        SQLiteDatabase db = usersDB.getWritableDatabase();
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
            adapter.notifyDataSetChanged();
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
                    for (int i=0; i<response.length();i++) {
                        Skeleton.getInstance().addUser(new User(response.getJSONObject(i)));
                    }
                    adapter.notifyDataSetChanged();
                    //Log.e("App", "Success: " + response.getString("yourJsonElement") );
                } catch (JSONException ex) {
                    Log.e("App", "Failure", ex);
                }
            }
        }
    }
}