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
        palavras = array de Strings que apenas é diferente de null quando o grafo for composto por Strings
    */
    private int V;
    private int E;
    private NodeGrafo[] arestas;
    private String[] palavras;  

    private class NodeGrafo {
        /*
        Classe auxiliar que implementa um nó utilizado como unidade básica para representar um grafo.
        */
        private int valor; // valor do vértice
        private NodeGrafo proximo;
        private int n_adjacentes; // guarda o número de vértices adjacentes (é diferente de -1 apenas se for um nó
                                  // diretamente contido no array de nós)

        private NodeGrafo(int valor) {
            this.valor = valor;
            proximo = null;
            n_adjacentes = -1;
        }

        private void add(int v) {
            NodeGrafo aux = this;
            for (int i = 0; i < this.n_adjacentes; i++)
                aux = aux.proximo;
            aux.proximo = new NodeGrafo(v);
            this.n_adjacentes++;
        }
    }

    public Grafo(String endereco_palavras, int option) throws IOException {
        /*
        Construtor que cria um grafo a partir de um arquivo de texto. Esse arquivo pode ser em dois formatos,
        de acordo com a opção passada como argumento para este construtor. Portanto, "option" pode obter dois
        valores:
            1 = arquivo gerador de um grafo de inteiros.
            2 = arquivo gerador de um grafo de Strings.
        O formato de cada arquivo é melhor explicado em cada um dos dois métodos auxiliares que são aqui chamados.
        */
        if (option == 1)
            construtorInteiros(endereco_palavras);
        else
            construtorStrings(endereco_palavras);
    }

    public Grafo(int V, double p) {
        /*
        Construtor que, recebendo o número de vértices e a probabilidade p de cada uma das arestas existir, gera
        um grafo de inteiros (0 <= p <= 1). Cada vértice recebe um inteiro entre 0 e V - 1, incluindo os dois ex-
        tremos.
        */
        this.V = V;
        arestas = new NodeGrafo[V];
        for (int v = 0; v < V; v++) {
            arestas[v] = new NodeGrafo(v);
            arestas[v].n_adjacentes = 0;
        }
        Random rand = new Random();
        E = 0;
        for (int i = 0; i < V; i++) {
            for (int j = 0; j < V; j++) {
                if (i != j) {
                    double prob = rand.nextDouble();
                    if (prob < p) {
                        arestas[i].add(j);
                        arestas[j].add(i);
                        E++;
                    }
                }
            }
        }
    }

    private void construtorInteiros(String endereco) throws IOException {
        /*
        Método de construção auxiliar que gera um grafo de inteiros a partir das informações de um arquivo de texto.
        A primeira linha desse arquivo contém o número de vértices e arestas do grafo, enquanto as linhas subsequentes
        indicam quais as arestas que compõem esse grafo.

        É importante destacar que os vértices são valorados com inteiros entre 0 e V - 1, incluindo os dois extremos.
        */
        File arquivo = new File(endereco);
        Scanner input = new Scanner(arquivo);
        
        V = input.nextInt();
        E = input.nextInt();
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

    private void construtorStrings(String endereco_palavras) throws IOException {
        /*
        Método de construção auxiliar que gera um grafo de Strings a partir das informações de um arquivo de texto.
        Cada linha desse arquivo contém uma palavra a ser inserida no grafo. A forma de se construir o grafo - de tal
        forma que dois vértices estão conectados se, e somente se, suas palavras diferem-se em apenas uma letra - usa
        de uma estratégia encontrada no seguinte site:

        https://panda.ime.usp.br/panda/static/pythonds_pt/07-Grafos/BuildingtheWordLadderGraph.html

        Além disso, foi utilizada uma classe importada de biblioteca externa: ArrayList, que nada mais é do que um
        array dinâmico (já implementado em EPs anteriores).
        */
        E = 0;
        V = 0;
        File arquivo = new File(endereco_palavras);
        Scanner input = new Scanner(arquivo);
        ArrayList<String> palavras = new ArrayList<String>();
        ArrayList<String> baldes = new ArrayList<String>();
        ArrayList<ArrayList<Integer> > dicionario = new ArrayList<ArrayList<Integer> >();
        int contador = 0;
        
        while (input.hasNext()) {
            String palavra = input.next();
            palavras.add(palavra);
            for (int i = 0; i < palavra.length(); i++) {
                String balde = palavra.substring(0, i) + "_" + palavra.substring(i + 1);
                int indice = baldes.indexOf(balde);
                if (indice >= 0) // balde está em baldes
                    dicionario.get(indice).add(contador);
                else { // balde não está em baldes
                    baldes.add(balde);
                    ArrayList<Integer> novo_balde = new ArrayList<Integer>();
                    novo_balde.add(contador);
                    dicionario.add(novo_balde);
                }
            }
            contador++;
            V++;
        }

        String[] aux = new String[V];
        aux = palavras.toArray(aux);
        this.palavras = aux; // palavras passa a armazenar Strings com as palavras contidas no grafo

        arestas = new NodeGrafo[V]; // inicializa o objeto que armazena conjunto de arestas do grafo
        for (int v = 0; v < V; v++) {
            arestas[v] = new NodeGrafo(v);
            arestas[v].n_adjacentes = 0;
        }

        for (int i = 0; i < baldes.size(); i++) { // aqui, criam-se as arestas necessárias
            for (int palavra1 : dicionario.get(i)) {
                for (int palavra2 : dicionario.get(i)) {
                    if (palavra1 != palavra2) {
                        arestas[palavra1].add(palavra2);
                        arestas[palavra2].add(palavra1);
                        E++;
                    }
                }
            }
        }
        input.close();
    }

    public int[] encontraDistancias(int indice) {
        /*
        Método que, dado o índice de um vértice do grafo, calcula a distância de cada um dos outros vértices até
        ele, por meio de uma busca em largura. Caso o índice não tenha um valor válido, retorna null.
        */
        if (indice >= 0 && indice < V)
            return encontraDistAux(indice);
        else
            return null;
    }

    public int[] encontraDistancias(String palavra) {
        /*
        Método que, dado um String, calcula a distância de cada um dos outros vértices (Strings) até ele, por meio
        de uma busca em largura. Caso o String não esteja no grafo, retorna null.
        */
        Integer indice = null;
        if (palavras != null) {
            for (int i = 0; i < V; i++) {
                if (palavras[i].equals(palavra)) {
                    indice = i;
                    break;
                }
            }
        }
        if (indice != null)
            return encontraDistAux(indice);
        else
            return null; // caso em que a palavra não está no grafo
    }

    private int[] encontraDistAux(int indice) {
        /*
        Método auxiliar privativo para o cálculo das distâncias. Assume-se que o valor do índice é válido.
        */
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
                for (int j = 0; j < arestas[atual].n_adjacentes; j++) { // percorrendo os adjacentes de 'atual'
                    aux = aux.proximo;
                    int v = aux.valor;
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
        /*
        Método que encontra as componentes conexas de um grafo. Retorna um array de inteiros de tamanho V + 1, tal
        que o inteiro em cada índice corresponde à qual componente (identificada por um inteiro maior ou igual a zero)
        o vértice de mesmo índice pertence, e, na última posição, guarda o número de componentes conexas no grafo.
        */
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
        /*
        Método auxiliar que faz uma chamada recursiva da busca em profundidade em um grafo. Portanto, dado o
        índice de um vértice, percorre todos os seus adjacentes e chama a busca para cada um deles.
        */
        marked[v] = true;
        NodeGrafo aux = arestas[v];
        if (aux != null) {
            for (int i = 0; i < arestas[v].n_adjacentes; i++) {
                aux = aux.proximo;
                int w = aux.valor;
                if (!marked[w])
                    dfsR(w, marked);
            }
        }
    }

    public static void main (String[] args) throws IOException {
        Scanner input_usuarios = new Scanner(System.in); 
        System.out.println("Qual o tipo de grafo a ser criado?\n (1) Grafo de inteiros (a partir de um arquivo).");
        System.out.print(" (2) Grafo de inteiros (aleatório).\n (3) Grafo de Strings.\nSua escolha: ");
        int tipo = input_usuarios.nextInt();
        Grafo G;
        if (tipo == 1 || tipo == 3) {
            System.out.print("Qual o endereço do arquivo? ");
            input_usuarios.nextLine();
            String endereco = input_usuarios.nextLine();
            if (tipo == 1)
            G = new Grafo(endereco, 1);
            else
                G = new Grafo(endereco, 2);
        }
        else {
            System.out.print("V = ");
            int V = input_usuarios.nextInt();
            System.out.print("p = ");
            Double p = input_usuarios.nextDouble();
            G = new Grafo(V, p);
        }
        input_usuarios.close();

        int[] conexas = G.buscaConexas();
        System.out.printf("\nNúmero de componentes conexas: %d\n\n", conexas[G.V]);
        System.out.println("-- Calcula as distâncias para o primeiro vértice adicionado --");
        int[] distancias = G.encontraDistancias(0);
        if (G.palavras != null)
            for (int i = 0; i < G.V; i++)
                System.out.printf("%s -> %d\n", G.palavras[i], distancias[i]);
        else
            for (int i = 0; i < G.V; i++)
                System.out.printf("%d -> %d\n", i, distancias[i]);
    }
}