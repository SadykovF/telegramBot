package ru.redut16;

import org.apache.http.auth.AuthScope;
import org.apache.http.auth.Credentials;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import static ru.redut16.StaticVariable.*;

public class ApacheHttpClient {
    static HttpClient httpClient() {
        CredentialsProvider credentialsProvider = new BasicCredentialsProvider();

        Credentials credentials = new UsernamePasswordCredentials(LOGIN_API, PASSWORD_API);
        credentialsProvider.setCredentials(AuthScope.ANY, credentials);

        HttpClientBuilder clientBuilder = HttpClients.custom();
        clientBuilder.setDefaultCredentialsProvider(credentialsProvider);
        CloseableHttpClient httpClient = clientBuilder.build();
        return httpClient;
    }

}
