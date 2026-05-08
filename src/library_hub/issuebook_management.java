package library_hub;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;


public class issuebook_management extends javax.swing.JFrame {
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(issuebook_management.class.getName());

    public issuebook_management() {
        initComponents();
        
        // Apply color rendering to the Status column (Index 7)
        jTableIssuedBooks.getColumnModel().getColumn(7).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                String status = (value != null) ? value.toString() : "";

                if (status.equalsIgnoreCase("Overdue")) {
                    c.setForeground(Color.RED);
                    c.setFont(c.getFont().deriveFont(Font.BOLD));
                } else if (status.equalsIgnoreCase("Returned")) {
                    c.setForeground(new Color(0, 153, 0)); // Dark Green
                    c.setFont(c.getFont().deriveFont(Font.BOLD));
                } else if (status.equalsIgnoreCase("Issued")) {
                    c.setForeground(Color.BLUE);
                } else {
                    c.setForeground(Color.BLACK);
                }

                if (isSelected) {
                    c.setBackground(table.getSelectionBackground());
                } else {
                    c.setBackground(table.getBackground());
                }
                return c;
            }
        });

        populateIssuedTable("");
    }
    
    public void populateIssuedTable(String query) {
    DefaultTableModel model = (DefaultTableModel) jTableIssuedBooks.getModel();
    model.setRowCount(0);

    String selectedStatusFilter = cmbStatus.getSelectedItem().toString();
    java.text.SimpleDateFormat displayFormat = new java.text.SimpleDateFormat("MM/dd/yyyy HH:mm");
    java.time.LocalDateTime now = java.time.LocalDateTime.now();

    try {
        Connection conn = MySQLConnect.getConnection();
        // Updated SQL to include contact_no
        StringBuilder sql = new StringBuilder("SELECT * FROM issued_books WHERE (fullname LIKE ? OR book_title LIKE ?) ");
        
        if (selectedStatusFilter.equalsIgnoreCase("Unreturned")) {
            sql.append(" AND (status = 'Issued' OR status = 'Overdue') ");
        } else if (selectedStatusFilter.equalsIgnoreCase("Returned")) {
            sql.append(" AND status = 'Returned' ");
        } else if (selectedStatusFilter.equalsIgnoreCase("Lost")) {
            sql.append(" AND status = 'Lost' ");
        } else if (selectedStatusFilter.equalsIgnoreCase("Damage") || selectedStatusFilter.equalsIgnoreCase("Damaged")) {
            sql.append(" AND (status = 'Damage' OR status = 'Damaged') ");
        } else if (selectedStatusFilter.equalsIgnoreCase("Issued")) {
            sql.append(" AND status = 'Issued' AND due_date > NOW() ");
        } else if (selectedStatusFilter.equalsIgnoreCase("Overdue")) {
            sql.append(" AND (status = 'Overdue' OR (status = 'Issued' AND due_date < NOW())) ");
        }
        
        sql.append(" ORDER BY issue_date DESC");

        PreparedStatement pst = conn.prepareStatement(sql.toString());
        pst.setString(1, "%" + query + "%");
        pst.setString(2, "%" + query + "%");
        
        ResultSet rs = pst.executeQuery();
        
        while (rs.next()) {
            int issueId = rs.getInt("issue_id");
            String dbStatus = rs.getString("status");
            String contactNo = rs.getString("contact_no"); // NEW: Get contact number from DB
            java.sql.Timestamp issueTs = rs.getTimestamp("issue_date");
            java.sql.Timestamp dueTs = rs.getTimestamp("due_date");
            String penaltyDisplay = rs.getString("penalty_paid");
            String displayStatus = dbStatus;
            
            String issueDateStr = (issueTs != null) ? displayFormat.format(issueTs) : "";
            String dueDateStr = (dueTs != null) ? displayFormat.format(dueTs) : "";

            // AUTO-OVERDUE CALCULATION LOGIC
            if (dbStatus.equalsIgnoreCase("Issued") && dueTs != null) {
                java.time.LocalDateTime dueDate = dueTs.toLocalDateTime();
                
                if (now.isAfter(dueDate)) {
                    displayStatus = "Overdue";
                    
                    long businessDaysLate = 0;
                    java.time.LocalDateTime tempDate = dueDate;

                    while (tempDate.isBefore(now)) {
                        tempDate = tempDate.plusDays(1);
                        java.time.DayOfWeek day = tempDate.getDayOfWeek();
                        if (day != java.time.DayOfWeek.SATURDAY && day != java.time.DayOfWeek.SUNDAY) {
                            businessDaysLate++;
                        }
                    }
                    
                    long calculatedPenalty = businessDaysLate * 100; 
                    penaltyDisplay = String.valueOf(calculatedPenalty);
                    
                    String updateSql = "UPDATE issued_books SET penalty_paid = ?, status = 'Overdue' WHERE issue_id = ? AND status = 'Issued'";
                    PreparedStatement pstUpdate = conn.prepareStatement(updateSql);
                    pstUpdate.setString(1, penaltyDisplay);
                    pstUpdate.setInt(2, issueId);
                    pstUpdate.executeUpdate();
                }
            }
            
            // Added contactNo to the row array at Index 2
            Object[] row = {
                issueId,
                rs.getString("fullname"),
                contactNo,           // NEW: This fills the 'Contact No' column
                rs.getString("book_title"),
                rs.getString("book_acq_no"),
                issueDateStr, 
                dueDateStr,
                penaltyDisplay,
                displayStatus 
            };
            model.addRow(row);
        }
        
    } catch (SQLException e) {
        JOptionPane.showMessageDialog(null, "Error populating table: " + e.getMessage());
    }
}
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel3 = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        jButton5 = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTableIssuedBooks = new javax.swing.JTable();
        txtSearchMember = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        btnMarkAsReturned = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        cmbStatus = new javax.swing.JComboBox<>();
        close = new javax.swing.JButton();
        btnMarkAsDamage = new javax.swing.JButton();
        btnMarkAsLost = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel3.setBackground(new java.awt.Color(153, 0, 153));
        jPanel3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 5));
        jPanel3.setPreferredSize(new java.awt.Dimension(285, 53));

        jLabel8.setFont(new java.awt.Font("Century Gothic", 1, 24)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(255, 255, 255));
        jLabel8.setText("ISSUE BOOK MANAGEMENT");

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

        jPanel4.setBackground(new java.awt.Color(153, 0, 153));
        jPanel4.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 5));
        jPanel4.setPreferredSize(new java.awt.Dimension(285, 53));

        jTableIssuedBooks.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "Id", "Member Name", "Contact No", "Book Title", "Acq No", "Issued Date", "Due Date", "Penalty", "Status", "Select"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Boolean.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false, true
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(jTableIssuedBooks);

        txtSearchMember.addActionListener(this::txtSearchMemberActionPerformed);
        txtSearchMember.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtSearchMemberKeyReleased(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Search Member Name:");

        btnMarkAsReturned.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnMarkAsReturned.setForeground(new java.awt.Color(0, 0, 0));
        btnMarkAsReturned.setText("MARK AS RETURNED");
        btnMarkAsReturned.addActionListener(this::btnMarkAsReturnedActionPerformed);

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("STATUS");

        cmbStatus.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "All", "Issued", "Overdue", "Returned", "Unreturned" }));
        cmbStatus.addActionListener(this::cmbStatusActionPerformed);

        close.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        close.setText("x");
        close.addActionListener(this::closeActionPerformed);

        btnMarkAsDamage.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnMarkAsDamage.setForeground(new java.awt.Color(0, 0, 0));
        btnMarkAsDamage.setText("MARK AS DAMAGE");
        btnMarkAsDamage.addActionListener(this::btnMarkAsDamageActionPerformed);

        btnMarkAsLost.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnMarkAsLost.setForeground(new java.awt.Color(0, 0, 0));
        btnMarkAsLost.setText("MARK AS LOST");
        btnMarkAsLost.addActionListener(this::btnMarkAsLostActionPerformed);

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(btnMarkAsReturned)
                        .addGap(18, 18, 18)
                        .addComponent(btnMarkAsDamage)
                        .addGap(18, 18, 18)
                        .addComponent(btnMarkAsLost)
                        .addContainerGap())
                    .addComponent(jScrollPane1)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtSearchMember, javax.swing.GroupLayout.PREFERRED_SIZE, 164, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(close)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(cmbStatus, javax.swing.GroupLayout.PREFERRED_SIZE, 162, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 711, Short.MAX_VALUE))))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(45, 45, 45)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtSearchMember, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1)
                    .addComponent(jLabel2)
                    .addComponent(cmbStatus, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(close))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 32, Short.MAX_VALUE)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnMarkAsLost)
                    .addComponent(btnMarkAsDamage)
                    .addComponent(btnMarkAsReturned))
                .addGap(26, 26, 26))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, 1324, Short.MAX_VALUE)
            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, 1324, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, 377, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        this.dispose();
        frontpage w = new frontpage();
        w.setVisible(true);
    }//GEN-LAST:event_jButton5ActionPerformed

    private void btnMarkAsReturnedActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMarkAsReturnedActionPerformed
        int row = jTableIssuedBooks.getSelectedRow();

    if (row == -1) {
        JOptionPane.showMessageDialog(this, "Please select a record from the table first!");
        return;
    }

    DefaultTableModel model = (DefaultTableModel) jTableIssuedBooks.getModel();
    
    // Safety check for Status (Index 8)
    Object statusObj = model.getValueAt(row, 8);
    String currentStatus = (statusObj == null) ? "" : statusObj.toString();

    // 1. VALIDATION BLOCK
    if (currentStatus.equalsIgnoreCase("Returned")) {
        JOptionPane.showMessageDialog(this, "This book has already been returned!");
        return;
    }
    
    if (currentStatus.equalsIgnoreCase("Lost") || currentStatus.equalsIgnoreCase("Damage")) {
        JOptionPane.showMessageDialog(this, "This book is marked as " + currentStatus + ". Please settle in Penalty Management.");
        return;
    }

    // 2. DATA INITIALIZATION
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm");
    LocalDateTime now = LocalDateTime.now();
    
    try {
        int issueId = Integer.parseInt(model.getValueAt(row, 0).toString());
        String memberName = model.getValueAt(row, 1).toString().trim();
        String bookTitle = model.getValueAt(row, 3).toString();
        String acqNo = model.getValueAt(row, 4).toString();
        String issueDateStr = model.getValueAt(row, 5).toString();
        String dueDateStr = model.getValueAt(row, 6).toString();

        if (dueDateStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Error: Due Date is missing for this record.");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this, "Confirm return for Book: " + bookTitle + " (Acq No: " + acqNo + ")?", "Confirm Return", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            Connection conn = MySQLConnect.getConnection();

            // Fetch Member Details for the report
            String memberSql = "SELECT course, year, usertype FROM member_records WHERE LOWER(TRIM(fullname)) = LOWER(TRIM(?))";
            PreparedStatement pstMem = conn.prepareStatement(memberSql);
            pstMem.setString(1, memberName); 
            ResultSet rsMem = pstMem.executeQuery();
            
            String course = "N/A", year = "N/A", utype = "Member"; 
            if (rsMem.next()) {
                course = rsMem.getString("course");
                year = rsMem.getString("year");
                utype = rsMem.getString("usertype");
            }

            // 3. PENALTY CALCULATION
            LocalDateTime dueDate = LocalDateTime.parse(dueDateStr, formatter);
            LocalDateTime issueDate = LocalDateTime.parse(issueDateStr, formatter);
            double calculatedPenalty = 0;

            if (!utype.equalsIgnoreCase("Faculty")) {
                if (now.isAfter(dueDate)) {
                    long businessDaysLate = 0;
                    LocalDateTime tempDate = dueDate;
                    while (tempDate.isBefore(now)) {
                        tempDate = tempDate.plusDays(1);
                        // Only count Monday-Friday
                        if (tempDate.getDayOfWeek() != java.time.DayOfWeek.SATURDAY && tempDate.getDayOfWeek() != java.time.DayOfWeek.SUNDAY) {
                            businessDaysLate++;
                        }
                    }
                    calculatedPenalty = businessDaysLate * 100.0; 
                }
            }

            // Compare with existing penalty in the table (Index 7)
            Object penObj = model.getValueAt(row, 7);
            double existingPenalty = (penObj == null) ? 0.0 : Double.parseDouble(penObj.toString());
            double finalPenalty = Math.max(existingPenalty, calculatedPenalty);

            // Fetch Author
            String getAuthorSql = "SELECT author FROM books WHERE acquisition_no = ?";
            PreparedStatement pstAuth = conn.prepareStatement(getAuthorSql);
            pstAuth.setString(1, acqNo);
            ResultSet rsAuth = pstAuth.executeQuery();
            String authorName = rsAuth.next() ? rsAuth.getString("author") : "Unknown";

            // 4. DATABASE UPDATES
            
            // A. Add to issue_report (This makes it appear in History of Payment)
            String insertReportSql = "INSERT INTO issue_report (fullname, usertype, course, year, book_acq_no, book_title, author, issue_date, due_date, actual_return_date, penalty_paid, status) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, 'Returned')";
            PreparedStatement pstReport = conn.prepareStatement(insertReportSql);
            pstReport.setString(1, memberName); 
            pstReport.setString(2, utype);   
            pstReport.setString(3, course);  
            pstReport.setString(4, year);    
            pstReport.setString(5, acqNo);
            pstReport.setString(6, bookTitle);
            pstReport.setString(7, authorName); 
            pstReport.setTimestamp(8, java.sql.Timestamp.valueOf(issueDate)); 
            pstReport.setTimestamp(9, java.sql.Timestamp.valueOf(dueDate));
            pstReport.setTimestamp(10, java.sql.Timestamp.valueOf(now)); 
            pstReport.setDouble(11, finalPenalty); 
            pstReport.executeUpdate();

            // B. Update the original issued record status to 'Returned'
            String updateIssuedSql = "UPDATE issued_books SET status = 'Returned', penalty_paid = ? WHERE issue_id = ?";
            PreparedStatement pstUpdateIssued = conn.prepareStatement(updateIssuedSql);
            pstUpdateIssued.setDouble(1, finalPenalty);
            pstUpdateIssued.setInt(2, issueId);
            pstUpdateIssued.executeUpdate();

            // C. Make the book 'Available' in the inventory again
            String updateBookSql = "UPDATE books SET status = 'Available' WHERE acquisition_no = ?";
            PreparedStatement pstUpdateBook = conn.prepareStatement(updateBookSql);
            pstUpdateBook.setString(1, acqNo);
            pstUpdateBook.executeUpdate();

            JOptionPane.showMessageDialog(this, "Book successfully returned!\nStatus updated to Returned.\nPenalty Settle: ₱" + finalPenalty);
            
            // Refresh the table
            populateIssuedTable(""); 
            
        }
    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, "Error processing return: " + e.getMessage());
        e.printStackTrace();
    }
    }//GEN-LAST:event_btnMarkAsReturnedActionPerformed

    private void txtSearchMemberActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtSearchMemberActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtSearchMemberActionPerformed

    private void txtSearchMemberKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSearchMemberKeyReleased
        populateIssuedTable(txtSearchMember.getText());
    }//GEN-LAST:event_txtSearchMemberKeyReleased

    private void cmbStatusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbStatusActionPerformed
        populateIssuedTable(txtSearchMember.getText());
        
    }//GEN-LAST:event_cmbStatusActionPerformed

    private void closeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_closeActionPerformed
        txtSearchMember.setText("");
        cmbStatus.setSelectedIndex(0); // Reset to "All"
    }//GEN-LAST:event_closeActionPerformed

    private void btnMarkAsDamageActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMarkAsDamageActionPerformed
        int row = jTableIssuedBooks.getSelectedRow();

    if (row == -1) {
        JOptionPane.showMessageDialog(this, "Please select a record from the table first!");
        return;
    }

    DefaultTableModel model = (DefaultTableModel) jTableIssuedBooks.getModel();
    
    // Safety check for Status (Index 8)
    Object statusObj = model.getValueAt(row, 8);
    String currentStatus = (statusObj == null) ? "" : statusObj.toString();

    if (currentStatus.equalsIgnoreCase("Returned")) {
        JOptionPane.showMessageDialog(this, "This book has already been returned!");
        return;
    }

    // Get Acq No (Index 4) to find the book in the 'books' table
    String acqNo = model.getValueAt(row, 4).toString();
    String bookTitle = model.getValueAt(row, 3).toString();
    double bookPrice = 0.0;

    try {
        Connection conn = MySQLConnect.getConnection();
        
        // 1. FETCH THE PRICE FROM THE BOOKS TABLE
        String priceSql = "SELECT price FROM books WHERE acquisition_no = ?";
        PreparedStatement pstPrice = conn.prepareStatement(priceSql);
        pstPrice.setString(1, acqNo);
        ResultSet rsPrice = pstPrice.executeQuery();

        if (rsPrice.next()) {
            bookPrice = rsPrice.getDouble("price");
        }

        // 2. FALLBACK LOGIC: If price is 0 or null in DB, set a standard fee
        if (bookPrice <= 0) {
            bookPrice = 500.00; // Standard damage fee if price is missing
        }

        // 3. CONFIRMATION DIALOG
        int confirm = JOptionPane.showConfirmDialog(this, 
            "Mark Book '" + bookTitle + "' as DAMAGED?\nDamage Fee (Book Price): ₱" + bookPrice, 
            "Confirm Damage Report", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            // Update issued_books status and set penalty
            int issueId = Integer.parseInt(model.getValueAt(row, 0).toString());
            String updateIssuedSql = "UPDATE issued_books SET status = 'Damage', penalty_paid = ? WHERE issue_id = ?";
            PreparedStatement pstUpdate = conn.prepareStatement(updateIssuedSql);
            pstUpdate.setDouble(1, bookPrice);
            pstUpdate.setInt(2, issueId);
            pstUpdate.executeUpdate();

            // Update books inventory status
            String updateBookSql = "UPDATE books SET status = 'Damaged' WHERE acquisition_no = ?";
            PreparedStatement pstBook = conn.prepareStatement(updateBookSql);
            pstBook.setString(1, acqNo);
            pstBook.executeUpdate();

            JOptionPane.showMessageDialog(this, "Book marked as Damaged. Fee of ₱" + bookPrice + " recorded.");
            populateIssuedTable(""); // Refresh your table
        }

    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, "Error processing damage report: " + e.getMessage());
        e.printStackTrace();
    }
    }//GEN-LAST:event_btnMarkAsDamageActionPerformed

    private void btnMarkAsLostActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMarkAsLostActionPerformed
        int row = jTableIssuedBooks.getSelectedRow();

    if (row == -1) {
        JOptionPane.showMessageDialog(this, "Please select a record from the table first!");
        return;
    }

    DefaultTableModel model = (DefaultTableModel) jTableIssuedBooks.getModel();
    
    // Status Check (Index 8)
    Object statusObj = model.getValueAt(row, 8);
    String currentStatus = (statusObj == null) ? "" : statusObj.toString();

    if (currentStatus.equalsIgnoreCase("Returned")) {
        JOptionPane.showMessageDialog(this, "This book has already been returned!");
        return;
    }
    
    if (currentStatus.equalsIgnoreCase("Lost")) {
        JOptionPane.showMessageDialog(this, "This book is already marked as lost.");
        return;
    }

    // Get Data for DB update
    int issueId = Integer.parseInt(model.getValueAt(row, 0).toString());
    String acqNo = model.getValueAt(row, 4).toString(); // Index 4 for Acq No
    String bookTitle = model.getValueAt(row, 3).toString(); // Index 3 for Title
    double lostFee = 0.0;

    try {
        Connection conn = MySQLConnect.getConnection();
        
        // 1. FETCH THE BOOK PRICE
        String priceSql = "SELECT price FROM books WHERE acquisition_no = ?";
        PreparedStatement pstPrice = conn.prepareStatement(priceSql);
        pstPrice.setString(1, acqNo);
        ResultSet rsPrice = pstPrice.executeQuery();

        if (rsPrice.next()) {
            lostFee = rsPrice.getDouble("price");
        }

        // 2. FALLBACK: If price is 0, set a standard replacement fee (e.g., ₱1000)
        if (lostFee <= 0) {
            lostFee = 1000.00; 
        }

        // 3. CONFIRMATION
        int confirm = JOptionPane.showConfirmDialog(this, 
            "Mark Book '" + bookTitle + "' as LOST?\nReplacement Fee (Book Price): ₱" + lostFee, 
            "Confirm Lost Report", JOptionPane.YES_NO_OPTION, JOptionPane.ERROR_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            // Update issued_books status to 'Lost'
            String updateIssuedSql = "UPDATE issued_books SET status = 'Lost', penalty_paid = ? WHERE issue_id = ?";
            PreparedStatement pstUpdate = conn.prepareStatement(updateIssuedSql);
            pstUpdate.setDouble(1, lostFee);
            pstUpdate.setInt(2, issueId);
            pstUpdate.executeUpdate();

            // Update books inventory status to 'Lost'
            String updateBookSql = "UPDATE books SET status = 'Lost' WHERE acquisition_no = ?";
            PreparedStatement pstBook = conn.prepareStatement(updateBookSql);
            pstBook.setString(1, acqNo);
            pstBook.executeUpdate();

            JOptionPane.showMessageDialog(this, "Book marked as LOST. Penalty of ₱" + lostFee + " applied.");
            populateIssuedTable(""); // Refresh table
        }

    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, "Error processing lost report: " + e.getMessage());
        e.printStackTrace();
    }
    }//GEN-LAST:event_btnMarkAsLostActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception ex) { logger.log(java.util.logging.Level.SEVERE, null, ex); }
        java.awt.EventQueue.invokeLater(() -> new issuebook_management().setVisible(true));
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
        java.awt.EventQueue.invokeLater(() -> new issuebook_management().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnMarkAsDamage;
    private javax.swing.JButton btnMarkAsLost;
    private javax.swing.JButton btnMarkAsReturned;
    private javax.swing.JButton close;
    private javax.swing.JComboBox<String> cmbStatus;
    private javax.swing.JButton jButton5;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTableIssuedBooks;
    private javax.swing.JTextField txtSearchMember;
    // End of variables declaration//GEN-END:variables
}
