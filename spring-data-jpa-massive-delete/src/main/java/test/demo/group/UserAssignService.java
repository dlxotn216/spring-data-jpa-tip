package test.demo.group;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

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
//		userAssignRequest.getUsers().forEach(group::addUser);

		/* 
		모든 사용자를 삭제 후 Request의 모든 사용자 추가
		*/
//		groupUserRepository.deleteAllByGroupKeyInByQuery(Collections.singletonList(group.getGroupKey()));
//		userAssignRequest.getUsers().forEach(group::addUser);
		
		/*
		UserAssignedRequest 내에 존재하지 않는 사용자 키를 필터링하여 삭제후
		Request의 모든 사용자 추가
		-> addUser에서 존재하지 않는 사용자만 추가하므로 중복 객체는 없다

		하지만 group의 users에는 삭제된 내역이 반영되지 않으므로 로직이 불안정하다.
		-> 맞춰주려면 별도로 group.removeAllByUserKeys와 같은 메소드를 구현해야한다.
		 */
		
//		List<Long> userKeysForDelete
//				= group.getUsers().stream().filter(
//				groupUser -> userAssignRequest.getUsers().stream().noneMatch(user -> user.getUserKey().equals(groupUser.getUser().getUserKey()))
//		).map(groupUser -> groupUser.getUser().getUserKey())
//		.collect(Collectors.toList());
//		
//		groupUserRepository.deleteAllByGroupKeyAndUserKeyInByQueryy(group.getGroupKey(), userKeysForDelete);
//		userAssignRequest.getUsers().forEach(group::addUser);

		/*
		Request로 전달된 사용자 목록을 새로이 할당(Assign, replace) 한다
		결과로 반환 된 삭제된 사용자 목록을 map 연산을 통해 repository call에 필요한
		 userKey 목록으로 변환한다.
		 */
		List<Long> userKeysForDelete = group.assignUsers(userAssignRequest.getUsers()).stream()
				.map(groupUser-> groupUser.getUser().getUserKey())
				.collect(Collectors.toList());
		if(!CollectionUtils.isEmpty(userKeysForDelete)){
			groupUserRepository.deleteAllByGroupKeyAndUserKeyInByQueryy(group.getGroupKey(), userKeysForDelete);
		}
		
		return groupRepository.save(group);
	}
}
