package reversi.game.basic.com.tom.reversi.trash;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import reversi.game.basic.com.tom.reversi.trash.TCPConnectionService;

/**
 * Utility class to handle requests for service start.
 */
public final class ServiceRouter
{
    /***********************************************/
    /************** Available actions **************/
    /***********************************************/

    // Request to host game (Wait for connection)
    public static final String ACTION_HOST = "reversi.game.basic.com.tom.reversi.utility.action.HOST";

    // Request to join game (Search for connection)
    public static final String ACTION_JOIN = "reversi.game.basic.com.tom.reversi.utility.action.JOIN";

    // Request to send performed action (row + column of placed tile).
    public static final String ACTION_SEND = "reversi.game.basic.com.tom.reversi.utility.action.SEND";

    public static final String SERVICE_IP = "IP";

    public static final String SERVICE_ROW_KEY = "ROW";
    public static final String SERVICE_COLUMN_KEY = "COL";

    public static void startHostService(Context context)
    {
        Log.d("HOST", "Requested host service");
        Intent intent = new Intent(context, TCPConnectionService.class);
        intent.setAction(ACTION_HOST);
        context.startService(intent);
//        App.getInstance().startService(intent);
    }

    public static void startJoinService(Context context, String ipAddress)
    {
        Log.d("JOIN", "Requested join service");
        Intent intent = new Intent(context, TCPConnectionService.class);
        intent.setAction(ACTION_JOIN);
        intent.putExtra(SERVICE_IP, ipAddress);
        context.startService(intent);
    }

    public static void startSendService(Context context, int row, int column)
    {
        Log.d("SEND", "Requested send service");
        Intent intent = new Intent(context, TCPConnectionService.class);
        intent.setAction(ACTION_SEND);
        intent.putExtra(SERVICE_ROW_KEY, row);
        intent.putExtra(SERVICE_COLUMN_KEY, column);
        context.startService(intent);
    }
}
