package ca.noae.advancementhelper.server;

import java.io.*;
import java.security.*;
import java.security.cert.CertificateException;
import java.util.logging.Logger;
import javax.net.ssl.*;

public class mTLSClient {

    private static final String KEYSTORE_LOCATION = "clientKeystore.jks";
    private static final String KEYSTORE_PASSWORD = "clientKeystore";
    private static final String TRUSTSTORE_LOCATION = "clientTruststore.jks";
    private static final String TRUSTSTORE_PASSWORD = "clientKeystore";
    private static final Logger LOGGER = Logger.getLogger(mTLSClient.class.getName());

    public static void main(String[] args) {
        try {
            // Initialize SSL context
            SSLContext sslContext = SSLContext.getInstance("TLS");

            // Initialize key manager factory
            KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance("SunX509");
            KeyStore keyStore = KeyStore.getInstance("JKS");
            try (FileInputStream keyStoreFileInputStream = new FileInputStream(KEYSTORE_LOCATION)) {
                keyStore.load(keyStoreFileInputStream, KEYSTORE_PASSWORD.toCharArray());
                keyManagerFactory.init(keyStore, KEYSTORE_PASSWORD.toCharArray());
            }

            // Initialize trust manager factory
            TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance("SunX509");
            KeyStore trustStore = KeyStore.getInstance("JKS");
            try (FileInputStream trustStoreFileInputStream = new FileInputStream(TRUSTSTORE_LOCATION)) {
                trustStore.load(trustStoreFileInputStream, TRUSTSTORE_PASSWORD.toCharArray());
                trustManagerFactory.init(trustStore);
            }

            // Initialize SSL context with key and trust managers
            sslContext.init(keyManagerFactory.getKeyManagers(), trustManagerFactory.getTrustManagers(), null);
            SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

            try {
                // Create SSL socket
                SSLSocket sslSocket = (SSLSocket) sslSocketFactory.createSocket("localhost", 8000);

                // Initiate handshake
                sslSocket.startHandshake();

                // Send message to server
                OutputStream outputStream = sslSocket.getOutputStream();
                String message = "Hello Server\n";
                outputStream.write(message.getBytes());
                LOGGER.info("Sent message to server: " + message);

                // Receive response from server
                InputStream inputStream = sslSocket.getInputStream();
                try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream))) {
                    String response = bufferedReader.readLine();
                    LOGGER.info("Received message from server: " + response);
                } finally {
                    sslSocket.close();
                }
            } catch (SSLException e) {
                LOGGER.severe("SSLException occurred: " + e.getMessage());
            }
        } catch (IOException | NoSuchAlgorithmException | KeyStoreException | CertificateException | UnrecoverableKeyException | KeyManagementException e) {
            LOGGER.severe("An error occurred: " + e.getMessage());
        }
    }
}