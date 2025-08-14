package com.m42hub.m42hub_api.services.user;

import com.m42hub.m42hub_api.services.util.TestUtils;
import com.m42hub.m42hub_api.user.entity.SystemRole;
import com.m42hub.m42hub_api.user.service.SystemRoleService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;

public class SystemRoleServiceTest {
    private static final Logger logger = LoggerFactory.getLogger(SystemRoleServiceTest.class);

    private static final Long ADMIN_ROLE_ID = 1L;
    private static final String ADMIN_ROLE_NAME = "ADMIN";

    private static final Long USER_ROLE_ID = 2L;
    private static final String USER_ROLE_NAME = "USER";

    @Mock
    private SystemRoleService systemRoleService;

    private AutoCloseable mocks;

    private SystemRole userRole;
    private SystemRole adminRole;

    @BeforeEach
    public void setUp() {
        mocks = MockitoAnnotations.openMocks(this);
        userRole = TestUtils.createRole(ADMIN_ROLE_ID, ADMIN_ROLE_NAME);
        adminRole = TestUtils.createRole(USER_ROLE_ID, USER_ROLE_NAME);
    }

    @AfterEach
    public void tearDown() throws Exception {
        mocks.close();
    }

    @Test
    public void shouldReturnAllSystemRoles_whenFindAllIsCalled() {
        // GIVEN
        List<SystemRole> fakeSystemRoles = List.of(adminRole, userRole);
        Mockito.when(systemRoleService.findAll()).thenReturn(fakeSystemRoles);

        // WHEN
        List<SystemRole> systemRoles = systemRoleService.findAll();

        // THEN
        Assertions.assertEquals(fakeSystemRoles, systemRoles);
        Mockito.verify(systemRoleService, Mockito.times(1)).findAll();
    }

    @Test
    public void shouldReturnSystemRole_whenFindByIdIsCalled() {
        // GIVEN
        Mockito.when(systemRoleService.findById(ADMIN_ROLE_ID)).thenReturn(Optional.of(adminRole));

        // WHEN
        Optional<SystemRole> systemRole = systemRoleService.findById(ADMIN_ROLE_ID);

        // THEN
        Assertions.assertEquals(Optional.of(adminRole), systemRole);
        Mockito.verify(systemRoleService, Mockito.times(1)).findById(ADMIN_ROLE_ID);
    }

    @Test
    public void shouldSaveSystemRoleSucceed() {
        // GIVEN
        Mockito.when(systemRoleService.save(userRole)).thenReturn(userRole);

        // WHEN
        SystemRole systemRole = systemRoleService.save(userRole);

        // THEN
        Assertions.assertEquals(userRole, systemRole);
        Mockito.verify(systemRoleService, Mockito.times(1)).save(userRole);
    }

}