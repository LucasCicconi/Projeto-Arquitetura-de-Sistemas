/*
* @(#)Erro.java  1.0  27/11/2002
*	
* Aline Vieira de Mello
* Pontifícia Universidade Católica do Rio Grande do Sul - Faculdade de Informática
*/
import java.awt.event.*;
import javax.swing.*;

/**
* <i>Erro</i> apresenta os erros.
* modo 0 - console
* modo 1 - interface
*/
public class Erro
{
	
	private int modo;

	public Erro(int modo)
	{
		this.modo=modo;
	}
	
	/**
	* Apresenta erro na console e fecha o programa.
	*/
	public void show(String tipoErro)
	{
		if(modo==0)
			showConsole(tipoErro);
		else	
			showInterface(tipoErro);
	}

	/**
	* Apresenta erro na console e fecha o programa.
	*/
	public void show(String tipoErro,String linha)
	{
		if(modo==0)
			showConsole(tipoErro,linha);
		else	
			showInterface(tipoErro,linha);
	}


	/**
	* Apresenta erro.
	*/
	public void show(String tipoErro,int nlinha)
	{
		if(modo==0)
			showConsole(tipoErro,nlinha);
		else	
			showInterface(tipoErro,nlinha);
	}

	/**
	* Apresenta erro.
	*/
	public void show(String tipoErro,int nlinha,String linha)
	{
		if(modo==0)
			showConsole(tipoErro,nlinha,linha);
		else	
			showInterface(tipoErro,nlinha,linha);
	}

	/**
	* Apresenta erro na console e fecha o programa.
	*/
	public void showConsole(String tipoErro)
	{
		System.out.println("ERRO: ");
		System.out.println(getMensagem(tipoErro));
		System.exit(0);
	}

	/**
	* Apresenta erro na interface do programa.
	*/
	public void showInterface(String tipoErro)
	{
		String mensagem=getMensagem(tipoErro);
		JOptionPane.showMessageDialog(null,mensagem,"Mensagem de Erro",JOptionPane.ERROR_MESSAGE);
	}

	/**
	* Apresenta erro na console e fecha o programa.
	*/
	public void showConsole(String tipoErro,String linha)
	{
		System.out.println("ERRO: "+linha);
		System.out.println(getMensagem(tipoErro));
		System.exit(0);
	}

	/**
	* Apresenta erro na interface do programa.
	*/
	public void showInterface(String tipoErro,String linha)
	{
		String mensagem="ERRO: "+linha+"\n"+getMensagem(tipoErro);
		JOptionPane.showMessageDialog(null,mensagem,"Mensagem de Erro",JOptionPane.ERROR_MESSAGE);
	}

	/**
	* Apresenta erro na console e fecha o programa.
	*/
	public void showConsole(String tipoErro,int nlinha)
	{
		System.out.println("ERRO LINHA: "+nlinha);
		System.out.println(getMensagem(tipoErro));
		System.exit(0);
	}

	/**
	* Apresenta erro na interface do programa.
	*/
	public void showInterface(String tipoErro,int nlinha)
	{
		String mensagem="ERRO LINHA: "+nlinha+"\n"+getMensagem(tipoErro);
		JOptionPane.showMessageDialog(null,mensagem,"Mensagem de Erro",JOptionPane.ERROR_MESSAGE);
	}

	/**
	* Apresenta erro na console e fecha o programa.
	*/
	public void showConsole(String tipoErro,int nlinha,String linha)
	{
		System.out.println("ERRO: "+linha+" LINHA: "+nlinha);
		System.out.println(getMensagem(tipoErro));
		System.exit(0);
	}

	/**
	* Apresenta erro na interface do programa.
	*/
	public void showInterface(String tipoErro,int nlinha,String linha)
	{
		String mensagem="ERRO: "+linha+" LINHA: "+nlinha+"\n"+getMensagem(tipoErro);
		JOptionPane.showMessageDialog(null,mensagem,"Mensagem de Erro",JOptionPane.ERROR_MESSAGE);
	}

	/**
	* Retorna a mensagem correspondente ao tipo do erro.
	*/
	public String getMensagem(String tipoErro)
	{
		if(tipoErro.equalsIgnoreCase("ERRO_PARAMETRO"))
			return "Parâmetros: java Montador nomearquitetura nomearquivo.";
		else if(tipoErro.equalsIgnoreCase("TEXTFIELD_ARQUITETURA"))
			return "Deve ser preenchido o nome da Arquitetuta.";
		else if(tipoErro.equalsIgnoreCase("TEXTFIELD_ARQUIVO"))
			return "Deve ser preenchido o nome do arquivo .ASM.";
		else if(tipoErro.equalsIgnoreCase("FILE_OPEN"))
			return "Não foi possível abrir o arquivo";
		else if(tipoErro.equalsIgnoreCase("FILE_NOTFOUND"))
			return "Arquivo não encontrado!";
		else if(tipoErro.equalsIgnoreCase("EXT_ASM"))
			return "Arquivo para montagem não possui extensão ASM.";
		else if(tipoErro.equalsIgnoreCase("ERRO_REG"))
			return "Declaracao de registrador inválida.";
		else if(tipoErro.equalsIgnoreCase("ERRO_ARQ"))
			return "Erro no arquivo de configuração da Arquitetura";
		else if(tipoErro.equalsIgnoreCase("ERRO_ARQ_CAMPO"))
			return "Erro nos campos do arquivo de configuração da arquitetura.";
		else if(tipoErro.equalsIgnoreCase("ERRO_ARQ_REG"))
			return "Erro nos registradores do arquivo de configuração da arquitetura.";
		else if(tipoErro.equalsIgnoreCase("LABEL_DUPLO"))
			return "Declaracao duplificada do label.";
		else if(tipoErro.equalsIgnoreCase("FORMATO_INSTRUCAO"))
			return "Instrucao com formato inválido.";
		else if(tipoErro.equalsIgnoreCase("ERRO_PSEUDO"))
			return "Declaracao errada de pseudo codigo.";
		else if(tipoErro.equalsIgnoreCase("NAO_BINARIO"))
			return "Numero nao esta na base binaria.";
		else if(tipoErro.equalsIgnoreCase("NAO_HEXA"))
			return "Numero nao esta na base hexadecimal.";
		else if(tipoErro.equalsIgnoreCase("ERRO_DB"))
			return "DB deve ser precedido de um LABEL.";
		else if(tipoErro.equalsIgnoreCase("ERRO_LAB"))
			return "LinhaDados em posicao invalida.";
		else if(tipoErro.equalsIgnoreCase("INST_INEXISTENTE"))
			return "Instrucao Inexistente na Arquitetura.";
		else if(tipoErro.equalsIgnoreCase("ERRO_INST"))
			return "Instrucao em posicao invalida.";
		else if(tipoErro.equalsIgnoreCase("ERRO_CONST"))
			return "Constante em posicao invalida.";
		else if(tipoErro.equalsIgnoreCase("ERRO_PSEUDO_LABEL"))
			return "Label nao declarado.";
		else if(tipoErro.equalsIgnoreCase("MODO_PSEUDO_LABEL"))
			return "Os Labels que nao possuem dados devem ser do modo imediato #.";
		else if(tipoErro.equalsIgnoreCase("CONST4_MAIORMENOR"))
			return "Constante inferior a -7 ou superior a 8.";
		else if(tipoErro.equalsIgnoreCase("CONST8_MAIORMENOR"))
			return "Constante inferior a -127 ou superior a 128.";
		else if(tipoErro.equalsIgnoreCase("CONST10_MAIORMENOR"))
			return "Constante inferior a -1023 ou superior a 1024";
		else if(tipoErro.equalsIgnoreCase("CONST12_MAIORMENOR"))
			return "Constante inferior a -2047 ou superior a 2048";
		else if(tipoErro.equalsIgnoreCase("CONST16_MAIORMENOR"))
			return "Constante inferior a -32767 ou superior a 32768";
		else if(tipoErro.equalsIgnoreCase("NAO_CONST"))
			return "Constante invalida.";
		else if(tipoErro.equalsIgnoreCase("CAMPO"))
			return "Campo não encontrado!";
		else if(tipoErro.equalsIgnoreCase("DESLOC10_MAIOR"))
			return "Deslocamento maior que 10 bits.";
		else if(tipoErro.equalsIgnoreCase("ERRO_SALVA"))
			return "Nao foi possivel salvar o arquivo.";
		return "";
	
	}
}