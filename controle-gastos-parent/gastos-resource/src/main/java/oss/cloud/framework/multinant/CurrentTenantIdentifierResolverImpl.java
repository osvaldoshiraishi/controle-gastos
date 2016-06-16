package oss.cloud.framework.multinant;

import org.hibernate.context.spi.CurrentTenantIdentifierResolver;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

/**
 * Classe que resolve o tenent id atual do hibernate
 * 
 * @author osvaldo.shiraishi
 *
 */
//@Component
public class CurrentTenantIdentifierResolverImpl implements
		CurrentTenantIdentifierResolver {

	@Override
	public String resolveCurrentTenantIdentifier() {
		RequestAttributes requestAttributes = RequestContextHolder
				.getRequestAttributes();
		if (requestAttributes != null) {
			String identifier = (String) requestAttributes.getAttribute(
					MultiTenancyContant.CURRENT_TENANT_IDENTIFIER,
					RequestAttributes.SCOPE_REQUEST);
			if (identifier != null) {
				return identifier;
			}
		}
		return MultiTenancyContant.DEFAULT_TENANT_ID;
	}

	@Override
	public boolean validateExistingCurrentSessions() {
		return true;
	}

}
