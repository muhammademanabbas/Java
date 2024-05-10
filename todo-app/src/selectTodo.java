import javax.swing.*;
import java.awt.*;
import javax.swing.table.DefaultTableModel;
import java.sql.*;

public class selectTodo {
    JFrame updateTodoFrame;
    JPanel MainPanel  ;
    JPanel updatePanel  ;
    JPanel buttonPanel  ;
    JTable table;
    JButton selectTodoBtn;
    JButton BackToMainPageBtn;
    Connection connection  ;

    public selectTodo() {
        MainPanel = new JPanel();
        updatePanel = new JPanel();
        buttonPanel = new JPanel();
        connection = null ;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/TodoListDatabase",
                    "root",
                    "eman");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(updateTodoFrame, "Error occurred: " + e.getMessage(), "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
        
        updateTodoFrame = new JFrame("Select Todo");
        updateTodoFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        updateTodoFrame.setBounds(300,150,400,160);

        selectTodoBtn = new JButton("Select");
        BackToMainPageBtn = new JButton("Back");
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
        buttonPanel.add(selectTodoBtn);
        buttonPanel.add(BackToMainPageBtn);

        
        MainPanel.setLayout(new BoxLayout(MainPanel, BoxLayout.Y_AXIS));
        MainPanel.add(updatePanel);
    }

    public void updateTodos() {
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM tasks");
            DefaultTableModel model = new DefaultTableModel();
            model.addColumn("No.");
            model.addColumn("Title");
            model.addColumn("Description");

            
            while (resultSet.next()) {
                String getidfromResult = resultSet.getString("id");
                String getTitlefromResult = resultSet.getString("title");
                String getDescriptionfromResult = resultSet.getString("description");
                model.addRow(new Object[] { getidfromResult, getTitlefromResult, getDescriptionfromResult });
            }
            table = new JTable(model);
            BoxLayout box = new BoxLayout(updatePanel, BoxLayout.Y_AXIS);
            JScrollPane scrollPane = new JScrollPane(table);
            updatePanel.setLayout(box);
            updatePanel.add(scrollPane);
            updatePanel.add(buttonPanel, new FlowLayout(FlowLayout.CENTER));
            updateTodoFrame.setContentPane(updatePanel);
            updateTodoFrame.pack();
            updateTodoFrame.setVisible(true);

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(updateTodoFrame, "Error occurred: " + ex.getMessage(), "Error",
                    JOptionPane.ERROR_MESSAGE);
        }

        // Delete Button Functionality
        selectTodoBtn.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow != -1) { // Check if a row is selected
                try {
                    String userIdToupdate = (String) table.getValueAt(selectedRow, 0);
                    String userTodoTitle = (String) table.getValueAt(selectedRow, 1);
                    String userTodoDescription = (String) table.getValueAt(selectedRow, 2);
                    updateTodoFrame.dispose();
                    updateTodoForm updateform = new updateTodoForm();
                    updateform.update(userIdToupdate,userTodoTitle,userTodoDescription);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(updateTodoFrame, "Error occurred: " + ex.getMessage(), "Error",
                    JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(updateTodoFrame, "Please select a task to delete", "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        });
        BackToMainPageBtn.addActionListener(e -> {
            updateTodoFrame.dispose();
            todo.main(null);
        });
    }
}