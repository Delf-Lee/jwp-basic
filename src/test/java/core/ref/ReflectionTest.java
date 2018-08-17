package core.ref;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import next.model.Question;
import next.model.User;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ReflectionTest {
    private static final Logger logger = LoggerFactory.getLogger(ReflectionTest.class);

    @Test
    public void showClass() {
        Class<Question> clazz = Question.class;
        logger.debug(clazz.getName());
        Field[] fields = clazz.getDeclaredFields();
        for (int i = 0; i < fields.length; i++) logger.debug(fields[i].getName());
        Method[] methods = clazz.getMethods();
        for (int i = 0; i < methods.length; i++) logger.debug(methods[i].getName());
        Constructor<Question>[] constructor = (Constructor<Question>[]) clazz.getDeclaredConstructors();
        for (int i = 0; i < constructor.length; i++) logger.debug(constructor[i].getName());
    }

    @Test
    public void newInstanceWithConstructorArgs() throws IllegalAccessException, InvocationTargetException, InstantiationException {
        Class<User> clazz = User.class;
        logger.debug(clazz.getName());
        Constructor<User>[] constructors = (Constructor<User>[]) clazz.getDeclaredConstructors();
            User user = constructors[0].newInstance("id", "pass", "name", "email");
            logger.debug(user.getUserId() + user.getPassword() + user.getName() + user.getEmail());
    }

    @Test
    public void privateFieldAccess() throws NoSuchFieldException, IllegalAccessException {
        Class<Student> clazz = Student.class;
        logger.debug(clazz.getName());
        Student student = new Student();
        Field nameField = clazz.getDeclaredField("name"); // NoSuchFieldException
        nameField.setAccessible(true);

        nameField.set(student, "데르프"); // IllegalAccessException
        logger.debug("name: " + student.getName());
    }
}
