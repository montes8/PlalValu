package com.tayler.playvalu.ui.service

import android.annotation.SuppressLint
import android.app.Service
import android.content.Intent
import android.graphics.PixelFormat
import android.os.Build
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.WindowManager
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.setViewTreeLifecycleOwner
import androidx.savedstate.setViewTreeSavedStateRegistryOwner
import com.tayler.playvalu.R
import com.tayler.playvalu.component.MediaPlayerSingleton
import kotlin.math.abs

class MusicService : Service() {

    override fun onBind(intent: Intent?): IBinder? = null

    private val windowManager get() = getSystemService(WINDOW_SERVICE) as WindowManager

    private lateinit var  floatingView : ComposeView

    private lateinit var lifecycleOwner: MyLifecycleOwner
    internal var timeStart: Long = 0
    internal var timeEnd: Long = 0
    var sleep : Thread? = null

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate() {
        super.onCreate()
        showOverlay()
        MediaPlayerSingleton.playStartUpdate(MediaPlayerSingleton.listMusic[MediaPlayerSingleton.positionMusic].path
            ,MediaPlayerSingleton.positionDurationMusic)
        MediaPlayerSingleton.positionDurationMusic = MediaPlayerSingleton.playCurrentPosition()/10
        MediaPlayerSingleton.DurationTotalMusic = MediaPlayerSingleton.playDuration()/10
        detectedNextMusic()
    }

    private fun detectedNextMusic(){
         sleep = Thread({
            while (MediaPlayerSingleton.positionDurationMusic < MediaPlayerSingleton.DurationTotalMusic) {
                Thread.sleep((1000).toLong())
                MediaPlayerSingleton.positionDurationMusic = MediaPlayerSingleton.playCurrentPosition()/10
                try {
                    if (MediaPlayerSingleton.positionDurationMusic > MediaPlayerSingleton.DurationTotalMusic-10){
                        try {
                            var positionUpdate =  MediaPlayerSingleton.positionMusic
                            if (positionUpdate < MediaPlayerSingleton.listMusic.size -1){
                                MediaPlayerSingleton.positionMusic = positionUpdate +1
                                MediaPlayerSingleton.positionDurationMusic = 0
                                MediaPlayerSingleton.playStart(MediaPlayerSingleton.listMusic[MediaPlayerSingleton.positionMusic].path)
                                MediaPlayerSingleton.DurationTotalMusic = MediaPlayerSingleton.playDuration()/10
                            }
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        })
        sleep?.start()
    }

    private var windowType = 0
    private var windowFlag = 0
    @SuppressLint("ImplicitSamInstance")
    @RequiresApi(Build.VERSION_CODES.O)
    private fun showOverlay() {
        windowType =
            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
        windowFlag = WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN or
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or
                WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL or
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON

        val params = WindowManager.LayoutParams(
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.WRAP_CONTENT,
            windowType,
            windowFlag,
            PixelFormat.TRANSLUCENT
        )

        floatingView = ComposeView(this)
        floatingView.setContent {
            ComposeViewCreate(
                clickClose = {
                    try {
                        MediaPlayerSingleton.playStop()
                        sleep?.interrupt()
                        stopService(Intent(this, MusicService::class.java))
                    }catch (e: Exception){
                        e.printStackTrace()
                    }
                }
            )
        }

        lifecycleOwner = MyLifecycleOwner()
        lifecycleOwner.performRestore(null)
        lifecycleOwner.handleLifecycleEvent(Lifecycle.Event.ON_CREATE)
        floatingView.setViewTreeLifecycleOwner(lifecycleOwner)
        floatingView.setViewTreeSavedStateRegistryOwner(lifecycleOwner)
        windowManager.addView(floatingView, params)
        Handler(Looper.getMainLooper()).postDelayed({
            implementTouchListenerToFloatingWidgetView()
        },1000)

    }

    override fun onDestroy() {
        super.onDestroy()
        lifecycleOwner.handleLifecycleEvent(Lifecycle.Event.ON_DESTROY)
        if (floatingView.parent != null) {
            floatingView.clearAnimation()
            floatingView.setViewTreeLifecycleOwner(null)
            floatingView.setViewTreeSavedStateRegistryOwner(null)
            windowManager.removeView(floatingView)
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun implementTouchListenerToFloatingWidgetView() {
        floatingView.setOnTouchListener(object : View.OnTouchListener {
            private var lastAction: Int = 0
            private var initialX: Int = 0
            private var initialY: Int = 0
            private var initialTouchX: Float = 0.toFloat()
            private var initialTouchY: Float = 0.toFloat()

            override fun onTouch(v: View, event: MotionEvent): Boolean {
                Log.d("eventFloating", "onTouch")
                val params = floatingView?.layoutParams as WindowManager.LayoutParams
                val xCord = event.rawX
                val yCord = event.rawY
                when (event.action) {
                    MotionEvent.ACTION_DOWN -> {
                        timeStart = System.currentTimeMillis()
                        initialX = params.x
                        initialY = params.y

                        initialTouchX = event.rawX
                        initialTouchY = event.rawY

                        lastAction = event.action
                        return true
                    }
                    MotionEvent.ACTION_UP -> {
                        timeEnd = System.currentTimeMillis()
                        val xDiff = xCord - initialTouchX
                        val yDiff = yCord - initialTouchY
                        if (abs(xDiff) < 5 && abs(yDiff) < 5 && (timeEnd - timeStart < 300)) {

                        }
                        lastAction = event.action
                        return true
                    }
                    MotionEvent.ACTION_MOVE -> {
                        params.x = initialX + (event.rawX - initialTouchX).toInt()
                        params.y = initialY + (event.rawY - initialTouchY).toInt()
                        windowManager?.updateViewLayout(floatingView, params)
                        lastAction = event.action
                        return true
                    }
                }
                return false
            }
        })
    }
}

@SuppressLint("UnrememberedMutableInteractionSource")
@Composable
fun ComposeViewCreate(
    clickClose: () -> Unit = {}
) {

    Box(modifier = Modifier.size(52.dp)){
        Image(
            painterResource(R.drawable.ic_close),
            modifier = Modifier.size(18.dp).clickable{
                clickClose()
            }.align(Alignment.TopEnd),
            contentDescription = "",
            contentScale = ContentScale.Crop
        )
        Image(
            painterResource(R.drawable.ui_ic_music_two),
            modifier = Modifier.size(72.dp).align(Alignment.TopCenter),
            contentDescription = "",
            contentScale = ContentScale.Crop
        )
    }


}