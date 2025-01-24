package Model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javax.swing.JOptionPane;

public class Conexion {
    public static Connection con = null;
    public static String passw = "";  // Asigna la contraseña correspondiente
    public static String user = "root"; // Asigna el nombre de usuario correspondiente
    public static final String url = "jdbc:mysql://localhost:3306/bd_ejemplo";

    public Connection abrir() {
        System.out.println("Conectando a la base de datos ...");
        try {
            System.out.println("Cargando el driver");
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("Driver cargado!");
            con = DriverManager.getConnection(url, user, passw);
//            JOptionPane.showMessageDialog(null, "Conectado con éxito");
        } catch (ClassNotFoundException | SQLException e) {
            JOptionPane.showMessageDialog(null, "No se estableció una conexión: " + e.getMessage(), "Error de conexión",
                    JOptionPane.ERROR_MESSAGE);
        }
        return con;
    }

    public static void cerrar() {
        try {
            if (con != null) {
                con.close();
            }
            JOptionPane.showMessageDialog(null, "Se ha cerrado la conexión");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "No se logró cerrar la conexión: " + e.getMessage());
        }
    }
}
