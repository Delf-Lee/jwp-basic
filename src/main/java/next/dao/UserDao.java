package next.dao;

import core.jdbc.JdbcTemplate;
import core.jdbc.PreparedStatementSetter;
import core.jdbc.RowMapper;
import next.model.User;

import java.security.spec.PSSParameterSpec;
import java.sql.*;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class UserDao {
    public void insert(User user) throws SQLException {
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

    public void update(User user) throws SQLException {
        JdbcTemplate template = new JdbcTemplate();

        PreparedStatementSetter pss = pstmt -> {
            pstmt.setString(1, user.getPassword());
            pstmt.setString(2, user.getName());
            pstmt.setString(3, user.getEmail());
            pstmt.setString(4, user.getUserId());
        };
        String sql = "UPDATE USERS SET password = ?, name = ?, email = ? WHERE userId = ?";
        template.update(sql, pss);
    }

    public void delete(String userId) throws SQLException {
        JdbcTemplate template = new JdbcTemplate();

        PreparedStatementSetter pss = pstmt -> pstmt.setString(1, userId);
        String sql = "DELETE FROM USERS WHERE userId=?";
        template.update(sql, pss);
    }

    public Collection<User> findAll() throws SQLException {
        JdbcTemplate template = new JdbcTemplate();
        PreparedStatementSetter pss = pstmt -> {
        };
        RowMapper rowMapper = rs -> new User(
                rs.getString("userId"),
                rs.getString("password"),
                rs.getString("name"),
                rs.getString("email"));

        String sql = "SELECT * FROM USERS";
        return (List<User>) template.query(sql, pss, rowMapper);
    }

    public User findByUserId(String userId) throws SQLException {
        JdbcTemplate template = new JdbcTemplate();
        PreparedStatementSetter pss = pstmt -> pstmt.setString(1, userId);
        RowMapper rowMapper = rs -> new User(
                rs.getString("userId"),
                rs.getString("password"),
                rs.getString("name"),
                rs.getString("email"));

        String sql = "SELECT userId, password, name, email FROM USERS WHERE userid=?";
        return (User) template.queryForObject(sql, pss, rowMapper);
    }
}


