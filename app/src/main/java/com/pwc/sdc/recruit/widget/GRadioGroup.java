package com.pwc.sdc.recruit.widget;

/**
 * @author:dongpo 创建时间: 8/17/2016
 * 描述:
 * 修改:
 */

import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewParent;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import java.util.ArrayList;
import java.util.List;

public class GRadioGroup {

    public RadioButton mCheckedButton;
    private View mParent;

    public RadioButton getCheckedRadioButton() {
        return mCheckedButton;
    }

    public int getCheckedRadioButtonId(){
        return mCheckedButton.getId();
    }

    public String getCheckedRadioButtonDes(){
        return mCheckedButton.getText().toString();
    }

    public void check(int id){
        RadioButton checkView = (RadioButton) mParent.findViewById(id);
        onClick.onClick(checkView);
    }

    List<RadioButton> radios = new ArrayList<RadioButton>();

    /**
     * Constructor, which allows you to pass number of RadioButton instances,
     * making a group.
     *
     * @param radios One RadioButton or more.
     */
    public GRadioGroup(RadioButton... radios) {
        super();

        for (RadioButton rb : radios) {
            this.radios.add(rb);
            rb.setOnClickListener(onClick);
        }
    }

    /**
     * Constructor, which allows you to pass number of RadioButtons
     * represented by resource IDs, making a group.
     *
     * @param parent  Current View (or Activity) to which those RadioButtons
     *                  belong.
     * @param radiosIDs One RadioButton or more.
     */
    public GRadioGroup(View parent, int... radiosIDs) {
        super();
        mParent = parent;
        for (int radioButtonID : radiosIDs) {
            RadioButton rb = (RadioButton) parent.findViewById(radioButtonID);
            if (rb != null) {
                this.radios.add(rb);
                rb.setOnClickListener(onClick);
            }
        }
    }

    /**
     * This occurs everytime when one of RadioButtons is clicked,
     * and deselects all others in the group.
     */
    OnClickListener onClick = new OnClickListener() {

        @Override
        public void onClick(View v) {

            // let's deselect all radios in group
            for (RadioButton rb : radios) {

                ViewParent p = rb.getParent();
                if (p.getClass().equals(RadioGroup.class)) {
                    // if RadioButton belongs to RadioGroup,
                    // then deselect all radios in it
                    RadioGroup rg = (RadioGroup) p;
                    rg.clearCheck();
                } else {
                    // if RadioButton DOES NOT belong to RadioGroup,
                    // just deselect it
                    rb.setChecked(false);
                }
            }

            // now let's select currently clicked RadioButton
            if (v instanceof RadioButton) {
                RadioButton rb = (RadioButton) v;
                rb.setChecked(true);
                mCheckedButton = rb;
            }
        }
    };

    public boolean hasRadioButtonChecked() {
        return mCheckedButton != null;
    }

    public void setEnabled(boolean enabled) {
        for (int i = 0; i < radios.size(); i++) {
            radios.get(i).setEnabled(enabled);
        }
    }
}