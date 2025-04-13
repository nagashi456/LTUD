package org.o7planning.a48k141_03_duan_quanlyhoctap.login;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import org.o7planning.a48k141_03_duan_quanlyhoctap.R;

public class RegisterFragment extends Fragment {

    private EditText emailEditText;
    private ImageView emailStatusDot;
    private TextView btnNext;
    private TextView signInTextView;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_register, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        emailEditText = view.findViewById(R.id.email_edit_text);
        emailStatusDot = view.findViewById(R.id.email_status_dot);
        btnNext = view.findViewById(R.id.button_next);
        signInTextView = view.findViewById(R.id.sign_in);

        emailStatusDot.setImageResource(R.drawable.gray_dot);

        emailEditText.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (TextUtils.isEmpty(s)) {
                    emailStatusDot.setImageResource(R.drawable.gray_dot);
                } else if (Patterns.EMAIL_ADDRESS.matcher(s).matches()) {
                    emailStatusDot.setImageResource(R.drawable.green_dot);
                } else {
                    emailStatusDot.setImageResource(R.drawable.red_dot);
                }
            }
            @Override public void afterTextChanged(Editable s) {}
        });

        btnNext.setOnClickListener(v -> {
            String email = emailEditText.getText().toString().trim();
            if (Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                // Lưu email vào SharedPreferences
                requireActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
                        .edit()
                        .putString("email", email)
                        .apply();

                // Chuyển sang SignUpFragment mà không cần Bundle
                NavController navController = NavHostFragment.findNavController(this);
                navController.navigate(R.id.action_registerFragment_to_signUpFragment);
            } else {
                Toast.makeText(getContext(), "Vui lòng nhập email hợp lệ!", Toast.LENGTH_SHORT).show();
            }
        });

        signInTextView.setOnClickListener(v -> {
            NavHostFragment.findNavController(this)
                    .navigate(R.id.action_registerFragment_to_loginFragment);
        });
    }
}
