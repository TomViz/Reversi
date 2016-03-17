package reversi.game.basic.com.tom.reversi.network;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Class for handling join connection setup and message sending requests.
 */
public class JoinConnection
{
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

    public void close() throws IOException
    {
        socket.close();
    }
}
