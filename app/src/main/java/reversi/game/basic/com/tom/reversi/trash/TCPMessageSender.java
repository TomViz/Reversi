package reversi.game.basic.com.tom.reversi.trash;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

import reversi.game.basic.com.tom.reversi.network.JSONHandler;

/**
 * Created by Tom Vizel on 21/03/2016.
 */
public final class TCPMessageSender
{
    public static void send(Socket socket, int row, int column) throws IOException
    {
        PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
        out.println(JSONHandler.getMessageSendingString(row, column));
    }
}
