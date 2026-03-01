package org.univ_paris8.iut.montreuil.qdev.tp2025.gr7.jeuquizz.demo.config;

import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;

import java.io.IOException;
import java.util.UUID;

@Component
public class MdcLogFilter implements Filter {

    private static final String CORRELATION_ID = "correlationId";

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        if (request instanceof HttpServletRequest) {
            String reqId = ((HttpServletRequest) request).getHeader("X-Correlation-ID");
            if (reqId == null || reqId.isEmpty()) {
                reqId = UUID.randomUUID().toString();
            }
            MDC.put(CORRELATION_ID, reqId);
        }

        try {
            chain.doFilter(request, response);
        } finally {
            MDC.remove(CORRELATION_ID);
        }
    }
}
