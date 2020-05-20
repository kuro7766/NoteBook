package com.example.notebook.Util;

import java.util.ArrayList;
import java.util.List;

public class BlockUtil {
    private static List<OnstartRunnable> list;
    private boolean isChechingThreadRunning = false;

    private static class OnstartRunnable {
        OnStart onStart;
        Runnable runnable;

        public OnstartRunnable(OnStart onStart, Runnable runnable) {
            this.onStart = onStart;
            this.runnable = runnable;
        }
    }

    private void startCheckingThread() {
        Thread checkingThread = new Thread() {
            @Override
            public void run() {
                isChechingThreadRunning = true;
                while (list.size() > 0) {
                    for (OnstartRunnable onstartRunnable : list) {
                        if (onstartRunnable.onStart.onStart()) {
                            /*
                            在新的线程里发送一条通知过去
                             */
                            new Thread(onstartRunnable.runnable).start();
                            list.remove(onstartRunnable);
                        }
                    }
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                isChechingThreadRunning = false;
            }
        };
        checkingThread.start();
    }

    public interface OnStart {
        boolean onStart();
    }

    public void blockTask(Runnable runnable, OnStart b) {
        /*
          先填充数据
         */
        if (list == null) {
            list = new ArrayList<>();
        }
        list.add(new OnstartRunnable(b, runnable));

        /*
         * 这个要放在最后面
         */
        if (!isChechingThreadRunning) {
            startCheckingThread();
        }
    }
}
