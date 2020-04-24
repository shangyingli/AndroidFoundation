package com.example.recordactivity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.DialogInterface.OnShowListener;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;

public class SaveDialogFragment extends DialogFragment {

    private String mRecordName;
    private EditText mRename;
    private AlertDialog mSaveDeleteDialog;
    public final static String TAG = SaveDialogFragment.class.getSimpleName();

    public void setRecordName(String recordName) {
        mRecordName = recordName;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            mRecordName = savedInstanceState.getString("recordname");
        }
        Log.d(TAG, " mRecordName  = " +  mRecordName );
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.layout_rename,
                null);
        mRename = (EditText) view.findViewById(R.id.editTextRename);
        view.findViewById(R.id.id_message).setVisibility(View.GONE);
        mSaveDeleteDialog = new AlertDialog.Builder(getActivity())
                .setTitle(R.string.name_and_save)
                .setView(view)
                .setCancelable(false)
                .setPositiveButton(R.string.dialog_save, null)
                .setNegativeButton(R.string.dialog_delete,
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                OPSoundRecordActivity listener = (OPSoundRecordActivity) getActivity();
                                if (listener != null) {
                                    listener.onDelete();
                                }
                            }
                        }).create();
        mSaveDeleteDialog.getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        mSaveDeleteDialog.setOnShowListener(new OnShowListener() {

            @Override
            public void onShow(DialogInterface dialog) {

                if (mSaveDeleteDialog == null) {
                    return;
                }

                if (mRecordName != null && isAdded()) {
                    String text = getResources().getString(R.string.txt_record) + mRecordName;
                    mRename.setText(text);
                    mRename.setSelection(text.length());

                }
                mSaveDeleteDialog.getButton(AlertDialog.BUTTON_POSITIVE)
                        .setOnClickListener(new View.OnClickListener() {

                            @Override
                            public void onClick(View v) {
                                OPSoundRecordActivity listener = (OPSoundRecordActivity) getActivity();
                                if (listener != null && mRename != null) {
                                    if (listener.onSave(mRename.getText()
                                            .toString())) {
                                        dismiss();
                                    }
                                }
                            }
                        });
            }
        });

        return mSaveDeleteDialog;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putString("recordname", mRecordName);
        super.onSaveInstanceState(outState);
    }



}

