package reversi.game.basic.com.tom.reversi.network;

import android.util.Log;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import reversi.game.basic.com.tom.reversi.utility.BroadcastRouter;

/**
 * Utility class for handling TCP connections (Host/Join requests, and message passing)
 */
public final class ConnectionHandler
{
    private static final String HOST_TAG = "Host";
    private static final String JOIN_TAG = "Join";

    private static IConnection connection;
    private static final ExecutorService POOL = Executors.newFixedThreadPool(2);

    public static void startHost(final int port)
    {
        POOL.execute(new Runnable()
        {
            @Override
            public void run()
            {
                try
                {
                    if (connection != null)
                    {
                        connection.close();
                    }

                    connection = new HostConnection(port);
                    connection.start();
                    BroadcastRouter.sendToTitleScreen(BroadcastRouter.START_FIRST);
                }
                catch (IOException e)
                {
                    Log.e(HOST_TAG, "Failed to initiate host service");
                    BroadcastRouter.sendToTitleScreen(BroadcastRouter.START_FAILED);
                }
            }
        });
    }

    public static void startJoin(final String ip, final int port)
    {
        POOL.execute(new Runnable()
        {
            @Override
            public void run()
            {
                try
                {
                    if (connection != null)
                    {
                        connection.close();
                    }

                    connection = new JoinConnection(ip, port);
                    connection.start();
                    BroadcastRouter.sendToTitleScreen(BroadcastRouter.START_SECOND);
                }
                catch (IOException e)
                {
                    Log.e(JOIN_TAG, "Failed to initiate host service");
                    BroadcastRouter.sendToTitleScreen(BroadcastRouter.START_FAILED);
                }
            }
        });
    }

    public static void sendCommand(final int row, final int column)
    {
        POOL.execute(new Runnable()
        {
            @Override
            public void run()
            {
                if (connection == null)
                {
                    // TODO Broadcast "message sending failed"
                }

                connection.sendCoordinates(row, column);
            }
        });
    }
}
