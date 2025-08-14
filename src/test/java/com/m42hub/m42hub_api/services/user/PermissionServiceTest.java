package com.m42hub.m42hub_api.services.user;

import com.m42hub.m42hub_api.services.util.TestUtils;
import com.m42hub.m42hub_api.user.entity.Permission;
import com.m42hub.m42hub_api.user.repository.PermissionRepository;
import com.m42hub.m42hub_api.user.service.PermissionService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

public class PermissionServiceTest {
    private static final Logger logger = LoggerFactory.getLogger(PermissionServiceTest.class);

    private static final Long PRIMARY_PERMISSION_ID = 1L;
    private static final String PRIMARY_PERMISSION_NAME = "create";
    private static final String PRIMARY_PERMISSION_DESC = "Pode criar algo";

    private static final Long SECONDARY_PERMISSION_ID = 2L;
    private static final String SECONDARY_PERMISSION_NAME = "delete";
    private static final String SECONDARY_PERMISSION_DESC = "Pode deletar algo";

    private static final Long NEW_PERMISSION_ID = 3L;
    private static final String NEW_PERMISSION_NAME = "update";
    private static final String NEW_PERMISSION_DESC = "Pode atualizar algo";

    @Mock
    private PermissionRepository permissionRepository;

    @InjectMocks
    private PermissionService permissionService;

    private AutoCloseable mocks;

    private Permission permissionPrimary;
    private Permission permissionSecondary;
    private Permission newPermission;
    private Permission savedPermission;

    @BeforeEach
    public void setUp() {
        mocks = MockitoAnnotations.openMocks(this);
        permissionPrimary = TestUtils.createPermission(PRIMARY_PERMISSION_ID, PRIMARY_PERMISSION_NAME, PRIMARY_PERMISSION_DESC);
        permissionSecondary = TestUtils.createPermission(SECONDARY_PERMISSION_ID, SECONDARY_PERMISSION_NAME, SECONDARY_PERMISSION_DESC);

        newPermission = TestUtils.createPermission(null, NEW_PERMISSION_NAME, NEW_PERMISSION_DESC);
        savedPermission = TestUtils.createPermission(NEW_PERMISSION_ID, NEW_PERMISSION_NAME, NEW_PERMISSION_DESC);
    }

    @AfterEach
    public void tearDown() throws Exception {
        mocks.close();
    }

    @Test
    public void shouldReturnAllPermissions_whenFindAllIsCalled() {
        // GIVEN
        List<Permission> permissions = List.of(permissionPrimary, permissionSecondary);
        Mockito.when(permissionRepository.findAll()).thenReturn(permissions);

        // WHEN
        List<Permission> result = permissionService.findAll();

        // THEN
        assertThat(result)
                .hasSize(2)
                .containsExactlyInAnyOrder(permissionPrimary, permissionSecondary);
        Mockito.verify(permissionRepository, Mockito.times(1)).findAll();
    }

    @Test
    public void shouldReturnPermission_whenFindByIdIsCalled() {
        // GIVEN
        Mockito.when(permissionRepository.findById(PRIMARY_PERMISSION_ID))
                .thenReturn(Optional.of(permissionPrimary));

        // WHEN
        Optional<Permission> result = permissionService.findById(PRIMARY_PERMISSION_ID);

        // THEN
        assertThat(result)
                .isPresent()
                .containsSame(permissionPrimary);
        Mockito.verify(permissionRepository, Mockito.times(1)).findById(PRIMARY_PERMISSION_ID);
    }

    @Test
    public void shouldReturnEmpty_whenFindByInvalidId() {
        // GIVEN
        Long invalidId = 999L;
        Mockito.when(permissionRepository.findById(invalidId))
                .thenReturn(Optional.empty());

        // WHEN
        Optional<Permission> result = permissionService.findById(invalidId);

        // THEN
        assertThat(result).isEmpty();
        Mockito.verify(permissionRepository, Mockito.times(1)).findById(invalidId);
    }

    @Test
    public void shouldSavePermission_whenSaveIsCalled() {
        // GIVEN
        Mockito.when(permissionRepository.save(newPermission))
                .thenReturn(savedPermission);

        // WHEN
        Permission result = permissionService.save(newPermission);

        // THEN
        assertThat(result)
                .isNotNull()
                .extracting(Permission::getId, Permission::getName)
                .containsExactly(NEW_PERMISSION_ID, NEW_PERMISSION_NAME);
        Mockito.verify(permissionRepository, Mockito.times(1)).save(newPermission);
    }
}