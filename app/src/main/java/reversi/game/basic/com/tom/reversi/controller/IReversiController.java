package reversi.game.basic.com.tom.reversi.controller;

import reversi.game.basic.com.tom.reversi.events.IEventHandler;

/**
 * Interface for interacting with the game's controller unit.
 */
public interface IReversiController
{
    /**
     * Orders the controller to set its contents onto its respective view.
     */
    void setup();
}
