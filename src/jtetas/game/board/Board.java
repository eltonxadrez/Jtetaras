package jtetas.game.board;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ListIterator;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
//import java.util.concurrent.CopyOnWriteArrayList;

import jtetas.game.Game;
import jtetas.game.Regra;
import jtetas.graphics.Concreto;

public class Board implements Concreto {
	
	public Regra regra;
	public Game game;
	
	public char[][] boardM;
	public char[][] cBoardM;
	public ArrayList<Peca> pecas;
//	CopyOnWriteArrayList<Peca> pecas;// = new CopyOnWriteArrayList<T>();
	private final ReadWriteLock lock = new ReentrantReadWriteLock();
	
	public char vazio = 'v';
	public char ocupado = 'o';

	//tamanho de cada bloco do tabuleiro
	public int width = 30;
	public int height = 30;
	
	public int y;
	public int x;
	
	public int boardPosY;
	public int boardPosX;
	
	public int proxPecaPosX;
	public int proxPecaPosY;
	
	public boolean pecaCaindo;
	public Peca pecaAtual;
	public Peca pecaProx;
	public boolean palitoRotate;
	private boolean boardTravado;
	private boolean gameOver;

	public Board(int y, int x, Game game) {
		this.y = y;
		this.x = x;
		
		this.game = game;
		
		this.boardPosY = 2;
		this.boardPosX = 5;
		
		this.proxPecaPosY = 1;
		this.proxPecaPosX = 12;
		
		this.createBoardM(y, x);
		this.createCBoardM(y, x);
		
//		this.pecas = new CopyOnWriteArrayList<Peca>();
		this.pecas = new ArrayList<Peca>();
		this.pecaCaindo = false;
		this.palitoRotate = true;
		this.boardTravado = false;
		this.gameOver = false;
	}
	
	public void createCBoardM(int y, int x) {
		this.cBoardM = new char[y][x];
		this.cleanBoardM(this.cBoardM);
	}
	
	public void createBoardM(int y, int x) {
		this.boardM = new char[y][x];
		this.cleanBoardM(this.boardM);
	}
	
	private void cleanBoardM(char[][] board) {
		this.fillBoard(this.vazio, board);
	}	
	
	public void fillBoard(char caractere, char[][] board) {
		for (int y = 0; y < board.length; y++) {
			for (int x = 0; x < board[0].length; x++) {
				board[y][x] = caractere;
			}
		}
	}
	
	public void fillBoardTimed(char caractere, char[][] board) {
		for (int y = board.length - 1; y >= 0; y--) {
			for (int x = 0; x < board[0].length; x++) {
				this.game.renderizador.render();
				board[y][x] = caractere;
				try {
					Thread.sleep(1000/60);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	
	public boolean adicionarPeca(Peca peca) {
		
        lock.writeLock().lock();
        try {
            
        
		
//		System.out.println("ADICIONA PECA NO BOARD");
//		System.out.println("ATUALIZA CLONE BOARD");
//        	System.out.println("setar peca atual");
        	this.pecaAtual = peca;
        	this.updateBoard(cBoardM);
        	if(this.adicionarPecaNoBoard(peca)) {
        		this.pecas.add(peca);
//			System.out.println("add true");
        		return true;
        	}
        	else {
        		this.pecas.add(peca);
//			System.err.println("ADICIONAR PECA FALSE - BOARD");
        		this.boardTravado = true;
        		return false;			
        	}
        } finally {
            lock.writeLock().unlock();
        }
	}
	
	//verifica se pode adicionar peca
	private boolean adicionarPecaNoBoard(Peca peca) {
		if(this.pecaAtual != null) {
//			System.out.println("is not null XX");
			for (Unidade unidade : this.pecaAtual.unidades) {
				if(this.cBoardM[unidade.y][(unidade.x)] == this.ocupado) {
//					System.out.println(this.cBoardM[unidade.y][(unidade.x)] + "/*-");
//					System.out.println("add b false");
					return false;
				}
			}
		}
//		System.out.println("add b true");
		return true;
	}
	
	
	//TURN RIGHT
	public void pecaTR() {
		if(isPalito()) {
			// angulo de rotação em graus
			int angleDegrees = 90;
			this.calculoRotacao(angleDegrees);			
		}
	}
	
	//TURN LEFT
	public void pecaTL() {
		if(isPalito()) {
			// angulo de rotação em graus
			int angleDegrees = -90;
			this.calculoRotacao(angleDegrees);
		}
	}
	
	private boolean isPalito() {
		if(this.pecaAtual.tipoPeca == TipoPeca.BLOCO_PALITO) {
			int angleDegrees = 0;
			if(this.palitoRotate) {
				angleDegrees = 90;
				this.palitoRotate = false;
			}
			else {
				angleDegrees = -90;
				this.palitoRotate = true;
			}
			this.calculoRotacao(angleDegrees);
			return false;
		}//this.pecaAtual.tipoPeca == TipoPeca.BLOCO_N
		if(this.pecaAtual.tipoPeca == TipoPeca.BLOCO_N || this.pecaAtual.tipoPeca == TipoPeca.BLOCO_NI) {
			int angleDegrees = 0;
			if(this.palitoRotate) {
				angleDegrees = -90;
				this.palitoRotate = false;
			}
			else {
				angleDegrees = 90;
				this.palitoRotate = true;
			}
			this.calculoRotacao(angleDegrees);
			return false;
		}
		return true;
	}
	
	private void calculoRotacao(int angleDegrees) {
		
		Peca pecaClone = this.pecaAtual.clonarPeca();
		
		// fator de escala para manter precisão em aritmética fixa
        int scaleFactor = 10000;
        
        double angleRadians = Math.toRadians(angleDegrees);
        int cosThetaFixed = (int) (Math.cos(angleRadians) * scaleFactor);
        int sinThetaFixed = (int) (Math.sin(angleRadians) * scaleFactor);
        
        Unidade unidadeAlterRot = null;
        
		for (Unidade unidade : pecaClone.unidades) {
			if(unidade.isRotateCenter) {
				unidadeAlterRot = unidade;
			}
		}
		
		if(unidadeAlterRot != null) {
			int pivotX = unidadeAlterRot.x;
			int pivotY = unidadeAlterRot.y;
			
			// Rotacionar cada ponto em torno do ponto de pivot
			for (Unidade unidade : pecaClone.unidades) {
				// Subtrai as coordenadas do ponto de pivot
				int x = unidade.x - pivotX;
				int y = unidade.y - pivotY;
				
				// Aplica a rotação usando aritmética fixa
				int xNew = (x * cosThetaFixed - y * sinThetaFixed) / scaleFactor;
				int yNew = (x * sinThetaFixed + y * cosThetaFixed) / scaleFactor;
				
				// Adiciona as coordenadas do ponto de pivot de volta
				unidade.x = xNew + pivotX;
				unidade.y = yNew + pivotY;
			}
		}
		
		if(isFreeBoardRotate(pecaClone)) {
			this.pecaAtual.unidades = pecaClone.unidades;
		}
		
	}
	
	private boolean isFreeBoardRotate(Peca pecaClone) {
		for (Unidade unidade : pecaClone.unidades) {
			
			if(unidade.x >= 10) {
				return false;
			}
			if(unidade.x < 0) {
				return false;
			}
			if(unidade.y >= 24) {
				return false;
			}
			if(this.cBoardM[unidade.y][(unidade.x)] == this.ocupado) {
				return false;
			}
		}
		return true;
	}
	
	//X++
	public void pecaXP() {
		if(isFreeToMoveXP() && isFreeAXP()) {
			this.pecaAtual.x += 1;
			for (Unidade unidade : pecaAtual.unidades) {
				unidade.x += 1;				
			}			
		}
	}
	
	//verifica colisao do board no eixo X + 1
	private boolean isFreeToMoveXP() {
		for (Unidade unidade : pecaAtual.unidades) {
			if(unidade.x + 1 >= 10) {
				return false;
			}
		}
		return true;
	}
	
	//verifica colisao com outras pecas no eixo X + 1
	private boolean isFreeAXP() {
		for (Unidade unidade : pecaAtual.unidades) {
			if(this.cBoardM[unidade.y][(unidade.x + 1)] == this.ocupado) {
				return false;
			}
		}
		return true;
	}
	
	//X--
	public void pecaXN() {
		if(isFreeToMoveXN() && isFreeAXN()) {
			this.pecaAtual.x -= 1;
			for (Unidade unidade : pecaAtual.unidades) {
				unidade.x -= 1;
			}			
		}
	}
	
	//verifica colisao do board no eixo X - 1	
	private boolean isFreeToMoveXN() {
		for (Unidade unidade : pecaAtual.unidades) {
			if(unidade.x - 1 < 0) {
				return false;
			}
		}
		return true;
	}
	
	//verifica colisao com outras pecas no eixo X - 1
	private boolean isFreeAXN() {
		for (Unidade unidade : pecaAtual.unidades) {
			if(this.cBoardM[unidade.y][(unidade.x - 1)] == this.ocupado) {
				return false;
			}
		}
		return true;
	}
	
	public void pecaFP() {
		if(!boardTravado) {
			while(true) {
				if(isFreeToMoveYP() && isFreeAYP()) {
					this.pecaAtual.y += 1;
					for (Unidade unidade : pecaAtual.unidades) {
						unidade.y += 1;
					}	
				}
				else {
					this.pecaCaindo = false;
					updateBoard(cBoardM);
					verificarLinhaCompletada();
					updateBoard(cBoardM);
					this.pecaAtual = new Peca(0,0,TipoPeca.BLOCO_QUADRADO,99);
//					if(!boardTravado) {
//						this.regra.tick();
//					}
					break;
				}
			}
		}
	}
	
	//Y++
	public void pecaYP() {
		if(isFreeToMoveYP() && isFreeAYP()) {
			this.pecaAtual.y += 1;
			for (Unidade unidade : pecaAtual.unidades) {
				unidade.y += 1;
			}			
		}
		else {
			this.pecaCaindo = false;
			//delecao de linha
			updateBoard(cBoardM);
			verificarLinhaCompletada();
			updateBoard(cBoardM);
			
			this.pecaAtual = new Peca(0,0,TipoPeca.BLOCO_QUADRADO,99);
			if(!boardTravado) {
				this.regra.tick();				
			}
		}
	}

	private void verificarLinhaCompletada() {
		ArrayList<Integer> linhas = new ArrayList<Integer>();
		for(Unidade unidade : this.pecaAtual.unidades) {
			boolean linhaCompleta = true;
			for (int x = 0; x < this.cBoardM[unidade.y].length; x++) {
				if(cBoardM[unidade.y][x] == vazio) {
					linhaCompleta = false;
					break;
				}
			}
			if(linhaCompleta) {
				if(!linhas.contains(unidade.y)) {
					linhas.add(unidade.y);					
				}
			}
			else {
			}
		}
		Collections.sort(linhas);
		for (Integer linha : linhas) {
			deletarLinha(linha);
		}
		
	}
	
	private void deletarLinha(Integer linha) {
		
		lock.writeLock().lock();
		try {
			ArrayList<Unidade> listaOrganizada = new ArrayList<Unidade>();
 			for (Peca peca : pecas) {
 				for (Unidade unidade : peca.unidades) {
 					if(unidade.y == linha) {
// 						System.out.println(unidade.y);
 						listaOrganizada.add(unidade);
 					}										
				}
			}
			Collections.sort(listaOrganizada);
			for (Unidade unidade : listaOrganizada) {
				unidade.remover();
				updateBoard(cBoardM);
				this.game.renderizador.render();
				try {
					Thread.sleep(1000/60);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
//			ListIterator<Unidade> itUnidade = listaOrganizada.listIterator();
//			listaOrganizada = null;
//			while(itUnidade.hasNext()) {
//				linhaDeletada.add(itUnidade.next());
//				System.out.println("LINHA DELETADA");
//				itUnidade.re
//			}
//			System.err.println("LINHA DELETADA -> " + linha);
		shiftDown(linha);
		} finally {
			lock.writeLock().unlock();
		}
	}	
	
	private void shiftDown(Integer linhaMaisBaixa) {
//		System.out.println("ILHAS MOVER NA LINHA -> " + linhaMaisBaixa);
		lock.writeLock().lock();
		try {
			for (Peca peca : pecas) {
				for (Unidade unidade : peca.unidades) {
					if(unidade.y <= linhaMaisBaixa) {
						unidade.y += 1;
					}
				}
			}
//			System.err.println("ILHA MOVIDA");
			updateBoard(cBoardM);
		} finally {
			lock.writeLock().unlock();
		}
	}
	//verifica colisao do board no eixo Y + 1
	private boolean isFreeToMoveYP() {
		for (Unidade unidade : pecaAtual.unidades) {
			if(unidade.y + 1 >= 24) {
				return false;
			}
		}
		return true;
	}
	
	//verifica colisao com outras pecas no eixo Y + 1
	private boolean isFreeAYP() {
		for (Unidade unidade : pecaAtual.unidades) {
			if(this.cBoardM[unidade.y+1][(unidade.x)] == this.ocupado) {
				return false;
			}
		}
		return true;
	}
	
	//REFATORAR PARA PUXAR DO CLONE
	public void updateBoard(char[][] board) {
		System.out.println("att board");
//		lock.writeLock().lock();
        lock.readLock().lock();
        try {
        	this.cleanBoardM(board);
        	for (Peca peca : this.pecas) {
        		for (Unidade unidade : peca.unidades) {
        			board[unidade.y][unidade.x] = ocupado;
        		}			
        	}
        } finally {
            lock.readLock().unlock();
        }
	}
	
	public void gameOver() {
		this.gameOver = true;
		this.fillBoardTimed(ocupado, this.boardM);
		this.fillBoardTimed(vazio, this.boardM);
	}
	
	@Override
	public void render(Graphics graphics) {
		
//		System.out.println("ATUALIZA BOARD GRAFICO");
		if(!gameOver) {
			updateBoard(this.boardM);
		}
		
		for (int y = 4; y < boardM.length; y++) {
			
			for (int x = 0; x < boardM[0].length; x++) {
				
				if(boardM[y][x] == vazio) {
					graphics.setColor(Color.BLACK);
					graphics.fillRect((x + boardPosX) * width, (y - boardPosY) * height, width, height);	
					graphics.setColor(Color.GRAY);
					graphics.drawRect((x + boardPosX) * width, (y - boardPosY) * height, width, height);
				}
				
				else {
					graphics.setColor(Color.WHITE);
					graphics.fillRect((x + boardPosX) * width, (y - boardPosY) * height, width, height);	
					graphics.setColor(Color.BLACK);
					graphics.drawRect((x + boardPosX) * width, (y - boardPosY) * height, width, height);
				}
			}
		}
		if(this.pecaProx != null) {
			for (Unidade unidade : this.pecaProx.unidades) {
				graphics.setColor(Color.WHITE);
				graphics.fillRect((unidade.x + proxPecaPosX) * width, (unidade.y - proxPecaPosY) * height, width, height);	
				graphics.setColor(Color.BLACK);
				graphics.drawRect((unidade.x + proxPecaPosX) * width, (unidade.y - proxPecaPosY) * height, width, height);
			}
		}
//		else {
//			this.fillBoardTimed(ocupado, this.boardM);
//		}
	}
}
