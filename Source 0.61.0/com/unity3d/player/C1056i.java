package com.unity3d.player;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnBufferingUpdateListener;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.media.MediaPlayer.OnVideoSizeChangedListener;
import android.net.Uri;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.MediaController;
import android.widget.MediaController.MediaPlayerControl;
import com.google.android.exoplayer.util.NalUnitUtil;
import java.io.FileInputStream;
import java.io.IOException;

/* renamed from: com.unity3d.player.i */
public final class C1056i extends FrameLayout implements OnBufferingUpdateListener, OnCompletionListener, OnPreparedListener, OnVideoSizeChangedListener, Callback, MediaPlayerControl {
    private static boolean f696a;
    private final Context f697b;
    private final SurfaceView f698c;
    private final SurfaceHolder f699d;
    private final String f700e;
    private final int f701f;
    private final int f702g;
    private final boolean f703h;
    private final long f704i;
    private final long f705j;
    private final FrameLayout f706k;
    private final Display f707l;
    private int f708m;
    private int f709n;
    private int f710o;
    private int f711p;
    private MediaPlayer f712q;
    private MediaController f713r;
    private boolean f714s;
    private boolean f715t;
    private int f716u;
    private boolean f717v;
    private boolean f718w;
    private C1028a f719x;
    private volatile int f720y;

    /* renamed from: com.unity3d.player.i.a */
    public interface C1028a {
        void m497a(int i);
    }

    static {
        f696a = false;
    }

    protected C1056i(Context context, String str, int i, int i2, int i3, boolean z, long j, long j2, C1028a c1028a) {
        super(context);
        this.f714s = false;
        this.f715t = false;
        this.f716u = 0;
        this.f717v = false;
        this.f718w = false;
        this.f720y = 0;
        this.f719x = c1028a;
        this.f697b = context;
        this.f706k = this;
        this.f698c = new SurfaceView(context);
        this.f699d = this.f698c.getHolder();
        this.f699d.addCallback(this);
        this.f699d.setType(3);
        this.f706k.setBackgroundColor(i);
        this.f706k.addView(this.f698c);
        this.f707l = ((WindowManager) this.f697b.getSystemService("window")).getDefaultDisplay();
        this.f700e = str;
        this.f701f = i2;
        this.f702g = i3;
        this.f703h = z;
        this.f704i = j;
        this.f705j = j2;
        if (f696a) {
            C1056i.m570a("fileName: " + this.f700e);
        }
        if (f696a) {
            C1056i.m570a("backgroundColor: " + i);
        }
        if (f696a) {
            C1056i.m570a("controlMode: " + this.f701f);
        }
        if (f696a) {
            C1056i.m570a("scalingMode: " + this.f702g);
        }
        if (f696a) {
            C1056i.m570a("isURL: " + this.f703h);
        }
        if (f696a) {
            C1056i.m570a("videoOffset: " + this.f704i);
        }
        if (f696a) {
            C1056i.m570a("videoLength: " + this.f705j);
        }
        setFocusable(true);
        setFocusableInTouchMode(true);
    }

    private void m569a(int i) {
        this.f720y = i;
        if (this.f719x != null) {
            this.f719x.m497a(this.f720y);
        }
    }

    private static void m570a(String str) {
        Log.i("Video", "VideoPlayer: " + str);
    }

    private void m571b() {
        if (this.f712q != null) {
            this.f712q.setDisplay(this.f699d);
            if (!this.f717v) {
                if (f696a) {
                    C1056i.m570a("Resuming playback");
                }
                this.f712q.start();
                return;
            }
            return;
        }
        m569a(0);
        doCleanUp();
        try {
            this.f712q = new MediaPlayer();
            if (this.f703h) {
                this.f712q.setDataSource(this.f697b, Uri.parse(this.f700e));
            } else if (this.f705j != 0) {
                FileInputStream fileInputStream = new FileInputStream(this.f700e);
                this.f712q.setDataSource(fileInputStream.getFD(), this.f704i, this.f705j);
                fileInputStream.close();
            } else {
                try {
                    AssetFileDescriptor openFd = getResources().getAssets().openFd(this.f700e);
                    this.f712q.setDataSource(openFd.getFileDescriptor(), openFd.getStartOffset(), openFd.getLength());
                    openFd.close();
                } catch (IOException e) {
                    FileInputStream fileInputStream2 = new FileInputStream(this.f700e);
                    this.f712q.setDataSource(fileInputStream2.getFD());
                    fileInputStream2.close();
                }
            }
            this.f712q.setDisplay(this.f699d);
            this.f712q.setScreenOnWhilePlaying(true);
            this.f712q.setOnBufferingUpdateListener(this);
            this.f712q.setOnCompletionListener(this);
            this.f712q.setOnPreparedListener(this);
            this.f712q.setOnVideoSizeChangedListener(this);
            this.f712q.setAudioStreamType(3);
            this.f712q.prepare();
            if (this.f701f == 0 || this.f701f == 1) {
                this.f713r = new MediaController(this.f697b);
                this.f713r.setMediaPlayer(this);
                this.f713r.setAnchorView(this);
                this.f713r.setEnabled(true);
                this.f713r.show();
            }
        } catch (Exception e2) {
            if (f696a) {
                C1056i.m570a("error: " + e2.getMessage() + e2);
            }
            m569a(2);
        }
    }

    private void m572c() {
        if (!isPlaying()) {
            m569a(1);
            if (f696a) {
                C1056i.m570a("startVideoPlayback");
            }
            updateVideoLayout();
            if (!this.f717v) {
                start();
            }
        }
    }

    final boolean m573a() {
        return this.f717v;
    }

    public final boolean canPause() {
        return true;
    }

    public final boolean canSeekBackward() {
        return true;
    }

    public final boolean canSeekForward() {
        return true;
    }

    protected final void destroyPlayer() {
        if (f696a) {
            C1056i.m570a("destroyPlayer");
        }
        if (!this.f717v) {
            pause();
        }
        doCleanUp();
    }

    protected final void doCleanUp() {
        if (this.f712q != null) {
            this.f712q.release();
            this.f712q = null;
        }
        this.f710o = 0;
        this.f711p = 0;
        this.f715t = false;
        this.f714s = false;
    }

    public final int getBufferPercentage() {
        return this.f703h ? this.f716u : 100;
    }

    public final int getCurrentPosition() {
        return this.f712q == null ? 0 : this.f712q.getCurrentPosition();
    }

    public final int getDuration() {
        return this.f712q == null ? 0 : this.f712q.getDuration();
    }

    public final boolean isPlaying() {
        boolean z = this.f715t && this.f714s;
        return this.f712q == null ? !z : this.f712q.isPlaying() || !z;
    }

    public final void onBufferingUpdate(MediaPlayer mediaPlayer, int i) {
        if (f696a) {
            C1056i.m570a("onBufferingUpdate percent:" + i);
        }
        this.f716u = i;
    }

    public final void onCompletion(MediaPlayer mediaPlayer) {
        if (f696a) {
            C1056i.m570a("onCompletion called");
        }
        destroyPlayer();
        m569a(3);
    }

    public final boolean onKeyDown(int i, KeyEvent keyEvent) {
        if (i != 4 && (this.f701f != 2 || i == 0 || keyEvent.isSystem())) {
            return this.f713r != null ? this.f713r.onKeyDown(i, keyEvent) : super.onKeyDown(i, keyEvent);
        } else {
            destroyPlayer();
            m569a(3);
            return true;
        }
    }

    public final void onPrepared(MediaPlayer mediaPlayer) {
        if (f696a) {
            C1056i.m570a("onPrepared called");
        }
        this.f715t = true;
        if (this.f715t && this.f714s) {
            m572c();
        }
    }

    public final boolean onTouchEvent(MotionEvent motionEvent) {
        int action = motionEvent.getAction() & NalUnitUtil.EXTENDED_SAR;
        if (this.f701f != 2 || action != 0) {
            return this.f713r != null ? this.f713r.onTouchEvent(motionEvent) : super.onTouchEvent(motionEvent);
        } else {
            destroyPlayer();
            m569a(3);
            return true;
        }
    }

    public final void onVideoSizeChanged(MediaPlayer mediaPlayer, int i, int i2) {
        if (f696a) {
            C1056i.m570a("onVideoSizeChanged called " + i + "x" + i2);
        }
        if (i != 0 && i2 != 0) {
            this.f714s = true;
            this.f710o = i;
            this.f711p = i2;
            if (this.f715t && this.f714s) {
                m572c();
            }
        } else if (f696a) {
            C1056i.m570a("invalid video width(" + i + ") or height(" + i2 + ")");
        }
    }

    public final void pause() {
        if (this.f712q != null) {
            if (this.f718w) {
                this.f712q.pause();
            }
            this.f717v = true;
        }
    }

    public final void seekTo(int i) {
        if (this.f712q != null) {
            this.f712q.seekTo(i);
        }
    }

    public final void start() {
        if (f696a) {
            C1056i.m570a("Start");
        }
        if (this.f712q != null) {
            if (this.f718w) {
                this.f712q.start();
            }
            this.f717v = false;
        }
    }

    public final void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i2, int i3) {
        if (f696a) {
            C1056i.m570a("surfaceChanged called " + i + " " + i2 + "x" + i3);
        }
        if (this.f708m != i2 || this.f709n != i3) {
            this.f708m = i2;
            this.f709n = i3;
            if (this.f718w) {
                updateVideoLayout();
            }
        }
    }

    public final void surfaceCreated(SurfaceHolder surfaceHolder) {
        if (f696a) {
            C1056i.m570a("surfaceCreated called");
        }
        this.f718w = true;
        m571b();
    }

    public final void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        if (f696a) {
            C1056i.m570a("surfaceDestroyed called");
        }
        this.f718w = false;
    }

    protected final void updateVideoLayout() {
        if (f696a) {
            C1056i.m570a("updateVideoLayout");
        }
        if (this.f712q != null) {
            if (this.f708m == 0 || this.f709n == 0) {
                WindowManager windowManager = (WindowManager) this.f697b.getSystemService("window");
                this.f708m = windowManager.getDefaultDisplay().getWidth();
                this.f709n = windowManager.getDefaultDisplay().getHeight();
            }
            int i = this.f708m;
            int i2 = this.f709n;
            float f = ((float) this.f710o) / ((float) this.f711p);
            float f2 = ((float) this.f708m) / ((float) this.f709n);
            if (this.f702g == 1) {
                if (f2 <= f) {
                    i2 = (int) (((float) this.f708m) / f);
                } else {
                    i = (int) (((float) this.f709n) * f);
                }
            } else if (this.f702g == 2) {
                if (f2 >= f) {
                    i2 = (int) (((float) this.f708m) / f);
                } else {
                    i = (int) (((float) this.f709n) * f);
                }
            } else if (this.f702g == 0) {
                i = this.f710o;
                i2 = this.f711p;
            }
            if (f696a) {
                C1056i.m570a("frameWidth = " + i + "; frameHeight = " + i2);
            }
            this.f706k.updateViewLayout(this.f698c, new LayoutParams(i, i2, 17));
        }
    }
}
