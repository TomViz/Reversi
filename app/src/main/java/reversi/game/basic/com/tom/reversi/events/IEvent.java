package reversi.game.basic.com.tom.reversi.events;

/**
 * Interface for event object. Any object of the event class must be able to identify itself with an event type.
 */
public interface IEvent
{
    EventTypes getType();
}
