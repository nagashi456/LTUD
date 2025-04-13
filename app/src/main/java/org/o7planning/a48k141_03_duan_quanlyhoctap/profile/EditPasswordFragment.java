package org.o7planning.a48k141_03_duan_quanlyhoctap.profile;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
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
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import org.o7planning.a48k141_03_duan_quanlyhoctap.R;

public class EditPasswordFragment extends Fragment {

    private EditText currentPasswordEditText;
    private EditText newPasswordEditText;
    private EditText confirmPasswordEditText;
    private ImageView currentToggle;
    private ImageView newToggle;
    private ImageView confirmToggle;

    private boolean isCurrentVisible = false;
    private boolean isNewVisible = false;
    private boolean isConfirmVisible = false;

    private SharedPreferences sharedPreferences;

    private TextView profileNameText;
    private ImageView editButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_edit_password, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        currentPasswordEditText = view.findViewById(R.id.current_password_edit_text);
        newPasswordEditText = view.findViewById(R.id.new_password_edit_text);
        confirmPasswordEditText = view.findViewById(R.id.confirm_password_edit_text);

        currentToggle = view.findViewById(R.id.current_toggle);
        newToggle = view.findViewById(R.id.new_toggle);
        confirmToggle = view.findViewById(R.id.confirm_toggle);

        profileNameText = view.findViewById(R.id.profile_name);
        editButton = view.findViewById(R.id.edit_name_button);

        sharedPreferences = requireActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        String storedPassword = sharedPreferences.getString("password", "");
        String fullName = sharedPreferences.getString("fullname", "Tên của bạn");

        profileNameText.setText(fullName);

        // Mặc định khóa các ô nhập mật khẩu
        setPasswordFieldsEnabled(false);

        // Cho phép nhập khi nhấn nút edit (icon cây bút)
        editButton.setOnClickListener(v -> setPasswordFieldsEnabled(true));

        // Toggle visibility cho các ô mật khẩu
        currentToggle.setOnClickListener(v -> {
            isCurrentVisible = !isCurrentVisible;
            togglePasswordVisibility(currentPasswordEditText, currentToggle, isCurrentVisible);
        });

        newToggle.setOnClickListener(v -> {
            isNewVisible = !isNewVisible;
            togglePasswordVisibility(newPasswordEditText, newToggle, isNewVisible);
        });

        confirmToggle.setOnClickListener(v -> {
            isConfirmVisible = !isConfirmVisible;
            togglePasswordVisibility(confirmPasswordEditText, confirmToggle, isConfirmVisible);
        });

        // Nút Update
        view.findViewById(R.id.update_button).setOnClickListener(v -> {
            String currentPassword = currentPasswordEditText.getText().toString().trim();
            String newPassword = newPasswordEditText.getText().toString().trim();
            String confirmPassword = confirmPasswordEditText.getText().toString().trim();

            if (!currentPassword.equals(storedPassword)) {
                Toast.makeText(requireContext(), "Mật khẩu hiện tại không đúng", Toast.LENGTH_SHORT).show();
                return;
            }

            if (newPassword.isEmpty() || confirmPassword.isEmpty()) {
                Toast.makeText(requireContext(), "Vui lòng điền đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!newPassword.equals(confirmPassword)) {
                Toast.makeText(requireContext(), "Mật khẩu mới không khớp", Toast.LENGTH_SHORT).show();
                return;
            }

            // Lưu mật khẩu mới
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("password", newPassword);
            editor.apply();

            Toast.makeText(requireContext(), "Đổi mật khẩu thành công", Toast.LENGTH_SHORT).show();

            // Khóa lại các ô nhập
            setPasswordFieldsEnabled(false);

            // Quay về màn hình Profile
            Navigation.findNavController(view).navigate(R.id.action_editPasswordFragment_to_profileFragment);
        });

        // Nút Back layout
        ImageView backButton = view.findViewById(R.id.back_button);
        backButton.setOnClickListener(v -> Navigation.findNavController(view).navigateUp());

        // Nút back vật lý
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                Navigation.findNavController(view).navigateUp();
            }
        });
    }

    private void togglePasswordVisibility(EditText editText, ImageView toggleIcon, boolean isVisible) {
        if (isVisible) {
            editText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            toggleIcon.setImageResource(R.drawable.ic_visibility);
        } else {
            editText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            toggleIcon.setImageResource(R.drawable.ic_visibility_off);
        }
        editText.setSelection(editText.getText().length());
    }

    private void setPasswordFieldsEnabled(boolean enabled) {
        currentPasswordEditText.setEnabled(enabled);
        newPasswordEditText.setEnabled(enabled);
        confirmPasswordEditText.setEnabled(enabled);
        currentToggle.setEnabled(enabled);
        newToggle.setEnabled(enabled);
        confirmToggle.setEnabled(enabled);
    }
}
