/*
* @(#)Arquivo.java  1.0  30/03/2001
*	
* Aline Vieira de Mello
* Pontifícia Universidade Católica do Rio Grande do Sul - Faculdade de Informática
*/

import java.io.*;
import java.awt.*;
import java.awt.image.*;
import javax.swing.filechooser.*;
import javax.swing.*;
import java.awt.event.*;

/**
* <i>Arquivo</i> é responsável pela abertura, gravação e carga das 
* informações do arquivo .sym para simulação.
*/
public class Arquivo 
{
	private FileChooserDemo fileChooserDemo;
	private Simulador sim;
	private JFileChooser fc;
	private String nomearq,arq;
	private String aux1,aux2,aux3;
	private int nvariaveis,linhamem,linhavar;
	private boolean inicializa=false;
	
	public Arquivo(Simulador sim,String arquitetura)
	{
		this.sim=sim;
		arq=arquitetura;
		nomearq="";
		fileChooserDemo=new FileChooserDemo();
	}

	/**
	* Informa se existe arquivo aberto. 
	*/
	public boolean arquivoAberto()
	{
		if(!nomearq.equals(""))
			return true;
		return false;
	}
	
	/**
	* Exibe a JFileChooser com a opção para abertura de um arquivo .asm e
	* chama o método montar(). 
	**/
	public File abrir(File ultimodir)
	{
	  File ultimodiretorio=ultimodir;
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
      if(nomearq!=null)
      	montar();
      return ultimodiretorio;
    }
    return ultimodir;
  }
  
  /**
  * Executa o método Nativo que chama o montador e verifica se o montador
  * encontrou algum erro no arquivo .asm através do método DEBUG. Se o
  * montador encontrar erro o mesmo é exibido, caso contrário é executado
  * o método carregar().
  */
  public void montar()
	{
  	int tam,i;
		boolean erroMontador;
  	String nome="";

		Montador montador=new Montador(1,arq,nomearq);
		
		if(montador.executa())
		{
			tam=nomearq.length()-3;
		
  		for(i=0;i<tam;i++)
			{
				nome=nome.concat(nomearq.substring(i,i+1));
			}
			nome=nome.concat("sym");
			nomearq=nome;
			
			//Exibe o nome do arquivo a ser carregado.
			sim.getJanela().setTitle(sim.getNomeArquitetura()+" Simulator Carregando ["+nomearq+"]");
			carregar();
		}
	}	
	
	/**
	* Lê o arquivo DEBUG.INF e retorna TRUE se ocorrem erros durante
	* a execução do Montador e os exibe ao usuário, caso não ocorram
	* erros é retornado FALSE.
	*/
	public boolean debug()
	{
		String aux;
		String mensagem="";
		try
		{
			//Abre o arquivo para leitura.
			FileInputStream fis=new FileInputStream("debug.inf");
			DataInputStream d=new DataInputStream(fis);
		  	BufferedReader dis=new BufferedReader(new InputStreamReader(fis)); 
		  
		  aux=dis.readLine();
		  if(aux!=null)
		  {
		  	if(aux.equals("0"))
			{
					fis.close();
					return false;
		  	}
		  	else
			{
			  	int cont=0;
			  	while(cont!=2 && aux!=null)
					{
						cont++;
			          mensagem=mensagem.concat(aux);
			          mensagem=mensagem.concat("\n");
			          aux=dis.readLine();
					}
					System.out.println("ERRO");
					JOptionPane.showMessageDialog(sim.getJanela(),mensagem,"ERRO", JOptionPane.ERROR_MESSAGE);
					fis.close();
					return true;
				}
			}
		}
		catch(Exception e)
		{
			//Ocorrendo exception indica que nao foi possivel abrir o arquivo.
			sim.getJanela().setTitle(sim.getNomeArquitetura()+" Simulator [Não foi possivel abrir o arquivo DEBUG.INF]");
		}
		return true;
	}	
		
	/**
	* Exibe a JFileChooser com a opção para salvar o arquivo.
	*/
	public void salvar()
	{
		 //Create a file chooser
		final JFileChooser fc = new JFileChooser();
    int returnVal = fc.showSaveDialog(fileChooserDemo);
    if (returnVal == JFileChooser.APPROVE_OPTION)
		{
     	File file = fc.getSelectedFile();
     	nomearq=file.getName();
		}
	}
    
	/**
	* Carrega as informações do arquivo aberto .sym para a simulação.
	*/
	public void carregar()
	{	
		if(inicializa)
			sim.inicializa();

		boolean erro=false;
		aux1="COMECO";
		try
		{
			//Abre o arquivo para leitura.
			FileInputStream fis=new FileInputStream(nomearq);
			DataInputStream d=new DataInputStream(fis);
			BufferedReader dis = new BufferedReader(new InputStreamReader(fis));
			
			nvariaveis=0;
			//Le todas as instrucoes e inclui na lista da memoria e 
			//na tabela memoria.
			while(!aux1.equalsIgnoreCase("FIMINST") && !erro)
			{
				aux1=dis.readLine();
				if(!aux1.equalsIgnoreCase("FIMINST"))
				{
					aux2=dis.readLine();
					aux3=dis.readLine();
					linhamem=sim.getConversao().hexa_decimal(aux1);
					if(linhamem<sim.getTamanhoMemoria())
					{
						sim.getMemoria().inclui(aux1,aux2,aux3);
						sim.getJanela().getTabelaMemoria().setValueAt(aux3,linhamem,0);
						sim.getJanela().getTabelaMemoria().setValueAt(aux1,linhamem,1);
						sim.getJanela().getTabelaMemoria().setValueAt(aux2,linhamem,2);
					}
					else
					{
						erro=true;
				  	sim.inicializa();
				  	JOptionPane.showMessageDialog(sim.getJanela(),"O Tamanho da Memória definido não é suficiente.","Tamanho da Memória", JOptionPane.ERROR_MESSAGE);
					}
				}
			}
			
			if(sim.isHarvard())
			{
				while(!aux1.equalsIgnoreCase("FIMMEM") && !erro)
				{
					aux1=dis.readLine();
					if(!aux1.equalsIgnoreCase("FIMMEM"))
					{
						linhamem=sim.getConversao().hexa_decimal(aux1);
						if(nvariaveis<sim.getNumeroVariaveis())
						{
							aux2=dis.readLine();
							aux3=dis.readLine();
					
							if(!aux3.equals(""))
							{						
								sim.getVariaveis().inclui(aux1,aux2,aux3);
								sim.getJanela().getTabelaVariaveis().setValueAt(aux3,nvariaveis,0);
								sim.getJanela().getTabelaVariaveis().setValueAt(aux1,nvariaveis,1);
								sim.getJanela().getTabelaVariaveis().setValueAt(aux2,nvariaveis,2);
								nvariaveis++;
							}
							
							//Se nao eh label por que label so possui endereco "" nome e
							//nao consideravel dado
							if(!aux2.equals(""))
							{
								linhamem=sim.getConversao().hexa_decimal(aux1);
								if(linhamem<sim.getTamanhoMemoria())
								{
									if(sim.getJanela().getTabelaMemoriaDados().getValueAt(linhamem,0).equals("")
										|| sim.getJanela().getTabelaMemoriaDados().getValueAt(linhamem,0).equals("DADOS"))
									{
										
										sim.getMemoriaDados().inclui(aux1,aux2,aux3);
								  	sim.getJanela().getTabelaMemoriaDados().setValueAt("DADOS",linhamem,0);
										sim.getJanela().getTabelaMemoriaDados().setValueAt(aux1,linhamem,1);
										sim.getJanela().getTabelaMemoriaDados().setValueAt(aux2,linhamem,2);
									}
								}
								else
								{
									erro=true;
						  		sim.inicializa();
									JOptionPane.showMessageDialog(sim.getJanela(),"O Tamanho da Memória de Dados definido não é suficiente.","Tamanho da Memória de Dados", JOptionPane.ERROR_MESSAGE);
								}								
							}
						}
						else
						{
							erro=true;
							sim.inicializa();
							JOptionPane.showMessageDialog(sim.getJanela(),"O Número de Símbolos definido não é suficiente.","Número de Símbolos", JOptionPane.ERROR_MESSAGE);
						}
					}
				}
			}
			else
			{
				while(!aux1.equalsIgnoreCase("FIMMEM") && !erro)
				{
					aux1=dis.readLine();
					if(!aux1.equalsIgnoreCase("FIMMEM"))
					{
						linhamem=sim.getConversao().hexa_decimal(aux1);
						if(nvariaveis<sim.getNumeroVariaveis())
						{
							aux2=dis.readLine();
							aux3=dis.readLine();
					
							if(!aux3.equals(""))
							{						
								sim.getVariaveis().inclui(aux1,aux2,aux3);
								sim.getJanela().getTabelaVariaveis().setValueAt(aux3,nvariaveis,0);
								sim.getJanela().getTabelaVariaveis().setValueAt(aux1,nvariaveis,1);
								sim.getJanela().getTabelaVariaveis().setValueAt(aux2,nvariaveis,2);
								nvariaveis++;
							}
							
							//Se nao eh label por que label so possui endereco "" nome e
							//nao consideravel dado
							if(!aux2.equals(""))
							{						
								linhamem=sim.getConversao().hexa_decimal(aux1);
								if(linhamem<sim.getTamanhoMemoria())
								{
									sim.getMemoria().inclui(aux1,aux2,aux3);
							  	sim.getJanela().getTabelaMemoria().setValueAt("DADOS",linhamem,0);
									sim.getJanela().getTabelaMemoria().setValueAt(aux1,linhamem,1);
									sim.getJanela().getTabelaMemoria().setValueAt(aux2,linhamem,2);
								}
								else
								{
									erro=true;
						  		sim.getJanela().inicializa();
									JOptionPane.showMessageDialog(sim.getJanela(),"O Tamanho da Memória definido não é suficiente.","Tamanho da Memória", JOptionPane.ERROR_MESSAGE);
								}
							}
						}
						else
						{
							erro=true;
							sim.inicializa();
							JOptionPane.showMessageDialog(sim.getJanela(),"O Número de Símbolos definido não é suficiente.","Número de Símbolos", JOptionPane.ERROR_MESSAGE);
						}
					}
				}
			}
			dis.close();
			
			if(!erro)
			{
				//Habilita os botões STEP, RUN, PAUSE, STOP e RESET.
				inicializa=true;
				sim.getJanela().getBstep().setEnabled(true);
				sim.getJanela().getBrun().setEnabled(true);
				sim.getJanela().getBstop().setEnabled(true);
				sim.getJanela().getBreset().setEnabled(true);
			
				//Exibe o nome do arquivo carregado.
				sim.getJanela().setTitle(sim.getNomeArquitetura()+" Simulator ["+nomearq+"]");
			
				//Seleciona a primeira linha da tabela memoria.
				sim.getJanela().getTabelaMemoria().setRowSelectionInterval(0,0);
			}
		}
		catch(Exception e)
		{
			//Ocorrendo exception indica que nao foi possivel abrir o arquivo.
			sim.getJanela().setTitle(sim.getNomeArquitetura()+" Simulator [Não foi possivel abrir o arquivo ]");
		}
	}
}