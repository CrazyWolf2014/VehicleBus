package com.ifoer.expeditionphone.inteface;

import com.ifoer.adapter.BaseDiagAdapter;
import java.util.HashMap;
import java.util.List;

public interface IMainActivityInterface {
    void checkIfFirstCome();

    BaseDiagAdapter createBaseDiagAdapter(List<HashMap<String, Object>> list);

    void initPopupWindow();

    void initToastPopupWindow();

    void mainView();
}
