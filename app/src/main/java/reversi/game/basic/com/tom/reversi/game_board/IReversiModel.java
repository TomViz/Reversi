package reversi.game.basic.com.tom.reversi.game_board;

import java.util.List;

/**
 * Interface for a model holding a square (rows == columns) game board able to occupy up to 2 players.
 */
public interface IReversiModel
{
    /**
     * Retrieve size of game board.
     * @return Number of rows/columns.
     */
    int getDimension();

    /**
     * Retrieves content of the tile in the requested coordinate.
     * @param row Index of row [0..n]
     * @param col Index of column [0..n]
     * @return Player occupying the requested tile.
     */
    TileOccupancy getTileContent(int row, int col);

    /**
     * Replace content of tile.
     * @param tile Tile occupier and coordinates.
     */
    void setTile(GameTile tile);

    /**
     * Replace contents of multiple tiles.
     * @param tiles List of tile coordinates and occupiers.
     */
    void setTiles(List<GameTile> tiles);

    /**
     * Retrieves list containing tile data of all occupied (non-EMPTY) tiles.
     * @return List of occupied tiles.
     */
    List<GameTile> getOccupiedTiles();

    /**
     * Method for retrieving amount of tiles occupied by player 1.
     * @return Number of tiles occupied by player 1
     */
    int getNumOfP1Tiles();

    /**
     * Method for retrieving amount of tiles occupied by player 2.
     * @return Number of tiles occupied by player 2
     */
    int getNumOfP2Tiles();

    /**
     * Sets all tiles of the board as empty.
     */
    void cleanBoard();
}
