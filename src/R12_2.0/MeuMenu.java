/*
* @(#)MeuMenu.java  1.0  30/03/2001
*	
* Aline Vieira de Mello
* Pontifícia Universidade Católica do Rio Grande do Sul - Faculdade de Informática
*/

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;  

/**
* <i>MeuMenu</i> contem a implementação do menu.
*/
public class MeuMenu extends JMenuBar
{	
	private Janela janela;
	private JMenu Arquivo,Parametro,Ajuda;
	private JMenuItem novo,carregar,editar,sair,tammemoria,nvariaveis,sobre,ajuda;
			
	public MeuMenu(Janela janela)
	{
		super();
		this.janela=janela;
		
		criaItens();
		adicionaItens();
	}
	
	/**
	* Cria os itens do menu.
	*/
	private void criaItens()
	{	
		Arquivo=new JMenu("File");
		Parametro=new JMenu("Parameter");
		Ajuda=new JMenu("Help");	
		
		novo=new JMenuItem("New");
		novo.addActionListener(janela);
			
		carregar=new JMenuItem("Load...");
		carregar.addActionListener(janela);
		
		editar=new JMenuItem("Edit...");
		editar.addActionListener(janela);
		
		tammemoria=new JMenuItem("Size Memory  "+janela.getTamanhoMemoria());
		tammemoria.addActionListener(janela);
		
		nvariaveis=new JMenuItem("Number of Symbols "+janela.getNumeroVariaveis());
		nvariaveis.addActionListener(janela);
		
		sair=new JMenuItem("Exit");
		sair.addActionListener(janela);
		
		sobre=new JMenuItem("About");
		sobre.addActionListener(janela);
		
		ajuda=new JMenuItem("Help");			
		ajuda.addActionListener(janela);
	}
	
	/**
	* Adiciona os itens ao menu.
	*/
	private void adicionaItens()
	{
		janela.setJMenuBar(this);
		
		add(Arquivo);
		//add(Parametro);
		add(Ajuda);	
        	
		Arquivo.add(novo);
		Arquivo.add(carregar);
		Arquivo.add(editar);
		Arquivo.addSeparator();
		Arquivo.add(sair);
		
		Parametro.add(tammemoria);
		Parametro.add(nvariaveis);
		
		Ajuda.add(sobre);
		//Ajuda.add(ajuda);	
	}
}