package com.pwc.sdc.recruit.data.remote;

import com.pwc.sdc.recruit.config.AppConfig;
import com.pwc.sdc.recruit.constants.Constants;
import com.pwc.sdc.recruit.data.model.Candidate;
import com.pwc.sdc.recruit.data.model.Profile;
import com.pwc.sdc.recruit.data.model.RecruiterCandidate;
import com.pwc.sdc.recruit.data.model.User;
import com.pwc.sdc.recruit.data.model.api.DownloadAllParams;
import com.pwc.sdc.recruit.data.model.api.DownloadParams;
import com.pwc.sdc.recruit.data.model.api.Search;
import com.pwc.sdc.recruit.data.model.api.SearchParams;
import com.pwc.sdc.recruit.manager.AccountManager;
import com.thirdparty.proxy.net.http.ApiException;
import com.thirdparty.proxy.net.http.retrofit.HttpResponse;
import com.thirdparty.proxy.net.http.retrofit.rx.HttpSubscriber;
import com.thirdparty.proxy.utils.CyptoUtils;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import rx.Observable;
import rx.Subscription;
import rx.functions.Func1;

/**
 * Created by byang059 on 8/14/17.
 */
public class RetrofitHelperTest {

    private BackPointService mApiService;

    @After
    public void after() throws Exception {
        logOut();
    }

    @Before
    public void before() throws Exception {
        RetrofitHelper retrofitHelper = RetrofitHelper.getInstance();
        retrofitHelper.setBaseUrl(AppConfig.DEV_ENV_URL);
        mApiService = retrofitHelper.getService();
        login();
    }

    private void login() throws Exception {
        User user = new User();
        user.userName = "admin";
        user.password = CyptoUtils.md5Encode(user.userName);
        Observable<HttpResponse<User>> loginApi = mApiService.login(user);
        request(loginApi, new TestHttpSubscriber<User>() {
            @Override
            public void onSuccess(User user) {
                super.onSuccess(user);
                AccountManager.getInstance().login(user);
            }
        });
    }

    private void logOut() throws Exception {
        Observable<HttpResponse<Void>> logout = mApiService.logout();
        request(logout, new TestHttpSubscriber<Void>());
    }

    @Test
    public void testProfiles() throws Exception {
        Observable<HttpResponse<List<Profile>>> profilesApi = mApiService.profiles(1, 10);
        request(profilesApi, new TestHttpSubscriber<List<Profile>>());
    }

    @Test
    public void testEmployee() throws Exception {
        Observable<HttpResponse<List<RecruiterCandidate>>> employeeApi = mApiService
                .employee(1, 10);
        request(employeeApi, new TestHttpSubscriber<List<RecruiterCandidate>>());
    }

    @Test
    public void testGetCandidate() throws Exception {
        Observable<HttpResponse<Candidate>> candidateApi = mApiService.getCandidate("87");
        request(candidateApi, new TestHttpSubscriber<Candidate>());
    }

    @Test
    public void testSearchCandidate() throws Exception {
        Search search = new Search();
        search.keyword = "zhang";
        search.field = Constants.SEARCH_FIELD_PINYIN;
        Observable<HttpResponse<List<Profile>>> searchCandidate = mApiService
                .searchCandidate(search);
        request(searchCandidate, new TestHttpSubscriber<List<Profile>>());
    }

    @Test
    public void testSearchEmployee() throws Exception {
        Observable<HttpResponse<List<RecruiterCandidate>>> searchEmployee = mApiService
                .searchEmployee("nathan");
        request(searchEmployee, new TestHttpSubscriber<List<RecruiterCandidate>>());
    }

    @Test
    public void testWebDownloadPDF() throws Exception {
        RetrofitHelper helper = RetrofitHelper.getInstance();
        helper.setWebBaseUrl(AppConfig.DEV_ENV_URL);
        BackPointService service = helper.getService();

        DownloadParams downloadParams = new DownloadParams();
        ArrayList<DownloadParams.DataBean> data = new ArrayList<>();
        DownloadParams.DataBean dataBean = new DownloadParams.DataBean();
        dataBean.setCandidateId(106);
        data.add(dataBean);
        downloadParams.setData(data);

        Observable<ResponseBody> download = service.download(downloadParams);
        download.subscribe(new TestHttpSubscriber<ResponseBody>());
    }

    @Test
    public void testWebDownloadAll() throws Exception {
        RetrofitHelper helper = RetrofitHelper.getInstance();
        helper.setWebBaseUrl(AppConfig.DEV_ENV_URL);
        BackPointService service = helper.getService();

        DownloadAllParams params = new DownloadAllParams();
        params.setDownloadType("excel");
        DownloadAllParams.NewComplexSearchBean newComplexSearch = new DownloadAllParams.NewComplexSearchBean();
        newComplexSearch.setChineseName("张");
        params.setNewComplexSearch(newComplexSearch);

        Observable<ResponseBody> download = service.downloadAll(params);
        download.subscribe(new TestHttpSubscriber<ResponseBody>());
    }

    @Test
    public void testWebSearchCandidate() throws Exception {
        RetrofitHelper helper = RetrofitHelper.getInstance();
        helper.setWebBaseUrl(AppConfig.DEV_ENV_URL);
        BackPointService service = helper.getService();

        SearchParams params = new SearchParams();
        /**
         {
         "chineseName" : "一二三",
         "englishName" : "ABC",
         "appliedPosition" : "工程师",
         "phone" : "123456789",
         "interviewDateStart" : 20170809,
         "interviewDateEnd" : 20170809,
         "hrResult" : 1
         }


         */
        params.setAppliedPosition("工程师");
        params.setChineseName("李卫");
        params.setEnglishName("wei");
        params.setInterviewDate(20170805);


        Observable<ResponseBody> webSearchCandidate = service.webSearchCandidate(params, 1, 8);
        webSearchCandidate.subscribe(new TestHttpSubscriber<ResponseBody>());
    }

    private static <T> Subscription request(Observable<HttpResponse<T>> observable,
                                            HttpSubscriber<T> callBack) {
        return observable.flatMap(new Func1<HttpResponse<T>, Observable<T>>() {
            @Override
            public Observable<T> call(HttpResponse<T> tHttpResponse) {
                if (tHttpResponse.resultCode == 0) {
                    return Observable.just(tHttpResponse.data);
                } else {
                    return Observable.error(new ApiException(tHttpResponse.resultCode,
                            tHttpResponse.message));
                }
            }
        }).subscribe(callBack);
    }

    private class TestHttpSubscriber<T> extends HttpSubscriber<T> {

        @Override
        public void onSuccess(T t) {
            Assert.assertTrue(true);
        }

        @Override
        public void onFailure(int errorCode, String message) {
            Assert.assertTrue(false);
        }
    }
}