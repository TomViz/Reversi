package reversi.game.basic.com.tom.reversi.utility;

import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

/**
 * Utility class for handling broadcast sending and receiving.
 */
public final class BroadcastRouter
{
    public static final String START_FIRST = "FIRST";
    public static final String START_SECOND = "SECOND";
    public static final String START_FAILED = "FAILED";

    public static final String MOVE_SEND = "MOVE";
    public static final String BROADCAST_ROW_KEY = "ROW";
    public static final String BROADCAST_COLUMN_KEY = "COL";

    public static void sendToTitleScreen(String action)
    {
        Intent intent = new Intent(action);
        LocalBroadcastManager.getInstance(App.getInstance()).sendBroadcast(intent);
    }

    public static void sendMoveFromOtherPlayer(int row, int column)
    {
        Intent intent = new Intent(MOVE_SEND);
        intent.putExtra(BROADCAST_ROW_KEY, row);
        intent.putExtra(BROADCAST_COLUMN_KEY, column);
        LocalBroadcastManager.getInstance(App.getInstance()).sendBroadcast(intent);
    }
}
