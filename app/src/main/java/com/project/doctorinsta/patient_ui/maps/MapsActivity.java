package com.project.doctorinsta.patient_ui.maps;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.afollestad.materialdialogs.MaterialDialog;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.project.doctorinsta.R;
import com.project.doctorinsta.adapter.DoctorAdapter;
import com.project.doctorinsta.data.Doctor;
import com.project.doctorinsta.data.Specialisation;
import com.project.doctorinsta.patient_ui.specialisation.GridSpacingItemDecoration;
import com.project.doctorinsta.utils.SharedPrefs;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;
import static com.project.doctorinsta.utils.SharedPrefs.sharedPrefs;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private MaterialDialog materialDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SharedPrefs sharedPrefs = SharedPrefs.getInstance(this);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     *
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        List<Doctor>  doctors = new ArrayList<>();
        DatabaseReference dbRef= FirebaseDatabase.getInstance().getReference("doctors");
        Query specList = dbRef.orderByChild("phone");
        specList.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NotNull DataSnapshot dataSnapshot) {
                Log.d("found doctors",":"+dataSnapshot.getChildrenCount());
                for (DataSnapshot snapshot: dataSnapshot.getChildren()) {
                        Log.d("found doctor",":"+snapshot.toString());
                        String firstname =snapshot.child("firstname").getValue(String.class);
                        String lastname =snapshot.child("lastname").getValue(String.class);
                        Long idNumber =snapshot.child("idNumber").getValue(Long.class);
                        Long specialtyIdNumber =snapshot.child("specialtyIdNumber").getValue(Long.class);
                        String rate =snapshot.child("rate").getValue(String.class);
                        String experience =snapshot.child("experience").getValue(String.class);
                        String country =snapshot.child("country").getValue(String.class);
                        if(country!=null && country.contains("(")){
                            country= country.substring(0,country.indexOf("("));
                        }
                        String city =snapshot.child("city").getValue(String.class);
                        String address =snapshot.child("address").getValue(String.class);
                        if(address!=null && address.contains(",")){
                           address.replace(","," ");
                        }
                        final Doctor doctor  = new Doctor( experience, idNumber, specialtyIdNumber,  rate,  firstname,  lastname,  "phone",
                                "email",  "password",  country,  city,  address);
                        doctors.add(doctor);
                }
                for(Doctor doctor: doctors){
                    Geocoder coder = new Geocoder(MapsActivity.this);
                    try {
                        String docAddress = doctor.getAddress()+", "+doctor.getCity();
                        ArrayList<Address> adresses = (ArrayList<Address>) coder.getFromLocationName(docAddress, 50);
                        for(Address add : adresses){
                            if (add.getCountryName().equalsIgnoreCase(doctor.getCountry())) {//Controls to ensure it is right address such as country etc.
                                double latitude = add.getLatitude();
                                double longitude = add.getLongitude();
                                LatLng loc = new LatLng(latitude, longitude);
                                mMap.addMarker(new MarkerOptions()
                                        .position(loc)
                                        .title(getSpecialityById(doctor.getSpecialtyIdNumber())+ ","+doctor.getCity()));
                                mMap.moveCamera(CameraUpdateFactory.newLatLng(loc));
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w(TAG, "load doctors:onCancelled", databaseError.toException());
                // ...
            }
        });
    }

    private String getSpecialityById(Long specialityIdNumber) {
        for(Specialisation sp:  sharedPrefs.getSpecialities("specialities")){
            if(sp.getNumber().equals(specialityIdNumber)){
                return sp.getName();
            }
        }
        return "unknown";
    }
}