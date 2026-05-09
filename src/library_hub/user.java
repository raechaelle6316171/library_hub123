package library_hub;

import javax.swing.table.DefaultTableModel;
import java.sql.*;
import java.util.Vector;
import javax.swing.JOptionPane;

public class user extends javax.swing.JFrame {
    
     int userId = -1;
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(user.class.getName());

    private int id;
    private String check;
    
    public user() {
        initComponents();
        MySQLConnect.getConnection(); 
        populateTable();              
        setDefault();
    }

   private boolean isUsernameDuplicate(String username) {
    boolean exists = false;
    try {
        Connection conn = MySQLConnect.getConnection();
   
        String sql = "SELECT * FROM user WHERE userName = ? AND id != ?";
        PreparedStatement pst = conn.prepareStatement(sql);
        pst.setString(1, username);
        pst.setInt(2, userId); 
        
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
    
    addNewBtn.setEnabled(true);
    saveBtn.setEnabled(false);
    updateBtn.setEnabled(false);
    deleteBtn.setEnabled(false);
    
    txtFullName.setEnabled(false);
    txtUsername.setEnabled(false);
    txtPassword.setEnabled(false);
    txtConfirmPassword.setEnabled(false);
    cmbUserType.setEnabled(false);
    
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

    txtFullName.setText("");
    txtUsername.setText("");
    txtPassword.setText("");
    txtConfirmPassword.setText("");
    cmbUserType.setSelectedIndex(0);

    txtFullName.setEnabled(false);
    txtUsername.setEnabled(false);
    txtPassword.setEnabled(false);
    txtConfirmPassword.setEnabled(false);
    cmbUserType.setEnabled(false);
    
    addNewBtn.setEnabled(true);   
    updateBtn.setEnabled(false);  
    deleteBtn.setEnabled(false);  
    saveBtn.setEnabled(false);    
    
    id = -1; // Reset ID
}
    
    private void jTable1MouseClicked(java.awt.event.MouseEvent evt) {                                     
    int row = jTable2.getSelectedRow();
    
    if (row != -1) {
   
        userId = Integer.parseInt(jTable2.getValueAt(row, 0).toString());
        
        txtFullName.setText(jTable2.getValueAt(row, 1).toString());
        txtUsername.setText(jTable2.getValueAt(row, 2).toString());
        txtPassword.setText(jTable2.getValueAt(row, 3).toString());
        txtConfirmPassword.setText(jTable2.getValueAt(row, 3).toString());
        cmbUserType.setSelectedItem(jTable2.getValueAt(row, 4).toString());

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
    
    public boolean isFullNameDuplicate(String fullName) {
    boolean exists = false;
    try {
        Connection conn = MySQLConnect.getConnection();
        String sql = "SELECT * FROM user WHERE fullName = ?";
        PreparedStatement pst = conn.prepareStatement(sql);
        pst.setString(1, fullName);
        ResultSet rs = pst.executeQuery();
        
        if (rs.next()) {
            exists = true;
        }
    } catch (SQLException e) {
        JOptionPane.showMessageDialog(null, "Validation Error: " + e.getMessage());
    }
    return exists;
}
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel4 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        cmbUserType = new javax.swing.JComboBox<>();
        addNewBtn = new javax.swing.JButton();
        updateBtn = new javax.swing.JButton();
        saveBtn = new javax.swing.JButton();
        deleteBtn = new javax.swing.JButton();
        closeBtn = new javax.swing.JButton();
        txtFullName = new javax.swing.JTextField();
        txtUsername = new javax.swing.JTextField();
        txtPassword = new javax.swing.JPasswordField();
        txtConfirmPassword = new javax.swing.JPasswordField();
        jPanel5 = new javax.swing.JPanel();
        txtSearch = new javax.swing.JTextField();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTable2 = new javax.swing.JTable();
        jLabel12 = new javax.swing.JLabel();
        jPanel7 = new javax.swing.JPanel();
        jLabel15 = new javax.swing.JLabel();
        jButton10 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel4.setBackground(new java.awt.Color(67, 83, 189));

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("FULL NAME");

        jLabel7.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(255, 255, 255));
        jLabel7.setText("USERNAME");

        jLabel8.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(255, 255, 255));
        jLabel8.setText("PASSWORD");

        jLabel9.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(255, 255, 255));
        jLabel9.setText("CONFIRM PASSWORD");

        jLabel10.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(255, 255, 255));
        jLabel10.setText("USER TYPE");

        cmbUserType.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        cmbUserType.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "~Select User Type~", "Librarian", "Staff" }));
        cmbUserType.addActionListener(this::cmbUserTypeActionPerformed);

        addNewBtn.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        addNewBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/library_hub/add.png"))); // NOI18N
        addNewBtn.setText("ADD ");
        addNewBtn.addActionListener(this::addNewBtnActionPerformed);

        updateBtn.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        updateBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/library_hub/pencil.png"))); // NOI18N
        updateBtn.setText("UPDATE");
        updateBtn.addActionListener(this::updateBtnActionPerformed);

        saveBtn.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        saveBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/library_hub/download.png"))); // NOI18N
        saveBtn.setText("SAVE");
        saveBtn.addActionListener(this::saveBtnActionPerformed);

        deleteBtn.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        deleteBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/library_hub/bin.png"))); // NOI18N
        deleteBtn.setText("DELETE");
        deleteBtn.addActionListener(this::deleteBtnActionPerformed);

        closeBtn.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        closeBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/library_hub/cancel.png"))); // NOI18N
        closeBtn.setText("CANCEL");
        closeBtn.addActionListener(this::closeBtnActionPerformed);

        txtFullName.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N

        txtUsername.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N

        txtPassword.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N

        txtConfirmPassword.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 179, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 179, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(txtPassword, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 278, Short.MAX_VALUE)
                                .addComponent(txtUsername, javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel7, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 179, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(txtFullName, javax.swing.GroupLayout.Alignment.LEADING)))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                                    .addComponent(addNewBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 133, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                    .addComponent(updateBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 133, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(jPanel4Layout.createSequentialGroup()
                                    .addComponent(deleteBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 133, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                    .addComponent(saveBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 133, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addGap(70, 70, 70)
                                .addComponent(closeBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 137, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(txtConfirmPassword)
                                .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 132, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel9)
                                .addComponent(cmbUserType, 0, 278, Short.MAX_VALUE)))
                        .addGap(0, 49, Short.MAX_VALUE))))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(28, 28, 28)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtFullName, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel7)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtUsername, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel8)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(txtPassword, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel9)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtConfirmPassword, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel10)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cmbUserType, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(updateBtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(addNewBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(deleteBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(saveBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(closeBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(61, Short.MAX_VALUE))
        );

        jPanel5.setBackground(new java.awt.Color(67, 83, 189));

        txtSearch.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        txtSearch.addActionListener(this::txtSearchActionPerformed);
        txtSearch.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtSearchKeyReleased(evt);
            }
        });

        jTable2.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
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
                "Id", "Full Name", "Username", "Password", "User Type"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable2MouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(jTable2);

        jLabel12.setFont(new java.awt.Font("Trebuchet MS", 1, 24)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(255, 255, 255));
        jLabel12.setText("SEARCH");

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addContainerGap(24, Short.MAX_VALUE)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 1124, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(jLabel12)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 1031, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(16, 16, 16))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtSearch, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel12))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 556, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel7.setBackground(new java.awt.Color(67, 83, 189));

        jLabel15.setFont(new java.awt.Font("Lucida Fax", 1, 36)); // NOI18N
        jLabel15.setForeground(new java.awt.Color(255, 255, 255));
        jLabel15.setText("USER MANAGEMENT");

        jButton10.setIcon(new javax.swing.ImageIcon(getClass().getResource("/library_hub/cancel.png"))); // NOI18N
        jButton10.addActionListener(this::jButton10ActionPerformed);

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGap(332, 332, 332)
                .addComponent(jLabel15)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButton10, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton10, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel15))
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
                    .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void cmbUserTypeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbUserTypeActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cmbUserTypeActionPerformed

    private void addNewBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addNewBtnActionPerformed
        txtFullName.setEnabled(true);
        txtUsername.setEnabled(true);
        txtPassword.setEnabled(true);
        txtConfirmPassword.setEnabled(true);
        cmbUserType.setEnabled(true);

        saveBtn.setEnabled(true);
        addNewBtn.setEnabled(false);
        updateBtn.setEnabled(false);
        deleteBtn.setEnabled(false);

        txtFullName.requestFocus();
    }//GEN-LAST:event_addNewBtnActionPerformed

    private void updateBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_updateBtnActionPerformed
        String fullName = txtFullName.getText().trim();
        String userName = txtUsername.getText().trim();
        String password = txtPassword.getText();
        String confirmPass = txtConfirmPassword.getText();
        String userType = cmbUserType.getSelectedItem().toString();

        if (userId == -1) {
            JOptionPane.showMessageDialog(this, "Please select a user from the table first!");
            return;
        }

        if (fullName.isEmpty() || userName.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in all fields before updating!");
            return;
        }

        if (!password.equals(confirmPass)) {
            JOptionPane.showMessageDialog(this, "Passwords do not match! Update cancelled.");
            txtPassword.requestFocus();
            return;
        }

        if (cmbUserType.getSelectedIndex() == 0) {
            JOptionPane.showMessageDialog(this, "Select a User Type!");
            cmbUserType.requestFocus();
            return;
        }

        try {
            Connection conn = MySQLConnect.getConnection();

            String checkSql = "SELECT * FROM user WHERE (userName = ? OR fullName = ?) AND id != ?";
            PreparedStatement checkPst = conn.prepareStatement(checkSql);
            checkPst.setString(1, userName);
            checkPst.setString(2, fullName);
            checkPst.setInt(3, userId);
            ResultSet rs = checkPst.executeQuery();

            if (rs.next()) {
                String existingUser = rs.getString("userName");
                String existingName = rs.getString("fullName");

                if (existingUser.equalsIgnoreCase(userName)) {
                    JOptionPane.showMessageDialog(this, "The username '" + userName + "' is already taken by another user!");
                    txtUsername.requestFocus();
                } else if (existingName.equalsIgnoreCase(fullName)) {
                    JOptionPane.showMessageDialog(this, "The full name '" + fullName + "' is already registered to another user!");
                    txtFullName.requestFocus();
                }
                return;
            }

            String sql = "UPDATE user SET fullName=?, userName=?, password=?, category=? WHERE id=?";
            PreparedStatement pst = conn.prepareStatement(sql);

            pst.setString(1, fullName);
            pst.setString(2, userName);
            pst.setString(3, password);
            pst.setString(4, userType);
            pst.setInt(5, userId);

            int updated = pst.executeUpdate();

            if (updated > 0) {
                JOptionPane.showMessageDialog(this, "User updated successfully!");
                populateTable();
                clearFields();
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }//GEN-LAST:event_updateBtnActionPerformed

    private void saveBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveBtnActionPerformed
        String fullName = txtFullName.getText().trim();
        String userName = txtUsername.getText().trim();
        String password = txtPassword.getText();
        String confirmPass = txtConfirmPassword.getText();
        String userType = cmbUserType.getSelectedItem().toString();

        // 1. Specific Field Validations
        if (fullName.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter your full name");
            txtFullName.requestFocus();
            return;
        }

        if (userName.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter your username");
            txtUsername.requestFocus();
            return;
        }

        if (password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter your password");
            txtPassword.requestFocus();
            return;
        }

        if (confirmPass.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please confirm your password");
            txtConfirmPassword.requestFocus();
            return;
        }

        // 2. Password Matching Check
        if (!password.equals(confirmPass)) {
            JOptionPane.showMessageDialog(this, "Passwords do not match!");
            txtConfirmPassword.setText("");
            txtConfirmPassword.requestFocus();
            return;
        }

        // 3. User Type Check
        if (cmbUserType.getSelectedIndex() == 0 || userType.equals("~Select User Type~")) {
            JOptionPane.showMessageDialog(this, "Please select a User Type!");
            cmbUserType.requestFocus();
            return;
        }

        try {
            Connection conn = MySQLConnect.getConnection();

            // 4. Duplicate Check
            String checkSql = "SELECT * FROM user WHERE userName = ? OR fullName = ?";
            PreparedStatement checkPst = conn.prepareStatement(checkSql);
            checkPst.setString(1, userName);
            checkPst.setString(2, fullName);
            ResultSet rs = checkPst.executeQuery();

            if (rs.next()) {
                String existingUser = rs.getString("userName");
                String existingName = rs.getString("fullName");

                if (existingUser.equalsIgnoreCase(userName)) {
                    JOptionPane.showMessageDialog(this, "The username '" + userName + "' is already taken!");
                    txtUsername.requestFocus();
                } else if (existingName.equalsIgnoreCase(fullName)) {
                    JOptionPane.showMessageDialog(this, "The full name '" + fullName + "' is already registered!");
                    txtFullName.requestFocus();
                }
                return;
            }

            // 5. Insert Record
            String sql = "INSERT INTO user (fullName, userName, password, category) VALUES (?, ?, ?, ?)";
            PreparedStatement pst = conn.prepareStatement(sql);

            pst.setString(1, fullName);
            pst.setString(2, userName);
            pst.setString(3, password);
            pst.setString(4, userType);

            int result = pst.executeUpdate();

            if (result > 0) {
                JOptionPane.showMessageDialog(this, "User Created Successfully!");
                populateTable();
                clearFields();
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Database Error: " + e.getMessage());
        }
    }//GEN-LAST:event_saveBtnActionPerformed

    private void deleteBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteBtnActionPerformed
        if (userId == -1) {
            JOptionPane.showMessageDialog(this, "Please select a user from the table first!");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this user?", "Confirm Delete", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                Connection conn = MySQLConnect.getConnection();
                String sql = "DELETE FROM user WHERE id = ?";
                PreparedStatement pst = conn.prepareStatement(sql);
                pst.setInt(1, userId);

                int deleted = pst.executeUpdate();

                if (deleted > 0) {
                    JOptionPane.showMessageDialog(this, "User deleted successfully!");
                    populateTable();
                    clearFields();
                } else {
                    JOptionPane.showMessageDialog(this, "Delete failed. Could not find that User.");
                }
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
            }
        }
    }//GEN-LAST:event_deleteBtnActionPerformed

    private void closeBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_closeBtnActionPerformed
        setDefault();
        jTable2.clearSelection();
    }//GEN-LAST:event_closeBtnActionPerformed

    private void txtSearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtSearchActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtSearchActionPerformed

    private void txtSearchKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSearchKeyReleased
        DefaultTableModel model = (DefaultTableModel) jTable2.getModel();
        model.setRowCount(0);

        String searchText = txtSearch.getText().trim();

        try {
            Connection conn = MySQLConnect.getConnection();

            String sql = "SELECT * FROM user WHERE userName LIKE ?";
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1, "%" + searchText + "%");

            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getInt("id"),
                    rs.getString("fullName"),
                    rs.getString("userName"),
                    rs.getString("password"),
                    rs.getString("category")
                });
            }

            if (jTable2.getColumnCount() > 0) {
                jTable2.getColumnModel().getColumn(0).setMinWidth(0);
                jTable2.getColumnModel().getColumn(0).setMaxWidth(0);
                jTable2.getColumnModel().getColumn(0).setWidth(0);
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Search Error: " + e.getMessage());
        }
    }//GEN-LAST:event_txtSearchKeyReleased

    private void jTable2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable2MouseClicked
        DefaultTableModel tblModel = (DefaultTableModel) jTable2.getModel();
        int selectedRow = jTable2.getSelectedRow();

        if (selectedRow != -1) {
            userId = Integer.parseInt(tblModel.getValueAt(selectedRow, 0).toString());

            txtFullName.setText(tblModel.getValueAt(selectedRow, 1).toString());
            txtUsername.setText(tblModel.getValueAt(selectedRow, 2).toString());
            txtPassword.setText(tblModel.getValueAt(selectedRow, 3).toString());
            txtConfirmPassword.setText(tblModel.getValueAt(selectedRow, 3).toString());
            cmbUserType.setSelectedItem(tblModel.getValueAt(selectedRow, 4).toString());

            txtFullName.setEnabled(true);
            txtUsername.setEnabled(true);
            txtPassword.setEnabled(true);
            txtConfirmPassword.setEnabled(true);
            cmbUserType.setEnabled(true);

            updateBtn.setEnabled(true);
            deleteBtn.setEnabled(true);
            addNewBtn.setEnabled(false);
            saveBtn.setEnabled(false);
            txtUsername.setEnabled(false);
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
        java.awt.EventQueue.invokeLater(() -> new user().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addNewBtn;
    private javax.swing.JButton closeBtn;
    private javax.swing.JComboBox<String> cmbUserType;
    private javax.swing.JButton deleteBtn;
    private javax.swing.JButton jButton10;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable jTable2;
    private javax.swing.JButton saveBtn;
    private javax.swing.JPasswordField txtConfirmPassword;
    private javax.swing.JTextField txtFullName;
    private javax.swing.JPasswordField txtPassword;
    private javax.swing.JTextField txtSearch;
    private javax.swing.JTextField txtUsername;
    private javax.swing.JButton updateBtn;
    // End of variables declaration//GEN-END:variables
}
