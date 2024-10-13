/*
* @(#)Help.java  1.0  30/03/2001
*	
* Aline Vieira de Mello
* Pontifícia Universidade Católica do Rio Grande do Sul - Faculdade de Informática
*/

import java.io.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
* <i>Help</i> contem o help do Simulador.
*/
public class Help
{
	private JDialog d;
	private TextArea area;
	private Simulador sim;
	
  public Help(Simulador sim)      
  { 
  	this.sim=sim;
  }

  /**
  * Apresenta o help.
  */
  public void apresenta()
	{
		//area = new JTextArea(); 
		area = new TextArea(); 		
		//area.setBounds(0,0,500,400);
		try
		{
			DataInputStream dis=new DataInputStream(new FileInputStream("SimuladorHelp.txt"));
   		byte b[]=new byte[dis.available()];
   		dis.readFully(b);
   		area.append(new String(b));
   		area.setEditable(false);
			d = new JDialog(sim.getJanela());
			//d = new Dialog(sim.getJanela());
		  d.setTitle("Ajuda");
   		d.getContentPane().add(area);
   		//d.add(area);
   		d.setSize(500,400);
	  	d.setLocation(150,80);
	  	d.addWindowListener(new WindowAdapter(){public void windowClosing(WindowEvent e){d.dispose();}});
			d.setVisible(true);
		}
		catch(IOException e)
		{
			d.dispose();
			JOptionPane.showMessageDialog(sim.getJanela(),"Help não encontrado.","ERRO", JOptionPane.ERROR_MESSAGE);
		}
	}
}