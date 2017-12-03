package zipkin;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.smoketurner.dropwizard.zipkin.ConsoleZipkinWithHeadersFactory;
import com.smoketurner.dropwizard.zipkin.ZipkinFactory;
import io.dropwizard.Configuration;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

public class DropwizardZipkinConfiguration extends Configuration {

    @Valid
    @NotNull
    public final ZipkinFactory zipkinFactory = new ConsoleZipkinWithHeadersFactory();

    @JsonProperty
    public ZipkinFactory getZipkinFactory() {
        return zipkinFactory;
    }

}
