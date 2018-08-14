package next.controller.qna;

import core.mvc.AbstractController;
import core.mvc.JspView;
import core.mvc.ModelAndView;
import next.controller.UserSessionUtils;
import next.dao.QuestionDao;
import next.model.Question;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class DeleteQuestionController extends AbstractController {
    private QuestionDao questionDao = QuestionDao.getInstace();

    @Override
    public ModelAndView execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
        if (!UserSessionUtils.isLogined(request.getSession())) {
            return jspView(JspView.DEFAULT_REDIRECT_PREFIX + "/users/loginForm");
        }

        Long questionId = Long.parseLong(request.getParameter("questionId"));
        Question question = questionDao.findById(questionId);
        if (!question.isSameUser(UserSessionUtils.getUserFormSession(request.getSession()))) {
            throw new IllegalStateException("다른 사용자가 쓴 글을 수정할 수 없습니다.");
        }        // Question question = questionDao.findById(Long.parseLong(request.getParameter("questionId")));
        questionDao.delete(question);

        return jspView(JspView.DEFAULT_REDIRECT_PREFIX + "/");
    }
}
