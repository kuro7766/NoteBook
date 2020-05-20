package com.example.notebook.Util;


/**
 * Usage :
 *  make your class implements {@link ContinueCondition#onConditionReachListener}
 * and always use {@link ContinueCondition#check(Check, int)} and it will auto calls
 * {@link ContinueCondition#setOnConditionReach(OnConditionReachListener)} method
 * if the qualification is satisfied ,if you want to reset time counter of this class,
 * use {@link ContinueCondition#reset()} to reset time
 *
 */
public class ContinueCondition {
    private long startTime;
    private OnConditionReachListener onConditionReachListener;

    public void setOnConditionReach(OnConditionReachListener onConditionReach) {
        this.onConditionReachListener = onConditionReach;
    }

    @Deprecated
    public interface Check {
        boolean check();
    }

    public interface OnConditionReachListener {
        void onConditionReach();
    }

    public void reset() {
        startTime = 0;
    }

    /**
     * new {@link ContinueCondition.Check} interface is not memory friendly
     * using {@link ContinueCondition#check(boolean, int)} is recommended
     *
     */
    @Deprecated
    public void check(Check check, int millicons) {
        if (startTime == 0) {
            startTime = System.currentTimeMillis();
        }
        if (check.check()) {
            if (System.currentTimeMillis() - startTime > millicons) {
                if (onConditionReachListener != null) {
                    onConditionReachListener.onConditionReach();
                    startTime = 0;
                }
            }
        } else {
            startTime = 0;
        }
    }

    public void check(boolean check, int millicons) {
        if (startTime == 0) {
            startTime = System.currentTimeMillis();
        }
        if (check) {
            if (System.currentTimeMillis() - startTime > millicons) {
                if (onConditionReachListener != null) {
                    onConditionReachListener.onConditionReach();
                    startTime = 0;
                }
            }
        } else {
            startTime = 0;
        }
    }
}
