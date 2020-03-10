package com.example.pharmaquick;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.speech.tts.TextToSpeech;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CartFragment  extends BottomSheetDialogFragment {

    private View rootView;
    private TextView text;
    private Button clearCart,orderNow,upload;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private ImageView imageView;
    private SharedPreferences sharedPreferences;
    private HashMap<String,Integer> cartList = new HashMap<>();
    private DatabaseReference parentUserNode,currentOrder;
    private boolean prescriptiton=false;

    private Uri resultUri=null;
    private String imageBase64;


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
            if(!item.equals("prescription"))
                cartList.put(item,sharedPreferences.getInt(item,0));
        }
        text = (TextView)rootView.findViewById(R.id.bottom_sheet_heading);
        clearCart = (Button) rootView.findViewById(R.id.clear_cart);
        orderNow = (Button) rootView.findViewById(R.id.order);
        upload = (Button) rootView.findViewById(R.id.upload_prescription);
        imageView=(ImageView)rootView.findViewById(R.id.prescription);

        prescriptiton=sharedPreferences.getBoolean("prescription",false);

        recyclerView = (RecyclerView) rootView.findViewById(R.id.cart_recycler);
        recyclerView.setHasFixedSize(false);
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        currentOrder = FirebaseDatabase
                .getInstance()
                .getReference("Orders").push();

        parentUserNode = FirebaseDatabase
                .getInstance()
                .getReference("users")
                .child(FirebaseAuth.getInstance().getUid());


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
    public void onResume() {
        super.onResume();

        if(prescriptiton){
            orderNow.setVisibility(View.GONE);
            upload.setVisibility(View.VISIBLE);
        }
        else{
            orderNow.setVisibility(View.VISIBLE);
            upload.setVisibility(View.GONE);
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        orderNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(cartList.isEmpty())
                    Toast.makeText(getContext(), "Empty Cart", Toast.LENGTH_SHORT).show();
                else {
                    Toast.makeText(getContext(), "Proceed to payment", Toast.LENGTH_SHORT).show();
                    String key  = currentOrder.getKey();
                    parentUserNode.child("currentOrder").setValue(key);
                    currentOrder.child("items").setValue(cartList);
                    currentOrder.child("delivered").setValue(false);
                    currentOrder.child("userid").setValue(FirebaseAuth.getInstance().getCurrentUser().getUid());
                    parentUserNode.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            currentOrder.child("user_address").setValue(dataSnapshot.child("address").getValue());
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                    if(imageView!=null)
                        currentOrder.child("prescription").setValue(imageBase64);


                }
                dismiss();
                startActivity(new Intent(getContext(), MapsActivity.class));
            }
        });

        clearCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!cartList.isEmpty()) {
//                    DatabaseReference thisRef = parentUserNode.child("pastOrders").push();
//                    thisRef.child("items").setValue(cartList);
//                    thisRef.child("delivered").setValue(true);
//                    thisRef.child("timestamp").setValue(ServerValue.TIMESTAMP);
//                    parentUserNode.child("currentOrder").removeValue();
                    //TODO:Remove above later
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.clear();
                    editor.apply();

                    cartList.clear();
                    mAdapter = new CartAdapter(cartList);
                    recyclerView.setAdapter(mAdapter);
                }
                dismiss();
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
                prescriptiton=false;
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
