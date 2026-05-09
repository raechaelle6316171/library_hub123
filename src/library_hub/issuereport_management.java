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
        
        txtSearch.putClientProperty("JTextField.placeholderText", "Search (ID, Name, or Book)");
        
        showAllReports();
        populateIssuedTable("");      // Or whatever you named your "show data" method
        calculateTotalPaid();  // This makes the total show up immediately on open
        calculateTotalUnpaid();
        
       //updateTotalUnpaid();
        //updateTotalPaid();
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

            // Map columns to match your jTable headers
            Object[] row = {
                rs.getInt("id"),               
                rs.getString("school_id"),   // New: Fetching numeric ID
                rs.getString("fullname"),    
                rs.getString("usertype"),    
                rs.getString("contact_no"),  // New: Fetching 11-digit number
                rs.getString("course"),      
                rs.getString("year"),                         
                rs.getString("book_acq_no"),
                rs.getString("book_title"),
                rs.getString("author"),
                issueStr,    
                dueStr,      
                returnStr, 
                rs.getString("penalty_paid"),            
                rs.getString("status")
            };
            model.addRow(row);
        }
    } catch (SQLException e) {
        JOptionPane.showMessageDialog(null, "Report Error: " + e.getMessage());
    }
    calculateTotalPaid();
}
    
    public void calculateTotalUnpaid() {
    try {
        Connection conn = MySQLConnect.getConnection();
        
        // THE BULLETPROOF QUERY:
        // 1. IFNULL: Prevents crashes if any penalty is completely blank (NULL)
        // 2. CAST: Forces the database to treat penalty_paid as a decimal number, not text
        // 3. UPPER: Makes sure 'Overdue', 'overdue', and 'OVERDUE' all get counted
        String sql = "SELECT SUM(CAST(IFNULL(penalty_paid, 0) AS DECIMAL(10,2))) AS balance " +
                     "FROM issued_books " +
                     "WHERE TRIM(UPPER(status)) = 'OVERDUE' " +
                     "AND TRIM(UPPER(usertype)) != 'FACULTY'";
        
        PreparedStatement pst = conn.prepareStatement(sql);
        ResultSet rs = pst.executeQuery();

        if (rs.next()) {
            double unpaidTotal = rs.getDouble("balance");
            txtTotalUnpaid.setText(String.format("%.2f", unpaidTotal));
        } else {
            txtTotalUnpaid.setText("0.00");
        }
    } catch (Exception e) {
        javax.swing.JOptionPane.showMessageDialog(null, "Unpaid Box Error: " + e.getMessage());
        txtTotalUnpaid.setText("0.00");
    }
}
    
    public void calculateTotalPaid() {
    double total = 0;
    DefaultTableModel model = (DefaultTableModel) jTableIssueReport.getModel();
    
    for (int i = 0; i < model.getRowCount(); i++) {
        try {
            // Index 13 is "Penalty Paid" based on your table structure
            Object value = model.getValueAt(i, 13);
            
            if (value != null && !value.toString().isEmpty()) {
                double penaltyValue = Double.parseDouble(value.toString());
                total += penaltyValue;
            }
        } catch (NumberFormatException e) {
            // Skips invalid numbers
        }
    }
    // Updates the text field
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

        jPanel9 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        txtTotalPaid = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        txtTotalUnpaid = new javax.swing.JTextField();
        jPanel4 = new javax.swing.JPanel();
        jLabel21 = new javax.swing.JLabel();
        jButton10 = new javax.swing.JButton();
        txtSearch = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        cmbMonth = new javax.swing.JComboBox<>();
        generateReportBtn = new javax.swing.JButton();
        cmbWeek = new javax.swing.JComboBox<>();
        cmbYear = new javax.swing.JComboBox<>();
        close1 = new javax.swing.JButton();
        jLabel8 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTableIssueReport = new javax.swing.JTable();
        printBtn = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel9.setBackground(new java.awt.Color(255, 255, 255));

        jPanel2.setBackground(new java.awt.Color(255, 177, 177));
        jPanel2.setPreferredSize(new java.awt.Dimension(718, 99));

        txtTotalPaid.setEditable(false);
        txtTotalPaid.setBackground(new java.awt.Color(255, 177, 177));
        txtTotalPaid.setFont(new java.awt.Font("Segoe UI", 1, 36)); // NOI18N
        txtTotalPaid.setBorder(null);

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 20)); // NOI18N
        jLabel3.setText("TOTAL PAID");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(195, 195, 195)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtTotalPaid, javax.swing.GroupLayout.PREFERRED_SIZE, 294, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtTotalPaid, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jPanel1.setBackground(new java.awt.Color(255, 210, 135));

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 20)); // NOI18N
        jLabel2.setText("TOTAL UNPAID");

        txtTotalUnpaid.setBackground(new java.awt.Color(255, 210, 135));
        txtTotalUnpaid.setFont(new java.awt.Font("Segoe UI", 1, 36)); // NOI18N
        txtTotalUnpaid.setBorder(null);
        txtTotalUnpaid.addActionListener(this::txtTotalUnpaidActionPerformed);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(401, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtTotalUnpaid, javax.swing.GroupLayout.PREFERRED_SIZE, 294, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2))
                .addGap(49, 49, 49))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtTotalUnpaid, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jPanel4.setBackground(new java.awt.Color(177, 241, 255));
        jPanel4.setForeground(new java.awt.Color(0, 102, 0));
        jPanel4.setPreferredSize(new java.awt.Dimension(285, 53));

        jLabel21.setFont(new java.awt.Font("Lucida Fax", 1, 36)); // NOI18N
        jLabel21.setForeground(new java.awt.Color(0, 0, 0));
        jLabel21.setText("ISSUE REPORT");

        jButton10.setIcon(new javax.swing.ImageIcon(getClass().getResource("/library_hub/cancel.png"))); // NOI18N
        jButton10.addActionListener(this::jButton10ActionPerformed);

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel21)
                .addGap(577, 577, 577)
                .addComponent(jButton10, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton10, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel21))
                .addContainerGap())
        );

        txtSearch.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        txtSearch.setForeground(new java.awt.Color(204, 204, 204));
        txtSearch.setText("Search (ID, Name, or Book)");
        txtSearch.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 102, 0), 2));
        txtSearch.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtSearchFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtSearchFocusLost(evt);
            }
        });
        txtSearch.addActionListener(this::txtSearchActionPerformed);
        txtSearch.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtSearchKeyReleased(evt);
            }
        });

        jLabel4.setFont(new java.awt.Font("Trebuchet MS", 1, 24)); // NOI18N
        jLabel4.setText("REPORTS CONTROL");

        cmbMonth.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        cmbMonth.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "January", "February", "March ", "April", "May ", "June", "July ", "August", "September", "October", "November", "December" }));
        cmbMonth.setBorder(null);
        cmbMonth.addActionListener(this::cmbMonthActionPerformed);

        generateReportBtn.setBackground(new java.awt.Color(0, 153, 153));
        generateReportBtn.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        generateReportBtn.setForeground(new java.awt.Color(255, 255, 255));
        generateReportBtn.setText("GENERATE REPORT");
        generateReportBtn.addActionListener(this::generateReportBtnActionPerformed);

        cmbWeek.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        cmbWeek.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Today", "1st Week", "2nd Week", "3rd Week", "4th Week" }));
        cmbWeek.setBorder(null);

        cmbYear.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        cmbYear.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "2026", "2025", "2024" }));
        cmbYear.setBorder(null);

        close1.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        close1.setForeground(new java.awt.Color(0, 102, 0));
        close1.setText("X");
        close1.addActionListener(this::close1ActionPerformed);

        jLabel8.setFont(new java.awt.Font("Trebuchet MS", 1, 24)); // NOI18N
        jLabel8.setText("MONTH");

        jLabel10.setFont(new java.awt.Font("Trebuchet MS", 1, 24)); // NOI18N
        jLabel10.setText("YEAR");

        jLabel11.setFont(new java.awt.Font("Trebuchet MS", 1, 24)); // NOI18N
        jLabel11.setText("WEEK");

        jTableIssueReport.setForeground(new java.awt.Color(0, 0, 0));
        jTableIssueReport.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "Id", "School Id", "Member Name", "User Type", "Contact No", "Course", "Year", "Acq No", "Book Title", "Author", "Issued Date", "Due Date", "Actual Return Date", "Penalty Paid", "Status", "  Select"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Boolean.class
            };
            boolean[] canEdit = new boolean [] {
                false, true, false, false, false, false, false, false, false, false, false, false, false, false, false, true
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(jTableIssueReport);

        printBtn.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        printBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/library_hub/print.png"))); // NOI18N
        printBtn.setText("PRINT TO PDF");
        printBtn.addActionListener(this::printBtnActionPerformed);

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel9Layout.createSequentialGroup()
                        .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel9Layout.createSequentialGroup()
                                .addComponent(txtSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 228, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(close1))
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel9Layout.createSequentialGroup()
                                .addComponent(jLabel4)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 63, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel8)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cmbMonth, javax.swing.GroupLayout.PREFERRED_SIZE, 233, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel10)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cmbYear, javax.swing.GroupLayout.PREFERRED_SIZE, 233, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel11)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cmbWeek, javax.swing.GroupLayout.PREFERRED_SIZE, 233, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(generateReportBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 228, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(19, 19, 19))
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 1494, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, 744, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel9Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(printBtn)))
                .addContainerGap())
            .addComponent(jPanel4, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 1506, Short.MAX_VALUE)
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel9Layout.createSequentialGroup()
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(10, 10, 10)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel11)
                        .addComponent(cmbWeek, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(generateReportBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(cmbYear, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel10)
                        .addComponent(cmbMonth, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel8))
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(close1)
                            .addComponent(txtSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(20, 20, 20)
                .addComponent(printBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(15, 15, 15))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
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

        trs.setRowFilter(RowFilter.regexFilter("(?i)" + searchStr, 0, 2, 7, 8));

    }//GEN-LAST:event_txtSearchKeyReleased

    private void printBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_printBtnActionPerformed
    try {
        // 1. Force the table to save the checkbox state
        if (jTableIssueReport.isEditing()) {
            jTableIssueReport.getCellEditor().stopCellEditing();
        }

        int rowCount = jTableIssueReport.getRowCount();
        int selectColumnIndex = 15; // The 'Select' checkbox is the last column

        java.util.Map<String, java.util.List<Object[]>> groupedMembers = new java.util.LinkedHashMap<>();
        
        for (int i = 0; i < rowCount; i++) {
            Object value = jTableIssueReport.getValueAt(i, selectColumnIndex);
            boolean isChecked = false;
            
            // Safe conversion for checkboxes
            if (value instanceof Boolean) {
                isChecked = (Boolean) value;
            } else if (value != null) {
                isChecked = Boolean.parseBoolean(value.toString());
            }

            if (isChecked) {
                // Index 2 is Member Name
                String memberName = jTableIssueReport.getValueAt(i, 2).toString(); 
                Object[] rowData = new Object[jTableIssueReport.getColumnCount()];
                for (int col = 0; col < jTableIssueReport.getColumnCount(); col++) {
                    rowData[col] = jTableIssueReport.getValueAt(i, col);
                }
                groupedMembers.computeIfAbsent(memberName, k -> new java.util.ArrayList<>()).add(rowData);
            }
        }

        if (groupedMembers.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please check the 'Select' boxes first!");
            return;
        }

        // 2. Setup PDF Document (Receipt Size)
        String path = System.getProperty("user.home") + "/Desktop/Library_Receipt.pdf";
        com.itextpdf.text.Rectangle envelope = new com.itextpdf.text.Rectangle(226, 850); 
        com.itextpdf.text.Document doc = new com.itextpdf.text.Document(envelope, 15, 15, 10, 10); 
        com.itextpdf.text.pdf.PdfWriter.getInstance(doc, new java.io.FileOutputStream(path));
        doc.open();

        // Fonts
        com.itextpdf.text.Font boldTitle = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 11);
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
            doc.add(new Paragraph("-------------------------------------------------------------------------", tiny));

            // --- MEMBER DETAILS ---
            doc.add(new Paragraph("NAME: " + name, boldLabel));
            doc.add(new Paragraph("TYPE: " + info[3].toString().toUpperCase() + " | COURSE " + info[5] + " " + info[6], normal));
            doc.add(new Paragraph("SCHOOL ID: " + info[1].toString(), normal));
            doc.add(new Paragraph("CONTACT No. " + info[4].toString(), normal));
            doc.add(new Paragraph("PRINTED: " + java.time.LocalDate.now().format(java.time.format.DateTimeFormatter.ofPattern("MM/dd/yyyy")), normal));
            doc.add(new Paragraph("-------------------------------------------------------------------------", tiny));

            double totalPaid = 0;
            for (Object[] row : books) {
                // Table for Acq, Title, Penalty layout
                com.itextpdf.text.pdf.PdfPTable bookTable = new com.itextpdf.text.pdf.PdfPTable(new float[]{60, 40});
                bookTable.setWidthPercentage(100);

                bookTable.addCell(createCell("Acq No:", normal, Element.ALIGN_LEFT));
                bookTable.addCell(createCell(row[7].toString(), normal, Element.ALIGN_RIGHT));

                bookTable.addCell(createCell("Book Title:", normal, Element.ALIGN_LEFT));
                bookTable.addCell(createCell(row[8].toString().toUpperCase(), boldLabel, Element.ALIGN_RIGHT));

                bookTable.addCell(createCell("Penalty:", normal, Element.ALIGN_LEFT));
                bookTable.addCell(createCell("P" + row[13].toString(), normal, Element.ALIGN_RIGHT));

                doc.add(bookTable);

                // Dates & Status
                Paragraph details = new Paragraph(
                    "Issued: " + row[10].toString() + "\n" +
                    "Due: " + row[11].toString() + "\n" +
                    "Returned: " + row[12].toString() + " | Status: " + row[14].toString(), 
                    tiny
                );
                doc.add(details);
                doc.add(new Paragraph("-------------------------------------------------------------------------", tiny));

                try { totalPaid += Double.parseDouble(row[13].toString()); } catch (Exception e) {}
            }

            // --- FOOTER ---
            Paragraph totalPara = new Paragraph("TOTAL PAID: P" + String.format("%.2f", totalPaid), boldTitle);
            totalPara.setAlignment(Element.ALIGN_RIGHT);
            doc.add(totalPara);
            
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
}

// Keep the helper method from the previous response
private PdfPCell createCell(String text, com.itextpdf.text.Font font, int alignment) {
    PdfPCell cell = new PdfPCell(new Phrase(text, font));
    cell.setBorder(com.itextpdf.text.Rectangle.NO_BORDER);
    cell.setHorizontalAlignment(alignment);
    cell.setPaddingBottom(2f);
    return cell;

    }//GEN-LAST:event_printBtnActionPerformed



public void showAllReports() {
    DefaultTableModel model = (DefaultTableModel) jTableIssueReport.getModel();
    model.setRowCount(0); // Clear the table first

    try {
        Connection conn = MySQLConnect.getConnection();
        String sql = "SELECT * FROM issue_report"; // No filters, shows everything
        PreparedStatement pst = conn.prepareStatement(sql);
        ResultSet rs = pst.executeQuery();

        while (rs.next()) {
            model.addRow(new Object[]{
                rs.getString("id"),
                rs.getString("school_id"),
                rs.getString("fullname"),
                rs.getString("usertype"),
                rs.getString("contact_no"),
                rs.getString("course"),
                rs.getString("year"),
                rs.getString("book_acq_no"),
                rs.getString("book_title"),
                rs.getString("author"),
                rs.getString("issue_date"),
                rs.getString("due_date"),
                rs.getString("actual_return_date"),
                rs.getString("penalty_paid"),
                rs.getString("status"),
                false 
            });
        }
    } catch (Exception e) {
        JOptionPane.showMessageDialog(null, e);
    }
}



    private void close1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_close1ActionPerformed
        txtSearch.setText("");

        DefaultTableModel model = (DefaultTableModel) jTableIssueReport.getModel();
        TableRowSorter<DefaultTableModel> trs = new TableRowSorter<>(model);
        jTableIssueReport.setRowSorter(trs);
        trs.setRowFilter(null); 
        
        showAllReports();

        updateTotalPaid();
        calculateTotalPaid();

        txtSearch.requestFocus();
    }//GEN-LAST:event_close1ActionPerformed

    private void generateReportBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_generateReportBtnActionPerformed
        String selectedMonth = cmbMonth.getSelectedItem().toString(); 
    String selectedYear = cmbYear.getSelectedItem().toString();   
    String selectedWeek = cmbWeek.getSelectedItem().toString();   

    DefaultTableModel model = (DefaultTableModel) jTableIssueReport.getModel();
    model.setRowCount(0); 

    try {
        Connection conn = MySQLConnect.getConnection();
        String sql;
        PreparedStatement pst;

        // 1. Strict "Today" Logic
        if (selectedWeek.equalsIgnoreCase("today")) {
            // This query only returns rows if CURDATE matches the selected Month and Year
            sql = "SELECT * FROM issue_report WHERE DATE(actual_return_date) = CURDATE() " +
                  "AND MONTHNAME(actual_return_date) = ? " +
                  "AND YEAR(actual_return_date) = ?";
            
            pst = conn.prepareStatement(sql);
            pst.setString(1, selectedMonth);
            pst.setString(2, selectedYear);
            
        } else {
            // 2. Week Filtering Logic
            if (selectedWeek.contains("Select Week")) {
                JOptionPane.showMessageDialog(this, "Please select a week or 'Today'");
                return;
            }

            String weekOnlyNumbers = selectedWeek.replaceAll("[^0-9]", "");
            int weekNum = Integer.parseInt(weekOnlyNumbers);

            sql = "SELECT * FROM issue_report WHERE " +
                  "MONTHNAME(actual_return_date) = ? AND " +
                  "YEAR(actual_return_date) = ? AND " +
                  "FLOOR((DAY(actual_return_date) - 1) / 7) + 1 = ?";
            
            pst = conn.prepareStatement(sql);
            pst.setString(1, selectedMonth);
            pst.setString(2, selectedYear);
            pst.setInt(3, weekNum);
        }
        
        ResultSet rs = pst.executeQuery();

        while (rs.next()) {
            model.addRow(new Object[]{
                rs.getString("id"),
                rs.getString("school_id"),
                rs.getString("fullname"),
                rs.getString("usertype"),
                rs.getString("contact_no"),
                rs.getString("course"),
                rs.getString("year"),
                rs.getString("book_acq_no"),
                rs.getString("book_title"),
                rs.getString("author"),
                rs.getString("issue_date"),
                rs.getString("due_date"),
                rs.getString("actual_return_date"),
                rs.getString("penalty_paid"),
                rs.getString("status"),
                false 
            });
        }
        
        // Refresh dashboard totals
        calculateTotalPaid();
        calculateTotalUnpaid();
        
        if (model.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this, "No records found for " + selectedMonth + " " + selectedYear + " (" + selectedWeek + ")");
        }

    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
    }
    }//GEN-LAST:event_generateReportBtnActionPerformed

    private void txtSearchFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtSearchFocusGained
        if (txtSearch.getText().equals("Search (ID, Name, or Book)")) {
        txtSearch.setText("");
        txtSearch.setForeground(java.awt.Color.BLACK);
}
    }//GEN-LAST:event_txtSearchFocusGained

    private void txtSearchFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtSearchFocusLost
        if (txtSearch.getText().isEmpty()) {
        txtSearch.setForeground(java.awt.Color.GRAY);
        txtSearch.setText("Search (ID, Name, or Book)");
}
    }//GEN-LAST:event_txtSearchFocusLost

    private void cmbMonthActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbMonthActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cmbMonthActionPerformed

    private void txtTotalUnpaidActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtTotalUnpaidActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtTotalUnpaidActionPerformed

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
        java.awt.EventQueue.invokeLater(() -> new issuereport_management().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton close1;
    private javax.swing.JComboBox<String> cmbMonth;
    private javax.swing.JComboBox<String> cmbWeek;
    private javax.swing.JComboBox<String> cmbYear;
    private javax.swing.JButton generateReportBtn;
    private javax.swing.JButton jButton10;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTableIssueReport;
    private javax.swing.JButton printBtn;
    private javax.swing.JTextField txtSearch;
    private javax.swing.JTextField txtTotalPaid;
    private javax.swing.JTextField txtTotalUnpaid;
    // End of variables declaration//GEN-END:variables
}
