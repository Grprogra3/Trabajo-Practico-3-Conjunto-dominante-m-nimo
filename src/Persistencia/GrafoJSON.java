package Persistencia;

import Grafo.Grafo;

import Grafo.Vertice;
import com.google.gson.Gson;
import com.google.gson.JsonParseException;

import java.io.IOException;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

/**
 * Carga un grafo desde un archivo JSON con el formato:
 * 
 * <pre>
 * {
 *   "vertices": [1, 2, 3, 4],
 *   "aristas":  [[1, 2], [2, 3], [3, 4]]
 * }
 * </pre>
 */
public final class GrafoJSON {

    private GrafoJSON() {
    }

    // estructura intermedia que Gson completa a partir del archivo
    private static class DatosGrafo {
        List<Integer> vertices;
        List<int[]> aristas;
    }

    public static Grafo cargar(Path archivo) throws IOException {
    	DatosGrafo datos;
        try (Reader lector = Files.newBufferedReader(archivo, StandardCharsets.UTF_8)) {
            datos = new Gson().fromJson(lector, DatosGrafo.class); 
        } catch (JsonParseException e) {
            throw new IllegalArgumentException("El archivo no es un JSON válido: " + e.getMessage(), e);
        }
        return construir(datos);
    }

    private static Grafo construir(DatosGrafo datos) {
        if (datos == null || datos.vertices == null)
            throw new IllegalArgumentException("El JSON debe tener una lista \"vertices\"");

        Grafo grafo = new Grafo();
        for (Integer valor : datos.vertices) {
            if (valor == null)
                throw new IllegalArgumentException("Hay un vértice nulo en la lista");
            grafo.agregarVertice(new Vertice(valor));
        }

        if (datos.aristas != null) {
            for (int[] arista : datos.aristas) {
                if (arista == null || arista.length != 2)
                    throw new IllegalArgumentException("Cada arista debe tener exactamente 2 vértices, ej: [1, 2]");

                Vertice a = grafo.getVertice(arista[0]);
                Vertice b = grafo.getVertice(arista[1]);
                if (a == null || b == null)
                    throw new IllegalArgumentException("La arista [" + arista[0] + ", " + arista[1]
                            + "] usa un vértice que no está en \"vertices\"");
                grafo.agregarArista(a, b);
            }
        }
        return grafo;
    }
}
