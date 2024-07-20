package jtetas;

import jtetas.game.Game;

public class Main {

	public static void main(String[] args) {
		Game game = new Game();
		game.gameThread = new Thread(game);
		game.gameThread.start();
	}
}
