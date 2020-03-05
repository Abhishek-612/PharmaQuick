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
    private Button upload;
//    private int RESULT_OK = 1001;
    private View root;
    private Uri resultUri=null;
    private ImageView imageView;
    private String imageBase64;
    CropImageView cropImageView;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_notifications, container, false);
        button = root.findViewById(R.id.cart_button);
        upload = root.findViewById(R.id.upload);
        cropImageView = (CropImageView) root.findViewById(R.id.cropImageView);
        imageView=root.findViewById(R.id.image);
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

                FirebaseDatabase.getInstance()
                        .getReference("users").child(FirebaseAuth.getInstance().getUid())
                        .child("currentOrder/prescription").setValue(imageBase64);


            }
        });

        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadImage();
            }
        });
    }

    private void uploadImage(){
            CropImage.activity()
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .start(getContext(), this);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == Activity.RESULT_OK) {
                resultUri = result.getUri();
                imageView.setImageURI(resultUri);

                try {
                    imageBase64=convertBitmapToString(BitmapFactory
                            .decodeStream(getActivity().getContentResolver()
                                    .openInputStream(resultUri)));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }

    public static String convertBitmapToString(Bitmap bitmap){
        String encodedImage = "";

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        try {
            encodedImage= URLEncoder.encode(Base64.encodeToString(byteArrayOutputStream.toByteArray(), Base64.DEFAULT), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return encodedImage;
    }

}