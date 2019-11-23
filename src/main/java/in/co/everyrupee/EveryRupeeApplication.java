package in.co.everyrupee;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import in.co.everyrupee.constants.GenericConstants;
import in.co.everyrupee.events.listener.income.UserBudgetCreationListener;
import in.co.everyrupee.events.listener.income.UserBudgetUpdationListener;

/**
 * Initializing Every Rupee Application
 * 
 * @author Nagarjun
 *
 */
@SpringBootApplication
@EnableCaching
@ComponentScan(GenericConstants.EVERYRUPEE_PACKAGE)
public class EveryRupeeApplication {

	/**
	 * Password encryption for login and register
	 * 
	 * @return
	 */
	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
		return bCryptPasswordEncoder;
	}

	/**
	 * Initialize Listener properly
	 * 
	 * @return
	 */
	@Bean
	public UserBudgetCreationListener getUserBudgetCreationListener() {
		return new UserBudgetCreationListener();
	}

	/**
	 * Initialize Listener properly
	 * 
	 * @return
	 */
	@Bean
	public UserBudgetUpdationListener getUserBudgetUpdationListener() {
		return new UserBudgetUpdationListener();
	}

	public static void main(String[] args) {
		SpringApplication.run(EveryRupeeApplication.class, args);
	}

}
