package reversi.game.basic.com.tom.reversi.help;

import android.support.v4.app.Fragment;
import android.util.Log;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Tom Vizel on 21/03/2016.
 */
public final class FragmentScreenLoader
{
    private static final Map<Integer, Class<? extends Fragment>> SCREENS = new HashMap<>();
    static
    {
        SCREENS.put(0, Screen1Fragment.class);
        SCREENS.put(1, Screen2Fragment.class);
        SCREENS.put(2, Screen3Fragment.class);
    }

    public static Fragment getScreen(int screenNumber) throws InstantiationException
    {
        Class<?> c = SCREENS.get(screenNumber);
        if (c == null)
        {
            throw new InstantiationException();
        }

        try
        {
            Fragment frag = (Fragment) c.newInstance();
            return frag;
        }
        catch (IllegalAccessException e)
        {
            Log.e("Fragment", "Failed to create fragment");
        }

        return null;
    }
}
