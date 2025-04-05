package org.o7planning.a48k141_03_duan_quanlyhoctap.Start;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import org.o7planning.a48k141_03_duan_quanlyhoctap.BottomNavigationActivity;
import org.o7planning.a48k141_03_duan_quanlyhoctap.R;

public class LetStartFragment extends Fragment {

    public LetStartFragment() {
        // Constructor rỗng bắt buộc
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Gắn layout cho fragment
        return inflater.inflate(R.layout.fragment_let_start, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Button btnStart = view.findViewById(R.id.btnStart);
        btnStart.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), BottomNavigationActivity.class);
            startActivity(intent);
            requireActivity().finish(); // Không quay lại màn LetStart nữa
        });
    }
}
