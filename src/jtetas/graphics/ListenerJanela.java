package jtetas.graphics;

import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

public class ListenerJanela extends ComponentAdapter {
	
	private Renderizador renderizador;
	private Janela janela;
	
	public ListenerJanela(Renderizador canvas, Janela janela) {
		this.renderizador = canvas;
		this.janela = janela;
	}
	
	
	@Override
	public void componentResized(ComponentEvent evt) {
		this.renderizador.width = (int) this.janela.getBounds().getHeight();
		this.renderizador.height = (int) this.janela.getBounds().getWidth();
		this.renderizador.requestFocusInWindow();
//		System.out.println("ListenerJanela -> " + this.janela.getBounds());
    }
}
