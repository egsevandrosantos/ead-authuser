package com.ead.authuser.clients;

import com.ead.authuser.dtos.CourseDTO;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.log4j.Log4j2;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Component
@Log4j2
@Deprecated
public class CourseClient {
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private ObjectMapper objectMapper;
    @Value("${ead.api.url.authuser}")
    private String authUserURI;
    @Value("${ead.api.url.course}")
    private String coursesURI;

    public Page<CourseDTO> findAllCourses(Pageable pageable, UUID userId) {
        List<String> queryParams = new ArrayList<>();
        if (userId != null) {
            queryParams.add("userId=" + userId);
        }
        queryParams.add("page=" + pageable.getPageNumber());
        queryParams.add("size=" + pageable.getPageSize());
        queryParams.add("sort=" + pageable.getSort().toString().replaceAll(": ", ","));
        String requestUrl = coursesURI + "/courses?" + String.join("&", queryParams);

        log.info("Request URL: {}", requestUrl);
        try {
            ResponseEntity<String> responseEntity = restTemplate
                .exchange(requestUrl, HttpMethod.GET, null, String.class);
            JSONObject json = new JSONObject(responseEntity.getBody());
            TypeReference<List<CourseDTO>> courses = new TypeReference<>() {};
            return new PageImpl<>(
                objectMapper.readValue(json.get("content").toString(), courses),
                PageRequest.of(Integer.parseInt(json.get("number").toString()), Integer.parseInt(json.get("size").toString())),
                objectMapper.readValue(json.get("totalElements").toString(), Long.class)
            );
        } catch (Exception ex) {
            log.error("Error in request to URL: {}", requestUrl, ex);
        } finally {
            log.info("Completed request to URL: {}", requestUrl);
        }
        return null;
    }

    public void deleteCourseUserRelationship(UUID userId) {
        String requestUrl = coursesURI + "/users/" + userId + "/courses";

        HttpHeaders headers = new HttpHeaders();
        headers.set("Requested-By", Base64.getEncoder().encodeToString(authUserURI.getBytes()));
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> requestEntity = new HttpEntity<>(null, headers);

        log.info("Request URL: {}", requestUrl);
        try {
            restTemplate.exchange(requestUrl, HttpMethod.DELETE, requestEntity, String.class);
        } catch (HttpStatusCodeException ex) {
            log.error("Error in request to URL: {}", requestUrl, ex);
            throw ex;
        } finally {
            log.info("Completed request to URL: {}", requestUrl);
        }
    }
}
