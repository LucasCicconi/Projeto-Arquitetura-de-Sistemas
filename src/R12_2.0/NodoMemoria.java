/*
* @(#)NodoMemoria.java  1.0  30/03/2001
*	
* Aline Vieira de Mello
* Pontifícia Universidade Católica do Rio Grande do Sul - Faculdade de Informática
*/

/**
* <i>NodoMemoria</i> implementa o nodo da lista que contem as instruções
* armazenadas na memória.
*/
public class NodoMemoria
{
	private String pc;
	private String instrucao;
	private String linha;
	private NodoMemoria prox;

	public NodoMemoria(String pc,String instrucao,String linha)
	{
		this.pc=pc;
		this.instrucao=instrucao;
		this.linha=linha;
		prox=null;		
	}

//RETURNs
	public String getPc()	{return pc;}
	public String getInstrucao()	{return instrucao;}
	public String getLinha()	{return linha;}
	public NodoMemoria getProx()	{return prox;}

//SETs
	public void setPc(String p)	{pc=p;}
	public void setInstrucao(String i)	{instrucao=i;}
	public void setLinha(String l)	{linha=l;}
	public void setProx(NodoMemoria aux)	{prox=aux;}
}