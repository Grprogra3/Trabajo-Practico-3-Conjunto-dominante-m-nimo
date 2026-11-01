package Interfaz;


import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ventanaGrafo extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private ventanaPrincipal ventanaPrincipal;
	private boolean mostrarRegiones;

	
	//para mostrar grafo original y agm
	public ventanaGrafo( ventanaPrincipal ventanaPrincipal) {

	    this.ventanaPrincipal = ventanaPrincipal;
	    this.mostrarRegiones = false;
	    initialize();
	}
	
	//para mostrar grafo de las regiones obtenidas
	public ventanaGrafo( int k) {


        this.mostrarRegiones = true;
        initialize();
	}
	
	public void initialize() {
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 676, 571);
		contentPane = new JPanel();
		contentPane.setBackground(new Color(0, 0, 0));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblTitulo = new JLabel(mostrarRegiones ? "GRAFO REGIONALIZADO" : "MAPA PRINCIPAL");
		lblTitulo.setBounds(167, 11, 302, 25);
		lblTitulo.setForeground(Color.WHITE);
		lblTitulo.setFont(new Font("Tahoma", Font.PLAIN, 20));
		lblTitulo.setHorizontalAlignment(SwingConstants.CENTER);
		contentPane.add(lblTitulo);
		
		JTextArea areaSimilaridades = new JTextArea();

		areaSimilaridades.setEditable(false);
		areaSimilaridades.setBackground(Color.BLACK);
		areaSimilaridades.setForeground(Color.WHITE);
		areaSimilaridades.setFont(new Font("Monospaced", Font.PLAIN, 13));
		areaSimilaridades.setLineWrap(false);

		JScrollPane scroll = new JScrollPane(areaSimilaridades);
		scroll.setBounds(31, 47, 587, 290);
		contentPane.add(scroll);

	
			JButton btnAGM = new JButton("OBTENER ARBOL GENERADOR MÍNIMO");
			btnAGM.addActionListener(new ActionListener() {
			    public void actionPerformed(ActionEvent e) {		    	
			    	// Si el botón dice VOLVER ATRÁS, vuelvo a la principal
			        if (btnAGM.getText().equals("VOLVER PARA OBTENER REGIONES")) {
			        	ventanaGrafo.this.dispose();
			            ventanaPrincipal.setVisible(true);
			            ventanaPrincipal.toFront();		    
			            return;
			        }
			        // Si el botón dicen OBTENER AGM
			        try {

			        } catch (IllegalArgumentException ex) {
			            JOptionPane.showMessageDialog(ventanaGrafo.this, ex.getMessage());
			            return;
			        }
			        lblTitulo.setText("ÁRBOL GENERADOR MÍNIMO");
			        //cambio el texto del botón una vez mostrado el agm
			        btnAGM.setText("VOLVER PARA OBTENER REGIONES");
			    }
			});		
			btnAGM.setBounds(189, 354, 280, 23);
			contentPane.add(btnAGM);	
		}
	}		
