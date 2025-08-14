package com.m42hub.m42hub_api.services.user;

import com.m42hub.m42hub_api.services.util.TestUtils;
import com.m42hub.m42hub_api.user.entity.SystemRole;
import com.m42hub.m42hub_api.user.entity.User;
import com.m42hub.m42hub_api.user.service.SystemRoleService;
import com.m42hub.m42hub_api.user.service.UserService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

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

    @Mock
    private SystemRoleService systemRoleService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private UserService userService;

    private AutoCloseable mocks;

    private SystemRole adminRole;
    private User john;
    private User jane;

    @BeforeEach
    public void setUp() {
        mocks = MockitoAnnotations.openMocks(this);

        adminRole = TestUtils.createRole(ADMIN_ROLE_ID, ADMIN_ROLE_NAME);
        SystemRole userRole = TestUtils.createRole(USER_ROLE_ID, USER_ROLE_NAME);

        john = TestUtils.createUser(JOHN_ID, JOHN_USERNAME, JOHN_FIRST_NAME, JOHN_LAST_NAME, JOHN_EMAIL, adminRole);
        jane = TestUtils.createUser(JANE_ID, JANE_USERNAME, JANE_FIRST_NAME, JANE_LAST_NAME, JANE_EMAIL, userRole);
    }

    @AfterEach
    public void tearDown() throws Exception {
        mocks.close();
    }

    @Test
    public void shouldReturnAllUsers_whenFindAllIsCalled() {
        // GIVEN
        List<User> fakeUsers = List.of(john, jane);
        Mockito.when(userService.findAll()).thenReturn(fakeUsers);

        // WHEN
        List<User> result = userService.findAll();

        // THEN
        Assertions.assertEquals(fakeUsers, result);
        Mockito.verify(userService, Mockito.times(1)).findAll();
    }

    @Test
    public void shouldReturnUser_whenFindByIdIsCalled() {
        // GIVEN
        Mockito.when(userService.findById(JOHN_ID)).thenReturn(Optional.of(john));

        // WHEN
        Optional<User> result = userService.findById(JOHN_ID);

        // THEN
        Assertions.assertEquals(Optional.of(john), result);
        Mockito.verify(userService, Mockito.times(1)).findById(JOHN_ID);
    }

    @Test
    public void shouldSaveUserSucceed() {
        // GIVEN
        Mockito.when(passwordEncoder.encode(Mockito.anyString())).thenReturn("encodedPass");
        Mockito.when(systemRoleService.findById(ADMIN_ROLE_ID)).thenReturn(Optional.of(adminRole));
        Mockito.when(userService.save(john)).thenReturn(john);

        // WHEN
        User result = userService.save(john);

        // THEN
        Assertions.assertEquals(john, result);
        Mockito.verify(userService, Mockito.times(1)).save(john);
    }

}