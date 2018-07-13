package test.demo.group;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringRunner;
import test.demo.user.User;
import test.demo.user.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by Lee Tae Su on 2018-07-13.
 */
@RunWith(SpringRunner.class)
@DataJpaTest
public class GroupRepositoryTest {
	@Autowired
	private GroupRepository groupRepository;
	
	@Autowired
	private GroupUserRepository groupUserRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	@Test
	public void 영속성전이_CASCADE_삭제_테스트() {
		//Given
		IntStream.rangeClosed(1, 10)
				.forEach(this::createGroup);
		
		//When
		groupRepository.deleteAllByGroupKeyIn(groupRepository.findAll(PageRequest.of(0, 5))
				.map(Group::getGroupKey)
				.getContent());
		
		//Then
		assertThat(groupRepository.findAll().size()).isEqualTo(5);
		assertThat(groupRepository.findAll()
				.stream()
				.mapToLong(group -> group.getUsers().size())
				.sum()).isEqualTo(50);
		
	}
	
	@Test
	public void 영속성전이_CASCADE_삭제_테스트_ByQuery() {
		//Given
		IntStream.rangeClosed(1, 10)
				.forEach(this::createGroup);
		
		//When
		List<Long> keysForDelete = groupRepository.findAll(PageRequest.of(0, 5))
				.map(Group::getGroupKey)
				.getContent();
		groupUserRepository.deleteAllByGroupKeyInByQuery(keysForDelete);
		groupRepository.deleteAllByGroupKeyInByQuery(keysForDelete);
		
		//Then
		assertThat(groupRepository.findAll().size()).isEqualTo(5);
		assertThat(groupRepository.findAll()
				.stream()
				.mapToLong(group -> group.getUsers().size())
				.sum()).isEqualTo(50);
		
	}
	
	private void createGroup(int seq) {
		Group group = Group.builder().groupName("그룹" + seq).description("desc").users(new ArrayList<>()).build();
		
		IntStream.rangeClosed(1, 10)
				.boxed()
				.map(operand -> User.builder().email("test" + operand + "@test.com").userName("Tester" + operand).build())
				.forEach(user -> {
					userRepository.save(user);
					group.addUser(user);
				});
		groupRepository.save(group);
	}
}