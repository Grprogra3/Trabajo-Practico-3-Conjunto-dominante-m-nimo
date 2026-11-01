package Interfaz;

import Grafo.Grafo;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

public class ventanaGrafo extends JFrame {

	private static final long serialVersionUID = 1L;
	private static final String RUTA_FONDO = "/Interfaz/imagenes/fondo.jpg";

	private final ventanaPrincipal ventanaPrincipal;
	private final Grafo grafo;
	private PanelGrafo panelGrafo;
	private BufferedImage imagenFondo;

	public ventanaGrafo(ventanaPrincipal ventanaPrincipal, Grafo grafo) {
		this.ventanaPrincipal = ventanaPrincipal;
		this.grafo = grafo;
		initialize();
	}

	public void initialize() {
		setTitle("Visualización del grafo");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 760, 640);
		imagenFondo = cargarImagen(RUTA_FONDO);

		JPanel contentPane = new JPanel(new BorderLayout(0, 10)) {
			private static final long serialVersionUID = 1L;

			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				if (imagenFondo != null) g.drawImage(imagenFondo, 0, 0, null);
			}
		};
		contentPane.setBackground(Color.WHITE);
		contentPane.setBorder(new EmptyBorder(10, 10, 10, 10));
		setContentPane(contentPane);

		JLabel lblTitulo = new JLabel("GRAFO (" + grafo.getVertices().size() + " vértices, "
				+ grafo.getAristas().size() + " aristas)", SwingConstants.CENTER);
		lblTitulo.setForeground(Color.BLACK);
		lblTitulo.setFont(new Font("Tahoma", Font.BOLD, 18));
		contentPane.add(lblTitulo, BorderLayout.NORTH);

		panelGrafo = new PanelGrafo(grafo);
		contentPane.add(panelGrafo, BorderLayout.CENTER);

		JPanel botones = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
		botones.setOpaque(false);

		JButton btnDominante = new JButton("CALCULAR CONJUNTO DOMINANTE");
		btnDominante.addActionListener(e -> {
			try {
				Grafo dominante = grafo.ConjuntoDominanteMinimo();
				panelGrafo.setResaltados(dominante.getVertices());
				lblTitulo.setText("CONJUNTO DOMINANTE: " + dominante.getVertices().size() + " vértices");
			} catch (RuntimeException ex) {
				JOptionPane.showMessageDialog(this, "No se pudo calcular: " + ex.getMessage());
			}
		});
		botones.add(btnDominante);

		JButton btnVolver = new JButton("VOLVER");
		btnVolver.addActionListener(e -> dispose());
		botones.add(btnVolver);

		contentPane.add(botones, BorderLayout.SOUTH);

		// al cerrar (por botón o por la X) vuelvo a la ventana principal
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosed(WindowEvent e) {
				ventanaPrincipal.setVisible(true);
				ventanaPrincipal.toFront();
			}
		});
	}

	private BufferedImage cargarImagen(String ruta) {
		try (InputStream is = getClass().getResourceAsStream(ruta)) {
			return (is == null) ? null : ImageIO.read(is);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
}
