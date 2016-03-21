package reversi.game.basic.com.tom.reversi.help;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;

import reversi.game.basic.com.tom.reversi.R;

/**
 * Help screen + navigation
 */
public class HelpActivity extends FragmentActivity
{
    private static final int NUM_OF_PAGES = 3;
    private static final String FRAGMENT_TAG = "ScreenLoader";

    private ViewPager pager;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.help_activity_layout);

        pager = (ViewPager) findViewById(R.id.helpPager);
        pager.setAdapter(new ScreenSlidePagerAdapter(getSupportFragmentManager()));
    }

    @Override
    public void onBackPressed()
    {
        if (pager.getCurrentItem() == 0)
        {
            super.onBackPressed();
        }
        else
        {
            pager.setCurrentItem(pager.getCurrentItem() - 1);
        }
    }

    private static class ScreenSlidePagerAdapter extends FragmentPagerAdapter
    {
        public ScreenSlidePagerAdapter(FragmentManager fm)
        {
            super(fm);
        }

        @Override
        public Fragment getItem(int position)
        {
            try
            {
                return FragmentScreenLoader.getScreen(position);
            }
            catch (InstantiationException e)
            {
                Log.e(FRAGMENT_TAG, "Failed to load fragment");
            }

            return null;
        }

        @Override
        public int getCount()
        {
            return NUM_OF_PAGES;
        }
    }
}
