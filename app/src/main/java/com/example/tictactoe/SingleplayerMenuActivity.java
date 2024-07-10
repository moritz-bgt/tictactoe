package com.example.tictactoe;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class SingleplayerMenuActivity extends AppCompatActivity {

    private Button btn_leicht;
    private Button btn_mittel;
    private Button btn_unmöglch;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_singleplayer_menu);
        btn_unmöglch=findViewById(R.id.btn_unmöglich);
        btn_unmöglch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
             Intent intent = new Intent(SingleplayerMenuActivity.this, SpielfeldActivity.class);
             intent.putExtra("MODUS","Unmöglich");
             startActivity(intent);
            }});
        btn_mittel=findViewById(R.id.btn_mittel);
        btn_mittel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SingleplayerMenuActivity.this, SpielfeldActivity.class);
                intent.putExtra("MODUS","Mittel");
                startActivity(intent);
            }
        });
        btn_leicht=findViewById(R.id.btn_leicht);
        btn_leicht.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SingleplayerMenuActivity.this, SpielfeldActivity.class);
                intent.putExtra("MODUS", "Leicht");
                startActivity(intent);

            }
        });




}
}
