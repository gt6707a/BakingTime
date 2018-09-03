package com.android.gt6707a.bakingtime;

import android.content.Context;
import android.content.res.Configuration;

public class Utils {
    public static boolean hasTwoPanes(Context context) {
        return context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE
                && context.getResources().getConfiguration().smallestScreenWidthDp >= context.getResources().getInteger(R.integer.smallest_screen_width);
    }

    public static boolean isSinglePaneLandscape(Context context) {
        return context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE
                && context.getResources().getConfiguration().smallestScreenWidthDp < context.getResources().getInteger(R.integer.smallest_screen_width);
    }
}
