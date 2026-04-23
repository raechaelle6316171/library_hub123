package library_hub;

import javax.swing.table.DefaultTableModel;
import java.sql.*;
import java.util.Vector;
import javax.swing.JOptionPane;

public class register_member_try extends javax.swing.JFrame {
    
    int memberId = -1;
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(register_member_try.class.getName());
    
    private int id;
    private String check;
    public register_member_try(){
        initComponents();
        MySQLConnect.getConnection(); 
        populateTable();             
        setDefault();

    }
    
    private void clearFields() {

    txtFullName.setText("");
    cmbUserType.setSelectedIndex(0);
    cmbCourse.setSelectedIndex(0);
    cmbYear.setSelectedIndex(0);
    
    addNewBtn.setEnabled(true);
    saveBtn.setEnabled(false);
    updateBtn.setEnabled(false);
    deleteBtn.setEnabled(false);
    
    txtFullName.setEnabled(false);
    cmbUserType.setEnabled(false);
    cmbCourse.setEnabled(false);
    cmbYear.setEnabled(false);
    
    //cmbCourse.setEnabled(true);
    //cmbYear.setEnabled(true);
    
    memberId = -1; 
}
     public void makeEnable(){
    
        txtFullName.setEnabled(true);
        cmbUserType.setEnabled(true);
        cmbCourse.setEnabled(true);
        cmbYear.setEnabled(true);
        }
     
     
    public void setDefault() {

    txtFullName.setText("");
    cmbUserType.setSelectedIndex(0);
    cmbCourse.setSelectedIndex(0);
    cmbYear.setSelectedIndex(0);

    txtFullName.setEnabled(false);
    cmbUserType.setEnabled(false);
    cmbCourse.setEnabled(false);
    cmbYear.setEnabled(false);
    
    addNewBtn.setEnabled(true);   
    saveBtn.setEnabled(false);    
    updateBtn.setEnabled(false);  
    deleteBtn.setEnabled(false);  
    
    memberId = -1; 
}
    
    public void populateTable(String searchName) { 
    try {
        Connection conn = MySQLConnect.getConnection();
        DefaultTableModel tblModel = (DefaultTableModel) jTable2.getModel();
        tblModel.setRowCount(0);

        String query = "SELECT * FROM member_records WHERE fullName LIKE ?";
        PreparedStatement pst = conn.prepareStatement(query);
        pst.setString(1, "%" + searchName + "%"); 

        ResultSet rs = pst.executeQuery();

        while (rs.next()) {

            Object[] row = {
                rs.getInt("id"),
                rs.getString("fullName"),
                rs.getString("usertype"),
                rs.getString("course"),
                rs.getString("year")
            };
            tblModel.addRow(row);
        }
    } catch (SQLException e) {
        JOptionPane.showMessageDialog(null, "Database Error: " + e.getMessage());
    }
}
    

    public void populateTable(){
        int colCount;
        try{
            Connection conn = MySQLConnect.getConnection();
            Statement st = conn.createStatement();
            String query = "SELECT * FROM member_records";
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
                    colData.add(rs.getString("usertype"));
                    colData.add(rs.getString("course"));
                    colData.add(rs.getString("year"));
                }
                tblModel.addRow(colData);
            }
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null, e);
            
        }
        //updateBookCount();
    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        txtSearchUsername = new javax.swing.JPanel();
        txtSearchMember = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTable2 = new javax.swing.JTable();
        cancelBtn1 = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        jButton5 = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        txtFullName = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        cmbUserType = new javax.swing.JComboBox<>();
        cancelBtn = new javax.swing.JButton();
        saveBtn = new javax.swing.JButton();
        deleteBtn = new javax.swing.JButton();
        updateBtn = new javax.swing.JButton();
        addNewBtn = new javax.swing.JButton();
        cmbCourse = new javax.swing.JComboBox<>();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        cmbYear = new javax.swing.JComboBox<>();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        txtSearchUsername.setBackground(new java.awt.Color(102, 0, 102));
        txtSearchUsername.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 5));

        txtSearchMember.addActionListener(this::txtSearchMemberActionPerformed);
        txtSearchMember.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtSearchMemberKeyReleased(evt);
            }
        });

        jLabel7.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(255, 255, 255));
        jLabel7.setText("Search FullName");

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
                "Id", "Full Name", "User Type", "Course", "Year"
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

        cancelBtn1.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        cancelBtn1.setText("←BACK TO BORROWER");
        cancelBtn1.addActionListener(this::cancelBtn1ActionPerformed);

        javax.swing.GroupLayout txtSearchUsernameLayout = new javax.swing.GroupLayout(txtSearchUsername);
        txtSearchUsername.setLayout(txtSearchUsernameLayout);
        txtSearchUsernameLayout.setHorizontalGroup(
            txtSearchUsernameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(txtSearchUsernameLayout.createSequentialGroup()
                .addGap(26, 26, 26)
                .addGroup(txtSearchUsernameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(cancelBtn1)
                    .addGroup(txtSearchUsernameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(txtSearchUsernameLayout.createSequentialGroup()
                            .addComponent(jLabel7)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(txtSearchMember, javax.swing.GroupLayout.PREFERRED_SIZE, 146, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        txtSearchUsernameLayout.setVerticalGroup(
            txtSearchUsernameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(txtSearchUsernameLayout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addGroup(txtSearchUsernameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtSearchMember, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel7))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 231, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(26, 26, 26)
                .addComponent(cancelBtn1, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(26, Short.MAX_VALUE))
        );

        jPanel3.setBackground(new java.awt.Color(102, 0, 102));
        jPanel3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 5));
        jPanel3.setPreferredSize(new java.awt.Dimension(285, 53));

        jLabel8.setFont(new java.awt.Font("Century Gothic", 1, 24)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(255, 255, 255));
        jLabel8.setText("MEMBER REGISTRATION");

        jButton5.setText("x");
        jButton5.addActionListener(this::jButton5ActionPerformed);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel8)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 197, Short.MAX_VALUE)
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

        jPanel4.setBackground(new java.awt.Color(102, 0, 102));
        jPanel4.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 5));

        jLabel1.setFont(new java.awt.Font("Century Gothic", 1, 24)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("FULL NAME");

        jLabel6.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setText("USER TYPE");

        cmbUserType.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "~Select User Type~", "Student", "Faculty" }));
        cmbUserType.addItemListener(this::cmbUserTypeItemStateChanged);
        cmbUserType.addActionListener(this::cmbUserTypeActionPerformed);

        cancelBtn.setText("CANCEL");
        cancelBtn.addActionListener(this::cancelBtnActionPerformed);

        saveBtn.setText("SAVE");
        saveBtn.addActionListener(this::saveBtnActionPerformed);

        deleteBtn.setText("DELETE");
        deleteBtn.addActionListener(this::deleteBtnActionPerformed);

        updateBtn.setText("UPDATE");
        updateBtn.addActionListener(this::updateBtnActionPerformed);

        addNewBtn.setText("ADD NEW");
        addNewBtn.addActionListener(this::addNewBtnActionPerformed);

        cmbCourse.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "~Select Course~", "BSIT", "BSCE", "BSCS" }));
        cmbCourse.addActionListener(this::cmbCourseActionPerformed);

        jLabel10.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(255, 255, 255));
        jLabel10.setText("COURSE");

        jLabel11.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(255, 255, 255));
        jLabel11.setText("YEAR");

        cmbYear.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "~Select Year~", "I", "II", "III", "IV" }));
        cmbYear.addActionListener(this::cmbYearActionPerformed);

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(35, 35, 35)
                        .addComponent(jLabel1))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(24, 24, 24)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 179, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 179, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 179, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 179, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(txtFullName, javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(cmbUserType, javax.swing.GroupLayout.Alignment.LEADING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(cmbCourse, javax.swing.GroupLayout.Alignment.LEADING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(cmbYear, javax.swing.GroupLayout.Alignment.LEADING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addGroup(jPanel4Layout.createSequentialGroup()
                                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addComponent(deleteBtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(addNewBtn))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addComponent(updateBtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(saveBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                    .addGroup(jPanel4Layout.createSequentialGroup()
                                        .addComponent(cancelBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(42, 42, 42)))))))
                .addContainerGap(25, Short.MAX_VALUE))
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
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cmbUserType, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel10)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cmbCourse, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel11)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cmbYear, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(31, 31, 31)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(addNewBtn)
                    .addComponent(updateBtn))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(deleteBtn)
                    .addComponent(saveBtn))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(cancelBtn)
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
                    .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, 510, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtSearchUsername, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
            .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void txtSearchMemberActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtSearchMemberActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtSearchMemberActionPerformed

    private void txtSearchMemberKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSearchMemberKeyReleased
        populateTable(txtSearchMember.getText());
    }//GEN-LAST:event_txtSearchMemberKeyReleased

    private void jTable2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable2MouseClicked
        DefaultTableModel tblModel = (DefaultTableModel) jTable2.getModel();
    int selectedRow = jTable2.getSelectedRow();

    if (selectedRow != -1) {

        memberId = Integer.parseInt(tblModel.getValueAt(selectedRow, 0).toString());
        String name = tblModel.getValueAt(selectedRow, 1).toString();
        String type = tblModel.getValueAt(selectedRow, 2).toString();
        String course = tblModel.getValueAt(selectedRow, 3).toString();
        String year = tblModel.getValueAt(selectedRow, 4).toString();
        
        txtFullName.setText(name);
        cmbUserType.setSelectedItem(type);
        cmbCourse.setSelectedItem(course);
        cmbYear.setSelectedItem(year);

        if (type.equalsIgnoreCase("Faculty")) {
            cmbCourse.setEnabled(false);
            cmbYear.setEnabled(false);
        } else {
            cmbCourse.setEnabled(true);
            cmbYear.setEnabled(true);
        }

        txtFullName.setEnabled(true);
        cmbUserType.setEnabled(true);
        
        updateBtn.setEnabled(true); 
        deleteBtn.setEnabled(true);
        cancelBtn.setEnabled(true); 
        
        addNewBtn.setEnabled(false);
        saveBtn.setEnabled(false);
    }
    }//GEN-LAST:event_jTable2MouseClicked

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        this.dispose();
        frontpage w = new frontpage();
        w.setVisible(true);
    }//GEN-LAST:event_jButton5ActionPerformed

    private void cmbUserTypeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbUserTypeActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cmbUserTypeActionPerformed

    private void cancelBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelBtnActionPerformed
        setDefault();
        jTable2.clearSelection();
    }//GEN-LAST:event_cancelBtnActionPerformed

    private void saveBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveBtnActionPerformed
        String name = txtFullName.getText().trim();
    String type = cmbUserType.getSelectedItem().toString();
    String course = cmbCourse.getSelectedItem().toString();
    String year = cmbYear.getSelectedItem().toString();

    if (name.isEmpty() || type.equals("~Select User Type~")) {
        JOptionPane.showMessageDialog(this, "Please enter a Name and select a User Type!");
        return;
    }

    if (type.equalsIgnoreCase("Student")) {
        if (course.equals("~Select Course~") || year.equals("~Select Year~")) {
            JOptionPane.showMessageDialog(this, "Students must have a valid Course and Year!");
            return;
        }
    } else if (type.equalsIgnoreCase("Faculty")) {

        course = "N/A";
        year = "N/A";
    }

    try {
        Connection conn = MySQLConnect.getConnection();

        String checkQuery = "SELECT * FROM member_records WHERE fullname = ?";
        PreparedStatement checkPst = conn.prepareStatement(checkQuery);
        checkPst.setString(1, name);
        ResultSet rs = checkPst.executeQuery();

        if (rs.next()) {
            JOptionPane.showMessageDialog(this, "Error: Member '" + name + "' is already registered!", "Duplicate Entry", JOptionPane.ERROR_MESSAGE);
            txtFullName.requestFocus();
            return; 
        }

        String sql = "INSERT INTO member_records (fullname, usertype, course, year) VALUES (?, ?, ?, ?)";
        PreparedStatement pst = conn.prepareStatement(sql);
        pst.setString(1, name);
        pst.setString(2, type);
        pst.setString(3, course);
        pst.setString(4, year);

        pst.executeUpdate();
        JOptionPane.showMessageDialog(this, "Member Registered Successfully!");
        
        populateTable(""); 
        clearFields();     

    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
    }
    }//GEN-LAST:event_saveBtnActionPerformed

    private void deleteBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteBtnActionPerformed
        if (memberId == -1) {
            JOptionPane.showMessageDialog(this, "Please select a member from the table first!");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this, 
                "Are you sure you want to delete this member registration?", 
                "Confirm Delete", 
                JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                Connection conn = MySQLConnect.getConnection();

                String sql = "DELETE FROM member_records WHERE id = ?"; 
                PreparedStatement pst = conn.prepareStatement(sql);
                pst.setInt(1, memberId);

                int deleted = pst.executeUpdate();

                if (deleted > 0) {
                    JOptionPane.showMessageDialog(this, "Member deleted successfully!");
                    populateTable(); 
                    clearFields();   
                    memberId = -1;   
                } else {
                    JOptionPane.showMessageDialog(this, "Delete failed. Could not find that Member record.");
                }
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
            }
        }

    }//GEN-LAST:event_deleteBtnActionPerformed

    private void updateBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_updateBtnActionPerformed
        int row = jTable2.getSelectedRow();
    if (row == -1) {
        JOptionPane.showMessageDialog(this, "Please select a member from the table to update!");
        return;
    }

    String id = jTable2.getValueAt(row, 0).toString();
    String name = txtFullName.getText().trim();
    String type = cmbUserType.getSelectedItem().toString();
    String course = cmbCourse.getSelectedItem().toString();
    String year = cmbYear.getSelectedItem().toString();

    if (name.isEmpty() || type.equals("~Select User Type~")) {
        JOptionPane.showMessageDialog(this, "Full Name and User Type are required!");
        return;
    }

    if (type.equalsIgnoreCase("Student")) {
        if (course.equals("~Select Course~") || year.equals("~Select Year~")) {
            JOptionPane.showMessageDialog(this, "Please select a valid Course and Year for Students!");
            return;
        }
    } else {
        course = "N/A";
        year = "N/A";
    }

    try {
        Connection conn = MySQLConnect.getConnection();

        String checkSql = "SELECT * FROM member_records WHERE fullname = ? AND id != ?";
        PreparedStatement checkPst = conn.prepareStatement(checkSql);
        checkPst.setString(1, name);
        checkPst.setString(2, id);
        ResultSet rs = checkPst.executeQuery();

        if (rs.next()) {
            JOptionPane.showMessageDialog(this, "Error: The name '" + name + "' is already used by another member!", "Duplicate Entry", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String sql = "UPDATE member_records SET fullname=?, usertype=?, course=?, year=? WHERE id=?";
        PreparedStatement pst = conn.prepareStatement(sql);
        
        pst.setString(1, name);
        pst.setString(2, type);
        pst.setString(3, course);
        pst.setString(4, year);
        pst.setString(5, id);
        
        pst.executeUpdate();
        JOptionPane.showMessageDialog(this, "Member Updated Successfully!");
        
        populateTable(""); 
        clearFields();     
        
    } catch (SQLException e) {
        JOptionPane.showMessageDialog(this, "Update Error: " + e.getMessage());
    }
    }//GEN-LAST:event_updateBtnActionPerformed

    private void addNewBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addNewBtnActionPerformed
        txtFullName.setEnabled(true);
        cmbUserType.setEnabled(true);
        cmbCourse.setEnabled(true);
        cmbYear.setEnabled(true);

        saveBtn.setEnabled(true);     
        addNewBtn.setEnabled(false);  
        updateBtn.setEnabled(false);
        deleteBtn.setEnabled(false);

        //txtAcq.requestFocus();       
    }//GEN-LAST:event_addNewBtnActionPerformed

    private void cmbCourseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbCourseActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cmbCourseActionPerformed

    private void cmbYearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbYearActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cmbYearActionPerformed

    private void cancelBtn1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelBtn1ActionPerformed
        this.dispose();
        borrow_management_123 w = new borrow_management_123();
        w.setVisible(true);
    }//GEN-LAST:event_cancelBtn1ActionPerformed

    private void cmbUserTypeItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cmbUserTypeItemStateChanged
        String selectedUser = cmbUserType.getSelectedItem().toString();
    
        if (selectedUser.equalsIgnoreCase("Faculty")) {
           
            cmbCourse.setEnabled(false);
            cmbYear.setEnabled(false);

            cmbCourse.setSelectedIndex(0); 
            cmbYear.setSelectedIndex(0);
        } else {

            cmbCourse.setEnabled(true);
            cmbYear.setEnabled(true);
        }
    }//GEN-LAST:event_cmbUserTypeItemStateChanged

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
        java.awt.EventQueue.invokeLater(() -> new register_member_try().setVisible(true));
    }
    

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addNewBtn;
    private javax.swing.JButton cancelBtn;
    private javax.swing.JButton cancelBtn1;
    private javax.swing.JComboBox<String> cmbCourse;
    private javax.swing.JComboBox<String> cmbUserType;
    private javax.swing.JComboBox<String> cmbYear;
    private javax.swing.JButton deleteBtn;
    private javax.swing.JButton jButton5;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable jTable2;
    private javax.swing.JButton saveBtn;
    private javax.swing.JTextField txtFullName;
    private javax.swing.JTextField txtSearchMember;
    private javax.swing.JPanel txtSearchUsername;
    private javax.swing.JButton updateBtn;
    // End of variables declaration//GEN-END:variables
}
