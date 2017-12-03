package com.smoketurner.dropwizard.zipkin;

import java.util.Optional;
import javax.annotation.Nonnull;
import org.hibernate.validator.constraints.NotEmpty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.smoketurner.dropwizard.zipkin.managed.ReporterManager;
import com.smoketurner.dropwizard.zipkin.metrics.DropwizardReporterMetrics;
import brave.http.HttpTracing;
import io.dropwizard.setup.Environment;
import zipkin.Span;
import zipkin.reporter.AsyncReporter;
import zipkin.reporter.ReporterMetrics;
import zipkin.reporter.urlconnection.URLConnectionSender;

@JsonTypeName("http_with_headers")
public class HttpZipkinWithHeadersFactory extends AbstractZipkinWithHeadersFactory {

    private static final Logger LOGGER = LoggerFactory
            .getLogger(HttpZipkinWithHeadersFactory.class);

    @NotEmpty
    private String baseUrl = "http://127.0.0.1:9411/";

    @JsonProperty
    public String getBaseUrl() {
        return baseUrl;
    }

    @JsonProperty
    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    /**
     * Build a new {@link HttpTracing} instance for interfacing with Zipkin
     *
     * @param environment
     *            Environment
     * @return HttpTracing instance
     */
    @Override
    public Optional<HttpTracing> build(@Nonnull final Environment environment) {
        if (!isEnabled()) {
            LOGGER.warn("Zipkin tracing is disabled");
            return Optional.empty();
        }

        final ReporterMetrics metricsHandler = new DropwizardReporterMetrics(
                environment.metrics());

        final URLConnectionSender sender = URLConnectionSender
                .create(baseUrl + "api/v1/spans");

        final AsyncReporter<Span> reporter = AsyncReporter.builder(sender)
                .metrics(metricsHandler).build();

        environment.lifecycle().manage(new ReporterManager(reporter, sender));

        LOGGER.info("Sending spans to HTTP collector at: {}", baseUrl);

        return buildTracing(environment, reporter);
    }
}
