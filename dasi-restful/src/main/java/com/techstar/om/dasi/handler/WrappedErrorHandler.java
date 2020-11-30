package com.techstar.om.dasi.handler;

import com.techstar.om.dasi.jackson.JacksonMapper;
import com.techstar.om.dasi.wrapper.WrapperData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class WrappedErrorHandler implements HandlerExceptionResolver {
    private Logger logger = LoggerFactory.getLogger(WrappedErrorHandler.class);

    @Override
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");
        try (PrintWriter writer = response.getWriter()) {
            String message = JacksonMapper.instance.objectMapper.writeValueAsString(
                    WrapperData.wrap().code(1).message(ex.getMessage())
                            .status(HttpStatus.INTERNAL_SERVER_ERROR.value()));
            writer.write(message);
            logger.error("错误请求:" + request.getRequestURI(), ex);
        } catch (IOException e) {
            logger.error("异常拦截器错误:" + request.getRequestURI(), e);
        }
        return new ModelAndView();
    }
}
