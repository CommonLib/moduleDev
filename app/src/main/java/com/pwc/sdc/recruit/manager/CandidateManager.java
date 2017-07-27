package com.pwc.sdc.recruit.manager;

import android.graphics.Bitmap;

import com.pwc.sdc.recruit.data.model.Candidate;
import com.thirdparty.proxy.utils.BitmapUtils;

import java.io.File;

/**
 * @author:dongpo 创建时间: 8/5/2016
 * 描述:
 * 修改:
 */
public class CandidateManager {
    private static CandidateManager mInstance = new CandidateManager();
    private Candidate mCandidate;
    //在磁盘上的File目录
    private File mHeaderFile;
    private Bitmap mHeader;

    private CandidateManager() {
    }

    /**
     * 单一实例
     */
    public static CandidateManager getInstance() {
        return mInstance;
    }

    public void setCandidate(Candidate candidate) {
        mCandidate = candidate;
    }

    public Candidate getCandidate() {
        return mCandidate;
    }

    public File getHeaderFile() {
        return mHeaderFile;
    }

    public void setHeaderFile(File headerFile) {
        if (headerFile != null) {
            mHeaderFile = headerFile;
        }
    }

    public Bitmap getHeader() {
        return mHeader;
    }

    public void setHeader(Bitmap bitmap) {
        if (bitmap != null) {
            mHeader = bitmap;
        }
    }


    public void clear() {
        mCandidate = null;
        BitmapUtils.doRecycledIfNot(mHeader);
        mHeader = null;
        mHeaderFile = null;
    }

    public void recycleHeader() {
        if (mHeader != null) {
            BitmapUtils.doRecycledIfNot(mHeader);
        }
    }
}
