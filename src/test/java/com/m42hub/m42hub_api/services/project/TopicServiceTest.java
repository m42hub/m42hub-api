package com.m42hub.m42hub_api.services.project;

import com.m42hub.m42hub_api.project.entity.Topic;
import com.m42hub.m42hub_api.project.repository.TopicRepository;
import com.m42hub.m42hub_api.project.service.TopicService;
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

public class TopicServiceTest {
    private static final Logger logger = LoggerFactory.getLogger(TopicServiceTest.class);

    private static final Long PRIMARY_TOPIC_ID = 1L;
    private static final String PRIMARY_TOPIC_NAME = "PrimaryTopic";
    private static final String PRIMARY_TOPIC_HEX_COLOR = "#000000";
    private static final String PRIMARY_TOPIC_DESC = "Black colored";

    private static final Long SECONDARY_TOPIC_ID = 2L;
    private static final String SECONDARY_TOPIC_NAME = "SecondaryTopic";
    private static final String SECONDARY_TOPIC_HEX_COLOR = "#FFFFFF";
    private static final String SECONDARY_TOPIC_DESC = "White colored";

    private static final Long NEW_TOPIC_ID = 3L;
    private static final String NEW_TOPIC_NAME = "NewTopic";
    private static final String NEW_TOPIC_HEX_COLOR = "#123456";
    private static final String NEW_TOPIC_DESC = "New topic";

    private static final String UPDATED_COLOR = "#FF0000";

    @Mock
    private TopicRepository topicRepository;

    @InjectMocks
    private TopicService topicService;

    private AutoCloseable mocks;

    private Topic topicPrimary;
    private Topic topicSecondary;
    private Topic newTopic;
    private Topic savedTopic;

    @BeforeEach
    public void setUp() {
        mocks = MockitoAnnotations.openMocks(this);
        topicPrimary = TestUtils.createTopic(PRIMARY_TOPIC_ID, PRIMARY_TOPIC_NAME, PRIMARY_TOPIC_HEX_COLOR, PRIMARY_TOPIC_DESC);
        topicSecondary = TestUtils.createTopic(SECONDARY_TOPIC_ID, SECONDARY_TOPIC_NAME, SECONDARY_TOPIC_HEX_COLOR, SECONDARY_TOPIC_DESC);

        newTopic = TestUtils.createTopic(null, NEW_TOPIC_NAME, NEW_TOPIC_HEX_COLOR, NEW_TOPIC_DESC);
        savedTopic = TestUtils.createTopic(NEW_TOPIC_ID, NEW_TOPIC_NAME, NEW_TOPIC_HEX_COLOR, NEW_TOPIC_DESC);
    }

    @AfterEach
    public void tearDown() throws Exception {
        mocks.close();
    }

    @Test
    public void shouldReturnAllTopics_whenFindAllIsCalled() {
        // GIVEN
        List<Topic> topics = List.of(topicPrimary, topicSecondary);
        Mockito.when(topicRepository.findAll()).thenReturn(topics);

        // WHEN
        List<Topic> result = topicService.findAll();

        // THEN
        assertThat(result)
                .hasSize(2)
                .containsExactlyInAnyOrder(topicPrimary, topicSecondary);
        Mockito.verify(topicRepository, Mockito.times(1)).findAll();
    }

    @Test
    public void shouldReturnTopic_whenFindByIdIsCalled() {
        // GIVEN
        Mockito.when(topicRepository.findById(PRIMARY_TOPIC_ID))
                .thenReturn(Optional.of(topicPrimary));

        // WHEN
        Optional<Topic> result = topicService.findById(PRIMARY_TOPIC_ID);

        // THEN
        assertThat(result)
                .isPresent()
                .containsSame(topicPrimary);
        Mockito.verify(topicRepository, Mockito.times(1)).findById(PRIMARY_TOPIC_ID);
    }

    @Test
    public void shouldReturnEmpty_whenFindByInvalidId() {
        // GIVEN
        Long invalidId = 999L;
        Mockito.when(topicRepository.findById(invalidId))
                .thenReturn(Optional.empty());

        // WHEN
        Optional<Topic> result = topicService.findById(invalidId);

        // THEN
        assertThat(result).isEmpty();
        Mockito.verify(topicRepository, Mockito.times(1)).findById(invalidId);
    }

    @Test
    public void shouldSaveTopicSucceed() {
        // GIVEN
        Mockito.when(topicRepository.save(newTopic))
                .thenReturn(savedTopic);

        // WHEN
        Topic result = topicService.save(newTopic);

        // THEN
        assertThat(result)
                .isNotNull()
                .extracting(Topic::getId, Topic::getName)
                .containsExactly(NEW_TOPIC_ID, NEW_TOPIC_NAME);
        Mockito.verify(topicRepository, Mockito.times(1)).save(newTopic);
    }

    @Test
    public void shouldChangeColorSucceed() {
        // GIVEN
        Mockito.when(topicRepository.findById(PRIMARY_TOPIC_ID))
                .thenReturn(Optional.of(topicPrimary));
        Mockito.when(topicRepository.save(topicPrimary))
                .thenReturn(topicPrimary);

        // WHEN
        Optional<Topic> result = topicService.changeColor(PRIMARY_TOPIC_ID, UPDATED_COLOR);

        // THEN
        assertThat(result).isPresent();
        Topic actual = result.get();
        assertThat(actual).isSameAs(topicPrimary);
        assertThat(actual.getHexColor()).isEqualTo(UPDATED_COLOR);
        Mockito.verify(topicRepository, Mockito.times(1)).findById(PRIMARY_TOPIC_ID);
        Mockito.verify(topicRepository, Mockito.times(1)).save(topicPrimary);
    }

    @Test
    public void shouldNotChangeColor_whenTopicNotFound() {
        // GIVEN
        Long invalidId = 999L;
        Mockito.when(topicRepository.findById(invalidId))
                .thenReturn(Optional.empty());

        // WHEN
        Optional<Topic> result = topicService.changeColor(invalidId, UPDATED_COLOR);

        // THEN
        assertThat(result).isEmpty();
        Mockito.verify(topicRepository, Mockito.times(1)).findById(invalidId);
        Mockito.verify(topicRepository, Mockito.never()).save(Mockito.any());
    }
}