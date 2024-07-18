package jtetas.game;

import java.util.ArrayList;

import jtetas.game.board.Board;
import jtetas.game.input.Teclado;
import jtetas.graphics.Janela;
import jtetas.graphics.Renderizador;

public class Game implements Runnable{
	
	public ArrayList<Entity> entidades;
	
	public Janela janela;
	public Renderizador renderizador;
	public Teclado teclado;
	public Board board;
	public Regra regra;
	
	public int width = 640, height = 800;
	public int y = 24;
	public int x = 10;
	
	public Game() {
		init();
	}
	
	private void init() {
		this.entidades = new ArrayList<Entity>();
		
		this.board = new Board(this.y, this.x, this);
		
		this.regra = new Regra(board);
		this.board.regra = this.regra;
		this.entidades.add(regra);
		new Thread(regra).start();
		
		this.renderizador = new Renderizador(this.width, this.height);
		this.renderizador.elementosRenderizadosList.add(board);
		
		this.janela = new Janela(this.renderizador);
		
		this.teclado = new Teclado(regra);
		this.regra.teclado = teclado;
		
		this.renderizador.addKeyListener(teclado);
		this.renderizador.requestFocusInWindow();
		
	}
	
	@Override
	public void run() {
		Thread.currentThread().setName("TRD-RENDERIZADOR");
		while(true) {
			if(this.regra.pausado) {
				this.regra.pausarTeste();
			}
			this.renderizador.render();
			try {
				Thread.sleep(1000/75);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
