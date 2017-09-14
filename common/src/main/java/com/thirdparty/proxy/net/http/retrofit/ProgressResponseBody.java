package com.thirdparty.proxy.net.http.retrofit;

import android.os.Message;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;
import okio.ForwardingSource;
import okio.Okio;
import okio.Source;

/**
 * @author:dongpo 创建时间: 8/2/2016
 * 描述:
 * 修改:
 */
public class ProgressResponseBody extends ResponseBody {

    //实际的待包装响应体
    private final ResponseBody responseBody;
    private final ProgressHandler mHandler;
    //包装完成的BufferedSource
    private BufferedSource bufferedSource;

    /**
     * 构造函数，赋值
     *
     * @param responseBody 待包装的响应体
     * @param
     */
    public ProgressResponseBody(ResponseBody responseBody, DownloadProgressListener listener) {
        this.responseBody = responseBody;
        mHandler = new ProgressHandler(listener);
    }

    /**
     * 重写调用实际的响应体的contentType
     *
     * @return MediaType
     */
    @Override
    public MediaType contentType() {
        return responseBody.contentType();
    }

    /**
     * 重写调用实际的响应体的contentLength
     *
     * @return contentLength
     * @throws IOException 异常
     */
    @Override
    public long contentLength() {
        return responseBody.contentLength();
    }

    /**
     * 重写进行包装source
     *
     * @return BufferedSource
     * @throws IOException 异常
     */
    @Override
    public BufferedSource source() {
        if (bufferedSource == null) {
            //包装
            bufferedSource = Okio.buffer(source(responseBody.source()));
        }
        return bufferedSource;
    }

    /**
     * 读取，回调进度接口
     *
     * @param source Source
     * @return Source
     */
    private Source source(Source source) {

        return new ForwardingSource(source) {
            private long contentLength;
            //当前读取字节数
            long totalBytesRead = 0L;
            private long lastRefreshUiTime = 0L;  //最后一次刷新的时间
            private long lastWriteBytes = 0L;     //最后一次写入字节数据

            @Override
            public long read(Buffer sink, long byteCount) throws IOException {
                long bytesRead = super.read(sink, byteCount);
                if (contentLength == 0) {
                    //获得contentLength的值，后续不再调用
                    contentLength = contentLength();
                }
                //增加当前读取的字节数，如果读取完成了bytesRead会返回-1
                totalBytesRead += bytesRead != -1 ? bytesRead : 0;
                long curTime = System.currentTimeMillis();
                //每200毫秒刷新一次数据
                if (curTime - lastRefreshUiTime >= 200 || totalBytesRead == contentLength) {
                    //计算下载速度
                    long diffTime = (curTime - lastRefreshUiTime) / 1000;
                    if (diffTime == 0) diffTime += 1;
                    long diffBytes = totalBytesRead - lastWriteBytes;
                    long networkSpeed = diffBytes / diffTime;

                    ProgressInfo info = new ProgressInfo();
                    info.totalSize = contentLength;
                    info.current = totalBytesRead;
                    info.isDone = bytesRead == -1;
                    info.networkSpeed = networkSpeed;
                    Message message = mHandler.obtainMessage();
                    message.what = ProgressHandler.TYPE_DOWNLOAD;
                    message.obj = info;
                    message.sendToTarget();

                    lastRefreshUiTime = System.currentTimeMillis();
                    lastWriteBytes = totalBytesRead;
                }
                return bytesRead;
            }
        };
    }
}
