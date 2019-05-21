package core.mvc;

import com.google.common.collect.Lists;
import core.nmvc.AnnotationHandlerMapping;
import core.nmvc.HandlerAdapter;
import core.nmvc.HandlerExecutionHandlerAdapter;
import core.nmvc.HandlerMapping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@WebServlet(name = "dispatcher", urlPatterns = "/", loadOnStartup = 1)
public class DispatcherServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final Logger logger = LoggerFactory.getLogger(DispatcherServlet.class);
    private HandlerMapping handlerMapping;
    private HandlerAdapter handlerAdapter = new HandlerExecutionHandlerAdapter();

    @Override
    public void init() throws ServletException {
        AnnotationHandlerMapping ahm = new AnnotationHandlerMapping("next.controller");
        ahm.initialize();
        handlerMapping = ahm;
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException {
        String requestUri = req.getRequestURI();
        logger.debug("Method : {}, Request URI : {}", req.getMethod(), requestUri);
        Object handler = getHandler(req);
        if (handler == null) {
            throw new IllegalArgumentException("존재하지 않는 URL입니다.");
        }

        try {
            ModelAndView mnv = execute(handler, req, resp);
            View view = mnv.getView();
            view.render(mnv.getModel(), req, resp);
        } catch (Throwable e) {
            logger.error("Exception : {}", e);
            throw new ServletException(e.getMessage());
        }
    }

    private ModelAndView execute(Object handler, HttpServletRequest req, HttpServletResponse res) throws Exception {
        if (handlerAdapter.supports(handler)) {
            return handlerAdapter.handle(req, res, handler);
        }
        return null;
    }

    private Object getHandler(HttpServletRequest req) {
        Object handler = handlerMapping.getHandler(req);
        if (handler != null) {
            return handler;
        }
        return null;
    }
}