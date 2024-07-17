package jtetas.game.board;

public class Unidade implements Comparable<Unidade> {
	
	public int y, x;
	public int id;
	public boolean isRotateCenter;
	public Peca pecaLinkada;
	
	public Unidade() {
		
	}
	
	public Unidade(int y, int x, int id, boolean isRotateCenter, Peca pecaLinkada) {
		this.y = y;
		this.x = x;
		this.id = id;
		this.isRotateCenter = isRotateCenter;
		this.pecaLinkada = pecaLinkada;
	}
	
	public Unidade clonarUnidade() {
		Unidade unidadeClone = new Unidade();
		unidadeClone.y = this.y;
		unidadeClone.x = this.x;
		unidadeClone.id = this.id;
		unidadeClone.isRotateCenter = this.isRotateCenter;
		unidadeClone.pecaLinkada = this.pecaLinkada;
		return unidadeClone;
	}
	
	public int compareTo(Unidade unidade) {
	     return(this.x - unidade.x);
	}
	
	public void remover() {
		this.pecaLinkada.unidades.remove(this);
		this.pecaLinkada = null;
	}

}
