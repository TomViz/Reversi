package reversi.game.basic.com.tom.reversi.controller;

import android.graphics.Point;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import reversi.game.basic.com.tom.reversi.activities.IPresentation;
import reversi.game.basic.com.tom.reversi.game_board.GameTile;
import reversi.game.basic.com.tom.reversi.game_board.IReversiModel;
import reversi.game.basic.com.tom.reversi.game_board.TileOccupancy;

/**
 * Controller for Reversi game
 */
public class GameController implements IReversiController
{
    private static final List<Point> DIRECTIONS = new ArrayList<>(8);
    static
    {
        DIRECTIONS.add(new Point(-1, -1));
        DIRECTIONS.add(new Point(-1, 0));
        DIRECTIONS.add(new Point(-1, 1));
        DIRECTIONS.add(new Point(0, -1));
        DIRECTIONS.add(new Point(0, 1));
        DIRECTIONS.add(new Point(1, -1));
        DIRECTIONS.add(new Point(1, 0));
        DIRECTIONS.add(new Point(1, 1));
    }

    private final IReversiModel MODEL;
    private WeakReference<IPresentation> uiRef;
    private final ExecutorService POOL = Executors.newFixedThreadPool(4);

    private int currentPlayer; // 0 for player 1, 1 for player 2.
    private TileOccupancy[] playerType = {TileOccupancy.P1, TileOccupancy.P2};

    private List<GameTile> legalMovesForThisRound = new CopyOnWriteArrayList<>();
    private boolean[] areNoLegalMoves = new boolean[2];

    public GameController(IReversiModel model)
    {
        this.MODEL = model;
//        model.cleanBoard();
        setStartingTiles();
    }

    public void attach(IPresentation ui)
    {
        this.uiRef = new WeakReference<>(ui);
    }

    @Override
    public void setup()
    {
        POOL.execute(new SetupTask());
    }

    @Override
    public void onTileTouch(int row, int column)
    {
        for (GameTile tile : legalMovesForThisRound)
        {
            if (row == tile.getRow() && column == tile.getColumn())
            {
                POOL.execute(new UpdateTask(row, column));
                return;
            }
        }

        IPresentation ui = uiRef.get();
        if (ui != null)
        {
            ui.notifyIllegalMove();
        }
    }

    @Override
    public int getBoardSize()
    {
        return MODEL.getDimension();
    }

    private void setStartingTiles()
    {
        int middle = MODEL.getDimension() / 2;
        List<GameTile> tiles = new ArrayList<>(4);
        tiles.add(new GameTile(TileOccupancy.P1, middle, middle));
        tiles.add(new GameTile(TileOccupancy.P1, middle - 1, middle -1));
        tiles.add(new GameTile(TileOccupancy.P2, middle, middle - 1));
        tiles.add(new GameTile(TileOccupancy.P2, middle -1, middle));
        MODEL.setTiles(tiles);
    }

    private void changePlayer()
    {
        currentPlayer = 1 - currentPlayer;
    }

    private List<GameTile> updateInDirection(int startingRow, int startingColumn, int rowDirection, int colDirection)
    {
        int currentRow = startingRow + rowDirection;
        int currentColumn = startingColumn + colDirection;
        int otherPlayer = 1 - currentPlayer;

        List<GameTile> tilesToTurn = new ArrayList<>(6);
        TileOccupancy tileType = MODEL.getTileContent(currentRow, currentColumn);
//        while (isIndexInBounds(currentRow) && isIndexInBounds(currentColumn) && tileType == playerType[otherPlayer])
        while (tileType == playerType[otherPlayer])
        {
            tilesToTurn.add(new GameTile(playerType[currentPlayer], currentRow, currentColumn));
            currentRow += rowDirection;
            currentColumn += colDirection;
            tileType = MODEL.getTileContent(currentRow, currentColumn);
        }

        if (tileType != playerType[currentPlayer])
        {
            tilesToTurn.clear();
        }

        return tilesToTurn;
    }

    private class SetupTask implements Runnable
    {
        @Override
        public void run()
        {
            IPresentation ui = uiRef.get();
            if (ui == null)
            {
                return;
            }

            POOL.execute(new UpdateLegalMovesTask());

//            ui.clearScreen();
            ui.setTiles(MODEL.getOccupiedTiles());
            ui.setNumberOfPlayer1Tiles(MODEL.getNumOfP1Tiles());
            ui.setNumberOfPlayer2Tiles(MODEL.getNumOfP2Tiles());
        }
    }

    private class UpdateTask implements Runnable
    {
        private int rowIndex;
        private int columnIndex;

        public UpdateTask(int rowIndex, int columnIndex)
        {
            this.rowIndex = rowIndex;
            this.columnIndex = columnIndex;
        }

        @Override
        public void run()
        {
            List<GameTile> tilesToUpdate = new ArrayList<>(10);
            for (Point direction : DIRECTIONS)
            {
                List<GameTile> directionTiles = updateInDirection(rowIndex, columnIndex, direction.x, direction.y);
                tilesToUpdate.addAll(directionTiles);
            }

            IPresentation ui = uiRef.get();

            if (tilesToUpdate.size() == 0)
            {
                if (ui != null)
                {
                    ui.notifyIllegalMove();
                }

                return;
            }

            tilesToUpdate.add(new GameTile(playerType[currentPlayer], rowIndex, columnIndex));
            MODEL.setTiles(tilesToUpdate);
            changePlayer();

            POOL.execute(new UpdateLegalMovesTask());

            if (ui != null)
            {
                ui.setTiles(tilesToUpdate);
                ui.playerChange();
                ui.setNumberOfPlayer1Tiles(MODEL.getNumOfP1Tiles());
                ui.setNumberOfPlayer2Tiles(MODEL.getNumOfP2Tiles());
            }
        }

//        private boolean isIndexInBounds(int index)
//        {
//            return (0 <= index && index <= getBoardSize());
//        }
    }

    private class UpdateLegalMovesTask implements Runnable
    {
        @Override
        public void run()
        {
            legalMovesForThisRound.clear();

            List<GameTile> tiles = MODEL.getUnoccupiedTiles();
            for (GameTile tile : tiles)
            {
                addTileIfLegal(tile);
            }

            if (! legalMovesForThisRound.isEmpty())
            {
                markIllegalMoves();
            }
            else
            {
                areNoLegalMoves[currentPlayer] = true;
                if (areNoLegalMoves[1 - currentPlayer])
                {
                    finishGame();
                }
                else
                {
                    passTurn();
                }
            }
        }

        private void passTurn()
        {
            changePlayer();
            IPresentation ui = uiRef.get();
            if (ui != null)
            {
                ui.playerChange();
            }
            run();
        }

        private void finishGame()
        {
            int p1Tiles = MODEL.getNumOfP1Tiles();
            int p2Tiles = MODEL.getNumOfP2Tiles();

            IPresentation ui = uiRef.get();
            if (ui == null)
            {
                return;
            }

            if (p1Tiles == p2Tiles)
            {
                ui.notifyTie();
            }
            else if (p1Tiles > p2Tiles)
            {
                determineResult(ui, 0);
            }
            else
            {
                determineResult(ui, 1);
            }
        }

        private void determineResult(IPresentation ui, int playerIndex)
        {
            if (currentPlayer == playerIndex)
            {
                ui.notifyVictory();
            }
            else
            {
                ui.notifyLoss();
            }
        }

        private void markIllegalMoves()
        {
            areNoLegalMoves[0] = false;
            areNoLegalMoves[1] = false;
            IPresentation ui = uiRef.get();
            if (ui != null)
            {
                ui.setLegalTiles(legalMovesForThisRound);
            }
        }

        private void addTileIfLegal(GameTile tile)
        {
            for (Point direction : DIRECTIONS)
            {
                List<GameTile> tilesPerCoordinate = updateInDirection(tile.getRow(), tile.getColumn(), direction.x, direction.y);
                if (! tilesPerCoordinate.isEmpty())
                {
                    legalMovesForThisRound.add(tile);
                    break;
                }
            }
        }
    }
}
