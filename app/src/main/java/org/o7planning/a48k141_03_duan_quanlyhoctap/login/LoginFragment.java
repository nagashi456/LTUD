package org.o7planning.a48k141_03_duan_quanlyhoctap.login;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.text.Editable;
import android.text.TextWatcher;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import org.o7planning.a48k141_03_duan_quanlyhoctap.R;

public class LoginFragment extends Fragment {

    private EditText emailEditText;
    private EditText passwordEditText;
    private CheckBox rememberMeCheckBox;
    private ImageView passwordToggle;
    private ImageView emailStatusDot;

    private boolean passwordVisible = false;

    // Interface for login callbacks
    public interface LoginListener {
        void onLoginClicked(String email, String password, boolean rememberMe);
        void onForgotPasswordClicked();
        void onCreateAccountClicked();
    }

    private LoginListener loginListener;

    public void setLoginListener(LoginListener listener) {
        this.loginListener = listener;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        emailEditText = view.findViewById(R.id.email_edit_text);
        passwordEditText = view.findViewById(R.id.password_edit_text);
        rememberMeCheckBox = view.findViewById(R.id.remember_me_checkbox);
        passwordToggle = view.findViewById(R.id.password_toggle);
        emailStatusDot = view.findViewById(R.id.email_status_dot);
        TextView forgotPasswordText = view.findViewById(R.id.forgot_password_text);
        TextView signInButton = view.findViewById(R.id.sign_in_button);
        TextView createAccountText = view.findViewById(R.id.create_account_text);

        emailEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                String emailInput = s.toString().trim();
                if (emailInput.isEmpty()) {
                    emailStatusDot.setImageResource(R.drawable.gray_dot); // chưa nhập
                } else if (Patterns.EMAIL_ADDRESS.matcher(emailInput).matches()) {
                    emailStatusDot.setImageResource(R.drawable.green_dot); // đúng
                } else {
                    emailStatusDot.setImageResource(R.drawable.red_dot); // sai
                }
            }
        });


        // Toggle password visibility
        passwordToggle.setOnClickListener(v -> togglePasswordVisibility());

        // Sign in click
        signInButton.setOnClickListener(v -> {
            String email = emailEditText.getText().toString().trim();
            String password = passwordEditText.getText().toString().trim();
            boolean rememberMe = rememberMeCheckBox.isChecked();

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(getContext(), "Please enter both email and password.", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                Toast.makeText(getContext(), "Invalid email format.", Toast.LENGTH_SHORT).show();
                return;
            }

            if (loginListener != null) {
                loginListener.onLoginClicked(email, password, rememberMe);
            } else {
                Toast.makeText(getContext(), "Login attempt with: " + email, Toast.LENGTH_SHORT).show();
            }

            saveLoginInfo(email, password, rememberMe);
        });

        // Forgot password click
        forgotPasswordText.setOnClickListener(v -> {
            if (loginListener != null) {
                loginListener.onForgotPasswordClicked();
            } else {
                // Chuyển fragment nếu chưa dùng interface
                NavController navController = NavHostFragment.findNavController(LoginFragment.this);
                navController.navigate(R.id.action_loginFragment_to_forgetPasswordFragment);

            }
        });

        // Create account click
        createAccountText.setOnClickListener(v -> {
            if (loginListener != null) {
                loginListener.onCreateAccountClicked();
            } else {
                Toast.makeText(getContext(), "Create account clicked", Toast.LENGTH_SHORT).show();
            }
        });

        // Optional: Load saved login
        loadSavedLogin();
    }

    private void togglePasswordVisibility() {
        if (passwordVisible) {
            passwordEditText.setTransformationMethod(PasswordTransformationMethod.getInstance());
            passwordToggle.setImageResource(R.drawable.ic_visibility_off); // ẩn
        } else {
            passwordEditText.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            passwordToggle.setImageResource(R.drawable.ic_visibility); // hiện
        }
        passwordVisible = !passwordVisible;
        passwordEditText.setSelection(passwordEditText.length());
    }

    private void saveLoginInfo(String email, String password, boolean rememberMe) {
        if (getContext() == null) return;
        SharedPreferences prefs = getContext().getSharedPreferences("login_prefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        if (rememberMe) {
            editor.putString("email", email);
            editor.putString("password", password);
        } else {
            editor.clear();
        }
        editor.apply();
    }

    private void loadSavedLogin() {
        if (getContext() == null) return;
        SharedPreferences prefs = getContext().getSharedPreferences("login_prefs", Context.MODE_PRIVATE);
        boolean rememberMe = !prefs.getString("email", "").isEmpty(); // hoặc prefs.getBoolean("remember", false)

        // KHÔNG gán lại email/password vào EditText
        rememberMeCheckBox.setChecked(rememberMe);
    }

    public String getEmail() {
        return emailEditText != null ? emailEditText.getText().toString() : "";
    }

    public String getPassword() {
        return passwordEditText != null ? passwordEditText.getText().toString() : "";
    }

    public boolean isRememberMeChecked() {
        return rememberMeCheckBox != null && rememberMeCheckBox.isChecked();
    }

    public void clearForm() {
        if (emailEditText != null) emailEditText.setText("");
        if (passwordEditText != null) passwordEditText.setText("");
        if (rememberMeCheckBox != null) rememberMeCheckBox.setChecked(false);
    }
}
