package org.o7planning.a48k141_03_duan_quanlyhoctap.login;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.google.firebase.auth.FirebaseAuth;

import org.o7planning.a48k141_03_duan_quanlyhoctap.R;

public class LoginFragment extends Fragment {

    private EditText emailEditText;
    private EditText passwordEditText;
    private CheckBox rememberMeCheckBox;
    private ImageView passwordToggle;
    private ImageView emailStatusDot;
    private boolean passwordVisible = false;

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

        // Email dot update
        emailEditText.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override
            public void afterTextChanged(Editable s) {
                String emailInput = s.toString().trim();
                if (emailInput.isEmpty()) {
                    emailStatusDot.setImageResource(R.drawable.gray_dot);
                } else if (Patterns.EMAIL_ADDRESS.matcher(emailInput).matches()) {
                    emailStatusDot.setImageResource(R.drawable.green_dot);
                } else {
                    emailStatusDot.setImageResource(R.drawable.red_dot);
                }
            }
        });

        // Toggle password
        passwordToggle.setOnClickListener(v -> togglePasswordVisibility());

        // Sign In
        signInButton.setOnClickListener(v -> {
            String email = emailEditText.getText().toString().trim();
            String password = passwordEditText.getText().toString().trim();
            boolean rememberMe = rememberMeCheckBox.isChecked();

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(getContext(), "Vui lòng nhập đầy đủ email và mật khẩu", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                Toast.makeText(getContext(), "Email không hợp lệ", Toast.LENGTH_SHORT).show();
                return;
            }

            FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            // Đăng nhập thành công
                            saveLoginInfo(email, password, rememberMe);
                            NavHostFragment.findNavController(LoginFragment.this)
                                    .navigate(R.id.action_loginFragment_to_homeFragment);
                        } else {
                            // Đăng nhập thất bại
                            Toast.makeText(getContext(), "Email hoặc mật khẩu không đúng!", Toast.LENGTH_SHORT).show();
                        }
                    });

        });

        // Forgot Password
        forgotPasswordText.setOnClickListener(v -> {
            if (loginListener != null) {
                loginListener.onForgotPasswordClicked();
            } else {
                NavController navController = NavHostFragment.findNavController(LoginFragment.this);
                navController.navigate(R.id.action_loginFragment_to_forgetPasswordFragment);
            }
        });

        // Create Account
        createAccountText.setOnClickListener(v -> {
            if (loginListener != null) {
                loginListener.onCreateAccountClicked();
            } else {
                NavController navController = NavHostFragment.findNavController(LoginFragment.this);
                navController.navigate(R.id.action_loginFragment_to_registerFragment);
            }
        });

        // Load Remembered Account
        loadSavedLogin();
    }

    private void togglePasswordVisibility() {
        if (passwordVisible) {
            passwordEditText.setTransformationMethod(PasswordTransformationMethod.getInstance());
            passwordToggle.setImageResource(R.drawable.ic_visibility_off);
        } else {
            passwordEditText.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            passwordToggle.setImageResource(R.drawable.ic_visibility);
        }
        passwordVisible = !passwordVisible;
        passwordEditText.setSelection(passwordEditText.length());
    }

    private void saveLoginInfo(String email, String password, boolean rememberMe) {
        if (getContext() == null) return;

        SharedPreferences prefs = getContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        if (rememberMe) {
            editor.putString("email", email);
            editor.putString("password", password);
            editor.putBoolean("remember_me", true);
        } else {
            editor.remove("password");
            editor.putBoolean("remember_me", false);
        }

        editor.putBoolean("isOnline", true);
        editor.putString("email", email); // ✅ luôn lưu email
        editor.apply();
    }


    private void loadSavedLogin() {
        if (getContext() == null) return;

        SharedPreferences prefs = getContext().getSharedPreferences("login_prefs", Context.MODE_PRIVATE);
        boolean rememberMe = prefs.getBoolean("remember_me", false);

        if (rememberMe) {
            String savedEmail = prefs.getString("email", "");
            String savedPassword = prefs.getString("password", "");
            emailEditText.setText(savedEmail);
            passwordEditText.setText(savedPassword);
            rememberMeCheckBox.setChecked(true);
        } else {
            rememberMeCheckBox.setChecked(false);
        }
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
