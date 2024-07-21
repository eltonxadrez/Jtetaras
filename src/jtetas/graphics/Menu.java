package jtetas.graphics;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.RenderingHints;

import jtetas.game.Game;

public class Menu implements Concreto {
	
	public Game game;
	
//	private boolean menu2 = false;
	
	//MENU PRINCIPAL 01
	private boolean menuPrincipal = true;
	private int menuPrincipalOp = 1;
	
	private String start = "START";
	private float posStartX = 47;
	private float posStartY = 40;
	
	private String option = "OPTIONS";
	private float posOptionX = 47;
	private float posOptionY = 45;
	
	private String exit = "EXIT";
	private float posExitX = 47;
	private float posExitY = 50;
	
	// MENU GAME OVER
	private boolean menuGameOver = false;
	private int menuGameOverOp = 1;
	
	private String gameOver = "GAME OVER";
	private float posGameOverX = 45;
	private float posGameOverY = 45;
	
	private String reiniciar = "REINICIAR JOGO";
	private float posReiniciarX = 43;
	private float posReiniciarY = 50;
	
	private String voltarMenuPrincipal = "MENU PRINCIPAL";
	private float posVMenuPrincipalX = 43;
	private float posVMenuPrincipalY = 55;
	
	public Menu(Game game) {
		this.game = game;
	}
	
	public void upMenu() {
		if(menuPrincipal) {
			if(this.menuPrincipalOp > 1 ) {
				this.menuPrincipalOp --;			
			}			
		}
		else if(menuGameOver) {
			if(this.menuGameOverOp > 1 ) {
				this.menuGameOverOp --;			
			}	
		}
	}
	
	public void downMenu() {
		if(menuPrincipal) {
			if(this.menuPrincipalOp < 3) {
				this.menuPrincipalOp ++;			
			}			
		}
		else if(menuGameOver) {
			if(this.menuGameOverOp < 2) {
				this.menuGameOverOp ++;			
			}	
		}
	}
	
	public void enterMenu() {
		if(menuPrincipal) {
			switch (this.menuPrincipalOp) {
			case 1: 
				this.game.iniciarJogo();
				break;
			case 2: 
				System.out.println("opcao 2");
				break;
			case 3: 
				System.exit(0);
				break;
			}
		}
		else if(menuGameOver) {
			switch (this.menuGameOverOp) {
			case 1:
				this.game.iniciarJogo();
				break;
			case 2:
				this.menuPrincipal = true;
				this.menuGameOver = false;
				this.game.iniciarMenu();
				break;
			}
		}
	}
	
	public void startGameOver() {
		this.menuPrincipal = false;
		this.menuGameOver = true;
	}

	@Override
	public void render(Graphics2D graphics2d, int janelaWidth, int janelaHeight) {
		
		if(this.menuPrincipal) {
			graphics2d.setColor(Color.BLACK);
			
			graphics2d.drawString(start, ((janelaWidth * posStartX)/100) + 5, ((janelaHeight * posStartY)/100) + 5);
			graphics2d.drawString(option, ((janelaWidth * posOptionX)/100) + 5, ((janelaHeight * posOptionY)/100) + 5);
			graphics2d.drawString(exit, ((janelaWidth * posExitX)/100) + 5, ((janelaHeight * posExitY)/100) + 5);
			
			graphics2d.setColor(Color.DARK_GRAY);
			
			//START
			if(menuPrincipalOp == 1) {
				graphics2d.setColor(Color.LIGHT_GRAY);
			}
			graphics2d.drawString(start, ((janelaWidth * posStartX)/100)   , ((janelaHeight * posStartY)/100));
			
			//OPTIONS
			graphics2d.setColor(Color.DARK_GRAY);
			if(menuPrincipalOp == 2) {
				graphics2d.setColor(Color.LIGHT_GRAY);
			}
			graphics2d.drawString(option, ((janelaWidth * posOptionX)/100) , ((janelaHeight * posOptionY)/100));
			
			//EXIT
			graphics2d.setColor(Color.DARK_GRAY);
			if(menuPrincipalOp == 3) {
				graphics2d.setColor(Color.LIGHT_GRAY);
			}
			graphics2d.drawString(exit, ((janelaWidth * posExitX)/100)   , ((janelaHeight * posExitY)/100));
		}
		else if(this.menuGameOver) {
//			graphics2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
			graphics2d.setColor(Color.BLACK);
			graphics2d.drawString(gameOver, ((janelaWidth * posGameOverX)/100) + 5, ((janelaHeight * posGameOverY)/100) + 5);
			graphics2d.drawString(reiniciar, ((janelaWidth * posReiniciarX)/100) + 5, ((janelaHeight * posReiniciarY)/100) + 5);
			graphics2d.drawString(voltarMenuPrincipal, ((janelaWidth * posVMenuPrincipalX)/100) + 5, ((janelaHeight * posVMenuPrincipalY)/100) + 5);
			
			graphics2d.setColor(Color.LIGHT_GRAY);
			graphics2d.drawString(gameOver, ((janelaWidth * posGameOverX)/100) , ((janelaHeight * posGameOverY)/100) );
			
			graphics2d.setColor(Color.DARK_GRAY);
			if(menuGameOverOp == 1) {
				graphics2d.setColor(Color.LIGHT_GRAY);
			}
			graphics2d.drawString(reiniciar, ((janelaWidth * posReiniciarX)/100), ((janelaHeight * posReiniciarY)/100));
			
			graphics2d.setColor(Color.DARK_GRAY);
			if(menuGameOverOp == 2) {
				graphics2d.setColor(Color.LIGHT_GRAY);
			}
			graphics2d.drawString(voltarMenuPrincipal, ((janelaWidth * posVMenuPrincipalX)/100), ((janelaHeight * posVMenuPrincipalY)/100));
			
		}
	}
}
