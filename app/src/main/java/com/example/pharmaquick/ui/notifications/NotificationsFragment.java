package com.example.pharmaquick.ui.notifications;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.pharmaquick.CartFragment;
import com.example.pharmaquick.R;

public class NotificationsFragment extends Fragment {


    private Button button;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_notifications, container, false);
        button = root.findViewById(R.id.cart_button);
//        recyclerView = (RecyclerView) root.findViewById(R.id.cart_list);
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("MyCart", Context.MODE_PRIVATE);
        for(String x : sharedPreferences.getAll().keySet().toArray(new String[0])){
            Toast.makeText(getContext(), x, Toast.LENGTH_SHORT).show();
        }
//        layoutManager = new LinearLayoutManager(getContext());
//        recyclerView.setLayoutManager(layoutManager);
//
//        RecyclerViewClickListener listener = new RecyclerViewClickListener() {
//            @Override
//            public void onClick(View view, int position) {
//                Toast.makeText(getContext(), "Remove "+position, Toast.LENGTH_SHORT).show();
//        }
//    };

//        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("MyCart", Context.MODE_PRIVATE);
//        for(String x : sharedPreferences.getAll().keySet().toArray(new String[0])){
//            Toast.makeText(getContext(), x, Toast.LENGTH_SHORT).show();
//        }
//        mAdapter = new CartAdapter(myCart,listener);
//        recyclerView.setAdapter(mAdapter);

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CartFragment cartFragment =
                        CartFragment.newInstance();
                cartFragment.show(((AppCompatActivity)getActivity())
                        .getSupportFragmentManager(),"cart");
//                Toast.makeText(getContext(), "Cart", Toast.LENGTH_SHORT).show();
            }
        });
    }
}