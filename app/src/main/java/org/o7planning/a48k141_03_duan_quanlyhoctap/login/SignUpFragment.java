package org.o7planning.a48k141_03_duan_quanlyhoctap.login;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;

import org.o7planning.a48k141_03_duan_quanlyhoctap.R;

public class SignUpFragment extends Fragment {

    private EditText fullNameEditText, passwordEditText, confirmPasswordEditText;
    private TextView confirmButton;
    private FirebaseAuth mAuth;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_sign_up, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Ánh xạ view
        fullNameEditText = view.findViewById(R.id.fullname_edit_text);
        passwordEditText = view.findViewById(R.id.password_edit_text);
        confirmPasswordEditText = view.findViewById(R.id.confirm_password_edit_text);
        confirmButton = view.findViewById(R.id.button_next_1);

        confirmButton.setOnClickListener(v -> handleRegister(view));
        mAuth = FirebaseAuth.getInstance();

    }

    private void handleRegister(View view) {
        String fullName = fullNameEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();
        String confirmPassword = confirmPasswordEditText.getText().toString().trim();

        if (fullName.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            Toast.makeText(getContext(), "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!password.equals(confirmPassword)) {
            Toast.makeText(getContext(), "Passwords do not match", Toast.LENGTH_SHORT).show();
            return;
        }

        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        String email = sharedPreferences.getString("email", "");

        if (email.isEmpty()) {
            Toast.makeText(getContext(), "Email not found. Please register again.", Toast.LENGTH_SHORT).show();
            return;
        }

        // ✅ Đăng ký với Firebase
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // Đăng ký thành công: lưu full name nếu cần
                        sharedPreferences.edit()
                                .putString("fullName", fullName)
                                .apply();

                        showCustomSnackbarForRegisterAndNavigate();
                    } else {
                        // Đăng ký thất bại
                        Toast.makeText(getContext(), "Đăng ký thất bại: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void showCustomSnackbarForRegisterAndNavigate() {
        LayoutInflater layoutInflater = LayoutInflater.from(requireContext());
        View layout = layoutInflater.inflate(R.layout.custom_snackbar_for_register, (ViewGroup) requireView(), false);

        Toast toast = new Toast(requireContext());
        toast.setView(layout);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, 0);
        toast.show();

        new android.os.Handler().postDelayed(() -> {
            NavHostFragment.findNavController(SignUpFragment.this)
                    .navigate(R.id.action_signUpFragment_to_loginFragment);
        }, 3500);
    }
}
