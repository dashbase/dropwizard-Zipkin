package zipkin.resources;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ResultService {
    String V1_PATH_QUERY = "/get_result";

    @GET(V1_PATH_QUERY)
    Call<MyResponse> getResult() throws Exception;
}
