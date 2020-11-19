package com.project.doctorinsta.patient_ui.maps;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.afollestad.materialdialogs.MaterialDialog;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
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
import com.project.doctorinsta.data.Patient;
import com.project.doctorinsta.data.Specialisation;
import com.project.doctorinsta.patient_ui.booking.BookActivity;
import com.project.doctorinsta.patient_ui.specialisation.GridSpacingItemDecoration;
import com.project.doctorinsta.utils.SharedPrefs;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;
import static com.project.doctorinsta.utils.SharedPrefs.sharedPrefs;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback,GoogleMap.OnMarkerClickListener{

    private GoogleMap mMap;
    private SharedPrefs sharedPrefs;
    private Patient patient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        sharedPrefs = SharedPrefs.getInstance(this);
        patient =sharedPrefs.getPatient("loggedInPatient");
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * <p>
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        List<Doctor> doctors = sharedPrefs.getAllDoctors("all-docs");
        //List<Doctor> doctors = getDummyDocs();
        for (Doctor doctor : doctors) {
            Log.d("doctor: ",doctor.toString());
            Geocoder coder = new Geocoder(MapsActivity.this);
            try {
                String docAddress = doctor.getAddress() + " " + doctor.getCity() + " "+ doctor.getCountry();
                Log.d("db addr", docAddress);
                ArrayList<Address> adresses = (ArrayList<Address>) coder.getFromLocationName(docAddress, 5);
                for (Address add : adresses) {
                    Log.d("map addr, country: ", add.getCountryName());
                    if (add.getCountryName().startsWith(patient.getCountry().substring(0,3))) {//Controls to ensure it is right address such as country etc.
                        double latitude = add.getLatitude();
                        double longitude = add.getLongitude();
                        LatLng loc = new LatLng(latitude, longitude);
                     Marker mItem =   mMap.addMarker(new MarkerOptions()
                                .position(loc)
                                .title(getSpecialityById(doctor.getSpecialtyIdNumber()) + "," +
                                        doctor.getAddress()));
                     mItem.setTag(doctor);
                        mMap.moveCamera(CameraUpdateFactory.newLatLng(loc));
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    /** Called when the user clicks a marker. */
    @Override
    public boolean onMarkerClick(final Marker marker) {
        // Retrieve the data from the marker.
        Doctor doc = (Doctor) marker.getTag();
        sharedPrefs.setDoctor("selectedDoctor", doc);
        Intent intent = new Intent(MapsActivity.this, BookActivity.class);
        intent.putExtra("fragmentName", "Make Booking");
        startActivity(intent);
        // Return false to indicate that we have not consumed the event and that we wish
        // for the default behavior to occur (which is for the camera to move such that the
        // marker is centered and for the marker's info window to open, if it has one).
        return false;
    }

    private String getSpecialityById(Long specialityIdNumber) {
        for (Specialisation sp : sharedPrefs.getSpecialities("specialities")) {
            if (sp.getNumber().equals(specialityIdNumber)) {
                return sp.getName();
            }
        }
        return "unknown";
    }

    private List<Doctor> getDummyDocs(){
        List<Doctor>  doctors = new ArrayList<>();
        Doctor doctor = new Doctor();
        doctor.setCountry("Zimbabwe");
        doctor.setCity("Harare");
        doctor.setSpecialtyIdNumber((long) 1);
        doctor.setAddress("122 Lomagundi Road, Emerald Hill");

        Doctor doctor2 = new Doctor();
        doctor2.setSpecialtyIdNumber((long) 2);
        doctor2.setCountry("Zimbabwe");
        doctor2.setCity("Bulawayo");
        doctor2.setAddress("4781 Magwegwe West");

        Doctor doctor3 = new Doctor();
        doctor3.setCountry("United States");
        doctor3.setSpecialtyIdNumber((long) 3);
        doctor3.setCity("maryville");
        doctor3.setAddress("1121 N College drive");

        Doctor doctor4 = new Doctor();
        doctor4.setSpecialtyIdNumber((long) 4);
        doctor4.setCountry("United States");
        doctor4.setCity("Missouri");
        doctor4.setAddress("1121 North College Drive");

        doctors.add(doctor);
        doctors.add(doctor2);
        doctors.add(doctor3);
        doctors.add(doctor4);
        return doctors;
    }
}