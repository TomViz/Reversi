package reversi.game.basic.com.tom.reversi.activities;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import reversi.game.basic.com.tom.reversi.R;
import reversi.game.basic.com.tom.reversi.fragments.AboutDialogFragment;
import reversi.game.basic.com.tom.reversi.fragments.JoinDialogFragment;
import reversi.game.basic.com.tom.reversi.utility.ActivityRouter;
import reversi.game.basic.com.tom.reversi.utility.App;
import reversi.game.basic.com.tom.reversi.utility.BroadcastRouter;
import reversi.game.basic.com.tom.reversi.utility.ServiceRouter;

public class TitleScreenActivity extends AppCompatActivity
{
    private Button joinButton;
    private Button hostButton;

    private ProgressDialog dialog;

//    private final ExecutorService POOL = Executors.newSingleThreadExecutor();
    private ConnectionReceiver receiver = new ConnectionReceiver();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_title_screen);

        initDialog();

        joinButton = (Button) findViewById(R.id.joinButton);
        joinButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                enableButtons(false);
                showDialog("(Join) Finding another player. Please wait...");
                new JoinDialogFragment().show(getFragmentManager(), "join");
            }
        });

        hostButton = (Button) findViewById(R.id.hostButton);
        hostButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                enableButtons(false);
                showDialog("("+ App.getIPAddress() +") Waiting for another player. Please wait...");
                ServiceRouter.startHostService(TitleScreenActivity.this);
            }
        });
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        IntentFilter filter = new IntentFilter();
        filter.addAction(BroadcastRouter.START_FIRST);
        filter.addAction(BroadcastRouter.START_SECOND);
        filter.addAction(BroadcastRouter.START_FAILED);
        LocalBroadcastManager.getInstance(this).registerReceiver(receiver, filter);
    }

    @Override
    protected void onPause()
    {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(receiver);
        super.onPause();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.title_screen_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.about:
                new AboutDialogFragment().show(getFragmentManager(), "about");
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void enableButtons(boolean enabled)
    {
        joinButton.setEnabled(enabled);
        hostButton.setEnabled(enabled);
    }

    private void initDialog()
    {
        dialog = new ProgressDialog(this);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setIndeterminate(true);
        dialog.setCanceledOnTouchOutside(false);
    }

    private void showDialog(String message)
    {
        dialog.setMessage(message);
        dialog.show();
    }

    private void hideDialog()
    {
        dialog.dismiss();
    }

    private class ConnectionReceiver extends BroadcastReceiver
    {
        @Override
        public void onReceive(Context context, Intent intent)
        {
            Log.d("Broadcast", "Received broadcast!");
            String action = intent.getAction();
            switch (action)
            {
                case BroadcastRouter.START_FIRST:
                    ActivityRouter.startGame(TitleScreenActivity.this, true);
                    break;

                case BroadcastRouter.START_SECOND:
                    ActivityRouter.startGame(TitleScreenActivity.this, false);
                    break;

                case BroadcastRouter.START_FAILED:
                    hideDialog();
                    enableButtons(true);
                    new AlertDialog.Builder(TitleScreenActivity.this).
                            setTitle("Timeout").
                            setMessage("Failed to connect with the opposing player, please try again.").
                            setIcon(android.R.drawable.ic_dialog_alert).
                            setPositiveButton("OK", null).
                            create().
                            show();
                    break;

                default:
                    break;
            }
        }
    }
}
