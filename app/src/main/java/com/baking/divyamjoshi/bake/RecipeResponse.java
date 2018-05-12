package com.baking.divyamjoshi.bake;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;


public class RecipeResponse implements Parcelable{


    private int id;
    private String name;
    private int servings;
    private String image;
    private ArrayList<IngredientsBean> ingredients;
    private ArrayList<StepsBean> steps;

    public RecipeResponse() {
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeInt(servings);
        dest.writeString(image);
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList("ingredientsList",ingredients);
        bundle.putParcelableArrayList("stepsList", steps);
        dest.writeBundle(bundle);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<RecipeResponse> CREATOR = new Creator<RecipeResponse>() {
        @Override
        public RecipeResponse createFromParcel(Parcel in) {
            RecipeResponse recipeResponse = new RecipeResponse();
            recipeResponse.id = in.readInt();
            recipeResponse.image = in.readString();
            recipeResponse.name = in.readString();
            recipeResponse.servings = in.readInt();
            Bundle bundle = in.readBundle(IngredientsBean.class.getClassLoader());
            Bundle bundle1 = in.readBundle(StepsBean.class.getClassLoader());
            recipeResponse.ingredients = bundle.getParcelableArrayList("ingredientsList");
            recipeResponse.steps = bundle1.getParcelableArrayList("stepsList");
            return recipeResponse;
        }

        @Override
        public RecipeResponse[] newArray(int size) {
            return new RecipeResponse[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getServings() {
        return servings;
    }

    public String getImage() {
        return image;
    }

    public ArrayList<IngredientsBean> getIngredients() {
        return ingredients;
    }


    public ArrayList<StepsBean> getSteps() {
        return steps;
    }


    public static class IngredientsBean implements Parcelable{

        private double quantity;
        private String measure;
        private String ingredient;


        protected IngredientsBean(Parcel in) {
            quantity = in.readDouble();
            measure = in.readString();
            ingredient = in.readString();
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeDouble(quantity);
            dest.writeString(measure);
            dest.writeString(ingredient);
        }

        @Override
        public int describeContents() {
            return 0;
        }

        public static final Creator<IngredientsBean> CREATOR = new Creator<IngredientsBean>() {
            @Override
            public IngredientsBean createFromParcel(Parcel in) {
                return new IngredientsBean(in);
            }

            @Override
            public IngredientsBean[] newArray(int size) {
                return new IngredientsBean[size];
            }
        };

        public double getQuantity() {
            return quantity;
        }

        public String getMeasure() {
            return measure;
        }

        public String getIngredient() {
            return ingredient;
        }
    }

    public static class StepsBean implements Parcelable{


        private int id;
        private String shortDescription;
        private String description;
        private String videoURL;
        private String thumbnailURL;

        protected StepsBean(Parcel in) {
            id = in.readInt();
            shortDescription = in.readString();
            description = in.readString();
            videoURL = in.readString();
            thumbnailURL = in.readString();
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(id);
            dest.writeString(shortDescription);
            dest.writeString(description);
            dest.writeString(videoURL);
            dest.writeString(thumbnailURL);
        }

        @Override
        public int describeContents() {
            return 0;
        }

        public static final Creator<StepsBean> CREATOR = new Creator<StepsBean>() {
            @Override
            public StepsBean createFromParcel(Parcel in) {
                return new StepsBean(in);
            }

            @Override
            public StepsBean[] newArray(int size) {
                return new StepsBean[size];
            }
        };

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getShortDescription() {
            return shortDescription;
        }

        public String getDescription() {
            return description;
        }

        public String getVideoURL() {
            return videoURL;
        }

        public String getThumbnailURL() {
            return thumbnailURL;
        }
    }
}
