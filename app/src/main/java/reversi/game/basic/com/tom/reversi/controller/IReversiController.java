package reversi.game.basic.com.tom.reversi.controller;

/**
 * Interface for interacting with the game's controller unit.
 */
public interface IReversiController
{
    /**
     * Orders the controller to set its contents onto its respective view.
     */
    void setup();

    /**
     * Handles incoming events.
     * @param event Incoming event.
     */
    void onEvent(Event event);

    /**
     * Retrieves size of symmetrical board (Equal number of rows and columns).
     * @return Number of rows/columns.
     */
    int getBoardSize();
}
