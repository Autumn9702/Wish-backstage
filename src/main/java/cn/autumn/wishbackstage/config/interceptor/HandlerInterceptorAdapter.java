package cn.autumn.wishbackstage.config.interceptor;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Autumn
 * Created in 2023/1/4
 */
public abstract class HandlerInterceptorAdapter implements HandlerInterceptor {
    /**
     * Called before the business processor processes the request.
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        return true;
    }

    /**
     * After the business processor processing is complete. Execute before generating the view
     */
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    /**
    * After the DispatcherServlet is complete. It can be used to clear resources
    */
     @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
