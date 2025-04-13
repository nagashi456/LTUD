package org.o7planning.a48k141_03_duan_quanlyhoctap;

import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.Firebase;
import com.google.firebase.auth.FirebaseAuth;


public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();

        // Lấy BottomNavigationView từ layout
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);

        // Lấy NavController từ NavHostFragment
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager()
                .findFragmentById(R.id.nav_host_fragment);
        NavController navController = navHostFragment.getNavController();

        // Kết nối BottomNavigationView với NavController
        NavigationUI.setupWithNavController(bottomNavigationView, navController);

        // Lắng nghe khi destination thay đổi -> ẩn/hiện bottom nav
        navController.addOnDestinationChangedListener((controller, destination, arguments) -> {
            if (destination.getId() == R.id.loginFragment
                    || destination.getId() == R.id.registerFragment
                    || destination.getId() == R.id.forgetPasswordFragment
                    || destination.getId() == R.id.letStartFragment
                    || destination.getId() == R.id.otpVerificationFragment
                    || destination.getId() == R.id.registerFragment
                    || destination.getId() == R.id.setNewPasswordFragment
                    || destination.getId() == R.id.signUpFragment) {
                bottomNavigationView.setVisibility(View.GONE);
            } else {
                bottomNavigationView.setVisibility(View.VISIBLE);
            }
        });
    }
}
