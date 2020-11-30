package com.techstar.om.dasi.handler;

import com.techstar.om.dasi.jackson.JacksonMapper;
import com.techstar.om.dasi.wrapper.WrapperData;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.io.PrintWriter;

public class WrappedHandler implements HandlerMethodReturnValueHandler {

    @Override
    public boolean supportsReturnType(MethodParameter methodParameter) {
        Class<?> clazz = methodParameter.getParameterType();
        if (clazz.equals(ResponseEntity.class) ||
                clazz.equals(ModelAndView.class) ||  // 异常不包装
//                clazz.equals(Response.class) || // 定制输出不包装
                clazz.equals(OutputStream.class) || // 流输出不包装
                clazz.equals(WrapperData.class) // 已经包装过了，不重复包装
        ) {
            return false;
        }
        return true;
    }

    @Override
    public void handleReturnValue(Object o, MethodParameter methodParameter
            , ModelAndViewContainer modelAndViewContainer, NativeWebRequest nativeWebRequest) throws Exception {
        modelAndViewContainer.setRequestHandled(true);
        HttpServletResponse response = nativeWebRequest.getNativeResponse(HttpServletResponse.class);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");
        try (PrintWriter writer = response.getWriter()) {
            String message = JacksonMapper.instance.objectMapper.writeValueAsString(
                    WrapperData.wrap(o).status(response.getStatus()));
            writer.write(message);
        }
    }

}
