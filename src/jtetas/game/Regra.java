package jtetas.game;

import java.util.ArrayList;
import java.util.Collections;

import jtetas.game.board.Board;
import jtetas.game.board.Peca;
import jtetas.game.board.TipoPeca;
import jtetas.game.input.Teclado;
import jtetas.graphics.Hud;

public class Regra implements Entity, Runnable {
	
	public Board board;
	public Teclado teclado;
	public Hud hud;
	public Game game;
	
	public int velocidadeQueda = 5;
	public int idList = 0;
	public int score = 0;
	public int linhasDeletadas = 0;
	public int linhasDeletadasNivel = 0;
	public int linhasDeletadasScore = 0;
	public int nivel = 1;
	public boolean gameOver = false;
	public boolean pausado = false;
	private volatile boolean rodarLoop = true;
	public boolean rotacaoTravada = false;
	
	public ArrayList<Peca> bolsaPeca;
	
	public Regra() {
		this.bolsaPeca = new ArrayList<Peca>();
		this.board.pecaAtual = puxarDaBolsaPeca();
		this.board.pecaProx = puxarDaBolsaPeca();
	}
	
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
			int linhaX = 4;
			if(tipoPeca == TipoPeca.BLOCO_PALITO) {
				linhaX = 3;
			}
			//entre 3 e 2
			int quantidade = (int) ((Math.random() * (4 - 2)) + 2);
			for (int i = 0; i < quantidade; i++) {
				Peca novaPeca = new Peca(4, linhaX, tipoPeca, this.idList);
				this.bolsaPeca.add(novaPeca);
				this.idList++;
			}
		}
//		Collections.shuffle(bolsaPeca);
//		Collections.shuffle(bolsaPeca);
//		Collections.shuffle(bolsaPeca);
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
	
	public void calcularScore() {
		if(this.linhasDeletadasScore > 0) {
			this.score += (this.linhasDeletadasScore * (this.linhasDeletadasScore * 10));
			this.hud.score = this.score;
		}
//		System.out.println("score - " + this.score);
	}
	
	public void calcularNivel() {
		
		if(this.linhasDeletadasNivel > 10) {
			this.nivel += 1;
			this.hud.nivel = this.nivel;
			this.linhasDeletadasNivel = 0;
		}
		this.velocidadeQueda = nivel * 5;
//		System.out.println("linhasDeletadas - " + this.linhasDeletadasNivel);
//		System.out.println("nivel - " + this.nivel);
//		System.out.println("velocidadeQueda - " + this.velocidadeQueda);
	}
	
	@Override
	public void tick() {
		if(!this.board.pecaCaindo && !this.gameOver) {
			if(this.adicionarPeca(this.board.pecaProx)) {
				this.board.pecaProx = puxarDaBolsaPeca();
				this.board.pecaCaindo = true;
				this.calcularNivel();
				this.calcularScore();
			}
			else {
				System.out.println("GAME OVER a");
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
//			this.teclado.tecladoLivre = true;
		}
		else { 
			this.pausado = true;
//			System.out.println("PAUSADO!");
//			this.teclado.tecladoLivre = false;
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
//			if(!this.teclado.tecladoLivre) {
//				System.out.println("TICK TECLADO LIVRE ? " + this.teclado.tecladoLivre);
//				this.teclado.tecladoLivre = true;				
//			}
//			System.out.println(" -- REGRA THREAD TICK -- ");
			if(pausado) {
				this.pausarTeste();
			}else {
				tick();				
			}
			try {
				Thread.sleep(10000/velocidadeQueda);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}