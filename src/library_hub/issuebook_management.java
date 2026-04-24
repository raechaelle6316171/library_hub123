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

    String selectedStatusFilter = sort.getSelectedItem().toString();
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm");
    LocalDateTime now = LocalDateTime.now();

    try {
        Connection conn = MySQLConnect.getConnection();
        StringBuilder sql = new StringBuilder("SELECT * FROM issued_books WHERE (fullname LIKE ? OR book_title LIKE ?) ");
        
        if (selectedStatusFilter.equalsIgnoreCase("Returned")) {
            sql.append(" AND status = 'Returned' ");
        } else if (selectedStatusFilter.equalsIgnoreCase("Issued")) {
            sql.append(" AND status = 'Issued' AND STR_TO_DATE(due_date, '%m/%d/%Y %H:%i') > NOW() ");
        } else if (selectedStatusFilter.equalsIgnoreCase("Overdue")) {
            sql.append(" AND status = 'Issued' AND STR_TO_DATE(due_date, '%m/%d/%Y %H:%i') < NOW() ");
        }

        sql.append(" ORDER BY issue_date DESC");

        PreparedStatement pst = conn.prepareStatement(sql.toString());
        pst.setString(1, "%" + query + "%");
        pst.setString(2, "%" + query + "%");
        
        ResultSet rs = pst.executeQuery();
        
        while (rs.next()) {
            String dbStatus = rs.getString("status");
            String dueDateStr = rs.getString("due_date");
            String penaltyDisplay = rs.getString("penalty_paid");
            String displayStatus = dbStatus;

            if (dbStatus.equalsIgnoreCase("Issued") && dueDateStr != null) {
                try {
                    LocalDateTime dueDate = LocalDateTime.parse(dueDateStr, formatter);
                    if (now.isAfter(dueDate)) {
                        displayStatus = "Overdue";
                        
                        // --- UPDATED PENALTY CALCULATION (NO WEEKENDS) ---
                        long businessDaysLate = 0;
                        LocalDateTime tempDate = dueDate;

                        // Loop from the due date until the current time
                        while (tempDate.isBefore(now)) {
                            tempDate = tempDate.plusDays(1);
                            
                            // Get the day of the week
                            java.time.DayOfWeek day = tempDate.getDayOfWeek();
                            
                            // Only count if it is NOT Saturday and NOT Sunday
                            if (day != java.time.DayOfWeek.SATURDAY && day != java.time.DayOfWeek.SUNDAY) {
                                businessDaysLate++;
                            }
                        }
                        
                        long calculatedPenalty = businessDaysLate * 100; 
                        penaltyDisplay = String.valueOf(calculatedPenalty);
                    }
                } catch (Exception e) {
                    displayStatus = dbStatus;
                }
            }
            
            Object[] row = {
                rs.getInt("issue_id"),
                rs.getString("fullname"),
                rs.getString("book_title"),
                rs.getString("book_acq_no"),
                rs.getString("issue_date"),
                dueDateStr,
                penaltyDisplay,
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

        jPanel3 = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        jButton5 = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTableIssuedBooks = new javax.swing.JTable();
        txtSearchMember = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        btnMarkAsReturned = new javax.swing.JButton();
        deleteBtn = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        sort = new javax.swing.JComboBox<>();

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
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 551, Short.MAX_VALUE)
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
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null}
            },
            new String [] {
                "Id", "Member Name", "Book Title", "Acq No", "Issued Date", "Due Date", "Penalty Paid", "Status"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false
            };

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

        deleteBtn.setBackground(new java.awt.Color(255, 102, 102));
        deleteBtn.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        deleteBtn.setForeground(new java.awt.Color(0, 0, 0));
        deleteBtn.setText("DELETE");
        deleteBtn.addActionListener(this::deleteBtnActionPerformed);

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("STATUS");

        sort.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "All", "Issued", "Overdue", "Returned" }));
        sort.addActionListener(this::sortActionPerformed);

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addContainerGap(24, Short.MAX_VALUE)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtSearchMember, javax.swing.GroupLayout.PREFERRED_SIZE, 164, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(sort, javax.swing.GroupLayout.PREFERRED_SIZE, 162, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addGroup(jPanel4Layout.createSequentialGroup()
                            .addComponent(deleteBtn)
                            .addGap(18, 18, 18)
                            .addComponent(btnMarkAsReturned))
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 855, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(22, 22, 22))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(39, 39, 39)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtSearchMember, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1)
                    .addComponent(jLabel2)
                    .addComponent(sort, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 33, Short.MAX_VALUE)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(deleteBtn)
                    .addComponent(btnMarkAsReturned))
                .addGap(20, 20, 20))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, 911, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, 911, javax.swing.GroupLayout.PREFERRED_SIZE)
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

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm");
    LocalDateTime now = LocalDateTime.now();
    String nowStr = now.format(formatter);

    DefaultTableModel model = (DefaultTableModel) jTableIssuedBooks.getModel();

    int issueId = Integer.parseInt(model.getValueAt(row, 0).toString());
    String acqNo = model.getValueAt(row, 3).toString();
    String dueDateStr = model.getValueAt(row, 5).toString();
    String currentStatus = model.getValueAt(row, 6).toString();

    // Prevent returning books that are already returned
    if (currentStatus.equalsIgnoreCase("Returned")) {
        JOptionPane.showMessageDialog(this, "This book has already been returned!");
        return;
    }

    int confirm = JOptionPane.showConfirmDialog(this, "Confirm return for Book Acq No: " + acqNo + "?", "Confirm Return", JOptionPane.YES_NO_OPTION);

    if (confirm == JOptionPane.YES_OPTION) {
        try {
            Connection conn = MySQLConnect.getConnection();

            // Fetch Member Details
            String memberSql = "SELECT course, year, usertype FROM member_records WHERE LOWER(TRIM(fullname)) = LOWER(TRIM(?))";
            PreparedStatement pstMem = conn.prepareStatement(memberSql);
            String searchName = model.getValueAt(row, 1).toString().trim();
            pstMem.setString(1, searchName); 
            ResultSet rsMem = pstMem.executeQuery();

            String course = "N/A", year = "N/A", utype = "Member"; 
            if (rsMem.next()) {
                course = rsMem.getString("course");
                year = rsMem.getString("year");
                utype = rsMem.getString("usertype");
            }

            // Calculate Penalty if Overdue
            LocalDateTime dueDate = LocalDateTime.parse(dueDateStr, formatter);
            long penalty = 0;

            if (!utype.equalsIgnoreCase("Faculty")) {
                if (now.isAfter(dueDate)) {
                    long daysLate = java.time.Duration.between(dueDate, now).toDays();
                    daysLate = (daysLate < 1) ? 1 : daysLate + 1; // Ensure at least 1 day if hours passed

                    penalty = daysLate * 100; 
                    JOptionPane.showMessageDialog(this, "OVERDUE DETECTED!\nDays Late: " + daysLate + "\nPenalty Collected: ₱" + penalty);
                }
            }

            // Fetch Author
            String getAuthorSql = "SELECT author FROM books WHERE acquisition_no = ?";
            PreparedStatement pstAuth = conn.prepareStatement(getAuthorSql);
            pstAuth.setString(1, acqNo);
            ResultSet rsAuth = pstAuth.executeQuery();
            String authorName = rsAuth.next() ? rsAuth.getString("author") : "Unknown";

            // 1. Insert into Report Table
            String insertReportSql = "INSERT INTO issue_report (fullname, usertype, course, year, book_acq_no, book_title, author, issue_date, due_date, actual_return_date, penalty_paid, status) " +
                                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, 'Returned')";
            PreparedStatement pstReport = conn.prepareStatement(insertReportSql);
            pstReport.setString(1, model.getValueAt(row, 1).toString()); 
            pstReport.setString(2, utype);   
            pstReport.setString(3, course);  
            pstReport.setString(4, year);    
            pstReport.setString(5, acqNo);
            pstReport.setString(6, model.getValueAt(row, 2).toString()); 
            pstReport.setString(7, authorName); 
            pstReport.setString(8, model.getValueAt(row, 4).toString()); 
            pstReport.setString(9, dueDateStr);
            pstReport.setString(10, nowStr); 
            pstReport.setString(11, String.valueOf(penalty)); 
            pstReport.executeUpdate();

            // 2. Update Issued Books table (Set to Returned)
            String updateActiveSql = "UPDATE issued_books SET status = 'Returned', penalty_paid = ? WHERE issue_id = ?";
            PreparedStatement pstUpdateActive = conn.prepareStatement(updateActiveSql);
            pstUpdateActive.setString(1, String.valueOf(penalty)); 
            pstUpdateActive.setInt(2, issueId);
            pstUpdateActive.executeUpdate();

            // 3. Update Book Stock Status
            String updateBookSql = "UPDATE books SET status = 'Available' WHERE acquisition_no = ?";
            PreparedStatement pstBook = conn.prepareStatement(updateBookSql);
            pstBook.setString(1, acqNo);
            pstBook.executeUpdate();

            JOptionPane.showMessageDialog(this, "Book successfully returned and archived!");
            populateIssuedTable(""); // Refresh the table

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error during return: " + e.getMessage());
            e.printStackTrace();
        }
    }
    }//GEN-LAST:event_btnMarkAsReturnedActionPerformed

    private void deleteBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteBtnActionPerformed
        int row = jTableIssuedBooks.getSelectedRow();
    
    if (row == -1) {
        JOptionPane.showMessageDialog(this, "Please select a record from the table to delete.");
        return;
    }

    String id = jTableIssuedBooks.getValueAt(row, 0).toString();
    String memberName = jTableIssuedBooks.getValueAt(row, 1).toString();

    int confirm = JOptionPane.showConfirmDialog(this, 
            "Are you sure you want to delete the record for " + memberName + "?", 
            "Confirm Deletion", 
            JOptionPane.YES_NO_OPTION, 
            JOptionPane.WARNING_MESSAGE);

    if (confirm == JOptionPane.YES_OPTION) {
        try {
            Connection conn = MySQLConnect.getConnection();
            String sql = "DELETE FROM issued_books WHERE issue_id = ?";
            
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1, id);
            
            int result = pst.executeUpdate();
            
            if (result > 0) {
                JOptionPane.showMessageDialog(this, "Record deleted successfully.");

                populateIssuedTable(""); 
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error deleting record: " + e.getMessage());
        }
    }
    }//GEN-LAST:event_deleteBtnActionPerformed

    private void txtSearchMemberActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtSearchMemberActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtSearchMemberActionPerformed

    private void txtSearchMemberKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSearchMemberKeyReleased
        populateIssuedTable(txtSearchMember.getText());
    }//GEN-LAST:event_txtSearchMemberKeyReleased

    private void sortActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sortActionPerformed
        populateIssuedTable(txtSearchMember.getText());
    }//GEN-LAST:event_sortActionPerformed

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
    private javax.swing.JButton btnMarkAsReturned;
    private javax.swing.JButton deleteBtn;
    private javax.swing.JButton jButton5;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTableIssuedBooks;
    private javax.swing.JComboBox<String> sort;
    private javax.swing.JTextField txtSearchMember;
    // End of variables declaration//GEN-END:variables
}
