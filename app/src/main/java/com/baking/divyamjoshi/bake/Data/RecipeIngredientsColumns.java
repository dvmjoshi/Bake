package com.baking.divyamjoshi.bake.Data;

import net.simonvt.schematic.annotation.AutoIncrement;
import net.simonvt.schematic.annotation.DataType;
import net.simonvt.schematic.annotation.PrimaryKey;
import net.simonvt.schematic.annotation.References;

import static net.simonvt.schematic.annotation.DataType.Type.INTEGER;
import static net.simonvt.schematic.annotation.DataType.Type.REAL;
import static net.simonvt.schematic.annotation.DataType.Type.TEXT;

public interface RecipeIngredientsColumns {

    @DataType(INTEGER) @PrimaryKey @AutoIncrement String ID = "_id";

    @DataType(INTEGER) @References(
            table = RecipeDatabase.RECIPE_LIST, column = RecipeListColumns.RECIPE_ID) String RECIPE_LIST_ID = "recipeListId";

    @DataType(REAL) String QUANTITY = "quantity";

    @DataType(TEXT) String MEASUREMENT = "measurement";

    @DataType(TEXT) String INGREDIENT = "ingredient";
}
