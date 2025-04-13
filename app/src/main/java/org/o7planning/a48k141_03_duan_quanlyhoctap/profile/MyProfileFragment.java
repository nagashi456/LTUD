package org.o7planning.a48k141_03_duan_quanlyhoctap.profile;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import org.o7planning.a48k141_03_duan_quanlyhoctap.R;

public class MyProfileFragment extends Fragment {

    private EditText nameEditText;
    private EditText emailEditText;
    private TextView profileNameTextView;

    public MyProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_my_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        nameEditText = view.findViewById(R.id.username_edit_text); // dùng làm fullname
        emailEditText = view.findViewById(R.id.email_edit_text);
        ImageView editButton = view.findViewById(R.id.edit_name_button);
        ImageView backButton = view.findViewById(R.id.back_button);
        Button updateButton = view.findViewById(R.id.update_button);
        profileNameTextView = view.findViewById(R.id.profile_name);

        nameEditText.setEnabled(false);
        emailEditText.setEnabled(false);

        SharedPreferences prefs = requireContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        String fullname = prefs.getString("fullName", "");
        String email = prefs.getString("email", "");


        nameEditText.setText(fullname);
        emailEditText.setText(email);
        profileNameTextView.setText(fullname);

        editButton.setOnClickListener(v -> {
            nameEditText.setEnabled(true);
            emailEditText.setEnabled(true);
            nameEditText.requestFocus();
        });

        updateButton.setOnClickListener(v -> {
            String newFullname = nameEditText.getText().toString().trim();
            String newEmail = emailEditText.getText().toString().trim();

            SharedPreferences.Editor editor = prefs.edit();
            editor.putString("fullname", newFullname);
            editor.putString("email", newEmail);
            editor.apply();

            nameEditText.setEnabled(false);
            emailEditText.setEnabled(false);
            profileNameTextView.setText(newFullname);
        });

        backButton.setOnClickListener(v -> {
            NavController navController = Navigation.findNavController(view);
            navController.navigate(R.id.action_myProfileFragment_to_profileFragment);
        });
    }
}
