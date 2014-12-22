package hopper.image.utility;

import android.graphics.Bitmap;

import android.graphics.Bitmap.Config;
import android.graphics.BitmapShader;
import android.graphics.BlurMaskFilter;
import android.graphics.BlurMaskFilter.Blur;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.PorterDuff;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;



public class Transform {
	
	static public Bitmap roundedBorder(Bitmap bitmap){
		int w = bitmap.getWidth();                                          
		int h = bitmap.getHeight();                                         
	
		//int radius = Math.min(h / 2, w / 2);
		int radius = Math.min(h / 2, w / 2);
		Bitmap output = Bitmap.createBitmap(w + 8, h + 8, Config.ARGB_8888);
	
		Paint p = new Paint();                                              
		p.setAntiAlias(true);                                               
	
		Canvas c = new Canvas(output);                                      
		c.drawARGB(0, 0, 0, 0);                                             
		p.setStyle(Style.FILL);                                             
	
		c.drawCircle((w / 2) + 4, (h / 2) + 4, radius, p);                  
	
		p.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));                 
	
		c.drawBitmap(bitmap, 4, 4, p);                                      
		p.setXfermode(null);                                                
		p.setStyle(Style.STROKE);                                           
		p.setColor(Color.WHITE);                                            
		p.setStrokeWidth(3);                                                
		c.drawCircle((w / 2) + 4, (h / 2) + 4, radius, p);                  
	
		return output;   
	}
	
	static public Bitmap rectBorder(Bitmap bitmap){
		int borderSize = 5;
	    Bitmap bmpWithBorder = Bitmap.createBitmap(bitmap.getWidth() + borderSize * 2, bitmap.getHeight() + borderSize * 2, bitmap.getConfig());
	    Canvas canvas = new Canvas(bmpWithBorder);
	    canvas.drawColor(Color.WHITE);
	    canvas.drawBitmap(bitmap, borderSize, borderSize, null);
	    return bmpWithBorder;
	}
	
	public static Bitmap getRoundedCornerBitmapByCopy(Bitmap bitmap, int pixels) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap
                .getHeight(), Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);
        final float roundPx = pixels;

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        
        return output;
    }
	public static Bitmap roundCorners(Bitmap source, int radius) {
	    final Bitmap output = Bitmap.createBitmap(source.getWidth(), source
	            .getHeight(), Config.ARGB_8888);
	    final Canvas canvas = new Canvas(output);
	    final BitmapShader shader = new BitmapShader(source, Shader.TileMode.CLAMP,
        Shader.TileMode.CLAMP);
	   
	    final Paint paint = new Paint();
	    paint.setAntiAlias(true);
	    paint.setShader(shader);
	
	    // rect contains the bounds of the shape
	    // radius is the radius in pixels of the rounded corners
	    // paint contains the shader that will texture the shape
	    final RectF rect = new RectF(0.0f, 0.0f, source.getWidth(), source.getHeight());
	    canvas.drawRoundRect(rect, radius, radius, paint);
	
	    return output;
	}
	
	static public Bitmap highlightImage(Bitmap src) {		
        // create new bitmap, which will be painted and becomes result image
        Bitmap bmOut = Bitmap.createBitmap(src.getWidth() + 120, src.getHeight() + 96, Bitmap.Config.ARGB_8888);
        // setup canvas for painting
        Canvas canvas = new Canvas(bmOut);
        // setup default color
        canvas.drawColor(0, PorterDuff.Mode.CLEAR);
        // create a blur paint for capturing alpha
        Paint ptBlur = new Paint();
        ptBlur.setMaskFilter(new BlurMaskFilter(15, Blur.NORMAL));
        int[] offsetXY = new int[2];
        offsetXY[0] = 25;
        offsetXY[1] = 25;
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
	
	/*static public Bitmap highlightImage2(Bitmap src) {
		
    }*/
	
}
