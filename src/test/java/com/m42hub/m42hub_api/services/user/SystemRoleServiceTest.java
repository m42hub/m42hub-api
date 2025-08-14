package com.m42hub.m42hub_api.services.user;

import com.m42hub.m42hub_api.services.util.TestUtils;
import com.m42hub.m42hub_api.user.entity.Permission;
import com.m42hub.m42hub_api.user.entity.SystemRole;
import com.m42hub.m42hub_api.user.repository.SystemRoleRepository;
import com.m42hub.m42hub_api.user.service.PermissionService;
import com.m42hub.m42hub_api.user.service.SystemRoleService;
import org.junit.jupiter.api.AfterEach;
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

import static org.assertj.core.api.Assertions.assertThat;

public class SystemRoleServiceTest {
    private static final Logger logger = LoggerFactory.getLogger(SystemRoleServiceTest.class);

    private static final Long ADMIN_ROLE_ID = 1L;
    private static final String ADMIN_ROLE_NAME = "ADMIN";

    private static final Long USER_ROLE_ID = 2L;
    private static final String USER_ROLE_NAME = "USER";

    private static final Long NEW_ROLE_ID = 3L;
    private static final String NEW_ROLE_NAME = "MODERATOR";

    private static final Long PERM_ADMIN_ID = 1L;
    private static final String PERM_ADMIN_NAME = "PERM_ADMIN";
    private static final String PERM_ADMIN_DESC = "Admin Permission";

    private static final Long PERM_USER_ID = 2L;
    private static final String PERM_USER_NAME = "PERM_USER";
    private static final String PERM_USER_DESC = "User Permission";

    @Mock
    private SystemRoleRepository systemRoleRepository;

    @Mock
    private PermissionService permissionService;

    @InjectMocks
    private SystemRoleService systemRoleService;

    private AutoCloseable mocks;

    private SystemRole adminRole;
    private SystemRole userRole;
    private SystemRole newRole;
    private SystemRole savedRole;

    @BeforeEach
    public void setUp() {
        mocks = MockitoAnnotations.openMocks(this);
        adminRole = TestUtils.createRole(ADMIN_ROLE_ID, ADMIN_ROLE_NAME);
        userRole = TestUtils.createRole(USER_ROLE_ID, USER_ROLE_NAME);

        newRole = TestUtils.createRole(null, NEW_ROLE_NAME);
        savedRole = TestUtils.createRole(NEW_ROLE_ID, NEW_ROLE_NAME);

        Permission adminPermission = TestUtils.createPermission(PERM_ADMIN_ID, PERM_ADMIN_NAME, PERM_ADMIN_DESC);
        Permission userPermission = TestUtils.createPermission(PERM_USER_ID, PERM_USER_NAME, PERM_USER_DESC);

        List<Permission> permissions = new ArrayList<>();
        permissions.add(adminPermission);
        permissions.add(userPermission);
        newRole.setPermissions(permissions);
    }

    @AfterEach
    public void tearDown() throws Exception {
        mocks.close();
    }

    @Test
    public void shouldReturnAllSystemRoles_whenFindAllIsCalled() {
        // GIVEN
        List<SystemRole> expectedRoles = List.of(adminRole, userRole);
        Mockito.when(systemRoleRepository.findAll()).thenReturn(expectedRoles);

        // WHEN
        List<SystemRole> result = systemRoleService.findAll();

        // THEN
        assertThat(result)
                .hasSize(2)
                .containsExactlyInAnyOrder(adminRole, userRole);
        Mockito.verify(systemRoleRepository, Mockito.times(1)).findAll();
    }

    @Test
    public void shouldReturnEmpty_whenFindByInvalidId() {
        // GIVEN
        Long invalidId = 999L;
        Mockito.when(systemRoleRepository.findById(invalidId))
                .thenReturn(Optional.empty());

        // WHEN
        Optional<SystemRole> result = systemRoleService.findById(invalidId);

        // THEN
        assertThat(result).isEmpty();
        Mockito.verify(systemRoleRepository, Mockito.times(1)).findById(invalidId);
    }

    @Test
    public void shouldSaveSystemRoleSucceed() {
        // GIVEN
        Mockito.when(systemRoleRepository.save(newRole))
                .thenReturn(savedRole);

        // WHEN
        SystemRole result = systemRoleService.save(newRole);

        // THEN
        assertThat(result)
                .isNotNull()
                .extracting(SystemRole::getId, SystemRole::getName)
                .containsExactly(NEW_ROLE_ID, NEW_ROLE_NAME);
        Mockito.verify(systemRoleRepository, Mockito.times(1)).save(newRole);
    }
}