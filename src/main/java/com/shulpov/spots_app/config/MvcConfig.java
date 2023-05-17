package com.shulpov.spots_app.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MvcConfig implements WebMvcConfigurer {
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/css/**")
                .addResourceLocations("classpath:/static/assets/css/");
        registry.addResourceHandler("/images/**")
                .addResourceLocations("classpath:/static/assets/images/");
        registry.addResourceHandler("/js/**")
                .addResourceLocations("classpath:/static/assets/js/");
        registry.addResourceHandler("/user-images/**")
                .addResourceLocations("classpath:/storage/images/users/");
        registry.addResourceHandler("/spot-images/**")
                .addResourceLocations("classpath:/storage/images/spots/");
    }
}
