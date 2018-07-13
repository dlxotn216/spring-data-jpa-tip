package test.demo.group;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by taesu on 2018-07-13.
 */
public interface GroupRepository extends JpaRepository<Group, Long> {

    @Query(value = "SELECT g FROM Group g WHERE g.deleted = false")
    List<Group> findNotDeletedAll();

    @Query(value = "SELECT g FROM Group g WHERE g.deleted = false")
    List<Group> findNotDeletedAll(Sort sort);

    @Query(value = "SELECT g FROM Group g WHERE g.deleted = false")
    Page<Group> findNotDeletedAll(Pageable pageable);

    @Transactional
    @Modifying
    @Query(value = "UPDATE Group g SET g.deleted = true WHERE g.groupKey IN (:groupKeys)")
    void deleteGroupsByGroupKeyInAsQuery(@Param("groupKeys") List<Long> groupKeys);
}
