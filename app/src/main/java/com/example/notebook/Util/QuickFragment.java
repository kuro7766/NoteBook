package com.example.notebook.Util;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.io.Serializable;

public class QuickFragment{

    public static Fragment getFragment(AbsFragment absFragment){
        Fragment f=QFragment.newInstance();
        Bundle bundle = new Bundle();
        bundle.putSerializable("QuickFragment",absFragment);
        f.setArguments(bundle);
        return f;
    }

    public static class QFragment extends Fragment{
        private View v;
        private AbsFragment absFragment;

        @Deprecated
        public QFragment setAbsFragment(AbsFragment absFragment) {
            this.absFragment = absFragment;
            return this;
        }

        public static QFragment newInstance() {
            return new QFragment();
        }

        @Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            absFragment= (AbsFragment) getArguments().getSerializable("QuickFragment");
            v=inflater.inflate(absFragment.getViewId(),null);
            absFragment.drawView(v);
            return v;
        }
    }

    public abstract static class AbsFragment implements Parcelable ,Serializable {
        @LayoutRes
        public abstract int getViewId();
        public abstract void drawView(View v);

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {

        }
    }
}
