package se.bettercode.mockserver;

import static org.mockserver.matchers.Times.unlimited;
import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.mockserver.client.server.MockServerClient;
import org.mockserver.model.Header;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;

import java.io.IOException;

@Slf4j
public class Main {

  public static final Header HEADER_TEXT_HTML_UTF8 = new Header("Content-Type", "text/html; charset=utf-8");
  public static final Header HEADER_CACHE_CONTROL_PUBLIC_86400 = new Header("Cache-Control", "public, max-age=86400");

  /**
   * Run with: `./gradlew run`
   */
  public static void main(String args[]) {
    final String host = "localhost";
    final int port = 1080;
    MockServerClient client = new MockServerClient(host, port);
    client.reset();
    createExpectations(client);
  }

  /**
   * Sets up some sample expectations on the server
   */
  private static void createExpectations(MockServerClient client) {
    unavailable(client);
    unauthorized(client);
    welcomePage(client);
  }

  private static void welcomePage(MockServerClient mockClient) {
    mockClient
      .when(
        request()
          .withMethod("GET")
          .withPath("")
        , unlimited()
      )
      .respond(
        response()
          .withStatusCode(200)
          .withHeaders(
              HEADER_TEXT_HTML_UTF8,
              HEADER_CACHE_CONTROL_PUBLIC_86400
          )
          .withBody(readFile("index.html"))
      );
  }

  private static void unavailable(MockServerClient mockClient) {
    mockClient
      .when(
        request()
          .withMethod("POST")
          .withPath("/mock/unavailable")
        , unlimited()
      )
      .respond(
        response()
          .withStatusCode(200)
          .withHeaders(HEADER_TEXT_HTML_UTF8, HEADER_CACHE_CONTROL_PUBLIC_86400)
          .withBody("<html><body>Unavailable</body></html>")
      );
  }

  private static void unauthorized(MockServerClient mockClient) {
    mockClient
      .when(
        request()
          .withMethod("POST")
          .withPath("")
        , unlimited()
      )
      .respond(
        response()
          .withStatusCode(401)
          .withHeaders(HEADER_TEXT_HTML_UTF8, HEADER_CACHE_CONTROL_PUBLIC_86400)
          .withBody("Invalid username and password.")
      );
  }

  private static String readFile(String fileName) {
    //TODO: Use try-with-resource
    Resource resource = new DefaultResourceLoader().getResource("classpath:" + fileName);

    try {
      return FileUtils.readFileToString(resource.getFile(), "UTF-8");
    } catch (IOException e) {
      e.printStackTrace();
    }
    return "</>";
  }
}
