package io.analytics.service;

import static org.junit.Assert.assertTrue;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.core.io.FileSystemResourceLoader;
import org.springframework.mock.web.MockServletContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.google.api.client.auth.oauth2.Credential;

@RunWith(SpringJUnit4ClassRunner.class) 
@ContextConfiguration(locations={"file:src/main/webapp/WEB-INF/spring/root-context.xml"})

public class TestOf_GoogleAuthorizationService {
	
	@Ignore
	@Test
	/**
	 * This test may break if the static refresh token string here expires.
	 * According to Google, this should only happen if application access is 
	 * revoked from the account owner.
	 */
	public void testGetAccountCredentials() {
		final String oldRefreshToken = "1/Suscp1dN0GT0_xSoEID5Jsgp9LG7GWKK4GpiEFbCp1I";
		final String REFRESH_TOKEN = "1/OQ7u6b-r-212WlcycuCwrCiRA_XaUHsfttODlQ6bXZA";
		FileSystemResourceLoader fsrl = new FileSystemResourceLoader();
		MockServletContext msc = new MockServletContext(fsrl);
		GoogleAuthorizationService service = new GoogleAuthorizationService();
		service.setServletContext(msc);
		service.setClientSecretLocation("/client_secrets.json");
		Credential credential = service.getAccountCredentials(REFRESH_TOKEN);
		
		//We can't assume that thr refresh token actually works, unfortunately, because sometimes 
		//Google may revoke it. So this test really only serves to detect errors.
		//assertTrue(credential.getAccessToken() != null);
		
		System.out.println("Access token: " + credential.getAccessToken());
	}
}
