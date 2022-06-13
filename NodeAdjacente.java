public class NodeAdjacente {
    /*
    Classe que implementa um nó que indica um vértice adjacente. Portanto, esse nó
    nunca está contido no array de arestas, mas é apontado, pelo atributo "proximo"
    de algum outro nó.
    */
    private int indice; // valor do vértice
    private NodeAdjacente proximo;

    public NodeAdjacente(int indice) {
        this.indice = indice;
        proximo = null;
    }

    public NodeAdjacente proximo() {
        return proximo;
    }
    public int indice() {
        return indice;
    }
}
