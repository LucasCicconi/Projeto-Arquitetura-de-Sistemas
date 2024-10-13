/*
* @(#)Simulador.java  1.0  30/03/2001
*	
* @autor Aline Vieira de Mello
* Pontifícia Universidade Católica do Rio Grande do Sul - Faculdade de Informática
*/

import java.util.*;
import java.io.*;
import javax.swing.*;

/**
* <i>Simulador</i> seleciona a arquitetura e executa o simulador a partir da descrição
* contida no arquivo da arquitetura.
*/

/* Últimas atualizações:
*
* 16/11/01 
* -> Inserção das funções INF2 e SUP2 que fazem a comparação entre valores em complememto de 2.
* -> Correção do bug na instrução NOT
*
* 11/12/01
* -> Acréscimo da opção de informar a linha que se deseja visualizar na tabela memória ou na
* tabela de variáveis.
*
* 10/01/02
* -> Calculo correto das linhas visiveis da tabela Memória.
*
* 04/04/02
* -> Escrita em ordem dos endereco dos Labels no arquivo TXT (alteração no arquivos.c [Montado]).
*
* 18/04/02
* -> Inclusão das funções de JMPD10 e JMPDC10, ou seja, deslocamento de 10bits incondicional e
* condicional a um flag.
*
* 19/04/02
* -> Continuação do conserto das funções JMPD10 e JMPDC10
*
* 11/05/02
* -> Inclusão da Instrução IGNORE
*
* 16/05/02
* -> Recebe o código montado original e altera a ordem destro do simulador, e não no montador
* como estava ocorrendo anteriormente.
*
* -> Inclui a instrucao de SL0C,SL1C,SR0C,SR1C,LDRI,STRI,JALR,SLT,SKPEQ,SKPNE,
* SKPEQI e SKPNEI.
*
* -> Conserto no scroll da tabela que contem a memória.
*
* -> Conserto nas instruções ADDI e SUBI, que não possui ext_sinal
*
* 27/05/02
* -> Inclusão da instrução STSP e modificação na execução da LDSP.
*
* 28/05/02
* -> Seleciona a posição da memória apontada pela próxima instrução.
*
* -> Opção NOT da pilha, que refere-se quando a pilha não é utilizada no processador.
*
* 08/07/02
* -> Conserto da instrução SLT.
*
* 05/05/03
* -> Insercao do teste de verificacao de constantes terminadas em H e B. Antes se uma palavra
* comecava por # e terminava por H ou B esta era automaticamente uma constante. Agora se a 
* palavra tem estas caracteristicas eh realizado o teste isHexadecimal e isBinario que determina 
* se a palavra realmente eh H ou B.
*
* 12/05/03 e 13/03/2003
* -> Modifica a leitura dos campos de montagem na Arquitetura.
* -> Inserção da opção do registrador de mascara.
* -> Inserção das instruções da R11.
* -> Acesso as linhas das tabelas em Hexa.
*
* 15/05/03
* -> Inserção do breakpoint
* -> Conserto da instrução CMP
* -> Inserção de soma e subtração em binário.
*
* 19/05/03
* -> Conserto da função decimal_binario da classe Conversão, que não estava funcionando 
*    adequadamente para números negativos.
*
* 29/09/03
*	-> Conserto na instrução MOV
* 
* 30/09/03
* -> Conserto para trabalhar com instruções IGNORE no método selecionaInstrucao da classe
*    Arquitetura.
*
* 31/10/03
* -> Conserto no método substituiPseudoLabelMemoria(int nsubstitui) da classe Substitui
*    Esse método continha um erro que resultava na montagem incorreta da memória de dados.
*    Exemplo:   ASM     >> END1: DB A1,A1,#0003H
*                          A1: DB #AAAAH
*               memoria >> 0001  END1  AAAAAAAA
*
* 04/11/03
* -> Conserto do erro causado pela última alteração no método substituiPseudoLabelMemoria
* da classe Substitui.
*
* 13/11/03
* -> Modificação no código da instrução LDIM no arquivo de configuração da R12.
*/
public class Simulador 
{
	private Conversao conversao;
	private Memoria memoria,memoriadados;
	private Variaveis variaveis;
	private	Janela janela;
	private Arquivo arquivo;
	private Arquitetura arquitetura;
	private Execucao execucao;
	private int tamanhoMemoria;
	private int numeroVariaveis;
	private int tamanhoPalavra;
	private int nflag;
	private String posicaoSP,nomeArquitetura,movpilha,tipoArquitetura,mask;
	private String arqarq;

	public Simulador(String arquitetura)
	{
		arqarq=arquitetura;
	}
	public static void main(String s[])
	{
		String arquitetura;
		try
		{
			arquitetura=s[0];
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
			sim.implementa();
		}catch(ArrayIndexOutOfBoundsException e )
		{
			Configurador config=new Configurador();
		}
	}

	//Retorna as variaveis
	public Conversao getConversao(){return conversao;}
	public Memoria getMemoria(){return memoria;}
	public Memoria getMemoriaDados(){return memoriadados;}
	public Variaveis getVariaveis(){return variaveis;}
	public Janela getJanela(){return janela;}
	public Arquivo getArquivo(){return arquivo;}
	public Arquitetura getArquitetura(){return arquitetura;}
	public Execucao getExecucao(){return execucao;}
	public String getNomeArquitetura(){return nomeArquitetura;}
	public String getTipoArquitetura(){return tipoArquitetura;}
	public String  getMovPilha() {return movpilha;}
	public int getTamanhoMemoria(){return tamanhoMemoria;}
	public int getNumeroVariaveis(){return numeroVariaveis;}
	public String getPosicaoSP(){return posicaoSP;}
	public int getNflag(){return nflag;}
	public void setMascara(String mascara){janela.setMascara(mascara);}
	public String getMascara(){return janela.getMascara();}
	
	//Seta as variaveis
	public void setTamanhoMemoria(int t){tamanhoMemoria=t;}
	public void setNumeroVariaveis(int n){numeroVariaveis=n;}
	
	/**
	* Implementa o Simulador.
	*/
	public boolean implementa()
	{
		int nreg=0;
		boolean pc,ir,sp;
		String[]flags;
		String linha;
		StringTokenizer st;
		
		flags=new String[10];
		nflag=0;
		movpilha="";
		
		try
		{
			//Abre o arquivo para leitura.
			FileInputStream files=new FileInputStream(arqarq);
			DataInputStream d=new DataInputStream(files);
			BufferedReader dis = new BufferedReader(new InputStreamReader(files));
		  
		  //NOME ARQUITETURA
			do
			{
				linha=dis.readLine();
			}while(!linha.equalsIgnoreCase("$ARQ"));
			linha=dis.readLine();
			st=new StringTokenizer(linha);
			nomeArquitetura=st.nextToken();
			
			//ARQUITETURA
			do
			{
				linha=dis.readLine();
			}while(!linha.equalsIgnoreCase("$ARQUITETURA"));
			linha=dis.readLine();
			st=new StringTokenizer(linha);
			linha=st.nextToken();
			tipoArquitetura="";
			tipoArquitetura=linha;
				
			//TAMANHO MEMORIA
			do
			{
				linha=dis.readLine();
			}while(!linha.equalsIgnoreCase("$TAMMEMORIA"));
			linha=dis.readLine();
			st=new StringTokenizer(linha);
			linha=st.nextToken();
			tamanhoMemoria=Integer.valueOf(linha).intValue();

			//REGS
			do
			{
				linha=dis.readLine();
			}while(!linha.equalsIgnoreCase("$REG"));
			linha=dis.readLine();
		  	st=new StringTokenizer(linha);
			linha=st.nextToken();
			nreg = Integer.valueOf(linha).intValue();

			//MASK
			do
			{
				linha=dis.readLine();
			}while(!linha.equalsIgnoreCase("$MASK"));
			linha=dis.readLine();
			st=new StringTokenizer(linha);
			mask=st.nextToken();
			
			//FLAGS
			do
			{
				linha=dis.readLine();
			}while(!linha.equalsIgnoreCase("$FLAG"));
			linha=dis.readLine();
			while(!linha.equalsIgnoreCase("$ENDFLAG"))
			{
				st=new StringTokenizer(linha);
				linha=st.nextToken();
				flags[nflag]=linha;
				nflag++;
				linha=dis.readLine();
			}
			
			//PILHA
			do
			{
				linha=dis.readLine();
			}while(!linha.equalsIgnoreCase("$PILHA"));
			linha=dis.readLine();
			st=new StringTokenizer(linha);
			linha=st.nextToken();
			movpilha=linha;
			
			dis.close();
			
			//EXIBE
			/*
			System.out.println("nomearq="+nomeArquitetura);
			System.out.println("tipoarq="+tipoArquitetura);
			System.out.println("tammem="+tamanhoMemoria);
			System.out.println("nreg="+nreg);
			System.out.println("nflag="+nflag);
			for(int i=0;i<nflag;i++)
				System.out.println(flags[i]);
			System.out.println("movpilha="+movpilha);
			*/
		}
		catch(FileNotFoundException f)
		{
			JOptionPane.showMessageDialog(null,"A ARQUITETURA NÃO ENCONTRADA","MENSAGEM DE ERRO", JOptionPane.ERROR_MESSAGE);
			return false;
		}
		catch(Exception e)
		{
			JOptionPane.showMessageDialog(null,"ARQUIVO DE CONFIGURAÇÃO DA ARQUITETURA APRESENTA ERRO.","MENSAGEM DE ERRO", JOptionPane.ERROR_MESSAGE);
			return false;
		}
		
		conversao=new Conversao();
		
		numeroVariaveis=100; //Default	
		if(movpilha.equalsIgnoreCase("INC"))
			posicaoSP=conversao.decimal_hexa(tamanhoMemoria-((tamanhoMemoria/100)*10));
		else if(movpilha.equalsIgnoreCase("DEC"))
			posicaoSP=conversao.decimal_hexa(tamanhoMemoria-1);
			
	
		memoria=new Memoria(this);
		if(isHarvard())
			memoriadados=new Memoria(this);
		
		variaveis=new Variaveis(this);
		
		arquivo=new Arquivo(this,arqarq);
		
		arquitetura=new Arquitetura(this,arqarq);
		
		execucao=new Execucao(this,arquitetura);
		
		janela =new Janela(this,nreg,nflag,flags);
		return true;
	}
	
	/**
	* Verifica se a arquitetura é HARVARD
	*/
	public boolean isHarvard()
	{
		if(tipoArquitetura.equalsIgnoreCase("HARVARD"))
			return true;
		return false;
	}

	/**
	* Verifica se a arquitetura tem registrador de mascara.
	*/
	public boolean isMask()
	{
		if(mask.equalsIgnoreCase("YES"))
			return true;
		return false;
	}
	
	/**
	* Inicializa a janela do Simulador.
	*/
	public void inicializa()
	{
		janela.inicializa();	
		memoria.inicializa();
		variaveis.inicializa();
		if(isHarvard())
			memoriadados.inicializa();
	}
}