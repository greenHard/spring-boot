package com.example.SpringBootMyBatis.handle;

import com.example.SpringBootMyBatis.entity.Description;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeHandler;

import java.io.IOException;
import java.io.StringWriter;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author zhang yuyang
 * @ClassName: com.example.SpringBootMyBatis.handle.DescriptionTypeHandler
 * @Description: 类型转换
 * @create 2018/04/12 16:53
 */
public class DescriptionTypeHandler implements TypeHandler<Description>{

    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void setParameter(PreparedStatement ps, int i, Description parameter, JdbcType jdbcType) throws SQLException {
        try {
            StringWriter stringWriter = new StringWriter();
            objectMapper.writeValue(stringWriter,parameter);
            String desc = stringWriter.toString();
            ps.setString(i,desc);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Description getResult(ResultSet rs, String columnName) throws SQLException {
        String desc = rs.getString(columnName);
        Description description = null;
        return null;
    }

    @Override
    public Description getResult(ResultSet rs, int columnIndex) throws SQLException {
        return null;
    }

    @Override
    public Description getResult(CallableStatement cs, int columnIndex) throws SQLException {
        return null;
    }
}
