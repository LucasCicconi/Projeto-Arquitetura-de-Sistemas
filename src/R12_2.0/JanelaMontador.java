/*
* @(#)JanelaMontador.java  1.0  30/03/2001
*	
* Aline Vieira de Mello
* Pontifícia Universidade Católica do Rio Grande do Sul - Faculdade de Informática
*/

import java.awt.event.*;
import javax.swing.*;
import java.awt.*;
import java.io.*;

/**
* <i>JanelaMontador</i> implementa a janela para otimizar o arquivo.asm e o
* arquivo.vhdl, gerando dois arquivos: o arquivo asmlinha e o arquivo
* vhdllinha.
*/
public class JanelaMontador extends JFrame implements ActionListener
{
	private int modo;
	private JLabel larquitetura,larquivo;
	private JTextField tarquitetura,tarquivo;
	private JButton barquiteturaopen,barquivoopen,bmontar,bsair;
	private JPanel panelcenter;
	private File ultimodiretorio;
	private FileChooserDemo fileChooserDemo;
	private JFileChooser jfilechooser;
	private Color corfonte=Color.black;
	private Color cormensagem=new Color(91,127,57);
	private Color cormensagemerro=Color.red;
	private String narquivo,narquitetura,caminhoarquivo,caminhoarquitetura;
	//private MeuMenu menu;
	  	    
	public JanelaMontador() 
  {
  	super();
 		modo=1;
 		setTitle("MONTADOR");
 		//setBackground(corfundo);
		getContentPane().setLayout(new BorderLayout());
 	  setSize(440,150);
		Dimension resolucao=Toolkit.getDefaultToolkit().getScreenSize();
		setLocation((resolucao.width-440)/2,(resolucao.height-150)/2);
		ultimodiretorio=new File(System.getProperty("user.dir"));
		fileChooserDemo=new FileChooserDemo();
	  addWindowListener(new WindowAdapter(){public void windowClosing(WindowEvent e){System.exit(0);}});
	  implementa();
  }
	
	/**
	* Implementa a janela, criando seus componentes.
	*/
	public void implementa()
	{
	  //menu=new MeuMenu(this);
  	adicionaPanelCenter();
		setVisible(true);
	}
	
	/**
	* Adiciona o panel central.
	*/
	public void adicionaPanelCenter()
	{
		int x=120;
		int y=10;
		
		panelcenter =new JPanel();
		panelcenter.setLayout(null);
		implementaImagemGaph(10,60,130,45);
		implementaArquiteturaMontador(10,y,100,20);
		implementaArquivo(10,y+30,100,20);
		implementaBotaoMontar(180,70,100,30);
		implementaBotaoSair(300,70,100,30);
		getContentPane().add(panelcenter,BorderLayout.CENTER);
	}

	/**
	* Implementa a figura do GAPH.
	*/
	public void implementaImagemGaph(int x,int y,int dimx,int dimy)
	{
	  Canvas_Gif canvas=new Canvas_Gif();
	  canvas.setBounds(x,y,dimx,dimy);
		panelcenter.add(canvas);			
	}
	
	/**
	* Implementa o label e textfield reservado para a escrita do arquivo 
	* contem as informações sobre a arquitetura.
	*/
	public void implementaArquiteturaMontador(int x,int y,int dimx,int dimy)
	{
		larquitetura=new JLabel("Arquitetura:");
		//larquitetura.setForeground(corfonte);
		larquitetura.setBounds(x,y,dimx,dimy);
		panelcenter.add(larquitetura);

		tarquitetura=new JTextField("");
		tarquitetura.setBounds(x+85,y,dimx+150,dimy);
		panelcenter.add(tarquitetura);
		
		barquiteturaopen=new JButton("Open");
		//barquiteturaopen.setForeground(corfonte);
		barquiteturaopen.setBounds(x+340,y,dimx-30,dimy);
		barquiteturaopen.addActionListener(this);
		panelcenter.add(barquiteturaopen);
	}
	
	/**
	* Implementa o label e textfield reservado para a escrita do arquivo asm 
	* que será otimizado.
	*/
	public void implementaArquivo(int x,int y,int dimx,int dimy)
	{
		larquivo=new JLabel("Arquivo asm:");
		//larquivo.setForeground(corfonte);
		larquivo.setBounds(x,y,dimx,dimy);
		panelcenter.add(larquivo);

		tarquivo=new JTextField();
		tarquivo.setBounds(x+85,y,dimx+150,dimy);
		panelcenter.add(tarquivo);
		
		barquivoopen=new JButton("Open ");
		//barquivoopen.setForeground(corfonte);
		barquivoopen.setBounds(x+340,y,dimx-30,dimy);
		barquivoopen.addActionListener(this);
		panelcenter.add(barquivoopen);
	}

	/**
	* Implementa o botão Montar que inicia a montar o arquivo.
	*/
	public void implementaBotaoMontar(int x,int y,int dimx,int dimy)
	{
		bmontar=new JButton("Montar");
		bmontar.setBounds(x,y,dimx,dimy);
		bmontar.setForeground(cormensagem);
		bmontar.addActionListener(this);
		panelcenter.add(bmontar);
	}

	/**
	* Implementa o botão Sair que fecha o programa.
	*/
	public void implementaBotaoSair(int x,int y,int dimx,int dimy)
	{
		bsair=new JButton("Sair");
		bsair.setBounds(x,y,dimx,dimy);
		bsair.setForeground(cormensagem);
		bsair.addActionListener(this);
		panelcenter.add(bsair);
	}
	
	/**
	* Seleciona o arquivo que contem a arquitetura.
	*/
	public void selecionaArquivoArquiteturaMontador(JTextField tf,String ext,boolean arquivo)
	{
		//Cria um file chooser.
    jfilechooser = new JFileChooser(ultimodiretorio);
    
    //Filtra somente os diretorios e arquivos .asm.
    jfilechooser.setFileFilter(new FiltraArquivos(ext));
    
    //FileChosser para abrir arquivo.
    int returnVal = jfilechooser.showOpenDialog(fileChooserDemo);
    
    //verifica se foi clicado sobre Open.
    if (returnVal == JFileChooser.APPROVE_OPTION) 
		{
    	//Captura o arquivo aberto para ser montado.
    	File file = jfilechooser.getSelectedFile();
    	ultimodiretorio=jfilechooser.getCurrentDirectory();
    	tf.setText(file.getPath());
    	if(arquivo)
			{
    	 	caminhoarquivo=file.getParent();
    		narquivo=file.getName();
			}
			else
			{
    	 	caminhoarquitetura=file.getParent();
    		narquitetura=file.getName();
			}
    }
  }
	
	/**
	* Executa uma operação conforme o botão de controle que for pressionado.
	*/
	public void actionPerformed(ActionEvent e)
	{
		if(e.getActionCommand().equalsIgnoreCase("Montar"))
		{
			narquitetura=getArquiteturaMontador();
			narquivo=getArquivo();
	  	if(narquitetura.equals(""))
				new Erro(modo).show("TEXTFIELD_ARQUITETURA");
  		else
			{
				//verifica se o arquivo da arquitetura tem caminho
				//SE nao tiver inclui
				if(!narquitetura.substring(1,2).equals(":"))
					narquitetura=System.getProperty("user.dir")+"\\"+narquitetura;

				//Verifica se existe a extensão arq senao adiciona a extensão.
				if(narquitetura.length()>4)
				{
					if(!narquitetura.substring(narquitetura.length()-4,narquitetura.length()).equalsIgnoreCase(".arq"))
						narquitetura=narquitetura+".arq";
				}
				else
						narquitetura=narquitetura+".arq";
				
				
		  	if(narquivo.equals(""))
					new Erro(modo).show("TEXTFIELD_ARQUIVO");
		  	else
		  	{
					//verifica se o arquivo da arquitetura tem caminho
					//SE nao tiver inclui
					if(!narquivo.substring(1,2).equals(":"))
						narquivo=System.getProperty("user.dir")+"\\"+narquivo;

					//Verifica se existe a extensão arq senao adiciona a extensão.
					if(narquivo.length()>4)
					{
						if(!narquivo.substring(narquivo.length()-4,narquivo.length()).equalsIgnoreCase(".asm"))
							narquivo=narquivo+".asm";
					}
					else
							narquivo=narquivo+".asm";

					Montador montador=new Montador(modo,narquitetura,narquivo);
					
					if(montador.executa())
						JOptionPane.showMessageDialog(null,"Codigo Montado com Sucesso!","Montador",JOptionPane.INFORMATION_MESSAGE);
		  	}
			}
		}
		else if(e.getActionCommand().equalsIgnoreCase("Sair"))
			System.exit(0);
		else if(e.getActionCommand().equalsIgnoreCase("Open"))
			selecionaArquivoArquiteturaMontador(tarquitetura,"arq",false);
		else if(e.getActionCommand().equalsIgnoreCase("Open "))
			selecionaArquivoArquiteturaMontador(tarquivo,"asm",true);
	}
 
 	/**
 	* Captura o nome da arquitetura com o seu caminho informado no textfield.
 	*/
  public String getArquiteturaMontador(){return tarquitetura.getText();}
  
  /**
 	* Captura o nome do arquivo.asm com o seu caminho informado no textfield.
 	*/
  public String getArquivo(){return tarquivo.getText();}
  
  /**
 	* Captura somente o nome da arquitetura.
 	*/
  public String getNomeArquiteturaMontador() {return narquitetura;}
  
  /**
 	* Captura somente o nome do arquivo.asm.
 	*/
  public String getNomeArquivo() {return narquivo;}
  
  /**
 	* Captura somente o caminho da arquitetura.
 	*/
  public String getCaminhoArquiteturaMontador(){return caminhoarquitetura;}
  
  /**
 	* Captura somente o caminho do arquivo.asm.
 	*/
  public String getCaminhoArquivo(){return caminhoarquivo;}
  
   /**
 	* Atribui o nome da arquitetura no textfield.
 	*/
  public void setArquiteturaMontador(String a){tarquitetura.setText(a);}
	
	/**
 	* Atribui o nome da arquivo.asm no textfield.
 	*/
  public void setArquivo(String a){tarquivo.setText(a);}
	
}