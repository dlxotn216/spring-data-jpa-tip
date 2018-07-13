package test.demo.group;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import test.demo.user.User;

import javax.persistence.*;

/**
 * @author Lee Tae Su
 * @version 1.0
 * @project spring-data-jpa-massive-delete
 * @since 2018-07-13
 */
@Entity
@Table
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GroupUser {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "GroupUserSeqGen")
	@SequenceGenerator(name = "GroupUserSeqGen", sequenceName = "GROUP_USER_SEQ")
	private Long groupUserKey;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "GROUP_KEY")
	private Group group;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "USER_KEY")
	private User user;
}
