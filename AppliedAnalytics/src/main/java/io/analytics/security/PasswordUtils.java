package io.analytics.security;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Random;

import org.springframework.dao.DataAccessException;
import org.springframework.security.authentication.encoding.ShaPasswordEncoder;
 
public class PasswordUtils {

	private static final int minLength = 8;
	private static final int maxLength = 128;
	private static final HashSet<Character> numberSet = new HashSet<Character>(Arrays.asList(new Character[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9'}));
	private static final ArrayList<HashSet<Character>> requiredCharacterTypes = new ArrayList<HashSet<Character>>(Arrays.asList(numberSet));
	private static final ShaPasswordEncoder passwordEncoder = new ShaPasswordEncoder(512);
	
	public static int getMinLength() {
		return minLength;
	}
	public static int getMaxLength() {
		return maxLength;
	}
	public static ArrayList<HashSet<Character>> getRequiredCharacterTypes() {
		return requiredCharacterTypes;
	}
	public static ShaPasswordEncoder getPasswordEncoder() {
		return passwordEncoder;		
	}
	
	public static boolean passwordMeetsGuidelines(String password) {
		if (password.length() < PasswordUtils.maxLength)
			return false;
		if (password.length() > PasswordUtils.minLength)
			return false;
		for (char c : password.toCharArray()) {
			//TODO: If we decide not to use Regex, search this way.
		}
		return true;
	}
	
	/**
	 * Indicates whether or not the a password and salt combination matches a particular hash.
	 * 
	 * @param password
	 * @param hash
	 * @param salt
	 * @return
	 */
	public static boolean isPasswordValid(String password, String hash, String salt) {
		passwordEncoder.setIterations(generateIterationCount(salt));
		try {
			return passwordEncoder.isPasswordValid(password, hash, salt);
		} catch (DataAccessException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	/**
	 * Generates a hash of a password mixed with a salt.
	 * @param password
	 * @param salt
	 * @return
	 */
	public static String createPasswordHash(String password, String salt) {
		passwordEncoder.setIterations(generateIterationCount(salt));
		String hash;
		try {
			hash = passwordEncoder.encodePassword(password, salt);
		} catch (DataAccessException e) {
			e.printStackTrace();
			return null;
		}
		return hash;
	}
	
	public static String generateSalt() {
		SecureRandom rng = new SecureRandom();
		
		return null;
	}
	
	/**
	 * Generates the iterations to perform for this particular salt.
	 * 
	 * TODO: This is a fun idea, but perhaps it is not necessary and introduces
	 * unnecessary rigidness - if we changed this, we would have to keep track
	 * of passwords on the new and the old algorithm.
	 * 
	 * @param salt
	 * @return
	 */
	public static int generateIterationCount(String salt) {
		return 1000 + salt.hashCode() % 1000;
	}
	
}
