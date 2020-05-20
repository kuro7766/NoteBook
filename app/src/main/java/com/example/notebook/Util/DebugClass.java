package com.example.notebook.Util;

import android.util.Log;
import android.util.SparseLongArray;

/**
 * TIP:
 * to delete all debug tags ,use this regex below in ctrl+L bar:
 * <debugClass> is your object's name,if you use IDE suggested name,ignore this,else you need to change it
 * to your object's name
 * [Dd]ebugClass\.(TIME_DEBUG_LOG("?.*"?);|TIME_DEBUG_RESET\(\d?\);|TIME_DEBUG_TAG\(\d?\);)
 * <p>
 * to replace them with space and then tap alt+enter
 */
public class DebugClass {
    public int debug_counter = 0;
    public static int debug_counter_global = 0;
    private SparseLongArray sparseLongArray;
    private long DEBUG_TAG_FIRST = 0;

    private void ensureSparseArray() {
        if (sparseLongArray == null) {
            sparseLongArray = new SparseLongArray();
        }
    }

    public static void GlobalCountUp() {
        debug_counter_global++;
    }

    public void countUp() {
        debug_counter++;
    }

    /*
    [Dd]ebugClass\.(TIME_DEBUG_LOG("?.*"?);|TIME_DEBUG_RESET\(\d?\);|TIME_DEBUG_TAG\(\d?\);)
     */
    public void TIME_DEBUG_TAG(int tag) {
        ensureSparseArray();
        if (DEBUG_TAG_FIRST == 0) {
            DEBUG_TAG_FIRST = System.currentTimeMillis();
        }
        sparseLongArray.append(tag, System.currentTimeMillis() - DEBUG_TAG_FIRST);
    }

    public void TIME_DEBUG_RESET() {
        ensureSparseArray();
        DEBUG_TAG_FIRST = 0;
        sparseLongArray.clear();
    }

    /*
    [Dd]ebugClass\.(TIME_DEBUG_LOG("?.*"?);|TIME_DEBUG_RESET\(\d?\);|TIME_DEBUG_TAG\(\d?\);)
     */
    public void TIME_DEBUG_LOG(String TAG) {
        StringBuilder sb = new StringBuilder();
        Log.d(TAG, "DebugClass log: " + sparseLongArray.toString());
    }
}
