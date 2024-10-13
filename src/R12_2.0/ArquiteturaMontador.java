/*
* @(#)ArquiteturaMontador.java  1.0  27/11/2002
*	
* Aline Vieira de Mello
* Pontifícia Universidade Católica do Rio Grande do Sul - Faculdade de Informática
*/

import java.io.*;
import java.util.*;

/**
* <i>ArquiteturaMontador</i> armazena os dados do arquivo de configuração da arquitetura.
*/
public class ArquiteturaMontador
{
	private int modo;
	private String nome;
	private boolean harvard;
	private int tamanhoMemoria;
	private int nRegistradores;
	private Vector instrucoes;
	private InstrucaoMontador instrucao;
	
	public ArquiteturaMontador(int modo)
	{
		this.modo=modo;
		harvard=false;
		instrucoes = new Vector();
	}
	
	/**
	* Abre o arquivo que contém a descrição da arquitetura e carrega
	* as instruções.
	*/
	public boolean configura(String arquivo)
	{
		StringTokenizer st;
		String linha,aux;
		String mneu,inst;
		int nlinha,nreg,nconst,nlab,npal,nciclo;
		
		nlinha=0;
		try
		{
			//Abre o arquivo para leitura.
			FileInputStream files=new FileInputStream(arquivo);
			BufferedReader br = new BufferedReader(new InputStreamReader(files));

		  //ENCONTRANDO O NOME DA ARQUITETURA
			do
			{
				linha=br.readLine();
				nlinha++;
				
			}while(!linha.equalsIgnoreCase("$ARQ"));
			//Lendo e separando a linha em tokens			
			linha=br.readLine();
			nlinha++;
			st=new StringTokenizer(linha);
			//Lendo o nome da ArquiteturaMontador
			nome=st.nextToken();
	
		  //ENCONTRANDO O TIPO ARQUITETURA
			do
			{
				linha=br.readLine();
				nlinha++;
			}while(!linha.equalsIgnoreCase("$ARQUITETURA"));
			//Lendo e separando a linha em tokens			
			linha=br.readLine();
			nlinha++;
			st=new StringTokenizer(linha);
			//Lendo o Tipo da ArquiteturaMontador
			aux=st.nextToken();
			if(aux.equalsIgnoreCase("HARVARD"))
				harvard=true;

		  //ENCONTRANDO O TAMANHO DA MEMORIA
			do
			{
				linha=br.readLine();
				nlinha++;
			}while(!linha.equalsIgnoreCase("$TAMMEMORIA"));
			//Lendo e separando a linha em tokens			
			linha=br.readLine();
			nlinha++;
			st=new StringTokenizer(linha);
			//Lendo o tamanho da memoria da ArquiteturaMontador
			tamanhoMemoria=Integer.valueOf(st.nextToken()).intValue();


		  //ENCONTRANDO O NUMERO DE REGISTRADORES
			do
			{
				linha=br.readLine();
				nlinha++;
			}while(!linha.equalsIgnoreCase("$REG"));
			//Lendo e separando a linha em tokens			
			linha=br.readLine();
			nlinha++;
			st=new StringTokenizer(linha);
			//Lendo o número de registradores da ArquiteturaMontador
			nRegistradores=Integer.valueOf(st.nextToken()).intValue();

			//BUSCA O INICIO DA RELAÇÃO DE INSTRUÇÕES
			do
			{
				linha=br.readLine();
				nlinha++;
			}while(!linha.equalsIgnoreCase("$INST"));
			
			//Lendo a linha
			linha=br.readLine();
			nlinha++;

			do
			{
				String[] codigo=new String[4];
				String[] campoEscrita=new String[4];

				//Separando a linha em tokens			
				st=new StringTokenizer(linha);
				//Lendo o mneumonico
				mneu=st.nextToken();
				//Lendo a instrucao associada
				inst=st.nextToken();
				//Lendo o número de registradores
				nreg=Integer.valueOf(st.nextToken()).intValue();
				//Lendo o número de constantes
				nconst=Integer.valueOf(st.nextToken()).intValue();
				//Lendo o número de labels
				nlab=Integer.valueOf(st.nextToken()).intValue();
				//Lendo o número de palavras
				npal=Integer.valueOf(st.nextToken()).intValue();
				//Lendo o número de ciclos
				nciclo=Integer.valueOf(st.nextToken()).intValue();

				//Lendo o codigo da instrucao
				codigo[0]=st.nextToken();
				codigo[1]=st.nextToken();
				codigo[2]=st.nextToken();
				codigo[3]=st.nextToken();

				//Lendo os campos de escrita
				campoEscrita[0]=st.nextToken();
				campoEscrita[1]=st.nextToken();
				campoEscrita[2]=st.nextToken();
				campoEscrita[3]=st.nextToken();
			
			  //BUSCA O FIM DESTA INSTRUÇÃO
				do
				{
					//Lendo e separando a linha em tokens			
					linha=br.readLine();
					nlinha++;
					st=new StringTokenizer(linha);
					aux=st.nextToken();
				}while(!aux.equalsIgnoreCase("$ENDFLAGS"));
			
				instrucao = new InstrucaoMontador(mneu,inst,nreg,nconst,nlab,npal,nciclo,codigo,campoEscrita);
				instrucoes.add(instrucao);
				
				//Lendo e separando a linha em tokens			
				linha=br.readLine();
				nlinha++;
			}while(!linha.equalsIgnoreCase("$ENDINST"));
			br.close();
			return true;
			//exibe();
		}
		catch(FileNotFoundException f)
		{
			new Erro(modo).show("FILE_NOTFOUND",arquivo);
			return false;
		}	
		catch(Exception e)
		{
			new Erro(modo).show("ERRO_ARQ",nlinha);
			return false;
		}	
	}
	
	/**
	* Verifica se a arquitetura é HARVARD, se for retorna TRUE senão FALSE.
	*/
	public boolean isHarvard() {return harvard;}

	/**
	* Retorna a instrucao que tem o mneumonico informado, se não existir retorna null.
	*/
	public InstrucaoMontador getInstrucao(String mneumonico)
	{
		for(int i=0;i<instrucoes.size();i++)
		{
			//Retorna o instrucao do vetor de instrucoes que tem o mneumonico igual ao informado
			InstrucaoMontador inst=(InstrucaoMontador)instrucoes.get(i);
			if(inst.getMneumonico().equalsIgnoreCase(mneumonico))
				return inst;	
		}
		return null;
	}

	/**
	* Retorna o tamanho da memória da arquitetura.
	*/
	public int getTamanhoMemoria() {return tamanhoMemoria;}

	/**
	* Retorna o número de registradores da arquitetura.
	*/
	public int getNRegistradores() {return nRegistradores;}

	/**
	* Exibe as instruções da arquitetura.
	*/
	public void exibe()
	{
		System.out.print(nome+" ");
		if(isHarvard())
			System.out.print("HARVARD ");
		else
			System.out.print("VON NEUMAN ");
		System.out.println(tamanhoMemoria+" "+nRegistradores);
		exibeInstrucoes();
	}

	/**
	* Exibe as instruções da arquitetura.
	*/
	public void exibeInstrucoes()
	{
		for(int i=0;i<instrucoes.size();i++)
		{
			instrucao=(InstrucaoMontador)instrucoes.get(i);
			instrucao.exibe();	
		}
	}
}