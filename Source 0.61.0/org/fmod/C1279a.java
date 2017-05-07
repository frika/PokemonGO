package org.fmod;

import android.media.AudioRecord;
import android.util.Log;
import java.nio.ByteBuffer;

/* renamed from: org.fmod.a */
final class C1279a implements Runnable {
    private final FMODAudioDevice f745a;
    private final ByteBuffer f746b;
    private final int f747c;
    private final int f748d;
    private final int f749e;
    private volatile Thread f750f;
    private volatile boolean f751g;
    private AudioRecord f752h;
    private boolean f753i;

    C1279a(FMODAudioDevice fMODAudioDevice, int i, int i2) {
        this.f745a = fMODAudioDevice;
        this.f747c = i;
        this.f748d = i2;
        this.f749e = 2;
        this.f746b = ByteBuffer.allocateDirect(AudioRecord.getMinBufferSize(i, i2, 2));
    }

    private void m621d() {
        if (this.f752h != null) {
            if (this.f752h.getState() == 1) {
                this.f752h.stop();
            }
            this.f752h.release();
            this.f752h = null;
        }
        this.f746b.position(0);
        this.f753i = false;
    }

    public final int m622a() {
        return this.f746b.capacity();
    }

    public final void m623b() {
        if (this.f750f != null) {
            m624c();
        }
        this.f751g = true;
        this.f750f = new Thread(this);
        this.f750f.start();
    }

    public final void m624c() {
        while (this.f750f != null) {
            this.f751g = false;
            try {
                this.f750f.join();
                this.f750f = null;
            } catch (InterruptedException e) {
            }
        }
    }

    public final void run() {
        int i = 3;
        while (this.f751g) {
            int i2;
            if (!this.f753i && i > 0) {
                m621d();
                this.f752h = new AudioRecord(1, this.f747c, this.f748d, this.f749e, this.f746b.capacity());
                this.f753i = this.f752h.getState() == 1;
                if (this.f753i) {
                    this.f746b.position(0);
                    this.f752h.startRecording();
                    i2 = 3;
                    if (this.f753i || this.f752h.getRecordingState() != 3) {
                        i = i2;
                    } else {
                        this.f745a.fmodProcessMicData(this.f746b, this.f752h.read(this.f746b, this.f746b.capacity()));
                        this.f746b.position(0);
                        i = i2;
                    }
                } else {
                    Log.e("FMOD", "AudioRecord failed to initialize (status " + this.f752h.getState() + ")");
                    i--;
                    m621d();
                }
            }
            i2 = i;
            if (this.f753i) {
            }
            i = i2;
        }
        m621d();
    }
}
