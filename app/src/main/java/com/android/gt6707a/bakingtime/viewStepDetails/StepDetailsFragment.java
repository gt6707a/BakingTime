package com.android.gt6707a.bakingtime.viewStepDetails;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;

import com.android.gt6707a.bakingtime.R;
import com.android.gt6707a.bakingtime.Utils;
import com.android.gt6707a.bakingtime.entity.Recipe;
import com.android.gt6707a.bakingtime.entity.Step;
import com.android.gt6707a.bakingtime.viewDetails.ViewDetailsViewModel;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Optional;

/** A simple {@link Fragment} subclass. */
public class StepDetailsFragment extends Fragment {

  private static final String TAG = StepDetailsFragment.class.getSimpleName();
  private static final String PLAYER_POSITION = "playerPosition";
  private static final String PLAYER_PLAY_WHEN_READY = "playWhenReady";
  private ViewDetailsViewModel viewDetailsViewModel;

  @BindView(R.id.playerView)
  SimpleExoPlayerView playerView;

  @BindView(R.id.description_text_view)
  TextView descriptionTextView;

  @BindView(R.id.last_step_button)
  @Nullable
  Button lastStepButton;

  @BindView(R.id.next_step_button)
  @Nullable
  Button nextStepButton;

  @BindView(R.id.no_video_text_view)
  TextView noVideoTextView;

  private SimpleExoPlayer exoPlayer;
  private MediaSessionCompat mediaSession;

  private Recipe recipe;
  private int stepId;

  private long playerPosition;
  private boolean playWhenReady = true;

  public StepDetailsFragment() {
    // Required empty public constructor
  }

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    recipe = getActivity().getIntent().getParcelableExtra(getString(R.string.key_to_recipe));
  }

  @Override
  public View onCreateView(
      LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    // Inflate the layout for this fragment
    View view = inflater.inflate(R.layout.fragment_step_details, container, false);
    ButterKnife.bind(this, view);

    initializeMediaSession();

    playerPosition = savedInstanceState.getLong(PLAYER_POSITION);
    playWhenReady = savedInstanceState.getBoolean(PLAYER_PLAY_WHEN_READY);

    viewDetailsViewModel = ViewModelProviders.of(getActivity()).get(ViewDetailsViewModel.class);
    viewDetailsViewModel
        .getSelectedStepId()
        .observe(
            this,
            new Observer<Integer>() {
              @Override
              public void onChanged(@Nullable Integer stepId) {
                StepDetailsFragment.this.stepId = stepId;

                setNavigateButtonVisibilities();

                // Ideally should match on stepId
                Step step = recipe.getSteps().get(stepId);
                descriptionTextView.setText(step.getDescription());

                if (step.getVideoURL() == null || step.getVideoURL().isEmpty()) {
                  noVideoTextView.setVisibility(View.VISIBLE);
                  playerView.setVisibility(View.GONE);
                } else {
                  initializePlayer(Uri.parse(step.getVideoURL()));
                  noVideoTextView.setVisibility(View.GONE);
                  playerView.setVisibility(View.VISIBLE);
                }
              }
            });

    return view;
  }

  private void setNavigateButtonVisibilities() {
    if (!Utils.hasTwoPanes(getContext())) {
      lastStepButton.setVisibility(View.VISIBLE);
      nextStepButton.setVisibility(View.VISIBLE);

      if (stepId == 0) {
        lastStepButton.setVisibility(View.INVISIBLE);
      }
      if (stepId == recipe.getSteps().size() - 1) {
        nextStepButton.setVisibility(View.INVISIBLE);
      }
    }
  }

  @Override
  public void onActivityCreated(@Nullable Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);
    ViewGroup.LayoutParams params = playerView.getLayoutParams();
    params.height = ((View) playerView.getParent()).getHeight();
    playerView.setLayoutParams(params);

    if (Utils.isSinglePaneLandscape(getContext())) {
      ((ScrollView) playerView.getParent().getParent())
          .post(
              new Runnable() {
                @Override
                public void run() {
                  ViewGroup.LayoutParams params = playerView.getLayoutParams();
                  params.height = ((View) playerView.getParent()).getHeight();
                  playerView.setLayoutParams(params);
                  noVideoTextView.setLayoutParams(params);
                }
              });
    }
  }

  @Override
  public void onSaveInstanceState(@NonNull Bundle outState) {
    super.onSaveInstanceState(outState);
    if (exoPlayer != null) {
      outState.putLong(PLAYER_POSITION, exoPlayer.getCurrentPosition());
      outState.putBoolean(PLAYER_PLAY_WHEN_READY, exoPlayer.getPlayWhenReady());
    }
  }

  @Optional
  @OnClick(R.id.last_step_button)
  public void toLast() {
    if (exoPlayer != null) {
      exoPlayer.stop();
    }
    viewDetailsViewModel.select(--stepId);
  }

  @Optional
  @OnClick(R.id.next_step_button)
  public void toNext() {
    if (exoPlayer != null) {
      exoPlayer.stop();
    }
    viewDetailsViewModel.select(++stepId);
  }

  /**
   * Initializes the Media Session to be enabled with media buttons, transport controls, callbacks
   * and media controller.
   */
  private void initializeMediaSession() {

    // Create a MediaSessionCompat.
    mediaSession = new MediaSessionCompat(getActivity(), TAG);

    // Enable callbacks from MediaButtons and TransportControls.
    mediaSession.setFlags(
        MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS
            | MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS);

    // Do not let MediaButtons restart the player when the app is not visible.
    mediaSession.setMediaButtonReceiver(null);

    // Set an initial PlaybackState with ACTION_PLAY, so media buttons can start the player.
    PlaybackStateCompat.Builder stateBuilder =
        new PlaybackStateCompat.Builder()
            .setActions(
                PlaybackStateCompat.ACTION_PLAY
                    | PlaybackStateCompat.ACTION_PAUSE
                    | PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS
                    | PlaybackStateCompat.ACTION_PLAY_PAUSE);

    mediaSession.setPlaybackState(stateBuilder.build());

    // MySessionCallback has methods that handle callbacks from a media controller.
    // mediaSession.setCallback(new MySessionCallback());

    // Start the Media Session since the activity is active.
    mediaSession.setActive(true);
  }

  /**
   * Initialize ExoPlayer.
   *
   * @param mediaUri The URI of the sample to play.
   */
  private void initializePlayer(Uri mediaUri) {
    if (exoPlayer == null) {
      // Create an instance of the ExoPlayer.
      TrackSelector trackSelector = new DefaultTrackSelector();
      LoadControl loadControl = new DefaultLoadControl();
      exoPlayer = ExoPlayerFactory.newSimpleInstance(getActivity(), trackSelector, loadControl);
      playerView.setPlayer(exoPlayer);
    }
    // Prepare the MediaSource.
    String userAgent = Util.getUserAgent(getActivity(), "BakingTime");

    MediaSource mediaSource =
        new ExtractorMediaSource(
            mediaUri,
            new DefaultDataSourceFactory(getActivity(), userAgent),
            new DefaultExtractorsFactory(),
            null,
            null);
    exoPlayer.prepare(mediaSource);
    exoPlayer.seekTo(playerPosition);
    exoPlayer.setPlayWhenReady(playWhenReady);
  }

  @Override
  public void onDestroy() {
    super.onDestroy();
    releasePlayer();
    mediaSession.setActive(false);
  }

  /** Release ExoPlayer. */
  private void releasePlayer() {
    if (exoPlayer != null) {
      exoPlayer.stop();
      exoPlayer.release();
      exoPlayer = null;
    }
  }
}
