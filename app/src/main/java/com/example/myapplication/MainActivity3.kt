package com.example.myapplication

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.example.myapplication.databinding.ActivityMain2Binding
import com.example.myapplication.databinding.ActivityMain3Binding
import kotlin.math.roundToInt

class MainActivity3 : AppCompatActivity() {

    private lateinit var binding: ActivityMain3Binding
    private var timesStarted = false
    private lateinit var serviceIntent :Intent
    private var time = 0.0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMain3Binding.inflate(layoutInflater)
        setContentView(R.layout.activity_main3)

        binding.button4.setOnClickListener { startStop() }
        binding.button5.setOnClickListener { resetTime() }

        serviceIntent = Intent(applicationContext,Times::class.java)
        registerReceiver(updateTime, IntentFilter(Times.TIME))

        val but : Button = findViewById(R.id.button6)
        val buts : Button = findViewById(R.id.button5)
        but.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        buts.setOnClickListener {
            startTime()
        }

    }

    private fun resetTime(){

        stopTime()
        time = 0.0
        binding.textView3.text = getTime(time)

    }

    private fun startTime(){
        if(timesStarted){
            stopTime()
        }
        else{
            startStop()
        }
    }


    private fun startStop(){
        serviceIntent.putExtra(Times.EXTRA,time)
        startService(serviceIntent)
        binding.button4.text = "Stop"
        timesStarted = false

    }

    private fun stopTime(){
        stopService(serviceIntent)
        binding.button4.text = "Start"
        timesStarted = true
    }

    private val updateTime: BroadcastReceiver = object : BroadcastReceiver(){
        override fun onReceive(context: Context , intent: Intent) {
            time = intent.getDoubleExtra(Times.EXTRA,0.0)
            binding.textView3.text = getTime(time)
        }
    }

    private fun getTime(time:Double):String{
        val resultInt = time.roundToInt()
        val hours = resultInt % 86400 / 3600
        val min = resultInt % 86400 % 3600 /60
        val sec = resultInt % 86400 % 3600 % 60

        return makeTime(hours,min,sec)
    }

    private fun makeTime(hour: Int,min:Int,sec:Int): String = String.format("%02d:%02d:%02d",hour,min,sec)

}