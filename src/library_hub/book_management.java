package library_hub;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

public class book_management extends javax.swing.JFrame {
    
    private borrow_management parentForm;
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(book_management.class.getName());
    
    public book_management() {
        initComponents();
        updateJTable();
    }
    // Add this second constructor
    public book_management(borrow_management parent) {
        this.parentForm = parent;
        initComponents();
        updateJTable();
        // Hide buttons that shouldn't be used during "Selection Mode"
        addBtn.setVisible(false);
        editBtn.setVisible(false);
        deleteBtn.setVisible(false);
    }
    
    private Connection getConnection() throws SQLException {
    
    String url = "jdbc:mysql://localhost:3306/libraryhub";
    String dbUser = "root";
    String dbPass = "";
    return DriverManager.getConnection(url, dbUser,dbPass );
}   


// Helper method to reuse the clearing logic
    private void clearFields() {
        txtAcq.setText("");
        txtTitle.setText("");
        txtAuthor.setText("");
        txtDate.setText("");
        cmbCategory.setSelectedIndex(0);
        cmbStatus.setSelectedIndex(0);
    }
    
    private boolean checkAcquisitionExists(String acqNo) {
    boolean exists = false;
    String checkQuery = "SELECT COUNT(*) FROM books WHERE acquisition_no = ?";
    
    try (Connection con = getConnection();
         PreparedStatement pst = con.prepareStatement(checkQuery)) {
        
        pst.setString(1, acqNo);
        ResultSet rs = pst.executeQuery();
        
        if (rs.next()) {
            // if count > 0, it means it's already there
            exists = rs.getInt(1) > 0;
        }
    } catch (SQLException ex) {
        System.err.println("Validation Error: " + ex.getMessage());
    }
    return exists;
}
    
    private String formatDate(String input) {
    String digits = input.replaceAll("[^0-9]", ""); // Strip everything but numbers

    
    //User typed 8 digits (e.g., 09112001)
    if (digits.length() == 8) {
        return digits.substring(0, 2) + "/" + 
               digits.substring(2, 4) + "/" + 
               digits.substring(4);
    }

    return input; // If they typed something weird, just return it
}

    private void updateTotal() {
        txtTotalCount.setText(String.valueOf(jTable2.getRowCount()));
    }
    
    
    public void updateJTable() {
    // Make sure your table variable name is jTable2
    DefaultTableModel model = (DefaultTableModel) jTable2.getModel();
    model.setRowCount(0); // Clear the table first

    try {
        Connection con = getConnection();
        Statement st = con.createStatement();
        ResultSet rs = st.executeQuery("SELECT * FROM books"); // 'books' is your table name

        while (rs.next()) {
            // Add rows based on your database columns
            model.addRow(new Object[]{
                rs.getString("acquisition_no"),
                rs.getString("title"),
                rs.getString("author"),
                rs.getString("date_published"),
                rs.getString("category"),
                rs.getString("status")
            });
        }
        
        // Update that orange box with the total count
        txtTotalCount.setText(String.valueOf(model.getRowCount()));
        
    } catch (Exception e) {
        JOptionPane.showMessageDialog(null, "Error loading data: " + e.getMessage());
    }
}
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel3 = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        jButton5 = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        txtAcq = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        txtTitle = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        cmbCategory = new javax.swing.JComboBox<>();
        addBtn = new javax.swing.JButton();
        editBtn = new javax.swing.JButton();
        deleteBtn = new javax.swing.JButton();
        cancelBtn = new javax.swing.JButton();
        jLabel10 = new javax.swing.JLabel();
        cmbStatus = new javax.swing.JComboBox<>();
        txtAuthor = new javax.swing.JTextField();
        txtDate = new javax.swing.JTextField();
        txtSearchUsername = new javax.swing.JPanel();
        search = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        jPanel6 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        txtTotalCount = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTable2 = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel3.setBackground(new java.awt.Color(0, 102, 102));
        jPanel3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 5));
        jPanel3.setPreferredSize(new java.awt.Dimension(285, 53));

        jLabel8.setFont(new java.awt.Font("Century Gothic", 1, 24)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(255, 255, 255));
        jLabel8.setText("BOOK MANAGEMENT");

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

        jPanel4.setBackground(new java.awt.Color(0, 102, 102));
        jPanel4.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 5));

        jLabel2.setBackground(new java.awt.Color(0, 102, 102));
        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("ACQUISITION No");

        txtAcq.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txtAcqMouseClicked(evt);
            }
        });
        txtAcq.addActionListener(this::txtAcqActionPerformed);
        txtAcq.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtAcqKeyReleased(evt);
            }
        });

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("BOOK TITLE");

        txtTitle.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txtTitleMouseClicked(evt);
            }
        });
        txtTitle.addActionListener(this::txtTitleActionPerformed);

        jLabel4.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("BOOK AUTHOR");

        jLabel5.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setText("DATE PUBLISHED");

        jLabel6.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setText("SELECT CATEGORY");

        cmbCategory.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "~Select Category~", "Math", "English", "Science", "Filipino" }));
        cmbCategory.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                cmbCategoryMouseClicked(evt);
            }
        });
        cmbCategory.addActionListener(this::cmbCategoryActionPerformed);

        addBtn.setText("ADD");
        addBtn.addActionListener(this::addBtnActionPerformed);

        editBtn.setText("EDIT");
        editBtn.addActionListener(this::editBtnActionPerformed);

        deleteBtn.setText("DELETE");
        deleteBtn.addActionListener(this::deleteBtnActionPerformed);

        cancelBtn.setText("CANCEL");
        cancelBtn.addActionListener(this::cancelBtnActionPerformed);

        jLabel10.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(255, 255, 255));
        jLabel10.setText("STATUS");

        cmbStatus.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "~Select Status~", "Available", "Unavailable" }));
        cmbStatus.addActionListener(this::cmbStatusActionPerformed);

        txtAuthor.addActionListener(this::txtAuthorActionPerformed);

        txtDate.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createTitledBorder("Date (MM/DD/YYYY)")));
        txtDate.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtDateFocusLost(evt);
            }
        });
        txtDate.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txtDateMouseClicked(evt);
            }
        });
        txtDate.addActionListener(this::txtDateActionPerformed);

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(deleteBtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(addBtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(cancelBtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(editBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(22, 22, 22))
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(35, 35, 35)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 179, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 179, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 179, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 179, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 179, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 179, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cmbStatus, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cmbCategory, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtTitle, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtAcq, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtAuthor, javax.swing.GroupLayout.PREFERRED_SIZE, 146, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtDate, javax.swing.GroupLayout.PREFERRED_SIZE, 143, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtAcq, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtTitle, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtAuthor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(txtDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cmbCategory, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel10)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cmbStatus, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 26, Short.MAX_VALUE)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(addBtn)
                    .addComponent(editBtn))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(deleteBtn)
                    .addComponent(cancelBtn))
                .addGap(24, 24, 24))
        );

        txtSearchUsername.setBackground(new java.awt.Color(0, 102, 102));
        txtSearchUsername.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 5));

        search.addActionListener(this::searchActionPerformed);
        search.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                searchKeyReleased(evt);
            }
        });

        jLabel7.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(255, 255, 255));

        jPanel6.setBackground(new java.awt.Color(255, 102, 0));
        jPanel6.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel1.setText("TOTAL BOOK No");

        jPanel5.setBackground(new java.awt.Color(204, 204, 204));
        jPanel5.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        txtTotalCount.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        txtTotalCount.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        txtTotalCount.addActionListener(this::txtTotalCountActionPerformed);

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(txtTotalCount, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(txtTotalCount, javax.swing.GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jLabel9.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(255, 255, 255));
        jLabel9.setText("SEARCH TITLE");

        jTable2.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "ACQUSITION No", "BOOK TITLE", "BOOK AUTHOR", "DATE PUBLISHED", "CATEGORY", "STATUS"
            }
        ));
        jTable2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable2MouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(jTable2);

        javax.swing.GroupLayout txtSearchUsernameLayout = new javax.swing.GroupLayout(txtSearchUsername);
        txtSearchUsername.setLayout(txtSearchUsernameLayout);
        txtSearchUsernameLayout.setHorizontalGroup(
            txtSearchUsernameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(txtSearchUsernameLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(txtSearchUsernameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(txtSearchUsernameLayout.createSequentialGroup()
                        .addComponent(jLabel9)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel7))
                    .addComponent(search, javax.swing.GroupLayout.PREFERRED_SIZE, 146, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 661, Short.MAX_VALUE)
        );
        txtSearchUsernameLayout.setVerticalGroup(
            txtSearchUsernameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(txtSearchUsernameLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(txtSearchUsernameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(txtSearchUsernameLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addGroup(txtSearchUsernameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, txtSearchUsernameLayout.createSequentialGroup()
                                .addComponent(jLabel7)
                                .addGap(13, 13, 13))
                            .addComponent(jLabel9, javax.swing.GroupLayout.Alignment.TRAILING))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(search, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(32, 32, 32)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 274, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(102, 102, 102))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, 671, Short.MAX_VALUE)
                    .addComponent(txtSearchUsername, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtSearchUsername, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
            .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents
    
    
    
    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        this.dispose();
        frontpage w = new frontpage();
        w.setVisible(true);
    }//GEN-LAST:event_jButton5ActionPerformed

    private void txtAcqMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtAcqMouseClicked

    }//GEN-LAST:event_txtAcqMouseClicked

    private void txtAcqActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtAcqActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtAcqActionPerformed

    private void txtAcqKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtAcqKeyReleased

    }//GEN-LAST:event_txtAcqKeyReleased

    private void txtTitleMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtTitleMouseClicked

    }//GEN-LAST:event_txtTitleMouseClicked

    private void txtTitleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtTitleActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtTitleActionPerformed

    private void cmbCategoryMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_cmbCategoryMouseClicked

    }//GEN-LAST:event_cmbCategoryMouseClicked

    private void cmbCategoryActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbCategoryActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cmbCategoryActionPerformed

    private void addBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addBtnActionPerformed
                                   
    String query = "INSERT INTO books (acquisition_no, title, author, date_published, category, status) VALUES (?, ?, ?, ?, ?, ?)";
    
    
    String acqNo = txtAcq.getText().trim();

    // 1. Basic Validation: Make sure it's not empty
    if (acqNo.isEmpty()) {
        JOptionPane.showMessageDialog(this, "Acquisition Number is required!");
        return;
    }

    // 2. The Check: Does this ID already exist?
    if (checkAcquisitionExists(acqNo)) {
        JOptionPane.showMessageDialog(this, "Error: Acquisition No. " + acqNo + " already exists in the system!", "Duplicate Entry", JOptionPane.ERROR_MESSAGE);
        return; // STOP HERE! Don't run the insert.
    }
    try (Connection con = getConnection(); 
         PreparedStatement pst = con.prepareStatement(query)) {
        
        pst.setString(1, acqNo);
        pst.setString(2, txtTitle.getText());
        pst.setString(3, txtAuthor.getText()); // Put Author here!
        pst.setString(4, formatDate(txtDate.getText())); // Put Formatted Date here!
        pst.setString(5, cmbCategory.getSelectedItem().toString());
        pst.setString(6, cmbStatus.getSelectedItem().toString());

        pst.executeUpdate();
        JOptionPane.showMessageDialog(this, "Book Added!");
        
        updateJTable(); // Refresh UI
        clearFields();
    } catch (SQLException ex) {
        JOptionPane.showMessageDialog(this, "Add Failed: " + ex.getMessage());
    }
    }//GEN-LAST:event_addBtnActionPerformed

    private void editBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editBtnActionPerformed
     String query = "UPDATE books SET title=?, author=?, date_published=?, category=?, status=? WHERE acquisition_no=?";
     // Compare the current Text Field value to the value already in the Table
    int selectedRow = jTable2.getSelectedRow();
    boolean isSame = txtTitle.getText().equals(jTable2.getValueAt(selectedRow, 1).toString()) &&
    txtAuthor.getText().equals(jTable2.getValueAt(selectedRow, 2).toString()) &&
    txtDate.getText().equals(jTable2.getValueAt(selectedRow, 3).toString());
    if (isSame) {
        JOptionPane.showMessageDialog(this, "No changes detected.");
        return; // This STOPS the method so the code below never runs
    }   
    try (Connection con = getConnection(); 
         PreparedStatement pst = con.prepareStatement(query)) {
        
        pst.setString(1, txtTitle.getText());
        pst.setString(2, txtAuthor.getText());
        pst.setString(3, formatDate(txtDate.getText())); // Formatted Date
        pst.setString(4, cmbCategory.getSelectedItem().toString());
        pst.setString(5, cmbStatus.getSelectedItem().toString());
        pst.setString(6, txtAcq.getText());

        int updated = pst.executeUpdate();
        
        

        
        
        if (updated > 0) {
            JOptionPane.showMessageDialog(this, "Update Successful");
            updateJTable(); // This refreshes the table from the database
            clearFields();
        }else{
            JOptionPane.showMessageDialog(this, "No record found with that Acquisition No.");
        }
        
    } catch (SQLException ex) {
        JOptionPane.showMessageDialog(this, "Update Failed: " + ex.getMessage());
        }
    }//GEN-LAST:event_editBtnActionPerformed

    private void deleteBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteBtnActionPerformed
       DefaultTableModel model = (DefaultTableModel) jTable2.getModel();
        int selectedRow = jTable2.getSelectedRow();

        if(selectedRow >= 0) {
            // Get the ID from the selected row so we know WHİCH one to kill in MySQL
            String acqNo = model.getValueAt(selectedRow, 0).toString(); 

            int confirm = JOptionPane.showConfirmDialog(this, "Are you sure?", "Warning", JOptionPane.YES_NO_OPTION);

            if(confirm == JOptionPane.YES_OPTION) {
                // --- STEP 1: Delete from Database ---
                try (Connection con = getConnection(); 
                     PreparedStatement pst = con.prepareStatement("DELETE FROM books WHERE acquisition_no=?")) {

                    pst.setString(1, acqNo);
                    pst.executeUpdate(); 

                    // --- STEP 2: Update UI ---
                    updateJTable(); 
                    updateTotal();
                    clearFields();

                    JOptionPane.showMessageDialog(this, "Deleted from Database!");
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(this, "Database Error: " + ex.getMessage());
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select a row to delete!");
}
    }//GEN-LAST:event_deleteBtnActionPerformed

    private void cancelBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelBtnActionPerformed
    clearFields();
    jTable2.clearSelection();
    }//GEN-LAST:event_cancelBtnActionPerformed

    private void cmbStatusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbStatusActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cmbStatusActionPerformed

    private void txtAuthorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtAuthorActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtAuthorActionPerformed

    private void txtDateMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtDateMouseClicked

    }//GEN-LAST:event_txtDateMouseClicked

    private void txtDateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtDateActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtDateActionPerformed

    private void searchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_searchActionPerformed

    }//GEN-LAST:event_searchActionPerformed

    private void searchKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_searchKeyReleased
        
    }//GEN-LAST:event_searchKeyReleased

    private void txtTotalCountActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtTotalCountActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtTotalCountActionPerformed

    private void jTable2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable2MouseClicked
    int i = jTable2.getSelectedRow();
    DefaultTableModel model = (DefaultTableModel)jTable2.getModel();
    
    // Get the Acquisition No (Column 0)
    String acqNo = model.getValueAt(i, 0).toString(); 

    // If we opened this from the Borrow screen, send the data back!
    if (parentForm != null) {
        parentForm.setAcquisitionNo(acqNo); // This calls a method in borrow_management
        this.dispose(); // Closes the book list automatically
        return;
    }
    txtAcq.setText(model.getValueAt(i, 0).toString());
    txtTitle.setText(model.getValueAt(i, 1).toString());
    txtAuthor.setText(model.getValueAt(i, 2).toString());
    txtDate.setText(model.getValueAt(i, 3).toString());
    cmbCategory.setSelectedItem(model.getValueAt(i, 4).toString());
    cmbStatus.setSelectedItem(model.getValueAt(i, 5).toString());
    
    txtAcq.setEditable(false);
    }//GEN-LAST:event_jTable2MouseClicked

    private void txtDateFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtDateFocusLost
        // TODO add your handling code here:
        String input = txtDate.getText();
        if (input.length() == 8 && !input.contains("/")) {
            txtDate.setText(formatDate(input));
        }
    }//GEN-LAST:event_txtDateFocusLost
    
    
    
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
        java.awt.EventQueue.invokeLater(() -> new book_management().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addBtn;
    private javax.swing.JButton cancelBtn;
    private javax.swing.JComboBox<String> cmbCategory;
    private javax.swing.JComboBox<String> cmbStatus;
    private javax.swing.JButton deleteBtn;
    private javax.swing.JButton editBtn;
    private javax.swing.JButton jButton5;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable jTable2;
    private javax.swing.JTextField search;
    private javax.swing.JTextField txtAcq;
    private javax.swing.JTextField txtAuthor;
    private javax.swing.JTextField txtDate;
    private javax.swing.JPanel txtSearchUsername;
    private javax.swing.JTextField txtTitle;
    private javax.swing.JTextField txtTotalCount;
    // End of variables declaration//GEN-END:variables
}
