package com.example.fantasydraft.utils

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.TypedArray
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.View
import com.example.fantasydraft.R

//TODO CUSTOM_VIEW



class CircleCustomView (context: Context, attrs: AttributeSet,viewState : Int) :
    View(context, attrs){

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

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun drawFaceBackground(canvas: Canvas) {
        val d : Drawable  = resources.getDrawable(R.drawable.ic_check_circle_24)
        d.setBounds(10,60,50,10)
        d.draw(canvas)
    }

    fun setState(){
        background = context.getDrawable(R.drawable.ic_check_circle_24)
    }

    enum class State{
        DEFAULT,
        ERROR,
        CORRECT
    }
}