package android2.scarnesdice;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    int userOverall = 0;
    int computerOverall = 0;
    int turnScore = 0;
    int diceValue = 5;
    int currentTurn = 0;

    TextView computerScoreView;
    TextView playScoreView;
    TextView turnScoreView;
    TextView actionTextView;
    TextView currentTurnTextView;

    ImageView diceView;

    Button rollButton;
    Button holdButton;
    Button resetButton;
    Handler handler = new Handler();
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            rollDice(computerScoreView);
            actionTextView.setText("Computer rolled a "+diceValue);
            if (diceValue == 1){
                actionTextView.append(" and loses their turn!");
                handler.postDelayed(userSwitchRunnable,2000);
            }
            else if (turnScore >= 20){
                actionTextView.setText("Computer chooses to hold...");
                holdScore(computerScoreView);
                handler.postDelayed(userSwitchRunnable,2000);
            }
            else
                handler.postDelayed(this,2000);
        }
    };
    Runnable userSwitchRunnable = new Runnable() {
        @Override
        public void run() {
            rollButton.setClickable(true);
            holdButton.setClickable(true);
            actionTextView.setText("Now it is your turn");
            currentTurnTextView.setText("Current Turn: Player");
            currentTurn = 0;
            if (checkWin()){
                actionTextView.setText("Computer Wins!");
            }
            handler.removeCallbacks(this);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        computerScoreView = (TextView) findViewById(R.id.computerScoreTextBox);
        playScoreView = (TextView) findViewById(R.id.yourScoreTextBox);
        turnScoreView = (TextView) findViewById(R.id.turnScoreTextBox);
        actionTextView = (TextView) findViewById(R.id.actionTextView);
        currentTurnTextView = (TextView) findViewById(R.id.currentTurnTextView);

        diceView = (ImageView) findViewById(R.id.imageOfDice);

        rollButton = (Button) findViewById(R.id.rollButton);
        holdButton = (Button) findViewById(R.id.holdButton);
        resetButton = (Button) findViewById(R.id.resetButton);
    }

    public void rollDice(View view) {
        Random random = new Random();
        int selection = random.nextInt(6) + 1;
        diceValue = selection;
        switch(selection) {
            case 1:
                diceView.setImageResource(R.drawable.dice1);
                turnScore = 0;
                break;
            case 2:
                diceView.setImageResource(R.drawable.dice2);
                turnScore += selection;
                break;
            case 3:
                diceView.setImageResource(R.drawable.dice3);
                turnScore += selection;
                break;
            case 4:
                diceView.setImageResource(R.drawable.dice4);
                turnScore += selection;
                break;
            case 5:
                diceView.setImageResource(R.drawable.dice5);
                turnScore += selection;
                break;
            default:
                diceView.setImageResource(R.drawable.dice6);
                turnScore += selection;
        }
        turnScoreView.setText("Turn Score: " + turnScore);
        if (currentTurn == 0 && diceValue == 1) {
            actionTextView.setText("You've rolled a 1. You lose your turn.");
            computerTurn();
        }
    }
    public void computerTurn ()  {
        currentTurn = 1;
        currentTurnTextView.setText("Current Turn: Computer");
        actionTextView.setText("Computer is taking it's turn...");
        rollButton.setClickable(false);
        holdButton.setClickable(false);
        handler.post(runnable);
    }
    public void resetGame (View view){
        turnScore = 0;
        userOverall = 0;
        computerOverall = 0;
        diceValue = 5;
        diceView.setImageResource(R.drawable.dice5);
        playScoreView.setText("Your Score: 0");
        turnScoreView.setText("Turn Score: 0");
        computerScoreView.setText("Computer Score: 0");
        currentTurnTextView.setText("Current Turn: Player");
        actionTextView.setText("");
        rollButton.setClickable(true);
        holdButton.setClickable(true);
    }
    public void holdScore (View view) {
        if (currentTurn == 0){
            userOverall += turnScore;
            turnScore = 0;
            playScoreView.setText("Your Score: " + userOverall);
            if (checkWin()) {
                actionTextView.setText("Player wins!");
                return;
            }
            computerTurn();
        }
        else {
            computerOverall += turnScore;
            turnScore = 0;
            computerScoreView.setText("Computer Score: " + computerOverall);
        }
        turnScoreView.setText("Turn Score: 0");
    }
    public boolean checkWin(){
        if (computerOverall >= 100 || userOverall >= 100) {
            rollButton.setClickable(false);
            holdButton.setClickable(false);
            return true;
        }
        return false;
    }
}
