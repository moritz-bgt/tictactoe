package com.example.tictactoe;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    private Button btn_multiplayer;
    private Button btn_singleplayer;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btn_singleplayer=findViewById(R.id.btn_singleplayer);
        btn_singleplayer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainActivity.this, SingleplayerMenuActivity.class);
                startActivity(intent);
            }
        });
        btn_multiplayer=findViewById(R.id.btn_multiplayer);
        btn_multiplayer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainActivity.this, SpielfeldActivity.class);
                intent.putExtra("MODUS", "Multiplayer");
                startActivity(intent);
            }
        });


    }

}