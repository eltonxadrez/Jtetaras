package jtetas.graphics;

import java.awt.Canvas;

import javax.swing.JFrame;

public class Janela extends JFrame {
	
	private static final long serialVersionUID = 1L;

	public Janela(Canvas renderizador) {
		this.add(renderizador);
		this.setTitle("JTETAS");
		this.pack();
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);
	}
}
