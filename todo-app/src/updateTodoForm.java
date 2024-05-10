import javax.swing.*;
import java.sql.*;
public class updateTodoForm {
    Connection connection ;
    JFrame frame  ;
    JPanel buttonPanel  ;
    JButton updateTodoBtn  ;
    JButton BackToMainPageBtn  ;
    JLabel  TodoTitleLabel ;
    JLabel  TodoDescriptionLabel ;
    JTextField TodoTitleField ;
    JTextField TodoDescriptionField ;

    public updateTodoForm(){
        connection = null;
        frame = new JFrame("Update Todo");
        buttonPanel  = new JPanel();
        updateTodoBtn = new JButton("Update Todo");
        BackToMainPageBtn = new JButton("Back");
        TodoTitleLabel = new JLabel("Title: ");
        TodoDescriptionLabel = new JLabel("Description: ");
        TodoTitleField = new JTextField(22);
        TodoDescriptionField = new JTextField(22);

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/TodoListDatabase",
                    "root",
                    "eman");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(frame, "Error occurred: " + e.getMessage(), "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    public void update(String todoId , String TodoTitle , String TodoDescription){ 

        frame.setBounds(300,200,400,160);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 400);
        frame.setVisible(true);

        // setting value to Title Field
        TodoTitleField.setText(TodoTitle);

        // setting value to Description Field 
        TodoDescriptionField.setText(TodoDescription);
        
        

        // main JPanel
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        panel.add(TodoTitleLabel);
        panel.add(TodoTitleField);
        panel.add(TodoDescriptionLabel);
        panel.add(TodoDescriptionField);
      
        // button panel
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
        buttonPanel.add(updateTodoBtn);
        buttonPanel.add(BackToMainPageBtn);

        panel.add(buttonPanel);

        // Adding panel to JFrame
        frame.setContentPane(panel);
        frame.pack();

        // ActionListener
        updateTodoBtn.addActionListener(e -> {
            String title = TodoTitleField.getText();
            String description = TodoDescriptionField.getText();
            try {
                //  connecting with MySQL database
                Class.forName("com.mysql.cj.jdbc.Driver");
                Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/TodoListDatabase", "root",
                        "eman");
                System.out.println("Connection established successfully.");

                // Updating data in MySQL database
                String query = "UPDATE tasks SET title = ?, description = ? WHERE id = ?";
                PreparedStatement statement = connection.prepareStatement(query);
                statement.setString(1, title);
                statement.setString(2, description);
                statement.setString(3, todoId);
                statement.executeUpdate();

                JOptionPane.showMessageDialog(frame, "Todo updated successfully.");
                frame.dispose();
                selectTodo selectTodo = new selectTodo();
                selectTodo.updateTodos();
            } catch (Exception ex) {
                System.out.println("Connection Failed" + ex);
            }
        });
        BackToMainPageBtn.addActionListener(e->{
            frame.dispose();
            selectTodo selectTodo = new selectTodo();
            selectTodo.updateTodos();
        });
    }
}
