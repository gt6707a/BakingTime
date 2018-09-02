package com.android.gt6707a.bakingtime.viewDetails;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.gt6707a.bakingtime.R;
import com.android.gt6707a.bakingtime.entity.Step;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StepsAdapter extends RecyclerView.Adapter<StepsAdapter.ViewHolder> {

  private Context context;
  private List<Step> stepsList;

  public void setStepsList(List<Step> stepsList) {
    this.stepsList = stepsList;
    notifyDataSetChanged();
  }

  public StepsAdapter(Context context) {
    this.context = context;
  }

  @NonNull
  @Override
  public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    View view = LayoutInflater.from(context).inflate(R.layout.step_layout, parent, false);
    return new ViewHolder(view);
  }

  @Override
  public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
    Step step = stepsList.get(position);
    holder.shortDescriptionTextView.setText(step.getShortDescription());
  }

  @Override
  public int getItemCount() {
    return stepsList == null ? 0 : stepsList.size();
  }

  class ViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.short_description_text_view)
    TextView shortDescriptionTextView;

    public ViewHolder(View itemView) {
      super(itemView);
      ButterKnife.bind(this, itemView);
    }
  }
}
