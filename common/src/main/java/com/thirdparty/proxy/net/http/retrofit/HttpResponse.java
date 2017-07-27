package com.thirdparty.proxy.net.http.retrofit;

import java.io.Serializable;

/**
 * @author:dongpo 创建时间: 7/27/2016
 * 描述:
 * 修改:
 */
public class HttpResponse<T> implements Serializable{
    public int resultCode;
    public String message;
    public T data;
}
