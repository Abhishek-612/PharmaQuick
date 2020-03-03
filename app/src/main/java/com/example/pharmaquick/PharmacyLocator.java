package com.example.pharmaquick;

import android.app.Activity;
import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.common.api.GoogleApi;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Executor;

public class PharmacyLocator {

    private Geocoder geocoder;
    private float distance;
    private Location location;
    private ArrayList<PharmacyStore> pharmacyStores;
    Context co;

    public PharmacyLocator(Context c,Location location) {
        geocoder = new Geocoder(c);
        co=c;
        this.location = location;
        pharmacyStores = new ArrayList<>();

        getData();
//        pharmacyStores.add(new PharmacyStore("CritiCare Hospital","Plot No 516, Besides SBI, Telli Galli, Teli Gali, Andheri East, Mumbai, Maharashtra 400069"));
//        pharmacyStores.add(new PharmacyStore("BSES MG Hospital","SV Rd, Lohana Colony, Andheri West, Mumbai, Maharashtra 400058"));
//        pharmacyStores.add(new PharmacyStore("SevenHills Hospital","Marol Maroshi Rd, Shivaji Nagar JJC, Marol, Andheri East, Mumbai, Maharashtra 400069"));


    }

    private void getData(){
        final DatabaseReference storeRef = FirebaseDatabase.getInstance().getReference().child("pharmacyStores");

        storeRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot data:dataSnapshot.getChildren()){
                    PharmacyStore store = new PharmacyStore(data.child("name").getValue().toString(),data.child("address").getValue().toString());
                    getCoordinates(store);
                    Log.d("firebase",store.getName());
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void getCoordinates(PharmacyStore store) {
        String address = store.getAddress();
        List<Address> addr= null;
        try {
            addr = geocoder.getFromLocationName(address,1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Location l = new Location(LocationManager.GPS_PROVIDER);
        store.setLatitude(addr.get(0).getLatitude());
        store.setLongitude(addr.get(0).getLongitude());
        l.setLatitude(store.getLatitude());
        l.setLongitude(store.getLongitude());
        distance = getDistance(l);
        store.setDistance(distance);
        System.out.println("hey "+distance);
        if(distance<=2000)
            pharmacyStores.add(store);
        System.out.println("hey1 "+pharmacyStores.toString());
        System.out.println("GEOCODER: "+addr.get(0).getLatitude()+","+addr.get(0).getLongitude());
    }

    //TODO: Try distance using Directions API
    private float getDistance(Location dest) {
        return location.distanceTo(dest);
    }


    public ArrayList<PharmacyStore> getPharmacyStores(){
        return pharmacyStores;
    }

}
