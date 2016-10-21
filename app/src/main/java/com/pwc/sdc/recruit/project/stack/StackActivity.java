package com.pwc.sdc.recruit.project.stack;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.pwc.sdc.recruit.R;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author:burt.yang create: 10/21/2016
 * description:
 * revise:
 */
public class StackActivity extends AppCompatActivity {

    private FragmentStack mStack;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stack);
        ButterKnife.bind(this);
        mStack = FragmentStack.create(this, R.id.stack_container);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    int count  = 0;

    @OnClick({R.id.stack_push, R.id.stack_replace_top, R.id.stack_replace, R.id.stack_pop})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.stack_push:
                count ++;
                if(count % 2 == 0){
                    Fragment2 fragment1 = new Fragment2();
                    mStack.push(fragment1, "fragment2");
                }else{
                    Fragment1 fragment1 = new Fragment1();
                    mStack.push(fragment1, "fragment1");
                }
                break;
            case R.id.stack_replace_top:
                Fragment3 fragment3 = new Fragment3();
                mStack.replaceTop(fragment3, "fragment3");
                break;
            case R.id.stack_replace:
                Fragment3 fragment4 = new Fragment3();
                mStack.replace(fragment4, "fragment3");
                break;
            case R.id.stack_pop:
                mStack.pop();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        if(mStack.hasBackStack()){
            mStack.pop();
        }else{
            super.onBackPressed();
        }
    }
}
