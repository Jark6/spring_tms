package com.jark.TMS;
import com.jark.TMS.models.Users;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

public class TmsLogger {
    private final Logger logger;

    public TmsLogger(Class<?> clazz) {
        this.logger = LoggerFactory.getLogger(clazz);
    }

    public void logTrace(String message) {
        logger.trace(message);
    }

    public void logDebug(String message) {
        logger.debug(message);
    }

    public void logInfo(String message) {
        logWithUserContext(() -> logger.info(message));
    }

    public void logWarn(String message) {
        logger.warn(message);
    }

    public void logError(String message) {
        logger.error(message);
    }

    public void logError(String message, Throwable throwable) {
        logger.error(message, throwable);
    }

    private void logWithUserContext(Runnable logAction) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
                String username = ((UserDetails) authentication.getPrincipal()).getUsername();
                MDC.put("user", username);
            }

            logAction.run();
        } finally {
            MDC.clear();
        }
    }
}
