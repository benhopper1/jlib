package com.example.hopperlibrary;

import android.graphics.AvoidXfermode.Mode;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BlurMaskFilter;
import android.graphics.BlurMaskFilter.Blur;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.PorterDuff;

public class HopperImageEffect {

	public HopperImageEffect() {
		// TODO Auto-generated constructor stub
	}
	//HopperImageEffect.roundCorner(Bitmap src, float round)
	public static Bitmap roundCorner(Bitmap src, float round) {
	    // image size
	    int width = src.getWidth();
	    int height = src.getHeight();
	    // create bitmap output
	    Bitmap result = Bitmap.createBitmap(width, height, Config.ARGB_8888);
	    // set canvas for painting
	    Canvas canvas = new Canvas(result);
	    canvas.drawARGB(0, 0, 0, 0);
	 
	    // config paint
	    final Paint paint = new Paint();
	    paint.setAntiAlias(true);
	    paint.setColor(Color.BLACK);
	 
	    // config rectangle for embedding
	    final Rect rect = new Rect(0, 0, width, height);
	    final RectF rectF = new RectF(rect);
	 
	    // draw rect to canvas
	    canvas.drawRoundRect(rectF, round, round, paint);
	 
	    // create Xfer mode
	    paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
	    // draw source image to canvas
	    canvas.drawBitmap(src, rect, rect, paint);
	 
	    // return final image
	    return result;
	}
	public static Bitmap doHighlightImage(Bitmap src) {
		// create new bitmap, which will be painted and becomes result image
		Bitmap bmOut = Bitmap.createBitmap(src.getWidth() + 96, src.getHeight() + 96, Bitmap.Config.ARGB_8888);
		// setup canvas for painting
		Canvas canvas = new Canvas(bmOut);
		// setup default color
		canvas.drawColor(0, PorterDuff.Mode.CLEAR);

		// create a blur paint for capturing alpha
		Paint ptBlur = new Paint();
		ptBlur.setMaskFilter(new BlurMaskFilter(15, Blur.NORMAL));
		int[] offsetXY = new int[2];
		// capture alpha into a bitmap
		Bitmap bmAlpha = src.extractAlpha(ptBlur, offsetXY);
		// create a color paint
		Paint ptAlphaColor = new Paint();
		ptAlphaColor.setColor(0xFFFFFFFF);
		// paint color for captured alpha region (bitmap)
		canvas.drawBitmap(bmAlpha, offsetXY[0], offsetXY[1], ptAlphaColor);
		// free memory
		bmAlpha.recycle();

		// paint the image source
		canvas.drawBitmap(src, 0, 0, null);

		// return out final image
		return bmOut;
	}

}
