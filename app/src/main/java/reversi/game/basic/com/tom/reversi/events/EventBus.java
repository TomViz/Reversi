package reversi.game.basic.com.tom.reversi.events;

import android.util.Log;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Class designed to register/unregister controller to receive commands from method caller.
 */
public final class EventBus
{
    private static final String EVENT_TAG = "Event";

    private static final Map<EventTypes, Set<IEventHandler>> EVENTS = new ConcurrentHashMap<>(1);
    private static final ExecutorService POOL = Executors.newFixedThreadPool(3);

    public static void register(final IEventHandler handler, final EventTypes event)
    {
        POOL.execute(new Runnable()
        {
            @Override
            public void run()
            {
                Set<IEventHandler> handlers = EVENTS.get(event);
                if (handlers == null)
                {
                    handlers = new CopyOnWriteArraySet<>();
                    EVENTS.put(event, handlers);
                }

                handlers.add(handler);
            }
        });
    }

    public static void unregister(final IEventHandler handler, final EventTypes event)
    {
        POOL.execute(new Runnable()
        {
            @Override
            public void run()
            {
                Set<IEventHandler> handlers = EVENTS.get(event);
                if (handlers != null)
                {
                    handlers.remove(handler);
                }
            }
        });
    }

    public static void sendEvent(final IEvent event)
    {
        POOL.execute(new Runnable()
        {
            @Override
            public void run()
            {
                Set<IEventHandler> handlers = EVENTS.get(event.getType());
                if (handlers == null)
                {
                    return;
                }

//                Log.d("Receiver", "Opponent has touched row " + row + " and column " + column);
                Log.d(EVENT_TAG, "Received an event!");
                for (IEventHandler handler : handlers)
                {
                    handler.onEvent(event);
                }
            }
        });
    }
}
