package com.midina.core_ui.ui

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.View
import android.graphics.Bitmap

import android.graphics.drawable.BitmapDrawable
import com.midina.core_ui.R

//TODO Make Default circle size as others size

class CheckStateView (context: Context, attrs: AttributeSet) :
    View(context, attrs) {

    private val paint = Paint()

    // Some colors for the face background, eyes and mouth.
    private var stateImage = State.DEFAULT

    // View size in pixels
    private var size = 120


    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onDraw(canvas: Canvas) {

        super.onDraw(canvas)

        when(stateImage) {
            State.DEFAULT -> {
                val d = resources.getDrawable(R.drawable.default_state)
                val bitmap =  drawableToBitmap(d)
                if (bitmap != null) {
                    canvas.drawBitmap(bitmap,null, Rect(0,0,160,160),paint)
                }
            }
            State.CORRECT -> {
                val d = resources.getDrawable(R.drawable.ic_check_circle_24)
                val bitmap =  drawableToBitmap(d)
                if (bitmap != null) {
                    canvas.drawBitmap(bitmap,null, Rect(0,0,160,160),paint)
                }
            }
            State.ERROR -> {
                val d = resources.getDrawable(R.drawable.ic_baseline_cancel_24)
                val bitmap =  drawableToBitmap(d)
                if (bitmap != null) {
                    canvas.drawBitmap(bitmap,null, Rect(0,0,160,160),paint)
                }
            }
        }
    }
    fun drawableToBitmap(drawable: Drawable): Bitmap? {
        if (drawable is BitmapDrawable) {
            return drawable.bitmap
        }
        val bitmap = Bitmap.createBitmap(drawable.intrinsicWidth, drawable.intrinsicHeight, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        drawable.setBounds(0, 0, canvas.width, canvas.height)
        drawable.draw(canvas)
        return bitmap
    }
    fun setState(state : State){
        when(state){
            State.DEFAULT -> stateImage = State.DEFAULT
            State.CORRECT -> stateImage = State.CORRECT
            State.ERROR -> stateImage = State.ERROR
        }
        invalidate()
   }
}

enum class State{
    DEFAULT,
    ERROR,
    CORRECT
}

