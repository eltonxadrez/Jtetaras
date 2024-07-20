package jtetas.graphics;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.RenderingHints;

public class Hud implements Concreto {
	
	public int score = 0;
	public int nivel = 1;
	public GraphicsEnvironment ge;
	
	private float posScoreSombraX = 50;
	private float posScoreSombraY = 45;
	private float posNivelSombraX = 50;
	private float posNivelSombraY = 50;
	
	private float posScoreX = 50;
	private float posScoreY = 45;
	private float posNivelX = 50;
	private float posNivelY = 50;
	
	
	public Hud() {
		this.ge = GraphicsEnvironment.getLocalGraphicsEnvironment();  
	}
	
	@Override
	public void render(Graphics2D graphics2d, int janelaWidth, int janelaHeight) {
		Font valueFont = new Font("Symtext", Font.BOLD, 32);
		graphics2d.setFont(valueFont);
		
		graphics2d.setColor(Color.BLACK);
		graphics2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		graphics2d.drawString("SCORE: " + this.score, ((janelaWidth * posScoreSombraX)/100) + (6*32) + 5, ((janelaHeight * posScoreSombraY)/100) + 5);
		graphics2d.drawString("NIVEL: " + this.nivel, ((janelaWidth * posNivelSombraX)/100) + (6*32) + 5, ((janelaHeight * posNivelSombraY)/100) + 5);
		
		graphics2d.setColor(Color.LIGHT_GRAY);
		graphics2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		graphics2d.drawString("SCORE: " + this.score, ((janelaWidth * posScoreX)/100) + (6*32), ((janelaHeight * posScoreY)/100));
		graphics2d.drawString("NIVEL: " + this.nivel, ((janelaWidth * posNivelX)/100) + (6*32), ((janelaHeight * posNivelY)/100));
	}
}
