package com.example.pharmaquick;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashMap;

public class CartAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private RecyclerViewClickListener mListener;
    private HashMap<String, Integer> cartList = new HashMap<>();
    TextView name,count;

    public static class CartViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        // each data item is just a stringin this case
        private RecyclerViewClickListener mListener;

        public CartViewHolder(View v, RecyclerViewClickListener listener) {
            super(v);
            mListener = listener;
            v.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            mListener.onClick(view, getAdapterPosition());
        }

    }


    public CartAdapter(HashMap<String, Integer> cartList, RecyclerViewClickListener mListener) {
        this.cartList = cartList;
        this.mListener = mListener;
    }

    public CartAdapter(HashMap<String, Integer> cartList) {
        this.cartList = cartList;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cart_list, parent, false);

        name = (TextView)v.findViewById(R.id.name);
        count = (TextView)v.findViewById(R.id.count);

        CartAdapter.CartViewHolder vh = new CartAdapter.CartViewHolder(v, mListener);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof CartAdapter.CartViewHolder) {
            CartAdapter.CartViewHolder viewHolder = (CartAdapter.CartViewHolder) holder;
        }
        String key = cartList.keySet().toArray()[position].toString();
        name.setText(key);
        count.setText(String.valueOf(cartList.get(key)));
    }

    @Override
    public int getItemCount () {
        return cartList.size();
    }

}
