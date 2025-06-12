package me.zeanzai.globalexception.resolver;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.servlet.LocaleResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Locale;

/**
 * @author shawnwang
 * @version 1.0
 * @describe
 * @date 2023/4/22
 */
public class LanguageResolver implements LocaleResolver {
    /**
     * 请求header字段
     */
    private static final String LANG = "lang";

    /**
     * session
     */
    private static final String LANG_SESSION = "lang_session";
    @Override
    public Locale resolveLocale(HttpServletRequest request) {
        String lang = request.getHeader(LANG);
        Locale locale = Locale.getDefault();
        if (StringUtils.isNotBlank(lang)){
            String[] language = lang.split("_");
            locale = new Locale(language[0], language[1]);

            HttpSession session = request.getSession();
            session.setAttribute(LANG_SESSION, locale);
        }else{
            HttpSession session = request.getSession();
            Locale localeInSession = (Locale) session.getAttribute(LANG_SESSION);
            if (localeInSession != null){
                locale = localeInSession;
            }
        }
        return locale;
    }

    @Override
    public void setLocale(HttpServletRequest request, HttpServletResponse response, Locale locale) {

    }
}
