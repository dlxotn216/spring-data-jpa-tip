package test.demo.group;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.CollectionUtils;
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
        if (!isAlreadyAddedUser(user)) {
            GroupUser newGroupUser = GroupUser.builder().group(this).user(user).build();
            this.users.add(newGroupUser);
        }
    }

    public void addUsers(List<User> users) {
        if (!CollectionUtils.isEmpty(users)) {
            users.forEach(this::addUser);
        }
    }

    private Boolean isAlreadyAddedUser(User user) {
        return this.users.stream().anyMatch(groupUser -> groupUser.getUser().getUserKey().equals(user.getUserKey()));
    }

    public void removeUser(GroupUser groupUser) {
        this.users.remove(groupUser);
    }

    public void removeAllUsers() {
        this.users.clear();
    }

    /**
     * 사용자 목록을 그룹에 할당한다.
     *
     * <code>users</code>에 기존에 할당 된 사용자가 존재하더라도
     * filtering 하여 존재하지 않는 사용자만 추가된다.
     *
     * 삭제된 사용자는 목록에서 제거되며
     * 삭제된 사용자 목록은 반환된다.
     *
     * @param users 대체할 사용자 목록
     * @return 삭제된 사용자 목록
     */
    public List<GroupUser> assignUsers(List<User> users) {
        //삭제 대상
        List<GroupUser> usersForDelete = this.users.stream()
                .filter(groupUser -> users.stream().noneMatch(user -> user.getUserKey().equals(groupUser.getUser().getUserKey())))
                .collect(Collectors.toList());

        usersForDelete.forEach(this::removeUser);

        this.addUsers(users.stream()
                .filter(user -> this.users.stream().noneMatch(groupUser -> groupUser.getUser().getUserKey().equals(user.getUserKey())))
                .collect(Collectors.toList()));

        //삭제 대상 반환
        return usersForDelete;
    }

}
