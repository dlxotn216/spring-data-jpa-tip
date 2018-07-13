package test.demo.group;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author Lee Tae Su
 * @version 1.0
 * @project spring-data-jpa-massive-delete
 * @since 2018-07-13
 */
public interface GroupRepository extends JpaRepository<Group, Long> {
	/**
	 * Group과 GroupUser의 mapping에서 cascade 설정이 ALL로 되어있으므로
	 * 아래의 메소드 실행 시 GroupUser에 대한 삭제도 진행된다
	 * 
	 * Group과 GroupUser에 대한 삭제는 건 by 건으로 진행됨을 주의
	 * @param groupKeys 삭제 대상 groupKeys
	 */
	@Transactional
	@Modifying
	void deleteAllByGroupKeyIn(@Param("groupKeys") List<Long> groupKeys);
	
	/**
	 * Group 목록에 대해 일괄 삭제한다.
	 * GroupUser와 Mapping 되어있으므로 반드시 아래 메소드를 먼저 호출 하여야 한다.
	 * 
	 * Group과 GroupUser에 대한 삭제는 일괄로 처리된다.
	 * @see GroupUserRepository#deleteAllByGroupKeyInByQuery(List) 
	 * @param groupKeys 삭제 대상 groupKeys
	 */
	@Transactional
	@Modifying
	@Query(value = "DELETE FROM Group g WHERE g.groupKey IN (:groupKeys)")
	void deleteAllByGroupKeyInByQuery(@Param("groupKeys") List<Long> groupKeys);
}
