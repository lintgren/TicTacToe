package se.andreasjl.tictactoe;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import java.util.Random;

public class Gameactivity extends Activity implements View.OnClickListener {
    private TextView textView;
    private String player1,player2;
    private int enabledButtons;
    private String curretPlayer;
    private int[][] board;
    private Button[] buttons;
    private int columns,rows;
    private Button bReset;
    private int isThereAi;
    private static String PLAYERONE_SYMBOL;
    private static String PLAYERTWO_SYMBOL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        textView = (TextView) findViewById(R.id.tvTurn);
        bReset = (Button) findViewById(R.id.bReset);
        bReset.setOnClickListener(this);
        /*
        easy to change symbols. Should be easy to implement so that users can chose a symbol in menu
         then send it to this class with intent.
         */
        PLAYERONE_SYMBOL = "X";
        PLAYERTWO_SYMBOL = "O";

        Bundle basket = getIntent().getExtras();
        rows = basket.getInt("rows");
        columns = basket.getInt("columns");
        player1 = basket.getString("player1");
        player2 = basket.getString("player2");
        /*
        if both players have same name, adds a 2 on player2's name.
         */
        if(player1.equals(player2)){
            player2 = player2+"2";
        }
        isThereAi = basket.getInt("ai");
        board = new int[rows][columns];
        buttons = new Button[rows*columns];
        for(int i =0; i <rows;i++){
            for (int j = 0 ; j < columns ; j++){
                /*
                fill the board with a negative number to make it easier for declaring a winner by
                just adding all the columns.
                 */
                board[i][j] = -20;
            }
        }
        createBoardButtons();
        /*
        Makes it random who starts.
         */
        Random rand = new Random();

        if(rand.nextDouble()*10>5){
            curretPlayer =player1;
            textView.setText(curretPlayer+ " ("+PLAYERONE_SYMBOL+") turn");
            if(isThereAi == 1){
                /*
                if player1 is ai. ai starts and makes a move.
                 */
                aiSimulatedClick();
            }
        }else{
            curretPlayer =player2;
            textView.setText(curretPlayer+ " ("+PLAYERTWO_SYMBOL+") turn");
            if(isThereAi == 2){
                /*
                if player2 is ai. ai starts and makes a move.
                 */
                aiSimulatedClick();
            }
        }

    }
    /*
    creates the ui for the board. The columns are buttons in rows that are linearlayouts.
     */
    private void createBoardButtons(){
        LinearLayout layout = (LinearLayout) findViewById(R.id.llWrapper);
        layout.setBackgroundColor(Color.BLACK);
        layout.setPadding(5, 5, 5, 5);
        /*
        this padding makes the surrounding black boarder the same width as the internal
         */
        for (int i = 0; i < rows; i++) {
            LinearLayout row = new LinearLayout(this);
            row.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
            for (int j = 0; j < columns; j++) {
                Button button = new Button(this);
                LayoutParams layoutParams = new LayoutParams(LayoutParams.WRAP_CONTENT ,LayoutParams.WRAP_CONTENT,1);
                /*
                The wrapping linearlayouts background is black. therefore uses a margin so it looks like a boarder on each button.
                 */
                layoutParams.setMargins(5, 5, 5, 5);
                button.setBackgroundColor(Color.WHITE);
                /*
                index all the buttons from 0 -> j(column) + (i(row) * nbr of columns)
                 */
                button.setId(j + (i * columns));
                button.setLayoutParams(layoutParams);
                row.addView(button);
                button.setOnClickListener(this);
                buttons[j+(i*columns)] = button;
            }

            layout.addView(row);
        }

    }

    /*
    Generates a  random value between 0-> number of buttons. It will then see if ai can place its mark
     on that button otherwise generate a new value until it can. When it finds its value it will
     simulate a click on that button and place its mark.
     */
    private void aiSimulatedClick(){
        Random rand = new Random();
        Button tmp;
        while(!(tmp = buttons[rand.nextInt(buttons.length)]).isEnabled()){
        }
        tmp.performClick();
    }

    @Override
    public void onClick(View v) {
        Button b = (Button) v;
        if(b.equals(bReset)){
            reset();
        }else if(b.isEnabled()) {
            if(curretPlayer.equals(player1)) {
                b.setText(PLAYERONE_SYMBOL);
                board[b.getId()/rows][b.getId()%columns] = 1;
                /*
                This makes player1 always X. adds value 1 to the column in board[][] with the right index of that button.
                 */
                curretPlayer = player2;
                textView.setText(curretPlayer + " ("+PLAYERTWO_SYMBOL+") turn");
            }else{
                b.setText(PLAYERTWO_SYMBOL);
                /*
                This makes player2 always O. adds value 0 to the column in board[][] with the right index of that button.
                 */
                board[b.getId()/rows][b.getId()%columns] = 0;
                curretPlayer = player1;
                textView.setText(curretPlayer + " ("+PLAYERONE_SYMBOL+") turn");
            }
            b.setEnabled(false);
            enabledButtons++;
            if(enabledButtons==rows*columns){
                textView.setText("no more moves");
                /*
                the ai should not try to make more moves. Changes so that it's the non ai players turn always.
                 */
                if(isThereAi == 1){
                    curretPlayer = player2;
                }else if(isThereAi == 2){
                    curretPlayer = player1;
                }
            }

            /*
            updates the highscores that are saved in android sharedPreferences. Key is each player
            and value is their nbr of wins.
             */
            String winner;
            if((winner = isThereWinner()) != null){
                SharedPreferences prefs = this.getSharedPreferences("highscoreList", Context.MODE_PRIVATE);
                if(prefs.contains(winner)){
                    /*
                    Won before and should just add 1 to the streak.
                     */
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putInt(winner, prefs.getInt(winner,0)+1);
                    editor.commit();
                }else{
                    /*
                    First win and player should be added to the preferences.
                     */
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putInt(winner, 1);
                    editor.commit();
                }
            }else if(curretPlayer.equals(player1) && isThereAi == 1){
                /*
                ais turn and ai is player1
                 */
                aiSimulatedClick();
            }else if(curretPlayer.equals(player2) && isThereAi == 2){
                /*
                ais turn and ai is player2
                 */
                aiSimulatedClick();
            }
        }
    }
    /*
    locks the buttons because there is a winner. No more moves can be made
     */
    public void lockButtons(){
        for(int i = 0;i<buttons.length;i++){
            buttons[i].setEnabled(false);
        }
    }

    /*
    Starts a new GameActivity with the same attributes. Also removes the current one.
     */
    public void reset(){
        Intent intent = new Intent(this,Gameactivity.class);
        Bundle basket = new Bundle();
        basket.putString("player1",player1);
        basket.putString("player2", player2);
        basket.putInt("rows", rows);
        basket.putInt("columns", columns);
        basket.putInt("ai", isThereAi);
        intent.putExtras(basket);
        startActivity(intent);
        finish();
        /*
        Using finish to remove the current acitivty so it is not saved in the Backstack.
         */
    }
    /*
    checks for a winner. Returns name of the winner and locks the game if someone have won.
    returns null otherwise
     */
    private String isThereWinner(){
        /*
        row by row and column by column adds the values. If the result is 0. player2 won. if >3 player1 won.
         */
        int sumRow=0;
        int sumCol=0;
        int sumTopToBottom = 0;
        int sumBottomToTop = 0;
        for(int i = 0; i < rows;i++) {
            /*
            Just adds the values in each column to see how many 0s/1s there is. if one of the sums = 0
            player2 wins. If one of the sums = 3 (in case of 3x3 board) player 1 won.
             */
            //adds the diagonal rows.
            sumBottomToTop += board[i][i];
            sumTopToBottom += board[i][(columns-1)-i];
            for (int j = 0; j < columns; j++) {
                //adds the horizontal and vertical rows/columns
                sumRow += board[j][i];
                sumCol += board[i][j];
            }
            if (sumRow > columns-1 || sumCol> columns-1 || sumBottomToTop > columns-1 ||sumTopToBottom > columns-1) {
                    /*
                    player1 wins
                     */
                textView.setText(player1 + " Wins!");
                lockButtons();
                return player1;
            }else if(sumRow == 0 || sumCol == 0) {
                    /*
                    player2 wins
                     */
                textView.setText(player2 + " Wins!");
                lockButtons();
                return player2;
            }
            sumCol = 0;
            sumRow = 0;
        }
        if(sumBottomToTop == 0 || sumTopToBottom == 0) {
                    /*
                    player2 wins
                     */
            textView.setText(player2 + " Wins!");
            lockButtons();
            return player2;
        }
        return null;
    }
}
