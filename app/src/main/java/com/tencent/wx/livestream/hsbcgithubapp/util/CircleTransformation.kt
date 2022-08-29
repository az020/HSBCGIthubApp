package com.tencent.wx.livestream.hsbcgithubapp.util

import android.graphics.*
import com.squareup.picasso.Transformation

private const val KEY = "circle"

/**
 * Picasso Transformation to make bitmaps circular
 */
class CircleTransformation : Transformation {

    override fun transform(source: Bitmap?): Bitmap? {
        source?.let{
            val size = it.width.coerceAtMost(it.height)
            val squaredBitmap = Bitmap.createBitmap(it, (it.width - size) / 2, (it.height - size) / 2, size, size)
            if (squaredBitmap != it) {
                it.recycle()
            }

            val bitmap = Bitmap.createBitmap(size, size, it.config)
            val shader = BitmapShader(squaredBitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)
            val paint = Paint().apply {
                isAntiAlias = true
                setShader(shader)
            }

            Canvas(bitmap).run {
                val radius = size / 2f
                drawCircle(radius, radius, radius, paint)
            }

            squaredBitmap.recycle()
            return bitmap
        }
        return source
    }

    override fun key(): String {
        return KEY
    }

}
