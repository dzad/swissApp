package com.swiss4ward.swissapp.ui.main;

import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.swiss4ward.swissapp.MainActivity;
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

    public interface OnItemSelectedListener{
        public void onItemSelectedListener(int position);
    }

    public void onUserAdded() {
        Log.e("fragment" , "useradded!");
        if(adapter!=null){
            adapter.notifyDataSetChanged();
        }
    }

    private MainViewModel mViewModel;
    public ListView myListView;
    public UserAdapter adapter;
    public OnItemSelectedListener mCallback;

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
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try{
            mCallback = (OnItemSelectedListener) context;
        }catch (ClassCastException cce){
            throw new ClassCastException(context.toString() + " must implement onItemSelectedListener");
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(MainViewModel.class);
        // TODO: Use the ViewModel
        myListView = this.getView().findViewById(R.id.users_list);
        adapter = new UserAdapter(getContext(), R.layout.item_layout, Skeleton.getInstance().getUsers());
        myListView.setAdapter(adapter);
        myListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                mCallback.onItemSelectedListener(i);
            }
        });

    }

}