package uk.co.danielshearer.client;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.springframework.stereotype.Component;

@Component
public class SimpleHttpClient {

  private final HttpClient client;

  public SimpleHttpClient() throws Exception{
    final SSLContext sslContext = SSLContext.getInstance("SSL");
    // #Oops. Bad SSL
    final X509TrustManager x509TrustManager = new MyX509TrustManager();
    sslContext.init(null, new TrustManager[] {x509TrustManager}, new SecureRandom());

    client = HttpClient.newBuilder()
      .sslContext(sslContext)
      .build();
  }

  public String get(final String url) throws Exception {

    final HttpRequest request = HttpRequest.newBuilder()
      .uri(URI.create(url))
      .build();

    final HttpResponse<String> response = client
      .send(request, HttpResponse.BodyHandlers.ofString());
    return response.body();
  }

  private static class MyX509TrustManager implements X509TrustManager {
    @Override
    public void checkClientTrusted(final X509Certificate[] chain, final String authType) throws CertificateException {

    }

    @Override
    public void checkServerTrusted(final X509Certificate[] chain, final String authType) throws CertificateException {

    }

    @Override
    public X509Certificate[] getAcceptedIssuers() {
      return new X509Certificate[0];
    }
  }
}
