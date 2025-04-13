package org.o7planning.a48k141_03_duan_quanlyhoctap.login;

import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;

import org.o7planning.a48k141_03_duan_quanlyhoctap.R;

public class OtpVerificationFragment extends Fragment {

    private EditText[] otpEditTexts;
    private TextView timerTextView;
    private TextView verifyButton;
    private TextView resendCodeButton;
    private TextView emailTextView;
    private CountDownTimer countDownTimer;
    private static final long OTP_TIMEOUT_MILLIS = 60000;

    private String email;
    private ImageView backButton;
    private FirebaseAuth mAuth;


    public OtpVerificationFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            email = getArguments().getString("email", "");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mAuth = FirebaseAuth.getInstance(); // Khởi tạo FirebaseAuth
        return inflater.inflate(R.layout.fragment_otp_verification, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ImageView backButton = view.findViewById(R.id.back_button);
        otpEditTexts = new EditText[]{
                view.findViewById(R.id.otp_box_1),
                view.findViewById(R.id.otp_box_2),
                view.findViewById(R.id.otp_box_3),
                view.findViewById(R.id.otp_box_4)
        };

        timerTextView = view.findViewById(R.id.timer_text);
        verifyButton = view.findViewById(R.id.verify_button);
        resendCodeButton = view.findViewById(R.id.verify_button_resend);
        emailTextView = view.findViewById(R.id.email_text);

        emailTextView.setText(email);
        setupOtpInputs();

        verifyButton.setEnabled(false);
        verifyButton.setAlpha(0.5f);

        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                NavHostFragment.findNavController(OtpVerificationFragment.this).popBackStack();
            }
        });

        backButton = view.findViewById(R.id.back_button);
        backButton.setOnClickListener(v -> {
            NavHostFragment.findNavController(OtpVerificationFragment.this).popBackStack();
        });

        resendCodeButton.setOnClickListener(v -> {
            Toast.makeText(getContext(), "Verification code resent to " + email, Toast.LENGTH_SHORT).show();

            // Xóa trắng các ô nhập OTP
            for (EditText editText : otpEditTexts) {
                editText.setText("");
                editText.setBackgroundResource(R.drawable.otp_box_background); // reset về giao diện bình thường nếu có highlight
            }

            // Đặt lại focus về ô đầu tiên
            otpEditTexts[0].requestFocus();

            // Bắt đầu lại đồng hồ đếm
            startCountdownTimer();

            // Disable nút resend
            resendCodeButton.setEnabled(false);
            resendCodeButton.setAlpha(0.5f);
        });

        verifyButton.setOnClickListener(v -> {
            String enteredOtp = getEnteredOtp();
            if (enteredOtp.length() != 4) {
                highlightEmptyFields();
                Toast.makeText(getContext(), "Please enter a 6-digit OTP", Toast.LENGTH_SHORT).show();
                return;
            }

            mAuth.verifyPasswordResetCode(enteredOtp)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            // Mã hợp lệ => chuyển sang fragment đặt mật khẩu mới
                            NavHostFragment.findNavController(OtpVerificationFragment.this)
                                    .navigate(R.id.action_otpVerificationFragment_to_setNewPasswordFragment);
                        } else {
                            Toast.makeText(getContext(), "Invalid OTP. Please try again.", Toast.LENGTH_SHORT).show();
                        }
                    });
        });


        startCountdownTimer();
    }

    private void setupOtpInputs() {
        for (int i = 0; i < otpEditTexts.length; i++) {
            final int index = i;

            otpEditTexts[i].addTextChangedListener(new TextWatcher() {
                @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
                @Override public void onTextChanged(CharSequence s, int start, int before, int count) {}

                @Override
                public void afterTextChanged(Editable s) {
                    if (s.length() == 1 && index < otpEditTexts.length - 1) {
                        otpEditTexts[index + 1].requestFocus();
                    }
                    checkAllFieldsFilled();
                }
            });

            otpEditTexts[i].setOnKeyListener((v, keyCode, event) -> {
                if (keyCode == KeyEvent.KEYCODE_DEL && event.getAction() == KeyEvent.ACTION_DOWN &&
                        otpEditTexts[index].getText().toString().isEmpty() && index > 0) {
                    otpEditTexts[index - 1].requestFocus();
                    return true;
                }
                return false;
            });
        }
    }

    private void highlightEmptyFields() {
        for (EditText editText : otpEditTexts) {
            if (editText.getText().toString().isEmpty()) {
                editText.setBackgroundResource(R.drawable.edittext_error_background); // tạo background màu đỏ nhẹ
            } else {
                editText.setBackgroundResource(R.drawable.edittext_normal_background); // màu bình thường
            }
        }
    }

    private void checkAllFieldsFilled() {
        boolean allFilled = true;
        for (EditText editText : otpEditTexts) {
            if (editText.getText().toString().isEmpty()) {
                allFilled = false;
                break;
            }
        }
        verifyButton.setEnabled(allFilled);
        verifyButton.setAlpha(allFilled ? 1.0f : 0.5f);
    }

    private String getEnteredOtp() {
        StringBuilder otp = new StringBuilder();
        for (EditText editText : otpEditTexts) {
            otp.append(editText.getText().toString());
        }
        return otp.toString();
    }

    private void startCountdownTimer() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }

        countDownTimer = new CountDownTimer(OTP_TIMEOUT_MILLIS, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                int seconds = (int) (millisUntilFinished / 1000);
                timerTextView.setText(String.format("%02d:%02d Sec", seconds / 60, seconds % 60));
            }

            @Override
            public void onFinish() {
                timerTextView.setText("00:00 Sec");
                resendCodeButton.setEnabled(true);
                resendCodeButton.setAlpha(1.0f);
            }
        }.start();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }

}
