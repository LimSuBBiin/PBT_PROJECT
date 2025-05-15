package org.execute.service;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DatabaseTestService {

    @Autowired
    private DataSource dataSource;

    public void testConnection() {
        try (Connection connection = dataSource.getConnection()) {
            System.out.println("MySQL에 연결되었습니다.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
