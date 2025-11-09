package CaNhan;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBConnection {
    // Thông tin kết nối đến MySQL
    private static final String DB_URL = "jdbc:sqlite:D:/Project/BTCN/src/CaNhan/db/order.db"; // đổi "test" thành tên database của bạn

    // Hàm trả về đối tượng Connection
    public static Connection getConnection() {
        Connection conn = null;
        try {
            // Nạp driver JDBC cho MySQL
            Class.forName("org.sqlite.JDBC");

            // Tạo kết nối
            conn = DriverManager.getConnection(DB_URL);
            System.out.println("Kết nối SQlite thành công!");
        } catch (Exception e) {
            System.out.println("Kết nối SQlite thất bại!");
            e.printStackTrace();
        }
        return conn;
    }

    // Hàm main để test kết nối
    public static void main(String[] args) {
        Connection c = DBConnection.getConnection();
        if (c != null) {
            System.out.println("Đã kết nối và sẵn sàng thao tác DB!");
        } else {
            System.out.println("Không thể kết nối CSDL.");
        }
    }
}
