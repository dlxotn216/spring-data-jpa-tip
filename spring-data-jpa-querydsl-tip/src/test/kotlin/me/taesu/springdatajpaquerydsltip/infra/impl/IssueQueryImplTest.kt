package me.taesu.springdatajpaquerydsltip.infra.impl

import me.taesu.springdatajpaquerydsltip.domain.Issue
import me.taesu.springdatajpaquerydsltip.domain.IssueRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.junit.jupiter.SpringExtension

/**
 * Created by itaesu on 2022/05/16.
 *
 * @author Lee Tae Su
 * @version ConsentV3 v1.0 wB202203
 * @since ConsentV3 v1.0 wB202203
 */
@ExtendWith(SpringExtension::class)
@DataJpaTest
@ActiveProfiles("test")
internal class IssueQueryImplTest {
    @Autowired
    private lateinit var issueRepository: IssueRepository

    /*
    select
        issue0_.consent_key as col_0_0_,
        issue0_.id as col_1_0_,
        issue0_.name as col_2_0_,
        issue0_.description as col_3_0_
    from
        con_consent issue0_
    where
        lower(issue0_.name) like ? escape '!'
    order by
        issue0_.consent_key desc limit ?

    binding parameter [1] as [VARCHAR] - [%테스트!_%]
     */
    @Test
    fun `Contains Expression은 %% 문자를 알아서 붙이고 escape도 알아서 처리 된다`() {
        // given
        // when
        val result = issueRepository.findAllByPaginate(
            IssuePaginateCriteria(
                page = 1,
                size = 10,
                name = "테스트_"
            )
        )

        // then
        assertThat(result.content.size).isEqualTo(2)
    }

    /*
    select
        issue0_.consent_key as col_0_0_,
        issue0_.id as col_1_0_,
        issue0_.name as col_2_0_,
        issue0_.description as col_3_0_
    from
        con_consent issue0_
    where
        lower(issue0_.description) like ? escape '\'
    order by
        issue0_.consent_key desc limit ?

    binding parameter [1] as [VARCHAR] - [%설명\_%]
     */
    @Test
    fun `LIKE Expression은 %% 문자를 알아서 붙이지 않고 escape도 직접 처리 해야한다`() {
        // given
        // when
        val result = issueRepository.findAllByPaginate(
            IssuePaginateCriteria(
                page = 1,
                size = 10,
                description = "설명_"
            )
        )

        // then
        assertThat(result.content.size).isEqualTo(2)
    }

    /*
    select
        issue0_.consent_key as col_0_0_,
        issue0_.id as col_1_0_,
        issue0_.name as col_2_0_,
        issue0_.status as col_3_0_,
        issue0_.description as col_4_0_
    from
        con_consent issue0_
    where
        lower(issue0_.status) like ?
    order by
        issue0_.consent_key desc limit ?
    binding parameter [1] as [VARCHAR] - [%상태_%]
     */
    @Test
    fun `LIKE Expression은 escape 하지 않는 경우 원치않은 결과가 나올 수 있다`() {
        // given
        // when
        val result = issueRepository.findAllByPaginate(
            IssuePaginateCriteria(
                page = 1,
                size = 10,
                status = "상태_"
            )
        )

        // then
        assertThat(result.content.size).isEqualTo(3) // 상태 03도 결과에 나옴
    }

    /*
    select
        issue0_.consent_key as col_0_0_,
        issue0_.id as col_1_0_,
        issue0_.name as col_2_0_,
        issue0_.status as col_3_0_,
        issue0_.description as col_4_0_
    from
        con_consent issue0_
    order by
        issue0_.consent_key desc limit ?
     */
    @Test
    fun `Empty Expression에 대한 방어코드를 까먹지 말아야 한다`() {
        // given
        // when
        val result = issueRepository.findAllByPaginate(
            IssuePaginateCriteria(
                page = 1,
                size = 10,
            )
        )

        // then
        assertThat(result.content.size).isEqualTo(3)
    }

    @BeforeEach
    fun beforeEach() {
        issueRepository.saveAll(
            listOf(
                Issue(id = "TEST_01", name = "이슈 테스트_01", status = "상태_01", description = "이슈 테스트용 설명_01"),
                Issue(id = "TEST_02", name = "이슈 테스트_02", status = "상태_02", description = "이슈 테스트용 설명_02"),
                Issue(id = "TEST_03", name = "이슈 테스트 03", status = "상태 03", description = "이슈 테스트용 설명 03"),
            )
        )
    }
}