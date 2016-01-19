package se.andreasjl.tictactoe;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.NumberPicker;

public class Menuactivity extends Activity implements View.OnClickListener {
    private Button startGame;
    private Button highScore;
    private EditText player1,player2;
    private CheckBox cbPlayer1,cbPlayer2;
    NumberPicker np;
    private int isThereAi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        startGame = (Button) findViewById(R.id.bPlay);
        startGame.setOnClickListener(this);
        highScore = (Button) findViewById(R.id.bHighscore);
        highScore.setOnClickListener(this);
        player1 = (EditText) findViewById(R.id.etPlayerOne);
        player2 = (EditText) findViewById(R.id.etPlayerTwo);
        np = (NumberPicker) findViewById(R.id.numberPicker1);
        cbPlayer1 = (CheckBox) findViewById(R.id.cbPlayer1);
        cbPlayer2 = (CheckBox) findViewById(R.id.cbPlayer2);
        isThereAi = 0;
        np.setMinValue(1);
        np.setMaxValue(9);
        np.setWrapSelectorWheel(false);
    }

    @Override
    public void onClick(View v) {
        if(v.equals(startGame)){
            Intent intent = new Intent(this, Gameactivity.class);
            Bundle basket = new Bundle();
            basket.putString("player1",player1.getText().toString());
            basket.putString("player2",player2.getText().toString());
            basket.putInt("rows", np.getValue());
            basket.putInt("columns", np.getValue());
            basket.putInt("ai", isThereAi);
            intent.putExtras(basket);
            startActivity(intent);
        }else if(v.equals(highScore)){
            Intent intent = new Intent(this,Highscore.class);
            startActivity(intent);
        }

    }
    /*
    There can only be one Ai. And isThereAi determines which player is the ai if there is one.
     */
    public void onCheckButtonClicked(View view) {
        boolean checked = ((CheckBox) view).isChecked();

        switch(view.getId()) {
            case R.id.cbPlayer1:
                if (checked) {
                    cbPlayer2.setChecked(false);
                    isThereAi = 1;
                }else{
                    isThereAi = 0;
                }
                    break;
            case R.id.cbPlayer2:
                if (checked) {
                    cbPlayer1.setChecked(false);
                    isThereAi = 2;
                }else{
                    isThereAi = 0;
                }
                    break;
        }
    }
}
