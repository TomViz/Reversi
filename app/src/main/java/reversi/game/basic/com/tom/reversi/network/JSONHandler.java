package reversi.game.basic.com.tom.reversi.network;

import android.graphics.Point;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import reversi.game.basic.com.tom.reversi.utility.ServiceRouter;

/**
 * Utility class for handling JSONs utilized in this project's scope.
 */
public class JSONHandler
{
    private static final String COMMAND_KEY = "COMMAND";
    private static final String ROW_KEY = "ROW";
    private static final String COLUMN_KEY = "COL";

    private static final String ACTION_HOST = "Host";
    private static final String ACTION_JOIN = "Join";
    private static final String ACTION_SEND = "Send";

    private static final String JSON_TAG = "JSON Error";
    private static final String JSON_ERROR = "Failed to create JSON Object";

    private static final JSONObject HOST_JSON = new JSONObject();
    private static final JSONObject JOIN_JSON = new JSONObject();
    static
    {
        try
        {
            HOST_JSON.put(COMMAND_KEY, ACTION_HOST);
            JOIN_JSON.put(COMMAND_KEY, ACTION_JOIN);
        }
        catch (JSONException e)
        {
            Log.e(JSON_TAG, JSON_ERROR);
        }
    }

    public static String getHostString()
    {
        return HOST_JSON.toString();
    }

    public static String getJoinString()
    {
        return JOIN_JSON.toString();
    }

    public static String getMessageSendingString(int row, int column)
    {
        try
        {
            JSONObject sendObject = new JSONObject();
            sendObject.put(COMMAND_KEY, ACTION_SEND);
            sendObject.put(ROW_KEY, row);
            sendObject.put(COLUMN_KEY, column);
            return sendObject.toString();
        }
        catch (JSONException e)
        {
            Log.e(JSON_TAG, Log.getStackTraceString(e));
            return null;
        }
    }

    /**
     * Retrieves coordinates from JSON.
     * @param jsonString String from sent JSON meant to contain coordinates.
     * @return Coordinates (x = row, y = column) if legal, null otherwise.
     */
    public static Point getCoordinates(String jsonString)
    {
        try
        {
            JSONObject recObject = new JSONObject(jsonString);
            if (ACTION_SEND.equals(recObject.getString(COMMAND_KEY)))
            {
                Point p = new Point();
                p.x = recObject.getInt(ROW_KEY);
                p.y = recObject.getInt(COLUMN_KEY);
                return p;
            }
        }
        catch (JSONException e)
        {
            Log.e(JSON_TAG, JSON_ERROR);
        }

        return null;
    }
}
