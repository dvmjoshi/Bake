package com.baking.divyamjoshi.bake.Baking;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.baking.divyamjoshi.bake.R;
import com.baking.divyamjoshi.bake.RecipeResponse;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Divyam on 06-12-2017.
 */

public class IngredientsAdapter extends RecyclerView.Adapter<IngredientsAdapter.ViewHolder> {
    private ArrayList<RecipeResponse.IngredientsBean> iList;
    private Context context;
    private Orientation mOrientation;
    private LayoutInflater mLayoutInflater;
    /**
     * IngredientsAdapter constructor
     */
    public IngredientsAdapter(Context context, ArrayList<RecipeResponse.IngredientsBean> iList){
        this.context = context;
        this.iList = iList;

    }

    @Override
    public IngredientsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.ingredients_recycler_item,parent,false);
        return new IngredientsAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(IngredientsAdapter.ViewHolder holder, int position) {

        String iName = iList.get(position).getIngredient();
        String iMeasure = iList.get(position).getMeasure();
        double iQuantity = iList.get(position).getQuantity();
        String ingredientAmount = context.getString(R.string.ingredient_amount,Double.toString(iQuantity),iMeasure);

        holder.name.setText(iName);
        holder.amount.setText(ingredientAmount);
    }

    @Override
    public int getItemCount() {
        return iList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.ingredient_name)TextView name;
        @BindView(R.id.ingredient_amount)TextView amount;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }


    }
}
