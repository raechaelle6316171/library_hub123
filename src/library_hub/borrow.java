/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package library_hub;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author RACHELL
 */
public class borrow extends javax.swing.JFrame {
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(borrow.class.getName());

    /**
     * Creates new form borrow
     */
    public borrow() {
        initComponents();
        populateMemberTable("");
        populateBookTable("");
        //String memberContact = "";
    }
    String selectedMemberContact = "";
    
    private boolean hasOverdue(String borrowerName) {
    boolean isBlocked = false;
    try {
        Connection conn = MySQLConnect.getConnection();
        // This query checks if the borrower has any active 'Overdue' status in your records
        String sql = "SELECT * FROM issued_books WHERE fullname = ? AND status = 'Overdue'";
        PreparedStatement pst = conn.prepareStatement(sql);
        pst.setString(1, borrowerName);
        ResultSet rs = pst.executeQuery();

        if (rs.next()) {
            isBlocked = true;
        }
    } catch (Exception e) {
        System.out.println("Error checking overdue: " + e.getMessage());
    }
    return isBlocked;
}
    
    
    public void setAutomaticDates() {
    // 1. Get current date and time for the Issue Date
    LocalDateTime now = LocalDateTime.now();
    
    // 2. Set the Due Date: Tomorrow (plus 1 day) at exactly 5:00 PM (17:00)
    LocalDateTime tomorrowAtFive = now.plusDays(1)
                                      .withHour(17)
                                      .withMinute(0)
                                      .withSecond(0)
                                      .withNano(0);     
    
    // 3. Create the formatter to match your UI display
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm");
    
    // 4. Update the text fields
    txtIssueDate.setText(now.format(formatter));
    txtDueDate.setText(tomorrowAtFive.format(formatter));
    
    // 5. Keep them non-editable so students can't change the deadline
    txtIssueDate.setEditable(false);
    txtDueDate.setEditable(false);
}

    public void populateMemberTable(String query) {
    DefaultTableModel model = (DefaultTableModel) jTableMembers.getModel();
    model.setRowCount(0); 

    try {
        Connection conn = MySQLConnect.getConnection();
        String sql;
        
        if (query.isEmpty()) {
            sql = "SELECT fullname, usertype, course, year FROM member_records";
        } else {
            sql = "SELECT fullname, usertype, course, year FROM member_records WHERE fullname LIKE ?";
        }

        PreparedStatement pst = conn.prepareStatement(sql);
        if (!query.isEmpty()) {
            pst.setString(1, "%" + query + "%");
        }

        ResultSet rs = pst.executeQuery();

        while (rs.next()) {

            Object[] row = {
                rs.getString("fullname"),
                rs.getString("usertype"),
                rs.getString("course"),
                rs.getString("year")
            };
            model.addRow(row);
        }
    } catch (SQLException e) {
        JOptionPane.showMessageDialog(this, "Error loading members: " + e.getMessage());
    }
}
    
    public void populateBookTable(String query) {
DefaultTableModel model = (DefaultTableModel) jTableBooks.getModel();
    model.setRowCount(0); 

    try {
        Connection conn = MySQLConnect.getConnection();
        String sql;
        
        // Changed condition to ONLY show 'Available' books
        if (query.isEmpty()) {
            sql = "SELECT acquisition_no, title, author, status FROM books WHERE status = 'Available'";
        } else {
            sql = "SELECT acquisition_no, title, author, status FROM books " +
                  "WHERE (acquisition_no LIKE ? OR title LIKE ?) AND status = 'Available'";
        }

        PreparedStatement pst = conn.prepareStatement(sql);
        if (!query.isEmpty()) {
            pst.setString(1, "%" + query + "%");
            pst.setString(2, "%" + query + "%"); 
        }

        ResultSet rs = pst.executeQuery();

        while (rs.next()) {
            Object[] row = {
                rs.getString("acquisition_no"),
                rs.getString("title"),
                rs.getString("author"),
                rs.getString("status")
            };
            model.addRow(row);
        }
    } catch (SQLException e) {
        JOptionPane.showMessageDialog(this, "Error loading available books: " + e.getMessage());
    }
}
    private void clearTransactionFields() {
        
        txtFullName.setText("");
        txtUserType.setText("");
        txtAcq.setText("");
        txtTitle.setText("");

        txtIssueDate.setText("");
        txtDueDate.setText("");

        jTableMembers.clearSelection();
        jTableBooks.clearSelection();
}

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel5 = new javax.swing.JPanel();
        jLabel11 = new javax.swing.JLabel();
        jButton10 = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTableMembers = new javax.swing.JTable();
        txtSearchMember = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTableBooks = new javax.swing.JTable();
        txtSearchBook = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jPanel6 = new javax.swing.JPanel();
        jLabel12 = new javax.swing.JLabel();
        jPanel7 = new javax.swing.JPanel();
        txtUserType = new javax.swing.JTextField();
        txtFullName = new javax.swing.JTextField();
        txtTitle = new javax.swing.JTextField();
        txtAcq = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        txtDueDate = new javax.swing.JFormattedTextField();
        txtIssueDate = new javax.swing.JFormattedTextField();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        IssueBookBtn = new javax.swing.JToggleButton();
        cancelBtn = new javax.swing.JToggleButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel5.setBackground(new java.awt.Color(255, 177, 177));

        jLabel11.setFont(new java.awt.Font("Lucida Fax", 1, 36)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(0, 0, 0));
        jLabel11.setText("BORROW MANAGEMENT");

        jButton10.setIcon(new javax.swing.ImageIcon(getClass().getResource("/library_hub/cancel.png"))); // NOI18N
        jButton10.addActionListener(this::jButton10ActionPerformed);

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap(550, Short.MAX_VALUE)
                .addComponent(jLabel11)
                .addGap(471, 471, 471)
                .addComponent(jButton10, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(11, 11, 11)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel11)
                    .addComponent(jButton10, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(11, Short.MAX_VALUE))
        );

        jPanel2.setBackground(new java.awt.Color(179, 248, 179));

        jTableMembers.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Member Name", "User Type", "Course", "Year"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTableMembers.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTableMembersMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(jTableMembers);

        txtSearchMember.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        txtSearchMember.addActionListener(this::txtSearchMemberActionPerformed);
        txtSearchMember.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtSearchMemberKeyReleased(evt);
            }
        });

        jLabel6.setFont(new java.awt.Font("Trebuchet MS", 1, 24)); // NOI18N
        jLabel6.setText("SEARCH");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(29, 29, 29)
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtSearchMember, javax.swing.GroupLayout.PREFERRED_SIZE, 637, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(23, Short.MAX_VALUE))
            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel2Layout.createSequentialGroup()
                    .addGap(21, 21, 21)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 743, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(18, Short.MAX_VALUE)))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtSearchMember, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel2Layout.createSequentialGroup()
                    .addGap(72, 72, 72)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );

        jPanel3.setBackground(new java.awt.Color(179, 248, 179));

        jTableBooks.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Acq No.", "Book Title", "Author", "Status"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTableBooks.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTableBooksMouseClicked(evt);
            }
        });
        jTableBooks.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTableBooksKeyReleased(evt);
            }
        });
        jScrollPane3.setViewportView(jTableBooks);

        txtSearchBook.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtSearchBookKeyReleased(evt);
            }
        });

        jLabel5.setFont(new java.awt.Font("Trebuchet MS", 1, 24)); // NOI18N
        jLabel5.setText("SEARCH");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap(17, Short.MAX_VALUE)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 702, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtSearchBook, javax.swing.GroupLayout.PREFERRED_SIZE, 608, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(18, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap(13, Short.MAX_VALUE)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtSearchBook, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5, javax.swing.GroupLayout.Alignment.TRAILING))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 199, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jPanel6.setBackground(new java.awt.Color(255, 177, 177));

        jLabel12.setFont(new java.awt.Font("Lucida Fax", 1, 36)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(0, 0, 0));
        jLabel12.setText("~TRANSACTION DETAILS~");

        jPanel7.setBackground(new java.awt.Color(179, 248, 179));

        txtUserType.setEditable(false);
        txtUserType.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N

        txtFullName.setEditable(false);
        txtFullName.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N

        txtTitle.setEditable(false);
        txtTitle.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N

        txtAcq.setEditable(false);
        txtAcq.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        txtAcq.addActionListener(this::txtAcqActionPerformed);

        jLabel7.setFont(new java.awt.Font("Trebuchet MS", 1, 24)); // NOI18N
        jLabel7.setText("FULLNAME:");

        jLabel8.setFont(new java.awt.Font("Trebuchet MS", 1, 24)); // NOI18N
        jLabel8.setText("USERTYPE:");

        jLabel9.setFont(new java.awt.Font("Trebuchet MS", 1, 24)); // NOI18N
        jLabel9.setText("BOOK ACQ No:");

        jLabel10.setFont(new java.awt.Font("Trebuchet MS", 1, 24)); // NOI18N
        jLabel10.setText("BOOK TITLE:");

        txtDueDate.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N

        txtIssueDate.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N

        jLabel13.setFont(new java.awt.Font("Trebuchet MS", 1, 24)); // NOI18N
        jLabel13.setText("ISSUE DATE:");

        jLabel14.setFont(new java.awt.Font("Trebuchet MS", 1, 24)); // NOI18N
        jLabel14.setText("DUE DATE:");

        jLabel15.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel15.setForeground(new java.awt.Color(0, 0, 0));
        jLabel15.setText("(mm/dd/yyyy)");

        jLabel16.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel16.setForeground(new java.awt.Color(0, 0, 0));
        jLabel16.setText("(mm/dd/yyyy)");

        IssueBookBtn.setBackground(new java.awt.Color(255, 102, 102));
        IssueBookBtn.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        IssueBookBtn.setText("ISSUE BOOK");
        IssueBookBtn.addActionListener(this::IssueBookBtnActionPerformed);

        cancelBtn.setBackground(new java.awt.Color(153, 255, 153));
        cancelBtn.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        cancelBtn.setText("CANCEL");
        cancelBtn.addActionListener(this::cancelBtnActionPerformed);

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel7)
                    .addComponent(jLabel8))
                .addGap(58, 58, 58)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtFullName)
                            .addComponent(txtUserType, javax.swing.GroupLayout.PREFERRED_SIZE, 316, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(299, 299, 299)
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addGroup(jPanel7Layout.createSequentialGroup()
                                .addComponent(jLabel10)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(txtTitle, javax.swing.GroupLayout.PREFERRED_SIZE, 316, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel7Layout.createSequentialGroup()
                                .addComponent(jLabel9)
                                .addGap(18, 18, 18)
                                .addComponent(txtAcq, javax.swing.GroupLayout.PREFERRED_SIZE, 316, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(90, 90, 90))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel13)
                            .addComponent(jLabel14))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel7Layout.createSequentialGroup()
                                .addComponent(IssueBookBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 173, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(39, 39, 39)
                                .addComponent(cancelBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 173, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addGroup(jPanel7Layout.createSequentialGroup()
                                    .addComponent(jLabel16)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(txtDueDate, javax.swing.GroupLayout.PREFERRED_SIZE, 339, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(jPanel7Layout.createSequentialGroup()
                                    .addComponent(jLabel15)
                                    .addGap(42, 42, 42)
                                    .addComponent(txtIssueDate, javax.swing.GroupLayout.PREFERRED_SIZE, 339, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addGap(460, 460, 460))))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addGap(4, 4, 4)
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtFullName, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel7)))
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addGap(66, 66, 66)
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtUserType, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel8)))
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel9)
                            .addComponent(txtAcq, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtTitle, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel10))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtIssueDate, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel13)
                            .addComponent(jLabel15))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtDueDate, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel16)
                            .addComponent(jLabel14))))
                .addGap(18, 18, 18)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cancelBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(IssueBookBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(14, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel12)
                .addGap(513, 513, 513))
            .addComponent(jPanel7, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(jLabel12)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(30, 30, 30))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 4, Short.MAX_VALUE))
            .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, 386, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jTableMembersMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTableMembersMouseClicked
        int row = jTableMembers.getSelectedRow();
        DefaultTableModel model = (DefaultTableModel) jTableMembers.getModel();

        // Index 0 is Member Name in your Borrow Management table
        String name = model.getValueAt(row, 0).toString();
        txtFullName.setText(name);

        // Index 1 is User Type
        txtUserType.setText(model.getValueAt(row, 1).toString());

        // FETCH CONTACT AUTOMATICALLY:
        try {
            java.sql.Connection conn = MySQLConnect.getConnection();
            // Adjust "full_name" if your registration table uses a different column name
            String sql = "SELECT contact_number FROM member_records WHERE fullname = ?";
            java.sql.PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1, name);
            java.sql.ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                // This fills the hidden variable so the Issue Book button can see it
                selectedMemberContact = rs.getString("contact_number");
            } else {
                selectedMemberContact = "";
            }
        } catch (Exception e) {
            System.out.println("Error fetching contact: " + e.getMessage());
        }
    }//GEN-LAST:event_jTableMembersMouseClicked

    private void txtSearchMemberActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtSearchMemberActionPerformed

    }//GEN-LAST:event_txtSearchMemberActionPerformed

    private void txtSearchMemberKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSearchMemberKeyReleased
        String search = txtSearchMember.getText();
        populateMemberTable(search);
    }//GEN-LAST:event_txtSearchMemberKeyReleased

    private void jTableBooksMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTableBooksMouseClicked
        int row = jTableBooks.getSelectedRow();
        DefaultTableModel model = (DefaultTableModel) jTableBooks.getModel();

        // Fill the text fields with book info
        txtAcq.setText(model.getValueAt(row, 0).toString());
        txtTitle.setText(model.getValueAt(row, 1).toString());

        // Calculate and display the 1-day/5 PM deadline
        setAutomaticDates();
    }//GEN-LAST:event_jTableBooksMouseClicked

    private void jTableBooksKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTableBooksKeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_jTableBooksKeyReleased

    private void txtSearchBookKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSearchBookKeyReleased
        String query = txtSearchBook.getText().trim();

        DefaultTableModel model = (DefaultTableModel) jTableBooks.getModel();
        model.setRowCount(0);

        try {
            Connection conn = MySQLConnect.getConnection();

            // Added ( ) around the OR conditions and added the status check
            String sql = "SELECT * FROM books WHERE (acquisition_no LIKE ? OR title LIKE ?) AND status = 'Available'";
            PreparedStatement pst = conn.prepareStatement(sql);

            pst.setString(1, "%" + query + "%");
            pst.setString(2, "%" + query + "%");

            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                String acq = rs.getString("acquisition_no");
                String title = rs.getString("title");
                String author = rs.getString("author");
                String status = rs.getString("status");

                model.addRow(new Object[]{acq, title, author, status});
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Search Error: " + e.getMessage());
        }
    }//GEN-LAST:event_txtSearchBookKeyReleased

    private void txtAcqActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtAcqActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtAcqActionPerformed

    private void IssueBookBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_IssueBookBtnActionPerformed
        String name = txtFullName.getText().trim();
        String userType = txtUserType.getText().trim();
        String acqNo = txtAcq.getText().trim();
        String title = txtTitle.getText().trim();
        String iDate = txtIssueDate.getText().trim();
        String dDate = txtDueDate.getText().trim();

        // Uses the hidden variable instead of a text field
        String contact = selectedMemberContact;

        // 1. GLOBAL OVERDUE CHECK (BLOCKS STUDENTS ONLY)
        if (hasOverdue(name)) {
            if (!userType.equalsIgnoreCase("Faculty")) {
                JOptionPane.showMessageDialog(this,
                    "BORROWING BLOCKED: This user has an outstanding overdue book. " +
                    "\nPlease settle penalties in the Penalty module first.",
                    "Account Restriction",
                    JOptionPane.ERROR_MESSAGE);
                return;
            }
        }

        // 2. EMPTY FIELD VALIDATION
        if (name.isEmpty() || contact.isEmpty() || acqNo.isEmpty() || iDate.isEmpty() || dDate.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please select a member, a book, and fill in the dates!");
            return;
        }

        Connection conn = null;
        try {
            conn = MySQLConnect.getConnection();
            conn.setAutoCommit(false);

            // 3. STUDENT BORROWING LIMIT CHECK (3 BOOKS TOTAL)
            if (userType.equalsIgnoreCase("Student")) {
                String countSql = "SELECT COUNT(*) FROM issued_books WHERE fullname = ? AND status = 'Issued'";
                PreparedStatement countPst = conn.prepareStatement(countSql);
                countPst.setString(1, name);
                ResultSet rsCount = countPst.executeQuery();
                if (rsCount.next() && rsCount.getInt(1) >= 3) {
                    JOptionPane.showMessageDialog(this, "Student Limit Reached: " + name + " already has 3 books.", "Borrowing Limit", JOptionPane.WARNING_MESSAGE);
                    return;
                }
            }

            // 4. DUPLICATE TITLE & OVERDUE TITLE CHECK (APPLIES TO EVERYONE, INCLUDING FACULTY)
            // This query specifically prevents borrowing the same title if it is currently 'Issued' OR 'Overdue'
            String duplicateSql = "SELECT status FROM issued_books WHERE fullname = ? AND book_title = ? AND (status = 'Issued' OR status = 'Overdue')";
            PreparedStatement duplicatePst = conn.prepareStatement(duplicateSql);
            duplicatePst.setString(1, name);
            duplicatePst.setString(2, title);
            ResultSet rsDuplicate = duplicatePst.executeQuery();

            if (rsDuplicate.next()) {
                String currentStatus = rsDuplicate.getString("status");
                String message = (currentStatus.equalsIgnoreCase("Overdue"))
                ? "RESTRICTED: " + name + " has an OVERDUE copy of '" + title + "'. It must be returned before borrowing this title again."
                : "DUPLICATE: " + name + " already has an active copy of '" + title + "' issued.";

                JOptionPane.showMessageDialog(this, message, "Borrowing Denied", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // 5. DATE PARSING
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm");
            java.sql.Timestamp issueTS = java.sql.Timestamp.valueOf(LocalDateTime.parse(iDate, formatter));
            java.sql.Timestamp dueTS = java.sql.Timestamp.valueOf(LocalDateTime.parse(dDate, formatter));

            // 6. INSERT RECORD
            String issueSql = "INSERT INTO issued_books (fullname, contact_no, usertype, book_acq_no, book_title, issue_date, due_date, penalty_paid, status) "
            + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, 'Issued')";

            PreparedStatement issuePst = conn.prepareStatement(issueSql);
            issuePst.setString(1, name);
            issuePst.setString(2, contact);
            issuePst.setString(3, userType);
            issuePst.setString(4, acqNo);
            issuePst.setString(5, title);
            issuePst.setTimestamp(6, issueTS);
            issuePst.setTimestamp(7, dueTS);
            issuePst.setString(8, "0");

            int result = issuePst.executeUpdate();

            if (result > 0) {
                // Update Book Availability to prevent others from borrowing the same copy
                String updateBookSql = "UPDATE books SET status = 'Unavailable' WHERE acquisition_no = ?";
                PreparedStatement updatePst = conn.prepareStatement(updateBookSql);
                updatePst.setString(1, acqNo);
                updatePst.executeUpdate();

                conn.commit();
                JOptionPane.showMessageDialog(this, "Book Issued Successfully!");
                populateBookTable("");
                clearTransactionFields();
                selectedMemberContact = ""; // Reset for next transaction
            }

        } catch (Exception e) {
            if (conn != null) try { conn.rollback(); } catch (SQLException ex) { ex.printStackTrace(); }
            JOptionPane.showMessageDialog(this, "Database Error: " + e.getMessage());
        } finally {
            if (conn != null) try { conn.setAutoCommit(true); } catch (SQLException e) { e.printStackTrace(); }
        }
    }//GEN-LAST:event_IssueBookBtnActionPerformed

    private void cancelBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelBtnActionPerformed
        clearTransactionFields();
    }//GEN-LAST:event_cancelBtnActionPerformed

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
        java.awt.EventQueue.invokeLater(() -> new borrow().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JToggleButton IssueBookBtn;
    private javax.swing.JToggleButton cancelBtn;
    private javax.swing.JButton jButton10;
    private javax.swing.JButton jButton9;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTable jTableBooks;
    private javax.swing.JTable jTableMembers;
    private javax.swing.JTextField txtAcq;
    private javax.swing.JFormattedTextField txtDueDate;
    private javax.swing.JTextField txtFullName;
    private javax.swing.JFormattedTextField txtIssueDate;
    private javax.swing.JTextField txtSearchBook;
    private javax.swing.JTextField txtSearchMember;
    private javax.swing.JTextField txtTitle;
    private javax.swing.JTextField txtUserType;
    // End of variables declaration//GEN-END:variables
}
