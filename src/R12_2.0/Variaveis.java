/*
* @(#)Variaveis.java  1.0  30/03/2001
*	
* Aline Vieira de Mello
* Pontifícia Universidade Católica do Rio Grande do Sul - Faculdade de Informática
*/

/**
* <i>Variaveis</i> implementa a lista que contém as informações sobre as variáveis
* utilizadas no programa .asm do usuário.
*/
public class Variaveis
{
	private NodoVariavel inicio;
	private NodoVariavel fim;
	private Simulador sim;
	private int nLinhaTabela;

	public Variaveis(Simulador sim)
	{
		this.sim=sim;
		inicializa();
	}

	/**
	* Captura o nodo que corresponde ao inicio da lista.
	*/
	public NodoVariavel getInicio()	{return inicio;}

	/**
	* Captura o nodo que corresponde ao fim da lista.
	*/
	public NodoVariavel getFim()	{return fim;}

	/**
	* Atribui o nodo ao inicio da lista.
	*/
	public void setInicio(NodoVariavel aux)	{inicio=aux;}

	/**
	* Atribui o nodo ao fim da lista.
	*/
	public void setFim(NodoVariavel aux)	{fim=aux;}

	/**
	* Inicializa a lista com inicio e fim iguais a null.
	*/
	public void inicializa()
	{
		inicio=null;
		fim=null;
		nLinhaTabela=0;
	}

	/**
	* Inclui no fim da lista.
	*/
	public void inclui(String endereco,String valor,String label)
	{
		NodoVariavel novo=new NodoVariavel(nLinhaTabela,endereco,valor,label);
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
		nLinhaTabela++;
	}
	
	/**
	* Captura o valor correspondente ao endereço.
	*/
	public String getValor(String e)
	{
		NodoVariavel aux=inicio;
		while(aux!=null)
		{
			if(aux.getEndereco().equals(e))
				return aux.getValor();
			aux=aux.getProx();
		}
		return "0000";
	}

	/**
	* Atribui ao endereço um novo valor, caso o endereço não exista
	* este é incluído.
	*/
	public void setValor(String e,String v,String l)
	{
		boolean achei=false;
		NodoVariavel aux=inicio;
		
		while(aux!=null && !achei)
		{
			if(aux.getEndereco().equals(e))
			{
				achei=true;
				aux.setValor(v);
				
				for(int i=0;i<sim.getNumeroVariaveis();i++)
				{
					if((sim.getJanela().getTabelaVariaveis().getValueAt(i,1)).equals(e))
					{
						sim.getJanela().getTabelaVariaveis().setValueAt(v,i,2);
						i=sim.getNumeroVariaveis();
					}
				}
			}
			aux=aux.getProx();
		}
		if(!achei && !l.equals(""))
			inclui(e,v,l);
	}

	/**
	* Exibe todos os nodos incluídos na lista.
	*/
	public void exibe()
	{
		System.out.println("Variavel");
	
		NodoVariavel aux=inicio;
		while(aux!=null)
		{
			System.out.println("Endereco:"+aux.getEndereco()+" Valor:"+aux.getValor()+" Label:"+aux.getLabel());
			aux=aux.getProx();
		}
	}
}