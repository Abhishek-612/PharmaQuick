package com.example.pharmaquick.ui.dashboard;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pharmaquick.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class DashboardFragment extends Fragment {


    private TextView name,Text, email, contact, address;
    EditText addrEdit;
    Button addR;
    private DatabaseReference myRef;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_dashboard, container, false);

        name = (TextView) root.findViewById(R.id.my_name);
        Text = (TextView) root.findViewById(R.id.m_name);
        email = (TextView) root.findViewById(R.id.email);
        contact = (TextView) root.findViewById(R.id.contact);
        address = (TextView) root.findViewById(R.id.address);
        addR = (Button) root.findViewById(R.id.addr_btn);
        addrEdit = (EditText) root.findViewById(R.id.addr_edit);

        myRef = FirebaseDatabase.getInstance().getReference()
                .child("users");

        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot data:dataSnapshot.getChildren()){
                     if(data.child("email").getValue().equals(FirebaseAuth.getInstance().getCurrentUser()))
                         Text.setText(data.child("name").getValue().toString());
                         name.setText(data.child("name").getValue().toString());
                         email.setText(data.child("email").getValue().toString());
                         contact.setText(data.child("phone").getValue().toString());
                         address.setText(data.child("address").getValue().toString());
//                    Toast.makeText(getContext(), data.child("name").getValue().toString(), Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



        return root;
    }

    DatabaseReference ref;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        addR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String address_text=addrEdit.getText().toString();
//                Toast.makeText(getContext(), address_text, Toast.LENGTH_SHORT).show();
                if(!address_text.equals("")) {
                    ref = FirebaseDatabase.getInstance().getReference().child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
                    ref.child("address").setValue(address_text);
                    Toast.makeText(getContext(), "Added", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}