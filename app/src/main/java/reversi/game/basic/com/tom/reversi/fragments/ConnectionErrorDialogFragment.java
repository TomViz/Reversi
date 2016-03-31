package reversi.game.basic.com.tom.reversi.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

import reversi.game.basic.com.tom.reversi.utility.ActivityRouter;

/**
 * Created by Tom Vizel on 24/03/2016.
 */
public class ConnectionErrorDialogFragment extends DialogFragment
{
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        return new AlertDialog.Builder(getActivity()).
                setTitle("Connection error").
                setMessage("Unable to connect to the other player, you will be returned to the title screen").
                setIcon(android.R.drawable.ic_dialog_alert).
                setCancelable(false).
                setNeutralButton("OK", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        ActivityRouter.returnToTitleScreen(getActivity());
                        getActivity().finish();
                    }
                }).
                create();
    }
}
