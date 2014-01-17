package io.analytics.service;

import static org.junit.Assert.assertTrue;

import org.junit.Ignore;
import org.junit.Test;

import com.google.api.client.auth.oauth2.Credential;

public class TestOf_GoogleAuthorizationService {

	@Test
	/**
	 * This test may break if the static refresh token string here expires somehow.
	 */
	public void testGetAccountCredentials() {
		GoogleAuthorizationService service = new GoogleAuthorizationService();
		Credential credential = service.getAccountCredentials("1/Suscp1dN0GT0_xSoEID5Jsgp9LG7GWKK4GpiEFbCp1I");
		System.out.println("Access token: " + credential.getAccessToken());
		
		
		assertTrue(credential.getAccessToken() != null);
	}
}
