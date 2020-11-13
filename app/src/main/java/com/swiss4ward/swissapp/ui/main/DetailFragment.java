package com.swiss4ward.swissapp.ui.main;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.swiss4ward.swissapp.R;
import com.swiss4ward.swissapp.Skeleton;
import com.swiss4ward.swissapp.models.User;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DetailFragment extends Fragment implements OnMapReadyCallback {

    private static final String ARG_POSITION = "Position";

    MapView mapView;
    GoogleMap map;
    User user;
    TextView name, email, phone, address, website, company;
    private int position;

    public DetailFragment() {
        // Required empty public constructor
        user = new User();
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
    public void onMapReady(GoogleMap googleMap) {
        googleMap.getUiSettings().setMyLocationButtonEnabled(false);
        if (!user.getAddress().getLat().isEmpty()) {
            LatLng location = new LatLng(Float.valueOf(user.getAddress().getLat()), Float.valueOf(user.getAddress().getLng()));
            googleMap.addMarker(new MarkerOptions().position(location)
                    .title(user.getAddress().toString()));
            googleMap.moveCamera(CameraUpdateFactory.newLatLng(location));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_detail, container, false);
        mapView = (MapView) v.findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);

        try {
            MapsInitializer.initialize(this.getActivity());
        } catch (Exception e) {
            e.printStackTrace();
        }


        return v;
    }

    @Override
    public void onResume() {
        mapView.onResume();
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
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


        MapView mapView = getView()
                .findViewById(R.id.mapView);
        mapView.getMapAsync(this);

        if (this.getArguments() != null) {
            setDetailItem(this.position);
        }
    }

    public void setDetailItem(int position) {
        Toast.makeText(getContext(), "llllllll", Toast.LENGTH_SHORT).show();
        this.user = Skeleton.getInstance().getUsers().get(position);
        name.setText(user.getName());
        email.setText(user.getEmail());
        phone.setText(user.getPhone());
        address.setText(user.getAddress().toString());
        website.setText(user.getWebsite());
        company.setText(user.getCompany().getName());

        phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel: " + user.getPhone()));
                if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
                    startActivity(intent);
                }
            }
        });

        website.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www." + user.getWebsite()));
                if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
                    startActivity(intent);
                }
            }
        });
    }
}