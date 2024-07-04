package com.example.tictactoe;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SpielfeldActivity extends AppCompatActivity {
    private Button[][] buttons = new Button[3][3];
    private boolean player1Turn = true;
    private int roundCount = 0;
    private boolean gameEnded = false;
    private Button btn_restart;
    private Button btn_back;
    private String modus;
    private TextView tv_headline;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spielfeld);
        btn_back = findViewById(R.id.btn_back);
        tv_headline=findViewById(R.id.tv_headline);
        Intent intent = getIntent();
        modus = intent.getStringExtra("MODUS");
        tv_headline.setText(modus);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SpielfeldActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
        btn_restart = findViewById(R.id.btn_restart);
        btn_restart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SpielfeldActivity.this, SpielfeldActivity.class);
                startActivity(intent);
            }
        });


        buttons[0][0] = findViewById(R.id.button1);
        buttons[0][1] = findViewById(R.id.button2);
        buttons[0][2] = findViewById(R.id.button3);
        buttons[1][0] = findViewById(R.id.button4);
        buttons[1][1] = findViewById(R.id.button5);
        buttons[1][2] = findViewById(R.id.button6);
        buttons[2][0] = findViewById(R.id.button7);
        buttons[2][1] = findViewById(R.id.button8);
        buttons[2][2] = findViewById(R.id.button9);

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                buttons[i][j].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(modus.equals("Multiplayer")){
                            multiplayer((Button) v);
                        }
                        if(modus.equals("Leicht")){
                            leicht((Button) v);
                        }
                        if(modus.equals("Mittel")){
                            onButtonClick(  (Button) v);
                        }

                    }
                });
            }
        }
    }


    private void multiplayer(Button button) {
        if (gameEnded) {
            return; // Das Spiel ist bereits beendet
        }

        if (!button.getText().toString().equals("")) {
            return; // Button ist bereits markiert
        }

        if (player1Turn) {
            button.setText("X");
        } else {
            button.setText("O");
        }

        roundCount++;

        if (checkForWin()) {
            String winner = player1Turn ? "Player X" : "Player O";
            showToast(winner + " wins!");
            gameEnded = true;
        } else if (roundCount == 9) {
            showToast("Draw!");
            gameEnded = true;
        } else {
            player1Turn = !player1Turn; // Wechsle den Spieler
        }
    }
    private void leicht (Button button) {
        if (gameEnded || !button.getText().toString().equals("")) {
            return;
        }

        if (player1Turn) {
            button.setText("X");
        } else {
            button.setText("O");
        }

        roundCount++;

        if (checkForWin()) {
            String winner = player1Turn ? "Player X" : "Player O";
            showToast(winner + " wins!");
            gameEnded = true;
        } else if (roundCount == 9) {
            showToast("Draw!");
            gameEnded = true;
        } else {
            player1Turn = !player1Turn;

            if (!player1Turn) {
                botMove();
            }
        }
    }








    private void onButtonClick(Button button) {
        if (!button.getText().toString().equals("")) {
            return;
        }

        if (player1Turn) {
            button.setText("X");
        } else {
            button.setText("O");
        }

        roundCount++;

        if (checkForWin()) {
            if (player1Turn) {
                showToast("Player 1 wins!");
            } else {
                showToast("Player 2 wins!");
            }

        } else if (roundCount == 9) {
            showToast("Draw!");

        } else {
            player1Turn = !player1Turn;
            if (!player1Turn) {
                botMove();
            }
        }
    }

    private void botmovemittel() {
        if (roundCount == 9) {
            return;
        }

        // Medium difficulty bot logic
        if (!tryToWinOrBlock("O") && !tryToWinOrBlock("X")) {
            // Make a random move if no winning or blocking move is possible
            makeRandomMove();
        }

        roundCount++;
        if (checkForWin()) {
            showToast("Player 2 wins!");

        } else if (roundCount == 9) {
            showToast("Draw!");
        } else {
            player1Turn = true;
        }
    }

    private boolean tryToWinOrBlock(String symbol) {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (buttons[i][j].getText().toString().equals("")) {
                    buttons[i][j].setText(symbol);
                    if (checkForWin()) {
                        if (symbol.equals("O")) {
                            return true; // Bot wins
                        } else {
                            buttons[i][j].setText("O");
                            return true; // Bot blocks
                        }
                    } else {
                        buttons[i][j].setText("");
                    }
                }
            }
        }
        return false;
    }

    private void makeRandomMove() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (buttons[i][j].getText().toString().equals("")) {
                    buttons[i][j].setText("O");
                    return;
                }
            }
        }
    }















    private void botMove() {
        List<Button> availableButtons = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (buttons[i][j].getText().toString().equals("")) {
                    availableButtons.add(buttons[i][j]);
                }
            }
        }

        if (availableButtons.size() > 0) {
            Random random = new Random();
            Button button = availableButtons.get(random.nextInt(availableButtons.size()));
            button.setText("O");
            roundCount++;

            if (checkForWin()) {
                showToast("Bot wins!");
                gameEnded = true;
            } else if (roundCount == 9) {
                showToast("Draw!");
                gameEnded = true;
            } else {
                player1Turn = !player1Turn;
            }
        }
    }

    private boolean checkForWin() {
        String[][] field = new String[3][3];

        // Buttons in das Spielfeld eintragen
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                field[i][j] = buttons[i][j].getText().toString();
            }
        }

        // Überprüfen der Zeilen
        for (int i = 0; i < 3; i++) {
            if (field[i][0].equals(field[i][1]) && field[i][0].equals(field[i][2]) && !field[i][0].isEmpty()) {
                return true;
            }
        }

        // Überprüfen der Spalten
        for (int i = 0; i < 3; i++) {
            if (field[0][i].equals(field[1][i]) && field[0][i].equals(field[2][i]) && !field[0][i].isEmpty()) {
                return true;
            }
        }

        // Überprüfen der Diagonalen
        if (field[0][0].equals(field[1][1]) && field[0][0].equals(field[2][2]) && !field[0][0].isEmpty()) {
            return true;
        }
        if (field[0][2].equals(field[1][1]) && field[0][2].equals(field[2][0]) && !field[0][2].isEmpty()) {
            return true;
        }

        return false;
    }

    private void showToast(String message) {
        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.custom_toast, findViewById(R.id.toast_container));

        TextView textView = layout.findViewById(R.id.toast_text);
        textView.setText(message);

        Toast toast = new Toast(getApplicationContext());
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(layout);
        toast.show();
    }

}







