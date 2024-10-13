/*
* @(#)Nodovariável.java  1.0  30/03/2001
*	
* Aline Vieira de Mello
* Pontifícia Universidade Católica do Rio Grande do Sul - Faculdade de Informática
*/

/**
* <i>Nodovariavel</i> implementa o nodo da lista que contem as variáveis
* armazenadas na memória.
*/

public class NodoVariavel
{
	private int nLinhaTabela;
	private String endereco;
	private String valor;
	private String label;
	private NodoVariavel prox;

	public NodoVariavel(int nLinhaTabela,String endereco,String valor,String label)
	{
		this.nLinhaTabela=nLinhaTabela;
		this.endereco=endereco;
		this.valor=valor;
		this.label=label;
		prox=null;		
	}

//RETURNs
	public int getNLinhaTabela()	{return nLinhaTabela;}
	public String getEndereco()	{return endereco;}
  public String getValor()	{return valor;}
  public String getLabel()	{return label;}
  public NodoVariavel getProx()	{return prox;}

//SETs
	public void setNLinhaTabela(int n)	{nLinhaTabela=n;}
	public void setEndereco(String e)	{endereco=e;}
	public void setValor(String v)	{valor=v;}
	public void setLabel(String l)	{label=l;}
	public void setProx(NodoVariavel aux)	{prox=aux;}
}