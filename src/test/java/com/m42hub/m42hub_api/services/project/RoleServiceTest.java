package com.m42hub.m42hub_api.services.project;

import com.m42hub.m42hub_api.project.entity.Role;
import com.m42hub.m42hub_api.project.repository.RoleRepository;
import com.m42hub.m42hub_api.project.service.RoleService;
import com.m42hub.m42hub_api.services.util.TestUtils;
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

public class RoleServiceTest {
    private static final Logger logger = LoggerFactory.getLogger(RoleServiceTest.class);

    private static final Long PRIMARY_ROLE_ID = 1L;
    private static final String PRIMARY_ROLE_NAME = "ADMIN";
    private static final String PRIMARY_ROLE_DESC = "Usuário que administra o sistema";

    private static final Long SECONDARY_ROLE_ID = 2L;
    private static final String SECONDARY_ROLE_NAME = "USER";
    private static final String SECONDARY_ROLE_DESC = "Usuário comum";

    private static final Long NEW_ROLE_ID = 3L;
    private static final String NEW_ROLE_NAME = "MODERATOR";
    private static final String NEW_ROLE_DESC = "Usuário moderador";

    @Mock
    private RoleRepository roleRepository;

    @InjectMocks
    private RoleService roleService;

    private AutoCloseable mocks;

    private Role rolePrimary;
    private Role roleSecondary;
    private Role newRole;
    private Role savedRole;

    @BeforeEach
    public void setUp() {
        mocks = MockitoAnnotations.openMocks(this);
        rolePrimary = TestUtils.createRole(PRIMARY_ROLE_ID, PRIMARY_ROLE_NAME, PRIMARY_ROLE_DESC);
        roleSecondary = TestUtils.createRole(SECONDARY_ROLE_ID, SECONDARY_ROLE_NAME, SECONDARY_ROLE_DESC);

        newRole = TestUtils.createRole(null, NEW_ROLE_NAME, NEW_ROLE_DESC);
        savedRole = TestUtils.createRole(NEW_ROLE_ID, NEW_ROLE_NAME, NEW_ROLE_DESC);
    }

    @AfterEach
    public void tearDown() throws Exception {
        mocks.close();
    }

    @Test
    public void shouldReturnAllRoles_whenFindAllIsCalled() {
        // GIVEN
        List<Role> roles = List.of(rolePrimary, roleSecondary);
        Mockito.when(roleRepository.findAll()).thenReturn(roles);

        // WHEN
        List<Role> result = roleService.findAll();

        // THEN
        assertThat(result)
                .hasSize(2)
                .containsExactlyInAnyOrder(rolePrimary, roleSecondary);
        Mockito.verify(roleRepository, Mockito.times(1)).findAll();
    }

    @Test
    public void shouldReturnRole_whenFindByIdIsCalled() {
        // GIVEN
        Mockito.when(roleRepository.findById(PRIMARY_ROLE_ID))
                .thenReturn(Optional.of(rolePrimary));

        // WHEN
        Optional<Role> result = roleService.findById(PRIMARY_ROLE_ID);

        // THEN
        assertThat(result)
                .isPresent()
                .containsSame(rolePrimary);
        Mockito.verify(roleRepository, Mockito.times(1)).findById(PRIMARY_ROLE_ID);
    }

    @Test
    public void shouldReturnEmpty_whenFindByInvalidId() {
        // GIVEN
        Long invalidId = 999L;
        Mockito.when(roleRepository.findById(invalidId))
                .thenReturn(Optional.empty());

        // WHEN
        Optional<Role> result = roleService.findById(invalidId);

        // THEN
        assertThat(result).isEmpty();
        Mockito.verify(roleRepository, Mockito.times(1)).findById(invalidId);
    }

    @Test
    public void shouldSaveRoleSucceed() {
        // GIVEN
        Mockito.when(roleRepository.save(newRole))
                .thenReturn(savedRole);

        // WHEN
        Role result = roleService.save(newRole);

        // THEN
        assertThat(result)
                .isNotNull()
                .extracting(Role::getId, Role::getName)
                .containsExactly(NEW_ROLE_ID, NEW_ROLE_NAME);
        Mockito.verify(roleRepository, Mockito.times(1)).save(newRole);
    }
}