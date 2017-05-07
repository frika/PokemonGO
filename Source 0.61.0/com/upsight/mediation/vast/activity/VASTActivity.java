package com.upsight.mediation.vast.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Rect;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.media.MediaPlayer.OnSeekCompleteListener;
import android.media.MediaPlayer.OnVideoSizeChangedListener;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewCompat;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
import com.google.android.gms.location.places.Place;
import com.mopub.volley.DefaultRetryPolicy;
import com.upsight.mediation.log.FuseLog;
import com.upsight.mediation.vast.Postroll.MRaidPostroll;
import com.upsight.mediation.vast.Postroll.Postroll;
import com.upsight.mediation.vast.Postroll.Postroll.Listener;
import com.upsight.mediation.vast.VASTPlayer;
import com.upsight.mediation.vast.model.TRACKING_EVENTS_TYPE;
import com.upsight.mediation.vast.model.VASTModel;
import com.upsight.mediation.vast.model.VASTTracking;
import com.upsight.mediation.vast.util.Assets;
import com.upsight.mediation.vast.util.HttpTools;
import com.voxelbusters.nativeplugins.defines.Keys.GameServices;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import spacemadness.com.lunarconsole.BuildConfig;
import spacemadness.com.lunarconsole.C1628R;

public class VASTActivity extends Activity implements OnCompletionListener, OnErrorListener, OnPreparedListener, OnSeekCompleteListener, OnVideoSizeChangedListener, Callback, Listener {
    private static final long QUARTILE_TIMER_INTERVAL = 250;
    private static String TAG = null;
    private static final long TOOLBAR_HIDE_DELAY = 3000;
    private static final long VIDEO_PROGRESS_TIMER_INTERVAL = 33;
    public static DisplayMetrics displayMetrics;
    private String mActionText;
    private int mCurrentOrientation;
    private int mCurrentVideoPosition;
    private String mEndCardHtml;
    private Handler mHandler;
    private boolean mIsCompleted;
    private boolean mIsPlayBackError;
    private boolean mIsProcessedImpressions;
    private boolean mIsRewarded;
    private boolean mIsVideoPaused;
    private final int mMaxProgressTrackingPoints;
    private MediaPlayer mMediaPlayer;
    private RelativeLayout mOverlay;
    private PlayerControls mPlayerControls;
    private Postroll mPostroll;
    private boolean mPostrollFlag;
    private ProgressBar mProgressBar;
    private int mQuartile;
    private RelativeLayout mRootLayout;
    private int mScreenHeight;
    private int mScreenWidth;
    private long mSkipOffset;
    private boolean mSkipOffsetRelative;
    private long mSkipOffsetServer;
    private Timer mStartVideoProgressTimer;
    private SurfaceHolder mSurfaceHolder;
    private SurfaceView mSurfaceView;
    private Timer mToolBarTimer;
    private HashMap<TRACKING_EVENTS_TYPE, List<VASTTracking>> mTrackingEventMap;
    private Timer mTrackingEventTimer;
    public VASTModel mVastModel;
    private String mVersion;
    private int mVideoDuration;
    private int mVideoHeight;
    private LinkedList<Integer> mVideoProgressTracker;
    private int mVideoWidth;
    private Rect rekt;
    private boolean shouldPlayOnResume;
    private boolean showingPostroll;

    /* renamed from: com.upsight.mediation.vast.activity.VASTActivity.1 */
    class C12381 implements OnTouchListener {
        C12381() {
        }

        public boolean onTouch(View v, MotionEvent event) {
            VASTActivity.this.overlayClicked();
            return false;
        }
    }

    /* renamed from: com.upsight.mediation.vast.activity.VASTActivity.2 */
    class C12392 implements OnTouchListener {
        C12392() {
        }

        public boolean onTouch(View v, MotionEvent event) {
            switch (event.getAction()) {
                case C1628R.styleable.RecyclerView_android_orientation /*0*/:
                    VASTActivity.this.rekt = new Rect(v.getLeft(), v.getTop(), v.getRight(), v.getBottom());
                    Assets.setAlpha(v, PlayerControls.DOWN_STATE);
                    break;
                case C1628R.styleable.RecyclerView_layoutManager /*1*/:
                    if (VASTActivity.this.rekt.contains(v.getLeft() + ((int) event.getX()), v.getTop() + ((int) event.getY()))) {
                        Assets.setAlpha(v, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
                        VASTActivity.this.infoClicked(true);
                        break;
                    }
                    break;
            }
            return true;
        }
    }

    /* renamed from: com.upsight.mediation.vast.activity.VASTActivity.3 */
    class C12403 implements OnTouchListener {
        C12403() {
        }

        public boolean onTouch(View v, MotionEvent event) {
            switch (event.getAction()) {
                case C1628R.styleable.RecyclerView_android_orientation /*0*/:
                    VASTActivity.this.rekt = new Rect(v.getLeft(), v.getTop(), v.getRight(), v.getBottom());
                    Assets.setAlpha(v, PlayerControls.DOWN_STATE);
                    break;
                case C1628R.styleable.RecyclerView_layoutManager /*1*/:
                    if (VASTActivity.this.rekt.contains(v.getLeft() + ((int) event.getX()), v.getTop() + ((int) event.getY()))) {
                        Assets.setAlpha(v, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
                        VASTActivity.this.skipClicked();
                        break;
                    }
                    break;
            }
            return true;
        }
    }

    /* renamed from: com.upsight.mediation.vast.activity.VASTActivity.4 */
    class C12424 extends TimerTask {

        /* renamed from: com.upsight.mediation.vast.activity.VASTActivity.4.1 */
        class C12411 implements Runnable {
            C12411() {
            }

            public void run() {
                VASTActivity.this.mPlayerControls.setVisibility(8);
            }
        }

        C12424() {
        }

        public void run() {
            VASTActivity.this.mHandler.post(new C12411());
        }
    }

    /* renamed from: com.upsight.mediation.vast.activity.VASTActivity.5 */
    class C12435 extends TimerTask {
        final /* synthetic */ List val$events;
        final /* synthetic */ int val$videoDuration;

        C12435(int i, List list) {
            this.val$videoDuration = i;
            this.val$events = list;
        }

        public void run() {
            try {
                int curPos = VASTActivity.this.mMediaPlayer.getCurrentPosition();
                if (curPos != 0) {
                    int percentage = (curPos * 100) / this.val$videoDuration;
                    if (percentage >= VASTActivity.this.mQuartile * 25) {
                        if (VASTActivity.this.mQuartile == 0) {
                            VASTActivity.this.processEvent(TRACKING_EVENTS_TYPE.start);
                            VASTPlayer.currentPlayer.listener.vastProgress(0);
                        } else if (VASTActivity.this.mQuartile == 1) {
                            VASTActivity.this.processEvent(TRACKING_EVENTS_TYPE.firstQuartile);
                            VASTPlayer.currentPlayer.listener.vastProgress(25);
                        } else if (VASTActivity.this.mQuartile == 2) {
                            VASTActivity.this.processEvent(TRACKING_EVENTS_TYPE.midpoint);
                            VASTPlayer.currentPlayer.listener.vastProgress(50);
                        } else if (VASTActivity.this.mQuartile == 3) {
                            VASTActivity.this.processEvent(TRACKING_EVENTS_TYPE.thirdQuartile);
                            VASTPlayer.currentPlayer.listener.vastProgress(75);
                            VASTActivity.this.stopTrackingEventTimer();
                        }
                        VASTActivity.this.mQuartile = VASTActivity.this.mQuartile + 1;
                    }
                    if (VASTActivity.this.mVersion.equals("3.0") && this.val$events != null) {
                        for (VASTTracking v : this.val$events) {
                            if (v.isOffsetRelative()) {
                                if (((long) percentage) >= v.getParsedOffset() && !v.isConsumed()) {
                                    VASTActivity.this.processProgressEvent(v);
                                    v.setConsumed(true);
                                    VASTPlayer.currentPlayer.listener.vastProgress(percentage);
                                }
                            } else if (((long) curPos) >= v.getParsedOffset() && !v.isConsumed()) {
                                VASTActivity.this.processProgressEvent(v);
                                v.setConsumed(true);
                                VASTPlayer.currentPlayer.listener.vastProgress(percentage);
                            }
                        }
                    }
                }
            } catch (Exception e) {
                FuseLog.m609w(VASTActivity.TAG, "mediaPlayer.getCurrentPosition exception: " + e.getMessage());
                cancel();
            }
        }
    }

    /* renamed from: com.upsight.mediation.vast.activity.VASTActivity.6 */
    class C12456 extends TimerTask {
        int maxAmountInList;

        /* renamed from: com.upsight.mediation.vast.activity.VASTActivity.6.1 */
        class C12441 implements Runnable {
            C12441() {
            }

            public void run() {
                if (VASTActivity.this.mPlayerControls != null) {
                    VASTActivity.this.mPlayerControls.update(VASTActivity.VIDEO_PROGRESS_TIMER_INTERVAL);
                }
            }
        }

        C12456() {
            this.maxAmountInList = 19;
        }

        public void run() {
            if (VASTActivity.this.mMediaPlayer != null) {
                if (VASTActivity.this.mVideoProgressTracker.size() == this.maxAmountInList && ((Integer) VASTActivity.this.mVideoProgressTracker.getLast()).intValue() > ((Integer) VASTActivity.this.mVideoProgressTracker.getFirst()).intValue()) {
                    VASTActivity.this.mVideoProgressTracker.removeFirst();
                }
                try {
                    int curPos = VASTActivity.this.mMediaPlayer.getCurrentPosition();
                    VASTActivity.this.runOnUiThread(new C12441());
                    VASTActivity.this.mVideoProgressTracker.addLast(Integer.valueOf(curPos));
                } catch (Exception e) {
                }
            }
        }
    }

    public VASTActivity() {
        this.mVideoProgressTracker = null;
        this.mMaxProgressTrackingPoints = 20;
        this.mVastModel = null;
        this.mEndCardHtml = BuildConfig.FLAVOR;
        this.mIsVideoPaused = false;
        this.mIsPlayBackError = false;
        this.mIsProcessedImpressions = false;
        this.mIsCompleted = false;
        this.mQuartile = 0;
        this.shouldPlayOnResume = false;
    }

    static {
        TAG = "VASTActivity";
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent i = getIntent();
        this.mPostrollFlag = i.getBooleanExtra("postroll", false);
        this.mEndCardHtml = i.getStringExtra("endCardHtml");
        this.mSkipOffsetServer = i.getLongExtra("skipOffset", 0);
        this.mIsRewarded = i.getBooleanExtra("rewarded", true);
        this.mActionText = i.getStringExtra("actionText");
        this.mVastModel = (VASTModel) i.getSerializableExtra("com.nexage.android.vast.player.vastModel");
        if (this.mVastModel == null) {
            FuseLog.m604d(TAG, "vastModel is null. Stopping activity.");
            finishVAST();
        } else if (VASTPlayer.currentPlayer == null) {
            FuseLog.m604d(TAG, "currentPlayer is null. Stopping activity.");
            finishVAST();
        } else {
            hideTitleStatusBars();
            this.mHandler = new Handler();
            displayMetrics = getResources().getDisplayMetrics();
            this.mScreenWidth = displayMetrics.widthPixels;
            this.mScreenHeight = displayMetrics.heightPixels;
            this.mVersion = this.mVastModel.getVastVersion();
            this.mTrackingEventMap = this.mVastModel.getTrackingEvents();
            createUIComponents();
        }
    }

    public void setRequestedOrientation(int requestedOrientation) {
        super.setRequestedOrientation(requestedOrientation);
    }

    protected void onStart() {
        super.onStart();
    }

    protected void onResume() {
        super.onResume();
        if (this.shouldPlayOnResume && this.mMediaPlayer != null) {
            surfaceCreated(this.mSurfaceHolder);
        }
    }

    protected void onStop() {
        super.onStop();
    }

    protected void onRestart() {
        super.onRestart();
    }

    protected void onPause() {
        super.onPause();
        if (this.mMediaPlayer != null) {
            processPauseSteps();
            this.mCurrentVideoPosition = this.mMediaPlayer.getCurrentPosition();
        }
    }

    protected void onDestroy() {
        super.onDestroy();
        cleanActivityUp();
    }

    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
    }

    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }

    private void hideTitleStatusBars() {
        requestWindowFeature(1);
        getWindow().setFlags(Place.TYPE_SUBLOCALITY_LEVEL_2, Place.TYPE_SUBLOCALITY_LEVEL_2);
    }

    private void createUIComponents() {
        LayoutParams params = new LayoutParams(-1, -1);
        createRootLayout(params);
        createSurface(params);
        createMediaPlayer();
        createOverlay(params);
        createPlayerControls();
        if (this.mPostrollFlag) {
            createPostroll();
        }
        setContentView(this.mRootLayout);
        createProgressBar();
    }

    private void createProgressBar() {
        LayoutParams params = new LayoutParams(-1, -2);
        params.addRule(13);
        this.mProgressBar = new ProgressBar(this);
        this.mProgressBar.setLayoutParams(params);
        this.mRootLayout.addView(this.mProgressBar);
        this.mProgressBar.setVisibility(8);
    }

    private void showProgressBar() {
        this.mProgressBar.setVisibility(0);
    }

    private void hideProgressBar() {
        this.mProgressBar.setVisibility(8);
    }

    private void createRootLayout(LayoutParams params) {
        this.mRootLayout = new RelativeLayout(this);
        this.mRootLayout.setLayoutParams(params);
        this.mRootLayout.setPadding(0, 0, 0, 0);
        this.mRootLayout.setBackgroundColor(ViewCompat.MEASURED_STATE_MASK);
    }

    private void createSurface(LayoutParams params) {
        this.mSurfaceView = new SurfaceView(this);
        this.mSurfaceView.setLayoutParams(params);
        this.mSurfaceHolder = this.mSurfaceView.getHolder();
        this.mSurfaceHolder.addCallback(this);
        this.mSurfaceHolder.setType(3);
        this.mRootLayout.addView(this.mSurfaceView);
    }

    private void createMediaPlayer() {
        this.mMediaPlayer = new MediaPlayer();
        this.mMediaPlayer.setOnCompletionListener(this);
        this.mMediaPlayer.setOnErrorListener(this);
        this.mMediaPlayer.setOnPreparedListener(this);
        this.mMediaPlayer.setOnVideoSizeChangedListener(this);
        this.mMediaPlayer.setOnSeekCompleteListener(this);
        this.mMediaPlayer.setAudioStreamType(3);
    }

    private void createOverlay(LayoutParams params) {
        this.mOverlay = new RelativeLayout(this);
        this.mOverlay.setLayoutParams(params);
        this.mOverlay.setPadding(0, 0, 0, 0);
        this.mOverlay.setBackgroundColor(0);
        this.mOverlay.setOnTouchListener(new C12381());
        this.mRootLayout.addView(this.mOverlay);
    }

    private void createPlayerControls() {
        LayoutParams params = new LayoutParams(-1, -2);
        params.addRule(10);
        resolveSkipOffset();
        this.mPlayerControls = new PlayerControls(this);
        this.mPlayerControls.setVastModel(this.mVastModel);
        this.mPlayerControls.init(this.mSkipOffset != -1, this.mPostrollFlag);
        this.mPlayerControls.setLayoutParams(params);
        this.mPlayerControls.setVisibility(8);
        TextView button = this.mPlayerControls.getLearnText();
        if (button != null) {
            button.setText(this.mActionText);
            button.setTextSize((float) (20 - (this.mActionText.length() / 2)));
            button.setOnTouchListener(new C12392());
        }
        this.mPlayerControls.setSkipButtonListener(new C12403());
        this.mOverlay.addView(this.mPlayerControls);
    }

    private void resolveSkipOffset() {
        if (this.mVersion.equals("3.0")) {
            String skipOffsetValue = this.mVastModel.getSkipOffset();
            if (skipOffsetValue == null || skipOffsetValue.length() == 0) {
                this.mSkipOffset = this.mSkipOffsetServer;
            } else if (skipOffsetValue.endsWith("%")) {
                this.mSkipOffsetRelative = true;
                this.mSkipOffset = Long.parseLong(skipOffsetValue.substring(0, skipOffsetValue.indexOf("%")));
            } else {
                this.mSkipOffset = Assets.parseOffset(skipOffsetValue);
            }
        } else {
            this.mSkipOffset = this.mSkipOffsetServer;
        }
        FuseLog.m608v(TAG, "skipOffset:  " + this.mSkipOffset);
    }

    private void createPostroll() {
        if (this.mEndCardHtml == null || this.mEndCardHtml.length() <= 0) {
            String clickThroughUrl = this.mVastModel.getVideoClicks().getClickThrough();
            boolean shouldShowActionButton = clickThroughUrl != null && clickThroughUrl.length() > 0;
            this.mPostroll = new DefaultPostroll(this, this, shouldShowActionButton, this.mActionText);
        } else {
            this.mPostroll = new MRaidPostroll(this, this.mEndCardHtml, this);
        }
        this.mPostroll.init();
    }

    public void infoClicked(boolean shouldHandleClickThrough) {
        if (VASTPlayer.currentPlayer.listener != null) {
            VASTPlayer.currentPlayer.listener.vastClick();
        }
        activateButtons(false);
        if (shouldHandleClickThrough) {
            processClickThroughEvent();
        }
        closeClicked();
    }

    private void activateButtons(boolean active) {
        if (this.mPlayerControls != null) {
            if (active) {
                this.mPlayerControls.setVisibility(0);
            } else {
                this.mPlayerControls.setVisibility(8);
            }
        }
    }

    private void processClickThroughEvent() {
        String clickThroughUrl = this.mVastModel.getVideoClicks().getClickThrough();
        FuseLog.m608v(TAG, "clickThrough url: " + clickThroughUrl);
        fireUrls("click", this.mVastModel.getVideoClicks().getClickTracking());
        try {
            Intent intent = new Intent("android.intent.action.VIEW", Uri.parse(clickThroughUrl));
            if (getPackageManager().resolveActivity(intent, 32) == null) {
                FuseLog.m604d(TAG, "Clickthrough error occured, uri unresolvable");
                if (((double) this.mCurrentVideoPosition) >= ((double) this.mMediaPlayer.getCurrentPosition()) * 0.99d) {
                    this.mMediaPlayer.start();
                }
                activateButtons(true);
                return;
            }
            startActivity(intent);
        } catch (NullPointerException e) {
        }
    }

    public void closeClicked() {
        cleanActivityUp();
        if (!this.mIsPlayBackError) {
            processEvent(TRACKING_EVENTS_TYPE.close);
        }
        finishVAST();
    }

    public void replayedClicked() {
        createPostroll();
        resetPlayerToBeginning();
        VASTPlayer.currentPlayer.listener.vastReplay();
    }

    public void onOpenMRaidUrl(String url) {
        FuseLog.m607i(TAG, "Opening MRAID Postroll click through link: " + url);
        Intent intent = new Intent("android.intent.action.VIEW", Uri.parse(url));
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    private void resetPlayerToBeginning() {
        this.showingPostroll = false;
        this.mIsCompleted = false;
        this.mQuartile = 0;
        createPlayerControls();
        surfaceCreated(this.mSurfaceHolder);
    }

    private void skipClicked() {
        if (this.mPostrollFlag) {
            cleanActivityUp();
            showPostroll();
        } else {
            finishVAST();
        }
        if (this.mVersion.equals("3.0")) {
            processEvent(TRACKING_EVENTS_TYPE.skip);
        }
        VASTPlayer.currentPlayer.listener.vastSkip();
    }

    private void processPauseSteps() {
        this.mIsVideoPaused = true;
        this.mMediaPlayer.pause();
        this.shouldPlayOnResume = true;
        stopVideoProgressTimer();
        stopToolBarTimer();
    }

    private void processPlaySteps() {
        this.mIsVideoPaused = false;
        this.mMediaPlayer.start();
        startToolBarTimer();
        startVideoProgressTimer();
    }

    public void onBackPressed() {
        if (this.mSkipOffset > 0) {
            if (!this.mIsCompleted) {
                VASTPlayer.currentPlayer.listener.vastSkip();
            }
            closeClicked();
            super.onBackPressed();
        }
    }

    public void surfaceCreated(SurfaceHolder holder) {
        if (!this.showingPostroll) {
            try {
                if (this.mMediaPlayer == null) {
                    createMediaPlayer();
                }
                showProgressBar();
                this.mMediaPlayer.setDisplay(holder);
                if (this.mMediaPlayer == null || !this.mIsVideoPaused) {
                    this.mMediaPlayer.setDataSource(this.mVastModel.getPickedMediaFileLocation());
                    if (this.mVastModel.getPickedMediaFileDeliveryType().equals("streaming")) {
                        this.mMediaPlayer.prepareAsync();
                        return;
                    } else {
                        this.mMediaPlayer.prepare();
                        return;
                    }
                }
                processPlaySteps();
                hideProgressBar();
            } catch (Exception e) {
                FuseLog.m610w(TAG, e.getMessage(), e);
                VASTPlayer.currentPlayer.listener.vastError(VASTPlayer.ERROR_VIDEO_PLAYBACK);
                finishVAST();
            }
        }
    }

    public void surfaceChanged(SurfaceHolder surfaceHolder, int arg1, int arg2, int arg3) {
    }

    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        this.shouldPlayOnResume = false;
    }

    public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {
        this.mVideoWidth = width;
        this.mVideoHeight = height;
        calculateAspectRatio();
        FuseLog.m608v(TAG, "video size: " + this.mVideoWidth + "x" + this.mVideoHeight);
    }

    public void onPrepared(MediaPlayer mp) {
        this.mVideoDuration = this.mMediaPlayer.getDuration();
        this.mVideoWidth = this.mMediaPlayer.getVideoWidth();
        this.mVideoHeight = this.mMediaPlayer.getVideoHeight();
        if (this.mSkipOffsetRelative) {
            this.mSkipOffset = (long) ((((float) this.mVideoDuration) / 100.0f) * ((float) this.mSkipOffset));
        }
        calculateAspectRatio();
        hideProgressBar();
        if (this.mIsVideoPaused) {
            this.mMediaPlayer.pause();
        } else {
            startVideoProgressTimer();
        }
        if (this.mCurrentVideoPosition > 0) {
            this.mMediaPlayer.seekTo(this.mCurrentVideoPosition);
        }
        if (!(this.mMediaPlayer.isPlaying() || this.mIsVideoPaused)) {
            this.mMediaPlayer.start();
            VASTPlayer.currentPlayer.listener.vastDisplay();
            startTrackingEventTimer();
            startToolBarTimer();
        }
        if (!this.mIsProcessedImpressions && this.mMediaPlayer.isPlaying()) {
            processImpressions();
        }
    }

    private void calculateAspectRatio() {
        if (this.mVideoWidth == 0 || this.mVideoHeight == 0) {
            FuseLog.m604d(TAG, "mVideoWidth or mVideoHeight is 0, skipping calculateAspectRatio");
            return;
        }
        double scale = Math.min((1.0d * ((double) this.mScreenWidth)) / ((double) this.mVideoWidth), (1.0d * ((double) this.mScreenHeight)) / ((double) this.mVideoHeight));
        int surfaceWidth = (int) (((double) this.mVideoWidth) * scale);
        int surfaceHeight = (int) (((double) this.mVideoHeight) * scale);
        LayoutParams params = new LayoutParams(surfaceWidth, surfaceHeight);
        params.addRule(13);
        this.mSurfaceView.setLayoutParams(params);
        this.mSurfaceHolder.setFixedSize(surfaceWidth, surfaceHeight);
        FuseLog.m608v(TAG, " screen size: " + this.mScreenWidth + "x" + this.mScreenHeight);
        FuseLog.m608v(TAG, " video size:  " + this.mVideoWidth + "x" + this.mVideoHeight);
        FuseLog.m608v(TAG, "surface size: " + surfaceWidth + "x" + surfaceHeight);
    }

    private void cleanActivityUp() {
        cleanUpMediaPlayer();
        stopTrackingEventTimer();
        stopVideoProgressTimer();
        stopToolBarTimer();
        if (this.mPlayerControls != null) {
            this.mPlayerControls.setVisibility(8);
            this.mOverlay.removeView(this.mPlayerControls);
            this.mPlayerControls = null;
        }
    }

    private void cleanUpMediaPlayer() {
        if (this.mMediaPlayer != null) {
            if (this.mMediaPlayer.isPlaying()) {
                this.mMediaPlayer.stop();
            }
            this.mMediaPlayer.setOnCompletionListener(null);
            this.mMediaPlayer.setOnErrorListener(null);
            this.mMediaPlayer.setOnPreparedListener(null);
            this.mMediaPlayer.setOnVideoSizeChangedListener(null);
            this.mMediaPlayer.release();
            this.mMediaPlayer = null;
        }
    }

    public boolean onError(MediaPlayer mp, int what, int extra) {
        this.mIsPlayBackError = true;
        FuseLog.m609w(TAG, "Shutting down Activity due to Media Player errors: WHAT:" + what + ": EXTRA:" + extra + UpsightEndpoint.SIGNED_MESSAGE_SEPARATOR);
        VASTPlayer.currentPlayer.listener.vastError(VASTPlayer.ERROR_VIDEO_PLAYBACK);
        processErrorEvent();
        closeClicked();
        return true;
    }

    private void processErrorEvent() {
        fireUrls(GameServices.ERROR, this.mVastModel.getErrorUrl());
    }

    public void onSeekComplete(MediaPlayer mp) {
        this.mMediaPlayer.start();
    }

    public void onCompletion(MediaPlayer mediaPlayer) {
        cleanActivityUp();
        if (!(this.mIsPlayBackError || this.mIsCompleted)) {
            this.mIsCompleted = true;
            processEvent(TRACKING_EVENTS_TYPE.complete);
            if (VASTPlayer.currentPlayer.listener != null) {
                VASTPlayer.currentPlayer.listener.vastProgress(100);
                VASTPlayer.currentPlayer.listener.vastComplete();
                if (this.mIsRewarded) {
                    VASTPlayer.currentPlayer.listener.vastRewardedVideoComplete();
                }
            }
        }
        if (this.mPostrollFlag) {
            showPostroll();
        } else {
            closeClicked();
        }
    }

    private void showPostroll() {
        this.showingPostroll = true;
        cleanActivityUp();
        this.mPostroll.show(this.mRootLayout);
    }

    private void overlayClicked() {
        startToolBarTimer();
    }

    private void processImpressions() {
        this.mIsProcessedImpressions = true;
        fireUrls("impression", this.mVastModel.getImpressions());
    }

    private void fireUrl(String type, String url) {
        if (url != null) {
            HttpTools.httpGetURL(type, url, VASTPlayer.currentPlayer);
        } else {
            FuseLog.m604d(TAG, "\turl is null");
        }
    }

    private void fireUrls(String type, List<String> urls) {
        if (urls != null) {
            for (String url : urls) {
                HttpTools.httpGetURL(type, url, VASTPlayer.currentPlayer);
            }
            return;
        }
        FuseLog.m604d(TAG, "\turl list is null");
    }

    private void startToolBarTimer() {
        if (this.mQuartile != 4) {
            if (this.mMediaPlayer != null && this.mMediaPlayer.isPlaying()) {
                stopToolBarTimer();
                this.mToolBarTimer = new Timer();
                this.mToolBarTimer.schedule(new C12424(), TOOLBAR_HIDE_DELAY);
                this.mPlayerControls.setVisibility(0);
            }
            if (this.mIsVideoPaused) {
                activateButtons(true);
            }
        }
    }

    private void stopToolBarTimer() {
        if (this.mToolBarTimer != null) {
            this.mToolBarTimer.cancel();
            this.mToolBarTimer = null;
        }
    }

    private void startTrackingEventTimer() {
        stopTrackingEventTimer();
        if (!this.mIsCompleted) {
            int videoDuration = this.mMediaPlayer.getDuration();
            List<VASTTracking> eventsTemp = null;
            try {
                eventsTemp = (List) this.mTrackingEventMap.get(TRACKING_EVENTS_TYPE.progress);
            } catch (Exception e) {
            }
            List<VASTTracking> events = eventsTemp;
            this.mTrackingEventTimer = new Timer();
            this.mTrackingEventTimer.scheduleAtFixedRate(new C12435(videoDuration, events), 0, QUARTILE_TIMER_INTERVAL);
        }
    }

    private void stopTrackingEventTimer() {
        if (this.mTrackingEventTimer != null) {
            this.mTrackingEventTimer.cancel();
            this.mTrackingEventTimer = null;
        }
    }

    private void startVideoProgressTimer() {
        this.mStartVideoProgressTimer = new Timer();
        this.mVideoProgressTracker = new LinkedList();
        this.mPlayerControls.setTimes((long) this.mVideoDuration, this.mSkipOffset);
        this.mStartVideoProgressTimer.schedule(new C12456(), 0, VIDEO_PROGRESS_TIMER_INTERVAL);
    }

    private void stopVideoProgressTimer() {
        if (this.mStartVideoProgressTimer != null) {
            this.mStartVideoProgressTimer.cancel();
        }
    }

    private void processEvent(TRACKING_EVENTS_TYPE eventName) {
        List<VASTTracking> events = (List) this.mTrackingEventMap.get(eventName);
        List<String> urls = new ArrayList();
        if (events != null) {
            for (VASTTracking v : events) {
                urls.add(v.getValue());
            }
            fireUrls(eventName.name(), urls);
        }
    }

    private void processProgressEvent(VASTTracking event) {
        if (event != null) {
            fireUrl(event.getEvent().toString(), event.getValue());
        }
    }

    private void finishVAST() {
        cleanActivityUp();
        try {
            VASTPlayer.currentPlayer.listener.vastDismiss();
        } catch (NullPointerException e) {
        }
        VASTPlayer.currentPlayer.setLoaded(false);
        finish();
    }
}
