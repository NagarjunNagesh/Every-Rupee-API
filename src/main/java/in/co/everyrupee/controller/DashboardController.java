package in.co.everyrupee.controller;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import in.co.everyrupee.constants.GenericConstants;
import in.co.everyrupee.utils.GenericResponse;

@Controller
public class DashboardController {

	@Value("${spring.profiles.active}")
	private String springActiveProfile;

	@Autowired
	public DashboardController(Environment environment) {
	}

	/**
	 * Checks if the session is alive from the server.
	 * 
	 * @param userPrincipal
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = GenericConstants.KEEP_ALIVE_CHECK_URL, method = RequestMethod.GET)
	public GenericResponse keepAliveCheck(Principal userPrincipal) {
		if (userPrincipal == null) {
			throw new SecurityException();
		}

		return new GenericResponse("success");

	}

}
