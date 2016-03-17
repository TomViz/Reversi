package reversi.game.basic.com.tom.reversi.game_board;

/**
 * Created by Tom Vizel on 14/03/2016.
 */
public class GameTile
{
    private TileOccupancy player;
    private int row;
    private int column;

    public GameTile(TileOccupancy player, int row, int column)
    {
        this.player = player;
        this.row = row;
        this.column = column;
    }

    /**
     * Returns enum corresponding to the player currently occupying this tile.
     * @return Empty, player 1 or player 2.
     */
    public TileOccupancy getPlayer()
    {
        return player;
    }

    /**
     * Retrieves row index.
     * @return row.
     */
    public int getRow()
    {
        return row;
    }

    /**
     * Retrieves column index.
     * @return column.
     */
    public int getColumn()
    {
        return column;
    }

    public void setPlayer(TileOccupancy player)
    {
        this.player = player;
    }

    public void setRow(int row)
    {
        this.row = row;
    }

    public void setColumn(int column)
    {
        this.column = column;
    }
}
