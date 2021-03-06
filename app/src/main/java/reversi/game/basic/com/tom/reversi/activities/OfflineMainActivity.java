package reversi.game.basic.com.tom.reversi.activities;

import android.app.AlertDialog;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import reversi.game.basic.com.tom.reversi.R;
import reversi.game.basic.com.tom.reversi.controller.IReversiController;
import reversi.game.basic.com.tom.reversi.fragments.AboutDialogFragment;
import reversi.game.basic.com.tom.reversi.game_board.GameTile;
import reversi.game.basic.com.tom.reversi.game_board.TileOccupancy;
import reversi.game.basic.com.tom.reversi.utility.App;
import reversi.game.basic.com.tom.reversi.utility.PlayerIconContainer;
import reversi.game.basic.com.tom.reversi.utility.ShakeListener;

/**
 * Main activity for debug purposes (No network component in place).
 */
public class OfflineMainActivity extends AppCompatActivity implements IPresentation
{
    private static final String P1_TURN_DESCRIPTION = "Putin's turn";
    private static final String P2_TURN_DESCRIPTION = "Obama's turn";

    private BoardFragment board;
    private IReversiController controller;
    private boolean isPlayerTurn;

    private ImageView[] playerIcons = new ImageView[2];
    private TextView[] playerScores = new TextView[2];
    private TextView currentTurnDescription;

    private SensorManager sensorMgr;
    private Sensor accelerometer;
    private ShakeListener shakeListener = new ShakeListener();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sensorMgr = (SensorManager) getSystemService(SENSOR_SERVICE);
        accelerometer = sensorMgr.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        shakeListener.setOnShakeListener(new ShakeListener.OnShakeListener()
        {
            @Override
            public void onShake()
            {
                new AlertDialog.Builder(OfflineMainActivity.this).
                        setTitle("Shake detected").
                        setMessage("Whoa there, slow down!").
                        setIcon(android.R.drawable.ic_dialog_alert).
                        setPositiveButton("Shame!", null).
                        create().show();
            }
        });


        board = new BoardFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.customBoardLayout, board).addToBackStack(null).commit();
        controller = App.getController(this, false);
        findViews();

        isPlayerTurn = true;
        currentTurnDescription.setText(P1_TURN_DESCRIPTION);
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        sensorMgr.registerListener(shakeListener, accelerometer, SensorManager.SENSOR_DELAY_UI);
//        App.register(controller);
        App.registerController();
        controller.setup();
    }

    @Override
    protected void onPause()
    {
        sensorMgr.unregisterListener(shakeListener);
//        App.unregister(controller);
        App.unregisterController();
        super.onPause();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.settings:
                return true;

            case R.id.about:
                new AboutDialogFragment().show(getFragmentManager(), "about");
                return true;

            default:
                return super.onOptionsItemSelected(item);

        }
    }

    @Override
    public void setTiles(final List<GameTile> tiles)
    {
        runOnUiThread(new Runnable()
        {
            @Override
            public void run()
            {
                board.setTiles(tiles);
            }
        });
    }

    @Override
    public void setLegalTiles(final List<GameTile> tiles)
    {
        runOnUiThread(new Runnable()
        {
            @Override
            public void run()
            {
                board.clearLegalTiles();
                board.setLegalTiles(tiles);
            }
        });
    }

    @Override
    public void displayCurrentMove(GameTile tile)
    {
        // Intentionally blank.
    }

    @Override
    public void playerChange()
    {
        runOnUiThread(new Runnable()
        {
            @Override
            public void run()
            {
                isPlayerTurn = !isPlayerTurn;
                currentTurnDescription.setText(isPlayerTurn ? P1_TURN_DESCRIPTION : P2_TURN_DESCRIPTION);
            }
        });
    }

    @Override
    public void notifyIllegalMove()
    {
        runOnUiThread(new Runnable()
        {
            @Override
            public void run()
            {
                Toast toast = Toast.makeText(OfflineMainActivity.this, "Illegal move, try again!", Toast.LENGTH_SHORT);
                toast.show();
            }
        });
    }

    @Override
    public void notifyVictory()
    {
        offlineResult();
    }

    @Override
    public void notifyLoss()
    {
        offlineResult();
    }

    private void offlineResult()
    {
        runOnUiThread(new Runnable()
        {
            @Override
            public void run()
            {
//                String message = (isPlayerTurn) ? "Putin is the winner!" : "Obama is the winner!";
                String message = "No more possible moves!";
                Toast toast = Toast.makeText(OfflineMainActivity.this, message, Toast.LENGTH_SHORT);
                toast.show();
            }
        });
    }

    @Override
    public void notifyTie()
    {
        runOnUiThread(new Runnable()
        {
            @Override
            public void run()
            {
                Toast toast = Toast.makeText(OfflineMainActivity.this, "A tie! Well that's rare...", Toast.LENGTH_SHORT);
                toast.show();
            }
        });
    }

    @Override
    public void notifyError()
    {
        // Intentionally blank.
    }

    @Override
    public void setNumberOfPlayer1Tiles(int numOfTiles)
    {
        changePlayerScore(0, numOfTiles);
    }

    @Override
    public void setNumberOfPlayer2Tiles(int numOfTiles)
    {
        changePlayerScore(1, numOfTiles);
    }

    private void changePlayerScore(final int playerIndex, final int numOfTiles)
    {
        runOnUiThread(new Runnable()
        {
            @Override
            public void run()
            {
                playerScores[playerIndex].setText(String.valueOf(numOfTiles));
            }
        });
    }

    private void findViews()
    {
        View playerOneLayout = findViewById(R.id.playerOneStats);
        playerIcons[0] = (ImageView) playerOneLayout.findViewById(R.id.playerImage);
        playerIcons[0].setImageBitmap(PlayerIconContainer.getIcon(TileOccupancy.P1));
        playerScores[0] = (TextView) playerOneLayout.findViewById(R.id.playerScore);

        View playerTwoLayout = findViewById(R.id.playerTwoStats);
        playerIcons[1] = (ImageView) playerTwoLayout.findViewById(R.id.playerImage);
        playerIcons[1].setImageBitmap(PlayerIconContainer.getIcon(TileOccupancy.P2));
        playerScores[1] = (TextView) playerTwoLayout.findViewById(R.id.playerScore);

        currentTurnDescription = (TextView) findViewById(R.id.turnDescription);
    }
}
