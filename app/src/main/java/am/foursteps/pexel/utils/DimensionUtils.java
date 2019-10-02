package am.foursteps.pexel.utils;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Point;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;

public class DimensionUtils {
   /**
    * This method converts dp unit to equivalent pixels, depending on device density.
    *
    * @param dp A value in dp (density independent pixels) unit. Which we need to convert into pixels
    * @return A float value to represent px equivalent to dp depending on device density
    */
   public static float convertDpToPixel(float dp){
       return dp * ((float) Resources.getSystem().getDisplayMetrics().densityDpi / DisplayMetrics.DENSITY_DEFAULT);
   }
   /**
    * This method converts device specific pixels to density independent pixels.
    *
    * @param px A value in px (pixels) unit. Which we need to convert into db
    * @param context Context to get resources and device specific display metrics
    * @return A float value to represent dp equivalent to px value
    */
   public static float convertPixelsToDp(float px, Context context){
       return px / ((float) context.getResources().getDisplayMetrics().densityDpi / DisplayMetrics.DENSITY_DEFAULT);
   }
   /**
    * @param context Context to get resources and device specific display metrics
    * @return A integer value to represent device width
    */
   public static int getDisplayWidth(Context context){
       WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
       Display display = wm.getDefaultDisplay();
       Point size = new Point();
       display.getSize(size);
       return size.x;
   }
   /**
    * @param context Context to get resources and device specific display metrics
    * @return A integer value to represent device height
    */
   public static int getDisplayHeight(Context context){
       WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
       Display display = wm.getDefaultDisplay();
       Point size = new Point();
       display.getSize(size);
       return size.y;
   }
}