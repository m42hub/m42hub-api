package com.m42hub.m42hub_api.user.dto.request;

import java.util.List;

public record UserInfoRequest(
        String firstName,
        String lastName,
        String biography,
        String discord,
        String linkedin,
        String github,
        String personalWebsite,
        List<Long> interestRoles
) {
}