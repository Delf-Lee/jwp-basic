package core.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class JdbcTemplate {
    public void update(String sql, PreparedStatementSetter pss) throws DataAccessException {
        try (Connection con = ConnectionManager.getConnection(); PreparedStatement pstmt = con.prepareStatement(sql);) {
            pss.setValues(pstmt);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException(e);
        }
    }

    @SuppressWarnings("rawtypes")
    public Object queryForObject(String sql, PreparedStatementSetter pss, RowMapper rowMapper) throws SQLException {
        List res = query(sql, pss, rowMapper);
        if (res.isEmpty()) {
            return null;
        }
        return res.get(0);
    }

    @SuppressWarnings("rawtypes")
    public List query(String sql, PreparedStatementSetter pss, RowMapper rowMapper) throws SQLException {

        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            con = ConnectionManager.getConnection();
            pstmt = con.prepareStatement(sql);
            pss.setValues(pstmt);
            rs = pstmt.executeQuery();
            List<Object> res = new ArrayList<>();
            if (rs.next()) {
                res.add(rowMapper.mapRow(rs));
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
}
