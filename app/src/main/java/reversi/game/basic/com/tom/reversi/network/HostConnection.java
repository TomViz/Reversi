package reversi.game.basic.com.tom.reversi.network;

import android.util.Log;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import reversi.game.basic.com.tom.reversi.events.EventBus;
import reversi.game.basic.com.tom.reversi.events.EventTypes;
import reversi.game.basic.com.tom.reversi.events.SimpleEvent;

/**
 * Class for handling host connection setup and message sending requests.
 */
public class HostConnection implements IConnection
{
    private static final String HOST_TAG = "Host";

    private ServerSocket server;
    private Socket socket;
    private int port;
    private final ExecutorService POOL = Executors.newFixedThreadPool(2);

    public HostConnection(int port)
    {
        this.port = port;
    }

    public void start() throws IOException
    {
        server = new ServerSocket(port);
        socket = server.accept();
        POOL.execute(new TCPListener(socket));
    }

    @Override
    public void sendCoordinates(int row, int column)
    {
        try
        {
//            TCPMessageSender.send(socket, row, column);
            PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
            out.println(JSONHandler.getMessageSendingString(row, column));
        }
        catch (IOException e)
        {
            Log.e(HOST_TAG, "Failed to send message");
            EventBus.sendEvent(new SimpleEvent(EventTypes.DISCONNECTION));
        }
    }

    public void close()
    {
        try
        {
            socket.close();
            server.close();
        }
        catch (IOException e)
        {
            Log.e(HOST_TAG, "Failed to close resources");
        }
    }
}
