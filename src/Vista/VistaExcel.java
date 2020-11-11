/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Vista;


import java.io.File;
import java.sql.*;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import Modelo.Conexion;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class VistaExcel extends javax.swing.JFrame {

    File archivo = null;
    JFileChooser selecArchivo = new JFileChooser();
   
    Connection conn = null;
    Conexion Conexion = null;
    

    public VistaExcel() {
       
        initComponents();       
        
        Conexion = new Conexion();
     
       
        
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tabla = new javax.swing.JTable();
        jPanel2 = new javax.swing.JPanel();
        btnImportar = new javax.swing.JButton();
        btnExportar = new javax.swing.JButton();
        btnActualizarPrecio = new javax.swing.JButton();

        jPanel1.setLayout(new java.awt.BorderLayout());

        tabla.setModel(modeloTabla);
        jScrollPane2.setViewportView(tabla);

        jPanel1.add(jScrollPane2, java.awt.BorderLayout.CENTER);

        jPanel2.setFont(new java.awt.Font("Decker", 1, 14)); // NOI18N

        btnImportar.setText("Importar");
        btnImportar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnImportarActionPerformed(evt);
            }
        });
        jPanel2.add(btnImportar);

        btnExportar.setText("Exportar");
        jPanel2.add(btnExportar);

        btnActualizarPrecio.setText("Actualizar precio");
        btnActualizarPrecio.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnActualizarPrecioActionPerformed(evt);
            }
        });
        jPanel2.add(btnActualizarPrecio);

        jPanel1.add(jPanel2, java.awt.BorderLayout.PAGE_START);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 678, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 403, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnImportarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnImportarActionPerformed

        getFile();
    }//GEN-LAST:event_btnImportarActionPerformed

    private void btnActualizarPrecioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnActualizarPrecioActionPerformed
        try {
            int numFila = this.tabla.getRowCount();
            int numColumna = this.tabla.getColumnCount();

            conn = Conexion.getConexion();
         
            //PreparedStatement ps = conn.prepareStatement("UPDATE productos SET PRECIO=? WHERE CODIGO_ARTICULO=?");
           PreparedStatement ps = conn.prepareStatement("INSERT INTO productos ( DESCRIPCION_PROD, CODIGO_PROD, PRECIO_PROD,CANTIDAD_PROD,ID_PROVEEDOR)"
                   + "VALUES(?,?,?,?,(SELECT ID_PROVEEDOR FROM proveedor WHERE NOM_PROVEEDOR=?))");
          
            for (int i = 0; i < numFila; i++) {

                ps.setString(1, (String) modeloTabla.getValueAt(i, 0));
                ps.setDouble(2, (double) modeloTabla.getValueAt(i, 1));
                ps.setDouble(3, (double) modeloTabla.getValueAt(i, 2));
                ps.setDouble(4, (double) modeloTabla.getValueAt(i, 3));
                ps.setString(5, (String) modeloTabla.getValueAt(i, 4));
       

                ps.execute();

                System.out.println("bucle" + "-->  " + i);

            }
            JOptionPane.showMessageDialog(null, "FINISH HIM");

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }//GEN-LAST:event_btnActualizarPrecioActionPerformed

    public void getFile() {

        if (selecArchivo.showDialog(null, "Seleccionar archivo") == JFileChooser.APPROVE_OPTION) {

            archivo = selecArchivo.getSelectedFile();

            if (archivo.getName().endsWith("xls") || archivo.getName().endsWith("xlsx")) {

                JOptionPane.showMessageDialog(null, importar(archivo, tabla));
            } else {
                JOptionPane.showMessageDialog(null, "Elija un formato válido");
            }
        }
    }

    public String importar(File archivo, JTable tabla) {

        String respuesta = "Error en la importacion";

        Workbook wb;

        try {

            wb = WorkbookFactory.create(new FileInputStream(archivo));
            Sheet hoja = wb.getSheetAt(0);
            Iterator filaIterator = hoja.rowIterator();
            int indiceFila = -1;

            while (filaIterator.hasNext()) {
                indiceFila++;
                Row fila = (Row) filaIterator.next();
                Iterator columnIterator = fila.cellIterator();

                Object[] listaColumna = new Object[20];
                int indiceColumna = -1;

                while (columnIterator.hasNext()) {
                    indiceColumna++;

                    Cell cell = (Cell) columnIterator.next();
                    if (indiceFila == 0) {

                        modeloTabla.addColumn(cell.getStringCellValue());
                    } else {

                        if (cell != null) {

                            switch (cell.getCellType()) {

                                case Cell.CELL_TYPE_NUMERIC:
                                    listaColumna[indiceColumna] = (double) cell.getNumericCellValue();

                                    break;

                                case Cell.CELL_TYPE_STRING:

                                    listaColumna[indiceColumna] = cell.getStringCellValue();
                                    break;

                                case Cell.CELL_TYPE_BOOLEAN:

                                    listaColumna[indiceColumna] = cell.getBooleanCellValue();
                                    break;

                                default:

                                    listaColumna[indiceColumna] = cell.getDateCellValue();
                                    break;

                            }
                        }

                    }

                }
                if (indiceFila != 0) {
                    modeloTabla.addRow(listaColumna);
                }

            }
            respuesta = "Importación exitosa";

        } catch (Exception e) {

            e.printStackTrace();
        }

        return respuesta;
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    public javax.swing.JButton btnActualizarPrecio;
    public javax.swing.JButton btnExportar;
    public javax.swing.JButton btnImportar;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane2;
    public javax.swing.JTable tabla;
    // End of variables declaration//GEN-END:variables
private DefaultTableModel modeloTabla = new DefaultTableModel();

}
