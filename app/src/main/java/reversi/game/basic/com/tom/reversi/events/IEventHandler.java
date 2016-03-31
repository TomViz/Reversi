package reversi.game.basic.com.tom.reversi.events;

import reversi.game.basic.com.tom.reversi.events.IEvent;

/**
 * Interface for event handlers to be registered with the event bus.
 */
public interface IEventHandler
{
    /**
     * Handles incoming events.
     * @param event Incoming event.
     */
    void onEvent(IEvent event);
}
