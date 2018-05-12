package com.baking.divyamjoshi.bake;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.test.espresso.IdlingResource;
import android.support.v7.app.AppCompatActivity;

import com.baking.divyamjoshi.bake.UiTesting.SimpleIdlingResource;

import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {
    @Nullable
    private SimpleIdlingResource mIdlingResource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        // Refresh recipe list in case database was updated
        RecipeFragment fragment = (RecipeFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_recipe);
        fragment.refreshList();
    }

    @VisibleForTesting
    @NonNull
    public IdlingResource getIdlingResource() {
        if (mIdlingResource == null) {
            mIdlingResource = new SimpleIdlingResource();
        }
        return mIdlingResource;
    }

}
