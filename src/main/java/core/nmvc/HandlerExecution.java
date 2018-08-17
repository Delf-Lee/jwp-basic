package core.nmvc;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import core.mvc.ModelAndView;
import org.omg.CORBA.OBJ_ADAPTER;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class HandlerExecution {
    private static final Logger logger = LoggerFactory.getLogger(HandlerExecution.class);
    private Object declaredObject;
    private Method method;

    public HandlerExecution(Object declaredObject, Method method) {
        this.declaredObject = declaredObject;
        this.method = method;
    }

    public ModelAndView handle(HttpServletRequest request, HttpServletResponse response) throws Exception {
        try {
            return (ModelAndView) method.invoke(declaredObject, request, response);
        } catch (IllegalAccessException | InvocationTargetException e) {
            logger.error("{} method invoke fail. error message: {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
