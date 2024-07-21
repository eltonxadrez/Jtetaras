package jtetas.game;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GraphicsEnvironment;
import java.awt.Toolkit;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import jtetas.game.board.Board;
import jtetas.game.input.Teclado;
import jtetas.graphics.Concreto;
import jtetas.graphics.Hud;
import jtetas.graphics.Janela;
import jtetas.graphics.Menu;
import jtetas.graphics.Renderizador;
import jtetas.graphics.TesteImage;

public class Game implements Runnable{
	
	public ArrayList<Entity> entidades;
	
	public Janela janela;
	public Renderizador renderizador;
	public Teclado teclado;
	public Board board;
	public Regra regra;
	public Hud hud;
	public Menu menu;
//	public TesteImage testeImage;
	public Thread gameThread;
//	public Thread boardThread;
	public Thread regraThread;
	
	//tamanho janela
	public int width, height;
	
	public int y = 24;
	public int x = 10;
	
	public int speedGame = 75;
	public boolean renderizar = true;
	public boolean isMenu = true;
	
	public Game() {
		init();
	}
	
	private void init() {
		this.importFont();
		
		this.setWindowSize();
		
		this.initMainComponents();
		
		this.initMainConections();
		
		this.iniciarMenu();
	}
	
	private void initMainComponents() {
		this.entidades = new ArrayList<Entity>();
		this.menu = new Menu(this);
		this.teclado = new Teclado(this);
		this.renderizador = new Renderizador(this.width, this.height);
		this.janela = new Janela(this.renderizador, this.width, this.height);
	}
	
	public void initMainConections() {
		this.teclado.janela = this.janela;
		this.teclado.menu = this.menu;
		this.renderizador.jFrame = this.janela;
		this.renderizador.addKeyListener(teclado);
		this.renderizador.requestFocusInWindow();
	}
	
	public void iniciarMenu() {
		System.out.println("INICIAR MENU");
		this.renderizador.renderizar = false;
		this.renderizar = false;
		this.isMenu = true;
		
		this.renderizador.elementosRenderizadosList.clear();
		this.renderizador.elementosRenderizadosList.add(menu);
		
		this.renderizador.renderizar = true;
		this.renderizar = true;
	}
	
	
	private void initGameComponents() {
		this.hud = new Hud();
		this.board = new Board(this.y, this.x, this);
		this.regra = new Regra(this, this.board);
	}
	
	private void initGameConections() {
		this.regra.board = this.board;
		this.regra.teclado = teclado;
		this.regra.hud = this.hud;
		this.board.regra = this.regra;
		this.teclado.regra = this.regra;
		this.entidades.add(regra);
	}
	
	public void iniciarJogo() {
		this.initGameComponents();
		this.initGameConections();
		
		this.isMenu = false;
		this.renderizador.renderizar = false;
		this.renderizar = false;
		
		this.renderizador.elementosRenderizadosList.clear();
		this.renderizador.elementosRenderizadosList.add(board);
		this.renderizador.elementosRenderizadosList.add(hud);
		
		this.renderizador.renderizar = true;
		this.renderizar = true;
		this.board.reiniciarBoard(this.y, this.x);
		this.regra.reiniciarRegra();
		this.hud.reiniciarHud();
		
		this.iniciarRegraThread();
	}

	public void iniciarGameOver() {
		this.menu.startGameOver();
		this.renderizador.renderizar = false;
		this.renderizar = false;
		this.isMenu = true;
		
		this.renderizador.elementosRenderizadosList.clear();
		this.renderizador.elementosRenderizadosList.add(menu);
		
		this.renderizador.renderizar = true;
		this.renderizar = true;
	}
	
	private void iniciarRegraThread() {
		this.regraThread = new Thread(regra);
		this.regraThread.start();
	}
	

	
	private void setWindowSize() {
		Toolkit toolkit =  Toolkit.getDefaultToolkit ();
		Dimension dim = toolkit.getScreenSize();
		//1920 - 1080
		this.width = (int) dim.getWidth()/2;
		this.height = (int) dim.getHeight()/2;
		
		this.width = 1024;
		this.height = 768;
	}
	
	private void importFont() {
		try {
		     GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment(); 
		     ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, getClass().getResourceAsStream("/fonts/Symtext-9YmnL.ttf")));
		} catch (IOException|FontFormatException e) {
		    e.printStackTrace();
		}
	}
	
	@Override
	public void run() {
		Thread.currentThread().setName("TRD-GAME");
		
//		this.renderizador.init();
//		try {
//			Thread.sleep(1000/1);
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		}
		
		while(true) {
			try {
				if(renderizar) {
					this.renderizador.render();				
				}
				Thread.sleep(1000/75);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

}
