package library_hub;

import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;import javax.swing.table.DefaultTableModel;

public class dashboard extends javax.swing.JFrame {
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(dashboard.class.getName());
    
    public static String userRole = "";

     public dashboard(String role) {
        initComponents();
        handleRoleAndPermissions(role); // Use the helper method below
        loadDashboardData();
     }
    
    private void applyPermissions() {
    // We check the static variable to see if the user is a Librarian
    if ("Librarian".equalsIgnoreCase(dashboard.userRole) || "Staff".equalsIgnoreCase(dashboard.userRole)) {
        // These MUST match the 'Variable Name' in your NetBeans Design properties
        issueReportBtn.setVisible(false);
        addBookBtn.setVisible(false);
        userBtn.setVisible(false);
        penaltyBtn.setVisible(false);
        
        // Debugging: This will print to your NetBeans console so you can verify it's working
        System.out.println("Permissions Applied: Librarian buttons hidden.");
    }
    
    this.revalidate();
    this.repaint();
}
    
    
    
    public void handleRoleAndPermissions(String role) {
    // 1. Sync the Role to your global variable
    if (role != null && !role.isEmpty()) {
        dashboard.userRole = role; 
    }

    // 2. Set Visibility based on Role
    boolean isLibrarian = "Librarian".equalsIgnoreCase(dashboard.userRole);

    // LIBRARIANS see everything; STAFF only sees basic buttons
    // Management Buttons (Hidden for Staff)
    issueReportBtn.setVisible(isLibrarian);
    addBookBtn.setVisible(isLibrarian);
    userBtn.setVisible(isLibrarian);
    penaltyBtn.setVisible(isLibrarian);

    // Basic Buttons (Always visible for both)
    addMemberBtn.setVisible(true);
    borrowBookBtn.setVisible(true);
    issueBookBtn.setVisible(true);

    // 3. Debugging Output
    System.out.println("Permissions Sync: Role is " + dashboard.userRole);

    // 4. Refresh the UI
    this.revalidate();
    this.repaint();
    
    
    
}
    
public dashboard() {
    initComponents();
    handleRoleAndPermissions(null); 
    loadDashboardData();
    
    // Remove the individual if("Librarian") checks from here
    displayTotalMembers();
    displayTotalBooks();
    updateTotalBorrowed();
    displayTotalOverdue();
}
    
    
    public void loadDashboardData() {
    showMembersInTable();
    showBooksInTable();
    showIssuedBooksInTable();
    
    displayTotalMembers();
    displayTotalBooks();
    updateTotalBorrowed();
    displayTotalOverdue();
    // No setVisible code should be in here
}

public void showMembersInTable() {
    
    DefaultTableModel model = (DefaultTableModel) jTableMember.getModel();
    
    model.setRowCount(0); 

    try {
        Connection conn = MySQLConnect.getConnection();
        
        String sql = "SELECT fullname FROM member_records"; 
        PreparedStatement pst = conn.prepareStatement(sql);
        ResultSet rs = pst.executeQuery();

        while(rs.next()){

            String name = rs.getString("fullname");
            
            model.addRow(new Object[]{name});
        }
        
    } catch (Exception e) {
 
        javax.swing.JOptionPane.showMessageDialog(this, "Database Error: " + e.getMessage());
    }
}

public void showBooksInTable() {

    javax.swing.table.DefaultTableModel model = (javax.swing.table.DefaultTableModel) jTableBook.getModel();
    
    model.setRowCount(0); 

    try {

        java.sql.Connection conn = MySQLConnect.getConnection();
        
        String sql = "SELECT title FROM books"; 
        java.sql.PreparedStatement pst = conn.prepareStatement(sql);
        java.sql.ResultSet rs = pst.executeQuery();

        while(rs.next()){

            String title = rs.getString("title");
            
            model.addRow(new Object[]{title});
        }
        
    } catch (Exception e) {
        javax.swing.JOptionPane.showMessageDialog(this, "Book Table Error: " + e.getMessage());
    }
}

public void showIssuedBooksInTable() {

    javax.swing.table.DefaultTableModel model = (javax.swing.table.DefaultTableModel) jTable3.getModel();
    
    model.setRowCount(0); 

    try {
        java.sql.Connection conn = MySQLConnect.getConnection();
        
        String sql = "SELECT fullname, book_title, status FROM issued_books"; 
        java.sql.PreparedStatement pst = conn.prepareStatement(sql);
        java.sql.ResultSet rs = pst.executeQuery();

        while(rs.next()){
            String name = rs.getString("fullname");
            String title = rs.getString("book_title");
            String status = rs.getString("status");
            
            model.addRow(new Object[]{name, title, status});
        }
        
    } catch (Exception e) {
        javax.swing.JOptionPane.showMessageDialog(this, "Issued Table Error: " + e.getMessage());
    }
}

    public void updateTotalBorrowed() {
    try {
        Connection conn = MySQLConnect.getConnection();
        
        String sql = "SELECT COUNT(*) AS total FROM issued_books WHERE status IN ('Issued', 'Overdue')";
        
        
        
        PreparedStatement pst = conn.prepareStatement(sql);
        ResultSet rs = pst.executeQuery();
        
        if (rs.next()) {
            int count = rs.getInt("total");
            
            txtTotalBorrowed.setText(String.valueOf(count));
        }
    } catch (SQLException e) {
        JOptionPane.showMessageDialog(this, "Dashboard Error: " + e.getMessage());
    }
}
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        dashboardBtn = new javax.swing.JButton();
        addMemberBtn = new javax.swing.JButton();
        borrowBookBtn = new javax.swing.JButton();
        issueBookBtn = new javax.swing.JButton();
        issueReportBtn = new javax.swing.JButton();
        penaltyBtn = new javax.swing.JButton();
        addBookBtn = new javax.swing.JButton();
        userBtn = new javax.swing.JButton();
        logoutBtn = new javax.swing.JButton();
        jLabel13 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        txtTotalMember = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        txtTotalBook = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jPanel6 = new javax.swing.JPanel();
        txtTotalBorrowed = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jPanel7 = new javax.swing.JPanel();
        txtTotalOverdue = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTableMember = new javax.swing.JTable();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTableBook = new javax.swing.JTable();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTable3 = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(67, 83, 189));

        dashboardBtn.setBackground(new java.awt.Color(179, 248, 179));
        dashboardBtn.setFont(new java.awt.Font("Segoe UI Semibold", 0, 24)); // NOI18N
        dashboardBtn.setForeground(new java.awt.Color(0, 153, 0));
        dashboardBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/library_hub/home (1)_1.png"))); // NOI18N
        dashboardBtn.setText("DASHBOARD");
        dashboardBtn.addActionListener(this::dashboardBtnActionPerformed);

        addMemberBtn.setBackground(new java.awt.Color(179, 248, 179));
        addMemberBtn.setFont(new java.awt.Font("Segoe UI Semibold", 0, 24)); // NOI18N
        addMemberBtn.setForeground(new java.awt.Color(0, 153, 0));
        addMemberBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/library_hub/members.png"))); // NOI18N
        addMemberBtn.setText("ADD MEMBER");
        addMemberBtn.addActionListener(this::addMemberBtnActionPerformed);

        borrowBookBtn.setBackground(new java.awt.Color(179, 248, 179));
        borrowBookBtn.setFont(new java.awt.Font("Segoe UI Semibold", 0, 24)); // NOI18N
        borrowBookBtn.setForeground(new java.awt.Color(0, 153, 0));
        borrowBookBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/library_hub/borrow.png"))); // NOI18N
        borrowBookBtn.setText("BORROW BOOK");
        borrowBookBtn.addActionListener(this::borrowBookBtnActionPerformed);

        issueBookBtn.setBackground(new java.awt.Color(179, 248, 179));
        issueBookBtn.setFont(new java.awt.Font("Segoe UI Semibold", 0, 24)); // NOI18N
        issueBookBtn.setForeground(new java.awt.Color(0, 153, 0));
        issueBookBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/library_hub/issuebook.png"))); // NOI18N
        issueBookBtn.setText("ISSUE BOOK");
        issueBookBtn.addActionListener(this::issueBookBtnActionPerformed);

        issueReportBtn.setBackground(new java.awt.Color(179, 248, 179));
        issueReportBtn.setFont(new java.awt.Font("Segoe UI Semibold", 0, 24)); // NOI18N
        issueReportBtn.setForeground(new java.awt.Color(0, 153, 0));
        issueReportBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/library_hub/issuereport.png"))); // NOI18N
        issueReportBtn.setText("ISSUE REPORT");
        issueReportBtn.addActionListener(this::issueReportBtnActionPerformed);

        penaltyBtn.setBackground(new java.awt.Color(179, 248, 179));
        penaltyBtn.setFont(new java.awt.Font("Segoe UI Semibold", 0, 24)); // NOI18N
        penaltyBtn.setForeground(new java.awt.Color(0, 153, 0));
        penaltyBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/library_hub/record-book.png"))); // NOI18N
        penaltyBtn.setText("PENALTY");
        penaltyBtn.addActionListener(this::penaltyBtnActionPerformed);

        addBookBtn.setBackground(new java.awt.Color(179, 248, 179));
        addBookBtn.setFont(new java.awt.Font("Segoe UI Semibold", 0, 24)); // NOI18N
        addBookBtn.setForeground(new java.awt.Color(0, 153, 0));
        addBookBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/library_hub/add (1).png"))); // NOI18N
        addBookBtn.setText("ADD BOOK");
        addBookBtn.addActionListener(this::addBookBtnActionPerformed);

        userBtn.setBackground(new java.awt.Color(179, 248, 179));
        userBtn.setFont(new java.awt.Font("Segoe UI Semibold", 0, 24)); // NOI18N
        userBtn.setForeground(new java.awt.Color(0, 153, 0));
        userBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/library_hub/user1.png"))); // NOI18N
        userBtn.setText("USER");
        userBtn.addActionListener(this::userBtnActionPerformed);

        logoutBtn.setBackground(new java.awt.Color(179, 248, 179));
        logoutBtn.setFont(new java.awt.Font("Segoe UI Semibold", 0, 24)); // NOI18N
        logoutBtn.setForeground(new java.awt.Color(0, 153, 0));
        logoutBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/library_hub/log-out.png"))); // NOI18N
        logoutBtn.setText("LOGOUT");
        logoutBtn.addActionListener(this::logoutBtnActionPerformed);

        jLabel13.setIcon(new javax.swing.ImageIcon(getClass().getResource("/library_hub/westernlogo.png"))); // NOI18N

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(dashboardBtn, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(addMemberBtn, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(borrowBookBtn, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(issueReportBtn, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(penaltyBtn, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(addBookBtn, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(userBtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(issueBookBtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(logoutBtn, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 241, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(9, 9, 9)))
                .addContainerGap(17, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addComponent(jLabel13, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addComponent(dashboardBtn)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(addMemberBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(borrowBookBtn)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(issueBookBtn)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(issueReportBtn)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(penaltyBtn)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(addBookBtn)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(userBtn)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(logoutBtn)
                .addGap(58, 58, 58))
        );

        jPanel2.setBackground(new java.awt.Color(67, 83, 189));
        jPanel2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 102, 0)));

        jPanel3.setBackground(new java.awt.Color(177, 241, 255));
        jPanel3.setForeground(new java.awt.Color(0, 0, 0));

        jLabel2.setFont(new java.awt.Font("Trebuchet MS", 1, 36)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(0, 0, 0));
        jLabel2.setText("Western Leyte College ");

        jLabel3.setFont(new java.awt.Font("Trebuchet MS", 1, 36)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(0, 0, 0));
        jLabel3.setText("Library Hub System");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2)
                    .addComponent(jLabel3))
                .addContainerGap(799, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel3)
                .addContainerGap(23, Short.MAX_VALUE))
        );

        jPanel4.setBackground(new java.awt.Color(179, 248, 179));

        txtTotalMember.setEditable(false);
        txtTotalMember.setBackground(new java.awt.Color(179, 248, 179));
        txtTotalMember.setFont(new java.awt.Font("Segoe UI", 1, 50)); // NOI18N
        txtTotalMember.setForeground(new java.awt.Color(0, 0, 0));
        txtTotalMember.setBorder(null);
        txtTotalMember.addActionListener(this::txtTotalMemberActionPerformed);

        jLabel5.setFont(new java.awt.Font("Trebuchet MS", 1, 24)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(0, 153, 0));
        jLabel5.setText("Total Members");

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/library_hub/group (1).png"))); // NOI18N

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 198, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(txtTotalMember, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(26, 26, 26)
                        .addComponent(jLabel1)))
                .addContainerGap(28, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, 50, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtTotalMember, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addGap(13, 13, 13)))
                .addContainerGap())
        );

        jPanel5.setBackground(new java.awt.Color(255, 210, 135));

        txtTotalBook.setEditable(false);
        txtTotalBook.setBackground(new java.awt.Color(255, 210, 135));
        txtTotalBook.setFont(new java.awt.Font("Segoe UI", 1, 50)); // NOI18N
        txtTotalBook.setForeground(new java.awt.Color(0, 0, 0));
        txtTotalBook.setBorder(null);
        txtTotalBook.addActionListener(this::txtTotalBookActionPerformed);

        jLabel6.setFont(new java.awt.Font("Trebuchet MS", 1, 24)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(255, 153, 0));
        jLabel6.setText("Total Books");

        jLabel9.setIcon(new javax.swing.ImageIcon(getClass().getResource("/library_hub/books.png"))); // NOI18N

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 198, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(txtTotalBook, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 34, Short.MAX_VALUE)
                        .addComponent(jLabel9)
                        .addGap(18, 18, 18))))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, 43, Short.MAX_VALUE)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(txtTotalBook, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 48, Short.MAX_VALUE)
                        .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(16, 16, 16))))
        );

        jPanel6.setBackground(new java.awt.Color(199, 188, 255));

        txtTotalBorrowed.setEditable(false);
        txtTotalBorrowed.setBackground(new java.awt.Color(199, 188, 255));
        txtTotalBorrowed.setFont(new java.awt.Font("Segoe UI", 1, 50)); // NOI18N
        txtTotalBorrowed.setForeground(new java.awt.Color(0, 0, 0));
        txtTotalBorrowed.setBorder(null);
        txtTotalBorrowed.addActionListener(this::txtTotalBorrowedActionPerformed);

        jLabel7.setFont(new java.awt.Font("Trebuchet MS", 1, 24)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(102, 102, 255));
        jLabel7.setText("Total Borrowed");

        jLabel10.setIcon(new javax.swing.ImageIcon(getClass().getResource("/library_hub/borrowbookk.png"))); // NOI18N

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 198, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(txtTotalBorrowed, javax.swing.GroupLayout.PREFERRED_SIZE, 149, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 31, Short.MAX_VALUE)
                        .addComponent(jLabel10)
                        .addGap(20, 20, 20))))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtTotalBorrowed, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel10, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel7.setBackground(new java.awt.Color(255, 177, 177));

        txtTotalOverdue.setEditable(false);
        txtTotalOverdue.setBackground(new java.awt.Color(255, 177, 177));
        txtTotalOverdue.setFont(new java.awt.Font("Segoe UI", 1, 50)); // NOI18N
        txtTotalOverdue.setForeground(new java.awt.Color(0, 0, 0));
        txtTotalOverdue.setBorder(null);
        txtTotalOverdue.addActionListener(this::txtTotalOverdueActionPerformed);

        jLabel8.setFont(new java.awt.Font("Trebuchet MS", 1, 24)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(255, 51, 51));
        jLabel8.setText("Total Overdue");

        jLabel11.setIcon(new javax.swing.ImageIcon(getClass().getResource("/library_hub/overdue.png"))); // NOI18N

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 198, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addComponent(txtTotalOverdue, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 29, Short.MAX_VALUE)
                        .addComponent(jLabel11)
                        .addGap(19, 19, 19))))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(jLabel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(txtTotalOverdue, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel11))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jTableMember.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null},
                {null},
                {null},
                {null},
                {null},
                {null},
                {null},
                {null},
                {null},
                {null},
                {null},
                {null},
                {null},
                {null},
                {null}
            },
            new String [] {
                "Member Name"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(jTableMember);

        jTableBook.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null},
                {null},
                {null},
                {null},
                {null},
                {null},
                {null},
                {null},
                {null},
                {null},
                {null},
                {null},
                {null},
                {null},
                {null}
            },
            new String [] {
                "Books"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane2.setViewportView(jTableBook);

        jTable3.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "Member Name", "Book Title", "Status"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane3.setViewportView(jTable3);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(11, 11, 11)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 280, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(27, 27, 27)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                            .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(27, 27, 27)
                                .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jScrollPane3))))
                .addContainerGap(15, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(29, 29, 29)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 343, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(29, 29, 29))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(14, Short.MAX_VALUE)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(26, 26, 26))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void dashboardBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_dashboardBtnActionPerformed
        dashboard w = new dashboard(dashboard.userRole);
        w.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_dashboardBtnActionPerformed

    private void addMemberBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addMemberBtnActionPerformed
        this.dispose();
        member w = new member();
        w.setVisible(true);
    }//GEN-LAST:event_addMemberBtnActionPerformed

    private void borrowBookBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_borrowBookBtnActionPerformed
        this.dispose();
        borrow w = new borrow();
        w.setVisible(true);
    }//GEN-LAST:event_borrowBookBtnActionPerformed

    private void issueBookBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_issueBookBtnActionPerformed
        this.dispose();
        issuebook w = new issuebook();
        w.setVisible(true);
    }//GEN-LAST:event_issueBookBtnActionPerformed

    private void issueReportBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_issueReportBtnActionPerformed
        this.dispose();
        issuereport_management w = new issuereport_management();
        w.setVisible(true);
    }//GEN-LAST:event_issueReportBtnActionPerformed

    private void penaltyBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_penaltyBtnActionPerformed
        this.dispose();
        penalty_up w = new penalty_up();
        w.setVisible(true);
    }//GEN-LAST:event_penaltyBtnActionPerformed

    private void addBookBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addBookBtnActionPerformed
        this.dispose();
        bookmanagement_update w = new bookmanagement_update();
        w.setVisible(true);
    }//GEN-LAST:event_addBookBtnActionPerformed

    private void userBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_userBtnActionPerformed

        /*if ("Admin".equals(userRole)) {

            new user_management().setVisible(true);
            this.dispose();
        } else {
            userBtn.setVisible(false);

            javax.swing.JOptionPane.showMessageDialog(this, "Access Denied.");
        }
        this.revalidate();

        this.repaint();*/

        this.dispose();
        user w = new user();
        w.setVisible(true);
    }//GEN-LAST:event_userBtnActionPerformed

    private void logoutBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_logoutBtnActionPerformed
        int response = javax.swing.JOptionPane.showConfirmDialog(this,
            "Are you sure you want to log out?",
            "Logout Confirmation",
            javax.swing.JOptionPane.YES_NO_OPTION,
            javax.swing.JOptionPane.QUESTION_MESSAGE);

        if (response == javax.swing.JOptionPane.YES_OPTION) {

            this.dispose();
            new libraryOrStaff().setVisible(true);
        }

    }//GEN-LAST:event_logoutBtnActionPerformed

    private void txtTotalMemberActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtTotalMemberActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtTotalMemberActionPerformed

    private void txtTotalBookActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtTotalBookActionPerformed
        //this.dispose();
        //book_management_copy w = new book_management();
        //w.setVisible(true);
    }//GEN-LAST:event_txtTotalBookActionPerformed

    private void txtTotalBorrowedActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtTotalBorrowedActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtTotalBorrowedActionPerformed

    private void txtTotalOverdueActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtTotalOverdueActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtTotalOverdueActionPerformed

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
        java.awt.EventQueue.invokeLater(() -> new dashboard().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addBookBtn;
    private javax.swing.JButton addMemberBtn;
    private javax.swing.JButton borrowBookBtn;
    private javax.swing.JButton dashboardBtn;
    private javax.swing.JButton issueBookBtn;
    private javax.swing.JButton issueReportBtn;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
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
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTable jTable3;
    private javax.swing.JTable jTableBook;
    private javax.swing.JTable jTableMember;
    private javax.swing.JButton logoutBtn;
    private javax.swing.JButton penaltyBtn;
    private javax.swing.JTextField txtTotalBook;
    private javax.swing.JTextField txtTotalBorrowed;
    private javax.swing.JTextField txtTotalMember;
    private javax.swing.JTextField txtTotalOverdue;
    private javax.swing.JButton userBtn;
    // End of variables declaration//GEN-END:variables

    public void displayTotalMembers() {
        try {
        Connection con = MySQLConnect.getConnection(); 
        String sql = "SELECT COUNT(*) FROM member_records";
        PreparedStatement pst = con.prepareStatement(sql);
        ResultSet rs = pst.executeQuery();

        if (rs.next()) {
            txtTotalMember.setText(String.valueOf(rs.getInt(1))); 
        }
    } catch (Exception e) {
        System.out.println("Member Count Error: " + e.getMessage());
    }
    }
public void displayTotalBooks() {
        try {
        Connection conn = MySQLConnect.getConnection(); 
        String sql = "SELECT COUNT(*) FROM books";
        PreparedStatement pst = conn.prepareStatement(sql);
        ResultSet rs = pst.executeQuery();

        if (rs.next()) {
            txtTotalBook.setText(String.valueOf(rs.getInt(1)));
        }
    } catch (Exception e) {
        System.out.println("Book Count Error: " + e.getMessage());
    }
    }

public void displayTotalOverdue() {
    try {
        Connection conn = MySQLConnect.getConnection();
        
        // This version looks for both 'Issued' and 'Overdue' statuses
        String sql = "SELECT COUNT(*) FROM issued_books " +
                     "WHERE (status = 'Issued' OR status = 'Overdue') " +
                     "AND due_date < NOW()";
        
        PreparedStatement pst = conn.prepareStatement(sql);
        ResultSet rs = pst.executeQuery();

        if (rs.next()) {
            txtTotalOverdue.setText(String.valueOf(rs.getInt(1)));
        }
    } catch (Exception e) {
        System.out.println("Overdue Calculation Error: " + e.getMessage());
    }
}


    
}
