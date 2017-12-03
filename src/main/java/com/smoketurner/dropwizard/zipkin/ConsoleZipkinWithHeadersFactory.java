package com.smoketurner.dropwizard.zipkin;

import java.util.Optional;
import javax.annotation.Nonnull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.fasterxml.jackson.annotation.JsonTypeName;
import brave.http.HttpTracing;
import io.dropwizard.setup.Environment;
import zipkin.reporter.Reporter;

@JsonTypeName("console_with_headers")
public class ConsoleZipkinWithHeadersFactory extends AbstractZipkinWithHeadersFactory {

    private static final Logger LOGGER = LoggerFactory
            .getLogger(ConsoleZipkinWithHeadersFactory.class);

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

        LOGGER.info("Sending spans to console");
        return buildTracing(environment, Reporter.CONSOLE);
    }
}
