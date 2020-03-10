package com.example.pharmaquick.ui.home;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pharmaquick.MapsActivity;
import com.example.pharmaquick.MedAdapter;
import com.example.pharmaquick.PharmacyStore;
import com.example.pharmaquick.R;
import com.example.pharmaquick.RecyclerViewClickListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class HomeFragment extends Fragment {


    SharedPreferences sharedpreferences;
    public static final String MyPREFERENCES = "MyCart" ;
    public static final String Count = "countKey";


    private AutoCompleteTextView autoCompleteTextView;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private DatabaseReference medRef;
    private ArrayList<String> medDataset;
    private ProgressDialog progress;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        medDataset=new ArrayList<>();

        medRef = FirebaseDatabase.getInstance().getReference()
                .child("medicines");

        medRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot data:dataSnapshot.getChildren()){
                    medDataset.add(data.getKey());
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



        progress = new ProgressDialog(getContext());
        progress.setTitle("Connecting");
        progress.setMessage("Please wait while we connect to devices...");
        progress.show();
        final Timer t = new Timer();
        t.schedule(new TimerTask() {
            public void run() {
                progress.dismiss(); // when the task active then close the dialog
                t.cancel(); // also just top the timer thread, otherwise, you may receive a crash report
            }
        }, 2000);


        autoCompleteTextView = (AutoCompleteTextView)root.findViewById(R.id.med_find);
        recyclerView = (RecyclerView) root.findViewById(R.id.med_list);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
//        sharedpreferences =



        RecyclerViewClickListener listener = new RecyclerViewClickListener() {
            @Override
            public void onClick(View view, int position) {
                addToCart(position);
            }
        };




        autoCompleteTextView.setThreshold(1);
        autoCompleteTextView.setAdapter(getAutoCompleteAdapter(getContext()));

        mAdapter = new MedAdapter(medDataset,listener);
        recyclerView.setAdapter(mAdapter);


        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        sharedpreferences = getActivity().getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                addToCart(position);
            }
        });
    }

    private ArrayAdapter<String> getAutoCompleteAdapter(Context context) {
        return new ArrayAdapter<String>(context, android.R.layout.simple_dropdown_item_1line, medDataset);
    }



    private void addToCart(final int position){
        final boolean[] flag = new boolean[1];
        medRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                flag[0] =Boolean.parseBoolean(dataSnapshot.child(medDataset.get(position)).child("prescribe").getValue().toString());
                Toast.makeText(getContext(), flag[0]+"", Toast.LENGTH_SHORT).show();
                SharedPreferences.Editor editor = sharedpreferences.edit();
                if(flag[0]) {
                    Toast.makeText(getContext(), ": This item requires a prescription", Toast.LENGTH_SHORT).show();
                    editor.putInt(medDataset.get(position),sharedpreferences.getInt(medDataset.get(position),0)+1);
                    editor.putBoolean("prescription",true);
                    editor.apply();
                }
                else{
                Toast.makeText(getContext(), medDataset.get(position)+" added to cart", Toast.LENGTH_SHORT).show();
                editor.putInt(medDataset.get(position),sharedpreferences.getInt(medDataset.get(position),0)+1);
                editor.apply();}
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}