package com.leon.wheelchairmonitor;

import android.os.VibrationEffect;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    FirebaseDatabase database;
    DatabaseReference myRef;

    Button mVerifyButton;

    ImageView mImageView;

    boolean mIsInDangerous = false;

    Vibrator vibrator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("isInDangerous");

        mVerifyButton = findViewById(R.id.verifyButton);
        mImageView = findViewById(R.id.imageView);

        vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);

        readFromDataBase();

        configOnClick();
    }

    public void configOnClick(){
        mVerifyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                readFromDataBase();
            }
        });
    }
    public void readFromDataBase(){

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                mIsInDangerous = dataSnapshot.getValue(Boolean.class);
                if(mIsInDangerous){
                    Glide.with(getApplicationContext())
                            .load(R.drawable.alert)
                            .into(mImageView);
                    vibrator.vibrate(1000);
                }
                else{
                    Glide.with(getApplicationContext())
                            .load(R.drawable.safe)
                            .into(mImageView);
                }
                Log.d("Firebase_OnDataChange", "Value is: " + mIsInDangerous);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("Firebase_OnDataChange", "Failed to read value.", error.toException());
            }
        });
    }
}
