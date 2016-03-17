package reversi.game.basic.com.tom.reversi.debug;

import android.app.AlertDialog;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.List;

import reversi.game.basic.com.tom.reversi.R;
import reversi.game.basic.com.tom.reversi.activities.IPresentation;
import reversi.game.basic.com.tom.reversi.controller.IReversiController;
import reversi.game.basic.com.tom.reversi.fragments.AboutDialogFragment;
import reversi.game.basic.com.tom.reversi.game_board.GameBoardLayout;
import reversi.game.basic.com.tom.reversi.game_board.GamePieceView;
import reversi.game.basic.com.tom.reversi.game_board.GameTile;
import reversi.game.basic.com.tom.reversi.utility.App;
import reversi.game.basic.com.tom.reversi.utility.PlayerIconContainer;
import reversi.game.basic.com.tom.reversi.utility.ShakeListener;

/**
 * Main activity for debug purposes (No network component in place).
 */
public class OfflineMainActivity extends AppCompatActivity implements IPresentation
{
    private int boardSize;
    private GameBoardLayout board;
    private IReversiController controller;

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

        board = (GameBoardLayout) findViewById(R.id.gameBoard);
        controller = App.getController(this);

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

        board.setOnTileTouchListener(new GameBoardLayout.ITileTouchListener()
        {
            @Override
            public void onTileTouch(int row, int col)
            {
                controller.onTileTouch(row, col);
            }
        });

    }

    @Override
    protected void onResume()
    {
        super.onResume();
        sensorMgr.registerListener(shakeListener, accelerometer, SensorManager.SENSOR_DELAY_UI);
        controller.setup();
    }

    @Override
    protected void onPause()
    {
        sensorMgr.unregisterListener(shakeListener);
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
        // Intentionally left blank
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
    public void setNumberOfPlayer1Tiles(int numOfTiles)
    {
        // Blank
    }

    @Override
    public void setNumberOfPlayer2Tiles(int numOfTiles)
    {
        // Blank
    }

    private int getLinearIndex(int row, int col)
    {
        return ( (row * boardSize) + col );
    }
}
