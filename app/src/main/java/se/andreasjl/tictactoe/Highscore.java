package se.andreasjl.tictactoe;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TextView;
import java.util.Arrays;
import java.util.Iterator;

public class Highscore extends Activity {
    private LinearLayout wrapper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_highscore);
        SharedPreferences prefs = this.getSharedPreferences("highscoreList", Context.MODE_PRIVATE);
        wrapper = (LinearLayout) findViewById(R.id.llHighscoreWrapper);
        Iterator<String> winners = prefs.getAll().keySet().iterator();
        Player[] players = new Player[prefs.getAll().size()];
        /*
        all the keys in sharedPrefs is players and the values is the scores.
         */
        int i = 0;
        while(winners.hasNext()){
            String winner = winners.next();
            players[i] = new Player(winner,prefs.getInt(winner,0));
            i++;
        }
        /*
        Sorts and displayes the top 10 players if there is 10 players that have won.
         */
        Arrays.sort(players);
        for(int j =0 ;j<10 && j< players.length;j++){
            TextView textView = new TextView(this);
            textView.setGravity(Gravity.CENTER_HORIZONTAL);
            textView.setTextSize(22);
            textView.setText(players[j].name + ": " + players[j].score);
            wrapper.addView(textView);
        }

    }

    /*
    Help class for making a easy comparable object that can be sorted.
     */
    private class Player implements Comparable<Player>{
        public String name;
        public int score;

        private Player(String name, int score){
            this.name = name;
            this.score = score;
        }

        @Override
        public int compareTo(Player player) {
            if (this.score < player.score) {
                return 1;
            }
            else if(this.score > player.score){
                return -1;
            }

            return 0;
        }

    }
}
