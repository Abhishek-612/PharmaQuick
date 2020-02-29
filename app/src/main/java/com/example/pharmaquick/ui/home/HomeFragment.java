package com.example.pharmaquick.ui.home;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pharmaquick.MapsActivity;
import com.example.pharmaquick.MedAdapter;
import com.example.pharmaquick.R;
import com.example.pharmaquick.RecyclerViewClickListener;

public class HomeFragment extends Fragment {

    private Button button;
    private AutoCompleteTextView autoCompleteTextView;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;

    private String[] myDataset = {"List 1","List 2","List 3","List 4","List 5","List 6","List 7","List 8","List 9","List 10"};

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        button = root.findViewById(R.id.button);
        autoCompleteTextView = (AutoCompleteTextView)root.findViewById(R.id.med_find);
        recyclerView = (RecyclerView) root.findViewById(R.id.med_list);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        autoCompleteTextView.setThreshold(1);
        autoCompleteTextView.setAdapter(getEmailAddressAdapter(getContext()));
        // specify an adapter (see also next example)
        RecyclerViewClickListener listener = new RecyclerViewClickListener() {
            @Override
            public void onClick(View view, int position) {
                Toast.makeText(getContext(), myDataset[position], Toast.LENGTH_SHORT).show();
            }
        };
        mAdapter = new MedAdapter(myDataset,listener);
        recyclerView.setAdapter(mAdapter);

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), MapsActivity.class));
            }
        });


    }

    private ArrayAdapter<String> getEmailAddressAdapter(Context context) {
        return new ArrayAdapter<String>(context, android.R.layout.simple_dropdown_item_1line, myDataset);
    }

}