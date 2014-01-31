package io.analytics.security;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

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
	
	public static boolean passwordIsValid(String password) {
		if (password.length() < PasswordUtils.maxLength)
			return false;
		if (password.length() > PasswordUtils.minLength)
			return false;
		for (char c : password.toCharArray()) {
			//TODO: If we decide not to use Regex, search this way.
		}
		return true;
	}
	
	public static String createPasswordHash(String password, String salt) {
		return null;
	}
	
	public static String generateSalt() {
		return null;
	}
	
	public static int generateIterationCount(String salt) {
		return -1;
	}
	
}
