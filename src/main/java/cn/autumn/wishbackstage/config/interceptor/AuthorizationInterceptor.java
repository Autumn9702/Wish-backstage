package cn.autumn.wishbackstage.config.interceptor;

import cn.autumn.wishbackstage.config.resp.RespCode;
import cn.autumn.wishbackstage.serve.RedisCacheServe;
import cn.autumn.wishbackstage.util.JwtUtil;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.AsyncHandlerInterceptor;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static cn.autumn.wishbackstage.config.Configuration.*;

/**
 * @author Autumn
 * Created in 2023/1/4
 * Description Interceptor
 */
@Slf4j
public final class AuthorizationInterceptor implements AsyncHandlerInterceptor {

    @Resource
    private JwtUtil jwtUtil;

    @Resource
    private RedisCacheServe redisCacheServe;

    /**
     * Called before the business processor processes the request.
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String uri = request.getRequestURI();
        if (uri.startsWith(WISH_PATH + LOGIN_PATH + SLASH)) {
            log.info("Request login. login user: {}", request.getAttribute("username"));
            return true;
        }

        if (uri.startsWith(WISH_PATH + API_PATH + SLASH)) {
            String token = request.getAttribute("token").toString();
            if (token == null) {
                response.sendError(RespCode.TK_NULL.getCode(), RespCode.TK_NULL.getMsg());
                return false;
            }
            Claims claims = jwtUtil.getClaimsByToken(token);
            if (claims == null) {
                response.sendError(RespCode.TK_PERJURY.getCode(), RespCode.TK_PERJURY.getMsg());
                return false;
            }
            String username = claims.get("username", String.class);
            if (username == null) {
                response.sendError(RespCode.TK_PERJURY.getCode(), RespCode.TK_PERJURY.getMsg());
                return false;
            }
            String userStr = redisCacheServe.getOnlineUser(username);
            if (userStr == null) {
                String rsh = redisCacheServe.getOnlineRefresh(username);
                if (rsh == null) {
                    response.sendError(RespCode.TK_RE_LOGIN.getCode(), RespCode.TK_RE_LOGIN.getMsg());
                    return false;
                }
                // TODO: 2023/1/5 16:28 return token
                response.sendError(RespCode.TK_EXPIRE.getCode(), RespCode.TK_EXPIRE.getMsg());
                return false;
            }
            // TODO: 2023/1/5 16:31 获取权限数组 是否包含访问地址 不包含则权限不足
            return true;
        }
        log.error("Request failure. Incorrect request address: {}", request.getRequestURI());
        return false;
    }


}
