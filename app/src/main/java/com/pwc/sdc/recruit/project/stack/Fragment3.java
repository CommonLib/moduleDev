package com.pwc.sdc.recruit.project.stack;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.pwc.sdc.recruit.R;


/**
 * @author:dongpo 创建时间: 3016/9/3
 * 描述:
 * 修改:
 */
public class Fragment3 extends Fragment implements View.OnClickListener {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d("section", "TextSection3+onCreateView3_"+this);
        View inflate = inflater.inflate(R.layout.fragment_section3, container, false);
        return inflate;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        Log.d("section", "TextSection3+onAttach3_"+this);
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d("section", "TextSection3+onResume3_"+this);
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d("section", "TextSection3+onStart3_"+this);
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d("section", "TextSection3+onStop3_"+this);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.d("section", "TextSection3+onDetch3_"+this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("section", "TextSection3+onDestroy3_"+this);
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d("section", "TextSection3+onPause3_"+this);
    }

    @Override
    public void onClick(View v) {

    }
}
