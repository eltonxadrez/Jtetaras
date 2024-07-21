package jtetas.graphics;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.image.BufferStrategy;
import java.util.ArrayList;

import javax.swing.JFrame;

public class Renderizador extends Canvas{
	
	public Graphics graphics;
	public Graphics2D graphics2d;
	
	private Font valueFont;
	public BufferStrategy bs;
	public int width, height;
	public JFrame jFrame;
	
	public ArrayList<Concreto> elementosRenderizadosList;

	private static final long serialVersionUID = 1L;
	
	public boolean renderizar = true;
	
	public Renderizador(int width, int height) {
		System.out.println("criacao renderizador");
		this.setBackground(Color.BLACK);
		this.elementosRenderizadosList = new ArrayList<Concreto>();
		this.setPreferredSize(new Dimension(width, height));
		this.width = width;
		this.height = height;
		this.valueFont = new Font("Symtext", Font.BOLD, 32);
		
	}
	
	public void init() {
		System.out.println("init");
		try {
			Thread.sleep(1000/1);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println("create");
		this.createBufferStrategy(3);
		try {
			Thread.sleep(1000/1);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		this.bs = this.getBufferStrategy();
		try {
			Thread.sleep(1000/1);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		if(bs == null) {
			this.createBufferStrategy(3);
		}
		try {
			Thread.sleep(1000/1);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		this.graphics = bs.getDrawGraphics();
		this.graphics2d = (Graphics2D) this.graphics;
		System.out.println("font");
		
	}
	
	public void render() {
		if(renderizar) {
			this.bs = this.getBufferStrategy();
			
			if(this.bs == null) {
				this.createBufferStrategy(3);
				return;
			}
			
			this.graphics = bs.getDrawGraphics();
			this.graphics2d = (Graphics2D) this.graphics;
			graphics2d.setFont(valueFont);
			this.graphics2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
//			graphics2d.setFont(valueFont);
			
//			System.out.println("06 bs" + this.getBufferStrategy());
//			this.init();
//			System.out.println("qweqwe333");
			this.background();
			
			for (Concreto concreto : this.elementosRenderizadosList) {
				concreto.render(this.graphics2d, this.jFrame.getBounds().width, this.jFrame.getBounds().height);
			}
			
			this.bs.show();
		}
	}
	
	private void background() {
		this.graphics2d.setColor(Color.BLACK);
		this.graphics2d.fillRect(0, 0, this.height, this.width);
		
//		graphics2D.setColor(Color.darkGray);
//		graphics2D.fillRect((int)(((janelaWidth * 50)/100) - ((5*33)) + 5) + (x * 33),
//							(int)(((janelaHeight * 50)/100) - ((10*33)) + 5) + ((y-4) * 33),     33, 33);
		
		this.graphics2d.setColor(new Color(25, 25, 25));
		this.graphics2d.fillRect(this.height / 7, 0,
							     this.height - (this.height / 4) , this.width);
		//linhas vermelhas marcando o centro
//		this.graphics2d.setColor(Color.RED);
//		this.graphics2d.fillRect(0, this.width/2, this.height, 1);
//		this.graphics2d.setColor(Color.RED);
//		this.graphics2d.fillRect(this.height/2, 0, 1, this.width);
	}
}
