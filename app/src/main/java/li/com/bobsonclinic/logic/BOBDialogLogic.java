package li.com.bobsonclinic.logic;

import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;

import li.com.bobsonclinic.fragment.dialog.ResetPasswordDialog;
import li.com.bobsonclinic.fragment.dialog.base.BaseDialogFragment;
import li.com.bobsonclinic.fragment.dialog.base.DialogPickerDialogListener;

/**
 * Created by Zack on 8/10/16.
 */
public class BOBDialogLogic {

    private static BOBDialogLogic mLogic = new BOBDialogLogic();

    public static synchronized BOBDialogLogic getInstance(){
        return mLogic;
    }

    private static final String FragmentTag = "BOBDialogFragment";

    private BOBDialogLogic(){
    }

    public BaseDialogFragment showResetPasswordDialog(FragmentActivity context, boolean isCancelable, DialogPickerDialogListener l){
        try{
            if (context == null || context.isFinishing()) {
                return null;
            }

            ResetPasswordDialog dialog = new ResetPasswordDialog();
            dialog.setCancelable(isCancelable);
            dialog.setDialogPickerDialogListener(l);

            FragmentTransaction transaction = context.getSupportFragmentManager().beginTransaction();
            transaction.add(dialog, FragmentTag);
            transaction.commitAllowingStateLoss();

            return dialog;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}

