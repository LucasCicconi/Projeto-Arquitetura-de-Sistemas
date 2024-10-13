/*
* @(#)Linha.java  1.0  27/11/2002
*	
* Aline Vieira de Mello
* Pontifícia Universidade Católica do Rio Grande do Sul - Faculdade de Informática
*/

import java.util.*;

/**
* <i>Linha</i> armazena os dados referentes a uma linha do arquivo que
* deseja-se montar.
*/
public class Linha
{
	private int nroLinha;
	private String linha;
	private String endereco;
	private String nomeInstrucao;
	private Vector tokens=new Vector();
	private Vector tipos=new Vector();
	private String[] codigo=new String[4];

	public Linha(String lin,int nroLinha,int endereco,String nomeInstrucao,Vector tok,Vector tip,String[] codigo)
	{
		this.linha=lin;
		this.nroLinha=nroLinha;
		this.endereco=new Conversao().decimal_hexa(endereco);
		this.nomeInstrucao=nomeInstrucao;
		if(tok==null)
			tokens=new Vector();
		else
			tokens=tok;
		if(tip==null)
			tipos=new Vector();
		else
			tipos=tip;
		for(int i=0;i<codigo.length;i++)
			this.codigo[i]=codigo[i];
	}
	
	public Linha(String lin,int nroLinha,int endereco,Vector tok,Vector tip)
	{
		this.linha=lin;
		this.nroLinha=nroLinha;
		this.endereco=new Conversao().decimal_hexa(endereco);
		this.nomeInstrucao=null;
		if(tok==null)
			tokens=new Vector();
		else
			tokens=tok;
		if(tip==null)
			tipos=new Vector();
		else
			tipos=tip;
		for(int i=0;i<codigo.length;i++)
			this.codigo[i]="0";
	}
	
	/**
	* Retorna a linha.
	*/
	public String getLinha(){return linha;}

	/**
	* Retorna o número da linha.
	*/
	public int getNroLinha(){return nroLinha;}

	/**
	* Retorna o endereco da linha.
	*/
	public String getEndereco(){return endereco;}

	/**
	* Retorna o endereco da linha em decimal.
	*/
	public int getEnderecoDecimal()
	{
		int decimal=new Conversao().hexa_decimal(endereco);
		return decimal;
	}

	/**
	* Retorna a instrucao correspondente da linha.
	*/
	public String getNomeInstrucao(){return nomeInstrucao;}

	/**
	* Retorna o código da instrucao na posição informada pelo indice.
	*/
	public String getCodigo(int indice){return codigo[indice];}

	/**
	* Retorna o código da instrucao na posição informada pelo indice.
	*/
	public String getCodigo()
	{
		return (codigo[0]+codigo[1]+codigo[2]+codigo[3]);
	}

	/**
	* Retorna o token informado pelo indice.
	*/
	public String getToken(int indice)
	{
		if(indice<tokens.size())
			return (String)tokens.get(indice);
		return "";		
	}

	/**
	* Retorna o tipo do token informado pelo indice.
	*/
	public String getTipoToken(int indice)
	{
		if(indice<tipos.size())
			return (String)tipos.get(indice);
		return "";		
	}

	/**
	* Retorna o vetor que contem os tipos dos tokens da linha.
	*/
	public Vector getTiposTokens(){return tipos;}
	

	/**
	* Retorna o ultimo tipo de token armazenado.
	*/
	public String getUltimoTipoToken()
	{
		if(!tipos.isEmpty())
			return (String)tipos.lastElement();
		return "NOVALINHA";
	}

	/**
	* Retorna o indice que contem o tipo informado.
	*/
	public int getIndiceTipoToken(String tipo)
	{
		for(int i=0;i<tipos.size();i++)
		{
			if(((String)tipos.get(i)).equalsIgnoreCase(tipo))
				return i;	
		}	
		return tipos.size();
	}

	/**
	* Retorna o indice do codigo que contem o codigo informado.
	*/
	public int getIndiceCodigo(String cod)
	{
		for(int i=0;i<codigo.length;i++)
		{
			if(codigo[i].toUpperCase().indexOf(cod)!=-1)
				return i;	
		}	
		return -1;
	}

	/**
	* Captura o pseudo codigo do campo informado.
	* RT -3
	* RS1 -1 
	* RS2 -2
	*/
	public String getPseudoCodigo(String camp)
	{
		if(camp.equalsIgnoreCase("RS1"))
			return "-1";
		else if(camp.equalsIgnoreCase("RS2"))
			return "-2";
		else if(camp.equalsIgnoreCase("RT"))
			return "-3";
		else if(isCampoConstante(camp) && isCampoOpcode(camp))
		{
			int inicioOpcode,inicioConstante,separador1,separador2,indice;
			String opcode,codigo,pseudoCodigo;
			
			//verifica se o campo é um misto entre constante e opcode e em que ordem
			//captura o indice inicial do campo que é referente ao opcode
			inicioOpcode=camp.toUpperCase().indexOf("OP");
			//captura o indice inicial do campo que é referente a constante
			inicioConstante=camp.toUpperCase().indexOf("CT");
			//captura a primeira ocorrência do símbolo "-" que divide
			separador1=camp.indexOf("-");
			//verifica a ordem se é OP-CT ou CT-OP
			if(inicioOpcode<inicioConstante)
			{
				//OPCODE
				//captura o opcode
				opcode=camp.substring(inicioOpcode,separador1);
				//encontra o codigo que possui o opcode
				indice=getIndiceCodigo(opcode);
				if(indice==-1)
					return "null";
				
				//captura o codigo que possui o opcode
				codigo=getCodigo(indice);
				//captura o indice inicial do codigo que é referente ao opcode
				inicioOpcode=codigo.toUpperCase().indexOf("OP");
				//captura a primeira ocorrência do símbolo "-" após o inicioOpcode
				separador2=codigo.indexOf("-",inicioOpcode);
				//inclui no pseudoCodigo a parte referente ao opcode 
				pseudoCodigo=codigo.substring(inicioOpcode,separador2);
				
				//CONSTANTE
				//inclui no pseudoCodigo a parte referente a constante 
				pseudoCodigo=pseudoCodigo+camp.substring(separador1,camp.length());
			}
			else
			{
				//CONSTANTE
				//inclui no pseudoCodigo a parte referente a constante 
				pseudoCodigo=camp.substring(0,separador1);

				//OPCODE
				//captura o opcode
				opcode=camp.substring(inicioOpcode,camp.length());
				//encontra o codigo que possui o opcode
				indice=getIndiceCodigo(opcode);
				//captura o codigo que possui o opcode
				codigo=getCodigo(indice);
				//captura o indice inicial do codigo que é referente ao opcode
				inicioOpcode=codigo.toUpperCase().indexOf("OP");
				//captura a primeira ocorrência do símbolo "-" após o inicioOpcode
				separador2=codigo.indexOf("-",inicioOpcode);
				//inclui no pseudoCodigo a parte referente ao opcode 
				pseudoCodigo=pseudoCodigo+"-"+codigo.substring(inicioOpcode,separador2);
			}
			return pseudoCodigo;
		}	
		return camp;
	}
	
	/**
	* Captura o indice do codigo que possui o pseudo codigo.
	*/
	public int getIndicePseudoCodigo(String pseudoCodigo)
	{
		for(int i=0;i<codigo.length;i++)
		{
			if(codigo[i].trim().equalsIgnoreCase(pseudoCodigo))
				return i;
		}
		return -1;
	}

	/**
	* Atribui o endereco informado a linha.
	*/
	public void setEndereco(int endereco)
	{
		this.endereco=new Conversao().decimal_hexa(endereco);
	}

	/**
	* Atribui o token informado ao vetor de tokens na posicao informada pelo indice.
	*/
	public void setToken(int indice,String token){tokens.set(indice,token);}

	/**
	* Atribui o tipo informado ao vetor de tipos de tokens na posicao informada pelo indice.
	*/
	public void setTipoToken(int indice,String tipo){tipos.set(indice,tipo);}
	
	/**
	* Retorna o código da instrucao na posição informada pelo indice.
	*/
	public void setCodigo(int indice,String cod){codigo[indice]=cod;}

	/**
	* Verifica se existe na linha o tipo de token informado.
	*/
	public boolean existeTipoToken(String tipo)
	{
		for(int i=0;i<tipos.size();i++)
		{
			if(((String)tipos.get(i)).equalsIgnoreCase(tipo))
				return true;	
		}	
		return false;
	}

	/**
	* Verifica se o existe o pseudoCodigo informado.
	*/
	public boolean existePseudoCodigo(String pseudoCodigo)
	{
		for(int i=0;i<codigo.length;i++)
		{
			if(codigo[i].equalsIgnoreCase(pseudoCodigo))
				return true;
		}
		return false;
	}

	/**
	* Verifica se tipo token do indice informado é igual ao tipo informado.
	*/
	public boolean isTipoToken(int indice,String tipo)
	{
		if(((String)tipos.get(indice)).equalsIgnoreCase(tipo))
			return true;	
		return false;
	}

	/**
	* Verifica se o codigo do indice informado nao é pseudo codigo, ou seja,
	* indice possui seu codigo definitivo.
	*/
	public boolean isCodigoResolvido(int indice)
	{
		if(!isPseudoCodigo(codigo[indice]))	
			return true;
		return false;
	}

	/**
	* Verifica se os codigos da constante n for todos resolvido
	*/
	public boolean isCodigoConstanteResolvido(int nConstante)
	{
		//Verifica se existe dentro do codigo a substring "CT1" onde 1 representa o
		//nConstante informada por parâmetro. Se um dos códigos contiver a substring
		//o código referente a esta constante nao esta resolvido.
		for(int i=0;i<codigo.length;i++)
		{
			if(codigo[i].toUpperCase().indexOf("CT"+nConstante)!=-1)
				return false;
		}
		return true;
	}

	/**
	* Verifica se o codigo informado é pseudo codigo, ou seja,
	* indice possui codigo não resolvido, logo tem o sinal (-) no primeiro byte.
	*/
	public boolean isPseudoCodigo(String cod)
	{
		if(cod.startsWith("-"))	
			return true;
		return false;
	}

	/**
	* Verifica se a linha possui instrucao.
	*/
	public boolean isInstrucao()
	{
		if(nomeInstrucao!=null)
			return true;
		return false;	
	}

	/**
	* Verifica se o campo informado é uma constante.
	*/
	public boolean isCampoConstante(String campo)
	{
		if(campo.toUpperCase().indexOf("CT")!=-1)
			return true;
		return false;		
	}

	/**
	* Verifica se o campo informado é uma constante.
	*/
	public boolean isCampoConstante(int nConstante,String campo)
	{
		if(campo.toUpperCase().indexOf("CT"+nConstante)!=-1)
			return true;
		return false;		
	}

	/**
	* Verifica se o campo informado é uma opcode.
	*/
	public boolean isCampoOpcode(String campo)
	{
		if(campo.indexOf("OP")!=-1)
			return true;
		else if(new Conversao().isHexa(campo))
			return true;
		return false;		
	}

	/**
	* Verifica se o campo informado é um registrador.
	*/
	public boolean isCampoRegistrador(String campo)
	{
		if(campo.startsWith("R"))
			return true;
		return false;		
	}

	/**
	* Exibe o número e a linha.
	*/
	public void exibeLinha()
	{
		System.out.println(nroLinha+" "+endereco+" "+linha);
	}
	
	/**
	* Exibe os tokens da linha.
	*/
	public void exibeTokens()
	{
		System.out.print("Tokens -");
		for(int i=0;i<tokens.size();i++)
			System.out.print((String)tokens.get(i)+"-");
		System.out.println();
	}

	/**
	* Exibe o codigo da linha.
	*/
	public void exibeCodigo()
	{
		if(nomeInstrucao!=null)
		{
			for(int i=0;i<4;i++)
				System.out.print(codigo[i]);
			System.out.println(" "+nomeInstrucao);
		}
	}

	/**
	* Exibe os dados da linha.
	*/
	public void exibe()
	{
		System.out.print(nroLinha+" "+endereco+" "+linha+" "+nomeInstrucao);
		System.out.print("Tokens -");
		for(int i=0;i<tokens.size();i++)
			System.out.print((String)tokens.get(i)+"-");
		System.out.print(" Tipos Tokens -");
		for(int i=0;i<tipos.size();i++)
			System.out.print((String)tipos.get(i)+"-");
		System.out.print(" Codigo - ");
		for(int i=0;i<4;i++)
			System.out.print(codigo[i]);
		System.out.println();
	}
}