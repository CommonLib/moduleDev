package com.pwc.sdc.recruit.business.main;

import com.pwc.sdc.recruit.base.BaseModel;
import com.thirdparty.proxy.log.TLog;

import java.util.List;

/**
 * @author:dongpo 创建时间: 2016/6/25
 * 描述:
 * 修改:
 */
public class MainMode extends BaseModel{

    public String dealWithValue(String value) {
        TLog.d("MainMode+dealWithValue + Model完成处理");
        return "dealed";
    }

    public List<String> getData(){
        return null;
    }
}
