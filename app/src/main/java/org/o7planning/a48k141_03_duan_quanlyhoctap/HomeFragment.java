package org.o7planning.a48k141_03_duan_quanlyhoctap;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

public class HomeFragment extends Fragment {

    private CardView documentsCard, personalProjectCard, groupProjectCard;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // Ánh xạ các CardView
        documentsCard = view.findViewById(R.id.documents_card);
        personalProjectCard = view.findViewById(R.id.personal_project_card);
        groupProjectCard = view.findViewById(R.id.group_project_card);

        // Gán sự kiện click
        documentsCard.setOnClickListener(v ->
                Toast.makeText(getContext(), "Opening Documents", Toast.LENGTH_SHORT).show()
        );

        personalProjectCard.setOnClickListener(v ->
                Toast.makeText(getContext(), "Opening Personal Projects", Toast.LENGTH_SHORT).show()
        );

        groupProjectCard.setOnClickListener(v ->
                Toast.makeText(getContext(), "Opening Group Projects", Toast.LENGTH_SHORT).show()
        );

        return view;
    }
}
