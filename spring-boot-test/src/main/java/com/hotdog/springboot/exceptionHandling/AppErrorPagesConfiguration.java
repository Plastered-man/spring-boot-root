package com.hotdog.springboot.exceptionHandling;

import org.springframework.boot.context.embedded.ConfigurableEmbeddedServletContainer;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.boot.web.servlet.ErrorPage;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

/**
 * Created by hotdog on 2017/3/24.
 */
@Component
public class AppErrorPagesConfiguration {

    @Bean
    public EmbeddedServletContainerCustomizer containerCustomizer () {
        return new EmbeddedServletContainerCustomizer () {
            @Override
            public void customize(ConfigurableEmbeddedServletContainer configurableEmbeddedServletContainer) {
                configurableEmbeddedServletContainer.addErrorPages(new ErrorPage(HttpStatus.NOT_FOUND, "/error/404"));
                configurableEmbeddedServletContainer.addErrorPages(new ErrorPage(HttpStatus.BAD_REQUEST, "/error/400"));
                configurableEmbeddedServletContainer.addErrorPages(new ErrorPage(HttpStatus.INTERNAL_SERVER_ERROR, "/error/500"));
                configurableEmbeddedServletContainer.addErrorPages(new ErrorPage(java.lang.Throwable.class, "/error/500"));
            }
        };
    }
}
