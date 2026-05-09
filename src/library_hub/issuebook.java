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

public class issuebook extends javax.swing.JFrame {
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(issuebook.class.getName());

    /**
     * Creates new form issuebook
     */
    public issuebook() {
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
        // JOIN is required to check the 'usertype' from member_records while loading the issued books
        StringBuilder sql = new StringBuilder(
            "SELECT i.*, m.usertype FROM issued_books i " +
            "LEFT JOIN member_records m ON LOWER(TRIM(i.fullname)) = LOWER(TRIM(m.fullname)) " +
            "WHERE (i.fullname LIKE ? OR i.book_title LIKE ?) "
        );
        
        if (selectedStatusFilter.equalsIgnoreCase("Unreturned")) {
            sql.append(" AND (i.status = 'Issued' OR i.status = 'Overdue') ");
        } else if (selectedStatusFilter.equalsIgnoreCase("Returned")) {
            sql.append(" AND i.status = 'Returned' ");
        } else if (selectedStatusFilter.equalsIgnoreCase("Lost")) {
            sql.append(" AND i.status = 'Lost' ");
        } else if (selectedStatusFilter.equalsIgnoreCase("Damaged") || selectedStatusFilter.equalsIgnoreCase("Damaged")) {
            sql.append(" AND (i.status = 'Damaged' OR i.status = 'Damaged') ");
        } else if (selectedStatusFilter.equalsIgnoreCase("Issued")) {
            sql.append(" AND i.status = 'Issued' AND i.due_date > NOW() ");
        } else if (selectedStatusFilter.equalsIgnoreCase("Overdue")) {
            sql.append(" AND (i.status = 'Overdue' OR (i.status = 'Issued' AND i.due_date < NOW())) ");
        }
        
        sql.append(" ORDER BY i.issue_date DESC");

        PreparedStatement pst = conn.prepareStatement(sql.toString());
        pst.setString(1, "%" + query + "%");
        pst.setString(2, "%" + query + "%");
        
        ResultSet rs = pst.executeQuery();
        
        while (rs.next()) {
            int issueId = rs.getInt("issue_id");
            String dbStatus = rs.getString("status");
            String contactNo = rs.getString("contact_no");
            String usertype = rs.getString("usertype"); 
            java.sql.Timestamp issueTs = rs.getTimestamp("issue_date");
            java.sql.Timestamp dueTs = rs.getTimestamp("due_date");
            
            double storedPenalty = rs.getDouble("penalty_paid");
            String displayStatus = dbStatus;
            
            String issueDateStr = (issueTs != null) ? displayFormat.format(issueTs) : "";
            String dueDateStr = (dueTs != null) ? displayFormat.format(dueTs) : "";

            // AUTO-OVERDUE CALCULATION
            if (dbStatus.equalsIgnoreCase("Issued") && dueTs != null) {
                java.time.LocalDateTime dueDate = dueTs.toLocalDateTime();
                
                if (now.isAfter(dueDate)) {
                    displayStatus = "Overdue";
                    long businessDaysLate = 0;
                    java.time.LocalDateTime tempDate = dueDate;

                    while (tempDate.isBefore(now)) {
                        tempDate = tempDate.plusDays(1);
                        if (tempDate.getDayOfWeek() != java.time.DayOfWeek.SATURDAY && 
                            tempDate.getDayOfWeek() != java.time.DayOfWeek.SUNDAY) {
                            businessDaysLate++;
                        }
                    }
                    
                    storedPenalty = businessDaysLate * 100.0;
                    
                    // Update Database for non-faculty or general records
                    String updateSql = "UPDATE issued_books SET penalty_paid = ?, status = 'Overdue' WHERE issue_id = ? AND status = 'Issued'";
                    PreparedStatement pstUpdate = conn.prepareStatement(updateSql);
                    pstUpdate.setDouble(1, storedPenalty);
                    pstUpdate.setInt(2, issueId);
                    pstUpdate.executeUpdate();
                }
            }

            // CRITICAL FIX: FORCE 0.00 FOR FACULTY OVERDUE
            // This ensures that even if the database says 200.00, the UI shows 0.00
            double finalPenaltyValue = storedPenalty;
            if ("Faculty".equalsIgnoreCase(usertype) && 
               (displayStatus.equalsIgnoreCase("Overdue") || displayStatus.equalsIgnoreCase("Issued"))) {
                finalPenaltyValue = 0.00;
            }
            
            Object[] row = {
                issueId,
                rs.getString("fullname"),
                contactNo,
                rs.getString("book_title"),
                rs.getString("book_acq_no"),
                issueDateStr, 
                dueDateStr,
                finalPenaltyValue, // This variable now carries the forced 0.00 rule
                displayStatus 
            };
            model.addRow(row);
        }
        
    } catch (SQLException e) {
        JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
    }
}    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel4 = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        jButton10 = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTableIssuedBooks = new javax.swing.JTable();
        cmbStatus = new javax.swing.JComboBox<>();
        close = new javax.swing.JButton();
        txtSearchMember = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        btnMarkAsReturned = new javax.swing.JButton();
        btnMarkAsDamage = new javax.swing.JButton();
        btnMarkAsLost = new javax.swing.JButton();
        jButton6 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel4.setBackground(new java.awt.Color(67, 83, 189));
        jPanel4.setForeground(new java.awt.Color(0, 102, 0));
        jPanel4.setPreferredSize(new java.awt.Dimension(285, 53));

        jLabel9.setFont(new java.awt.Font("Lucida Fax", 1, 24)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(0, 102, 0));

        jLabel21.setFont(new java.awt.Font("Lucida Fax", 1, 36)); // NOI18N
        jLabel21.setForeground(new java.awt.Color(255, 255, 255));
        jLabel21.setText("ISSUE BOOK MANAGEMENT");

        jButton10.setIcon(new javax.swing.ImageIcon(getClass().getResource("/library_hub/cancel.png"))); // NOI18N
        jButton10.addActionListener(this::jButton10ActionPerformed);

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap(545, Short.MAX_VALUE)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                        .addComponent(jLabel9)
                        .addGap(239, 239, 239))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                        .addComponent(jLabel21)
                        .addGap(420, 420, 420)
                        .addComponent(jButton10, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jButton10, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel21)
                    .addComponent(jLabel9))
                .addContainerGap())
        );

        jPanel2.setBackground(new java.awt.Color(67, 83, 189));

        jTableIssuedBooks.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "Id", "Member Name", "Contact No", "Book Title", "Acq No", "Issued Date", "Due Date", "Penalty", "Status"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(jTableIssuedBooks);

        cmbStatus.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        cmbStatus.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "All", "Issued", "Overdue", "Returned", "Damaged", "Lost", "Unreturned" }));
        cmbStatus.addActionListener(this::cmbStatusActionPerformed);

        close.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        close.setText("x");
        close.addActionListener(this::closeActionPerformed);

        txtSearchMember.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        txtSearchMember.addActionListener(this::txtSearchMemberActionPerformed);
        txtSearchMember.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtSearchMemberKeyReleased(evt);
            }
        });

        jLabel4.setFont(new java.awt.Font("Trebuchet MS", 1, 24)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("SEARCH");

        jLabel5.setFont(new java.awt.Font("Trebuchet MS", 1, 24)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setText("STATUS");

        btnMarkAsReturned.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        btnMarkAsReturned.setForeground(new java.awt.Color(0, 0, 0));
        btnMarkAsReturned.setText("MARK AS RETURNED");
        btnMarkAsReturned.addActionListener(this::btnMarkAsReturnedActionPerformed);

        btnMarkAsDamage.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        btnMarkAsDamage.setForeground(new java.awt.Color(0, 0, 0));
        btnMarkAsDamage.setText("MARK AS DAMAGE");
        btnMarkAsDamage.addActionListener(this::btnMarkAsDamageActionPerformed);

        btnMarkAsLost.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        btnMarkAsLost.setForeground(new java.awt.Color(0, 0, 0));
        btnMarkAsLost.setText("MARK AS LOST");
        btnMarkAsLost.addActionListener(this::btnMarkAsLostActionPerformed);

        jButton6.setText("x");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtSearchMember, javax.swing.GroupLayout.PREFERRED_SIZE, 301, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(close, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cmbStatus, javax.swing.GroupLayout.PREFERRED_SIZE, 365, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(btnMarkAsReturned)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnMarkAsDamage)
                        .addGap(18, 18, 18)
                        .addComponent(btnMarkAsLost)))
                .addContainerGap())
            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel2Layout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(jButton6)
                    .addGap(0, 749, Short.MAX_VALUE)))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(64, 64, 64)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addComponent(close, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(6, 6, 6))
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(txtSearchMember, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(cmbStatus, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel4)
                        .addComponent(jLabel5)))
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnMarkAsLost, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnMarkAsDamage, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnMarkAsReturned, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(85, Short.MAX_VALUE))
            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel2Layout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(jButton6)
                    .addGap(0, 0, Short.MAX_VALUE)))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, 1525, Short.MAX_VALUE)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void cmbStatusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbStatusActionPerformed
         populateIssuedTable(txtSearchMember.getText());
    }//GEN-LAST:event_cmbStatusActionPerformed

    private void closeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_closeActionPerformed
       txtSearchMember.setText("");
        cmbStatus.setSelectedIndex(0); // Reset to "All"
    }//GEN-LAST:event_closeActionPerformed

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

        // Note: We allow "Lost" or "Damage" to proceed if they are clicking "Mark as Returned"
        // to finalize the record, but the penalty logic below ensures Faculty don't pay for "Overdue".
        if (currentStatus.equalsIgnoreCase("Lost") || currentStatus.equalsIgnoreCase("Damaged")) {
            int settleConfirm = JOptionPane.showConfirmDialog(this,
                "This book is marked as " + currentStatus + ". Finalizing return will record the penalty. Proceed?",
                "Settle Penalty", JOptionPane.YES_NO_OPTION);
            if (settleConfirm != JOptionPane.YES_OPTION) return;
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

                // Fetch Member Details
                String memberSql = "SELECT school_id, contact_number, course, year, usertype FROM member_records WHERE LOWER(TRIM(fullname)) = LOWER(TRIM(?))";
                PreparedStatement pstMem = conn.prepareStatement(memberSql);
                pstMem.setString(1, memberName);
                ResultSet rsMem = pstMem.executeQuery();

                String schoolId = "N/A", contactNo = "N/A", course = "N/A", year = "N/A", utype = "Member";
                if (rsMem.next()) {
                    schoolId = rsMem.getString("school_id");
                    contactNo = rsMem.getString("contact_number");
                    course = rsMem.getString("course");
                    year = rsMem.getString("year");
                    utype = rsMem.getString("usertype");
                }

                // 3. PENALTY CALCULATION
                LocalDateTime dueDate = LocalDateTime.parse(dueDateStr, formatter);
                double calculatedPenalty = 0;

                // OVERDUE CHECK: Only for non-faculty members
                if (!utype.equalsIgnoreCase("Faculty")) {
                    if (now.isAfter(dueDate)) {
                        long businessDaysLate = 0;
                        LocalDateTime tempDate = dueDate;
                        while (tempDate.isBefore(now)) {
                            tempDate = tempDate.plusDays(1);
                            if (tempDate.getDayOfWeek() != java.time.DayOfWeek.SATURDAY &&
                                tempDate.getDayOfWeek() != java.time.DayOfWeek.SUNDAY) {
                                businessDaysLate++;
                            }
                        }
                        calculatedPenalty = businessDaysLate * 100.0;
                    }
                }

                // Get existing penalty (from Mark as Lost/Damage) from column index 7
                Object penObj = model.getValueAt(row, 7);
                double existingPenalty = (penObj == null) ? 0.0 : Double.parseDouble(penObj.toString());

                // FINAL PENALTY LOGIC:
                // Faculty only pay existing (Lost/Damage) penalties.
                // Students pay the higher between existing and overdue fines.
                double finalPenalty;
                if (utype.equalsIgnoreCase("Faculty")) {
                    finalPenalty = existingPenalty;
                } else {
                    finalPenalty = Math.max(existingPenalty, calculatedPenalty);
                }

                // Fetch Author
                String getAuthorSql = "SELECT author FROM books WHERE acquisition_no = ?";
                PreparedStatement pstAuth = conn.prepareStatement(getAuthorSql);
                pstAuth.setString(1, acqNo);
                ResultSet rsAuth = pstAuth.executeQuery();
                String authorName = rsAuth.next() ? rsAuth.getString("author") : "Unknown";

                // 4. DATABASE UPDATES

                // A. Add to issue_report
                String insertReportSql = "INSERT INTO issue_report (school_id, fullname, usertype, contact_no, course, year, book_acq_no, book_title, author, issue_date, due_date, actual_return_date, penalty_paid, status) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, 'Returned')";
                PreparedStatement pstReport = conn.prepareStatement(insertReportSql);
                pstReport.setString(1, schoolId);
                pstReport.setString(2, memberName);
                pstReport.setString(3, utype);
                pstReport.setString(4, contactNo);
                pstReport.setString(5, course);
                pstReport.setString(6, year);
                pstReport.setString(7, acqNo);
                pstReport.setString(8, bookTitle);
                pstReport.setString(9, authorName);
                pstReport.setTimestamp(10, java.sql.Timestamp.valueOf(LocalDateTime.parse(issueDateStr, formatter)));
                pstReport.setTimestamp(11, java.sql.Timestamp.valueOf(dueDate));
                pstReport.setTimestamp(12, java.sql.Timestamp.valueOf(now));
                pstReport.setDouble(13, finalPenalty);
                pstReport.executeUpdate();

                // B. Update the original issued record status
                String updateIssuedSql = "UPDATE issued_books SET status = 'Returned', penalty_paid = ? WHERE issue_id = ?";
                PreparedStatement pstUpdateIssued = conn.prepareStatement(updateIssuedSql);
                pstUpdateIssued.setDouble(1, finalPenalty);
                pstUpdateIssued.setInt(2, issueId);
                pstUpdateIssued.executeUpdate();

                // C. Make the book 'Available' again
                String updateBookSql = "UPDATE books SET status = 'Available' WHERE acquisition_no = ?";
                PreparedStatement pstUpdateBook = conn.prepareStatement(updateBookSql);
                pstUpdateBook.setString(1, acqNo);
                pstUpdateBook.executeUpdate();

                JOptionPane.showMessageDialog(this, "Book successfully returned!\nStatus updated to Returned.\nPenalty Settle: ₱" + finalPenalty);

                // Refresh table
                populateIssuedTable("");

            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error processing return: " + e.getMessage());
            e.printStackTrace();
        }
    }//GEN-LAST:event_btnMarkAsReturnedActionPerformed

    private void btnMarkAsDamageActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMarkAsDamageActionPerformed
        int row = jTableIssuedBooks.getSelectedRow();

        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Please select a record from the table first!");
            return;
        }

        DefaultTableModel model = (DefaultTableModel) jTableIssuedBooks.getModel();

        // Check if already returned
        Object statusObj = model.getValueAt(row, 8);
        String currentStatus = (statusObj == null) ? "" : statusObj.toString();

        if (currentStatus.equalsIgnoreCase("Returned")) {
            JOptionPane.showMessageDialog(this, "This book has already been returned!");
            return;
        }

        // 1. GET ALL NECESSARY DATA FROM THE SELECTED ROW
        int issueId = Integer.parseInt(model.getValueAt(row, 0).toString());
        String fullName = model.getValueAt(row, 1).toString();
        String contactNo = model.getValueAt(row, 2).toString();
        String bookTitle = model.getValueAt(row, 3).toString();
        String acqNo = model.getValueAt(row, 4).toString();
        String issueDateStr = model.getValueAt(row, 5).toString();
        String dueDateStr = model.getValueAt(row, 6).toString();

        double bookPrice = 0.0;
        String schoolId = "";
        String userType = "";
        String course = "N/A";
        String year = "N/A";
        String author = "";

        try {
            Connection conn = MySQLConnect.getConnection();

            // 2. FETCH MISSING DATA (School ID, UserType, Author, Price) FROM DB
            // Fetch User Info
            String userSql = "SELECT school_id, usertype, course, year FROM member_records WHERE fullname = ?";
            PreparedStatement pstUser = conn.prepareStatement(userSql);
            pstUser.setString(1, fullName);
            ResultSet rsUser = pstUser.executeQuery();
            if (rsUser.next()) {
                schoolId = rsUser.getString("school_id");
                userType = rsUser.getString("usertype");
                course = rsUser.getString("course");
                year = rsUser.getString("year");
            }

            // Fetch Book Info
            String bookSql = "SELECT author, price FROM books WHERE acquisition_no = ?";
            PreparedStatement pstBookInfo = conn.prepareStatement(bookSql);
            pstBookInfo.setString(1, acqNo);
            ResultSet rsBook = pstBookInfo.executeQuery();
            if (rsBook.next()) {
                author = rsBook.getString("author");
                bookPrice = rsBook.getDouble("price");
            }

            if (bookPrice <= 0) bookPrice = 500.00; // Fallback fee

            // 3. CONFIRMATION
            int confirm = JOptionPane.showConfirmDialog(this,
                "Mark '" + bookTitle + "' as DAMAGED?\nFee: ₱" + bookPrice,
                "Confirm Damage Report", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

            if (confirm == JOptionPane.YES_OPTION) {
                // 4. UPDATE ACTIVE TRANSACTION
                String updateIssuedSql = "UPDATE issued_books SET status = 'Damaged', penalty_paid = ? WHERE issue_id = ?";
                PreparedStatement pstUpdate = conn.prepareStatement(updateIssuedSql);
                pstUpdate.setDouble(1, bookPrice);
                pstUpdate.setInt(2, issueId);
                pstUpdate.executeUpdate();

                // 5. UPDATE INVENTORY
                String updateInvSql = "UPDATE books SET status = 'Damaged' WHERE acquisition_no = ?";
                PreparedStatement pstInv = conn.prepareStatement(updateInvSql);
                pstInv.setString(1, acqNo);
                pstInv.executeUpdate();

                // 6. LOG TO ISSUE REPORT (MATCHING YOUR 15 COLUMNS)
                // Note: actual_return_date is set to NOW()
                String reportSql = "INSERT INTO issue_report (school_id, fullname, usertype, contact_no, course, year, " +
                "book_acq_no, book_title, author, issue_date, due_date, actual_return_date, penalty_paid, status) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, NOW(), ?, 'Damaged')";

                PreparedStatement pstReport = conn.prepareStatement(reportSql);
                pstReport.setString(1, schoolId);
                pstReport.setString(2, fullName);
                pstReport.setString(3, userType);
                pstReport.setString(4, contactNo);
                pstReport.setString(5, course);
                pstReport.setString(6, year);
                pstReport.setString(7, acqNo);
                pstReport.setString(8, bookTitle);
                pstReport.setString(9, author);

                // Convert date strings back to Timestamps for the DB
                java.text.SimpleDateFormat format = new java.text.SimpleDateFormat("MM/dd/yyyy HH:mm");
                pstReport.setTimestamp(10, new java.sql.Timestamp(format.parse(issueDateStr).getTime()));
                pstReport.setTimestamp(11, new java.sql.Timestamp(format.parse(dueDateStr).getTime()));

                pstReport.setDouble(12, bookPrice);

                pstReport.executeUpdate();

                JOptionPane.showMessageDialog(this, "Book marked as Damaged and record saved to Issue Report.");
                populateIssuedTable("");
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

        // Check current status before proceeding
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

        // 1. GET DATA FROM THE SELECTED TABLE ROW
        int issueId = Integer.parseInt(model.getValueAt(row, 0).toString());
        String fullName = model.getValueAt(row, 1).toString();
        String contactNo = model.getValueAt(row, 2).toString();
        String bookTitle = model.getValueAt(row, 3).toString();
        String acqNo = model.getValueAt(row, 4).toString();
        String issueDateStr = model.getValueAt(row, 5).toString();
        String dueDateStr = model.getValueAt(row, 6).toString();

        // Variables for database lookups
        double lostFee = 0.0;
        String schoolId = "";
        String userType = "";
        String course = "N/A";
        String year = "N/A";
        String author = "";

        try {
            Connection conn = MySQLConnect.getConnection();

            // 2. FETCH MEMBER DETAILS (Matches your user summary for school_id)
            String userSql = "SELECT school_id, usertype, course, year FROM member_records WHERE fullname = ?";
            PreparedStatement pstUser = conn.prepareStatement(userSql);
            pstUser.setString(1, fullName);
            ResultSet rsUser = pstUser.executeQuery();
            if (rsUser.next()) {
                schoolId = rsUser.getString("school_id");
                userType = rsUser.getString("usertype");
                course = rsUser.getString("course");
                year = rsUser.getString("year");
            }

            // 3. FETCH BOOK PRICE AND AUTHOR
            String bookSql = "SELECT author, price FROM books WHERE acquisition_no = ?";
            PreparedStatement pstBookInfo = conn.prepareStatement(bookSql);
            pstBookInfo.setString(1, acqNo);
            ResultSet rsBook = pstBookInfo.executeQuery();
            if (rsBook.next()) {
                author = rsBook.getString("author");
                lostFee = rsBook.getDouble("price");
            }

            if (lostFee <= 0) lostFee = 1000.00; // Default replacement fee

            // 4. CONFIRMATION DIALOG
            int confirm = JOptionPane.showConfirmDialog(this,
                "Mark Book '" + bookTitle + "' as LOST?\nMember: " + fullName + "\nReplacement Fee: ₱" + lostFee,
                "Confirm Lost Report", JOptionPane.YES_NO_OPTION, JOptionPane.ERROR_MESSAGE);

            if (confirm == JOptionPane.YES_OPTION) {
                // 5. UPDATE ACTIVE TRANSACTION IN issued_books
                String updateIssuedSql = "UPDATE issued_books SET status = 'Lost', penalty_paid = ? WHERE issue_id = ?";
                PreparedStatement pstUpdate = conn.prepareStatement(updateIssuedSql);
                pstUpdate.setDouble(1, lostFee);
                pstUpdate.setInt(2, issueId);
                pstUpdate.executeUpdate();

                // 6. UPDATE BOOK STATUS IN INVENTORY
                String updateInvSql = "UPDATE books SET status = 'Lost' WHERE acquisition_no = ?";
                PreparedStatement pstInv = conn.prepareStatement(updateInvSql);
                pstInv.setString(1, acqNo);
                pstInv.executeUpdate();

                // 7. LOG TO ISSUE REPORT (15-column matching image_1a6a39.png)
                String reportSql = "INSERT INTO issue_report (school_id, fullname, usertype, contact_no, course, year, " +
                "book_acq_no, book_title, author, issue_date, due_date, actual_return_date, penalty_paid, status) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, NOW(), ?, 'Lost')";

                PreparedStatement pstReport = conn.prepareStatement(reportSql);
                pstReport.setString(1, schoolId);
                pstReport.setString(2, fullName);
                pstReport.setString(3, userType);
                pstReport.setString(4, contactNo);
                pstReport.setString(5, course);
                pstReport.setString(6, year);
                pstReport.setString(7, acqNo);
                pstReport.setString(8, bookTitle);
                pstReport.setString(9, author);

                // Format dates for SQL Timestamp
                java.text.SimpleDateFormat format = new java.text.SimpleDateFormat("MM/dd/yyyy HH:mm");
                pstReport.setTimestamp(10, new java.sql.Timestamp(format.parse(issueDateStr).getTime()));
                pstReport.setTimestamp(11, new java.sql.Timestamp(format.parse(dueDateStr).getTime()));

                pstReport.setDouble(12, lostFee);

                // EXECUTE MUST BE ON A SEPARATE LINE TO PREVENT 'VOID' ERROR
                pstReport.executeUpdate();

                JOptionPane.showMessageDialog(this, "Book marked as LOST. Record saved to Issue Report.");
                populateIssuedTable("");
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error processing lost report: " + e.getMessage());
            e.printStackTrace();
        }
    }//GEN-LAST:event_btnMarkAsLostActionPerformed

    private void txtSearchMemberActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtSearchMemberActionPerformed
         populateIssuedTable(txtSearchMember.getText());
    }//GEN-LAST:event_txtSearchMemberActionPerformed

    private void txtSearchMemberKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSearchMemberKeyReleased
        populateIssuedTable(txtSearchMember.getText());
    }//GEN-LAST:event_txtSearchMemberKeyReleased

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
        java.awt.EventQueue.invokeLater(() -> new issuebook().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnMarkAsDamage;
    private javax.swing.JButton btnMarkAsLost;
    private javax.swing.JButton btnMarkAsReturned;
    private javax.swing.JButton close;
    private javax.swing.JComboBox<String> cmbStatus;
    private javax.swing.JButton jButton10;
    private javax.swing.JButton jButton6;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTableIssuedBooks;
    private javax.swing.JTextField txtSearchMember;
    // End of variables declaration//GEN-END:variables
}
