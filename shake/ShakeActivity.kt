package com.zapp.app.shake

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.zapp.app.databinding.ActivityShakeBinding
import com.zapp.app.utils.PreferenceHelper
import dagger.hilt.android.AndroidEntryPoint
import java.io.BufferedReader
import java.io.File
import java.io.FileReader
import java.io.IOException
import java.io.PrintWriter
import javax.inject.Inject


@AndroidEntryPoint
class ShakeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityShakeBinding

    @Inject
    lateinit var preferenceHelper: PreferenceHelper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityShakeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.scrollView.post {
            binding.scrollView.fullScroll(View.FOCUS_DOWN);
        }
        setClickListeners()
        setLogs()

    }

    private fun setLogs() {
        try {
            val temp = File(cacheDir, "log.file")
            val path: String = temp.path
            val fileReader = FileReader(path)
            val bufferedReader = BufferedReader(fileReader)
            var buffer: String?
            val stringBuilder = StringBuilder()

            while (bufferedReader.readLine().also { buffer = it } != null) {
                stringBuilder.append(buffer)
                stringBuilder.append("\n")
            }
            binding.text.text = stringBuilder.toString()

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun clearLogs() {
        val logFile = File(cacheDir, "log.file")
        if (!logFile.exists()) {
            return
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

    private fun setClickListeners() {
        binding.apply {
            ivBack.setOnClickListener {
                finish()
            }
            tvClear.setOnClickListener {
                clearLogs()
                setLogs()
            }
        }
    }
}