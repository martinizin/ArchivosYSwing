import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.ArrayList;

public class Formularios extends JFrame {
    private JTextField nombreUsuario;
    private JPasswordField contrasenia;
    private JButton BotonRegistro;
    private JButton botonLogin;

    private ArrayList<Usuarios> usuariosList;
    private JLabel Usuario;
    private JLabel Contraseña;

    public Formularios() {
        usuariosList = new ArrayList<>();

        setTitle("Aplicación de Usuarios");
        setSize(300, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(4, 2, 10, 10));

        JLabel usernameLabel = new JLabel("Usuario:");
        nombreUsuario = new JTextField();
        JLabel passwordLabel = new JLabel("Contraseña:");
        contrasenia = new JPasswordField();
        BotonRegistro = new JButton("Registrar");
        botonLogin = new JButton("Iniciar sesión");

        BotonRegistro.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = nombreUsuario.getText();
                char[] passwordChars = contrasenia.getPassword();
                String password = new String(passwordChars);

                if (!username.isEmpty() && !password.isEmpty()) {
                    Usuarios usuarios = new Usuarios(username, password);
                    usuariosList.add(usuarios);

                    guardarUsuario();
                    JOptionPane.showMessageDialog(Formularios.this, "Usuario registrado correctamente.");
                } else {
                    JOptionPane.showMessageDialog(Formularios.this, "Por favor, ingresa usuario y contraseña.");
                }
            }
        });

        botonLogin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = nombreUsuario.getText();
                char[] passwordChars = contrasenia.getPassword();
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
        add(nombreUsuario);
        add(passwordLabel);
        add(contrasenia);
        add(new JLabel()); // Empty label for layout purposes
        add(BotonRegistro);
        add(new JLabel()); // Empty label for layout purposes
        add(botonLogin);

        cargarUsuarios();
    }

    private void cargarUsuarios() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("usuarios.dat"))) {
            usuariosList = (ArrayList<Usuarios>) ois.readObject();
        } catch (FileNotFoundException e) {
            // Ignore if the file doesn't exist, it will be created when saving users
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void guardarUsuario() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("usuarios.dat"))) {
            oos.writeObject(usuariosList);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean validarUsuario(String username, String password) {
        for (Usuarios usuarios : usuariosList) {
            if (usuarios.getUsername().equals(username) && usuarios.getPassword().equals(password)) {
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

class Usuarios implements Serializable {
    private static final long serialVersionUID = 1L;
    private String username;
    private String password;

    public Usuarios(String username, String password) {
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
