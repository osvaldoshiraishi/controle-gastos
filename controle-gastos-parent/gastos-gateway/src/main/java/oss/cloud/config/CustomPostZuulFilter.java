package oss.cloud.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import javax.servlet.http.Cookie;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;

@Component
public class CustomPostZuulFilter extends ZuulFilter {

    private static final String REFRESH_TOKEN = "refresh_token";
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public Object run() {
        final RequestContext ctx = RequestContext.getCurrentContext();
        logger.info("filtro Zuul " + ctx.getRequest().getRequestURI());

        try {
            final InputStream is = ctx.getResponseDataStream();
            String responseBody = IOUtils.toString(is, "UTF-8");
            if (responseBody.contains(REFRESH_TOKEN)) {
                final Map<String, Object> responseMap = mapper.readValue(responseBody, new TypeReference<Map<String, Object>>() {
                });
                final String refreshToken = responseMap.get(REFRESH_TOKEN).toString();
                responseMap.remove(REFRESH_TOKEN);
                responseBody = mapper.writeValueAsString(responseMap);

                final Cookie cookie = new Cookie("refreshToken", refreshToken);
                cookie.setHttpOnly(true);
                cookie.setPath(ctx.getRequest().getContextPath() + "/oauth/token");
                cookie.setMaxAge(2592000);
//                cookie.setSecure(true); Habilitar para utilização de cookies seguros
                ctx.getResponse().addCookie(cookie);
                logger.info("refresh token = " + refreshToken);

            }
            ctx.setResponseBody(responseBody);

        } catch (final IOException e) {
            logger.error("Erro ocorrido no filtro Zuul - POST", e);
        }
        return null;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public int filterOrder() {
        return 10;
    }

    @Override
    public String filterType() {
        return "post";
    }

}
