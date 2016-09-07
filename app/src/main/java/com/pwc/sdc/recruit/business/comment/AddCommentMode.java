package com.pwc.sdc.recruit.business.comment;

import com.google.gson.Gson;
import com.pwc.sdc.recruit.PwcApplication;
import com.pwc.sdc.recruit.base.BaseModel;
import com.pwc.sdc.recruit.config.AppConfig;
import com.pwc.sdc.recruit.data.model.Candidate;
import com.pwc.sdc.recruit.data.model.Recruiter;
import com.thirdparty.proxy.net.http.retrofit.HttpResponse;
import com.thirdparty.proxy.net.http.retrofit.rx.HttpSubscriber;

import java.util.ArrayList;

import rx.Observable;

/**
 * @author:dongpo 创建时间: 7/12/2016
 * 描述:
 * 修改:
 */
public class AddCommentMode extends BaseModel {

    public void getCandidate(Observable<HttpResponse<Candidate>> request, final HttpSubscriber<Candidate> callback) {
        if (AppConfig.MODE_CONNECTION) {
            sendHttpRequest(request, callback);
        } else {
            PwcApplication.getHandler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    callback.onSuccess(getCandidate());
                }
            }, AppConfig.DEBUG_WATING);
        }
    }

    public void uploadComment(Observable<HttpResponse<Object>> request, final HttpSubscriber<Object> callback) {
        if (AppConfig.MODE_CONNECTION) {
            sendHttpRequest(request, callback);
        } else {
            PwcApplication.getHandler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    callback.onSuccess(null);
                }
            }, AppConfig.DEBUG_WATING);
        }
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

    private Candidate getCandidate() {
        String json = "{\n" +
                "    \"basic\": {\n" +
                "        \"academicDegree\": \"大专\",\n" +
                "        \"birth\": \"1992/4/25\",\n" +
                "        \"chineseName\": \"张东方\",\n" +
                "        \"country\": \"中国\",\n" +
                "        \"currentAddress\": \"上海市闵行区七宝镇佳宝四村28幢602\",\n" +
                "        \"email\": \"asdong@163.com\",\n" +
                "        \"emergency\": {\n" +
                "            \"name\": \"吴燕\",\n" +
                "            \"phone\": \"15922060713\",\n" +
                "            \"relationship\": \"朋友\"\n" +
                "        },\n" +
                "        \"emplyeeReferral\": \"无\",\n" +
                "        \"englishName\": \"mInfoEtEnglishName\",\n" +
                "        \"gender\": \"先生\",\n" +
                "        \"graduateDate\": \"\",\n" +
                "        \"graduateSchool\": \"天津工业大学\",\n" +
                "        \"hasFriendsInPWC\": \"张安茂\",\n" +
                "        \"identityNo\": \"372928199206097219\",\n" +
                "        \"interests\": \"打羽毛球，玩游戏，写代码\",\n" +
                "        \"languageLevel\": {\n" +
                "            \"english\": \"TEM-8\",\n" +
                "            \"jspanese\": \"日语1级\",\n" +
                "            \"others\": \"\"\n" +
                "        },\n" +
                "        \"major\": \"纺织工程\",\n" +
                "        \"married\": \"\",\n" +
                "        \"medicalHistory\": \"有\",\n" +
                "        \"mobile\": \"18516585792\",\n" +
                "        \"passportNo\": \"EU66666666\",\n" +
                "        \"postCode\": \"387400\",\n" +
                "        \"takeService\": \"没有\",\n" +
                "        \"telephone\": \"0530-5460432\"\n" +
                "    },\n" +
                "    \"language\": \"chinese\",\n" +
                "    \"position\": {\n" +
                "        \"applyFor\": \"开发工程师\",\n" +
                "        \"availableDate\": \"2016/7/25\",\n" +
                "        \"experience\": \"2\",\n" +
                "        \"recruitChannel\": \"智联招聘\"\n" +
                "    },\n" +
                "    \"workExperiences\": [\n" +
                "        {\n" +
                "            \"companyName\": \"上海网络科技有限公司\",\n" +
                "            \"companyPhone\": \"020-5460311\",\n" +
                "            \"leaveReason\": \"想换一家氛围\",\n" +
                "            \"position\": \"软件工程师\",\n" +
                "            \"referee\": {\n" +
                "                \"name\": \"张三\",\n" +
                "                \"phone\": \"领导\",\n" +
                "                \"relationship\": \"15922060713\"\n" +
                "            },\n" +
                "            \"from\": \"2015/9/14\",\n" +
                "            \"to\": \"2015/8/14\"\n" +
                "        }\n" +
                "    ]\n" +
                "}";

        Gson gson = new Gson();
        return gson.fromJson(json, Candidate.class);
    }
}
