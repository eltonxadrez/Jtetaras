package jtetas.game.board;

public class Unidade {
	
	public int y, x;
	public int id;
	public boolean isRotateCenter;
	
	public Unidade() {
		
	}
	
	public Unidade(int y, int x, int id, boolean isRotateCenter) {
		this.y = y;
		this.x = x;
		this.id = id;
		this.isRotateCenter = isRotateCenter;
	}
	
	public Unidade clonarUnidade() {
		Unidade unidadeClone = new Unidade();
		unidadeClone.y = this.y;
		unidadeClone.x = this.x;
		unidadeClone.id = this.id;
		unidadeClone.isRotateCenter = this.isRotateCenter;
		return unidadeClone;
	}

}
