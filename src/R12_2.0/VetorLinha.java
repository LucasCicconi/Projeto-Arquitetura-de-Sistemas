/*
* @(#)VetorLinha.java  1.0  27/11/2002
*	
* Aline Vieira de Mello
* Pontifícia Universidade Católica do Rio Grande do Sul - Faculdade de Informática
*/

import java.util.*;

/**
* <i>VetorLinha</i> contem a armazena as linhas de dados
*/
public class VetorLinha extends Vector
{
	//private Vector linhas;
	
	public VetorLinha()
	{
		super();
//		linhas=new Vector();
	}
	
	/**
	* Exibe as linhas do arquivo.
	*/
	public void exibeLinhas()
	{
		System.out.println("LINHAS ARQUIVO.ASM");
		
		for(int i=0;i<super.size();i++)
		{
			Linha linha=(Linha)super.get(i);
			linha.exibeLinha();	
		}
	}

	/**
	* Exibe os tokens das linhas do arquivo.
	*/
	public void exibeTokensLinhas()
	{
		System.out.println("TOKENS DAS LINHAS DO ARQUIVO.ASM");
		for(int i=0;i<super.size();i++)
		{
			Linha linha=(Linha)super.get(i);
			linha.exibeTokens();	
		}
	}

	/**
	* Exibe o codigo objeto das linhas do arquivo.
	*/
	public void exibeCodigoLinhas()
	{
		System.out.println("TOKENS DAS LINHAS DO ARQUIVO.ASM");
		for(int i=0;i<super.size();i++)
		{
			Linha linha=(Linha)super.get(i);
			linha.exibeCodigo();	
		}
	}

	/**
	* Exibe as informações completas das linhas do arquivo.
	*/
	public void exibeInfoLinhas()
	{
		System.out.println("INFORMAÇÕES DAS LINHAS DO ARQUIVO.ASM");
		for(int i=0;i<super.size();i++)
		{
			Linha linha=(Linha)super.get(i);
			linha.exibe();	
		}
	}
}