package com.example.tictactoe;

import android.content.Intent;
import android.os.AsyncTask;
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
import android.os.AsyncTask;

public class SpielfeldActivity extends AppCompatActivity {
    private Button[][] buttons = new Button[3][3];
    private boolean player1Turn = true;
    private int roundCount = 0;
    private boolean gameEnded = false;
    private Button btn_restart;
    private Button btn_back;
    private String modus;
    private TextView tv_headline;
    private boolean gameActive;

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
                intent.putExtra("MODUS", modus);
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
                        if(modus.equals("Multiplayer"))
                            multiplayer((Button) v);
                        if(modus.equals("Leicht")){
                            leicht((Button) v);
                        }
                        if(modus.equals("Mittel")){
                            onButtonClickmittel((Button)v);

                        }
                        if(modus.equals("Unmöglich")){
                            onButtonClickunmöglich((Button)v);
                        }

                    }
                });
            }
        }
        if (savedInstanceState != null) {
            roundCount = savedInstanceState.getInt("roundCount");
            player1Turn = savedInstanceState.getBoolean("player1Turn");

            String[][] field = new String[3][3];
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    field[i][j] = savedInstanceState.getString("button" + i + j);
                    buttons[i][j].setText(field[i][j]);
                }
            }
        }
    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putInt("roundCount", roundCount);
        outState.putBoolean("player1Turn", player1Turn);

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                outState.putString("button" + i + j, buttons[i][j].getText().toString());
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
        }
            player1Turn = !player1Turn; // Wechsle den Spieler
        
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
                botMove1();
            }
        }
    }








    private void onButtonClickmittel(Button button) {
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
                botMove1();
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
            gameActive = false;
            if (player1Turn) {
                showToast("Player 1 wins!");
            } else {
                showToast("Player 2 wins!");
            }
        } else if (roundCount == 9) {
            gameActive = false;
            showToast("Draw!");
        } else {
            player1Turn = !player1Turn;
            if (!player1Turn) {
                botMove1();
            }
        }
    }

    private void botMove1() {
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
            gameActive = false;
            showToast("Player 2 wins!");
        } else if (roundCount == 9) {
            gameActive = false;
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

    private void onButtonClickunmöglich(Button button) {
        if (!button.getText().toString().equals("")) {
            return;
        }

        if (player1Turn) {
            button.setText("X");
            roundCount++;
            if (checkForWin()) {
                showToast("Spieler 1 gewinnt!");
                return;
            } else if (roundCount == 9) {
                showToast("Unentschieden!");
                return;
            } else {
                player1Turn = false;
                botMoveImpossible();
            }
        }
    }

    private void botMoveImpossible() {
        String[][] field = new String[3][3];
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                field[i][j] = buttons[i][j].getText().toString();
            }
        }
        new ImpossibleBotMoveTask(field).execute();
    }

    private class ImpossibleBotMoveTask extends AsyncTask<Void, Void, int[]> {
        private final String[][] field;

        ImpossibleBotMoveTask(String[][] field) {
            this.field = field;
        }

        @Override
        protected int[] doInBackground(Void... voids) {
            return minimaxMove(field);
        }

        @Override
        protected void onPostExecute(int[] move) {
            if (move != null && move.length == 2) {
                buttons[move[0]][move[1]].setText("O");
                roundCount++;
                if (checkForWin()) {
                    showToast("Bot gewinnt!");
                } else if (roundCount == 9) {
                    showToast("Unentschieden!");
                } else {
                    player1Turn = true;
                }
            }
        }
    }

    private int[] minimaxMove(String[][] field) {
        int bestScore = Integer.MIN_VALUE;
        int[] bestMove = {-1, -1};

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (field[i][j].equals("")) {
                    field[i][j] = "O";
                    int score = minimax(field, 0, false);
                    field[i][j] = "";
                    if (score > bestScore) {
                        bestScore = score;
                        bestMove[0] = i;
                        bestMove[1] = j;
                    }
                }
            }
        }
        return bestMove;
    }

    private int minimax(String[][] field, int depth, boolean isMaximizing) {
        int score = evaluate(field);
        if (score == 10) return score - depth;
        if (score == -10) return score + depth;
        if (!isMovesLeft(field)) return 0;

        if (isMaximizing) {
            int best = Integer.MIN_VALUE;
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    if (field[i][j].equals("")) {
                        field[i][j] = "O";
                        best = Math.max(best, minimax(field, depth + 1, false));
                        field[i][j] = "";
                    }
                }
            }
            return best;
        } else {
            int best = Integer.MAX_VALUE;
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    if (field[i][j].equals("")) {
                        field[i][j] = "X";
                        best = Math.min(best, minimax(field, depth + 1, true));
                        field[i][j] = "";
                    }
                }
            }
            return best;
        }
    }

    private boolean isMovesLeft(String[][] field) {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (field[i][j].equals("")) {
                    return true;
                }
            }
        }
        return false;
    }

    private int evaluate(String[][] field) {
        for (int i = 0; i < 3; i++) {
            if (field[i][0].equals(field[i][1]) && field[i][1].equals(field[i][2])) {
                if (field[i][0].equals("O")) return +10;
                else if (field[i][0].equals("X")) return -10;
            }
        }

        for (int i = 0; i < 3; i++) {
            if (field[0][i].equals(field[1][i]) && field[1][i].equals(field[2][i])) {
                if (field[0][i].equals("O")) return +10;
                else if (field[0][i].equals("X")) return -10;
            }
        }

        if (field[0][0].equals(field[1][1]) && field[1][1].equals(field[2][2])) {
            if (field[0][0].equals("O")) return +10;
            else if (field[0][0].equals("X")) return -10;
        }

        if (field[0][2].equals(field[1][1]) && field[1][1].equals(field[2][0])) {
            if (field[0][2].equals("O")) return +10;
            else if (field[0][2].equals("X")) return -10;
        }

        return 0;
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
        View layout = inflater.inflate(R.layout.activity_toast, findViewById(R.id.toast_container));

        TextView textView = layout.findViewById(R.id.toast_text);
        textView.setText(message);

        Toast toast = new Toast(getApplicationContext());
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(layout);
        toast.show();
    }


}








