package reversi.game.basic.com.tom.reversi.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;

import reversi.game.basic.com.tom.reversi.utility.App;

/**
 * Created by Tom Vizel on 14/03/2016.
 */
public class AboutDialogFragment extends DialogFragment
{
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        return new AlertDialog.Builder(getActivity()).
                setTitle("About").
                setMessage("Hello there! Your IP Address is " + App.getIPAddress()).
                setIcon(android.R.drawable.ic_dialog_info).
                setPositiveButton("Hi!", null).
                create();
    }
}
