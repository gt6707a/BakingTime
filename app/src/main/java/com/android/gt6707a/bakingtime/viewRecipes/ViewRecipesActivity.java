package com.android.gt6707a.bakingtime.viewRecipes;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.test.espresso.IdlingResource;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;

import com.android.gt6707a.bakingtime.R;
import com.android.gt6707a.bakingtime.SimpleIdlingResource;
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

    getIdlingResource();

    ViewRecipesViewModel viewModel = ViewModelProviders.of(this).get(ViewRecipesViewModel.class);
    viewModel
        .getRecipeList(mIdlingResource)
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
    return (int) (dpWidth / 240);
  }

  @Override
  public void onItemClickListener(Recipe recipe) {
    Intent intent = new Intent(ViewRecipesActivity.this, ViewDetailsActivity.class);
    intent.putExtra(getString(R.string.key_to_recipe), recipe);
    startActivity(intent);
  }

  @Nullable private SimpleIdlingResource mIdlingResource;

  @VisibleForTesting
  @NonNull
  public IdlingResource getIdlingResource() {
    if (mIdlingResource == null) {
      mIdlingResource = new SimpleIdlingResource();
    }
    return mIdlingResource;
  }
}
