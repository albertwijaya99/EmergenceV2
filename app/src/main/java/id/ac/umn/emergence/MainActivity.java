package id.ac.umn.emergence;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {
    private CoordinatorLayout coordinatorLayout;

    private EditText etEmail, etPassword;
    private TextView tvRegister, tvForgotPassword;
    private Button btnLogin;

    private FirebaseAuth mAuth;
    private ProgressDialog progressDialog;

    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("177013");

        coordinatorLayout = findViewById(R.id.coordinatorLayout);
        Intent intent = getIntent();
        String verifyEmail = intent.getStringExtra("verifyEmail");
        if(verifyEmail != null){
            if(verifyEmail.contains("verifyEmail")){
                Snackbar snackbar = Snackbar.make(coordinatorLayout, "Please verify your email address", Snackbar.LENGTH_SHORT);
                View sbView = snackbar.getView();
                sbView.setBackgroundColor(Color.parseColor("#FDA89F"));
                snackbar.show();
            }
        }


        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);

        tvRegister = findViewById(R.id.tvRegister);
        String register = "Register ";
        SpannableString contentRegister = new SpannableString(register);
        contentRegister.setSpan(new UnderlineSpan(), 0, 8, 0);
        tvRegister.setText(contentRegister);

        tvForgotPassword = findViewById(R.id.tvForgotPassword);
        String forgotPassword = "/ Forgot Password";
        SpannableString contentForgotPassword = new SpannableString(forgotPassword);
        contentForgotPassword.setSpan(new UnderlineSpan(), 1, forgotPassword.length(), 0);
        tvForgotPassword.setText(contentForgotPassword);

        mAuth = FirebaseAuth.getInstance();

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginUser();
            }
        });

        tvRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

        tvForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ForgotPasswordActivity.class);
                startActivity(intent);
            }
        });
    }

    private boolean isEmailValid(CharSequence email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private void loginUser(){
        boolean valid=true;

        String email = etEmail.getText().toString();
        if (email.isEmpty()){
            etEmail.setError("Email cannot be empty!");
            valid=false;
        }else if (!isEmailValid(email)){
            etEmail.setError("Email is invalid");
            valid=false;
        }

        String password = etPassword.getText().toString();
        if (password.isEmpty()){
            etPassword.setError("Password cannot be empty");
            valid=false;
        }

        if(valid){
            progressDialog = new ProgressDialog(MainActivity.this);
            progressDialog.setMessage("Signing in");
            progressDialog.show();

            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                if(mAuth.getCurrentUser().isEmailVerified()){
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
                                                            Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                                                            intent.putExtra("user", user);
                                                            startActivity(intent);
                                                        }
                                                    }
                                                }
                                            }

                                            @Override
                                            public void onCancelled(DatabaseError databaseError) {
                                                Snackbar snackbar = Snackbar.make(coordinatorLayout, "Something went wrong, please try again", Snackbar.LENGTH_SHORT);
                                                View sbView = snackbar.getView();
                                                sbView.setBackgroundColor(Color.parseColor("#FDA89F"));
                                                snackbar.show();
                                            }
                                        });
                                    }
                                } else{
                                    Snackbar snackbar = Snackbar.make(coordinatorLayout, "Please verify your email address", Snackbar.LENGTH_SHORT);
                                    View sbView = snackbar.getView();
                                    sbView.setBackgroundColor(Color.parseColor("#FDA89F"));
                                    snackbar.show();
                                }
                            }
                            else {
                                if(isOnline()){
                                    Snackbar snackbar = Snackbar.make(coordinatorLayout, "Wrong email or password", Snackbar.LENGTH_SHORT);
                                    View sbView = snackbar.getView();
                                    sbView.setBackgroundColor(Color.parseColor("#FDA89F"));
                                    snackbar.show();
                                }
                                else{
                                    Snackbar snackbar = Snackbar.make(coordinatorLayout, "Check your internet connection", Snackbar.LENGTH_SHORT);
                                    View sbView = snackbar.getView();
                                    sbView.setBackgroundColor(Color.parseColor("#FDA89F"));
                                    snackbar.show();
                                }
                            }
                            progressDialog.dismiss();

                        }
                    });
        }
    }

    public boolean isOnline() {
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }

}
