package jtetas;

import jtetas.game.Game;
import jtetas.game.board.Peca;
import jtetas.game.board.TipoPeca;
import jtetas.game.board.Unidade;

public class Main {

	public static void main(String[] args) {
		Game game = new Game();
		new Thread(game).start();
	}
}
