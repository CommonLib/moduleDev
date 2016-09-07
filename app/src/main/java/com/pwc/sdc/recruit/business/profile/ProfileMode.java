package com.pwc.sdc.recruit.business.profile;

import android.text.TextUtils;

import com.pwc.sdc.recruit.PwcApplication;
import com.pwc.sdc.recruit.R;
import com.pwc.sdc.recruit.base.BaseModel;
import com.pwc.sdc.recruit.config.AppConfig;
import com.pwc.sdc.recruit.data.model.Comment;
import com.pwc.sdc.recruit.data.model.Profile;
import com.pwc.sdc.recruit.data.model.Recruiter;
import com.pwc.sdc.recruit.data.model.RecruiterCandidate;
import com.thirdparty.proxy.net.http.retrofit.HttpResponse;
import com.thirdparty.proxy.net.http.retrofit.rx.HttpSubscriber;
import com.thirdparty.proxy.utils.StringUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import rx.Observable;
import rx.Subscription;

/**
 * @author:dongpo 创建时间: 7/12/2016
 * 描述:
 * 修改:
 */
public class ProfileMode extends BaseModel {
    int currentPage = 1;

    public void requestProfiles(Observable<HttpResponse<List<Profile>>> request, final HttpSubscriber<List<Profile>> callBack) {
        if (AppConfig.MODE_CONNECTION) {
            sendHttpRequest(request, callBack);
        } else {
            PwcApplication.getHandler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    List<Profile> originList = getOriginList();
                    Collections.sort(originList);
                    callBack.onSuccess(originList);
                }
            }, AppConfig.DEBUG_WATING);
        }
    }

    public void requestLoadMore(Observable<HttpResponse<List<Profile>>> request, final HttpSubscriber<List<Profile>> callBack) {
        if (AppConfig.MODE_CONNECTION) {
            sendHttpRequest(request, callBack);
        } else {
            PwcApplication.getHandler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    List<Profile> list = getList();
                    Collections.sort(list);
                    callBack.onSuccess(list);
                }
            }, AppConfig.DEBUG_WATING);
        }


    }

    public Subscription uploadRecruiter(Observable<HttpResponse<Void>> request, final HttpSubscriber<Void> callBack) {
        if (AppConfig.MODE_CONNECTION) {
            return sendHttpRequest(request, callBack);
        } else {
            PwcApplication.getHandler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    callBack.onSuccess(null);
                }
            }, AppConfig.DEBUG_WATING);
        }
        return null;
    }

    public void search(Observable<HttpResponse<List<Profile>>> request, final HttpSubscriber<List<Profile>> callBack) {
        if (AppConfig.MODE_CONNECTION) {
            sendHttpRequest(request, callBack);
        } else {
            PwcApplication.getHandler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    callBack.onSuccess(getSearchList());
                }
            }, AppConfig.DEBUG_WATING);
        }

    }

    public void employee(Observable<HttpResponse<List<RecruiterCandidate>>> request, final HttpSubscriber<List<RecruiterCandidate>> callBack) {
        if (AppConfig.MODE_CONNECTION) {
            sendHttpRequest(request, callBack);
        } else {
            PwcApplication.getHandler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    callBack.onSuccess(getRecruiterCandidate());
                }
            }, AppConfig.DEBUG_WATING);
        }

    }

    public void searchEmployee(Observable<HttpResponse<List<RecruiterCandidate>>> request, final HttpSubscriber<List<RecruiterCandidate>> callBack) {
        if (AppConfig.MODE_CONNECTION) {
            sendHttpRequest(request, callBack);
        } else {
            PwcApplication.getHandler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    callBack.onSuccess(getRecruiterCandidate());
                }
            }, AppConfig.DEBUG_WATING);
        }
    }

    public String getSearchEmptyDes(String keyword) {
        StringBuilder sb = new StringBuilder();
        sb.append(getApplicationContext().getString(R.string.no_search_result_front)).append(" \"").append(keyword).append("\" ").append(getApplicationContext().getString(R.string.no_search_result_end));
        return sb.toString();
    }

    public String getEmptyDes() {
       return getApplicationContext().getString(R.string.no_more_data);
    }

    /**
     * @param origin
     * @return 筛选面试官，将已经超过的时间的面试官出掉
     */
    public ArrayList<Recruiter> filterRecruiter(ArrayList<Recruiter> origin) {
        ArrayList<Recruiter> newList = new ArrayList<>();
        newList.addAll(origin);
        for (int i = 0; i < newList.size(); i++) {
            Recruiter recruiter = newList.get(i);
            if (recruiter.isCommented()) {
                newList.remove(recruiter);
                i--;
            }
        }
        return newList;
    }

    public List<Profile> getOriginList() {
        List<Profile> list = new ArrayList<>();
        for (int i = 0; i < 30; i++) {
            Profile profile = new Profile();
            profile.chineseName = "CN_new" + i;
            profile.englishName = "EN" + i;
            profile.phone = "Phone" + i;
            if (i % 2 == 0) {
                profile.recruiters = new ArrayList<>();
                Recruiter recruiter = new Recruiter();
                recruiter.name = "Recruiter_more" + i;
                recruiter.comment = new Comment();
                recruiter.comment.interViewTime = StringUtils.getDateString(new Date());
                profile.recruiters.add(recruiter);
                recruiter.comment.comment = "comment" + i;

            } else {
                profile.recruiters = new ArrayList<>();
            }

            if (i <= 15) {
                profile.submitTime = System.currentTimeMillis();
            } else {
                Calendar instance = Calendar.getInstance();
                instance.set(2016, 06, 28, 14, 40, 30);
                profile.submitTime = instance.getTimeInMillis();
            }
            profile.position = "position" + i;
            list.add(profile);
        }
        return list;
    }

    public List<Profile> getSearchList() {
        List<Profile> list = new ArrayList<>();
        for (int i = 0; i < 30; i++) {
            Profile profile = new Profile();
            profile.chineseName = "SearchCN" + i;
            profile.englishName = "SearchEN" + i;
            profile.phone = "SearchPhone" + i;
            if (i % 2 == 0) {
                profile.recruiters = new ArrayList<>();
                Recruiter recruiter = new Recruiter();
                recruiter.name = "SearchRecruiter_more" + i;
                recruiter.comment = new Comment();
                recruiter.comment.interViewTime = StringUtils.getDateString(new Date());
                profile.recruiters.add(recruiter);
                recruiter.comment.comment = "Search_comments" + i;

            } else {
                profile.recruiters = new ArrayList<>();
            }

            if (i <= 15) {
                profile.submitTime = System.currentTimeMillis();
            } else {
                Calendar instance = Calendar.getInstance();
                instance.set(2016, 06, 28, 14, 40, 30);
                profile.submitTime = instance.getTimeInMillis();
            }
            profile.position = "Search_position" + i;
            list.add(profile);
        }
        return list;
    }

    public List<Profile> getList() {
        currentPage++;
        List<Profile> list = new ArrayList<>();
        for (int i = 0; i < 30; i++) {
            Profile profile = new Profile();
            profile.chineseName = "CN_more" + i;
            profile.englishName = "EN_more" + i;
            profile.phone = "Phone_more" + i;
            if (i % 2 == 0) {
                profile.recruiters = new ArrayList<>();
                Recruiter recruiter = new Recruiter();
                recruiter.name = "Recruiter_more" + i;
                recruiter.comment = new Comment();
                recruiter.comment.interViewTime = StringUtils.getDateString(new Date());
                profile.recruiters.add(recruiter);
                recruiter.comment.comment = "comment" + i;
            } else {
                profile.recruiters = new ArrayList<>();
            }

            Calendar instance = Calendar.getInstance();
            instance.set(2016, 06, 25 - currentPage, 14, 40, 30);
            profile.submitTime = instance.getTimeInMillis();

            profile.position = "position" + i;
            list.add(profile);
        }
        return list;
    }

    public List<RecruiterCandidate> getRecruiterCandidate() {
        List<RecruiterCandidate> candidates = new ArrayList<>();
        for (int i = 0; i < 30; i++) {
            RecruiterCandidate candidate = new RecruiterCandidate();
            candidate.name = "candidate" + i;
            candidate.department = "IMAT";
            candidate.guid = "example" + i;
            candidates.add(candidate);
        }
        return candidates;
    }

    public List<Object> getTimeZone(List<Profile> profiles) {
        List<Object> profileTimes = new ArrayList<>();
        for (int i = 0; i < profiles.size(); i++) {
            Profile profile = profiles.get(i);
            Date date = new Date(profile.submitTime);
            String days = StringUtils.getDateString(date);
            profile.time = days;
            if (i == 0) {
                profileTimes.add(days);
            } else {
                String lastProfileTime = profiles.get(i - 1).time;
                if (!TextUtils.equals(lastProfileTime, days)) {
                    profileTimes.add(days);
                }
            }
            profileTimes.add(profile);
        }
        return profileTimes;
    }

}
