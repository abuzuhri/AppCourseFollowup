package ae.ac.adec.coursefollowup.services.dailogs;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import ae.ac.adec.coursefollowup.R;
import ae.ac.adec.coursefollowup.views.event.IDialogClick;

/**
 * Created by Tareq on 03/14/2015.
 */
public class ConfirmationDialog {

    public static void Delete(Context context, final IDialogClick cInterface){
        new AlertDialog.Builder(context)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle(context.getString(R.string.delete_confirmation_title))
                .setMessage(context.getString(R.string.delete_confirmation))
                .setPositiveButton(context.getString(R.string.yes), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        cInterface.onConfirm();
                    }

                })
                .setNegativeButton(context.getString(R.string.no), null)
                .show();
    }
}
