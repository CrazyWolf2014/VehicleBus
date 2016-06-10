package com.iflytek.record;

import android.content.Context;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import com.cnlaunch.framework.network.http.SyncHttpTransportSE;
import com.iflytek.msc.p013f.C0273b;
import com.iflytek.msc.p013f.C0276e;
import com.iflytek.record.C0287a.C0286a;
import com.iflytek.speech.SpeechError;

public class PcmPlayer {
    private AudioTrack f1060a;
    private C0287a f1061b;
    private Context f1062c;
    private C0285b f1063d;
    private C0284a f1064e;
    private volatile PLAY_STATE f1065f;
    private int f1066g;
    private boolean f1067h;
    private int f1068i;
    private Handler f1069j;

    public enum PLAY_STATE {
        INIT,
        BUFFERING,
        PLAYING,
        PAUSED,
        STOPED
    }

    /* renamed from: com.iflytek.record.PcmPlayer.a */
    public interface C0284a {
        void m1267a();

        void m1268a(int i, int i2, int i3);

        void m1269a(SpeechError speechError);

        void m1270b();

        void m1271c();
    }

    /* renamed from: com.iflytek.record.PcmPlayer.b */
    private class C0285b extends Thread {
        final /* synthetic */ PcmPlayer f1059a;

        private C0285b(PcmPlayer pcmPlayer) {
            this.f1059a = pcmPlayer;
        }

        public void run() {
            C0276e.m1220a("start player");
            int a = this.f1059a.f1061b.m1292a();
            int minBufferSize = AudioTrack.getMinBufferSize(a, 2, 2);
            C0273b.m1211a(this.f1059a.f1062c, Boolean.valueOf(this.f1059a.f1067h));
            this.f1059a.f1060a = new AudioTrack(this.f1059a.f1066g, a, 2, 2, minBufferSize * 2, 1);
            if (minBufferSize == -2 || minBufferSize == -1) {
                throw new Exception();
            }
            AudioManager audioManager = (AudioManager) this.f1059a.f1062c.getSystemService("audio");
            audioManager.setMode(0);
            audioManager.setStreamVolume(this.f1059a.f1066g, audioManager.getStreamVolume(this.f1059a.f1066g), 0);
            this.f1059a.f1061b.m1298b();
            if (this.f1059a.f1065f != PLAY_STATE.STOPED) {
                this.f1059a.f1065f = PLAY_STATE.PLAYING;
            }
            while (this.f1059a.f1065f != PLAY_STATE.STOPED) {
                if (this.f1059a.f1065f == PLAY_STATE.PLAYING || this.f1059a.f1065f == PLAY_STATE.BUFFERING) {
                    if (this.f1059a.f1061b.m1303f()) {
                        if (this.f1059a.f1065f == PLAY_STATE.BUFFERING) {
                            this.f1059a.f1065f = PLAY_STATE.PLAYING;
                            Message.obtain(this.f1059a.f1069j, 2).sendToTarget();
                        }
                        int c = this.f1059a.f1061b.m1300c();
                        C0286a d = this.f1059a.f1061b.m1301d();
                        if (d != null) {
                            this.f1059a.f1068i = d.f1073d;
                            Message.obtain(this.f1059a.f1069j, 3, c, d.f1072c).sendToTarget();
                        }
                        if (this.f1059a.f1060a.getPlayState() != 3) {
                            this.f1059a.f1060a.play();
                        }
                        this.f1059a.f1061b.m1293a(this.f1059a.f1060a, minBufferSize);
                    } else if (this.f1059a.f1061b.m1302e()) {
                        C0276e.m1220a("play stoped");
                        Message.obtain(this.f1059a.f1069j, 4).sendToTarget();
                        break;
                    } else {
                        try {
                            if (this.f1059a.f1065f == PLAY_STATE.PLAYING) {
                                C0276e.m1220a("play onpaused!");
                                this.f1059a.f1065f = PLAY_STATE.BUFFERING;
                                Message.obtain(this.f1059a.f1069j, 1).sendToTarget();
                            }
                            C0285b.sleep(50);
                        } catch (Exception e) {
                            e.printStackTrace();
                            Message.obtain(this.f1059a.f1069j, 0, new SpeechError(15, SyncHttpTransportSE.DEFAULTTIMEOUT)).sendToTarget();
                        } finally {
                            this.f1059a.f1065f = PLAY_STATE.STOPED;
                            if (this.f1059a.f1060a != null) {
                                this.f1059a.f1060a.release();
                                this.f1059a.f1060a = null;
                            }
                            C0273b.m1212b(this.f1059a.f1062c, Boolean.valueOf(this.f1059a.f1067h));
                        }
                    }
                } else if (this.f1059a.f1065f == PLAY_STATE.PAUSED) {
                    this.f1059a.f1060a.pause();
                }
            }
            this.f1059a.f1060a.stop();
        }
    }

    public PcmPlayer(Context context) {
        this.f1060a = null;
        this.f1061b = null;
        this.f1062c = null;
        this.f1063d = null;
        this.f1064e = null;
        this.f1065f = PLAY_STATE.INIT;
        this.f1066g = 3;
        this.f1067h = false;
        this.f1068i = 0;
        this.f1069j = new C0288b(this, Looper.getMainLooper());
        this.f1062c = context;
    }

    public PcmPlayer(Context context, int i, boolean z) {
        this.f1060a = null;
        this.f1061b = null;
        this.f1062c = null;
        this.f1063d = null;
        this.f1064e = null;
        this.f1065f = PLAY_STATE.INIT;
        this.f1066g = 3;
        this.f1067h = false;
        this.f1068i = 0;
        this.f1069j = new C0288b(this, Looper.getMainLooper());
        this.f1062c = context;
        this.f1066g = i;
        this.f1067h = z;
    }

    public PLAY_STATE m1285a() {
        return this.f1065f;
    }

    public boolean m1286a(C0287a c0287a, C0284a c0284a) {
        if (this.f1065f != PLAY_STATE.STOPED && this.f1065f != PLAY_STATE.INIT) {
            return false;
        }
        this.f1065f = PLAY_STATE.INIT;
        this.f1061b = c0287a;
        this.f1064e = c0284a;
        this.f1063d = new C0285b();
        this.f1063d.start();
        return true;
    }

    public boolean m1287b() {
        if (this.f1065f != PLAY_STATE.PLAYING) {
            return false;
        }
        this.f1065f = PLAY_STATE.PAUSED;
        return true;
    }

    public boolean m1288c() {
        if (this.f1065f != PLAY_STATE.PAUSED) {
            return false;
        }
        this.f1065f = PLAY_STATE.PLAYING;
        return true;
    }

    public void m1289d() {
        this.f1065f = PLAY_STATE.STOPED;
    }
}
