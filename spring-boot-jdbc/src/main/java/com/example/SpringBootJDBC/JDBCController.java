package com.example.SpringBootJDBC;

import com.example.SpringBootJDBC.domain.User;
import com.example.SpringBootJDBC.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.jdbc.core.StatementCallback;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.sql.*;
import java.util.*;

/**
 * @author zhang yuyang
 * @ClassName: com.example.SpringBootJDBC.JDBCController
 * @Description: JDBC 控制器
 * @create 2018/04/09 10:31
 */
@RestController
public class JDBCController {

    @Resource
    private DataSource dataSource;

    @Autowired
    private UserService userService;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * judge is if support transaction
     */
    @RequestMapping("/jdbc/meta/transaction/supported")
    public boolean supportedTransaction() {
        Connection connection = null;
        boolean result = false;
        try {
            connection = dataSource.getConnection();
            result = connection.getMetaData().supportsTransactions();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * get all users by the metaData
     *
     * @return
     */
    @RequestMapping("get/users")
    public List<Map<String, Object>> getUsers() {
        return jdbcTemplate.execute(new StatementCallback<List<Map<String, Object>>>() {
            @Override
            public List<Map<String, Object>> doInStatement(Statement stmt) throws SQLException, DataAccessException {
                ResultSet resultSet = stmt.executeQuery("SELECT * FROM user;");
                ResultSetMetaData metaData = resultSet.getMetaData();
                int columnCount = metaData.getColumnCount();
                List<String> columnsNames = new ArrayList<>(columnCount);
                for (int i = 1; i <= columnCount; i++) {
                    String columnName = metaData.getColumnName(i);
                    columnsNames.add(columnName);
                }

                List<Map<String, Object>> data = new LinkedList<>();
                while (resultSet.next()) {
                    Map<String, Object> map = new HashMap<>();
                    for (String columnsName : columnsNames) {
                        Object columnsValue = resultSet.getObject(columnsName);
                        map.put(columnsName, columnsValue);
                    }
                    data.add(map);
                }

                return data;
            }
        });
    }

    @RequestMapping("/user/get")
    public Map<String, Object> getUser(@RequestParam(value = "id", defaultValue = "1") int id) {
        Map<String, Object> data = new HashMap<>();
        Connection connection = null;
        try {
            connection = dataSource.getConnection();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM user WHERE id =" + id);
            while (resultSet.next()) {
                id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                int age = resultSet.getInt("age");
                data.put("id", id);
                data.put("name", name);
                data.put("age", age);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return data;
    }


    @PostMapping("/user/add")
    public Map<String, Object> addUser(@RequestBody User user) {
        Map<String, Object> data = new HashMap<>();
        boolean result = userService.addUser(user);
        data.put("result", result);
        return data;
    }


}
