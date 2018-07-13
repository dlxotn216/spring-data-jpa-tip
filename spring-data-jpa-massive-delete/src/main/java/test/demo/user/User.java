package test.demo.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * @author Lee Tae Su
 * @version 1.0
 * @project spring-data-jpa-massive-delete
 * @since 2018-07-13
 */
@Entity
@Table(name = "APP_USER")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "UserSeqGen")
	@SequenceGenerator(name = "UserSeqGen", sequenceName = "USER_SEQ")
	private Long userKey;
	private String userName;
	private String email;
	
	
	
}
