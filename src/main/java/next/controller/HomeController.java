package next.controller;

import core.mvc.AbstractController;
import core.mvc.ModelAndView;
import next.dao.QuestionDao;
import next.model.Question;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

public class HomeController extends AbstractController {
    private QuestionDao questionDao = QuestionDao.getInstace();
    @Override
    public ModelAndView execute(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        req.setAttribute("questions", questionDao.findAll());
        List<Question> list = questionDao.findAll();

        return jspView("home.jsp");
    }
}