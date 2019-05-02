package br.com.cantinho.bestroute.utils

import com.google.android.gms.maps.model.BitmapDescriptorFactory

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import br.com.cantinho.bestroute.R
import com.google.android.gms.maps.model.BitmapDescriptor



object MapUtils {

    //TODO fix this. Icon position keeps at the wrong location
    @JvmStatic fun getMarkerIconFromDrawable(context: Context, drawable: Drawable): BitmapDescriptor {
        val canvas = Canvas()
        val bitmap = Bitmap.createBitmap(drawable.intrinsicWidth, drawable.intrinsicHeight, Bitmap.Config.ARGB_8888)
        canvas.setBitmap(bitmap)
        drawable.setBounds(
            0,
            0,
            context.resources.getDimension(R.dimen.marker_height).toInt(),
            context.resources.getDimension(R.dimen.marker_width).toInt()
        )
        drawable.draw(canvas)
        return BitmapDescriptorFactory.fromBitmap(bitmap)
    }


}