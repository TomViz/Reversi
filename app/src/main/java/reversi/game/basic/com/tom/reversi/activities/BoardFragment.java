package reversi.game.basic.com.tom.reversi.activities;


import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import reversi.game.basic.com.tom.reversi.R;
import reversi.game.basic.com.tom.reversi.controller.ControllerBus;
import reversi.game.basic.com.tom.reversi.controller.Event;
import reversi.game.basic.com.tom.reversi.controller.EventTypes;
import reversi.game.basic.com.tom.reversi.game_board.GameBoardLayout;
import reversi.game.basic.com.tom.reversi.game_board.GamePieceView;
import reversi.game.basic.com.tom.reversi.game_board.GameTile;
import reversi.game.basic.com.tom.reversi.utility.App;
import reversi.game.basic.com.tom.reversi.utility.PlayerIconContainer;

/**
 * A simple {@link Fragment} subclass.
 */
public class BoardFragment extends Fragment
{
    private GameBoardLayout board;
    private List<GamePieceView> legalTilesForThisRound = new ArrayList<>(4);
    private GameBoardLayout.ITileTouchListener listener;
    private int boardSize;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View v = inflater.inflate(R.layout.custom_board_layout, container, false);
        board = (GameBoardLayout) v.findViewById(R.id.gameBoard);
        boardSize = App.getModel().getDimension();
        board.setColumnCount(boardSize);
        board.setRowCount(boardSize);
        for (int i = 0; i < boardSize; ++i)
        {
            for (int j = 0; j < boardSize; ++j)
            {
                GamePieceView piece = new GamePieceView(getActivity(), i, j);
                piece.setBackgroundColor(Color.WHITE);
                board.addView(piece);
            }
        }

        listener = new GameBoardLayout.ITileTouchListener()
        {
            @Override
            public void onTileTouch(int row, int col)
            {
//                App.sendCoordinates(row, col);
                ControllerBus.sendEvent(new Event(EventTypes.MESSAGE_SEND, row, col));
            }
        };

        board.setOnTileTouchListener(listener);

        return v;
    }

    public void setTiles(List<GameTile> tiles)
    {
        for (GameTile tile : tiles)
        {
            GamePieceView view = (GamePieceView) board.getChildAt(getLinearIndex(tile.getRow(), tile.getColumn()));
            view.setImageBitmap(PlayerIconContainer.getIcon(tile.getPlayer()));
        }
    }

    public void clearLegalTiles()
    {
        for (GamePieceView gamePieceView : legalTilesForThisRound)
        {
            gamePieceView.setBackgroundColor(Color.WHITE);
        }

        legalTilesForThisRound.clear();
    }

    public void setLegalTiles(List<GameTile> tiles)
    {
        for (GameTile tile : tiles)
        {
            GamePieceView view = (GamePieceView) board.getChildAt(getLinearIndex(tile.getRow(), tile.getColumn()));
            legalTilesForThisRound.add(view);
            view.setBackgroundColor(Color.GREEN);
        }
    }

    public void enableTileTouchListener(boolean isEnabled)
    {
        board.setOnTileTouchListener(isEnabled ? listener : null);
    }

    private int getLinearIndex(int row, int col)
    {
        return ( (row * boardSize) + col );
    }
}
