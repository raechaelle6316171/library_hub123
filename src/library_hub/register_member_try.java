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
    // 1. Clear all text
    txtSchoolFacultyID.setText("");
    txtFullName.setText("");
    txtContactNumber.setText("");
    
    // 2. Reset all combo boxes
    cmbUserType.setSelectedIndex(0); 
    cmbCourse.setSelectedIndex(0);   
    cmbYear.setSelectedIndex(0);     

    // 3. Disable the fields (This makes them look like the others in your screenshot)
    txtSchoolFacultyID.setEnabled(false);
    txtFullName.setEnabled(false);
    txtContactNumber.setEnabled(false);
    cmbUserType.setEnabled(false);
    cmbCourse.setEnabled(false);
    cmbYear.setEnabled(false);

    // 4. Disable buttons that shouldn't be used yet
    updateBtn.setEnabled(false);
    saveBtn.setEnabled(false);
    deleteBtn.setEnabled(false);
}
     public void makeEnable(){
    
        txtFullName.setEnabled(true);
        cmbUserType.setEnabled(true);
        cmbCourse.setEnabled(true);
        cmbYear.setEnabled(true);
        }
     
     
    public void setDefault() {
    txtSchoolFacultyID.setText("");
    txtFullName.setText("");
    txtContactNumber.setText("");
    cmbUserType.setSelectedIndex(0);
    cmbCourse.setSelectedIndex(0);
    cmbYear.setSelectedIndex(0);
    
    txtSchoolFacultyID.setEnabled(false);
    txtFullName.setEnabled(false);
    txtContactNumber.setEnabled(false);

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

            // Ensure you are selecting the NEW columns (school_id and contact_number)
            String query = "SELECT id, school_id, fullName, usertype, course, year, contact_number FROM member_records WHERE fullName LIKE ?";
            PreparedStatement pst = conn.prepareStatement(query);
            pst.setString(1, "%" + searchName + "%"); 
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                // YOU MUST FOLLOW THIS EXACT ORDER:
                Object[] row = {
                    rs.getInt("id"),               // Index 0 (Hidden Database ID)
                    rs.getString("school_id"),     // Index 1 (School/Faculty Id)
                    rs.getString("fullName"),      // Index 2 (Full Name)
                    rs.getString("usertype"),      // Index 3 (User Type)
                    rs.getString("course"),        // Index 4 (Course)
                    rs.getString("year"),          // Index 5 (Year)
                    rs.getString("contact_number") // Index 6 (Contact Number)
                };
                tblModel.addRow(row);
            }

            // Hide the first column (Database ID)
            jTable2.getColumnModel().getColumn(0).setMinWidth(0);
            jTable2.getColumnModel().getColumn(0).setMaxWidth(0);
            jTable2.getColumnModel().getColumn(0).setWidth(0);

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Database Error: " + e.getMessage());
        }
    }

    public void populateTable() {
    try {
        Connection conn = MySQLConnect.getConnection();
        DefaultTableModel tblModel = (DefaultTableModel) jTable2.getModel();
        tblModel.setRowCount(0); // Clears the table before reloading

        // 1. You must select every column explicitly from your database
        String query = "SELECT id, school_id, fullName, usertype, course, year, contact_number FROM member_records";
        Statement st = conn.createStatement();
        ResultSet rs = st.executeQuery(query);

        while (rs.next()) {
            // 2. The order here must match your JTable columns exactly
            Object[] row = {
                rs.getInt("id"),               // Index 0 (Database ID - Hidden)
                rs.getString("school_id"),     // Index 1 (School/ Faculty Id)
                rs.getString("fullName"),      // Index 2 (Full Name)
                rs.getString("usertype"),      // Index 3 (User Type)
                rs.getString("course"),        // Index 4 (Course)
                rs.getString("year"),          // Index 5 (Year)
                rs.getString("contact_number") // Index 6 (Contact Number)
            };
            tblModel.addRow(row);
        }

        // 3. Hide the Database ID (Index 0) so the user doesn't see it
        jTable2.getColumnModel().getColumn(0).setMinWidth(0);
        jTable2.getColumnModel().getColumn(0).setMaxWidth(0);
        jTable2.getColumnModel().getColumn(0).setWidth(0);

    } catch (SQLException e) {
        JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
    }
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
        jLabel3 = new javax.swing.JLabel();
        txtSchoolFacultyID = new javax.swing.JTextField();
        txtContactNumber = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();

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
                "Id", "School/ Faculty Id", "Full Name", "User Type", "Course", "Year", "Contact Number"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false
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
        cancelBtn1.setText("←GO TO BORROW");
        cancelBtn1.addActionListener(this::cancelBtn1ActionPerformed);

        javax.swing.GroupLayout txtSearchUsernameLayout = new javax.swing.GroupLayout(txtSearchUsername);
        txtSearchUsername.setLayout(txtSearchUsernameLayout);
        txtSearchUsernameLayout.setHorizontalGroup(
            txtSearchUsernameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(txtSearchUsernameLayout.createSequentialGroup()
                .addGap(26, 26, 26)
                .addGroup(txtSearchUsernameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(txtSearchUsernameLayout.createSequentialGroup()
                        .addComponent(jLabel7)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtSearchMember, javax.swing.GroupLayout.PREFERRED_SIZE, 146, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(txtSearchUsernameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addGroup(txtSearchUsernameLayout.createSequentialGroup()
                            .addGap(95, 95, 95)
                            .addComponent(cancelBtn1))
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 622, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(35, Short.MAX_VALUE))
        );
        txtSearchUsernameLayout.setVerticalGroup(
            txtSearchUsernameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(txtSearchUsernameLayout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addGroup(txtSearchUsernameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtSearchMember, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel7))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                .addGap(34, 34, 34)
                .addComponent(cancelBtn1, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(63, 63, 63))
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

        jPanel4.setBackground(new java.awt.Color(102, 0, 102));
        jPanel4.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 5));

        jLabel1.setFont(new java.awt.Font("Century Gothic", 1, 24)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("SCHOOL/FACULTY ID");

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

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("FULL NAME");

        jLabel4.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("CONTACT NUMBER");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(35, 35, 35)
                .addComponent(jLabel1)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(txtContactNumber)
                    .addComponent(jLabel4)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(deleteBtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(addNewBtn))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(updateBtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(saveBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(56, 56, 56)
                        .addComponent(cancelBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 179, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 179, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(txtFullName)
                        .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, 179, Short.MAX_VALUE))
                    .addComponent(txtSchoolFacultyID, javax.swing.GroupLayout.PREFERRED_SIZE, 178, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(cmbCourse, javax.swing.GroupLayout.Alignment.LEADING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(cmbUserType, javax.swing.GroupLayout.Alignment.LEADING, 0, 179, Short.MAX_VALUE)
                        .addComponent(jLabel10, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(cmbYear, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel2))
                .addGap(0, 23, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 12, Short.MAX_VALUE)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(txtSchoolFacultyID, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtFullName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel6)
                .addGap(9, 9, 9)
                .addComponent(cmbUserType, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel10)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cmbCourse, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel11)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cmbYear, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(txtContactNumber, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(29, 29, 29)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(addNewBtn)
                    .addComponent(updateBtn))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(deleteBtn)
                    .addComponent(saveBtn))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cancelBtn)
                .addGap(23, 23, 23))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, 693, Short.MAX_VALUE)
                    .addComponent(txtSearchUsername, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtSearchUsername, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
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
        // Index 0: Hidden Database ID (Primary Key)
        memberId = Integer.parseInt(tblModel.getValueAt(selectedRow, 0).toString());
        
        // Index 1: School/Faculty ID
        Object schoolIdVal = tblModel.getValueAt(selectedRow, 1);
        txtSchoolFacultyID.setText(schoolIdVal != null ? schoolIdVal.toString() : "");
        
        // Index 2: Full Name
        String name = tblModel.getValueAt(selectedRow, 2).toString();
        txtFullName.setText(name);
        
        // Index 3: User Type
        String type = tblModel.getValueAt(selectedRow, 3).toString();
        cmbUserType.setSelectedItem(type);
        
        // Index 4: Course
        String course = tblModel.getValueAt(selectedRow, 4).toString();
        cmbCourse.setSelectedItem(course);
        
        // Index 5: Year
        String year = tblModel.getValueAt(selectedRow, 5).toString();
        cmbYear.setSelectedItem(year);
        
        // Index 6: Contact Number
        Object contactVal = tblModel.getValueAt(selectedRow, 6);
        txtContactNumber.setText(contactVal != null ? contactVal.toString() : "");

        // Faculty logic
        if (type.equalsIgnoreCase("Faculty")) {
            cmbCourse.setEnabled(false);
            cmbYear.setEnabled(false);
        } else {
            cmbCourse.setEnabled(true);
            cmbYear.setEnabled(true);
        }

        // Enable UI for editing
        txtSchoolFacultyID.setEnabled(true);
        txtFullName.setEnabled(true);
        cmbUserType.setEnabled(true);
        txtContactNumber.setEnabled(true);
        
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
        String schoolId = txtSchoolFacultyID.getText().trim();
        String name = txtFullName.getText().trim();
        String type = cmbUserType.getSelectedItem().toString();
        String course = cmbCourse.getSelectedItem().toString();
        String year = cmbYear.getSelectedItem().toString();
        String contact = txtContactNumber.getText().trim();

        // --- SCHOOL ID VALIDATION ---
        if (schoolId.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter your School/Faculty ID.", "Validation Error", JOptionPane.WARNING_MESSAGE);
            txtSchoolFacultyID.requestFocus();
            return;
        }
        if (!schoolId.matches("\\d+")) {
            JOptionPane.showMessageDialog(this, "School/Faculty ID must contain numbers only!", "Input Error", JOptionPane.ERROR_MESSAGE);
            txtSchoolFacultyID.requestFocus();
            return;
        }

        // --- FULL NAME VALIDATION ---
        if (name.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter your Full Name.", "Validation Error", JOptionPane.WARNING_MESSAGE);
            txtFullName.requestFocus();
            return;
        }

        // --- USER TYPE & STUDENT DETAILS VALIDATION ---
        if (type.equals("~Select User Type~")) {
            JOptionPane.showMessageDialog(this, "Please select a User Type.", "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (type.equalsIgnoreCase("Student")) {
            if (course.equals("~Select Course~")) {
                JOptionPane.showMessageDialog(this, "Please select a Course.", "Validation Error", JOptionPane.WARNING_MESSAGE);
                return;
            }
            if (year.equals("~Select Year~")) {
                JOptionPane.showMessageDialog(this, "Please select a Year.", "Validation Error", JOptionPane.WARNING_MESSAGE);
                return;
            }
        } else {
            course = "N/A";
            year = "N/A";
        }

        // --- CONTACT NUMBER VALIDATION ---
        if (contact.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter your Contact Number.", "Validation Error", JOptionPane.WARNING_MESSAGE);
            txtContactNumber.requestFocus();
            return;
        }
        if (!contact.matches("\\d+")) {
            JOptionPane.showMessageDialog(this, "Contact Number must contain numbers only!", "Input Error", JOptionPane.ERROR_MESSAGE);
            txtContactNumber.requestFocus();
            return;
        }

        // --- DATABASE INSERT ---
        try {
            Connection conn = MySQLConnect.getConnection();
            String sql = "INSERT INTO member_records (school_id, fullname, usertype, course, year, contact_number) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement pst = conn.prepareStatement(sql);

            pst.setString(1, schoolId);
            pst.setString(2, name);
            pst.setString(3, type);
            pst.setString(4, course);
            pst.setString(5, year);
            pst.setString(6, contact);

            pst.executeUpdate();
            JOptionPane.showMessageDialog(this, "Member Registered Successfully!");

            populateTable(""); 
            setDefault();      

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }//GEN-LAST:event_saveBtnActionPerformed

    private void deleteBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteBtnActionPerformed
        int row = jTable2.getSelectedRow();
    
    if (row == -1) {
        JOptionPane.showMessageDialog(this, "Please select a member from the table to delete!");
        return;
    }

    String id = jTable2.getValueAt(row, 0).toString();
    String name = txtFullName.getText();

    int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete " + name + "?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
    
    if (confirm == JOptionPane.YES_OPTION) {
        try {
            Connection conn = MySQLConnect.getConnection();
            String sql = "DELETE FROM member_records WHERE id=?";
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1, id);
            
            pst.executeUpdate();
            JOptionPane.showMessageDialog(this, "Member Deleted Successfully!");

            // Refresh the table and lock the fields
            populateTable(""); 
            clearFields(); 
            
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Delete Error: " + e.getMessage());
        }
    }
    }//GEN-LAST:event_deleteBtnActionPerformed

    private void updateBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_updateBtnActionPerformed
        int row = jTable2.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Please select a member from the table to update!");
            return;
        }

        // Capture fields from UI
        String id = jTable2.getValueAt(row, 0).toString(); // The hidden primary key ID
        String schoolId = txtSchoolFacultyID.getText().trim();
        String name = txtFullName.getText().trim();
        String type = cmbUserType.getSelectedItem().toString();
        String course = cmbCourse.getSelectedItem().toString();
        String year = cmbYear.getSelectedItem().toString();
        String contact = txtContactNumber.getText().trim();

        // --- SCHOOL ID VALIDATION ---
        if (schoolId.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter your School/Faculty ID.", "Validation Error", JOptionPane.WARNING_MESSAGE);
            txtSchoolFacultyID.requestFocus();
            return;
        }
        if (!schoolId.matches("\\d+")) {
            JOptionPane.showMessageDialog(this, "Invalid ID! School/Faculty ID must contain numbers only.", "Input Error", JOptionPane.ERROR_MESSAGE);
            txtSchoolFacultyID.requestFocus();
            return;
        }

        // --- FULL NAME VALIDATION ---
        if (name.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter your Full Name.", "Validation Error", JOptionPane.WARNING_MESSAGE);
            txtFullName.requestFocus();
            return;
        }

        // --- USER TYPE & STUDENT DETAILS VALIDATION ---
        if (type.equals("~Select User Type~")) {
            JOptionPane.showMessageDialog(this, "Please select a User Type.", "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (type.equalsIgnoreCase("Student")) {
            if (course.equals("~Select Course~")) {
                JOptionPane.showMessageDialog(this, "Please select a Course.", "Validation Error", JOptionPane.WARNING_MESSAGE);
                return;
            }
            if (year.equals("~Select Year~")) {
                JOptionPane.showMessageDialog(this, "Please select a Year.", "Validation Error", JOptionPane.WARNING_MESSAGE);
                return;
            }
        } else {
            course = "N/A";
            year = "N/A";
        }

        // --- CONTACT NUMBER VALIDATION ---
        if (contact.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter your Contact Number.", "Validation Error", JOptionPane.WARNING_MESSAGE);
            txtContactNumber.requestFocus();
            return;
        }
        if (!contact.matches("\\d+")) {
            JOptionPane.showMessageDialog(this, "Invalid Contact Number! Please use numbers only.", "Input Error", JOptionPane.ERROR_MESSAGE);
            txtContactNumber.requestFocus();
            return;
        }

        // --- DATABASE UPDATE ---
        try {
            Connection conn = MySQLConnect.getConnection();

            // Check for duplicate names (excluding the current record being updated)
            String checkSql = "SELECT * FROM member_records WHERE fullname = ? AND id != ?";
            PreparedStatement checkPst = conn.prepareStatement(checkSql);
            checkPst.setString(1, name);
            checkPst.setString(2, id);
            ResultSet rs = checkPst.executeQuery();

            if (rs.next()) {
                JOptionPane.showMessageDialog(this, "Error: The name '" + name + "' is already used by another member!", "Duplicate Entry", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Updated SQL to include all numeric and text fields
            String sql = "UPDATE member_records SET school_id=?, fullname=?, usertype=?, course=?, year=?, contact_number=? WHERE id=?";
            PreparedStatement pst = conn.prepareStatement(sql);

            pst.setString(1, schoolId);
            pst.setString(2, name);
            pst.setString(3, type);
            pst.setString(4, course);
            pst.setString(5, year);
            pst.setString(6, contact);
            pst.setString(7, id); // Use the row ID for the WHERE clause

            pst.executeUpdate();
            JOptionPane.showMessageDialog(this, "Member Updated Successfully!");

            populateTable(""); 
            clearFields();     

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Update Error: " + e.getMessage());
        }
    }//GEN-LAST:event_updateBtnActionPerformed

    private void addNewBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addNewBtnActionPerformed
        // 1. Clear all text and reset combo boxes
        txtSchoolFacultyID.setText("");
        txtFullName.setText("");

        cmbUserType.setSelectedIndex(0); 
        cmbCourse.setSelectedIndex(0);   
        cmbYear.setSelectedIndex(0);     

        // 3. Enable the fields so the user can type
        txtSchoolFacultyID.setEnabled(true);
        txtFullName.setEnabled(true);
        txtContactNumber.setEnabled(true);
        cmbUserType.setEnabled(true);
        cmbCourse.setEnabled(true);
        cmbYear.setEnabled(true);

        // 4. Button Management
        saveBtn.setEnabled(true);    // Enable Save so they can submit later
        addNewBtn.setEnabled(false); // Disable Add New while entry is in progress
        updateBtn.setEnabled(false); 
        deleteBtn.setEnabled(false);

        // 5. Set focus to the first field
        txtSchoolFacultyID.requestFocus();
    }//GEN-LAST:event_addNewBtnActionPerformed

    private void cmbCourseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbCourseActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cmbCourseActionPerformed

    private void cmbYearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbYearActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cmbYearActionPerformed

    private void cancelBtn1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelBtn1ActionPerformed
        this.dispose();
        borrow_management w = new borrow_management();
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
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable jTable2;
    private javax.swing.JButton saveBtn;
    private javax.swing.JTextField txtContactNumber;
    private javax.swing.JTextField txtFullName;
    private javax.swing.JTextField txtSchoolFacultyID;
    private javax.swing.JTextField txtSearchMember;
    private javax.swing.JPanel txtSearchUsername;
    private javax.swing.JButton updateBtn;
    // End of variables declaration//GEN-END:variables
}
