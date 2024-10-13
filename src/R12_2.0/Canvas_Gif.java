/*
* @(#)Canvas_Gif.java  1.0  30/03/2001
*	
* Aline Vieira de Mello
* Pontifícia Universidade Católica do Rio Grande do Sul - Faculdade de Informática
*/

import java.awt.*;

/**
* <i>Canvas_Gif</i> implementa o canvas que contém a figura Gaph.gif.
*/
public class Canvas_Gif extends Canvas
{
	private Image imagem;
	private int img_indice;
	
	public Canvas_Gif()
	{
		super();
	}

	/**
	* Repinta a área do canvas.
	*/
	public void paint(Graphics g)
	{
		imagem=getToolkit().getDefaultToolkit().getImage("gaph.gif");
    g.drawImage(imagem,0,0,this);
	}
	
	/**
	* Reescrita o método update para tornar a pintura da tela mais veloz.
	*/
	public void update(Graphics g)
	{
		paint(g);
	}
}