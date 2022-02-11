package xyz.klenkiven.auth.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Web MVC configurer for mapping request and pages
 * @author klenkiven
 */
@Configuration
public class AuthWebConfigure implements WebMvcConfigurer {

    /**
     * Registry for View and Controller Pages
     * <p>
     *     <code>
     *          {@code @GetMapping("/login.html")}
     *          public String loginPage() {
     *              return "login";
     *          }
     *     </code>
     * </p>
     * @param registry View Controller Registry
     */
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        // registry.addViewController("/login.html").setViewName("login");
        registry.addViewController("/reg.html").setViewName("reg");
    }

}
