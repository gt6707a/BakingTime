package com.android.gt6707a.bakingtime.viewDetails;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.gt6707a.bakingtime.R;
import com.android.gt6707a.bakingtime.entity.Recipe;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ViewDetailsFragment extends Fragment {

  private Recipe recipe;

  @BindView(R.id.recipe_name_textview)
  TextView recipeNameTextView;

  @BindView(R.id.ingredients_recycler_view)
  RecyclerView ingredientsRecyclerView;

  @BindView(R.id.steps_recycler_view)
  RecyclerView stepsRecyclerView;

  public ViewDetailsFragment() {
    // Required empty public constructor
  }

  @Override
  public View onCreateView(
      LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_view_details, container, false);
    ButterKnife.bind(this, view);

    Intent intent = getActivity().getIntent();
    recipe =
        intent.getParcelableExtra(getActivity().getResources().getString(R.string.key_to_recipe));

    recipeNameTextView.setText(recipe.getName());

    ingredientsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    IngredientsAdapter ingredientsAdapter = new IngredientsAdapter(getActivity());
    ingredientsAdapter.setIngredientsList(recipe.getIngredients());
    ingredientsRecyclerView.setAdapter(ingredientsAdapter);

    stepsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    StepsAdapter stepsAdapter = new StepsAdapter(getActivity());
    stepsAdapter.setStepsList(recipe.getSteps());
    stepsRecyclerView.setAdapter(stepsAdapter);

    return view;
  }
}
