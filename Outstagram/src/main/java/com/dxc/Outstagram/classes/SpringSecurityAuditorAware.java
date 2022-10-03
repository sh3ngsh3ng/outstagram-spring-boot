

import java.util.Optional;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import com.dxc.Outstagram.entity.User;



public class SpringSecurityAuditorAware implements AuditorAware<User> {

	@Override
	public Optional<User> getCurrentAuditor() {
		// TODO Auto-generated method stub
		return Optional.ofNullable(SecurityContextHolder.getContext())
	            .map(SecurityContext::getAuthentication)
	            .filter(Authentication::isAuthenticated)
	            .map(Authentication::getPrincipal)
	            .map(User.class::cast);
	}

}
