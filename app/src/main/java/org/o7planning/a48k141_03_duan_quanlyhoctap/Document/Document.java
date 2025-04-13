package org.o7planning.a48k141_03_duan_quanlyhoctap.Document;
import org.o7planning.a48k141_03_duan_quanlyhoctap.R;
public class Document {
    private String title;
    private DocumentType type;
    private String date;

    public Document(String title, DocumentType type, String date) {
        this.title = title;
        this.type = type;
        this.date = date;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public DocumentType getType() {
        return type;
    }

    public void setType(DocumentType type) {
        this.type = type;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
