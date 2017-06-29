package com.tistory.tobyspring.dao;

import com.tistory.tobyspring.dao.sql.SqlService;
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
public class UserDaoJdbc implements UserDao {

    private JdbcTemplate jdbcTemplate;

    private SqlService sqlService;

    private RowMapper<User> userMapper =
                new RowMapper<User>() {
                    @Override
                    public User mapRow(ResultSet rs, int i) throws SQLException {
                        return new User(rs.getString("id"),
                                        rs.getString("name"),
                                        rs.getString("password"),
                                        Level.valueOf(rs.getInt("level")),
                                        rs.getInt("login_count"),
                                        rs.getInt("recommend_count"),
                                        rs.getString("email"));
                    }
                };

    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public void setSqlService(SqlService sqlService) {
        this.sqlService = sqlService;
    }

    @Override
    public void add(final User user) {
        jdbcTemplate
                .update(sqlService.getSql("userAdd"),
                        user.getId(), user.getName(), user.getPassword(), user.getLevel().intValue(), user.getLoginCount(), user.getRecommendCount(), user.getEmail());
    }

    @Override
    public User get(String id) {
        return jdbcTemplate
                .queryForObject(sqlService.getSql("userGet"),
                                new Object[] { id },
                                this.userMapper);
    }

    @Override
    public List<User> getAll() {
        return jdbcTemplate
                .query(sqlService.getSql("userGetAll"),
                        this.userMapper);
    }

    @Override
    public void deleteAll() {
        jdbcTemplate
            .execute(sqlService.getSql("userDeleteAll"));
    }

    @Override
    public int getCount() {
        return jdbcTemplate
                .queryForInt(sqlService.getSql("userGetCount"));
    }

    @Override
    public void update(User user) {
        jdbcTemplate.update(sqlService.getSql("userUpdate"),
                user.getName(), user.getPassword(), user.getLevel().intValue(), user.getLoginCount(), user.getRecommendCount(), user.getEmail(),
                user.getId());
    }

    @Override
    public void createTable() {
        jdbcTemplate
            .execute(sqlService.getSql("userCreateTable"));
    }
}