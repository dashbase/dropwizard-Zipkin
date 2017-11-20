package zipkin;

import brave.http.HttpTracing;
import com.smoketurner.dropwizard.zipkin.ZipkinBundle;
import com.smoketurner.dropwizard.zipkin.ZipkinFactory;
import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import zipkin.resources.HelloWorldResource;

import java.io.IOException;
import java.util.Optional;

public class DropwizardZipkinApplication extends Application<DropwizardZipkinConfiguration> {

    public static void main(final String[] args) throws Exception {
        new DropwizardZipkinApplication().run(args);
    }

    @Override
    public String getName() {
        return "DropwizardZipkin";
    }

    @Override
    public void initialize(final Bootstrap<DropwizardZipkinConfiguration> bootstrap) {
        //===================== add zipkin bundle ===================
        String serviceName = "dropwizard-zipkin"; // it will be used as service name in zipkin
        bootstrap.addBundle(new ZipkinBundle<DropwizardZipkinConfiguration>(serviceName) {
            @Override
            public ZipkinFactory getZipkinFactory(DropwizardZipkinConfiguration configuration) {
                return configuration.getZipkinFactory();
            }
        });
        //===========================================================
    }

    @Override
    public void run(final DropwizardZipkinConfiguration configuration,
                    final Environment environment) throws IOException {
        //=================== register tracing ========================
        final Optional<HttpTracing> tracing = configuration.getZipkinFactory().build(environment);
        if (tracing.isPresent()) {
            final HelloWorldResource resource = new HelloWorldResource(tracing.get());
            environment.jersey().register(resource);
        }
        //=============================================================
    }
}
