package itis.khabibullina.net;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public class NetSample implements HttpClient {
    @Override
    public String get(String url, Map<String, String> params) {
        try {
            String method = "GET";
            URL fullUrl = new URL(buildUrl(url, params));
            HttpURLConnection connection = (HttpURLConnection) fullUrl.openConnection();

            connection.setRequestMethod(method);
            setHeadersAndTimeout(connection);

            String response = readResponse(connection);
            connection.disconnect();
            return response;

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String post(String url, Map<String, String> params) {
        try {
            URL postUrl = new URL(url);
            String method = "POST";

            HttpURLConnection postConnection = (HttpURLConnection) postUrl.openConnection();

            postConnection.setRequestMethod(method);
            setHeadersAndTimeout(postConnection);

            postConnection.setDoOutput(true);

            ObjectMapper objectMapper = new ObjectMapper();
            String jsonInput = objectMapper.writeValueAsString(params);

            try (OutputStream outputStream = postConnection.getOutputStream()) {
                byte[] input = jsonInput.getBytes(StandardCharsets.UTF_8);
                outputStream.write(input, 0, input.length);
            }

            String response = readResponse(postConnection);
            postConnection.disconnect();
            return response;

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String put(String url, Map<String, String> params) {
        try {
            String id = getId(url, params);
            URL postUrl = new URL(url + "/" + id);
            String method = "PUT";

            HttpURLConnection postConnection = (HttpURLConnection) postUrl.openConnection();

            postConnection.setRequestMethod(method);
            setHeadersAndTimeout(postConnection);

            String response = readResponse(postConnection);
            postConnection.disconnect();
            return response;

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String delete(String url, Map<String, String> params) {
        try {
            String id = getId(url, params);

            URL fullUrl = new URL(url + "/" + id);
            String method = "DELETE";
            HttpURLConnection deleteConnection = (HttpURLConnection) fullUrl.openConnection();

            deleteConnection.setRequestMethod(method);
            setHeadersAndTimeout(deleteConnection);

            String response = readResponse(deleteConnection);
            deleteConnection.disconnect();

            return response;

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private String getId(String url, Map<String, String> params) {
        String jsonStr = get(url, params);
        StringBuilder id = new StringBuilder();
        for (int i = 7; i < jsonStr.length(); i++) {
            char jsonChar = jsonStr.charAt(i);
            if (('0' <= jsonChar) && (jsonChar <= '9')) {
                id.append(jsonChar);
            } else {
                break;
            }
        }
        return id.toString();
    }

    private String buildUrl(String url, Map<String, String> params) {
        StringBuilder urlBuilder = new StringBuilder(url);
        if (!params.isEmpty()) {
            urlBuilder.append("?");
            for (Map.Entry<String, String> pair : params.entrySet()) {
                String key = pair.getKey().replaceAll(" ", "%20");
                String value = pair.getValue().replaceAll(" ", "%20");
                urlBuilder.append(key).append("=").append(value).append("&");
            }
            urlBuilder.deleteCharAt(urlBuilder.length() - 1);
        }
        url = urlBuilder.toString();
        return url;
    }

    private void setHeadersAndTimeout(HttpURLConnection connection) {
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setRequestProperty("Accept", "application/json");
        connection.setRequestProperty("Authorization", "Bearer e21d7666f3aa8df7819218b3d87bdf57b3e2f96c7077ae1b5c99c76fe82ec069");

        connection.setConnectTimeout(5000);
        connection.setReadTimeout(5000);

    }

    private static String readResponse(HttpURLConnection connection) throws IOException {
        if (connection != null) {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                StringBuilder content = new StringBuilder();
                String input;
                while ((input = reader.readLine()) != null) {
                    content.append(input);
                }
                return content.toString();
            }
        }
        return null;
    }
}
