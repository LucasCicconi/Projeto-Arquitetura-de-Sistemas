/*
* @(#)Token 1.0  27/11/2002
*	
* Aline Vieira de Mello
* Pontifícia Universidade Católica do Rio Grande do Sul - Faculdade de Informática
*/

import java.util.*;

/**
* <i>Token</i> contem as operações sobre um token.
*/
public class Token
{
	private ArquiteturaMontador arquitetura;
	private boolean pseudo[]={false,false,false,false};	

	public Token(ArquiteturaMontador arquitetura)
	{
		this.arquitetura=arquitetura;
	}

	/**
	* Retorna o tipo do token.
	*/
	public String getTipo(String token)
	{
		InstrucaoMontador inst=arquitetura.getInstrucao(token);
		if(isComentario(token))
		{
	    //System.out.print("Comentario "+token);
			return "COMENTARIO";
		}
		if(inst != null)
		{
	    //System.out.print("InstrucaoMontador "+token);
			return "INSTRUCAO";
		}
		if(isRegistrador(token))
		{
    	//System.out.print("registrador "+token);
			return "REGISTRADOR";
		}
		if(isLabel(token))
		{
	    //System.out.print("label "+token);
			return "LABEL";
		}
		if(isOrg(token))
		{
	    //System.out.print("ORG "+token);
			return "ORG";
		}
		if(isPseudo(token))
		{
	    //System.out.println("Pseudo "+token);
			return "PSEUDO";
		}
		if(isDb(token))
		{
	    //System.out.print("DB "+token);
			return "DB";
		}
		if(isConstante(token))
		{		
			//System.out.print("Constante "+token);
			return "CONSTANTE";
		}
		//System.out.print("PseudoLabel "+token);
		return "PSEUDOLABEL";
	}

	/**
	* Verifica se o token possui R mais numero, que deve
	* variar entre 0 e o numero de registradores.
	*/
	public boolean isRegistrador(String token)
	{
		String registrador;
		Conversao conversao=new Conversao();
		for(int i=0;i<arquitetura.getNRegistradores();i++)
		{
	  	registrador="R"+i;
	    if(registrador.equalsIgnoreCase(token))
				return true;
		}
	  return false;
	}

	/**
	* Verifica se o token possui dois pontos(:), identificando-a 
	* como um label.
	*/
	public boolean isLabel(String token)
	{
		if(token.substring(token.length()-1,token.length()).equals(":"))
			return true;
		return false;
	}

	/**
	* Verifica se o token eh igual .ORG
	*/
	public boolean isOrg(String token)
	{
		if(token.equalsIgnoreCase(".ORG"))
			return true;
		return false;
	}

	/**
	* Verifica se o token eh igual a DB(Define Byte)
	*/
	public boolean isDb(String token)
	{
		if(token.equalsIgnoreCase("DB"))
			return true;
		return false;
	}

	/**
	* Verifica se o primeiro byte do token eh igual a ponto-e-vírgula (;)
	*/
	public boolean isComentario(String token)
	{
		if(token.substring(0,1).equalsIgnoreCase(";"))
			return true;
		return false;
	}

	/**
	* Verifica se o primeiro byte do token eh igual a cerquilha (#)
	*/
	public boolean isConstante(String token)
	{
		if(token.substring(0,1).equalsIgnoreCase("#"))
	    	return verificaConstante(token);
		return false;
	}

	/**
	* Verifica se o primeiro byte do token eh igual a ponto final (.)
	*/
	public boolean isPseudo(String token)
	{
		if(token.substring(0,1).equalsIgnoreCase("."))
			return true;
		return false;
	}

	/**
	* Verifica se #token selecionado como constante, realmente é.
	*	Verifica se o token corresponde a um valor na base decimal, binária
	* ou hexadecimal. Se o token não for um valor então o token
	* não é uma CONSTANTE e sim um PSEUDOLABEL.
	*/
	public boolean verificaConstante(String token)
	{
		//Se retornou -1 indica que o token não pode ser convertido
		//para Decimal então Não é valor 
		if(getValor(token)==-1) 
			return false;
		return true;
	}
	
	/**
	* Retorna a constante sem o cerquilha (#) e convertida para hexadecimal. 
	*/
	public String getConstante(String valor)
	{
	  int decimal = getValor(valor);
    valor = new Conversao().decimal_hexa(decimal);
    return valor;
	}
		
	/*
	*	Retira o primeiro byte do token que corresponde a cerquilha (#) e 
	* verifica se o restante corresponde a um número na base decimal, binária
	* ou hexadecimal. Se o restante do token não for um numero então o token
	* não é uma CONSTANTE e sim um PSEUDOLABEL.
	* Por Exemplo:
	* #0F1H - é uma constante, porque corresponde a um valor na base hexadecimal
	* #1010B - é uma constante, porque corresponde a um valor na base binária
	* #50 - é uma constante, porque corresponde a um valor na base decimal
	* #LOOP - NÃO é uma constante, porque não corresponde a um valor em qualquer
	*         uma das bases e sim a um PSEUDOLABEL.
	*/
	public int getValor(String token)
	{
		Conversao conversao=new Conversao();
		int decimal;
		
		//Remove o primeiro byte correspondente a #
		token=token.substring(1,token.length());
		
		//Verifica se o último byte é igual a H indicando que PODE ser
		//um valor na base hexadecimal
		if(token.substring(token.length()-1,token.length()).equalsIgnoreCase("H"))
		{
			//Remove o ultimo byte correspondente a H
			token=token.substring(0,token.length()-1);
			if(conversao.isHexadecimal(token))
				decimal=conversao.hexa_decimal(token);
			else
				decimal=-1;	
		}
		//Verifica se o último byte é igual a B indicando que PODE ser
		//um valor na base binária
		else if(token.substring(token.length()-1,token.length()).equalsIgnoreCase("B"))
		{
			//Remove o ultimo byte correspondente a B
			token=token.substring(0,token.length()-1);
			if(conversao.isBinario(token))
				decimal=conversao.binario_decimal(token);
			else
				decimal=-1;	
		}
		else
		{
			try
			{
				decimal =Integer.valueOf(token).intValue();
			}catch(NumberFormatException e) {decimal=-1;}
		}
		return decimal;
	}

	/**
	* Verifica se o pseudo codigo foi declarado corretamente.
	*/
	public boolean verificaPseudo(String token)
	{
		if(token.equalsIgnoreCase(".CODE"))
		{
			if(pseudo[0])
				return false;
			pseudo[0]=true;
		}
		else if(token.equalsIgnoreCase(".ENDCODE"))
		{
			if(!pseudo[0])
				return false;
			else if(pseudo[1])
				return false;
			pseudo[1]=true;
		}
		else if(token.equalsIgnoreCase(".DATA"))
		{
			if(pseudo[2])
				return false;
			pseudo[2]=true;
		}
		else if(token.equalsIgnoreCase(".ENDDATA"))
		{
			if(!pseudo[2])
				return false;
			else if(pseudo[3])
				return false;
			pseudo[3]=true;
		}
		else
			return false;
		return true;
	}
}