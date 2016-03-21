package reversi.game.basic.com.tom.reversi.controller;

import android.util.Log;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Class designed to register/unregister controller to receive commands from method caller.
 */
public final class ControllerBus
{
    private final Map<Events, List<IReversiController>> EVENTS = new ConcurrentHashMap<>(1);
    private final ExecutorService POOL = Executors.newFixedThreadPool(3);

    public void register(final IReversiController controller, final Events event)
    {
        POOL.execute(new Runnable()
        {
            @Override
            public void run()
            {
                List<IReversiController> controllers = EVENTS.get(event);
                if (controllers == null)
                {
                    controllers = new CopyOnWriteArrayList<>();
                    EVENTS.put(event, controllers);
                }

                controllers.add(controller);
            }
        });
    }

    public void unregister(final IReversiController controller, final Events event)
    {
        POOL.execute(new Runnable()
        {
            @Override
            public void run()
            {
                List<IReversiController> controllers = EVENTS.get(event);
                if (controllers != null)
                {
                    controllers.remove(controller);
                }
            }
        });
    }

    /**
     * Send coordinates to controllers registered under the "MESSAGE_SEND" event.
     * @param row row coordinate
     * @param column column coordinate
     */
    public void passCoordinates(final int row, final int column)
    {
        POOL.execute(new Runnable()
        {
            @Override
            public void run()
            {
                List<IReversiController> controllers = EVENTS.get(Events.MESSAGE_SEND);
                if (controllers == null)
                {
                    return;
                }

                Log.d("Receiver", "Opponent has touched row " + row + " and column " + column);
                for (IReversiController controller : controllers)
                {
                    controller.onTileTouch(row, column);
                }
            }
        });
    }
}
