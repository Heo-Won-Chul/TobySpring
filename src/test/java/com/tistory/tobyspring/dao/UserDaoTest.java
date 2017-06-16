package com.tistory.tobyspring.dao;

import com.tistory.tobyspring.domain.User;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.sql.SQLException;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

// JUnit이 테스트를 진행하는 중에 테스트에 사용할 어플리케이션 컨텍스트를 만들고 관리하는 작업을 진행
@RunWith(SpringJUnit4ClassRunner.class)
// 자동으로 만들어줄 어플리케이션 컨텍스트의 설정 파일 위치 지정
@ContextConfiguration(locations = "/test-context-datasource.xml")
// @DirtiesContext // 해당 레벨(클래스, 메소드)에 해당하는 컨텍스트 구성이나 상태를 변경
public class UserDaoTest {

    @Autowired
    private UserDao dao;

    private User user1 = new User("wonchul", "허원철", "1234");;
    private User user2 = new User("naeun", "전나은", "1234");
    private User user3 = new User("toby", "이일민", "1234");

    @Before
    public void before_init() throws SQLException {
        dao.createTable();
    }

    @Test
    public void test_addAndGet() throws SQLException {
        dao.deleteAll();
        assertThat(dao.getCount(), is(0));

        dao.add(user1);
        assertThat(dao.getCount(), is(1));

        user2 = dao.get(user1.getId());

        assertThat(user1.getName(), is(user2.getName()));
        assertThat(user1.getPassword(), is(user2.getPassword()));
    }

    @Test
    public void test_count() throws SQLException {
        dao.deleteAll();
        assertThat(dao.getCount(), is(0));

        dao.add(user1);
        assertThat(dao.getCount(), is(1));

        dao.add(user2);
        assertThat(dao.getCount(), is(2));

        dao.add(user3);
        assertThat(dao.getCount(), is(3));
    }

    @Test(expected = EmptyResultDataAccessException.class)
    public void test_getUserFailure() throws SQLException {
        dao.deleteAll();
        assertThat(dao.getCount(), is(0));

        dao.get("unknown_id"); // Incorrect result size: expected 1, actual 0
    }

    @Test
    public void test_getAll() throws SQLException {
        dao.deleteAll();

        dao.add(user1);
        List<User> userList1 = dao.getAll();
        assertThat(userList1.size(), is(1));
        checkSameUser(user1, userList1.get(0));

        dao.add(user2);
        List<User> userList2 = dao.getAll();
        assertThat(userList2.size(), is(2));
        checkSameUser(user1, userList2.get(0));
        checkSameUser(user2, userList2.get(1));

        dao.add(user3);
        List<User> userList3 = dao.getAll();
        assertThat(userList3.size(), is(3));
        checkSameUser(user1, userList3.get(0));
        checkSameUser(user2, userList3.get(1));
        checkSameUser(user3, userList3.get(2));
    }

    private void checkSameUser(User user1, User user2) throws SQLException {
        assertThat(user1.getId(), is(user2.getId()));
        assertThat(user1.getName(), is(user2.getName()));
        assertThat(user1.getPassword(), is(user2.getPassword()));
    }
}