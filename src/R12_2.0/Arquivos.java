/*
* @(#)Arquivos.java  1.0  27/10/2002
*	
* Aline Vieira de Mello
* Pontifícia Universidade Católica do Rio Grande do Sul - Faculdade de Informática
*/

import java.io.*;
import java.util.*;

/**
* <i>Arquivos</i> gera os arquivos.
*/
public class Arquivos
{
	private int modo;
	private String nomeArquivo;
	private ArquiteturaMontador arquitetura;
	private VetorLinha linhas;
	private VetorLinhaDado linhasDados,labels;
	
	public Arquivos(int modo,String nomeArquivo,ArquiteturaMontador arquitetura,VetorLinha linhas,VetorLinhaDado linhasDados,VetorLinhaDado labels)
	{
		this.modo=modo;
		this.nomeArquivo=nomeArquivo;
		this.arquitetura=arquitetura;
		this.linhas=linhas;
		this.linhasDados=linhasDados;
		this.labels=labels;
	}
	
	/**
	* Gera o arquivo de simulacao.
	*/
	public boolean geraSym()
	{
		String nome=nomeArquivo.concat(".sym");
		Linha linha;
		LinhaDado linhaDado,label;
		try
		{
			//Gera o arquivo para a simulacao.
			FileOutputStream fos=new FileOutputStream(nome);
			DataOutputStream dos=new DataOutputStream(fos);
			
			for(int i=0;i<linhas.size();i++)
			{
				linha=(Linha)linhas.get(i);
				if(linha.isInstrucao())
				{
					dos.writeBytes(linha.getEndereco());
					dos.writeBytes("\r\n"+linha.getCodigo());
					dos.writeBytes("\r\n"+linha.getLinha());
					dos.writeBytes("\r\n");
				}
			}
			dos.writeBytes("FIMINST");
			dos.writeBytes("\r\n");
		
			for(int i=0;i<linhasDados.size();i++)
			{
				linhaDado=(LinhaDado)linhasDados.get(i);
				dos.writeBytes(linhaDado.getEndereco());
				dos.writeBytes("\r\n"+linhaDado.getValor());
				if(linhaDado.isLabel())
				{
					dos.writeBytes("\r\n"+linhaDado.getNome());
				}				
				else
					dos.writeBytes("\r\n");
				dos.writeBytes("\r\n");
			}
			
			for(int i=0;i<labels.size();i++)
			{
				label=(LinhaDado)labels.get(i);
				if(!label.isDado())
				{
					dos.writeBytes(label.getEndereco());
					dos.writeBytes("\r\n");
					dos.writeBytes("\r\n"+label.getNome());
					dos.writeBytes("\r\n");
				}
			}
			dos.writeBytes("FIMMEM");
			return true;
		}	
		catch(Exception e)
		{
			new Erro(modo).show("ERRO_SALVA",nome);
			return false;
		}
	}
	
	
	/**
	* Gera o arquivo Hexa para download no FPGA.
	*/
	public boolean geraHex()
	{
		Conversao conversao=new Conversao();
		String nome=nomeArquivo.concat(".hex");
		String crcHex,nrolinhaHex,codigo;
		Linha linha;
		LinhaDado linhaDado;
		int nLinha=linhas.size();
		int nLinhaDado=linhasDados.size();
		int indVetorLinha,indVetorLinhaDado;
    int crc = 16;

		try
		{
			//Gera o arquivo para a simulacao.
			FileOutputStream fos=new FileOutputStream(nome);
			DataOutputStream dos=new DataOutputStream(fos);

			indVetorLinha=0;
			if(indVetorLinha<linhas.size())
				linha=(Linha)linhas.firstElement();
			else
				linha=null;
				
			indVetorLinhaDado=0;
			if(indVetorLinhaDado<linhasDados.size())
				linhaDado=(LinhaDado)linhasDados.firstElement();
			else
				linhaDado=null;

			for(int nrolinha=0;nrolinha<=512;nrolinha=nrolinha+2)
			{
        /* a cada 16 bytes coloca o cabecalho: */
        if (nrolinha % 16 == 0)
        {
            if(nrolinha!= 0)
            {
                crc = 0 - crc;
                crcHex=conversao.decimal_hexa(crc);
                //Captura os dois bytes menos significativos, porque
                //crc é no máximo 255
                crcHex=crcHex.substring(2,4);
                dos.writeBytes(crcHex+"\r\n");
            }
            
            if (nrolinha == 256)
                break;
            nrolinhaHex=conversao.decimal_hexa(nrolinha);
            //Captura os dois bytes menos significativos
						nrolinhaHex=nrolinhaHex.substring(2,4);
						
						dos.writeBytes(":1000"+nrolinhaHex+"00");
	
            crc = 16;
            crc += nrolinha;
        }
        if(linha!=null)
        {
					codigo=linha.getCodigo();
          indVetorLinha++;
          if(indVetorLinha<linhas.size())
         		linha=(Linha)linhas.get(indVetorLinha);
         	else
         		linha=null;
        }
        else if(!arquitetura.isHarvard() && linhaDado!=null)
				{
					codigo=linhaDado.getValor();
					indVetorLinhaDado++;
          if(indVetorLinhaDado<linhasDados.size())
         		linhaDado=(LinhaDado)linhasDados.get(indVetorLinhaDado);
         	else
         		linhaDado=null;
				}
				else
        	codigo="0000";

				dos.writeBytes(codigo);
	
        crc += conversao.hexa_decimal(codigo);
    	}
			dos.writeBytes(":00000001FF\r\n");
			dos.close();
		}
		catch(Exception e)
		{
			new Erro(modo).show("ERRO_SALVA",nome);
			return false;
		}
		if(arquitetura.isHarvard())
		{
			if(!geraHexDados())
				return false;
		}
		return true;
	}
	
	/**
	* Gera o arquivo Hexa com dados para download no FPGA.
	*/
	public boolean geraHexDados()
	{
		Conversao conversao=new Conversao();
		String nome=nomeArquivo.concat("Dados.hex");
		String crcHex,nrolinhaHex,codigo;
		LinhaDado linhaDado;
		int nLinhaDado=linhasDados.size();
		int indVetorLinhaDado;
    int crc = 16;

		try
		{
			//Gera o arquivo para a simulacao.
			FileOutputStream fos=new FileOutputStream(nome);
			DataOutputStream dos=new DataOutputStream(fos);

			indVetorLinhaDado=0;
			if(indVetorLinhaDado<linhasDados.size())
				linhaDado=(LinhaDado)linhasDados.firstElement();
			else
				linhaDado=null;

			for(int nrolinha=0;nrolinha<=512;nrolinha=nrolinha+2)
			{
        /* a cada 16 bytes coloca o cabecalho: */
        if (nrolinha % 16 == 0)
        {
            if(nrolinha!= 0)
            {
                crc = 0 - crc;
                crcHex=conversao.decimal_hexa(crc);
                //Captura os dois bytes menos significativos, porque
                //crc é no máximo 255
                crcHex=crcHex.substring(2,4);
                dos.writeBytes(crcHex+"\r\n");
            }
            
            if (nrolinha == 256)
                break;
            nrolinhaHex=conversao.decimal_hexa(nrolinha);
            //Captura os dois bytes menos significativos
						nrolinhaHex=nrolinhaHex.substring(2,4);
						
						dos.writeBytes(":1000"+nrolinhaHex+"00");
	
            crc = 16;
            crc += nrolinha;
        }
        if(linhaDado!=null)
				{
					codigo=linhaDado.getValor();
					indVetorLinhaDado++;
          if(indVetorLinhaDado<linhasDados.size())
         		linhaDado=(LinhaDado)linhasDados.get(indVetorLinhaDado);
         	else
         		linhaDado=null;
				}
				else
        	codigo="0000";

				dos.writeBytes(codigo);
	
        crc += conversao.hexa_decimal(codigo);
    	}
			dos.writeBytes(":00000001FF\r\n");
			dos.close();
		}
		catch(Exception e)
		{
			new Erro(modo).show("ERRO_SALVA",nome);
			return false;
		}
		return true;
	}

	/**
	* Gera o arquivo txt para a simulação em VHDL
	*/
	public boolean geraTxt()
	{
		String nome=nomeArquivo.concat(".txt");
		Linha linha;
		LinhaDado linhaDado,label;
		try
		{
			//Gera o arquivo para a simulacao.
			FileOutputStream fos=new FileOutputStream(nome);
			DataOutputStream dos=new DataOutputStream(fos);
			
			if(arquitetura.isHarvard())
				dos.writeBytes(".CODE\r\n");

			for(int i=0;i<linhas.size();i++)
			{
				linha=(Linha)linhas.get(i);
				if(linha.isInstrucao())
				{
					dos.writeBytes(linha.getEndereco());
					dos.writeBytes("\t"+linha.getCodigo());
					dos.writeBytes("\t"+linha.getLinha());
					dos.writeBytes("\r\n");
				}
			}

			if(arquitetura.isHarvard())
				dos.writeBytes(".DATA\r\n");

			for(int i=0;i<linhasDados.size();i++)
			{
				linhaDado=(LinhaDado)linhasDados.get(i);
				dos.writeBytes(linhaDado.getEndereco());
				dos.writeBytes("\t"+linhaDado.getValor());
				if(linhaDado.isLabel())
				{
					dos.writeBytes("\t"+linhaDado.getNome());
				}				
				dos.writeBytes("\r\n");
			}

		}	
		catch(Exception e)
		{
			new Erro(modo).show("ERRO_SALVA",nome);
			return false;
		}
		return true;
	}
	
	/**
	* Gera o arquivo txt contendo os dados para a simulação em VHDL
	*/
	public boolean geraTxtDados()
	{
		String nome=nomeArquivo.concat("Dados.txt");
		Linha linha;
		LinhaDado linhaDado,label;
		try
		{
			//Gera o arquivo para a simulacao.
			FileOutputStream fos=new FileOutputStream(nome);
			DataOutputStream dos=new DataOutputStream(fos);
						
			for(int i=0;i<linhasDados.size();i++)
			{
				linhaDado=(LinhaDado)linhasDados.get(i);
				dos.writeBytes(linhaDado.getEndereco());
				dos.writeBytes("\t"+linhaDado.getValor());
				if(linhaDado.isLabel())
				{
					dos.writeBytes("\t"+linhaDado.getNome());
				}				
				dos.writeBytes("\r\n");
			}
		}	
		catch(Exception e)
		{
			new Erro(modo).show("ERRO_SALVA",nome);
			return false;
		}
		return true;
	}

}