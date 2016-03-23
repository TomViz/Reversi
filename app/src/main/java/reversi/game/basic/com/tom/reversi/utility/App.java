package reversi.game.basic.com.tom.reversi.utility;

import android.app.Application;
import android.net.wifi.WifiManager;
import android.util.Log;

import java.math.BigInteger;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.ByteOrder;

import reversi.game.basic.com.tom.reversi.activities.IPresentation;
import reversi.game.basic.com.tom.reversi.controller.ControllerBus;
import reversi.game.basic.com.tom.reversi.controller.EventTypes;
import reversi.game.basic.com.tom.reversi.controller.GameController;
import reversi.game.basic.com.tom.reversi.controller.IReversiController;
import reversi.game.basic.com.tom.reversi.game_board.GameBoardModel;
import reversi.game.basic.com.tom.reversi.network.ConnectionHandler;
import reversi.game.basic.com.tom.reversi.network.DummyConnection;

/**
 * Application class for keeping singleton key components.
 */
public final class App extends Application
{
    private static App INSTANCE;
    private static GameBoardModel MODEL;
    private static GameController CONTROLLER;

    public App()
    {
        INSTANCE = this;
    }

    public static App getInstance()
    {
        return INSTANCE;
    }

    public static IReversiController getController(IPresentation ui, boolean isOnline)
    {
        if (CONTROLLER == null)
        {
            CONTROLLER = new GameController(getModel());
        }

        CONTROLLER.attach(ui);
        CONTROLLER.setPlayer(isOnline ? ConnectionHandler.getConnection() : new DummyConnection());
        return CONTROLLER;
    }

    public static GameBoardModel getModel()
    {
        if (MODEL == null)
        {
            MODEL = new GameBoardModel();
        }

        return MODEL;
    }

    public static void register(IReversiController controller)
    {
        ControllerBus.register(controller, EventTypes.MESSAGE_SEND);
        ControllerBus.register(controller, EventTypes.DISCONNECTION);
    }

    public static void unregister(IReversiController controller)
    {
        ControllerBus.unregister(controller, EventTypes.MESSAGE_SEND);
        ControllerBus.unregister(controller, EventTypes.DISCONNECTION);
    }

    /**
     * @see <a href="http://stackoverflow.com/questions/16730711/get-my-wifi-ip-address-android">Stack Overflow - Get my wifi ip address Android</a>
     * @return Local IP Address
     */
    public static String getIPAddress()
    {
        WifiManager wifiManager = (WifiManager) INSTANCE.getSystemService(WIFI_SERVICE);
        int ipAddress = wifiManager.getConnectionInfo().getIpAddress();

        // Convert little-endian to big-endianif needed
        if (ByteOrder.nativeOrder().equals(ByteOrder.LITTLE_ENDIAN))
        {
            ipAddress = Integer.reverseBytes(ipAddress);
        }

        byte[] ipByteArray = BigInteger.valueOf(ipAddress).toByteArray();

        String ipAddressString = null;
        try
        {
            ipAddressString = InetAddress.getByAddress(ipByteArray).getHostAddress();
        }
        catch (UnknownHostException ex)
        {
            Log.e("WIFIIP", "Unable to get host address.");
//            ex.printStackTrace();
        }

        return ipAddressString;
    }
}
