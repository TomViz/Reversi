package reversi.game.basic.com.tom.reversi.activities;

import java.util.List;

import reversi.game.basic.com.tom.reversi.game_board.GameTile;

/**
 * Created by Tom Vizel on 14/03/2016.
 */
public interface IPresentation
{
    /**
     * Display specified tiles with its corresponding contents.
     * @param tiles
     */
    void setTiles(List<GameTile> tiles);

    /**
     * Notify that the currently active player has changed.
     */
    void playerChange();

    /**
     * Handle what happens when game controller declares a move as illegal.
     */
    void notifyIllegalMove();

    /**
     * Sets number of player 1 tiles for display.
     * @param numOfTiles Number of tiles occupied by player 1
     */
    void setNumberOfPlayer1Tiles(int numOfTiles);

    /**
     * Sets number of player 2 tiles for display.
     * @param numOfTiles Number of tiles occupied by player 2
     */
    void setNumberOfPlayer2Tiles(int numOfTiles);
}
