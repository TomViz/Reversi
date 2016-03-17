package reversi.game.basic.com.tom.reversi.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import reversi.game.basic.com.tom.reversi.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class BoardFragment extends Fragment
{
    public BoardFragment()
    {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_board_container, container, false);
    }

}
