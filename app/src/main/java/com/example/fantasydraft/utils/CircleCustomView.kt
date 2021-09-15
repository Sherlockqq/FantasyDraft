package com.example.fantasydraft.utils

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import com.example.fantasydraft.R

//TODO CUSTOM_VIEW
class CircleCustomView (context: Context, attrs: AttributeSet) : View(context, attrs){

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    // Some colors for the face background, eyes and mouth.
    private var unUsedColor = Color.GRAY

    // View size in pixels
    private var size = 120


    override fun onDraw(canvas: Canvas) {
        // call the super method to keep any drawing from the parent side.
        super.onDraw(canvas)
        drawFaceBackground(canvas)

    }

    private fun drawFaceBackground(canvas: Canvas) {

        paint.color = unUsedColor
        paint.style = Paint.Style.FILL

        val radius = size / 2f

        canvas.drawCircle(size / 2f, size / 2f, radius, paint)
    }
}