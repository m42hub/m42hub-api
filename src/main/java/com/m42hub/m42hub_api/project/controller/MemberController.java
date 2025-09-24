package com.m42hub.m42hub_api.project.controller;

import com.m42hub.m42hub_api.config.JWTUserData;
import com.m42hub.m42hub_api.project.dto.request.MemberRejectRequest;
import com.m42hub.m42hub_api.project.dto.request.MemberRequest;
import com.m42hub.m42hub_api.project.dto.response.MemberProjectResponse;
import com.m42hub.m42hub_api.project.dto.response.MemberResponse;
import com.m42hub.m42hub_api.project.entity.Member;
import com.m42hub.m42hub_api.project.mapper.MemberMapper;
import com.m42hub.m42hub_api.project.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/project/member")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @GetMapping()
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('member:get_all')")
    public ResponseEntity<List<MemberResponse>> getAll() {
        return ResponseEntity.ok(memberService.findAll()
                .stream()
                .map(MemberMapper::toMemberResponse)
                .toList());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('member:get_by_id')")
    public ResponseEntity<MemberResponse> getById(@PathVariable Long id) {
        return memberService.findById(id)
                .map(member -> ResponseEntity.ok(MemberMapper.toMemberResponse(member)))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/user/{username}")
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('member:get_by_username')")
    public ResponseEntity<List<MemberProjectResponse>> getByUsername(@PathVariable String username) {
        return ResponseEntity.ok(memberService.findByUsername(username)
                .stream()
                .map(MemberMapper::toMemberProjectsResponse)
                .toList());
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('member:create')")
    public ResponseEntity<MemberResponse> save(@RequestBody @Valid MemberRequest request) {
        Member newMember = MemberMapper.toMember(request);
        Member savedMember = memberService.save(newMember);
        return ResponseEntity.status(HttpStatus.CREATED).body(MemberMapper.toMemberResponse(savedMember));
    }

    @PostMapping("/apply")
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('member:create')")
    public ResponseEntity<MemberResponse> apply(@RequestBody @Valid MemberRequest request) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        JWTUserData userData = (JWTUserData) authentication.getPrincipal();

        Member newMember = MemberMapper.toMemberApply(request, userData.id());
        Member savedMember = memberService.save(newMember);
        return ResponseEntity.status(HttpStatus.CREATED).body(MemberMapper.toMemberResponse(savedMember));
    }

    @PatchMapping("/approve/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('member:approve')")
    public ResponseEntity<MemberResponse> approve(@PathVariable Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        JWTUserData userData = (JWTUserData) authentication.getPrincipal();

        return memberService.approve(id, userData.id())
                .map(member -> ResponseEntity.ok(MemberMapper.toMemberResponse(member)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PatchMapping("/reject/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('member:reject')")
    public ResponseEntity<MemberResponse> reject(@PathVariable Long id, @RequestBody @Valid MemberRejectRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        JWTUserData userData = (JWTUserData) authentication.getPrincipal();

        return memberService.reject(id, request.applicationFeedback(), userData.id())
                .map(member -> ResponseEntity.ok(MemberMapper.toMemberResponse(member)))
                .orElse(ResponseEntity.notFound().build());
    }

}
