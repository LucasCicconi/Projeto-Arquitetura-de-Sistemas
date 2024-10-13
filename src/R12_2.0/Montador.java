import java.util.*;

/**
* <i>Montador</i> monta o codigo do objeto de um arquivo.asm baseado no arquitetura
* descrita no arquivo de configuração.
*/

/*
* Modificações:
*
* 13/05/03
* -> Na geração de Arquivos txt para a arquitetura HARVARD:
*    Escrita em apenas um arquivo das instruções e dos dados.
*    Código das instruçoes começam pelo opcode .CODE e código dos dados pelo
*    opcode .DATA.
*     
*/

public class Montador
{
	private int modo;
	private String nomeArquivoAsm,arquivoAsm,arquivoArquiteturaMontador;
	private ArquiteturaMontador arquitetura;
	private PreProcessa preProcessa;
	private Substitui substitui;
	private Arquivos arquivos;
	private VetorLinha linhas;
	private VetorLinhaDado linhasDados,labels;
	
	public Montador(int modo,String arquivoArquiteturaMontador,String arquivoAsm)
	{
		this.modo=modo;
		this.arquivoArquiteturaMontador=arquivoArquiteturaMontador;
		this.arquivoAsm=arquivoAsm;
		this.nomeArquivoAsm=arquivoAsm.substring(0,arquivoAsm.length()-4);
		linhas = new VetorLinha();
		linhasDados = new VetorLinhaDado();
		labels = new VetorLinhaDado();
		arquitetura=new ArquiteturaMontador(modo);
		preProcessa=new PreProcessa(modo,arquitetura,linhas,linhasDados,labels);
		substitui=new Substitui(modo,arquitetura,linhas,linhasDados,labels);
		arquivos=new Arquivos(modo,nomeArquivoAsm,arquitetura,linhas,linhasDados,labels);
	}
	
	public boolean executa()
	{
		if(arquitetura.configura(arquivoArquiteturaMontador))
		{	
			if(preProcessa.executa(arquivoAsm))
			{
				if(substitui.substituiPseudoLabelMemoria())
				{
	    substitui.adicionaConstantesMemoria();
					if(substitui.substituiPseudoLabel())
					{
						if(substitui.substituiCodigo())
						{
							if(arquivos.geraSym())
							{
								if(arquivos.geraHex())
								{
									if(arquivos.geraTxt())
									{
										//linhas.exibeCodigoLinhas();
										//labels.exibeLabels();
										//linhasDados.exibeLinhasDados();
										return true;
									}
								}
							}
						}
					}
				}
			}
		}
		return false;
	}
	
	/**
	* Exibe as informações completas do arquivo.
	*/
	public void exibe()
	{
		linhas.exibeInfoLinhas();
		linhasDados.exibeLinhasDados();
		labels.exibeLinhasDados();
	}


	public static void main(String s[])
	{
		String arquivoArquiteturaMontador="";
		String arquivoAsm="";
		
		// Captura o arquivo da ArquiteturaMontador
		try
   	{
   		arquivoArquiteturaMontador=s[0];
	
	 		if(arquivoArquiteturaMontador.length()>4)
			{
				//Se nao existir extensão .arq concat-a ao arquivoArquivo
				if(!arquivoArquiteturaMontador.substring(arquivoArquiteturaMontador.length()-4,arquivoArquiteturaMontador.length()).equalsIgnoreCase(".arq"))
					arquivoArquiteturaMontador=arquivoArquiteturaMontador.concat(".arq");	
			}
			else
				arquivoArquiteturaMontador=arquivoArquiteturaMontador.concat(".arq");	

			
   		arquivoAsm=s[1];
	 		if(arquivoAsm.length()>4)
			{
				//Verifica se a extensao esta correta e depois remove-a
				if(!arquivoAsm.substring(arquivoAsm.length()-4,arquivoAsm.length()).equalsIgnoreCase(".asm"))
					arquivoAsm=arquivoAsm.concat(".asm");	
			}
			else
					arquivoAsm=arquivoAsm.concat(".asm");	
   			
			Montador montador=new Montador(0,arquivoArquiteturaMontador,arquivoAsm);
			montador.executa();
			System.exit(0);

	  }catch(Exception e)
	  {   			
			new JanelaMontador().show();
		//Montador montador=new Montador(arquivoArquiteturaMontador,arquivoArquivo);
		//montador.executa();
  	}
	}
}