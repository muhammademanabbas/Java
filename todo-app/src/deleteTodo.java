import javax.swing.*;
import java.awt.*;
import javax.swing.table.DefaultTableModel;
import java.sql.*;

public class deleteTodo {
    JFrame deleteFrame;
    JPanel MainPanel = new JPanel();
    JPanel DeletePanel = new JPanel();
    JPanel buttonPanel = new JPanel();
    JTable table;
    JButton DeleteTodoBtn;
    JButton BackToMainPageBtn;
    Connection connection = null ;

    public deleteTodo() {
        
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/TodoListDatabase",
                    "root",
                    "eman");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(deleteFrame, "Error occurred: " + e.getMessage(), "Error",
                    JOptionPane.ERROR_MESSAGE);
        }

        deleteFrame = new JFrame("Delete Todo");
        deleteFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        deleteFrame.setBounds(300, 150, 400, 400);

        DeleteTodoBtn = new JButton("Delete Todo");
        BackToMainPageBtn = new JButton("Back");
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
        buttonPanel.add(DeleteTodoBtn);
        buttonPanel.add(BackToMainPageBtn);

        
        MainPanel.setLayout(new BoxLayout(MainPanel, BoxLayout.Y_AXIS));
        MainPanel.add(DeletePanel);
    }

    public void deleteFrame() {
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM tasks");
            DefaultTableModel model = new DefaultTableModel();
            model.addColumn("id");
            model.addColumn("Title");
            model.addColumn("Description");

            
            while (resultSet.next()) {
                String getidfromResult = resultSet.getString("id");
                String getTitlefromResult = resultSet.getString("title");
                String getDescriptionfromResult = resultSet.getString("description");
                model.addRow(new Object[] { getidfromResult, getTitlefromResult, getDescriptionfromResult });
            }
            table = new JTable(model);
            BoxLayout box = new BoxLayout(DeletePanel, BoxLayout.Y_AXIS);
            JScrollPane scrollPane = new JScrollPane(table);
            DeletePanel.setLayout(box);
            DeletePanel.add(scrollPane);
            DeletePanel.add(buttonPanel, new FlowLayout(FlowLayout.CENTER));
            deleteFrame.setContentPane(DeletePanel);
            deleteFrame.pack();
            deleteFrame.setVisible(true);

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(deleteFrame, "Error occurred: " + ex.getMessage(), "Error",
                    JOptionPane.ERROR_MESSAGE);
        }

        // Delete Button Functionality
        DeleteTodoBtn.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow != -1) { // Check if a row is selected
                try {
                    Class.forName("com.mysql.cj.jdbc.Driver");
                    String userIdToDelete = (String) table.getValueAt(selectedRow, 0);
                    // Get ID from selected row
                    PreparedStatement preparedStatement = connection.prepareStatement(
                            "DELETE FROM tasks WHERE id = ?");
                    preparedStatement.setString(1, userIdToDelete);
                    preparedStatement.executeUpdate();
                    refreshTable();
                    JOptionPane.showMessageDialog(deleteFrame, "Task Deleted Successful!");
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(deleteFrame, "Delete Button: " + ex.getMessage(), "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(deleteFrame, "Please select a task to delete", "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        });
        BackToMainPageBtn.addActionListener(e -> {
            deleteFrame.dispose();
            todo.main(null);
        });
    }
    private void refreshTable() {
        try {
            // Clear existing data from table model
            DefaultTableModel model = (DefaultTableModel) table.getModel();
            model.setRowCount(0);

            // Repopulate table with updated data
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM tasks");
            while (resultSet.next()) {
                String id = resultSet.getString("id");
                String title = resultSet.getString("title");
                String description = resultSet.getString("description");
                model.addRow(new Object[] { id, title, description });
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(deleteFrame, "Error occurred: " + ex.getMessage(), "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}