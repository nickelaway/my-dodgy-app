package uk.co.danielshearer.contoller;

import uk.co.danielshearer.client.SimpleHttpClient;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProxyController {
  private final Logger logger = LoggerFactory.getLogger(HashItController.class);
  private final SimpleHttpClient httpClient;

  public ProxyController(final SimpleHttpClient httpClient) {
    this.httpClient = httpClient;
  }

  @GetMapping(path = "/proxy", produces = "text/plain")
  public String proxy(@RequestParam("url") final String url) throws Exception {
    logger.info("Requesting: " + url);
    return httpClient.get(url);
  }
}
