package reversi.game.basic.com.tom.reversi.activities;

import android.app.AlertDialog;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import reversi.game.basic.com.tom.reversi.fragments.ConnectionErrorDialogFragment;
import reversi.game.basic.com.tom.reversi.game_board.GameTile;
import reversi.game.basic.com.tom.reversi.game_board.TileOccupancy;
import reversi.game.basic.com.tom.reversi.network.ConnectionHandler;
import reversi.game.basic.com.tom.reversi.utility.App;
import reversi.game.basic.com.tom.reversi.utility.PlayerIconContainer;
import reversi.game.basic.com.tom.reversi.utility.ShakeListener;

public class MainActivity extends AppCompatActivity implements IPresentation
{
    public static final String WHO_IS_FIRST_KEY = "FIRST";

    private static final String MY_TURN_DESCRIPTION = "Your turn";
    private static final String OPPONENT_TURN_DESCRIPTION = "Opponent's turn";

    private ImageView[] playerIcons = new ImageView[2];
    private TextView[] playerScores = new TextView[2];
    private TextView currentTurnDescription;

    private BoardFragment board;

    private IReversiController controller;
    private boolean isPlayerTurn;

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
                new AlertDialog.Builder(MainActivity.this).
                        setTitle("Shake detected").
                        setMessage("Whoa there, slow down!").
                        setIcon(android.R.drawable.ic_dialog_alert).
                        setPositiveButton("Shame!", null).
                        create().show();
            }
        });

        controller = App.getController(this, true);
        board = new BoardFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.customBoardLayout, board).addToBackStack(null).commit();
        findViews();

        isPlayerTurn = getIntent().getBooleanExtra(WHO_IS_FIRST_KEY, true);
        currentTurnDescription.setText( isPlayerTurn ? MY_TURN_DESCRIPTION : OPPONENT_TURN_DESCRIPTION);
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        sensorMgr.registerListener(shakeListener, accelerometer, SensorManager.SENSOR_DELAY_GAME);
//        App.register(controller);
        App.registerController();
        board.enableTileTouchListener(isPlayerTurn);
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
                if (! isPlayerTurn)
                {
                    return;
                }

                board.setLegalTiles(tiles);
            }
        });
    }

    @Override
    public void displayCurrentMove(GameTile tile)
    {
        ConnectionHandler.sendCommand(tile.getRow(), tile.getColumn());
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
                board.enableTileTouchListener(isPlayerTurn);
                currentTurnDescription.setText(isPlayerTurn ? MY_TURN_DESCRIPTION : OPPONENT_TURN_DESCRIPTION);
            }
        });
    }

    @Override
    public void notifyIllegalMove()
    {
        if (! isPlayerTurn)
        {
            return;
        }

        runOnUiThread(new Runnable()
        {
            @Override
            public void run()
            {
                Toast toast = Toast.makeText(MainActivity.this, "Illegal move, try again!", Toast.LENGTH_SHORT);
                toast.show();
                board.enableTileTouchListener(true);
            }
        });
    }

    @Override
    public void notifyVictory()
    {
        runOnUiThread(new Runnable()
        {
            @Override
            public void run()
            {
                Toast toast = Toast.makeText(MainActivity.this, "Congratulations! You have won!", Toast.LENGTH_SHORT);
                toast.show();
            }
        });
    }

    @Override
    public void notifyLoss()
    {
        runOnUiThread(new Runnable()
        {
            @Override
            public void run()
            {
                Toast toast = Toast.makeText(MainActivity.this, "Haha, what a LOSER!!!", Toast.LENGTH_SHORT);
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
                Toast toast = Toast.makeText(MainActivity.this, "A tie! Well that's rare...", Toast.LENGTH_SHORT);
                toast.show();
            }
        });
    }

    @Override
    public void notifyError()
    {
        runOnUiThread(new Runnable()
        {
            @Override
            public void run()
            {
                new ConnectionErrorDialogFragment().show(getFragmentManager(), null);
            }
        });
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
