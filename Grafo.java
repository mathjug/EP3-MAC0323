import java.util.*;
import java.io.*;
import java.util.Scanner;
import java.util.Random;

public class Grafo {
    /*
    Classe que implementa um grafo, com os seguintes atributos:
        V = número de vértices
        E = número de arestas
        arestas = array com V listas ligadas, cada uma contendo os vértices adjacentes àquele contido no vetor
    */
    private int V;
    private int E;
    private NodeGrafo[] arestas;

    private class NodeGrafo {
        /*
        Classe que implementa um nó utilizado para implementar um grafo, contido no array
        de arestas.
        */
        private int valor; // valor do vértice
        private NodeGrafo proximo;
        private int n_adjacentes; // guarda o número de vértices adjacentes (é -1 caso seja um nó na lista dos adjacentes)

        private NodeGrafo(int valor) {
            this.valor = valor;
            proximo = null;
            n_adjacentes = -1;
        }

        private int nAdjacentes() {
            return n_adjacentes;
        }
        private NodeGrafo proximo() {
            return proximo;
        }
        private int valor() {
            return valor;
        }

        private void add(int v) {
            NodeGrafo aux = this;
            for (int i = 0; i < this.n_adjacentes; i++)
                aux = aux.proximo;
            aux.proximo = new NodeGrafo(v);
            this.n_adjacentes++;
        }
    }

    public Grafo(String endereco) throws IOException {
        File arquivo = new File(endereco);
        Scanner input = new Scanner(arquivo);
        
        V = input.nextInt();
        E = input.nextInt();
        //input.nextLine(); // apenas para pular para a próxima linha
        arestas = new NodeGrafo[V];
        for (int v = 0; v < V; v++) {
            arestas[v] = new NodeGrafo(v);
            arestas[v].n_adjacentes = 0;
        }

        for (int e = 0; e < E; e++) {
            int u = input.nextInt();
            int v = input.nextInt();
            arestas[u].add(v);
            arestas[v].add(u);
        }
        input.close();
    }

    public Grafo(int V, double p) {
        /*
        Construtor que, recebendo o número de vértices e a probabilidade p de cada uma das arestas
        existir, gera um grafo (0 <= p <= 1).
        */
        this.V = V;
        arestas = new NodeGrafo[V];
        for (int v = 0; v < V; v++) {
            arestas[v] = new NodeGrafo(v);
            arestas[v].n_adjacentes = 0;
        }
        Random rand = new Random();
        int e = 0;
        for (int i = 0; i < V; i++) {
            for (int j = 0; j < V; j++) {
                if (i != j) {
                    double prob = rand.nextDouble();
                    if (prob < p) {
                        arestas[i].add(j);
                        arestas[j].add(i);
                        e++;
                    }
                }
            }
        }
        E = e;
    }

    public Grafo(int V, int E, NodeGrafo[] arestas) {
        /*
        Construtor que, recebendo o número de vértices, o número de arestas e o array de arestas,
        gera um grafo com esses atributos.
        */
        this.V = V;
        this.E = E;
        this.arestas = arestas;
    }

    public int[] encontraDistancias(int indice) { // fazer busca em largura
        int[] distancias = new int[V];
        Queue<Integer> fila = new LinkedList<>();

        for (int i = 0; i < V; i++)
            distancias[i] = -1;
        distancias[indice] = 0;
        fila.add(indice);

        while (!fila.isEmpty()) {
            int atual = fila.remove();
            NodeGrafo aux = arestas[atual];
            if (aux != null) {
                for (int j = 0; j < arestas[atual].nAdjacentes(); j++) { // percorrendo os adjacentes de 'atual'
                    aux = aux.proximo();
                    int v = aux.valor();
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
        int[] componentes = new int[V + 1];
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
        componentes[V] = c; // guarda o número de componentes na última posição do array
        return componentes;
    }

    private void dfsR(int v, boolean[] marked) {
        marked[v] = true;
        NodeGrafo aux = arestas[v];
        if (aux != null) {
            for (int i = 0; i < arestas[v].nAdjacentes(); i++) {
                aux = aux.proximo();
                int w = aux.valor();
                if (!marked[w])
                    dfsR(w, marked);
            }
        }
    }

    public static void main (String[] args) throws IOException {
        int V = 300;
        Grafo G = new Grafo(V, 0.01);
        int[] conexas = G.buscaConexas();
        System.out.println(conexas[V]);
        G.encontraDistancias(2);
    }
}