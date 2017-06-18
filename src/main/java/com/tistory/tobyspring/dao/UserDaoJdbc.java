package com.tistory.tobyspring.dao;

import com.tistory.tobyspring.domain.Level;
import com.tistory.tobyspring.domain.User;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * DAO (Data Access Object) <BR>
 * 사용자 정보를 DB에 넣고 관리할 수 있는 클래스 <BR>
 */
public class UserDaoJdbc implements UserDao{

    private JdbcTemplate jdbcTemplate;

    private RowMapper<User> userMapper =
                new RowMapper<User>() {
                    @Override
                    public User mapRow(ResultSet rs, int i) throws SQLException {
                        return new User(rs.getString("id"),
                                        rs.getString("name"),
                                        rs.getString("password"),
                                        Level.valueOf(rs.getInt("level")),
                                        rs.getInt("login_count"),
                                        rs.getInt("recommend_count"));
                    }
                };

    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public void add(final User user) {
        jdbcTemplate
                .update("INSERT INTO USERS (id, name, password, level, login_count, recommend_count) VALUES (?, ?, ?, ?, ?, ?)",
                        user.getId(), user.getName(), user.getPassword(), user.getLevel().intValue(), user.getLoginCount(), user.getRecommendCount());
    }

    @Override
    public User get(String id) {
        return jdbcTemplate
                .queryForObject("SELECT * FROM USERS WHERE ID = ?",
                                new Object[] { id },
                                this.userMapper);
    }

    @Override
    public List<User> getAll() {
        return jdbcTemplate
                .query("SELECT * FROM USERS",
                        this.userMapper);
    }

    @Override
    public void deleteAll() {
        jdbcTemplate
            .execute("DELETE FROM USERS");
    }

    @Override
    public int getCount() {
        return jdbcTemplate
                .queryForInt("SELECT COUNT(*) FROM USERS");
    }

    @Override
    public void createTable() {
        jdbcTemplate
            .execute("CREATE TABLE IF NOT EXISTS USERS ( " +
                        "ID VARCHAR(10) PRIMARY KEY, " +
                        "NAME VARCHAR(20) NOT NULL, " +
                        "PASSWORD VARCHAR(10) NOT NULL, " +
                        "LEVEL SMALLINT, " +
                        "LOGIN_COUNT INT, " +
                        "RECOMMEND_COUNT INT " +
                    ")");
    }
}
