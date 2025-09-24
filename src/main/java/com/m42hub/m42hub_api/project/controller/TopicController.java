package com.m42hub.m42hub_api.project.controller;

import com.m42hub.m42hub_api.project.dto.request.ChangeColorRequest;
import com.m42hub.m42hub_api.project.dto.request.TopicRequest;
import com.m42hub.m42hub_api.project.dto.response.TopicResponse;
import com.m42hub.m42hub_api.project.entity.Topic;
import com.m42hub.m42hub_api.project.mapper.TopicMapper;
import com.m42hub.m42hub_api.project.service.TopicService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/project/topic")
@RequiredArgsConstructor
public class TopicController {

    private final TopicService topicService;

    @GetMapping()
    public ResponseEntity<List<TopicResponse>> getAll() {
        return ResponseEntity.ok(topicService.findAll()
                .stream()
                .map(TopicMapper::toTopicResponse)
                .toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TopicResponse> getById(@PathVariable Long id) {
        return topicService.findById(id)
                .map(topic -> ResponseEntity.ok(TopicMapper.toTopicResponse(topic)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('topic:create')")
    public ResponseEntity<TopicResponse> save(@RequestBody @Valid TopicRequest request) {
        Topic newTopic = TopicMapper.toTopic(request);
        Topic savedTopic = topicService.save(newTopic);
        return ResponseEntity.status(HttpStatus.CREATED).body(TopicMapper.toTopicResponse(savedTopic));
    }

    @PatchMapping("/color/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('topic:change_color')")
    public ResponseEntity<TopicResponse> changeColor(@PathVariable Long id, @RequestBody @Valid ChangeColorRequest request) {
        return topicService.changeColor(id, request.hexColor())
                .map(topic -> ResponseEntity.ok(TopicMapper.toTopicResponse(topic)))
                .orElse(ResponseEntity.notFound().build());
    }

}
