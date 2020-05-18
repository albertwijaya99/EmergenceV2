package id.ac.umn.emergence;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
public class HomeActivity extends AppCompatActivity {

    private CoordinatorLayout coordinatorLayout;
    private TextView tvPhone, tvEmail, tvGender, tvBlood, tvAddress;

    private DatabaseReference mDatabase;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Intent intent = getIntent();
        user = intent.getParcelableExtra("user");
        setTitle(user.getName());

        tvPhone = findViewById(R.id.tvPhone);
        tvPhone.setText(user.getPhone());

        tvEmail = findViewById(R.id.tvEmail);
        tvEmail.setText(user.getEmail());

        tvGender = findViewById(R.id.tvGender);
        tvGender.setText(user.getGender());

        tvBlood = findViewById(R.id.tvBlood);
        tvBlood.setText(user.getBlood());

        tvAddress = findViewById(R.id.tvAddress);
        tvAddress.setText(user.getAddress());
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.app_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(HomeActivity.this, MainActivity.class);
        startActivity(intent);
        return true;
    }
}
