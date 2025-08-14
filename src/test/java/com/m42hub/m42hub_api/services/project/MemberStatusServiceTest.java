package com.m42hub.m42hub_api.services.project;

import com.m42hub.m42hub_api.project.entity.MemberStatus;
import com.m42hub.m42hub_api.project.repository.MemberStatusRepository;
import com.m42hub.m42hub_api.project.service.MemberStatusService;
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

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

public class MemberStatusServiceTest {
    private static final Logger logger = LoggerFactory.getLogger(MemberStatusServiceTest.class);

    private static final Long PRIMARY_MEMBER_STATUS_ID = 1L;
    private static final String PRIMARY_MEMBER_STATUS_NAME = "Aprovado";
    private static final String PRIMARY_MEMBER_STATUS_DESC = "Usuário solicitou para participar do projeto";

    private static final Long SECONDARY_MEMBER_STATUS_ID = 2L;
    private static final String SECONDARY_MEMBER_STATUS_NAME = "Recusado";
    private static final String SECONDARY_MEMBER_STATUS_DESC = "Usuário está participando do projeto";

    private static final Long NEW_MEMBER_STATUS_ID = 3L;
    private static final String NEW_MEMBER_STATUS_NAME = "Pendente";
    private static final String NEW_MEMBER_STATUS_DESC = "Aguardando aprovação";

    @Mock
    private MemberStatusRepository memberStatusRepository;

    @InjectMocks
    private MemberStatusService memberStatusService;

    private AutoCloseable mocks;

    private MemberStatus memberStatusPrimary;
    private MemberStatus memberStatusSecondary;
    private MemberStatus newMemberStatus;
    private MemberStatus savedMemberStatus;

    @BeforeEach
    public void setUp() {
        mocks = MockitoAnnotations.openMocks(this);
        memberStatusPrimary = TestUtils.createMemberStatus(PRIMARY_MEMBER_STATUS_ID, PRIMARY_MEMBER_STATUS_NAME, PRIMARY_MEMBER_STATUS_DESC);
        memberStatusSecondary = TestUtils.createMemberStatus(SECONDARY_MEMBER_STATUS_ID, SECONDARY_MEMBER_STATUS_NAME, SECONDARY_MEMBER_STATUS_DESC);

        newMemberStatus = TestUtils.createMemberStatus(null, NEW_MEMBER_STATUS_NAME, NEW_MEMBER_STATUS_DESC);
        savedMemberStatus = TestUtils.createMemberStatus(NEW_MEMBER_STATUS_ID, NEW_MEMBER_STATUS_NAME, NEW_MEMBER_STATUS_DESC);
    }

    @AfterEach
    public void tearDown() throws Exception {
        mocks.close();
    }

    @Test
    public void shouldReturnAllMemberStatus_whenFindAllIsCalled() {
        // GIVEN
        List<MemberStatus> memberStatuses = List.of(memberStatusPrimary, memberStatusSecondary);
        Mockito.when(memberStatusRepository.findAll()).thenReturn(memberStatuses);

        // WHEN
        List<MemberStatus> result = memberStatusService.findAll();

        // THEN
        assertThat(result)
                .hasSize(2)
                .containsExactlyInAnyOrder(memberStatusPrimary, memberStatusSecondary);
        Mockito.verify(memberStatusRepository, Mockito.times(1)).findAll();
    }

    @Test
    public void shouldReturnMemberStatus_whenFindByIdIsCalled() {
        // GIVEN
        Mockito.when(memberStatusRepository.findById(PRIMARY_MEMBER_STATUS_ID))
                .thenReturn(Optional.of(memberStatusPrimary));

        // WHEN
        Optional<MemberStatus> result = memberStatusService.findById(PRIMARY_MEMBER_STATUS_ID);

        // THEN
        assertThat(result)
                .isPresent()
                .containsSame(memberStatusPrimary);
        Mockito.verify(memberStatusRepository, Mockito.times(1)).findById(PRIMARY_MEMBER_STATUS_ID);
    }

    @Test
    public void shouldReturnEmpty_whenFindByInvalidId() {
        // GIVEN
        Long invalidId = 999L;
        Mockito.when(memberStatusRepository.findById(invalidId))
                .thenReturn(Optional.empty());

        // WHEN
        Optional<MemberStatus> result = memberStatusService.findById(invalidId);

        // THEN
        assertThat(result).isEmpty();
        Mockito.verify(memberStatusRepository, Mockito.times(1)).findById(invalidId);
    }

    @Test
    public void shouldSaveMemberStatusSucceed() {
        // GIVEN
        Mockito.when(memberStatusRepository.save(newMemberStatus))
                .thenReturn(savedMemberStatus);

        // WHEN
        MemberStatus result = memberStatusService.save(newMemberStatus);

        // THEN
        assertThat(result)
                .isNotNull()
                .extracting(MemberStatus::getId, MemberStatus::getName)
                .containsExactly(NEW_MEMBER_STATUS_ID, NEW_MEMBER_STATUS_NAME);
        Mockito.verify(memberStatusRepository, Mockito.times(1)).save(newMemberStatus);
    }
}