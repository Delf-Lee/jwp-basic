package next.controller.qna;

import core.mvc.AbstractController;
import core.mvc.JspView;
import core.mvc.ModelAndView;
import next.controller.UserSessionUtils;
import next.dao.AnswerDao;
import next.dao.QuestionDao;
import next.model.Answer;
import next.model.Question;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

public class DeleteQuestionController extends AbstractController {
    private QuestionDao questionDao = QuestionDao.getInstace();
    private AnswerDao answerDao = AnswerDao.getInstance();

    @Override
    public ModelAndView execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
        if (!UserSessionUtils.isLogined(request.getSession())) {
            return jspView(JspView.DEFAULT_REDIRECT_PREFIX + "/users/loginForm");
        }

        Long questionId = Long.parseLong(request.getParameter("questionId"));
        Question question = questionDao.findById(questionId);
        if (!question.isSameUser(UserSessionUtils.getUserFormSession(request.getSession()))) {
            throw new IllegalStateException("다른 사용자가 쓴 글을 삭제할 수 없습니다.");
        }        // Question question = questionDao.findById(Long.parseLong(request.getParameter("questionId")));

        List<Answer> answers = answerDao.findAllByQuestionId(questionId);
            if (answers.isEmpty()) {
                questionDao.delete(question);
                return jspView(JspView.DEFAULT_REDIRECT_PREFIX + "/");
            }

        boolean canDelete = true;
        for (Answer answer : answers) {
            String writer = question.getWriter();
            if (!writer.equals(answer.getWriter())) {
                canDelete = false;
                break;
            }
        }

        if (canDelete) {
            questionDao.delete(question);
            return jspView(JspView.DEFAULT_REDIRECT_PREFIX + "/");
        }

        return createModelAndView(question, answers, "다른 사용자가 추가한 댓글이 존재해 삭제할 수 없습니다.");
    }

    private ModelAndView createModelAndView(Question question, List<Answer> answers, String errorMessage) {
        return jspView("show.jsp")
                .addObject("question", question)
                .addObject("answer", answers)
                .addObject("errorMessage", errorMessage);
    }
}
