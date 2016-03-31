package reversi.game.basic.com.tom.reversi.events;

/**
 * Event containing only a type and no extra data.
 */
public class SimpleEvent implements IEvent
{
    private EventTypes type;

    public SimpleEvent(EventTypes type)
    {
        this.type = type;
    }

    @Override
    public EventTypes getType()
    {
        return type;
    }
}
