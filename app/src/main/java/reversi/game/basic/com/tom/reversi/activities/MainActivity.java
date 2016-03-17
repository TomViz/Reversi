package reversi.game.basic.com.tom.reversi.activities;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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
import reversi.game.basic.com.tom.reversi.game_board.GameBoardLayout;
import reversi.game.basic.com.tom.reversi.game_board.GamePieceView;
import reversi.game.basic.com.tom.reversi.game_board.GameTile;
import reversi.game.basic.com.tom.reversi.game_board.TileOccupancy;
import reversi.game.basic.com.tom.reversi.utility.ActivityRouter;
import reversi.game.basic.com.tom.reversi.utility.App;
import reversi.game.basic.com.tom.reversi.utility.BroadcastRouter;
import reversi.game.basic.com.tom.reversi.utility.PlayerIconContainer;
import reversi.game.basic.com.tom.reversi.utility.ServiceRouter;
import reversi.game.basic.com.tom.reversi.utility.ShakeListener;

public class MainActivity extends AppCompatActivity implements IPresentation
{
    private static final String MY_TURN_DESCRIPTION = "Your turn";
    private static final String OPPONENT_TURN_DESCRIPTION = "Opponent's turn";

    private ImageView[] playerIcons = new ImageView[2];
    private TextView[] playerScores = new TextView[2];
    private TextView currentTurnDescription;

    private GameBoardLayout board;
    private GameBoardLayout.ITileTouchListener listener;

    private int boardSize;
    private IReversiController controller;
    private boolean isPlayerTurn;

    private SensorManager sensorMgr;
    private Sensor accelerometer;
    private ShakeListener shakeListener = new ShakeListener();

    private OpponentCommandReceiver receiver = new OpponentCommandReceiver();

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

        controller = App.getController(this);
        board = (GameBoardLayout) findViewById(R.id.gameBoard);
        findViews();

        boardSize = controller.getBoardSize();
        board.setColumnCount(boardSize);
        board.setRowCount(boardSize);
//        board.setUseDefaultMargins(true);

        for (int i = 0; i < boardSize; ++i)
        {
            for (int j = 0; j < boardSize; ++j)
            {
                GamePieceView piece = new GamePieceView(this, i, j);
                piece.setBackgroundColor(Color.WHITE);
                board.addView(piece);
            }
        }

        isPlayerTurn = getIntent().getBooleanExtra(ActivityRouter.WHO_IS_FIRST_KEY, true);
        currentTurnDescription.setText( isPlayerTurn ? MY_TURN_DESCRIPTION : OPPONENT_TURN_DESCRIPTION);

        listener = new GameBoardLayout.ITileTouchListener()
        {
            @Override
            public void onTileTouch(int row, int col)
            {
                board.setOnTileTouchListener(null);
                controller.onTileTouch(row, col);
                Log.d("Listener", "User has touched row #" + row + " and column #" + col);
                ServiceRouter.startSendService(MainActivity.this, row, col);
            }
        };

        board.setOnTileTouchListener( isPlayerTurn ? listener : null);

    }

    @Override
    protected void onResume()
    {
        super.onResume();
        sensorMgr.registerListener(shakeListener, accelerometer, SensorManager.SENSOR_DELAY_UI);
        IntentFilter filter = new IntentFilter(BroadcastRouter.MOVE_SEND);
        LocalBroadcastManager.getInstance(this).registerReceiver(receiver, filter);
        controller.setup();
    }

    @Override
    protected void onPause()
    {
        sensorMgr.unregisterListener(shakeListener);
        LocalBroadcastManager.getInstance(this).unregisterReceiver(receiver);
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
                for (GameTile tile : tiles)
                {
                    GamePieceView view = (GamePieceView) board.getChildAt(getLinearIndex(tile.getRow(), tile.getColumn()));
                    view.setImageBitmap(PlayerIconContainer.getIcon(tile.getPlayer()));
                }
            }
        });
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
                board.setOnTileTouchListener( isPlayerTurn ? listener : null);
                currentTurnDescription.setText( isPlayerTurn ? MY_TURN_DESCRIPTION : OPPONENT_TURN_DESCRIPTION);
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
                Toast toast = Toast.makeText(MainActivity.this, "Illegal move, try again!", Toast.LENGTH_SHORT);
                toast.show();
                board.setOnTileTouchListener(listener);
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

    private void changePlayerScore(int playerIndex, int numOfTiles)
    {
        playerScores[playerIndex].setText(String.valueOf(numOfTiles));
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

    private int getLinearIndex(int row, int col)
    {
        return ( (row * boardSize) + col );
    }

    private class OpponentCommandReceiver extends BroadcastReceiver
    {
        @Override
        public void onReceive(Context context, Intent intent)
        {
            Log.d("Receiver", "Received opponent move!");
            if (BroadcastRouter.MOVE_SEND.equals(intent.getAction()))
            {
                int row = intent.getIntExtra(BroadcastRouter.BROADCAST_ROW_KEY, -1);
                int column = intent.getIntExtra(BroadcastRouter.BROADCAST_COLUMN_KEY, -1);
                Log.d("Receiver", "Opponent has touched row " + row + " and column " + column);
                controller.onTileTouch(row, column);
            }
        }
    }
}
