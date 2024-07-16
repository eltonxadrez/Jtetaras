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
			this.regra.board.pecaXP();
		}
		else if(e.getKeyCode() == KeyEvent.VK_LEFT) {
			this.regra.board.pecaXN();
		}
		if(e.getKeyCode() == KeyEvent.VK_DOWN) {
			this.regra.board.pecaYP();
		}
		if(e.getKeyCode() == KeyEvent.VK_SPACE) {
			this.regra.board.pecaFP();
		}
		if(e.getKeyCode() == KeyEvent.VK_Z) {
			this.regra.board.pecaTL();
		}
		if(e.getKeyCode() == KeyEvent.VK_X) {
			this.regra.board.pecaTR();
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
	}

}
