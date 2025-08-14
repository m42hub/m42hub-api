package com.m42hub.m42hub_api.services.project;

import com.m42hub.m42hub_api.project.entity.Status;
import com.m42hub.m42hub_api.project.repository.StatusRepository;
import com.m42hub.m42hub_api.project.service.StatusService;
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

public class StatusServiceTest {
    private static final Logger logger = LoggerFactory.getLogger(StatusServiceTest.class);

    private static final Long PRIMARY_STATUS_ID = 1L;
    private static final String PRIMARY_STATUS_NAME = "Concluído";
    private static final String PRIMARY_STATUS_DESC = "Já é possivel acessar o projeto";

    private static final Long SECONDARY_STATUS_ID = 2L;
    private static final String SECONDARY_STATUS_NAME = "Em Andamento";
    private static final String SECONDARY_STATUS_DESC = "Projeto em progresso";

    private static final Long NEW_STATUS_ID = 3L;
    private static final String NEW_STATUS_NAME = "Novo Status";
    private static final String NEW_STATUS_DESC = "Descrição do novo status";

    @Mock
    private StatusRepository statusRepository;

    @InjectMocks
    private StatusService statusService;

    private AutoCloseable mocks;

    private Status statusPrimary;
    private Status statusSecondary;
    private Status newStatus;
    private Status savedStatus;

    @BeforeEach
    public void setUp() {
        mocks = MockitoAnnotations.openMocks(this);
        statusPrimary = TestUtils.createStatus(PRIMARY_STATUS_ID, PRIMARY_STATUS_NAME, PRIMARY_STATUS_DESC);
        statusSecondary = TestUtils.createStatus(SECONDARY_STATUS_ID, SECONDARY_STATUS_NAME, SECONDARY_STATUS_DESC);

        newStatus = TestUtils.createStatus(null, NEW_STATUS_NAME, NEW_STATUS_DESC);
        savedStatus = TestUtils.createStatus(NEW_STATUS_ID, NEW_STATUS_NAME, NEW_STATUS_DESC);
    }

    @AfterEach
    public void tearDown() throws Exception {
        mocks.close();
    }

    @Test
    public void shouldReturnAllStatus_whenFindAllIsCalled() {
        // GIVEN
        List<Status> statuses = List.of(statusPrimary, statusSecondary);
        Mockito.when(statusRepository.findAll()).thenReturn(statuses);

        // WHEN
        List<Status> result = statusService.findAll();

        // THEN
        assertThat(result)
                .hasSize(2)
                .containsExactlyInAnyOrder(statusPrimary, statusSecondary);
        Mockito.verify(statusRepository, Mockito.times(1)).findAll();
    }

    @Test
    public void shouldReturnStatus_whenFindByIdIsCalled() {
        // GIVEN
        Mockito.when(statusRepository.findById(PRIMARY_STATUS_ID))
                .thenReturn(Optional.of(statusPrimary));

        // WHEN
        Optional<Status> result = statusService.findById(PRIMARY_STATUS_ID);

        // THEN
        assertThat(result)
                .isPresent()
                .containsSame(statusPrimary);
        Mockito.verify(statusRepository, Mockito.times(1)).findById(PRIMARY_STATUS_ID);
    }

    @Test
    public void shouldReturnEmpty_whenFindByInvalidId() {
        // GIVEN
        Long invalidId = 999L;
        Mockito.when(statusRepository.findById(invalidId))
                .thenReturn(Optional.empty());

        // WHEN
        Optional<Status> result = statusService.findById(invalidId);

        // THEN
        assertThat(result).isEmpty();
        Mockito.verify(statusRepository, Mockito.times(1)).findById(invalidId);
    }

    @Test
    public void shouldSaveStatusSucceed() {
        // GIVEN
        Mockito.when(statusRepository.save(newStatus))
                .thenReturn(savedStatus);

        // WHEN
        Status result = statusService.save(newStatus);

        // THEN
        assertThat(result)
                .isNotNull()
                .extracting(Status::getId, Status::getName)
                .containsExactly(NEW_STATUS_ID, NEW_STATUS_NAME);
        Mockito.verify(statusRepository, Mockito.times(1)).save(newStatus);
    }
}