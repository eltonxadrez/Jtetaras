package jtetas.graphics;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.util.ArrayList;

public class Renderizador extends Canvas{
	
	public Graphics g;
	public BufferStrategy bs;
	public int width, height;
	
	public ArrayList<Concreto> elementosRenderizadosList;

	private static final long serialVersionUID = 1L;
	
	public Renderizador(int width, int height) {
		this.elementosRenderizadosList = new ArrayList<Concreto>();
		this.setPreferredSize(new Dimension(width, height));
		this.width = width;
		this.height = height;
	}
	
	public void render() {
		this.bs = this.getBufferStrategy();
		if(bs == null) {
			this.createBufferStrategy(3);
			return;
		}
		this.g = bs.getDrawGraphics();
		this.background();
		
		for (Concreto concreto : this.elementosRenderizadosList) {
			concreto.render(this.g);
		}
		
		this.bs.show();
	}
	
	private void background() {
		this.g.setColor(Color.black);
		this.g.fillRect(0, 0, this.width, this.height);
	}

}
