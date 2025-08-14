package com.m42hub.m42hub_api.services.project;

import com.m42hub.m42hub_api.project.entity.Role;
import com.m42hub.m42hub_api.project.service.RoleService;
import com.m42hub.m42hub_api.services.util.TestUtils;
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

public class RoleServiceTest {
    private static final Logger logger = LoggerFactory.getLogger(RoleServiceTest.class);

    private static final Long PRIMARY_ROLE_ID = 1L;
    private static final String PRIMARY_ROLE_NAME = "ADMIN";
    private static final String PRIMARY_ROLE_DESC = "Usuário que administra o sistema";

    private static final Long SECONDARY_ROLE_ID = 2L;
    private static final String SECONDARY_ROLE_NAME = "USER";
    private static final String SECONDARY_ROLE_DESC = "Usuário comum";

    @Mock
    private RoleService roleService;

    private AutoCloseable mocks;

    private Role rolePrimary;
    private Role roleSecondary;

    @BeforeEach
    public void setUp() {
        mocks = MockitoAnnotations.openMocks(this);
        rolePrimary = TestUtils.createRole(PRIMARY_ROLE_ID, PRIMARY_ROLE_NAME, PRIMARY_ROLE_DESC);
        roleSecondary = TestUtils.createRole(SECONDARY_ROLE_ID, SECONDARY_ROLE_NAME, SECONDARY_ROLE_DESC);
    }

    @AfterEach
    public void tearDown() throws Exception {
        mocks.close();
    }

    @Test
    public void shouldReturnAllRoles_whenFindAllIsCalled() {
        // GIVEN
        List<Role> roles = List.of(rolePrimary, roleSecondary);
        Mockito.when(roleService.findAll()).thenReturn(roles);

        // WHEN
        List<Role> result = roleService.findAll();

        // THEN
        Assertions.assertEquals(roles, result);
        Mockito.verify(roleService, Mockito.times(1)).findAll();
    }

    @Test
    public void shouldReturnRole_whenFindByIdIsCalled() {
        // GIVEN
        Mockito.when(roleService.findById(PRIMARY_ROLE_ID)).thenReturn(Optional.of(rolePrimary));

        // WHEN
        Optional<Role> result = roleService.findById(PRIMARY_ROLE_ID);

        // THEN
        Assertions.assertEquals(Optional.of(rolePrimary), result);
        Mockito.verify(roleService, Mockito.times(1)).findById(PRIMARY_ROLE_ID);
    }

    @Test
    public void shouldSaveRoleSucceed() {
        // GIVEN
        Mockito.when(roleService.save(rolePrimary)).thenReturn(rolePrimary);

        // WHEN
        Role result = roleService.save(rolePrimary);

        // THEN
        Assertions.assertEquals(rolePrimary, result);
        Mockito.verify(roleService, Mockito.times(1)).save(rolePrimary);
    }
}