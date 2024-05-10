import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.sql.*;

public class todo {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Todo Form");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setBounds(300,200,400,160);

        // Todo Form
        JLabel TodoTitleLabel = new JLabel("Title: ");
        JTextField TodoTitleField = new JTextField(22);
        JLabel TodoDescriptionLabel = new JLabel("Description: ");
        JTextField TodoDescriptionField = new JTextField(22);
        // Buttons
        JButton AddTodoBtn = new JButton("Add Task");
        JButton ShowTodoBtn = new JButton("Show Task");
        JButton DeleteTodoBtn = new JButton("Delete Task");
        JButton UpdateTodoBtn = new JButton("Update Task");

        // Button Panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
        buttonPanel.add(AddTodoBtn);
        buttonPanel.add(ShowTodoBtn);
        buttonPanel.add(DeleteTodoBtn);
        buttonPanel.add(UpdateTodoBtn);

        // JPanel
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.add(TodoTitleLabel);
        panel.add(TodoTitleField);
        panel.add(TodoDescriptionLabel);
        panel.add(TodoDescriptionField);

        
        // Parent Panel
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.add(panel);
        mainPanel.add(buttonPanel);

        // Adding panel to JFrame
        frame.setContentPane(mainPanel);
        frame.setVisible(true);

        // Add Todo Button Functionality 
        AddTodoBtn.addActionListener(e -> {
            String title = TodoTitleField.getText();
            String description = TodoDescriptionField.getText();

            // Validation for checking empty fields
            if (title.isEmpty() || description.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Please fill all the fields");
            }

            else {
                try {
                    Class.forName("com.mysql.cj.jdbc.Driver");
                    Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/TodoListDatabase",
                            "root",
                            "eman");
                    System.out.println("Connection established successfully.");

                    PreparedStatement preparedStatement = connection
                            .prepareStatement("INSERT INTO tasks (title, description) VALUES (?, ?)");
                    preparedStatement.setString(1, title);
                    preparedStatement.setString(2, description);
                    preparedStatement.executeUpdate();
                    TodoTitleField.setText("");
                    TodoDescriptionField.setText("");

                    JOptionPane.showMessageDialog(frame, "Todo Added Successfully!!");
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(frame, "Error occurred: " + ex.getMessage(), "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // Delete Todo Button Functionality
        DeleteTodoBtn.addActionListener(e -> {
            frame.dispose();
            deleteTodo deletetodo = new deleteTodo();
            deletetodo.deleteFrame();
        });
        
        // Show Todo Button Functionality
        ShowTodoBtn.addActionListener(e -> {
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
                // Class.forName("com.mysql.cj.jdbc.Driver");
                Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/TodoListDatabase",
                        "root",
                        "eman");
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
                JTable table = new JTable(model);
                JOptionPane.showMessageDialog(frame, new JScrollPane(table), "Tasks", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(frame, "Error occurred: " + ex.getMessage(), "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        });

        // Update Todo Button Functionality
        UpdateTodoBtn.addActionListener(e -> {
            frame.dispose();
            selectTodo updatetodo = new selectTodo();
            updatetodo.updateTodos();
        });
    }
}