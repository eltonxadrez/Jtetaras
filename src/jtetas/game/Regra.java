package jtetas.game;

import java.util.ArrayList;

import jtetas.game.board.Board;
import jtetas.game.board.Peca;
import jtetas.game.board.TipoPeca;
import jtetas.game.input.Teclado;

public class Regra implements Entity, Runnable {
	
	public Board board;
	public Teclado teclado;
	
	public int velocidadeQueda = 40;
	public int idList = 0;
	public boolean gameOver = false;
	public boolean pausado = false;
	private volatile boolean rodarLoop = true;
	
	public ArrayList<Peca> bolsaPeca;
	
	public Regra(Board board) {
		this.board = board;
		this.bolsaPeca = new ArrayList<Peca>();
		this.board.pecaAtual = puxarDaBolsaPeca();
		this.board.pecaProx = puxarDaBolsaPeca();
	}
	
	public void finalizarThread() {
		rodarLoop = false;
	}
	
	private Peca puxarDaBolsaPeca () {
		if(bolsaPeca.isEmpty()) {
			criarBolsaPeca();
			return puxarPecaRandomDaBolsa();
		}
		else {
			return puxarPecaRandomDaBolsa();
		}
	}
	
	private Peca puxarPecaRandomDaBolsa() {
		int numeroSorteado = (int) ((Math.random() * ((this.bolsaPeca.size()-1) - 1)) + 1);
		Peca pecaPuxada = this.bolsaPeca.get(numeroSorteado);
		this.bolsaPeca.remove(pecaPuxada);
		return pecaPuxada;
	}
	
	private void criarBolsaPeca() {
		for (TipoPeca tipoPeca : TipoPeca.values()) {
			//entre 3 e 2
			int quantidade = (int) ((Math.random() * (5 - 2)) + 1);
			for (int i = 0; i < quantidade; i++) {
				Peca novaPeca = new Peca(4, 4, tipoPeca, this.idList);
				this.bolsaPeca.add(novaPeca);
				this.idList++;
			}
		}
	}
	
	//DELETAR
	@SuppressWarnings("unused")
	private Peca criarPecaAleatoria() {
		
		Peca pecaAleatoria = null;
		int numeroAleatorio = (int) ((Math.random() * (8 - 1)) + 1);
		
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
	
	public boolean adicionarPeca(Peca peca) {
		return this.board.adicionarPeca(peca);
	}
	
	@Override
	public void tick() {
		if(!this.board.pecaCaindo && !this.gameOver) {
			if(this.adicionarPeca(this.board.pecaProx)) {
				this.board.pecaProx = puxarDaBolsaPeca();
				this.board.pecaCaindo = true;
			}
			else {
				System.out.println("GAME OVER");
				this.board.pecaYP();
				this.gameOver = true;
				this.board.gameOver();
			}
		}
		else if(this.gameOver) {
			System.out.println("GAME OVER");
			this.finalizarThread();
		}
		else {
			this.board.pecaYP();
		}
	}
	
	public void pausarJogo() {
		if(pausado) {
			this.despausarTeste();
			this.pausado = false;
			this.teclado.tecladoLivre = true;
		}
		else { 
			this.pausado = true;
			this.teclado.tecladoLivre = false;
		}
	}
	
	public synchronized void pausarTeste() {
		try {
			wait();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public synchronized void despausarTeste() {
		notifyAll();
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
			if(pausado) {
				this.pausarTeste();
			}
			tick();
			try {
				Thread.sleep(10000/velocidadeQueda);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
	}
}