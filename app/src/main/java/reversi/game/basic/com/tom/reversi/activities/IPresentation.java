package reversi.game.basic.com.tom.reversi.activities;

import java.util.List;

import reversi.game.basic.com.tom.reversi.game_board.GameTile;

/**
 * Interface for interacting with the UI.
 */
public interface IPresentation
{
    /**
     * Display specified tiles with its corresponding contents.
     * @param tiles Tiles to be updated.
     */
    void setTiles(List<GameTile> tiles);

    /**
     * Mark specified tiles as legal.
     * @param tiles Tiles containing legal coordinates to mark.
     */
    void setLegalTiles(List<GameTile> tiles);

    /**
     * Displays the (legal) move that is being played.
     * @param tile Tile containing coordinates and player.
     */
    void displayCurrentMove(GameTile tile);

    /**
     * Notify that the currently active player has changed.
     */
    void playerChange();

    /**
     * Handle what happens when game controller declares a move as illegal.
     */
    void notifyIllegalMove();

    /**
     * Handle what happens when the player is victorious.
     */
    void notifyVictory();

    /**
     * Handle what happens when the player loses.
     */
    void notifyLoss();

    /**
     * Handle what happens in the case of a tie.
     */
    void notifyTie();

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
