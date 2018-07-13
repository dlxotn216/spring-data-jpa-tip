package test.demo.user;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by Lee Tae Su on 2018-07-13.
 */
@RunWith(SpringRunner.class)
@DataJpaTest
public class UserRepositoryTest {
	@Autowired
	private UserRepository userRepository;
	
	@Test
	public void 사용자대량삭제() {
		//Given
		userRepository.saveAll(
				IntStream.rangeClosed(1, 10)
						.boxed()
						.map(operand -> User.builder().email("test" + operand + "@test.com").userName("Tester" + operand).build())
						.collect(Collectors.toList()));
		
		assertThat(userRepository.findAll().size()).isEqualTo(10);
		
		
		//When
		userRepository.deleteAllByUserKeyInByQuery(userRepository.findAll(PageRequest.of(0, 5))
				.map(User::getUserKey)
				.getContent());
		
		//Then
		assertThat(userRepository.findAll().size()).isEqualTo(5);
	}
}