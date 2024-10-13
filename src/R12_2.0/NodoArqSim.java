/*
* @(#)NodoArqSim.java  1.0  30/03/2001
*	
* Aline Vieira de Mello
* Pontifícia Universidade Católica do Rio Grande do Sul - Faculdade de Informática
*/

/**
* <i>NodoArqSim</i> implementa o nodo da lista de instruções padrão do
* simulador.
*/
public class NodoArqSim
{
	private String campo[];
	private String instrucao;
	private NodoArqSim prox;

	public NodoArqSim(String inst,String camp1,String camp2,String camp3,String camp4)
	{
		campo=new String[4];
		campo[0]=camp1;
		campo[1]=camp2;
		campo[2]=camp3;
		campo[3]=camp4;
		instrucao=inst;
		prox=null;		
	}

//RETURNs
	public String getCampo(int n) {return campo[n];}
	public String getCampo1()	{return campo[0];}
	public String getCampo2()	{return campo[1];}
	public String getCampo3()	{return campo[2];}
	public String getCampo4()	{return campo[3];}
	public String getInstrucao()	{return instrucao;}
  public NodoArqSim getProx()	{return prox;}

//SETs
	public void setCampo(int n,String c) {campo[n]=c;}
	public void setCampo1(String c)	{campo[0]=c;}
	public void setCampo2(String c)	{campo[1]=c;}
	public void setCampo3(String c)	{campo[2]=c;}
	public void setCampo4(String c)	{campo[3]=c;}
	public void setInstrucao(String i)	{instrucao=i;}
	public void setProx(NodoArqSim aux)	{prox=aux;}
	
	/**
	* Retorna TRUE se o campo existe na instrução e FALSE se não existe.
	*/
	public boolean existeCampo(String c)
	{
		for(int i=0;i<4;i++)
		{
			if(campo[i].equalsIgnoreCase(c))	
				return true;
		}
		return false;
	}

	/**
	* Retorna o indice do campo que contem o campo informado. Se o campo informado não
	* existir, retorna -1.
	*/
	public int procura(String c)
	{
		for(int i=0;i<4;i++)
		{
			if(campo[i].equalsIgnoreCase(c))
				return i;
		}
		return -1;
	}
	
	/**
	* Exibe a instrucao do simulador.
	*/
	public void exibe()
	{
		System.out.println(" inst="+instrucao+" campos="+campo[0]+campo[1]+campo[2]+campo[3]);
	}

}