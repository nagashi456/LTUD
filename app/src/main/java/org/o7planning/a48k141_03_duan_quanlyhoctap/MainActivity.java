package org.o7planning.a48k141_03_duan_quanlyhoctap;


import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import org.o7planning.a48k141_03_duan_quanlyhoctap.Start.LetStartFragment;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Chỉ load LetStartFragment
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.flFragment, new LetStartFragment())
                    .commit();
        }
    }
}
