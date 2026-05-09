package library_hub;

import javax.swing.table.DefaultTableModel;
import java.sql.*;
import java.util.Vector;
import javax.swing.JOptionPane;

public class member extends javax.swing.JFrame {
    
    int memberId = -1;

    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(member.class.getName());
    
    private int id;
    private String check;
    
    public member() {
        initComponents();
        MySQLConnect.getConnection(); 
        populateTable();             
        setDefault();
    }

    private void clearFields() {
    // 1. Clear all text
    txtSchoolId.setText("");
    txtFullName.setText("");
    txtContactNumber.setText("");
    
    // 2. Reset all combo boxes
    cmbUserType.setSelectedIndex(0); 
    cmbCourse.setSelectedIndex(0);   
    cmbYear.setSelectedIndex(0);     

    // 3. Disable the fields (This makes them look like the others in your screenshot)
    txtSchoolId.setEnabled(false);
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
    txtSchoolId.setText("");
    txtFullName.setText("");
    txtContactNumber.setText("");
    cmbUserType.setSelectedIndex(0);
    cmbCourse.setSelectedIndex(0);
    cmbYear.setSelectedIndex(0);
    
    txtSchoolId.setEnabled(false);
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
            DefaultTableModel tblModel = (DefaultTableModel) jTable7.getModel();
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
            jTable7.getColumnModel().getColumn(0).setMinWidth(0);
            jTable7.getColumnModel().getColumn(0).setMaxWidth(0);
            jTable7.getColumnModel().getColumn(0).setWidth(0);

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Database Error: " + e.getMessage());
        }
    }

    public void populateTable() {
    try {
        Connection conn = MySQLConnect.getConnection();
        DefaultTableModel tblModel = (DefaultTableModel) jTable7.getModel();
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
        jTable7.getColumnModel().getColumn(0).setMinWidth(0);
        jTable7.getColumnModel().getColumn(0).setMaxWidth(0);
        jTable7.getColumnModel().getColumn(0).setWidth(0);

    } catch (SQLException e) {
        JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
    }
}
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel4 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel23 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        txtSchoolId = new javax.swing.JTextField();
        txtFullName = new javax.swing.JTextField();
        cmbYear = new javax.swing.JComboBox<>();
        addNewBtn = new javax.swing.JButton();
        updateBtn = new javax.swing.JButton();
        saveBtn = new javax.swing.JButton();
        deleteBtn = new javax.swing.JButton();
        cancelBtn = new javax.swing.JButton();
        txtContactNumber = new javax.swing.JTextField();
        cmbUserType = new javax.swing.JComboBox<>();
        cmbCourse = new javax.swing.JComboBox<>();
        jPanel10 = new javax.swing.JPanel();
        jScrollPane7 = new javax.swing.JScrollPane();
        jTable7 = new javax.swing.JTable();
        txtSearchMember = new javax.swing.JTextField();
        jLabel22 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jLabel11 = new javax.swing.JLabel();
        jButton10 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel4.setBackground(new java.awt.Color(67, 83, 189));

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("SCHOOL/FACULTY ID");

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("FULL NAME");

        jLabel6.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setText("USER TYPE");

        jLabel10.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(255, 255, 255));
        jLabel10.setText("COURSE");

        jLabel23.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel23.setForeground(new java.awt.Color(255, 255, 255));
        jLabel23.setText("YEAR");

        jLabel4.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("CONTACT NUMBER");

        txtSchoolId.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N

        txtFullName.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N

        cmbYear.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        cmbYear.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "~Select Year~", "I", "II", "III", "IV" }));
        cmbYear.addActionListener(this::cmbYearActionPerformed);

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

        cancelBtn.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        cancelBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/library_hub/cancel.png"))); // NOI18N
        cancelBtn.setText("CANCEL");
        cancelBtn.addActionListener(this::cancelBtnActionPerformed);

        txtContactNumber.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N

        cmbUserType.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        cmbUserType.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "~Select User Type~", "Student", "Faculty" }));
        cmbUserType.addItemListener(this::cmbUserTypeItemStateChanged);
        cmbUserType.addActionListener(this::cmbUserTypeActionPerformed);

        cmbCourse.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        cmbCourse.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "~Select Course~", "BSIT", "BSCE", "BSCS" }));
        cmbCourse.addActionListener(this::cmbCourseActionPerformed);

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(44, 44, 44)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jLabel4)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel10, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel4Layout.createSequentialGroup()
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jLabel23, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 179, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel6, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 179, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel2))
                                .addGap(0, 0, Short.MAX_VALUE)))
                        .addGap(1340, 1340, 1340))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(cmbYear, javax.swing.GroupLayout.Alignment.LEADING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(addNewBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 133, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(deleteBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 133, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(saveBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 133, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(updateBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 133, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addGap(1310, 1310, 1310))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtContactNumber, javax.swing.GroupLayout.PREFERRED_SIZE, 278, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(txtSchoolId, javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(txtFullName, javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(cmbCourse, javax.swing.GroupLayout.Alignment.LEADING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(cmbUserType, javax.swing.GroupLayout.Alignment.LEADING, 0, 278, Short.MAX_VALUE)
                                .addComponent(jLabel3, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                        .addGap(0, 0, Short.MAX_VALUE))))
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(116, 116, 116)
                .addComponent(cancelBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 133, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtSchoolId, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtFullName, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cmbUserType, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel10)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cmbCourse, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel23)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cmbYear, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtContactNumber, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(addNewBtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(updateBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(saveBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(deleteBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(cancelBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(15, Short.MAX_VALUE))
        );

        jPanel10.setBackground(new java.awt.Color(67, 83, 189));
        jPanel10.setForeground(new java.awt.Color(255, 255, 255));

        jTable7.setModel(new javax.swing.table.DefaultTableModel(
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
        jTable7.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable7MouseClicked(evt);
            }
        });
        jScrollPane7.setViewportView(jTable7);

        txtSearchMember.addActionListener(this::txtSearchMemberActionPerformed);
        txtSearchMember.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtSearchMemberKeyReleased(evt);
            }
        });

        jLabel22.setFont(new java.awt.Font("Trebuchet MS", 1, 24)); // NOI18N
        jLabel22.setForeground(new java.awt.Color(255, 255, 255));
        jLabel22.setText("SEARCH");

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane7, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 1142, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel10Layout.createSequentialGroup()
                        .addComponent(jLabel22)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtSearchMember)))
                .addGap(22, 22, 22))
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addGap(56, 56, 56)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtSearchMember, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel22))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane7, javax.swing.GroupLayout.PREFERRED_SIZE, 517, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(52, Short.MAX_VALUE))
        );

        jPanel2.setBackground(new java.awt.Color(67, 83, 189));

        jLabel11.setFont(new java.awt.Font("Lucida Fax", 1, 36)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(255, 255, 255));
        jLabel11.setText("REGISTER MEMBER");

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
                    .addComponent(jButton10, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel11))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, 350, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(jPanel2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void cmbYearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbYearActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cmbYearActionPerformed

    private void cmbCourseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbCourseActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cmbCourseActionPerformed

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

    private void cmbUserTypeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbUserTypeActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cmbUserTypeActionPerformed

    private void addNewBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addNewBtnActionPerformed
        // 1. Clear all text and reset combo boxes
        txtSchoolId.setText("");
        txtFullName.setText("");

        cmbUserType.setSelectedIndex(0);
        cmbCourse.setSelectedIndex(0);
        cmbYear.setSelectedIndex(0);

        // 3. Enable the fields so the user can type
        txtSchoolId.setEnabled(true);
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
        txtSchoolId.requestFocus();
    }//GEN-LAST:event_addNewBtnActionPerformed

    private void updateBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_updateBtnActionPerformed
        int row = jTable7.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Please select a member from the table to update!");
            return;
        }

        // Capture fields from UI
        String id = jTable7.getValueAt(row, 0).toString(); // The hidden primary key ID
        String schoolId = txtSchoolId.getText().trim();
        String name = txtFullName.getText().trim();
        String type = cmbUserType.getSelectedItem().toString();
        String course = cmbCourse.getSelectedItem().toString();
        String year = cmbYear.getSelectedItem().toString();
        String contact = txtContactNumber.getText().trim();

        // --- SCHOOL ID VALIDATION ---
        if (schoolId.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter your School/Faculty ID.", "Validation Error", JOptionPane.WARNING_MESSAGE);
            txtSchoolId.requestFocus();
            return;
        }
        if (!schoolId.matches("\\d+")) {
            JOptionPane.showMessageDialog(this, "Invalid ID! School/Faculty ID must contain numbers only.", "Input Error", JOptionPane.ERROR_MESSAGE);
            txtSchoolId.requestFocus();
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

    private void saveBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveBtnActionPerformed
        // 1. Capture inputs
        String schoolId = txtSchoolId.getText().trim();
        String fullName = txtFullName.getText().trim();
        String contactNo = txtContactNumber.getText().trim();
        String userType = cmbUserType.getSelectedItem().toString();
        String course = cmbCourse.getSelectedItem().toString();
        String year = cmbYear.getSelectedItem().toString();

        // 2. Basic Validations
        if (schoolId.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter your School/Faculty ID.");
            txtSchoolId.requestFocus();
            return;
        }

        if (!schoolId.matches("\\d+")) {
            JOptionPane.showMessageDialog(this, "Invalid ID! School/Faculty ID must contain numbers only.", "Input Error", JOptionPane.ERROR_MESSAGE);
            txtSchoolId.requestFocus();
            return;
        }

        if (fullName.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter your full name");
            txtFullName.requestFocus();
            return;
        }

        if (userType.equals("~Select User Type~")) {
            JOptionPane.showMessageDialog(this, "Please select a user type");
            cmbUserType.requestFocus();
            return;
        }

        // 3. Conditional Validations: Only check Course/Year if it's a Student
        if (userType.equalsIgnoreCase("Student")) {
            if (course.equals("~Select Course~")) {
                JOptionPane.showMessageDialog(this, "Please select a course");
                cmbCourse.requestFocus();
                return;
            }

            if (year.equals("~Select Year~")) {
                JOptionPane.showMessageDialog(this, "Please select a year");
                cmbYear.requestFocus();
                return;
            }
        } else {
            // If Faculty, set these to "N/A" for the database
            course = "N/A";
            year = "N/A";
        }

        if (contactNo.isEmpty() || contactNo.equals("09")) {
            JOptionPane.showMessageDialog(this, "Please enter your contact number");
            txtContactNumber.requestFocus();
            return;
        }

        // 4. Database Logic
        try {
            Connection conn = MySQLConnect.getConnection();

            // Duplicate check for ID to prevent primary key errors
            String checkSql = "SELECT * FROM member_records WHERE school_id = ?";
            PreparedStatement checkPst = conn.prepareStatement(checkSql);
            checkPst.setString(1, schoolId);
            ResultSet rs = checkPst.executeQuery();

            if (rs.next()) {
                JOptionPane.showMessageDialog(this, "This School/Faculty ID is already registered!");
                return;
            }

            // Insert new member record
            String sql = "INSERT INTO member_records (school_id, fullname, usertype, course, year, contact_number) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1, schoolId);
            pst.setString(2, fullName);
            pst.setString(3, userType);
            pst.setString(4, course);
            pst.setString(5, year);
            pst.setString(6, contactNo);

            pst.executeUpdate();

            // Success Message
            JOptionPane.showMessageDialog(this, "Member Registered Successfully");

            // Refresh UI components
            populateTable();
            clearFields();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }//GEN-LAST:event_saveBtnActionPerformed

    private void deleteBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteBtnActionPerformed
        int row = jTable7.getSelectedRow();

        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Please select a member from the table to delete!");
            return;
        }

        String id = jTable7.getValueAt(row, 0).toString();
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

    private void cancelBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelBtnActionPerformed
        setDefault();
        jTable7.clearSelection();
    }//GEN-LAST:event_cancelBtnActionPerformed

    private void txtSearchMemberKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSearchMemberKeyReleased
        populateTable(txtSearchMember.getText());
    }//GEN-LAST:event_txtSearchMemberKeyReleased

    private void txtSearchMemberActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtSearchMemberActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtSearchMemberActionPerformed

    private void jTable7MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable7MouseClicked
        DefaultTableModel tblModel = (DefaultTableModel) jTable7.getModel();
        int selectedRow = jTable7.getSelectedRow();

        if (selectedRow != -1) {
            // Index 0: Hidden Database ID (Primary Key)
            memberId = Integer.parseInt(tblModel.getValueAt(selectedRow, 0).toString());

            // Index 1: School/Faculty ID
            Object schoolIdVal = tblModel.getValueAt(selectedRow, 1);
            txtSchoolId.setText(schoolIdVal != null ? schoolIdVal.toString() : "");

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
            txtSchoolId.setEnabled(true);
            txtFullName.setEnabled(true);
            cmbUserType.setEnabled(true);
            txtContactNumber.setEnabled(true);

            updateBtn.setEnabled(true);
            deleteBtn.setEnabled(true);
            cancelBtn.setEnabled(true);
            addNewBtn.setEnabled(false);
            saveBtn.setEnabled(false);
        }
    }//GEN-LAST:event_jTable7MouseClicked

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
        java.awt.EventQueue.invokeLater(() -> new member().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addNewBtn;
    private javax.swing.JButton cancelBtn;
    private javax.swing.JComboBox<String> cmbCourse;
    private javax.swing.JComboBox<String> cmbUserType;
    private javax.swing.JComboBox<String> cmbYear;
    private javax.swing.JButton deleteBtn;
    private javax.swing.JButton jButton10;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JTable jTable7;
    private javax.swing.JButton saveBtn;
    private javax.swing.JTextField txtContactNumber;
    private javax.swing.JTextField txtFullName;
    private javax.swing.JTextField txtSchoolId;
    private javax.swing.JTextField txtSearchMember;
    private javax.swing.JButton updateBtn;
    // End of variables declaration//GEN-END:variables
}
