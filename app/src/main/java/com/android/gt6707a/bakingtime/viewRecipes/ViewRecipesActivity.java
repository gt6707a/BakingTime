package com.android.gt6707a.bakingtime.viewRecipes;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.GridView;

import com.android.gt6707a.bakingtime.R;
import com.android.gt6707a.bakingtime.entity.Recipe;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ViewRecipesActivity extends AppCompatActivity {

    @BindView(R.id.recipes_grid_view)
    @Nullable
    GridView recipesGridView;

    RecipesGridAdapter recipesGridAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_recipes);

        ButterKnife.bind(this);

        recipesGridAdapter = new RecipesGridAdapter(this);
        recipesGridView.setAdapter(recipesGridAdapter);

        ViewRecipesViewModel viewModel = ViewModelProviders.of(this).get(ViewRecipesViewModel.class);
        viewModel.getRecipeList().observe(this, new Observer<List<Recipe>>() {
            @Override
            public void onChanged(@Nullable List<Recipe> recipes) {
                recipesGridAdapter.setRecipeList(recipes);
            }
        });
    }
}
