package core.jdbc;

import next.model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public abstract class SelectJdbcTemplate {
    @SuppressWarnings("rawtypes")
    public Object queryForObject(String sql) throws SQLException {
        List res = query(sql);
        if (res.isEmpty()) {
            return null;
        }
        User user = (User) res.get(0);
        System.out.println(user.getUserId() + user.getPassword() + user.getEmail() + user.getEmail());
        return res.get(0);
    }

    @SuppressWarnings("rawtypes")
    public List query(String sql) throws SQLException {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            con = ConnectionManager.getConnection();
            pstmt = con.prepareStatement(sql);
            setValues(pstmt);
            rs = pstmt.executeQuery();
            List<Object> res = new ArrayList<>();
            if (rs.next()) {
                res.add(mapRow(rs));
            }
            return res;
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (pstmt != null) {
                pstmt.close();
            }
            if (con != null) {
                con.close();
            }
        }
    }

    public abstract void setValues(PreparedStatement pstmt) throws SQLException;

    public abstract Object mapRow(ResultSet rs) throws SQLException;
}
