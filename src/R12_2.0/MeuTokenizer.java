/*
* @(#)MeuTokenizer.java  1.0  05/05/2001
*	
* Aline Vieira de Mello
* Pontifícia Universidade Católica do Rio Grande do Sul - Faculdade de Informática
*/
import java.util.*;
	
/**
* <i>MeuTokenizer</i> basea-se na lista de separadores para dividir a linha em
* tokens.
*/
public class MeuTokenizer
{
	private String[] separadores;
	private String linha;
	private Vector tokens;
	private int numerotokens;
	private int posicaolinha;
	private int tamanholinha;
	private int indice;
		
	public MeuTokenizer(String[] separadores,String linha)
	{
		this.separadores=separadores;
		this.linha=linha;
		tokens=new Vector();
		numerotokens=0;
		posicaolinha=0;
		tamanholinha=linha.length();
		indice=0;
		separaLinha();
	}
		
	/**
	* Verifica se o caracter é um separador, se for retorna true.
	*/
	public boolean ehSeparador(String s)
	{
		for(int i=0;i<separadores.length;i++)
		{
			if(separadores[i].equalsIgnoreCase(s))
				return true;
		}
		return false;
	}

	/**
	* Verifica se o caracter é um separador, se for retorna true.
	*/
	public boolean ehSeparador(String[] separadores,String s)
	{
		for(int i=0;i<separadores.length;i++)
		{
			if(separadores[i].equalsIgnoreCase(s))
				return true;
		}
		return false;
	}

	/**
	* Lê a linha separado-a em tokens conforme os separadores. 
	*/
	public void separaLinha()
	{	
		while(posicaolinha<tamanholinha)
		{
			String token="";
			while (posicaolinha<tamanholinha && ehSeparador(linha.substring(posicaolinha,posicaolinha+1)))
				posicaolinha++;
			if(posicaolinha<tamanholinha)
			{
				while ((posicaolinha<tamanholinha) && !ehSeparador(linha.substring(posicaolinha,posicaolinha+1)))
				{
						token=token.concat(linha.substring(posicaolinha,posicaolinha+1));
						posicaolinha++;
				}
				numerotokens++;
				tokens.addElement((Object)token);
			}
		}
	}
	
	/**
	* Retira os separadores do tipo Removíveis.
	* A sequencia de separadores removiveis são eliminadas e dentre estas as que 
	* ocorrem entre os tokens são substituídas por UM espaço em branco caso o 
	* o antecessor for diferente de um separador fixo.
	*
	* Por exemplo:
	*	 sepadoresDel={"\t","\n"} e separadoresFixos={",","(",")"}
	*  linha="      ADD          R12,  R13     "
	* Logo teremos:
	*  linha="ADD R12,R13" 		
	*/
	public String removeBrancos(String[] separadoresDel,String[] separadoresFix,String linha)	
	{
		String linhaRetorno=new String();
		int tamanhoLinha=linha.length();
		int indiceLinha=0;
		boolean separadorFixo=false,separadorDel=false;

				
		while(indiceLinha<tamanhoLinha)
		{
			//Verifica se é separador REMOVIVEIS
			while(indiceLinha<tamanhoLinha && ehSeparador(separadoresDel,linha.substring(indiceLinha,indiceLinha+1)))
			{
				separadorDel=true;
				indiceLinha++;
			}
			
			//Continua se não for fim da linha
			if(indiceLinha!=tamanhoLinha)
			{
				//Se não está no início e o último byte válido não for um
				//separador Fixo então inclui UM espaço em branco.
				if(linhaRetorno.length()!=0 && !separadorFixo)
					linhaRetorno=linhaRetorno.concat(" ");
				
				while(indiceLinha<tamanhoLinha && !ehSeparador(separadoresDel,linha.substring(indiceLinha,indiceLinha+1)))
				{
					if(ehSeparador(separadoresFix,linha.substring(indiceLinha,indiceLinha+1)))
					{
						//ultimo byte for um separador Removivel, remove o espaço em
						//incluído no fim da linha indevidamente
						if(separadorDel)
							linhaRetorno=linhaRetorno.substring(0,linhaRetorno.length()-1);

						separadorFixo=true;
						separadorDel=false;
					}
					else
					{
						separadorDel=false;
						separadorFixo=false;
					}
						
					linhaRetorno=linhaRetorno.concat(linha.substring(indiceLinha,indiceLinha+1));
					indiceLinha++;
				}
			}
		}
		return linhaRetorno;	
	}	

	/**
	* Retorna o próximo token da linha.
	*/
	public String nextToken()
	{
		try
		{
			return (String)tokens.elementAt(indice++);
		}catch(Exception e){System.err.println("Não há próximo Token.");}
		return null;
	}
	
	/**
	* Retorna o token da linha na posição n informada pelo parâmetro.
	*/
	public String nextToken(int n)
	{
		try
		{
			return (String)tokens.elementAt(n);
		}catch(Exception e){System.err.println("Não há próximo Token.");}
		return null;
	}
	
	/**
	* Retorna o número de tokens da linha.
	*/
	public int countTokens(){return numerotokens;}
}