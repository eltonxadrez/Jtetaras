package jtetas.game.input;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import jtetas.game.Regra;

public class Teclado implements KeyListener {
	
	private Regra regra;
	
	public Teclado(Regra regra) {
		this.regra = regra;
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_RIGHT) {
			if (!this.regra.pausado){
				this.regra.board.pecaXP();				
			}
		}
		else if(e.getKeyCode() == KeyEvent.VK_LEFT) {
			if (!this.regra.pausado){
				this.regra.board.pecaXN();				
			}
		}
		if(e.getKeyCode() == KeyEvent.VK_DOWN) {
			if (!this.regra.pausado){
				this.regra.board.pecaYP();				
			}
		}
		if(e.getKeyCode() == KeyEvent.VK_SPACE) {
			if (!this.regra.pausado){
				this.regra.board.pecaFP();				
			}
		}
		if(e.getKeyCode() == KeyEvent.VK_Z) {
			if (!this.regra.pausado){
				this.regra.board.pecaTL();				
			}
		}
		if(e.getKeyCode() == KeyEvent.VK_X) {
			if (!this.regra.pausado){
				this.regra.board.pecaTR();				
			}
		}		
		if(e.getKeyCode() == KeyEvent.VK_P) {
			this.regra.pausarJogo();
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
	}

}
