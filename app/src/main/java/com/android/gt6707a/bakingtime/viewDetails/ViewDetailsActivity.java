package com.android.gt6707a.bakingtime.viewDetails;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.android.gt6707a.bakingtime.R;
import com.android.gt6707a.bakingtime.Utils;
import com.android.gt6707a.bakingtime.entity.Recipe;
import com.android.gt6707a.bakingtime.viewStepDetails.ViewStepDetailsActivity;

public class ViewDetailsActivity extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_view_details);

    if (getSupportActionBar() != null) {
      getSupportActionBar().setDisplayHomeAsUpEnabled(true);
      getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    ViewDetailsViewModel viewDetailsViewModel =
        ViewModelProviders.of(this).get(ViewDetailsViewModel.class);
    viewDetailsViewModel
        .getSelectedStepId()
        .observe(
            this,
            new Observer<Integer>() {
              @Override
              public void onChanged(@Nullable Integer stepId) {
                if (!Utils.hasTwoPanes(ViewDetailsActivity.this)) {
                  viewStepDetails(stepId);
                }
              }
            });
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    if (item.getItemId() == android.R.id.home) {
      finish();
    }
    return super.onOptionsItemSelected(item);
  }

  private void viewStepDetails(int stepId) {
    Recipe recipe = getIntent().getParcelableExtra(getString(R.string.key_to_recipe));

    Intent intent = new Intent(ViewDetailsActivity.this, ViewStepDetailsActivity.class);
    intent.putExtra(getString(R.string.key_to_recipe), recipe);
    intent.putExtra(getString(R.string.key_to_step_id), stepId);

    startActivity(intent);
  }
}
