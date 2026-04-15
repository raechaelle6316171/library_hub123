package library_hub;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

public class book_management_copy extends javax.swing.JFrame {
    
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(book_management_copy.class.getName());
    
    private int id;
    private String check;
    public book_management_copy(){
        initComponents();
        MySQLConnect.getConnection(); // Make sure your DB connects
        populateTable();              // Load your data
        setDefault();
}   
    
    public void updateBookCount() {
    // Get the number of rows currently in the table
    int totalRows = jTable2.getRowCount();
    
    // Display it in your orange box (txtTotal is the variable name)
    // .setText requires a String, so we use String.valueOf()
    txtTotalCount.setText(String.valueOf(totalRows));
}
    
    public void search(String str) {
    DefaultTableModel model = (DefaultTableModel) jTable2.getModel();
    model.setRowCount(0); // Clear table
    
    try {
        Connection conn = MySQLConnect.getConnection();
        
        // This query checks Acquisition No, Title, OR Author for the same search string
        String sql = "SELECT * FROM books WHERE acquisition_no LIKE ? "
                   + "OR title LIKE ? "
                   + "OR author LIKE ?";
        
        PreparedStatement pst = conn.prepareStatement(sql);
        
        // Set the search string for all three placeholders (?)
        String searchData = "%" + str + "%";
        pst.setString(1, searchData);
        pst.setString(2, searchData);
        pst.setString(3, searchData);
        
        ResultSet rs = pst.executeQuery();
        
        while(rs.next()) {
            model.addRow(new Object[]{
                rs.getString("id"),
                rs.getString("acquisition_no"),
                rs.getString("title"),
                rs.getString("author"),
                rs.getString("date_published"),
                rs.getString("category"),
                rs.getString("status")
            });
        }
        updateBookCount();; // Update the Total Book No display
        
    } catch (SQLException e) {
        JOptionPane.showMessageDialog(null, "Search Error: " + e.getMessage());
    }
}
    public void populateTable(){
        int colCount;
        try{
            Connection conn = MySQLConnect.getConnection();
            Statement st = conn.createStatement();
            String query = "SELECT * FROM books";
            ResultSet rs = st.executeQuery(query);
            ResultSetMetaData rsData = rs.getMetaData();
            colCount = rsData.getColumnCount();
            
            DefaultTableModel tblModel = (DefaultTableModel) jTable2.getModel();
            tblModel.setRowCount(0);
            while(rs.next()){
                Vector colData = new Vector();
                for(int i = 1; i < colCount; i++){
                    colData.add(rs.getInt("id"));
                    colData.add(rs.getString("acquisition_no"));
                    colData.add(rs.getString("title"));
                    colData.add(rs.getString("author"));
                    colData.add(rs.getString("date_published"));
                    colData.add(rs.getString("category"));
                    colData.add(rs.getString("status"));
                }
                tblModel.addRow(colData);
            }
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null, e);
            
        }
        updateBookCount();
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
        addNewBtn = new javax.swing.JButton();
        updateBtn = new javax.swing.JButton();
        deleteBtn = new javax.swing.JButton();
        saveBtn = new javax.swing.JButton();
        jLabel10 = new javax.swing.JLabel();
        cmbStatus = new javax.swing.JComboBox<>();
        txtAuthor = new javax.swing.JTextField();
        txtDate = new javax.swing.JTextField();
        closeBtn = new javax.swing.JButton();
        txtSearchUsername = new javax.swing.JPanel();
        txtSearch = new javax.swing.JTextField();
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

        addNewBtn.setText("ADD NEW");
        addNewBtn.addActionListener(this::addNewBtnActionPerformed);

        updateBtn.setText("UPDATE");
        updateBtn.addActionListener(this::updateBtnActionPerformed);

        deleteBtn.setText("DELETE");
        deleteBtn.addActionListener(this::deleteBtnActionPerformed);

        saveBtn.setText("SAVE");
        saveBtn.addActionListener(this::saveBtnActionPerformed);

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

        closeBtn.setText("CANCEL");
        closeBtn.addActionListener(this::closeBtnActionPerformed);

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
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
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(deleteBtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(addNewBtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(updateBtn, javax.swing.GroupLayout.DEFAULT_SIZE, 83, Short.MAX_VALUE)
                            .addComponent(saveBtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(22, 22, 22))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                        .addComponent(closeBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(64, 64, 64))))
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
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(addNewBtn)
                    .addComponent(updateBtn))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(deleteBtn)
                    .addComponent(saveBtn))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(closeBtn)
                .addContainerGap(42, Short.MAX_VALUE))
        );

        txtSearchUsername.setBackground(new java.awt.Color(0, 102, 102));
        txtSearchUsername.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 5));

        txtSearch.addActionListener(this::txtSearchActionPerformed);
        txtSearch.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtSearchKeyReleased(evt);
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

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        txtTotalCount.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        txtTotalCount.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        txtTotalCount.addActionListener(this::txtTotalCountActionPerformed);

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(txtTotalCount, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addGap(0, 0, Short.MAX_VALUE))))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(txtTotalCount, javax.swing.GroupLayout.DEFAULT_SIZE, 44, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jLabel9.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(255, 255, 255));
        jLabel9.setText("SEARCH TITLE");

        jTable2.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null}
            },
            new String [] {
                "user ID", "ACQUSITION No", "BOOK TITLE", "BOOK AUTHOR", "DATE PUBLISHED", "CATEGORY", "STATUS"
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
                    .addComponent(txtSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 146, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(37, 37, 37))
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
                        .addComponent(txtSearch, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(32, 32, 32)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 274, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(83, 83, 83))
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
                .addComponent(txtSearchUsername, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents
    
    public void makeEnable(){
    
        txtAcq.setEnabled(true);
        txtTitle.setEnabled(true);
        txtAuthor.setEnabled(true);
        txtDate.setEnabled(true);
        cmbCategory.setEnabled(true);
        cmbStatus.setEnabled(true);
    }
    public void setDefault() {
    // 1. Clear everything
    txtAcq.setText("");
    txtTitle.setText("");
    txtAuthor.setText("");
    txtDate.setText("");
    cmbCategory.setSelectedIndex(0);
    cmbStatus.setSelectedIndex(0);

    // 2. Disable all input fields
    txtAcq.setEnabled(false);
    txtTitle.setEnabled(false);
    txtAuthor.setEnabled(false);
    txtDate.setEnabled(false);
    cmbCategory.setEnabled(false);
    cmbStatus.setEnabled(false);
    
    // 3. Button States for Startup/Cancel
    addNewBtn.setEnabled(true);   // Only Add New is clickable
    updateBtn.setEnabled(false);  // Update is greyed out
    deleteBtn.setEnabled(false);  // Delete is greyed out
    saveBtn.setEnabled(false);    // Save is greyed out
    
    id = -1; // Reset ID
}
    
    
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

    private void addNewBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addNewBtnActionPerformed
        // 1. Unlock everything
    txtAcq.setEnabled(true);
    txtTitle.setEnabled(true);
    txtAuthor.setEnabled(true);
    txtDate.setEnabled(true);
    cmbCategory.setEnabled(true);
    cmbStatus.setEnabled(true);
    
    // 2. UI Logic
    saveBtn.setEnabled(true);     // Now they can save the new book
    addNewBtn.setEnabled(false);  // Disable this so they don't click it twice
    updateBtn.setEnabled(false);
    deleteBtn.setEnabled(false);
    
    txtAcq.requestFocus();        // Cursor jumps to the first box
    }//GEN-LAST:event_addNewBtnActionPerformed

    private void updateBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_updateBtnActionPerformed
        try {
    Connection conn = MySQLConnect.getConnection();
    
    // 1. Prepare the SQL using 'id' (the primary key we grabbed in MouseClicked)
    String sql = "UPDATE books SET acquisition_no=?, title=?, author=?, date_published=?, category=?, status=? WHERE id=?";
    PreparedStatement pst = conn.prepareStatement(sql);
    
    // 2. Set the values from your text boxes
    pst.setString(1, txtAcq.getText());
    pst.setString(2, txtTitle.getText());
    pst.setString(3, txtAuthor.getText());
    pst.setString(4, txtDate.getText());
    pst.setObject(5, cmbCategory.getSelectedItem());
    pst.setObject(6, cmbStatus.getSelectedItem());
    pst.setInt(7, id); // Use the global 'id' variable
    
    // 3. EXECUTE the update
    int k = pst.executeUpdate();
    
    if(k == 1) {
        JOptionPane.showMessageDialog(this, "Book Updated Successfully!");
        
        // 4. REFRESH everything so the greyed-out boxes reset
        populateTable(); 
        setDefault(); 
    } else {
        JOptionPane.showMessageDialog(this, "Update Failed");
    }

} catch (SQLException ex) {
    JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
}
    }//GEN-LAST:event_updateBtnActionPerformed

    private void deleteBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteBtnActionPerformed
        if (JOptionPane.showConfirmDialog(null, "Are you sure you want to delete this?", "Confirm", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
    try {
        Connection conn = MySQLConnect.getConnection();
        
        // 1. Double check that a row is actually selected in jTable2
        int row = jTable2.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(null, "Please select a book from the table first!");
            return;
        }

        // 2. Use the 'id' variable that was set when you clicked the table
        // We use pst.setInt because your 'id' is an Integer
        String query = "DELETE FROM books WHERE id = ?"; 
        PreparedStatement pst = conn.prepareStatement(query);
        pst.setInt(1, id); 
        
        int success = pst.executeUpdate();
        
        if (success > 0) {
            JOptionPane.showMessageDialog(null, "Book deleted successfully!");
            
            // 3. Refresh the UI
            populateTable(); // This reloads the table from the database
            setDefault();    // This clears the boxes and resets the buttons
        } else {
            JOptionPane.showMessageDialog(null, "Delete failed. Could not find that ID.");
        }

    } catch(SQLException e) {
        JOptionPane.showMessageDialog(null, "Database Error: " + e.getMessage());
    }
}
    }//GEN-LAST:event_deleteBtnActionPerformed

    private void saveBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveBtnActionPerformed
        String acqNo = txtAcq.getText().trim();
    
    // 1. Basic Validation
    if (acqNo.isEmpty() || txtTitle.getText().isEmpty()) {
        JOptionPane.showMessageDialog(this, "Please fill all fields!");
        return;
    }

    try {
        Connection conn = MySQLConnect.getConnection();

        // 2. DUPLICATE CHECK
        String checkQuery = "SELECT * FROM books WHERE acquisition_no = ?";
        PreparedStatement checkPst = conn.prepareStatement(checkQuery);
        checkPst.setString(1, acqNo);
        ResultSet rs = checkPst.executeQuery();

        if (rs.next()) {
            // If rs.next() is true, it means the number was found!
            JOptionPane.showMessageDialog(this, "Error: Acquisition No " + acqNo + " already exists!", "Duplicate Entry", JOptionPane.ERROR_MESSAGE);
            txtAcq.requestFocus();
            return; // STOP the code here
        }

        // 3. IF NO DUPLICATE, PROCEED TO SAVE
        String sql = "INSERT INTO books (acquisition_no, title, author, date_published, category, status) VALUES (?,?,?,?,?,?)";
        PreparedStatement pst = conn.prepareStatement(sql);
        
        pst.setString(1, acqNo);
        pst.setString(2, txtTitle.getText());
        pst.setString(3, txtAuthor.getText());
        pst.setString(4, txtDate.getText());
        pst.setObject(5, cmbCategory.getSelectedItem());
        pst.setObject(6, cmbStatus.getSelectedItem());

        pst.executeUpdate();
        
        JOptionPane.showMessageDialog(this, "Book Added Successfully!");
        
        populateTable(); // Refresh table
        setDefault();    // Reset UI
        
    } catch (SQLException e) {
        JOptionPane.showMessageDialog(this, "Database Error: " + e.getMessage());
    }
    }//GEN-LAST:event_saveBtnActionPerformed

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

    private void txtSearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtSearchActionPerformed

    }//GEN-LAST:event_txtSearchActionPerformed

    private void txtSearchKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSearchKeyReleased
        search(txtSearch.getText()); // Replace txtSearch with your search field name

    }//GEN-LAST:event_txtSearchKeyReleased

    private void txtTotalCountActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtTotalCountActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtTotalCountActionPerformed

    private void jTable2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable2MouseClicked
        DefaultTableModel tblModel = (DefaultTableModel) jTable2.getModel();
    int selectedRow = jTable2.getSelectedRow();
    
    if (selectedRow != -1) {
        // Unlock the fields so they aren't greyed out
        txtAcq.setEnabled(true);
        txtTitle.setEnabled(true);
        txtAuthor.setEnabled(true);
        txtDate.setEnabled(true);
        cmbCategory.setEnabled(true);
        cmbStatus.setEnabled(true);

        // --- THE MAPPING (Matches your screenshot) ---
        
        // Column 0 = user ID
        id = Integer.parseInt(tblModel.getValueAt(selectedRow, 0).toString());
        
        // Column 1 = ACQUISITION No
        txtAcq.setText(tblModel.getValueAt(selectedRow, 1).toString());
        
        // Column 2 = BOOK TITLE
        txtTitle.setText(tblModel.getValueAt(selectedRow, 2).toString());
        
        // Column 3 = BOOK AUTHOR
        txtAuthor.setText(tblModel.getValueAt(selectedRow, 3).toString());
        
        // Column 4 = DATE PUBLISHED
        txtDate.setText(tblModel.getValueAt(selectedRow, 4).toString());
        
        // Column 5 = CATEGORY
        cmbCategory.setSelectedItem(tblModel.getValueAt(selectedRow, 5).toString());
        
        // Column 6 = STATUS
        cmbStatus.setSelectedItem(tblModel.getValueAt(selectedRow, 6).toString());

        // Button Logic
        addNewBtn.setEnabled(false);
        updateBtn.setEnabled(true);
        deleteBtn.setEnabled(true);
        saveBtn.setEnabled(false);
    }
    }//GEN-LAST:event_jTable2MouseClicked

    private void txtDateFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtDateFocusLost
        
    }//GEN-LAST:event_txtDateFocusLost

    private void closeBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_closeBtnActionPerformed
        //if(closeBtn.getText().equals("Close")){
        //    System.exit(0);
        //} else {
        //    setDefault();
        //}
        // This calls the method that clears everything and resets the UI
        setDefault();

        // Safety: Clear the selection from the table so no row stays highlighted
        jTable2.clearSelection();
    }//GEN-LAST:event_closeBtnActionPerformed
    
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
        java.awt.EventQueue.invokeLater(() -> new book_management_copy().setVisible(true));
    }
   
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addNewBtn;
    private javax.swing.JButton closeBtn;
    private javax.swing.JComboBox<String> cmbCategory;
    private javax.swing.JComboBox<String> cmbStatus;
    private javax.swing.JButton deleteBtn;
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
    private javax.swing.JButton saveBtn;
    private javax.swing.JTextField txtAcq;
    private javax.swing.JTextField txtAuthor;
    private javax.swing.JTextField txtDate;
    private javax.swing.JTextField txtSearch;
    private javax.swing.JPanel txtSearchUsername;
    private javax.swing.JTextField txtTitle;
    private javax.swing.JTextField txtTotalCount;
    private javax.swing.JButton updateBtn;
    // End of variables declaration//GEN-END:variables
}