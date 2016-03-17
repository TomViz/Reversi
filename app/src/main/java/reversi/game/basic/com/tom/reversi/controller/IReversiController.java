package reversi.game.basic.com.tom.reversi.controller;

/**
 * Created by Tom Vizel on 15/03/2016.
 */
public interface IReversiController
{
    /**
     * Orders the controller to set its contents onto its respective view.
     */
    void setup();

    /**
     * Perform action when a specific tile is touched.
     * @param row Row index of selected tile [0..n]
     * @param column Column index of selected tile [0..n]
     */
    void onTileTouch(int row, int column);

    /**
     * Retrieves size of symmetrical board (Equal number of rows and columns).
     * @return Number of rows/columns.
     */
    int getBoardSize();
}
