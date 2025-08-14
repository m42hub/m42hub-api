package com.m42hub.m42hub_api.services.project;

import com.m42hub.m42hub_api.project.entity.*;
import com.m42hub.m42hub_api.project.service.ProjectService;
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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public class ProjectServiceTest {
    private static final Logger logger = LoggerFactory.getLogger(ProjectServiceTest.class);

    private static final Long PRIMARY_PROJECT_ID = 1L;
    private static final String PRIMARY_PROJECT_NAME = "Projeto Primário";
    private static final String PRIMARY_PROJECT_DESC = "Descrição completa do projeto primário";

    private static final Long SECONDARY_PROJECT_ID = 2L;
    private static final String SECONDARY_PROJECT_NAME = "Projeto Secundário";
    private static final String SECONDARY_PROJECT_DESC = "Outra descrição de teste";

    private static final Long STATUS_ID = 1L;
    private static final String STATUS_NAME = "Ativo";
    private static final String STATUS_DESC = "Status padrão do projeto";

    private static final Long COMPLEXITY_ID = 1L;
    private static final String COMPLEXITY_NAME = "Baixa";
    private static final String COMPLEXITY_COLOR = "#00FF00";
    private static final String COMPLEXITY_DESC = "Projeto de complexidade geral baixa";

    @Mock
    private ProjectService projectService;

    private AutoCloseable mocks;

    private Project projectPrimary;
    private Project projectSecondary;

    @BeforeEach
    public void setUp() {
        mocks = MockitoAnnotations.openMocks(this);

        Status defaultStatus = TestUtils.createStatus(STATUS_ID, STATUS_NAME, STATUS_DESC);
        Complexity defaultComplexity = TestUtils.createComplexity(COMPLEXITY_ID, COMPLEXITY_NAME, COMPLEXITY_COLOR, COMPLEXITY_DESC);

        Date startDate = new Date();
        Date endDate = null;

        List<Tool> tools = new ArrayList<>();
        List<Topic> topics = new ArrayList<>();
        List<Role> unfilledRoles = new ArrayList<>();
        List<Member> members = new ArrayList<>();

        projectPrimary = TestUtils.createProject(
                PRIMARY_PROJECT_ID,
                PRIMARY_PROJECT_NAME,
                PRIMARY_PROJECT_DESC,
                PRIMARY_PROJECT_DESC,
                defaultStatus,
                defaultComplexity,
                null,
                startDate,
                endDate,
                tools,
                topics,
                unfilledRoles,
                members
        );

        projectSecondary = TestUtils.createProject(
                SECONDARY_PROJECT_ID,
                SECONDARY_PROJECT_NAME,
                SECONDARY_PROJECT_DESC,
                SECONDARY_PROJECT_DESC,
                defaultStatus,
                defaultComplexity,
                null,
                startDate,
                endDate,
                tools,
                topics,
                unfilledRoles,
                members
        );
    }

    @AfterEach
    public void tearDown() throws Exception {
        mocks.close();
    }

    @Test
    public void shouldReturnAllProjects_whenFindAllIsCalled() {
        // GIVEN
        List<Project> fakeStatus = List.of(projectPrimary, projectSecondary);
        Mockito.when(projectService.findAll()).thenReturn(fakeStatus);

        // WHEN
        List<Project> result = projectService.findAll();

        // THEN
        Assertions.assertEquals(fakeStatus, result);
        Mockito.verify(projectService, Mockito.times(1)).findAll();
    }

    @Test
    public void shouldReturnProject_whenFindByIdIsCalled() {
        // GIVEN
        Mockito.when(projectService.findById(PRIMARY_PROJECT_ID)).thenReturn(Optional.of(projectPrimary));

        // WHEN
        Optional<Project> result = projectService.findById(PRIMARY_PROJECT_ID);

        // THEN
        Assertions.assertEquals(Optional.of(projectPrimary), result);
        Mockito.verify(projectService, Mockito.times(1)).findById(PRIMARY_PROJECT_ID);
    }

    @Test
    public void shouldSaveProjectSucceed() {
        // GIVEN
        Mockito.when(projectService.save(projectPrimary)).thenReturn(projectPrimary);

        // WHEN
        Project result = projectService.save(projectPrimary);

        // THEN
        Assertions.assertEquals(projectPrimary, result);
        Mockito.verify(projectService, Mockito.times(1)).save(projectPrimary);
    }
}