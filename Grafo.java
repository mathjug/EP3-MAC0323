import java.util.*;
import java.io.*;
import java.util.Scanner;

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

    public Grafo(String endereco) throws IOException {
        File arquivo = new File(endereco);
        Scanner input = new Scanner(arquivo);
        
        V = input.nextInt();
        E = input.nextInt();
        input.nextLine();
        arestas = (NodeGrafo<Item>[]) new Object[V];

        for (int e = 0; e < E; e++) {
            
        }
        
        input.close();
    }

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
        int[] distancias = new int[V];
        Queue<Integer> fila = new LinkedList<>();

        for (int i = 0; i < V; i++)
            distancias[i] = -1;
        distancias[indice] = 0;
        fila.add(indice);

        while (!fila.isEmpty()) {
            int atual = fila.remove();
            NodeAdjacente aux = arestas[atual].proximo(); // a primeira iteração é feita separadamente, porque arestas[atual]
                                                            // não é da classe NodeAdjacente, mas da classe NodeGrafo
            if (aux != null) {
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
        }
        return distancias;
    }
    
    public int[] buscaConexas() { 
        int c = 0;
        boolean[] marcados = new boolean[V];
        int[] componentes = new int[V];
        for (int i = 0; i < V; i++) {
            marcados[i] = false;
            componentes[i] = -1;
        }
        for (int i = 0; i < V; i++) {
            if (!marcados[i]) {
                dfsR(i, marcados);
                for (int j = 0; j < V; j++)
                    if (marcados[j] && componentes[j] == -1)
                        componentes[j] = c;
                c++;
            }
        }
        return componentes;
    }

    private void dfsR(int v, boolean[] marked) {
        marked[v] = true;
        NodeAdjacente aux = arestas[v].proximo(); // a primeira iteração é feita separadamente, porque arestas[atual]
                                                    // não é da classe NodeAdjacente, mas da classe NodeGrafo
        if (aux != null) {
            int k = aux.indice();
            if (!marked[k])
                dfsR(k, marked);

            for (int i = 0; i < arestas[v].nAdjacentes() - 1; i++) {
                aux = aux.proximo();
                int w = aux.indice();
                if (!marked[w])
                    dfsR(w, marked);
            }
        }
    }
}