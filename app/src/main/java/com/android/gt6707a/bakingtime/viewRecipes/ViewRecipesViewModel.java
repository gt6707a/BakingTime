package com.android.gt6707a.bakingtime.viewRecipes;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import com.android.gt6707a.bakingtime.WebService;
import com.android.gt6707a.bakingtime.entity.Recipe;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import timber.log.Timber;

public class ViewRecipesViewModel extends AndroidViewModel {
  private final WebService webService;

  public ViewRecipesViewModel(@NonNull Application application) {
    super(application);

    Retrofit retrofit =
        new Retrofit.Builder()
            .baseUrl("https://d17h27t6h515a5.cloudfront.net")
            .addConverterFactory(GsonConverterFactory.create())
            .build();
    webService = retrofit.create(WebService.class);
  }

  public LiveData<List<Recipe>> getRecipeList() {
    final MutableLiveData<List<Recipe>> data = new MutableLiveData<>();
    webService
        .getRecipeList()
        .enqueue(
            new Callback<List<Recipe>>() {
              @Override
              public void onResponse(Call<List<Recipe>> call, Response<List<Recipe>> response) {
                data.setValue(response.body());
              }

              @Override
              public void onFailure(Call<List<Recipe>> call, Throwable t) {
                Timber.d(t, "Failed to get recipes.");
              }
            });
    return data;
  }
}
