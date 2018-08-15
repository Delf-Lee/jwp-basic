package next.dao;

import core.jdbc.JdbcTemplate;
import core.jdbc.KeyHolder;
import core.jdbc.PreparedStatementCreator;
import core.jdbc.RowMapper;
import next.model.Question;
import next.model.User;

import java.sql.*;
import java.util.List;

public class QuestionDao {

    private static QuestionDao instance;
    private QuestionDao(){}
    public static QuestionDao getInstance() {
        if (instance == null) {
            instance = new QuestionDao();
        }
        return instance;
    }

    public Question insert(Question question) {
        String sql = "INSERT INTO QUESTIONS (writer, title, contents, createdDate) VALUES (?, ?, ?, ?)";
        PreparedStatementCreator psc = new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                PreparedStatement pstmt = con.prepareStatement(sql);
                pstmt.setString(1, question.getWriter());
                pstmt.setString(2, question.getTitle());
                pstmt.setString(3, question.getContents());
                pstmt.setTimestamp(4, new Timestamp(question.getTimeFromCreateDate()));
                return pstmt;
            }
        };

        JdbcTemplate jdbcTemplate = new JdbcTemplate();
        KeyHolder keyHolder = new KeyHolder();
        jdbcTemplate.update(psc, keyHolder);
        return findById(keyHolder.getId());
    }

    public List<Question> findAll() {
        JdbcTemplate jdbcTemplate = new JdbcTemplate();
        String sql = "SELECT questionId, writer, title, createdDate, countOfAnswer FROM QUESTIONS order by questionId desc";

        RowMapper<Question> rm = new RowMapper<Question>() {
            @Override
            public Question mapRow(ResultSet rs) throws SQLException {
                return new Question(rs.getLong("questionId"), rs.getString("writer"), rs.getString("title"), null,
                        rs.getTimestamp("createdDate"), rs.getInt("countOfAnswer"));
            }

        };

        return jdbcTemplate.query(sql, rm);
    }

    public Question findById(long questionId) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate();
        String sql = "SELECT questionId, writer , title, contents, createdDate, countOfAnswer FROM QUESTIONS "
                + "WHERE questionId = ?";

        RowMapper<Question> rm = new RowMapper<Question>() {
            @Override
            public Question mapRow(ResultSet rs) throws SQLException {
                return new Question(rs.getLong("questionId"), rs.getString("writer"), rs.getString("title"),
                        rs.getString("contents"), rs.getTimestamp("createdDate"), rs.getInt("countOfAnswer"));
            }
        };

        return jdbcTemplate.queryForObject(sql, rm, questionId);
    }

    public void update(Question question) {
        JdbcTemplate template = new JdbcTemplate();
        String sql = "UPDATE QUESTIONS SET writer = ?, title = ?, contents = ?, createdDate = ?, countOfAnswer = ? WHERE questionId = ?";
        template.update(sql, question.getWriter(),question.getTitle(), question.getContents(), question.getCreatedDate(), question.getCountOfComment(), question.getQuestionId());
    }

    public void delete(Long questionId) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate();
        String sql = "DELETE FROM QUESTIONS WHERE questionId = ?";
        jdbcTemplate.update(sql, questionId);
    }

    public void delete(Question question) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate();
        String sql = "DELETE FROM QUESTIONS WHERE questionId = ?";
        jdbcTemplate.update(sql, question.getQuestionId());
    }
}