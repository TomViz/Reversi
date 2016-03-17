package reversi.game.basic.com.tom.reversi.network;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Class for handling host connection setup and message sending requests.
 */
public class HostConnection
{
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

    public void close() throws IOException
    {
        socket.close();
        server.close();
    }
}
