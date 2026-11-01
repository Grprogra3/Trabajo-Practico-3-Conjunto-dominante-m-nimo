package Interfaz;

import Grafo.Arista;
import Grafo.Grafo;
import Grafo.Vertice;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.util.*;
import java.util.List;

/**
 * Dibuja un grafo: cada vértice es un círculo con su valor y cada arista una línea.
 * Los vértices se ubican inicialmente en círculo y se pueden arrastrar con el mouse.
 * Opcionalmente se puede resaltar un conjunto de vértices (ej: el conjunto dominante).
 */
public class PanelGrafo extends JPanel {

    private static final long serialVersionUID = 1L;
    private static final int RADIO = 20;
    private static final int MARGEN = 40;

    private static final Color COLOR_ARISTA = new Color(70, 70, 90);
    private static final Color COLOR_VERTICE = new Color(52, 128, 172);
    private static final Color COLOR_RESALTADO = new Color(230, 120, 40);
    private static final Color COLOR_DOMINADO = new Color(150, 200, 225);

    private final Grafo grafo;
    // posiciones normalizadas (0..1) para que el dibujo se adapte al tamaño del panel
    private final Map<Vertice, Point2D.Double> posiciones = new HashMap<>();
    private Set<Vertice> resaltados = Collections.emptySet();
    private Vertice arrastrado;

    public PanelGrafo(Grafo grafo) {
        this.grafo = grafo;
        setOpaque(false);
        ubicarEnCirculo();

        MouseAdapter mouse = new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                arrastrado = verticeEn(e.getPoint());
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                if (arrastrado == null) return;
                double x = (e.getX() - MARGEN) / (double) Math.max(1, getWidth() - 2 * MARGEN);
                double y = (e.getY() - MARGEN) / (double) Math.max(1, getHeight() - 2 * MARGEN);
                posiciones.put(arrastrado, new Point2D.Double(limitar(x), limitar(y)));
                repaint();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                arrastrado = null;
            }

            @Override
            public void mouseMoved(MouseEvent e) {
                setCursor(verticeEn(e.getPoint()) != null
                        ? Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)
                        : Cursor.getDefaultCursor());
            }
        };
        addMouseListener(mouse);
        addMouseMotionListener(mouse);
    }

    /** Resalta los vértices dados (y pinta más claro a los que dominan). null para limpiar. */
    public void setResaltados(Set<Vertice> vertices) {
        resaltados = (vertices == null) ? Collections.emptySet() : new HashSet<>(vertices);
        repaint();
    }

    private void ubicarEnCirculo() {
        List<Vertice> lista = new ArrayList<>(grafo.getVertices());
        lista.sort(Comparator.comparingInt(Vertice::getValor));
        int n = lista.size();
        for (int i = 0; i < n; i++) {
            double angulo = 2 * Math.PI * i / Math.max(1, n) - Math.PI / 2;
            posiciones.put(lista.get(i), new Point2D.Double(
                    0.5 + 0.5 * Math.cos(angulo),
                    0.5 + 0.5 * Math.sin(angulo)));
        }
    }

    private Point aPantalla(Vertice v) {
        Point2D.Double p = posiciones.get(v);
        int ancho = getWidth() - 2 * MARGEN;
        int alto = getHeight() - 2 * MARGEN;
        return new Point(MARGEN + (int) (p.x * ancho), MARGEN + (int) (p.y * alto));
    }

    private Vertice verticeEn(Point punto) {
        for (Vertice v : posiciones.keySet()) {
            if (aPantalla(v).distance(punto) <= RADIO) return v;
        }
        return null;
    }

    private static double limitar(double valor) {
        return Math.max(0, Math.min(1, valor));
    }

    private boolean estaDominado(Vertice v) {
        if (resaltados.isEmpty() || resaltados.contains(v)) return false;
        for (Vertice vecino : v.getVecinos()) {
            if (resaltados.contains(vecino)) return true;
        }
        return false;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        // aristas
        g2.setStroke(new BasicStroke(2.5f));
        g2.setColor(COLOR_ARISTA);
        for (Arista a : grafo.getAristas()) {
            Point p1 = aPantalla(a.getVertice1());
            Point p2 = aPantalla(a.getVertice2());
            g2.drawLine(p1.x, p1.y, p2.x, p2.y);
        }

        // vertices
        g2.setFont(getFont().deriveFont(Font.BOLD, 14f));
        FontMetrics fm = g2.getFontMetrics();
        for (Vertice v : posiciones.keySet()) {
            Point p = aPantalla(v);
            Ellipse2D circulo = new Ellipse2D.Double(p.x - RADIO, p.y - RADIO, 2 * RADIO, 2 * RADIO);

            Color relleno = resaltados.contains(v) ? COLOR_RESALTADO
                    : estaDominado(v) ? COLOR_DOMINADO : COLOR_VERTICE;
            g2.setColor(relleno);
            g2.fill(circulo);
            g2.setColor(Color.DARK_GRAY);
            g2.setStroke(new BasicStroke(2f));
            g2.draw(circulo);

            String texto = String.valueOf(v.getValor());
            g2.setColor(Color.WHITE);
            g2.drawString(texto, p.x - fm.stringWidth(texto) / 2, p.y + fm.getAscent() / 2 - 2);
        }
        g2.dispose();
    }
}
