package com.m42hub.m42hub_api.services.project;

import com.m42hub.m42hub_api.project.entity.Tool;
import com.m42hub.m42hub_api.project.service.ToolService;
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

public class ToolServiceTest {
    private static final Logger logger = LoggerFactory.getLogger(ToolServiceTest.class);

    private static final Long PRIMARY_TOOL_ID = 1L;
    private static final String PRIMARY_TOOL_NAME = "PrimaryTool";
    private static final String PRIMARY_TOOL_HEX_COLOR = "#000000";
    private static final String PRIMARY_TOOL_DESC = "Black colored";

    private static final Long SECONDARY_TOOL_ID = 2L;
    private static final String SECONDARY_TOOL_NAME = "SecondaryTool";
    private static final String SECONDARY_TOOL_HEX_COLOR = "#FFFFFF";
    private static final String SECONDARY_TOOL_DESC = "White colored";

    @Mock
    private ToolService toolService;

    private AutoCloseable mocks;

    private Tool toolPrimary;
    private Tool toolSecondary;

    @BeforeEach
    public void setUp() {
        mocks = MockitoAnnotations.openMocks(this);
        toolPrimary = TestUtils.createTool(PRIMARY_TOOL_ID, PRIMARY_TOOL_NAME, PRIMARY_TOOL_HEX_COLOR, PRIMARY_TOOL_DESC);
        toolSecondary = TestUtils.createTool(SECONDARY_TOOL_ID, SECONDARY_TOOL_NAME, SECONDARY_TOOL_HEX_COLOR, SECONDARY_TOOL_DESC);
    }

    @AfterEach
    public void tearDown() throws Exception {
        mocks.close();
    }

    @Test
    public void shouldReturnAllTools_whenFindAllIsCalled() {
        // GIVEN
        List<Tool> tools = List.of(toolPrimary, toolSecondary);
        Mockito.when(toolService.findAll()).thenReturn(tools);

        // WHEN
        List<Tool> result = toolService.findAll();

        // THEN
        Assertions.assertEquals(tools, result);
        Mockito.verify(toolService, Mockito.times(1)).findAll();
    }

    @Test
    public void shouldReturnTool_whenFindByIdIsCalled() {
        // GIVEN
        Mockito.when(toolService.findById(PRIMARY_TOOL_ID)).thenReturn(Optional.of(toolPrimary));

        // WHEN
        Optional<Tool> result = toolService.findById(PRIMARY_TOOL_ID);

        // THEN
        Assertions.assertEquals(Optional.of(toolPrimary), result);
        Mockito.verify(toolService, Mockito.times(1)).findById(PRIMARY_TOOL_ID);
    }

    @Test
    public void shouldSaveToolSucceed() {
        // GIVEN
        Mockito.when(toolService.save(toolPrimary)).thenReturn(toolPrimary);

        // WHEN
        Tool result = toolService.save(toolPrimary);

        // THEN
        Assertions.assertEquals(toolPrimary, result);
        Mockito.verify(toolService, Mockito.times(1)).save(toolPrimary);
    }

    @Test
    public void shouldChangeColorSucceed() {
        // GIVEN
        Mockito.when(toolService.changeColor(PRIMARY_TOOL_ID, PRIMARY_TOOL_HEX_COLOR))
                .thenReturn(Optional.of(toolPrimary));

        // WHEN
        Optional<Tool> result = toolService.changeColor(PRIMARY_TOOL_ID, PRIMARY_TOOL_HEX_COLOR);

        // THEN
        Assertions.assertEquals(Optional.of(toolPrimary), result);
        Mockito.verify(toolService, Mockito.times(1)).changeColor(PRIMARY_TOOL_ID, PRIMARY_TOOL_HEX_COLOR);
    }
}