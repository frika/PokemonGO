package org.fmod;

import android.media.AudioTrack;
import android.util.Log;
import java.nio.ByteBuffer;

public class FMODAudioDevice implements Runnable {
    private static int f734h;
    private static int f735i;
    private static int f736j;
    private static int f737k;
    private volatile Thread f738a;
    private volatile boolean f739b;
    private AudioTrack f740c;
    private boolean f741d;
    private ByteBuffer f742e;
    private byte[] f743f;
    private volatile C1279a f744g;

    static {
        f734h = 0;
        f735i = 1;
        f736j = 2;
        f737k = 3;
    }

    public FMODAudioDevice() {
        this.f738a = null;
        this.f739b = false;
        this.f740c = null;
        this.f741d = false;
        this.f742e = null;
        this.f743f = null;
    }

    private native int fmodGetInfo(int i);

    private native int fmodProcess(ByteBuffer byteBuffer);

    private void releaseAudioTrack() {
        if (this.f740c != null) {
            if (this.f740c.getState() == 1) {
                this.f740c.stop();
            }
            this.f740c.release();
            this.f740c = null;
        }
        this.f742e = null;
        this.f743f = null;
        this.f741d = false;
    }

    public synchronized void close() {
        stop();
    }

    native int fmodProcessMicData(ByteBuffer byteBuffer, int i);

    public boolean isRunning() {
        return this.f738a != null && this.f738a.isAlive();
    }

    public void run() {
        int i = 3;
        while (this.f739b) {
            int i2;
            if (this.f741d || i <= 0) {
                i2 = i;
            } else {
                releaseAudioTrack();
                int fmodGetInfo = fmodGetInfo(f734h);
                int round = Math.round(((float) AudioTrack.getMinBufferSize(fmodGetInfo, 3, 2)) * 1.1f) & -4;
                int fmodGetInfo2 = fmodGetInfo(f735i);
                i2 = fmodGetInfo(f736j);
                if ((fmodGetInfo2 * i2) * 4 > round) {
                    round = (i2 * fmodGetInfo2) * 4;
                }
                this.f740c = new AudioTrack(3, fmodGetInfo, 3, 2, round, 1);
                this.f741d = this.f740c.getState() == 1;
                if (this.f741d) {
                    this.f742e = ByteBuffer.allocateDirect((fmodGetInfo2 * 2) * 2);
                    this.f743f = new byte[this.f742e.capacity()];
                    this.f740c.play();
                    i2 = 3;
                } else {
                    Log.e("FMOD", "AudioTrack failed to initialize (status " + this.f740c.getState() + ")");
                    releaseAudioTrack();
                    i2 = i - 1;
                }
            }
            if (!this.f741d) {
                i = i2;
            } else if (fmodGetInfo(f737k) == 1) {
                fmodProcess(this.f742e);
                this.f742e.get(this.f743f, 0, this.f742e.capacity());
                this.f740c.write(this.f743f, 0, this.f742e.capacity());
                this.f742e.position(0);
                i = i2;
            } else {
                releaseAudioTrack();
                i = i2;
            }
        }
        releaseAudioTrack();
    }

    public synchronized void start() {
        if (this.f738a != null) {
            stop();
        }
        this.f738a = new Thread(this, "FMODAudioDevice");
        this.f738a.setPriority(10);
        this.f739b = true;
        this.f738a.start();
        if (this.f744g != null) {
            this.f744g.m623b();
        }
    }

    public synchronized int startAudioRecord(int i, int i2, int i3) {
        if (this.f744g == null) {
            this.f744g = new C1279a(this, i, i2);
            this.f744g.m623b();
        }
        return this.f744g.m622a();
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public synchronized void stop() {
        /*
        r1 = this;
        monitor-enter(r1);
    L_0x0001:
        r0 = r1.f738a;	 Catch:{ all -> 0x001e }
        if (r0 == 0) goto L_0x0013;
    L_0x0005:
        r0 = 0;
        r1.f739b = r0;	 Catch:{ all -> 0x001e }
        r0 = r1.f738a;	 Catch:{ InterruptedException -> 0x0011 }
        r0.join();	 Catch:{ InterruptedException -> 0x0011 }
        r0 = 0;
        r1.f738a = r0;	 Catch:{ InterruptedException -> 0x0011 }
        goto L_0x0001;
    L_0x0011:
        r0 = move-exception;
        goto L_0x0001;
    L_0x0013:
        r0 = r1.f744g;	 Catch:{ all -> 0x001e }
        if (r0 == 0) goto L_0x001c;
    L_0x0017:
        r0 = r1.f744g;	 Catch:{ all -> 0x001e }
        r0.m624c();	 Catch:{ all -> 0x001e }
    L_0x001c:
        monitor-exit(r1);
        return;
    L_0x001e:
        r0 = move-exception;
        monitor-exit(r1);
        throw r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.fmod.FMODAudioDevice.stop():void");
    }

    public synchronized void stopAudioRecord() {
        if (this.f744g != null) {
            this.f744g.m624c();
            this.f744g = null;
        }
    }
}
