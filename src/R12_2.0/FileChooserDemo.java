/*
* @(#)FileChooserDemo.java  1.0  30/03/2001
*	
* Aline Vieira de Mello
* Pontifícia Universidade Católica do Rio Grande do Sul - Faculdade de Informática
*/
import java.io.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.filechooser.*;

/**
* <i>FileChooserDem</i> Implementa a Janela para abertura ou gravação de arquivos.
*/
public class FileChooserDemo extends JFrame implements WindowListener
{
	static private final String newline = "\n";
	
	public FileChooserDemo()
	{
  	super("FileChooserDemo");
		addWindowListener(this);
    final JTextArea log= new JTextArea(5,20);
    log.setMargin(new Insets(5,5,5,5));
    log.setEditable(false);
    JScrollPane logScrollPane = new JScrollPane(log);
	}

	public void windowActivated(WindowEvent e) {}
	public void windowClosed(WindowEvent e) {}
	public void windowDeactivated(WindowEvent e) {}
	public void windowDeiconified(WindowEvent e) {}
	public void windowIconified(WindowEvent e) {}
	public void windowOpened(WindowEvent e) {}
	public void windowClosing(WindowEvent e){this.dispose();}  
}