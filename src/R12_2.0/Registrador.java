/*
* @(#)Registrador.java  1.0  30/03/2001
*	
* Aline Vieira de Mello
* Pontifícia Universidade Católica do Rio Grande do Sul - Faculdade de Informática
*/

import javax.swing.*;

/**
* <i>Registrador</i> implementa o registrador.
*/
public class Registrador 
{
	private Simulador sim;
	private	JTextField	treg;
	private JLabel			lreg;
	private String 			nome;
	private int tam=5;
	
	public Registrador(Simulador sim,JPanel panel,String nome,int x,int y,int dimx,int dimy,String msg)
	{
		this.sim=sim;
		this.nome=nome;
		lreg=new JLabel(nome);
		lreg.setBounds(x,y,dimx,dimy);
		panel.add(lreg);
		treg=new JTextField(tam);
		treg.setHorizontalAlignment(treg.CENTER);
		treg.setBounds(x+25,y,dimx,dimy);
		treg.setEnabled(false);
		treg.setToolTipText(msg);
		panel.add(treg);
	}	
	
	//RETURN
	public String getNome(){return nome;}
	public String getValReg(){return treg.getText();} 
	
	//SETS
	public void setValReg(String valor){treg.setText(valor);}
	
	/**
	* Incrementa, ou seja, adiciona 1 ao valor do registrador.
	*/
	public void incrementa()
	{
		String valorhex;
		int valordec;
		
		valorhex=getValReg();
		valordec=sim.getConversao().hexa_decimal(valorhex);
		valordec=valordec+1;
		valorhex=sim.getConversao().decimal_hexa(valordec);
		setValReg(valorhex);
	}
	
	/**
	* Decrementa, ou seja, diminui 1 ao valor do registrador.
	*/
	public void decrementa()
	{
		String valorhex;
		int valordec;
		
		valorhex=getValReg();
		valordec=sim.getConversao().hexa_decimal(valorhex);
		valordec=valordec-1;
		valorhex=sim.getConversao().decimal_hexa(valordec);
		setValReg(valorhex);
	}
}