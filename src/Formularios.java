import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.ArrayList;

public class Formularios extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton registerButton;
    private JButton loginButton;

    private ArrayList<User> userList;

    public Formularios() {
        userList = new ArrayList<>();

        setTitle("Aplicación de Usuarios");
        setSize(300, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(4, 2, 10, 10));

        JLabel usernameLabel = new JLabel("Usuario:");
        usernameField = new JTextField();
        JLabel passwordLabel = new JLabel("Contraseña:");
        passwordField = new JPasswordField();
        registerButton = new JButton("Registrar");
        loginButton = new JButton("Iniciar sesión");

        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                char[] passwordChars = passwordField.getPassword();
                String password = new String(passwordChars);

                if (!username.isEmpty() && !password.isEmpty()) {
                    User user = new User(username, password);
                    userList.add(user);

                    guardarUsuario();
                    JOptionPane.showMessageDialog(Formularios.this, "Usuario registrado correctamente.");
                } else {
                    JOptionPane.showMessageDialog(Formularios.this, "Por favor, ingresa usuario y contraseña.");
                }
            }
        });

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                char[] passwordChars = passwordField.getPassword();
                String password = new String(passwordChars);

                if (!username.isEmpty() && !password.isEmpty()) {
                    if (validarUsuario(username, password)) {
                        showWelcomeForm(username);
                    } else {
                        JOptionPane.showMessageDialog(Formularios.this, "Credenciales inválidas. Inténtalo de nuevo.");
                    }
                } else {
                    JOptionPane.showMessageDialog(Formularios.this, "Por favor, ingresa usuario y contraseña.");
                }
            }
        });

        add(usernameLabel);
        add(usernameField);
        add(passwordLabel);
        add(passwordField);
        add(new JLabel()); // Empty label for layout purposes
        add(registerButton);
        add(new JLabel()); // Empty label for layout purposes
        add(loginButton);

        cargarUsuarios();
    }

    private void cargarUsuarios() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("usuarios.dat"))) {
            userList = (ArrayList<User>) ois.readObject();
        } catch (FileNotFoundException e) {
            // Ignore if the file doesn't exist, it will be created when saving users
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void guardarUsuario() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("usuarios.dat"))) {
            oos.writeObject(userList);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean validarUsuario(String username, String password) {
        for (User user : userList) {
            if (user.getUsername().equals(username) && user.getPassword().equals(password)) {
                return true;
            }
        }
        return false;
    }

    private void showWelcomeForm(String username) {
        JOptionPane.showMessageDialog(this, "¡Bienvenido, " + username + "!");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new Formularios().setVisible(true);
            }
        });
    }
}

class User implements Serializable {
    private static final long serialVersionUID = 1L;
    private String username;
    private String password;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

}
