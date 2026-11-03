package Grafo;

import Grafo.Grafo;
import Grafo.Vertice;
import Persistencia.GrafoJSON;
import java.io.File;
import java.io.IOException;
import java.util.Set;

public class ControladorGrafo {
    private Grafo grafo;

    public ControladorGrafo() {
        this.grafo = new Grafo();
    }

    public void agregarVertice(int valor) {
        grafo.agregarVertice(new Vertice(valor));
    }

    public void agregarArista(int valor1, int valor2) {
        Vertice a = grafo.getVertice(valor1);
        Vertice b = grafo.getVertice(valor2);
        if (a == null || b == null) {
            throw new IllegalArgumentException("Uno o ambos vertices no existen");
        }
        grafo.agregarArista(a, b);
    }

    public void cargarDesdeJSON(File archivo) throws IOException {
        this.grafo = GrafoJSON.cargar(archivo.toPath());
    }

    public Grafo getGrafo() {
        return this.grafo;
    }

    public int getCantidadVertices() {
        return grafo.getVertices().size();
    }

    public int getCantidadAristas() {
        return grafo.getAristas().size();
    }

    public boolean estaVacio() {
        return grafo.getVertices().isEmpty();
    }

   
    public Set<Vertice> calcularConjuntoDominante() {
        Grafo dominante = grafo.ConjuntoDominanteMinimo();
        return dominante.getVertices();
    }
}
