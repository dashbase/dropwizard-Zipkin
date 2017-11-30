package zipkin.resources;

import brave.http.HttpTracing;
import brave.okhttp3.TracingInterceptor;
import com.codahale.metrics.annotation.Timed;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.Dispatcher;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.net.URL;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@Path("/")
@Produces(MediaType.APPLICATION_JSON)
public class HelloWorldResource {
    private static final ObjectMapper mapper = new ObjectMapper();
    private OkHttpClient client;
    private Retrofit restAdator;

    public HelloWorldResource(HttpTracing tracing) throws Exception {

        //============== Add tracing to okhttp3 client ================
        client = new OkHttpClient.Builder()
                .addInterceptor(chain -> {
                    Request original = chain.request();

                    Request request = original.newBuilder()
                            .header("client", "myapp")
                            .build();
                    return chain.proceed(request);
                })
                .dispatcher(new Dispatcher(
                        tracing.tracing().currentTraceContext().executorService(new Dispatcher().executorService())
                ))
                .addNetworkInterceptor(TracingInterceptor.create(tracing))
                .build();
        //=============================================================

        URL url = new URL("http://127.0.0.1:8081/");
        restAdator = new Retrofit.Builder().baseUrl(HttpUrl.get(url))
                .addConverterFactory(JacksonConverterFactory.create(mapper))
                .client(client).build();

        FetchService svc = restAdator.create(FetchService.class);
        svc.fetch().execute();
    }

    @GET
    @Timed
    @Path("/query")
    public MyResponse query()
            throws Exception {
        final Random random = new Random();
        TimeUnit.MILLISECONDS.sleep(random.nextInt(1000));
        FetchService svc = restAdator.create(FetchService.class);
        return svc.fetch().execute().body();
    }

    @GET
    @Timed
    @Path("/fetch")
    public MyResponse fetch() throws Exception {
        final Random random = new Random();
        TimeUnit.MILLISECONDS.sleep(random.nextInt(1000));
        ResultService svc = restAdator.create(ResultService.class);
        return svc.getResult().execute().body();
    }

    @GET
    @Timed
    @Path("/get_result")
    public MyResponse result() throws InterruptedException {
        final Random random = new Random();
        TimeUnit.MILLISECONDS.sleep(random.nextInt(1000));
        return new MyResponse("This is content.");
    }
}