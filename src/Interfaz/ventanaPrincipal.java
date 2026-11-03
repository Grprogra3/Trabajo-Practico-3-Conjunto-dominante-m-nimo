package Interfaz;


import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

import Grafo.ControladorGrafo;
import Grafo.Grafo;
import Grafo.Vertice;
import Persistencia.GrafoJSON;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;


public class ventanaPrincipal extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField nombreProvincia;
	private JComboBox<String> comboBox1;
	private JComboBox<String> comboBox2;
	// Ruta (dentro del classpath) de la imagen de fondo: src/Interfaz/imagenes/fondo.png
	private static final String RUTA_FONDO = "/Interfaz/imagenes/fondo.jpg";
	private BufferedImage imagenFondo;
	//private Grafo grafo = new Grafo();
	private ControladorGrafo controlador = new ControladorGrafo();


	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ventanaPrincipal frame = new ventanaPrincipal();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

    public ventanaPrincipal() {
        initialize();
    }

	 private void initialize() {
         setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
         setBounds(100, 100, 676, 571);
         imagenFondo = cargarImagen(RUTA_FONDO);
         contentPane = new JPanel() {
             private static final long serialVersionUID = 1L;

             @Override
             protected void paintComponent(Graphics g) {
                 super.paintComponent(g);
                 g.drawImage(imagenFondo, 0, 0, null);

             }
         };
         contentPane.setBackground(Color.WHITE);
         contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
         setContentPane(contentPane);
         contentPane.setLayout(null);

         Font adwaitaSans = null;
         try {
             // 1. Cargar el archivo TTF desde los recursos de tu proyecto
             InputStream is = ventanaPrincipal.class.getResourceAsStream("fuente/adwaita-sans-latin-800-italic.ttf");
             adwaitaSans = Font.createFont(Font.TRUETYPE_FONT, is);

             // 2. Registrar la fuente en el entorno gráfico de Java
             GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
             ge.registerFont(adwaitaSans);

         } catch (Exception e) {
             e.printStackTrace();
             System.out.println("No se pudo cargar la fuente. Se usará la por defecto.");
         }

         JLabel titulo = new JLabel("CDMinimo Gennerator");
         titulo.setBackground(Color.BLUE);
         titulo.setForeground(Color.BLACK);
         titulo.setFont(adwaitaSans.deriveFont(Font.PLAIN, 14f));
         titulo.setBounds(220, 11, 310, 25);
         contentPane.add(titulo);

         nombreProvincia = new JTextField();
         nombreProvincia.setColumns(10);
         nombreProvincia.setBounds(182, 95, 195, 20);
         contentPane.add(nombreProvincia);

         JButton btnCrearVertice = new JButton("crear Vertice");
         btnCrearVertice.setFont(adwaitaSans.deriveFont(Font.PLAIN, 14f));

         btnCrearVertice.addActionListener(new ActionListener() {
             public void actionPerformed(ActionEvent e) {
                 String nombre = nombreProvincia.getText().trim().toUpperCase();
                 //verifico que el nombre no esté vacío
                 if (nombre.isEmpty()) {
                     JOptionPane.showMessageDialog(ventanaPrincipal.this, "Ingresá el valor del vertice.");
                     return;
                 }
                 //verifico que las coordenadas estén completas

                 //verifico que la provincia no esté repetida
                 for (int i = 0; i < comboBox1.getItemCount(); i++) {
                     if (comboBox1.getItemAt(i).toString().equalsIgnoreCase(nombre)) {
                         JOptionPane.showMessageDialog(ventanaPrincipal.this, "un vertice ya posee este valor");
                         return;
                     }
                 }
                 try {
                     int valor = Integer.parseInt(nombre);
                     controlador.agregarVertice(valor);

                     comboBox1.addItem(nombre);
                     comboBox2.addItem(nombre);
                     nombreProvincia.setText("");

                     JOptionPane.showMessageDialog(ventanaPrincipal.this, "Vertice creado correctamente.");

                 } catch (NumberFormatException ex) {
                     JOptionPane.showMessageDialog(ventanaPrincipal.this, "el valor debe ser numerico");
                 } catch (IllegalArgumentException ex) {
                     JOptionPane.showMessageDialog(ventanaPrincipal.this, "un vertice ya posee este valor");
                 }

             }
         });
         btnCrearVertice.setBounds(448, 110, 173, 23);
         contentPane.add(btnCrearVertice);

         JButton btnCrearArista = new JButton("CREAR ARISTA");
         btnCrearArista.setFont(adwaitaSans.deriveFont(Font.PLAIN, 14f));
         btnCrearArista.addActionListener(e -> crearArista());
         btnCrearArista.setBounds(448, 307, 173, 23);
         contentPane.add(btnCrearArista);

         comboBox1 = new JComboBox<>();
         comboBox1.setBounds(102, 266, 173, 22);
         contentPane.add(comboBox1);

         comboBox2 = new JComboBox<>();
         comboBox2.setBounds(102, 307, 173, 22);
         contentPane.add(comboBox2);

         JLabel labelProvincia = new JLabel("Valor Del vertice:");
         labelProvincia.setForeground(Color.BLACK);
         labelProvincia.setFont(adwaitaSans.deriveFont(Font.PLAIN, 14f));
         labelProvincia.setBounds(20, 95, 152, 17);
         contentPane.add(labelProvincia);

         JButton btnNewButton = new JButton("VISUALIZAR GRAFO");
         btnNewButton.setFont(adwaitaSans.deriveFont(Font.PLAIN, 14f));
         btnNewButton.addActionListener(new ActionListener() {
             public void actionPerformed(ActionEvent e) {
                 mostrarGrafo();
             }
         });
         btnNewButton.setBounds(243, 406, 173, 23);
         contentPane.add(btnNewButton);

         JButton btnCargarJSON = new JButton("CARGAR JSON");
         btnCargarJSON.setFont(adwaitaSans.deriveFont(Font.PLAIN, 14f));
         btnCargarJSON.addActionListener(e -> cargarDesdeJSON());
         btnCargarJSON.setBounds(243, 446, 173, 23);
         contentPane.add(btnCargarJSON);

         JLabel subTitulo1 = new JLabel("INGRESO DE VERTICES");
         subTitulo1.setBackground(Color.LIGHT_GRAY);
         subTitulo1.setForeground(Color.BLACK);
         subTitulo1.setFont(adwaitaSans.deriveFont(Font.PLAIN, 14f));
         subTitulo1.setBounds(231, 59, 173, 25);
         contentPane.add(subTitulo1);

         JLabel subTitulo2 = new JLabel("INGRESO DE ARISTAS");
         subTitulo2.setBackground(Color.LIGHT_GRAY);
         subTitulo2.setForeground(Color.BLACK);
         subTitulo2.setFont(adwaitaSans.deriveFont(Font.PLAIN, 14f));
         subTitulo2.setBounds(231, 220, 173, 25);
         contentPane.add(subTitulo2);

         JLabel labelProv1 = new JLabel("Vertice 1:");
         labelProv1.setForeground(Color.BLACK);
         labelProv1.setFont(adwaitaSans.deriveFont(Font.PLAIN, 14f));
         labelProv1.setBounds(20, 268, 72, 14);
         contentPane.add(labelProv1);

         JLabel labelProv2 = new JLabel("Vertice 2:");
         labelProv2.setForeground(Color.BLACK);
         labelProv2.setFont(adwaitaSans.deriveFont(Font.PLAIN, 14f));
         labelProv2.setBounds(20, 309, 72, 14);
         contentPane.add(labelProv2);

         JLabel lblEjemplo = new JLabel("Ej: 0.25");
         lblEjemplo.setForeground(Color.GRAY);
         lblEjemplo.setBounds(181, 354, 46, 14);
         contentPane.add(lblEjemplo);
     }

	 private BufferedImage cargarImagen(String ruta) {
		 try (InputStream is = getClass().getResourceAsStream(ruta)) {
			 if (is == null) {
				 System.err.println("No se encontró la imagen de fondo: " + ruta);
				 return null;
			 }
			 return ImageIO.read(is);
		 } catch (IOException e) {
			 e.printStackTrace();
			 return null;
		 }
	 }

	 
	 private void mostrarGrafo() {
		 if (controlador.estaVacio()) {
			 JOptionPane.showMessageDialog(this, "El grafo está vacío. Creá vértices o cargá un JSON.");
			 return;
		 }
		 new ventanaGrafo(this, controlador).setVisible(true);
		 this.setVisible(false);
	}

	 private void crearArista() {
		 Object sel1 = comboBox1.getSelectedItem();
		 Object sel2 = comboBox2.getSelectedItem();
		 if (sel1 == null || sel2 == null) {
			 JOptionPane.showMessageDialog(this, "Seleccioná los dos vértices.");
			 return;
		 }
		 if (sel1.equals(sel2)) {
			 JOptionPane.showMessageDialog(this, "Los vértices de la arista deben ser distintos.");
			 return;
		 }
		 try {
			 int a = Integer.parseInt(sel1.toString());
			 int b = Integer.parseInt(sel2.toString());
			 controlador.agregarArista(a, b);
			 JOptionPane.showMessageDialog(this, "Arista " + a + " - " + b + " creada correctamente.");
		 } catch (IllegalArgumentException ex) {
			 JOptionPane.showMessageDialog(this, ex.getMessage());
		 }
	 }

	 private void cargarDesdeJSON() {
		 JFileChooser selector = new JFileChooser(new File(System.getProperty("user.dir")));
		 selector.setFileFilter(new FileNameExtensionFilter("Archivos JSON", "json"));
		 if (selector.showOpenDialog(this) != JFileChooser.APPROVE_OPTION) return;

		 try {
			 controlador.cargarDesdeJSON(selector.getSelectedFile());
		 } catch (IOException ex) {
			 JOptionPane.showMessageDialog(this, "No se pudo leer el archivo: " + ex.getMessage());
			 return;
		 } catch (IllegalArgumentException ex) {
			 JOptionPane.showMessageDialog(this, "Error en el JSON: " + ex.getMessage());
			 return;
		 }

		 // actualizo los combos con los vertices cargados
		 comboBox1.removeAllItems();
		 comboBox2.removeAllItems();
		 controlador.getValoresVerticesOrdenados().forEach(v -> {
		        comboBox1.addItem(v);
		        comboBox2.addItem(v);
		    });

		 JOptionPane.showMessageDialog(this, "Grafo cargado: " + controlador.getCantidadVertices()
				 + " vértices y " + controlador.getCantidadAristas() + " aristas.");
	 }

	public class EjemploFuente {

		}
	}

