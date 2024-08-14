package com.finder.global.security.jwt.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.finder.global.exception.CustomException;
import com.finder.global.exception.ErrorResponse;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.io.OutputStream;

@Component
public class JwtExceptionFilter extends OncePerRequestFilter {
    private final ObjectMapper objectMapper;

    public JwtExceptionFilter(@Qualifier("objectMapper") ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            filterChain.doFilter(request, response);
        } catch (CustomException e) {
            sendError(response, e);
        }
    }

    private void sendError(HttpServletResponse response, CustomException exception) throws IOException {
        OutputStream stream = response.getOutputStream();

        stream.write(objectMapper.writeValueAsBytes(
                new ErrorResponse(exception.getError())
        ));
        stream.flush();
    }
}
