package com.m42hub.m42hub_api.project.mapper;

import com.m42hub.m42hub_api.project.dto.request.ProjectRequest;
import com.m42hub.m42hub_api.project.dto.request.ProjectUpdateRequest;
import com.m42hub.m42hub_api.project.dto.response.*;
import com.m42hub.m42hub_api.project.entity.*;
import com.m42hub.m42hub_api.user.entity.User;
import lombok.experimental.UtilityClass;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@UtilityClass
public class ProjectMapper {

    public static Project toProject(ProjectRequest request, Long managerId) {

        Status status = Status.builder().id(request.statusId()).build();

        Complexity complexity = Complexity.builder().id(request.complexityId()).build();

        Role managerRole = Role.builder().id(request.managerRoleId()).build();

        User manager = User.builder().id(managerId).build();

        MemberStatus memberStatus = MemberStatus.builder().id(2L).build();

        List<Tool> tools = request.toolIds().stream()
                .map(toolId -> Tool.builder().id(toolId).build())
                .toList();

        List<Topic> topics = request.topicIds().stream()
                .map(topicId -> Topic.builder().id(topicId).build())
                .toList();

        List<Role> unfilledRoles = new ArrayList<>(request.unfilledRoleIds().stream()
                .map(unfilledRoleId -> Role.builder().id(unfilledRoleId).build())
                .filter(role -> !Objects.equals(role.getId(), managerRole.getId()))
                .toList());

        Project project = Project
                .builder()
                .name(request.name())
                .summary(request.summary())
                .description(request.description())
                .status(status)
                .complexity(complexity)
                .imageUrl(request.imageUrl())
                .startDate(request.startDate())
                .endDate(request.endDate())
                .tools(tools)
                .topics(topics)
                .unfilledRoles(unfilledRoles)
                .discord(request.discord())
                .github(request.github())
                .projectWebsite(request.projectWebsite())
                .build();

        Member memberManager = Member.builder()
                .isManager(true)
                .role(managerRole)
                .user(manager)
                .project(project)
                .memberStatus(memberStatus)
                .build();

        project.setMembers(List.of(memberManager));

        return project;
    }

    public static Project toUpdatedProject(ProjectUpdateRequest request) {

        Status status = null;
        if (request.statusId() != null) {
            status = Status.builder().id(request.statusId()).build();
        }

        Complexity complexity = null;
        if (request.complexityId() != null) {
            complexity = Complexity.builder().id(request.complexityId()).build();
        }

        List<Tool> tools = null;
        if (request.toolIds() != null) {
            tools = new ArrayList<>();
            for (Long toolId : request.toolIds()) {
                tools.add(Tool.builder().id(toolId).build());
            }
        }

        List<Topic> topics = null;
        if (request.topicIds() != null) {
            topics = new ArrayList<>();
            for (Long topicId : request.topicIds()) {
                topics.add(Topic.builder().id(topicId).build());
            }
        }

        List<Role> unfilledRoles = null;
        if (request.unfilledRoleIds() != null) {
            unfilledRoles = new ArrayList<>();
            for (Long roleId : request.unfilledRoleIds()) {
                unfilledRoles.add(Role.builder().id(roleId).build());
            }
        }

        return Project
                .builder()
                .name(request.name())
                .summary(request.summary())
                .description(request.description())
                .status(status)
                .complexity(complexity)
                .imageUrl(request.imageUrl())
                .startDate(request.startDate())
                .endDate(request.endDate())
                .tools(tools)
                .topics(topics)
                .unfilledRoles(unfilledRoles)
                .discord(request.discord())
                .github(request.github())
                .projectWebsite(request.projectWebsite())
                .build();

    }

    public static ProjectResponse toProjectResponse(Project project) {

        StatusResponse status = project.getStatus() != null ? StatusMapper.toStatusResponse(project.getStatus()) : null;
        ComplexityResponse complexity = project.getComplexity() != null ? ComplexityMapper.toComplexityResponse(project.getComplexity()) : null;

        List<ToolResponse> tools = new ArrayList<>();
        if (project.getTools() != null) {
            tools = project.getTools()
                    .stream()
                    .map(ToolMapper::toToolResponse)
                    .toList();
        }

        List<TopicResponse> topics = new ArrayList<>();
        if (project.getTopics() != null) {
            topics = project.getTopics()
                    .stream()
                    .map(TopicMapper::toTopicResponse)
                    .toList();
        }

        List<RoleResponse> unfilledRoles = new ArrayList<>();
        if (project.getUnfilledRoles() != null) {
            unfilledRoles = project.getUnfilledRoles()
                    .stream()
                    .map(RoleMapper::toRoleResponse)
                    .toList();
        }

        List<MemberResponse> members = new ArrayList<>();
        if (project.getMembers() != null) {
            members = project.getMembers()
                    .stream()
                    .map(MemberMapper::toMemberResponse)
                    .toList();
        }

        return ProjectResponse
                .builder()
                .id(project.getId())
                .name(project.getName())
                .summary(project.getSummary())
                .description(project.getDescription())
                .status(status)
                .imageUrl(project.getImageUrl())
                .complexity(complexity)
                .creationDate(project.getCreatedAt())
                .startDate(project.getStartDate())
                .endDate(project.getEndDate())
                .tools(tools)
                .topics(topics)
                .unfilledRoles(unfilledRoles)
                .members(members)
                .discord(project.getDiscord())
                .github(project.getGithub())
                .projectWebsite(project.getProjectWebsite())
                .build();
    }

    public static ProjectListItemResponse toProjectListResponse(Project project) {

        String statusName = project.getStatus() != null ? project.getStatus().getName() : null;
        String complexityName = project.getComplexity() != null ? project.getComplexity().getName() : null;

        List<String> toolNames = new ArrayList<>();
        if (project.getTools() != null) {
            toolNames = project.getTools()
                    .stream()
                    .map(Tool::getName)
                    .toList();
        }

        List<String> topicNames = new ArrayList<>();
        if (project.getTopics() != null) {
            topicNames = project.getTopics()
                    .stream()
                    .map(Topic::getName)
                    .toList();
        }

        List<String> unfilledRoleNames = new ArrayList<>();
        if (project.getUnfilledRoles() != null) {
            unfilledRoleNames = project.getUnfilledRoles()
                    .stream()
                    .map(Role::getName)
                    .toList();
        }

        String manager = null;
        if (project.getMembers() != null) {
            manager = project.getMembers()
                    .stream()
                    .filter(Member::getIsManager)
                    .map(member -> member.getUser().getUsername())
                    .findFirst()
                    .orElse(null);
        }

        return ProjectListItemResponse
                .builder()
                .id(project.getId())
                .name(project.getName())
                .summary(project.getSummary())
                .statusName(statusName)
                .imageUrl(project.getImageUrl())
                .complexityName(complexityName)
                .creationDate(project.getCreatedAt())
                .startDate(project.getStartDate())
                .endDate(project.getEndDate())
                .toolNames(toolNames)
                .topicNames(topicNames)
                .unfilledRoleNames(unfilledRoleNames)
                .manager(manager)
                .discord(project.getDiscord())
                .github(project.getGithub())
                .projectWebsite(project.getProjectWebsite())
                .build();
    }

}
