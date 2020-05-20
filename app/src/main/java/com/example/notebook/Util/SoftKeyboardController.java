package com.example.notebook.Util;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

public class SoftKeyboardController {
    private static InputMethodManager imm;

    public static void init(Activity activity) {
        imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
    }

    public static void forceShow(){
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
    }

    @Deprecated
    public static void forceHide(Window window){
        window.setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
        );
    }

    /**** 关闭软键盘  ****/
    public static void closeKeyboard(Activity activity) {
        View view = activity.getWindow().peekDecorView();
        if (view != null) {
            imm.hideSoftInputFromWindow(view.getWindowToken(),0);
        }
    }
}
