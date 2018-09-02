package com.android.gt6707a.bakingtime.viewRecipes;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;

import com.android.gt6707a.bakingtime.R;
import com.android.gt6707a.bakingtime.entity.Recipe;
import com.android.gt6707a.bakingtime.viewDetails.ViewDetailsActivity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ViewRecipesActivity extends AppCompatActivity
    implements RecipeListAdapter.ItemClickListener {

  @BindView(R.id.recipes_recycler_view)
  @Nullable
  RecyclerView recipesRecyclerView;

  RecipeListAdapter recipeListAdapter;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_view_recipes);

    ButterKnife.bind(this);

    recipesRecyclerView.setLayoutManager(new GridLayoutManager(this, calculateNumberOfColumns()));
    recipeListAdapter = new RecipeListAdapter(this, this);
    recipesRecyclerView.setAdapter(recipeListAdapter);

    ViewRecipesViewModel viewModel = ViewModelProviders.of(this).get(ViewRecipesViewModel.class);
    viewModel
        .getRecipeList()
        .observe(
            this,
            new Observer<List<Recipe>>() {
              @Override
              public void onChanged(@Nullable List<Recipe> recipes) {
                recipeListAdapter.setRecipeList(recipes);
              }
            });
  }

  private int calculateNumberOfColumns() {
    DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
    float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
    int numberOfColumns = (int) (dpWidth / 240);
    return numberOfColumns;
  }

  @Override
  public void onItemClickListener(Recipe recipe) {
    Intent intent = new Intent(ViewRecipesActivity.this, ViewDetailsActivity.class);
    intent.putExtra(getString(R.string.key_to_recipe), recipe);
    startActivity(intent);
  }
}
