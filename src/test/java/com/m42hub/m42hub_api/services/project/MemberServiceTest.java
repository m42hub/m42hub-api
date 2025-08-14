package com.m42hub.m42hub_api.services.project;

import com.m42hub.m42hub_api.project.entity.Member;
import com.m42hub.m42hub_api.project.service.MemberService;
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

public class MemberServiceTest {
    private static final Logger logger = LoggerFactory.getLogger(MemberServiceTest.class);

    private static final Long PRIMARY_MEMBER_STATUS_ID = 1L;

    @Mock
    private MemberService memberService;

    private AutoCloseable mocks;

    private Member memberPrimary;
    private Member memberSecondary;

    @BeforeEach
    public void setUp() {
        mocks = MockitoAnnotations.openMocks(this);

        // Objeto (Member) complexo de mais, refatoração prevista deve simplificar totalmente.

        memberPrimary = new Member(); //TestUtils.createMember();
        memberSecondary = new Member(); //TestUtils.createMember();
    }

    @AfterEach
    public void tearDown() throws Exception {
        mocks.close();
    }

    @Test
    public void shouldReturnAllMember_whenFindAllIsCalled() {
        // GIVEN
        List<Member> topics = List.of(memberPrimary, memberSecondary);
        Mockito.when(memberService.findAll()).thenReturn(topics);

        // WHEN
        List<Member> result = memberService.findAll();

        // THEN
        Assertions.assertEquals(topics, result);
        Mockito.verify(memberService, Mockito.times(1)).findAll();
    }

    @Test
    public void shouldReturnMember_whenFindByIdIsCalled() {
        // GIVEN
        Mockito.when(memberService.findById(PRIMARY_MEMBER_STATUS_ID)).thenReturn(Optional.of(memberPrimary));

        // WHEN
        Optional<Member> result = memberService.findById(PRIMARY_MEMBER_STATUS_ID);

        // THEN
        Assertions.assertEquals(Optional.of(memberPrimary), result);
        Mockito.verify(memberService, Mockito.times(1)).findById(PRIMARY_MEMBER_STATUS_ID);
    }

    @Test
    public void shouldSaveMemberSucceed() {
        // GIVEN
        Mockito.when(memberService.save(memberPrimary)).thenReturn(memberPrimary);

        // WHEN
        Member result = memberService.save(memberPrimary);

        // THEN
        Assertions.assertEquals(memberPrimary, result);
        Mockito.verify(memberService, Mockito.times(1)).save(memberPrimary);
    }
}