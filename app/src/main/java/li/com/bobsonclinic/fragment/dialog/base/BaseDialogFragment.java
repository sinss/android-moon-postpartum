package li.com.bobsonclinic.fragment.dialog.base;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class BaseDialogFragment extends DialogFragment {

	protected static boolean DEBUG = false;

	protected String LOGTAG;

	protected DialogPickerDialogListener mListener;
	protected DialogInterface.OnDismissListener dismissListener;

	public BaseDialogFragment() {
		super();
		LOGTAG = ((Object) this).getClass().getSimpleName();
	}

	@Override
	public void onAttach(Activity activity) {
		if (DEBUG) {
			 Log.d("FragmentState", LOGTAG + " onAttach");
		}
		super.onAttach(activity);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		if (DEBUG) {
			 Log.d("FragmentState", LOGTAG + " onActivityCreated");
		}
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (DEBUG) {
			 Log.d("FragmentState", LOGTAG + " onActivityResult");
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		if (DEBUG) {
			 Log.d("FragmentState", LOGTAG + " onCreate");
		}
		super.onCreate(savedInstanceState);
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		if (DEBUG) {
			 Log.d("FragmentState", LOGTAG + " onCreateDialog");
		}
		return super.onCreateDialog(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if (DEBUG) {
			 Log.d("FragmentState", LOGTAG + " onCreateView");
		}
		return super.onCreateView(inflater, container, savedInstanceState);
	}

	@Override
	public void onCancel(DialogInterface dialog) {
		if (DEBUG) {
			 Log.d("FragmentState", LOGTAG + " onCancel");
		}
		super.onCancel(dialog);
	}

	@Override
	public void onDestroy() {
		if (DEBUG) {
			 Log.d("FragmentState", LOGTAG + " onDestroy");
		}
		super.onDestroy();
	}

	@Override
	public void onDestroyView() {
		if (DEBUG) {
			 Log.d("FragmentState", LOGTAG + " onDestroyView");
		}
		super.onDestroyView();
	}

	@Override
	public void onDetach() {
		if (DEBUG) {
			 Log.d("FragmentState", LOGTAG + " onDetach");
		}
		super.onDetach();
	}

	@Override
	public void onPause() {
		if (DEBUG) {
			 Log.d("FragmentState", LOGTAG + " onPause");
		}
		super.onPause();
	}

	@Override
	public void onResume() {
		if (DEBUG) {
			 Log.d("FragmentState", LOGTAG + " onResume");
		}
		super.onResume();
	}

	@Override
	public void onStart() {
		if (DEBUG) {
			 Log.d("FragmentState", LOGTAG + " onStart");
		}
		super.onStart();
	}

	@Override
	public void onStop() {
		if (DEBUG) {
			 Log.d("FragmentState", LOGTAG + " onStop");
		}
		super.onStop();
	}

	public void setOnDismissListener(DialogInterface.OnDismissListener dismissListener) {
		this.dismissListener = dismissListener;
	}

	@Override
	public void onDismiss(DialogInterface dialog) {
		super.onDismiss(dialog);
		if(dismissListener !=null)
			dismissListener.onDismiss(dialog);
	}

	public void setDialogPickerDialogListener(DialogPickerDialogListener l){
		mListener = l;
	}
}
