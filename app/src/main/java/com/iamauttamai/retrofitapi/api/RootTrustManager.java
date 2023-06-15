package com.iamauttamai.retrofitapi.api;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.net.Socket;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.SSLEngine;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509ExtendedTrustManager;

@RequiresApi(api = Build.VERSION_CODES.N)
public class RootTrustManager extends X509ExtendedTrustManager {
    private X509ExtendedTrustManager standardTrustManager = null;

    public RootTrustManager(KeyStore keystore)
            throws NoSuchAlgorithmException, KeyStoreException {
        super();
        TrustManagerFactory factory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        factory.init(keystore);
        TrustManager[] trustmanagers = factory.getTrustManagers();
        if (trustmanagers.length == 0) {
            throw new NoSuchAlgorithmException("no trust manager found");
        }
        this.standardTrustManager = (X509ExtendedTrustManager) trustmanagers[0];
    }

    @Override
    public void checkClientTrusted(X509Certificate[] chain, String authType)
            throws CertificateException {
        // Use the default configuration for all client authentication. Domain specific configs are
        // only for use in checking server trust not client trust.
        standardTrustManager.checkClientTrusted(chain, authType);
    }

    @Override
    public void checkClientTrusted(X509Certificate[] certs, String authType, Socket socket)
            throws CertificateException {
        // Use the default configuration for all client authentication. Domain specific configs are
        // only for use in checking server trust not client trust.
        standardTrustManager.checkClientTrusted(certs, authType, socket);
    }

    @Override
    public void checkClientTrusted(X509Certificate[] certs, String authType, SSLEngine engine)
            throws CertificateException {
        // Use the default configuration for all client authentication. Domain specific configs are
        // only for use in checking server trust not client trust.
        standardTrustManager.checkClientTrusted(certs, authType, engine);
    }

    @Override
    public void checkServerTrusted(X509Certificate[] certs, String authType, Socket socket)
            throws CertificateException {
        if (socket instanceof SSLSocket) {
            SSLSocket sslSocket = (SSLSocket) socket;
            SSLSession session = sslSocket.getHandshakeSession();
            if (session == null) {
                throw new CertificateException("Not in handshake; no session available");
            }
            standardTrustManager.checkServerTrusted(certs, authType, socket);
        } else {
            // Not an SSLSocket, use the hostname unaware checkServerTrusted.
            checkServerTrusted(certs, authType);
        }
    }

    @Override
    public void checkServerTrusted(X509Certificate[] certs, String authType, SSLEngine engine)
            throws CertificateException {
        standardTrustManager.checkServerTrusted(certs, authType, engine);
    }

    @Override
    public void checkServerTrusted(X509Certificate[] certs, String authType)
            throws CertificateException {
        standardTrustManager.checkServerTrusted(certs, authType);
    }

    /**
     * Hostname aware version of {@link #checkServerTrusted(X509Certificate[], String)}.
     * This interface is used by conscrypt and android.net.http.X509TrustManagerExtensions do not
     * modify without modifying those callers.
     */
//    public List<X509Certificate> checkServerTrusted(X509Certificate[] certs, String authType,
//                                                    String hostname) throws CertificateException {
//        return standardTrustManager.checkServerTrusted(certs, authType, hostname);
//    }

    @Override
    public X509Certificate[] getAcceptedIssuers() {
        // getAcceptedIssuers is meant to be used to determine which trust anchors the server will
        // accept when verifying clients. Domain specific configs are only for use in checking
        // server trust not client trust so use the default config.
        return standardTrustManager.getAcceptedIssuers();
    }

    /**
     * Returns {@code true} if this trust manager uses the same trust configuration for the provided
     * hostnames.
     *
     * <p>This is required by android.net.http.X509TrustManagerExtensions.
     */
//    public boolean isSameTrustConfiguration(String hostname1, String hostname2) {
//        return mConfig.getConfigForHostname(hostname1)
//                .equals(mConfig.getConfigForHostname(hostname2));
//    }
}
