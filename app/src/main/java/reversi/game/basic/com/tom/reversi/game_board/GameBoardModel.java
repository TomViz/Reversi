package reversi.game.basic.com.tom.reversi.game_board;

import java.util.ArrayList;
import java.util.List;

/**
 * Model implementation for Reversi game board.
 */
public class GameBoardModel implements IReversiModel
{
    private List<List<TileOccupancy>> board;
    private int numOfP1Tiles;
    private int numOfP2Tiles;

    /**
     * Constructs default 8x8 board.
     */
    public GameBoardModel()
    {
        this(8);
    }

    /**
     * Constructs board with equal number of rows and columns.
     * @param dimension The number of rows/columns.
     */
    public GameBoardModel(int dimension)
    {
        this.board = new ArrayList<>(dimension);

        for (int i = 0; i < dimension; ++i)
        {
            buildEmptyColumnInBoard(dimension);
        }
    }

    @Override
    public int getDimension()
    {
        return board.size();
    }

    @Override
    public TileOccupancy getTileContent(int row, int col)
    {
        if ( isIndexInBounds(row) && isIndexInBounds(col) )
        {
            return board.get(col).get(row);
        }

        return null;
    }

    @Override
    public void setTile(GameTile tile)
    {
        int rowIndex = tile.getRow();
        int columnIndex = tile.getColumn();
        TileOccupancy playerFromTile = tile.getPlayer();

        List<TileOccupancy> col = board.get(columnIndex);
        TileOccupancy playerInBoard = col.get(rowIndex);

        if (playerFromTile == playerInBoard)
        {
            return;
        }

        if (playerFromTile == TileOccupancy.P1)
        {
            ++numOfP1Tiles;
        }
        else if (playerFromTile == TileOccupancy.P2)
        {
            ++numOfP2Tiles;
        }

        if (playerInBoard == TileOccupancy.P1)
        {
            --numOfP1Tiles;
        }
        else if (playerInBoard == TileOccupancy.P2)
        {
            --numOfP2Tiles;
        }

        col.set(rowIndex, playerFromTile);
//        board.get(tile.getColumn()).set(tile.getRow(), tile.getPlayer());
    }

    @Override
    public void setTiles(List<GameTile> tiles)
    {
        for (GameTile tile : tiles)
        {
            setTile(tile);
        }
    }

    @Override
    public List<GameTile> getOccupiedTiles()
    {
        int numOfColumns = board.size();
        List<GameTile> occupied = new ArrayList<>(numOfColumns * numOfColumns / 2);

        for (int i = 0; i < numOfColumns; ++i)
        {
            addOccupiedInColumn(occupied, i);
        }

        return occupied;
    }

    @Override
    public List<GameTile> getUnoccupiedTiles()
    {
        int numOfColumns = board.size();
        List<GameTile> emptyTiles = new ArrayList<>(numOfColumns * numOfColumns / 2 - 4);

        for (int i = 0; i < numOfColumns; ++i)
        {
            addUnoccupiedInColumn(emptyTiles, i);
        }

        return emptyTiles;
    }

    @Override
    public int getNumOfP1Tiles()
    {
        return numOfP1Tiles;
    }

    @Override
    public int getNumOfP2Tiles()
    {
        return numOfP2Tiles;
    }

    @Override
    public void cleanBoard()
    {
        board.clear();
        numOfP1Tiles = 0;
        numOfP2Tiles = 0;
    }

    private void buildEmptyColumnInBoard(int dimension)
    {
        List<TileOccupancy> column = new ArrayList<>(dimension);
        for (int j = 0; j < dimension; ++j)
        {
            column.add(TileOccupancy.EMPTY);
        }

        board.add(column);
    }

    private void addOccupiedInColumn(List<GameTile> occupied, int columnIndex)
    {
        List<TileOccupancy> row = board.get(columnIndex);
        int numOfRows = row.size();

        for (int j = 0; j < numOfRows; ++j)
        {
            TileOccupancy typeInCell = row.get(j);
            if (typeInCell != TileOccupancy.EMPTY)
            {
                occupied.add(new GameTile(typeInCell, j, columnIndex));
            }
        }
    }

    private void addUnoccupiedInColumn(List<GameTile> unoccupied, int columnIndex)
    {
        List<TileOccupancy> row = board.get(columnIndex);
        int numOfRows = row.size();

        for (int j = 0; j < numOfRows; ++j)
        {
            TileOccupancy typeInCell = row.get(j);
            if (typeInCell == TileOccupancy.EMPTY)
            {
                unoccupied.add(new GameTile(typeInCell, j, columnIndex));
            }
        }
    }

    private boolean isIndexInBounds(int index)
    {
        return (0 <= index && index < board.size());
    }
}
