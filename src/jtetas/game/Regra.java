package jtetas.game;

import java.util.ArrayList;

import jtetas.game.board.Board;
import jtetas.game.board.Peca;
import jtetas.game.board.TipoPeca;

public class Regra implements Entity, Runnable {
	
	public Board board;
	
	public int velocidadeQueda = 10;
	public int idList = 0;
	public boolean gameOver = false;
	private volatile boolean rodarLoop = true;
	
	//debugOnly
	private int initialBlocoX = 0;
	
	public ArrayList<Peca> bolsaPeca;
	
	public Regra(Board board) {
		this.board = board;
		this.bolsaPeca = new ArrayList<Peca>();
	}
	
	public void finalizarThread() {
		rodarLoop = false;
	}
	
	private void criarBolsaPeca() {
		
	}
	
	private Peca criarPecaAleatoria() {
		
		Peca pecaAleatoria = null;
		int numeroAleatorio = (int) ((Math.random() * (8 - 1)) + 1);
		
//		System.out.println(numeroAleatorio);
		
		switch (numeroAleatorio) {
		case 1: 
			pecaAleatoria = new Peca(4, 4, TipoPeca.BLOCO_L, this.idList);
			break;
		case 2: 
			pecaAleatoria = new Peca(4, 4, TipoPeca.BLOCO_LI, this.idList);
			break;
		case 3: 
			pecaAleatoria = new Peca(4, 4, TipoPeca.BLOCO_QUADRADO, this.idList);
			break;
		case 4: 
			pecaAleatoria = new Peca(4, 4, TipoPeca.BLOCO_N, this.idList);
			break;
		case 5: 
			pecaAleatoria = new Peca(4, 4, TipoPeca.BLOCO_NI, this.idList);
			break;
		case 6: 
			pecaAleatoria = new Peca(4, 4, TipoPeca.BLOCO_T, this.idList);
			break;
		case 7: 
			pecaAleatoria = new Peca(4, 3, TipoPeca.BLOCO_PALITO, this.idList);
			break;
		}
		return pecaAleatoria;
	}
	
	//REFATORAR
	@Override
	public void tick() {
//		System.out.println("TICK");
		if(!this.board.pecaCaindo && !this.gameOver) {
//			System.out.println("TICK2");
			//debugOnly
//			if(this.adicionarPeca(new Peca(4, initialBlocoX, TipoPeca.BLOCO_QUADRADO, this.idList))) {
			if(this.adicionarPeca(criarPecaAleatoria())) {
//				System.out.println("TICK3");
				this.idList++;
				this.board.pecaCaindo = true;
				
				//debugOnly
//				this.initialBlocoX += 2;
//				if(this.initialBlocoX >= 10) {
//					this.initialBlocoX = 0;
//				}
				
//				try {
//					Thread.sleep(50);
//				} catch (InterruptedException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
			}
			else {
//				System.out.println("TICK4");
				this.board.pecaYP();
				this.gameOver = true;
				System.out.println("GAME OVER");
			}
		}
		//fazer algo no game over
		else if(this.gameOver) {
//			System.out.println("TICK5");
			this.finalizarThread();
			System.out.println("GAME OVER");
		}
		else {
//			System.out.println("TICK6");
			this.board.pecaYP();
		}
	}
	
	public boolean adicionarPeca(Peca peca) {
//		System.err.println("ADICIONAR PECA - REGRA");
		return this.board.adicionarPeca(peca);
	}

	@Override
	public void run() {
		Thread.currentThread().setName("TRD-REGRA");
		try {
			Thread.sleep(500);
		} catch (Exception e) {
			e.printStackTrace();
		}
		while(rodarLoop) {

			tick();
			try {
				
				Thread.sleep(10000/velocidadeQueda);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}