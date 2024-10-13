/*
* @(#)Tabela.java  1.0  30/03/2001
*	
* Aline Vieira de Mello
* Pontifícia Universidade Católica do Rio Grande do Sul - Faculdade de Informática
*/

import java.awt.*;
import java.lang.*;
import javax.swing.*;
import javax.swing.table.*;

/**
* <i>Tabela</i> implementa Tabela.
*/
public class Tabela extends JTable
{
	private Simulador sim;
	private int ncolunas;
	private int nlinhas;
	private int[] dimcoluna;
	
	public Tabela(Simulador sim,Object[][] data,String[] nomecolunas,int nlinhas,int ncolunas,int[] dimcoluna )
	{
		super(data,nomecolunas);
		this.sim=sim;
		this.nlinhas=nlinhas;
		this.ncolunas=ncolunas;
		this.dimcoluna=dimcoluna;
		removeEditor();
		redimensionaColunas();
	}
	
	/**
	* Redimensiona a largura das colunas.
	*/
	public void redimensionaColunas()
	{         
		TableColumn column = null;
    for (int i = 0; i < ncolunas; i++)
    {
    	column = getColumnModel().getColumn(i);
      column.setPreferredWidth(dimcoluna[i]); //sport column is bigger
    }
	}

	/**
	* Adiciona um memu de opcoes em uma coluna da tabela.
	*/
	public void memuColuna(int ncoluna,int nitem,String[] item)
	{
  	TableColumn sportColumn = this.getColumnModel().getColumn(ncoluna);
    JComboBox comboBox = new JComboBox();
    for(int i=0;i<nitem;i++)
		{
    	comboBox.addItem(item[i]);
		}
    sportColumn.setCellEditor(new DefaultCellEditor(comboBox));
	}
	
	/**
	* Inicializa a tabela.
	*/
	public void inicializa(Object[][] dados)
	{
		//System.out.println("inicializa Tabela,,,,");
		for(int i=0;i<nlinhas;i++)
		{
			for(int j=0;j<ncolunas;j++)
				setValueAt(dados[i][j],i,j);
		}
		//System.out.println("fim inicializa Tabela,,,,");
	}
	
	/**
	* Atribui um novo valor as colunas de uma linha da tabela.
	* dado[0]=coluna1 dado[1]=coluna2 dado[2]=coluna0
  */
	public void setLinha(String[] dado)
	{
		int linha= sim.getConversao().hexa_decimal(dado[0]);
		setValueAt(dado[2],linha,0);
		setValueAt(dado[1],linha,2);
	}

	/**
	* Atribui um novo valor as colunas de uma linha da tabela.
	* endereco=coluna1 valor=coluna2
  */
	public void setLinha(String endereco)
	{
		for(int i=0;i<nlinhas;i++)
		{
			if(((String)getValueAt(i,1)).equalsIgnoreCase(endereco))
			{
				setValueAt("",i,0);
				setValueAt("",i,1);
				setValueAt("",i,2);
			}
		}
	}
}