package com.example.myapplication

import android.app.Service
import android.content.Intent
import android.os.IBinder
import com.google.android.gms.tasks.Tasks
import java.util.Timer
import java.util.TimerTask

class Times : Service()
{


    override fun onBind(p0: Intent?): IBinder? = null

    private val timer = Timer()

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        val time = intent.getDoubleExtra(EXTRA,0.0)
        timer.scheduleAtFixedRate(Task(time),0,1000)
        return START_NOT_STICKY

    }

    override fun onDestroy() {
        timer.cancel()
        super.onDestroy()
    }

    private inner class Task(private var time:Double): TimerTask(){
        override fun run(){
            val intent = Intent(TIME)
            time++
            intent.putExtra(EXTRA,time)
            sendBroadcast(intent)
        }
    }


    companion object{
        const val TIME = "timeUpdate"
        const val EXTRA = "timeExtra"
    }
}