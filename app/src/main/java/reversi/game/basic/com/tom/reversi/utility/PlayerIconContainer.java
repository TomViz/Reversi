package reversi.game.basic.com.tom.reversi.utility;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.util.HashMap;
import java.util.Map;

import reversi.game.basic.com.tom.reversi.R;
import reversi.game.basic.com.tom.reversi.game_board.TileOccupancy;

/**
 * Class for mapping player icons.
 */
public final class PlayerIconContainer
{
    private static Map<TileOccupancy, Bitmap> icons = new HashMap<>(2);
    static
    {
        Resources res = App.getInstance().getResources();
        icons.put(TileOccupancy.P1, BitmapFactory.decodeResource(res, R.drawable.putin));
        icons.put(TileOccupancy.P2, BitmapFactory.decodeResource(res, R.drawable.obama));
    }

    /**
     * Retrieve bitmap corresponding to the player.
     * @param player Player currently occupying the tile.
     * @return Bitmap image of the icon corresponding to the player.
     */
    public static Bitmap getIcon(TileOccupancy player)
    {
        return icons.get(player);
    }

//    public static void updateIcon(TileOccupancy player, int resID)
//    {
//
//    }
}
