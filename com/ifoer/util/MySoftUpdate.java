package com.ifoer.util;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;
import com.car.result.X431PadSoftListResult;
import com.cnlaunch.x431frame.C0136R;
import com.google.protobuf.DescriptorProtos.MessageOptions;
import com.ifoer.db.DBDao;
import com.ifoer.entity.X431PadSoftDTO;
import com.ifoer.expeditionphone.MainActivity;
import com.ifoer.webservice.X431PadDiagSoftService;
import com.thoughtworks.xstream.XStream;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import org.xbill.DNS.KEYRecord;

public class MySoftUpdate {
    private static final String TAG = "SfotUpdate";
    Context context;
    private final Handler mHandler;
    private Updater mWork;
    private List<X431PadSoftDTO> resultList;

    /* renamed from: com.ifoer.util.MySoftUpdate.1 */
    class C07541 extends Handler {
        C07541() {
        }

        public void handleMessage(Message msg) {
            switch (msg.what) {
                case KEYRecord.OWNER_USER /*0*/:
                    Toast.makeText(MySoftUpdate.this.context, C0136R.string.timeout, 0).show();
                case MessageOptions.MESSAGE_SET_WIRE_FORMAT_FIELD_NUMBER /*1*/:
                    Toast.makeText(MySoftUpdate.this.context, C0136R.string.get_data_fail, 1).show();
                case MessageOptions.NO_STANDARD_DESCRIPTOR_ACCESSOR_FIELD_NUMBER /*2*/:
                    Toast.makeText(MySoftUpdate.this.context, MySoftUpdate.this.context.getResources().getText(C0136R.string.connect_server_error), 1).show();
                default:
            }
        }
    }

    class Updater extends Thread {
        Updater() {
        }

        public void run() {
            MySoftUpdate.this.checkUpdate();
        }
    }

    public MySoftUpdate(Context context) {
        this.resultList = new ArrayList();
        this.mHandler = new C07541();
        this.context = context;
    }

    public void checkUpdateAsync() {
        if (this.mWork == null) {
            this.mWork = new Updater();
            this.mWork.start();
        }
    }

    private void checkUpdate() {
        if (!DBDao.getInstance(this.context).isExistSoftId(MainActivity.database)) {
            DBDao.getInstance(this.context).updateUpgrade(MainActivity.database);
        }
        X431PadDiagSoftService service = new X431PadDiagSoftService();
        String cc = MySharedPreferences.getStringValue(this.context, MySharedPreferences.CCKey);
        String token = MySharedPreferences.getStringValue(this.context, MySharedPreferences.TokenKey);
        String serialNo = MySharedPreferences.getStringValue(this.context, MySharedPreferences.serialNoKey);
        if (MainActivity.country == null || MainActivity.country.length() <= 0) {
            MainActivity.country = Locale.getDefault().getCountry();
        }
        X431PadSoftListResult result = null;
        try {
            result = service.queryLatestDiagSofts(cc, token, serialNo, Integer.valueOf(AndroidToLan.getLanId(MainActivity.country)), Integer.valueOf(XStream.NO_REFERENCES));
        } catch (SocketTimeoutException e) {
            this.mHandler.obtainMessage(0).sendToTarget();
        } catch (NullPointerException e2) {
            this.mHandler.obtainMessage(1).sendToTarget();
        }
        Intent intents;
        if (result == null) {
            intents = new Intent("Show_new_action");
            intents.putExtra("show_new", false);
            this.context.sendBroadcast(intents);
        } else if (result.getCode() == -1) {
            if (serialNo != null) {
                this.mHandler.obtainMessage(2).sendToTarget();
            }
        } else if (result.getCode() != 0) {
            intents = new Intent("Show_new_action");
            intents.putExtra("show_new", false);
            this.context.sendBroadcast(intents);
        } else if (result != null && result.getDtoList().size() > 0) {
            String lanName = AndroidToLan.toLan(Locale.getDefault().getLanguage());
            for (X431PadSoftDTO dto : result.getDtoList()) {
                lanName = AndroidToLan.getIdToLanName(Integer.parseInt(dto.getLanId()));
                String maxOldVersion = DBDao.getInstance(this.context).queryMaxVersion(serialNo, dto.getSoftId(), lanName, MainActivity.database);
                double maxOld = 0.0d;
                if (maxOldVersion != null) {
                    maxOld = Double.parseDouble(maxOldVersion.split("V")[1]);
                }
                if (maxOld < Double.parseDouble(dto.getVersionNo().split("V")[1])) {
                    dto.setMaxOldVersion(maxOldVersion);
                    this.resultList.add(dto);
                }
            }
            if (this.resultList.size() > 0) {
                intents = new Intent("Show_new_action");
                intents.putExtra("show_new", true);
                this.context.sendBroadcast(intents);
                return;
            }
            intents = new Intent("Show_new_action");
            intents.putExtra("show_new", false);
            this.context.sendBroadcast(intents);
        }
    }
}
