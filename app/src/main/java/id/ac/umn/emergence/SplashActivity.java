package id.ac.umn.emergence;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SplashActivity extends AppCompatActivity {
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        final FirebaseUser mUser = FirebaseAuth.getInstance().getCurrentUser();

        if(mUser != null) {
            // Get a reference to our posts
            final FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference ref = database.getReference("users");

            // Attach a listener to read the data at our posts reference
            ref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    String currentUserId = mUser.getUid().toString();
                    for (DataSnapshot ds:dataSnapshot.getChildren()){
                        if(ds != null){
                            String key = ds.getKey().toString();
                            if(currentUserId.equals(key)){
                                String name = ds.child("name").getValue().toString();
                                String phone = ds.child("phone").getValue().toString();
                                String address = ds.child("address").getValue().toString();
                                String gender = ds.child("gender").getValue().toString();
                                String blood = ds.child("blood").getValue().toString();
                                String email = ds.child("email").getValue().toString();

                                user = new User(name, phone, address, gender, blood, email);
                                Intent intent = new Intent(SplashActivity.this, HomeActivity.class);
                                intent.putExtra("user", user);
                                startActivity(intent);
                            }
                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            startActivity(new Intent(SplashActivity.this,MainActivity.class));
                        }
                    },2000);
                }
            });
        }
        else{
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    startActivity(new Intent(SplashActivity.this,MainActivity.class));
                }
            },2000);
        }
    }
}
