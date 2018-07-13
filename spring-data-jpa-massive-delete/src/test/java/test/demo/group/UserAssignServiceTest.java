package test.demo.group;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import test.demo.user.User;
import test.demo.user.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by Lee Tae Su on 2018-07-13.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class UserAssignServiceTest {
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private GroupRepository groupRepository;
	
	@Autowired
	private GroupUserRepository groupUserRepository;
	
	@Autowired
	private UserAssignService userAssignService;
	
	@Test
	public void 그룹에_사용자_할당_테스트() {
		//Given
		Group group = groupRepository.save(Group.builder().groupName("그룹1").description("테스트").users(new ArrayList<>()).build());
		
		//When
		userAssignService.assignUser(new UserAssignRequest(group, userRepository.saveAll(
				IntStream.rangeClosed(1, 10)
						.boxed()
						.map(operand -> User.builder().email("test" + operand + "@test.com").userName("Tester" + operand).build())
						.collect(Collectors.toList()))));
		
		//Then
		Group saved = groupRepository.findById(group.getGroupKey()).orElseThrow(IllegalArgumentException::new);
		assertThat(groupUserRepository.findGroupUserByGroup(saved).size()).isEqualTo(10);
	}
	
	@Test
	public void 그룹에_사용자_전체_재할당_테스트() {
		//Given
		Group group = groupRepository.save(Group.builder().groupName("그룹1").description("테스트").users(new ArrayList<>()).build());
		
		userAssignService.assignUser(new UserAssignRequest(group, userRepository.saveAll(
				IntStream.rangeClosed(1, 10)
						.boxed()
						.map(operand -> User.builder().email("test" + operand + "@test.com").userName("Tester" + operand).build())
						.collect(Collectors.toList()))));
		
		//When
		List<User> usersForNewAssign = userRepository.saveAll(
				IntStream.rangeClosed(101, 102)
						.boxed()
						.map(operand -> User.builder().email("test" + operand + "@test.com").userName("Tester" + operand).build())
						.collect(Collectors.toList()));
		
		userAssignService.assignUser(new UserAssignRequest(group, usersForNewAssign));
		
		//Then
		Group saved = groupRepository.findById(group.getGroupKey()).orElseThrow(IllegalArgumentException::new);
		assertThat(groupUserRepository.findGroupUserByGroup(saved).size()).isEqualTo(usersForNewAssign.size());
	}
	
	@Test
	public void 그룹에_사용자_일부_재할당_테스트() {
		//Given
		Group group = groupRepository.save(Group.builder().groupName("그룹1").description("테스트").users(new ArrayList<>()).build());
		
		List<User> usersForPreAssign = userRepository.saveAll(
				IntStream.rangeClosed(1, 10)
						.boxed()
						.map(operand -> User.builder().email("test" + operand + "@test.com").userName("Tester" + operand).build())
						.collect(Collectors.toList()));
		userAssignService.assignUser(new UserAssignRequest(group, usersForPreAssign));
		
		//When
		List<User> usersForNewAssign = userRepository.saveAll(
				IntStream.rangeClosed(101, 102)
						.boxed()
						.map(operand -> User.builder().email("test" + operand + "@test.com").userName("Tester" + operand).build())
						.collect(Collectors.toList()));
		
		//Append pre-assigned users
		usersForNewAssign.addAll(usersForPreAssign.subList(0, 5));
		
		userAssignService.assignUser(new UserAssignRequest(group, usersForNewAssign));
		
		//Then
		Group saved = groupRepository.findById(group.getGroupKey()).orElseThrow(IllegalArgumentException::new);
		assertThat(groupUserRepository.findGroupUserByGroup(saved).size()).isEqualTo(usersForNewAssign.size());
	}
	
}