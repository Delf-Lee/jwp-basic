package core.mvc;

import com.google.common.collect.Lists;
import core.nmvc.AnnotationHandlerMapping;
import core.nmvc.HandlerExecution;
import core.nmvc.HandlerMapping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "dispatcher", urlPatterns = "/", loadOnStartup = 1)
public class DispatcherServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final Logger logger = LoggerFactory.getLogger(DispatcherServlet.class);
    private List<HandlerMapping> mappings = Lists.newArrayList();

    @Override
    public void init() throws ServletException {
        LegacyRequestMapping lrm = new LegacyRequestMapping();
        lrm.initMapping();
        AnnotationHandlerMapping ahm = new AnnotationHandlerMapping("next.controller");
        ahm.initialize();

        mappings.add(lrm);
        mappings.add(ahm);
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
        if (handler instanceof Controller) {
            System.out.println("((this is Controller");
            return ((Controller) handler).execute(req, res);
        } else {
            System.out.println("((this is HandlerExecution");
            return ((HandlerExecution) handler).handle(req, res);
        }
    }

    private Object getHandler(HttpServletRequest request) {
        for (HandlerMapping handlerMapping : mappings) {
            Object handler = handlerMapping.getHandler(request);
            if (handler != null) {
                return handler;
            }
        }
        return null;
    }
}