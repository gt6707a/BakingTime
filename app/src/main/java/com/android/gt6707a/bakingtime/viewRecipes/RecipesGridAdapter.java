package com.android.gt6707a.bakingtime.viewRecipes;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.gt6707a.bakingtime.R;
import com.android.gt6707a.bakingtime.entity.Recipe;

import java.util.List;

public class RecipesGridAdapter extends BaseAdapter {

    private Context context;
    private List<Recipe> recipeList;
    public void setRecipeList(List<Recipe> recipeList) {
        this.recipeList = recipeList;
        notifyDataSetChanged();
    }

    public RecipesGridAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return recipeList == null ? 0 : recipeList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View grid;

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            grid = inflater.inflate(R.layout.recipe_list_item_layout, null);
            TextView textView = grid.findViewById(R.id.name_textview);
            textView.setText(recipeList.get(position).getName());
        } else {
            grid = convertView;
        }

        return grid;
    }
}
