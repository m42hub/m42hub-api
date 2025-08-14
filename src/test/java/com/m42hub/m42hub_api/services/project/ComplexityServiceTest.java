package com.m42hub.m42hub_api.services.project;

import com.m42hub.m42hub_api.project.entity.Complexity;
import com.m42hub.m42hub_api.project.service.ComplexityService;
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

    @Mock
    private ComplexityService complexityService;

    private AutoCloseable mocks;

    private Complexity complexityPrimary;
    private Complexity complexitySecondary;

    @BeforeEach
    public void setUp() {
        mocks = MockitoAnnotations.openMocks(this);
        complexityPrimary = TestUtils.createComplexity(PRIMARY_COMPLEXITY_ID, PRIMARY_COMPLEXITY_NAME, PRIMARY_COMPLEXITY_COLOR, PRIMARY_COMPLEXITY_DESC);
        complexitySecondary = TestUtils.createComplexity(SECONDARY_COMPLEXITY_ID, SECONDARY_COMPLEXITY_NAME, SECONDARY_COMPLEXITY_COLOR, SECONDARY_COMPLEXITY_DESC);
    }

    @AfterEach
    public void tearDown() throws Exception {
        mocks.close();
    }

    @Test
    public void shouldReturnAllComplexities_whenFindAllIsCalled() {
        // GIVEN
        List<Complexity> complexities = List.of(complexityPrimary, complexitySecondary);
        Mockito.when(complexityService.findAll()).thenReturn(complexities);

        // WHEN
        List<Complexity> result = complexityService.findAll();


        // THEN
        Assertions.assertEquals(complexities, result);
        Mockito.verify(complexityService, Mockito.times(1)).findAll();
    }

    @Test
    public void shouldReturnComplexity_whenFindByIdIsCalled() {
        // GIVEN
        Mockito.when(complexityService.findById(PRIMARY_COMPLEXITY_ID)).thenReturn(Optional.of(complexityPrimary));

        // WHEN
        Optional<Complexity> result = complexityService.findById(PRIMARY_COMPLEXITY_ID);

        // THEN
        Assertions.assertEquals(Optional.of(complexityPrimary), result);
        Mockito.verify(complexityService, Mockito.times(1)).findById(PRIMARY_COMPLEXITY_ID);
    }

    @Test
    public void shouldSaveComplexitySucceed() {
        // GIVEN
        Mockito.when(complexityService.save(complexityPrimary)).thenReturn(complexityPrimary);

        // WHEN
        Complexity result = complexityService.save(complexityPrimary);

        // THEN
        Assertions.assertEquals(complexityPrimary, result);
        Mockito.verify(complexityService, Mockito.times(1)).save(complexityPrimary);
    }
}