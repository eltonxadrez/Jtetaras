package jtetas.game;

import java.util.ArrayList;

import jtetas.game.board.Board;
import jtetas.game.board.Peca;
import jtetas.game.board.TipoPeca;
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
		
		this.board = new Board(this.y, this.x);
		
		this.regra = new Regra(board);
		this.entidades.add(regra);
		new Thread(regra).start();
		
		this.renderizador = new Renderizador(this.width, this.height);
		this.renderizador.elementosRenderizadosList.add(board);
		
		this.janela = new Janela(this.renderizador);
		
		this.teclado = new Teclado(regra);
		this.renderizador.addKeyListener(teclado);
//		this.janela.addKeyListener(teclado);
		
	}
	
//	public void tick() {
//		for (Entity entity : this.entidades) {
//			entity.tick();
//		}
//	}

	@Override
	public void run() {
		
		while(true) {
			
			this.renderizador.render();
			
			try {
				Thread.sleep(1000/60);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
