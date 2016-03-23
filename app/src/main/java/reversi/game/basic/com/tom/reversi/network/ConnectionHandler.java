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
    private static final String NEW_CONNECT_TAG = "Connect";
    private static final String SEND_TAG = "Send";

    private static final int PORT = 9000;

    private static IConnection connection;
    private static final ExecutorService POOL = Executors.newFixedThreadPool(2);

    /**
     * Factory method for starting a new connection.
     * @param type Type of connection that needs to be established.
     * @param ip IP Address to join. Note that this parameter is ignored if "Host" connection is requested.
     */
    public static void startConnection(final ConnectionTypes type, final String ip)
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

                    switch(type)
                    {
                        case HOST:
                            connection = new HostConnection(PORT);
                            break;
                        case JOIN:
                            connection = new JoinConnection(ip, PORT);
                            break;
                        default:
                            Log.e(NEW_CONNECT_TAG, "Unknown connection request type");
                            throw new IOException();
                    }

                    connection.start();
                    BroadcastRouter.sendToTitleScreen(type.getBroadcastTarget());
                }
                catch (IOException e)
                {
                    Log.e(NEW_CONNECT_TAG, "Failed to initiate connection service");
                    BroadcastRouter.sendToTitleScreen(BroadcastRouter.START_FAILED);
                }
            }
        });
    }

    public static IConnection getConnection()
    {
        return connection;
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
                    Log.d(SEND_TAG, "Bla bla bla");
                }

                connection.sendCoordinates(row, column);
            }
        });
    }
}
