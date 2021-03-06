package com.hotdog.springboot.config.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hotdog.springboot.common.util.Constants;
import com.hotdog.springboot.common.util.JwtHelper;
import com.hotdog.springboot.common.util.ResultMsg;
import com.hotdog.springboot.common.util.ResultStatusCode;
import com.hotdog.springboot.model.customProp.Audience;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by hotdog on 2017/3/30.
 */
public class HTTPHotdogAuthorizeAttribute implements Filter {

    @Autowired
    private Audience audienceEntity;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        SpringBeanAutowiringSupport.processInjectionBasedOnServletContext(this,
                filterConfig.getServletContext());
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        ResultMsg resultMsg;
        HttpServletRequest httpRequest = (HttpServletRequest)request;
        String auth = httpRequest.getHeader("Authorization");
        if ((auth != null) && (auth.length() > 7))
        {
            String HeadStr = auth.substring(0, 6);
            if (HeadStr.compareTo(Constants.JwtConstans.TOKEN_TYPE) == 0)
            {

                auth = auth.substring(7, auth.length());
                if (JwtHelper.parseJWT(auth, audienceEntity.getBase64Secret()) != null)
                {
                    chain.doFilter(request, response);
                    return;
                }
            }
        }

        HttpServletResponse httpResponse = (HttpServletResponse) response;
        httpResponse.setCharacterEncoding("UTF-8");
        httpResponse.setContentType("application/json; charset=utf-8");
        httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        ObjectMapper mapper = new ObjectMapper();

        resultMsg = new ResultMsg(ResultStatusCode.INVALID_TOKEN.getErrcode(), ResultStatusCode.INVALID_TOKEN.getErrmsg(), null);
        httpResponse.getWriter().write(mapper.writeValueAsString(resultMsg));

        return;
    }

    @Override
    public void destroy() {

    }
}
