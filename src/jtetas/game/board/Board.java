package jtetas.game.board;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
//import java.util.concurrent.CopyOnWriteArrayList;

import jtetas.game.Game;
import jtetas.game.Regra;
import jtetas.graphics.Concreto;
import jtetas.graphics.ImageRepository;

public class Board implements Concreto, Runnable {
	
	public Regra regra;
	public Game game;
	
	public Thread boardThread;
	
	public char[][] boardM;
	public char[][] cBoardM;
	public ArrayList<Peca> pecas;
	private final ReadWriteLock lock = new ReentrantReadWriteLock();
	
	public char vazio = 'v';
	public char yellow = 'o';
	public char cyan = 'i';
	public char red = 's';
	public char green = 'z';
	public char orange = 'l';
	public char blue = 'j';
	public char purple = 't';
	public char white = 'w';
	

	//tamanho de cada bloco do tabuleiro
//	public int width;
//	public int height;
	
//	public int y;
//	public int x;
	
//	public int boardPosY;
//	public int boardPosX;
	
//	public int proxPecaPosX;
//	public int proxPecaPosY;
	
	public Peca pecaAtual;
	public Peca pecaAtualPrevisao;
	public Peca pecaProx;
	public boolean pecaCaindo;
	public boolean palitoRotate;
	private boolean boardTravado;
	private boolean gameOver;
	
	ImageRepository imageRepository = new ImageRepository();
	
	private BufferedImage imgBlockYellow = this.imageRepository.getImageRepo(ImageRepository.IMG_BLOCK_YELLOW);
	private BufferedImage imgBlockCyan = this.imageRepository.getImageRepo(ImageRepository.IMG_BLOCK_CYAN);
	private BufferedImage imgBlockRed = this.imageRepository.getImageRepo(ImageRepository.IMG_BLOCK_RED);
	private BufferedImage imgBlockGreen = this.imageRepository.getImageRepo(ImageRepository.IMG_BLOCK_GREEN);
	private BufferedImage imgBlockOrange = this.imageRepository.getImageRepo(ImageRepository.IMG_BLOCK_ORANGE);
	private BufferedImage imgBlockBlue = this.imageRepository.getImageRepo(ImageRepository.IMG_BLOCK_BLUE);
	private BufferedImage imgBlockPurple = this.imageRepository.getImageRepo(ImageRepository.IMG_BLOCK_PURPLE);
	private BufferedImage imgBlockBlack = this.imageRepository.getImageRepo(ImageRepository.IMG_BLOCK_BLACK);
	
//		this.y = y;
//		this.x = x;

	public Board(int y, int x, Game game) {
		
		this.game = game;
		
//		this.boardPosY = this.game.height / 650;
//		this.boardPosX = this.game.width / 100;
//		this.boardPosY = 10;
//		this.boardPosX = 10;
		
//		int tamanhoBloco = (this.game.height + this.game.width) / 75;
		
//		this.width = tamanhoBloco;
//		this.height = tamanhoBloco;
		
//		this.proxPecaPosY = 1;
//		this.proxPecaPosX = 12;
		
//		this.proxPecaPosY = boardPosY - 1;
//		this.proxPecaPosX = boardPosX + 9;
		
		this.createBoardM(y, x);
		this.createCBoardM(y, x);
		
		this.pecas = new ArrayList<Peca>();
		this.pecaCaindo = false;
		this.palitoRotate = true;
		this.boardTravado = false;
		this.gameOver = false;
	}
	
	public void reiniciarBoard(int y, int x) {
		this.pecas = new ArrayList<Peca>();
		this.createBoardM(y, x);
		this.createCBoardM(y, x);
		this.gameOver = false;
		this.boardTravado = false;
	}
	
	
	public void gameOver() {
		this.gameOver = true;
		this.pecaProx = null;
		this.pecaAtualPrevisao = null;
		this.fillBoardTimed(white, this.boardM);//escolher cor
		this.fillBoardTimed(vazio, this.boardM);
		this.regra.finalizarThread();
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
		this.game.renderizar = false;
		for (int y = board.length - 1; y >= 0; y--) {
			for (int x = 0; x < board[0].length; x++) {
				this.game.renderizador.render();
				board[y][x] = caractere;
				try {
					Thread.sleep(100/15);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		this.game.renderizar = true;
	}
	
	public boolean adicionarPeca(Peca peca) {
        lock.writeLock().lock();
        try {
		
        	this.pecaAtual = peca;
        	this.pecaAtualPrevisao = peca.clonarPeca();
        	this.pecaFPPrevisao();
        	this.updateBoard(cBoardM);
        	if(this.adicionarPecaNoBoard(peca)) {
        		this.pecas.add(peca);
        		return true;
        	}
        	else {
        		this.pecas.add(peca);
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
			for (Unidade unidade : this.pecaAtual.unidades) {
				if(this.cBoardM[unidade.y][(unidade.x)] != this.vazio) {
					return false;
				}
			}
		}
		return true;
	}
	
	
	//TURN RIGHT
	public void pecaTR() {
		if(isPalito(this.pecaAtual)) {
//			isPalito(this.pecaAtualPrevisao);
			// angulo de rotação em graus
			int angleDegrees = 90;
			this.calculoRotacao(angleDegrees, this.pecaAtual);	
			this.calculoRotacao(angleDegrees, this.pecaAtualPrevisao);
			this.pecaFPPrevisao();
		}
	}
	
	//TURN LEFT
	public void pecaTL() {
		if(isPalito(this.pecaAtual)) {
//			isPalito(this.pecaAtualPrevisao);
			// angulo de rotação em graus
			int angleDegrees = -90;
			this.calculoRotacao(angleDegrees, this.pecaAtual);
			this.calculoRotacao(angleDegrees, this.pecaAtualPrevisao);
		}
	}
	
	private boolean isPalito(Peca peca) {
		if(peca.tipoPeca == TipoPeca.BLOCO_PALITO) {
			int angleDegrees = 0;
			if(this.palitoRotate) {
				angleDegrees = 90;
				this.palitoRotate = false;
			}
			else {
				angleDegrees = -90;
				this.palitoRotate = true;
			}
			this.calculoRotacao(angleDegrees, this.pecaAtual);
			this.calculoRotacao(angleDegrees, this.pecaAtualPrevisao);
			return false;
		}
		if(peca.tipoPeca == TipoPeca.BLOCO_N || peca.tipoPeca == TipoPeca.BLOCO_NI) {
			int angleDegrees = 0;
			if(this.palitoRotate) {
				angleDegrees = -90;
				this.palitoRotate = false;
			}
			else {
				angleDegrees = 90;
				this.palitoRotate = true;
			}
			this.calculoRotacao(angleDegrees, this.pecaAtual);
			this.calculoRotacao(angleDegrees, this.pecaAtualPrevisao);
			return false;
		}
		return true;
	}
	
	private void calculoRotacao(int angleDegrees, Peca peca) {
		
		Peca pecaClone = peca.clonarPeca();
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
			peca.unidades = pecaClone.unidades;
//			this.pecaAtualPrevisao.unidades = pecaClone.unidades;
		}
		this.pecaFPPrevisao();
		
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
			if(this.cBoardM[unidade.y][(unidade.x)] != this.vazio) { //CUIDADO
				return false;
			}
		}
		return true;
	}
	
	//X++
	public void pecaXP() {
		if(isFreeToMoveXP() && isFreeAXP()) {
			this.pecaXPM(pecaAtual);
			this.pecaXPM(pecaAtualPrevisao);
			this.pecaFPPrevisao();
			
		}
	}

	private void pecaXPM(Peca peca) {
		peca.x += 1;
		for (Unidade unidade : peca.unidades) {
			unidade.x += 1;				
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
			if(this.cBoardM[unidade.y][(unidade.x + 1)] != this.vazio) {
				return false;
			}
		}
		return true;
	}
	
	//X--
	public void pecaXN() {
		if(isFreeToMoveXN() && isFreeAXN()) {
			this.pecaXNM(pecaAtual);
			this.pecaXNM(pecaAtualPrevisao);
			this.pecaFPPrevisao();
		}
	}

	private void pecaXNM(Peca peca) {
		peca.x -= 1;
		for (Unidade unidade : peca.unidades) {
			unidade.x -= 1;
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
			if(this.cBoardM[unidade.y][(unidade.x - 1)] != this.vazio) {
				return false;
			}
		}
		return true;
	}
	
	public void resetarYPrevisao() {
		int quantidadeResetada = this.pecaAtual.y;
		this.pecaAtualPrevisao.y = quantidadeResetada;
		this.pecaAtualPrevisao.unidades = this.pecaAtual.clonarUnidades();
	}
	
	public void pecaFPPrevisao() {
		this.resetarYPrevisao();
		if(!boardTravado) {
			while(true) {
				if(isFreeToMoveYP(this.pecaAtualPrevisao) && isFreeAYP(this.pecaAtualPrevisao)) {
					this.pecaAtualPrevisao.y += 1;
					for (Unidade unidade : pecaAtualPrevisao.unidades) {
						unidade.y += 1;
					}	
				}
				else {
					break;
//					this.pecaCaindo = false;					
//					this.regra.pausarJogo();
//					updateBoard(cBoardM);
//					verificarLinhaCompletada();
//					updateBoard(cBoardM);
//					this.pecaAtual = new Peca(0,0,TipoPeca.BLOCO_QUADRADO,99);
//					this.regra.pausarJogo();
				}
			}
		}
	}
	
	public void pecaFP() {
		if(!boardTravado) {
			while(true) {
				if(isFreeToMoveYP(this.pecaAtual) && isFreeAYP(this.pecaAtual)) {
					this.pecaAtual.y += 1;
					for (Unidade unidade : this.pecaAtual.unidades) {
						unidade.y += 1;
					}	
				}
				else {
					this.game.renderizar = false;
					this.pecaAtualPrevisao = null;
					this.regra.pausarJogo();
					this.pecaCaindo = false;
					//delecao de linha
					updateBoard(cBoardM);
					this.boardThread = new Thread(this);
					this.boardThread.start();
//					verificarLinhaCompletada();
					
//					updateBoard(cBoardM);
//					if(!boardTravado) {
//						this.regra.tick();						
//					}
//					this.regra.pausarJogo();
					
					break;
				}
			}
		}
//		this.pecaFPPrevisao();
	}
	
	//Y++
	public void pecaYP() {
		if(this.pecaCaindo) {
			if(isFreeToMoveYP(this.pecaAtual) && isFreeAYP(this.pecaAtual)) {
				this.pecaAtual.y += 1;
				for (Unidade unidade : this.pecaAtual.unidades) {
					unidade.y += 1;
				}			
			}
			else {
				this.game.renderizar = false;
				this.pecaAtualPrevisao = null;
				this.pecaCaindo = false;
				this.regra.pausarJogo();
				//delecao de linha
				updateBoard(cBoardM);
				this.boardThread = new Thread(this);
				this.boardThread.start();
//				this.run();
//				verificarLinhaCompletada();
//				updateBoard(cBoardM);
//				
//				if(!boardTravado) {
//					this.regra.tick();
//				}
//				this.regra.pausarJogo();
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
		this.regra.linhasDeletadas += linhas.size();
		this.regra.linhasDeletadasNivel += linhas.size();
		this.regra.linhasDeletadasScore = linhas.size();
//		System.out.println(linhas.size());
//		this.game.speedGame = 1;
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
					this.boardThread.sleep(1000/60);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		shiftDown(linha);
		} finally {
			lock.writeLock().unlock();
		}
	}	
	
	private void shiftDown(Integer linhaMaisBaixa) {
		lock.writeLock().lock();
		try {
			for (Peca peca : pecas) {
				for (Unidade unidade : peca.unidades) {
					if(unidade.y <= linhaMaisBaixa) {
						unidade.y += 1;
					}
				}
			}
			updateBoard(cBoardM);
		} finally {
			lock.writeLock().unlock();
		}
	}
	//verifica colisao do board no eixo Y + 1
	private boolean isFreeToMoveYP(Peca peca) {
		for (Unidade unidade : peca.unidades) {
			if(unidade.y + 1 >= 24) {
				return false;
			}
		}
		return true;
	}
	
	//verifica colisao com outras pecas no eixo Y + 1
	private boolean isFreeAYP(Peca peca) {
		for (Unidade unidade : peca.unidades) {
			if(this.cBoardM[unidade.y+1][(unidade.x)] != this.vazio) {
				return false;
			}
		}
		return true;
	}
	
	//REFATORAR PARA PUXAR DO CLONE
	public void updateBoard(char[][] board) {
        lock.readLock().lock();
        try {
        	this.cleanBoardM(board);
        	for (Peca peca : this.pecas) {
        		
        		char tipoCor = this.verificaTipoPeca(peca.tipoPeca);
        		
        		for (Unidade unidade : peca.unidades) {
        			
        			board[unidade.y][unidade.x] = tipoCor;
        		}			
        	}
        } finally {
            lock.readLock().unlock();
        }
	}
	
	public char verificaTipoPeca (TipoPeca tipoPeca ) {
		char tipoCor = 'v';
		
		switch (tipoPeca) {
		case BLOCO_L: 
			tipoCor = orange;
			break;
		case BLOCO_LI:
			tipoCor = blue;
			break;
		case BLOCO_N:
			tipoCor = red;
			break;
		case BLOCO_NI:
			tipoCor = green;
			break;
		case BLOCO_PALITO:
			tipoCor = cyan;
			break;
		case BLOCO_QUADRADO:
			tipoCor = yellow;
			break;
		case BLOCO_T:
			tipoCor = purple;
			break;
		}
		return tipoCor;
	}
	
	@Override
	public void render(Graphics2D graphics2D, int janelaWidth, int janelaHeight) {
		
		if(!gameOver) {
			updateBoard(this.boardM);
		}
		
		for (int y = 0; y < 5; y++) {			
			for (int x = 0; x < 6; x++) {
				graphics2D.setColor(new Color(50, 50, 50));
				graphics2D.fillRect((int)(((janelaWidth * 50)/100) - ((5*33)) + 355) + (x * 33),
									(int)(((janelaHeight * 50)/100) - ((10*33)) + 5) + ((y) * 33),
										33, 33);
				graphics2D.setColor(new Color(40, 40, 40));
				graphics2D.drawRect((int)(((janelaWidth * 50)/100) - ((5*33)) + 350) + (x * 33),
									(int)(((janelaHeight * 50)/100) - ((10*33)) ) + ((y) * 33),
										33, 33);
			}
		}
		
		for (int y = 4; y < boardM.length; y++) {			
			for (int x = 0; x < boardM[0].length; x++) {
				if(boardM[y][x] == vazio) {
					graphics2D.setColor(new Color(50, 50, 50));
					graphics2D.fillRect((int)(((janelaWidth * 50)/100) - ((5*33)) + 5) + (x * 33),
										(int)(((janelaHeight * 50)/100) - ((10*33)) + 5) + ((y-4) * 33),
											33, 33);
					//new Color(25, 25, 25)
					graphics2D.setColor(new Color(40, 40, 40));
					graphics2D.drawRect((int)(((janelaWidth * 50)/100) - ((5*33))) + (x * 33),
										(int)(((janelaHeight * 50)/100) - ((10*33))) + ((y-4) * 33),
											33, 33);
				}
			}
		}
		
		if(this.pecaAtualPrevisao != null) {
			for (Unidade unidade : this.pecaAtualPrevisao.unidades) {
				graphics2D.setColor(new Color(75, 75, 75));
				graphics2D.fillRect((int)(((janelaWidth * 50)/100) - ((5*33))) + (unidade.x * 33),
						(int)(((janelaHeight * 50)/100) - ((10*33))) + ((unidade.y-4) * 33),
						33, 33);
			}
		}
		
		for (int y = 4; y < boardM.length; y++) {
			for (int x = 0; x < boardM[0].length; x++) {
				if(boardM[y][x] != vazio) {
					graphics2D.drawImage(this.retornaCor(boardM[y][x]),
													  (int)(((janelaWidth * 50)/100) - ((5*33)-1)) + (x * 33),
													  (int)(((janelaHeight * 50)/100) - ((10*33)-1)) + ((y-4) * 33), null);
				}
			}
		}
		
		if(this.pecaAtualPrevisao != null) {
			for (Unidade unidade : this.pecaAtualPrevisao.unidades) {
//				graphics.setColor(Color.LIGHT_GRAY);
//				graphics.fillRect((unidade.x + boardPosX) * width, (unidade.y - boardPosY) * height, width, height);	
				graphics2D.setColor(new Color(125, 125, 125));
				graphics2D.drawRect((int)(((janelaWidth * 50)/100) - ((5*33))) + (unidade.x * 33),
						(int)(((janelaHeight * 50)/100) - ((10*33))) + ((unidade.y-4) * 33),
						33, 33);
				
//				graphics2D.setColor(Color.WHITE);
//				graphics2D.drawRect((unidade.x + boardPosX) * width, (unidade.y - boardPosY) * height, width, height);
			}
		}
		
		if(this.pecaProx != null) {
//			Color cor = this.retornaCor(verificaTipoPeca(this.pecaProx.tipoPeca));
			for (Unidade unidade : this.pecaProx.unidades) {
//				graphics.setColor(Color.BLACK);
//				graphics.fillRect((unidade.x + proxPecaPosX) * width + 6, (unidade.y - proxPecaPosY) * height + 6, width, height);	
				
				graphics2D.drawImage(this.retornaCor(verificaTipoPeca(this.pecaProx.tipoPeca)),
						  (int)(((janelaWidth * 50)/100) - ((5*33) - 285)) + (unidade.x * 33),
						  (int)(((janelaHeight * 50)/100) - ((10*33) - 67)) + ((unidade.y-4) * 33), null);
				
//				graphics.setColor(cor);
//				graphics.fillRect((unidade.x + proxPecaPosX) * width, (unidade.y - proxPecaPosY) * height, width, height);	
//				
//				graphics.setColor(Color.BLACK);
//				graphics.drawRect((unidade.x + proxPecaPosX) * width, (unidade.y - proxPecaPosY) * height, width, height);
			}
		}
		
		//debug
		
		
	}

	private BufferedImage retornaCor(char tipoPeca) {
		switch (tipoPeca) {
		case 'o':
			return this.imgBlockYellow;
		case 'i':
			return this.imgBlockCyan;
		case 's':
			return this.imgBlockRed;
		case 'z':
			return this.imgBlockGreen;
		case 'l':
			return this.imgBlockOrange;
		case 'j':
			return this.imgBlockBlue;
		case 't':
			return this.imgBlockPurple;
		case 'w':
			return this.imgBlockBlack;
		default :
			return null;
		}
	}

	@Override
	public void run() {
		Thread.currentThread().setName("TRD-BOARD");
		this.game.teclado.tecladoLivre = false;
		verificarLinhaCompletada();
		updateBoard(cBoardM);
		if(!boardTravado) {
			this.regra.tick();						
		}
		this.game.renderizar = true;
		this.game.teclado.tecladoLivre = true;
		this.regra.pausarJogo();
	}
}
