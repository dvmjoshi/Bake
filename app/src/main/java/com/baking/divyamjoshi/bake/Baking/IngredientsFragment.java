package com.baking.divyamjoshi.bake.Baking;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.baking.divyamjoshi.bake.R;
import com.baking.divyamjoshi.bake.RecipeResponse;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by Divyam on 06-12-2017.
 */

public class IngredientsFragment extends Fragment {
    public static final String INGREDIENTS_LIST = "ingredientsList";

    @BindView(R.id.ingredients_recyclerView)RecyclerView recyclerView;

    private ArrayList<RecipeResponse.IngredientsBean> ingredientsList;
    private Unbinder unbinder;

    public static IngredientsFragment newInstance(ArrayList<RecipeResponse.IngredientsBean> list){
        IngredientsFragment fragment = new IngredientsFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(INGREDIENTS_LIST,list);
        fragment.setArguments(bundle);
        return fragment;
    }

    public IngredientsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ingredientsList = getArguments().getParcelableArrayList(INGREDIENTS_LIST);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_ingredients, container, false);
        unbinder = ButterKnife.bind(this,view);

        IngredientsAdapter ingredientsRecyclerAdapter = new IngredientsAdapter(getContext(), ingredientsList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false));
        recyclerView.setAdapter(ingredientsRecyclerAdapter);

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
