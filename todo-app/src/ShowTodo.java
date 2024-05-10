import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.sql.*;

public class ShowTodo {
    Connection connection = null ;
    JFrame ShowFrame;
    private int todo_number ;
    
    
    public ShowTodo(){
        todo_number = 0 ;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/TodoListDatabase",
                    "root",
                    "eman");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(ShowFrame , "Error occurred: " + ex.getMessage(), "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
    void showTodos(){
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM tasks");
            DefaultTableModel model = new DefaultTableModel();
            model.addColumn("No.");
            model.addColumn("Title");
            model.addColumn("Description");
            
            while (resultSet.next()) {
                
                String getTitlefromResult = resultSet.getString("title");
                String getDescriptionfromResult = resultSet.getString("description");
                model.addRow(new Object[] { ++todo_number, getTitlefromResult, getDescriptionfromResult });
            }
            JTable table = new JTable(model);
            JOptionPane.showMessageDialog(ShowFrame, new JScrollPane(table), "Tasks", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(ShowFrame , "Error occurred: " + ex.getMessage(), "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}