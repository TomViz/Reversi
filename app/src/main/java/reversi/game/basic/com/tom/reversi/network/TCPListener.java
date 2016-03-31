package reversi.game.basic.com.tom.reversi.network;

import android.graphics.Point;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

import reversi.game.basic.com.tom.reversi.events.EventBus;
import reversi.game.basic.com.tom.reversi.events.EventTypes;
import reversi.game.basic.com.tom.reversi.events.MoveEvent;
import reversi.game.basic.com.tom.reversi.events.SimpleEvent;

/**
 * Class intended to be run as a listener thread and passing along the incoming messages via broadcast.
 */
public class TCPListener implements Runnable
{
    private Socket socket;

    public TCPListener(Socket socket)
    {
        this.socket = socket;
    }

    @Override
    public void run()
    {
        try
        {
            String line;
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            while ( (! Thread.currentThread().isInterrupted()) && ((line = in.readLine()) != null) )
            {
                Point p = JSONHandler.getCoordinates(line);
                if (p != null)
                {
//                    BroadcastRouter.sendMoveFromOtherPlayer(p.x, p.y);
//                    App.sendCoordinates(p.x, p.y);
                    EventBus.sendEvent(new MoveEvent(EventTypes.MESSAGE_SEND, p.x, p.y));
                }
                // TODO else broadcast received illegal command?
            }
        }
        catch (IOException e)
        {
//            Log.e(CONNECTION_TAG, Log.getStackTraceString(e));
        }
        finally
        {
            closeStream();
            EventBus.sendEvent(new SimpleEvent(EventTypes.DISCONNECTION));
        }
    }

    private void closeStream()
    {
        try
        {
            socket.shutdownInput();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}
