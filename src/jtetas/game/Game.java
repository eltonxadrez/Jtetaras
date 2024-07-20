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
import jtetas.graphics.Hud;
import jtetas.graphics.Janela;
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
	
	public Game() {
		init();
	}
	
	private void init() {
		this.entidades = new ArrayList<Entity>();
		this.importFont();
		this.setWindowSize();
		this.initComponents();
		
		this.teclado.janela = this.janela;
		
		this.renderizador.jFrame = this.janela;
		this.renderizador.elementosRenderizadosList.add(board);
		this.renderizador.elementosRenderizadosList.add(hud);
		
		this.regra.board = this.board;
		this.regra.teclado = teclado;
		this.regra.hud = this.hud;
		
		this.board.regra = this.regra;
		this.entidades.add(regra);
		
		
		this.regraThread = new Thread(regra);
		this.regraThread.start();
		this.renderizador.addKeyListener(teclado);
		this.renderizador.requestFocusInWindow();
	}
	
	private void initComponents() {
		this.hud = new Hud();
		this.board = new Board(this.y, this.x, this);
		this.regra = new Regra(this.board);
		this.teclado = new Teclado(regra);
		this.renderizador = new Renderizador(this.width, this.height);
		this.janela = new Janela(this.renderizador, this.width, this.height);
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
		     ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, new File("src/jtetas/resources/fonts/Symtext-9YmnL.ttf")));
		} catch (IOException|FontFormatException e) {
		    e.printStackTrace();
		}
	}
	
	@Override
	public void run() {
		Thread.currentThread().setName("TRD-GAME");
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
