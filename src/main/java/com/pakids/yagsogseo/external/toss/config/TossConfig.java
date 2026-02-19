package com.pakids.yagsogseo.external.toss.config;

import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import java.io.ByteArrayInputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyFactory;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;
import javax.net.ssl.KeyManagerFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;
import com.pakids.yagsogseo.external.toss.TossWebClient;

@Configuration
@RequiredArgsConstructor
public class TossConfig {

  private final ResourceLoader resourceLoader;
  private final TossCertProperties tossProperties;

  @Bean
  public TossWebClient tossWebClient() {
    try {
      KeyManagerFactory keyManagerFactory = createKeyManagerFactory(
          tossProperties.getCertPath(),
          tossProperties.getKeyPath()
      );

      SslContext sslContext = SslContextBuilder.forClient()
          .keyManager(keyManagerFactory)
          .build();

      HttpClient httpClient = HttpClient.create()
          .secure(sslContextSpec -> sslContextSpec.sslContext(sslContext));

      WebClient webClient = WebClient.builder()
          .clientConnector(new ReactorClientHttpConnector(httpClient))
          .baseUrl("https://apps-in-toss-api.toss.im")
          .build();

      return new TossWebClient(webClient);

    } catch (Exception e) {
      throw new RuntimeException("Failed to configure mTLS for TossWebClient", e);
    }
  }

  private KeyManagerFactory createKeyManagerFactory(String certPath, String keyPath)
      throws Exception {
    X509Certificate cert = loadCertificate(certPath);
    PrivateKey key = loadPrivateKey(keyPath);

    KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
    keyStore.load(null, null);
    keyStore.setCertificateEntry("client-cert", cert);
    keyStore.setKeyEntry("client-key", key, "".toCharArray(),
        new Certificate[]{cert});

    KeyManagerFactory kmf = KeyManagerFactory.getInstance(
        KeyManagerFactory.getDefaultAlgorithm());
    kmf.init(keyStore, "".toCharArray());

    return kmf;
  }

  private X509Certificate loadCertificate(String certPath) throws Exception {
    Resource resource = resourceLoader.getResource(certPath);
    String certContent = Files.readString(Paths.get(resource.getURI()))
        .replace("-----BEGIN CERTIFICATE-----", "")
        .replace("-----END CERTIFICATE-----", "")
        .replaceAll("\\s", "");

    byte[] certBytes = Base64.getDecoder().decode(certContent);
    return (X509Certificate) CertificateFactory.getInstance("X.509")
        .generateCertificate(new ByteArrayInputStream(certBytes));
  }

  private PrivateKey loadPrivateKey(String keyPath) throws Exception {
    Resource resource = resourceLoader.getResource(keyPath);
    String keyContent = Files.readString(Paths.get(resource.getURI()))
        .replace("-----BEGIN PRIVATE KEY-----", "")
        .replace("-----END PRIVATE KEY-----", "")
        .replaceAll("\\s", "");

    byte[] keyBytes = Base64.getDecoder().decode(keyContent);
    PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(keyBytes);
    return KeyFactory.getInstance("RSA").generatePrivate(spec);
  }
}
