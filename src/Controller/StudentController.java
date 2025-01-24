/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controller;

import Model.Conexion;
import static Model.Conexion.con;
import Vista.Student;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.JOptionPane;

/**
 *
 * @author USUARIO
 */
public class StudentController implements ActionListener {
    
    Student stude;
    Conexion conecta = new Conexion();
    Controller contro = new Controller();
    Connection con;
    PreparedStatement ps;
    ResultSet rs;
    
    
    public void iniciarStudent(){
        
        this.stude = new Student();
        stude.setVisible(true);
        stude.setLocationRelativeTo(null);
        this.stude.consultar.addActionListener(this);
        this.stude.regresar.addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent ex) {
        
        if (ex.getSource() == stude.consultar) {
            consultarNota();
        }
        if (ex.getSource() == stude.regresar) {
            stude.dispose();
            contro.iniciar_user();
        }
    }
    
    public void consultarNota(){
  
         try{
            String sql = "SELECT Nombre, Apellido, Primer30, Segundo30, Ultimo40 FROM students WHERE id = ?";
            
            Object[] object = new Object[7];
            con = conecta.abrir();
            ps = con.prepareStatement(sql);
            ps.setString(1, stude.textId.getText());
            rs = ps.executeQuery();
            
            if (!rs.isBeforeFirst()) {
            JOptionPane.showMessageDialog(null, "Aún no tienes notas registradas");
            }else{while (rs.next()) {
                
                
                stude.nombre.setText(rs.getString("Nombre"));
                stude.apellido.setText(rs.getString("Apellido"));
                
                
                object[3] = rs.getString("Primer30");
                object[4] = rs.getString("Segundo30");
                object[5] = rs.getString("ultimo40");
       
                 double promedio = (Double.parseDouble(object[3].toString()) * 0.3) +
                          (Double.parseDouble(object[4].toString()) * 0.3) +
                          (Double.parseDouble(object[5].toString()) * 0.4);
        
                object[6] = promedio;
                
                stude.notaFinal.setText(String.valueOf(promedio));}
                double promedio = (Double.parseDouble(object[3].toString()) * 0.3) +
                          (Double.parseDouble(object[4].toString()) * 0.3) +
                          (Double.parseDouble(object[5].toString()) * 0.4);    
            }
            
            
 
            }catch(Exception ex){
                ex.printStackTrace();
        }
    }
}
