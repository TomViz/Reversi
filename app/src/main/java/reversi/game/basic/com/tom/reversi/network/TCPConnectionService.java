package reversi.game.basic.com.tom.reversi.network;

import android.app.IntentService;
import android.content.Intent;
//import android.os.PowerManager;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import reversi.game.basic.com.tom.reversi.utility.BroadcastRouter;
import reversi.game.basic.com.tom.reversi.utility.ServiceRouter;

public class TCPConnectionService extends IntentService
{
    /***********************************************/
    /************* Connection Details **************/
    /***********************************************/

    // Hard coded server details
    private static final int SERVER_PORT = 9000;
//    private static final String SERVER_IP_ADDRESS = "192.168.1.17";

    private static final String COMMAND_KEY = "COMMAND";
    private static final String ROW_KEY = "ROW";
    private static final String COLUMN_KEY = "COL";

    private static final String JSON_TAG = "JSON Error";
    private static final String JSON_ERROR = "Failed to create JSON Object";

    private static final JSONObject HOST_JSON = new JSONObject();
    private static final JSONObject JOIN_JSON = new JSONObject();
    static
    {
        try
        {
            HOST_JSON.put(COMMAND_KEY, ServiceRouter.ACTION_HOST);
            JOIN_JSON.put(COMMAND_KEY, ServiceRouter.ACTION_JOIN);
        }
        catch (JSONException e)
        {
            Log.e(JSON_TAG, JSON_ERROR);
        }
    }

    /***********************************************/
    /******************** Code *********************/
    /***********************************************/

    private static final String CONNECTION_TAG = "TCP";

    private final ExecutorService POOL = Executors.newSingleThreadExecutor();

    // Created during connection and remain alive until shutdown is requested(?).
    private static ServerSocket server;
    private static Socket socket;
    private static PrintWriter out;
//    private static PowerManager.WakeLock keepAwake;


//    private static final String TAG_LOCK = "Still Alive";

    public TCPConnectionService()
    {
        super("TCPConnectionService");
    }

    @Override
    protected void onHandleIntent(Intent intent)
    {
        Log.d("Service", "Started service!");
        if (intent == null)
        {
            return;
        }

        final String action = intent.getAction();
        switch (action)
        {
            case ServiceRouter.ACTION_JOIN:
                Log.d("Service", "Join service");
                handleJoin(intent.getStringExtra(ServiceRouter.SERVICE_IP));
                break;

            case ServiceRouter.ACTION_HOST:
                Log.d("Service", "Host service");
                handleHost();
                break;

            case ServiceRouter.ACTION_SEND:
                handleSend(intent.getIntExtra(ServiceRouter.SERVICE_ROW_KEY, -1), intent.getIntExtra(ServiceRouter.SERVICE_COLUMN_KEY, -1));
                break;

            default:
                break;
        }
    }

    /**
     * Act as client and join server.
     */
    private void handleJoin(String ipAddress)
    {
        try
        {
            InetAddress address = InetAddress.getByName(ipAddress);
            socket = new Socket(address, SERVER_PORT);
            Log.d(CONNECTION_TAG, "TCP Join connection established");
            startListener();
            BroadcastRouter.sendToTitleScreen(BroadcastRouter.START_SECOND);
        }
        catch (IOException e)
        {
            Log.e(CONNECTION_TAG, Log.getStackTraceString(e));
            BroadcastRouter.sendToTitleScreen(BroadcastRouter.START_FAILED);
        }
    }

    /**
     * Act as server and await client connection.
     */
    private void handleHost()
    {
        try
        {
            server = new ServerSocket(SERVER_PORT);
            socket = server.accept();
            Log.d(CONNECTION_TAG, "TCP Host connection established");
            startListener();
            BroadcastRouter.sendToTitleScreen(BroadcastRouter.START_FIRST);
        }
        catch (IOException e)
        {
            Log.e(CONNECTION_TAG, Log.getStackTraceString(e));
        }
    }

    private void startListener() throws IOException
    {
        POOL.execute(new OpponentListener());
        out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
//        PowerManager pm = (PowerManager) getSystemService(POWER_SERVICE);
//        keepAwake = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, TAG_LOCK);
//        keepAwake.acquire();
    }

    /**
     * Send JSON containing move coordinates through the socket.
     * @param row Row coordinate [0..n]
     * @param column Column coordinate [0..n]
     */
    private void handleSend(int row, int column)
    {
        try
        {
            JSONObject toSend = prepareSendObject(row, column);
            out.println(toSend.toString());
        }
        catch (JSONException e)
        {
            Log.e(JSON_TAG, Log.getStackTraceString(e));
        }
    }

    private JSONObject prepareSendObject(int row, int column) throws JSONException
    {
        JSONObject sendObject = new JSONObject();
        sendObject.put(COMMAND_KEY, ServiceRouter.ACTION_SEND);
        sendObject.put(ROW_KEY, row);
        sendObject.put(COLUMN_KEY, column);
        return sendObject;
    }

    private class OpponentListener implements Runnable
    {
        private BufferedReader in;

        @Override
        public void run()
        {
            try
            {
                String line; // IDE screams "redundant" if set as null.
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                while ( (! Thread.currentThread().isInterrupted()) && ((line = in.readLine()) != null) )
                {
                    JSONObject incoming = new JSONObject(line);
                    if (ServiceRouter.ACTION_SEND.equals(incoming.getString(COMMAND_KEY)))
                    {
                        int row = incoming.getInt(ROW_KEY);
                        int column = incoming.getInt(COLUMN_KEY);
                        BroadcastRouter.sendMoveFromOtherPlayer(row, column);
                    }
                }

            }
            catch (IOException e)
            {
                Log.e(CONNECTION_TAG, Log.getStackTraceString(e));
            }
            catch (JSONException e)
            {
                Log.e(JSON_TAG, Log.getStackTraceString(e));
            }
            finally
            {
                closeStreams();
                closeSockets();
//                keepAwake.release();
            }
        }

        private void closeStreams()
        {
            try
            {
                if (in != null)
                {
                    in.close();
                }

                if (out != null)
                {
                    out.close();
                }
            }
            catch (IOException e)
            {
                Log.e(CONNECTION_TAG, Log.getStackTraceString(e));
            }
        }

        private void closeSockets()
        {
            try
            {
                if (socket != null)
                {
                    socket.close();
                }

                if (server != null)
                {
                    server.close();
                }
            }
            catch (IOException e)
            {
                Log.e(CONNECTION_TAG, Log.getStackTraceString(e));
            }
        }
    }
}
