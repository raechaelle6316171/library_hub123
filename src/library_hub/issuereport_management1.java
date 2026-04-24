package library_hub;

import java.sql.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import javax.swing.RowFilter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.DayOfWeek;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.FileOutputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import javax.swing.JFileChooser;

public class issuereport_management1 extends javax.swing.JFrame {
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(issuereport_management1.class.getName());

    public issuereport_management1() {
        initComponents();
        populateIssuedTable(""); 
        updateTotalPaid();
        updateTotalUnpaid();
    }
    
    
    public void populateIssuedTable(String query) {
    DefaultTableModel model = (DefaultTableModel) jTableIssueReport.getModel();
    model.setRowCount(0);

    try {
        Connection conn = MySQLConnect.getConnection();

       String sql = "SELECT * FROM issue_report WHERE fullname LIKE ? OR book_title LIKE ? ORDER BY id DESC";

        PreparedStatement pst = conn.prepareStatement(sql);
        String search = "%" + query + "%";
        pst.setString(1, search);
        pst.setString(2, search);
        
        ResultSet rs = pst.executeQuery();
        
        while (rs.next()) {
            Object[] row = {
                rs.getInt("id"),               
                rs.getString("fullname"),
                rs.getString("usertype"),
                rs.getString("course"),
                rs.getString("year"),                         
                rs.getString("book_acq_no"),
                rs.getString("book_title"),
                rs.getString("author"),
                rs.getString("issue_date"),
                rs.getString("due_date"),
                rs.getString("actual_return_date"), 
                rs.getString("penalty_paid"),            
                rs.getString("status")
            };
            model.addRow(row);
        }
    } catch (SQLException e) {
        JOptionPane.showMessageDialog(null, "Report Error: " + e.getMessage());
    }
    calculateTotalUnpaid();
}
    
    public void calculateTotalUnpaid() {
    double totalUnpaid = 0.0;
    
    // Iterate through every row in the JTable
    for (int i = 0; i < jTableIssueReport.getRowCount(); i++) {
        try {
            // Get the Status (Index 7) and Penalty (Index 6)
            String status = jTableIssueReport.getValueAt(i, 7).toString();
            Object penaltyValue = jTableIssueReport.getValueAt(i, 6);
            
            // Only sum up if the status is "Overdue"
            if (status.equalsIgnoreCase("Overdue") && penaltyValue != null) {
                double amount = Double.parseDouble(penaltyValue.toString());
                totalUnpaid += amount;
            }
        } catch (NumberFormatException e) {
            // Skip rows with invalid numbers
        }
    }
    
    // Update your "TOTAL UNPAID" text field
    // Change 'txtTotalUnpaid' to your actual variable name
    txtTotalUnpaid.setText(String.format("%.2f", totalUnpaid));
}

    public void calculateTotalPaid() {
    double total = 0;
    
    // Iterate through every row in the table
    for (int i = 0; i < jTableIssueReport.getRowCount(); i++) {
        try {
            // Get the value from the 'Penalty Paid' column (Index 11)
            Object value = jTableIssueReport.getValueAt(i, 11);
            
            if (value != null) {
                // Convert the object/string to a double
                double amount = Double.parseDouble(value.toString());
                total += amount;
            }
        } catch (NumberFormatException e) {
            // Skip rows that don't have a valid number
        }
    }
    
    // Set the result to your Total Paid text field
    // Replace 'txtTotalPaid' with the actual variable name of your text field
    txtTotalPaid.setText(String.format("%.2f", total));
}
    
    public void updateTotalUnpaid() {
    double totalUnpaid = 0;
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm");
    LocalDateTime now = LocalDateTime.now();

    try {
        Connection conn = MySQLConnect.getConnection();
        // We only care about books that are NOT 'Returned'
        String sql = "SELECT due_date FROM issued_books WHERE status != 'Returned'";
        PreparedStatement pst = conn.prepareStatement(sql);
        ResultSet rs = pst.executeQuery();

        while (rs.next()) {
            String dueDateStr = rs.getString("due_date");
            if (dueDateStr != null) {
                try {
                    LocalDateTime dueDate = LocalDateTime.parse(dueDateStr, formatter);
                    if (now.isAfter(dueDate)) {
                        long businessDaysLate = 0;
                        LocalDateTime tempDate = dueDate;

                        // Weekend Skip Logic
                        while (tempDate.isBefore(now)) {
                            tempDate = tempDate.plusDays(1);
                            java.time.DayOfWeek day = tempDate.getDayOfWeek();
                            if (day != java.time.DayOfWeek.SATURDAY && day != java.time.DayOfWeek.SUNDAY) {
                                businessDaysLate++;
                            }
                        }
                        totalUnpaid += (businessDaysLate * 100);
                    }
                } catch (Exception e) { /* Skip formatting errors */ }
            }
        }
        // Change 'txtTotalUnpaid' to your actual Variable Name for the white box
        txtTotalUnpaid.setText(String.format("%.2f", totalUnpaid));
        
    } catch (SQLException e) {
        System.out.println("Error: " + e.getMessage());
    }
}
    
    public void updateTotalPaid() {
    double total = 0.0;
    // Index 11 is the 'Penalty Paid' column
    int penaltyColumnIndex = 11; 

    for (int i = 0; i < jTableIssueReport.getRowCount(); i++) {
        try {
            Object value = jTableIssueReport.getValueAt(i, penaltyColumnIndex);
            if (value != null) {
                // Remove any spaces and convert to a number
                double amount = Double.parseDouble(value.toString().trim());
                total += amount;
            }
        } catch (NumberFormatException e) {
            // This skips rows that don't have a valid number
        }
    }
    // Display the sum with two decimal places
    txtTotalPaid.setText(String.format("%.2f", total));
}
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel3 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTableIssueReport = new javax.swing.JTable();
        deleteBtn = new javax.swing.JButton();
        printBtn = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        txtSearch = new javax.swing.JTextField();
        jPanel5 = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        txtTotalUnpaid = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jPanel7 = new javax.swing.JPanel();
        jPanel8 = new javax.swing.JPanel();
        txtTotalPaid = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        jButton6 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel3.setBackground(new java.awt.Color(102, 51, 0));
        jPanel3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 5));
        jPanel3.setPreferredSize(new java.awt.Dimension(285, 53));

        jTableIssueReport.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "Id", "Member Name", "User Type", "Course", "Year", "Acq No", "Book Title", "Author", "Issued Date", "Due Date", "Actual Return Date", "Penalty Paid", "Status"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(jTableIssueReport);

        deleteBtn.setBackground(new java.awt.Color(255, 51, 51));
        deleteBtn.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        deleteBtn.setText("DELETE");
        deleteBtn.addActionListener(this::deleteBtnActionPerformed);

        printBtn.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        printBtn.setText("PRINT TO PDF");
        printBtn.addActionListener(this::printBtnActionPerformed);

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Search");

        txtSearch.addActionListener(this::txtSearchActionPerformed);
        txtSearch.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtSearchKeyReleased(evt);
            }
        });

        jPanel5.setBackground(new java.awt.Color(255, 255, 255));

        jPanel6.setBackground(new java.awt.Color(255, 102, 102));

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 14, Short.MAX_VALUE)
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );

        txtTotalUnpaid.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel2.setText("TOTAL UNPAID");

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtTotalUnpaid, javax.swing.GroupLayout.PREFERRED_SIZE, 139, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2))
                .addGap(0, 8, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtTotalUnpaid, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jPanel7.setBackground(new java.awt.Color(255, 255, 255));

        jPanel8.setBackground(new java.awt.Color(153, 153, 255));

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 15, Short.MAX_VALUE)
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );

        txtTotalPaid.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel3.setText("TOTAL PAID");

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtTotalPaid, javax.swing.GroupLayout.PREFERRED_SIZE, 139, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 113, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 7, Short.MAX_VALUE))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtTotalPaid, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 164, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(227, 227, 227)
                        .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(120, 120, 120)
                        .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 472, Short.MAX_VALUE)))
                .addContainerGap())
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(566, 566, 566)
                .addComponent(deleteBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(printBtn)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(15, 15, 15)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jPanel7, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jPanel5, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 29, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel1)
                            .addComponent(txtSearch, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)))
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 319, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(29, 29, 29)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(deleteBtn)
                    .addComponent(printBtn))
                .addGap(43, 43, 43))
        );

        jPanel4.setBackground(new java.awt.Color(102, 51, 0));
        jPanel4.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 5));
        jPanel4.setPreferredSize(new java.awt.Dimension(285, 53));

        jLabel9.setFont(new java.awt.Font("Century Gothic", 1, 24)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(255, 255, 255));
        jLabel9.setText("ISSUE REPORT");

        jButton6.setText("x");
        jButton6.addActionListener(this::jButton6ActionPerformed);

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel9)
                .addGap(603, 603, 603)
                .addComponent(jButton6)
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel9)
                    .addComponent(jButton6))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, 1403, Short.MAX_VALUE)
            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, 1403, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, 572, Short.MAX_VALUE))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void txtSearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtSearchActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtSearchActionPerformed

    private void txtSearchKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSearchKeyReleased
        String searchStr = txtSearch.getText();
        DefaultTableModel model = (DefaultTableModel) jTableIssueReport.getModel();
        TableRowSorter<DefaultTableModel> trs = new TableRowSorter<>(model);
        jTableIssueReport.setRowSorter(trs);

        trs.setRowFilter(RowFilter.regexFilter("(?i)" + searchStr, 1));
    }//GEN-LAST:event_txtSearchKeyReleased

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        this.dispose();
        frontpage w = new frontpage();
        w.setVisible(true);
    }//GEN-LAST:event_jButton6ActionPerformed

    private void deleteBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteBtnActionPerformed
        int row = jTableIssueReport.getSelectedRow();

    if (row == -1) {
        JOptionPane.showMessageDialog(this, "Select a report record to delete!");
        return;
    }

    String reportId = jTableIssueReport.getValueAt(row, 0).toString();

    int confirm = JOptionPane.showConfirmDialog(this, "Permanently delete report ID: " + reportId + "?", "Warning", JOptionPane.YES_NO_OPTION);

    if (confirm == JOptionPane.YES_OPTION) {
        try {
            Connection conn = MySQLConnect.getConnection();
            
            String sql = "DELETE FROM issue_report WHERE id = ?"; 
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1, reportId);

            pst.executeUpdate();
            JOptionPane.showMessageDialog(this, "Report entry removed.");
            populateIssuedTable(""); 
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error deleting report: " + e.getMessage());
        }
    }
    }//GEN-LAST:event_deleteBtnActionPerformed

    private void printBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_printBtnActionPerformed
        try {
    // 1. Get selected row index
    int row = jTableIssueReport.getSelectedRow();
    if (row == -1) {
        javax.swing.JOptionPane.showMessageDialog(null, "Please select a record from the table first!");
        return;
    }

    // 2. Set path to desktop for easy access
    String memberName = jTableIssueReport.getValueAt(row, 1).toString().replace(" ", "_");
    String fileName = memberName + "_Receipt.pdf";
    String path = System.getProperty("user.home") + "/Desktop/" + fileName;

    // 3. Create a narrow "Receipt" page size (80mm wide is standard thermal printer width)
    com.itextpdf.text.Rectangle envelope = new com.itextpdf.text.Rectangle(226, 600); 
    com.itextpdf.text.Document doc = new com.itextpdf.text.Document(envelope, 15, 15, 10, 10); // slightly wider left margin
    com.itextpdf.text.pdf.PdfWriter.getInstance(doc, new java.io.FileOutputStream(path));
    doc.open();

    // 4. Set Fonts
    com.itextpdf.text.Font boldTitle = com.itextpdf.text.FontFactory.getFont(com.itextpdf.text.FontFactory.HELVETICA_BOLD, 12);
    com.itextpdf.text.Font normalTitle = com.itextpdf.text.FontFactory.getFont(com.itextpdf.text.FontFactory.HELVETICA_BOLD, 10);
    com.itextpdf.text.Font bold = com.itextpdf.text.FontFactory.getFont(com.itextpdf.text.FontFactory.HELVETICA_BOLD, 10);
    com.itextpdf.text.Font normal = com.itextpdf.text.FontFactory.getFont(com.itextpdf.text.FontFactory.HELVETICA, 10);
    com.itextpdf.text.Font small = com.itextpdf.text.FontFactory.getFont(com.itextpdf.text.FontFactory.HELVETICA, 8);

    // Dotted Line Helper (iText dynamic dotted line)
    com.itextpdf.text.pdf.draw.DottedLineSeparator dottedLine = new com.itextpdf.text.pdf.draw.DottedLineSeparator();
    dottedLine.setLineColor(com.itextpdf.text.BaseColor.LIGHT_GRAY);
    dottedLine.setGap(2f);
    doc.add(new com.itextpdf.text.Chunk(dottedLine));
    
    doc.add(new com.itextpdf.text.Paragraph(" ")); // spacing

    // 5. Centered Header
    com.itextpdf.text.Paragraph collegeName = new com.itextpdf.text.Paragraph("WESTERN LEYTE COLLEGE\n", boldTitle);
    collegeName.setAlignment(com.itextpdf.text.Element.ALIGN_CENTER);
    collegeName.setLeading(15f); // spacing
    doc.add(collegeName);

    com.itextpdf.text.Paragraph hubName = new com.itextpdf.text.Paragraph("LIBRARY HUB\n", normalTitle);
    hubName.setAlignment(com.itextpdf.text.Element.ALIGN_CENTER);
    hubName.setLeading(12f);
    doc.add(hubName);
    
    doc.add(new com.itextpdf.text.Paragraph(" ")); // spacing
    com.itextpdf.text.Paragraph transaction = new com.itextpdf.text.Paragraph("Transaction Receipt\n\n", small);
    transaction.setAlignment(com.itextpdf.text.Element.ALIGN_CENTER);
    transaction.setLeading(10f);
    doc.add(transaction);

    doc.add(new com.itextpdf.text.Chunk(dottedLine)); // separator
    doc.add(new com.itextpdf.text.Paragraph(" ")); // spacing

    // 6. Centered Transaction/Member Details
    com.itextpdf.text.Paragraph datePara = new com.itextpdf.text.Paragraph("Date: " + java.time.LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm")), small);
    datePara.setAlignment(com.itextpdf.text.Element.ALIGN_CENTER);
    datePara.setLeading(15f);
    doc.add(datePara);

    com.itextpdf.text.Paragraph memberPara = new com.itextpdf.text.Paragraph("Member: " + jTableIssueReport.getValueAt(row, 1).toString(), normal);
    memberPara.setAlignment(com.itextpdf.text.Element.ALIGN_CENTER);
    memberPara.setLeading(15f);
    doc.add(memberPara);

    com.itextpdf.text.Paragraph typePara = new com.itextpdf.text.Paragraph("Type: " + jTableIssueReport.getValueAt(row, 2).toString(), small);
    typePara.setAlignment(com.itextpdf.text.Element.ALIGN_CENTER);
    typePara.setLeading(15f);
    doc.add(typePara);

    doc.add(new com.itextpdf.text.Paragraph(" ")); // spacing
    doc.add(new com.itextpdf.text.Chunk(dottedLine)); // separator
    doc.add(new com.itextpdf.text.Paragraph(" ")); // spacing

    // 7. Centered Book Details
    com.itextpdf.text.Paragraph bookTitleLabel = new com.itextpdf.text.Paragraph("BOOK TITLE:", small);
    bookTitleLabel.setAlignment(com.itextpdf.text.Element.ALIGN_CENTER);
    doc.add(bookTitleLabel);

    com.itextpdf.text.Paragraph bookTitlePara = new com.itextpdf.text.Paragraph(jTableIssueReport.getValueAt(row, 6).toString(), normal);
    bookTitlePara.setAlignment(com.itextpdf.text.Element.ALIGN_CENTER);
    bookTitlePara.setLeading(15f);
    doc.add(bookTitlePara);

    com.itextpdf.text.Paragraph acqNoPara = new com.itextpdf.text.Paragraph("Acq No: " + jTableIssueReport.getValueAt(row, 5).toString(), small);
    acqNoPara.setAlignment(com.itextpdf.text.Element.ALIGN_CENTER);
    acqNoPara.setLeading(12f);
    doc.add(acqNoPara);

    com.itextpdf.text.Paragraph issuedPara = new com.itextpdf.text.Paragraph("Issued: " + jTableIssueReport.getValueAt(row, 8).toString(), small);
    issuedPara.setAlignment(com.itextpdf.text.Element.ALIGN_CENTER);
    issuedPara.setLeading(12f);
    doc.add(issuedPara);

    com.itextpdf.text.Paragraph returnedPara = new com.itextpdf.text.Paragraph("Returned: " + jTableIssueReport.getValueAt(row, 10).toString(), small);
    returnedPara.setAlignment(com.itextpdf.text.Element.ALIGN_CENTER);
    returnedPara.setLeading(12f);
    doc.add(returnedPara);
    
    doc.add(new com.itextpdf.text.Paragraph(" ")); // spacing
    doc.add(new com.itextpdf.text.Chunk(dottedLine)); // separator
    doc.add(new com.itextpdf.text.Paragraph(" ")); // spacing

    // 8. Centered Footer / Penalty
    com.itextpdf.text.Paragraph penaltyLabel = new com.itextpdf.text.Paragraph("Penalty details:", small);
    penaltyLabel.setAlignment(com.itextpdf.text.Element.ALIGN_CENTER);
    doc.add(penaltyLabel);

    com.itextpdf.text.Paragraph penaltyAmount = new com.itextpdf.text.Paragraph("PENALTY PAID: ₱" + jTableIssueReport.getValueAt(row, 11).toString(), bold);
    penaltyAmount.setAlignment(com.itextpdf.text.Element.ALIGN_CENTER);
    penaltyAmount.setLeading(15f);
    doc.add(penaltyAmount);

    com.itextpdf.text.Paragraph statusPara = new com.itextpdf.text.Paragraph("Status: " + jTableIssueReport.getValueAt(row, 12).toString(), small);
    statusPara.setAlignment(com.itextpdf.text.Element.ALIGN_CENTER);
    statusPara.setLeading(12f);
    doc.add(statusPara);
    
    com.itextpdf.text.Paragraph thankYouPara = new com.itextpdf.text.Paragraph("\nThank you for returning :) ", small);
    thankYouPara.setAlignment(com.itextpdf.text.Element.ALIGN_CENTER);
    thankYouPara.setLeading(20f);
    doc.add(thankYouPara);

    doc.close();
    javax.swing.JOptionPane.showMessageDialog(null, "Receipt generated to Desktop!");

} catch (Exception e) {
    javax.swing.JOptionPane.showMessageDialog(null, "Error generating PDF: " + e.getMessage());
    e.printStackTrace();
}
    }//GEN-LAST:event_printBtnActionPerformed

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
        java.awt.EventQueue.invokeLater(() -> new issuereport_management1().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton deleteBtn;
    private javax.swing.JButton jButton6;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTableIssueReport;
    private javax.swing.JButton printBtn;
    private javax.swing.JTextField txtSearch;
    private javax.swing.JTextField txtTotalPaid;
    private javax.swing.JTextField txtTotalUnpaid;
    // End of variables declaration//GEN-END:variables
}
