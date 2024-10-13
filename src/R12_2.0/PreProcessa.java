/*
* @(#)PreProcessa.java  1.0  27/11/2002
*	
* Aline Vieira de Mello
* Pontifícia Universidade Católica do Rio Grande do Sul - Faculdade de Informática
*/

import java.io.*;
import java.util.*;

/**
* <i>PreProcessa</i> 
*
*armazena os dados do arquivo a ser montado o codigo objeto.
*/
public class PreProcessa
{
	private int modo;
	private ArquiteturaMontador arquitetura;
	private Token token;
	private VetorLinha linhas;
	private VetorLinhaDado linhasDados,labels;
	private Linha linha;
	private LinhaDado linhaDado;
	
	public PreProcessa(int modo,ArquiteturaMontador arq,VetorLinha linhas,VetorLinhaDado linhasDados,VetorLinhaDado labels)
	{
		this.modo=modo;
		this.arquitetura=arq;
		token = new Token(arquitetura);
		this.linhas=linhas;
		this.linhasDados=linhasDados;
		this.labels=labels;
	}
	
	/**
	* Abre o arquivo que deseja montar e carrega as linhas do mesmo.
	*/
	public boolean executa(String arquivo)
	{
		MeuTokenizer st;
		String[] separadores={" ","\t","\n","\r","\f",",","(",")"};
		String[] separadoresDel={" ","\t","\n","\r","\f"};
		String[] separadoresFix={",","(",")"};
		InstrucaoMontador instrucao;
		Vector tokens,tipos;
		String lin,aux,tipo,ultimoTipo;
		int pc=0,pcAnterior;
		int pcDados=0,pcDadosAnterior;
		int nlinha=0,ntokens,nregs,nconst;
		boolean fimLinha;
			
		try
		{
			//Abre o arquivo para leitura.
			FileInputStream files=new FileInputStream(arquivo);
			BufferedReader br = new BufferedReader(new InputStreamReader(files));
		
			//Lendo a linha
			lin=br.readLine();
			nlinha++;
			while(lin!=null)
			{
				//Inicializa dados
				fimLinha=false;
				ultimoTipo="NOVALINHA";
				instrucao=null;
				nregs=0;
				nconst=0;
				pcAnterior=pc;
				pcDadosAnterior=pcDados;
				
				//Converte a linha para maiusculo.
				lin=lin.toUpperCase();
				//System.out.println("linha="+lin);
				
				//Inicializando o vetor de tokens e tipos
				tokens=new Vector();
				tipos=new Vector();
				//Separando a linha em tokens
				st=new MeuTokenizer(separadores,lin);
				//Cada token
				ntokens=st.countTokens();
				for(int i=0;i<ntokens && !fimLinha;i++)
				{
					pcDadosAnterior=pcDados;

					aux=st.nextToken();
					tipo=token.getTipo(aux);
					//System.out.println("tipo="+tipo);
				
					if(tipo.equalsIgnoreCase("COMENTARIO"))
					{
						//Se a linha possui uma instrucao e o número de registradores ou constantes
						//da linha não são igual aos da mesma ocorreu ERRO.
						if(instrucao!=null)
						{
							if(instrucao.getNRegistradores()!=nregs || instrucao.getNConstantes()!=nconst)
							{
								new Erro(modo).show("FORMATO_INSTRUCAO",nlinha,lin);
								return false;
							}
						}
						//Remove o comentario da linha
						int indice=lin.indexOf(";");
						if(indice!=0)
							lin=lin.substring(0,indice);
							
						fimLinha=true;
						ultimoTipo="NOVALINHA";
					}
					else if(tipo.equalsIgnoreCase("INSTRUCAO"))
					{
						instrucao=arquitetura.getInstrucao(aux);
						pc = pc + instrucao.getNPalavras();
					}
					else if(tipo.equalsIgnoreCase("REGISTRADOR"))
					{
						nregs++;	
					}
					else if(tipo.equalsIgnoreCase("CONSTANTE"))
					{

						if(ultimoTipo.equalsIgnoreCase("ORG"))
							pc=token.getValor(aux);
						else if(instrucao==null)
						{
							aux=token.getConstante(aux);
							if(ultimoTipo.equalsIgnoreCase("DB"))
							{
								if(!labels.addValor(aux))
								{
									new Erro(modo).show("ERRO_CONST",nlinha,aux);
									return false;
								}
								if(!linhasDados.addLinhaDado(labels,aux))
								{
									new Erro(modo).show("ERRO_CONST",nlinha,aux);
									return false;
								}

								if(arquitetura.isHarvard())
								{
									if(!labels.setEnd(aux,pcDadosAnterior))
									{
										new Erro(modo).show("ERRO_CONST",nlinha,aux);
										return false;	
									}
									
									linhasDados.setEnd(pcDadosAnterior);
									pcDados++;
								}
								else
									pc++;
							}
							else if(ultimoTipo.equalsIgnoreCase("CONSTANTE") 
								|| ultimoTipo.equalsIgnoreCase("PSEUDOLABEL"))
							{
								if(arquitetura.isHarvard())
									pcDados++;
								else
									pc++;
									
							}
						}
						else
							aux=token.getConstante(aux);
						nconst++;	
					}
					else if(tipo.equalsIgnoreCase("LABEL"))
					{
						//remove o ultimo byte do teoken que corresponde aos :
						aux=aux.substring(0,aux.length()-1);
						if(!labels.addLabel(aux,pcAnterior))
						{
							new Erro(modo).show("LABEL_DUPLO",nlinha,aux);
							return false;
						}
						//pc++;
					}
					else if(tipo.equalsIgnoreCase("PSEUDOLABEL"))
					{
						if(instrucao==null)
						{
							if(ultimoTipo.equalsIgnoreCase("DB"))
							{
								if(!linhasDados.addLinhaDado(labels))
								{
									new Erro(modo).show("ERRO_CONST",nlinha);
									return false;
								}
								if(arquitetura.isHarvard())
								{
									if(!labels.setEnd(aux,pcDadosAnterior))
									{
										new Erro(modo).show("ERRO_CONST",nlinha,aux);
										return false;	
									}
									linhasDados.setEnd(pcDadosAnterior);
									pcDados++;
								}
								else
									pc++;
							}
							else if(ultimoTipo.equalsIgnoreCase("CONSTANTE") 
								|| ultimoTipo.equalsIgnoreCase("PSEUDOLABEL"))
							{
								if(arquitetura.isHarvard())
									pcDados++;
								else
									pc++;
									
							}
						}
						else
						{
							if(instrucao.getNLabels()!=0)
								nconst++;	
						}
					}
					else if(tipo.equalsIgnoreCase("PSEUDO"))
					{
						if(!token.verificaPseudo(aux))
							new Erro(modo).show("ERRO_PSEUDO",nlinha,lin);
					}
										
					if(!verificaSintaxe(nlinha,aux,tipo,ultimoTipo))
						return false;
					
					if(!tipo.equalsIgnoreCase("COMENTARIO"))
					{
						tokens.add(aux);
						tipos.add(tipo);
						ultimoTipo=tipo;
					}
				}

				//Ao chegar ao final da linha insere a linha se a mesma não
				//estiver vazia, ou seja, se possuir pelo menos UM token
				if(!tokens.isEmpty())
				{
					//retira os espacos extras na linha
					lin=st.removeBrancos(separadoresDel,separadoresFix,lin);

					//Se a linha possui uma instrucao e o número de registradores ou constantes
					//da linha não são igual aos da mesma ocorreu ERRO.
					if(instrucao!=null)
					{
						if(instrucao.getNRegistradores()!=nregs || instrucao.getNConstantes()!=nconst)
						{
							new Erro(modo).show("FORMATO_INSTRUCAO",nlinha,lin);
							return false;
						}

						//instrucaoAssociada=instrucao.getInstrucaoMontadorAssociada();
						//codigo=
						linha=new Linha(lin,nlinha,pcAnterior,instrucao.getMneumonico(),tokens,tipos,instrucao.getCodigo());
					}
					else
						linha=new Linha(lin,nlinha,pcAnterior,tokens,tipos);

					linhas.add(linha);
				}
				//Lendo a proxima linha
				lin=br.readLine();
				nlinha++;
			}
			br.close();
			
			//Nao for harvard ajusta os enderecos dos dados que devem começar
			//no endereco que terminou as instrucoes
			//if(!arquitetura.isHarvard())
			//	linhasDados.ajustaEnderecoDados(pc);
			
			return true;
			//exibeInfoLinhas();
			//exibeLinhasDados();
		}
		catch(FileNotFoundException f)
		{
			new Erro(modo).show("FILE_NOTFOUND",arquivo);
			return false;
		}	
		catch(Exception e)
		{
			new Erro(modo).show("FILE_OPEN",arquivo);
			return false;
		}	
	}

	/**
	* Verifica se existe erro na sintaxe das linhas.
	*/
	public boolean verificaSintaxe(int nlinha,String token,String tipo,String ultimoTipo)
	{
		token=token.toUpperCase();
		tipo=tipo.toUpperCase();
		ultimoTipo=ultimoTipo.toUpperCase();
		if(tipo.equals("DB") && !ultimoTipo.equals("LABEL"))
		{
			new Erro(modo).show("ERRO_DB",nlinha,token);
			return false;
		}
		else if(tipo.equals("REGISTRADOR") && !ultimoTipo.equals("REGISTRADOR")
			&& !ultimoTipo.equals("INSTRUCAO") && !ultimoTipo.equals("CONSTANTE")
			&& !ultimoTipo.equals("LABEL") && !ultimoTipo.equals("PSEUDOLABEL"))
		{
		 	new Erro(modo).show("INST_INEXISTENTE",nlinha,token);
			return false;
		}
    else if(tipo.equals("LABEL") && !ultimoTipo.equals("NOVALINHA"))
		{
		 	new Erro(modo).show("ERRO_LAB",nlinha,token);
			return false;
		}
    else if(tipo.equals("CONSTANTE") && !ultimoTipo.equals("REGISTRADOR")
    	&& !ultimoTipo.equals("INSTRUCAO") && !ultimoTipo.equals("DB")
    	&& !ultimoTipo.equals("CONSTANTE") && !ultimoTipo.equals("PSEUDOLABEL")
			&& !ultimoTipo.equals("ORG"))
		{
		 	new Erro(modo).show("ERRO_CONST",nlinha,token);
			return false;
		}
		else if(tipo.equals("INST") && !ultimoTipo.equals("NOVALINHA")
			&& !ultimoTipo.equals("LABEL"))
		{
		 	new Erro(modo).show("ERRO_INST",nlinha,token);
			return false;
		}
		else if (tipo.equals("PSEUDOLABEL") && !ultimoTipo.equals("CONSTANTE")
			&& !ultimoTipo.equals("REGISTRADOR") && !ultimoTipo.equals("PSEUDOLABEL")
			&& !ultimoTipo.equals("DB") && !ultimoTipo.equals("INSTRUCAO"))
		{
		 	new Erro(modo).show("INST_INEXISTENTE",nlinha,token);
			return false;
		}
		return true;
	}
}