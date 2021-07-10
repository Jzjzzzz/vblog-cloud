package team.blogserver.common.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.context.annotation.Configuration;
import team.ark.core.util.WebUtils;
import team.blogserver.common.mapper.JournalMapper;
import team.blogserver.common.model.domain.Journal;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.Objects;

/**
 * Jzj
 * 2021-07-10
 */
@Slf4j
@Aspect
@Configuration
public class LogAspect {
    @Resource
    private JournalMapper journalMapper;

    @Around("execution(!static team.ark.core.response.R *(..)) && " +
            "(@within(org.springframework.stereotype.Controller) || " +
            "@within(org.springframework.web.bind.annotation.RestController))"
    )
    public Object requestLog(ProceedingJoinPoint point) throws Throwable {

        HttpServletRequest request = WebUtils.getRequest();
        String requestUri = Objects.requireNonNull(request).getRequestURI();
        Journal journal = new Journal();
        journal.setDate(new Date());
        journal.setHostip(WebUtils.getClientIP(request));
        journal.setRequestmethod(requestUri);
        journalMapper.insert(journal);
        return point.proceed();
    }

}