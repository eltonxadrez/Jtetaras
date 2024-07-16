package jtetas.game.board;

import java.util.ArrayList;
//import java.util.concurrent.CopyOnWriteArrayList;

public class Peca {
	
	public ArrayList<Unidade> unidades;
//	public CopyOnWriteArrayList<Unidade> unidades;
	
	public int id;
	public int y;
	public int x;
	public TipoPeca tipoPeca;
	
	public Peca() {
//		this.unidades = new CopyOnWriteArrayList<Unidade>();
		this.unidades = new ArrayList<Unidade>();
	}

	public Peca(int y, int x, TipoPeca tipoPeca, int id) {
		
		this.id = id;
		this.y = y;
		this.x = x;
		this.tipoPeca = tipoPeca;
		
//		this.unidades = new CopyOnWriteArrayList<Unidade>();
		this.unidades = new ArrayList<Unidade>();
		
		switch (tipoPeca) {
		case BLOCO_L:
			this.criarBlocoL();
			break;
		case BLOCO_LI:
			this.criarBlocoLI();
			break;
		case BLOCO_QUADRADO: 
			this.criarBlocoQuadrado();
			break;
		case BLOCO_N:
			this.criarBlocoN();
			break;
		case BLOCO_NI:
			this.criarBlocoNI();
			break;
		case BLOCO_T:
			this.criarBlocoT();
			break;
		case BLOCO_PALITO:
			this.criarBlocoPalito();
			break;
		}
		
	}
	
	private void criarUnidade(int y, int x, int id, boolean isRotateCenter) {
		this.unidades.add(new Unidade(y, x, id, isRotateCenter));
	}
	
	// 01 PECA L
	
	//  0, 0 C   0,+1 B  0,+2 A
	// +1, 0 D
	
	//  X E X 0 0
	//  X 0 0 0 0 
	//  0 0 0 0 0 
	//  0 0 0 0 0 
	//  0 0 0 0 0 
	//  0 0 0 0 0 
	//  0 0 0 0 0 
	//  0 0 0 0 0 
	//  0 0 0 0 0 
	
	public void criarBlocoL() {
		criarUnidade(y, x, 1, false);
		criarUnidade(y, x + 1, 2, true);
		criarUnidade(y, x + 2, 3, false);
		criarUnidade(y + 1, x, 4, false);
	}
	
	// 02 PECA LI
	
	//  0, 0 C   0,+1 B  0,+2 A
	// 				    +1, 0 D
	
	//  X E X 0 0
	//  0 0 X 0 0 
	//  0 0 0 0 0 
	//  0 0 0 0 0 
	//  0 0 0 0 0 
	//  0 0 0 0 0 
	//  0 0 0 0 0 
	//  0 0 0 0 0 
	//  0 0 0 0 0 
	
	public void criarBlocoLI() {
		criarUnidade(y, x, 1, false);
		criarUnidade(y, x + 1, 2, true);
		criarUnidade(y, x + 2, 3, false);
		criarUnidade(y + 1 , x + 2, 4, false );
	}
	
	// 03 PECA QUADRADO
	
	//  0, 0   0,+1
	// +1, 0  +1,+1
	
	//  X X 0 0 0
	//  X X 0 0 0 
	//  0 0 0 0 0 
	//  0 0 0 0 0 
	//  0 0 0 0 0 
	//  0 0 0 0 0 
	//  0 0 0 0 0  
	//  0 0 0 0 0 
	//  0 0 0 0 0 
	
	public void criarBlocoQuadrado() {
		criarUnidade(y, x, 1, false);
		criarUnidade(y, x + 1, 2, false);
		criarUnidade(y + 1, x, 3, false);
		criarUnidade(y + 1, x + 1, 4, false);
	}
	
	// 04 PECA N
	
	//  	   0,+1  0,+2
	// +1, 0  +1,+1
	
	//  0 E X 0 0
	//  X X 0 0 0 
	//  0 0 0 0 0 
	//  0 X 0 0 0 
	//  0 R X 0 0 
	//  0 0 X 0 0 
	//  0 0 0 0 0 
	//  0 0 X X 0 
	//  0 X R 0 0 
	//  0 0 0 0 0 
	
	public void criarBlocoN() {
		criarUnidade(y, x + 1, 1, true);
		criarUnidade(y, x + 2, 2, false);
		criarUnidade(y + 1, x, 3, false);
		criarUnidade(y + 1, x + 1, 4, false);
	}
	
	// 05 PECA NI
	
	//  0, 0  +1, 0
	//   	  +1,+1  +1,+2  
	
	//  X R 0 0 0
	//  0 X X 0 0 
	//  0 0 0 0 0 
	//  0 0 0 0 0 
	//  0 0 0 0 0 
	//  0 0 0 0 0 
	//  0 0 0 0 0 
	//  0 0 0 0 0 
	//  0 0 0 0 0 
	
	public void criarBlocoNI() {
		criarUnidade(y, x, 1, false);
		criarUnidade(y, x + 1, 2, true);
		criarUnidade(y + 1, x + 1, 3, false);
		criarUnidade(y + 1, x + 2, 4, false);
	}
	
	// 06 PECA T
	
	//  0, 0   0,+1   0,+2
	//        +1,+1  
	
	//  X E X 0 0
	//  0 X 0 0 0 
	//  0 0 0 0 0 
	//  0 0 0 0 0 
	//  0 0 0 0 0 
	//  0 0 0 0 0 
	//  0 0 0 0 0 
	//  0 0 0 0 0 
	//  0 0 0 0 0 
	
	public void criarBlocoT() {
		criarUnidade(y, x, 1, false);
		criarUnidade(y, x + 1, 2, true);
		criarUnidade(y, x + 2, 3, false);
		criarUnidade(y + 1, x + 1, 4, false);
	}
	
	// 07 PECA PALITO
	
    //  4, 0  4,+1  4,+2  4,+3
	
	//  0 0 0 0 0 
	//  0 0 0 0 0 
	//  0 0 0 0 0 
	//  X X E X 0
	//  0 0 0 0 0 
	//  0 0 0 0 0 
	//  0 0 0 0 0 
	//  0 0 0 0 0 
	//  0 0 0 0 0 
	//  0 0 0 0 0 
	//  0 0 0 0 0 
	//  0 0 0 0 0 
	
	public void criarBlocoPalito() {
		criarUnidade(y, x, 1, false);
		criarUnidade(y, x + 1, 2, false);
		criarUnidade(y, x + 2, 3, true);
		criarUnidade(y, x + 3, 4, false);
	}
	
	public Peca clonarPeca() {
		Peca pecaClone = new Peca();
		pecaClone.id = this.id;
		pecaClone.tipoPeca = this.tipoPeca;
		pecaClone.y = this.y;
		pecaClone.x = this.x;
		for(Unidade unidade : this.unidades) {
			pecaClone.unidades.add(unidade.clonarUnidade());
		}
		return pecaClone;
	}
	
	public ArrayList<Unidade> clonarUnidades(){
		ArrayList<Unidade> unidades = new ArrayList<Unidade>();
		for(Unidade unidade : this.unidades) {
			unidades.add(unidade.clonarUnidade());
		}
		return unidades;
	}

}
