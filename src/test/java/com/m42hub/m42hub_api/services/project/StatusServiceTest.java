package com.m42hub.m42hub_api.services.project;

import com.m42hub.m42hub_api.project.entity.Status;
import com.m42hub.m42hub_api.project.service.StatusService;
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

public class StatusServiceTest {
    private static final Logger logger = LoggerFactory.getLogger(StatusServiceTest.class);

    private static final Long PRIMARY_STATUS_ID = 1L;
    private static final String PRIMARY_STATUS_NAME = "Concluído";
    private static final String PRIMARY_STATUS_DESC = "Já é possivel acessar o projeto";

    private static final Long SECONDARY_STATUS_ID = 2L;
    private static final String SECONDARY_STATUS_NAME = "Em Andamento";
    private static final String SECONDARY_STATUS_DESC = "Projeto em progresso";

    @Mock
    private StatusService statusService;

    private AutoCloseable mocks;

    private Status statusPrimary;
    private Status statusSecondary;

    @BeforeEach
    public void setUp() {
        mocks = MockitoAnnotations.openMocks(this);
        statusPrimary = TestUtils.createStatus(PRIMARY_STATUS_ID, PRIMARY_STATUS_NAME, PRIMARY_STATUS_DESC);
        statusSecondary = TestUtils.createStatus(SECONDARY_STATUS_ID, SECONDARY_STATUS_NAME, SECONDARY_STATUS_DESC);
    }

    @AfterEach
    public void tearDown() throws Exception {
        mocks.close();
    }

    @Test
    public void shouldReturnAllStatus_whenFindAllIsCalled() {
        // GIVEN
        List<Status> statuses = List.of(statusPrimary, statusSecondary);
        Mockito.when(statusService.findAll()).thenReturn(statuses);

        // WHEN
        List<Status> result = statusService.findAll();

        // THEN
        Assertions.assertEquals(statuses, result);
        Mockito.verify(statusService, Mockito.times(1)).findAll();
    }

    @Test
    public void shouldReturnStatus_whenFindByIdIsCalled() {
        // GIVEN
        Mockito.when(statusService.findById(PRIMARY_STATUS_ID)).thenReturn(Optional.of(statusPrimary));

        // WHEN
        Optional<Status> result = statusService.findById(PRIMARY_STATUS_ID);

        // THEN
        Assertions.assertEquals(Optional.of(statusPrimary), result);
        Mockito.verify(statusService, Mockito.times(1)).findById(PRIMARY_STATUS_ID);
    }

    @Test
    public void shouldSaveStatusSucceed() {
        // GIVEN
        Mockito.when(statusService.save(statusPrimary)).thenReturn(statusPrimary);

        // WHEN
        Status result = statusService.save(statusPrimary);

        // THEN
        Assertions.assertEquals(statusPrimary, result);
        Mockito.verify(statusService, Mockito.times(1)).save(statusPrimary);
    }
}