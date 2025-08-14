package com.m42hub.m42hub_api.services.util;

import com.m42hub.m42hub_api.project.entity.*;
import com.m42hub.m42hub_api.user.entity.Permission;
import com.m42hub.m42hub_api.user.entity.SystemRole;
import com.m42hub.m42hub_api.user.entity.User;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TestUtils {

    public static SystemRole createRole(Long id, String name) {
        return SystemRole.builder()
                .id(id)
                .name(name)
                .build();
    }

    public static User createUser(Long id, String username, String firstName, String lastName, String email, SystemRole role) {
        return User.builder()
                .id(id)
                .username(username)
                .firstName(firstName)
                .lastName(lastName)
                .email(email)
                .isActive(true)
                .systemRole(role)
                .build();
    }

    public static Permission createPermission(Long id, String name, String description) {
        return Permission.builder()
                .id(id)
                .name(name)
                .description(description)
                .build();
    }

    public static Topic createTopic(Long id, String name, String hexColor, String description) {
        return Topic.builder()
                .id(id)
                .name(name)
                .hexColor(hexColor)
                .description(description)
                .build();
    }

    public static Tool createTool(Long id, String name, String hexColor, String description) {
        return Tool.builder()
                .id(id)
                .name(name)
                .hexColor(hexColor)
                .description(description)
                .build();
    }

    public static Status createStatus(Long id, String name, String description) {
        return Status.builder()
                .id(id)
                .name(name)
                .description(description)
                .build();
    }

    public static Role createRole(Long id, String name, String description) {
        return Role.builder()
                .id(id)
                .name(name)
                .description(description)
                .build();
    }

    public static MemberStatus createMemberStatus(Long id, String name, String description) {
        return MemberStatus.builder()
                .id(id)
                .name(name)
                .description(description)
                .build();
    }

    public static Member createMember(
            Long id,
            Boolean isManager,
            Project project,
            Role role,
            User user,
            MemberStatus memberStatus,
            String applicationMessage,
            String applicationFeedback
    ) {
        return Member.builder()
                .id(id)
                .isManager(isManager)
                .project(project)
                .role(role)
                .user(user)
                .memberStatus(memberStatus)
                .applicationMessage(applicationMessage)
                .applicationFeedback(applicationFeedback)
                .build();
    }

    public static Project createProject(
            Long id,
            String name,
            String summary,
            String description,
            Status status,
            Complexity complexity,
            String imageUrl,
            Date startDate,
            Date endDate,
            List<Tool> tools,
            List<Topic> topics,
            List<Role> unfilledRoles,
            List<Member> members
    ) {
        return Project.builder()
                .id(id)
                .name(name)
                .summary(summary)
                .description(description)
                .status(status)
                .complexity(complexity)
                .imageUrl(imageUrl)
                .startDate(startDate)
                .endDate(endDate)
                .tools(tools != null ? tools : new ArrayList<>())
                .topics(topics != null ? topics : new ArrayList<>())
                .unfilledRoles(unfilledRoles != null ? unfilledRoles : new ArrayList<>())
                .members(members != null ? members : new ArrayList<>())
                .build();
    }

    public static Complexity createComplexity(Long id, String name, String hexColor, String description) {
        return Complexity.builder()
                .id(id)
                .name(name)
                .hexColor(hexColor)
                .description(description)
                .build();
    }
}