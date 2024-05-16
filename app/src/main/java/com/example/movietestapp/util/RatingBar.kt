package com.example.movietestapp.util

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.View
import androidx.annotation.ColorInt
import com.example.movietestapp.R
import kotlin.math.floor

class RatingBar: View {
    private var rating: Double = 0.0
    private var stars: Int = 5
    private var starsColor: Int = Color.YELLOW
    private var starSize: Float = 50f

    private val paint = Paint()

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        parseAttributes(attrs)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        parseAttributes(attrs)
    }

    private fun parseAttributes(attrs: AttributeSet?) {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.RatingBarView)
        rating = typedArray.getFloat(R.styleable.RatingBarView_rating, 0.0f).toDouble()
        stars = typedArray.getInt(R.styleable.RatingBarView_stars, 5)
        starsColor = typedArray.getColor(R.styleable.RatingBarView_starsColor, Color.YELLOW)
        starSize = typedArray.getDimension(R.styleable.RatingBarView_starSize, 50f)
        typedArray.recycle()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val filledStars = floor(rating).toInt()
        val halfStar = !(rating.rem(1).equals(0.0))

        paint.color = starsColor

        for (i in 0 until filledStars) {
            drawStar(canvas, i * (starSize * 2 + 10), 0f)
        }

        if (halfStar) {
            drawHalfStar(canvas, filledStars * (starSize * 2 + 10), 0f)
        }

        for (i in filledStars + (if (halfStar) 1 else 0) until stars) {
            drawEmptyStar(canvas, i * (starSize * 2 + 10), 0f)
        }
    }

    private fun drawStar(canvas: Canvas, x: Float, y: Float) {
        val path = Path()
        path.moveTo(x + starSize / 2, y)
        path.lineTo(x + starSize * 1.5f, y + starSize * 3 / 5)
        path.lineTo(x + starSize * 3 / 2, y + starSize)
        path.lineTo(x + starSize, y + starSize)
        path.lineTo(x + starSize * 1.25f, y + starSize * 4 / 5)
        path.lineTo(x + starSize / 2, y + starSize)
        path.lineTo(x + starSize * 3 / 4, y + starSize * 3 / 5)
        path.lineTo(x, y + starSize * 3 / 5)
        path.lineTo(x + starSize / 2, y)
        path.close()
        canvas.drawPath(path, paint)
    }

    private fun drawHalfStar(canvas: Canvas, x: Float, y: Float) {
        val path = Path()
        path.moveTo(x + starSize / 2, y)
        path.lineTo(x + starSize * 1.25f, y)
        path.lineTo(x + starSize * 3 / 2, y + starSize)
        path.lineTo(x + starSize, y + starSize)
        path.lineTo(x + starSize * 1.25f, y + starSize * 4 / 5)
        path.lineTo(x + starSize / 2, y + starSize)
        path.lineTo(x + starSize * 3 / 4, y + starSize * 3 / 5)
        path.lineTo(x, y + starSize * 3 / 5)
        path.lineTo(x + starSize / 2, y)
        path.close()
        canvas.drawPath(path, paint)
    }

    private fun drawEmptyStar(canvas: Canvas, x: Float, y: Float) {
        val path = Path()
        path.moveTo(x + starSize / 2, y)
        path.lineTo(x + starSize * 1.5f, y)
        path.lineTo(x + starSize * 2, y + starSize * 3 / 5)
        path.lineTo(x + starSize * 3 / 2, y + starSize)
        path.lineTo(x + starSize, y + starSize)
        path.lineTo(x + starSize / 2, y + starSize * 3 / 4)
        path.lineTo(x, y + starSize)
        path.lineTo(x + starSize / 4, y + starSize * 3 / 5)
        path.lineTo(x + starSize / 2, y)
        path.close()
        canvas.drawPath(path, paint)
    }

    fun setRating(rating: Double) {
        this.rating = rating
        invalidate()
    }

    fun setStars(stars: Int) {
        this.stars = stars
        invalidate()
    }

    fun setStarsColor(@ColorInt color: Int) {
        this.starsColor = color
        invalidate()
    }

    fun setStarSize(size: Float) {
        this.starSize = size
        invalidate()
    }
}