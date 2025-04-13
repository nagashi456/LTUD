package org.o7planning.a48k141_03_duan_quanlyhoctap;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); // chỉ set layout thôi, không replace fragment
    }
}
