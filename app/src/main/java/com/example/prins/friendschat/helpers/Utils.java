package com.example.prins.friendschat.helpers;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;

import com.example.prins.friendschat.R;
import com.example.prins.friendschat.widget.FriendsAppWidget;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by prins on 2/17/2018.
 */

public class Utils {
    public static boolean isEmailValid(String email) {
        boolean isValid = false;

        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        CharSequence inputStr = email;

        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);
        if (matcher.matches()) {
            isValid = true;
        }
        return isValid;
    }

    public static void updateWidgets(Context context) {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);

        // Get a list of the widgets that need updating
        int[] widgetIds = appWidgetManager.getAppWidgetIds(
                new ComponentName(context, FriendsAppWidget.class)
        );

        appWidgetManager.notifyAppWidgetViewDataChanged(widgetIds, R.id.WidgetGrid);
    }

}
