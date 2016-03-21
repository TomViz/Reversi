package reversi.game.basic.com.tom.reversi.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.EditText;

import reversi.game.basic.com.tom.reversi.network.ConnectionHandler;
//import reversi.game.basic.com.tom.reversi.trash.ServiceRouter;

/**
 * Dialog for manually entering IP address and attempting to connect to server.
 */
public class JoinDialogFragment extends DialogFragment
{
    private static final String IP_START = "192.168.1.";

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        final EditText textView = new EditText(getActivity());
        textView.setText(IP_START);
        return new AlertDialog.Builder(getActivity()).
                setTitle("Join request").
                setMessage("Pleas enter the host IP address you wish to join").
                setView(textView).
                setIcon(android.R.drawable.ic_dialog_dialer).
                setPositiveButton("Join", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        String ipAddress = textView.getText().toString();
//                        ServiceRouter.startJoinService(getActivity(), ipAddress);
                        ConnectionHandler.startJoin(ipAddress, 9000);
                    }
                }).create();
    }
}
