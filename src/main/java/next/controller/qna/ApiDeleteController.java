package next.controller.qna;

import core.mvc.AbstractController;
import core.mvc.JspView;
import core.mvc.ModelAndView;
import core.service.QnaService;
import next.CannotDeleteException;
import next.controller.UserSessionUtils;
import next.model.Result;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ApiDeleteController extends AbstractController {
    private QnaService qnaService = QnaService.getInstance();

    @Override
    public ModelAndView execute(HttpServletRequest req, HttpServletResponse resp) throws Exception {

        if (!UserSessionUtils.isLogined(req.getSession())) {
            return jspView(JspView.DEFAULT_REDIRECT_PREFIX + "/users/loginForm");
        }

        Long questionId = Long.parseLong(req.getParameter("questionId"));
        try {
            qnaService.deleteQuestion(questionId, UserSessionUtils.getUserFormSession(req.getSession()));
            return jspView(JspView.DEFAULT_REDIRECT_PREFIX + "/");
        } catch (CannotDeleteException e) {
            return jsonView().addObject("result", Result.ok());
        }
    }
}