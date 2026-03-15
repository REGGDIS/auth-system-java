package auth.util;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class ConexionBD {

    private static final String DEFAULT_URL = "jdbc:mysql://localhost:3306/sistema_usuarios";
    private static final String DEFAULT_USER = "root";
    private static final String DEFAULT_PASSWORD = "";

    private static final HikariDataSource dataSource;

    static {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(System.getenv().getOrDefault("DB_URL", DEFAULT_URL));
        config.setUsername(System.getenv().getOrDefault("DB_USER", DEFAULT_USER));
        config.setPassword(System.getenv().getOrDefault("DB_PASSWORD", DEFAULT_PASSWORD));
        
        // Configuraciones recomendadas para MySQL
        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "250");
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
        
        dataSource = new HikariDataSource(config);
    }

    public static Connection conectar() {
        try {
            return dataSource.getConnection();
        } catch (SQLException e) {
            System.out.println("❌ Error al obtener conexión del pool:");
            e.printStackTrace();
            return null;
        }
    }
}
