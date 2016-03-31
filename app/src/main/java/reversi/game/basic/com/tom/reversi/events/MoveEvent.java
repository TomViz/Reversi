package reversi.game.basic.com.tom.reversi.events;

/**
 * Event containing row and column coordinates from a performed move.
 */
public class MoveEvent implements IEvent
{
    private EventTypes type;
    private int row;
    private int column;

    public MoveEvent(EventTypes type, int row, int column)
    {
        this.type = type;
        this.row = row;
        this.column = column;
    }

    @Override
    public EventTypes getType()
    {
        return type;
    }

    public int getRow()
    {
        return row;
    }

    public int getColumn()
    {
        return column;
    }
}
