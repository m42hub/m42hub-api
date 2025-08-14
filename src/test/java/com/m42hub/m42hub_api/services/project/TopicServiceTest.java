package com.m42hub.m42hub_api.services.project;

import com.m42hub.m42hub_api.project.entity.Topic;
import com.m42hub.m42hub_api.project.service.TopicService;
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

    @Mock
    private TopicService topicService;

    private AutoCloseable mocks;

    private Topic topicPrimary;
    private Topic topicSecondary;

    @BeforeEach
    public void setUp() {
        mocks = MockitoAnnotations.openMocks(this);
        topicPrimary = TestUtils.createTopic(PRIMARY_TOPIC_ID, PRIMARY_TOPIC_NAME, PRIMARY_TOPIC_HEX_COLOR, PRIMARY_TOPIC_DESC);
        topicSecondary = TestUtils.createTopic(SECONDARY_TOPIC_ID, SECONDARY_TOPIC_NAME, SECONDARY_TOPIC_HEX_COLOR, SECONDARY_TOPIC_DESC);
    }

    @AfterEach
    public void tearDown() throws Exception {
        mocks.close();
    }

    @Test
    public void shouldReturnAllTopics_whenFindAllIsCalled() {
        // GIVEN
        List<Topic> topics = List.of(topicPrimary, topicSecondary);
        Mockito.when(topicService.findAll()).thenReturn(topics);

        // WHEN
        List<Topic> result = topicService.findAll();

        // THEN
        Assertions.assertEquals(topics, result);
        Mockito.verify(topicService, Mockito.times(1)).findAll();
    }

    @Test
    public void shouldReturnTopic_whenFindByIdIsCalled() {
        // GIVEN
        Mockito.when(topicService.findById(PRIMARY_TOPIC_ID)).thenReturn(Optional.of(topicPrimary));

        // WHEN
        Optional<Topic> result = topicService.findById(PRIMARY_TOPIC_ID);

        // THEN
        Assertions.assertEquals(Optional.of(topicPrimary), result);
        Mockito.verify(topicService, Mockito.times(1)).findById(PRIMARY_TOPIC_ID);
    }

    @Test
    public void shouldSaveTopicSucceed() {
        // GIVEN
        Mockito.when(topicService.save(topicPrimary)).thenReturn(topicPrimary);

        // WHEN
        Topic result = topicService.save(topicPrimary);

        // THEN
        Assertions.assertEquals(topicPrimary, result);
        Mockito.verify(topicService, Mockito.times(1)).save(topicPrimary);
    }

    @Test
    public void shouldChangeColorSucceed() {
        // GIVEN
        Mockito.when(topicService.changeColor(PRIMARY_TOPIC_ID, PRIMARY_TOPIC_HEX_COLOR))
                .thenReturn(Optional.of(topicPrimary));

        // WHEN
        Optional<Topic> result = topicService.changeColor(PRIMARY_TOPIC_ID, PRIMARY_TOPIC_HEX_COLOR);

        // THEN
        Assertions.assertEquals(Optional.of(topicPrimary), result);
        Mockito.verify(topicService, Mockito.times(1)).changeColor(PRIMARY_TOPIC_ID, PRIMARY_TOPIC_HEX_COLOR);
    }
}