/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controller;

import Model.Conexion;
import Vista.Professor;
import Vista.StudentsNotes;
import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

/**
 *
 * @author USUARIO
 */
public class ProfessorController implements ActionListener {
    
    
    Professor profe;
    StudentsNotes notas;
    Conexion conecta = new Conexion();
    Controller contro = new Controller();
    Connection con;
    PreparedStatement ps;
    ResultSet rs;
    DefaultTableModel modelo = new DefaultTableModel();
    
    public ProfessorController(){

        this.profe = new Professor();
        this.notas = new StudentsNotes();
        

    }

    @Override
    public void actionPerformed(ActionEvent ex) {
        if (ex.getSource() == profe.IngresarNota) {
            iniciarStudentsNotes();
        }
        if (ex.getSource() == profe.listar) {
            listarEstudiantes(profe.mitabla);
        }
        if (ex.getSource() == profe.Regresar) {
            profe.dispose();
            contro.iniciar_user();
        }
        if (ex.getSource() == profe.exportar) {

            try {
                listarEstudiantes(profe.mitabla);
                exportarExcel(profe.mitabla);
            } catch (IOException ex1) {
                
            }  
        }

        if (ex.getSource() == notas.cancelar) {
            notas.dispose();
        }
        if (ex.getSource() == notas.enviar){
            ingresarNota();
        }
        if (ex.getSource() == notas.actualizar){
            actualizarNota();
        }

    }
    
    public void iniciarProfessor(){
        profe.setVisible(true);
        profe.mitabla.setVisible(false);
        this.profe.IngresarNota.addActionListener(this);
        this.profe.textDocumento.addActionListener(this);
        this.profe.Regresar.addActionListener(this);   
        this.profe.listar.addActionListener(this);
        this.profe.exportar.addActionListener(this);
    }
    
    public void listarEstudiantes(JTable tabla){
            String sql = "select * from students";
        try {
            con = conecta.abrir();
            ps = con.prepareStatement(sql);
            
            
            rs = ps.executeQuery();
            Object[] object = new Object[7];
            modelo = (DefaultTableModel)tabla.getModel();
            
            modelo.setRowCount(0);
            
             if (!rs.isBeforeFirst()) {
            JOptionPane.showMessageDialog(null, "No hay estudiantes registrados");
            }
            
            while (rs.next()) {
                object[0] = rs.getString("Id");
                object[1] = rs.getString("Nombre");
                object[2] = rs.getString("Apellido");
                object[3] = rs.getString("Primer30");
                object[4] = rs.getString("Segundo30");
                object[5] = rs.getString("ultimo40");
       
                 double promedio = (Double.parseDouble(object[3].toString()) * 0.3) +
                          (Double.parseDouble(object[4].toString()) * 0.3) +
                          (Double.parseDouble(object[5].toString()) * 0.4);
        
                object[6] = promedio;
                modelo.addRow(object);
            }
            profe.mitabla.setModel(modelo);
        } catch (Exception e) {
            e.printStackTrace();
        }
        profe.mitabla.setVisible(true);
    }
    
    public void exportarExcel(JTable t) throws IOException {
        JFileChooser chooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Archivos de excel", "xls");
        chooser.setFileFilter(filter);
        chooser.setDialogTitle("Guardar archivo");
        chooser.setAcceptAllFileFilterUsed(false);
        if (chooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
            String ruta = chooser.getSelectedFile().toString().concat(".xls");
            try {
                File archivoXLS = new File(ruta);
                if (archivoXLS.exists()) {
                    archivoXLS.delete();
                }
                archivoXLS.createNewFile();
                Workbook libro = new HSSFWorkbook();
                FileOutputStream archivo = new FileOutputStream(archivoXLS);
                Sheet hoja = libro.createSheet("Mi hoja de trabajo 1");
                hoja.setDisplayGridlines(false);
                for (int f = 0; f < t.getRowCount(); f++) {
                    Row fila = hoja.createRow(f);
                    for (int c = 0; c < t.getColumnCount(); c++) {
                        Cell celda = fila.createCell(c);
                        if (f == 0) {
                            celda.setCellValue(t.getColumnName(c));
                        }
                    }
                }
                int filaInicio = 1;
                for (int f = 0; f < t.getRowCount(); f++) {
                    Row fila = hoja.createRow(filaInicio);
                    filaInicio++;
                    for (int c = 0; c < t.getColumnCount(); c++) {
                        Cell celda = fila.createCell(c);
                        if (t.getValueAt(f, c) instanceof Double) {
                            celda.setCellValue(Double.parseDouble(t.getValueAt(f, c).toString()));
                        } else if (t.getValueAt(f, c) instanceof Float) {
                            celda.setCellValue(Float.parseFloat((String) t.getValueAt(f, c)));
                        } else {
                            celda.setCellValue(String.valueOf(t.getValueAt(f, c)));
                        }
                    }
                }
                libro.write(archivo);
                archivo.close();
                Desktop.getDesktop().open(archivoXLS);
            } catch (IOException | NumberFormatException e) {
                throw e;
            }
        }
    }
  
    public void iniciarStudentsNotes() {
        this.notas.enviar.addActionListener(this);
        this.notas.cancelar.addActionListener(this);
        this.notas.actualizar.addActionListener(this);

        String sql = "SELECT Nombre, Apellido FROM employee WHERE ID = ?";
        
        try {
            con = conecta.abrir();
            ps = con.prepareStatement(sql);
            ps.setString(1, profe.textDocumento.getText());

            rs = ps.executeQuery();

            if (!rs.isBeforeFirst()) {
                JOptionPane.showMessageDialog(null, "No hay estudiantes registrados con esa información");
            } else {
                rs.next();
                String nombre2 = rs.getString("Nombre");
                String apellido2 = rs.getString("Apellido");

                notas.setVisible(true);
                notas.NombreEstudiante.setText(nombre2);
                notas.apellidoEstudiante.setText(apellido2);
                
            }
        } catch (Exception ex) {
        
    }
}

    public void ingresarNota(){
        
        String id = profe.textDocumento.getText();
        String nom = notas.NombreEstudiante.getText();
        String ape = notas.apellidoEstudiante.getText();
        String pri30 = notas.nota1.getText();
        String sec30 = notas.nota2.getText();
        String ult40 = notas.nota3.getText();
        String notasSql = "SELECT COUNT(*) FROM students WHERE id = ?";
        
        
        try{
            ps = con.prepareStatement(notasSql);
            ps.setString(1, profe.textDocumento.getText());
            ResultSet rsNotas = ps.executeQuery();
            
            if (rsNotas.next() && rsNotas.getInt(1) > 0) {
                    JOptionPane.showMessageDialog(null, "El estudiante ya tiene notas,\npara cambiarlas seleccione actualizar.");
                } else {
        
        
        
        
                try {


                    double primer = Double.parseDouble(pri30);
                    double segundo = Double.parseDouble(sec30);
                    double ultimo = Double.parseDouble(ult40);


                    String sql = "INSERT INTO students(ID, Nombre, Apellido, Primer30, Segundo30, Ultimo40)"
                            + "VALUES('" + id + "', '" + nom + "', '" + ape + "', '" + primer + "', '"
                            + segundo + "', '" + ultimo + "')";
                    con = conecta.abrir();
                    ps = con.prepareStatement(sql);
                    ps.executeUpdate();
                    JOptionPane.showMessageDialog(null, "Las notas del estudiante se agregó con éxito!");
                    

                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Debe ser un número."+ ex.getMessage());
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "Error al agregar nota de estudiante: " + ex.getMessage());
                }
                }
            }catch(Exception ex){
        }
    }
    
    public void actualizarNota(){

        String id = profe.textDocumento.getText();
        String pri30 = notas.nota1.getText();
        String sec30 = notas.nota2.getText();
        String ult40 = notas.nota3.getText();
 
        try{

            String sql= "UPDATE students SET " +
                        "Primer30 = ?, " +
                        "Segundo30 = ?, " +
                        "Ultimo40 = ? " +
                        "WHERE id = ?";

            con = conecta.abrir();
            ps = con.prepareStatement(sql);
            ps.setString(1, pri30);
            ps.setString(2, sec30);
            ps.setString(3, ult40);
            ps.setString(4, id);

            ps.executeUpdate();

                    JOptionPane.showMessageDialog(null, "Las notas del estudiante se actualizaron con éxito!");
      
            }catch(Exception ex){
                ex.printStackTrace();
        }
    }
}
