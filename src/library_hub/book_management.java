package library_hub;

import java.sql.*;
import java.util.Vector;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.RowFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

public class book_management extends javax.swing.JFrame {
    
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(book_management.class.getName());
    private String originalAcqNo = ""; 
    private int id;
    private String check;
    public book_management() {
    initComponents();

    fillAllCategoryComboBoxes();

    populateTable();

    cmbStatusBook.setSelectedItem("Available");
    
    cmbCategoryTbl.setSelectedItem("~Select Category~");

    filterTable();

    lockFields();
    
    addNewBtn.setEnabled(true);
    closeBtn.setEnabled(true);
    updateBtn.setEnabled(false);
    deleteBtn.setEnabled(false);
    saveBtn.setEnabled(false);

    try {
        javax.swing.text.MaskFormatter dateMask = new javax.swing.text.MaskFormatter("##/##/####");
        dateMask.setPlaceholderCharacter('_');
        dateMask.install(txtDate);
    } catch (java.text.ParseException e) {
        System.out.println("Mask Error: " + e.getMessage());
    }
}
    
    private boolean isValidDate(String dateStr) {

    if (dateStr.contains("_") || dateStr.length() < 10) return false;

    try {

        java.time.format.DateTimeFormatter dtf = 
        java.time.format.DateTimeFormatter.ofPattern("MM/dd/uuuu")
        .withResolverStyle(java.time.format.ResolverStyle.STRICT);
            
        java.time.LocalDate inputDate = java.time.LocalDate.parse(dateStr, dtf);
        java.time.LocalDate today = java.time.LocalDate.now(); 

        if (inputDate.isAfter(today)) {
            return false;
        }

        return inputDate.getYear() >= 1450;
        
    } catch (java.time.format.DateTimeParseException e) {
        return false; 
    } catch (Exception e) {
        return false;
    }
}
    
    public void fillAllCategoryComboBoxes() {
    try {
        Connection conn = MySQLConnect.getConnection();
        String sql = "SELECT DISTINCT category_name FROM categories ORDER BY category_name ASC";
        PreparedStatement pst = conn.prepareStatement(sql);
        ResultSet rs = pst.executeQuery();

        cmbCategory.removeAllItems();    
        cmbCategoryTbl.removeAllItems(); 

        cmbCategory.addItem("~Select Category~");
        cmbCategoryTbl.addItem("~Select Category~"); 

        while (rs.next()) {
            String catName = rs.getString("category_name");
            cmbCategory.addItem(catName);
            cmbCategoryTbl.addItem(catName); 
        }
    } catch (SQLException e) {
        System.out.println("Error: " + e.getMessage());
    }
}
    
    private void filterTable() {
        
    if (cmbCategoryTbl.getSelectedItem() == null || cmbStatusBook.getSelectedItem() == null) {
        return; 
    }

    String selectedCategory = cmbCategoryTbl.getSelectedItem().toString();
    String selectedStatus = cmbStatusBook.getSelectedItem().toString();
    String searchText = txtSearch.getText().toLowerCase().trim();

    DefaultTableModel model = (DefaultTableModel) jTable2.getModel();
    TableRowSorter<DefaultTableModel> trs = new TableRowSorter<>(model);
    jTable2.setRowSorter(trs);

    java.util.List<RowFilter<Object, Object>> filters = new java.util.ArrayList<>();

    if (!selectedCategory.equals("~Select Category~")) {
        filters.add(RowFilter.regexFilter("(?i)" + selectedCategory, 5));
    }

    if (!selectedStatus.equals("All")) {
        filters.add(RowFilter.regexFilter("(?i)^" + selectedStatus + "$", 7));
    }

    if (!searchText.isEmpty()) {
        filters.add(RowFilter.regexFilter("(?i)" + searchText));
    }

    if (filters.isEmpty()) {
        trs.setRowFilter(null);
    } else {
        trs.setRowFilter(RowFilter.andFilter(filters));
    }
    
    updateBookCount();
}

    public void fillCategoryCombo() {
        
    cmbCategory.removeAllItems();
    
    cmbCategory.addItem("~Select Category~");
    
    try {
        Connection conn = MySQLConnect.getConnection();
        String sql = "SELECT category_name FROM categories ORDER BY category_name ASC";
        PreparedStatement pst = conn.prepareStatement(sql);
        ResultSet rs = pst.executeQuery();
        
        while(rs.next()) {
      
            cmbCategory.addItem(rs.getString("category_name"));
        }
    } catch (SQLException e) {
        System.out.println("Error: " + e.getMessage());
    }
}
     private void unlockFields() {
    txtAcq.setEnabled(true);
    txtTitle.setEnabled(true);
    txtAuthor.setEnabled(true);
    txtDate.setEnabled(true);
    cmbCategory.setEnabled(true);
    cmbStatus.setEnabled(true);
    }
    public void lockFields() {
     txtAcq.setEnabled(false);
        txtTitle.setEnabled(false);
        txtAuthor.setEnabled(false);
        txtDate.setEnabled(false);
        cmbCategory.setEnabled(false);
        btnAddCategory.setEnabled(false);
        btnDeleteCategory.setEnabled(false);
        cmbStatus.setEnabled(false);
        saveBtn.setEnabled(false);
        updateBtn.setEnabled(false);
        deleteBtn.setEnabled(false);
 }
    
    private void resetButtons() {
    addNewBtn.setEnabled(true);
    closeBtn.setEnabled(true);
    updateBtn.setEnabled(false);
    deleteBtn.setEnabled(false);
    saveBtn.setEnabled(false);
    
    txtAcq.setEnabled(true); 
}
    
    public void updateBookCount() {

        int count = jTable2.getRowSorter() == null ? jTable2.getRowCount() : jTable2.getRowSorter().getViewRowCount();
        txtTotalCount.setText(String.valueOf(count));
}
    
   public void search(String str) {
    DefaultTableModel model = (DefaultTableModel) jTable2.getModel();
    model.setRowCount(0); 

    try {
        Connection conn = MySQLConnect.getConnection();
        String selectedStatus = cmbStatusBook.getSelectedItem().toString();
        String selectedCat = cmbCategoryTbl.getSelectedItem().toString();
        
        StringBuilder sql = new StringBuilder("SELECT * FROM books WHERE (acquisition_no LIKE ? OR title LIKE ? OR author LIKE ?)");
        
        if (!selectedStatus.equals("All")) {
            sql.append(" AND status = ?");
        }
        if (!selectedCat.equals("~Select Category~")) {
            sql.append(" AND category = ?");
        }

        PreparedStatement pst = conn.prepareStatement(sql.toString());
        String searchData = "%" + str + "%";
        pst.setString(1, searchData);
        pst.setString(2, searchData);
        pst.setString(3, searchData);

        int paramIndex = 4;
        if (!selectedStatus.equals("All")) {
            pst.setString(paramIndex++, selectedStatus);
        }
        if (!selectedCat.equals("~Select Category~")) {
            pst.setString(paramIndex++, selectedCat);
        }

        ResultSet rs = pst.executeQuery();
        while(rs.next()) {
            java.sql.Date dbDate = rs.getDate("date_published");
            String formattedDate = (dbDate != null) ? new java.text.SimpleDateFormat("MM/dd/yyyy").format(dbDate) : "";
            
            model.addRow(new Object[]{
                rs.getInt("id"),
                rs.getString("acquisition_no"),
                rs.getString("title"),
                rs.getString("author"),
                formattedDate,
                rs.getString("category"),
                rs.getString("price"),
                rs.getString("status")
            });
        }
    } catch (SQLException e) {
        JOptionPane.showMessageDialog(null, "Search Error: " + e.getMessage());
    }
    updateBookCount();
}
   
    public void populateTable() {
    try {
        Connection conn = MySQLConnect.getConnection();
        // It is safer to select specific columns to ensure they match your Vector order
        String query = "SELECT id, acquisition_no, title, author, date_published, price, category, status FROM books"; 
        Statement st = conn.createStatement();
        ResultSet rs = st.executeQuery(query);

        DefaultTableModel tblModel = (DefaultTableModel) jTable2.getModel();
        tblModel.setRowCount(0);

       while (rs.next()) {
    Vector colData = new Vector();
    colData.add(rs.getInt("id"));               // Index 0 (Hidden)
    colData.add(rs.getString("acquisition_no"));// Index 1 (Acquisition No)
    colData.add(rs.getString("title"));         // Index 2 (Book Title)
    colData.add(rs.getString("author"));        // Index 3 (Book Author)
    
    // Date formatting
    java.sql.Date dbDate = rs.getDate("date_published"); 
    String formattedDate = (dbDate != null) ? 
        new java.text.SimpleDateFormat("MM/dd/yyyy").format(dbDate) : "";
    colData.add(formattedDate);                 // Index 4 (Date Published)

    // Match these to your UI Headers
    colData.add(rs.getString("category"));      // Index 5 (Category)
    colData.add(rs.getString("price"));         // Index 6 (Book Price)
    colData.add(rs.getString("status"));        // Index 7 (Status)
    
    tblModel.addRow(colData);
}
    } catch (SQLException e) {
        JOptionPane.showMessageDialog(null, "Error populating table: " + e.getMessage());
    }
    
    // Keep the ID column hidden
    jTable2.getColumnModel().getColumn(0).setMinWidth(0);
    jTable2.getColumnModel().getColumn(0).setMaxWidth(0);
    jTable2.getColumnModel().getColumn(0).setWidth(0);
    jTable2.getTableHeader().getColumnModel().getColumn(0).setMaxWidth(0);
    
    updateBookCount();
}
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel3 = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        jButton5 = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        txtAcq = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        txtTitle = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        cmbCategory = new javax.swing.JComboBox<>();
        addNewBtn = new javax.swing.JButton();
        updateBtn = new javax.swing.JButton();
        deleteBtn = new javax.swing.JButton();
        saveBtn = new javax.swing.JButton();
        jLabel10 = new javax.swing.JLabel();
        cmbStatus = new javax.swing.JComboBox<>();
        txtAuthor = new javax.swing.JTextField();
        closeBtn = new javax.swing.JButton();
        txtDate = new javax.swing.JFormattedTextField();
        jLabel11 = new javax.swing.JLabel();
        btnAddCategory = new javax.swing.JButton();
        btnDeleteCategory = new javax.swing.JButton();
        btnAddCopy = new javax.swing.JButton();
        txtBookPrice = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        closeBtnn = new javax.swing.JPanel();
        txtSearch = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        jPanel6 = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        txtTotalCount = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTable2 = new javax.swing.JTable();
        cancelBtn1 = new javax.swing.JButton();
        STATUS = new javax.swing.JLabel();
        cmbStatusBook = new javax.swing.JComboBox<>();
        jLabel13 = new javax.swing.JLabel();
        close = new javax.swing.JButton();
        STATUS1 = new javax.swing.JLabel();
        cmbCategoryTbl = new javax.swing.JComboBox<>();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel3.setBackground(new java.awt.Color(0, 102, 102));
        jPanel3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 5));
        jPanel3.setPreferredSize(new java.awt.Dimension(285, 53));

        jLabel8.setFont(new java.awt.Font("Century Gothic", 1, 24)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(255, 255, 255));
        jLabel8.setText("BOOK MANAGEMENT");

        jButton5.setText("x");
        jButton5.addActionListener(this::jButton5ActionPerformed);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel8)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 655, Short.MAX_VALUE)
                .addComponent(jButton5)
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel8)
                    .addComponent(jButton5))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel4.setBackground(new java.awt.Color(0, 102, 102));
        jPanel4.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 5));

        jLabel2.setBackground(new java.awt.Color(0, 102, 102));
        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("ACQUISITION No");

        txtAcq.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txtAcqMouseClicked(evt);
            }
        });
        txtAcq.addActionListener(this::txtAcqActionPerformed);
        txtAcq.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtAcqKeyReleased(evt);
            }
        });

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("BOOK TITLE");

        txtTitle.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txtTitleMouseClicked(evt);
            }
        });
        txtTitle.addActionListener(this::txtTitleActionPerformed);

        jLabel4.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("BOOK AUTHOR");

        jLabel5.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setText("DATE PUBLISHED  ");

        jLabel6.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setText("SELECT CATEGORY");

        cmbCategory.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                cmbCategoryMouseClicked(evt);
            }
        });
        cmbCategory.addActionListener(this::cmbCategoryActionPerformed);

        addNewBtn.setText("ADD NEW");
        addNewBtn.addActionListener(this::addNewBtnActionPerformed);

        updateBtn.setText("UPDATE");
        updateBtn.addActionListener(this::updateBtnActionPerformed);

        deleteBtn.setText("DELETE");
        deleteBtn.addActionListener(this::deleteBtnActionPerformed);

        saveBtn.setText("SAVE");
        saveBtn.addActionListener(this::saveBtnActionPerformed);

        jLabel10.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(255, 255, 255));
        jLabel10.setText("SELECT STATUS");

        cmbStatus.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Available", "Unavailable" }));
        cmbStatus.addActionListener(this::cmbStatusActionPerformed);

        txtAuthor.addActionListener(this::txtAuthorActionPerformed);

        closeBtn.setText("CANCEL");
        closeBtn.addActionListener(this::closeBtnActionPerformed);

        txtDate.addActionListener(this::txtDateActionPerformed);

        jLabel11.setForeground(new java.awt.Color(255, 255, 255));
        jLabel11.setText("(mm/dd/yyyy)");

        btnAddCategory.setBackground(new java.awt.Color(255, 51, 51));
        btnAddCategory.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnAddCategory.setText("+");
        btnAddCategory.addActionListener(this::btnAddCategoryActionPerformed);

        btnDeleteCategory.setBackground(new java.awt.Color(0, 0, 255));
        btnDeleteCategory.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnDeleteCategory.setText("-");
        btnDeleteCategory.addActionListener(this::btnDeleteCategoryActionPerformed);

        btnAddCopy.setBackground(new java.awt.Color(255, 51, 51));
        btnAddCopy.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnAddCopy.setText("+");
        btnAddCopy.addActionListener(this::btnAddCopyActionPerformed);

        jLabel9.setBackground(new java.awt.Color(0, 102, 102));
        jLabel9.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(255, 255, 255));
        jLabel9.setText("BOOK PRICE");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                        .addComponent(closeBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(42, 42, 42))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(deleteBtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(addNewBtn))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(updateBtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(saveBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(34, 34, 34))
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap(23, Short.MAX_VALUE)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 179, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 179, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 179, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 179, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 179, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addComponent(txtTitle, javax.swing.GroupLayout.PREFERRED_SIZE, 172, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnAddCopy))
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addComponent(cmbCategory, javax.swing.GroupLayout.PREFERRED_SIZE, 144, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnAddCategory)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnDeleteCategory)))
                        .addContainerGap(24, Short.MAX_VALUE))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel9)
                            .addComponent(txtDate, javax.swing.GroupLayout.PREFERRED_SIZE, 210, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 179, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 179, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtAuthor, javax.swing.GroupLayout.PREFERRED_SIZE, 210, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtAcq, javax.swing.GroupLayout.PREFERRED_SIZE, 209, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(txtBookPrice, javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(cmbStatus, javax.swing.GroupLayout.Alignment.LEADING, 0, 209, Short.MAX_VALUE)))
                        .addGap(0, 0, Short.MAX_VALUE))))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtAcq, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtTitle, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnAddCopy))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtAuthor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel5)
                .addGap(1, 1, 1)
                .addComponent(jLabel11)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtDate, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cmbCategory, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnAddCategory)
                    .addComponent(btnDeleteCategory))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel10)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cmbStatus, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel9)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtBookPrice, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(addNewBtn)
                    .addComponent(updateBtn))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(deleteBtn)
                    .addComponent(saveBtn))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(closeBtn)
                .addContainerGap(14, Short.MAX_VALUE))
        );

        cmbCategory.getAccessibleContext().setAccessibleName("");

        closeBtnn.setBackground(new java.awt.Color(0, 102, 102));
        closeBtnn.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 5));

        txtSearch.addActionListener(this::txtSearchActionPerformed);
        txtSearch.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtSearchKeyReleased(evt);
            }
        });

        jLabel7.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(255, 255, 255));

        jPanel6.setBackground(new java.awt.Color(255, 102, 0));
        jPanel6.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jPanel5.setBackground(new java.awt.Color(204, 204, 204));
        jPanel5.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 42, Short.MAX_VALUE)
        );

        txtTotalCount.setEditable(false);
        txtTotalCount.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        txtTotalCount.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        txtTotalCount.addActionListener(this::txtTotalCountActionPerformed);

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel1.setText("TOTAL BOOK No");

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(8, 8, 8)
                .addComponent(txtTotalCount, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel5, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                        .addComponent(txtTotalCount, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())))
        );

        jTable2.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null}
            },
            new String [] {
                "Id", "Acquisition No", "Book Title", "Book Author", "Date Published", "Category", "Book Price", "Status"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable2.setShowGrid(false);
        jTable2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable2MouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(jTable2);

        cancelBtn1.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        cancelBtn1.setText("←GO TO BORROW");
        cancelBtn1.addActionListener(this::cancelBtn1ActionPerformed);

        STATUS.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        STATUS.setForeground(new java.awt.Color(255, 255, 255));
        STATUS.setText("STATUS");

        cmbStatusBook.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Available", "Unavailable" }));
        cmbStatusBook.addActionListener(this::cmbStatusBookActionPerformed);

        jLabel13.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel13.setForeground(new java.awt.Color(255, 255, 255));
        jLabel13.setText("SEARCH TITLE");

        close.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        close.setText("x");
        close.addActionListener(this::closeActionPerformed);

        STATUS1.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        STATUS1.setForeground(new java.awt.Color(255, 255, 255));
        STATUS1.setText("CATEGORY");

        cmbCategoryTbl.addActionListener(this::cmbCategoryTblActionPerformed);

        javax.swing.GroupLayout closeBtnnLayout = new javax.swing.GroupLayout(closeBtnn);
        closeBtnn.setLayout(closeBtnnLayout);
        closeBtnnLayout.setHorizontalGroup(
            closeBtnnLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(closeBtnnLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(closeBtnnLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(closeBtnnLayout.createSequentialGroup()
                        .addComponent(jLabel13)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(close)
                        .addGap(12, 12, 12)
                        .addComponent(STATUS1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cmbCategoryTbl, javax.swing.GroupLayout.PREFERRED_SIZE, 173, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(STATUS)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(cmbStatusBook, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(8, 8, 8))
                    .addGroup(closeBtnnLayout.createSequentialGroup()
                        .addComponent(jScrollPane2)
                        .addContainerGap())
                    .addGroup(closeBtnnLayout.createSequentialGroup()
                        .addComponent(jLabel7)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, closeBtnnLayout.createSequentialGroup()
                .addContainerGap(738, Short.MAX_VALUE)
                .addComponent(cancelBtn1, javax.swing.GroupLayout.PREFERRED_SIZE, 195, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        closeBtnnLayout.setVerticalGroup(
            closeBtnnLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(closeBtnnLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel7)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(closeBtnnLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, closeBtnnLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(txtSearch, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel13)
                        .addComponent(STATUS)
                        .addComponent(cmbStatusBook, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(close)
                        .addComponent(STATUS1)
                        .addComponent(cmbCategoryTbl, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jPanel6, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(38, 38, 38)
                .addComponent(cancelBtn1, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(63, 63, 63))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, 949, Short.MAX_VALUE)
                    .addComponent(closeBtnn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(closeBtnn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addComponent(jPanel4, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents
    
    public void makeEnable(){
    
        txtAcq.setEnabled(true);
        txtTitle.setEnabled(true);
        txtAuthor.setEnabled(true);
        txtDate.setEnabled(true);
        cmbCategory.setEnabled(true);
        cmbStatus.setEnabled(true);
    }
    public void setDefault() {

    txtAcq.setText("");
    txtTitle.setText("");
    txtAuthor.setText("");
    txtDate.setText("");
    cmbCategory.setSelectedIndex(0);
    cmbStatus.setSelectedIndex(0);

    txtAcq.setEnabled(false);
    txtTitle.setEnabled(false);
    txtAuthor.setEnabled(false);
    txtDate.setEnabled(false);
    cmbCategory.setEnabled(false);
    cmbStatus.setEnabled(false);
    
    addNewBtn.setEnabled(true);   
    updateBtn.setEnabled(false);  
    deleteBtn.setEnabled(false);  
    saveBtn.setEnabled(false);   
    
    txtDate.setValue(null);
    
    id = -1; 
}
    
    
    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        this.dispose();
        frontpage w = new frontpage();
        w.setVisible(true);
    }//GEN-LAST:event_jButton5ActionPerformed

    private void txtAcqMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtAcqMouseClicked

    }//GEN-LAST:event_txtAcqMouseClicked

    private void txtAcqActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtAcqActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtAcqActionPerformed

    private void txtAcqKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtAcqKeyReleased

    }//GEN-LAST:event_txtAcqKeyReleased

    private void txtTitleMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtTitleMouseClicked

    }//GEN-LAST:event_txtTitleMouseClicked

    private void txtTitleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtTitleActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtTitleActionPerformed

    private void cmbCategoryMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_cmbCategoryMouseClicked

    }//GEN-LAST:event_cmbCategoryMouseClicked

    private void cmbCategoryActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbCategoryActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cmbCategoryActionPerformed

    private void addNewBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addNewBtnActionPerformed
        // Enable the text fields
        txtAcq.setEnabled(true);
        txtTitle.setEnabled(true);
        txtAuthor.setEnabled(true);
        txtDate.setEnabled(true);
        
        addNewBtn.setEnabled(false);

        cmbCategory.setEnabled(true);
        btnAddCategory.setEnabled(true);    // Unlock the '+'
        btnDeleteCategory.setEnabled(true); // Unlock the '-'

        cmbStatus.setEnabled(true);
        saveBtn.setEnabled(true);

        txtAcq.requestFocus();
    }//GEN-LAST:event_addNewBtnActionPerformed

    private void updateBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_updateBtnActionPerformed
       String newAcqNo = txtAcq.getText().trim();
    String title = txtTitle.getText().trim();
    String author = txtAuthor.getText().trim();
    String dateInput = txtDate.getText(); 
    String priceInput = txtBookPrice.getText().trim(); // New: Get price input
    
    Object selectedCatObj = cmbCategory.getSelectedItem();
    String category = (selectedCatObj != null) ? selectedCatObj.toString() : "";
    Object selectedStatusObj = cmbStatus.getSelectedItem();

    // Validations
    if (newAcqNo.isEmpty()) {
        JOptionPane.showMessageDialog(this, "Select a book from the table first!");
        return;
    }
    
    if (title.isEmpty()) {
        JOptionPane.showMessageDialog(this, "Book Title is required!");
        txtTitle.requestFocus();
        return;
    }

    // New: Price Validation
    double price = 0.0;
    try {
        price = Double.parseDouble(priceInput);
    } catch (NumberFormatException e) {
        JOptionPane.showMessageDialog(this, "Invalid Price! Please enter a numeric value.");
        txtBookPrice.requestFocus();
        return;
    }

    if (!isValidDate(dateInput)) {
        JOptionPane.showMessageDialog(this, 
            "Invalid Date Published!\n" +
            "- Date must exist.\n" +
            "- Date cannot be in the future.", 
            "Date Error", JOptionPane.ERROR_MESSAGE);
        txtDate.requestFocus();
        return;
    }

    if (category.equals("~Select Category~") || category.isEmpty()) {
        JOptionPane.showMessageDialog(this, "Please select a valid Book Category!");
        cmbCategory.requestFocus();
        return;
    }

    if (author.isEmpty()) {
        author = "Anonymous";
    }

    try {
        Connection conn = MySQLConnect.getConnection();
        
        java.text.SimpleDateFormat inputFormat = new java.text.SimpleDateFormat("MM/dd/yyyy");
        java.util.Date parsedDate = inputFormat.parse(dateInput);
        java.sql.Date sqlDatePublished = new java.sql.Date(parsedDate.getTime());
        
        // Duplicate check if Acquisition No is being changed
        if (!newAcqNo.equals(originalAcqNo)) {
            String checkSql = "SELECT title FROM books WHERE acquisition_no = ?";
            PreparedStatement pstCheck = conn.prepareStatement(checkSql);
            pstCheck.setString(1, newAcqNo);
            ResultSet rsCheck = pstCheck.executeQuery();
            
            if (rsCheck.next()) {
                JOptionPane.showMessageDialog(this, 
                    "Error: Acquisition Number '" + newAcqNo + "' is already assigned to: " + rsCheck.getString("title"), 
                    "Duplicate ID", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }
        
        // Updated SQL to include price
        String sql = "UPDATE books SET acquisition_no = ?, title = ?, author = ?, date_published = ?, category = ?, price = ?, status = ? WHERE acquisition_no = ?";
        PreparedStatement pst = conn.prepareStatement(sql);

        pst.setString(1, newAcqNo);      
        pst.setString(2, title);
        pst.setString(3, author);
        pst.setDate(4, sqlDatePublished);
        pst.setString(5, category);
        pst.setDouble(6, price);            // New: Set Price
        pst.setObject(7, selectedStatusObj);
        pst.setString(8, originalAcqNo);    // Matches WHERE clause

        int updatedRows = pst.executeUpdate();
        
        if (updatedRows > 0) {
            JOptionPane.showMessageDialog(this, "Book Updated Successfully!");
            originalAcqNo = newAcqNo; // Update reference for subsequent edits
            
            fillAllCategoryComboBoxes(); 
            populateTable(); 
            setDefault(); 
        } else {
            JOptionPane.showMessageDialog(this, "Error: Book with Acquisition No " + originalAcqNo + " not found.");
        }
    } catch (java.text.ParseException e) {
        JOptionPane.showMessageDialog(this, "Date Error: Please use the format MM/dd/yyyy");
    } catch (SQLException e) {
        JOptionPane.showMessageDialog(this, "Database Error: " + e.getMessage());
    }
    }//GEN-LAST:event_updateBtnActionPerformed

    private void deleteBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteBtnActionPerformed
        if (JOptionPane.showConfirmDialog(null, "Are you sure you want to delete this?", "Confirm", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
    try {
        Connection conn = MySQLConnect.getConnection();
        
        int row = jTable2.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(null, "Please select a book from the table first!");
            return;
        }

        String query = "DELETE FROM books WHERE id = ?"; 
        PreparedStatement pst = conn.prepareStatement(query);
        pst.setInt(1, id); 
        
        int success = pst.executeUpdate();
        
        if (success > 0) {
            JOptionPane.showMessageDialog(null, "Book deleted successfully!");
      
            populateTable(); 
            setDefault();    
        } else {
            JOptionPane.showMessageDialog(null, "Delete failed. Could not find that ID.");
        }

    } catch(SQLException e) {
        JOptionPane.showMessageDialog(null, "Database Error: " + e.getMessage());
    }
    lockFields();
    
    btnAddCategory.setEnabled(false);    // The + button
    btnDeleteCategory.setEnabled(false); // The - button
}
    }//GEN-LAST:event_deleteBtnActionPerformed

    private void saveBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveBtnActionPerformed
        String acqNo = txtAcq.getText().trim();
    String title = txtTitle.getText().trim();
    String author = txtAuthor.getText().trim();
    String dateInput = txtDate.getText().trim(); 
    String priceInput = txtBookPrice.getText().trim(); // Assuming your price field is named txtPrice

    // Null safety for ComboBox selections
    Object selectedCatObj = cmbCategory.getSelectedItem();
    String category = (selectedCatObj != null) ? selectedCatObj.toString() : "";
    Object selectedStatusObj = cmbStatus.getSelectedItem();

    // Validations
    if (acqNo.isEmpty()) {
        JOptionPane.showMessageDialog(this, "Acquisition No is required!");
        txtAcq.requestFocus();
        return;
    }

    if (title.isEmpty()) {
        JOptionPane.showMessageDialog(this, "Book Title is required!");
        txtTitle.requestFocus();
        return;
    }

    // Price Validation
    double price = 0.0;
    if (priceInput.isEmpty()) {
        JOptionPane.showMessageDialog(this, "Book Price is required!");
        txtBookPrice.requestFocus();
        return;
    } else {
        try {
            price = Double.parseDouble(priceInput);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid Price! Please enter a numeric value (e.g., 500.00)");
            txtBookPrice.requestFocus();
            return;
        }
    }

    if (!isValidDate(dateInput)) {
        JOptionPane.showMessageDialog(this, 
            "Invalid Date Published!\n" +
            "- Date must exist.\n" +
            "- Date cannot be in the future.", 
            "Date Error", JOptionPane.ERROR_MESSAGE);
        txtDate.requestFocus();
        return;
    }

    String mysqlDate;
    try {
        java.time.format.DateTimeFormatter inputFormatter = java.time.format.DateTimeFormatter.ofPattern("MM/dd/yyyy");
        java.time.LocalDate date = java.time.LocalDate.parse(dateInput, inputFormatter);
        mysqlDate = date.toString(); 
    } catch (Exception e) {
        mysqlDate = "0000-00-00";
    }

    if (category.equals("~Select Category~") || category.isEmpty()) {
        JOptionPane.showMessageDialog(this, "Please select a valid Book Category!");
        cmbCategory.requestFocus();
        return;
    }

    if (author.isEmpty()) {
        author = "Anonymous";
    }

    try {
        Connection conn = MySQLConnect.getConnection();

        // Check for duplicate Acquisition Number
        String checkQuery = "SELECT * FROM books WHERE acquisition_no = ?";
        PreparedStatement checkPst = conn.prepareStatement(checkQuery);
        checkPst.setString(1, acqNo);
        ResultSet rs = checkPst.executeQuery();

        if (rs.next()) {
            JOptionPane.showMessageDialog(this, "Error: Acquisition No " + acqNo + " already exists!", "Duplicate Entry", JOptionPane.ERROR_MESSAGE);
            txtAcq.requestFocus();
            return; 
        }

        // Insert into database
        String sql = "INSERT INTO books (acquisition_no, title, author, date_published, category, price, status) VALUES (?,?,?,?,?,?,?)";
        PreparedStatement pst = conn.prepareStatement(sql);

        pst.setString(1, acqNo);
        pst.setString(2, title);
        pst.setString(3, author);
        pst.setString(4, mysqlDate); 
        pst.setString(5, category);
        pst.setDouble(6, price); // Price mapping
        pst.setObject(7, selectedStatusObj);

        int result = pst.executeUpdate();
        if (result > 0) {
            JOptionPane.showMessageDialog(this, "Book Added Successfully!");
            
            fillAllCategoryComboBoxes(); 
            populateTable(); 
            setDefault(); 
        }

    } catch (SQLException e) {
        JOptionPane.showMessageDialog(this, "Database Error: " + e.getMessage());
    }
    }//GEN-LAST:event_saveBtnActionPerformed

    private void cmbStatusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbStatusActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cmbStatusActionPerformed

    private void txtAuthorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtAuthorActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtAuthorActionPerformed

    private void txtSearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtSearchActionPerformed

    }//GEN-LAST:event_txtSearchActionPerformed

    private void txtSearchKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSearchKeyReleased
        search(txtSearch.getText()); 
        filterTable(); 
    }//GEN-LAST:event_txtSearchKeyReleased

    private void txtTotalCountActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtTotalCountActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtTotalCountActionPerformed

    private void jTable2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable2MouseClicked
        int viewRow = jTable2.getSelectedRow();

    if (viewRow != -1) {
        int modelRow = jTable2.convertRowIndexToModel(viewRow);
        DefaultTableModel tblModel = (DefaultTableModel) jTable2.getModel();

        // Store ID and Original Acquisition Number for updates
        id = Integer.parseInt(tblModel.getValueAt(modelRow, 0).toString());
        originalAcqNo = tblModel.getValueAt(modelRow, 1).toString(); 

        // Mapping Data to Text Fields
        txtAcq.setText(tblModel.getValueAt(modelRow, 1).toString());      // Col 1: Acq No
        txtTitle.setText(tblModel.getValueAt(modelRow, 2).toString());    // Col 2: Title
        txtAuthor.setText(tblModel.getValueAt(modelRow, 3).toString());   // Col 3: Author

        // Date Handling
        txtDate.setValue(null); 
        txtDate.setText(tblModel.getValueAt(modelRow, 4).toString());     // Col 4: Date

        // NEW: Book Price Mapping (Col 5)
        //txtBookPrice.setText(tblModel.getValueAt(modelRow, 5).toString());    // Col 5: Book Price

        // ComboBox Mapping (Status moves to Col 6)
        cmbCategory.setSelectedItem(tblModel.getValueAt(modelRow, 5).toString()); // Col 6: Category
        txtBookPrice.setText(tblModel.getValueAt(modelRow, 6).toString());    // Col 5: Book Price
        cmbStatus.setSelectedItem(tblModel.getValueAt(modelRow, 7).toString());   // Col 7: Status

        // UI Component State Management
        unlockFields(); 

        addNewBtn.setEnabled(false);
        updateBtn.setEnabled(true);
        deleteBtn.setEnabled(true);
        saveBtn.setEnabled(false);

        btnAddCategory.setEnabled(true);
        btnDeleteCategory.setEnabled(true);
    }
    }//GEN-LAST:event_jTable2MouseClicked

    private void closeBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_closeBtnActionPerformed
        setDefault();
    
        txtSearch.setText(""); 
        cmbStatusBook.setSelectedIndex(0); 

        btnAddCategory.setEnabled(false);
        btnDeleteCategory.setEnabled(false);

        jTable2.clearSelection();
        search("");
    }//GEN-LAST:event_closeBtnActionPerformed

    private void cancelBtn1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelBtn1ActionPerformed
        this.dispose();
        borrow_management w = new borrow_management();
        w.setVisible(true);                        
    }//GEN-LAST:event_cancelBtn1ActionPerformed

    private void btnAddCategoryActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddCategoryActionPerformed
        String newCat = JOptionPane.showInputDialog(this, "Enter New Category Name:");

        if (newCat != null && !newCat.trim().isEmpty()) {
            String trimmedCat = newCat.trim();
            try {
                Connection conn = MySQLConnect.getConnection();

                String checkSql = "SELECT COUNT(*) FROM categories WHERE category_name = ?";
                PreparedStatement checkPst = conn.prepareStatement(checkSql);
                checkPst.setString(1, trimmedCat);
                ResultSet rs = checkPst.executeQuery();

                if (rs.next() && rs.getInt(1) > 0) {
                    JOptionPane.showMessageDialog(this, "The category '" + trimmedCat + "' already exists!", "Duplicate Entry", JOptionPane.WARNING_MESSAGE);
                } else {
                    String sql = "INSERT INTO categories (category_name) VALUES (?)";
                    PreparedStatement pst = conn.prepareStatement(sql);
                    pst.setString(1, trimmedCat);

                    pst.executeUpdate();
                    JOptionPane.showMessageDialog(this, "Category Added!");

                    fillAllCategoryComboBoxes(); 
                }

            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Database Error: " + e.getMessage());
            }
        }
    }//GEN-LAST:event_btnAddCategoryActionPerformed

    private void btnDeleteCategoryActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteCategoryActionPerformed
        Object selectedItem = cmbCategory.getSelectedItem();

        if (selectedItem != null && !selectedItem.toString().equals("~Select Category~")) {
            String categoryToDelete = selectedItem.toString();

            int confirm = JOptionPane.showConfirmDialog(this, 
                "Are you sure you want to delete the category: " + categoryToDelete + "?", 
                "Confirm Deletion", JOptionPane.YES_NO_OPTION);

            if (confirm == JOptionPane.YES_OPTION) {
                try {
                    Connection conn = MySQLConnect.getConnection();
                    String sql = "DELETE FROM categories WHERE category_name = ?";
                    PreparedStatement pst = conn.prepareStatement(sql);
                    pst.setString(1, categoryToDelete);

                    int result = pst.executeUpdate();

                    if (result > 0) {
                        JOptionPane.showMessageDialog(this, "Category Deleted Successfully!");

                        fillAllCategoryComboBoxes(); 
                    }

                } catch (SQLException e) {
                    JOptionPane.showMessageDialog(this, "Database Error: " + e.getMessage());
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select a valid category to delete.");
        }
    }//GEN-LAST:event_btnDeleteCategoryActionPerformed

    private void txtDateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtDateActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtDateActionPerformed

    private void btnAddCopyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddCopyActionPerformed
        String title = txtTitle.getText();
        String author = txtAuthor.getText();
        String date = txtDate.getText();
        Object category = cmbCategory.getSelectedItem();

        if (title.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please select a book first to add a copy!");
            return;
        }

        txtAcq.setText("");
        txtAcq.requestFocus(); 

        txtTitle.setText(title);
        txtAuthor.setText(author);
        txtDate.setText(date);
        cmbCategory.setSelectedItem(category);
        cmbStatus.setSelectedItem("Available"); 

        updateBtn.setEnabled(false);
        deleteBtn.setEnabled(false);
        saveBtn.setEnabled(true); 

        JOptionPane.showMessageDialog(this, "Copying details for '" + title + "'. \nPlease enter a NEW unique Acquisition Number.");
    }//GEN-LAST:event_btnAddCopyActionPerformed

    private void cmbStatusBookActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbStatusBookActionPerformed
        try {
        String selectedStatus = cmbStatusBook.getSelectedItem().toString();
        String sql;

        if (selectedStatus.equals("All")) {
            sql = "SELECT * FROM books";
        } else {
            sql = "SELECT * FROM books WHERE status = ?";
        }

        Connection conn = MySQLConnect.getConnection();
        PreparedStatement pst = conn.prepareStatement(sql);

        if (!selectedStatus.equals("All")) {
            pst.setString(1, selectedStatus);
        }

        ResultSet rs = pst.executeQuery();
        DefaultTableModel model = (DefaultTableModel) jTable2.getModel();
        model.setRowCount(0);

        while(rs.next()) {
            java.sql.Date dbDate = rs.getDate("date_published");
            String formattedDate = (dbDate != null) ? 
            new java.text.SimpleDateFormat("MM/dd/yyyy").format(dbDate) : "";
            model.addRow(new Object[]{
                rs.getString("id"),
                rs.getString("acquisition_no"),
                rs.getString("title"),
                rs.getString("author"),
                formattedDate,
                rs.getString("category"),
                rs.getString("status")
            });
        }
        updateBookCount(); 
        
    } catch (Exception e) {
        JOptionPane.showMessageDialog(null, e);
    }
        search(txtSearch.getText());
        filterTable();
    }//GEN-LAST:event_cmbStatusBookActionPerformed

    private void closeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_closeActionPerformed
        txtSearch.setText("");
        cmbCategoryTbl.setSelectedIndex(0); // Reset to "All"
        cmbStatusBook.setSelectedIndex(0);  // Reset to "All"
        filterTable();
    }//GEN-LAST:event_closeActionPerformed

    private void cmbCategoryTblActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbCategoryTblActionPerformed
        filterTable();
    }//GEN-LAST:event_cmbCategoryTblActionPerformed
    
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ReflectiveOperationException | javax.swing.UnsupportedLookAndFeelException ex) {
            logger.log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> new book_management().setVisible(true));
    }
   
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel STATUS;
    private javax.swing.JLabel STATUS1;
    private javax.swing.JButton addNewBtn;
    private javax.swing.JButton btnAddCategory;
    private javax.swing.JButton btnAddCopy;
    private javax.swing.JButton btnDeleteCategory;
    private javax.swing.JButton cancelBtn1;
    private javax.swing.JButton close;
    private javax.swing.JButton closeBtn;
    private javax.swing.JPanel closeBtnn;
    private javax.swing.JComboBox<String> cmbCategory;
    private javax.swing.JComboBox<String> cmbCategoryTbl;
    private javax.swing.JComboBox<String> cmbStatus;
    private javax.swing.JComboBox<String> cmbStatusBook;
    private javax.swing.JButton deleteBtn;
    private javax.swing.JButton jButton5;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable jTable2;
    private javax.swing.JButton saveBtn;
    private javax.swing.JTextField txtAcq;
    private javax.swing.JTextField txtAuthor;
    private javax.swing.JTextField txtBookPrice;
    private javax.swing.JFormattedTextField txtDate;
    private javax.swing.JTextField txtSearch;
    private javax.swing.JTextField txtTitle;
    private javax.swing.JTextField txtTotalCount;
    private javax.swing.JButton updateBtn;
    // End of variables declaration//GEN-END:variables
}