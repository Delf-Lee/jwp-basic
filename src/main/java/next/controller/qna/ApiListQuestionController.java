package next.controller.qna;

import core.mvc.AbstractController;
import core.mvc.JspView;
import core.mvc.ModelAndView;
import next.controller.UserSessionUtils;
import next.dao.AnswerDao;
import next.dao.QuestionDao;
import next.model.Answer;
import next.model.Question;
import next.model.Result;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

public class ApiListQuestionController extends AbstractController {
    private QuestionDao questionDao = QuestionDao.getInstace();
    private AnswerDao answerDao = AnswerDao.getInstance();

    @Override
    public ModelAndView execute(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        if (!UserSessionUtils.isLogined(req.getSession())) {
            return jsonView().addObject("result", Result.fail("Login is required"));
        }
        // return jsonView().addObject("questions", questionDao.findAll());

        long questionId = Long.parseLong(req.getParameter("questionId"));
        Question question = questionDao.findById(questionId);

        if (question == null) {
            return jsonView().addObject("result", Result.fail("존재하지 않는 질문입니다."));
        }

        if (!question.isSameUser(UserSessionUtils.getUserFormSession(req.getSession()))) {
            return jsonView().addObject("result", Result.fail("다른 사용자가 쓴 글은 삭제할 수 없습니다."));
        }

        List<Answer> answers = answerDao.findAllByQuestionId(questionId);
        if (answers.isEmpty()) {
            questionDao.delete(question);
            return jsonView().addObject("result", Result.ok());
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
            return jsonView().addObject("result", Result.ok());
        } else {
            return jsonView().addObject("result", Result.fail("다른 사용자가 추가한 댓글이 존재해 삭제할 수 없습니다."));
        }
    }
}
