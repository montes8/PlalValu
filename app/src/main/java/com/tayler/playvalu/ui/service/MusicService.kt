package com.tayler.playvalu.ui.service

import android.annotation.SuppressLint
import android.app.Service
import android.content.Intent
import android.graphics.PixelFormat
import android.os.Build
import android.os.IBinder
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.setViewTreeLifecycleOwner
import androidx.savedstate.setViewTreeSavedStateRegistryOwner
import com.tayler.playvalu.R
import kotlin.math.abs

class MusicService : Service() {

    override fun onBind(intent: Intent?): IBinder? = null

    private val windowManager get() = getSystemService(WINDOW_SERVICE) as WindowManager

    private lateinit var  floatingView : ComposeView

    private lateinit var lifecycleOwner: MyLifecycleOwner
    internal var timeStart: Long = 0
    internal var timeEnd: Long = 0

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate() {
        super.onCreate()
        showOverlay()
    }

    private var windowType = 0
    private var windowFlag = 0
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
            ComposeView(
                clickScope = {
                    Toast.makeText(this, "Bye bye ComposeView", Toast.LENGTH_LONG).show()
                    stopSelf()
                }
            )
        }

        lifecycleOwner = MyLifecycleOwner()
        lifecycleOwner.performRestore(null)
        lifecycleOwner.handleLifecycleEvent(Lifecycle.Event.ON_CREATE)

        floatingView.setViewTreeLifecycleOwner(lifecycleOwner)
        floatingView.setViewTreeSavedStateRegistryOwner(lifecycleOwner)

        windowManager.addView(floatingView, params)
        implementTouchListenerToFloatingWidgetView()
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

@Composable
fun ComposeView(
    clickScope: () -> Unit = {}
) {
    Image(
        painterResource(R.drawable.ic_music),
        contentDescription = "",
        contentScale = ContentScale.Crop
    )
}