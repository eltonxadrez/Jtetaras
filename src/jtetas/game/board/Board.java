package jtetas.game.board;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.ListIterator;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
//import java.util.concurrent.CopyOnWriteArrayList;

import jtetas.game.Regra;
import jtetas.graphics.Concreto;

public class Board implements Concreto {
	
	public Regra regra;
	
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
	
	public boolean pecaCaindo;
	public Peca pecaAtual;
	public Peca pecaProx;
	public boolean palitoRotate;
	private boolean boardTravado;

	public Board(int y, int x) {
		this.y = y;
		this.x = x;
		
		this.boardPosY = 2;
		this.boardPosX = 5;
		
		this.createBoardM(y, x);
		this.createCBoardM(y, x);
		
//		this.pecas = new CopyOnWriteArrayList<Peca>();
		this.pecas = new ArrayList<Peca>();
		this.pecaCaindo = false;
		this.palitoRotate = true;
		this.boardTravado = false;
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
	
	public boolean adicionarPeca(Peca peca) {
		
//		System.out.println("ADICIONA PECA NO BOARD");
//		System.out.println("ATUALIZA CLONE BOARD");
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
					if(!boardTravado) {
						this.regra.tick();
					}
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
//		int[] linhas = new int[4];
		ArrayList<Integer> linhas = new ArrayList<Integer>();
		int shiftDown = 0;
		int linhaMaisBaixa = 0;
		for(Unidade unidade : this.pecaAtual.unidades) {
			boolean linhaCompleta = true;
			for (int x = 0; x < this.cBoardM[unidade.y].length; x++) {
				if(cBoardM[unidade.y][x] == vazio) {
//					System.out.println("ha vazio na linha " + unidade.y + " X -> " + x);
					linhaCompleta = false;
					break;
				}
			}
			//remover redundancia de linha
			if(linhaCompleta) {
//				System.err.println("A linha Y " + unidade.y + " está completa");
				if(!linhas.contains(unidade.y)) {
					linhas.add(unidade.y);					
					shiftDown++;
				}
				if(unidade.y > linhaMaisBaixa) {
					linhaMaisBaixa = unidade.y;
				}
//				System.out.println();
			}
			else {
//				System.out.println("Não está completa a Linha Y " + unidade.y);
			}
		}
		for (Integer linha : linhas) {
//			System.out.println("deletando linha -> " + linha);
			deletarLinha(linha);
		}
//		System.out.println("quantos shifts down eu preciso fazer? " + shiftDown);
		shiftDown(shiftDown, linhaMaisBaixa);
	}
	
	private void shiftDown(int shiftDown, int linhaMaisBaixa) {
		for (Peca peca : pecas) {
			for (Unidade unidade : peca.unidades) {
				if(unidade.y <= linhaMaisBaixa) {
					unidade.y += shiftDown;
				}
			}
		}
	}
	
	private void deletarLinha(Integer linha) {
		for (Peca peca : pecas) {
			ListIterator<Unidade> itUnidade = peca.unidades.listIterator();
			while(itUnidade.hasNext()) {
				if(itUnidade.next().y == linha) {
					itUnidade.remove();
				}					
			}
		}
		System.out.println("LINHA DELETADA");
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
//		lock.writeLock().lock();
//		lock.readLock().lock();
		this.cleanBoardM(board);
		for (Peca peca : this.pecas) {
			for (Unidade unidade : peca.unidades) {
				board[unidade.y][unidade.x] = ocupado;
			}			
		}
	}
	
	@Override
	public void render(Graphics g) {
		
//		System.out.println("ATUALIZA BOARD GRAFICO");
		updateBoard(this.boardM);
		
		for (int y = 4; y < boardM.length; y++) {
			
			for (int x = 0; x < boardM[0].length; x++) {
				
				if(boardM[y][x] == vazio) {
					g.setColor(Color.BLACK);
					g.fillRect((x + boardPosX) * width, (y - boardPosY) * height, width, height);	
					g.setColor(Color.GRAY);
					g.drawRect((x + boardPosX) * width, (y - boardPosY) * height, width, height);
				}
				
				else {
					g.setColor(Color.WHITE);
					g.fillRect((x + boardPosX) * width, (y - boardPosY) * height, width, height);	
					g.setColor(Color.BLACK);
					g.drawRect((x + boardPosX) * width, (y - boardPosY) * height, width, height);
				}
			}
		}
	}
}
