/*
* @(#)VetorLinhaDado.java  1.0  27/11/2002
*	
* Aline Vieira de Mello
* Pontifícia Universidade Católica do Rio Grande do Sul - Faculdade de Informática
*/

import java.util.*;

/**
* <i>VetorLinhaDado</i> contem os métodos sobre o vetor de linhas de dados.
*/
public class VetorLinhaDado extends Vector
{
	public VetorLinhaDado()
	{
		super();
	}
		
	/**
	* Exibe os linhaDados do arquivo.
	*/
	public void exibeLinhasDados()
	{
		System.out.println("LINHAS DE DADOS DO ARQUIVO.ASM");
		for(int i=0;i<super.size();i++)
		{
			LinhaDado linhaDado=(LinhaDado)super.get(i);
			linhaDado.exibe();	
		}
	}

	/**
	* Exibe os super do arquivo.
	*/
	public void exibeLabels()
	{
		System.out.println("LABELS DO ARQUIVO.ASM");
		for(int i=0;i<super.size();i++)
		{
			LinhaDado linhaDado=(LinhaDado)super.get(i);
			linhaDado.exibe();	
		}
	}
	
	/**
	* Quando a arquitetura não for HARVARD então é necessário
	* ajustar os enderecos dos dados que devem começar após o 
	* último endereço de instrução.
	*/
	public void ajustaEnderecoDados(int enderecoFimCode)
	{
		int enderecoInicial,endereco;

		if(!super.isEmpty())
		{
			LinhaDado linhaDado=(LinhaDado)super.firstElement();
			
			enderecoInicial=linhaDado.getEnderecoDecimal();
			if(enderecoInicial<enderecoFimCode)
				enderecoInicial=enderecoFimCode-enderecoInicial;
			else
				enderecoInicial=enderecoInicial-enderecoFimCode;
			
			for(int i=0;i<super.size();i++)
			{
				linhaDado=(LinhaDado)super.get(i);
				endereco=linhaDado.getEnderecoDecimal();
				if(endereco<enderecoInicial)
					endereco=enderecoInicial-endereco;
				else
					endereco=endereco-enderecoInicial;
				linhaDado.setEndereco(endereco);
			}
		}
	}
	
	/**
	* adiciona um label a linha de dados
	*/
	public boolean addLabel(String token,int endereco)
	{
		//verifica se o label ja existe
		if(existe(token))
			return false;
				
		LinhaDado linhaDado=new LinhaDado(token,endereco);
		super.add(linhaDado);
		return true;
	}

	/**
	* adiciona um dado a linha de dados.
	*/
	public boolean addLinhaDado(VetorLinhaDado labels,String token)
	{
		LinhaDado linhaDado;
		
		//Se nao existir nenhum label incluido ocorreu um erro
		if(labels.isEmpty())
			return false;
			
		//Captura o ultimo label incluido
		linhaDado=(LinhaDado)labels.lastElement();
		//Captura os dados do label
		String nome=linhaDado.getNome();
		String endereco=linhaDado.getEndereco();
		//Insere os dados do label com seu respectivo valor
		//no vetor de linhas de dado
		linhaDado=new LinhaDado(nome,endereco,token);
		super.add(linhaDado);
		return true;
	}

	/**
	* adiciona um dado ao vetor.
	*/
	public boolean addLinhaDado(VetorLinhaDado labels)
	{
		LinhaDado linhaDado;

		//Se nao existir nenhum label incluido ocorreu um erro
		if(labels.isEmpty())
			return false;			
		//Captura o ultimo label incluido
		linhaDado=(LinhaDado)labels.lastElement();
		//Captura os dados do label
		String nome=linhaDado.getNome();
		String endereco=linhaDado.getEndereco();
		//Insere os dados do label com seu respectivo valor
		//no vetor de linhas de dado
		linhaDado=new LinhaDado(nome,endereco);
		super.add(linhaDado);
		return true;
	}

	/**
	* Adiciona o valor ao ultimo label da lista de linhas de dados
	*/
	public boolean addValor(String token)
	{
		//Se nao existir nenhum label incluido ocorreu um erro
		if(super.isEmpty())
			return false;
			
		//Captura o ultimo label incluido
		LinhaDado linhaDado=(LinhaDado)super.lastElement();
    linhaDado.setValor(token);
    return true;
	}

	/**
	* Seta o endereco informado a ultima linha de dados.
	*/
	public void setEnd(int endereco)
	{
		LinhaDado linhaDado=(LinhaDado)super.lastElement();
		linhaDado.setEndereco(endereco);
	}

	/**
	* Seta o endereco informado ao ultimo label da lista de super.
	*/
	public boolean setEnd(String token,int endereco)
	{
		//Se nao existir nenhum label incluido ocorreu um erro
		if(super.isEmpty())
			return false;			
		//Captura o ultimo label incluido
		LinhaDado linhaDado=(LinhaDado)super.lastElement();
    linhaDado.setEndereco(endereco);
		return true;			
	}

	/**
	* Retorna true se o token existe no vetor de linhaDadoss
	*/
	public boolean existe(String token)
	{
		if(!token.equals(""))
		{
			//verifica se o label ja existe
			for(int i=0;i<super.size();i++)
			{
				LinhaDado linhaDado=(LinhaDado)super.get(i);
				if(linhaDado.getNome().equalsIgnoreCase(token))
					return true;
			}
		}
		return false;
	}
	
	/**
	* Retorna a LinhaDados que possui o nome igual ao informado.
	*/
	public LinhaDado getLinhaDado(String nome)
	{
		LinhaDado linhaDado;
		//procura o label nas linhas de dados
		for(int i=0;i<super.size();i++)
		{
			linhaDado=(LinhaDado)super.get(i);
			if(linhaDado.getNome().equalsIgnoreCase(nome))
				return linhaDado;
		}
		return null;
	}
}