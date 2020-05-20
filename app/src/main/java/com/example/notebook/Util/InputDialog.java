package com.example.notebook.Util;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.text.Editable;
import android.widget.EditText;
import android.widget.Toast;


public class InputDialog extends DialogFragment {
    public static InputDialog with(FragmentActivity activity){
        return new InputDialog().setActivity(activity);
    }
    FragmentActivity activity;
    InputDialog setActivity(FragmentActivity activity){
        this.activity=activity;
        edt = new EditText(activity);
        return this;
    }
    private InputDialog requestFocuse(){
        edt.requestFocus();
        return this;
    }
    private EditText edt;
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Dialog Fragment");
//        builder.setMessage("Are you sure do you want to quit?");
//        builder.setCancelable(false);

        builder.setView(edt);
        setCancelable(false);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
//                Toast.makeText(getActivity(), "you clicked OK", Toast.LENGTH_SHORT).show();
                if(callBack!=null){
                    Editable e=edt.getText();
                    if(e!=null)
                        callBack.call(e.toString());
                }
            }
        });

        builder.setNegativeButton("CANCLE", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getActivity(), "you clicked CANCLE", Toast.LENGTH_SHORT).show();
            }
        });

        AlertDialog alertDialog = builder.create();
        return alertDialog;
    }
    private TCallBack<String> callBack;

    public InputDialog setCallBack(TCallBack<String> callBack) {
        this.callBack = callBack;
        return this;
    }

    public InputDialog show(){
        show(activity.getSupportFragmentManager(),"0");
        requestFocuse();
        return this;
    }
}
