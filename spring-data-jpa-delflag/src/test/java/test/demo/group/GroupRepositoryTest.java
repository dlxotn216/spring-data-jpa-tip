package test.demo.group;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.junit4.SpringRunner;
import test.demo.item.ItemRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by taesu on 2018-07-13.
 */
@RunWith(SpringRunner.class)
@DataJpaTest
public class GroupRepositoryTest {
    @Autowired
    private GroupRepository groupRepository;

    @Autowired
    private ItemRepository itemRepository;
    //https://stackoverflow.com/questions/14936266/spring-data-jpa-manual-commit-transaction-and-restart-new-one
    //http://www.baeldung.com/spring-data-jpa-query
    //https://stackoverflow.com/questions/19323557/handling-soft-deletes-with-spring-jpa
    @Test
    public void QueryTest() {
        //Given
        groupRepository.saveAll(IntStream.range(1, 10)
                .mapToObj(value -> new Group(null, "Group " + value, "Desc " + value, false, new ArrayList<>()))
                .collect(Collectors.toList()));

        //When
        List<Group> groups = groupRepository.findAll();
//        groups.forEach(Group::deleteGroup);
        //먼저 삭제 로직을 수행하면 JPA의 변경 감지가 되어 update query가 10번 수행된 후에 UPDATE IN query가 수행 됨

        //아래 메소드를 수행하지 않으면 update 쿼리가 건 바이 건으로 처리 됨
        itemRepository.deleteItemsByGroupKeyInAsQuery(groups.stream().map(Group::getGroupKey).collect(Collectors.toList()));
        groupRepository.deleteGroupsByGroupKeyInAsQuery(groups.stream().map(Group::getGroupKey).collect(Collectors.toList()));

        groups.forEach(Group::deleteGroup);//soft delete 처리 후 맞추거나 새로 findAll을 해야할 듯


        //Then
        assertThat(groupRepository.findNotDeletedAll().size()).isEqualTo(0);
        assertThat(groupRepository.findNotDeletedAll(Sort.by(Sort.Order.asc("groupKey"))).size()).isEqualTo(0);
        assertThat(groupRepository.findNotDeletedAll(PageRequest.of(0, 100)).getTotalElements()).isEqualTo(0);
    }
}