package com.iflytek.p006b;

import android.text.TextUtils;
import com.iflytek.p007c.C0255a;

/* renamed from: com.iflytek.b.b */
public class C0253b {
    public static final String[][] f964a;
    public static final String[] f965b;

    static {
        r0 = new String[18][];
        r0[0] = new String[]{"surl", "server_url"};
        r0[1] = new String[]{"besturl_search", "search_best_url"};
        r0[2] = new String[]{"bsts", "search_best_url"};
        r0[3] = new String[]{"plain_result", "plr"};
        r0[4] = new String[]{"asr_nomatch_error", "asr_nme"};
        r0[5] = new String[]{"asr_sch", "sch"};
        r0[6] = new String[]{"asr_ptt", "ptt"};
        r0[7] = new String[]{"grammar_type", "grt"};
        r0[8] = new String[]{"search_area", "area"};
        r0[9] = new String[]{"vad_bos", "vad_timeout"};
        r0[10] = new String[]{"bos", "vad_timeout"};
        r0[11] = new String[]{"vad_eos", "vad_speech_tail", "eos"};
        r0[12] = new String[]{"eos", "vad_speech_tail", "eos"};
        r0[13] = new String[]{"asr_audio_path", "aap"};
        r0[14] = new String[]{"tts_buffer_time", "tbt"};
        r0[15] = new String[]{"tts_audio_path", "tap"};
        r0[16] = new String[]{"subject", "sub"};
        r0[17] = new String[]{"data_type", "dtt"};
        f964a = r0;
        f965b = new String[]{"tap", "aap"};
    }

    public static boolean m1130a(String str) {
        if (TextUtils.isEmpty(str)) {
            return false;
        }
        if (C0255a.m1145a()) {
            return true;
        }
        for (String equals : f965b) {
            if (equals.equals(str)) {
                return false;
            }
        }
        return true;
    }
}
