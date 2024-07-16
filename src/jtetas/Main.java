package jtetas;

import jtetas.game.Game;
import jtetas.game.board.Peca;
import jtetas.game.board.TipoPeca;
import jtetas.game.board.Unidade;

public class Main {

	public static void main(String[] args) {
		Game game = new Game();
		new Thread(game).start();
		Peca pecaL = new Peca(4, 4, TipoPeca.BLOCO_L, 01);
		Peca clonePecaL = pecaL.clonarPeca();
		System.out.println("-----");
		for (Unidade unidade : pecaL.unidades) {
			System.out.println("("+unidade.x +","+ unidade.y+")");
		}
		// Coordenadas dos quatro pontos da peça em int
//        int[][] points = {
//            {4, 4}, // ponto 1
//            {4, 5}, // ponto 2
//            {4, 6}, // ponto 3
//            {5, 4}  // ponto 4
//        };

        // Ângulo de rotação em graus
        int angleDegrees = 90;

        // Fator de escala para manter precisão em aritmética fixa
        int scaleFactor = 10000;

        // Converte o ângulo para radianos em aritmética fixa
        double angleRadians = Math.toRadians(angleDegrees);
        int cosThetaFixed = (int) (Math.cos(angleRadians) * scaleFactor);
        int sinThetaFixed = (int) (Math.sin(angleRadians) * scaleFactor);

        // Ponto em torno do qual rotacionar (vamos usar o primeiro ponto)
        
		Unidade unidadeAlterRot = null;
		for (Unidade unidade : pecaL.unidades) {
			if(unidade.isRotateCenter) {
				unidadeAlterRot = unidade;
			}
		}
        int pivotX = unidadeAlterRot.x;
        int pivotY = unidadeAlterRot.y;
//        int pivotX = points[1][0];
//        int pivotY = points[1][1];

        // Rotacionar cada ponto em torno do ponto de pivot
        for (Unidade unidade : pecaL.unidades) {
            // Subtrai as coordenadas do ponto de pivot
            int x = unidade.x - pivotX;
            int y = unidade.y - pivotY;

            // Aplica a rotação usando aritmética fixa
            int xNew = (x * cosThetaFixed - y * sinThetaFixed) / scaleFactor;
            int yNew = (x * sinThetaFixed + y * cosThetaFixed) / scaleFactor;

            // Adiciona as coordenadas do ponto de pivot de volta
            unidade.x = xNew + pivotX;
            unidade.y = yNew + pivotY;
        }

        // Imprime as novas coordenadas dos pontos
//        for (int i = 0; i < points.length; i++) {
//            System.out.printf("Ponto %d: (%d, %d)%n", i + 1, points[i][0], points[i][1]);
//        }
		
//		for (Unidade unidade : pecaL.unidades) {
//			
//			System.out.println("(" + unidade.y + ","+ unidade.x + ")");
//			
//			double theta = Math.toRadians(45);
//			
//			int tempx = unidade.x;
//			
//			unidade.y = (int)(tempx*Math.sin(theta) + unidade.y * Math.cos(theta));
//		    unidade.x = (int)(tempx*Math.cos(theta) - unidade.y * Math.sin(theta));
//		}
		System.out.println("-----");
		for (Unidade unidade : pecaL.unidades) {
			System.out.println("("+unidade.x +","+ unidade.y+")");
		}
//		Unidade unidadeAlterRot = null;
//		for (Unidade unidade : pecaL.unidades) {
//			if(unidade.isRotateCenter) {
//				unidadeAlterRot = unidade;
//			}
//		}
//		Unidade unidadeCloneAlterRot = null;
//		for (Unidade unidade : clonePecaL.unidades) {
//			if(unidade.isRotateCenter) {
//				unidadeCloneAlterRot = unidade;
//			}
//		}
//		System.out.println("-----");
//		System.out.println("("+unidadeAlterRot.x +","+ unidadeAlterRot.y+")");
//		System.out.println("("+unidadeCloneAlterRot.x +","+ unidadeCloneAlterRot.y+")");
//		
//		int correcaoY = unidadeCloneAlterRot.y - unidadeAlterRot.y;
//		int correcaoX = unidadeCloneAlterRot.x - unidadeAlterRot.x;
//		
//		System.out.println(correcaoY);
//		System.out.println(correcaoX);
//		
//		for (Unidade unidade : pecaL.unidades) {
//			unidade.y = (unidade.y + correcaoY);
//			unidade.x = (unidade.x + correcaoX);
//		}
//		System.out.println("-----");
//		for (Unidade unidade : pecaL.unidades) {
//			System.out.println("("+unidade.x +","+ unidade.y+")");
//		}
//		System.out.println("("+unidade.x +","+ unidade.y+")");
		
	}
}
