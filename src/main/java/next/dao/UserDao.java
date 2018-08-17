package next.dao;

import core.jdbc.JdbcTemplate;
import core.jdbc.PreparedStatementSetter;
import core.jdbc.RowMapper;
import next.model.User;

import java.util.List;

public class UserDao {
    private static UserDao instance;

    private UserDao(){};
    public static UserDao getInstance() {
        if (instance == null) {
            instance = new UserDao();
        }
        return instance;
    }

    public void insert(User user) {
        JdbcTemplate template = new JdbcTemplate();
        PreparedStatementSetter pss = pstmt -> {
            pstmt.setString(1, user.getUserId());
            pstmt.setString(2, user.getPassword());
            pstmt.setString(3, user.getName());
            pstmt.setString(4, user.getEmail());
        };
        String sql = "INSERT INTO USERS VALUES (?, ?, ?, ?)";
        template.update(sql, pss);
    }

    public void update(User user) {
        JdbcTemplate template = new JdbcTemplate();
        String sql = "UPDATE USERS SET password = ?, name = ?, email = ? WHERE userId = ?";
        template.update(sql, user.getPassword(), user.getName(), user.getEmail(), user.getUserId());
    }

    public void delete(String userId) {
        JdbcTemplate template = new JdbcTemplate();

        PreparedStatementSetter pss = pstmt -> pstmt.setString(1, userId);
        String sql = "DELETE FROM USERS WHERE userId=?";
        template.update(sql, pss);
    }

    public List<User> findAll() {
        JdbcTemplate template = new JdbcTemplate();
        PreparedStatementSetter pss = pstmt -> {
        };
        RowMapper<User> rowMapper = rs -> new User(
                rs.getString("userId"),
                rs.getString("password"),
                rs.getString("name"),
                rs.getString("email"));

        String sql = "SELECT * FROM USERS";
        return template.query(sql, rowMapper, pss);
    }

    public User findByUserId(String userId) {
        JdbcTemplate template = new JdbcTemplate();
        PreparedStatementSetter pss = pstmt -> pstmt.setString(1, userId);
        RowMapper<User> rowMapper = rs -> new User(
                rs.getString("userId"),
                rs.getString("password"),
                rs.getString("name"),
                rs.getString("email"));

        String sql = "SELECT userId, password, name, email FROM USERS WHERE userId=?";
        return template.queryForObject(sql, rowMapper, pss);
    }
}


