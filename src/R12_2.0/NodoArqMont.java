/*
* @(#)NodoArqMont.java  1.0  30/03/2001
*	
* Aline Vieira de Mello
* Pontifícia Universidade Católica do Rio Grande do Sul - Faculdade de Informática
*/

import java.io.*;

/**
* <i>NodoArqMont</i> implementa o nodo da lista de instruções descritas pelo
* Configurador de Arquitetura.
*/
public class NodoArqMont implements Serializable
{
	private String cod[];
	private String campo[];
	private String instrucao;
	private String mneumonico;
	private String flags[];
	private String flagDep;
	private int nflag;
	private int nclock;
	private NodoArqMont prox;

	public NodoArqMont(String mneum,String inst,int ncl,String c1,String c2,String c3,String c4,String camp1,String camp2,String camp3,String camp4,String flagDep,int nflag,String[] flags)
	{
		cod=new String[4];
		campo=new String[4];
		mneumonico=mneum;
		instrucao=inst;
		nclock=ncl;
		cod[0]=c1;
		cod[1]=c2;
		cod[2]=c3;
		cod[3]=c4;
		campo[0]=camp1;
		campo[1]=camp2;
		campo[2]=camp3;
		campo[3]=camp4;
		this.flagDep=flagDep;
		this.nflag=nflag;
		this.flags=flags;
		prox=null;		
	}

//RETURNs
	public String getMneumonico()	{return mneumonico;}
	public String getInstrucao()	{return instrucao;}
	public int getNclock()	{return nclock;}

	public String getCod(int n) {return cod[n];}
	public String getCod1()	{return cod[0];}
	public String getCod2()	{return cod[1];}
	public String getCod3()	{return cod[2];}
	public String getCod4()	{return cod[3];}

	public String getCampo(int n) {return campo[n];}
	public String getCampo1()	{return campo[0];}
	public String getCampo2()	{return campo[1];}
	public String getCampo3()	{return campo[2];}
	public String getCampo4()	{return campo[3];}

	public String getFlag_dep() {return flagDep;}
	public int getNflag(){return nflag;}
	public String[] getFlags() {return flags;}
	public String getFlags(int indice) {return flags[indice];}
	
	public NodoArqMont getProx()	{return prox;}

//SETs
	public void setMneumonico(String m)	{mneumonico=m;}
	public void setInstrucao(String i)	{instrucao=i;}
	public void setNclock(int n)	{nclock=n;}
		
	public void setCod(int n,String c) {cod[n]=c;}
	public void setCod1(String c)	{cod[0]=c;}
	public void setCod2(String c)	{cod[1]=c;}
	public void setCod3(String c)	{cod[2]=c;}
	public void setCod4(String c)	{cod[3]=c;}

	public void setCampo(int n,String c) {campo[n]=c;}
	public void setCampo1(String c)	{campo[0]=c;}
	public void setCampo2(String c)	{campo[1]=c;}
	public void setCampo3(String c)	{campo[2]=c;}
	public void setCampo4(String c)	{campo[3]=c;}

	public void setFlag_Dep(String f) {flagDep=f;}
	public void setNflag(int n) {nflag=n;}
	public void setFlags(String[] f)	{flags=f;}

	public void setProx(NodoArqMont aux)	{prox=aux;}

	/**
	* Retorna o indice do campo.
	*/
	public int getIndiceCampo(String c)
	{
		for(int i=0;i<4;i++)
		{
			if(campo[i].toUpperCase().indexOf(c.toUpperCase())!=-1)	
				return i;
		}
		return -1;
	}
	
	/**
	* Verifica se o campo informado é uma constante.
	*/
	public boolean isCampoConstante(String campo)
	{
		if(campo.toUpperCase().indexOf("CT")!=-1)
			return true;
		return false;		
	}

	/**
	* Verifica se o campo informado é um opcode.
	*/
	public boolean isCampoOpcode(String campo)
	{
		if(campo.indexOf("OP")!=-1)
			return true;
		else if(new Conversao().isHexa(campo))
			return true;
		return false;		
	}
	
	/**
	* Retorna TRUE se o campo existe na instrução e FALSE se não existe.
	*/
	public boolean existeCampo(String c)
	{
		for(int i=0;i<4;i++)
		{
			if(campo[i].toUpperCase().indexOf(c.toUpperCase())!=-1)	
				return true;
		}
		return false;
	}
	
	public void exibe()
	{
		System.out.print("mneu="+mneumonico+" inst="+instrucao);
		System.out.print(" codigo="+cod[0]+cod[1]+cod[2]+cod[3]);
		System.out.println(" campos="+campo[0]+campo[1]+campo[2]+campo[3]);
	}
	
}