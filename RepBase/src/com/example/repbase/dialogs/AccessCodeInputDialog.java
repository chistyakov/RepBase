package com.example.repbase.dialogs;

import com.example.repbase.Common;
import com.example.repbase.R;

import android.app.DialogFragment;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager.LayoutParams;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

public class AccessCodeInputDialog extends DialogFragment
		implements OnEditorActionListener {
	private EditText etAccCode;
	private int groupId;
	private String groupName;
	private final static String KEY_GROUP_ID = "groupId";
	private final static String KEY_GROUP_NAME = "groupName";

	public interface AccessCodeInputDialogListener {
		void onFinishedAccCodeDialog(int groupId, String inputText);
	}
	
	/**
	 * Create a new instance of AccessCodeInputDialog
	 * 
	 * @param groupId group's id to check access code
	 * @param groupName group's name to set dialog's title
	 */
    public static AccessCodeInputDialog newInstance(int groupId, String groupName) {
    	AccessCodeInputDialog d = new AccessCodeInputDialog();

        // Supply group input as an argument.
        Bundle args = new Bundle();
        args.putInt(KEY_GROUP_ID, groupId);
        args.putString(KEY_GROUP_NAME, groupName);
        d.setArguments(args);

        return d;
    }


	public AccessCodeInputDialog() {
		Log.d(Common.TEMP_TAG, "default constructor AccessCodeInputDialog");
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		Log.d(Common.TEMP_TAG, "method onCreateView() in AccessCodeInputDialog class");
		View v = inflater.inflate(R.layout.dialog_acccode, container);
		etAccCode = (EditText) v.findViewById(R.id.etAccCode);
		getDialog().setTitle(groupName);


		// Show soft keyboard automatically
		etAccCode.requestFocus();
		getDialog().getWindow().setSoftInputMode(
				LayoutParams.SOFT_INPUT_STATE_VISIBLE);
		etAccCode.setOnEditorActionListener(this);

		return v;
	}

	public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
		if (EditorInfo.IME_ACTION_DONE == actionId) {
			// Return input text to activity
			AccessCodeInputDialogListener activity = (AccessCodeInputDialogListener) getActivity();
			activity.onFinishedAccCodeDialog(groupId, etAccCode.getText().toString());
			this.dismiss();
			return true;
		}
		return false;
	}


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.groupId = getArguments().getInt(KEY_GROUP_ID);
		this.groupName = getArguments().getString(KEY_GROUP_NAME);
	}

}
