package org.o7planning.a48k141_03_duan_quanlyhoctap.profile;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import org.o7planning.a48k141_03_duan_quanlyhoctap.R;

public class ProfileFragment extends Fragment {

    private View statusDot;
    private TextView statusText;

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        statusDot = view.findViewById(R.id.status_dot);
        statusText = view.findViewById(R.id.status_text);

        SharedPreferences prefs = requireContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        String fullName = prefs.getString("fullName", "");

        TextView nameText = view.findViewById(R.id.profile_name); // bạn thêm TextView này trong layout nếu cần
        nameText.setText(fullName);
        boolean isOnline = prefs.getBoolean("isOnline", true);

        updateStatusUI(isOnline);

        //  Nhấn nút back → quay về màn hình trước đó
        view.findViewById(R.id.back_button).setOnClickListener(v -> {
            NavHostFragment.findNavController(ProfileFragment.this).popBackStack();
        });

        // Chuyển đến trang My Profile
        view.findViewById(R.id.profile_item).setOnClickListener(v -> {
            NavHostFragment.findNavController(ProfileFragment.this)
                    .navigate(R.id.action_profileFragment_to_myProfileFragment);
        });


        //  Chuyển đến trang đổi mật khẩu
        view.findViewById(R.id.password_item).setOnClickListener(v -> {
            NavHostFragment.findNavController(ProfileFragment.this)
                    .navigate(R.id.action_profileFragment_to_editPasswordFragment);
        });

        //  Xử lý logout → chuyển về LetStartFragment
        view.findViewById(R.id.logout_item).setOnClickListener(v -> {
            Toast.makeText(getContext(), "Đăng xuất...", Toast.LENGTH_SHORT).show();

            // Đặt trạng thái offline (không xóa toàn bộ prefs)
            prefs.edit().putBoolean("isOnline", false).apply();

            // Điều hướng về LetStartFragment và xóa back stack
            NavController navController = NavHostFragment.findNavController(ProfileFragment.this);
            navController.navigate(R.id.action_profileFragment_to_letStartFragment);
        });
    }

    private void updateStatusUI(boolean isOnline) {
        if (statusDot != null && statusText != null) {
            if (isOnline) {
                statusDot.setBackgroundResource(R.drawable.green_dot);
                statusText.setText("Active");
            } else {
                statusDot.setBackgroundResource(R.drawable.red_dot);
                statusText.setText("Offline");
            }
        }
    }
}
