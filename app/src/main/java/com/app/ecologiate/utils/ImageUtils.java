package com.app.ecologiate.utils;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.AppCompatImageView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import com.app.ecologiate.R;
import com.app.ecologiate.models.PuntoRecoleccion;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;

public class ImageUtils {


    public static BitmapDescriptor getMarkerIconFromPdr(Context context, PuntoRecoleccion pdr){
        View customMarkerView = ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.marker_layout, null);
        AppCompatImageView markerImageView = (AppCompatImageView) customMarkerView.findViewById(R.id.iv_material_marker);
        FrameLayout markerBackground = (FrameLayout)customMarkerView.findViewById(R.id.marker_backgroud);

        markerImageView.setImageResource(pdr.getImageResourceId());
        Drawable iconito = DrawableCompat.wrap(markerImageView.getDrawable());
        markerImageView.setImageDrawable(iconito);


        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP) {
            //markerBackground.setSupportBackgroundTintList(ContextCompat.getColorStateList(context, pdr.getBackgroundColor()));
        } else {
            ViewCompat.setBackgroundTintList(markerBackground, ContextCompat.getColorStateList(context, pdr.getBackgroundColor()));
            DrawableCompat.setTintList(iconito, ContextCompat.getColorStateList(context, pdr.getIconColor()));
        }

        customMarkerView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        customMarkerView.layout(0, 0, customMarkerView.getMeasuredWidth(), customMarkerView.getMeasuredHeight());
        customMarkerView.buildDrawingCache();
        Bitmap returnedBitmap = Bitmap.createBitmap(customMarkerView.getMeasuredWidth(), customMarkerView.getMeasuredHeight(),
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(returnedBitmap);
        canvas.drawColor(Color.WHITE, PorterDuff.Mode.SRC_IN);
        Drawable drawable = customMarkerView.getBackground();
        if (drawable != null)
            drawable.draw(canvas);
        customMarkerView.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(returnedBitmap);
    }

}
