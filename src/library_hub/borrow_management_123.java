package library_hub;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class borrow_management_123 extends javax.swing.JFrame {
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(borrow_management_123.class.getName());

    public borrow_management_123() {
        initComponents();
        populateMemberTable("");
        populateBookTable("");
    }
    
    public void setAutomaticDates() {

    LocalDateTime now = LocalDateTime.now();
    
    LocalDateTime deadline = now.plusHours(24);
    
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm");
    
    txtIssueDate.setText(now.format(formatter));
    txtDueDate.setText(deadline.format(formatter));
    
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
        
        if (query.isEmpty()) {
            sql = "SELECT acquisition_no, title, author, status FROM books WHERE status != 'Unavailable'";
        } else {
            sql = "SELECT acquisition_no, title, author, status FROM books " +
                  "WHERE (acquisition_no LIKE ? OR title LIKE ?) AND status != 'Unavailable'";
        }

        PreparedStatement pst = conn.prepareStatement(sql);
        if (!query.isEmpty()) {
            pst.setString(1, "%" + query + "%");
            pst.setString(2, "%" + query + "%"); // This handles the title search
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
        JOptionPane.showMessageDialog(this, "Error loading books: " + e.getMessage());
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

        jPanel2 = new javax.swing.JPanel();
        jLabel24 = new javax.swing.JLabel();
        jButton5 = new javax.swing.JButton();
        jPanel5 = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTableMembers = new javax.swing.JTable();
        txtSearchMember = new javax.swing.JTextField();
        IssueBookBtn1 = new javax.swing.JToggleButton();
        jPanel4 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTableBooks = new javax.swing.JTable();
        jLabel9 = new javax.swing.JLabel();
        txtSearchBook = new javax.swing.JTextField();
        IssueBookBtn2 = new javax.swing.JToggleButton();
        jPanel3 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        txtIssueDate = new javax.swing.JFormattedTextField();
        jLabel5 = new javax.swing.JLabel();
        txtDueDate = new javax.swing.JFormattedTextField();
        jLabel6 = new javax.swing.JLabel();
        txtFullName = new javax.swing.JTextField();
        txtUserType = new javax.swing.JTextField();
        txtTitle = new javax.swing.JTextField();
        txtAcq = new javax.swing.JTextField();
        IssueBookBtn = new javax.swing.JToggleButton();
        cancelBtn = new javax.swing.JToggleButton();
        jPanel1 = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        cancelBtn2 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel2.setBackground(new java.awt.Color(255, 102, 0));

        jLabel24.setFont(new java.awt.Font("Century Gothic", 1, 24)); // NOI18N
        jLabel24.setForeground(new java.awt.Color(0, 0, 0));
        jLabel24.setText("BORROW MANAGEMENT");

        jButton5.setText("x");
        jButton5.addActionListener(this::jButton5ActionPerformed);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(316, 316, 316)
                .addComponent(jLabel24)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 314, Short.MAX_VALUE)
                .addComponent(jButton5)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel24)
                    .addComponent(jButton5))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel5.setBackground(new java.awt.Color(51, 51, 51));
        jPanel5.setForeground(new java.awt.Color(0, 204, 153));
        jPanel5.setPreferredSize(new java.awt.Dimension(500, 589));

        jLabel8.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(255, 255, 255));
        jLabel8.setText("Search Full Name:");

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

        txtSearchMember.addActionListener(this::txtSearchMemberActionPerformed);
        txtSearchMember.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtSearchMemberKeyReleased(evt);
            }
        });

        IssueBookBtn1.setBackground(new java.awt.Color(255, 102, 102));
        IssueBookBtn1.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        IssueBookBtn1.setText("CLICK HERE TO ADD NEW MEMBER");
        IssueBookBtn1.addActionListener(this::IssueBookBtn1ActionPerformed);

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel8)
                        .addGap(18, 18, 18)
                        .addComponent(txtSearchMember, javax.swing.GroupLayout.PREFERRED_SIZE, 229, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel5Layout.createSequentialGroup()
                        .addGap(25, 25, 25)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 401, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(30, Short.MAX_VALUE))
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(106, 106, 106)
                .addComponent(IssueBookBtn1)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(27, 27, 27)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(txtSearchMember, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 165, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(IssueBookBtn1)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel4.setBackground(new java.awt.Color(51, 51, 51));
        jPanel4.setForeground(new java.awt.Color(0, 204, 153));
        jPanel4.setPreferredSize(new java.awt.Dimension(500, 589));

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

        jLabel9.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(255, 255, 255));
        jLabel9.setText("Search Acq No:");

        txtSearchBook.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtSearchBookKeyReleased(evt);
            }
        });

        IssueBookBtn2.setBackground(new java.awt.Color(255, 102, 102));
        IssueBookBtn2.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        IssueBookBtn2.setForeground(new java.awt.Color(0, 0, 0));
        IssueBookBtn2.setText("CLICK HERE TO ADD NEW BOOKS");
        IssueBookBtn2.addActionListener(this::IssueBookBtn2ActionPerformed);

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(27, 27, 27)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addComponent(jLabel9)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(txtSearchBook))
                            .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 422, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(125, 125, 125)
                        .addComponent(IssueBookBtn2)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(26, 26, 26)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(txtSearchBook, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 167, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(IssueBookBtn2)
                .addContainerGap(14, Short.MAX_VALUE))
        );

        jPanel3.setBackground(new java.awt.Color(51, 51, 51));
        jPanel3.setForeground(new java.awt.Color(51, 51, 51));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("FULLNAME:");

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("USER TYPE:");

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("BOOK ACQ No:");

        jLabel4.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("BOOK TITLE:");

        jLabel5.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setText("ISSUE DATE:");

        jLabel6.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setText("DUE DATE:");

        txtFullName.setEditable(false);

        txtUserType.setEditable(false);

        txtTitle.setEditable(false);

        txtAcq.setEditable(false);
        txtAcq.addActionListener(this::txtAcqActionPerformed);

        IssueBookBtn.setBackground(new java.awt.Color(255, 102, 102));
        IssueBookBtn.setText("ISSUE BOOK");
        IssueBookBtn.addActionListener(this::IssueBookBtnActionPerformed);

        cancelBtn.setBackground(new java.awt.Color(153, 255, 153));
        cancelBtn.setText("CANCEL");
        cancelBtn.addActionListener(this::cancelBtnActionPerformed);

        jPanel1.setBackground(new java.awt.Color(255, 102, 0));

        jLabel7.setFont(new java.awt.Font("Segoe UI Emoji", 1, 24)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(0, 0, 0));
        jLabel7.setText("-TRANSACTION DETAILS-");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(303, 303, 303)
                .addComponent(jLabel7)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(13, Short.MAX_VALUE)
                .addComponent(jLabel7)
                .addContainerGap())
        );

        jLabel10.setForeground(new java.awt.Color(255, 255, 255));
        jLabel10.setText("(mm/dd/yyyy)");

        jLabel11.setForeground(new java.awt.Color(255, 255, 255));
        jLabel11.setText("(mm/dd/yyyy)");

        cancelBtn2.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        cancelBtn2.setText("←GO TO ISSUE BOOK");
        cancelBtn2.addActionListener(this::cancelBtn2ActionPerformed);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addGap(70, 70, 70)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtFullName, javax.swing.GroupLayout.PREFERRED_SIZE, 228, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtUserType, javax.swing.GroupLayout.PREFERRED_SIZE, 228, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 107, Short.MAX_VALUE)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtAcq, javax.swing.GroupLayout.PREFERRED_SIZE, 228, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtTitle, javax.swing.GroupLayout.PREFERRED_SIZE, 228, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(57, 57, 57))
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(349, 349, 349)
                .addComponent(IssueBookBtn)
                .addGap(38, 38, 38)
                .addComponent(cancelBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(cancelBtn2, javax.swing.GroupLayout.PREFERRED_SIZE, 195, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(28, 28, 28))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel10))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel11)))
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(txtDueDate, javax.swing.GroupLayout.PREFERRED_SIZE, 232, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtIssueDate, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 232, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(249, 249, 249))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(24, 24, 24)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(txtFullName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtUserType, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel3)
                            .addComponent(txtAcq, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addGap(2, 2, 2)
                                .addComponent(jLabel4))
                            .addComponent(txtTitle, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(11, 11, 11)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(txtIssueDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtDueDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel6)
                            .addComponent(jLabel11)))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(1, 1, 1)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel10)
                            .addComponent(jLabel5))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(IssueBookBtn)
                    .addComponent(cancelBtn)
                    .addComponent(cancelBtn2, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(24, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, 456, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, 479, Short.MAX_VALUE))
            .addComponent(jPanel3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, 292, Short.MAX_VALUE)
                    .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, 292, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jTableMembersMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTableMembersMouseClicked
        int row = jTableMembers.getSelectedRow();
        DefaultTableModel model = (DefaultTableModel) jTableMembers.getModel();

        txtFullName.setText(model.getValueAt(row, 0).toString());
        txtUserType.setText(model.getValueAt(row, 1).toString());
    }//GEN-LAST:event_jTableMembersMouseClicked

    private void txtSearchMemberActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtSearchMemberActionPerformed

    }//GEN-LAST:event_txtSearchMemberActionPerformed

    private void txtSearchMemberKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSearchMemberKeyReleased
        String search = txtSearchMember.getText();
        populateMemberTable(search);
    }//GEN-LAST:event_txtSearchMemberKeyReleased

    private void txtAcqActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtAcqActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtAcqActionPerformed

    private void cancelBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelBtnActionPerformed
        clearTransactionFields();
    }//GEN-LAST:event_cancelBtnActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        this.dispose();
        frontpage w = new frontpage();
        w.setVisible(true);
    }//GEN-LAST:event_jButton5ActionPerformed

    private void jTableBooksMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTableBooksMouseClicked
        int row = jTableBooks.getSelectedRow();
        DefaultTableModel model = (DefaultTableModel) jTableBooks.getModel();

        txtAcq.setText(model.getValueAt(row, 0).toString());
        txtTitle.setText(model.getValueAt(row, 1).toString());

        setAutomaticDates();

    }//GEN-LAST:event_jTableBooksMouseClicked

    private void jTableBooksKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTableBooksKeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_jTableBooksKeyReleased

    private void txtSearchBookKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSearchBookKeyReleased
        String query = txtSearchBook.getText().trim();
    
    DefaultTableModel model = (DefaultTableModel) jTableBooks.getModel(); // Make sure this is your book table name
    model.setRowCount(0); // Clear the table first

    try {
        Connection conn = MySQLConnect.getConnection();
        
        String sql = "SELECT * FROM books WHERE acquisition_no LIKE ? OR title LIKE ?";
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

    private void IssueBookBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_IssueBookBtnActionPerformed
        String name = txtFullName.getText().trim();
    String userType = txtUserType.getText().trim();
    String acqNo = txtAcq.getText().trim();
    String title = txtTitle.getText().trim();
    String iDate = txtIssueDate.getText().trim();
    String dDate = txtDueDate.getText().trim();

    // 1. Basic validation
    if (name.isEmpty() || acqNo.isEmpty() || iDate.isEmpty() || dDate.isEmpty()) {
        JOptionPane.showMessageDialog(this, "Please select a member, a book, and fill in the dates!");
        return;
    }

    try {
        Connection conn = MySQLConnect.getConnection();

        if (userType.equalsIgnoreCase("Student")) {
            String countSql = "SELECT COUNT(*) FROM issued_books WHERE fullname = ? AND status = 'Issued'"; 
            PreparedStatement countPst = conn.prepareStatement(countSql);
            countPst.setString(1, name);
            ResultSet rsCount = countPst.executeQuery();
            

            if (rsCount.next()) {
                int borrowedCount = rsCount.getInt(1);
                if (borrowedCount >= 3) {
                    JOptionPane.showMessageDialog(this, 
                        "Student Limit Reached: " + name + " already has " + borrowedCount + " books borrowed.", 
                        "Borrowing Limit", JOptionPane.WARNING_MESSAGE);
                    return; 
                }
            }
        }
        
        String duplicateSql = "SELECT COUNT(*) FROM issued_books WHERE fullname = ? AND book_title = ? AND status = 'Issued'";
        PreparedStatement duplicatePst = conn.prepareStatement(duplicateSql);
        duplicatePst.setString(1, name);
        duplicatePst.setString(2, title);
        ResultSet rsDuplicate = duplicatePst.executeQuery();

        if (rsDuplicate.next()) {
            if (rsDuplicate.getInt(1) > 0) {
                JOptionPane.showMessageDialog(this, 
                    "DUPLICATE DETECTED: " + name + " already has an active copy of '" + title + "'.\n" +
                    "They must return the existing copy before borrowing another one.", 
                    "Borrowing Denied", JOptionPane.ERROR_MESSAGE);
                return; 
            }
        }

        String checkSql = "SELECT status FROM books WHERE acquisition_no = ?";
        PreparedStatement checkPst = conn.prepareStatement(checkSql);
        checkPst.setString(1, acqNo);
        ResultSet rsBook = checkPst.executeQuery();

        if (rsBook.next()) {
            if (rsBook.getString("status").equalsIgnoreCase("Unavailable")) {
                JOptionPane.showMessageDialog(this, "This book is currently unavailable/borrowed!");
                return;
            }
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm");
        LocalDateTime issueLDT = LocalDateTime.parse(txtIssueDate.getText(), formatter);
        java.sql.Timestamp issueTS = java.sql.Timestamp.valueOf(issueLDT);
        LocalDateTime dueLDT = LocalDateTime.parse(txtDueDate.getText(), formatter);
        java.sql.Timestamp dueTS = java.sql.Timestamp.valueOf(dueLDT);
        
        
        String issueSql = "INSERT INTO issued_books (fullname, usertype, book_acq_no, book_title, issue_date, due_date, penalty_paid, status) "
                        + "VALUES (?, ?, ?, ?, ?, ?, ?, 'Issued')";

        PreparedStatement issuePst = conn.prepareStatement(issueSql);
        issuePst.setString(1, name);
        issuePst.setString(2, userType);
        issuePst.setString(3, acqNo);
        issuePst.setString(4, title);
        issuePst.setTimestamp(5, issueTS);
        issuePst.setTimestamp(6, dueTS);        
        issuePst.setString(7, "0"); // Manually setting initial penalty to 0 to avoid DB errors

        int result = issuePst.executeUpdate();

        if (result > 0) {
            String updateBookSql = "UPDATE books SET status = 'Unavailable' WHERE acquisition_no = ?";
            PreparedStatement updatePst = conn.prepareStatement(updateBookSql);
            updatePst.setString(1, acqNo);
            updatePst.executeUpdate();

            JOptionPane.showMessageDialog(this, "Book Issued Successfully!");

            populateBookTable(""); 
            clearTransactionFields();
        }

    } catch (SQLException e) {
        JOptionPane.showMessageDialog(this, "Database Error: " + e.getMessage());
        e.printStackTrace();
    }
    }//GEN-LAST:event_IssueBookBtnActionPerformed

    private void IssueBookBtn1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_IssueBookBtn1ActionPerformed
        this.dispose();
        register_member_try w = new register_member_try();
        w.setVisible(true);
    }//GEN-LAST:event_IssueBookBtn1ActionPerformed

    private void IssueBookBtn2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_IssueBookBtn2ActionPerformed
        this.dispose();
        book_management_copy w = new book_management_copy();
        w.setVisible(true);
    }//GEN-LAST:event_IssueBookBtn2ActionPerformed

    private void cancelBtn2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelBtn2ActionPerformed
        this.dispose();
        issuebook_management w = new issuebook_management();
        w.setVisible(true);
    }//GEN-LAST:event_cancelBtn2ActionPerformed

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
        java.awt.EventQueue.invokeLater(() -> new borrow_management_123().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JToggleButton IssueBookBtn;
    private javax.swing.JToggleButton IssueBookBtn1;
    private javax.swing.JToggleButton IssueBookBtn2;
    private javax.swing.JToggleButton cancelBtn;
    private javax.swing.JButton cancelBtn2;
    private javax.swing.JButton jButton5;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
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
