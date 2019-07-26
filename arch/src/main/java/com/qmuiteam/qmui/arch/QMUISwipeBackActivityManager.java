/*
 * Tencent is pleased to support the open source community by making QMUI_Android available.
 *
 * Copyright (C) 2017-2018 THL A29 Limited, a Tencent company. All rights reserved.
 *
 * Licensed under the MIT License (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 *
 * http://opensource.org/licenses/MIT
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is
 * distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.qmuiteam.qmui.arch;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.support.annotation.MainThread;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.Stack;


public class QMUISwipeBackActivityManager implements Application.ActivityLifecycleCallbacks {
    private static QMUISwipeBackActivityManager sInstance;
    private Stack<Activity> mActivityStack = new Stack<>();


    @MainThread
    public static QMUISwipeBackActivityManager getInstance() {
        if (sInstance == null) {
            throw new IllegalAccessError("the QMUISwipeBackActivityManager is not initialized; " +
                    "please call QMUISwipeBackActivityManager.init(Application) in your application.");
        }
        return sInstance;
    }

    private QMUISwipeBackActivityManager() {
    }

    public static void init(@NonNull Application application) {
        if (sInstance == null) {
            sInstance = new QMUISwipeBackActivityManager();
            application.registerActivityLifecycleCallbacks(sInstance);
        }
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        mActivityStack.add(activity);
    }

    @Override
    public void onActivityDestroyed(Activity activity) {
        mActivityStack.remove(activity);
    }

    @Override
    public void onActivityStarted(Activity activity) {

    }

    @Override
    public void onActivityResumed(Activity activity) {

    }

    @Override
    public void onActivityPaused(Activity activity) {

    }

    @Override
    public void onActivityStopped(Activity activity) {

    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

    }


    /**
     *
     * refer to https://github.com/bingoogolapple/BGASwipeBackLayout-Android/
     * @param currentActivity the last activity
     * @return
     */
    @Nullable
    public Activity getPenultimateActivity(Activity currentActivity) {
        Activity activity = null;
        try {
            if (mActivityStack.size() > 1) {
                activity = mActivityStack.get(mActivityStack.size() - 2);

                if (currentActivity.equals(activity)) {
                    int index = mActivityStack.indexOf(currentActivity);
                    if (index > 0) {
                        // if memory leaks or the last activity is being finished
                        activity = mActivityStack.get(index - 1);
                    } else if (mActivityStack.size() == 2) {
                        // if screen orientation changes, there may be an error sequence in the stack
                        activity = mActivityStack.lastElement();
                    }
                }
            }
        } catch (Exception ignored) {
        }
        return activity;
    }

    public boolean canSwipeBack() {
        return mActivityStack.size() > 1;
    }
}
