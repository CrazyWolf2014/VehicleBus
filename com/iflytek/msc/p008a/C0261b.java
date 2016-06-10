package com.iflytek.msc.p008a;

/* renamed from: com.iflytek.msc.a.b */
final class C0261b implements Runnable {
    final /* synthetic */ C0260a f1001a;

    C0261b(C0260a c0260a) {
        this.f1001a = c0260a;
    }

    public void run() {
        /* JADX: method processing error */
/*
        Error: jadx.core.utils.exceptions.JadxRuntimeException: Can't find block by offset: 0x0060 in list []
	at jadx.core.utils.BlockUtils.getBlockByOffset(BlockUtils.java:42)
	at jadx.core.dex.instructions.IfNode.initBlocks(IfNode.java:58)
	at jadx.core.dex.visitors.blocksmaker.BlockFinish.initBlocksInIfNodes(BlockFinish.java:48)
	at jadx.core.dex.visitors.blocksmaker.BlockFinish.visit(BlockFinish.java:33)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.ProcessClass.process(ProcessClass.java:37)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:281)
	at jadx.api.JavaClass.decompile(JavaClass.java:59)
	at jadx.api.JadxDecompiler$1.run(JadxDecompiler.java:161)
*/
        /*
        r4 = this;
        r0 = r4.f1001a;	 Catch:{ IOException -> 0x0021, SpeechError -> 0x009a, InterruptedException -> 0x00d6, Exception -> 0x0118, all -> 0x015a }
        r0.m1164i();	 Catch:{ IOException -> 0x0021, SpeechError -> 0x009a, InterruptedException -> 0x00d6, Exception -> 0x0118, all -> 0x015a }
    L_0x0005:
        r0 = r4.f1001a;	 Catch:{ IOException -> 0x0021, SpeechError -> 0x009a, InterruptedException -> 0x00d6, Exception -> 0x0118, all -> 0x015a }
        r0 = r0.f995f;	 Catch:{ IOException -> 0x0021, SpeechError -> 0x009a, InterruptedException -> 0x00d6, Exception -> 0x0118, all -> 0x015a }
        r1 = com.iflytek.msc.p008a.C0260a.C0259a.exiting;	 Catch:{ IOException -> 0x0021, SpeechError -> 0x009a, InterruptedException -> 0x00d6, Exception -> 0x0118, all -> 0x015a }
        if (r0 == r1) goto L_0x0066;	 Catch:{ IOException -> 0x0021, SpeechError -> 0x009a, InterruptedException -> 0x00d6, Exception -> 0x0118, all -> 0x015a }
    L_0x000d:
        r0 = r4.f1001a;	 Catch:{ IOException -> 0x0021, SpeechError -> 0x009a, InterruptedException -> 0x00d6, Exception -> 0x0118, all -> 0x015a }
        r0 = r0.f994e;	 Catch:{ IOException -> 0x0021, SpeechError -> 0x009a, InterruptedException -> 0x00d6, Exception -> 0x0118, all -> 0x015a }
        if (r0 != 0) goto L_0x0066;	 Catch:{ IOException -> 0x0021, SpeechError -> 0x009a, InterruptedException -> 0x00d6, Exception -> 0x0118, all -> 0x015a }
    L_0x0013:
        r0 = r4.f1001a;	 Catch:{ IOException -> 0x0021, SpeechError -> 0x009a, InterruptedException -> 0x00d6, Exception -> 0x0118, all -> 0x015a }
        r0 = r0.f995f;	 Catch:{ IOException -> 0x0021, SpeechError -> 0x009a, InterruptedException -> 0x00d6, Exception -> 0x0118, all -> 0x015a }
        r1 = com.iflytek.msc.p008a.C0260a.C0259a.exited;	 Catch:{ IOException -> 0x0021, SpeechError -> 0x009a, InterruptedException -> 0x00d6, Exception -> 0x0118, all -> 0x015a }
        if (r0 == r1) goto L_0x0066;	 Catch:{ IOException -> 0x0021, SpeechError -> 0x009a, InterruptedException -> 0x00d6, Exception -> 0x0118, all -> 0x015a }
    L_0x001b:
        r0 = r4.f1001a;	 Catch:{ IOException -> 0x0021, SpeechError -> 0x009a, InterruptedException -> 0x00d6, Exception -> 0x0118, all -> 0x015a }
        r0.m1162g();	 Catch:{ IOException -> 0x0021, SpeechError -> 0x009a, InterruptedException -> 0x00d6, Exception -> 0x0118, all -> 0x015a }
        goto L_0x0005;
    L_0x0021:
        r0 = move-exception;
        r0.printStackTrace();	 Catch:{ IOException -> 0x0021, SpeechError -> 0x009a, InterruptedException -> 0x00d6, Exception -> 0x0118, all -> 0x015a }
        r0 = r4.f1001a;	 Catch:{ IOException -> 0x0021, SpeechError -> 0x009a, InterruptedException -> 0x00d6, Exception -> 0x0118, all -> 0x015a }
        r1 = new com.iflytek.speech.SpeechError;	 Catch:{ IOException -> 0x0021, SpeechError -> 0x009a, InterruptedException -> 0x00d6, Exception -> 0x0118, all -> 0x015a }
        r2 = 14;	 Catch:{ IOException -> 0x0021, SpeechError -> 0x009a, InterruptedException -> 0x00d6, Exception -> 0x0118, all -> 0x015a }
        r3 = 30000; // 0x7530 float:4.2039E-41 double:1.4822E-319;	 Catch:{ IOException -> 0x0021, SpeechError -> 0x009a, InterruptedException -> 0x00d6, Exception -> 0x0118, all -> 0x015a }
        r1.<init>(r2, r3);	 Catch:{ IOException -> 0x0021, SpeechError -> 0x009a, InterruptedException -> 0x00d6, Exception -> 0x0118, all -> 0x015a }
        r0.f998i = r1;	 Catch:{ IOException -> 0x0021, SpeechError -> 0x009a, InterruptedException -> 0x00d6, Exception -> 0x0118, all -> 0x015a }
        r0 = r4.f1001a;
        r0 = r0.f998i;
        if (r0 == 0) goto L_0x0060;
    L_0x0038:
        r0 = new java.lang.StringBuilder;
        r0.<init>();
        r1 = r4.f1001a;
        r1 = r1.m1165j();
        r0 = r0.append(r1);
        r1 = " occur Error = ";
        r0 = r0.append(r1);
        r1 = r4.f1001a;
        r1 = r1.f998i;
        r1 = r1.toString();
        r0 = r0.append(r1);
        r0 = r0.toString();
        com.iflytek.msc.p013f.C0276e.m1220a(r0);
    L_0x0060:
        r0 = r4.f1001a;
        r0.m1163h();
    L_0x0065:
        return;
    L_0x0066:
        r0 = r4.f1001a;
        r0 = r0.f998i;
        if (r0 == 0) goto L_0x0094;
    L_0x006c:
        r0 = new java.lang.StringBuilder;
        r0.<init>();
        r1 = r4.f1001a;
        r1 = r1.m1165j();
        r0 = r0.append(r1);
        r1 = " occur Error = ";
        r0 = r0.append(r1);
        r1 = r4.f1001a;
        r1 = r1.f998i;
        r1 = r1.toString();
        r0 = r0.append(r1);
        r0 = r0.toString();
        com.iflytek.msc.p013f.C0276e.m1220a(r0);
    L_0x0094:
        r0 = r4.f1001a;
        r0.m1163h();
        goto L_0x0065;
    L_0x009a:
        r0 = move-exception;
        r0.printStackTrace();	 Catch:{ IOException -> 0x0021, SpeechError -> 0x009a, InterruptedException -> 0x00d6, Exception -> 0x0118, all -> 0x015a }
        r1 = r4.f1001a;	 Catch:{ IOException -> 0x0021, SpeechError -> 0x009a, InterruptedException -> 0x00d6, Exception -> 0x0118, all -> 0x015a }
        r1.f998i = r0;	 Catch:{ IOException -> 0x0021, SpeechError -> 0x009a, InterruptedException -> 0x00d6, Exception -> 0x0118, all -> 0x015a }
        r0 = r4.f1001a;
        r0 = r0.f998i;
        if (r0 == 0) goto L_0x00d0;
    L_0x00a8:
        r0 = new java.lang.StringBuilder;
        r0.<init>();
        r1 = r4.f1001a;
        r1 = r1.m1165j();
        r0 = r0.append(r1);
        r1 = " occur Error = ";
        r0 = r0.append(r1);
        r1 = r4.f1001a;
        r1 = r1.f998i;
        r1 = r1.toString();
        r0 = r0.append(r1);
        r0 = r0.toString();
        com.iflytek.msc.p013f.C0276e.m1220a(r0);
    L_0x00d0:
        r0 = r4.f1001a;
        r0.m1163h();
        goto L_0x0065;
    L_0x00d6:
        r0 = move-exception;
        r0.printStackTrace();	 Catch:{ IOException -> 0x0021, SpeechError -> 0x009a, InterruptedException -> 0x00d6, Exception -> 0x0118, all -> 0x015a }
        r1 = r4.f1001a;	 Catch:{ IOException -> 0x0021, SpeechError -> 0x009a, InterruptedException -> 0x00d6, Exception -> 0x0118, all -> 0x015a }
        r2 = new com.iflytek.speech.SpeechError;	 Catch:{ IOException -> 0x0021, SpeechError -> 0x009a, InterruptedException -> 0x00d6, Exception -> 0x0118, all -> 0x015a }
        r2.<init>(r0);	 Catch:{ IOException -> 0x0021, SpeechError -> 0x009a, InterruptedException -> 0x00d6, Exception -> 0x0118, all -> 0x015a }
        r1.f998i = r2;	 Catch:{ IOException -> 0x0021, SpeechError -> 0x009a, InterruptedException -> 0x00d6, Exception -> 0x0118, all -> 0x015a }
        r0 = r4.f1001a;
        r0 = r0.f998i;
        if (r0 == 0) goto L_0x0111;
    L_0x00e9:
        r0 = new java.lang.StringBuilder;
        r0.<init>();
        r1 = r4.f1001a;
        r1 = r1.m1165j();
        r0 = r0.append(r1);
        r1 = " occur Error = ";
        r0 = r0.append(r1);
        r1 = r4.f1001a;
        r1 = r1.f998i;
        r1 = r1.toString();
        r0 = r0.append(r1);
        r0 = r0.toString();
        com.iflytek.msc.p013f.C0276e.m1220a(r0);
    L_0x0111:
        r0 = r4.f1001a;
        r0.m1163h();
        goto L_0x0065;
    L_0x0118:
        r0 = move-exception;
        r0.printStackTrace();	 Catch:{ IOException -> 0x0021, SpeechError -> 0x009a, InterruptedException -> 0x00d6, Exception -> 0x0118, all -> 0x015a }
        r1 = r4.f1001a;	 Catch:{ IOException -> 0x0021, SpeechError -> 0x009a, InterruptedException -> 0x00d6, Exception -> 0x0118, all -> 0x015a }
        r2 = new com.iflytek.speech.SpeechError;	 Catch:{ IOException -> 0x0021, SpeechError -> 0x009a, InterruptedException -> 0x00d6, Exception -> 0x0118, all -> 0x015a }
        r2.<init>(r0);	 Catch:{ IOException -> 0x0021, SpeechError -> 0x009a, InterruptedException -> 0x00d6, Exception -> 0x0118, all -> 0x015a }
        r1.f998i = r2;	 Catch:{ IOException -> 0x0021, SpeechError -> 0x009a, InterruptedException -> 0x00d6, Exception -> 0x0118, all -> 0x015a }
        r0 = r4.f1001a;
        r0 = r0.f998i;
        if (r0 == 0) goto L_0x0153;
    L_0x012b:
        r0 = new java.lang.StringBuilder;
        r0.<init>();
        r1 = r4.f1001a;
        r1 = r1.m1165j();
        r0 = r0.append(r1);
        r1 = " occur Error = ";
        r0 = r0.append(r1);
        r1 = r4.f1001a;
        r1 = r1.f998i;
        r1 = r1.toString();
        r0 = r0.append(r1);
        r0 = r0.toString();
        com.iflytek.msc.p013f.C0276e.m1220a(r0);
    L_0x0153:
        r0 = r4.f1001a;
        r0.m1163h();
        goto L_0x0065;
    L_0x015a:
        r0 = move-exception;
        r1 = r4.f1001a;
        r1 = r1.f998i;
        if (r1 == 0) goto L_0x0189;
    L_0x0161:
        r1 = new java.lang.StringBuilder;
        r1.<init>();
        r2 = r4.f1001a;
        r2 = r2.m1165j();
        r1 = r1.append(r2);
        r2 = " occur Error = ";
        r1 = r1.append(r2);
        r2 = r4.f1001a;
        r2 = r2.f998i;
        r2 = r2.toString();
        r1 = r1.append(r2);
        r1 = r1.toString();
        com.iflytek.msc.p013f.C0276e.m1220a(r1);
    L_0x0189:
        r1 = r4.f1001a;
        r1.m1163h();
        throw r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.iflytek.msc.a.b.run():void");
    }
}
