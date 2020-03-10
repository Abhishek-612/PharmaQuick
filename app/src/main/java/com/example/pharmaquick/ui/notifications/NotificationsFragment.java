package com.example.pharmaquick.ui.notifications;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.pharmaquick.CartFragment;
import com.example.pharmaquick.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class NotificationsFragment extends Fragment {


    private Button button;
//    private int RESULT_OK = 1001;
    private View root;
    CropImageView cropImageView;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_notifications, container, false);
        button = root.findViewById(R.id.cart_button);
        cropImageView = (CropImageView) root.findViewById(R.id.cropImageView);
//        recyclerView = (RecyclerView) root.findViewById(R.id.cart_list);
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("MyCart", Context.MODE_PRIVATE);

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