/*
* @(#)Configurador.java  1.0  30/03/2001
*	
* Aline Vieira de Mello
* Pontifícia Universidade Católica do Rio Grande do Sul - Faculdade de Informática
*/

import javax.swing.*;
import javax.swing.filechooser.*;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.io.*;
import java.lang.*;

/**
* <i>Configurador</i> implementa a janela que inclui o arquivo da arquitetura
* descrita através do Configurador de Arquiteturas para a simulação.
*/
public class Configurador extends JFrame implements ActionListener
{
	private JLabel informacao,informacao2;
	private JTextField arq;
	private JButton ok,configurador;	
	private String arquitetura,caminho;
	private JFileChooser fc;
	private File ultimodiretorio;
	private FileChooserDemo fcd;
	
	public Configurador()
	{
		super();
		setTitle("Simulator of Archictetures");
 		getContentPane().setLayout(null);
	  setSize(345,160);
	  setLocation(221,220);
    addWindowListener(new WindowAdapter(){public void windowClosing(WindowEvent e){System.exit(0);}});
  	ultimodiretorio=new File(System.getProperty("user.dir"));
  	fcd=new FileChooserDemo();
	  implementa();	
	}
	
	/**
	* Implementa o layout da janela.
	*/
	public void implementa()
	{
		implementaMenu();
		
		//Figura GAPH
	  Canvas_Gif canvas=new Canvas_Gif();
	  canvas.setBounds(190,5,160,60);
		getContentPane().add(canvas);
	
		informacao=new JLabel("Deverá ser informado qual a");
		informacao.setBounds(15,10,350,20);
		getContentPane().add(informacao);
		
		informacao2=new JLabel("arquitetura será simulada.");
		informacao2.setBounds(15,30,350,20);
		getContentPane().add(informacao2);
		
		arq=new JTextField();
		arq.setBounds(35,65,200,30);
		getContentPane().add(arq);
		
		ok=new JButton("Ok");
		ok.setBounds(245,65,50,30);
		ok.addActionListener(this);
		getContentPane().add(ok);
		setVisible(true);
	}	

	/**
	* Implementa Menu para a janela.
	*/
	public void implementaMenu()
	{
		JMenuBar menu=new JMenuBar();
		JMenu abrir=new JMenu("Open");
		
		//Itens do Menu Abrir
		JMenuItem abrirArq=new JMenuItem("Open Architecture");
		abrirArq.addActionListener(this);
		abrir.add(abrirArq);
		abrir.addSeparator();
		JMenuItem sairArq=new JMenuItem("Exit");
		sairArq.addActionListener(this);
		abrir.add(sairArq);
				
		menu.add(abrir);
		setJMenuBar(menu);
	}

	/**
	* Implementação da ActionListener com a execução dos botões.
	*/
	public void actionPerformed(ActionEvent e)
	{
		if(e.getActionCommand().equalsIgnoreCase("OK"))
		{
			arquitetura=arq.getText();	
			if(arquitetura.length()>4)
			{
				if(!arquitetura.substring(arquitetura.length()-4,arquitetura.length()).equalsIgnoreCase(".arq"))
					arquitetura=arquitetura.concat(".arq");	
			}
			else
			{
				arquitetura=arquitetura.concat(".arq");	
			}
	
			Simulador sim=new Simulador(arquitetura);
		  if(sim.implementa())
				dispose();
		}
		else if(e.getActionCommand().equalsIgnoreCase("Open Architecture"))
		{
			fc = new JFileChooser(ultimodiretorio);
    
    	//Filtra somente os diretorios e arquivos .arq.
    	fc.setFileFilter(new FiltraArquivos("arq"));
    
    	//FileChosser para abrir arquivo.
    	int returnVal = fc.showOpenDialog(fcd);
    
    	//verifica se foi clicado sobre Open.
    	if (returnVal == JFileChooser.APPROVE_OPTION) 
			{
	    	//Captura o arquivo aberto.
	    	File file = fc.getSelectedFile();
	   		ultimodiretorio=fc.getCurrentDirectory();
       	arq.setText(file.getName());
	    }
		}
		else if(e.getActionCommand().equalsIgnoreCase("Exit"))
			System.exit(0);
	}
}