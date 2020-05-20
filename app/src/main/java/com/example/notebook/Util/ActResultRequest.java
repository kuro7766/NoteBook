package com.example.notebook.Util;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.SparseArray;

/**
 * 前提是activity的不能重写对应方法，否则会被拦截
 */
public class ActResultRequest {

    public static abstract class ActResultRequestCallBackAdapter implements Callback {
        @Override
        public void onActivityResult(int resultCode, Intent data) {

        }

        @Override
        public void onRequestPermissionResult(String[] permissions, int[] grantResults) {

        }
    }

    private OnActResultEventDispatcherFragment fragment;

    public ActResultRequest(FragmentActivity activity) {
        fragment = getEventDispatchFragment(activity);
    }

    public void startActivityForResult(Intent intent, Callback callback) {
        fragment.startActivityForResult(intent, callback);
    }

    public void startPermissionRequestForResult(String[] permissons, Callback callback) {
        fragment.startPermissionRequestForResult(fragment, permissons, callback);
    }

    private OnActResultEventDispatcherFragment getEventDispatchFragment(FragmentActivity activity) {
        FragmentManager fragmentManager = activity.getSupportFragmentManager();
        OnActResultEventDispatcherFragment fragment = findEventDispatchFragment(fragmentManager);
        if (fragment == null) {
            fragment = new OnActResultEventDispatcherFragment();
            fragmentManager
                    .beginTransaction()
                    .add(fragment, OnActResultEventDispatcherFragment.TAG)
                    .commitAllowingStateLoss();
            fragmentManager.executePendingTransactions();
        }
        return fragment;
    }

    private OnActResultEventDispatcherFragment findEventDispatchFragment(FragmentManager manager) {
        return (OnActResultEventDispatcherFragment) manager.findFragmentByTag(OnActResultEventDispatcherFragment.TAG);
    }


    public interface Callback {
        void onActivityResult(int resultCode, Intent data);

        void onRequestPermissionResult(String[] permissions, int[] grantResults);
    }

    public static class OnActResultEventDispatcherFragment extends Fragment {
        public static final String TAG = "on_act_result_event_dispatcher";

        public int mRequestCode = 0x1111;

        private SparseArray<Callback> mCallbacks = new SparseArray<>();

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setRetainInstance(true);
        }

        public void startActivityForResult(Intent intent, ActResultRequest.Callback callback) {
            // mRequestCode与callback需要一一对应
            mCallbacks.put(mRequestCode, callback);
            startActivityForResult(intent, mRequestCode);
            mRequestCode++;
        }

        public void startPermissionRequestForResult(OnActResultEventDispatcherFragment activity, String[] permissions, Callback callback) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                mCallbacks.put(mRequestCode, callback);
                activity.requestPermissions(permissions, mRequestCode);
                mRequestCode++;
            }
        }

        @Override
        public void onActivityResult(int requestCode, int resultCode, Intent data) {
            super.onActivityResult(requestCode, resultCode, data);

            ActResultRequest.Callback callback = mCallbacks.get(requestCode);
            mCallbacks.remove(requestCode);

            if (callback != null) {
                callback.onActivityResult(resultCode, data);
            }
        }

        /**
         *
         *
         * @param requestCode
         * @param permissions
         * @param grantResults
         */
        @Override
        public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
            ActResultRequest.Callback callback = mCallbacks.get(requestCode);
            mCallbacks.remove(requestCode);
            if (callback != null) {
                callback.onRequestPermissionResult(permissions, grantResults);
            }
        }
    }
}