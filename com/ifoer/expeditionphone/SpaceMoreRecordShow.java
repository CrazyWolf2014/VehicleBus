package com.ifoer.expeditionphone;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.ifoer.entity.SpaceInfoRecord;

public class SpaceMoreRecordShow extends Fragment {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle bundle) {
        SpaceInfoRecord info = (SpaceInfoRecord) getArguments().getSerializable("info");
        return new SpaceCurrentModeGraphLayout(info.getId(), info.getPath(), info.getSeletedId(), info.getFilterLists(), getActivity());
    }
}
