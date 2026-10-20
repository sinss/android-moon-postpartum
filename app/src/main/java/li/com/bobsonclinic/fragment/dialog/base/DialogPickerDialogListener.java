package li.com.bobsonclinic.fragment.dialog.base;

public interface DialogPickerDialogListener {

	public enum DialogPickerType{OK};

	public void onClickDialogPicker(DialogPickerType type, Object obj);
}