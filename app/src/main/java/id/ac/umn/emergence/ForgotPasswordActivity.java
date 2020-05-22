package id.ac.umn.emergence;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPasswordActivity extends AppCompatActivity {
    private CoordinatorLayout coordinatorLayout;

    private EditText etForgot;
    private Button btnSend;
    private ProgressDialog progressDialog;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        setTitle("Password Recovery");

        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorLayout);
        etForgot = findViewById(R.id.etForgot);
        btnSend = findViewById(R.id.btnSend);
        mAuth = FirebaseAuth.getInstance();

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean valid = true;
                String email = etForgot.getText().toString();
                if (email.isEmpty()){
                    etForgot.setError("Email cannot be empty");
                    valid = false;
                }else if (!isEmailValid(email)){
                    etForgot.setError("Email is invalid");
                    valid=false;
                }

                if(valid){
                    progressDialog = new ProgressDialog(ForgotPasswordActivity.this);
                    progressDialog.setMessage("Sending email");
                    progressDialog.show();
                    mAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                Snackbar snackbar = Snackbar.make(coordinatorLayout, "Email sent", Snackbar.LENGTH_SHORT);
                                View sbView = snackbar.getView();
                                sbView.setBackgroundColor(Color.parseColor("#FDA89F"));
                                snackbar.show();
                            } else {
                                Snackbar snackbar = Snackbar.make(coordinatorLayout, "Failed to send email", Snackbar.LENGTH_SHORT);
                                View sbView = snackbar.getView();
                                sbView.setBackgroundColor(Color.parseColor("#FDA89F"));
                                snackbar.show();
                            }
                            progressDialog.dismiss();
                        }
                    });
                }
            }
        });
    }

    private boolean isEmailValid(CharSequence email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
}
