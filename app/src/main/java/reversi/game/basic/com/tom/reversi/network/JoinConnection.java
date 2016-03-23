package reversi.game.basic.com.tom.reversi.network;

import android.util.Log;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import reversi.game.basic.com.tom.reversi.controller.ControllerBus;
import reversi.game.basic.com.tom.reversi.controller.Event;
import reversi.game.basic.com.tom.reversi.controller.EventTypes;

/**
 * Class for handling join connection setup and message sending requests.
 */
public class JoinConnection implements IConnection
{
    private static final String JOIN_TAG = "Join";

    private Socket socket;
    private String ipAddress;
    private int port;
    private final ExecutorService POOL = Executors.newFixedThreadPool(2);

    public JoinConnection(String ipAddress, int port)
    {
        this.ipAddress = ipAddress;
        this.port = port;
    }

    public void start() throws IOException
    {
        InetAddress address = InetAddress.getByName(ipAddress);
        socket = new Socket(address, port);
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
            Log.e(JOIN_TAG, "Failed to send message");
            ControllerBus.sendEvent(new Event(EventTypes.DISCONNECTION));
        }
    }

    public void close()
    {
        try
        {
            socket.close();
        }
        catch (IOException e)
        {
            Log.e(JOIN_TAG, "Failed to close resources");
        }
    }
}
