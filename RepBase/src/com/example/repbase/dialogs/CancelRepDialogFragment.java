package com.example.repbase.dialogs;

import com.example.repbase.R;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
//import android.support.v4.app.DialogFragment;
import android.app.DialogFragment;

public class CancelRepDialogFragment extends DialogFragment {

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setMessage(R.string.okCancelRep).setPositiveButton(
				R.string.btnOkText, new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub

					}
				});
		return builder.create();
	}

}
