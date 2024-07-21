package jtetas.game.input;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import jtetas.game.Game;
import jtetas.game.Regra;
import jtetas.graphics.Janela;
import jtetas.graphics.Menu;

public class Teclado implements KeyListener {
	
	public Regra regra;
	public Game game;
	public Menu menu;
	public Janela janela;
	public boolean tecladoLivre;
	
	public Teclado(Game game) {
		this.game = game;
		this.tecladoLivre = true;
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
	}

	@Override
	public void keyPressed(KeyEvent e) {
//		System.out.println("TECLADO LIVRE ? " + this.tecladoLivre);
		if(this.game.isMenu) {
			if(e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_W) {
				this.menu.upMenu();
			}
			
			if(e.getKeyCode() == KeyEvent.VK_DOWN || e.getKeyCode() == KeyEvent.VK_S) {
				this.menu.downMenu();
			}
			
			if(e.getKeyCode() == KeyEvent.VK_LEFT || e.getKeyCode() == KeyEvent.VK_A) {
				System.out.println("LEFT");
			}
			
			if(e.getKeyCode() == KeyEvent.VK_RIGHT || e.getKeyCode() == KeyEvent.VK_D) {
				System.out.println("RIGHT");
			}
			
			if(e.getKeyCode() == KeyEvent.VK_ENTER || e.getKeyCode() == KeyEvent.VK_SPACE) {
				this.menu.enterMenu();
			}
			
			if(e.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
				System.out.println("VOLTAR");
			}
			
		}
		else {
			if(tecladoLivre) {
				if(e.getKeyCode() == KeyEvent.VK_RIGHT || e.getKeyCode() == KeyEvent.VK_D) {
					if (!this.regra.pausado){
						this.regra.board.pecaXP();
					}
				}
				else if(e.getKeyCode() == KeyEvent.VK_LEFT || e.getKeyCode() == KeyEvent.VK_A) {
					if (!this.regra.pausado){
						this.regra.board.pecaXN();				
					}
				}
				if(e.getKeyCode() == KeyEvent.VK_DOWN || e.getKeyCode() == KeyEvent.VK_S) {
					if (!this.regra.pausado){
						this.regra.board.pecaYP();	
					}
				}
				if(e.getKeyCode() == KeyEvent.VK_SPACE) {
					if (!this.regra.pausado){
						this.regra.board.pecaFP();
					}
				}
				if(e.getKeyCode() == KeyEvent.VK_Z || e.getKeyCode() == KeyEvent.VK_K) {
					if (!this.regra.pausado && !this.regra.rotacaoTravada){
						this.regra.board.pecaTL();
						this.regra.rotacaoTravada = true;
					}
				}
				if(e.getKeyCode() == KeyEvent.VK_X || e.getKeyCode() == KeyEvent.VK_L) {
					if (!this.regra.pausado && !this.regra.rotacaoTravada){
						this.regra.board.pecaTR();	
						this.regra.rotacaoTravada = true;
					}
				}		
				if(e.getKeyCode() == KeyEvent.VK_P) {
					this.regra.pausarJogo();
				}
			}
		}
		if(e.getKeyCode() == KeyEvent.VK_ESCAPE) {
			System.exit(0);
		}
		if(e.getKeyCode() == KeyEvent.VK_F11) {
			this.janela.turnFullscreen();
		}

	}

	@Override
	public void keyReleased(KeyEvent e) {
		if(tecladoLivre) {
			if(e.getKeyCode() == KeyEvent.VK_Z || e.getKeyCode() == KeyEvent.VK_K) {
				this.regra.rotacaoTravada = false;
			}
			if(e.getKeyCode() == KeyEvent.VK_X || e.getKeyCode() == KeyEvent.VK_L) {
				this.regra.rotacaoTravada = false;
			}	
		}
	}

}
