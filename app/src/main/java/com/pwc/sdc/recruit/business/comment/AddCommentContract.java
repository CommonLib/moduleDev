package com.pwc.sdc.recruit.business.comment;

import com.pwc.sdc.recruit.base.interf.ViewLayer;
import com.pwc.sdc.recruit.business.info.resume.InfoConstract;
import com.pwc.sdc.recruit.data.model.Candidate;
import com.pwc.sdc.recruit.data.model.Comment;
import com.pwc.sdc.recruit.data.model.Recruiter;

import java.util.ArrayList;

/**
 * @author:dongpo 创建时间: 7/12/2016
 * 描述:
 * 修改:
 */
public interface AddCommentContract {

    interface View extends ViewLayer{
        void showCandidate(Candidate candidate);
        Comment getComment();
        void showInterViewer(ArrayList<Recruiter> recruiters);
        boolean checkCommentEmpty();
        void showAlertDialog();
    }

    interface Presenter extends InfoConstract.Presenter{
        void requestCandidate();
    }
}
