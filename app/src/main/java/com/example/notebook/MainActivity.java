package com.example.notebook;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseViewHolder;
import com.example.notebook.Entity.RvTreeViewItemBean;
import com.example.notebook.Entity.TreeStatusControl;
import com.example.notebook.Fragments.FragmentDrawBoard;
import com.example.notebook.Util.BaseActivity;
import com.example.notebook.Util.BitmapUtil;
import com.example.notebook.Util.DisableScrollViewPager;
import com.example.notebook.Util.Photos;
import com.example.notebook.Util.QuickFragment;
import com.example.notebook.Util.QuickRecyclerView;
import com.example.notebook.Util.ScreenUtil;
import com.example.notebook.Util.SoftKeyboardController;
import com.example.notebook.Util.TCallBack;
import com.example.notebook.Util.ToastUtil;
import com.example.notebook.drawableview.draw.SerializablePath;

import java.util.ArrayList;

public class MainActivity extends BaseActivity {
    private static final String TAG = "NoteBookMainActivity";
    private TabLayout tabLayout;
    private DisableScrollViewPager mainViewPager;
    private String[] titiles = new String[]{"树", "记事本", "拍照", "画板"};
    private View[] views = new View[3];

    private TreeStatusControl control = new TreeStatusControl();
    private TextView pressBack;
    private TextView storeCurrent;
    private TextView add;

    private EditText editText;

    private FragmentDrawBoard fragmentDrawBoard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SoftKeyboardController.init(MainActivity.this);
        fragmentDrawBoard = FragmentDrawBoard.newInstance();
        processor.ensureFirstOpen();
        control.currentRoot = processor.getRootId();
        initView();
        initData();
        initListener();
    }

    private void initListener() {
        fragmentDrawBoard.setOnFingerUpListener(new TCallBack<ArrayList<SerializablePath>>() {
            @Override
            public void call(ArrayList<SerializablePath> aVoid) {
                control.setPicDraw(aVoid);
            }
        });
    }

    @Override
    protected void initView() {
        super.initView();
        tabLayout = findViewById(R.id.tab_layout);
        mainViewPager = findViewById(R.id.main_view_pager);
        mainViewPager.setAdapter(new FragmentStatePagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(final int i) {
                if (i < 3)
                    return QuickFragment.getFragment(new QuickFragment.AbsFragment() {
                        @Override
                        public int getViewId() {
                            switch (i) {
                                case 0:
                                    return R.layout.tree_view;
                                case 1:
                                    return R.layout.edit_view;
                                case 2:
                                    return R.layout.picture_view;
                            }
                            return 0;
                        }

                        @SuppressLint("SetTextI18n")
                        @Override
                        public void drawView(View v) {
                            //拿到引用
                            if (views[i] == null) {
                                views[i] = v;
                                treeViewInit(i);
                            }

                            switch (i) {
                                case 0:
                                    final QuickRecyclerView<Long> quickRecyclerView = ((QuickRecyclerView) (v.findViewById(R.id.quick_rv)));
                                    quickRecyclerView.setItemLayout(R.layout.tree_view_rv_item_layout,
                                            RvTreeViewItemBean.fromId(control.currentRoot).getChilds())
                                            .describeLayout(new QuickRecyclerView.RvAdapter.DescribeItem<Long>() {
                                                @Override
                                                public void describeItem(final BaseViewHolder helper, final Long item, final int position) {
                                                    helper.setText(R.id.tv_item, RvTreeViewItemBean.fromId(item).txt());

                                                    if (position == control.selected) {
                                                        helper.setBackgroundRes(R.id.selecting_view, R.drawable.selecting);
                                                    } else {
                                                        helper.setBackgroundRes(R.id.selecting_view, R.drawable.unselected);
                                                    }
                                                    helper.getView(R.id.response_click_view).setOnClickListener(new View.OnClickListener() {
                                                        @Override
                                                        public void onClick(View v) {
                                                            control.currentSelection = item;
                                                            if (control.selected == position) {
                                                                control.inner();
//                                                                doubleClick();
                                                                control.selected = -1;
                                                            } else {
                                                                control.selected = position;
                                                            }
                                                            onTargetChange();
                                                        }
                                                    });
                                                    helper.getView(R.id.response_click_view).setOnLongClickListener(new View.OnLongClickListener() {
                                                        @Override
                                                        public boolean onLongClick(View v) {
//                                                        ToastUtil.showShort(position+"");
                                                            ListView listView = new ListView(context);
                                                            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(context, R.layout.textview);
                                                            arrayAdapter.add("删除");
                                                            listView.setAdapter(arrayAdapter);
                                                            listView.setBackgroundColor(Color.WHITE);
                                                            final PopupWindow popupWindow = new PopupWindow(listView, 200, ViewGroup.LayoutParams.WRAP_CONTENT, true);
                                                            popupWindow.showAsDropDown(v);
                                                            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                                                @Override
                                                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                                                    control.removeChild(item);
                                                                    onTargetChange();
                                                                    popupWindow.dismiss();
                                                                }
                                                            });
                                                            return false;
                                                        }
                                                    });
                                                }
                                            }).setLayoutManager(new GridLayoutManager(context, ScreenUtil.width() / ScreenUtil.dp2px(context, 100) + 1));
                                    break;
                                case 1:
                                    final TextView textView = v.findViewById(R.id.text_count);
                                    editText = ((EditText) v.findViewById(R.id.edt_default));
                                    editText.setText(control.txt());
                                    textView.setText(getResources().getString(R.string.text_count) + editText.length());
                                    editText.addTextChangedListener(new TextWatcher() {
                                        @Override
                                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                                        }

                                        @Override
                                        public void onTextChanged(CharSequence s, int start, int before, int count) {
                                            Log.d(TAG, "onTextChanged" + s);
                                        }

                                        @Override
                                        public void afterTextChanged(Editable s) {
                                            control.setTxt(editText.getText().toString());
//                                        control.setTxt(s.toString());
                                            textView.setText(getResources().getString(R.string.text_count) + editText.length());
                                        }
                                    });
                                    break;
                                case 2:
                                    Bitmap b;
                                    if ((b = control.camera()) != null) {
                                        ((ImageView) (views[2].findViewById(R.id.bg))).setImageBitmap(b);
                                    } else {
                                        ((ImageView) (views[2].findViewById(R.id.bg))).setImageBitmap(null);
                                    }
                                    v.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(final View v) {
                                            Photos.take(new TCallBack<Bitmap>() {
                                                @Override
                                                public void call(Bitmap bitmap) {
                                                    if (bitmap == null) return;
                                                    bitmap = BitmapUtil.restrictSize(bitmap, 1000);
                                                    if (bitmap.getWidth() > bitmap.getHeight()) {
                                                        bitmap = BitmapUtil.rotateBitmap(bitmap, 90);
                                                    }
                                                    ((ImageView) v.findViewById(R.id.bg)).setImageBitmap(bitmap);
                                                    control.setCamera(bitmap);
                                                }
                                            }, (FragmentActivity) context);
                                        }
                                    });
                                    break;
                            }
                        }
                    });
                if (i == 3) {
                    return fragmentDrawBoard;
                }
                return null;
            }

            @Override
            public int getCount() {
                return titiles.length;
            }

            @Nullable
            @Override
            public CharSequence getPageTitle(int position) {
                return titiles[position];
            }
        });
        mainViewPager.setOffscreenPageLimit(5);
        mainViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                if (i == 0) {
                    onTargetChange();
                }

                if (i != 1) {
                    SoftKeyboardController.closeKeyboard(MainActivity.this);
                } else {
                    editText.setSelection(editText.getText().length());
                    editText.requestFocus();
                    SoftKeyboardController.forceShow();
                }

                if (i == 3) {
                    mainViewPager.setScrollable(false);
                    fragmentDrawBoard.postInvalidate();
                } else {
                    mainViewPager.clearFocus();
                    mainViewPager.setScrollable(true);
                }
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
        tabLayout.setupWithViewPager(mainViewPager);
    }

//    private void doubleClick() {
//
//    }


    @Override
    public void onBackPressed() {
        if(mainViewPager.getCurrentItem()!=0){
            mainViewPager.setCurrentItem(0);
        }else {
            if (control.currentRoot != 0) {
                pressBack.callOnClick();
            } else
                super.onBackPressed();
        }

    }

    private void treeViewInit(final int i) {
        switch (i) {
            case 0:
                final QuickRecyclerView<Long> quickRecyclerView = (views[i].findViewById(R.id.quick_rv));
                pressBack = views[i].findViewById(R.id.press_back);
                storeCurrent = views[i].findViewById(R.id.store_current);
                add = views[i].findViewById(R.id.add);
                add.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        RvTreeViewItemBean.fromId(control.currentRoot).addChild(
                                new RvTreeViewItemBean(processor.newId())
                        );
                        quickRecyclerView.getAdapter().setNewData(RvTreeViewItemBean.fromId(control.currentRoot).getChilds());
                    }
                });
                storeCurrent.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        processor.setRootId(control.currentRoot);
                    }
                });

                pressBack.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (control.currentRoot == 0) {
                            return;
                        }
                        control.back();
                        quickRecyclerView.getAdapter().setNewData(RvTreeViewItemBean.fromId(control.currentRoot).getChilds());
                        onTargetChange();
                    }
                });
                break;
            case 1:
                break;
            case 2:
                break;
        }
    }

    private void onTargetChange() {
        editText.setText(control.txt());
        ((QuickRecyclerView) (views[0].findViewById(R.id.quick_rv))).getAdapter().setNewData(RvTreeViewItemBean.fromId(
                control.currentRoot
        ).getChilds());
        Bitmap b;
        if ((b = control.camera()) != null) {
            ((ImageView) (views[2].findViewById(R.id.bg))).setImageBitmap(b);
        } else {
            ((ImageView) (views[2].findViewById(R.id.bg))).setImageBitmap(null);
        }

        ArrayList<SerializablePath> serializablePaths = control.picDraw();
//        Log.d(TAG, "onTargetChange: "+serializablePaths);
        fragmentDrawBoard.setPaths(serializablePaths);
    }
}
