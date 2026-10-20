package li.com.bobsonclinic.fragment.dialog;

import android.app.Dialog;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import li.com.bobsonclinic.R;
import li.com.bobsonclinic.fragment.dialog.base.BaseDialogFragment;
import li.com.bobsonclinic.fragment.dialog.base.DialogPickerDialogListener;

/**
 * Created by Zack on 8/13/16.
 */
public class ResetPasswordDialog extends BaseDialogFragment{

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NO_FRAME, R.style.CustomUIDialog);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.ui_view_modified_password_layout, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        findView();
        init();
    }

    private void findView() {
        final EditText pass = (EditText)getView().findViewById(R.id.new_password_editV);
        final EditText confirm = (EditText)getView().findViewById(R.id.confirm_password_editV);
        View okV = getView().findViewById(R.id.ok_btnV);
        okV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String password = pass.getText().toString();
                String confirmPass = confirm.getText().toString();
                if(password.equals(confirmPass)){
                    if(mListener != null){
                        mListener.onClickDialogPicker(DialogPickerDialogListener.DialogPickerType.OK, password);
                    }
                }else{
                    Toast.makeText(getActivity(), "密碼不一致!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void init(){
    }


    @Override
    public void onStart() {
        super.onStart();
        Dialog d = getDialog();
        if (d != null){
            int width = ViewGroup.LayoutParams.WRAP_CONTENT;
            int height = ViewGroup.LayoutParams.WRAP_CONTENT;
            d.getWindow().setLayout(width, height);
            d.getWindow().setGravity(Gravity.CENTER);
        }
    }
}
