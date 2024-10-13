/*
* @(#)ArqSim.java  1.0  30/03/2001
*	
* Aline Vieira de Mello
* Pontifícia Universidade Católica do Rio Grande do Sul - Faculdade de Informática
*/

/**
* <i>ArqSim</i> implementa a lista que contem as instruções da arquitetura
* padrão do simulador.
*/
public class ArqSim
{
	private NodoArqSim inicio;
	private NodoArqSim fim;
	
	public ArqSim()
	{
		inicio=null;
		fim=null;		
	}	
	
	/**
	* Retorna o apontador para o inicio da lista.
	*/
	public NodoArqSim getInicio() {return inicio;}
	
	/**
	* Retorna o apontador para o fim da lista.
	*/
	public NodoArqSim getFim() {return fim;}
	
	/**
	* Informa ao apontador o inicio da lista.
	*/
	public void setInicio(NodoArqSim n) {inicio=n;}
	
	/**
	* Informa ao apontador o fim da lista.
	*/
	public void setFim(NodoArqSim n) {fim=n;}
		
	/**
	* Inclui um nodo no fim da lista de intruções.
	*/
	public void inclui(String inst,String camp1,String camp2,String camp3,String camp4)
	{
		NodoArqSim novo=new NodoArqSim(inst,camp1,camp2,camp3,camp4);
		
		if(inicio==null)
		{
			inicio=novo;
			fim=novo;	
		}
		else
		{
			fim.setProx(novo);
			fim=novo;		
		}
	}
	
	/**
	* Retorna o nodo que contem a instrução informada, caso a instrução não
	* exista retorna NULL. 
	*/
	public NodoArqSim procuraInstrucao(String inst)
	{
		NodoArqSim aux=inicio;
		while(aux!=null)
		{
			if(aux.getInstrucao().equalsIgnoreCase(inst))
				return aux;
			aux=aux.getProx();	
		}
		return null;
	}
	
	/**
	* Exibe na console todas as instruções contidas na lista.
	*/
	public void exibe()
	{
		System.out.println("LISTA DAS INSTRUÇÕES PADRÃO DO SIMULADOR");
		NodoArqSim aux=inicio;
		while(aux!=null)
		{
			System.out.println("\n"+aux.getInstrucao());
			System.out.println(aux.getCampo1()+" "+aux.getCampo2()+" "+aux.getCampo3()+" "+aux.getCampo4());
			aux=aux.getProx();	
		}
	}
}