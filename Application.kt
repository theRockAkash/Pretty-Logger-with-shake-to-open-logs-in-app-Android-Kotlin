package com.app.app.application


import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.util.Log
import com.app.app.shake.ActivityLiveCycleListener
import com.app.app.shake.AppStateListener
import com.app.app.shake.ShakeActivity
import java.io.File
import java.io.IOException
import java.io.PrintWriter
import javax.inject.Inject
import kotlin.math.sqrt

class Application : android.app.Application() {

    //For shake event *******************
    private var sensorManager: SensorManager? = null
    private var acceleration = 0f
    private var currentAcceleration = 0f
    private var lastAcceleration = 0f
    //********************

    override fun onCreate() {
        super.onCreate()

        //For shake event *******************
        if (com.app.app.BuildConfig.DEBUG) {
            clearLogs()
            sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
            registerActivityLifecycleCallbacks(ActivityLiveCycleListener(object : AppStateListener {
                override fun onAppForeGround() {
                    //start network listener
                    sensorManager?.registerListener(
                        sensorListener, sensorManager!!.getDefaultSensor(
                            Sensor.TYPE_ACCELEROMETER
                        ), SensorManager.SENSOR_DELAY_NORMAL
                    )
                }

                override fun onAppBackground() {
                    //remove network listener
                    sensorManager!!.unregisterListener(sensorListener)
                }
            }))

            acceleration = 10f
            currentAcceleration = SensorManager.GRAVITY_EARTH
            lastAcceleration = SensorManager.GRAVITY_EARTH
        }
        //********************************
    }


    //For shake event *******************
    private fun clearLogs() {
        val logFile = File(cacheDir, "log.file")
        if (!logFile.exists()) {
            try {
                logFile.createNewFile()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        val writer = PrintWriter(logFile)

        try {
            writer.print("")
            writer.close()
        } catch (e: IOException) {
            writer.close()
            e.printStackTrace()
        }
    }


    private val sensorListener: SensorEventListener by lazy {
        object : SensorEventListener {
            override fun onSensorChanged(event: SensorEvent) {

                // Fetching x,y,z values
                val x = event.values[0]
                val y = event.values[1]
                val z = event.values[2]
                lastAcceleration = currentAcceleration

                // Getting current accelerations
                // with the help of fetched x,y,z values
                currentAcceleration = sqrt((x * x + y * y + z * z).toDouble()).toFloat()
                val delta: Float = currentAcceleration - lastAcceleration
                acceleration = acceleration * 0.9f + delta

                // Display a Toast message if
                // acceleration value is over 12
                if (acceleration > 12) {
                    // Utils.toasty(applicationContext, "Shake event detected")
                    startActivity(
                        Intent(applicationContext, ShakeActivity::class.java).addFlags(
                            FLAG_ACTIVITY_NEW_TASK
                        )
                    )
                }
            }

            override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {}
        }
    }

    //*************************

}
