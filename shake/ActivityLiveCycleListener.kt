package com.zapp.app.shake

import android.app.Activity
import android.app.Application
import android.os.Bundle

class ActivityLiveCycleListener(private val appStateListener: AppStateListener) :
    Application.ActivityLifecycleCallbacks {

    companion object {
        var foregroundActivities = 0
    }

    override fun onActivityPaused(p0: Activity) {

    }

    override fun onActivityStarted(p0: Activity) {
        if (foregroundActivities == 0) {
            appStateListener.onAppForeGround()
        }
        foregroundActivities++
    }

    override fun onActivityDestroyed(p0: Activity) {

    }

    override fun onActivitySaveInstanceState(p0: Activity, p1: Bundle) {

    }

    override fun onActivityStopped(p0: Activity) {
        foregroundActivities--
        if (foregroundActivities == 0) {
            appStateListener.onAppBackground()
        }
    }

    override fun onActivityCreated(p0: Activity, p1: Bundle?) {

    }

    override fun onActivityResumed(p0: Activity) {

    }
}