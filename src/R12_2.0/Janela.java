/*
* @(#)Janela.java  1.0  11/12/2001
*	
* Aline Vieira de Mello
* Pontifícia Universidade Católica do Rio Grande do Sul - Faculdade de Informática
*/

import javax.swing.*;
import javax.swing.table.*;
import java.awt.event.*;
import java.awt.*;
import java.io.*;

/**
* <i>Janela</i> Implementa a janela para a simulação com os menus Arquivo e Ajuda.
* Implementa também, as tabelas de memória e de variáveis, os registradores mais o
* IR, o PC e o SP, os flags e os botões de controle da simulação.
*/
public class Janela extends JFrame implements ActionListener
{
	private Simulador sim;
	private File ultimodiretorio;
	private FileChooserDemo fileChooserDemo;
	private JFileChooser fc;
	private Tabela tabelamemoria,tabelavariaveis,tabeladado;
	private JFrame fMensagem;
	private JLabel velocidade,lMensagem;
	private JPanel panelcenter;
	private JRadioButton lento,normal,rapido;
	private JButton bstep,brun,bstop,bpause,breset,blinhaMemoria,blinhaVariavel,blinhaDados,bBreakpoint;
	private JScrollPane scrollPaneMemoria,scrollPaneVariaveis,scrollPaneDados;
	private ButtonGroup grupo;
	private MeuMenu menu;
	private Registrador reg[];
	private Flag flag[];
	private Object[][] dadosVariaveis,dadosMemoria,dadosMemoriaDados;
	private String[] nomeColunasVariaveis,nomeColunasMemoria,nomeColunasDados,flags;
	private int[] dimColunasMemoria,dimColunasVariaveis,dimColunasDados;
	private int nreg;
	private int nflag;
	private double tempo;
	private boolean step;
	private boolean run;
	private boolean pause;
	private boolean stop;
	private boolean reset;
	private boolean breakpoint;
	private String linhabreakpoint;
	private int nclock,ninstrucao;
	  	    
	public Janela(Simulador sim,int nreg,int nflag,String[] flags) 
	{
		super();
 		this.sim=sim;
 		this.nreg=nreg;
 		this.nflag=nflag;
 		this.flags=flags;
 		nclock=0;
 		ninstrucao=0;
		breakpoint=false;
 		setTitle(sim.getNomeArquitetura()+" Simulator");
 		getContentPane().setLayout(new BorderLayout());
		setSize(770,540);
		Dimension resolucao=Toolkit.getDefaultToolkit().getScreenSize();
		setLocation((resolucao.width-770)/2,(resolucao.height-540)/2);
    addWindowListener(new WindowAdapter(){public void windowClosing(WindowEvent e){System.exit(0);}});
		ultimodiretorio=new File(System.getProperty("user.dir")+"\\Exemplos\\"+sim.getNomeArquitetura());
		fileChooserDemo=new FileChooserDemo();
		menu=new MeuMenu(this);
		reg=new Registrador[nreg+4];
		flag=new Flag[nflag];
		adicionaPanelCenter();
		setVisible(true);
	}

	/**
	* Adiciona o panel central com os registradores, o IR, o PC, o SP, os flags,
  * as tabelas de memória e variáveis e os botões de controle.
	*/
	public void adicionaPanelCenter()
	{
		panelcenter =new JPanel();
		panelcenter.setLayout(null);
		
		implementaRegistradores();
		
		implementaFlag();
		
		inicializaRegistradoresFlag();
		
		implementaDadosTabelas();

		if(sim.isHarvard())
		{
			implementaTabelaMemoria("Instruction Memory",10,10,305,370);
			implementaTabelaMemoriaDados(320,10,550,180);
			implementaTabelaVariaveis(320,200,550,370);
			implementaMoverParaLinhaMemoriaDados(465,180);
		}
		else
		{
			implementaTabelaMemoria("Memory",10,10,305,370);
			implementaTabelaVariaveis(320,10,550,370);
		}
		
		implementaMoverParaLinhaMemoria(220,370);
		implementaMoverParaLinhaSimbolo(465,370);
		implementaBotaoBreakpoint(12,370);
		implementaBotoes(60,410);
		
		implementaTempo(90,450);
		
		getContentPane().add(panelcenter,BorderLayout.CENTER);
	}

  /**
  * Implementa os registradores de uso geral, mais o ir, o pc e o sp.
  */
  public void implementaRegistradores()
  {
    int tam=5;
    int x=580;
    int y=30;
    int dimx=40;
    int dimy=20;
    int somay,somay1,somay2;
    int indice;
  
    // Primeira metade dos registradores
    for(indice=0,somay1=0;indice<((nreg+1)/2);indice++,somay1=somay1+30)
      reg[indice]=new Registrador(sim,panelcenter,"R"+indice,x,y+somay1,dimx,dimy,"Registrador R"+indice);
    
    // Segunda metade dos registradores
    for(indice=((nreg+1)/2),somay2=0;indice<(nreg);indice++,somay2=somay2+30)
      reg[indice]=new Registrador(sim,panelcenter,"R"+indice,x+80,y+somay2,dimx,dimy,"Registrador R"+indice);
      
    if(somay1>somay2)
      somay=somay1+10;
    else
      somay=somay1+10;
    
    //IR
   	reg[nreg]=new Registrador(sim,panelcenter,"IR",x,y+somay,dimx,dimy,"Registrador de Intruções");

    //PC
   	reg[nreg+1]=new Registrador(sim,panelcenter,"PC",x+80,y+somay,dimx,dimy,"Contador de Programa");
    
    somay=somay+30;
    //SP
		reg[nreg+2]=new Registrador(sim,panelcenter,"SP",x,y+somay,dimx,dimy,"Apontador de Pilha");
    
    //MASK
    if(sim.isMask())
    	reg[nreg+3]=new Registrador(sim,panelcenter,"MSK",x+80,y+somay,dimx,dimy,"Máscara dos Flags");
    
    JLayeredPane lbRegistradores=new JLayeredPane();
    lbRegistradores.setPreferredSize(new Dimension(175,somay+50));
    lbRegistradores.setBounds(x-15,y-20,175,somay+50);
    lbRegistradores.setBorder(BorderFactory.createTitledBorder("Registers"));
    panelcenter.add(lbRegistradores);
  }
	
  /**
  * Implementa os flags.
  */
  public void implementaFlag()
  {
    int x=580;
    int y=390;
    int dimx=70;
    int dimy=20;
    int somay1,somay2,indice;
    
    for(indice=0,somay1=0;indice<((nflag+1)/2);indice++,somay1=somay1+20)
      flag[indice]=new Flag(panelcenter,flags[indice],x,y+somay1,dimx,dimy,"Flag de "+flags[indice]);
    
    for(indice=((nflag+1)/2),somay2=0;indice<nflag;indice++,somay2=somay2+20)
        flag[indice]=new Flag(panelcenter,flags[indice],x+80,y+somay2,dimx,dimy,"Flag de "+flags[indice]);
    
    JLayeredPane lbFlag=new JLayeredPane();
    if(somay1>somay2)
    {
      lbFlag.setPreferredSize(new Dimension(175,somay1+30));
      lbFlag.setBounds(x-15,y-20,175,somay1+30);
    }        
    else
    {
      lbFlag.setPreferredSize(new Dimension(175,somay2+30));
      lbFlag.setBounds(x-15,y-20,175,somay2+30);
    }
    lbFlag.setBorder(BorderFactory.createTitledBorder("Flags"));
    panelcenter.add(lbFlag);
  }

  /**
  * Inicializa os registradores e flags.
  */
  public void inicializaRegistradoresFlag()
  {  
    for(int i=0;i<nreg;i++)
      getReg("R"+i).setValReg("0000");

    getReg("IR").setValReg("0000");
    getReg("PC").setValReg("0000");
    getReg("SP").setValReg(sim.getPosicaoSP());

    //MASK
    if(sim.isMask())
    	getReg("MSK").setValReg("");

    for(int i=0;i<nflag;i++)
      getFlag(i).setValFlag(false);
      
      
  }

  /**
  * Instância os dados iniciais das tabelas Memória e Variáveis.
  */
  public void implementaDadosTabelas()
  {
    //DADOS DA MEMORIA
    dadosMemoria=new Object[sim.getTamanhoMemoria()][3];
    for (int i=0;i<sim.getTamanhoMemoria();i++)
    {
      dadosMemoria[i][0]="";
      dadosMemoria[i][1]=sim.getConversao().decimal_hexa(i);
      dadosMemoria[i][2]="";
    }

    //DADOS DA MEMORIA DE DADOS
		if(sim.isHarvard())
  	{
  	  dadosMemoriaDados=new Object[sim.getTamanhoMemoria()][3];
    	for (int i=0;i<sim.getTamanhoMemoria();i++)
	    {
  	    dadosMemoriaDados[i][0]="";
    	  dadosMemoriaDados[i][1]=sim.getConversao().decimal_hexa(i);
	      dadosMemoriaDados[i][2]="";
	    }
  	}
  
    //DADOS DAS VARIAVEIS
    dadosVariaveis=new Object[sim.getNumeroVariaveis()][3];
    for (int i=0;i<sim.getNumeroVariaveis();i++)
    {
      dadosVariaveis[i][0]="";
      dadosVariaveis[i][1]="";
      dadosVariaveis[i][2]="";
    }
  }
  
  /**
  * Implementa a tabela que contem a memória, se for arquitetura harvard é a 
  * tabela que contém a memória de instruções.
  */
  public void implementaTabelaMemoria(String titulo,int x1,int y1,int x2,int y2)
  {
    nomeColunasMemoria=new String[3];
    nomeColunasMemoria[0]="Instruction";
    nomeColunasMemoria[1]="Address";
    nomeColunasMemoria[2]="Code";

    dimColunasMemoria=new int[3];
    dimColunasMemoria[0]=(x2-x1-30)/100*60;
    dimColunasMemoria[1]=(x2-x1-30)/100*20;
    dimColunasMemoria[2]=(x2-x1-30)/100*20;
    
    JLayeredPane lbMemoria=new JLayeredPane();
    lbMemoria.setPreferredSize(new Dimension(x2-x1,y2-y1));
    lbMemoria.setBounds(x1,y1,x2-x1,y2-y1);
    lbMemoria.setBorder(BorderFactory.createTitledBorder(titulo));
    panelcenter.add(lbMemoria);
    
    tabelamemoria = new Tabela(sim,dadosMemoria,nomeColunasMemoria,sim.getTamanhoMemoria(),3,dimColunasMemoria);
    scrollPaneMemoria = new JScrollPane(tabelamemoria);
    scrollPaneMemoria.setBounds(x1+15,y1+20,x2-x1-30,y2-y1-30);
    panelcenter.add(scrollPaneMemoria);
  }

  /**
  * Implementa a tabela de Variáveis.
  */
  public void implementaTabelaVariaveis(int x1,int y1,int x2,int y2)
  {
    nomeColunasVariaveis = new String[3];
    nomeColunasVariaveis[0]="Symbol";
    nomeColunasVariaveis[1]="Address";
    nomeColunasVariaveis[2]="Value";
    
    dimColunasVariaveis=new int[3];
    dimColunasVariaveis[0]=(x2-x1-30)/100*50;
    dimColunasVariaveis[1]=(x2-x1-30)/100*25;
    dimColunasVariaveis[2]=(x2-x1-30)/100*25;

    JLayeredPane lbVariaveis=new JLayeredPane();
    lbVariaveis.setPreferredSize(new Dimension(x2-x1,y2-y1));
    lbVariaveis.setBounds(x1,y1,x2-x1,y2-y1);
    lbVariaveis.setBorder(BorderFactory.createTitledBorder("Symbols"));
    panelcenter.add(lbVariaveis);
  
    tabelavariaveis = new Tabela(sim,dadosVariaveis,nomeColunasVariaveis,sim.getNumeroVariaveis(),3,dimColunasVariaveis);
    scrollPaneVariaveis = new JScrollPane(tabelavariaveis);
    scrollPaneVariaveis.setBounds(x1+15,y1+20,x2-x1-30,y2-y1-30);
    panelcenter.add(scrollPaneVariaveis);
  }

  /**
  * Implementa a tabela que contém a memória de Dados. Somente é incluída na
  * interface quando a arquitetura é HARVARD.
  */
  public void implementaTabelaMemoriaDados(int x1,int y1,int x2,int y2)
  {
    nomeColunasDados = new String[3];
    nomeColunasDados[0]="Data";
    nomeColunasDados[1]="Address";
    nomeColunasDados[2]="Value";
    
    dimColunasDados=new int[3];
    dimColunasDados[0]=(x2-x1-30)/100*50;
    dimColunasDados[1]=(x2-x1-30)/100*25;
    dimColunasDados[2]=(x2-x1-30)/100*25;

    JLayeredPane lbDados=new JLayeredPane();
    lbDados.setPreferredSize(new Dimension(x2-x1,y2-y1));
    lbDados.setBounds(x1,y1,x2-x1,y2-y1);
    lbDados.setBorder(BorderFactory.createTitledBorder("Data Memory"));
    panelcenter.add(lbDados);
  
    tabeladado = new Tabela(sim,dadosMemoriaDados,nomeColunasDados,sim.getTamanhoMemoria(),3,dimColunasDados);
    scrollPaneDados = new JScrollPane(tabeladado);
    scrollPaneDados.setBounds(x1+15,y1+20,x2-x1-30,y2-y1-30);
    panelcenter.add(scrollPaneDados);
  }
  
  /**
	* Implementa a interface da opção de selecionar a linha informada da tabela memória.
	*/
	public void implementaMoverParaLinhaMemoria(int x,int y)
	{
		int dimx=85,dimy=20;
		blinhaMemoria = new JButton("line",new ImageIcon("zoom.gif"));
	    blinhaMemoria.setToolTipText("Visualiza uma linha da tabela.");
		blinhaMemoria.setBounds(x,y,dimx,dimy);
		blinhaMemoria.addActionListener(this);
		getContentPane().add(blinhaMemoria);
	}
		
  /**
	* Implementa a interface da opção de selecionar a linha informada da tabela memória
	* de dados. Este botão é incluído a interface quando a arquitetura é HARVARD.
	*/
	public void implementaMoverParaLinhaMemoriaDados(int x,int y)
	{
		int dimx=85,dimy=20;
		blinhaDados = new JButton("line",new ImageIcon("zoom.gif"));
	    blinhaDados.setToolTipText("Visualiza uma linha da tabela.");
		blinhaDados.setBounds(x,y,dimx,dimy);
		blinhaDados.addActionListener(this);
		getContentPane().add(blinhaDados);
	}

  /**
	* Implementa a interface da opção de selecionar a linha informada da tabela de símbolos.
	*/
	public void implementaMoverParaLinhaSimbolo(int x,int y)
	{
		int dimx=85,dimy=20;
		blinhaVariavel = new JButton("line",new ImageIcon("zoom.gif"));
    	blinhaVariavel.setToolTipText("Visualiza uma linha da tabela.");
		blinhaVariavel.setBounds(x,y,dimx,dimy);
		blinhaVariavel.addActionListener(this);
		getContentPane().add(blinhaVariavel);
	}

	/**
	* Implementa a interface da opção de selecionar a linha informada da tabela memória.
	*/
	public void implementaBotaoBreakpoint(int x,int y)
	{
		int dimx=160,dimy=20;
		bBreakpoint = new JButton("insert breakpoint",new ImageIcon("breakpoint.gif"));
		bBreakpoint.setToolTipText("Insere um breakpoint na execução.");
		bBreakpoint.setForeground(new Color(90,123,57));
		bBreakpoint.setBounds(x,y,dimx,dimy);
		bBreakpoint.addActionListener(this);
		getContentPane().add(bBreakpoint);
	}


	/**
  *Implementa os botões que controlam a simulação.
  */
  public void implementaBotoes(int x,int y)
  {
    bstep=new JButton("STEP");
    bstep.setBounds(x,y,80,30);
    bstep.setToolTipText("Executa passo a passo.");
    bstep.addActionListener(this);
    bstep.setEnabled(false);
    panelcenter.add(bstep);
    brun=new JButton("RUN");
    brun.setBounds(x+85,y,80,30);
    brun.setToolTipText("Executa a simulação.");
    brun.addActionListener(this);
    brun.setEnabled(false);
    panelcenter.add(brun);
    bpause=new JButton("PAUSE");
    bpause.setBounds(x+170,y,80,30);
    bpause.setToolTipText("Para a simulação.");
    bpause.addActionListener(this);
    bpause.setEnabled(false);
    panelcenter.add(bpause);
    bstop=new JButton("STOP");
    bstop.setBounds(x+255,y,80,30);
    bstop.setToolTipText("Termina a simulação.");
    bstop.addActionListener(this);
    bstop.setEnabled(false);
    panelcenter.add(bstop);
    breset=new JButton("RESET");
    breset.setBounds(x+340,y,80,30);
    breset.setToolTipText("Reinicializa os dados da simulação.");
    breset.addActionListener(this);
    breset.setEnabled(false);
    panelcenter.add(breset);
  }

	/**
	* Implementa os botões que controlam a velocidade da simulação.
	*/
	public void implementaTempo(int x,int y)
	{
		velocidade=new JLabel("Speed:");
		velocidade.setBounds(x,y,80,20);
		panelcenter.add(velocidade);
		lento=new JRadioButton("Slow",false);
		lento.setBounds(x+85,y,100,20);
		lento.setToolTipText("Intervalo de 3s entre a execução das instruções.");
		panelcenter.add(lento);
    normal=new JRadioButton("Normal",true);
		normal.setBounds(x+190,y,100,20);
		normal.setToolTipText("Intervalo de 1s entre a execução das instruções.");
		panelcenter.add(normal);
		rapido=new JRadioButton("Fast",false);
		rapido.setBounds(x+295,y,100,20);
		rapido.setToolTipText("Intervalo de 0s entre a execução das instruções.");
		panelcenter.add(rapido);
		
		grupo=new ButtonGroup();
		grupo.add(lento);
		grupo.add(normal);
		grupo.add(rapido);
	}

	/**
	* Inicializa a tabela memoria.
	*/
	public void inicializaTabelaMemoria()
	{
		NodoMemoria nodo=sim.getMemoria().getInicio();
		while(nodo!=null)
		{
			//Inicializa as linhas da tabela que contem dados.
			String[] linha={nodo.getPc(),"",""};
			tabelamemoria.setLinha(linha);
			nodo=nodo.getProx();
		}
	}

	/**
	* Inicializa a tabela memoria de dados.
	*/
	public void inicializaTabelaMemoriaDados()
	{
		NodoMemoria nodo=sim.getMemoriaDados().getInicio();
		while(nodo!=null)
		{
			//Inicializa as linhas da tabela que contem dados.
			String[] linha={nodo.getPc(),"",""};
			tabeladado.setLinha(linha);
			nodo=nodo.getProx();
		}
	}

	/**
	* Inicializa a tabela de variaveis.
	*/
	public void inicializaTabelaVariaveis()
	{
		NodoVariavel nodo=sim.getVariaveis().getInicio();
		while(nodo!=null)
		{
			//Inicializa as linhas da tabela que contem dados.
			tabelavariaveis.setLinha(nodo.getEndereco());
			nodo=nodo.getProx();
		}
	}
		
	/**
	* Inicializa as tabelas de memoria e variavel.
	*/
	public void inicializaTabelas()
	{
		inicializaTabelaMemoria();
		inicializaTabelaVariaveis();
		if(sim.isHarvard())
			inicializaTabelaMemoriaDados();
	}
	
	/**
	* Inicializa as tabelas, os flags e os registradores.
	*/
	public void inicializa()
	{
		String title=getTitle();
    setTitle(sim.getNomeArquitetura()+" Simulator                      REINICILIZANDO OS DADOS DA SIMULAÇÃO.");
		inicializaRegistradoresFlag();
		inicializaTabelas();
		ninstrucao=0;
		nclock=0;
		setTitle(title);
	}		
	
  /**
  * Incrementa o número de clocks.
  */
  public void incrementaNclock(int n){nclock=nclock+n;}

  /**
  * Decrementa o número de clocks.
  */
  public void decrementaNclock(int n){nclock=nclock-n;}

  /**
  * Incrementa o número de instruções.
  */
  public void incrementaNinstrucao(){ninstrucao++;}
	
	/**
	* Executa a simulação da instrução atual do pc.
	*/
	public void step()
	{
		step=true;
		run=false;
		stop=false;
		pause=false;
		reset=false;
		Execucao exec=new Execucao(sim,sim.getArquitetura());
		exec.start();
	}
	
	/**
	* Executa a simulação das instruções.
	*/
	public void run()
	{
		step=false;
		run=true;
		stop=false;
		pause=false;
		reset=false;
		brun.setEnabled(false);
		bstep.setEnabled(false);
		breset.setEnabled(false);
		bpause.setEnabled(true);
		Execucao exec=new Execucao(sim,sim.getArquitetura());
		exec.start();
	}
	
	/**
	* Produz uma pausa na execução da simulação.
	*/
	public void pause()
	{
		step=false;
		run=false;
		stop=false;
		pause=true;
		reset=false;
		brun.setEnabled(true);
		bstep.setEnabled(true);
		bpause.setEnabled(false);
	}
	
	/**
	* Termina a execução da simulação.
	*/
	public void stop()
	{
		step=false;
		run=false;
		stop=true;
		pause=false;
		reset=false;
		brun.setEnabled(true);
		bstep.setEnabled(true);
		breset.setEnabled(true);
  	bpause.setEnabled(false);
	}
	
	/**
	* Reinicializa a simulação.
	*/
	public void reset()
	{
    step=false;
		run=false;
		stop=false;
		pause=false;
		reset=true;
		bpause.setEnabled(false);
	  sim.getExecucao().setHalt(false);
		tabelamemoria.setRowSelectionInterval(0,0);
		scrollPaneMemoria.getVerticalScrollBar().setValue(0);
		sim.getArquivo().carregar();
	}	
	
	/**
	* Abre o notepad para edição de um novo arquivo .asm
	*/
	public void novo()
	{
		try
		{
			Process process = Runtime.getRuntime().exec("notepad");
		}
		catch(Exception exc)
		{
			JOptionPane.showMessageDialog(this,"Notepad não pode ser aberto.","ERROR MESSAGE",JOptionPane.ERROR_MESSAGE);
		}
	}
	
		
	/**
	* Carrega um arquivo .asm para ser simulado.
	*/
	public void carrega()
	{
  	ultimodiretorio= sim.getArquivo().abrir(ultimodiretorio);
		tabelamemoria.setRowSelectionInterval(0,0);
		scrollPaneMemoria.getVerticalScrollBar().setValue(0);
	}
	
	/**
	* Abre o notepad para com um arquivo existente para a edição do mesmo.
	*/
	public void edita()
	{
		String nomearq="";

	  //Cria um file chooser.
    fc = new JFileChooser(ultimodiretorio);
    
    //Filtra somente os diretorios e arquivos .asm.
    fc.setFileFilter(new FiltraArquivos("asm"));
    
    //FileChosser para abrir arquivo.
    int returnVal = fc.showOpenDialog(fileChooserDemo);
    
    //verifica se foi clicado sobre Open.
    if (returnVal == JFileChooser.APPROVE_OPTION) 
		{
    	//Captura o arquivo aberto para ser montado.
    	File file = fc.getSelectedFile();
    	ultimodiretorio=fc.getCurrentDirectory();
    	nomearq=file.getPath();
	  
	  	try
			{
				Process process = Runtime.getRuntime().exec("notepad "+nomearq);
			}
			catch(Exception exc)
			{
				JOptionPane.showMessageDialog(this,"Arquivo "+nomearq+" não pode ser abertopelo Notepad.","ERROR MESSAGE",JOptionPane.ERROR_MESSAGE);
			}
		}
	}
	
	/**
	* Seleciona uma linha, da tabela memória, especificada pelo usuário.
	*/
	public void selecionaLinhaMemoria()
	{	
		String nro =(String)JOptionPane.showInputDialog("Digite o número da linha que deseja visualizar[0-"+sim.getConversao().decimal_hexa((sim.getTamanhoMemoria()-1))+"]:");
		if(nro != null)	
		{
			try
			{
				if (sim.getConversao().isHexadecimal(nro.trim()))
				{
					int linha = sim.getConversao().hexa_decimal(nro.trim());
					if(linha<0 || linha>=sim.getTamanhoMemoria())
						JOptionPane.showMessageDialog(null,"Número de linha menor ou maior que o permitido.",sim.getNomeArquitetura()+" Simulator",JOptionPane.ERROR_MESSAGE);
					else
					{
						tabelamemoria.setRowSelectionInterval(linha,linha);
						scrollPaneMemoria.getVerticalScrollBar().setValue((scrollPaneMemoria.getVerticalScrollBar().getMaximum() / sim.getTamanhoMemoria()) * linha);
					}
				}
				else
					JOptionPane.showMessageDialog(null,"Número de linha inválido",sim.getNomeArquitetura()+" Simulator",JOptionPane.ERROR_MESSAGE);
			}
			catch(NumberFormatException exc)
			{
				JOptionPane.showMessageDialog(null,"Número de linha inválido",sim.getNomeArquitetura()+" Simulator",JOptionPane.ERROR_MESSAGE);
			}
		}
	}
	
	/**
	* Seleciona uma linha, da tabela memória de dados, especificada pelo usuário.
	*/
	public void selecionaLinhaMemoriaDados()
	{	
		String nro =(String)JOptionPane.showInputDialog("Digite o número da linha que deseja visualizar[0-"+sim.getConversao().decimal_hexa((sim.getTamanhoMemoria()-1))+"]:");
		if(nro != null)	
		{
			try
			{
				if (sim.getConversao().isHexadecimal(nro.trim()))
				{
					int linha = sim.getConversao().hexa_decimal(nro.trim());
					if(linha<0 || linha>=sim.getTamanhoMemoria())
						JOptionPane.showMessageDialog(null,"Número de linha menor ou maior que o permitido.",sim.getNomeArquitetura()+" Simulator",JOptionPane.ERROR_MESSAGE);
					else
					{
						tabeladado.setRowSelectionInterval(linha,linha);
						scrollPaneDados.getVerticalScrollBar().setValue((scrollPaneDados.getVerticalScrollBar().getMaximum() / sim.getTamanhoMemoria()) * linha);
					}
				}
				else
					JOptionPane.showMessageDialog(null,"Número de linha inválido",sim.getNomeArquitetura()+" Simulator",JOptionPane.ERROR_MESSAGE);
			}
			catch(NumberFormatException exc)
			{
				JOptionPane.showMessageDialog(null,"Número de linha inválido",sim.getNomeArquitetura()+" Simulator",JOptionPane.ERROR_MESSAGE);
			}
		}
	}


	/**
	* Seleciona uma linha, da tabela variável, especificada pelo usuário.
	*/
	public void selecionaLinhaVariavel()
	{
		String nro =(String)JOptionPane.showInputDialog(null,"Digite o número da linha que deseja visualizar[0-"+sim.getConversao().decimal_hexa((sim.getNumeroVariaveis()-1))+"]:","Visualizador de Linha",JOptionPane.QUESTION_MESSAGE);
		if(nro != null)	
		{
			try
			{
				if (sim.getConversao().isHexadecimal(nro.trim()))
				{
					int linha = sim.getConversao().hexa_decimal(nro.trim());
					if(linha<0 || linha>=sim.getNumeroVariaveis())
						JOptionPane.showMessageDialog(null,"Número de linha menor ou maior que o permitido.",sim.getNomeArquitetura()+" Simulator",JOptionPane.ERROR_MESSAGE);
					else
					{
						tabelavariaveis.setRowSelectionInterval(linha,linha);
						scrollPaneVariaveis.getVerticalScrollBar().setValue((scrollPaneVariaveis.getVerticalScrollBar().getMaximum() / sim.getNumeroVariaveis()) * linha);
				      }
				}
				else
					JOptionPane.showMessageDialog(null,"Número de linha inválido",sim.getNomeArquitetura()+" Simulator",JOptionPane.ERROR_MESSAGE);
			}
			catch(NumberFormatException exc)
			{
				JOptionPane.showMessageDialog(null,"Número de linha inválido",sim.getNomeArquitetura()+" Simulator",JOptionPane.ERROR_MESSAGE);
			}
		}
	}

	
	/**
	* Insere breakpoint em uma linha.
	*/
	public void insereBreakpoint()
	{	
		String nro =(String)JOptionPane.showInputDialog("Digite o número da linha que deseja inserir breakpoint[0-"+sim.getConversao().decimal_hexa((sim.getTamanhoMemoria()-1))+"]:");
		if(nro != null)	
		{
			try
			{
				if (sim.getConversao().isHexadecimal(nro.trim()))
				{
					int linha = sim.getConversao().hexa_decimal(nro.trim());
					if(linha<0 || linha>=sim.getTamanhoMemoria())
						JOptionPane.showMessageDialog(null,"Número de linha menor ou maior que o permitido.",sim.getNomeArquitetura()+" Simulator",JOptionPane.ERROR_MESSAGE);
					else
					{
						String linhaHex = sim.getConversao().decimal_hexa(linha,4);
						setLinhaBreakpoint(linhaHex);
						setBreakpoint(true);
						bBreakpoint.setText("remove breakpoint");
						bBreakpoint.setToolTipText("Remove o breakpoint da execução.");
						bBreakpoint.setForeground(Color.red);
					}
				}
				else
					JOptionPane.showMessageDialog(null,"Número de linha inválido",sim.getNomeArquitetura()+" Simulator",JOptionPane.ERROR_MESSAGE);
			}
			catch(NumberFormatException exc)
			{
				JOptionPane.showMessageDialog(null,"Número de linha inválido",sim.getNomeArquitetura()+" Simulator",JOptionPane.ERROR_MESSAGE);
			}
		}
	}

	/**
	* Insere breakpoint em uma linha.
	*/
	public void removeBreakpoint()
	{	
		setBreakpoint(false);
		bBreakpoint.setText("insert breakpoint");
		bBreakpoint.setToolTipText("Insere um breakpoint na execução.");
		bBreakpoint.setForeground(new Color(90,123,57));
	}
	
	/**
	* Executa uma operação conforme o botão de controle que for pressionado.
	*/
	public void actionPerformed(ActionEvent e)
	{
		if(e.getActionCommand().equalsIgnoreCase("STEP"))
	   	step();
		else if(e.getActionCommand().equalsIgnoreCase("RUN"))
	   	run();
		else if(e.getActionCommand().equalsIgnoreCase("PAUSE"))
	   	pause();
		else if(e.getActionCommand().equalsIgnoreCase("STOP"))
	   	stop();
		else if(e.getActionCommand().equalsIgnoreCase("RESET"))
	   	reset();
		else if(e.getActionCommand().equalsIgnoreCase("New"))
			novo();
		else if(e.getActionCommand().equalsIgnoreCase("Load..."))
      carrega();
    else if(e.getActionCommand().equalsIgnoreCase("Edit..."))
			edita();
		else if(e.getActionCommand().equalsIgnoreCase("Exit"))
      System.exit(0);
		else if(e.getActionCommand().equalsIgnoreCase("About"))
    	JOptionPane.showMessageDialog(this,"Simulador de Arquiteturas        13.11.2003\nAline Vieira de Mello\nKarine de Pinho Peralta","VERSÃO 2.3",JOptionPane.INFORMATION_MESSAGE);
		else if(e.getActionCommand().equalsIgnoreCase("Help"))
    {
			Help help=new Help(sim);
    	help.apresenta();
		}
		else if(e.getSource().equals(blinhaMemoria))
			selecionaLinhaMemoria();
		else if(e.getSource().equals(blinhaDados))
			selecionaLinhaMemoriaDados();
		else if(e.getSource().equals(blinhaVariavel))
			selecionaLinhaVariavel();
		else if(e.getSource().equals(bBreakpoint) && bBreakpoint.getText().equalsIgnoreCase("insert breakpoint"))
			insereBreakpoint();
		else if(e.getSource().equals(bBreakpoint) && bBreakpoint.getText().equalsIgnoreCase("remove breakpoint"))
			removeBreakpoint();
  }
  
  /**
  * Captura o valor booleano do STEP.
  */
  public boolean getStep(){return step;} 
  
  /**
  * Captura o valor booleano do RUN.
  */
  public boolean getRun(){return run;}
  
  /**
  * Captura o valor booleano do PAUSE.
  */
  public boolean getPause(){return pause;}  
  
  /**
  * Captura o valor booleano do STOP.
  */
  public boolean getStop(){return stop;}
  
  /**
  * Captura o valor booleano do RESET.
  */
  public boolean getReset(){return reset;}
         
  /**
  * Captura o botão Registrador corresponde ao nome informado.
  */
  public Registrador getReg(String nome)
  {
    if(nome.equalsIgnoreCase("RA"))
      nome="R10";
    else if(nome.equalsIgnoreCase("RB"))
      nome="R11";
    else if(nome.equalsIgnoreCase("RC"))
      nome="R12";
    else if(nome.equalsIgnoreCase("RD"))
      nome="R13";
    else if(nome.equalsIgnoreCase("RE"))
      nome="R14";
    else if(nome.equalsIgnoreCase("RF"))
      nome="R15";
            
    //Registrador R0 a R15
    for(int indice=0;indice<nreg;indice++)
    {
      if(reg[indice].getNome().equalsIgnoreCase(nome))
        return reg[indice];
    }  
    
    //Registrador IR
    if(reg[nreg].getNome().equalsIgnoreCase(nome))
      return reg[nreg];
    
    //Registrador PC
    if(reg[nreg+1].getNome().equalsIgnoreCase(nome))
      return reg[nreg+1];
    
    //Registrador SP
    if(reg[nreg+2].getNome().equalsIgnoreCase(nome))
      return reg[nreg+2];

    //Registrador MSK
    if(reg[nreg+3].getNome().equalsIgnoreCase(nome))
      return reg[nreg+3];

    return null;
  }

  /**
  * Seta o registrador que contém a máscara dos flags. 
  */
 	public void setMascara(String mascaraBin)
 	{
 		String mascaraHex = sim.getConversao().binario_hexa(mascaraBin,2);
 		reg[nreg+3].setValReg(mascaraHex);
 	}

  /**
  * Retorna a mascara em binaria. 
  */
	public String getMascara()
	{
		String mascaraHex = reg[nreg+3].getValReg();
		return sim.getConversao().hexa_binario(mascaraHex,8);
	}

  /**
  * Retorna o registrador indicado pelo indice informado. 
  */
  public Registrador getReg(int i){return reg[i];}
  
  /**
  * Captura o tempo para simulação.
  */
  public double getTempo()
  {
    if(lento.isSelected())
      tempo=3.0;
    else if(normal.isSelected())
      tempo=1.0;
    else if(rapido.isSelected())
      tempo=0;
    return tempo;
  }
  
  /**
  * Retorna o flag indicado pelo indice informado. 
  */
  public Flag getFlag(int i){return flag[i];}
  
  /**
  * Captura o flag correspondente ao nome indicado.
  */
  public Flag getFlag(String n)
  {
    for(int i=0;i<nflag;i++)
    {
      if(flag[i].getNome().equalsIgnoreCase(n))
        return flag[i];
    }
    return null;
  }
  
  /**
  * Captura a tabela Memória.
  */
  public Tabela getTabelaMemoria() {return tabelamemoria; }
  
  /**
  * Captura a tabela Memória de Dados.
  */
  public Tabela getTabelaMemoriaDados() {return tabeladado; }


  /**
  * Captura a tabela de Variáveis.
  */
  public Tabela getTabelaVariaveis() {return tabelavariaveis; }
  
  /**
  * Captura o botão STEP.
  */  
  public JButton getBstep(){return bstep;}
  
  /**
  * Captura o botão RESET.
  */  
  public JButton getBreset(){return breset;}
  
  /**
  * Captura o botão STOP.
  */  
  public JButton getBstop(){return bstop;}
  
  /**
  * Captura o botão RUN.
  */  
  public JButton getBrun(){return brun;}
  
  /**
  * Captura o botão PAUSE.
  */  
  public JButton getBpause(){return bpause;}
  
  /**
  * Captura o panel central.
  */  
  public JPanel getPanel(){return panelcenter;}
  
  /**
  * Captura o tamanho da memória.
  */  
  public int getTamanhoMemoria(){return sim.getTamanhoMemoria();}
  
  /**
  * Captura o número máximo de variáveis definido.
  */  
  public int getNumeroVariaveis(){return sim.getNumeroVariaveis();}
  
  /**
  * Captura o número de instruções do programa.
  */  
  public int getNInstrucoes(){return ninstrucao;}
  
  /**
  * Captura o número de clocks do programa.
  */  
  public int getNClocks(){return nclock;}

	/**
	* Verifica se um breakpoint foi inserido.
	*/
	public boolean getBreakpoint() {return breakpoint;}

	/**
	* Seta se existe ou não breakpoint.
	*/
	public void setBreakpoint(boolean b) 
	{
		breakpoint=b;
		if(breakpoint)
			bBreakpoint.setForeground(Color.red);
		else
			bBreakpoint.setForeground(Color.black);
	}
  
	/**
	* Captura a linha do breakpoint.
	*/
	public String getLinhaBreakpoint() {return linhabreakpoint;}

	/**
	* Seta a linha onde foi inserido o breakpoint.
	*/
	public void setLinhaBreakpoint(String l) {linhabreakpoint=l;}


	/**
	* Seta se existe ou não breakpoint.
	*/
	public void setBreakPoint(boolean b) {breakpoint=b;}



  /**
  * Captura o panel que controla a memória.
  */
  public JScrollPane getScrollPaneMemoria(){return scrollPaneMemoria;}
}