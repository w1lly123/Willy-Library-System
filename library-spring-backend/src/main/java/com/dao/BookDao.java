package com.dao;

import com.model.Inventory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.ConnectionCallback;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

@Repository
public class BookDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<Inventory> findAllInventory() {
        return jdbcTemplate.execute((ConnectionCallback<List<Inventory>>) con -> {
            try (CallableStatement cs = con.prepareCall("{call sp_get_all_inventory(?)}")) {
                
                cs.registerOutParameter(1, Types.REF_CURSOR);
                
                cs.execute();

                try (ResultSet rs = (ResultSet) cs.getObject(1)) {
                    List<Inventory> list = new ArrayList<>();
                    while (rs != null && rs.next()) {
                        Inventory i = new Inventory();
                        i.setInventoryId(rs.getInt("inventory_id"));
                        i.setIsbn(rs.getString("isbn"));
                        i.setStatus(rs.getInt("status"));
                        i.setBookName(rs.getString("name"));
                        i.setAuthor(rs.getString("author"));
                        list.add(i);
                    }
                    return list;
                }
            }
        });
    }

    //[借書] 呼叫SP
    public String borrowBook(Integer userId, Integer inventoryId) {
        return jdbcTemplate.execute((ConnectionCallback<String>) con -> {
            try (CallableStatement cs = con.prepareCall("{call sp_borrow_book(?, ?, ?)}")) {
                cs.setInt(1, userId);
                cs.setInt(2, inventoryId);
                cs.registerOutParameter(3, Types.VARCHAR);
                cs.execute();
                return cs.getString(3);
            }
        });
    }

    //[還書] 呼叫SP
    public String returnBook(Integer userId, Integer inventoryId) {
        return jdbcTemplate.execute((ConnectionCallback<String>) con -> {
            try (CallableStatement cs = con.prepareCall("{call sp_return_book(?, ?, ?)}")) {
                cs.setInt(1, userId);
                cs.setInt(2, inventoryId);
                cs.registerOutParameter(3, Types.VARCHAR);
                cs.execute();
                return cs.getString(3);
            }
        });
    }
}