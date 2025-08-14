package com.m42hub.m42hub_api.services.user;

import com.m42hub.m42hub_api.services.util.TestUtils;
import com.m42hub.m42hub_api.user.entity.SystemRole;
import com.m42hub.m42hub_api.user.entity.User;
import com.m42hub.m42hub_api.user.repository.UserRepository;
import com.m42hub.m42hub_api.user.service.SystemRoleService;
import com.m42hub.m42hub_api.user.service.UserService;
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
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class UserServiceTests {
    private static final Logger logger = LoggerFactory.getLogger(UserServiceTests.class);

    private static final Long ADMIN_ROLE_ID = 1L;
    private static final String ADMIN_ROLE_NAME = "ADMIN";

    private static final Long USER_ROLE_ID = 2L;
    private static final String USER_ROLE_NAME = "USER";

    private static final Long JOHN_ID = 1L;
    private static final String JOHN_USERNAME = "john.doe";
    private static final String JOHN_FIRST_NAME = "John";
    private static final String JOHN_LAST_NAME = "Doe";
    private static final String JOHN_EMAIL = "john@example.com";

    private static final Long JANE_ID = 2L;
    private static final String JANE_USERNAME = "jane.smith";
    private static final String JANE_FIRST_NAME = "Jane";
    private static final String JANE_LAST_NAME = "Smith";
    private static final String JANE_EMAIL = "jane@example.com";

    private static final Long NEW_USER_ID = 3L;
    private static final String NEW_USER_USERNAME = "new.user";
    private static final String NEW_USER_FIRST_NAME = "New";
    private static final String NEW_USER_LAST_NAME = "User";
    private static final String NEW_USER_EMAIL = "new@example.com";
    private static final String RAW_PASSWORD = "password123";
    private static final String ENCODED_PASSWORD = "encodedPassword";

    @Mock
    private SystemRoleService systemRoleService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    private AutoCloseable mocks;

    private SystemRole adminRole;
    private User john;
    private User jane;
    private User newUser;
    private User savedUser;

    @BeforeEach
    public void setUp() {
        mocks = MockitoAnnotations.openMocks(this);

        adminRole = TestUtils.createRole(ADMIN_ROLE_ID, ADMIN_ROLE_NAME);
        SystemRole userRole = TestUtils.createRole(USER_ROLE_ID, USER_ROLE_NAME);

        john = TestUtils.createUser(JOHN_ID, JOHN_USERNAME, JOHN_FIRST_NAME, JOHN_LAST_NAME, JOHN_EMAIL, adminRole);
        jane = TestUtils.createUser(JANE_ID, JANE_USERNAME, JANE_FIRST_NAME, JANE_LAST_NAME, JANE_EMAIL, userRole);

        newUser = TestUtils.createUser(
                null,
                NEW_USER_USERNAME,
                NEW_USER_FIRST_NAME,
                NEW_USER_LAST_NAME,
                NEW_USER_EMAIL,
                adminRole
        );
        newUser.setPassword(RAW_PASSWORD);

        savedUser = TestUtils.createUser(
                NEW_USER_ID,
                NEW_USER_USERNAME,
                NEW_USER_FIRST_NAME,
                NEW_USER_LAST_NAME,
                NEW_USER_EMAIL,
                adminRole
        );
        savedUser.setPassword(ENCODED_PASSWORD);
    }

    @AfterEach
    public void tearDown() throws Exception {
        mocks.close();
    }

    @Test
    public void shouldReturnAllUsers_whenFindAllIsCalled() {
        // GIVEN
        List<User> expectedUsers = List.of(john, jane);
        Mockito.when(userRepository.findAll()).thenReturn(expectedUsers);

        // WHEN
        List<User> result = userService.findAll();

        // THEN
        assertThat(result)
                .hasSize(2)
                .containsExactlyInAnyOrder(john, jane);
        Mockito.verify(userRepository, Mockito.times(1)).findAll();
    }

    @Test
    public void shouldReturnUser_whenFindByIdIsCalled() {
        // GIVEN
        Mockito.when(userRepository.findById(JOHN_ID))
                .thenReturn(Optional.of(john));

        // WHEN
        Optional<User> result = userService.findById(JOHN_ID);

        // THEN
        assertThat(result)
                .isPresent()
                .containsSame(john);
        Mockito.verify(userRepository, Mockito.times(1)).findById(JOHN_ID);
    }

    @Test
    public void shouldReturnEmpty_whenFindByInvalidId() {
        // GIVEN
        Long invalidId = 999L;
        Mockito.when(userRepository.findById(invalidId))
                .thenReturn(Optional.empty());

        // WHEN
        Optional<User> result = userService.findById(invalidId);

        // THEN
        assertThat(result).isEmpty();
        Mockito.verify(userRepository, Mockito.times(1)).findById(invalidId);
    }

    @Test
    public void shouldSaveUserSucceed() {
        // GIVEN
        Mockito.when(passwordEncoder.encode(RAW_PASSWORD))
                .thenReturn(ENCODED_PASSWORD);
        Mockito.when(systemRoleService.findById(ADMIN_ROLE_ID))
                .thenReturn(Optional.of(adminRole));
        Mockito.when(userRepository.save(newUser))
                .thenReturn(savedUser);

        // WHEN
        User result = userService.save(newUser);

        // THEN
        assertThat(result)
                .isNotNull()
                .extracting(User::getId, User::getUsername)
                .containsExactly(NEW_USER_ID, NEW_USER_USERNAME);

        assertThat(newUser.getPassword()).isEqualTo(ENCODED_PASSWORD);
        Mockito.verify(passwordEncoder, Mockito.times(1)).encode(RAW_PASSWORD);
        Mockito.verify(systemRoleService, Mockito.times(1)).findById(ADMIN_ROLE_ID);
        Mockito.verify(userRepository, Mockito.times(1)).save(newUser);
    }
}