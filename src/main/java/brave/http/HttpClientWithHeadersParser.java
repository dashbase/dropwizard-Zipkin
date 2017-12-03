package brave.http;

import brave.SpanCustomizer;

import javax.annotation.Nullable;

/**
 * Parses the request and response into reasonable defaults for http client spans. Subclass to
 * customize, for example, to add tags based on response headers.
 */
public class HttpClientWithHeadersParser extends HttpClientParser {

  /**
   * Customizes the span based on the request that will be sent to the server.
   * <p>
   * <p>{@inheritDoc}
   */
  @Override
  public <Req> void request(HttpAdapter<Req, ?> adapter, Req req,
                            SpanCustomizer customizer) {
    super.request(adapter, req, customizer);

    headerToTag("caller-ip", "dashbase.meta.remoteid", adapter, req, customizer);
    headerToTag("user-agent", "dashbase.meta.agent", adapter, req, customizer);
  }

  private <Req> void headerToTag(String headerName, String tagName, HttpAdapter<Req, ?> adapter, Req req, SpanCustomizer customizer) {
    String v = adapter.requestHeader(req, headerName);
    if (v != null)
      customizer.tag(tagName, adapter.requestHeader(req, v));
  }

  /**
   * Customizes the span based on the response received from the server.
   * <p>
   * <p>{@inheritDoc}
   */
  @Override
  public <Resp> void response(HttpAdapter<?, Resp> adapter, @Nullable Resp res,
                              @Nullable Throwable error, SpanCustomizer customizer) {
    super.response(adapter, res, error, customizer);
  }
}
