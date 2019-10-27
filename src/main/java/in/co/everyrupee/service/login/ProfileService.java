package in.co.everyrupee.service.login;

import java.security.Principal;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.context.MessageSource;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import in.co.everyrupee.utils.RegexUtils;

/**
 * Handle requests related to user registration and login
 * 
 * @author Nagarjun Nagesh
 *
 */
@Transactional
@Service("profileService")
@CacheConfig(cacheNames = { "users" })
public class ProfileService {

	@Autowired
	private HttpServletRequest request;

	@Autowired
	private MessageSource messages;

	@Autowired
	private RegexUtils regexUtils;

	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	private static final Pattern[] inputRegexes = new Pattern[4];

	// Compile the regular expression during compile time.
	static {
		inputRegexes[0] = Pattern.compile(".*[A-Z].*");
		inputRegexes[1] = Pattern.compile(".*[a-z].*");
		inputRegexes[2] = Pattern.compile(".*\\d.*");
		inputRegexes[3] = Pattern.compile(".*[`~!@#$%^&*()\\-_=+\\\\|\\[{\\]};:'\",<.>/?].*");
	}

	Logger logger = LoggerFactory.getLogger(this.getClass());

	/**
	 * SECURITY: Prevent Unauthorized access by Client Side Data Modification
	 * 
	 * @param userPrincipal
	 * @param financialPortfolioId
	 */
	public void validateUser(Principal userPrincipal, String financialPortfolioId) {
		// TODO
	}

	/**
	 * SECURITY: Validate user login and send the financial portfolio id
	 * 
	 * @param userPrincipal
	 * @return
	 */
	public Integer validateUser(Principal userPrincipal) {
		// TODO
		return null;
	}

	public HttpServletRequest getRequest() {
		return request;
	}

	public MessageSource getMessages() {
		return messages;
	}

	public RegexUtils getRegexUtils() {
		return regexUtils;
	}

	public BCryptPasswordEncoder getbCryptPasswordEncoder() {
		return bCryptPasswordEncoder;
	}

}
