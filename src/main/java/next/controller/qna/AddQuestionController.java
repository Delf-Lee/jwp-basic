package next.controller.qna;

import core.mvc.AbstractController;
import core.mvc.ModelAndView;
import next.dao.QuestionDao;
import next.model.Question;
import next.model.User;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class AddQuestionController extends AbstractController {
    @Override
    public ModelAndView execute(HttpServletRequest request, HttpServletResponse response) throws Exception {

       HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        System.out.println("user:" + user);
        /*   if (user == null) {
            return jspView("/users/login.jsp");
        }*/

        Question question = new Question(
                user.getName(),
                request.getParameter("title"),
                request.getParameter("contents"));
        QuestionDao questionDao = new QuestionDao();
        questionDao.insert(question);

        return jspView("redirect:/");
    }
}
