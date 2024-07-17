package jtetas;

import java.util.ArrayList;
import java.util.Collections;

import jtetas.game.Game;
import jtetas.game.board.Peca;
import jtetas.game.board.TipoPeca;
import jtetas.game.board.Unidade;

public class Main {

	public static void main(String[] args) {
		Game game = new Game();
		new Thread(game).start();
//		for (TipoPeca tipoPeca : TipoPeca.values()) {
//			System.out.println(tipoPeca);
//		
//		ArrayList<Integer> listanumeros = new ArrayList<Integer>();
//		listanumeros.add(5);
//		listanumeros.add(3);
//		listanumeros.add(10);
//		listanumeros.add(2);
//		listanumeros.add(2);
//		listanumeros.sort(null);
//		Collections.sort(listanumeros);
//		System.out.println(listanumeros.toString());
		
	}
}
