/*
* @(#)Substitui.java  1.0  27/11/2002
*
* Aline Vieira de Mello
* Pontifícia Universidade Católica do Rio Grande do Sul - Faculdade de Informática
*/

import java.util.*;

/**
* <i>Substitui</i>
*
*/
public class Substitui
{
	private int modo;
	private ArquiteturaMontador arquitetura;
	private VetorLinha linhas;
	private VetorLinhaDado linhasDados,labels;

	public Substitui(int modo,ArquiteturaMontador arquitetura,VetorLinha linhas,VetorLinhaDado linhasDados,VetorLinhaDado labels)
	{
		this.modo=modo;
		this.arquitetura=arquitetura;
		this.linhas = linhas;
		this.linhasDados = linhasDados;
		this.labels = labels;
	}


	/**
	* Identifica os tokens que são do tipo PSEUDOLABEL nas linhas de dados,
	* ou seja, nas linhas que possuem um token do tipo DB.
	* Repete o processo mais uma vez se todos os labels nao foram resolvidos.
	* Se novamente os dados nao forem resolvido presenta erro.
	*/
	public boolean substituiPseudoLabelMemoria()
	{
		if(!substituiPseudoLabelMemoria(1))
		{
			if(!substituiPseudoLabelMemoria(2))
			{
				new Erro(modo).show("MODO_PSEUDO_LABEL");
				return false;
			}
		}
		return true;
	}

	/**
	* Identifica os tokens que são do tipo PSEUDOLABEL nas linhas de dados,
	* ou seja, nas linhas que possuem um token do tipo DB.
	* Repete o processo mais uma vez para que labels fora de ordem possam
	* ser resolvidos.Se nao forem apresenta erro.
	*/
	public boolean substituiPseudoLabelMemoria(int nsubstitui)
	{
		Vector tipos;
		Linha linha;
		LinhaDado label,labelLinha,linhaDado;
		String token;
		int endLabelLinha,indice;
		boolean imediato,resolvido=true;
		
		for(int i=0;i<linhas.size();i++)
		{
			linha=(Linha)linhas.get(i);
			if(linha.existeTipoToken("DB"))
			{
				tipos=linha.getTiposTokens();
				//Captura o label da linha
				indice=linha.getIndiceTipoToken("LABEL");
				token=linha.getToken(indice);
				labelLinha=labels.getLinhaDado(token);
				endLabelLinha=labelLinha.getEnderecoDecimal();

				for(int j=0;j<tipos.size();j++)
				{
					if(((String)tipos.get(j)).equalsIgnoreCase("PSEUDOLABEL"))
					{
						//captura o token que é do tipo PSEUDOLABEL
						token=(String)linha.getToken(j);

						//verifica se o PSEUDOLABEL é imediato, ou seja, se possui
						//cerquilha (#) no primeiro byte.
						if(token.substring(0,1).equals("#"))
						{
							imediato=true;
							//remove a cerquilha (#)
							token=token.substring(1,token.length());
						}
						else
							imediato=false;

						//Captura o label correspondente ao PSEUDOLABEL
						label=labels.getLinhaDado(token);
						if(label==null)
						{
							new Erro(modo).show("ERRO_PSEUDO_LABEL",linha.getNroLinha(),token);
							return false;
						}

						//captura o valor correspondente ao PSEUDOLABEL
						if(imediato)
							token=label.getEndereco();
						else
						{
							//Se valor do label nao foi definido ainda
							token=label.getValor();
							if(token==null)
								resolvido=false;
						}

						if(token!=null)
						{
							//se tipoToken anterior ao pseudoLabel igual a DB
							//insere o valor ao label	e insere a linha de dados
							if(((String)tipos.get(j-1)).equalsIgnoreCase("DB"))
							{
								labelLinha.setValor(token);
								linhaDado=linhasDados.getLinhaDado(labelLinha.getNome());
								if(linhaDado!=null)
									linhaDado.setValor(token);
							}
							/*else
							{
								//se tipoToken anterior ao pseudoLabel diferente de DB
								//insere somente na linha de dados
                //-2 refere-se ao label e o DB que não contam como endereco
                endLabelLinha=endLabelLinha+j-2;
                linhaDado=new LinhaDado(endLabelLinha,token);
                linhasDados.add(linhaDado);
              }*/              
              //seta o novo token e o novo tipo
              linha.setToken(j,token);
              linha.setTipoToken(j,"CONSTANTE");
		}
	    }
	   }
	 }
	}
	return resolvido;
  }

	/**
	* Identifica os tokens que são do tipo CONSTANTES nas linhas de dados,
	* ou seja, nas linhas que possuem um token do tipo DB e insere as mesmas.
	* Observacao
	*	São inseridas as costantes que não tem como tipo de token
	* antecessor um DB, porque estas ja foram inseridas junto as labels.
	* Por exemplo:
	*   VETOR: DB #01H,#02H,#03H //label VETOR no endereco 0000
	* É inserido:
	* endereco 0001 valor 0002
	* endereco 0002 valor 0003
	*/
	public void adicionaConstantesMemoria()
	{
		Vector tipos;
		Linha linha;
		LinhaDado labelLinha,linhaDado;
		String token;
		int endLabelLinha,indice;
		for(int i=0;i<linhas.size();i++)
		{
			linha=(Linha)linhas.get(i);
			if(linha.existeTipoToken("DB"))
			{
				tipos=linha.getTiposTokens();
				//Captura o label da linha
				indice=linha.getIndiceTipoToken("LABEL");
				token=linha.getToken(indice);
				labelLinha=labels.getLinhaDado(token);
				endLabelLinha=labelLinha.getEnderecoDecimal();

				//Soma 2 ao indice de DB para desprezar o token DB e o imediatamente após
				//porque este ja foi incluido
				for(int j=linha.getIndiceTipoToken("DB")+2;j<tipos.size();j++)
				{
					//captura o token que é do tipo PSEUDOLABEL
					if(((String)linha.getTipoToken(j)).equalsIgnoreCase("CONSTANTE"))
					{
						//captura o token que é do tipo CONSTANTE
						token=(String)linha.getToken(j);

						endLabelLinha++;
						linhaDado=new LinhaDado(endLabelLinha,token);
						linhasDados.add(linhaDado);
					}
				}
			}
		}
		ordenaLinhasDados();
	}

	/**
	* Identifica os tokens nas linhas que são do tipo PSEUDOLABEL e substitui
	* pelo endereço ou valor do label correspondente.
	* Se o PSEUDOLABEL possuir em seu primeiro byte o cerquilha (#) indicará
	* que o PSEUDOLABEL será substituido pelo endereço do label (valor imediato),
	* ou seja, pelo  do label. Senão o PSEUDOLABEL é substitui pelo valor do label.
	*/
	public boolean substituiPseudoLabel()
	{
		Vector tipos;
		Linha linha;
		LinhaDado label;
		String token;
		boolean imediato;
		for(int i=0;i<linhas.size();i++)
		{
			linha=(Linha)linhas.get(i);
			tipos=linha.getTiposTokens();
			for(int j=0;j<tipos.size();j++)
			{
				if(((String)tipos.get(j)).equalsIgnoreCase("PSEUDOLABEL"))
				{
					//captura o token que é do tipo PSEUDOLABEL
					token=(String)linha.getToken(j);

					//verifica se o PSEUDOLABEL é imediato, ou seja, se possui
					//cerquilha (#) no primeiro byte.
					if(token.substring(0,1).equals("#"))
					{
						imediato=true;
						//remove a cerquilha (#)
						token=token.substring(1,token.length());
					}
					else
						imediato=false;

					label=labels.getLinhaDado(token);
					if(label==null)
					{
						new Erro(modo).show("ERRO_PSEUDO_LABEL",linha.getNroLinha(),token);
						return false;
					}

					//captura o valor correspondente ao PSEUDOLABEL
					token="";
					if(imediato)
						token=token.concat(label.getEndereco());
					else
					{
						token=label.getValor();
						if(token==null)
						{
							new Erro(modo).show("MODO_PSEUDO_LABEL",linha.getNroLinha(),label.getNome());
							return false;
						}

						token=token.concat(label.getValor());
					}

					token=selecionaParteValor(linha,token,label);


					//seta o novo token e o novo tipo
					linha.setToken(j,token);
					linha.setTipoToken(j,"CONSTANTE");
				}
			}
		}
		return true;
	}

	/**
	* substitui os campos do codigo correspondente aos registradores e constantes.
	*/
	public boolean substituiCodigo()
	{
		if(substituiRegistradores())
		{
			if(substituiConstantes())
				return true;
		}
		return false;
	}

	/**
	* substitui os codigos referentes ao registradores
	*/
	public boolean substituiRegistradores()
	{
		Linha linha;
		Vector tipos;
		String codigoReg,pseudoCodigo;
		String[] campoEscrita;
		InstrucaoMontador instrucao;
		int indice,indiceReg=0;
		boolean resolvido;
		for(int i=0;i<linhas.size();i++)
		{
			//captura a linha
			linha=(Linha)linhas.get(i);

			//captura o vetor de tipos
			tipos=linha.getTiposTokens();
			for(int j=0;j<tipos.size();j++)
			{
				resolvido=false;

				//verifica se o token do indice j é do tipo REGISTRADOR
				if(linha.isTipoToken(j,"REGISTRADOR"))
				{
					//captura codigo correspondente ao registrador
					codigoReg=getCodigoRegistrador(linha.getNroLinha(),linha.getToken(j));

					//captura a instrucao associada da linha
					instrucao=arquitetura.getInstrucao(linha.getNomeInstrucao());

					//captura campos de escrita e montagem e codigos da instrucao
					campoEscrita=instrucao.getCamposEscrita();

					indice=0;
					while(!resolvido && indice<campoEscrita.length)
					{
						//Verifica se o campo do indice é um registrador
						if(linha.isCampoRegistrador(campoEscrita[indice]))
						{
							// captura o pseudoCodigo, ou seja, se RT -3,RS1 -1 e RS2 -2
							pseudoCodigo=linha.getPseudoCodigo(campoEscrita[indice]);

							// captura indice do codigo que possui o pseudo Codigo
							indiceReg=linha.getIndicePseudoCodigo(pseudoCodigo);

							// seignifica que o codigo nao eh pseudoCodigo
							if(indiceReg!=-1)
							{
						 		resolvido=true;

								//Atribui o novo codigo
								linha.setCodigo(indiceReg,codigoReg);
							}
						}
						indice++;
					}
					//Se nao existir campoEscrita em campoMontagem o arquivo de configuracao
					//tem erro
					if(! resolvido)
					{
						new Erro(modo).show("ERRO_ARQ_CAMPO",linha.getLinha());
						return false;
					}
				}
			}
		}
		return true;
	}

	/**
	* substitui os codigos referentes as constantes.
	*/
	public boolean substituiConstantes()
	{
		Linha linha;
		Vector tipos;
		String constante,pseudoCodigo,codigoConstante;
		String[] campoEscrita;
		InstrucaoMontador instrucao;
		int indice,indiceConst=0,nConstante;
		boolean resolvido;
		for(int i=0;i<linhas.size();i++)
		{
			//captura a linha
			linha=(Linha)linhas.get(i);
			nConstante=0;

			//captura o vetor de tipos
			tipos=linha.getTiposTokens();
			for(int j=0;j<tipos.size();j++)
			{
				//verifica se o token do indice j é do tipo CONSTANTE
				if(linha.isTipoToken(j,"CONSTANTE") && linha.isInstrucao())
				{
					nConstante++;

					//captura codigo correspondente ao registrador
					constante=linha.getToken(j);

					//captura a instrucao associada da linha
					instrucao=arquitetura.getInstrucao(linha.getNomeInstrucao());

					//captura campos de escrita e montagem e codigos da instrucao
					campoEscrita=instrucao.getCamposEscrita();

					//Enquanto o codigo da constante nao for resolvido repete
					while(!linha.isCodigoConstanteResolvido(nConstante))
					{
						indice=0;
						resolvido=false;
						while(!resolvido && indice<campoEscrita.length)
						{
							//Verifica se o campo do indice é uma constante
							if(linha.isCampoConstante(nConstante,campoEscrita[indice]))
							{
						 		// captura o pseudoCodigo
								pseudoCodigo=linha.getPseudoCodigo(campoEscrita[indice]);

								// captura indice do codigo que possui o pseudo Codigo
								indiceConst=linha.getIndicePseudoCodigo(pseudoCodigo);

								// significa que o codigo nao eh pseudoCodigo
								if(indiceConst!=-1)
								{
									resolvido=true;

									//verifica se o campo é um misto de constante e OPcode
									//ja foi verificado que o campo e constante, verifica
									//se também é opcode
									if(linha.isCampoOpcode(campoEscrita[indice]))
										codigoConstante=getCodigoConstanteOpcode(constante,pseudoCodigo);
									else
										codigoConstante=getCodigoConstante(constante,pseudoCodigo);

									//Atribui o novo codigo
									linha.setCodigo(indiceConst,codigoConstante);
								}
							}
							indice++;
						}
						//Se nao existir campoEscrita em campoMontagem o arquivo de configuracao
						//tem erro
						if(! resolvido)
						{
								new Erro(modo).show("ERRO_ARQ_CAMPO",linha.getLinha());
								return false;
						}
					}
				}
			}
		}
		return true;
	}

	/**
	* seleciona a parte do token informado conforme a instrucao que a linha
	* identifica.
	*/
	public String selecionaParteValor(Linha linha,String token,LinhaDado label)
	{
		String instrucaoAssociada,valor="",zero="0";
		String nomeInstrucao=linha.getNomeInstrucao();
		int valorDecimal=0,enderecoLinha=0;
		if(nomeInstrucao==null)
			return token;
		else
		{
			InstrucaoMontador instrucao=arquitetura.getInstrucao(nomeInstrucao);
			instrucaoAssociada=instrucao.getInstrucaoMontadorAssociada();

			valor=token;

			if(instrucaoAssociada.equalsIgnoreCase("LDH"))
			{
				//torna a constante de 4 Bytes se já não é.
				int tam=4-valor.length();
				for(int i=0;i<tam;i++)
					valor=zero.concat(valor);
				//captura apenas os 2 bytes mais significativos
				valor=valor.substring(0,2);

			}
			else if(instrucaoAssociada.equalsIgnoreCase("LDL"))
			{
				//torna a constante de 4 Bytes se já não é.
				int tam=4-valor.length();
				for(int i=0;i<tam;i++)
					valor=zero.concat(valor);
				//captura apenas os 2 bytes menos significativos
				valor=valor.substring(2,4);
			}
			else if(instrucaoAssociada.equalsIgnoreCase("JMPD")
				|| instrucaoAssociada.equalsIgnoreCase("JMPDC")
				|| instrucaoAssociada.equalsIgnoreCase("JSRD")
				|| instrucaoAssociada.equalsIgnoreCase("JSRDC")
				|| instrucaoAssociada.equalsIgnoreCase("JSRDP")
				|| instrucaoAssociada.equalsIgnoreCase("JMPD10")
				|| instrucaoAssociada.equalsIgnoreCase("JMPDC10")
				|| instrucaoAssociada.equalsIgnoreCase("JCMR")
				|| instrucaoAssociada.equalsIgnoreCase("JMPDMASK")
				|| instrucaoAssociada.equalsIgnoreCase("JSRDMASK")
				|| instrucaoAssociada.equalsIgnoreCase("JALR"))
			{
				//para as instrucoes acima é desprezado o token informado
				//porque estas tem sempre o valor imediato (endereco do label)
				valorDecimal=label.getEnderecoDecimal();
				enderecoLinha=linha.getEnderecoDecimal();

				valorDecimal=valorDecimal - enderecoLinha - 1; //-1 pq causa do tamanho da memoria

				valor=new Conversao().decimal_hexa(valorDecimal);
				//captura 3 bytes - significativos ou 12 bits - significativos
				valor=valor.substring(1,4);
			}
			else if(instrucaoAssociada.equalsIgnoreCase("JMPD8")
				|| instrucaoAssociada.equalsIgnoreCase("JMPD8C")
				|| instrucaoAssociada.equalsIgnoreCase("STMSK"))
			{
				//para as instrucoes acima é desprezado o token informado
				//porque estas tem sempre o valor imediato (endereco do label)
				valorDecimal=label.getEnderecoDecimal();
				enderecoLinha=linha.getEnderecoDecimal();

				valorDecimal=valorDecimal - enderecoLinha - 1; //-1 pq causa do tamanho da memoria

				valor=new Conversao().decimal_hexa(valorDecimal);
				//captura 2 bytes - significativos ou 8 bits - significativos
				valor=valor.substring(3,4);
			}
			return valor;
		}
	}

	/**
	* Retorna o codigo da constante conforme o pseudoCodigo.
	* Captura o indice inicial do pseudoCodigo referente a constante a
	* partir dai encontrar o primeiro separador "_" que é seguido do bit
	* Final da constante o segundo separador "_" seguido do bit Inicial
	* da constante. Por exemplo: constante= 0000000000001011
 	* Se Ct1_3_0 - bits de 3 a 0 da constante = 1011
 	* Se Ct1_1_0 - bits de 1 a 0 da constante = 11
	*/
	public String getCodigoConstante(String constante,String pseudoCodigo)
	{
		String zero="0";

		//torna a constante de 4 Bytes se já não é.
		int tam=4-constante.length();
		for(int i=0;i<tam;i++)
			constante=zero.concat(constante);

		//converte a constante que esta na base decimal para a base binaria de 16 bits
		String constanteBinaria=new Conversao().hexa_binario(constante,16);

		try
		{
			int inicio=pseudoCodigo.indexOf("C");
			//captura a primeira ocorrencia do separador _
			int separador1=pseudoCodigo.indexOf("_",inicio);
			//captura a segunda ocorrencia do separador _
			int separador2=pseudoCodigo.indexOf("_",separador1+1);
			//captura o número do bit final do pseudocodigo
			int bitFinal=Integer.valueOf(pseudoCodigo.substring(separador1+1,separador2)).intValue();
			//captura o número do bit inicial do pseudocodigo
			int bitInicial=Integer.valueOf(pseudoCodigo.substring(separador2+1,pseudoCodigo.length())).intValue();

			//O máximo é 4 bits por codigo
			if(bitFinal-bitInicial>4)
				return null;

			//inverte proque a string captura da esquerda para direita e os bits são da direita para esquerda
			bitInicial=16-bitInicial;
			bitFinal=16-(bitFinal+1);

			//Seleciona a parte da string entre o bitInicial e o bit Final;
			constanteBinaria=constanteBinaria.substring(bitFinal,bitInicial);
			//Converte a parte selecionada para 1 byte em hexadecimal
			return new Conversao().binario_hexa(constanteBinaria,1);
		}catch(Exception e){return null;}
	}


	/**
	* Retorna o codigo da constante concatenada com o opcode
	* conforme o pseudoCodigo.
	* Captura o valor do opcode identificando o indice inicial do pseudoCodigo
	* referente ao opcode e a partir dai encontra o primeiro separador "_",
	* depois deste encontra-se o valor do opcode
	* Por exemplo: OP_3 valor 3
	* Captura o valor da constante identificando o indice inicial do pseudoCodigo
	* referente a constante e a partir dai encontra o primeiro separador "_"
	* que é seguido do bit Final da constante o segundo separador "_" seguido do
	* bit Inicial da constante.
 	* Por exemplo: constante= 0000000000001011
 	* Se Ct1_3_2 - bits de 3 a 2 da constante = 10
 	* Se Ct1_1_0 - bits de 1 a 0 da constante = 11
 	* Por fim concatena o opcode com a constante na ordem em que aparecem
 	* no pseudoCodigo, se:
 	* OP e CT entao 2 bits - significativo do opcode concatenado com os 2bits
 	* - significativos da parte da constante selecionada.
 	* Por exemplo: OP_3-CT1_3_2 codigo em binario 1110 logo E
 	* CT e OP entao 2 bits - significativos da parte da constante selecionada
 	* concatenado com os 2 bits - significativo do opcode
 	* Por exemplo: CT1_3_2-OP_3 codigo em binario 1011 logo B
 	* Observação: A PARTE SELECIONADA DA CONSTANTE TEM TAMANHO IGUAL A 2 BITS
 	* NAO PODE SER SUPERIOR A ISTO.
	*/
	public String getCodigoConstanteOpcode(String constante,String pseudoCodigo)
	{
		String zero="0",codigo;
		int inicioOpcode,inicioConstante;
		int separador1,separador2,separador3;
		int bitFinal,bitInicial;

		//torna a constante de 4 Bytes se já não é.
		int tam=4-constante.length();
		for(int i=0;i<tam;i++)
			constante=zero.concat(constante);

		//converte a constante que esta na base decimal para a base binaria de 16 bits
		String constanteBinaria=new Conversao().hexa_binario(constante,16);

		try
		{
			inicioOpcode=pseudoCodigo.indexOf("O");
			inicioConstante=pseudoCodigo.indexOf("C");
			if(inicioOpcode<inicioConstante)
			{
				//se inicioOpcode menor que inicioConstante indica que OP-CT logo
				//primeiro 2 bits valor opcode depois 2 bits constante
				//OPCODE
				//captura a primeira ocorrencia do separador _
				separador1=pseudoCodigo.indexOf("_",inicioOpcode);
				//captura a segunda ocorrencia do separador _
				separador2=pseudoCodigo.indexOf("-",separador1+1);
				//captura valor opcode
				String opcode=pseudoCodigo.substring(separador1+1,separador2);
				//converte o opcode para binario retornando os 2 bits - significativos
				opcode=new Conversao().hexa_binario(opcode,2);
				//CONSTANTE
				inicioConstante=pseudoCodigo.indexOf("C");
				//captura a primeira ocorrencia do separador _
				separador1=pseudoCodigo.indexOf("_",inicioConstante);
				//captura a segunda ocorrencia do separador _
				separador2=pseudoCodigo.indexOf("_",separador1+1);
				//captura o número do bit final do pseudocodigo
				bitFinal=Integer.valueOf(pseudoCodigo.substring(separador1+1,separador2)).intValue();
				//captura o número do bit inicial do pseudocodigo
				bitInicial=Integer.valueOf(pseudoCodigo.substring(separador2+1,pseudoCodigo.length())).intValue();
				//O máximo é 2 bits por codigo
				if(bitFinal-bitInicial>2)
					return null;
				//inverte proque a string captura da esquerda para direita e os bits são da direita para esquerda
				bitInicial=16-bitInicial;
				bitFinal=16-(bitFinal+1);
				//seleciona a parte da string entre o bitInicial e o bit Final;
				constanteBinaria=constanteBinaria.substring(bitFinal,bitInicial);
				//CONCATENA
				//concatena primeiro 2 bits valor opcode depois 2 bits constante
				codigo=opcode+constanteBinaria;
			}
			else
			{
				//se inicioConstante menor que inicioOpcode indica que CT-OP logo
				//primeiro 2 bits constante depois 2 bits valor opcode
				//CONSTANTE
				inicioConstante=pseudoCodigo.indexOf("C");
				//captura a primeira ocorrencia do separador _
				separador1=pseudoCodigo.indexOf("_",inicioConstante);
				//captura a segunda ocorrencia do separador _
				separador2=pseudoCodigo.indexOf("_",separador1+1);
				//captura o número do bit final do pseudocodigo
				bitFinal=Integer.valueOf(pseudoCodigo.substring(separador1+1,separador2)).intValue();
				//captura o ocorrencia do separador -
				separador3=pseudoCodigo.indexOf("-",separador2+1);
				//captura o número do bit inicial do pseudocodigo
				bitInicial=Integer.valueOf(pseudoCodigo.substring(separador2+1,separador3)).intValue();
				//O máximo é 2 bits por codigo
				if(bitFinal-bitInicial>2)
					return null;
				//inverte proque a string captura da esquerda para direita e os bits são da direita para esquerda
				bitInicial=16-bitInicial;
				bitFinal=16-(bitFinal+1);
				//seleciona a parte da string entre o bitInicial e o bit Final;
				constanteBinaria=constanteBinaria.substring(bitFinal,bitInicial);
				//OPCODE
				//captura a primeira ocorrencia do separador _
				separador1=pseudoCodigo.indexOf("_",inicioOpcode);
				//captura valor opcode
				String opcode=pseudoCodigo.substring(separador1+1,pseudoCodigo.length());
				//converte o opcode para binario retornando os 2 bits - significativos
				opcode=new Conversao().hexa_binario(opcode,2);
				//CONCATENA
				//concatena primeiro 2 bits constante depois 2 bits valor opcode
				codigo=constanteBinaria+opcode;
			}
			//Converte o codigo para 1 byte em hexadecimal
			return new Conversao().binario_hexa(codigo,1);
		}catch(Exception e){return null;}
	}

	/**
	* captura o codigo correspondente ao registrador
	*/
	public String getCodigoRegistrador(int nlinha,String registrador)
	{
		int reg=-1;

		//remove o R inicial do registrador
		String registradorMod=registrador.substring(1,registrador.length());

		//captura o decimal que representa o registrador
		try
		{
			reg =Integer.valueOf(registradorMod).intValue();
		}catch(NumberFormatException e){new Erro(modo).show("ERRO_REG",nlinha,registrador);}

		switch (reg)
		{
    	case (0):
        return "0";
    	case (1):
        return "1";
    	case (2):
        return "2";
    	case (3):
        return "3";
    	case (4):
        return "4";
    	case (5):
        return "5";
    	case (6):
        return "6";
    	case (7):
        return "7";
    	case (8):
        return "8";
    	case (9):
        return "9";
    	case (10):
        return "A";
    	case (11):
        return "B";
    	case (12):
        return "C";
    	case (13):
        return "D";
    	case (14):
        return "E";
    	case (15):
        return "F";
    }
    new Erro(modo).show("ERRO_REG",nlinha,registrador);
    return null;
	}

	/**
	* ordena o vetor que contem as linhas de dados em funcao do endereco
	* das linhas
	*/
	public void ordenaLinhasDados()
	{
		LinhaDado linha1,linha2;
		int end1,end2;

		for(int i=0;i<(linhasDados.size()-1);i++)
		{
			for(int j=0;j<(linhasDados.size()-1-i);j++)
			{
				linha1=(LinhaDado)linhasDados.get(j);
				linha2=(LinhaDado)linhasDados.get(j+1);
				end1=linha1.getEnderecoDecimal();
				end2=linha2.getEnderecoDecimal();

				if(end1>end2)
				{
					linhasDados.set(j,linha2);
					linhasDados.set(j+1,linha1);
				}
			}
		}
	}

}