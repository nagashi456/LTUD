package org.o7planning.a48k141_03_duan_quanlyhoctap.Document;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import org.o7planning.a48k141_03_duan_quanlyhoctap.R;
public class DocumentAdapter extends RecyclerView.Adapter<DocumentAdapter.DocumentViewHolder> {

    private List<Document> documents;

    public DocumentAdapter(List<Document> documents) {
        this.documents = documents;
    }

    public static class DocumentViewHolder extends RecyclerView.ViewHolder {
        TextView textTitle;
        TextView textType;
        TextView textDate;
        ImageView imageMore;

        public DocumentViewHolder(@NonNull View itemView) {
            super(itemView);
            textTitle = itemView.findViewById(R.id.textDocumentTitle);
            textType = itemView.findViewById(R.id.textDocumentType);
            textDate = itemView.findViewById(R.id.textDocumentDate);
            imageMore = itemView.findViewById(R.id.imageMore);
        }
    }

    @NonNull
    @Override
    public DocumentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_document, parent, false);
        return new DocumentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DocumentViewHolder holder, int position) {
        Document document = documents.get(position);

        holder.textTitle.setText(document.getTitle());
        holder.textDate.setText(document.getDate());

        switch (document.getType()) {
            case DOCUMENT:
                holder.textType.setText("document");
                holder.textType.setTextColor(Color.parseColor("#b02929")); // Red
                break;
            case EXCEL:
                holder.textType.setText("excel");
                holder.textType.setTextColor(Color.parseColor("#34af45")); // Green
                break;
            case CODE:
                holder.textType.setText("code");
                holder.textType.setTextColor(Color.parseColor("#46bdf0")); // Blue
                break;
        }

        holder.imageMore.setOnClickListener(v -> {
            // Handle more options click
        });
    }

    @Override
    public int getItemCount() {
        return documents.size();
    }
}
