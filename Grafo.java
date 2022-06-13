import java.util.*;

public class Grafo<Item> {
    /*
    Classe que implementa um grafo, com os seguintes atributos:
        V = número de vértices
        E = número de arestas
        arestas = array com V listas ligadas, cada uma contendo os vértices adjacentes àquele contido no vetor
    */
    private int V;
    private int E;
    private NodeGrafo<Item>[] arestas;

    public Grafo(int V, int E, NodeGrafo<Item>[] arestas) {
        /*
        Construtor que, recebendo o número de vértices, o número de arestas e o array de arestas,
        gera um grafo com esses atributos.
        */
        this.V = V;
        this.E = E;
        this.arestas = arestas;
    }

    public int[] encontraDistancias(NodeGrafo<Item>[] arestas, int indice) { // fazer busca em largura
        NodeGrafo<Item> u = arestas[indice];
        int[] distancias = new int[V];
        Queue<Integer> fila = new LinkedList<>();

        for (int i = 0; i < V; i++)
            distancias[i] = -1;
        distancias[indice] = 0;
        fila.add(indice);

        NodeAdjacente aux = u.proximo(); // primeiro adjacente do inicial
        distancias[aux.indice()] = 1;
        fila.add(aux.indice());

        while (!fila.isEmpty()) {
            int atual = fila.remove();

            aux = arestas[atual].proximo(); // a primeira iteração é feita separadamente, porque arestas[atual] não é
                                            // da classe NodeAdjacente, mas da classe NodeGrafo
            int v = aux.indice();
            if (distancias[v] == -1) {
                distancias[v] = distancias[atual] + 1;
                fila.add(v);
            }
            
            for (int j = 0; j < arestas[atual].nAdjacentes() - 1; j++) { // percorrendo os adjacentes de 'atual'
                aux = aux.proximo();
                v = aux.indice();
                if (distancias[v] == -1) {
                    distancias[v] = distancias[atual] + 1;
                    fila.add(v);
                }
            }
        }
        return distancias;
    }    
}