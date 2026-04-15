package library_hub;

import javax.swing.table.DefaultTableModel;
import java.sql.*;
import java.util.Vector;
import javax.swing.JOptionPane;


public class user_management extends javax.swing.JFrame {
    
    int userId = -1;
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(user_management.class.getName());
    
    private int id;
    private String check;
    public user_management(){
        initComponents();
        MySQLConnect.getConnection(); // Make sure your DB connects
        populateTable();              // Load your data
        setDefault();
}   
    
    private boolean isUsernameDuplicate(String username) {
    boolean exists = false;
    try {
        Connection conn = MySQLConnect.getConnection();
        // Check if username exists, but exclude the current user ID (for updates)
        String sql = "SELECT * FROM user WHERE userName = ? AND id != ?";
        PreparedStatement pst = conn.prepareStatement(sql);
        pst.setString(1, username);
        pst.setInt(2, userId); // userId is captured when you click a row
        
        ResultSet rs = pst.executeQuery();
        if (rs.next()) {
            exists = true;
        }
    } catch (SQLException e) {
        JOptionPane.showMessageDialog(this, "Validation Error: " + e.getMessage());
    }
    return exists;
}
    
    private void clearFields() {
    txtFullName.setText("");
    txtUsername.setText("");
    txtPassword.setText("");
    txtConfirmPassword.setText("");
    cmbUserType.setSelectedIndex(0);
    
    // Reset buttons to default state
    addNewBtn.setEnabled(true);
    saveBtn.setEnabled(false);
    updateBtn.setEnabled(false);
    deleteBtn.setEnabled(false);
    
    // Lock fields again
    txtFullName.setEnabled(false);
    txtUsername.setEnabled(false);
    txtPassword.setEnabled(false);
    txtConfirmPassword.setEnabled(false);
    cmbUserType.setEnabled(false);
    
    // Reset the ID tracker
    userId = -1;
}
    
    public void populateTable(){
        int colCount;
        try{
            Connection conn = MySQLConnect.getConnection();
            Statement st = conn.createStatement();
            String query = "SELECT * FROM user";
            ResultSet rs = st.executeQuery(query);
            ResultSetMetaData rsData = rs.getMetaData();
            colCount = rsData.getColumnCount();
            
            DefaultTableModel tblModel = (DefaultTableModel) jTable2.getModel();
            tblModel.setRowCount(0);
            while(rs.next()){
                Vector colData = new Vector();
                for(int i = 1; i < colCount; i++){
                    colData.add(rs.getInt("id"));
                    colData.add(rs.getString("fullName"));
                    colData.add(rs.getString("userName"));
                    colData.add(rs.getString("password"));
                    colData.add(rs.getString("category"));
                }
                tblModel.addRow(colData);
            }
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null, e);
            
        }
        //updateBookCount();
    }
    
     public void makeEnable(){
    
        txtFullName.setEnabled(true);
        txtUsername.setEnabled(true);
        txtPassword.setEnabled(true);
        txtConfirmPassword.setEnabled(true);
        cmbUserType.setEnabled(true);
        }
     
    public void setDefault() {
    // 1. Clear everything
    txtFullName.setText("");
    txtUsername.setText("");
    txtPassword.setText("");
    txtConfirmPassword.setText("");
    cmbUserType.setSelectedIndex(0);

    // 2. Disable all input fields
    txtFullName.setEnabled(false);
    txtUsername.setEnabled(false);
    txtPassword.setEnabled(false);
    txtConfirmPassword.setEnabled(false);
    cmbUserType.setEnabled(false);
    
    // 3. Button States for Startup/Cancel
    addNewBtn.setEnabled(true);   // Only Add New is clickable
    updateBtn.setEnabled(false);  // Update is greyed out
    deleteBtn.setEnabled(false);  // Delete is greyed out
    saveBtn.setEnabled(false);    // Save is greyed out
    
    id = -1; // Reset ID
}
    
    private void btnAddNewActionPerformed(java.awt.event.ActionEvent evt) {                                          
    // Enable fields for typing
    txtFullName.setEnabled(true);
    txtUsername.setEnabled(true);
    txtPassword.setEnabled(true);
    txtConfirmPassword.setEnabled(true);
    cmbUserType.setEnabled(true);
    
    // UI logic
    saveBtn.setEnabled(true);
    addNewBtn.setEnabled(false);
    txtFullName.requestFocus();
}
    
    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {
    String pass = txtPassword.getText();
    String confirmPass = txtConfirmPassword.getText();

    // Check if passwords match
    if (!pass.equals(confirmPass)) {
        JOptionPane.showMessageDialog(this, 
            "Passwords do not match!", 
            "Security Warning", 
            JOptionPane.WARNING_MESSAGE);
        return; // Stops the save from happening
    }
    
    // If they match, continue with your INSERT query...
}
    
    private void jTable1MouseClicked(java.awt.event.MouseEvent evt) {                                     
    int row = jTable2.getSelectedRow();
    
    if (row != -1) {
        // Grab the ID from Column 0 and save it to the global userId variable
        userId = Integer.parseInt(jTable2.getValueAt(row, 0).toString());
        
        // Fill the text fields (Indices shift up by 1 because ID is at 0)
        txtFullName.setText(jTable2.getValueAt(row, 1).toString());
        txtUsername.setText(jTable2.getValueAt(row, 2).toString());
        txtPassword.setText(jTable2.getValueAt(row, 3).toString());
        txtConfirmPassword.setText(jTable2.getValueAt(row, 3).toString());
        cmbUserType.setSelectedItem(jTable2.getValueAt(row, 4).toString());

        // Unlock fields and buttons
        txtFullName.setEnabled(true);
        txtUsername.setEnabled(true);
        txtPassword.setEnabled(true);
        txtConfirmPassword.setEnabled(true);
        cmbUserType.setEnabled(true);
        
        addNewBtn.setEnabled(false);
        updateBtn.setEnabled(true);
        deleteBtn.setEnabled(true);
        saveBtn.setEnabled(false);
    }
}
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel4 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        txtFullName = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        txtUsername = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        txtPassword = new javax.swing.JPasswordField();
        txtConfirmPassword = new javax.swing.JPasswordField();
        cmbUserType = new javax.swing.JComboBox<>();
        jLabel9 = new javax.swing.JLabel();
        jButton6 = new javax.swing.JButton();
        closeBtn = new javax.swing.JButton();
        saveBtn = new javax.swing.JButton();
        deleteBtn = new javax.swing.JButton();
        updateBtn = new javax.swing.JButton();
        addNewBtn = new javax.swing.JButton();
        txtSearchUsername = new javax.swing.JPanel();
        txtSearch = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTable2 = new javax.swing.JTable();
        jPanel3 = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        jButton5 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel4.setBackground(new java.awt.Color(0, 0, 102));
        jPanel4.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 5));

        jLabel1.setFont(new java.awt.Font("Century Gothic", 1, 24)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("CREATE ACCOUNT");

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("FULL NAME");

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("USERNAME");

        jLabel4.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("PASSWORD");

        jLabel5.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setText("CONFIRM PASSWORD");

        jLabel6.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setText("USER TYPE");

        cmbUserType.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Admin", "Librarian" }));
        cmbUserType.addActionListener(this::cmbUserTypeActionPerformed);

        jLabel9.setForeground(new java.awt.Color(255, 255, 255));
        jLabel9.setText("Already have an account?");

        jButton6.setText("LOGIN");
        jButton6.addActionListener(this::jButton6ActionPerformed);

        closeBtn.setText("CANCEL");
        closeBtn.addActionListener(this::closeBtnActionPerformed);

        saveBtn.setText("SAVE");
        saveBtn.addActionListener(this::saveBtnActionPerformed);

        deleteBtn.setText("DELETE");
        deleteBtn.addActionListener(this::deleteBtnActionPerformed);

        updateBtn.setText("UPDATE");
        updateBtn.addActionListener(this::updateBtnActionPerformed);

        addNewBtn.setText("ADD NEW");
        addNewBtn.addActionListener(this::addNewBtnActionPerformed);

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(35, 35, 35)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(deleteBtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(addNewBtn))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(updateBtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(saveBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                                .addComponent(closeBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(42, 42, 42))))
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 179, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtFullName, javax.swing.GroupLayout.PREFERRED_SIZE, 197, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 179, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 179, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(txtUsername)
                        .addComponent(txtPassword)
                        .addComponent(txtConfirmPassword)
                        .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 179, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 179, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(cmbUserType, javax.swing.GroupLayout.PREFERRED_SIZE, 197, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel1))
                .addContainerGap(26, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                        .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 144, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(58, 58, 58))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                        .addComponent(jButton6, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(80, 80, 80))))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addComponent(jLabel1)
                .addGap(18, 18, 18)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtFullName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtUsername, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtPassword, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtConfirmPassword, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cmbUserType, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(12, 12, 12)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(addNewBtn)
                    .addComponent(updateBtn))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(deleteBtn)
                    .addComponent(saveBtn))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(closeBtn)
                .addGap(18, 18, 18)
                .addComponent(jLabel9)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton6)
                .addContainerGap(12, Short.MAX_VALUE))
        );

        txtSearchUsername.setBackground(new java.awt.Color(0, 0, 102));
        txtSearchUsername.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 5));

        txtSearch.addActionListener(this::txtSearchActionPerformed);
        txtSearch.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtSearchKeyReleased(evt);
            }
        });

        jLabel7.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(255, 255, 255));
        jLabel7.setText("Search Username");

        jTable2.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "ID", "FULL NAME", "USERNAME", "PASSWORD", "USER TYPE"
            }
        ));
        jTable2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable2MouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(jTable2);

        javax.swing.GroupLayout txtSearchUsernameLayout = new javax.swing.GroupLayout(txtSearchUsername);
        txtSearchUsername.setLayout(txtSearchUsernameLayout);
        txtSearchUsernameLayout.setHorizontalGroup(
            txtSearchUsernameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(txtSearchUsernameLayout.createSequentialGroup()
                .addGap(26, 26, 26)
                .addGroup(txtSearchUsernameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(txtSearchUsernameLayout.createSequentialGroup()
                        .addComponent(jLabel7)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 146, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(18, Short.MAX_VALUE))
        );
        txtSearchUsernameLayout.setVerticalGroup(
            txtSearchUsernameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(txtSearchUsernameLayout.createSequentialGroup()
                .addGap(31, 31, 31)
                .addGroup(txtSearchUsernameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtSearch, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel7))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 412, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel3.setBackground(new java.awt.Color(0, 0, 102));
        jPanel3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 5));
        jPanel3.setPreferredSize(new java.awt.Dimension(285, 53));

        jLabel8.setFont(new java.awt.Font("Century Gothic", 1, 24)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(255, 255, 255));
        jLabel8.setText("USER MANAGEMENT");

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
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(jButton5))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(txtSearchUsername, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, 510, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtSearchUsername, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void cmbUserTypeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbUserTypeActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cmbUserTypeActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        this.dispose();
        frontpage w = new frontpage();
        w.setVisible(true);
    }//GEN-LAST:event_jButton5ActionPerformed

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        this.dispose();
        librarianOradministrator w = new librarianOradministrator();
        w.setVisible(true);
    }//GEN-LAST:event_jButton6ActionPerformed

    private void txtSearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtSearchActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtSearchActionPerformed

    private void txtSearchKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSearchKeyReleased
        DefaultTableModel model = (DefaultTableModel) jTable2.getModel();
    model.setRowCount(0); // Clear current table data

    String searchText = txtSearch.getText().trim();
    
    try {
        Connection conn = MySQLConnect.getConnection();
        // The % allows the database to find any username that contains your search text
        String sql = "SELECT * FROM user WHERE userName LIKE ?"; 
        PreparedStatement pst = conn.prepareStatement(sql);
        pst.setString(1, "%" + searchText + "%");
        
        ResultSet rs = pst.executeQuery();

        while (rs.next()) {
            model.addRow(new Object[]{
                rs.getInt("id"),           // Index 0: Hidden ID
                rs.getString("fullName"),  // Index 1
                rs.getString("userName"),  // Index 2
                rs.getString("password"),  // Index 3
                rs.getString("category")   // Index 4
            });
        }
        
        // Keep the ID column hidden after searching
        if (jTable2.getColumnCount() > 0) {
            jTable2.getColumnModel().getColumn(0).setMinWidth(0);
            jTable2.getColumnModel().getColumn(0).setMaxWidth(0);
            jTable2.getColumnModel().getColumn(0).setWidth(0);
        }

    } catch (SQLException e) {
        JOptionPane.showMessageDialog(this, "Search Error: " + e.getMessage());
    }
    }//GEN-LAST:event_txtSearchKeyReleased

    private void closeBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_closeBtnActionPerformed
        setDefault();

        // Safety: Clear the selection from the table so no row stays highlighted
        jTable2.clearSelection();
    }//GEN-LAST:event_closeBtnActionPerformed

    private void saveBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveBtnActionPerformed
        String fullName = txtFullName.getText().trim();
        String userName = txtUsername.getText().trim();
        String password = txtPassword.getText();
        String confirmPass = txtConfirmPassword.getText();
        String userType = cmbUserType.getSelectedItem().toString();

        // 1. Check if any fields are empty
        if (fullName.isEmpty() || userName.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in all fields before saving!");
            return; 
        }

        // 2. Check if passwords match
        if (!password.equals(confirmPass)) {
            JOptionPane.showMessageDialog(this, "Passwords do not match!");
            txtPassword.requestFocus();
            return; 
        }

        try {
            Connection conn = MySQLConnect.getConnection();

            // 3. DUPLICATE CHECK: See if the username already exists
            String checkSql = "SELECT * FROM user WHERE userName = ?";
            PreparedStatement checkPst = conn.prepareStatement(checkSql);
            checkPst.setString(1, userName);
            ResultSet rs = checkPst.executeQuery();

            if (rs.next()) {
                // If the database finds a match, show error and stop
                JOptionPane.showMessageDialog(this, "The username '" + userName + "' is already taken!");
                txtUsername.requestFocus();
                return; 
            }

            // 4. SAVE LOGIC: If we reach here, the username is unique
            String sql = "INSERT INTO user (fullName, userName, password, category) VALUES (?, ?, ?, ?)";
            PreparedStatement pst = conn.prepareStatement(sql);

            pst.setString(1, fullName);
            pst.setString(2, userName);
            pst.setString(3, password);
            pst.setString(4, userType);

            int result = pst.executeUpdate();

            if (result > 0) {
                JOptionPane.showMessageDialog(this, "User Created Successfully!");
                populateTable(); // Updates your user list
                clearFields();       // Resets the text boxes
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Database Error: " + e.getMessage());
        }
    }//GEN-LAST:event_saveBtnActionPerformed

    private void deleteBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteBtnActionPerformed
        // 1. Check if a user is actually selected (userId should not be -1)
    if (userId == -1) {
        JOptionPane.showMessageDialog(this, "Please select a user from the table first!");
        return;
    }

    // 2. Ask for confirmation before deleting
    int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this user?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
    
    if (confirm == JOptionPane.YES_OPTION) {
        try {
            Connection conn = MySQLConnect.getConnection();
            // Use the userId variable we captured in the MouseClicked event
            String sql = "DELETE FROM user WHERE id = ?"; 
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setInt(1, userId);
            
            int deleted = pst.executeUpdate();
            
            if (deleted > 0) {
                JOptionPane.showMessageDialog(this, "User deleted successfully!");
                populateTable(); // Refresh the table
                clearFields();       // Clear the text boxes
            } else {
                // This is the error you are seeing! 
                JOptionPane.showMessageDialog(this, "Delete failed. Could not find that User.");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }
    }//GEN-LAST:event_deleteBtnActionPerformed

    private void updateBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_updateBtnActionPerformed
        // 1. SQL String with 5 parameters (fullName, userName, password, category, and ID)
    String sql = "UPDATE user SET fullName=?, userName=?, password=?, category=? WHERE id=?";
    
    try {
        Connection conn = MySQLConnect.getConnection();
        PreparedStatement pst = conn.prepareStatement(sql);
        
        // 2. Setting the parameters (Must be exactly 5)
        pst.setString(1, txtFullName.getText());   // Parameter 1
        pst.setString(2, txtUsername.getText());   // Parameter 2
        pst.setString(3, txtPassword.getText());   // Parameter 3
        pst.setString(4, cmbUserType.getSelectedItem().toString()); // Parameter 4
        pst.setInt(5, userId);                     // Parameter 5 (The ID)

        // 3. Execute
        int updated = pst.executeUpdate();
        
        if (updated > 0) {
            JOptionPane.showMessageDialog(this, "User updated successfully!");
            populateTable(); // Refresh the table
            clearFields();       // Clear boxes and lock buttons
        }
        
    } catch (SQLException e) {
        JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
    }

    }//GEN-LAST:event_updateBtnActionPerformed

    private void addNewBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addNewBtnActionPerformed
        // 1. Unlock everything
        txtFullName.setEnabled(true);
        txtUsername.setEnabled(true);
        txtPassword.setEnabled(true);
        txtConfirmPassword.setEnabled(true);
        cmbUserType.setEnabled(true);

        // 2. UI Logic
        saveBtn.setEnabled(true);     // Now they can save the new book
        addNewBtn.setEnabled(false);  // Disable this so they don't click it twice
        updateBtn.setEnabled(false);
        deleteBtn.setEnabled(false);

        //txtAcq.requestFocus();        // Cursor jumps to the first box
    }//GEN-LAST:event_addNewBtnActionPerformed

    private void jTable2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable2MouseClicked
        DefaultTableModel tblModel = (DefaultTableModel) jTable2.getModel();
        int selectedRow = jTable2.getSelectedRow();

        if (selectedRow != -1) {
            // 1. Capture the ID from the first column (Column 0)
            // This ensures the update/delete queries don't use -1
            userId = Integer.parseInt(tblModel.getValueAt(selectedRow, 0).toString());

            // 2. Map data to your text fields (Indices: 1=Name, 2=User, 3=Pass, 4=Type)
            txtFullName.setText(tblModel.getValueAt(selectedRow, 1).toString());
            txtUsername.setText(tblModel.getValueAt(selectedRow, 2).toString());
            txtPassword.setText(tblModel.getValueAt(selectedRow, 3).toString());
            txtConfirmPassword.setText(tblModel.getValueAt(selectedRow, 3).toString());
            cmbUserType.setSelectedItem(tblModel.getValueAt(selectedRow, 4).toString());

            // 3. Enable editing fields so you can type in them
            txtFullName.setEnabled(true);
            txtUsername.setEnabled(true);
            txtPassword.setEnabled(true);
            txtConfirmPassword.setEnabled(true);
            cmbUserType.setEnabled(true);

            // 4. BUTTON LOGIC: Enable Update and Delete, Disable Add/Save
            updateBtn.setEnabled(true); 
            deleteBtn.setEnabled(true);
            addNewBtn.setEnabled(false);
            saveBtn.setEnabled(false);
            txtUsername.setEnabled(false);
        }
    }//GEN-LAST:event_jTable2MouseClicked
   
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
        java.awt.EventQueue.invokeLater(() -> new user_management().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addNewBtn;
    private javax.swing.JButton closeBtn;
    private javax.swing.JComboBox<String> cmbUserType;
    private javax.swing.JButton deleteBtn;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JLabel jLabel1;
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
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable jTable2;
    private javax.swing.JButton saveBtn;
    private javax.swing.JPasswordField txtConfirmPassword;
    private javax.swing.JTextField txtFullName;
    private javax.swing.JPasswordField txtPassword;
    private javax.swing.JTextField txtSearch;
    private javax.swing.JPanel txtSearchUsername;
    private javax.swing.JTextField txtUsername;
    private javax.swing.JButton updateBtn;
    // End of variables declaration//GEN-END:variables

}
