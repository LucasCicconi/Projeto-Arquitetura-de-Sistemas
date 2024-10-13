/*
* @(#)Execucao.java  1.0  30/03/2001
*	
* Aline Vieira de Mello
* Pontifícia Universidade Católica do Rio Grande do Sul - Faculdade de Informática
*/

import java.awt.event.*;
import java.awt.*;
import java.math.*;
import javax.swing.*;

/**
* <i>Execução</i> executa as informações contidas na lista Memória.
* Extende a classe Thread.
*/
public class Execucao extends Thread
{
	private Simulador sim;
	private int contlinha;
	private double tempo;
	private boolean halt;
	private boolean zero,carry,negativo,overflow,geral;
	private Arquitetura arq;
	private NodoArqMont instAtual;
	private String linha;
	private String codigo,codigo1,codigo2,codigo3,codigo4;
	private	String valorhex1,valorhex2,resultadohex;
	private	String valorbin1,valorbin2,resultadobin;
	private int valordec1,valordec2,resultadodec;
		
	public Execucao(Simulador sim,Arquitetura arq)
	{
		super();
		this.sim=sim;
		this.arq=arq;
		halt=false;
	}
	
	/**
	* Seta um valor boolean para Halt.
	*/
	public void setHalt(boolean b){halt=b;}
	
	/**
	* Executa as instruções contidas na memória,
	*/
	public void run()
	{
		NodoMemoria inst=sim.getMemoria().getInicio();	
		//Se for clicado ou pressionado o botão STEP executa uma instrução.
		if(sim.getJanela().getStep() && !halt)
		{
			fetch();
			executa();

			if (sim.getJanela().getBreakpoint())
			{
				String pc=capturaValor("PC");
				String linhabreakpoint=sim.getJanela().getLinhaBreakpoint();
				if (linhabreakpoint.equalsIgnoreCase(pc))
				{
					sim.getJanela().setBreakpoint(false);
					sim.getJanela().stop();
				}
			}					

		}
		//Se for clicado ou pressionado o botão RUN executa todas
		//instrução com um intervalo de tempo definido pela seleção do
		//CheckBox rápido, normal ou lento.
		if(sim.getJanela().getRun())
		{
			while(sim.getJanela().getRun() && !halt)
			{
				fetch(); 
				executa();
				tempo=sim.getJanela().getTempo();
				try
				{
					sleep((int)tempo*1000);
				}catch(InterruptedException e){}

				if (sim.getJanela().getBreakpoint())
				{
					String pc=capturaValor("PC");
					String linhabreakpoint=sim.getJanela().getLinhaBreakpoint();
					if (linhabreakpoint.equalsIgnoreCase(pc))
					{
						//sim.getJanela().setBreakpoint(false);
						sim.getJanela().stop();
						break;
					}
				}					
		
			}
		}
	}

	/**
	* Captura o valor da memória que corresponde ao endereço armazenado
	* em PC.
	*/
	public void fetch()      
	{ 
		//Captura o PC e o converte para decimal.
		valorhex1=capturaValor("PC");

		//Captura a instrucao correspondente ao endereco do PC.
		NodoMemoria inst=sim.getMemoria().getMemoria(valorhex1);


		if(inst!=null)
		{
			codigo=inst.getInstrucao();
			sim.getJanela().getReg("IR").setValReg(codigo);
			linha=inst.getLinha();
		}
		else
		{
			codigo="0000";
			linha="";
		}
		codigo1=(codigo.substring(0,1));
		codigo2=(codigo.substring(1,2));
		codigo3=(codigo.substring(2,3));
		codigo4=(codigo.substring(3,4));
			
		sim.getJanela().getReg("PC").incrementa();
	}


	/**
	* Seleciona e executa a instrução conforme o código armazenado
	* na memória.
	*/
	public void executa()
	{
		String inst;
		String cod[];
		cod=new String[4];		
		
		cod[0]=codigo1;
		cod[1]=codigo2;
		cod[2]=codigo3;
		cod[3]=codigo4;

		instAtual=arq.selecionaInstrucao(cod);
		
		if(instAtual!=null)
		{
			//Posiciona o codigo conforme os campos do simulador.
			cod = arq.posicionaCodigo(instAtual,cod);
			
			codigo1 = cod[0];
			codigo2 = cod[1];
			codigo3 = cod[2];
			codigo4 = cod[3];

			//Incrementa o numero de clocks, conforme a instrucao executada
			sim.getJanela().incrementaNclock(instAtual.getNclock());
			
			//incrementa o numero de instrucoes
			sim.getJanela().incrementaNinstrucao();

			inst = instAtual.getInstrucao();
			
			if(inst.equalsIgnoreCase("ADD"))
				add();
			else if(inst.equalsIgnoreCase("SUB"))
				sub();
			else if(inst.equalsIgnoreCase("AND"))
				and();
			else if(inst.equalsIgnoreCase("OR"))
				or();
			else if(inst.equalsIgnoreCase("XOR"))
				xor();
			else if(inst.equalsIgnoreCase("ADDI"))
				addi();
			else if(inst.equalsIgnoreCase("SUBI"))
				subi();
			else if(inst.equalsIgnoreCase("LDL"))
				ldl();
			else if(inst.equalsIgnoreCase("LDH"))
				ldh();
			else if(inst.equalsIgnoreCase("SL"))
				sl();
			else if(inst.equalsIgnoreCase("SR"))
				sr();
			else if(inst.equalsIgnoreCase("NOT"))
				not();
			else if(inst.equalsIgnoreCase("POP"))
				pop();
			else if(inst.equalsIgnoreCase("PUSH"))
				push();
			else if(inst.equalsIgnoreCase("LD"))
				ld();
			else if(inst.equalsIgnoreCase("ST"))
				st();
			else if(inst.equalsIgnoreCase("LD2"))
				ld2();
			else if(inst.equalsIgnoreCase("ST2"))
				st2();
			else if(inst.equalsIgnoreCase("LDRI"))
				ldri();
			else if(inst.equalsIgnoreCase("STRI"))
				stri();
			else if(inst.equalsIgnoreCase("CMP"))
				cmp();
			else if(inst.equalsIgnoreCase("DIF"))
				dif();
			else if(inst.equalsIgnoreCase("EQL"))
				eql();
			else if(inst.equalsIgnoreCase("SUP"))
				sup();
			else if(inst.equalsIgnoreCase("SUP2"))
				sup2();
			else if(inst.equalsIgnoreCase("INF"))
				inf();
			else if(inst.equalsIgnoreCase("INF2"))
				inf2();
			else if(inst.equalsIgnoreCase("LDSP"))
				ldsp();
			else if(inst.equalsIgnoreCase("STSP"))
				stsp();
			else if(inst.equalsIgnoreCase("ADDSP"))
				addsp();
			else if(inst.equalsIgnoreCase("NOP"))
				nop();
			else if(inst.equalsIgnoreCase("RTS"))
				rts();
			else if(inst.equalsIgnoreCase("RTSP"))
				rtsp();
			else if(inst.equalsIgnoreCase("HALT"))
				halt();
			else if(inst.equalsIgnoreCase("JMP"))
				jmp();
			else if(inst.equalsIgnoreCase("JMPR"))
				jmpr();
			else if(inst.equalsIgnoreCase("JMPRC"))
				jmprc();
			else if(inst.equalsIgnoreCase("JMPD"))
				jmpd();
			else if(inst.equalsIgnoreCase("JMPDMASK"))
				jmpdMask();
			else if(inst.equalsIgnoreCase("JPRGMASK"))
				jprgMask();
			else if(inst.equalsIgnoreCase("JPRGRMASK"))
				jprgrMask();
			else if(inst.equalsIgnoreCase("JMPC"))
				jmpc();
			else if(inst.equalsIgnoreCase("JMPD8"))
				jmpd8();
			else if(inst.equalsIgnoreCase("JMPDC"))
				jmpdc();
			else if(inst.equalsIgnoreCase("JMPDC8"))
				jmpdc8();
			else if(inst.equalsIgnoreCase("JMPD10"))
				jmpd10();
			else if(inst.equalsIgnoreCase("JMPDC10"))
				jmpdc10();
			else if(inst.equalsIgnoreCase("JSR"))
				jsr();
			else if(inst.equalsIgnoreCase("JSRR"))
				jsrr();
			else if(inst.equalsIgnoreCase("JSRC"))
				jsrc();
			else if(inst.equalsIgnoreCase("JSRRC"))
				jsrrc();
			else if(inst.equalsIgnoreCase("JSRD"))
				jsrd();
			else if(inst.equalsIgnoreCase("JSRDMASK"))
				jsrdMask();
			else if(inst.equalsIgnoreCase("JSRDP"))
				jsrdp();
			else if(inst.equalsIgnoreCase("JSRDC"))
				jsrdc();
			else if(inst.equalsIgnoreCase("JN"))
				jn();
			else if(inst.equalsIgnoreCase("JV"))
				jv();
			else if(inst.equalsIgnoreCase("JC"))
				jc();
			else if(inst.equalsIgnoreCase("JZ"))
				jz();
			else if(inst.equalsIgnoreCase("JNR"))
				jnr();
			else if(inst.equalsIgnoreCase("JVR"))
				jvr();
			else if(inst.equalsIgnoreCase("JCR"))
				jcr();
			else if(inst.equalsIgnoreCase("JZR"))
				jzr();
			else if(inst.equalsIgnoreCase("JND"))
				jnd();
			else if(inst.equalsIgnoreCase("JVD"))
				jvd();
			else if(inst.equalsIgnoreCase("JCD"))
				jcd();
			else if(inst.equalsIgnoreCase("JZD"))
				jzd();
			else if(inst.equalsIgnoreCase("SL0"))
				sl0();
			else if(inst.equalsIgnoreCase("SL1"))
				sl1();
			else if(inst.equalsIgnoreCase("SR0"))
				sr0();
			else if(inst.equalsIgnoreCase("SR1"))
				sr1();
			else if(inst.equalsIgnoreCase("INC"))
				inc();
			else if(inst.equalsIgnoreCase("DEC"))
				dec();
			else if(inst.equalsIgnoreCase("NEG"))
				neg();
			else if(inst.equalsIgnoreCase("MOV"))
				mov();
			else if(inst.equalsIgnoreCase("SL0C"))
				sl0c();
			else if(inst.equalsIgnoreCase("SL1C"))
				sl1c();
			else if(inst.equalsIgnoreCase("SR0C"))
				sr0c();
			else if(inst.equalsIgnoreCase("SR1C"))
				sr1c();
			else if(inst.equalsIgnoreCase("RL"))
				rl();
			else if(inst.equalsIgnoreCase("RR"))
				rr();
			else if(inst.equalsIgnoreCase("RLR"))
				rlr();
			else if(inst.equalsIgnoreCase("RRR"))
				rrr();
			else if(inst.equalsIgnoreCase("RLRC"))
				rlrc();
			else if(inst.equalsIgnoreCase("RRRC"))
				rrrc();
			else if(inst.equalsIgnoreCase("INCI"))
				inci();
			else if(inst.equalsIgnoreCase("DECI"))
				deci();
			else if(inst.equalsIgnoreCase("SWI"))
				swi();
			else if(inst.equalsIgnoreCase("SWIP"))
				swip();
			else if(inst.equalsIgnoreCase("SKPEQ"))
				skpeq();
			else if(inst.equalsIgnoreCase("SKPNE"))
				skpne();
			else if(inst.equalsIgnoreCase("SKPEQI"))
				skpeqi();
			else if(inst.equalsIgnoreCase("SKPNEI"))
				skpnei();
			else if(inst.equalsIgnoreCase("JALR"))
				jalr();
			else if(inst.equalsIgnoreCase("JCMR"))
				jcmr();
			else if(inst.equalsIgnoreCase("JCMRG"))
				jcmrg();
			else if(inst.equalsIgnoreCase("JCMRGR"))
				jcmrgr();
			else if(inst.equalsIgnoreCase("PUSHRG"))
				pushrg();
			else if(inst.equalsIgnoreCase("POPRG"))
				poprg();
			else if(inst.equalsIgnoreCase("JSRG"))
				jsrg();
			else if(inst.equalsIgnoreCase("JSRGMASK"))
				jsrgMask();
			else if(inst.equalsIgnoreCase("JSRGR"))
				jsrgr();
			else if(inst.equalsIgnoreCase("JSRGRMASK"))
				jsrgrMask();
			else if(inst.equalsIgnoreCase("SLT"))
				slt();
			else if(inst.equalsIgnoreCase("IGNORE"))
				ignore();
			else if(inst.equalsIgnoreCase("STFM"))
				stfm();
			else if(inst.equalsIgnoreCase("STMSK"))
				stmsk();
			else if(inst.equalsIgnoreCase("RSTFM"))
				rstfm();
			else
				JOptionPane.showMessageDialog(sim.getJanela(),"Instrucao "+inst+" Inexistente no Simulador.","INFORMATION", JOptionPane.INFORMATION_MESSAGE);
	
		}
		else
		{
			sim.getJanela().getBrun().setEnabled(false);
			sim.getJanela().getBstep().setEnabled(false);
			sim.getJanela().getBstop().setEnabled(false);
			sim.getJanela().getBpause().setEnabled(false);
			sim.getJanela().getBreset().setEnabled(true);
			JOptionPane.showMessageDialog(sim.getJanela(),"Código Inexistente "+codigo,"ERRO", JOptionPane.ERROR_MESSAGE);
		}

		
		/* Seleciona na memória a próxima posição apontada por PC */
		
		//Captura o PC e o converte para decimal.
		valorhex1=capturaValor("PC");
		valordec1=sim.getConversao().hexa_decimal(valorhex1);
		
		//Seleciona a linha da tabela Memória que corresponde ao valor.
		sim.getJanela().getTabelaMemoria().setRowSelectionInterval(valordec1,valordec1);
		
		//Primeira posicao visível da tabela.
		int pos_visivel = sim.getJanela().getScrollPaneMemoria().getVerticalScrollBar().getValue() / 17;
		
		//Corre a barra conforme a posição da memória OBS: 17 é o numero de linhas visiveis da tabela memória. 
		if((valordec1 > (pos_visivel + 17)) || (valordec1 < pos_visivel)) 
			sim.getJanela().getScrollPaneMemoria().getVerticalScrollBar().setValue((sim.getJanela().getScrollPaneMemoria().getVerticalScrollBar().getMaximum() / sim.getTamanhoMemoria()) * valordec1);
	}
	
		
	/**
	* ADD RT,RS1,RS2  {RT <- RS1 + RS2}
	* OP1 RT RS1 RS2
	* Captura o valor do codigo3 e do codigo4, soma e armazena
	* o resultado no registrador representado pelo codigo2.
	*/
	public void add()
	{
		valorhex1=capturaValor(codigo3);
		valorhex2=capturaValor(codigo4);
		resultadohex=soma(valorhex1,valorhex2);
		sim.getJanela().getReg("R"+codigo2).setValReg(resultadohex);
		tratandoFlags("ADD");
	}
	
	/**
	* ADDI RT,CT1  {RT <- RT + ("00000000"& constante)}
	* OP1 RT CT1_7_4 CT1_3_0
	* Captura o valor do codigo2 e soma com o codigo3 concatenado com o codigo4
	* e armazena o resultado no registrador representado pelo codigo2.
	*/
	public void addi()
	{
		valorhex1=capturaValor(codigo2);
		valorhex2="00"+codigo3+codigo4;
		resultadohex=soma(valorhex1,valorhex2);
		sim.getJanela().getReg("R"+codigo2).setValReg(resultadohex);
	  	tratandoFlags("ADDI");
	}

	/**
	* AND RT,RS1,RS2  {RT <- RS1 and RS2}
	* OP1 RT RS1 RS2
	* Captura o valor do codigo3 e do codigo4, realiza a operação AND
	* e armazena o resultado no registrador representado pelo codigo2.
	*/
	public void and()
	{
		String zero="0";
		int tam;
		
		valorhex1=capturaValor(codigo3);
		valorhex2=capturaValor(codigo4);
		valordec1=sim.getConversao().hexa_decimal(valorhex1);
		valordec2=sim.getConversao().hexa_decimal(valorhex2);
		valorbin1=sim.getConversao().decimal_binario(valordec1);
		valorbin2=sim.getConversao().decimal_binario(valordec2);
		
		resultadobin=e(valorbin1,valorbin2);
		resultadodec=sim.getConversao().binario_decimal(resultadobin);
		resultadohex=sim.getConversao().decimal_hexa(resultadodec);
		sim.getJanela().getReg("R"+codigo2).setValReg(resultadohex);
	
  	tratandoFlags("AND");
	}

	/**
	* ANDI RT,CT1  {RT <- RT and (ext_sinal & constante)}
	* OP1 RT CT1_7_4 CT1_3_0
	* Captura o valor do codigo2 e realiza a operação AND com a
	* extensão do sinal do codigo3 concatenado com o codigo4 e
	* armazena o resultado no registrador representado pelo codigo2.
	*/
	public void andi()
	{
		String zero="0";
		int tam;
		
		valorhex1=capturaValor(codigo2);
		valorhex2=ext_sinal(codigo3);
		valordec1=sim.getConversao().hexa_decimal(valorhex1);
		valordec2=sim.getConversao().hexa_decimal(valorhex2);
		valorbin1=sim.getConversao().decimal_binario(valordec1);
		valorbin2=sim.getConversao().decimal_binario(valordec2);
		
		resultadobin=e(valorbin1,valorbin2);
		resultadodec=sim.getConversao().binario_decimal(resultadobin);
		resultadohex=sim.getConversao().decimal_hexa(resultadodec);
		sim.getJanela().getReg("R"+codigo2).setValReg(resultadohex);
		
		tratandoFlags("ANDI");
	}

	/**
	* JCMR CT1,mask  {if(mask) PC <- PC + (ext_sinal & codigo3 & codigo4)}
	* OP1 CT2_3_0 CT1_7_4 CT1_3_0
	* Se os bits ativos da mask(codigo2) tiverem seus correspondentes flags também ativos
	* então PC <- PC + extensão de sinal do codigo3 e codigo4.
	* Exemplo: codigo2= 1011 (mascara de NZCV) logo para ocorrer o jmp o flag
	* negativo deve estar ativado, o flag de carry ativado e o flag de overflow ativado.
	*/
	public void jcmr()
	{
		valordec1=sim.getConversao().hexa_decimal(codigo2); //mascara
		valorbin1=sim.getConversao().decimal_binario(valordec1);
		//captura os 4 bits menos significativos pq a conversao para binario retorna 16 bits
		valorbin1=valorbin1.substring(12,16);
		
		if(testaMascara(valorbin1))
		{
			valorhex1=capturaValor("PC");
			valorhex2=ext_sinal(codigo3);
			valordec2=sim.getConversao().hexa_decimal(valorhex2); //Valor
			if(valordec2>32767)
				valordec2=-(65535-valordec2+1);
			valorhex2=sim.getConversao().decimal_hexa(valordec2); //Valor
			resultadohex=soma(valorhex1,valorhex2);
			
			sim.getJanela().getReg("PC").setValReg(resultadohex);

			tratandoFlags("JCMR");
		}	
		else
		{
			//caso a instrucao não seja executada o número de ciclos de clock é decrementado
			//em 2.
			sim.getJanela().decrementaNclock(2);
		}	
	}

	/**
	* JCMRG RT,mask  {if(mask) PC <- RT}
	* OP1 CT1_3_0 RT OP2
	* Se os bits ativos da mask(codigo2) tiverem seus correspondentes flags também ativos
	* então PC <- codigo3.
	* Exemplo: codigo2= 1011 (mascara de NZCV) logo para ocorrer o jmp o flag
	* negativo deve estar ativado, o flag de carry ativado e o flag de overflow ativado.
	*/
	public void jcmrg()
	{
		valordec1=sim.getConversao().hexa_decimal(codigo2);//mascara
		valorbin1=sim.getConversao().decimal_binario(valordec1);
		//captura os 4 bits menos significativos pq a conversao para binario retorna 16 bits
		valorbin1=valorbin1.substring(12,16);
		
		if(testaMascara(valorbin1))
		{
			valorhex1=capturaValor("PC");
			valorhex2=capturaValor(codigo3);
			valordec2=sim.getConversao().hexa_decimal(valorhex2); //Valor
			
			if(valordec2>32767)
				valordec2=-(65535-valordec2+1);
	
			resultadodec=valordec2;
			resultadohex=sim.getConversao().decimal_hexa(resultadodec);
			sim.getJanela().getReg("PC").setValReg(resultadohex);

			tratandoFlags("JCMRG",resultadohex);
		}
		else
		{
			//caso a instrucao não seja executada o número de ciclos de clock é decrementado
			//em 2.
			sim.getJanela().decrementaNclock(1);
		}	
	}

	/**
	* JCMRGR RT,mask  {if(mask) PC <- PC + RT}
	* OP1 CT1_3_0 RT OP2
	* Se os bits ativos da mask(codigo2) tiverem seus correspondentes flags também ativos
	* então PC <- PC + codigo3.
	* Exemplo: codigo2= 1011 (mascara de NZCV) logo para ocorrer o jmp o flag
	* negativo deve estar ativado, o flag de carry ativado e o flag de overflow ativado.
	*/
	public void jcmrgr()
	{
		int tam;
		String zero="0";

		valordec1=sim.getConversao().hexa_decimal(codigo2);
		valorbin1=sim.getConversao().decimal_binario(valordec1);
		//captura os 4 bits menos significativos pq a conversao para binario retorna 16 bits
		valorbin1=valorbin1.substring(12,16);
		
		if(testaMascara(valorbin1))
		{
			valorhex1=capturaValor("PC");
			valorhex2=capturaValor(codigo3);
			valordec2=sim.getConversao().hexa_decimal(valorhex2); //Valor
			if(valordec2>32767)
				valordec2=-(65535-valordec2+1);
			valorhex2=sim.getConversao().decimal_hexa(valordec2); //Valor
			resultadohex=soma(valorhex1,valorhex2);

			sim.getJanela().getReg("PC").setValReg(resultadohex);

			tratandoFlags("JCMRGR");
		}
		else
		{
			//caso a instrucao não seja executada o número de ciclos de clock é decrementado
			//em 2.
			sim.getJanela().decrementaNclock(2);
		}	
	}

	/**
	* JSRDP constante  {SP <- SP + 1(INC) ou SP <- SP - 1(dec); PMEM(SP)<- PC; PC <- PC + (ext_sinal & constante)}
	* OP1 CT1_11_8 CT1_7_4 CT1_3_0
	* Realiza um jump incondicional a uma subrotina com SP pré-incrementado ou 
	* pré-decrementado, armazenando o valor de PC na posicao de memória de SP
	* e armazenando em PC o seu valor somado ao valor do deslocamento(12 bits) 
	* com a extensão do sinal.O SP posteriormente incrementado.
	*/
	public void jsrdp()
	{
		if(sim.getMovPilha().equalsIgnoreCase("INC"))
			sim.getJanela().getReg("SP").incrementa();
		else
			sim.getJanela().getReg("SP").decrementa();

		valorhex1=capturaValor("PC");
		valorhex2=capturaValor("SP");

		String linha[]={valorhex2,valorhex1,"DADOS"};
		if(sim.isHarvard())
		{
			sim.getMemoriaDados().setMemoria(linha);
			sim.getJanela().getTabelaMemoriaDados().setLinha(linha);
		}
		else
		{
			sim.getMemoria().setMemoria(linha);
			sim.getJanela().getTabelaMemoria().setLinha(linha);
		}

		valorhex2=ext_sinal(codigo2);
		resultadohex=soma(valorhex1,valorhex2);
		sim.getJanela().getReg("PC").setValReg(resultadohex);
		
		tratandoFlags("JSRDP");
	}

	/**
	* JSRG RS1  {SP <- SP + 1(INC) ou SP <- SP - 1(dec); PMEM(SP)<- PC; PC <- RS1}
	* OP1 OP2 RS1 OP3
	* Pré-incrementa ou pré-decrementa o registrador SP, armazena
	* na posição de memória apontada por SP o valor do registrador PC e
	* PC recebe o valor do registrador apontado pelo codigo3.
	* [SP <- SP-1(DEC) ou SP <- SP+1(INC)]; PMEM(SP)<-PC; PC<-reg do codigo3;
	*/
	public void jsrg()
	{
		if(sim.getMovPilha().equalsIgnoreCase("DEC"))
			sim.getJanela().getReg("SP").decrementa();
		else
			sim.getJanela().getReg("SP").incrementa();

		valorhex1=capturaValor("PC");
		valorhex2=capturaValor("SP");
	
		String linha[]={valorhex2,valorhex1,"DADOS"};
		if(sim.isHarvard())
		{
			sim.getMemoriaDados().setMemoria(linha);
			sim.getJanela().getTabelaMemoriaDados().setLinha(linha);
		}
		else
		{
			sim.getMemoria().setMemoria(linha);
			sim.getJanela().getTabelaMemoria().setLinha(linha);
		}
	
		valorhex2=capturaValor(codigo3);
		sim.getJanela().getReg("PC").setValReg(valorhex2);

		tratandoFlags("JSRG",valorhex1);
	}

	/**
	* JSRGMASK RS1  {if mask SP <- SP + 1(INC) ou SP <- SP - 1(dec); PMEM(SP)<- PC; PC <- RS1}
	* OP1 OP2 RS1 OP3
	* Pré-incrementa ou pré-decrementa o registrador SP, armazena
	* na posição de memória apontada por SP o valor do registrador PC e
	* PC recebe o valor do registrador apontado pelo codigo3.
	*/
	public void jsrgMask()
	{
		if(testaMascara8bits())
		{
			if(sim.getMovPilha().equalsIgnoreCase("DEC"))
				sim.getJanela().getReg("SP").decrementa();
			else
				sim.getJanela().getReg("SP").incrementa();
	
			valorhex1=capturaValor("PC");
			valorhex2=capturaValor("SP");
		
			String linha[]={valorhex2,valorhex1,"DADOS"};
			if(sim.isHarvard())
			{
				sim.getMemoriaDados().setMemoria(linha);
				sim.getJanela().getTabelaMemoriaDados().setLinha(linha);
			}
			else
			{
				sim.getMemoria().setMemoria(linha);
				sim.getJanela().getTabelaMemoria().setLinha(linha);
			}
		
			valorhex2=capturaValor(codigo3);
			sim.getJanela().getReg("PC").setValReg(valorhex2);
	
			tratandoFlags("JSRGMASK",valorhex1);
		}
	}

	/**
	* JSRGR RS1  {SP <- SP + 1(INC) ou SP <- SP - 1(dec); PMEM(SP)<- PC; PC <- PC + RS1}
	* OP1 OP2 RS1 OP3
	* Pré-incrementa ou pré-decrementa o registrador SP, armazena
	* na posição de memória apontada por SP o valor do registrador PC e
	* PC recebe o valor do PC somado ao do registrador apontado pelo codigo3.
	* [SP <- SP-1(DEC) ou SP <- SP+1(INC)]; PMEM(SP)<-PC; PC<-PC+reg do codigo3;
	*/
	public void jsrgr()
	{
		if(sim.getMovPilha().equalsIgnoreCase("DEC"))
			sim.getJanela().getReg("SP").decrementa();
		else
			sim.getJanela().getReg("SP").incrementa();

		valorhex1=capturaValor("PC");
		valorhex2=capturaValor("SP");
	
		String linha[]={valorhex2,valorhex1,"DADOS"};
		if(sim.isHarvard())
		{
			sim.getMemoriaDados().setMemoria(linha);
			sim.getJanela().getTabelaMemoriaDados().setLinha(linha);
		}
		else
		{
			sim.getMemoria().setMemoria(linha);
			sim.getJanela().getTabelaMemoria().setLinha(linha);
		}
		valorhex2=capturaValor(codigo3);
		resultadohex=soma(valorhex1,valorhex2);
		sim.getJanela().getReg("PC").setValReg(resultadohex);

		tratandoFlags("JSRGR");
	}


	/**
	* JSRGRMASK RS1  {if maskara SP <- SP + 1(INC) ou SP <- SP - 1(dec); PMEM(SP)<- PC; PC <- PC + RS1}
	* OP1 OP2 RS1 OP3
	* Pré-incrementa ou pré-decrementa o registrador SP, armazena
	* na posição de memória apontada por SP o valor do registrador PC e
	* PC recebe o valor do PC somado ao do registrador apontado pelo codigo3.
	* [SP <- SP-1(DEC) ou SP <- SP+1(INC)]; PMEM(SP)<-PC; PC<-PC+reg do codigo3;
	*/
	public void jsrgrMask()
	{
		if(testaMascara8bits())
		{
	
			if(sim.getMovPilha().equalsIgnoreCase("DEC"))
				sim.getJanela().getReg("SP").decrementa();
			else
				sim.getJanela().getReg("SP").incrementa();
	
			valorhex1=capturaValor("PC");
			valorhex2=capturaValor("SP");
		
			String linha[]={valorhex2,valorhex1,"DADOS"};
			if(sim.isHarvard())
			{
				sim.getMemoriaDados().setMemoria(linha);
				sim.getJanela().getTabelaMemoriaDados().setLinha(linha);
			}
			else
			{
				sim.getMemoria().setMemoria(linha);
				sim.getJanela().getTabelaMemoria().setLinha(linha);
			}
			valorhex2=capturaValor(codigo3);
			resultadohex=soma(valorhex1,valorhex2);
			sim.getJanela().getReg("PC").setValReg(resultadohex);
	
			tratandoFlags("JSRGRMASK");
		}
	}


	/**
	* LDH RT, CT1   {RT <- constanteHigh & RT_low }
	* OP RT CT1_7_4 CT1_3_4
	* Captura o valor da parte alta do codigo2 e concatena com o valor
	* armazenado no codigo3 e codigo4.O resultado da concatenação é
	* armazenado no registrador representado pelo codigo2.
	*/
	public void ldh()
	{
		valorhex1=capturaValor(codigo2);
		valorhex2=codigo3.concat(codigo4);
		resultadohex=valorhex1.substring(2,4);
		resultadohex=valorhex2.concat(resultadohex);
		sim.getJanela().getReg("R"+codigo2).setValReg(resultadohex);
		
		tratandoFlags("LDH",resultadohex);
	}
	
	/**
	* LDL RT, CT1   {RT <- RT_high & constanteLOW}
	* OP RT CT1_7_4 CT1_3_4
	* Captura o valor da parte baixa do codigo2 e concatena com o valor
	* armazenado no codigo3 e codigo4.O resultado da concatenação é
	* armazenado no registrador representado pelo codigo2.
	*/
	public void ldl()
	{
		valorhex1=capturaValor(codigo2);
		valorhex2=codigo3.concat(codigo4);
		resultadohex=valorhex1.substring(0,2);
		resultadohex=resultadohex.concat(valorhex2);
		sim.getJanela().getReg("R"+codigo2).setValReg(resultadohex);
		
		tratandoFlags("LDL",resultadohex);
	}


	/**
	* OR RT,RS1,RS2  {RT <- RS1 or RS2}
	* OP1 RT RS1 RS2
	* Captura o valor do codigo3 e do codigo4, realiza a operação OR
	* e armazena o resultado no registrador representado pelo codigo2.
	*/
	public void or()
	{
		String zero="0";
		int tam;
		
		valorhex1=capturaValor(codigo3);
		valorhex2=capturaValor(codigo4);
		valordec1=sim.getConversao().hexa_decimal(valorhex1);
		valordec2=sim.getConversao().hexa_decimal(valorhex2);
		valorbin1=sim.getConversao().decimal_binario(valordec1);
		valorbin2=sim.getConversao().decimal_binario(valordec2);
		
		resultadobin=or(valorbin1,valorbin2);
		resultadodec=sim.getConversao().binario_decimal(resultadobin);
		resultadohex=sim.getConversao().decimal_hexa(resultadodec);
		sim.getJanela().getReg("R"+codigo2).setValReg(resultadohex);
		
		tratandoFlags("OR");
	}

	/**
	* ORI RT,CT1  {RT <- RT or (ext_sinal & constante)}
	* OP1 RT CT1_7_4 CT1_3_0
	* Captura o valor do codigo2 e realiza a operação OR com a
	* extensão do sinal do codigo3 concatenado com o codigo4 e
	* armazena o resultado no registrador representado pelo codigo2.
	*/
	public void ori()
	{
		String zero="0";
		int tam;
		
		valorhex1=capturaValor(codigo2);
		valorhex2=ext_sinal(codigo3);
		valordec1=sim.getConversao().hexa_decimal(valorhex1);
		valordec2=sim.getConversao().hexa_decimal(valorhex2);
		valorbin1=sim.getConversao().decimal_binario(valordec1);
		valorbin2=sim.getConversao().decimal_binario(valordec2);
		
		resultadobin=or(valorbin1,valorbin2);
		resultadodec=sim.getConversao().binario_decimal(resultadobin);
		resultadohex=sim.getConversao().decimal_hexa(resultadodec);
		sim.getJanela().getReg("R"+codigo2).setValReg(resultadohex);
		
		tratandoFlags("ORI");
	}

	/**
	* POPRG RS1  {RS1 <- PMEM(SP); SP<-SP+1(DEC) ou SP<-SP-1(INC)}
	* OP1 OP2 RS1 OP3
	* Armazena no registrador apontado pelo codigo3 o valor da posição
	* de memória apontada pelo registrador SP, após incrementa o SP se
	* a pilha é INC ou decrementa o SP se a pilha é DEC.
	*/
	public void poprg()
	{
		valorhex1=capturaValor("SP");

		if(sim.isHarvard())
			resultadohex=sim.getMemoriaDados().getInstrucao(valorhex1);
		else
			resultadohex=sim.getMemoria().getInstrucao(valorhex1);
		
		sim.getJanela().getReg("R"+codigo3).setValReg(resultadohex);

		if(sim.getMovPilha().equalsIgnoreCase("INC"))
			sim.getJanela().getReg("SP").decrementa();
		else
			sim.getJanela().getReg("SP").incrementa();
	
		tratandoFlags("POPRG",resultadohex);
	}


	/**
	* PUSHRG RS1  {SP<-SP - 1(DEC) ou SP<-SP + 1(INC); PMEM(SP) <- RS1;}
	* OP1 OP2 RS1 OP3
	* Pré-incrementa ou pré-decrementa o registrador SP e armazena na
	* posição de memória apontada por SP o valor do registrador do codigo3.
	*/
	public void pushrg()
	{
		if(sim.getMovPilha().equalsIgnoreCase("DEC"))
			sim.getJanela().getReg("SP").decrementa();
		else
			sim.getJanela().getReg("SP").incrementa();

		valorhex1=capturaValor(codigo3);
		valorhex2=capturaValor("SP");

		String linha[]={valorhex2,valorhex1,""};
		if(sim.isHarvard())
		{
			sim.getMemoriaDados().setMemoria(linha);
			sim.getJanela().getTabelaMemoriaDados().setLinha(linha);
		}
		else
		{
			sim.getMemoria().setMemoria(linha);
			sim.getJanela().getTabelaMemoria().setLinha(linha);
		}
	
		tratandoFlags("PUSHRG",valorhex1);
	}

  /**
	* RL RT,CT1  {RT <- RT[15-CT1;0] & RT[15;15-CT1]}
	* OP1 RT CT1_3_0 OP2
	* Rotaciona o registrador do codigo2 para a esquerda n bits informados
	* no codigo3.
	*/
	public void rl()
	{
		//Registrador a ser deslocado
		valorhex1=capturaValor(codigo2);
		valordec1=sim.getConversao().hexa_decimal(valorhex1);
		valorbin1=sim.getConversao().decimal_binario(valordec1);
		
		//Numero de bits a ser deslocado
		valorhex2=codigo3;
		valordec2=sim.getConversao().hexa_decimal(valorhex2);
				
		for(int i=0;i<valordec2;i++)
			valorbin1=valorbin1.substring(1,16).concat(valorbin1.substring(0,1));
					
		resultadodec=sim.getConversao().binario_decimal(valorbin1);
		resultadohex=sim.getConversao().decimal_hexa(resultadodec);
		sim.getJanela().getReg("R"+codigo2).setValReg(resultadohex);
		
		tratandoFlags("RL",resultadohex);
	}

	/**
	* RLR RT,RS1  {RT <- RS1[14;0] & RS1[15]}
	* OP1 RT RS1 OP2
	* Rotaciona o registrador do codigo3 1 bit para a esquerda e armazena
	* no registrador do codigo2.
	*/
	public void rlr()
	{
		//Registrador a ser deslocado
		valorhex1=capturaValor(codigo3);
		valordec1=sim.getConversao().hexa_decimal(valorhex1);
		valorbin1=sim.getConversao().decimal_binario(valordec1);

		valorbin1=valorbin1.substring(1,16).concat(valorbin1.substring(0,1));
		
		resultadodec=sim.getConversao().binario_decimal(valorbin1);
		resultadohex=sim.getConversao().decimal_hexa(resultadodec);
		sim.getJanela().getReg("R"+codigo2).setValReg(resultadohex);
		
		tratandoFlags("RLR",resultadohex);
	}

	
	/**
	* RLRC RT,RS1  {RT <- RS1[14;0] & CARRY;}
	* OP1 RT RS1 OP2
	* Rotaciona o registrador do codigo3 1 bit para a esquerda incluindo
	* o valor de carry no bit menos significativo e armazena no registrador
	* do codigo2.
	*/
	public void rlrc()
	{
		String zero="0",um="1";
		
		//Registrador a ser deslocado
		valorhex1=capturaValor(codigo3);
		valordec1=sim.getConversao().hexa_decimal(valorhex1);
		valorbin1=sim.getConversao().decimal_binario(valordec1);
		
		//captura o bit que sera descartado para setá-lo ao flag de carry
		valorbin2=valorbin1.substring(0,1);

		if(sim.getJanela().getFlag("CARRY").getValFlag())
			valorbin1=valorbin1.substring(1,16).concat(um);
		else
			valorbin1=valorbin1.substring(1,16).concat(zero);
					
		resultadodec=sim.getConversao().binario_decimal(valorbin1);
		resultadohex=sim.getConversao().decimal_hexa(resultadodec);
		sim.getJanela().getReg("R"+codigo2).setValReg(resultadohex);
		
		if(valorbin2.equals("1"))
			sim.getJanela().getFlag("CARRY").setValFlag(true);
		else
			sim.getJanela().getFlag("CARRY").setValFlag(false);

		String [] noModifyFlags={"CARRY"};
		tratandoFlags("RLRC",resultadohex,noModifyFlags);
	}

  /**
	* RR RT,CT1  {RT <- RT[CT1;0] & RT[15;CT1]}
	* OP1 RT CT1_3_0 OP2
	* Rotaciona o registrador do codigo2 para a direita n bits informados
	* no codigo3.
	*/
	public void rr()
	{
		int tam;
		String zero="0";
		
		//Registrador a ser deslocado
		valorhex1=capturaValor(codigo2);
		valordec1=sim.getConversao().hexa_decimal(valorhex1);
		valorbin1=sim.getConversao().decimal_binario(valordec1);
		
		//Numero de bits a ser deslocado
		valorhex2=codigo3;
		valordec2=sim.getConversao().hexa_decimal(valorhex2);
		
		for(int i=0;i<valordec2;i++)
			valorbin1=valorbin1.substring(15,16).concat(valorbin1.substring(0,15));
					
		resultadodec=sim.getConversao().binario_decimal(valorbin1);
		resultadohex=sim.getConversao().decimal_hexa(resultadodec);
		sim.getJanela().getReg("R"+codigo2).setValReg(resultadohex);
		
		tratandoFlags("RR",resultadohex);
	}

	/**
	* RRR RT,RS1  {RT <- RS1[0] & RS1[15;1]}
	* OP1 RT RS1 OP2
	* Rotaciona o registrador do codigo3 1 bit para a direita e armazena
	* no registrador do codigo2.
	*/
	public void rrr()
	{
		//Registrador a ser deslocado
		valorhex1=capturaValor(codigo3);
		valordec1=sim.getConversao().hexa_decimal(valorhex1);
		valorbin1=sim.getConversao().decimal_binario(valordec1);
		
		valorbin1=valorbin1.substring(15,16).concat(valorbin1.substring(0,15));
					
		resultadodec=sim.getConversao().binario_decimal(valorbin1);
		resultadohex=sim.getConversao().decimal_hexa(resultadodec);
		sim.getJanela().getReg("R"+codigo2).setValReg(resultadohex);
		
		tratandoFlags("RRR",resultadohex);
	}
	

	/**
	* RRRC RT,RS1  {RT <- CARRY & RS1[15;1]}
	* OP1 RT RS1 OP2
	* Rotaciona o registrador do codigo3 1 bit para a direita incluindo
	* o valor do carry no bit mais significativo e armazena no registrador 
	* do codigo2.
	*/
	public void rrrc()
	{
		String zero="0",um="1";
		
		//Registrador a ser deslocado
		valorhex1=capturaValor(codigo3);
		valordec1=sim.getConversao().hexa_decimal(valorhex1);
		valorbin1=sim.getConversao().decimal_binario(valordec1);
		
		//captura o bit que sera descartado para setá-lo ao flag de carry
		valorbin2=valorbin1.substring(15,16);

		if(sim.getJanela().getFlag("CARRY").getValFlag())
			valorbin1=um.concat(valorbin1.substring(0,15));
		else
			valorbin1=zero.concat(valorbin1.substring(0,15));
					
		if(valorbin2.equals("1"))
			sim.getJanela().getFlag("CARRY").setValFlag(true);
		else
			sim.getJanela().getFlag("CARRY").setValFlag(false);
								
					
		resultadodec=sim.getConversao().binario_decimal(valorbin1);
		resultadohex=sim.getConversao().decimal_hexa(resultadodec);
		sim.getJanela().getReg("R"+codigo2).setValReg(resultadohex);
		
		String [] noModifyFlags={"CARRY"};
		tratandoFlags("RRRC",resultadohex,noModifyFlags);
	}

	/**
	* SL RT,RS2  {if(flag_dep) RT <- RS2[14;0] & '1' else RT <- RS2[14;0] & '0'}
	* OP1 RT OP2 RS2 
	* Desloca para a esquerda o valor armazenado no codigo4 e concatena 
	* no bit menos significativo UM (caso o flag geral estiver setado
	* em true) ou ZERO (caso contrário). 
	*/
	public void sl()
	{
		int tam;
		String zero="0";
		String flag_dep;
		flag_dep=instAtual.getFlag_dep();
		
		valorhex1=capturaValor(codigo4);
		valordec1=sim.getConversao().hexa_decimal(valorhex1);
		valorbin1=sim.getConversao().decimal_binario(valordec1);
		
		valorbin2="";
		for(int i=1;i<16;i++)
			valorbin2=valorbin2.concat(valorbin1.substring(i,i+1));
	
		if(sim.getJanela().getFlag(flag_dep).getValFlag())
			valorbin2=valorbin2.concat("1");
		else	
			valorbin2=valorbin2.concat("0");
	
  	resultadodec=sim.getConversao().binario_decimal(valorbin2);
		resultadohex=sim.getConversao().decimal_hexa(resultadodec);
		sim.getJanela().getReg("R"+codigo2).setValReg(resultadohex);
	
		tratandoFlags("SL",resultadohex);
	}

	/**
	* SL0 RT,RS1  {RT <- RS1[14;0] & '0'}
	* OP1 RT OP2 RS1 
	* Desloca para a esquerda o valor armazenado no codigo4 e concatena 
	* no bit menos significativo ZERO.
	*/
	public void sl0()
	{
		int tam;
		String zero="0";
		
		valorhex1=capturaValor(codigo4);
		valordec1=sim.getConversao().hexa_decimal(valorhex1);
		valorbin1=sim.getConversao().decimal_binario(valordec1);
	
		valorbin2="";
		for(int i=1;i<16;i++)
			valorbin2=valorbin2.concat(valorbin1.substring(i,i+1));
	
		valorbin2=valorbin2.concat("0");
		resultadodec=sim.getConversao().binario_decimal(valorbin2);
		resultadohex=sim.getConversao().decimal_hexa(resultadodec);
		sim.getJanela().getReg("R"+codigo2).setValReg(resultadohex);

		tratandoFlags("SL0",resultadohex);
	}

	/**
	* SL1 RT,RS1  {RT <- RS1[14;0] & '1'}
	* OP1 RT OP2 RS1 
	* Desloca para a esquerda o valor armazenado no codigo4 e concatena 
	* no bit menos significativo UM.
	*/
	public void sl1()
	{
		int tam;
		String zero="0";
		
		valorhex1=capturaValor(codigo4);
		valordec1=sim.getConversao().hexa_decimal(valorhex1);
		valorbin1=sim.getConversao().decimal_binario(valordec1);
		
		valorbin2="";
		for(int i=1;i<16;i++)
			valorbin2=valorbin2.concat(valorbin1.substring(i,i+1));
	
		valorbin2=valorbin2.concat("1");
		resultadodec=sim.getConversao().binario_decimal(valorbin2);
		resultadohex=sim.getConversao().decimal_hexa(resultadodec);
		sim.getJanela().getReg("R"+codigo2).setValReg(resultadohex);
		
		tratandoFlags("SL1",resultadohex);
	}

  /**
	* SL0C RT,CT1   {RT <- RT[15-CT1_3_0;0] & ('0'*CT1_3_0)}
	* OP1 RT CT1_3_0 OP2 
	* Desloca o registrador do codigo2 para a esquerda n bits informados
	* no codigo3 e concatena ZEROS nos bits menos significativo.
	*/
	public void sl0c()
	{
		int tam;
		String zero="0";
			
		//Registrador a ser deslocado
		valorhex1=capturaValor(codigo2);
		valordec1=sim.getConversao().hexa_decimal(valorhex1);
		valorbin1=sim.getConversao().decimal_binario(valordec1);
		
		//Numero de bits a ser deslocado
		valorhex2=codigo3;
		valordec2=sim.getConversao().hexa_decimal(valorhex2);
		
		valorbin2="";
		for(int i=valordec2;i<16;i++)
			valorbin2=valorbin2.concat(valorbin1.substring(i,i+1));
	
		//concatena zeros ate formar 16 bits
		tam=16-valorbin2.length();
		for(int i=0;i<tam;i++)
			valorbin2=valorbin2.concat("0");
				
		resultadodec=sim.getConversao().binario_decimal(valorbin2);
		resultadohex=sim.getConversao().decimal_hexa(resultadodec);
		sim.getJanela().getReg("R"+codigo2).setValReg(resultadohex);

		tratandoFlags("SL0C",resultadohex);
	}

  /**
	* SL1C RT,CT1   {RT <- RT[15-CT1_3_0;0] & ('1'*CT1_3_0)}
	* OP1 RT CT1_3_0 OP2 
	* Desloca o registrador do codigo2 para a esquerda n bits informados
	* no codigo3 e concatena UMS nos bits menos significativo.
	*/
	public void sl1c()
	{
		int tam;
		String zero="0";
			
		//Registrador a ser deslocado
		valorhex1=capturaValor(codigo2);
		valordec1=sim.getConversao().hexa_decimal(valorhex1);
		valorbin1=sim.getConversao().decimal_binario(valordec1);
		
		//Numero de bits a ser deslocado
		valorhex2=codigo3;
		valordec2=sim.getConversao().hexa_decimal(valorhex2);
		
		valorbin2="";
		for(int i=valordec2;i<16;i++)
			valorbin2=valorbin2.concat(valorbin1.substring(i,i+1));
	
		//concatena zeros ate formar 16 bits
		tam=16-valorbin2.length();
		for(int i=0;i<tam;i++)
			valorbin2=valorbin2.concat("1");
				
		resultadodec=sim.getConversao().binario_decimal(valorbin2);
		resultadohex=sim.getConversao().decimal_hexa(resultadodec);
		sim.getJanela().getReg("R"+codigo2).setValReg(resultadohex);
		
		tratandoFlags("SL1C",resultadohex);
	}

	/**
	* SR RT,RS2  {if(flag_dep) RT <- '1' & RS2[15;1] else RT <- '0' & RS2[14;0]}
	* OP1 RT OP2 RS2 
	* Desloca para a direita o valor armazenado no codigo4 e concatena 
	* no bit mais significativo UM (caso o flag geral estiver setado
	* em true) ou ZERO (caso contrário) e armazena o resltado no codigo2.
	*/
	public void sr()
	{
		int tam;
		String zero="0";
		String flag_dep;
		flag_dep=instAtual.getFlag_dep();
		
		valorhex1=capturaValor(codigo4);
		valordec1=sim.getConversao().hexa_decimal(valorhex1);
		valorbin1=sim.getConversao().decimal_binario(valordec1);
		
		valorbin2="";
		for(int i=0;i<15;i++)
			valorbin2=valorbin2.concat(valorbin1.substring(i,i+1));
	
		if(sim.getJanela().getFlag(flag_dep).getValFlag())
			valorbin2="1".concat(valorbin2);
		else	
			valorbin2="0".concat(valorbin2);
	
		resultadodec=sim.getConversao().binario_decimal(valorbin2);
		resultadohex=sim.getConversao().decimal_hexa(resultadodec);
		sim.getJanela().getReg("R"+codigo2).setValReg(resultadohex);
		
		tratandoFlags("SR",resultadohex);
	}

	/**
	* SR0 RT,RS1  {RT <- '0' & RS1[15;1]}
	* OP1 RT OP2 RS1 
	* Desloca para a direita 1 bit o valor armazenado no codigo4 e concatena 
	* no bit mais significativo ZERO. Armazena o resultado no registrador do
	* codigo2. 
	*/
	public void sr0()
	{
		int tam;
		String zero="0";
		
		valorhex1=capturaValor(codigo4);
		valordec1=sim.getConversao().hexa_decimal(valorhex1);
		valorbin1=sim.getConversao().decimal_binario(valordec1);
		
		valorbin2="";
		for(int i=0;i<15;i++)
			valorbin2=valorbin2.concat(valorbin1.substring(i,i+1));
	
		valorbin2="0".concat(valorbin2);
	
		resultadodec=sim.getConversao().binario_decimal(valorbin2);
		resultadohex=sim.getConversao().decimal_hexa(resultadodec);
		sim.getJanela().getReg("R"+codigo2).setValReg(resultadohex);

		tratandoFlags("SR0",resultadohex);
	}

	/**
	* SR0C RT,CT1   {RT <- ('0'*CT1_3_0) & RT[15;CT1_3_0] }
	* OP1 RT CT1_3_0 OP2 
	* Desloca o registrador do codigo2 para a direita n bits informados
	* no codigo3 e concatena ZEROS nos bits menos significativo.
	*/
	public void sr0c()
	{
		int tam;
		String zero="0";
			
		//Registrador a ser deslocado
		valorhex1=capturaValor(codigo2);
		valordec1=sim.getConversao().hexa_decimal(valorhex1);
		valorbin1=sim.getConversao().decimal_binario(valordec1);
		
		//Numero de bits a ser deslocado
		valorhex2=codigo3;
		valordec2=sim.getConversao().hexa_decimal(valorhex2);
		
		valorbin2="";
		for(int i=0;i<(16-valordec2);i++)
			valorbin2=valorbin2.concat(valorbin1.substring(i,i+1));
	
		//concatena zeros ate formar 16 bits
		tam=16-valorbin2.length();
		for(int i=0;i<tam;i++)
			valorbin2="0".concat(valorbin2);
				
		resultadodec=sim.getConversao().binario_decimal(valorbin2);
		resultadohex=sim.getConversao().decimal_hexa(resultadodec);
		sim.getJanela().getReg("R"+codigo2).setValReg(resultadohex);
		
		tratandoFlags("SR0C",resultadohex);
	}

	/**
	* SR1 RT,RS2  {RT <- '1' & RS2[15;1]}
	* OP1 RT OP2 RS2 
	* Desloca para a direita o valor armazenado no codigo4 e concatena 
	* no bit mais significativo UM. 
	*/
	public void sr1()
	{
		int tam;
		String zero="0";
		
		valorhex1=capturaValor(codigo4);
		valordec1=sim.getConversao().hexa_decimal(valorhex1);
		valorbin1=sim.getConversao().decimal_binario(valordec1);
		
		valorbin2="";
		for(int i=0;i<15;i++)
			valorbin2=valorbin2.concat(valorbin1.substring(i,i+1));
	
		valorbin2="1".concat(valorbin2);
	
		resultadodec=sim.getConversao().binario_decimal(valorbin2);
		resultadohex=sim.getConversao().decimal_hexa(resultadodec);
		sim.getJanela().getReg("R"+codigo2).setValReg(resultadohex);

		tratandoFlags("SR1",resultadohex);
	}
	
  /**
	* SR1C RT,CT1   {RT <- ('1'*CT1_3_0) & RT[15;CT1_3_0] }
	* OP1 RT CT1_3_0 OP2 
	* Desloca o registrador do codigo2 para a direita n bits informados
	* no codigo3 e concatena UMS nos bits menos significativo.
	*/
	public void sr1c()
	{
		int tam;
		String zero="0";
		
		//Registrador a ser deslocado
		valorhex1=capturaValor(codigo2);
		valordec1=sim.getConversao().hexa_decimal(valorhex1);
		valorbin1=sim.getConversao().decimal_binario(valordec1);
		
		//Numero de bits a ser deslocado
		valorhex2=codigo3;
		valordec2=sim.getConversao().hexa_decimal(valorhex2);
		
		valorbin2="";
		for(int i=0;i<(16-valordec2);i++)
			valorbin2=valorbin2.concat(valorbin1.substring(i,i+1));
	
		//concatena zeros ate formar 16 bits
		tam=16-valorbin2.length();
		for(int i=0;i<tam;i++)
			valorbin2="1".concat(valorbin2);
				
		resultadodec=sim.getConversao().binario_decimal(valorbin2);
		resultadohex=sim.getConversao().decimal_hexa(resultadodec);
		sim.getJanela().getReg("R"+codigo2).setValReg(resultadohex);
		
		tratandoFlags("SR1C",resultadohex);
	}


	
	/**
	* SUB RT,RS1,RS2  {RT <- RS1 - RS2}
	* OP1 RT RS1 RS2
	* Captura o valor do codigo3 e do codigo4, subtrai e armazena
	* o resultado no registrador representado pelo codigo2.
	*/
	public void sub()
	{
		valorhex1=capturaValor(codigo3);
		valorhex2=capturaValor(codigo4);
		resultadohex=subt(valorhex1,valorhex2);
		sim.getJanela().getReg("R"+codigo2).setValReg(resultadohex);
		
		tratandoFlags("SUB");
	}

	/**
	* SUBI RT,CT1  {RT <- RT - ("00000000" & constante)}
	* OP1 RT CT1_7_4 CT1_3_0
	* Captura o valor do codigo2 e subtrai do codigo3 concatenado com o codigo4
	* e armazena o resultado no registrador representado pelo codigo2.
	*/
	public void subi()
	{
		valorhex1=capturaValor(codigo2);
		valorhex2="00"+codigo3+codigo4;
		resultadohex=subt(valorhex1,valorhex2);
		sim.getJanela().getReg("R"+codigo2).setValReg(resultadohex);
		
		tratandoFlags("SUBI");
	}

	/**
	* XOR RT,RS1,RS2  {RT <- RS1 xor RS2}
	* OP1 RT RS1 RS2
	* Captura o valor do codigo3 e do codigo4, realiza a operação XOR
	* e armazena o resultado no registrador representado pelo codigo2.
	*/
	public void xor()
	{
		String zero="0";
		int tam;
		
		valorhex1=capturaValor(codigo3);
		valorhex2=capturaValor(codigo4);
		valordec1=sim.getConversao().hexa_decimal(valorhex1);
		valordec2=sim.getConversao().hexa_decimal(valorhex2);
		valorbin1=sim.getConversao().decimal_binario(valordec1);
		valorbin2=sim.getConversao().decimal_binario(valordec2);
		
		resultadobin=xor(valorbin1,valorbin2);
		resultadodec=sim.getConversao().binario_decimal(resultadobin);
		resultadohex=sim.getConversao().decimal_hexa(resultadodec);
		sim.getJanela().getReg("R"+codigo2).setValReg(resultadohex);

		tratandoFlags("XOR");
	}

	/**
	* XORI RT,CT1  {RT <- RT xor (ext_sinal & constante)}
	* OP1 RT RS1 RS2
	* Captura o valor do codigo2 e realiza a operação XOR com a
	* extensão do sinal do codigo3 concatenado com o codigo4 e
	* armazena o resultado no registrador representado pelo codigo2.
	*/
	public void xori()
	{
		String zero="0";
		int tam;
		
		valorhex1=capturaValor(codigo2);
		valorhex2=ext_sinal(codigo3);
		valordec1=sim.getConversao().hexa_decimal(valorhex1);
		valordec2=sim.getConversao().hexa_decimal(valorhex2);
		valorbin1=sim.getConversao().decimal_binario(valordec1);
		valorbin2=sim.getConversao().decimal_binario(valordec2);
	
		resultadobin=xor(valorbin1,valorbin2);
		resultadodec=sim.getConversao().binario_decimal(resultadobin);
		resultadohex=sim.getConversao().decimal_hexa(resultadodec);
		sim.getJanela().getReg("R"+codigo2).setValReg(resultadohex);
		
		tratandoFlags("XORI");
	}
	

	/**
	* MOV RT,RS1  {RT <- RS1}
	* OP1 RT OP2 RS1
	* Captura o valor armazenado no registrador indicado pelo codigo4
	* e move para o registrador indicado pelo codigo2.
	*/
	public void mov()
	{
		valorhex1=capturaValor(codigo4);
		sim.getJanela().getReg("R"+codigo2).setValReg(valorhex1);

		tratandoFlags("MOV",valorhex1);
	}
	
	/**
	* NOT RT,RS1  {RT <- (TAMMEM-1) - RS1}
	* OP1 RT OP2 RS1
	* Captura o valor armazenado no codigo4 e o subtrai do tamanho
	* da memoria -1. O resultado é armazenado no registrador representado
	* pelo codigo2.
	*/
	public void not()
	{
		valorhex1=sim.getConversao().decimal_hexa(sim.getTamanhoMemoria()-1,4);
		valorhex2=capturaValor(codigo4);
		resultadohex=subt(valorhex1,valorhex2);
		sim.getJanela().getReg("R"+codigo2).setValReg(resultadohex);

		tratandoFlags("NOT");
	}

	/**
	* Captura o valor armazenado no codigo4 e o subtrai do tamanho
	* da memoria -1 para fazer o not e soma um para negar. O resultado é armazenado no registrador
	* representado pelo codigo2.
	*/
	public void neg()
	{
		valorhex1=sim.getConversao().decimal_hexa(sim.getTamanhoMemoria(),4);
		valorhex2=capturaValor(codigo4);
		resultadohex=subt(valorhex1,valorhex2);
		sim.getJanela().getReg("R"+codigo2).setValReg(resultadohex);

		tratandoFlags("NEG");
	}

	/**
	* Captura o valor do codigo4 e incrementa armazenando
	* o resultado no registrador representado pelo codigo2.
	*/
	public void inc()
	{
		valorhex1=capturaValor(codigo4);
		valorhex2="0001";
		resultadohex=soma(valorhex1,valorhex2);
		sim.getJanela().getReg("R"+codigo2).setValReg(resultadohex);
		
		tratandoFlags("INC");
	}

	/**
	* Captura o valor do codigo2 e incrementa com o valor da
	* constante armazenada no codigo3.
	*/
	public void inci()
	{
		valorhex1=capturaValor(codigo2);
		valorhex2=codigo3;
		resultadohex=soma(valorhex1,valorhex2);
	
	
		sim.getJanela().getReg("R"+codigo2).setValReg(resultadohex);
		
		tratandoFlags("INCI");
	}

	/**
	* Captura o valor do codigo2 e decrementa armazenando
	* o resultado no registrador representado pelo codigo2.
	*/
	public void dec()
	{
		valorhex1=capturaValor(codigo4);
		valorhex2="FFFF";
		resultadohex=soma(valorhex1,valorhex2);
		sim.getJanela().getReg("R"+codigo2).setValReg(resultadohex);
		
		tratandoFlags("DEC");
	}

	/**
	* Captura o valor do codigo2 e decrementa com o valor da
	* constante armazenada no codigo3.
	*/
	public void deci()
	{
		valorhex1=capturaValor(codigo2);
		valorhex2=codigo3;
		resultadohex=subt(valorhex1,valorhex2);
		sim.getJanela().getReg("R"+codigo2).setValReg(resultadohex);
		
		tratandoFlags("DECI");
	}

	/**
	* LD RT,RS1  {RT <- PMEM(RS1)}
	* OP1 RT OP2 RS1
	* Armazena no registrador representado pelo codigo2 o valor armazenado
	* na posição de memória capturada através do codigo4. 
	*/
	public void ld()
	{
		valorhex1=capturaValor(codigo4);
		if(sim.isHarvard())
			resultadohex=sim.getMemoriaDados().getInstrucao(valorhex1);
		else
			resultadohex=sim.getMemoria().getInstrucao(valorhex1);
			
		sim.getJanela().getReg("R"+codigo2).setValReg(resultadohex);
		
		tratandoFlags("LD",resultadohex);
	}

	/**
	* LD2 RT,RS1,RS2  {RT <- PMEM(RS1+RS2)}
	* OP1 RT RS1 RS2
	* Armazena no registrador representado pelo codigo2 o valor armazenado
	* na posição de memória capturada através do codigo3 somado com codigo4. 
	*/
	public void ld2()
	{
		valorhex1=capturaValor(codigo3);
		valorhex2=capturaValor(codigo4);
		resultadohex=soma(valorhex1,valorhex2);

		if(sim.isHarvard())
			resultadohex=sim.getMemoriaDados().getInstrucao(resultadohex);
			
		else
			resultadohex=sim.getMemoria().getInstrucao(resultadohex);

		sim.getJanela().getReg("R"+codigo2).setValReg(resultadohex);
		
		tratandoFlags("LD2");
	}

	/**
	* LDRI RS1,CT,RT  {RT <- PMEM(RS1+CT)}
	* OP1 RT CT RS2
	* Armazena no registrador representado pelo codigo4 o valor armazenado
	* na posição de memória capturada através do codigo3(constante de 4 bits)
	* somado com codigo2(registrador).
	*/
	public void ldri()
	{
		valorhex1=ext_sinal_4bits(codigo3);
		valorhex2=capturaValor(codigo4);
		resultadohex=soma(valorhex1,valorhex2);

		if(sim.isHarvard())
			resultadohex=sim.getMemoriaDados().getInstrucao(resultadohex);
		else
			resultadohex=sim.getMemoria().getInstrucao(resultadohex);

		sim.getJanela().getReg("R"+codigo2).setValReg(resultadohex);
		
		tratandoFlags("LDRI");
	}

	/**
	* Armazena na posição de memória representado pelo codigo4 o valor
	* capturado do codigo3. 
	*/
	public void st()
	{
		valorhex1=capturaValor(codigo3);
		valorhex2=capturaValor(codigo4);
		
		sim.getVariaveis().setValor(valorhex1,valorhex2,"");
		String linha[]={valorhex1,valorhex2,"DADOS"};
		if(sim.isHarvard())
		{
			sim.getMemoriaDados().setMemoria(linha);
			sim.getJanela().getTabelaMemoriaDados().setLinha(linha);
		}
		else
		{
			sim.getMemoria().setMemoria(linha);
			sim.getJanela().getTabelaMemoria().setLinha(linha);
		}
		
		tratandoFlags("ST",valorhex2);
	}

	/**
	* Armazena na posição de memória representado pelo codigo4
	* somado ao do codigo3 o valor capturado do codigo2. 
	*/
	public void st2()
	{
		valorhex1=capturaValor(codigo3);
		valorhex2=capturaValor(codigo4);
		resultadohex=soma(valorhex1,valorhex2);
	
		valorhex1=capturaValor(codigo2);

		sim.getVariaveis().setValor(resultadohex,valorhex1,"");

		String linha[]={resultadohex,valorhex1,"DADOS"};
		if(sim.isHarvard())
		{
			sim.getMemoriaDados().setMemoria(linha);
			sim.getJanela().getTabelaMemoriaDados().setLinha(linha);
		}
		else
		{
			sim.getMemoria().setMemoria(linha);
			sim.getJanela().getTabelaMemoria().setLinha(linha);
		}

		tratandoFlags("ST2",valorhex1);
	}

	/**
	* Armazena na posição de memória representada pela somada do codigo4(registrador)
	* e a constante de 4 bits(codigo3) o valor capturado do codigo2(registdor).
	* PMEM(registrador(codig4)+ constante(codigo3))<-registrador(codigo2)
	*/
	public void stri()
	{
		valorhex1=ext_sinal_4bits(codigo3);
		valorhex2=capturaValor(codigo4);
		resultadohex=soma(valorhex1,valorhex2);


		valorhex1=capturaValor(codigo2);

		sim.getVariaveis().setValor(resultadohex,valorhex1,"");

		String linha[]={resultadohex,valorhex1,"DADOS"};
		if(sim.isHarvard())
		{
			sim.getMemoriaDados().setMemoria(linha);
			sim.getJanela().getTabelaMemoriaDados().setLinha(linha);
		}
		else
		{
			sim.getMemoria().setMemoria(linha);
			sim.getJanela().getTabelaMemoria().setLinha(linha);
		}
		
		tratandoFlags("STRI",valorhex1);
	}

 	/**
	* POP RT {RT <- PMEM(SP - 1); [pilha INC] or RT <- PMEM(SP + 1); [pilha DEC] }
	* OP1 RT OP2 OP3
	* Armazena no registrador representado pelo codigo2 o valor armazenado
	* na posição de memória capturada através do SP decrementado de 1. 
	*/
	public void pop()
	{
		if(!sim.getJanela().getReg("SP").getValReg().equalsIgnoreCase(sim.getPosicaoSP()))
		{
			if(sim.getMovPilha().equalsIgnoreCase("INC"))
				sim.getJanela().getReg("SP").decrementa();
			else
				sim.getJanela().getReg("SP").incrementa();

			valorhex1=capturaValor("SP");
			
			if(sim.isHarvard())
				resultadohex=sim.getMemoriaDados().getInstrucao(valorhex1);
			else
				resultadohex=sim.getMemoria().getInstrucao(valorhex1);

			String linha[]={valorhex1,"",""};
			if(sim.isHarvard())
			{
				sim.getMemoriaDados().setMemoria(linha);
				sim.getJanela().getTabelaMemoriaDados().setLinha(linha);
			}
			else
			{
				sim.getMemoria().setMemoria(linha);
				sim.getJanela().getTabelaMemoria().setLinha(linha);
			}

			sim.getJanela().getReg("R"+codigo2).setValReg(resultadohex);
			
			tratandoFlags("POP",resultadohex);
		}
		else
			JOptionPane.showMessageDialog(sim.getJanela(),"POP em Pilha Vazia!","ERRO", JOptionPane.ERROR_MESSAGE);
	}

 	/**
	* PUSH RT { PMEM(SP) <- RS1; SP <- SP + 1;[pilha INC] SP <- SP - 1; [pilha DEC] }
	* OP1 OP2 RS1 OP3
	* Armazena na posição de memória representado por SP o valor
	* capturado do codigo3 e incrementa de 1 SP. 
	*/
	public void push()
	{
		valorhex1=capturaValor("SP");
		valorhex2=capturaValor(codigo3);

		String linha[]={valorhex1,valorhex2,"DADOS"};
		if(sim.isHarvard())
		{
			sim.getMemoriaDados().setMemoria(linha);
			sim.getJanela().getTabelaMemoriaDados().setLinha(linha);
		}
		else
		{
			sim.getMemoria().setMemoria(linha);
			sim.getJanela().getTabelaMemoria().setLinha(linha);
		}

		
		if(sim.getMovPilha().equalsIgnoreCase("INC"))
			sim.getJanela().getReg("SP").incrementa();
		else
			sim.getJanela().getReg("SP").decrementa();
			
		tratandoFlags("PUSH",valorhex2);
	}

	/**
	* CMP RT,RS1  {RS1 - RT)}
	* OP1 OP2 RT RS1
	* Compara os valores dos codigo3 e codigo4 e seta os flags.
	*/
	public void cmp()
	{
		valorhex1=capturaValor(codigo3); //rt
		valorhex2=capturaValor(codigo4); //rs1
		resultadohex=subt(valorhex1,valorhex2);
	
		tratandoFlags("CMP");
	}

	/**
	* Seta o flag geral para TRUE se o valor capturado do registrador
	* representado pelo codigo3 for diferente do valor capturado do 
	* registrador representado pelo codigo4. Caso contrário, seta FALSO
	* para o flag geral.
	*/
	public void dif()
	{
		String flag_dep;
		flag_dep=instAtual.getFlag_dep();
		
		valorhex1=capturaValor(codigo3);
		valorhex2=capturaValor(codigo4);
		resultadohex=subt(valorhex1,valorhex2);
		resultadodec=sim.getConversao().hexa_decimal(resultadohex);
				
		if(resultadodec!=0)
			sim.getJanela().getFlag(flag_dep).setValFlag(true);
		else
			sim.getJanela().getFlag(flag_dep).setValFlag(false);
		
		tratandoFlags("DIF");
	}

	/**
	* Seta o flag geral para TRUE se o valor capturado do registrador
	* representado pelo codigo3 for igual ao valor capturado do 
	* registrador representado pelo codigo4. Caso contrário, seta FALSO
	* para o flag geral.
	*/
	public void eql()
	{
		String flag_dep;
		flag_dep=instAtual.getFlag_dep();
		
		valorhex1=capturaValor(codigo3);
		valorhex2=capturaValor(codigo4);
		resultadohex=subt(valorhex1,valorhex2);
		resultadodec=sim.getConversao().hexa_decimal(resultadohex);

		if(resultadodec==0)
			sim.getJanela().getFlag(flag_dep).setValFlag(true);
		else
			sim.getJanela().getFlag(flag_dep).setValFlag(false);

		tratandoFlags("EQL");
	}
	
	/**
	* Seta o flag para TRUE se o valor capturado do registrador
	* representado pelo codigo3 for superior ao valor capturado do 
	* registrador representado pelo codigo4. Caso contrário, seta FALSO
	* para o flag. Nesta operação FFFF = 65535.
	*/
	public void sup()
	{
		String flag_dep;
		flag_dep=instAtual.getFlag_dep();
		
		valorhex1=capturaValor(codigo3);
		valorhex2=capturaValor(codigo4);
		resultadohex=subt(valorhex1,valorhex2);
		resultadodec=sim.getConversao().hexa_decimal(resultadohex);

		
		if(resultadodec>0)
			sim.getJanela().getFlag(flag_dep).setValFlag(true);
		else
			sim.getJanela().getFlag(flag_dep).setValFlag(false);
		
		tratandoFlags("SUP");
	}
	
	/**
	* Seta o flag para TRUE se o valor capturado do registrador
	* representado pelo codigo3 for superior ao valor capturado do 
	* registrador representado pelo codigo4. Caso contrário, seta FALSO
	* para o flag. Nesta operação FFFF = -1.
	*/
	public void sup2()
	{
		String flag_dep;
		flag_dep=instAtual.getFlag_dep();

		valorhex1=capturaValor(codigo3);
		valorhex2=capturaValor(codigo4);
		resultadohex=subt(valorhex1,valorhex2);
		resultadodec=sim.getConversao().hexa_decimal(resultadohex);


		if(resultadodec>0)
			sim.getJanela().getFlag(flag_dep).setValFlag(true);
		else
			sim.getJanela().getFlag(flag_dep).setValFlag(false);
		

		tratandoFlags("SUP2");
	}

	/**
	* Seta o flag para TRUE se o valor capturado do registrador
	* representado pelo codigo3 for inferior ao valor capturado do 
	* registrador representado pelo codigo4. Caso contrário, seta FALSO
	* para o flag. Nesta operação FFFF = 65535
	*/
	public void inf()
	{
		String flag_dep;
		flag_dep=instAtual.getFlag_dep();
		
		valorhex1=capturaValor(codigo3);
		valorhex2=capturaValor(codigo4);
		resultadohex=subt(valorhex1,valorhex2);
		resultadodec=sim.getConversao().hexa_decimal(resultadohex);

		if(resultadodec<0)
			sim.getJanela().getFlag(flag_dep).setValFlag(true);
		else
			sim.getJanela().getFlag(flag_dep).setValFlag(false);

		tratandoFlags("INF");
	}
	
	/**
	* Seta o flag para TRUE se o valor capturado do registrador
	* representado pelo codigo3 em complemento de 2 for inferior ao valor
	* capturado do registrador representado pelo codigo4. Caso contrário,
	* seta FALSO para o flag. Nesta operação FFFF = -1
	*/
	public void inf2()
	{
		String flag_dep;
		flag_dep=instAtual.getFlag_dep();
		
		valorhex1=capturaValor(codigo3);
		valorhex2=capturaValor(codigo4);
		resultadohex=subt(valorhex1,valorhex2);
		resultadodec=sim.getConversao().hexa_decimal(resultadohex);

		if(resultadodec<0)
			sim.getJanela().getFlag(flag_dep).setValFlag(true);
		else
			sim.getJanela().getFlag(flag_dep).setValFlag(false);

		tratandoFlags("INF2");
	}

	/**
	* STSP RS1  {SP<- RS1;}
	* OP1 OP2 RS1 OP3
	* Armazena em SP o valor capturado do registrador representado pelo
	* codigo3.
	*/
	public void stsp()
	{
		valorhex1=capturaValor(codigo3);
		sim.getJanela().getReg("SP").setValReg(valorhex1);
		
		tratandoFlags("STSP",valorhex1);
	}

	/**
	* LDSP RS1  {RS1<- SP;}
	* OP1 OP2 RS1 OP3
	* Captura o valor de SP e armazena no registrador representado pelo
	* codigo3.
	*/
	public void ldsp()
	{
		valorhex1=capturaValor("SP");
		sim.getJanela().getReg("R"+codigo3).setValReg(valorhex1);
		
		tratandoFlags("LDSP",valorhex1);
	}
	
	/**
	* Captura o valor do SP e soma com a constante de 8 bits armazenada
	* nos codigo3 e codigo4.
	*/
	public void addsp()
	{
		valorhex1=capturaValor("SP");
		valorhex2="00"+codigo3+codigo4;
		resultadohex=soma(valorhex1,valorhex2);
		sim.getJanela().getReg("SP").setValReg(resultadohex);

	  	tratandoFlags("ADDSP",resultadohex);
	}

	/**
	* Não executa nenhuma operação.
	*/
	public void nop(){}
	
	/**
	* Retorna da subrotina. Captura o valor armazenado no endereço contido
	* em SP decrementado de 1 e armazena-o em PC. 
	* [SP<-SP+1;(DEC) ou SP<-SP-1;(INC)] PC<-PMEM(SP); 
	*/
	public void rts()
	{
		if(sim.getMovPilha().equalsIgnoreCase("INC"))
			sim.getJanela().getReg("SP").decrementa();
		else
			sim.getJanela().getReg("SP").incrementa();

		valorhex1=capturaValor("SP");
		
		if(sim.isHarvard())
			resultadohex=sim.getMemoriaDados().getInstrucao(valorhex1);
		else
			resultadohex=sim.getMemoria().getInstrucao(valorhex1);
		
		sim.getJanela().getReg("PC").setValReg(resultadohex);

		tratandoFlags("RTS",resultadohex);
	}

	/**
	* RTSP   {PC<-PMEM(SP); SP<-SP+1;(DEC) ou SP<-SP-1;(INC)}
	* OP1 OP2 OP3 OP4
	* Retorna da subrotina com pilha pré-incrementada ou pré-decrementada. 
	* Captura o valor armazenado no endereço contido em SP e armazena-o em PC,
	* após incrementa ou decrementa SP.
	*/
	public void rtsp()
	{
		valorhex1=capturaValor("SP");
		
		if(sim.isHarvard())
			valorhex1=sim.getMemoriaDados().getInstrucao(valorhex1);
		else
			valorhex1=sim.getMemoria().getInstrucao(valorhex1);
		
		sim.getJanela().getReg("PC").setValReg(valorhex1);

		if(sim.getMovPilha().equalsIgnoreCase("INC"))
			sim.getJanela().getReg("SP").decrementa();
		else
			sim.getJanela().getReg("SP").incrementa();

		tratandoFlags("RTSP",valorhex1);
	}


	
	/**
	* Termina a execução da simulação, desabilitando os botões STEP, RUN,
	* PAUSE e STOP.
	*/
	public void halt()
	{
		halt=true;
		sim.getJanela().getBrun().setEnabled(false);
		sim.getJanela().getBstep().setEnabled(false);
		sim.getJanela().getBstop().setEnabled(false);
		sim.getJanela().getBpause().setEnabled(false);
  	sim.getJanela().getBreset().setEnabled(true);
  	int ninst=sim.getJanela().getNInstrucoes();
  	int nclock=sim.getJanela().getNClocks();
  	float cpi=((float)nclock/(float)ninst);
  	JOptionPane.showMessageDialog(sim.getJanela(),"Número de Instruções: "+ninst+"\nNúmero de Clocks: "+nclock+"\nCPI: "+cpi,"CPI do PROGRAMA",JOptionPane.INFORMATION_MESSAGE);
	}
	
	/**
	* Se o flag geral estiver setado em TRUE realiza um jump referente
	* ao registrador representado pelo codigo3 somado ao valor do PC.
	* Esta soma é armazenada em PC.
	*/
	public void jmprc()
	{
		String flag_dep;
		flag_dep=instAtual.getFlag_dep();
		
		if(sim.getJanela().getFlag(flag_dep).getValFlag())
		{
			valorhex1=capturaValor("PC");
			valorhex2=capturaValor(codigo3);
			resultadohex=soma(valorhex1,valorhex2);
	
			sim.getJanela().getReg("PC").setValReg(resultadohex);
			
			tratandoFlags("JMPRC");
		}
	}
	


	
	/**
	* Realiza um jump incondicional referente ao registrador representado
	* pelo codigo3 somado ao valor do PC.
	* Esta soma é armazenada em PC.
	*/
	public void jmpr()
	{
			valorhex1=capturaValor("PC");
			valorhex2=capturaValor(codigo3);
			resultadohex=soma(valorhex1,valorhex2);

			sim.getJanela().getReg("PC").setValReg(resultadohex);
			tratandoFlags("JMPR");
	}
	
	/**
	* JPRGRMASK RS1 {if mask PC <- PC + RS1;}
	* OP1 OP2 RS1 OP3
	* Realiza um jump condicional a mascara referente ao registrador representado
	* pelo codigo3 somado ao valor do PC.
	* Esta soma é armazenada em PC.
	*/
	public void jprgrMask()
	{
		if(testaMascara8bits())
		{
			valorhex1=capturaValor("PC");
			valorhex2=capturaValor(codigo3);
			resultadohex=soma(valorhex1,valorhex2);

			sim.getJanela().getReg("PC").setValReg(resultadohex);
			tratandoFlags("JPRGRMASK");
		}
	}


	
	/**
	* Se o flag geral estiver setado em TRUE realiza um jump a uma
	* subrotina, armazenando o valor de PC na posicao de memória de
	* SP e armazenando em PC o seu valor somado ao valor do SP, que
	* após é incrementado.
	*/
	public void jsrrc()
	{
		String flag_dep;
		flag_dep=instAtual.getFlag_dep();
		
		if(sim.getJanela().getFlag(flag_dep).getValFlag())
		{
			valorhex1=capturaValor("PC");
			valorhex2=capturaValor("SP");

			String linha[]={valorhex2,valorhex1,"DADOS"};
			if(sim.isHarvard())
			{
				sim.getMemoriaDados().setMemoria(linha);
				sim.getJanela().getTabelaMemoriaDados().setLinha(linha);
			}
			else
			{
				sim.getMemoria().setMemoria(linha);
				sim.getJanela().getTabelaMemoria().setLinha(linha);
			}
			
			if(sim.getMovPilha().equalsIgnoreCase("INC"))
				sim.getJanela().getReg("SP").incrementa();
			else
				sim.getJanela().getReg("SP").decrementa();
				
			valorhex2=capturaValor(codigo3);
			resultadohex=soma(valorhex1,valorhex2);
			sim.getJanela().getReg("PC").setValReg(resultadohex);

			tratandoFlags("JSRRC",valorhex1);
		}
	}
	
	/**
	* Realiza um jump incondicional a uma subrotina, armazenando o valor
	* de PC na posicao de memória de SP e armazenando em PC o seu valor
	* somado ao valor do SP, que após é incrementado.
	*/
	public void jsrr()
	{
			valorhex1=capturaValor("PC");
			valorhex2=capturaValor("SP");
			
			String linha[]={valorhex2,valorhex1,"DADOS"};
			if(sim.isHarvard())
			{
				sim.getMemoriaDados().setMemoria(linha);
				sim.getJanela().getTabelaMemoriaDados().setLinha(linha);
			}
			else
			{
				sim.getMemoria().setMemoria(linha);
				sim.getJanela().getTabelaMemoria().setLinha(linha);
			}
			
			if(sim.getMovPilha().equalsIgnoreCase("INC"))
					sim.getJanela().getReg("SP").incrementa();
			else					
					sim.getJanela().getReg("SP").incrementa();
					
			valorhex2=capturaValor(codigo3);
			resultadohex=soma(valorhex1,valorhex2);
			sim.getJanela().getReg("PC").setValReg(resultadohex);

			tratandoFlags("JSRR",valorhex1);
	}
	
	/**
	* Se o flag geral estiver setado em TRUE realiza um jump referente
	* ao registrador representado pelo codigo3 que é armazenado em PC.
	*/
	public void jmpc()
	{
		String flag_dep;
		flag_dep = instAtual.getFlag_dep();
		if(sim.getJanela().getFlag(flag_dep).getValFlag())
		{
			valorhex1=capturaValor(codigo3);
			sim.getJanela().getReg("PC").setValReg(valorhex1);
			
			tratandoFlags("JMPC",valorhex1);
		}	
	}

	/**
	* Realiza um jump incondicional referente ao registrador representado
	* pelo codigo3 que é armazenado em PC.
	*/
	public void jmp()
	{
		valorhex1=capturaValor(codigo3);
		sim.getJanela().getReg("PC").setValReg(valorhex1);

		tratandoFlags("JMP",valorhex1);
	}


	/**
	* JPRGMASK RS1  {if mask PC <- RS1;}
	* OP1 OP2 RS1 OP3
	* Realiza um jump condicional a mascara. Armazena em PC o valor do registrador
	* representado pelo codigo3.
	*/
	public void jprgMask()
	{
		if(testaMascara8bits())
		{
			valorhex1=capturaValor(codigo3);
			sim.getJanela().getReg("PC").setValReg(valorhex1);

			tratandoFlags("JPRGMARK",valorhex1);
		}
	}


	/**
	* Se o flag geral estiver setado em TRUE realiza um jump a uma
	* subrotina, armazenando o valor de PC na posicao de memória de
	* SP e armazenando em PC o valordo SP, que após é incrementado.
	*/
	public void jsrc()
	{
		String flag_dep;
		flag_dep=instAtual.getFlag_dep();
		
		if(sim.getJanela().getFlag(flag_dep).getValFlag())
		{
			valorhex1=capturaValor("SP");
			valorhex2=capturaValor("PC");
			
			String linha[]={valorhex1,valorhex2,"DADOS"};
			if(sim.isHarvard())
			{
				sim.getMemoriaDados().setMemoria(linha);
				sim.getJanela().getTabelaMemoriaDados().setLinha(linha);
			}
			else
			{
				sim.getMemoria().setMemoria(linha);
				sim.getJanela().getTabelaMemoria().setLinha(linha);
			}
			
			if(sim.getMovPilha().equalsIgnoreCase("INC"))			
				sim.getJanela().getReg("SP").incrementa();
			else
				sim.getJanela().getReg("SP").decrementa();
			valorhex1=capturaValor(codigo3);
		    sim.getJanela().getReg("PC").setValReg(valorhex1);

			tratandoFlags("JSRC",valorhex2);
		}
	}

	/**
	* Realiza um jump incondicional a uma subrotina, armazenando o valor
	* de PC na posicao de memória de SP e armazenando em PC o valor do
	* SP, que após é incrementado.
	*/
	public void jsr()
	{
			valorhex1=capturaValor("SP");
			valorhex2=capturaValor("PC");
			
			String linha[]={valorhex1,valorhex2,"DADOS"};
			if(sim.isHarvard())
			{
				sim.getMemoriaDados().setMemoria(linha);
				sim.getJanela().getTabelaMemoriaDados().setLinha(linha);
			}
			else
			{
				sim.getMemoria().setMemoria(linha);
				sim.getJanela().getTabelaMemoria().setLinha(linha);
			}
			
			if(sim.getMovPilha().equalsIgnoreCase("INC"))
				sim.getJanela().getReg("SP").incrementa();
			else
				sim.getJanela().getReg("SP").decrementa();
			valorhex1=capturaValor(codigo3);
			sim.getJanela().getReg("PC").setValReg(valorhex1);

			tratandoFlags("JSR",valorhex2);
	}

	/**
	* Se o flag geral estiver setado em TRUE realiza um jump referente
	* a um valor de deslocamento(12 bits) no qual é realizado a extensão
	* do sinal para posteriormente ser somado ao valor do PC e armazenado
	* no próprio PC.
	*/
	public void jmpdc()
	{
		String flag_dep;
		flag_dep=instAtual.getFlag_dep();
		
		if(sim.getJanela().getFlag(flag_dep).getValFlag())
		{
			valorhex1=capturaValor("PC");
			valorhex2=ext_sinal(codigo2);
			valordec2=sim.getConversao().hexa_decimal(valorhex2); //Valor
			if(valordec2>32767)
				valordec2=-(65535-valordec2+1);
			
			valorhex2=sim.getConversao().decimal_hexa(valordec2); //Valor
			resultadohex=soma(valorhex1,valorhex2);

			sim.getJanela().getReg("PC").setValReg(resultadohex);

			tratandoFlags("JMPDC");
		}	
	}

	/**
	* JMPDMASK constante12bits {if mask PC <- PC + (EXT_SINAL & CONSTANTE12BITS)}
	* OP1 CT1_11_8 CT1_7_4 CT1_3_0
	* Realiza um jump condicional a mascara. Armazena em PC a soma do PC com
	* o valor de deslocamento(12 bits) no qual é realizado a extensão do sinal.
	*/
	public void jmpdMask()
	{
		if(testaMascara8bits())
		{
			valorhex1=capturaValor("PC");
			valorhex2=ext_sinal(codigo2);
			valordec2=sim.getConversao().hexa_decimal(valorhex2); //Valor
			if(valordec2>32767)
					valordec2=-(65535-valordec2+1);
			valorhex2=sim.getConversao().decimal_hexa(valordec2); //Valor
			resultadohex=soma(valorhex1,valorhex2);

			sim.getJanela().getReg("PC").setValReg(resultadohex);
	
			tratandoFlags("JMPDMASK");
		}
	}


	/**
	* Realiza um jump incondicional referente a um valor de 
	* deslocamento(12 bits) no qual é realizado a extensão do sinal
	* para posteriormente ser somado ao valor do PC e armazenado
	* no próprio PC.
	*/
	public void jmpd()
	{
		valorhex1=capturaValor("PC");
		valorhex2=ext_sinal(codigo2);
		valordec2=sim.getConversao().hexa_decimal(valorhex2); //Valor
		if(valordec2>32767)
				valordec2=-(65535-valordec2+1);
		valorhex2=sim.getConversao().decimal_hexa(valordec2); //Valor
		resultadohex=soma(valorhex1,valorhex2);

		sim.getJanela().getReg("PC").setValReg(resultadohex);

		tratandoFlags("JMPD");
	}

	/**
	* Se o flag geral estiver setado em TRUE realiza um jump referente
	* a um valor de deslocamento(8 bits) no qual é realizado a extensão
	* do sinal para posteriormente ser somado ao valor do PC e armazenado
	* no próprio PC.
	*/
	public void jmpdc8()
	{
		String flag_dep;
		
		flag_dep=instAtual.getFlag_dep();
		if(sim.getJanela().getFlag(flag_dep).getValFlag())
		{
			valorhex1=capturaValor("PC");
			valorhex2=ext_sinal(codigo3);
			valordec2=sim.getConversao().hexa_decimal(valorhex2); //Valor
			if(valordec2>32767)
					valordec2=-(65535-valordec2+1);
			valorhex2=sim.getConversao().decimal_hexa(valordec2); //Valor
			resultadohex=soma(valorhex1,valorhex2);
	
			sim.getJanela().getReg("PC").setValReg(resultadohex);
			tratandoFlags("JMPDC8");
		}	
	}

	/**
	* Realiza um jump incondicional referente a um valor de 
	* deslocamento(8 bits) no qual é realizado a extensão do sinal
	* para posteriormente ser somado ao valor do PC e armazenado
	* no próprio PC.
	*/
	public void jmpd8()
	{
		valorhex1=capturaValor("PC");
		valorhex2=ext_sinal(codigo3);
		valordec2=sim.getConversao().hexa_decimal(valorhex2); //Valor
		if(valordec2>32767)
				valordec2=-(65535-valordec2+1);
		valorhex2=sim.getConversao().decimal_hexa(valordec2); //Valor
		resultadohex=soma(valorhex1,valorhex2);
	
		sim.getJanela().getReg("PC").setValReg(resultadohex);

		tratandoFlags("JMPD8");
	}

	/**
	* Se o flag geral estiver setado em TRUE realiza um jump referente
	* a um valor de deslocamento(10 bits) no qual é realizado a extensão
	* do sinal para posteriormente ser somado ao valor do PC e armazenado
	* no próprio PC.
	*/
	public void jmpdc10()
	{
		String flag_dep;
		
		flag_dep=instAtual.getFlag_dep();
		if(sim.getJanela().getFlag(flag_dep).getValFlag())
		{
			valorhex1=capturaValor("PC");
			// Captura somente os 10 bits menos significativos
			valorhex2=captura_10bits(codigo2);
			valordec2=sim.getConversao().hexa_decimal(valorhex2); //Valor
			if(valordec2>32767)
					valordec2=-(65535-valordec2+1);
			valorhex2=sim.getConversao().decimal_hexa(valordec2); //Valor
			resultadohex=soma(valorhex1,valorhex2);

			sim.getJanela().getReg("PC").setValReg(resultadohex);

			tratandoFlags("JMPDC10");
		}	
	}

	/**
	* Realiza um jump incondicional referente a um valor de 
	* deslocamento(10 bits) no qual é realizado a extensão do sinal
	* para posteriormente ser somado ao valor do PC e armazenado
	* no próprio PC.
	*/
	public void jmpd10()
	{
		valorhex1=capturaValor("PC");
		// Captura somente os 10 bits menos significativos
		valorhex2=captura_10bits(codigo2);
		valordec2=sim.getConversao().hexa_decimal(valorhex2); //Valor
		if(valordec2>32767)
				valordec2=-(65535-valordec2+1);
		valorhex2=sim.getConversao().decimal_hexa(valordec2); //Valor
		resultadohex=soma(valorhex1,valorhex2);

		sim.getJanela().getReg("PC").setValReg(resultadohex);

		tratandoFlags("JMPD10");
	}

	/**
	* Se o flag geral estiver setado em TRUE realiza um jump a uma
	* subrotina, armazenando o valor de PC na posicao de memória de
	* SP e armazenando em PC o seu valor somado ao valor do deslocamento
	* (12 bits) com a extensão do sinal.O SP posteriormente incrementado.
	*/
	public void jsrdc()
	{
		String flag_dep;
		flag_dep=instAtual.getFlag_dep();
		
		if(sim.getJanela().getFlag(flag_dep).getValFlag())
		{
			valorhex1=capturaValor("PC");
			valorhex2=capturaValor("SP");
			
			String linha[]={valorhex2,valorhex1,"DADOS"};
			if(sim.isHarvard())
			{
				sim.getMemoriaDados().setMemoria(linha);
				sim.getJanela().getTabelaMemoriaDados().setLinha(linha);
			}
			else
			{
				sim.getMemoria().setMemoria(linha);
				sim.getJanela().getTabelaMemoria().setLinha(linha);
			}
			
			if(sim.getMovPilha().equalsIgnoreCase("INC"))
				sim.getJanela().getReg("SP").incrementa();
			else
				sim.getJanela().getReg("SP").decrementa();
	
			valorhex2=ext_sinal(codigo2);
			resultadohex=soma(valorhex1,valorhex2);
	
			sim.getJanela().getReg("PC").setValReg(resultadohex);
			
			tratandoFlags("JSRDC");
		}	
	}

	/**
	* Realiza um jump condicional a uma subrotina, armazenando o valor de
	* PC na posicao de memória de SP e armazenando em PC o seu valor somado
	* ao valor do deslocamento(12 bits) com a extensão do sinal.O SP
	* posteriormente incrementado.
	*/
	public void jsrd()
	{
		valorhex1=capturaValor("PC");
		valorhex2=capturaValor("SP");
		
		String linha[]={valorhex2,valorhex1,"DADOS"};
		if(sim.isHarvard())
		{
			sim.getMemoriaDados().setMemoria(linha);
			sim.getJanela().getTabelaMemoriaDados().setLinha(linha);
		}
		else
		{
			sim.getMemoria().setMemoria(linha);
			sim.getJanela().getTabelaMemoria().setLinha(linha);
		}

		if(sim.getMovPilha().equalsIgnoreCase("INC"))
			sim.getJanela().getReg("SP").incrementa();
		else
			sim.getJanela().getReg("SP").decrementa();
		valorhex2=ext_sinal(codigo2);
		resultadohex=soma(valorhex1,valorhex2);

		sim.getJanela().getReg("PC").setValReg(resultadohex);
		
		tratandoFlags("JSRD");
	}

	/**
	* JSRDMASK constante12bits {if mask SP <- SP - 1(DEC) ou SP <- SP + 1(INC); PMEM(SP) <- PC; PC <- PC + constante12bits;}
	* OP1 CT1_11_8 CT1_7_4 CT1_3_0
	* Realiza um jump condicional a mascara. O SP é incrementado ou decrementado, na posição de memória de SP armazenando
	* o valor de PC e armazenando em PC o seu valor somado ao valor do deslocamento(12 bits) com a extensão do sinal.
	*/
	public void jsrdMask()
	{
		if(testaMascara8bits())
		{
			if(sim.getMovPilha().equalsIgnoreCase("INC"))
				sim.getJanela().getReg("SP").incrementa();
			else
				sim.getJanela().getReg("SP").decrementa();

			valorhex1=capturaValor("PC");
			valorhex2=capturaValor("SP");
			
			String linha[]={valorhex2,valorhex1,"DADOS"};
			if(sim.isHarvard())
			{
				sim.getMemoriaDados().setMemoria(linha);
				sim.getJanela().getTabelaMemoriaDados().setLinha(linha);
			}
			else
			{
				sim.getMemoria().setMemoria(linha);
				sim.getJanela().getTabelaMemoria().setLinha(linha);
			}
		
			valorhex2=ext_sinal(codigo2);
			resultadohex=soma(valorhex1,valorhex2);

			sim.getJanela().getReg("PC").setValReg(resultadohex);
			
			tratandoFlags("JSRDMASK");
		}
	}

	/**
	* STFM mask {if(mask(0)='1') flagNegativo=true; if(mask(1)='1') flagZero=false; if(mask(2)='1') flagCarry=true; if(mask(3)='1') flagOverflow=true;}
	* OP1 OP2 CT1_3_0 OP3
	* Liga flags da mascara. Por exemplo: codigo3=1001 (mask NZCV) logo flag
	* Negativo e flag Overflow serao ativados.
	*/
	public void stfm()
	{
		valordec1=sim.getConversao().hexa_decimal(codigo3); //mascara
		valorbin1=sim.getConversao().decimal_binario(valordec1);
		//captura os 4 bits menos significativos pq a conversao para binario retorna 16 bits
		valorbin1=valorbin1.substring(12,16);

		if(valorbin1.substring(0,1).equals("1"))
			sim.getJanela().getFlag("NEGATIVO").setValFlag(true);
		if(valorbin1.substring(1,2).equals("1"))
			sim.getJanela().getFlag("ZERO").setValFlag(true);
		if(valorbin1.substring(2,3).equals("1"))
			sim.getJanela().getFlag("CARRY").setValFlag(true);
		if(valorbin1.substring(3,4).equals("1"))
			sim.getJanela().getFlag("OVERFLOW").setValFlag(true);
	}

	/**
	* STMSK mask(8bits) {mascara <- mask}
	* OP1 CT17_4 CT1_3_0 OP2
	* Seta os bits da mascara N/N'/Z/Z'/C/C'/V/V'
	*/
	public void stmsk()
	{
		String mascaraHex = codigo2.concat(codigo3);
		String mascara=sim.getConversao().hexa_binario(mascaraHex,8);
		sim.setMascara(mascara);
	}

	/**
	* RSTFM mask {if(mask(0)='1') flagNegativo=false; if(mask(1)='1') flagZero=false; if(mask(2)='1') flagCarry=false; if(mask(3)='1') flagOverflow=false;}
	* OP1 OP2 CT1_3_0 OP3
	* Desliga flags da mascara. Por exemplo: codigo3=1001 (mask NZCV) logo flag
	* Negativo e flag Overflow serao desativados.
	*/
	public void rstfm()
	{
		valordec1=sim.getConversao().hexa_decimal(codigo3); //mascara
		valorbin1=sim.getConversao().decimal_binario(valordec1);
		//captura os 4 bits menos significativos pq a conversao para binario retorna 16 bits
		valorbin1=valorbin1.substring(12,16); 
		
		if(valorbin1.substring(0,1).equals("1"))
			sim.getJanela().getFlag("NEGATIVO").setValFlag(false);
		if(valorbin1.substring(1,2).equals("1"))
			sim.getJanela().getFlag("ZERO").setValFlag(false);
		if(valorbin1.substring(2,3).equals("1"))
			sim.getJanela().getFlag("CARRY").setValFlag(false);
		if(valorbin1.substring(3,4).equals("1"))
			sim.getJanela().getFlag("OVERFLOW").setValFlag(false);
	}


	/**
	* Se o flag negativo estiver setado em TRUE realiza um jump referente
	* ao registrador representado pelo codigo4 que é armazenado em PC.
	*/
	public void jn()
	{
		if(sim.getJanela().getFlag("NEGATIVO").getValFlag())
		{
			valorhex1=capturaValor(codigo4);
			sim.getJanela().getReg("PC").setValReg(valorhex1);
			
			tratandoFlags("JN",valorhex1);
		}			
	}

	/**
	* Se o flag de zero estiver setado em TRUE realiza um jump referente
	* ao registrador representado pelo codigo4 que é armazenado em PC.
	*/
	public void jz()
	{
		if(sim.getJanela().getFlag("ZERO").getValFlag())
		{
			valorhex1=capturaValor(codigo4);
			sim.getJanela().getReg("PC").setValReg(valorhex1);
			
			tratandoFlags("JZ",valorhex1);
		}			
	}
	
	/**
	* Se o flag de carry estiver setado em TRUE realiza um jump referente
	* ao registrador representado pelo codigo4 que é armazenado em PC.
	*/
	public void jc()
	{
		if(sim.getJanela().getFlag("CARRY").getValFlag())
		{
			valorhex1=capturaValor(codigo4);
			sim.getJanela().getReg("PC").setValReg(valorhex1);
			
			tratandoFlags("JC",valorhex1);
		}			
	}
	
	/**
	* Se o flag de overflow estiver setado em TRUE realiza um jump referente
	* ao registrador representado pelo codigo4 que é armazenado em PC.
	*/
	public void jv()
	{
		if(sim.getJanela().getFlag("OVERFLOW").getValFlag())
		{
			valorhex1=capturaValor(codigo4);
			sim.getJanela().getReg("PC").setValReg(valorhex1);

			tratandoFlags("JV",valorhex1);
		}			
	}

	/**
	* Se o flag negativo estiver setado em TRUE realiza um jump referente
	* ao registrador representado pelo codigo4 somado com PC e armazenado
	* em PC.
	*/
	public void jnr()
	{
		if(sim.getJanela().getFlag("NEGATIVO").getValFlag())
		{
			valorhex1=capturaValor(codigo4);
			valorhex2=capturaValor("PC");
			resultadohex=soma(valorhex1,valorhex2);

			sim.getJanela().getReg("PC").setValReg(resultadohex);
			
			tratandoFlags("JNR");
		}			
	}

	/**
	* Se o flag de zero estiver setado em TRUE realiza um jump referente
	* ao registrador representado pelo codigo4 somado com PC e armazenado
	* em PC.
	*/
	public void jzr()
	{
		if(sim.getJanela().getFlag("ZERO").getValFlag())
		{
			valorhex1=capturaValor(codigo4);
			valorhex2=capturaValor("PC");
			resultadohex=soma(valorhex1,valorhex2);

			sim.getJanela().getReg("PC").setValReg(resultadohex);
						
			tratandoFlags("JZR");
		}			
	}
	
	/**
	* Se o flag de carry estiver setado em TRUE realiza um jump referente
	* ao registrador representado pelo codigo4 somado com PC e armazenado
	* em PC.
	*/
	public void jcr()
	{
		if(sim.getJanela().getFlag("CARRY").getValFlag())
		{
			valorhex1=capturaValor(codigo4);
			valorhex2=capturaValor("PC");
			resultadohex=soma(valorhex1,valorhex2);

			sim.getJanela().getReg("PC").setValReg(resultadohex);
						
			tratandoFlags("JCR");
		}			
	}
	
	/**
	* Se o flag de overflow estiver setado em TRUE realiza um jump referente
	* ao registrador representado pelo codigo4 somado com PC e armazenado
	* em PC.
	*/
	public void jvr()
	{
		if(sim.getJanela().getFlag("OVERFLOW").getValFlag())
		{
			valorhex1=capturaValor(codigo4);
			valorhex2=capturaValor("PC");
			resultadohex=soma(valorhex1,valorhex2);
			sim.getJanela().getReg("PC").setValReg(resultadohex);
									
			tratandoFlags("JVR");
		}			
	}

	/**
	* Se o flag negativo estiver setado em TRUE realiza um jump referente
	* ao deslocamento com extensão de sinal que é somado com PC e armazenado
	* em PC.
	*/
	public void jnd()
	{
		if(sim.getJanela().getFlag("NEGATIVO").getValFlag())
		{
			valorhex1=ext_sinal(codigo2);
			valorhex2=capturaValor("PC");
			resultadohex=soma(valorhex1,valorhex2);

			sim.getJanela().getReg("PC").setValReg(resultadohex);
						
			tratandoFlags("JND");
		}			
	}

	/**
	* Se o flag de zero estiver setado em TRUE realiza um jump referente
	* ao deslocamento com extensão de sinal que é somado com PC e armazenado
	* em PC.
	*/
	public void jzd()
	{
		if(sim.getJanela().getFlag("ZERO").getValFlag())
		{
			valorhex1=ext_sinal(codigo2);
			valorhex2=capturaValor("PC");
			resultadohex=soma(valorhex1,valorhex2);

			sim.getJanela().getReg("PC").setValReg(resultadohex);

			tratandoFlags("JZD");
		}			
	}
	
	/**
	* Se o flag de carry estiver setado em TRUE realiza um jump referente
	* ao deslocamento com extensão de sinal que é somado com PC e armazenado
	* em PC.
	*/
	public void jcd()
	{
		if(sim.getJanela().getFlag("CARRY").getValFlag())
		{
			valorhex1=ext_sinal(codigo2);
			valorhex2=capturaValor("PC");
			resultadohex=soma(valorhex1,valorhex2);

			sim.getJanela().getReg("PC").setValReg(resultadohex);

			tratandoFlags("JCD");
		}			
	}
	
	/**
	* Se o flag de overflow estiver setado em TRUE realiza um jump referente
	* ao deslocamento com extensão de sinal que é somado com PC e armazenado
	* em PC.
	*/
	public void jvd()
	{
		if(sim.getJanela().getFlag("OVERFLOW").getValFlag())
		{
			valorhex1=ext_sinal(codigo2);
			valorhex2=capturaValor("PC");
			resultadohex=soma(valorhex1,valorhex2);

			sim.getJanela().getReg("PC").setValReg(resultadohex);

			tratandoFlags("JVD");
		}			
	}

	/**
	* Registrador R15 recebe o valor de PC e PC recebe PC 
	* adicionado da constante de 12 bits, representada nos
	* codigos 2, 3 e 4.
	*/
	public void jalr()
	{
		valorhex1=ext_sinal(codigo2);
		valorhex2=capturaValor("PC");
		//R15 <- PC
		sim.getJanela().getReg("RF").setValReg(valorhex2);
		resultadohex=soma(valorhex1,valorhex2);
		sim.getJanela().getReg("PC").setValReg(resultadohex);
		tratandoFlags("JALR");
	}

	/**
	* Se codigo3 < codigo4 então codigo2 <- 1 senão codigo2 <- 0.
	*/
	public void slt()
	{
		valorhex1=capturaValor(codigo3);
	  valorhex2=capturaValor(codigo4);
	  valordec1=sim.getConversao().hexa_decimal(valorhex1);
		valordec2=sim.getConversao().hexa_decimal(valorhex2);

		if((valordec1-valordec2)>32767)
		{
			//codigo2 <- 1
			resultadohex=sim.getConversao().decimal_hexa(1);
			sim.getJanela().getReg("R"+codigo2).setValReg(resultadohex);
		}
		else
		{
			//codigo2 <- 0
			resultadohex=sim.getConversao().decimal_hexa(0);
			sim.getJanela().getReg("R"+codigo2).setValReg(resultadohex);
		}
		tratandoFlags("JALR",resultadohex);
	}

	/**
	* Registrador R15 recebe o valor de PC e PC recebe (0FFH)
	* concatenado com a constante de 8 bits, representada nos
	* codigos 3 e 4.
	*/
	public void swi()
	{
		valorhex1=capturaValor("PC");
		valordec1=sim.getConversao().hexa_decimal(valorhex1);

		valorhex2=codigo3.concat(codigo4);
		valorhex2="FF".concat(valorhex2);

		//R15 <- PC
		sim.getJanela().getReg("RF").setValReg(valorhex1);
		// PC <- (0FFH) & imed8;
		sim.getJanela().getReg("PC").setValReg(valorhex2);

		tratandoFlags("SWI",valorhex1);
	}


	/**
	* SWIP CT1 {SP <- SP + 1(INC) ou SP <- SP - 1(DEC); PMEM(SP) <- PC; PC <- (FFH) & constante;}
	* OP1 OP2 CT1_7_4 CT1_3_0
	* Pré-incrementa ou pré-decrementa o registrador SP, armazena
	* na posição de memória apontada por SP o valor do registrador PC e
	* PC recebe (0FFH) concatenado com a constante de 8 bits representada 
	* nos codigos 3 e 4.
	*/
	public void swip()
	{
		if(sim.getMovPilha().equalsIgnoreCase("DEC"))
			sim.getJanela().getReg("SP").decrementa();
		else
			sim.getJanela().getReg("SP").incrementa();

		valorhex1=capturaValor("PC");
		valorhex2=capturaValor("SP");
	
		String linha[]={valorhex2,valorhex1,"DADOS"};
		if(sim.isHarvard())
		{
			sim.getMemoriaDados().setMemoria(linha);
			sim.getJanela().getTabelaMemoriaDados().setLinha(linha);
		}
		else
		{
			sim.getMemoria().setMemoria(linha);
			sim.getJanela().getTabelaMemoria().setLinha(linha);
		}
		
		valorhex2=codigo3.concat(codigo4);
		valorhex2="FF".concat(valorhex2);

		// PC <- (0FFH) & imed8;
		sim.getJanela().getReg("PC").setValReg(valorhex2);

		tratandoFlags("SWIP",valorhex2);
	}


	/**
	* Se codigo3 == codigo4 entao PC <- PC + 1
	*/
	public void skpeq()
	{
		valorhex1=capturaValor(codigo3);
		valordec1=sim.getConversao().hexa_decimal(valorhex1);

		valorhex2=capturaValor(codigo4);
		valordec2=sim.getConversao().hexa_decimal(valorhex2);
		
		//Se codigo3 == codigo4
		if(valordec1 == valordec2)
		{
			//PC <- Pc + 1
			sim.getJanela().getReg("PC").incrementa();
				
			tratandoFlags("SKPEQ");
		}
	}

	/**
	* Se codigo3 <> codigo4 entao PC <- PC + 1
	*/
	public void skpne()
	{
		valorhex1=capturaValor(codigo3);
		valordec1=sim.getConversao().hexa_decimal(valorhex1);

		valorhex2=capturaValor(codigo4);
		valordec2=sim.getConversao().hexa_decimal(valorhex2);
		
		//Se codigo3 <> codigo4
		if(valordec1 != valordec2)
		{
			//PC <- Pc + 1
			sim.getJanela().getReg("PC").incrementa();
				
			tratandoFlags("SKPNE");
		}
	}

	/**
	* Se codigo3 <> codigo4 entao PC <- PC + 1
	*/
	public void skpeqi()
	{
		valorhex1=ext_sinal_4bits(codigo3);
		valordec1=sim.getConversao().hexa_decimal(valorhex1);

		valorhex2=capturaValor(codigo4);
		valordec2=sim.getConversao().hexa_decimal(valorhex2);
		
		//Se codigo3 == codigo4
		if(valordec1 == valordec2)
		{
			//PC <- Pc + 1
			sim.getJanela().getReg("PC").incrementa();
			
			tratandoFlags("SKPEQI");
		}
	}

	/**
	* Se codigo3 <> codigo4 entao PC <- PC + 1
	*/
	public void skpnei()
	{
		valorhex1=ext_sinal_4bits(codigo3);
		valordec1=sim.getConversao().hexa_decimal(valorhex1);

		valorhex2=capturaValor(codigo4);
		valordec2=sim.getConversao().hexa_decimal(valorhex2);
		
		//Se codigo3 <> codigo4
		if(valordec1 != valordec2)
		{
			//PC <- Pc + 1
			sim.getJanela().getReg("PC").incrementa();
				
			tratandoFlags("SKPNEI");
		}
	}


	/**
	* Instrução utilizada no configurador quando não existe uma instrução associada
	* no simulador. Não modifica o estado de simulação e ewnvia mensagem alertando
	* aso usuário.
	*/
	public void	ignore()
	{
   	JOptionPane.showMessageDialog(null,"Não existe instrução associada ao mneumônico.","Simulador de Arquiteturas",JOptionPane.INFORMATION_MESSAGE);
	}

	
	/**
	* Concatena ZEROs a esquerda se o bit mais significativo é ZERO, e
	* concatena UMs caso o bit mais significativo seja UM.
	*/
	public String ext_sinal(String codigo)
	{
		String valor="";
				
		valordec1=sim.getConversao().hexa_decimal(codigo);
		
		if(valordec1<8)
		{
			if(codigo.equals(codigo2))
				valor=("0");
			else if(codigo.equals(codigo3))
				valor=("00");
			else if(codigo.equals(codigo4))
				valor=("000");
		}
		else	
		{
			if(codigo.equals(codigo2))
				valor=("F");
			else if(codigo.equals(codigo3))
				valor=("FF");
			else if(codigo.equals(codigo4))
				valor=("FFF");
		}
		
		if(codigo.equals(codigo2))
			valor=valor.concat(codigo2);
		if(codigo.equals(codigo2)||codigo.equals(codigo3))
			valor=valor.concat(codigo3);
		if(codigo.equals(codigo2)||codigo.equals(codigo3)||codigo.equals(codigo4))
			valor=valor.concat(codigo4);

		return valor;
	}

	/**
	* Concatena ZEROs a esquerda se o bit mais significativo é ZERO, e
	* concatena UMs caso o bit mais significativo seja UM.
	*/
	public String ext_sinal_4bits(String codigo)
	{
		valordec1=sim.getConversao().hexa_decimal(codigo);
		if(valordec1<8)
			return "000".concat(codigo);
		else	
			return "FFF".concat(codigo);
	}

	/**
	* Concatena ZEROs a esquerda se o bit mais significativo é ZERO, e
	* concatena UMs caso o bit mais significativo seja UM para .
	*/
	public String captura_10bits(String codigo)
	{
		String valor="";
				
		valordec1=sim.getConversao().hexa_decimal(codigo);
		valorbin1=sim.getConversao().decimal_binario(valordec1);
		valorbin1=valorbin1.substring(14,16); //captura 8 bits - significativos

		//extensão de sinal do codigo
		if (valorbin1.substring(0,1).equals("0"))
			valorbin1= "000000".concat(valorbin1);
		else
			valorbin1= "111111".concat(valorbin1);
			
		valordec1=sim.getConversao().binario_decimal(valorbin1);
		valor = sim.getConversao().decimal_hexa(valordec1);
		valor = valor.substring(2,4); //captura a parte válida do valor
		
		if(codigo.equals(codigo1))
			valor = valor + codigo2 + codigo3;
		else if(codigo.equals(codigo2))
			valor = valor + codigo3 + codigo4;

		return valor;
	}


	/**
	* Captura o valor armazenado no registrador referente ao codigo. 
	*/
	public String capturaValor(String codigo)
	{
		String valor;

		if(codigo.equals("PC"))
			valor=sim.getJanela().getReg("PC").getValReg();
		else if(codigo.equals("SP"))
			valor=sim.getJanela().getReg("SP").getValReg();
		else		
			valor=sim.getJanela().getReg("R"+codigo).getValReg();
	
		if(valor.equals(""))
			valor="0000";
		return valor;
	}	
	
	/**
	* Executa a operação lógica AND.
	*/
	public String e(String valorbin1,String valorbin2)
	{
		String resultado,res;
		String val1,val2;
		
		resultado="";
		
		for(int bit=16;bit>0;bit--)
		{
			val1=valorbin1.substring(bit-1,bit);
			val2=valorbin2.substring(bit-1,bit);
			if(val1.equals("0")||val2.equals("0"))
				res="0";
			else
				res="1";
			resultado=res.concat(resultado);
		}

		setFlags(sim.getConversao().binario_hexa(resultado));
		return resultado;
	}
	
	/**
	* Executa a operação lógica OR.
	*/
	public String or(String valorbin1,String valorbin2)
	{
		String resultado,res;
		String val1,val2;
		
		resultado="";
		
		for(int bit=16;bit>0;bit--)
		{
			val1=valorbin1.substring(bit-1,bit);
			val2=valorbin2.substring(bit-1,bit);
			if(val1.equals("1")||val2.equals("1"))
				res="1";
			else
				res="0";
			resultado=res.concat(resultado);
		}
		setFlags(sim.getConversao().binario_hexa(resultado));

		return resultado;
	}

	/**
	* Executa a operação lógica XOR.
	*/
	public String xor(String valorbin1,String valorbin2)
	{
		String resultado,res;
		String val1,val2;
		
		resultado="";
		
		for(int bit=16;bit>0;bit--)
		{
			val1=valorbin1.substring(bit-1,bit);
			val2=valorbin2.substring(bit-1,bit);
			if((val1.equals("0")&&val2.equals("0"))||(val1.equals("1")&&val2.equals("1")))
				res="0";
			else
				res="1";
			resultado=res.concat(resultado);
		}
		
		setFlags(sim.getConversao().binario_hexa(resultado));
    
		return resultado;
	}
	
	/**
	* Soma os valores informados e seta os flags.
	*/
	public String soma(String valorHex1,String valorHex2)
	{
		String valorBin1,valorBin2,resultadoBin,resultadoHex,carry;
		valorBin1=sim.getConversao().hexa_binario(valorHex1);
		valorBin2=sim.getConversao().hexa_binario(valorHex2);
		resultadoBin="";
		carry="0";
		for(int i=16;i>0;i--)
		{
			if(valorBin1.substring(i-1,i).equals("1"))
			{
				if(valorBin2.substring(i-1,i).equals("1"))
				{
					if(carry.equals("1")) // 1 1 1 -> 1 1
					{
						resultadoBin="1".concat(resultadoBin);	
						carry="1";
					}
					else // 1 1 0 -> 0 1
					{
						resultadoBin="0".concat(resultadoBin);	
						carry="1";
					}
				}
				else
				{
					if(carry.equals("1")) // 1 0 1 -> 0 1
					{
						resultadoBin="0".concat(resultadoBin);	
						carry="1";
					}
					else // 1 0 0 -> 1 0
					{
						resultadoBin="1".concat(resultadoBin);	
						carry="0";
					}
				}
			}	
			else
			{
				if(valorBin2.substring(i-1,i).equals("1"))
				{
					if(carry.equals("1")) // 0 1 1 -> 0 1
					{
						resultadoBin="0".concat(resultadoBin);	
						carry="1";
					}
					else // 0 1 0 -> 1 0
					{
						resultadoBin="1".concat(resultadoBin);	
						carry="0";
					}
				}
				else
				{
					if(carry.equals("1")) // 0 0 1 -> 1 0
					{
						resultadoBin="1".concat(resultadoBin);	
						carry="0";
					}
					else // 0 0 0 -> 0 0
					{
						resultadoBin="0".concat(resultadoBin);	
						carry="0";
					}
				}
			}
		}
		resultadoHex = sim.getConversao().binario_hexa(resultadoBin);
		setFlags("soma",resultadoHex,valorHex1,valorHex2,carry);
		return resultadoHex;		
	}

	/**
	* Subtrai os valores em complemento de dois.
	*/
	public String subt(String valorHex1,String valorHex2)
	{
		valorHex2=sim.getConversao().complemento2(valorHex2);
		String resultado= soma(valorHex1,valorHex2);
		return resultado;
	}

	/**
	* Trata os flags da instrução.
	*/
	public void tratandoFlags(String inst,String valorhex,String[] noModifyFlags)
	{
		setFlags(valorhex);
		tratandoFlags(inst,noModifyFlags);
	}

	/**
	* Trata os flags da instrução.
	*/
	public void tratandoFlags(String inst,String valorhex)
	{
		setFlags(valorhex);
		tratandoFlags(inst);
	}

	/**
	* Trata os flags da instrução.
	*/
	public void tratandoFlags(String inst,String[] noModifyFlags)
	{
		String [] flags;
		flags=instAtual.getFlags();
		
		for(int i=0;i<instAtual.getNflag();i++)
		{
			if(canModifyFlag(noModifyFlags,"GERAL") && flags[i]!=null && flags[i].equalsIgnoreCase("GERAL"))
				sim.getJanela().getFlag("GERAL").setValFlag(geral);
			if(canModifyFlag(noModifyFlags,"ZERO") && flags[i]!=null && flags[i].equalsIgnoreCase("ZERO"))
				sim.getJanela().getFlag("ZERO").setValFlag(zero);
			if(canModifyFlag(noModifyFlags,"CARRY") && flags[i]!=null && flags[i].equalsIgnoreCase("CARRY"))
				sim.getJanela().getFlag("CARRY").setValFlag(carry);
			if(canModifyFlag(noModifyFlags,"NEGATIVO") && flags[i]!=null && flags[i].equalsIgnoreCase("NEGATIVO"))
				sim.getJanela().getFlag("NEGATIVO").setValFlag(negativo);
			if(canModifyFlag(noModifyFlags,"OVERFLOW") && flags[i]!=null && flags[i].equalsIgnoreCase("OVERFLOW"))
				sim.getJanela().getFlag("OVERFLOW").setValFlag(overflow);
		}
	}

	
	/**
	* Trata os flags da instrução.
	*/
	public void tratandoFlags(String inst)
	{
		String [] flags;
		flags=instAtual.getFlags();
		
		for(int i=0;i<instAtual.getNflag();i++)
		{
			if(flags[i]!=null && flags[i].equalsIgnoreCase("GERAL"))
				sim.getJanela().getFlag("GERAL").setValFlag(geral);
			if(flags[i]!=null && flags[i].equalsIgnoreCase("ZERO"))
				sim.getJanela().getFlag("ZERO").setValFlag(zero);
			if(flags[i]!=null && flags[i].equalsIgnoreCase("CARRY"))
				sim.getJanela().getFlag("CARRY").setValFlag(carry);
			if(flags[i]!=null && flags[i].equalsIgnoreCase("NEGATIVO"))
				sim.getJanela().getFlag("NEGATIVO").setValFlag(negativo);
			if(flags[i]!=null && flags[i].equalsIgnoreCase("OVERFLOW"))
				sim.getJanela().getFlag("OVERFLOW").setValFlag(overflow);
		}
	}

	public boolean canModifyFlag(String[] noModifyFlags,String flag)
	{
		for(int i=0;i<noModifyFlags.length;i++)
		{
			if(noModifyFlags[i].equalsIgnoreCase(flag))
				return false;
		}
		return true;	
	}


	public void setFlags(String resultadoHex)
	{
	    //setFlagGeral(resultado);
	    setFlagZero(resultadoHex);
	    setFlagNegativo(resultadoHex);
	}

	public void setFlags(String inst,String resultadoHex,String valorHex1,String valorHex2,String carry)
	{
	    //setFlagGeral(resultado);
	    setFlagZero(resultadoHex);
	    setFlagNegativo(resultadoHex);
	    setFlagCarry(carry);
		setFlagOverflow(inst,resultadoHex,valorHex1,valorHex2);
	}

	public void setFlagGeral(int resultado)
	{
		geral=false;
	}
	
	/**
	* Seta o flag de Zero para um, quando o valor é zero e para zero
	* quando o valor é diferente de zero. 
	*/
	public void setFlagZero(String resultadoHex)
	{
		int resultadoDec=sim.getConversao().hexa_decimal(resultadoHex);
		if(resultadoDec==0)
			zero=true;
		else
			zero=false;
	}
	
	/**
	* Seta o flag de Carry para um, quando a operacao resultou um
	* valor superior a 65535; caso contrario seta o flag para zero.
	*/
	public void setFlagCarry(String carryout)
	{
		if(carryout.equals("1"))
			carry=true;
		else
			carry=false;	
	}
	
	/**
	* Seta o Flag de Negativo para um quando o valor for menor que
	* zero, caso contrário, seta o flag para zero.
	*/
	public void setFlagNegativo(String resultadoHex)
	{
		String valorbin=sim.getConversao().hexa_binario(resultadoHex,16);
		if(valorbin.substring(0,1).equals("1"))
			negativo=true;
		else	
			negativo=false;
	}
	
	/**
	* Seta o Flag de Overflow para 1 quando:
	* Adição: a[15]+ e b[15]+ e resultado[15]-
	*         a[15]- e b[15]- e resultado[15]+
	*
	* Subtração: a[15]+ e b[15]- e resultado[15]-
	*            a[15]- e b[15]+ e resultado[15]+
	*/
	public void setFlagOverflow(String inst,String resultadoHex, String valorHex1, String valorHex2)
	{
		String valorbin1=sim.getConversao().hexa_binario(valorHex1,16);
		String valorbin2=sim.getConversao().hexa_binario(valorHex2,16);
		String valorbinR=sim.getConversao().hexa_binario(resultadoHex,16);
		
		if(inst.equalsIgnoreCase("soma"))
		{
			if(valorbin1.substring(0,1).equals("0") && valorbin2.substring(0,1).equals("0") && valorbinR.substring(0,1).equals("1"))
				overflow=true;
			else if(valorbin1.substring(0,1).equals("1") && valorbin2.substring(0,1).equals("1") && valorbinR.substring(0,1).equals("0"))
				overflow=true;
			else 
				overflow=false;
		}
		else if(inst.equalsIgnoreCase("subt"))
		{
			if(valorbin1.substring(0,1).equals("0") && valorbin2.substring(0,1).equals("1") && valorbinR.substring(0,1).equals("1"))
				overflow=true;
			else if(valorbin1.substring(0,1).equals("1") && valorbin2.substring(0,1).equals("0") && valorbinR.substring(0,1).equals("0"))
				overflow=true;
			else 
				overflow=false;
		}
		else
			overflow=false;
	}
	
	/**
	* Testa a mascara informada. A mascara informada são 4 bits em binário
	* representando os flags NZCV respectivamente, e para que o teste retorne
	* TRUE os bits ativos (em 1) da mascara devem ter seus correspondentes
	* flags também ativos.
	* Por exemplo:
	*  mascara 1010, retornará TRUE se os flags Negativo e Carry estiverem ativos.
	*  mascara 0100, retornará TRUE se o flag de Zero estiver ativo.
	*/
	public boolean testaMascara(String mascara)
	{
		//Se a mascara esta ativa o flag deve estar ativo.
		if(mascara.substring(0,1).equals("1"))
		{
			if(!sim.getJanela().getFlag("NEGATIVO").getValFlag())
				return false;
		}
		if(mascara.substring(1,2).equals("1"))
		{
			if(!sim.getJanela().getFlag("ZERO").getValFlag())
				return false;
		}
		if(mascara.substring(2,3).equals("1"))
		{
			if(!sim.getJanela().getFlag("CARRY").getValFlag())
				return false;
		}
		if(mascara.substring(3,4).equals("1"))
		{
			if(!sim.getJanela().getFlag("OVERFLOW").getValFlag())
				return false;
		}
		return true;
	}


	/**
	* Testa a mascara de 8 bits em binário representando os flags N/N'/Z/Z'/C/C'/V/V'
	* respectivamente, e para que o teste retorne TRUE os bits ativos (em 1) da mascara
	* devem ter seus correspondentes flags também ativos.
	* Por exemplo:
	*  mascara 10100000, retornará TRUE se os flags Negativo e Zero estiverem ativos.
	*
	* JUMP = N.B7+N'.B6+Z.B5+Z'.B4+C.B3+C'.B2+V.B1+V'.B0
	*/
	public boolean testaMascara8bits()
	{
		String mascara=sim.getMascara();

		//Se a mascara esta ativa o flag deve estar ativo.
		if(mascara.substring(0,1).equals("1"))
		{
			if(sim.getJanela().getFlag("NEGATIVO").getValFlag())
				return true;
		}
		if(mascara.substring(1,2).equals("1"))
		{
			if(!sim.getJanela().getFlag("NEGATIVO").getValFlag())
				return true;
		}
		if(mascara.substring(2,3).equals("1"))
		{
			if(sim.getJanela().getFlag("ZERO").getValFlag())
				return true;
		}
		if(mascara.substring(3,4).equals("1"))
		{
			if(!sim.getJanela().getFlag("ZERO").getValFlag())
				return true;
		}
		if(mascara.substring(4,5).equals("1"))
		{
			if(sim.getJanela().getFlag("CARRY").getValFlag())
				return true;
		}
		if(mascara.substring(5,6).equals("1"))
		{
			if(!sim.getJanela().getFlag("CARRY").getValFlag())
				return true;
		}
		if(mascara.substring(6,7).equals("1"))
		{
			if(sim.getJanela().getFlag("OVERFLOW").getValFlag())
				return true;
		}
		if(mascara.substring(7,8).equals("1"))
		{
			if(!sim.getJanela().getFlag("OVERFLOW").getValFlag())
				return true;
		}
		return false;
	}
}