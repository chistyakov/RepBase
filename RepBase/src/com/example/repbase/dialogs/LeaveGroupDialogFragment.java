package com.example.repbase.dialogs;

import com.example.repbase.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;

public class LeaveGroupDialogFragment extends DialogFragment {
	public interface LeaveGroupDialogFragmentListener {
        public void onDialogPositiveClick(DialogFragment dialog);
        public void onDialogNegativeClick(DialogFragment dialog);
	}
	
	LeaveGroupDialogFragmentListener mListener;

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try{
			mListener = (LeaveGroupDialogFragmentListener) activity;
		} catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement LeaveGroupDialogFragmentListener");
		}
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setMessage(R.string.leaveGroup);
		builder.setPositiveButton(R.string.btnOkText, new OnClickListener() {
			
			public void onClick(DialogInterface dialog, int which) {
				mListener.onDialogPositiveClick(LeaveGroupDialogFragment.this);
			}
		});
		builder.setNegativeButton(R.string.btnCancelText, new OnClickListener() {
			
			public void onClick(DialogInterface dialog, int which) {
				mListener.onDialogNegativeClick(LeaveGroupDialogFragment.this);
			}
		});
		
		return builder.create();
	}
	
}
