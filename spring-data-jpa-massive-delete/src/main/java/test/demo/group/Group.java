package test.demo.group;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import test.demo.user.User;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Lee Tae Su
 * @version 1.0
 * @project spring-data-jpa-massive-delete
 * @since 2018-07-13
 */
@Entity
@Table(name = "APP_GROUP")
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Group {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "GroupKeyGen")
	@SequenceGenerator(name = "GroupKeyGen", sequenceName = "GROUP_SEQ")
	private Long groupKey;
	
	private String groupName;
	
	private String description;
	
	@OneToMany(mappedBy = "group", cascade = CascadeType.ALL)
	private List<GroupUser> users = new ArrayList<>();
	
	public void addUser(User user) {
		if(this.users.stream().noneMatch(groupUser -> groupUser.getUser().getUserKey().equals(user.getUserKey()))) {
			GroupUser newGroupUser = GroupUser.builder().group(this).user(user).build();
			this.users.add(newGroupUser);
		}
	}
	
	public void removeUser(User user) {
		GroupUser groupUser = GroupUser.builder().group(this).user(user).build();
		this.users.remove(groupUser);
	}
	
	public void removeAllUsers() {
		this.users.clear();
	}
	
	public List<GroupUser> replaceUsers(List<User> users) {
		List<GroupUser> usersForDelete = this.users.stream().filter(
				groupUser -> users.stream().noneMatch(user -> user.getUserKey().equals(groupUser.getUser().getUserKey()))
		).collect(Collectors.toList());
		
		this.users = users.stream().map(user -> GroupUser.builder().group(this).user(user).build()).collect(Collectors.toList());
		
		//삭제 대상 반환
		return usersForDelete;
	}
	
}
