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
    private static final String EVENT_TAG = "Event";

    private static final Map<EventTypes, List<IReversiController>> EVENTS = new ConcurrentHashMap<>(1);
    private static final ExecutorService POOL = Executors.newFixedThreadPool(3);

    public static void register(final IReversiController controller, final EventTypes event)
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

    public static void unregister(final IReversiController controller, final EventTypes event)
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

    public static void sendEvent(final Event event)
    {
        POOL.execute(new Runnable()
        {
            @Override
            public void run()
            {
                List<IReversiController> controllers = EVENTS.get(event.getType());
                if (controllers == null)
                {
                    return;
                }

//                Log.d("Receiver", "Opponent has touched row " + row + " and column " + column);
                Log.d(EVENT_TAG, "Received an event!");
                for (IReversiController controller : controllers)
                {
                    controller.onEvent(event);
                }
            }
        });
    }
}
