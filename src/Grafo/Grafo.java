package Grafo;

import java.util.*;
import java.util.PriorityQueue;
import java.util.Queue;

public class Grafo {
    private final HashMap<Integer, Vertice> vertices=new HashMap<>();
    private final List<Arista> aristas=new ArrayList<>();

    public void agregarVertice(Vertice v) {
        if (v == null) throw new IllegalArgumentException("el vertice no puede ser null");
        if(vertices.containsKey(v.getValor())) throw new IllegalArgumentException("el valor ya existe en el conjunto");
        vertices.put(v.getValor(),v);
    }

    public void agregarArista(Vertice a, Vertice b) {
        verificarExiste(a);
        verificarExiste(b);
        if (a.getVecinos().contains(b))
            throw new IllegalArgumentException("La arista " + a + "-" + b + " ya existe");

        a.agregarVecino(b);
        b.agregarVecino(a);
        aristas.add(new Arista(a, b));
    }

    public Set<Vertice> getVertices() { return Set.copyOf(vertices.values());  }
    public List<Arista> getAristas() { return Collections.unmodifiableList(aristas); }

    // devuelve el vertice del grafo con ese valor, o null si no existe
    public Vertice getVertice(int valor) { return vertices.get(valor); }

    public Set<Vertice> getVecinos(Vertice v) {
        verificarExiste(v);
        return v.getVecinos();
    }

    public Set<Vertice> recorrer(Vertice inicio) {
        verificarExiste(inicio);
        Set<Vertice> visitados = new LinkedHashSet<>();
        Queue<Vertice> cola = new LinkedList<>();
        visitados.add(inicio);
        cola.add(inicio);
        while (!cola.isEmpty()) {
            for (Vertice v : cola.remove().getVecinos()) {
                if (visitados.add(v)) cola.add(v);
            }
        }
        return visitados;
    }

    public boolean esConexo() {
        if (vertices.isEmpty()) return true;
        return recorrer(vertices.values().iterator().next()).size() == vertices.size();
    }

    private void verificarExiste(Vertice v) {
        if (v == null || !vertices.containsKey(v.getValor()))
            throw new IllegalArgumentException("el vertice no existe en el grafo");
    }

    public Grafo ConjuntoDominanteMinimo(){
        Set<Vertice> visitados = new HashSet<>();
        Grafo conjuntoD =new Grafo();

        Queue<Candidato> pila = new PriorityQueue<>();
        for (Vertice v : vertices.values()) {
            pila.add(new Candidato(v, 1 + v.getVecinos().size()));
        }

        while(visitados.size()!=vertices.size()){
            Vertice verticePila = Objects.requireNonNull(pila.poll()).vertice; //reordeno
            int gananciaReal = calcularGanancia(verticePila,visitados);
            if(gananciaReal==0){continue;}
            if(pila.isEmpty()||gananciaReal>=calcularGanancia(pila.peek().vertice,visitados)){
            conjuntoD.agregarVertice(verticePila);
            visitados.addAll(verticePila.getVecinos());
            visitados.add(verticePila);}
            else{
            pila.add(new Candidato(verticePila,gananciaReal));}
        }

return conjuntoD;
    }

    private int calcularGanancia(Vertice v1, Set<Vertice> visitados) {

        int ganancia=(visitados.contains(v1))? (ganancia=0):(ganancia=1);
        for (Vertice vecino : v1.getVecinos()) {
            if (!visitados.contains(vecino)) {
                ganancia++;  //contar solo vecinos NO visitados
            }
    }
    return ganancia;}

    private static class Candidato implements Comparable<Candidato>{
        Vertice vertice;
        int gananciaEstimada;

        Candidato(Vertice vertice, int gananciaEstimada) {
            this.vertice = vertice;
            this.gananciaEstimada = gananciaEstimada;
        }

        @Override
        public int compareTo(Candidato o) {
            // orden inverso: la PriorityQueue devuelve primero el de MAYOR ganancia
            return Integer.compare(o.gananciaEstimada, this.gananciaEstimada);
        }
    }

    @Override
    public String toString() {
        List<Vertice> vertices = new ArrayList<>(this.vertices.values());
        StringBuffer impresion=new StringBuffer();
        int numeroV=1;
        for(Vertice v:vertices)
        {

            impresion.append("vertice n° "+numeroV+"="+v+"\n");
        }
        return impresion.toString();
    }
}
