package com.m42hub.m42hub_api.services.project;

import com.m42hub.m42hub_api.project.entity.Complexity;
import com.m42hub.m42hub_api.project.repository.ComplexityRepository;
import com.m42hub.m42hub_api.project.service.ComplexityService;
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

public class ComplexityServiceTest {
    private static final Logger logger = LoggerFactory.getLogger(ComplexityServiceTest.class);

    private static final Long PRIMARY_COMPLEXITY_ID = 1L;
    private static final String PRIMARY_COMPLEXITY_NAME = "Médio";
    private static final String PRIMARY_COMPLEXITY_COLOR = "#FF0000";
    private static final String PRIMARY_COMPLEXITY_DESC = "Complexidade média";

    private static final Long SECONDARY_COMPLEXITY_ID = 2L;
    private static final String SECONDARY_COMPLEXITY_NAME = "Baixo";
    private static final String SECONDARY_COMPLEXITY_COLOR = "#00FF00";
    private static final String SECONDARY_COMPLEXITY_DESC = "Complexidade baixa";

    private static final Long NEW_COMPLEXITY_ID = 3L;
    private static final String NEW_COMPLEXITY_NAME = "Alta";
    private static final String NEW_COMPLEXITY_COLOR = "#FFA500";
    private static final String NEW_COMPLEXITY_DESC = "Complexidade alta";

    @Mock
    private ComplexityRepository complexityRepository;

    @InjectMocks
    private ComplexityService complexityService;

    private AutoCloseable mocks;

    private Complexity complexityPrimary;
    private Complexity complexitySecondary;
    private Complexity newComplexity;
    private Complexity savedComplexity;

    @BeforeEach
    public void setUp() {
        mocks = MockitoAnnotations.openMocks(this);

        complexityPrimary = TestUtils.createComplexity(
                PRIMARY_COMPLEXITY_ID,
                PRIMARY_COMPLEXITY_NAME,
                PRIMARY_COMPLEXITY_COLOR,
                PRIMARY_COMPLEXITY_DESC
        );

        complexitySecondary = TestUtils.createComplexity(
                SECONDARY_COMPLEXITY_ID,
                SECONDARY_COMPLEXITY_NAME,
                SECONDARY_COMPLEXITY_COLOR,
                SECONDARY_COMPLEXITY_DESC
        );

        newComplexity = TestUtils.createComplexity(
                null,
                NEW_COMPLEXITY_NAME,
                NEW_COMPLEXITY_COLOR,
                NEW_COMPLEXITY_DESC
        );

        savedComplexity = TestUtils.createComplexity(
                NEW_COMPLEXITY_ID,
                NEW_COMPLEXITY_NAME,
                NEW_COMPLEXITY_COLOR,
                NEW_COMPLEXITY_DESC
        );
    }

    @AfterEach
    public void tearDown() throws Exception {
        mocks.close();
    }

    @Test
    public void shouldReturnAllComplexities_whenFindAllIsCalled() {
        // GIVEN
        List<Complexity> complexities = List.of(complexityPrimary, complexitySecondary);
        Mockito.when(complexityRepository.findAll()).thenReturn(complexities);

        // WHEN
        List<Complexity> result = complexityService.findAll();

        // THEN
        assertThat(result)
                .hasSize(2)
                .containsExactlyInAnyOrder(complexityPrimary, complexitySecondary);

        Mockito.verify(complexityRepository, Mockito.times(1)).findAll();
    }

    @Test
    public void shouldReturnComplexity_whenFindByIdIsCalled() {
        // GIVEN
        Mockito.when(complexityRepository.findById(PRIMARY_COMPLEXITY_ID))
                .thenReturn(Optional.of(complexityPrimary));

        // WHEN
        Optional<Complexity> result = complexityService.findById(PRIMARY_COMPLEXITY_ID);

        // THEN
        assertThat(result)
                .isPresent()
                .containsSame(complexityPrimary);

        Mockito.verify(complexityRepository, Mockito.times(1)).findById(PRIMARY_COMPLEXITY_ID);
    }

    @Test
    public void shouldReturnEmpty_whenFindByInvalidId() {
        // GIVEN
        Long invalidId = 999L;
        Mockito.when(complexityRepository.findById(invalidId))
                .thenReturn(Optional.empty());

        // WHEN
        Optional<Complexity> result = complexityService.findById(invalidId);

        // THEN
        assertThat(result).isEmpty();
        Mockito.verify(complexityRepository, Mockito.times(1)).findById(invalidId);
    }

    @Test
    public void shouldSaveComplexitySucceed() {
        // GIVEN
        Mockito.when(complexityRepository.save(newComplexity))
                .thenReturn(savedComplexity);

        // WHEN
        Complexity result = complexityService.save(newComplexity);

        // THEN
        assertThat(result)
                .isNotNull()
                .extracting(Complexity::getId, Complexity::getName)
                .containsExactly(NEW_COMPLEXITY_ID, NEW_COMPLEXITY_NAME);

        Mockito.verify(complexityRepository, Mockito.times(1)).save(newComplexity);
    }
}