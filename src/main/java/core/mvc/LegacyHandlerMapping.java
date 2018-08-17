package core.mvc;

import core.nmvc.HandlerMapping;
import next.controller.HomeController;
import next.controller.qna.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

public class LegacyHandlerMapping implements HandlerMapping{
    private static final Logger logger = LoggerFactory.getLogger(DispatcherServlet.class);
    private Map<String, Controller> mappings = new HashMap<>();

    void initMapping() {
        mappings.put("/", new HomeController());
        // mappings.put("/users/form", new ForwardController("/user/form.jsp"));
        // mappings.put("/users", new ListUserController());
        // mappings.put("/users/create", new CreateUserController());
        // mappings.put("/users/login", new ForwardController("/user/login.jsp"));
        // mappings.put("/users/login", new LoginController());
        // mappings.put("/users/logout", new LogoutController());
        // mappings.put("/users/profile", new ProfileController());
        // mappings.put("/users/updateForm", new UpdateFormUserController());
        // mappings.put("/users/update", new UpdateUserController());
        mappings.put("/qna/form", new ForwardController("/qna/form.jsp"));
        mappings.put("/qna/create", new AddQuestionController());
        mappings.put("/qna/delete", new DeleteQuestionController());
        mappings.put("/qna/updateForm", new UpdateQuestionController());
        mappings.put("/qna/show", new ShowController());
        mappings.put("/api/qna/addAnswer", new AddAnswerController());
        mappings.put("/api/qna/deleteAnswer", new DeleteAnswerController());
        mappings.put("/api/qna/list", new ApiListQuestionController());

        logger.info("Initialized Request Mapping!");
    }

    /*public Controller findController(String url) {
        return mappings.get(url);
    }*/

    void put(String url, Controller controller) {
        mappings.put(url, controller);
    }

    @Override
    public Object getHandler(HttpServletRequest request) {
        return mappings.get(request.getRequestURI());
    }
}
