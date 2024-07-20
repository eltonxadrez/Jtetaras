package jtetas.graphics;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;

public class Bloco extends Rectangle implements Concreto{

	private static final long serialVersionUID = 1L;
	
	public Bloco(int x, int y) {
		super(x, y, 20, 20);
	}

	@Override
	public void render(Graphics2D graphics2d, int janelaWidth, int janelaHeight) {
		graphics2d.setColor(Color.WHITE);
		graphics2d.fillRect(x, y, width, height);
	}

}
