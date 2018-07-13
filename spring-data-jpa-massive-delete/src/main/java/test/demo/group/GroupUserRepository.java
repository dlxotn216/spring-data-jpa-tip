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
public interface GroupUserRepository extends JpaRepository<GroupUser, Long> {
	
	List<GroupUser> findGroupUserByGroup(Group group);
	
	@Transactional
	@Modifying
	@Query(value = "DELETE FROM GroupUser gu WHERE gu.group.groupKey IN (:groupKeys)")
	void deleteAllByGroupKeyInByQuery(@Param("groupKeys") List<Long> groupKeys);
	
	@Transactional
	@Modifying
	@Query(value = "DELETE FROM GroupUser gu WHERE gu.group.groupKey = :groupKey AND gu.user.userKey IN (:userKeys)")
	void deleteAllByGroupKeyAndUserKeyInByQueryy(@Param("groupKey") Long groupKey,
												 @Param("userKeys") List<Long> userKeys);
}
