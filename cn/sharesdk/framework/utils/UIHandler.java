package cn.sharesdk.framework.utils;

import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;

public class UIHandler {
    private static Handler handler;

    private static Message getShellMessage(int i, Callback callback) {
        Message message = new Message();
        message.what = i;
        return getShellMessage(message, callback);
    }

    private static Message getShellMessage(Message message, Callback callback) {
        Message message2 = new Message();
        message2.obj = new Object[]{message, callback};
        return message2;
    }

    private static void handleMessage(Message message) {
        Object[] objArr = (Object[]) message.obj;
        Message message2 = (Message) objArr[0];
        Callback callback = (Callback) objArr[1];
        if (callback != null) {
            callback.handleMessage(message2);
        }
    }

    public static void prepare() {
        if (handler == null) {
            handler = new Handler(new C0064i());
        }
    }

    public static boolean sendEmptyMessage(int i, Callback callback) {
        return handler.sendMessage(getShellMessage(i, callback));
    }

    public static boolean sendEmptyMessageAtTime(int i, long j, Callback callback) {
        return handler.sendMessageAtTime(getShellMessage(i, callback), j);
    }

    public static boolean sendEmptyMessageDelayed(int i, long j, Callback callback) {
        return handler.sendMessageDelayed(getShellMessage(i, callback), j);
    }

    public static boolean sendMessage(Message message, Callback callback) {
        return handler.sendMessage(getShellMessage(message, callback));
    }

    public static boolean sendMessageAtFrontOfQueue(Message message, Callback callback) {
        return handler.sendMessageAtFrontOfQueue(getShellMessage(message, callback));
    }

    public static boolean sendMessageAtTime(Message message, long j, Callback callback) {
        return handler.sendMessageAtTime(getShellMessage(message, callback), j);
    }

    public static boolean sendMessageDelayed(Message message, long j, Callback callback) {
        return handler.sendMessageDelayed(getShellMessage(message, callback), j);
    }
}
