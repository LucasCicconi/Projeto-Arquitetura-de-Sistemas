                                                                                                                                                                                                        /*
* @(#)Arquitetura.java  1.0  30/03/2001
*	
* Aline Vieira de Mello
* Pontifícia Universidade Católica do Rio Grande do Sul - Faculdade de Informática
*/

import java.io.*;
import java.util.*;
import javax.swing.*;

/**
* <i>Arquitetura</i> transforma a arquitetura descrita na arquitetura padrão.
*/
public class Arquitetura
{
	private Simulador sim;
	private ArqSim arqSim;
	private ArqMont arqMont;
	private String instrucao;
	private String arqarq;
	private String c[];
	private String cF[];
	
	public Arquitetura(Simulador sim,String arqarq)
	{
		arqSim=new ArqSim();
		arqMont=new ArqMont();		
		this.sim=sim;
		this.arqarq=arqarq;
		c=new String[4];
		carregaArqSim();
		carregaArqMont();
	}
	
	/**
	* Retorna o apontador para Arquitetura padrão do Simulador.
	*/
	public ArqSim getArqSim() {return arqSim;}
	
	/**
	* Retorna o apontador para Arquitetura descrita.
	*/
	public ArqMont getArqMont() {return arqMont;}
			
	/**
	* Carrega Arquitetura do Simulador.
	*/
	public void carregaArqSim()
	{
		arqSim.inclui("ADD","OP1","RT","RS1","RS2");	
		arqSim.inclui("ADDI","OP1","RT","CT1_7_4","CT1_3_0");	
		arqSim.inclui("ADDSP","OP1","OP2","CT1_7_4","CT1_3_0");	
		arqSim.inclui("AND","OP1","RT","RS1","RS2");	
		arqSim.inclui("CMP","OP1","OP2","RS1","RS2");	
		arqSim.inclui("DEC","OP1","RT","OP2","RS1");
		arqSim.inclui("DECI","OP1","RT","CT1_3_0","OP2");
		arqSim.inclui("DIF","OP1","OP2","RS1","RS2");	
		arqSim.inclui("EQL","OP1","OP2","RS1","RS2");	
		arqSim.inclui("HALT","OP1","OP2","OP3","OP4");	
		arqSim.inclui("IGNORE","","","","");
		arqSim.inclui("INC","OP1","RT","OP2","RS1");
		arqSim.inclui("INCI","OP1","RT","CT1_3_0","OP2");
		arqSim.inclui("INF","OP1","OP2","RS1","RS2");	
		arqSim.inclui("INF2","OP1","OP2","RS1","RS2");	
		arqSim.inclui("JALR","OP1","CT1_11_8","CT1_7_4","CT1_3_0");
		arqSim.inclui("JC","OP1","OP2","OP3","RS1");
		arqSim.inclui("JCD","OP1","CT1_11_8","CT1_7_4","CT1_3_0");
		arqSim.inclui("JCMR","OP1","CT2_3_0","CT1_7_4","CT1_3_0");	
		arqSim.inclui("JCMRG","OP1","CT1_3_0","RT","OP2");	
		arqSim.inclui("JCMRGR","OP1","CT1_3_0","RT","OP2");	
		arqSim.inclui("JCR","OP1","OP2","OP3","RS1");
		arqSim.inclui("JMP","OP1","OP2","RS1","OP3");	
		arqSim.inclui("JMPC","OP1","OP2","RS1","OP3");	
		arqSim.inclui("JMPD","OP1","CT1_11_8","CT1_7_4","CT1_3_0");	
		arqSim.inclui("JMPD10","OP1","OP2-CT1_9_8","CT1_7_4","CT1_3_0");	
		arqSim.inclui("JMPD8","OP1","OP2","CT1_7_4","CT1_3_0");	
		arqSim.inclui("JMPDC","OP1","CT1_11_8","CT1_7_4","CT1_3_0");	
		arqSim.inclui("JMPDC10","OP1","OP2-CT1_9_8","CT1_7_4","CT1_3_0");	
		arqSim.inclui("JMPDC8","OP1","OP2","CT1_7_4","CT1_3_0");	
		arqSim.inclui("JMPDMASK","OP1","CT1_11_8","CT1_7_4","CT1_3_0");	
		arqSim.inclui("JMPR","OP1","OP2","RS1","OP3");	
		arqSim.inclui("JMPRC","OP1","OP2","RS1","OP3");	
		arqSim.inclui("JN","OP1","OP2","OP3","RS1");
		arqSim.inclui("JND","OP1","CT1_11_8","CT1_7_4","CT1_3_0");
		arqSim.inclui("JNR","OP1","OP2","OP3","RS1");

		arqSim.inclui("JPRGMASK","OP1","OP2","RS1","OP3");	
		arqSim.inclui("JPRGRMASK","OP1","OP2","RS1","OP3");	

		arqSim.inclui("JSR","OP1","OP2","RS1","OP3");	
		arqSim.inclui("JSRC","OP1","OP2","RS1","OP3");	
		arqSim.inclui("JSRD","OP1","CT1_11_8","CT1_7_4","CT1_3_0");
		arqSim.inclui("JSRDC","OP1","CT1_11_8","CT1_7_4","CT1_3_0");
		arqSim.inclui("JSRDMASK","OP1","CT1_11_8","CT1_7_4","CT1_3_0");
		arqSim.inclui("JSRDP","OP1","CT1_11_8","CT1_7_4","CT1_3_0");
		arqSim.inclui("JSRG","OP1","OP2","RS1","OP3");
		arqSim.inclui("JSRGMASK","OP1","OP2","RS1","OP3");
		arqSim.inclui("JSRGR","OP1","OP2","RS1","OP3");
		arqSim.inclui("JSRGRMASK","OP1","OP2","RS1","OP3");
		arqSim.inclui("JSRR","OP1","OP2","RS1","OP3");	
		arqSim.inclui("JSRRC","OP1","OP2","RS1","OP3");	
		arqSim.inclui("JV","OP1","OP2","OP3","RS1");
		arqSim.inclui("JVD","OP1","CT1_11_8","CT1_7_4","CT1_3_0");
		arqSim.inclui("JVR","OP1","OP2","OP3","RS1");
		arqSim.inclui("JZ","OP1","OP2","OP3","RS1");
		arqSim.inclui("JZD","OP1","CT1_11_8","CT1_7_4","CT1_3_0");
		arqSim.inclui("JZR","OP1","OP2","OP3","RS1");
		arqSim.inclui("LD","OP1","RT","OP2","RS2");	
		arqSim.inclui("LD2","OP1","RT","RS1","RS2");	
		arqSim.inclui("LDH","OP1","RT","CT1_7_4","CT1_3_0");	
		arqSim.inclui("LDL","OP1","RT","CT1_7_4","CT1_3_0");	
		arqSim.inclui("LDRI","OP1","RT","CT1_3_0","RS2");	
		arqSim.inclui("LDSP","OP1","OP2","RS1","OP3");
		arqSim.inclui("MOV","OP1","RT","OP2","RS1"); 
		arqSim.inclui("NEG","OP1","RT","OP2","RS1");
		arqSim.inclui("NOP","OP1","OP2","OP3","OP4");	
		arqSim.inclui("NOT","OP1","RT","OP2","RS1");
		arqSim.inclui("OR","OP1","RT","RS1","RS2");	
		arqSim.inclui("POP","OP1","RT","OP2","OP3");	
		arqSim.inclui("POPRG","OP1","OP2","RS1","OP3");
		arqSim.inclui("PUSH","OP1","OP2","RT","OP3");	
		arqSim.inclui("PUSHRG","OP1","OP2",	"RS1","OP3");
		arqSim.inclui("RL","OP1","RT","CT1_3_0","OP2");
		arqSim.inclui("RR","OP1","RT","CT1_3_0","OP2");
		arqSim.inclui("RLR","OP1","RT","RS1","OP2");
		arqSim.inclui("RRR","OP1","RT","RS1","OP2");
		arqSim.inclui("RLRC","OP1","RT","RS1","OP2");
		arqSim.inclui("RRRC","OP1","RT","RS1","OP2");
		arqSim.inclui("RSTFM","OP1","OP2","CT1_3_0","OP3");
		arqSim.inclui("RTS","OP1","OP2","OP3","OP4");	
		arqSim.inclui("RTSP","OP1","OP2","OP3","OP4");	
		arqSim.inclui("SKPEQ","OP1","OP2","RS1","RS2");
		arqSim.inclui("SKPEQI","OP1","OP2","CT1_3_0","RS2");
		arqSim.inclui("SKPNE","OP1","OP2","RS1","RS2");
		arqSim.inclui("SKPNEI","OP1","OP2","CT1_3_0","RS2");
		arqSim.inclui("SL","OP1","RT","OP2","RS2");	
		arqSim.inclui("SL0","OP1","RT","OP2","RS1");
		arqSim.inclui("SL1","OP1","RT","OP2","RS1");
		arqSim.inclui("SL0C","OP1","RT","CT1_3_0","OP2");
		arqSim.inclui("SL1C","OP1","RT","CT1_3_0","OP2");
		arqSim.inclui("SLT","OP1","RT","RS1","RS2");
		arqSim.inclui("SR","OP1","RT","OP2","RS2");	
		arqSim.inclui("SR0","OP1","RT","OP2","RS1");
		arqSim.inclui("SR1","OP1","RT","OP2","RS1");
		arqSim.inclui("SR0C","OP1","RT","CT1_3_0","OP2");
		arqSim.inclui("SR1C","OP1","RT","CT1_3_0","OP2");
		arqSim.inclui("ST","OP1","OP2","RS1","RS2");	
		arqSim.inclui("ST2","OP1","RT","RS1","RS2");	
		arqSim.inclui("STFM","OP1","OP2","CT1_3_0","OP3");
		arqSim.inclui("STMSK","OP1","CT1_7_4","CT1_3_0","OP2");
		arqSim.inclui("STRI","OP1","RT","CT1_3_0","RS2");	
		arqSim.inclui("STSP","OP1","OP2","RS1","OP3");
		arqSim.inclui("SUB","OP1","RT","RS1","RS2");	
		arqSim.inclui("SUBI","OP1","RT","CT1_7_4","CT1_3_0");	
		arqSim.inclui("SUP","OP1","OP2","RS1","RS2");	
		arqSim.inclui("SUP2","OP1","OP2","RS1","RS2");	
		arqSim.inclui("SWI","OP1","OP2","CT1_7_4","CT1_3_0");
		arqSim.inclui("SWIP","OP1","OP2","CT1_7_4","CT1_3_0");
		arqSim.inclui("XOR","OP1","RT","RS1","RS2");	
	}
	
	/**
	* Abre o arquivo que contém a descrição da arquitetura e carrega
	* as instruções.
	*/
	public void carregaArqMont()
	{
		String lixo1;
		StringTokenizer st;
		String linha;
		String[] c,camp,flag;
		String mneu,inst,flagdep;
		int nflag,nclock;
			
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
			}while(!linha.equalsIgnoreCase("$INST"));
			
			linha=dis.readLine();
		
			do
			{
				//Separando a linha em tokens			
				st=new StringTokenizer(linha);
				c=new String[4];
				camp=new String[4];
				flag=new String[10];
				
				//Lendo o Mneumonico e a instrucao associada
				mneu=st.nextToken();
				inst=st.nextToken();
								
				//Eliminando numero de registradores, constantes, labels e nclock.
				lixo1=st.nextToken();
				lixo1=st.nextToken();
				lixo1=st.nextToken();
				lixo1=st.nextToken();
				
				//Lendo o número de clocks
				nclock=Integer.valueOf(st.nextToken()).intValue();
							
				//Lendo o codigo da instrucao
				c[0]=st.nextToken();
				c[1]=st.nextToken();
				c[2]=st.nextToken();
				c[3]=st.nextToken();
			
				

				//Lendo os campos de montagem						
				camp[0]=st.nextToken();
				camp[1]=st.nextToken();
				camp[2]=st.nextToken();
				camp[3]=st.nextToken();
			
			/*
			//Eliminando o campos de escrita
				lixo1=st.nextToken();
				lixo1=st.nextToken();
				lixo1=st.nextToken();
				lixo1=st.nextToken();

			*/
				//Lendo o flag dependende da instrucao
				linha=dis.readLine();
				st=new StringTokenizer(linha);
				lixo1=st.nextToken();
				flagdep=st.nextToken();
			
				//Lendo o numero de flags da instrucao
				linha=dis.readLine();
				st=new StringTokenizer(linha);
				lixo1=st.nextToken();
				nflag=Integer.valueOf(st.nextToken()).intValue();
			
				//Lendo os flags da instrucao
				int i=0;
				do
				{			
					linha=dis.readLine();
					st=new StringTokenizer(linha);
					lixo1=st.nextToken();
					if(!lixo1.equalsIgnoreCase("$ENDFLAGS"))
					{
						flag[i]=lixo1;
						i++;
					}
				}while(!lixo1.equalsIgnoreCase("$ENDFLAGS"));

				arqMont.inclui(mneu,inst,nclock,c[0],c[1],c[2],c[3],camp[0],camp[1],camp[2],camp[3],flagdep,nflag,flag);
				linha=dis.readLine();
			}
			while(!linha.equalsIgnoreCase("$ENDINST"));
			dis.close();
		}
		catch(FileNotFoundException f)
		{
			JOptionPane.showMessageDialog(sim.getJanela(),"ARQUIVO QUE CONTEM A ARQUITETURA \n DESCRITA NÃO ENCONTRADO","MENSAGEM DE ERRO", JOptionPane.ERROR_MESSAGE);
		}	
		catch(Exception e)
		{
			JOptionPane.showMessageDialog(sim.getJanela(),"NÃO FOI POSSÍVEL ABRIR O ARQUIVO","MENSAGEM DE ERRO", JOptionPane.ERROR_MESSAGE);
		}	
		//arqMont.exibe();
	}

	/**
	* Seleciona a instrucao da nova Arquitetura.
	*/
	public NodoArqMont selecionaInstrucao(String[] c)
	{
		NodoArqMont auxMont;
		Vector vInst=new Vector();
		Vector vOpcode=new Vector();
		boolean continua;
		int nOpcode;

		/*String codigo1=c[1];
		if(c[0].equalsIgnoreCase("D") || c[0].equalsIgnoreCase("E"))
		{
			int dec;
			String bin;
			dec = sim.getConversao().hexa_decimal(c[1]);
			bin = sim.getConversao().decimal_binario(dec);
			bin = bin.substring(12,14); //captura os 2 bits + significativos do c[1]
			bin = bin.concat("00");
			dec = sim.getConversao().binario_decimal(bin);
			c[1] = sim.getConversao().decimal_hexa(dec);
			c[1] = c[1].substring(3,4);
		}
		*/
		auxMont=arqMont.getInicio();
		while(auxMont!=null)
		{
			continua=true;
			nOpcode=0;		

			if(auxMont.existeCampo("Op1"))
			{
				//System.out.println("Existe OP1 c = " + c);
				nOpcode++;
				if(!verificaCodigo(auxMont,"Op1",c))
					continua=false;
			}
			
			if(continua && auxMont.existeCampo("Op2"))
			{
				//System.out.println("Existe OP2 c = " + c);
				nOpcode++;
				if(!verificaCodigo(auxMont,"Op2",c))
					continua=false;
			}
									
			if(continua && auxMont.existeCampo("Op3"))
			{
				nOpcode++;
				if(!verificaCodigo(auxMont,"Op3",c))
					continua=false;
			}
			
			if(continua && auxMont.existeCampo("Op4"))
			{
				nOpcode++;
				if(!verificaCodigo(auxMont,"Op4",c))
					continua=false;
			}
				
			if(continua && (verificaCampos(auxMont) || auxMont.getInstrucao().equalsIgnoreCase("IGNORE")))
			{
				vInst.add(auxMont);
				vOpcode.add(""+nOpcode);
			}
			auxMont=auxMont.getProx();
		}

		//c[1] = codigo1;
	
		//Se alguma instrucao foi identificada, retorna aquela que dentre as identificadas
		//possuir o maior número de opcodes.
		if(!vOpcode.isEmpty())
		{
			int maiorOpcode=0;
			
			for(int i=0;i<vOpcode.size();i++)
			{
				if(Integer.valueOf((String)vOpcode.get(maiorOpcode)).intValue() < Integer.valueOf((String)vOpcode.get(i)).intValue())
					maiorOpcode=i;
			}
			return (NodoArqMont)vInst.get(maiorOpcode);
		}
		return null;
	}

	/**
	* Retorna TRUE se o código do campo é igual ao codigo passado por parâmetro.
	*/
	public boolean verificaCodigo(NodoArqMont auxMont,String campo,String [] codigo)
	{
		int indice;
		//captura o indice do campo
		indice=auxMont.getIndiceCampo(campo);
		//captura o campo
		campo=auxMont.getCampo(indice);

		//verifica se o campo é um misto entre opcode e constante
		if(auxMont.isCampoOpcode(campo) && auxMont.isCampoConstante(campo))
		{
			String cod,codBinario="",constanteBinaria="";
			int inicioOpcode,inicioConstante,separador1,separador2;
			//captura o codigo do indice do campo
			cod=auxMont.getCod(indice);

			//captura o indice inicial do campo referente ao opcode
			inicioOpcode=campo.toUpperCase().indexOf("OP");
			//captura o indice inicial do campo referente a constante
			inicioConstante=campo.toUpperCase().indexOf("CT");
			//verifica a ordem do opcode e da constante - CT-OP ou OP-CT
			if(inicioOpcode<inicioConstante)
			{
				//captura o indice do codigo que possui a primeira ocorrência do símbolo "_"
				//após o inicioOpcode
				separador1=cod.indexOf("_",inicioOpcode);
				//captura o indice do codigo que possui a primeira ocorrência do símbolo "-"
				//após o separador1
				separador2=cod.indexOf("-",separador1+1);
			}
			else
			{
				//captura o indice do codigo que possui a primeira ocorrência do símbolo "_"
				//após o inicioOpcode
				separador1=cod.indexOf("_",inicioOpcode);
				//se o opcode vem após a constante o separador 2 é o fim do cod
				separador2=cod.length();
			}
			//captura o valor do opcode
			cod=cod.substring(separador1+1,separador2);	
			//converte o codigo para binário de 2 bytes
			codBinario=new Conversao().hexa_binario(cod,2);
			//faz a comparação do codigo com todas as possibilidades de opcode
			for(int i=0;i<4;i++)
			{
				//converte i para binario
				constanteBinaria=new Conversao().decimal_binario(i,2);
				//concatena conforme a ordem de OP_CT ou CT_OP
				if(inicioOpcode<inicioConstante)
					cod=codBinario+constanteBinaria;
				else
					cod=constanteBinaria+codBinario;
				//converte o cod para base hexadecimla de 1 byte
				cod=new Conversao().binario_hexa(cod,1);
				if(cod.equalsIgnoreCase(codigo[indice]))
					return true;
			}	
			return false;
		}
		else if(auxMont.getCod(indice).equalsIgnoreCase(codigo[indice]))
			return true;
		return false;
	}

	/**
	* Retorna TRUE se o código do campo é igual ao codigo passado por parâmetro.
	*/
	public boolean verificaCampos(NodoArqMont auxMont)
	{
		//Captura o nodo da instrucao no simulador
		NodoArqSim auxSim=arqSim.procuraInstrucao(auxMont.getInstrucao());
		
		if(auxSim.existeCampo(auxMont.getCampo1()) &&	auxSim.existeCampo(auxMont.getCampo2())
		&& auxSim.existeCampo(auxMont.getCampo3()) &&	auxSim.existeCampo(auxMont.getCampo4()))
				return true;
				
		return false;
	}

	/**
	* Retorna TRUE se o código do campo é igual ao codigo passado por parâmetro na
	* posição conforma a instrução do simulador.
	*/
	/*
	public boolean verificaCodigo(NodoArqSim auxSim,NodoArqMont auxMont,String campo,String [] codigo)
	{
		int indiceSim,indiceMont;
		
		//procura nos campos da instrucao do montador o campo
		//Caso existir, verifica se é igual ao codigo do montador neste campo.
		indiceMont=auxMont.procura(campo);
		indiceSim=indiceMont;

		if(auxMont.getCod(indiceMont).equalsIgnoreCase(codigo[indiceSim]))
			return true;
		return false;
	}
	*/
	/**
	* Posiciona o codigo conforme os campos da instrucao no simulador.
	*/
	public String[] posicionaCodigo(NodoArqMont instM,String[] codigo)
	{
		if(!instM.getInstrucao().equalsIgnoreCase("IGNORE"))
		{
			NodoArqSim instS = arqSim.procuraInstrucao(instM.getInstrucao());	
			String[] cod = new String[4];
			for(int i=0;i<4;i++)
				cod[instS.procura(instM.getCampo(i))] = codigo[i];
			return cod;
		}
		return codigo;
	}
	
}