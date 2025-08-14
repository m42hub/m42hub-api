package com.m42hub.m42hub_api.services.user;

import com.m42hub.m42hub_api.services.util.TestUtils;
import com.m42hub.m42hub_api.user.entity.Permission;
import com.m42hub.m42hub_api.user.repository.PermissionRepository;
import com.m42hub.m42hub_api.user.service.PermissionService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PermissionServiceTest {
    private static final Logger logger = LoggerFactory.getLogger(PermissionServiceTest.class);

    private static final Long PRIMARY_PERMISSION_ID = 1L;
    private static final String PRIMARY_PERMISSION_NAME = "create";
    private static final String PRIMARY_PERMISSION_DESC = "Pode criar algo";

    private static final Long SECONDARY_PERMISSION_ID = 2L;
    private static final String SECONDARY_PERMISSION_NAME = "delete";
    private static final String SECONDARY_PERMISSION_DESC = "Pode deletar algo";

    @Mock
    private PermissionService permissionService;

    private AutoCloseable mocks;

    private Permission permissionPrimary;
    private Permission permissionSecondary;

    @BeforeEach
    public void setUp() {
        mocks = MockitoAnnotations.openMocks(this);
        permissionPrimary = TestUtils.createPermission(PRIMARY_PERMISSION_ID, PRIMARY_PERMISSION_NAME, PRIMARY_PERMISSION_DESC);
        permissionSecondary = TestUtils.createPermission(SECONDARY_PERMISSION_ID, SECONDARY_PERMISSION_NAME, SECONDARY_PERMISSION_DESC);
    }

    @AfterEach
    public void tearDown() throws Exception {
        mocks.close();
    }

    @Test
    public void shouldReturnAllPermissions_whenFindAllIsCalled() {
        // GIVEN
        List<Permission> permissions = List.of(permissionPrimary, permissionSecondary);
        Mockito.when(permissionService.findAll()).thenReturn(permissions);

        // WHEN
        List<Permission> result = permissionService.findAll();

        // THEN
        Assertions.assertEquals(permissions, result);
        Mockito.verify(permissionService, Mockito.times(1)).findAll();
    }

    @Test
    public void shouldReturnPermission_whenFindByIdIsCalled() {
        // GIVEN
        Mockito.when(permissionService.findById(PRIMARY_PERMISSION_ID)).thenReturn(Optional.of(permissionPrimary));

        // WHEN
        Optional<Permission> result = permissionService.findById(PRIMARY_PERMISSION_ID);

        // THEN
        Assertions.assertEquals(Optional.of(permissionPrimary), result);
        Mockito.verify(permissionService, Mockito.times(1)).findById(PRIMARY_PERMISSION_ID);
    }

    @Test
    public void shouldSavePermission_whenSaveIsCalled() {
        // GIVEN
        Mockito.when(permissionService.save(permissionPrimary)).thenReturn(permissionPrimary);

        // WHEN
        Permission result = permissionService.save(permissionPrimary);

        // THEN
        Assertions.assertEquals(permissionPrimary, result);
        Mockito.verify(permissionService, Mockito.times(1)).save(permissionPrimary);
    }
}