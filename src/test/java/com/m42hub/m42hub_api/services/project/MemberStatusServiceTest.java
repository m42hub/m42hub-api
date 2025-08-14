package com.m42hub.m42hub_api.services.project;

import com.m42hub.m42hub_api.project.entity.MemberStatus;
import com.m42hub.m42hub_api.project.service.MemberStatusService;
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

public class MemberStatusServiceTest {
    private static final Logger logger = LoggerFactory.getLogger(MemberStatusServiceTest.class);

    private static final Long PRIMARY_MEMBER_STATUS_ID = 1L;
    private static final String PRIMARY_MEMBER_STATUS_NAME = "Aprovado";
    private static final String PRIMARY_MEMBER_STATUS_DESC = "Usuário solicitou para participar do projeto";

    private static final Long SECONDARY_MEMBER_STATUS_ID = 2L;
    private static final String SECONDARY_MEMBER_STATUS_NAME = "Recusado";
    private static final String SECONDARY_MEMBER_STATUS_DESC = "Usuário está participando do projeto";

    @Mock
    private MemberStatusService memberStatusService;

    private AutoCloseable mocks;

    private MemberStatus memberStatusPrimary;
    private MemberStatus memberStatusSecondary;

    @BeforeEach
    public void setUp() {
        mocks = MockitoAnnotations.openMocks(this);
        memberStatusPrimary = TestUtils.createMemberStatus(PRIMARY_MEMBER_STATUS_ID, PRIMARY_MEMBER_STATUS_NAME, PRIMARY_MEMBER_STATUS_DESC);
        memberStatusSecondary = TestUtils.createMemberStatus(SECONDARY_MEMBER_STATUS_ID, SECONDARY_MEMBER_STATUS_NAME, SECONDARY_MEMBER_STATUS_DESC);
    }

    @AfterEach
    public void tearDown() throws Exception {
        mocks.close();
    }

    @Test
    public void shouldReturnAllMemberStatus_whenFindAllIsCalled() {
        // GIVEN
        List<MemberStatus> topics = List.of(memberStatusPrimary, memberStatusSecondary);
        Mockito.when(memberStatusService.findAll()).thenReturn(topics);

        // WHEN
        List<MemberStatus> result = memberStatusService.findAll();

        // THEN
        Assertions.assertEquals(topics, result);
        Mockito.verify(memberStatusService, Mockito.times(1)).findAll();
    }

    @Test
    public void shouldReturnMemberStatus_whenFindByIdIsCalled() {
        // GIVEN
        Mockito.when(memberStatusService.findById(PRIMARY_MEMBER_STATUS_ID)).thenReturn(Optional.of(memberStatusPrimary));

        // WHEN
        Optional<MemberStatus> result = memberStatusService.findById(PRIMARY_MEMBER_STATUS_ID);

        // THEN
        Assertions.assertEquals(Optional.of(memberStatusPrimary), result);
        Mockito.verify(memberStatusService, Mockito.times(1)).findById(PRIMARY_MEMBER_STATUS_ID);
    }

    @Test
    public void shouldSaveMemberStatusSucceed() {
        // GIVEN
        Mockito.when(memberStatusService.save(memberStatusPrimary)).thenReturn(memberStatusPrimary);

        // WHEN
        MemberStatus result = memberStatusService.save(memberStatusPrimary);

        // THEN
        Assertions.assertEquals(memberStatusPrimary, result);
        Mockito.verify(memberStatusService, Mockito.times(1)).save(memberStatusPrimary);
    }
}