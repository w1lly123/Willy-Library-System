package com.dao;

import java.sql.Types;
import com.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.ConnectionCallback;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.CallableStatement;

@Repository
public class UserDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public String save(User user) {
        return jdbcTemplate.execute((ConnectionCallback<String>) con -> {
            try (CallableStatement cs = con.prepareCall("call sp_register_user(?, ?, ?, ?)")) {
                cs.setString(1, user.getPhoneNumber());
                cs.setString(2, user.getPassword());
                cs.setString(3, user.getUserName());
                // 註冊輸出參數來接收結果 (SUCCESS 或 DUPLICATE)
                cs.registerOutParameter(4, Types.VARCHAR);
                cs.execute();
                return cs.getString(4);
            }
        });
    }
    public User findByPhoneNumber(String phoneNumber) {
        return jdbcTemplate.execute((ConnectionCallback<User>) con -> {
            try (CallableStatement cs = con.prepareCall("{call sp_get_user_by_phone(?, ?, ?, ?, ?)}")) {
                
                cs.setString(1, phoneNumber);
                
                cs.registerOutParameter(2, Types.INTEGER);   //o_user_id
                cs.registerOutParameter(3, Types.VARCHAR);   //o_password
                cs.registerOutParameter(4, Types.VARCHAR);   //o_user_name
                cs.registerOutParameter(5, Types.TIMESTAMP); //o_last_login_time
                
                cs.execute();

                Integer userId = (Integer) cs.getObject(2);
                
                if (userId == null || userId == 0) {
                    return null; 
                }

                User u = new User();
                u.setUserId(userId);
                u.setPhoneNumber(phoneNumber);
                u.setPassword(cs.getString(3));
                u.setUserName(cs.getString(4));
                
                java.sql.Timestamp ts = cs.getTimestamp(5);
                if (ts != null) {
                    u.setLastLoginTime(ts.toLocalDateTime());
                }
                return u;
            }
        });
    }

    public void updateLastLoginTime(Integer userId) {
        jdbcTemplate.execute((ConnectionCallback<Object>) con -> {
            try (CallableStatement cs = con.prepareCall("CALL sp_update_login_time(?)")) {
                cs.setInt(1, userId);
                cs.execute();
                return null;
            }
        });
    }
}