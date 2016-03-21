package reversi.game.basic.com.tom.reversi.utility;

import android.content.Context;
import android.content.Intent;

import reversi.game.basic.com.tom.reversi.activities.MainActivity;
import reversi.game.basic.com.tom.reversi.help.HelpActivity;

/**
 * Utility class to change between activities.
 */
public final class ActivityRouter
{
    public static final String WHO_IS_FIRST_KEY = "FIRST";

    public static void startGame(Context context, boolean isFirst)
    {
        Intent intent = new Intent(context, MainActivity.class);
        intent.putExtra(WHO_IS_FIRST_KEY, isFirst);
        context.startActivity(intent);
    }

    public static void helpScreen(Context context)
    {
        Intent intent = new Intent(context, HelpActivity.class);
        context.startActivity(intent);
    }
}
