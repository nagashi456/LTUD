package org.o7planning.a48k141_03_duan_quanlyhoctap.login;

import android.os.Bundle;
import android.os.CountDownTimer;
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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import org.o7planning.a48k141_03_duan_quanlyhoctap.R;

public class ForgetPasswordFragment extends Fragment {

    private EditText emailEditText;
    private TextView countdownText, sendCodeButton, resendCodeButton;
    private ImageView emailStatusDot;
    private CountDownTimer countDownTimer;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_forget_password, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        emailEditText = view.findViewById(R.id.email_edit_text);
        countdownText = view.findViewById(R.id.countdown_text);
        sendCodeButton = view.findViewById(R.id.send_code_button);
        emailStatusDot = view.findViewById(R.id.email_status_dot);

        // Khởi tạo dot là màu xám
        emailStatusDot.setImageResource(R.drawable.gray_dot);

        // Lắng nghe khi người dùng nhập email
        emailEditText.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
                String input = s.toString().trim();

                if (TextUtils.isEmpty(input)) {
                    emailStatusDot.setImageResource(R.drawable.gray_dot); // ⚪ chưa nhập
                } else if (isValidEmail(input)) {
                    emailStatusDot.setImageResource(R.drawable.green_dot); // ✅ hợp lệ
                } else {
                    emailStatusDot.setImageResource(R.drawable.red_dot); // ❌ sai cú pháp
                }
            }
            @Override public void afterTextChanged(Editable s) {}
        });

        sendCodeButton.setOnClickListener(this::sendCode);
    }

    private void sendCode(View view) {
        String email = emailEditText.getText().toString().trim();

        if (!isValidEmail(email)) {
            Toast.makeText(getContext(), "Please enter a valid email.", Toast.LENGTH_SHORT).show();
            return;
        }

        Toast.makeText(getContext(), "Code sent to " + email, Toast.LENGTH_SHORT).show();
        startCountdown();

        // Gửi email sang OTP Fragment bằng Bundle
        Bundle bundle = new Bundle();
        bundle.putString("email", email);

        NavController navController = NavHostFragment.findNavController(this);
        navController.navigate(R.id.action_forgetPasswordFragment_to_otpVerificationFragment, bundle);
    }

    private void startCountdown() {
        if (countDownTimer != null) countDownTimer.cancel();

        countDownTimer = new CountDownTimer(5 * 60 * 1000, 1000) {
            public void onTick(long millisUntilFinished) {
                long minutes = millisUntilFinished / 60000;
                long seconds = (millisUntilFinished % 60000) / 1000;
                countdownText.setText("This code expires in " + minutes + "m " + seconds + "s");
            }

            public void onFinish() {
                countdownText.setText("The code has expired.");
            }
        }.start();
    }

    private boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
}
