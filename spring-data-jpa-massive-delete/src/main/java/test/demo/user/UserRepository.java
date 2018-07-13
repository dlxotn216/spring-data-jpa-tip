package test.demo.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.AbstractJpaQuery;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author Lee Tae Su
 * @version 1.0
 * @project spring-data-jpa-massive-delete
 * @since 2018-07-13
 */
public interface UserRepository extends JpaRepository<User, Long> {
	
	/**
	 * 아래 메소드에서 건 by 건으로 삭제를한다.
	 * @see org.springframework.data.jpa.repository.query.JpaQueryExecution.DeleteExecution#doExecute(AbstractJpaQuery, Object[])
	 *
	 * @param userKeys 삭제 대상 User key 목록
	 */
	@Transactional
	@Modifying
	void deleteAllByUserKeyIn(@Param("userKeys") List<Long> userKeys);
	
	/**
	 * JPQL로 삭제하여야 하나의 쿼리가 발행 됨
	 * 
	 * @param userKeys 삭제 대상 User key 목록
	 */
	@Transactional
	@Modifying
	@Query(value = "DELETE FROM User u WHERE u.userKey IN (:userKeys)")
	void deleteAllByUserKeyInByQuery(@Param("userKeys") List<Long> userKeys);
	
}
