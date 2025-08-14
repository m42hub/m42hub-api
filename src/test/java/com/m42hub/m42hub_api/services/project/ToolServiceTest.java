package com.m42hub.m42hub_api.services.project;

import com.m42hub.m42hub_api.project.entity.Tool;
import com.m42hub.m42hub_api.project.repository.ToolRepository;
import com.m42hub.m42hub_api.project.service.ToolService;
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

    private static final Long NEW_TOOL_ID = 3L;
    private static final String NEW_TOOL_NAME = "NewTool";
    private static final String NEW_TOOL_HEX_COLOR = "#123456";
    private static final String NEW_TOOL_DESC = "New tool";

    private static final String UPDATED_COLOR = "#FF0000";

    @Mock
    private ToolRepository toolRepository;

    @InjectMocks
    private ToolService toolService;

    private AutoCloseable mocks;

    private Tool toolPrimary;
    private Tool toolSecondary;
    private Tool newTool;
    private Tool savedTool;
    private Tool updatedTool;

    @BeforeEach
    public void setUp() {
        mocks = MockitoAnnotations.openMocks(this);
        toolPrimary = TestUtils.createTool(PRIMARY_TOOL_ID, PRIMARY_TOOL_NAME, PRIMARY_TOOL_HEX_COLOR, PRIMARY_TOOL_DESC);
        toolSecondary = TestUtils.createTool(SECONDARY_TOOL_ID, SECONDARY_TOOL_NAME, SECONDARY_TOOL_HEX_COLOR, SECONDARY_TOOL_DESC);

        newTool = TestUtils.createTool(null, NEW_TOOL_NAME, NEW_TOOL_HEX_COLOR, NEW_TOOL_DESC);
        savedTool = TestUtils.createTool(NEW_TOOL_ID, NEW_TOOL_NAME, NEW_TOOL_HEX_COLOR, NEW_TOOL_DESC);
        updatedTool = TestUtils.createTool(PRIMARY_TOOL_ID, PRIMARY_TOOL_NAME, UPDATED_COLOR, PRIMARY_TOOL_DESC);
    }

    @AfterEach
    public void tearDown() throws Exception {
        mocks.close();
    }

    @Test
    public void shouldReturnAllTools_whenFindAllIsCalled() {
        // GIVEN
        List<Tool> tools = List.of(toolPrimary, toolSecondary);
        Mockito.when(toolRepository.findAll()).thenReturn(tools);

        // WHEN
        List<Tool> result = toolService.findAll();

        // THEN
        assertThat(result)
                .hasSize(2)
                .containsExactlyInAnyOrder(toolPrimary, toolSecondary);
        Mockito.verify(toolRepository, Mockito.times(1)).findAll();
    }

    @Test
    public void shouldReturnTool_whenFindByIdIsCalled() {
        // GIVEN
        Mockito.when(toolRepository.findById(PRIMARY_TOOL_ID))
                .thenReturn(Optional.of(toolPrimary));

        // WHEN
        Optional<Tool> result = toolService.findById(PRIMARY_TOOL_ID);

        // THEN
        assertThat(result)
                .isPresent()
                .containsSame(toolPrimary);
        Mockito.verify(toolRepository, Mockito.times(1)).findById(PRIMARY_TOOL_ID);
    }

    @Test
    public void shouldReturnEmpty_whenFindByInvalidId() {
        // GIVEN
        Long invalidId = 999L;
        Mockito.when(toolRepository.findById(invalidId))
                .thenReturn(Optional.empty());

        // WHEN
        Optional<Tool> result = toolService.findById(invalidId);

        // THEN
        assertThat(result).isEmpty();
        Mockito.verify(toolRepository, Mockito.times(1)).findById(invalidId);
    }

    @Test
    public void shouldSaveToolSucceed() {
        // GIVEN
        Mockito.when(toolRepository.save(newTool))
                .thenReturn(savedTool);

        // WHEN
        Tool result = toolService.save(newTool);

        // THEN
        assertThat(result)
                .isNotNull()
                .extracting(Tool::getId, Tool::getName)
                .containsExactly(NEW_TOOL_ID, NEW_TOOL_NAME);
        Mockito.verify(toolRepository, Mockito.times(1)).save(newTool);
    }

    @Test
    public void shouldChangeColorSucceed() {
        // GIVEN
        String newColor = "#FF0000";

        Mockito.when(toolRepository.findById(PRIMARY_TOOL_ID))
                .thenReturn(Optional.of(toolPrimary));

        toolPrimary.setHexColor(newColor);

        Mockito.when(toolRepository.save(toolPrimary))
                .thenReturn(toolPrimary);

        // WHEN
        Optional<Tool> result = toolService.changeColor(PRIMARY_TOOL_ID, newColor);

        // THEN
        assertThat(result)
                .isPresent()
                .containsSame(toolPrimary);
        assertThat(toolPrimary.getHexColor()).isEqualTo(newColor);
        Mockito.verify(toolRepository, Mockito.times(1)).findById(PRIMARY_TOOL_ID);
        Mockito.verify(toolRepository, Mockito.times(1)).save(toolPrimary);
    }

    @Test
    public void shouldNotChangeColor_whenToolNotFound() {
        // GIVEN
        Long invalidId = 999L;
        Mockito.when(toolRepository.findById(invalidId))
                .thenReturn(Optional.empty());

        // WHEN
        Optional<Tool> result = toolService.changeColor(invalidId, UPDATED_COLOR);

        // THEN
        assertThat(result).isEmpty();
        Mockito.verify(toolRepository, Mockito.times(1)).findById(invalidId);
        Mockito.verify(toolRepository, Mockito.never()).save(Mockito.any());
    }
}