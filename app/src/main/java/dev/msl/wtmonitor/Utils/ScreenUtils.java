package dev.msl.wtmonitor.Utils;

import android.content.Context;
import android.content.res.Resources;
import android.util.TypedValue;

public class ScreenUtils {

    // Method for converting DP/DIP value to pixels
    public static int getPixelsFromDPs(Context c, int dps) {
        /*
            public abstract Resources getResources ()

                Return a Resources instance for your application's package.
        */
        Resources r = c.getResources();

        /*
            TypedValue

                Container for a dynamically typed data value. Primarily
                used with Resources for holding resource values.
        */

        /*
            applyDimension(int unit, float value, DisplayMetrics metrics)

                Converts an unpacked complex data value holding
                a dimension to its final floating point value.
        */

        /*
            Density-independent pixel (dp)

                A virtual pixel unit that you should use when defining UI layout,
                to express layout dimensions or position in a density-independent way.

                The density-independent pixel is equivalent to one physical pixel on
                a 160 dpi screen, which is the baseline density assumed by the system
                for a "medium" density screen. At runtime, the system transparently handles
                any scaling of the dp units, as necessary, based on the actual density
                of the screen in use. The conversion of dp units to screen pixels
                is simple: px = dp * (dpi / 160). For example, on a 240 dpi screen,
                1 dp equals 1.5 physical pixels. You should always use dp
                units when defining your application's UI, to ensure proper
                display of your UI on screens with different densities.
        */

        /*
            public static final int COMPLEX_UNIT_DIP

                TYPE_DIMENSION complex unit: Value is Device Independent Pixels.
                Constant Value: 1 (0x00000001)
        */
        return (int) (TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, dps, r.getDisplayMetrics()));
    }

}
