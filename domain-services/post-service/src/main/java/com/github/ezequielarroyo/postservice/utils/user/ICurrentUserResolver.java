package com.github.ezequielarroyo.postservice.utils.user;

import com.github.ezequielarroyo.postservice.entities.UserSnapshot;

public interface ICurrentUserResolver {
    UserSnapshot getCurrentUser();
}
