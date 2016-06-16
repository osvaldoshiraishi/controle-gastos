package oss.cloud.config;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.codec.Base64;
import org.springframework.stereotype.Component;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;

@Component
public class CustomPreZuulFilter extends ZuulFilter {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    
	private static final String CLIENT_ASSOCIADO_SECRET = "clientAssociado:secret";


    @Override
    public Object run() {
        final RequestContext ctx = RequestContext.getCurrentContext();
        logger.info("Filtro Zuul " + ctx.getRequest().getRequestURI());
        byte[] encoded;
        try {
        	if(ctx.getRequest().getRequestURI().contains("oauth/token")){
	            encoded = Base64.encode(CLIENT_ASSOCIADO_SECRET.getBytes("UTF-8"));
	            ctx.addZuulRequestHeader("Authorization", "Basic " + new String(encoded));
	            logger.info("Pre filtro");
	            logger.info("Header Authorization " +ctx.getRequest().getHeader("Authorization"));
	            // We need our JWT tokens relayed to resource servers
	            final HttpServletRequest req = ctx.getRequest();
	
	            final String refreshToken = extractRefreshToken(req);
	            if (refreshToken != null) {
	                final Map<String, String[]> param = new HashMap<>();
	                param.put("refresh_token", new String[] { refreshToken });
	                param.put("grant_type", new String[] { "refresh_token" });
	
	                ctx.setRequest(new CustomHttpServletRequest(req, param));
	            }
        	}

        } catch (final UnsupportedEncodingException e) {
            logger.error("Erro ocorrido no filtro Zuul - PRE", e);
        }


        return null;
    }

    private String extractRefreshToken(HttpServletRequest req) {
    	String refreshToken = "refreshToken";
        final Cookie[] cookies = req.getCookies();
        if (cookies != null) {
            for (int i = 0; i < cookies.length; i++) {
            	
            	if(refreshToken.equalsIgnoreCase(cookies[i].getName())){
                    return cookies[i].getValue();
                }
            }
        }
        return null;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public int filterOrder() {
        return -2;
    }

    @Override
    public String filterType() {
        return "pre";
    }

}
