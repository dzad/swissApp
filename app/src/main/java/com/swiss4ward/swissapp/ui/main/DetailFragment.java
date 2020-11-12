package com.swiss4ward.swissapp.ui.main;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.swiss4ward.swissapp.R;
import com.swiss4ward.swissapp.Skeleton;
import com.swiss4ward.swissapp.models.User;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DetailFragment extends Fragment {

    private static final String ARG_POSITION = "Position";

    private int position;

    TextView name,email,phone,address,website,company;

    public DetailFragment() {
        // Required empty public constructor
    }

    public static DetailFragment newInstance(int pos) {
        DetailFragment fragment = new DetailFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_POSITION, pos);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            this.position = getArguments().getInt(ARG_POSITION);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_detail, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        name = this.getView().findViewById(R.id.detail_name);
        email = this.getView().findViewById(R.id.email);
        phone = this.getView().findViewById(R.id.phone);
        address = this.getView().findViewById(R.id.address);
        website = this.getView().findViewById(R.id.website);
        company = this.getView().findViewById(R.id.company);

        if(this.getArguments() != null){
            setDetailItem(this.position);
        }
    }

    public void setDetailItem(int position){
        Toast.makeText(getContext(), "llllllll", Toast.LENGTH_SHORT).show();
        User user = Skeleton.getInstance().getUsers().get(position);
        name.setText(user.getName());
        email.setText(user.getEmail());
        phone.setText(user.getPhone());
        address.setText(user.getAddress().toString());
        website.setText(user.getWebsite());
        company.setText(user.getCompany().getName());

        phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel: "+user.getPhone()));
                if(intent.resolveActivity(getActivity().getPackageManager()) != null){
                    startActivity(intent);
                }
            }
        });

        website.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www."+user.getWebsite()));
                if(intent.resolveActivity(getActivity().getPackageManager()) != null){
                    startActivity(intent);
                }
            }
        });
    }
}