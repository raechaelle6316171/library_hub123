package library_hub;

import java.sql.*;
import java.util.Vector;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.RowFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

public class book_management_copy extends javax.swing.JFrame {
    
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(book_management_copy.class.getName());
    
    private int id;
    private String check;
    public book_management_copy() {
    initComponents();

    // 1. Sync and Fill all dropdowns first
    fillAllCategoryComboBoxes();

    // 2. Initial Data Load
    populateTable();

    // 3. Set the default filter state
    // We set status to "Available" so only available books show on startup
    cmbStatusBook.setSelectedItem("Available");
    
    // Updated to match your new category label
    cmbCategoryTbl.setSelectedItem("~Select Category~");

    // 4. Force the filter to run immediately
    // This applies the "Available" filter right when the window opens
    filterTable();

    // 5. Initial UI State
    lockFields();
    
    addNewBtn.setEnabled(true);
    closeBtn.setEnabled(true);
    updateBtn.setEnabled(false);
    deleteBtn.setEnabled(false);
    saveBtn.setEnabled(false);

    // 6. Set Date Mask
    try {
        javax.swing.text.MaskFormatter dateMask = new javax.swing.text.MaskFormatter("##/##/####");
        dateMask.setPlaceholderCharacter('_');
        dateMask.install(txtDate);
    } catch (java.text.ParseException e) {
        System.out.println("Mask Error: " + e.getMessage());
    }
}
    
    private boolean isValidDate(String dateStr) {
    // Check if the mask is completely filled
    //dateStr = dateStr.trim();
    if (dateStr.contains("_") || dateStr.length() < 10) return false;

    try {
        // STRICT mode prevents impossible dates like Feb 30
        java.time.format.DateTimeFormatter dtf = 
        java.time.format.DateTimeFormatter.ofPattern("MM/dd/uuuu")
        .withResolverStyle(java.time.format.ResolverStyle.STRICT);
            
        java.time.LocalDate inputDate = java.time.LocalDate.parse(dateStr, dtf);
        java.time.LocalDate today = java.time.LocalDate.now(); // This is April 25, 2026

        // RULE: The date must be today or in the past
        // If the date is after today (e.g., May 2026), it returns false
        if (inputDate.isAfter(today)) {
            return false;
        }

        // Sets a reasonable historical limit (e.g., year 1450)
        return inputDate.getYear() >= 1450;
        
    } catch (java.time.format.DateTimeParseException e) {
        return false; // Blocks invalid calendar dates
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

        // Must clear BOTH to keep them in sync
        cmbCategory.removeAllItems();    // The dropdown on the left side
        cmbCategoryTbl.removeAllItems(); // The filter dropdown above the table

        // Setting both to the same default text
        cmbCategory.addItem("~Select Category~");
        cmbCategoryTbl.addItem("~Select Category~"); 

        while (rs.next()) {
            String catName = rs.getString("category_name");
            cmbCategory.addItem(catName);
            cmbCategoryTbl.addItem(catName); // Adds it to the filter dropdown
        }
    } catch (SQLException e) {
        System.out.println("Error: " + e.getMessage());
    }
}
    
    private void filterTable() {
    // 1. GUARD: Prevent NullPointerException during initialization
    if (cmbCategoryTbl.getSelectedItem() == null || cmbStatusBook.getSelectedItem() == null) {
        return; 
    }

    // 2. Get Selections
    String selectedCategory = cmbCategoryTbl.getSelectedItem().toString();
    String selectedStatus = cmbStatusBook.getSelectedItem().toString();
    String searchText = txtSearch.getText().toLowerCase().trim();

    // 3. Setup Sorter
    DefaultTableModel model = (DefaultTableModel) jTable2.getModel();
    TableRowSorter<DefaultTableModel> trs = new TableRowSorter<>(model);
    jTable2.setRowSorter(trs);

    java.util.List<RowFilter<Object, Object>> filters = new java.util.ArrayList<>();

    // A. Category Filter (Column index 5)
    if (!selectedCategory.equals("~Select Category~")) {
        filters.add(RowFilter.regexFilter("(?i)" + selectedCategory, 5));
    }

    // B. Status Filter (Column index 6)
    // This handles your requirement to toggle between Available and Unavailable
    if (!selectedStatus.equals("All")) {
        filters.add(RowFilter.regexFilter("(?i)^" + selectedStatus + "$", 6));
    }

    // C. Search Text Filter
    if (!searchText.isEmpty()) {
        filters.add(RowFilter.regexFilter("(?i)" + searchText));
    }

    // 4. Apply combined filters
    if (filters.isEmpty()) {
        trs.setRowFilter(null);
    } else {
        trs.setRowFilter(RowFilter.andFilter(filters));
    }
    
    updateBookCount();
}

    public void fillCategoryCombo() {
    // 1. Clear the old "Item 1, Item 2" or empty space
    cmbCategory.removeAllItems();
    
    // 2. Add a default hint
    cmbCategory.addItem("~Select Category~");
    
    try {
        Connection conn = MySQLConnect.getConnection();
        String sql = "SELECT category_name FROM categories ORDER BY category_name ASC";
        PreparedStatement pst = conn.prepareStatement(sql);
        ResultSet rs = pst.executeQuery();
        
        while(rs.next()) {
            // 3. Add each name from your database into the box
            cmbCategory.addItem(rs.getString("category_name"));
        }
    } catch (SQLException e) {
        System.out.println("Error: " + e.getMessage());
    }
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
        
        // 1. Get the current status from the ComboBox
        String selectedStatus = cmbStatusBook.getSelectedItem().toString();
        String sql;

        // 2. Build the SQL based on whether "All" is selected or a specific status
        if (selectedStatus.equals("All")) {
            sql = "SELECT * FROM books WHERE (acquisition_no LIKE ? OR title LIKE ? OR author LIKE ?)";
        } else {
            // This ensures it matches the Search text AND the Status
            sql = "SELECT * FROM books WHERE (acquisition_no LIKE ? OR title LIKE ? OR author LIKE ?) AND status = ?";
        }

        PreparedStatement pst = conn.prepareStatement(sql);
        String searchData = "%" + str + "%";
        
        pst.setString(1, searchData);
        pst.setString(2, searchData);
        pst.setString(3, searchData);

        // 3. Only set the 4th parameter if we are filtering by a specific status
        if (!selectedStatus.equals("All")) {
            pst.setString(4, selectedStatus);
        }

        ResultSet rs = pst.executeQuery();

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

    } catch (SQLException e) {
        JOptionPane.showMessageDialog(null, "Search Error: " + e.getMessage());
    }
    int count = jTable2.getRowSorter() == null ? jTable2.getRowCount() : jTable2.getRowSorter().getViewRowCount();
    txtTotalCount.setText(String.valueOf(count));
}
    public void populateTable() {
        try {
            Connection conn = MySQLConnect.getConnection();
            String query = "SELECT * FROM books"; // Removed hardcoded 'Available' to allow filtering
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(query);

            DefaultTableModel tblModel = (DefaultTableModel) jTable2.getModel();
            tblModel.setRowCount(0);
            
            

            while (rs.next()) {
                Vector colData = new Vector();
                colData.add(rs.getInt("id"));
                colData.add(rs.getString("acquisition_no"));
                colData.add(rs.getString("title"));
                colData.add(rs.getString("author"));
                java.sql.Date dbDate = rs.getDate("date_published"); 
                String formattedDate = "";

                if (dbDate != null) {
                    // This matches your ##/##/#### mask exactly!
                    formattedDate = new java.text.SimpleDateFormat("MM/dd/yyyy").format(dbDate);
                }
                colData.add(formattedDate);
                colData.add(rs.getString("category"));
                colData.add(rs.getString("status"));
                tblModel.addRow(colData);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);
        }
        
        // Hide ID Column
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
        closeBtnn = new javax.swing.JPanel();
        txtSearch = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        jPanel6 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        txtTotalCount = new javax.swing.JTextField();
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
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
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

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
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
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtDate, javax.swing.GroupLayout.PREFERRED_SIZE, 210, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cmbStatus, javax.swing.GroupLayout.PREFERRED_SIZE, 209, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 179, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 179, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtAuthor, javax.swing.GroupLayout.PREFERRED_SIZE, 210, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtAcq, javax.swing.GroupLayout.PREFERRED_SIZE, 209, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE))))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addContainerGap(37, Short.MAX_VALUE)
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
                .addGap(36, 36, 36))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(22, 22, 22)
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
                .addGap(18, 18, 18)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(addNewBtn)
                    .addComponent(updateBtn))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(deleteBtn)
                    .addComponent(saveBtn))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(closeBtn)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
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

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel1.setText("TOTAL BOOK No");

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

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(txtTotalCount, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addGap(0, 0, Short.MAX_VALUE))))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtTotalCount, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );

        jTable2.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null}
            },
            new String [] {
                "Id", "Acquisition No", "Book Title", "Book Author", "Date Published", "Category", "Status"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false
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
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, closeBtnnLayout.createSequentialGroup()
                .addContainerGap(21, Short.MAX_VALUE)
                .addComponent(jLabel7)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(closeBtnnLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(closeBtnnLayout.createSequentialGroup()
                        .addComponent(jLabel13)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(close)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(STATUS1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cmbCategoryTbl, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(STATUS)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(cmbStatusBook, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(closeBtnnLayout.createSequentialGroup()
                        .addGroup(closeBtnnLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(cancelBtn1, javax.swing.GroupLayout.PREFERRED_SIZE, 195, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 793, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(3, 3, 3)))
                .addGap(31, 31, 31))
        );
        closeBtnnLayout.setVerticalGroup(
            closeBtnnLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(closeBtnnLayout.createSequentialGroup()
                .addContainerGap(19, Short.MAX_VALUE)
                .addComponent(jLabel7)
                .addGap(455, 455, 455))
            .addGroup(closeBtnnLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(closeBtnnLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(closeBtnnLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(txtSearch, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel13)
                        .addComponent(STATUS)
                        .addComponent(cmbStatusBook, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(close)
                        .addComponent(STATUS1)
                        .addComponent(cmbCategoryTbl, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(47, 47, 47)
                .addComponent(cancelBtn1, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(closeBtnn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, 864, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(closeBtnn, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
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

        // Enable the category selection and management buttons
        cmbCategory.setEnabled(true);
        btnAddCategory.setEnabled(true);    // Unlock the '+'
        btnDeleteCategory.setEnabled(true); // Unlock the '-'

        // Enable status and save
        cmbStatus.setEnabled(true);
        saveBtn.setEnabled(true);

        // Set focus to the first field
        txtAcq.requestFocus();
    }//GEN-LAST:event_addNewBtnActionPerformed

    private void updateBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_updateBtnActionPerformed
        // 1. Collect and Trim Data
    String acqNo = txtAcq.getText().trim();
    String title = txtTitle.getText().trim();
    String author = txtAuthor.getText().trim();
    String dateInput = txtDate.getText(); 
    
    // Null safety for ComboBox selections
    Object selectedCatObj = cmbCategory.getSelectedItem();
    String category = (selectedCatObj != null) ? selectedCatObj.toString() : "";
    
    Object selectedStatusObj = cmbStatus.getSelectedItem();

    // 2. Validation Checks
    if (acqNo.isEmpty()) {
        JOptionPane.showMessageDialog(this, "Select a book from the table first!");
        return;
    }
    
    if (title.isEmpty()) {
        JOptionPane.showMessageDialog(this, "Book Title is required!");
        txtTitle.requestFocus();
        return;
    }

    if (!isValidDate(dateInput)) {
    JOptionPane.showMessageDialog(this, 
        "Invalid Date Published!\n" +
        "- Date must exist (No Feb 30).\n" +
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

    // 3. Handle Anonymous Author
    if (author.isEmpty()) {
        author = "Anonymous";
    }

    // 4. Database Operations
    try {
        Connection conn = MySQLConnect.getConnection();
        
        java.text.SimpleDateFormat inputFormat = new java.text.SimpleDateFormat("MM/dd/yyyy");
        java.util.Date parsedDate = inputFormat.parse(dateInput);
        java.sql.Date sqlDatePublished = new java.sql.Date(parsedDate.getTime());
        
        // UPDATE DATA based on acquisition_no
        String sql = "UPDATE books SET title = ?, author = ?, date_published = ?, category = ?, status = ? WHERE acquisition_no = ?";
        PreparedStatement pst = conn.prepareStatement(sql);
        

        pst.setString(1, title);
        pst.setString(2, author);
        pst.setDate(3, sqlDatePublished);
        pst.setString(4, category);
        pst.setObject(5, selectedStatusObj);
        pst.setString(6, acqNo); // Matches the WHERE clause

        int updatedRows = pst.executeUpdate();
        
        if (updatedRows > 0) {
            JOptionPane.showMessageDialog(this, "Book Updated Successfully!");
            
            // 5. Refresh UI, Filters, and Reset
            fillAllCategoryComboBoxes(); 
            populateTable(); 
            setDefault(); 
        } else {
            JOptionPane.showMessageDialog(this, "Error: Book with Acquisition No " + acqNo + " not found.");
        }
    } catch (java.text.ParseException e) {
        // This catch block was missing!
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
        // 1. Collect and Trim Data
    String acqNo = txtAcq.getText().trim();
    String title = txtTitle.getText().trim();
    String author = txtAuthor.getText().trim();
    String dateInput = txtDate.getText().trim(); // Contains slashes from mask
    
    // Null safety for ComboBox selections
    Object selectedCatObj = cmbCategory.getSelectedItem();
    String category = (selectedCatObj != null) ? selectedCatObj.toString() : "";
    Object selectedStatusObj = cmbStatus.getSelectedItem();

    // 2. Validation Checks
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
        java.time.LocalDate date = java.time.LocalDate.parse(dateInput.trim(), inputFormatter);
        mysqlDate = date.toString(); // Result: "2026-04-25"
    } catch (Exception e) {
        mysqlDate = "0000-00-00"; // Fallback
    }

    if (category.equals("~Select Category~") || category.isEmpty()) {
        JOptionPane.showMessageDialog(this, "Please select a valid Book Category!");
        cmbCategory.requestFocus();
        return;
    }

    // 3. Handle Anonymous Author
    if (author.isEmpty()) {
        author = "Anonymous";
    }

    // 4. Database Operations
    try {
        Connection conn = MySQLConnect.getConnection();

        // DUPLICATE CHECK
        String checkQuery = "SELECT * FROM books WHERE acquisition_no = ?";
        PreparedStatement checkPst = conn.prepareStatement(checkQuery);
        checkPst.setString(1, acqNo);
        ResultSet rs = checkPst.executeQuery();

        if (rs.next()) {
            JOptionPane.showMessageDialog(this, "Error: Acquisition No " + acqNo + " already exists!", "Duplicate Entry", JOptionPane.ERROR_MESSAGE);
            txtAcq.requestFocus();
            return; 
        }

        // INSERT DATA
        String sql = "INSERT INTO books (acquisition_no, title, author, date_published, category, status) VALUES (?,?,?,?,?,?)";
        PreparedStatement pst = conn.prepareStatement(sql);

        pst.setString(1, acqNo);
        pst.setString(2, title);
        pst.setString(3, author);
        pst.setString(4, mysqlDate); 
        pst.setString(5, category);
        pst.setObject(6, selectedStatusObj);

        pst.executeUpdate();
        JOptionPane.showMessageDialog(this, "Book Added Successfully!");

        // 5. Refresh UI and Reset
        fillAllCategoryComboBoxes(); 
        populateTable(); 
        setDefault(); 

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
        // 1. Get the row index from the VIEW (the UI)
    int viewRow = jTable2.getSelectedRow();
    
    if (viewRow != -1) {
        // 2. IMPORTANT: Convert the view index to the MODEL index for filtered tables
        int modelRow = jTable2.convertRowIndexToModel(viewRow);
        DefaultTableModel tblModel = (DefaultTableModel) jTable2.getModel();
        
        //System.out.println("--- DEBUG START ---");
        //for(int i = 0; i < tblModel.getColumnCount(); i++) {
        //    System.out.println("Column " + i + ": " + tblModel.getValueAt(modelRow, i));
        //}
        //System.out.println("--- DEBUG END ---");
        
        

        // 1. Enable Fields
        txtAcq.setEnabled(true);
        txtTitle.setEnabled(true);
        txtAuthor.setEnabled(true);
        txtDate.setEnabled(true);
        cmbCategory.setEnabled(true);
        cmbStatus.setEnabled(true);

        // Mapping based on your Debug Console output:
        id = Integer.parseInt(tblModel.getValueAt(modelRow, 0).toString());
        txtAcq.setText(tblModel.getValueAt(modelRow, 1).toString());
        txtTitle.setText(tblModel.getValueAt(modelRow, 2).toString());
        txtAuthor.setText(tblModel.getValueAt(modelRow, 3).toString());
        
        id = Integer.parseInt(tblModel.getValueAt(modelRow, 0).toString());
        txtAcq.setText(String.valueOf(tblModel.getValueAt(modelRow, 1)));
        txtTitle.setText(String.valueOf(tblModel.getValueAt(modelRow, 2)));
        txtAuthor.setText(String.valueOf(tblModel.getValueAt(modelRow, 3)));
        
        // Date handling - The Mask is now happy because we fixed the source data
        txtDate.setValue(null); // Clear the formatter internal state
        txtDate.setText(String.valueOf(tblModel.getValueAt(modelRow, 4)));
        cmbCategory.setSelectedItem(tblModel.getValueAt(modelRow, 5).toString()); // Col 5: Poet
        cmbStatus.setSelectedItem(tblModel.getValueAt(modelRow, 6).toString());
        // 5. Button Logic
        addNewBtn.setEnabled(false);
        updateBtn.setEnabled(true);
        deleteBtn.setEnabled(true);
        saveBtn.setEnabled(false);
        
        btnAddCategory.setEnabled(true);
        btnDeleteCategory.setEnabled(true);
    }
    }//GEN-LAST:event_jTable2MouseClicked

    private void closeBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_closeBtnActionPerformed
        // 1. Run your existing default settings
    setDefault();
    
    // 2. Clear the search field and reset status to Available
    txtSearch.setText(""); 
    cmbStatusBook.setSelectedIndex(0); // Sets selection to "Available"
    
    // 3. Disable category buttons as per your current logic
    btnAddCategory.setEnabled(false);
    btnDeleteCategory.setEnabled(false);

    // 4. Clear table selection and refresh data based on the empty search
    jTable2.clearSelection();
    search("");
    }//GEN-LAST:event_closeBtnActionPerformed

    private void cancelBtn1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelBtn1ActionPerformed
        this.dispose();
        borrow_management_123 w = new borrow_management_123();
        w.setVisible(true);                        
    }//GEN-LAST:event_cancelBtn1ActionPerformed

    private void btnAddCategoryActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddCategoryActionPerformed
        String newCat = JOptionPane.showInputDialog(this, "Enter New Category Name:");

        if (newCat != null && !newCat.trim().isEmpty()) {
            try {
                Connection conn = MySQLConnect.getConnection();
                String sql = "INSERT INTO categories (category_name) VALUES (?)";
                PreparedStatement pst = conn.prepareStatement(sql);
                pst.setString(1, newCat.trim());

                pst.executeUpdate();
                JOptionPane.showMessageDialog(this, "Category Added!");

                // Use the method that clears and reloads BOTH dropdowns
                fillAllCategoryComboBoxes(); 

            } catch (SQLException e) {
                // If the category name is a unique key in your DB, this catches duplicates
                JOptionPane.showMessageDialog(this, "Category already exists or Database Error: " + e.getMessage());
            }
        }
    }//GEN-LAST:event_btnAddCategoryActionPerformed

    private void btnDeleteCategoryActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteCategoryActionPerformed
        // 1. Get the currently selected category from the dropdown
Object selectedItem = cmbCategory.getSelectedItem();

if (selectedItem != null && !selectedItem.toString().equals("~Select Category~")) {
    String categoryToDelete = selectedItem.toString();
    
    // 2. Confirmation Dialog (Professional UI practice)
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
                
                // 3. REFRESH: This updates both cmbCategory and cmbCategoryTbl
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
        // 1. Get the current details of the book you want to copy
        String title = txtTitle.getText();
        String author = txtAuthor.getText();
        String date = txtDate.getText();
        Object category = cmbCategory.getSelectedItem();

        if (title.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please select a book first to add a copy!");
            return;
        }

        // 2. Clear the Acquisition No so a new, unique one can be entered
        txtAcq.setText("");
        txtAcq.requestFocus(); // Put cursor here so user can type the new Acq No

        // 3. Keep the other details the same
        txtTitle.setText(title);
        txtAuthor.setText(author);
        txtDate.setText(date);
        cmbCategory.setSelectedItem(category);
        cmbStatus.setSelectedItem("Available"); // New copies should start as Available

        // 4. Disable Update/Delete and Enable "SAVE" or "ADD NEW" 
        // This prevents you from accidentally overwriting the original book
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
            // This handles 'Available' or 'Unavailable' dynamically
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
        updateBookCount(); // Update the orange counter box
        
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
        java.awt.EventQueue.invokeLater(() -> new book_management_copy().setVisible(true));
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
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable jTable2;
    private javax.swing.JButton saveBtn;
    private javax.swing.JTextField txtAcq;
    private javax.swing.JTextField txtAuthor;
    private javax.swing.JFormattedTextField txtDate;
    private javax.swing.JTextField txtSearch;
    private javax.swing.JTextField txtTitle;
    private javax.swing.JTextField txtTotalCount;
    private javax.swing.JButton updateBtn;
    // End of variables declaration//GEN-END:variables
}