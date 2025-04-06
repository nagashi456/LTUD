package org.o7planning.a48k141_03_duan_quanlyhoctap.login;

import android.os.Bundle;
import android.text.InputType;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import org.o7planning.a48k141_03_duan_quanlyhoctap.R;

public class SetNewPasswordFragment extends Fragment {

    private EditText newPasswordEditText, confirmPasswordEditText;
    private ImageView newPasswordToggle, confirmPasswordToggle;
    private TextView nextButton;

    private boolean isNewPasswordVisible = false;
    private boolean isConfirmPasswordVisible = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_set_new_password, container, false);

        // Ánh xạ view
        newPasswordEditText = view.findViewById(R.id.new_password_edit_text);
        confirmPasswordEditText = view.findViewById(R.id.confirm_password_edit_text);
        newPasswordToggle = view.findViewById(R.id.new_password_toggle);
        confirmPasswordToggle = view.findViewById(R.id.confirm_password_toggle);
        nextButton = view.findViewById(R.id.next_button);

        // Toggle mật khẩu
        newPasswordToggle.setOnClickListener(v -> togglePasswordVisibility(newPasswordEditText, newPasswordToggle, true));
        confirmPasswordToggle.setOnClickListener(v -> togglePasswordVisibility(confirmPasswordEditText, confirmPasswordToggle, false));

        // Xử lý khi nhấn Next
        nextButton.setOnClickListener(v -> {
            if (validatePassword()) {
                showCustomSnackbarAndNavigate();
            }
        });

        return view;
    }

    private void togglePasswordVisibility(EditText editText, ImageView toggleIcon, boolean isNewPassword) {
        boolean isVisible = isNewPassword ? isNewPasswordVisible : isConfirmPasswordVisible;

        if (isVisible) {
            editText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            toggleIcon.setImageResource(R.drawable.ic_visibility_off);
        } else {
            editText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            toggleIcon.setImageResource(R.drawable.ic_visibility);
        }

        editText.setSelection(editText.getText().length()); // Giữ con trỏ ở cuối

        if (isNewPassword) {
            isNewPasswordVisible = !isNewPasswordVisible;
        } else {
            isConfirmPasswordVisible = !isConfirmPasswordVisible;
        }
    }

    private boolean validatePassword() {
        String newPassword = newPasswordEditText.getText().toString().trim();
        String confirmPassword = confirmPasswordEditText.getText().toString().trim();

        if (newPassword.isEmpty() || confirmPassword.isEmpty()) {
            Toast.makeText(getContext(), "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return false;
        } else if (newPassword.length() < 6 || newPassword.length() > 20) {
            Toast.makeText(getContext(), "Password must be 6-20 characters long", Toast.LENGTH_SHORT).show();
            return false;
        } else if (!newPassword.matches(".*[a-zA-Z].*") || !newPassword.matches(".*\\d.*")) {
            Toast.makeText(getContext(), "Password must include letters and numbers", Toast.LENGTH_SHORT).show();
            return false;
        } else if (!newPassword.equals(confirmPassword)) {
            Toast.makeText(getContext(), "Passwords do not match", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    private void showCustomSnackbarAndNavigate() {
        LayoutInflater layoutInflater = LayoutInflater.from(requireContext());
        View layout = layoutInflater.inflate(R.layout.custom_snackbar, (ViewGroup) requireView(), false);

        Toast toast = new Toast(requireContext());
        toast.setView(layout);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, 0);
        toast.show();

        new android.os.Handler().postDelayed(() -> {
            NavHostFragment.findNavController(SetNewPasswordFragment.this)
                    .navigate(R.id.action_setNewPasswordFragment_to_loginFragment);
        }, 3500);
    }
}
