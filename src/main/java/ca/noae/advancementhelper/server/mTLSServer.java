package ca.noae.advancementhelper.server;

import ca.noae.advancementhelper.server.Helpers.ObjectHelper;
import ca.noae.advancementhelper.server.Structures.AHRequest;
import ca.noae.advancementhelper.server.Structures.Payload;
import ca.noae.advancementhelper.server.Structures.requestType;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwts;
import java.io.*;
import java.security.*;
import java.security.cert.CertificateException;
import java.util.Base64;
import java.util.logging.Logger;
import javax.net.ssl.*;

public class mTLSServer {

  private static final String KEYSTORE_LOCATION = "serverKeystore.jks";
  private static final String KEYSTORE_PASSWORD = "serverKeystore";
  private static final String TRUSTSTORE_LOCATION = "serverTruststore.jks";
  private static final String TRUSTSTORE_PASSWORD = "serverKeystore";
  private static final Logger LOGGER =
      Logger.getLogger(mTLSServer.class.getName());

  public static void main(String[] args) {

    ObjectMapper mapper = new ObjectMapper();
    new ObjectHelper(mapper);
    ClassLoader classloader = Thread.currentThread().getContextClassLoader();

    try {
      // Initialize SSL context
      SSLContext sslContext = SSLContext.getInstance("TLS");

      // Initialize key manager factory
      KeyManagerFactory keyManagerFactory =
          KeyManagerFactory.getInstance("SunX509");
      KeyStore keyStore = KeyStore.getInstance("JKS");
      try (InputStream sKInputStream =
               classloader.getResourceAsStream(KEYSTORE_LOCATION)) {
        keyStore.load(sKInputStream, KEYSTORE_PASSWORD.toCharArray());
        keyManagerFactory.init(keyStore, KEYSTORE_PASSWORD.toCharArray());
      }

      // Initialize trust manager factory
      TrustManagerFactory trustManagerFactory =
          TrustManagerFactory.getInstance("SunX509");
      KeyStore trustStore = KeyStore.getInstance("JKS");
      try (InputStream sTInputStream =
               classloader.getResourceAsStream(TRUSTSTORE_LOCATION)) {
        trustStore.load(sTInputStream, TRUSTSTORE_PASSWORD.toCharArray());
        trustManagerFactory.init(trustStore);
      }

      // Initialize SSL context with key and trust managers
      sslContext.init(keyManagerFactory.getKeyManagers(),
                      trustManagerFactory.getTrustManagers(), null);
      SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

      String userToken = Jwts.builder()
                             .issuer("server")
                             .subject("user/01")
                             .claim("through", "console")
                             .signWith(keyStore.getKey(
                                 "server", KEYSTORE_PASSWORD.toCharArray()))
                             .compact();

      LOGGER.info("[] Token is " + userToken);

      Payload payload = new Payload(userToken, "Hello World");

      AHRequest toSend = new AHRequest(requestType.READ, 100, payload);

      String finalStr = ObjectHelper.convertToString(toSend);

      try {
        // Create SSL socket
        SSLSocket sslSocket =
            (SSLSocket)sslSocketFactory.createSocket("localhost", 8001);

        // Initiate handshake
        sslSocket.startHandshake();

        // Send message to server
        OutputStream outputStream = sslSocket.getOutputStream();
        String message = finalStr + "\n";
        outputStream.write(message.getBytes());
        LOGGER.info("Sent message to database: " + message);

        // Receive response from server
        InputStream inputStream = sslSocket.getInputStream();
        try (BufferedReader bufferedReader =
                 new BufferedReader(new InputStreamReader(inputStream))) {
          String response = bufferedReader.readLine();
          LOGGER.info("Received message from server: " + response);
        } finally {
          sslSocket.close();
        }
      } catch (SSLException e) {
        LOGGER.severe("SSLException occurred: " + e.getMessage());
      }
    } catch (IOException | NoSuchAlgorithmException | KeyStoreException |
             CertificateException | UnrecoverableKeyException |
             KeyManagementException e) {
      LOGGER.severe("An error occurred: " + e.getMessage());
    }
  }
}