package test.demo.group;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Lee Tae Su
 * @version 1.0
 * @project spring-data-jpa-massive-delete
 * @since 2018-07-13
 */
@Service
public class UserAssignService {
	@Autowired
	private GroupRepository groupRepository;
	
	@Autowired
	private GroupUserRepository groupUserRepository;
	
	
	@Transactional
	public Group assignUser(UserAssignRequest userAssignRequest) {
		if(!userAssignRequest.isValidRequest()) {
			throw new IllegalStateException("UserAssignRequest status is not valid");
		}
		
		Group group = groupRepository.findById(userAssignRequest.getGroup().getGroupKey())
				.orElseThrow(IllegalArgumentException::new);
		
		/*
		Delete를 1건씩 진행한다 -> , orphanRemoval = true 옵션 필요
		*/
//		group.removeAllUsers();


		/* 
		모든 사용자를 삭제한다
		*/
//		groupUserRepository.deleteAllByGroupKeyInByQuery(Collections.singletonList(group.getGroupKey()));
		
		
		/*
		UserAssignedRequest 내에 존재하지 않는 사용자 키를 필터링한다
		하지만 group의 users에는 반영되지 않으므로 불안정하다.
		-> 맞춰주려면 별도로 group.removeAllByUserKeys와 같은 메소드를 구현해야한다.
		 */
		
//		List<Long> userKeysForDelete
//				= group.getUsers().stream().filter(
//				groupUser -> userAssignRequest.getUsers().stream().noneMatch(user -> user.getUserKey().equals(groupUser.getUser().getUserKey()))
//		).map(groupUser -> groupUser.getUser().getUserKey())
//		.collect(Collectors.toList());
//		
//		groupUserRepository.deleteAllByGroupKeyAndUserKeyInByQueryy(group.getGroupKey(), userKeysForDelete);
		
		
		List<Long> userKeysForDelete = group.replaceUsers(userAssignRequest.getUsers()).stream().mapToLong(groupUser-> groupUser.getUser().getUserKey())
				.boxed()
				.collect(Collectors.toList());
		groupUserRepository.deleteAllByGroupKeyAndUserKeyInByQueryy(group.getGroupKey(), userKeysForDelete);
		userAssignRequest.getUsers().forEach(group::addUser);
		
		return groupRepository.save(group);
	}
}
