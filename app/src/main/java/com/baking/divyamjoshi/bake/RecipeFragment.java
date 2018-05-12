package com.baking.divyamjoshi.bake;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.baking.divyamjoshi.bake.Baking.InstructionsActivity;
import com.baking.divyamjoshi.bake.Ui.GridSpacing;
import com.baking.divyamjoshi.bake.Ui.Singleton;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

@SuppressWarnings("ALL")
public class RecipeFragment extends Fragment {
    private Context mContext;
   RecyclerView mRecyclerView;
    @BindView(R.id.main_progress_bar)ProgressBar mProgressBar;
    @BindView(R.id.recipe_emptyView)TextView mEmptyView;
    CoordinatorLayout mCoordinatorLayout;

    private List<RecipeResponse> recipeResponseList;
    private RecipeRecyclerAdapter adapter;
    private Unbinder unbinder;

    public RecipeFragment() {
        // Required empty public constructor
    }
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mContext = activity;
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_recipe, container, false);
      mRecyclerView=ButterKnife.findById(view,R.id.recipe_recyclerView);
      mCoordinatorLayout=ButterKnife.findById(view,R.id.recipe_coordinator_layout);
        unbinder = ButterKnife.bind(this,view);

        setData();

        return view;

    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    /**
     * Method that retrieves the data
     */
    private void setData(){

        String url = "https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/baking.json";

        if(checkConnection(getContext())) {

            StringRequest recipeRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                // Set up Gson to parse Json
                Gson gson = new GsonBuilder().create();
                Type recipeListType = new TypeToken<ArrayList<RecipeResponse>>() {
                }.getType();
                // Update recipe list
                recipeResponseList = gson.fromJson(response, recipeListType);

                if (mContext.getResources().getBoolean(R.bool.tablet_mode)) {
                    adapter = new RecipeRecyclerAdapter(getContext(), recipeResponseList);
                    mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 1));
                    mRecyclerView.setHasFixedSize(true);
                    mRecyclerView.addItemDecoration(new GridSpacing(1,
                            GridSpacing.dpToPx(getContext(), 0), true));

                    mRecyclerView.setAdapter(adapter);
                  //  getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);
                } else {
                    adapter = new RecipeRecyclerAdapter(getContext(), recipeResponseList);
                    mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
                    mRecyclerView.setAdapter(adapter);
                }

                // Set up recycler item click listener
                adapter.setOnItemClickListener(new RecipeRecyclerAdapter.RecipeCardClickListener() {
                    @Override
                    public void onRecipeCardClicked(View view, int position) {

                        Intent intent = new Intent(getActivity(), InstructionsActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putInt(getString(R.string.position), position);
                        bundle.putString(getString(R.string.recipeName), recipeResponseList.get(position).getName());
                        bundle.putInt(getString(R.string.recipeId), recipeResponseList.get(position).getId());
                        bundle.putInt(getString(R.string.servingSize), recipeResponseList.get(position).getServings());
                        bundle.putParcelableArrayList(getString(R.string.iList), recipeResponseList.get(position).getIngredients());
                        bundle.putParcelableArrayList(getString(R.string.sList), recipeResponseList.get(position).getSteps());
                        intent.putExtras(bundle);
                        startActivityForResult(intent, 1);
                    }
                });

                mProgressBar.setVisibility(View.INVISIBLE);
                mEmptyView.setVisibility(View.INVISIBLE);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        Singleton.getInstance(getContext().getApplicationContext()).addToRequestQueue(recipeRequest);

        } else {
            mEmptyView.setVisibility(View.VISIBLE);
            mProgressBar.setVisibility(View.INVISIBLE);

            Snackbar snackbar = Snackbar.make(mCoordinatorLayout,
                    R.string.recipe_empty_view, Snackbar.LENGTH_INDEFINITE)
                    .setAction(R.string.retry, new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            setData();
                        }
                    });
            snackbar.show();

        }

    }

    /**
     * Method that checks if there is a valid network connection
     *
     * @param context Context
     * @return A boolean if there is a valid network connection
     */
    private boolean checkConnection(Context context){
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
    }


    /**
     * Method that refreshes the recipe list
     */
    public void refreshList(){
        if(adapter != null) {
            adapter.notifyDataSetChanged();
        }
    }
}
