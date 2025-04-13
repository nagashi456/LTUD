package org.o7planning.a48k141_03_duan_quanlyhoctap.Document;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.o7planning.a48k141_03_duan_quanlyhoctap.R;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class DocumentMenu extends AppCompatActivity {

    private RecyclerView recyclerView;
    private DocumentAdapter documentAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_document_menu);

        // Hide the action bar
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        // Initialize RecyclerView
        recyclerView = findViewById(R.id.recyclerViewDocuments);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Create sample document list
        List<Document> documents = new ArrayList<>();
        documents.add(new Document("Mobile design.doc", DocumentType.DOCUMENT, "2/17/2025"));
        documents.add(new Document("Data for bussiness.xls", DocumentType.EXCEL, "2/17/2025"));
        documents.add(new Document("Luyện python.py", DocumentType.CODE, "2/17/2025"));
        documents.add(new Document("Học java.js", DocumentType.CODE, "2/13/2025"));
        documents.add(new Document("Học java.js", DocumentType.CODE, "2/13/2025"));

        // Set up adapter
        documentAdapter = new DocumentAdapter(documents);
        recyclerView.setAdapter(documentAdapter);

        // Set up FAB click listener
        FloatingActionButton fab = findViewById(R.id.fabAddDocument);
        fab.setOnClickListener(view -> {
            // Handle add document action
        });
    }
}
