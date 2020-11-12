package com.swiss4ward.swissapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.swiss4ward.swissapp.R;
import com.swiss4ward.swissapp.models.User;

import java.util.List;

public class UserAdapter extends ArrayAdapter<User> {
    public UserAdapter(Context context, int resource, List<User> users) {
        super(context, resource, users);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView==null){
            convertView = LayoutInflater.from(this.getContext())
                    .inflate(R.layout.item_layout,parent,false);
        }
        TextView userId = convertView.findViewById(R.id.user_id);
        TextView name = convertView.findViewById(R.id.name);
        TextView username = convertView.findViewById(R.id.username);

        User user = getItem(position);
        userId.setText(String.valueOf(user.getId()));
        name.setText(user.getName());
        username.setText(user.getUsername());

        return convertView;
    }


}
