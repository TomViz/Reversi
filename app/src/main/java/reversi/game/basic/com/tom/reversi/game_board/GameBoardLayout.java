package reversi.game.basic.com.tom.reversi.game_board;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.widget.GridLayout;

/**
 * TODO: document your custom view class.
 */
public class GameBoardLayout extends GridLayout
{
    private int height;
    private int width;
    private ITileTouchListener onTouch;

    public GameBoardLayout(Context context)
    {
        super(context);
        init();
    }

    public GameBoardLayout(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        init();
    }

    public GameBoardLayout(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
        init();
    }

    @Override
    protected void onDraw(Canvas canvas)
    {
        super.onDraw(canvas);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        height = MeasureSpec.getSize(heightMeasureSpec);
        width = MeasureSpec.getSize(widthMeasureSpec);

        changeChildrenViewLayouts();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        if (onTouch != null && event.getAction() == MotionEvent.ACTION_DOWN)
        {
            onTouch.onTileTouch(yToRow(event.getY()), xToColumn(event.getX()));
        }

        return super.onTouchEvent(event);
    }

    public void setOnTileTouchListener(ITileTouchListener onTouch)
    {
        this.onTouch = onTouch;
    }

    /**
     * Interface for handling screen touch on a specific tile.
     * In the case of a user touch on screen, this interface will pass
     * along the row and column coordinates of the touched tile.
     * The starting coordinates are defined as (0,0).
     */
    public interface ITileTouchListener
    {
        void onTileTouch(int row, int col);
    }

    private void init()
    {
    }

    private void changeChildrenViewLayouts()
    {
        int numOfViews = getChildCount();
        int numOfColumns = getColumnCount();
        int numOfRows = getRowCount();

        for (int i = 0; i < numOfViews; ++i)
        {
            GamePieceView v = (GamePieceView) getChildAt(i);
            GridLayout.LayoutParams params = new LayoutParams();
            params.rowSpec = GridLayout.spec(v.getRow());
            params.columnSpec = GridLayout.spec(v.getCol());
            params.width = width / numOfColumns;
            params.height = height / numOfRows;
            params.bottomMargin = 1;
            params.leftMargin = 1;
            params.rightMargin = 1;
            params.topMargin = 1;
            params.setGravity(Gravity.CENTER);
            v.setLayoutParams(params);
        }
    }

    private int xToColumn(float x)
    {
        return (int)((x/width) * getColumnCount());
    }

    private int yToRow(float y)
    {
        return (int)((y/height) * getRowCount());
    }
}
