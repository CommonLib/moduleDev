package com.pwc.sdc.recruit.data.model.api;

import com.pwc.sdc.recruit.data.model.Recruiter;

import java.util.ArrayList;
import java.util.List;

/**
 * @author:dongpo 创建时间: 8/12/2016
 * 描述:
 * 修改:
 */
public class UploadRecruiter {
    private String candidateId;
    private List<Guid> recruiters = new ArrayList<>();

    public UploadRecruiter(String candidateId, List<Recruiter> recruiters) {
        this.candidateId = candidateId;
        for (int i = 0; i < recruiters.size(); i++) {
            Recruiter recruiter = recruiters.get(i);
            if(!recruiter.isInterview){
                Guid guid = new Guid();
                guid.guid = recruiter.guid;
                this.recruiters.add(guid);
            }
        }
    }
}
