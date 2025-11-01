package CaNhan;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.Date;
import java.util.List;

public class FE extends JFrame {
    private JTextField txtId, txtName, txtProducer, txtPrice, txtDate, txtSearch;
    private JTable table;
    private DefaultTableModel model;
    private BE be;

    public FE() {
        setTitle("Product Infomation");
        setSize(750, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        be = new BE();

        // Panel nhập liệu
        JPanel inputPanel = new JPanel(new GridLayout(5, 2, 5, 5));
        inputPanel.setBorder(BorderFactory.createTitledBorder("Product Infomation"));
        JLabel lblId = new JLabel("ID:");
        lblId.setVisible(false);
        txtId = new JTextField();
        txtId.setVisible(false);
        inputPanel.add(lblId);
        inputPanel.add(txtId);
        inputPanel.add(new JLabel("Name *:")); txtName = new JTextField(); inputPanel.add(txtName);
        inputPanel.add(new JLabel("Producer:")); txtProducer = new JTextField(); inputPanel.add(txtProducer);
        inputPanel.add(new JLabel("Price *:")); txtPrice = new JTextField(); inputPanel.add(txtPrice);
        inputPanel.add(new JLabel("Created_date(yyyy-MM-dd) *:")); txtDate = new JTextField(); inputPanel.add(txtDate);

        add(inputPanel, BorderLayout.NORTH);

        // Bảng dữ liệu
        model = new DefaultTableModel(new Object[]{"ID", "Name", "Producer", "Price", "Created Date"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Không cho sửa bất kỳ ô nào owr table
            }
        };

        table = new JTable(model);
        add(new JScrollPane(table), BorderLayout.CENTER);

        // Panel nút chức năng
        JPanel btnPanel = new JPanel();
        JButton btnAdd = new JButton("New");
        JButton btnUpdate = new JButton("Edit");
        JButton btnDelete = new JButton("Delete");
        JButton btnClear = new JButton("Reset");
        JButton btnSearch = new JButton("Search");
        txtSearch = new JTextField(10);

        btnPanel.add(btnAdd); btnPanel.add(btnUpdate); btnPanel.add(btnDelete);
        btnPanel.add(btnClear); btnPanel.add(new JLabel("Search by Name:")); btnPanel.add(txtSearch); btnPanel.add(btnSearch);
        add(btnPanel, BorderLayout.SOUTH);

        // Load dữ liệu
        loadData();

        // Sự kiện chọn dòng
        table.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int row = table.getSelectedRow();
                if (row >= 0) {
//                    txtId.setText(model.getValueAt(row, 0).toString());
                    txtName.setText(model.getValueAt(row, 1).toString());
                    txtProducer.setText(model.getValueAt(row, 2).toString());
                    txtPrice.setText(model.getValueAt(row, 3).toString());
                    txtDate.setText(model.getValueAt(row, 4).toString());
                }
            }
        });

        // Thêm sản phẩm
        btnAdd.addActionListener(e -> {
            try {
                String name = txtName.getText().trim();
                String producer = txtProducer.getText().trim();
                String priceText = txtPrice.getText().trim();
                String dateText = txtDate.getText().trim();
                if(txtName.getText().trim().isEmpty() || txtPrice.getText().trim().isEmpty()
                        || txtDate.getText().trim().isEmpty()){
                    JOptionPane.showMessageDialog(this,"Please enter all require information!");
                    return;
                }
                double price;
                try {
                    price = Double.parseDouble(priceText);
                }
                catch (NumberFormatException ex){
                    JOptionPane.showMessageDialog(this, "Price is invalid!");
                    return;
                }
                if (producer.isEmpty()) producer = null;
                Product p = new Product();
                p.setName(txtName.getText().trim());
                p.setProducer(txtProducer.getText().trim());
                p.setPrice(Double.parseDouble(txtPrice.getText().trim()));
                p.setCreated_date(Date.valueOf(txtDate.getText().trim()));

                if (be.addProduct(p)) {
                    JOptionPane.showMessageDialog(this,"Add product Successfully!");
                    loadData(); clearFields();
                }
            }
            catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage()); }
        });
        // Sửa sản phẩm
        btnUpdate.addActionListener(e -> {
            try {
                int row = table.getSelectedRow();
                if (row < 0) {
                    JOptionPane.showMessageDialog(this, "Please select one row to edit!");
                    return;
                }

                int id = Integer.parseInt(model.getValueAt(row, 0).toString());
                String name = txtName.getText().trim();
                String producer = txtProducer.getText().trim();
                String priceText = txtPrice.getText().trim();
                String dateText = txtDate.getText().trim();

                if (name.isEmpty() || priceText.isEmpty() || dateText.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Please enter all require information!");
                    return;
                }

                double price;
                try {
                    price = Double.parseDouble(priceText);
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "Price iss invalid");
                    return;
                }

                if (producer.isEmpty()) producer = null;

                Product p = new Product(id, name, producer, price, Date.valueOf(dateText));

                if (be.updateProduct(p)) {
                    JOptionPane.showMessageDialog(this, "Update Successfully");
                    loadData();
                    clearFields();
                } else {
                    JOptionPane.showMessageDialog(this, "Update False");
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
            }
        });
        // Làm mới
        btnClear.addActionListener(e -> clearFields());

        // Tìm kiếm
        btnSearch.addActionListener(e -> {
            String keyword = txtSearch.getText().trim();
            List<Product> list = keyword.isEmpty() ? be.getAllProduct() : be.searchProduct(keyword);
            updateTable(list);
        });
        btnDelete.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow < 0) {
                JOptionPane.showMessageDialog(this, "Please select a row to delete!");
                return;
            }

            int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this product?", "Confirm", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                int id = Integer.parseInt(model.getValueAt(selectedRow, 0).toString());
                be.deleteProduct(id);
                loadData(); clearFields();
            }
        });
    }
    private void loadData() { updateTable(be.getAllProduct()); }
    private void updateTable(List<Product> list) {
        model.setRowCount(0);
        for (Product p : list)
            model.addRow(new Object[]{p.getId(), p.getName(), p.getProducer(), p.getPrice(), p.getCreated_date()});
    }
    private void clearFields() { txtId.setText(""); txtName.setText(""); txtProducer.setText(""); txtPrice.setText(""); txtDate.setText(""); txtSearch.setText(""); }
}
