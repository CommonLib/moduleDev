package com.pwc.sdc.recruit.data.remote;

import com.pwc.sdc.recruit.data.model.Candidate;
import com.pwc.sdc.recruit.data.model.CandidateId;
import com.pwc.sdc.recruit.data.model.Comment;
import com.pwc.sdc.recruit.data.model.Profile;
import com.pwc.sdc.recruit.data.model.RecruiterCandidate;
import com.pwc.sdc.recruit.data.model.User;
import com.pwc.sdc.recruit.data.model.api.DownloadAllParams;
import com.pwc.sdc.recruit.data.model.api.DownloadParams;
import com.pwc.sdc.recruit.data.model.api.Search;
import com.pwc.sdc.recruit.data.model.api.SearchParams;
import com.pwc.sdc.recruit.data.model.api.UploadRecruiter;
import com.thirdparty.proxy.net.http.retrofit.HttpResponse;

import java.util.List;
import java.util.Map;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PartMap;
import retrofit2.http.Query;
import rx.Observable;

/**
 * @author:dongpo 创建时间: 6/20/2016
 * 描述:
 * 修改:
 */
public interface BackPointService {
    @POST("login")
    Observable<HttpResponse<User>> login(@Body() User user);

    @POST("uploadResume")
    Observable<HttpResponse<CandidateId>> uploadResume(@Body() Candidate candidate);

    @Multipart
    @POST("uploadHead")
    Observable<HttpResponse<Void>> uploadHeader(@PartMap Map<String, RequestBody> params);

    @Multipart
    @POST("uploadSignedImage")
    Observable<HttpResponse<Void>> uploadSignedImage(@PartMap Map<String, RequestBody> params);

    @GET("profiles")
    Observable<HttpResponse<List<Profile>>> profiles(@Query("page") int page,
                                                     @Query("count") int count);

    //缓存策略：10分钟
    @Headers("Cache-Control: public, max-age=600")
    @GET("employee")
    Observable<HttpResponse<List<RecruiterCandidate>>> employee(@Query("page") int page,
                                                                @Query("count") int count);

    @POST("assign")
    Observable<HttpResponse<Void>> uploadRecruiter(@Body() UploadRecruiter recruiters);

    //缓存策略：1周
    @Headers("Cache-Control: public, max-age=604800")
    @GET("resume")
    Observable<HttpResponse<Candidate>> getCandidate(@Query("candidateId") String candidateId);

    @POST("comment")
    Observable<HttpResponse<Object>> uploadComment(@Body() Comment comment);

    @POST("search")
    Observable<HttpResponse<List<Profile>>> searchCandidate(@Body() Search search);

    //缓存策略：10分钟
    @Headers("Cache-Control: public, max-age=600")
    @GET("searchEmployee")
    Observable<HttpResponse<List<RecruiterCandidate>>> searchEmployee(
            @Query("name") String employeeName);

    @Multipart
    @POST("uploadErrLog")
    Observable<HttpResponse<Void>> uploadCrashReport(@PartMap Map<String, RequestBody> params);

    @GET("logout")
    Observable<HttpResponse<Void>> logout();

    @POST("download")
    Observable<ResponseBody> download(@Body() DownloadParams downloadParams);

    @POST("downloadAll")
    Observable<ResponseBody> downloadAll(@Body() DownloadAllParams downloadParams);

    @POST("searchCandidate")
    Observable<ResponseBody> webSearchCandidate(@Body() SearchParams downloadParams,
                                                @Query("page") int page, @Query("count") int count);
}
