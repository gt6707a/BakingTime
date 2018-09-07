package com.android.gt6707a.bakingtime.widget;

import android.content.Context;
import android.content.Intent;
import android.widget.AdapterView;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.android.gt6707a.bakingtime.R;
import com.android.gt6707a.bakingtime.WebService;
import com.android.gt6707a.bakingtime.entity.Ingredient;
import com.android.gt6707a.bakingtime.entity.Recipe;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import timber.log.Timber;

public class WidgetRemoteViewsService extends RemoteViewsService {
  @Override
  public RemoteViewsFactory onGetViewFactory(Intent intent) {
    return new WidgetRemoteViewsFactory(this.getApplicationContext(), intent);
  }
}

class WidgetRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {

  Context context;
  int recipeId = -1;
  List<Ingredient> ingredients;

  public WidgetRemoteViewsFactory(Context context, Intent intent) {
    this.context = context;
    recipeId = intent.getIntExtra("test", -1);
    Timber.d("Recipe Id is: " + recipeId);
  }

  @Override
  public void onCreate() {}

  @Override
  public void onDataSetChanged() {
    Retrofit retrofit =
        new Retrofit.Builder()
            .baseUrl("https://d17h27t6h515a5.cloudfront.net")
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    WebService webService = retrofit.create(WebService.class);
    try{
        Response<List<Recipe>> response = webService.getRecipeList().execute();
        for (Recipe recipe : response.body()) {
          if (recipe.getId() == recipeId) {
            ingredients = recipe.getIngredients();
            Timber.d(ingredients.size() + " ingredients loaded");
          }
        }
    } catch (IOException ex) {
        Timber.d(ex, "failed to load recipes");
    }
  }

  @Override
  public void onDestroy() {}

  @Override
  public int getCount() {
    return ingredients == null ? 0 : ingredients.size();
  }

  @Override
  public RemoteViews getViewAt(int position) {
    if (position == AdapterView.INVALID_POSITION || ingredients == null) {
      return null;
    }

    RemoteViews rv = new RemoteViews(context.getPackageName(), R.layout.ingredient_layout);
    rv.setTextViewText(
        R.id.quantity_textview, String.valueOf(ingredients.get(position).getQuantity()));

    return rv;
  }

  @Override
  public RemoteViews getLoadingView() {
    return null;
  }

  @Override
  public int getViewTypeCount() {
    return 1;
  }

  @Override
  public long getItemId(int position) {
    return position;
  }

  @Override
  public boolean hasStableIds() {
    return true;
  }
}
