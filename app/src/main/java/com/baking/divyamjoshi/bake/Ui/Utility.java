package com.baking.divyamjoshi.bake.Ui;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.preference.PreferenceManager;

import com.baking.divyamjoshi.bake.Data.RecipeDatabase;
import com.baking.divyamjoshi.bake.Data.RecipeListColumns;


public class Utility {

    private static final String SAVED_INGREDIENT = "saved_ingredient";

    /**
     * Method to get saved recipe name
     *
     * @param context Context
     */
    public static String getSavedIngredientName(Context context){
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        return sp.getString(SAVED_INGREDIENT, "");
    }

    /**
     * Method to save recipe name
     *
     * @param context Context
     * @param recipeName Name of recipe to be saved
     */
    public static void setSavedIngredientName(Context context, String recipeName){
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(SAVED_INGREDIENT,recipeName);
        editor.apply();
    }


    public static boolean recipeExist(Context context, int recipeId){

        SQLiteOpenHelper database = com.baking.divyamjoshi.bake.Data.generated.RecipeDatabase.getInstance(context);
        SQLiteDatabase db = database.getReadableDatabase();
        long exist = DatabaseUtils.queryNumEntries(db, RecipeDatabase.RECIPE_LIST, RecipeListColumns.RECIPE_ID + " = " + recipeId, null);
        db.close();

        return exist > 0;
    }
}
