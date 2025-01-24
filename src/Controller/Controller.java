/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controller;

import Model.Conexion;
import Vista.AcercaDe;
import Vista.Admin;
import Vista.Credentials;
import Vista.Init;
import Vista.User;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.Timer;
import javax.swing.table.DefaultTableModel;
import java.sql.SQLIntegrityConstraintViolationException;

public class Controller implements ActionListener {

    Conexion conecta = new Conexion();
    Connection con;
    PreparedStatement ps;
    ResultSet rs;
    Admin adm;
    Credentials cred;
    Init init;
    User user;
  
    DefaultTableModel modelo = new DefaultTableModel();
    
    private Timer timer;

    public Controller() {
        
        this.adm = new Admin();
        this.cred = new Credentials();
        this.init = new Init();

        this.user = new User();

    }
    
    public void iniciar_init(){
        init.setVisible(true);
        iniciarTemporizador();

    }
    
    private void iniciarTemporizador() {
        
        timer = new Timer(2000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                iniciar_user();
            }
        });
        timer.setRepeats(false);
        timer.start();
    }
    
    public void iniciar_user(){
        user.setVisible(true);
        user.mitablaUsuario.setVisible(false);
        if(init.isVisible()){
        init.dispose();
        timer.stop();
        }
        
        this.user.item1.addActionListener(this);
        this.user.info.addActionListener(this);
        this.user.buscar.addActionListener(this);
        this.user.item2.addActionListener(this);
        this.user.item3.addActionListener(this);
    }

    public void iniciar_admin() 
    {
        this.adm.Listar.addActionListener(this);
        this.adm.Agregar.addActionListener(this);
        this.adm.Actualizar.addActionListener(this);
        this.adm.Eliminar.addActionListener(this);
        this.adm.Salir.addActionListener(this);
        this.adm.vinculo.getSelectedItem();
        this.adm.Regresar.addActionListener(this);
        
        
        adm.setVisible(true);
        adm.setLocationRelativeTo(null);
        adm.mitabla.setVisible(false);
        
        
        adm.mitabla.addMouseListener(new java.awt.event.MouseAdapter() {
        @Override
        public void mouseClicked(MouseEvent e) {
            int fila = adm.mitabla.rowAtPoint(e.getPoint());  // Obtener la fila clicada
            if (fila != -1) {  // Verificar que sea una fila válida
                adm.nombre.setText(adm.mitabla.getValueAt(fila, 1).toString());
                adm.apellido.setText(adm.mitabla.getValueAt(fila, 2).toString());
                adm.direccion.setText(adm.mitabla.getValueAt(fila, 3).toString());
                adm.telefono.setText(adm.mitabla.getValueAt(fila, 4).toString());
                adm.ide.setText(adm.mitabla.getValueAt(fila, 5).toString());
                adm.email.setText(adm.mitabla.getValueAt(fila, 6).toString());
                adm.salario.setText(adm.mitabla.getValueAt(fila, 7).toString());
                adm.fecha.setText(adm.mitabla.getValueAt(fila, 8).toString());
                adm.vinculo.setSelectedItem(adm.mitabla.getValueAt(fila, 9).toString());
            }
        }
    });
  
    }
    // Escuchar al presionar botones
    @Override
    public void actionPerformed(ActionEvent ex) {
        if (ex.getSource() == adm.Listar) {
            Listar(adm.mitabla); 
        }
        if (ex.getSource() == adm.Agregar) {
            Agregar();
            Listar(adm.mitabla);
        }
        if (ex.getSource() == adm.Actualizar) {
            Actualizar();
        }
        if (ex.getSource() == adm.Eliminar) {
            Eliminar();
        }
        if (ex.getSource() == adm.Salir) {
            System.exit(0);
        }
        if (ex.getSource() == adm.Regresar) {
            adm.dispose();
            iniciar_user();
        }
        if (ex.getSource() == user.item1){
            cred = new Credentials((Vista.User) user);  
            cred.setVisible(true);
            
        }
        if (ex.getSource() == user.buscar){
            
            BuscarUsuario(user.mitablaUsuario);
        }
        if (ex.getSource() == user.info){
            AcercaDe acerca = new AcercaDe();
            acerca.setVisible(true);
        }
        if (ex.getSource() == user.item2){
            cred = new Credentials((Vista.User) user);  
            cred.setVisible(true);
            
        }
        if (ex.getSource() == user.item3){
            
            StudentController student = new StudentController();
            student.iniciarStudent();
            user.dispose();
        }
        
    }
    
    
   public void BuscarUsuario(JTable tabla){
        
        this.user.cajaUsuario.addActionListener(this);
 
        String sql = "select * from employee WHERE Email = ?";
        try {
            con = conecta.abrir();
            ps = con.prepareStatement(sql);
            ps.setString(1, user.cajaUsuario.getText());
            modelo = (DefaultTableModel)tabla.getModel();
            rs = ps.executeQuery();
            Object[] object = new Object[10];

            modelo.setRowCount(0);
            
             if (!rs.isBeforeFirst()) {
            JOptionPane.showMessageDialog(null, "No hay usuarios registrados con esa información");
            }
            
            while (rs.next()) {
                object[0] = rs.getString("Id");
                object[1] = rs.getString("Nombre");
                object[2] = rs.getString("Apellido");
                object[3] = rs.getString("Direccion");
                object[4] = rs.getString("Telefono");
                object[5] = rs.getString("Ident");
                object[6] = rs.getString("Email");
                object[7] = rs.getString("Salario");
                object[8] = rs.getString("Fecha_Vinc");
                object[9] = rs.getString("Vinculo");
                
                modelo.addRow(object);
            }
            user.mitablaUsuario.setModel(modelo);
        } catch (Exception e) {
        }
        user.mitablaUsuario.setVisible(true);
    }

    public void Agregar() {
        String nom = adm.nombre.getText();
        String ape = adm.apellido.getText();
        String dir = adm.direccion.getText();
        String tele = adm.telefono.getText();
        String iden = adm.ide.getText();
        String mail = adm.email.getText();
        String sal = adm.salario.getText();
        String fecha = adm.fecha.getText();
        String vinculo = adm.vinculo.getSelectedItem().toString();

//        if (sal == null || nom.isEmpty()) {
//            JOptionPane.showMessageDialog(null, "El campo no puede estar vacío.");
//            return;
//        }

        try {
            double salario = Double.parseDouble(sal);
            
            
            
            
            String sql = "INSERT INTO employee(Nombre, Apellido, Direccion, Telefono, Ident, Email, Salario, Fecha_Vinc, vinculo) "
                    + "VALUES('" + nom + "', '" + ape + "', '" + dir + "', '" + tele + "', '"
                    + iden + "', '" + mail + "', '" + salario + "', '" + fecha + "', '" + vinculo + "')";

            con = conecta.abrir();
            ps = con.prepareStatement(sql);
            ps.executeUpdate();
            JOptionPane.showMessageDialog(null, "El empleado se agregó con éxito!");

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(null, "Salario no válido. Debe ser un número.");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Error al agregar el empleado: " + ex.getMessage());
        }
    }

    public void Listar(JTable tabla) {
        String sql = "select * from employee";
        try {
            con = conecta.abrir();
            ps = con.prepareStatement(sql);
            
            
            rs = ps.executeQuery();
            Object[] object = new Object[10];
            modelo = (DefaultTableModel)tabla.getModel();
            
            modelo.setRowCount(0);
            
             if (!rs.isBeforeFirst()) {
            JOptionPane.showMessageDialog(null, "No hay empleados registrados");
            }
            
            while (rs.next()) {
                object[0] = rs.getString("Id");
                object[1] = rs.getString("Nombre");
                object[2] = rs.getString("Apellido");
                object[3] = rs.getString("Direccion");
                object[4] = rs.getString("Telefono");
                object[5] = rs.getString("Ident");
                object[6] = rs.getString("Email");
                object[7] = rs.getString("Salario");
                object[8] = rs.getString("Fecha_Vinc");
                object[9] = rs.getString("Vinculo");
                
                modelo.addRow(object);
            }
            adm.mitabla.setModel(modelo);
        } catch (Exception e) {
        }
        adm.mitabla.setVisible(true);
    }
    
    public void Actualizar() {
    int fila = adm.mitabla.getSelectedRow();  // Obtener la fila seleccionada
     
    if (fila == -1) {  // Verificar si no se ha seleccionado ninguna fila
        JOptionPane.showMessageDialog(null, "Primero debe seleccionar un usuario para actualizar!!");
        return;
    }

    String sql = "UPDATE Employee SET " +
                 "Nombre = ?, " +
                 "Apellido = ?, " +
                 "Direccion = ?, " +
                 "Telefono = ?, " +
                 "Ident = ?, " +
                 "Email = ?, " +
                 "Salario = ?, " +
                 "Fecha_Vinc = ?, " +
                 "Vinculo = ? " +
                 "WHERE ID = ?;";
    
    
    
    try {
        con = conecta.abrir();
        ps = con.prepareStatement(sql);

        String nombre = adm.nombre.getText();
        String apellido = adm.apellido.getText();
        String direccion = adm.direccion.getText();
        String telefono = adm.telefono.getText();
        String ident = adm.ide.getText();
        String email = adm.email.getText();
        double salario = Double.parseDouble(adm.salario.getText());
        String fechaVinc = adm.fecha.getText();
        String vinculo = adm.vinculo.getSelectedItem().toString();

        int id = Integer.parseInt(adm.mitabla.getValueAt(fila, 0).toString());

        
        ps.setString(1, nombre);
        ps.setString(2, apellido);
        ps.setString(3, direccion);
        ps.setString(4, telefono);
        ps.setString(5, ident);
        ps.setString(6, email);
        ps.setDouble(7, salario);
        ps.setString(8, fechaVinc);
        ps.setString(9, vinculo);
        ps.setInt(10, id);

        
        int filasActualizadas = ps.executeUpdate();

        if (filasActualizadas > 0) {
            JOptionPane.showMessageDialog(null, "Empleado actualizado exitosamente.");
            Listar(adm.mitabla);
        } else {
            JOptionPane.showMessageDialog(null, "No se pudo actualizar el empleado.");
        }
    } catch (NumberFormatException e) {
        JOptionPane.showMessageDialog(null, "Salario no válido. Debe ser un número.");
    } catch (Exception e) {
        e.printStackTrace();
    } finally {
        try {
            if (ps != null) ps.close();
            if (con != null) con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
  
    public void Eliminar() {
        int fila = adm.mitabla.getSelectedRow();
        String sql = "DELETE FROM Employee WHERE Id = ?"; 


        if (fila != -1) {
            try {
                con = conecta.abrir(); 
                ps = con.prepareStatement(sql);

                String idStr = adm.mitabla.getValueAt(fila, 0).toString();
                int id = Integer.parseInt(idStr);

                ps.setInt(1, id);

                int filasEliminadas = ps.executeUpdate();

                if (filasEliminadas > 0) {
                    JOptionPane.showMessageDialog(null, "¡Empleado eliminado!");
                }

                modelo.removeRow(fila);

            }  catch (SQLIntegrityConstraintViolationException e) {
                JOptionPane.showMessageDialog(null, "No puede eliminar el usuario ya que existe en otra tabla!!");
            }

            catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    if (ps != null) ps.close();
                    if (con != null) con.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        } else {
            JOptionPane.showMessageDialog(null, "Primero debe seleccionar un usuario para eliminar!!");
            }
    }    
}
    
    
    
    
    
    

   
    
    

