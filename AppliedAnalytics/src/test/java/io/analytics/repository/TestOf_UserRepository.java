package io.analytics.repository;

import static org.junit.Assert.fail;
import io.analytics.domain.User;
import io.analytics.repository.interfaces.IUserRepository;
import io.analytics.security.PasswordUtils;

import java.util.Calendar;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

@RunWith(SpringJUnit4ClassRunner.class) 
@ContextConfiguration(locations={"file:src/main/webapp/WEB-INF/spring/root-context.xml"})
public class TestOf_UserRepository {

	@Autowired private IUserRepository userRepository;
	@Ignore
	@Test
	public void testAddNewUser() {
		/*
		 * TODO: Replace DataSource with a mocked data source.
		 * Do not enable the code below, it will add bogus rows to the actual database.
		 * This is just here for some raw preliminary testing.
		 */
		
		//TODO: Autowire this.
		//userRepository = new UserRepository();
		
		if (true) {
			User user = Mockito.mock(User.class);
			try {
					
				assert(userRepository.addNewUser(user) == null);

				Mockito.when(user.getFirstName()).thenReturn("Larry");
				assert(userRepository.addNewUser(user) == null);
				
				Mockito.when(user.getLastName()).thenReturn("Page");
				assert(userRepository.addNewUser(user) == null);
				
				String uniqueEmail = PasswordUtils.createPasswordHash(PasswordUtils.generateSalt(), PasswordUtils.generateSalt());
				Mockito.when(user.getEmail()).thenReturn(uniqueEmail.substring(uniqueEmail.length() - 127));
				assert(userRepository.addNewUser(user) == null);

				String uniqueUsername = PasswordUtils.createPasswordHash(PasswordUtils.generateSalt(), PasswordUtils.generateSalt());
				Mockito.when(user.getUsername()).thenReturn(uniqueUsername.substring(uniqueUsername.length() - 45));
				assert(userRepository.addNewUser(user) == null);
				
				String salt = PasswordUtils.generateSalt();
				
				Mockito.when(user.getPassword()).thenReturn(PasswordUtils.createPasswordHash("123456", salt));
				assert(userRepository.addNewUser(user) == null);
				
				Mockito.when(user.getPasswordSalt()).thenReturn(salt);
				assert(userRepository.addNewUser(user) == null);

				Mockito.when(user.getProfileImageUrl()).thenReturn("http://www.google.com/pagepagepage.jpg");
				assert(userRepository.addNewUser(user) == null);
				
				Mockito.when(user.getJoinDate()).thenReturn(Calendar.getInstance());
				assert(userRepository.addNewUser(user) != null);
				
				
			} catch (Exception e) {
				System.err.println("testAddNewUser() encountered errors.");
				e.printStackTrace();
				fail();
			}
		}
	}
}
