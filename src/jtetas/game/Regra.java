package jtetas.game;

import java.util.ArrayList;

import jtetas.game.board.Board;
import jtetas.game.board.Peca;
import jtetas.game.board.TipoPeca;

public class Regra implements Entity, Runnable {
	
	public Board board;
	
	public int velocidadeQueda = 1;
	public int idList = 0;
	public boolean gameOver = false;
	private volatile boolean rodarLoop = true;
	
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
		
		System.out.println(numeroAleatorio);
		
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
		if(!this.board.pecaCaindo && !this.gameOver) {
			if(this.adicionarPeca(new Peca(4, 4, TipoPeca.BLOCO_PALITO, this.idList))) {
//			if(this.adicionarPeca(criarPecaAleatoria())) {
				
				this.idList++;
				this.board.pecaCaindo = true;	
				try {
					Thread.sleep(50);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			else {
				this.board.pecaYP();
				this.gameOver = true;
				System.out.println("GAME OVER");
			}
		}
		//fazer algo no game over
		else if(this.gameOver) {
			this.finalizarThread();
			System.out.println("GAME OVER");
		}
		else {
			System.out.println(" ");
			System.out.println("y" + this.board.pecaAtual.y + "x" + this.board.pecaAtual.x);
			this.board.pecaYP();
		}
	}
	
	public boolean adicionarPeca(Peca peca) {
		return this.board.adicionarPeca(peca);
	}

	@Override
	public void run() {
		try {
			Thread.sleep(500);
		} catch (Exception e) {
			e.printStackTrace();
		}
		while(rodarLoop) {

			tick();
			try {
				
				Thread.sleep(10000/5);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}