public class NodeGrafo<Item> {
    /*
    Classe que implementa um nó utilizado para implementar um grafo, contido no array
    de arestas.
    */
    private Item valor; // valor do vértice
    private NodeAdjacente proximo;
    private int n_adjacentes; //  guarda o número de vértices adjacentes

    public NodeGrafo(Item valor) {
        this.valor = valor;
        proximo = null;
        n_adjacentes = -1;
    }

    public int nAdjacentes() {
        return n_adjacentes;
    }
    public NodeAdjacente proximo() {
        return proximo;
    }
    public Item valor() {
        return valor;
    }
}
