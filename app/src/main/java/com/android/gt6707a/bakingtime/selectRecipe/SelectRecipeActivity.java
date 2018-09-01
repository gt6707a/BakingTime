package com.android.gt6707a.bakingtime.selectRecipe;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.android.gt6707a.bakingtime.R;
import com.android.gt6707a.bakingtime.entity.Recipe;
import com.android.gt6707a.bakingtime.selectRecipe.SelectRecipeViewModel;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SelectRecipeActivity extends AppCompatActivity {

    @BindView(R.id.recipes_recycler_view)
    RecyclerView recipesRecyclerView;

    RecipeListAdapter recipeListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_recipe);

        ButterKnife.bind(this);

        recipesRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        recipeListAdapter = new RecipeListAdapter(this);
        recipesRecyclerView.setAdapter(recipeListAdapter);

        SelectRecipeViewModel viewModel = ViewModelProviders.of(this).get(SelectRecipeViewModel.class);
        viewModel.getRecipeList().observe(this, new Observer<List<Recipe>>() {
            @Override
            public void onChanged(@Nullable List<Recipe> recipes) {
                recipeListAdapter.setRecipeList(recipes);
            }
        });
    }
}
