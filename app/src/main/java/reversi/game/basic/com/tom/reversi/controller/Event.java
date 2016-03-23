package reversi.game.basic.com.tom.reversi.controller;

/**
 * Event container.
 */
public class Event
{
    private EventTypes type;
    private int row;
    private int column;

    public Event(EventTypes type)
    {
        this.type = type;
    }

    public Event(EventTypes type, int row, int column)
    {
        this.type = type;
        this.row = row;
        this.column = column;
    }

    public void setRow(int row)
    {
        this.row = row;
    }

    public void setColumn(int column)
    {
        this.column = column;
    }

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
