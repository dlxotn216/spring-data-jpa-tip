package test.demo.item;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by taesu on 2018-07-13.
 */
public interface ItemRepository extends JpaRepository<Item, Long> {
    @Transactional
    @Modifying
    @Query(value = "UPDATE Item i SET i.deleted = true WHERE i.group.groupKey IN (:groupKeys)")
    void deleteItemsByGroupKeyInAsQuery(@Param("groupKeys") List<Long> groupKeys);
}
