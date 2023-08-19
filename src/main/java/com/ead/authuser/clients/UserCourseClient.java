package com.ead.authuser.clients;

import com.ead.authuser.dtos.CourseDTO;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.log4j.Log4j2;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.stream.Collectors;

@Component
@Log4j2
public class UserCourseClient {
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private ObjectMapper objectMapper;

    private static final String COURSES_URI = "http://localhost:8082/";

    public Page<CourseDTO> findAll(Pageable pageable, UUID userId) {
        List<String> queryParams = new ArrayList<>();
        if (userId != null) {
            queryParams.add("userId=" + userId);
        }
        queryParams.add("page=" + pageable.getPageNumber());
        queryParams.add("size=" + pageable.getPageSize());
        queryParams.add("sort=" + pageable.getSort().toString().replaceAll(": ", ","));
        String requestUrl = COURSES_URI + "/courses?" + String.join("&", queryParams);

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
}
