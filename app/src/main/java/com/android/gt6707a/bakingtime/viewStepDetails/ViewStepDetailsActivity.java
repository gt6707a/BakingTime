package com.android.gt6707a.bakingtime.viewStepDetails;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.android.gt6707a.bakingtime.R;
import com.android.gt6707a.bakingtime.viewDetails.ViewDetailsViewModel;

public class ViewStepDetailsActivity extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_view_step_details);

    if (getSupportActionBar() != null) {
      getSupportActionBar().setDisplayHomeAsUpEnabled(true);
      getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    ViewDetailsViewModel viewDetailsViewModel =
        ViewModelProviders.of(this).get(ViewDetailsViewModel.class);
    viewDetailsViewModel.select(getIntent().getIntExtra(getString(R.string.key_to_step_id), 0));
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    if (item.getItemId() == android.R.id.home) {
      finish();
    }
    return super.onOptionsItemSelected(item);
  }
}
