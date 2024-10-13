/*
* @(#)LinhaDado.java  1.0  27/11/2002
*	
* Aline Vieira de Mello
* Pontifícia Universidade Católica do Rio Grande do Sul - Faculdade de Informática
*/

import java.util.*;

/**
* <i>LinhaDado</i> armazena os dados referentes a uma instrução da arquitetura.
*/
public class LinhaDado
{
	private String nome;
	private String endereco;
	private String valor;


	public LinhaDado(String nome,int endereco)
	{
		this.nome=nome;
		this.endereco=new Conversao().decimal_hexa(endereco);
		this.valor=null;
	}

	public LinhaDado(String nome,String endereco)
	{
		this.nome=nome;
		this.endereco=endereco;
		this.valor=null;
	}


	public LinhaDado(int endereco,String valor)
	{
		this.nome=null;
		this.endereco=new Conversao().decimal_hexa(endereco);
		this.valor=valor;
	}

	public LinhaDado(String nome,int endereco,int valor)
	{
		this.nome=nome;
		this.endereco=new Conversao().decimal_hexa(endereco);
		this.valor=new Conversao().decimal_hexa(valor);
	}

	public LinhaDado(String nome,String endereco,String valor)
	{
		this.nome=nome;
		this.endereco=endereco;
		this.valor=valor;
	}

	public LinhaDado(String nome,int endereco,String valor)
	{
		this.nome=nome;
		this.endereco=new Conversao().decimal_hexa(endereco);
		this.valor=valor;
	}


	/**
	* Retorna o nome do LinhaDado.
	*/
	public String getNome(){return nome;}

	/**
	* Retorna o endereco da linha de Dado.
	*/
	public String getEndereco(){return endereco;}
	
	/**
	* Retorna o endereco da linha de dado em decimal.
	*/
	public int getEnderecoDecimal()
	{
		int decimal=new Conversao().hexa_decimal(endereco);
		return decimal;
	}

	/**
	* Retorna o valor do LinhaDado.
	*/
	public String getValor(){return valor;}
	
	/**
	* Atribui o endereco informado a linha.
	*/
	public void setEndereco(int endereco)
	{
		this.endereco=new Conversao().decimal_hexa(endereco);
	}

	/**
	* Atribui o valor informado como valor da linha de dado .
	*/
	public void setValor(String valor){this.valor=valor;}

	/**
	* Verifica se a linha de dados possui nome diferente de null,
	* ou seja, é uma linha que contem label.
	*/
	public boolean isLabel()
	{
		if(nome!=null)
			return true;
		else
			return false;
	}

	/**
	* Verifica se a linha de dados possui dados.
	*/
	public boolean isDado()
	{
		if(valor!=null)
			return true;
		else
			return false;
	}

	/**
	* Exibe os dados da instrução.
	*/
	public void exibe()
	{
		System.out.println(endereco+" "+nome+" "+valor);
	}
}