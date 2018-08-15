package next.controller.qna;

import core.mvc.AbstractController;
import core.mvc.JspView;
import core.mvc.ModelAndView;
import core.service.QnaService;
import next.CannotDeleteException;
import next.controller.UserSessionUtils;
import next.dao.AnswerDao;
import next.dao.QuestionDao;
import next.model.Answer;
import next.model.Question;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

public class DeleteQuestionController extends AbstractController {
    private QuestionDao questionDao = QuestionDao.getInstance();
    private AnswerDao answerDao = AnswerDao.getInstance();
    private QnaService qnaService = QnaService.getInstance();

    @Override
    public ModelAndView execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
        if (!UserSessionUtils.isLogined(request.getSession())) {
            return jspView(JspView.DEFAULT_REDIRECT_PREFIX + "/users/loginForm");
        }

        Long questionId = Long.parseLong(request.getParameter("questionId"));
        try {
            qnaService.deleteQuestion(questionId, UserSessionUtils.getUserFormSession(request.getSession()));
            return jspView(JspView.DEFAULT_REDIRECT_PREFIX + "/");
        } catch (CannotDeleteException e) {
            return jspView("show.jsp")
                    .addObject("question", questionDao.findById(questionId))
                    .addObject("answer", answerDao.findAllByQuestionId(questionId))
                    .addObject("errorMessage", e.getMessage());
        }
    }
}