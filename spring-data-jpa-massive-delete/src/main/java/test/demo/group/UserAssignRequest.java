package test.demo.group;

import lombok.Value;
import test.demo.user.User;

import java.util.List;

/**
 * @author Lee Tae Su
 * @version 1.0
 * @project spring-data-jpa-massive-delete
 * @since 2018-07-13
 */
@Value
public class UserAssignRequest {
	private Group group;
	private List<User> users;
	
	public Boolean isValidRequest() {
		return group != null && group.getGroupKey() != null && users != null;
	}
}
