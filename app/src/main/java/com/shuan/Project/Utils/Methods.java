package com.shuan.Project.Utils;

import android.content.Context;
import android.graphics.Typeface;

/**
 * Created by team-leader on 5/25/2016.
 */
public interface Methods {
    Typeface roboto(Context context);
    Typeface droid(Context context);
    String getTimeAgo(Context mContext,String created);
}
