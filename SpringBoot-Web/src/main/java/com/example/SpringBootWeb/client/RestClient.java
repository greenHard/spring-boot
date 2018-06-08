package com.example.SpringBootWeb.client;

import com.example.SpringBootWeb.controller.Person;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

public class RestClient {
    public static void main(String[] args) {
        // RestTemplate restTemplate = new RestTemplate();
        // Person person = restTemplate.getForObject("http://localhost:8080/json/person", Person.class);
        // System.out.println(person);

        HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();
        HttpClient build = httpClientBuilder.build();
        HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory(build);
        RestTemplate restTemplate = new RestTemplate(factory);
        Person person = restTemplate.getForObject("http://localhost:8080/json/person", Person.class);
        System.out.println(person);
    }
}
