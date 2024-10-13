/*
* @(#)Janela.java  1.0  30/03/2001
*	
* Aline Vieira de Mello
* Pontifícia Universidade Católica do Rio Grande do Sul - Faculdade de Informática
*/

import java.io.File;
import javax.swing.*;
import javax.swing.filechooser.*;

/**
* <i>FiltraArquivos</i> filtra apresentando todos os diretórios e
* arquivos com a extensão especificada.
*/
public class FiltraArquivos extends FileFilter
{
	private String ext;
		
	public FiltraArquivos(String ext)
	{
		this.ext=ext;	
	}
	
  public boolean accept(File f)
  {
    if (f.isDirectory())
      return true;
    
    String arquivo=f.getName();
    String extension="";
       
    int i = arquivo.lastIndexOf('.');

    if (i > 0 &&  i < arquivo.length() - 1)
      extension=arquivo.substring(i+1).toLowerCase();
       
    if (extension != null)
    {
      if (extension.equals(ext))
          return true;
      else
          return false;
    }
    return false;
  }
    
  // Descrição do files of type
  public String getDescription()
  {
    String ponto=".";
    return ponto.concat(ext);
  }
}