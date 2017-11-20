package zipkin.resources;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface FetchService {
    String V1_PATH_QUERY = "/fetch";

    @GET(V1_PATH_QUERY)
    Call<MyResponse> fetch() throws Exception;
}
