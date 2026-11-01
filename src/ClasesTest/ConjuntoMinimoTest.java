package ClasesTest;
import Grafo.Grafo;
import Grafo.Vertice;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.Assert.*;


public class ConjuntoMinimoTest {

    private Grafo g;
    private Vertice a, b, c ,d,e,f,h,i,j,k;


    @Before
    public void setUp2() {
        g = new Grafo();
        a = new Vertice(200);
        b = new Vertice(0);
        c = new Vertice(20);
        d = new Vertice(10);
        e = new Vertice(15);
        f = new Vertice(30);
        h = new Vertice(60);
        i = new Vertice(90);
        j = new Vertice(100);
        k = new Vertice(3);
        g.agregarVertice(a);
        g.agregarVertice(b);
        g.agregarVertice(c);
        g.agregarVertice(d);
        g.agregarVertice(e);
        g.agregarVertice(f);
        g.agregarVertice(h);
        g.agregarVertice(i);
        g.agregarVertice(j);
        g.agregarVertice(k);
        g.agregarArista(a, c);
        g.agregarArista(a, k);
        g.agregarArista(b, h);
        g.agregarArista(b, d);
        g.agregarArista(i, e);
        g.agregarArista(j, f);
    }

    @Test()
    public void obtenerConjuntoMinimo(){
        System.out.println(g.ConjuntoDominanteMinimo());
    }

    @Test
    public void validarPropiedadConjuntoDominante() {
        // Reemplazar con la inicialización de tu grafo donde el algoritmo da 6 en vez de 4


        Set<Vertice> resultado = g.ConjuntoDominanteMinimo().getVertices();

        for (Vertice v : g.getVertices()) {
            boolean estaEnResultado = resultado.contains(v);
            boolean tieneVecinoEnResultado = false;

            for (Vertice vecino : g.getVecinos( v)) {
                if (resultado.contains(vecino)) {
                    tieneVecinoEnResultado = true;
                    break;
                }
            }


            assertTrue(estaEnResultado || tieneVecinoEnResultado);
        }
    }
}
