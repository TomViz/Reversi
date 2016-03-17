package reversi.game.basic.com.tom.reversi.game_board;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Created by Tom Vizel on 14/03/2016.
 */
public class GamePieceView extends ImageView
{
    private int row;
    private int col;

    public GamePieceView(Context context, int row, int col)
    {
        super(context);
        this.row = row;
        this.col = col;
    }

    public int getRow()
    {
        return row;
    }

    public int getCol()
    {
        return col;
    }
}
