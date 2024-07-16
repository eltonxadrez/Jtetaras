package jtetas.graphics;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

public class Bloco extends Rectangle implements Concreto{

	private static final long serialVersionUID = 1L;
	
	public Bloco(int x, int y) {
		super(x, y, 20, 20);
	}

	@Override
	public void render(Graphics g) {
		g.setColor(Color.WHITE);
		g.fillRect(x, y, width, height);
	}

}
