package com.example.pharmaquick;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;

public class CartFragment  extends BottomSheetDialogFragment {

    private View rootView;
    private TextView text;
    private Button clearCart,orderNow;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private SharedPreferences sharedPreferences;
    private HashMap<String,Integer> cartList = new HashMap<>();

    public static CartFragment newInstance() {
        return new CartFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.cart_layout, container,
                false);
        sharedPreferences = getActivity().getSharedPreferences("MyCart", Context.MODE_PRIVATE);
        for(String item : sharedPreferences.getAll().keySet()){
            cartList.put(item,sharedPreferences.getInt(item,0));
        }
        text = (TextView)rootView.findViewById(R.id.bottom_sheet_heading);
        clearCart = (Button) rootView.findViewById(R.id.clear_cart);
        orderNow = (Button) rootView.findViewById(R.id.order);


        recyclerView = (RecyclerView) rootView.findViewById(R.id.cart_recycler);
        recyclerView.setHasFixedSize(false);
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        RecyclerViewClickListener listener = new RecyclerViewClickListener() {
            @Override
            public void onClick(View view, int position) {

            }
        };

        mAdapter = new CartAdapter(cartList,listener);
        recyclerView.setAdapter(mAdapter);
        return rootView;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        orderNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(), "Proceed to payment", Toast.LENGTH_SHORT).show();
            }
        });

        clearCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.clear();
                editor.apply();

                cartList.clear();
                mAdapter = new CartAdapter(cartList);
                recyclerView.setAdapter(mAdapter);
            }
        });
    }


}