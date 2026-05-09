/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package library_hub;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;
import javax.swing.JOptionPane;
import javax.swing.RowFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

/**
 *
 * @author RACHELL
 */
public class bookmanagement_update extends javax.swing.JFrame {
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(bookmanagement_update.class.getName());

    private String originalAcqNo = ""; 
    private int id;
    private String check;
    
    public bookmanagement_update() {
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
    txtAuthor.setEnabled(true);
    txtBookPrice.setEnabled(true);
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
        txtBookPrice.setText("0.00");
        txtBookPrice.setEnabled(false);
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
    
   /*public void search(String str) {
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
}*/
   
   
   
   
   public void search(String str) {
        DefaultTableModel model = (DefaultTableModel) jTable2.getModel();
        model.setRowCount(0); 

        try {
            Connection conn = MySQLConnect.getConnection();

            String selectedStatus = cmbStatusBook.getSelectedItem().toString();
            String sql;

            if (selectedStatus.equals("All")) {
                sql = "SELECT * FROM books WHERE (acquisition_no LIKE ? OR title LIKE ? OR author LIKE ?)";
            } else {
                sql = "SELECT * FROM books WHERE (acquisition_no LIKE ? OR title LIKE ? OR author LIKE ?) AND status = ?";
            }

            PreparedStatement pst = conn.prepareStatement(sql);
            String searchData = "%" + str + "%";

            pst.setString(1, searchData);
            pst.setString(2, searchData);
            pst.setString(3, searchData);

            if (!selectedStatus.equals("All")) {
                pst.setString(4, selectedStatus);
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
            updateBookCount();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Search Error: " + e.getMessage());
        }
        int count = jTable2.getRowSorter() == null ? jTable2.getRowCount() : jTable2.getRowSorter().getViewRowCount();
        txtTotalCount.setText(String.valueOf(count));
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
               
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel5 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTable2 = new javax.swing.JTable();
        txtSearch = new javax.swing.JTextField();
        close = new javax.swing.JButton();
        cmbCategoryTbl = new javax.swing.JComboBox<>();
        cmbStatusBook = new javax.swing.JComboBox<>();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        txtTotalCount = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        txtAcq = new javax.swing.JTextField();
        txtTitle = new javax.swing.JTextField();
        btnAddCopy = new javax.swing.JButton();
        txtAuthor = new javax.swing.JTextField();
        txtDate = new javax.swing.JFormattedTextField();
        cmbCategory = new javax.swing.JComboBox<>();
        btnAddCategory = new javax.swing.JButton();
        btnDeleteCategory = new javax.swing.JButton();
        cmbStatus = new javax.swing.JComboBox<>();
        txtBookPrice = new javax.swing.JTextField();
        updateBtn = new javax.swing.JButton();
        addNewBtn = new javax.swing.JButton();
        deleteBtn = new javax.swing.JButton();
        saveBtn = new javax.swing.JButton();
        closeBtn = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jLabel11 = new javax.swing.JLabel();
        jButton10 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel5.setBackground(new java.awt.Color(67, 83, 189));

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

        txtSearch.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        txtSearch.addActionListener(this::txtSearchActionPerformed);
        txtSearch.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtSearchKeyReleased(evt);
            }
        });

        close.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        close.setText("x");
        close.addActionListener(this::closeActionPerformed);

        cmbCategoryTbl.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        cmbCategoryTbl.addActionListener(this::cmbCategoryTblActionPerformed);

        cmbStatusBook.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        cmbStatusBook.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Available", "Unavailable", "Damaged", "Lost" }));
        cmbStatusBook.addActionListener(this::cmbStatusBookActionPerformed);

        jLabel13.setFont(new java.awt.Font("Trebuchet MS", 1, 24)); // NOI18N
        jLabel13.setForeground(new java.awt.Color(255, 255, 255));
        jLabel13.setText("STATUS");

        jLabel14.setFont(new java.awt.Font("Trebuchet MS", 1, 24)); // NOI18N
        jLabel14.setForeground(new java.awt.Color(255, 255, 255));
        jLabel14.setText("SEARCH TITLE");

        jLabel15.setFont(new java.awt.Font("Trebuchet MS", 1, 24)); // NOI18N
        jLabel15.setForeground(new java.awt.Color(255, 255, 255));
        jLabel15.setText("CATEGORY");

        jPanel1.setBackground(new java.awt.Color(153, 204, 163));

        txtTotalCount.setEditable(false);
        txtTotalCount.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        txtTotalCount.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        txtTotalCount.addActionListener(this::txtTotalCountActionPerformed);

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel1.setText("TOTAL BOOK No.");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtTotalCount)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addGap(0, 44, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(txtTotalCount, javax.swing.GroupLayout.DEFAULT_SIZE, 59, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel5Layout.createSequentialGroup()
                        .addGap(959, 959, 959)
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel5Layout.createSequentialGroup()
                        .addComponent(jLabel14)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 202, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(close, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel15)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cmbCategoryTbl, javax.swing.GroupLayout.PREFERRED_SIZE, 241, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel13)
                        .addGap(18, 18, 18)
                        .addComponent(cmbStatusBook, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 1136, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(16, 16, 16))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(30, 30, 30)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(close, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel15)
                        .addComponent(cmbCategoryTbl)
                        .addComponent(cmbStatusBook, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel13))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGap(5, 5, 5)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel14)
                            .addComponent(txtSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 461, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(67, 67, 67))
        );

        jPanel4.setBackground(new java.awt.Color(67, 83, 189));

        jLabel2.setBackground(new java.awt.Color(0, 102, 102));
        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("ACQUISITION No");

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("BOOK TITLE");

        jLabel4.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("BOOK AUTHOR");

        jLabel5.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setText("DATE PUBLISHED  ");

        jLabel12.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(255, 255, 255));
        jLabel12.setText("(mm/dd/yyyy)");

        jLabel6.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setText("SELECT CATEGORY");

        jLabel10.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(255, 255, 255));
        jLabel10.setText("SELECT STATUS");

        jLabel9.setBackground(new java.awt.Color(0, 102, 102));
        jLabel9.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(255, 255, 255));
        jLabel9.setText("BOOK PRICE");

        txtAcq.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
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

        txtTitle.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        txtTitle.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txtTitleMouseClicked(evt);
            }
        });
        txtTitle.addActionListener(this::txtTitleActionPerformed);

        btnAddCopy.setBackground(new java.awt.Color(255, 102, 102));
        btnAddCopy.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnAddCopy.setText("+");
        btnAddCopy.addActionListener(this::btnAddCopyActionPerformed);

        txtAuthor.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        txtAuthor.addActionListener(this::txtAuthorActionPerformed);

        txtDate.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        txtDate.addActionListener(this::txtDateActionPerformed);

        cmbCategory.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        cmbCategory.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                cmbCategoryMouseClicked(evt);
            }
        });
        cmbCategory.addActionListener(this::cmbCategoryActionPerformed);

        btnAddCategory.setBackground(new java.awt.Color(255, 102, 102));
        btnAddCategory.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnAddCategory.setText("+");
        btnAddCategory.addActionListener(this::btnAddCategoryActionPerformed);

        btnDeleteCategory.setBackground(new java.awt.Color(51, 153, 255));
        btnDeleteCategory.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnDeleteCategory.setText("-");
        btnDeleteCategory.addActionListener(this::btnDeleteCategoryActionPerformed);

        cmbStatus.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        cmbStatus.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Available", "Unavailable" }));
        cmbStatus.addActionListener(this::cmbStatusActionPerformed);

        txtBookPrice.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N

        updateBtn.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        updateBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/library_hub/pencil.png"))); // NOI18N
        updateBtn.setText("UPDATE");
        updateBtn.setPreferredSize(new java.awt.Dimension(121, 47));
        updateBtn.addActionListener(this::updateBtnActionPerformed);

        addNewBtn.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        addNewBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/library_hub/add.png"))); // NOI18N
        addNewBtn.setText("ADD");
        addNewBtn.addActionListener(this::addNewBtnActionPerformed);

        deleteBtn.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        deleteBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/library_hub/bin.png"))); // NOI18N
        deleteBtn.setText("DELETE");
        deleteBtn.addActionListener(this::deleteBtnActionPerformed);

        saveBtn.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        saveBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/library_hub/download.png"))); // NOI18N
        saveBtn.setText("SAVE");
        saveBtn.addActionListener(this::saveBtnActionPerformed);

        closeBtn.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        closeBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/library_hub/cancel.png"))); // NOI18N
        closeBtn.setText("CANCEL");
        closeBtn.setPreferredSize(new java.awt.Dimension(120, 47));
        closeBtn.addActionListener(this::closeBtnActionPerformed);

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(21, 21, 21)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(deleteBtn, javax.swing.GroupLayout.DEFAULT_SIZE, 141, Short.MAX_VALUE)
                                    .addComponent(addNewBtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(saveBtn, javax.swing.GroupLayout.DEFAULT_SIZE, 139, Short.MAX_VALUE)
                                    .addComponent(updateBtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addComponent(cmbCategory, javax.swing.GroupLayout.PREFERRED_SIZE, 192, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnAddCategory, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(btnDeleteCategory, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(cmbStatus, javax.swing.GroupLayout.PREFERRED_SIZE, 272, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel9)
                            .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 179, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 179, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 179, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 179, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 179, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(txtAcq)
                                .addGroup(jPanel4Layout.createSequentialGroup()
                                    .addComponent(txtTitle, javax.swing.GroupLayout.PREFERRED_SIZE, 228, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(btnAddCopy, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addComponent(txtAuthor)
                                .addComponent(txtDate, javax.swing.GroupLayout.PREFERRED_SIZE, 280, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel5))
                            .addComponent(txtBookPrice, javax.swing.GroupLayout.PREFERRED_SIZE, 272, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel6)))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(95, 95, 95)
                        .addComponent(closeBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 138, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(45, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel2)
                .addGap(7, 7, 7)
                .addComponent(txtAcq, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtTitle, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnAddCopy, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtAuthor, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel12)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtDate, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cmbCategory, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnAddCategory, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnDeleteCategory, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel10)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cmbStatus, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel9)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtBookPrice, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(addNewBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(updateBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(deleteBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(saveBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(closeBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(24, Short.MAX_VALUE))
        );

        jPanel2.setBackground(new java.awt.Color(67, 83, 189));

        jLabel11.setFont(new java.awt.Font("Lucida Fax", 1, 36)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(255, 255, 255));
        jLabel11.setText("BOOK MANAGEMENT");

        jButton10.setIcon(new javax.swing.ImageIcon(getClass().getResource("/library_hub/cancel.png"))); // NOI18N
        jButton10.addActionListener(this::jButton10ActionPerformed);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(332, 332, 332)
                .addComponent(jLabel11)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButton10, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel11)
                    .addComponent(jButton10, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addGap(0, 5, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txtTitleMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtTitleMouseClicked

    }//GEN-LAST:event_txtTitleMouseClicked

    private void txtTitleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtTitleActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtTitleActionPerformed

    private void txtAcqMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtAcqMouseClicked

    }//GEN-LAST:event_txtAcqMouseClicked

    private void txtAcqActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtAcqActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtAcqActionPerformed

    private void txtAcqKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtAcqKeyReleased

    }//GEN-LAST:event_txtAcqKeyReleased

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
                lockFields();
                populateTable();
                setDefault();
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Database Error: " + e.getMessage());
        }
    }//GEN-LAST:event_saveBtnActionPerformed

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
                lockFields();
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

    private void addNewBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addNewBtnActionPerformed
        // Enable the text fields
        txtAcq.setEnabled(true);
        txtTitle.setEnabled(true);
        txtAuthor.setEnabled(true);
        txtDate.setEnabled(true);
        txtBookPrice.setEnabled(true);

        addNewBtn.setEnabled(false);

        cmbCategory.setEnabled(true);
        btnAddCategory.setEnabled(true);    // Unlock the '+'
        btnDeleteCategory.setEnabled(true); // Unlock the '-'

        cmbStatus.setEnabled(true);
        saveBtn.setEnabled(true);

        txtBookPrice.setText("");

        txtAcq.requestFocus();

    }//GEN-LAST:event_addNewBtnActionPerformed

    private void cmbCategoryMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_cmbCategoryMouseClicked

    }//GEN-LAST:event_cmbCategoryMouseClicked

    private void cmbCategoryActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbCategoryActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cmbCategoryActionPerformed

    private void btnDeleteCategoryActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteCategoryActionPerformed
        Object selectedItem = cmbCategory.getSelectedItem();

        if (selectedItem != null && !selectedItem.toString().equals("~Select Category~")) {
            String categoryToDelete = selectedItem.toString();

            int confirm = JOptionPane.showConfirmDialog(this,
                "Delete Category: " + categoryToDelete + "?",
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

    private void txtDateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtDateActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtDateActionPerformed

    private void closeBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_closeBtnActionPerformed
        // 1. CALL THE LOCK METHOD YOU JUST UPDATED
        lockFields();

        // 2. RESET THE UI STATE
        setDefault();
        txtSearch.setText("");
        txtBookPrice.setText("0.00"); // Reset price text
        cmbStatusBook.setSelectedIndex(0);

        // These are already in your lockFields(),
        // but keeping them here doesn't hurt.
        btnAddCategory.setEnabled(false);
        btnDeleteCategory.setEnabled(false);

        jTable2.clearSelection();
        search("");

        //pariha sa close btn
        txtSearch.setText("");
        cmbCategoryTbl.setSelectedIndex(0); // Reset to "All"
        cmbStatusBook.setSelectedIndex(0);  // Reset to "All"
        filterTable();
    }//GEN-LAST:event_closeBtnActionPerformed

    private void txtAuthorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtAuthorActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtAuthorActionPerformed

    private void cmbStatusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbStatusActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cmbStatusActionPerformed

    private void txtTotalCountActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtTotalCountActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtTotalCountActionPerformed

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

    private void cmbCategoryTblActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbCategoryTblActionPerformed
        filterTable();
    }//GEN-LAST:event_cmbCategoryTblActionPerformed

    private void closeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_closeActionPerformed
        txtSearch.setText("");
        cmbCategoryTbl.setSelectedIndex(0); // Reset to "All"
        cmbStatusBook.setSelectedIndex(0);  // Reset to "All"
        filterTable();
    }//GEN-LAST:event_closeActionPerformed

    private void txtSearchKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSearchKeyReleased
        search(txtSearch.getText());
        filterTable();
    }//GEN-LAST:event_txtSearchKeyReleased

    private void txtSearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtSearchActionPerformed

    }//GEN-LAST:event_txtSearchActionPerformed

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

    private void jButton10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton10ActionPerformed
        this.dispose();
        dashboard w = new dashboard();
        w.setVisible(true);
    }//GEN-LAST:event_jButton10ActionPerformed

    /**
     * @param args the command line arguments
     */
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
        java.awt.EventQueue.invokeLater(() -> new bookmanagement_update().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addNewBtn;
    private javax.swing.JButton btnAddCategory;
    private javax.swing.JButton btnAddCopy;
    private javax.swing.JButton btnDeleteCategory;
    private javax.swing.JButton close;
    private javax.swing.JButton closeBtn;
    private javax.swing.JComboBox<String> cmbCategory;
    private javax.swing.JComboBox<String> cmbCategoryTbl;
    private javax.swing.JComboBox<String> cmbStatus;
    private javax.swing.JComboBox<String> cmbStatusBook;
    private javax.swing.JButton deleteBtn;
    private javax.swing.JButton jButton10;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
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
