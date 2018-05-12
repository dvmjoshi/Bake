package com.baking.divyamjoshi.bake.Widget;

import android.annotation.TargetApi;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.widget.RemoteViews;

import com.baking.divyamjoshi.bake.Baking.InstructionsActivity;
import com.baking.divyamjoshi.bake.MainActivity;
import com.baking.divyamjoshi.bake.R;
import com.baking.divyamjoshi.bake.Ui.Utility;

public class FavoriteRecipeWidgetProvider extends AppWidgetProvider {

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        CharSequence widgetText = context.getString(R.string.app_name);
        String recipeName = Utility.getSavedIngredientName(context);
        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.favorite_recipe_widget);

        if(recipeName.equals("")) {
            views.setTextViewText(R.id.appwidget_text, widgetText);
        } else {
            views.setTextViewText(R.id.appwidget_text, recipeName);

        }

        Intent intent = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
        views.setOnClickPendingIntent(R.id.widget_appBar, pendingIntent);

        // Set up the collection
        setRemoteAdapter(context, views);

        boolean useDetailActivity = context.getResources()
                .getBoolean(R.bool.use_detail_activity);
        Intent clickIntentTemplate = useDetailActivity
                ? new Intent(context, InstructionsActivity.class)
                : new Intent(context,MainActivity.class);
        PendingIntent clickPendingIntent = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            clickPendingIntent = TaskStackBuilder.create(context)
                    .addNextIntentWithParentStack(clickIntentTemplate)
                    .getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        }
        views.setPendingIntentTemplate(R.id.widget_list, clickPendingIntent);
        views.setEmptyView(R.id.widget_list, R.id.widget_emptyView);

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onAppWidgetOptionsChanged(Context context, AppWidgetManager appWidgetManager, int appWidgetId, Bundle newOptions) {
        context.startService(new Intent(context, WidgetRemoteViewsService.class));

    }

    @Override
    public void onReceive(Context context, Intent intent) {

        super.onReceive(context, intent);

        if(InstructionsActivity.ACTION_DATA_UPDATED.equals(intent.getAction())) {
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            int[] appwidgetIds = appWidgetManager.getAppWidgetIds(
                    new ComponentName(context, getClass())
            );
            appWidgetManager.notifyAppWidgetViewDataChanged(appwidgetIds, R.id.widget_list);
            onUpdate(context,appWidgetManager,appwidgetIds);
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private static void setRemoteAdapter(Context context, @NonNull final RemoteViews views){
        views.setRemoteAdapter(R.id.widget_list,
                new Intent(context, WidgetRemoteViewsService.class));
    }

    public static void setWidgetText(Context context, String name){

        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.favorite_recipe_widget);

        views.setTextViewText(R.id.appwidget_text,name);

    }

}

