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
import java.io.File;
import java.io.IOException;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;

public class issuereport_management extends javax.swing.JFrame {
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(issuereport_management.class.getName());

    public issuereport_management() {
        initComponents();
        populateIssuedTable(""); 
        updateTotalPaid();
        updateTotalUnpaid();
    }
    
    
    public void populateIssuedTable(String query) {
    DefaultTableModel model = (DefaultTableModel) jTableIssueReport.getModel();
    model.setRowCount(0);
    java.text.SimpleDateFormat displayFormat = new java.text.SimpleDateFormat("MM/dd/yyyy HH:mm");
    try {
        Connection conn = MySQLConnect.getConnection();

       String sql = "SELECT * FROM issue_report WHERE fullname LIKE ? OR book_title LIKE ? ORDER BY id DESC";

        PreparedStatement pst = conn.prepareStatement(sql);
        String search = "%" + query + "%";
        pst.setString(1, search);
        pst.setString(2, search);
        
        ResultSet rs = pst.executeQuery();
        
        while (rs.next()) {
            java.sql.Timestamp issueTs = rs.getTimestamp("issue_date");
            java.sql.Timestamp dueTs = rs.getTimestamp("due_date");
            java.sql.Timestamp returnTs = rs.getTimestamp("actual_return_date");

            String issueStr = (issueTs != null) ? displayFormat.format(issueTs) : "N/A";
            String dueStr = (dueTs != null) ? displayFormat.format(dueTs) : "N/A";
            String returnStr = (returnTs != null) ? displayFormat.format(returnTs) : "N/A";
            Object[] row = {
                rs.getInt("id"),               
                rs.getString("fullname"),
                rs.getString("usertype"),
                rs.getString("course"),
                rs.getString("year"),                         
                rs.getString("book_acq_no"),
                rs.getString("book_title"),
                rs.getString("author"),
                issueStr,    // Formatted
                dueStr,      // Formatted
                returnStr, 
                rs.getString("penalty_paid"),            
                rs.getString("status")
            };
            model.addRow(row);
        }
    } catch (SQLException e) {
        JOptionPane.showMessageDialog(null, "Report Error: " + e.getMessage());
    }
    //calculateTotalUnpaid();
    calculateTotalPaid();

}
    
    public void calculateTotalUnpaid() {
    double totalUnpaid = 0.0;
    for (int i = 0; i < jTableIssueReport.getRowCount(); i++) {
        try {

            Object statusObj = jTableIssueReport.getValueAt(i, 12);
            Object penaltyObj = jTableIssueReport.getValueAt(i, 11);
            
            if (statusObj != null && penaltyObj != null) {
                String status = statusObj.toString();

                if (status.equalsIgnoreCase("Overdue")) {
                    double amount = Double.parseDouble(penaltyObj.toString());
                    totalUnpaid += amount;
                }
            }
        } catch (Exception e) {

        }
    }
    txtTotalUnpaid.setText(String.format("%.2f", totalUnpaid));
}

    public void calculateTotalPaid() {
    double total = 0;
    for (int i = 0; i < jTableIssueReport.getRowCount(); i++) {
        try {
            // Penalty Paid is at Index 11
            Object value = jTableIssueReport.getValueAt(i, 11);
            String status = jTableIssueReport.getValueAt(i, 12).toString();
            
            if (status.equalsIgnoreCase("Returned") && value != null) {
                total += Double.parseDouble(value.toString());
            }
        } catch (Exception e) { /* Skip errors */ }
    }
    txtTotalPaid.setText(String.format("%.2f", total));
    }
   
    
    public void updateTotalUnpaid() {
    try {
        Connection conn = MySQLConnect.getConnection();
        
        String sql = "SELECT SUM(penalty_paid) AS total_fine FROM issued_books WHERE status = 'Overdue'";
        
        PreparedStatement pst = conn.prepareStatement(sql);
        ResultSet rs = pst.executeQuery();

        if (rs.next()) {
            double amount = rs.getDouble("total_fine");
            txtTotalUnpaid.setText(String.format("%.2f", amount));
        } else {
            txtTotalUnpaid.setText("0.00");
        }
        
    } catch (SQLException e) {

        JOptionPane.showMessageDialog(this, "Database Error in Unpaid Calculation:\n" + e.getMessage(), "SQL Error", JOptionPane.ERROR_MESSAGE);
    }
}
    
    public void updateTotalPaid() {
    try {
        Connection conn = MySQLConnect.getConnection();
  
        String sql = "SELECT SUM(penalty_paid) AS total FROM issue_report WHERE status = 'Returned'";
        
        PreparedStatement pst = conn.prepareStatement(sql);
        ResultSet rs = pst.executeQuery();

        if (rs.next()) {
            txtTotalPaid.setText(String.format("%.2f", rs.getDouble("total")));
        }
    } catch (SQLException e) {
        System.out.println("Paid DB Error: " + e.getMessage());
    }
}
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel3 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTableIssueReport = new javax.swing.JTable();
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
        close1 = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        jButton6 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel3.setBackground(new java.awt.Color(102, 51, 0));
        jPanel3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 5));
        jPanel3.setPreferredSize(new java.awt.Dimension(285, 53));

        jTableIssueReport.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "Id", "Member Name", "User Type", "Course", "Year", "Acq No", "Book Title", "Author", "Issued Date", "Due Date", "Actual Return Date", "Penalty Paid", "Status", "  Select"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Boolean.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false, false, false, false, false, true
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(jTableIssueReport);

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

        close1.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        close1.setText("x");
        close1.addActionListener(this::close1ActionPerformed);

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
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(close1)
                        .addGap(194, 194, 194)
                        .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(120, 120, 120)
                        .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 472, Short.MAX_VALUE)))
                .addContainerGap())
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(638, 638, 638)
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
                            .addComponent(txtSearch, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(close1))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)))
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 319, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(30, 30, 30)
                .addComponent(printBtn)
                .addGap(42, 42, 42))
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
        String searchStr = txtSearch.getText().trim();
        DefaultTableModel model = (DefaultTableModel) jTableIssueReport.getModel();
        TableRowSorter<DefaultTableModel> trs = new TableRowSorter<>(model);
        jTableIssueReport.setRowSorter(trs);

        trs.setRowFilter(RowFilter.regexFilter("(?i)" + searchStr, 0, 1, 5, 6));

    }//GEN-LAST:event_txtSearchKeyReleased

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        this.dispose();
        frontpage w = new frontpage();
        w.setVisible(true);
    }//GEN-LAST:event_jButton6ActionPerformed

    private void printBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_printBtnActionPerformed
        try {
        int rowCount = jTableIssueReport.getRowCount();
        int selectColumnIndex = 13; 
        
        java.util.Map<String, java.util.List<Object[]>> groupedMembers = new java.util.LinkedHashMap<>();
        for (int i = 0; i < rowCount; i++) {
            Object isChecked = jTableIssueReport.getValueAt(i, selectColumnIndex);
            if (isChecked != null && (boolean) isChecked) {
                String memberName = jTableIssueReport.getValueAt(i, 1).toString();
                Object[] rowData = new Object[jTableIssueReport.getColumnCount()];
                for(int col = 0; col < jTableIssueReport.getColumnCount(); col++){
                    rowData[col] = jTableIssueReport.getValueAt(i, col);
                }
                groupedMembers.computeIfAbsent(memberName, k -> new java.util.ArrayList<>()).add(rowData);
            }
        }

        if (groupedMembers.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please check the 'Select' boxes first!");
            return;
        }

        String path = System.getProperty("user.home") + "/Desktop/Library_Receipt.pdf";
        com.itextpdf.text.Rectangle envelope = new com.itextpdf.text.Rectangle(226, 850); 
        com.itextpdf.text.Document doc = new com.itextpdf.text.Document(envelope, 10, 10, 10, 10); 
        com.itextpdf.text.pdf.PdfWriter.getInstance(doc, new java.io.FileOutputStream(path));
        doc.open();

        // Fonts to match your edit
        com.itextpdf.text.Font boldTitle = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12);
        com.itextpdf.text.Font boldLabel = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 9);
        com.itextpdf.text.Font normal = FontFactory.getFont(FontFactory.HELVETICA, 9);
        com.itextpdf.text.Font tiny = FontFactory.getFont(FontFactory.HELVETICA, 8);

        for (String name : groupedMembers.keySet()) {
            doc.newPage();
            java.util.List<Object[]> books = groupedMembers.get(name);
            Object[] info = books.get(0); 

            // --- HEADER ---
            Paragraph header = new Paragraph("WESTERN LEYTE COLLEGE\nLIBRARY HUB SYSTEM", boldTitle);
            header.setAlignment(Element.ALIGN_CENTER);
            doc.add(header);
            
            Paragraph subHeader = new Paragraph("\nTransaction Receipt", normal);
            subHeader.setAlignment(Element.ALIGN_CENTER);
            doc.add(subHeader);
            doc.add(new Paragraph("-----------------------------------------------------------------------------", tiny));

            // --- MEMBER DETAILS ---
            doc.add(new Paragraph("NAME: " + name, boldLabel));
            doc.add(new Paragraph("TYPE: " + info[2].toString().toUpperCase() + " | COURSE " + info[3] + " " + info[4], normal));
            doc.add(new Paragraph("PRINTED: " + java.time.LocalDate.now().format(java.time.format.DateTimeFormatter.ofPattern("MM/dd/yyyy")), normal));
            doc.add(new Paragraph(" "));

            double totalPaid = 0;
            for (Object[] row : books) {
                // Book Info Table (3 Columns: Acq, Title, Penalty)
                com.itextpdf.text.pdf.PdfPTable bookTable = new com.itextpdf.text.pdf.PdfPTable(new float[]{30, 40, 30});
                bookTable.setWidthPercentage(100);

                // Column 1: Acq No
                PdfPCell c1 = new PdfPCell();
                c1.addElement(new Phrase("Acq No:", normal));
                c1.addElement(new Phrase(row[5].toString(), normal));
                c1.setBorder(Rectangle.NO_BORDER);
                
                // Column 2: Book Title
                PdfPCell c2 = new PdfPCell();
                c2.addElement(new Phrase("Book Title:", normal));
                c2.addElement(new Phrase(row[6].toString().toUpperCase(), boldLabel));
                c2.setBorder(Rectangle.NO_BORDER);
                
                // Column 3: Penalty
                PdfPCell c3 = new PdfPCell();
                c3.addElement(new Phrase("Penalty", normal));
                c3.addElement(new Phrase("P" + row[11].toString(), normal));
                c3.setBorder(Rectangle.NO_BORDER);
                c3.setHorizontalAlignment(Element.ALIGN_RIGHT);

                bookTable.addCell(c1);
                bookTable.addCell(c2);
                bookTable.addCell(c3);
                doc.add(bookTable);

                // Date Details
                Paragraph dateDetails = new Paragraph(
                    "Issued: " + row[8].toString() + "\n" +
                    "Due: " + row[9].toString() + "\n" +
                    "Returned: " + row[10].toString() + " | Status: " + row[12].toString(), 
                    tiny
                );
                doc.add(dateDetails);
                doc.add(new Paragraph("-----------------------------------------------------------------------------", tiny));

                try { totalPaid += Double.parseDouble(row[11].toString()); } catch (Exception e) {}
            }

            // --- FOOTER ---
            Paragraph total = new Paragraph("TOTAL PAID: P" + String.format("%.2f", totalPaid), boldTitle);
            total.setAlignment(Element.ALIGN_RIGHT);
            doc.add(total);
            
            doc.add(new Paragraph("\n\n"));
            Paragraph ty = new Paragraph("Thank you for returning\n: )", normal);
            ty.setAlignment(Element.ALIGN_CENTER);
            doc.add(ty);
        }

        doc.close();
        java.awt.Desktop.getDesktop().open(new java.io.File(path));

    } catch (Exception e) {
        javax.swing.JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
    }
    }//GEN-LAST:event_printBtnActionPerformed

    private void close1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_close1ActionPerformed
        txtSearch.setText("");

        DefaultTableModel model = (DefaultTableModel) jTableIssueReport.getModel();
        TableRowSorter<DefaultTableModel> trs = new TableRowSorter<>(model);
        jTableIssueReport.setRowSorter(trs);
        trs.setRowFilter(null); 

        updateTotalPaid();

        txtSearch.requestFocus();
    }//GEN-LAST:event_close1ActionPerformed

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
        java.awt.EventQueue.invokeLater(() -> new issuereport_management().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton close1;
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
