package CaNhan;

import java.sql.*;
import java.util.*;

public class BE {
    private Connection conn; // object truy xuất csdl

    public BE() { // hàm khoi tao contructor
        conn = DBConnection.getConnection(); //khi tao doi tuong BE contructor lấy kno tu DBconection gán vào conn
    }

    // get all sp
    public List<Product> getAllProduct() {
        List<Product> list = new ArrayList<>(); // tao 1 ds rỗng chứa cac sp
        String sql = "SELECT * FROM product";
        try (Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) { // gọi rs.next đọc dũ liệu,còn dưx liêu thi chạy tiep, hết thì tra false, dung vong lap
                Product p = new Product(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("producer"),
                        rs.getDouble("price"),
                        rs.getDate("created_date")
                );
                list.add(p);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // new sp
    public boolean addProduct(Product p) { //phương thúcư addproduct trả về true thêm tc, false thêm tb tham số product P
        //đối tương chứa dữ liệu cần chèn vào DB
        String sql = "INSERT INTO product(name, producer, price, created_date) VALUES (?,?,?,?)";
        try (PreparedStatement pst = conn.prepareStatement(sql)) { // gọi conn. để tạo 1 đóio tượng Prepare... dùng
            // đêr thực thi câu sql với các tham số ? ?
            pst.setString(1, p.getName());
            pst.setString(2, p.getProducer());
            pst.setDouble(3, p.getPrice());
            pst.setDate(4, p.getCreated_date());
            return pst.executeUpdate() > 0;// biêur thưc kiểm tra có ít nhất 1 dòng bị ảnh hưởng ko, có --> thành công
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // update sp
    public boolean updateProduct(Product p) {
        String sql = "UPDATE product SET name = ?, producer = ?, price = ?, created_date = ? WHERE id = ?";
        try (PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setString(1, p.getName());
            pst.setString(2, p.getProducer());
            pst.setDouble(3, p.getPrice());
            pst.setDate(4, p.getCreated_date());
            pst.setInt(5, p.getId());
            return pst.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // delete sp
    public boolean deleteProduct(int id) {
        String sql = "DELETE FROM product WHERE id = ?";
        try (PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setInt(1, id);
            return pst.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // searchSP
    public List<Product> searchProduct(String keyword) {
        List<Product> list = new ArrayList<>();
        String sql = "SELECT * FROM product WHERE name LIKE ?";
        try (PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setString(1, "%" + keyword + "%");
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                Product p = new Product(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("producer"),
                        rs.getDouble("price"),
                        rs.getDate("created_date")
                );
                list.add(p);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}
