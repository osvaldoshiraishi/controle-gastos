package oss.cloud.framework.multinant;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

/**
 * Interceptor que define qual tenent será usado no request
 * 
 * @author osvaldo.shiraishi
 *
 */
public class MultiTenancyInterceptor extends HandlerInterceptorAdapter {

	@Override
	public boolean preHandle(HttpServletRequest req, HttpServletResponse res,
			Object handler) throws Exception {
		@SuppressWarnings("unchecked")
		Map<String, Object> pathVars = (Map<String, Object>) req
				.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);

		if (pathVars.containsKey("tenantid")) {
			req.setAttribute(MultiTenancyContant.CURRENT_TENANT_IDENTIFIER,
					pathVars.get("tenantid"));
		}
		return true;
	}

}
