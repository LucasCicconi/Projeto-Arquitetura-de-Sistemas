# Projeto Final Arquiterura de Sistemas Computacionais

![GitHub Repository Size](https://img.shields.io/github/repo-size/LucasCicconi/Projeto-Arquitetura-de-Sistemas)

## Objetivo
Adicionar uma unidade externa (co-processador) de processamento matricial (MPU) ao processador R8.

## Processador R8
[Processador simples e didático desenvolvido na PUC-RS](https://www.inf.pucrs.br/~calazans/research/Projects/R8/R8_Processor_Core.html )

### Recursos disponíveis:
* Documentação em PDF
* Descrição em VHDL
* Testbench
* Simulador (multiplataforma em Java)
* Inclui o Assembler
* Inclui códigos de exemplo
  
### Execução do projeto
1. Desenvolvimento da MPU em VHDL:         29/outubro
2. Rotina de interrupção entre R8 e MPU:   12/novembro
3. Apresentação final com software:        26/novembro
   
### Etapa 1: Desenvolvimento da MPU
*  MPU atuará como um periférico mapeado em memória (MMIO)
  
```
entity MPU is
port(
ce_n, we_n, oe_n: in std_logic;
intr: out std_logic;
address: in reg16;
data: inout reg16
);
end MPU;
```

![image](https://github.com/user-attachments/assets/a158be8a-86b9-4249-b119-63394a6e36ba)
* Mapa de memória da MPU
  
![image](https://github.com/user-attachments/assets/ff7a8397-4d7d-4adc-824f-54135fe31126)

* Modelo básico de programação:
  * A CPU verifica o estado da MPU lendo a região de comando
  * A CPU escreve as matrizes A, B e C conforme necessário
  * A CPU escreve um comando na região de comando
  * A MPU executa o comando
  * A MPU levanta a linha de interrupção quando o comando estiver concluído
  * A CPU lê o estado e as matrizes A, B e C conforme necessário

* Comandos que deverão ser implementados:
  * add : Executa C[:] = A[:] + B[:]
  * sub : Executa C[:] = A[:] - B[:]
  * mul: Executa C[:] = A[:] x B[:] (produto matricial)
  * mac: Executa C[:] = C[:] + A[:] x B[:] (produto matricial)
  * fill M, v: Definir matriz M[:] inteira com o valor v
  * identity M, v: Definir matriz identidade M[:] com o valor v

* Todas as matrizes são de elementos inteiros de 16 bits em complemento de 2
* Definir a comunicação, formatos dos comandos e verificação de estado

### Etapa 2: Rotina de interrupção

* Modificar o R8 e criar a nova instrução:
	* INTR Rs1
		* Se Rs1 != 0, ativa interrupções e define MEM[Rs1] como alvo de interrupções
		* Se Rs1 == 0, desativa interrupções
	* A nova instrução terá que ser “compilada” manualmente
* Criar nova entrada intr_in (std_logic) no processador:
	* Se intr_in == 1 e interrupções estiverem ativas, pula imediatamente para alvo de interrupções definido pela instrução INTR
	* Lembrem-se de salvar o endereço de retorno para poder voltar com RTS
* Testar funcionamento com testbench e código simples com rotina de interrupção
	* Lembrem-se de salvar o estado no tratamento de interrupção

### Etapa 3: Apresentação final

* Juntar o R8 adaptado para interrupções com a MPU
	* Conectar a linha de interrupção
	* Definir endereços de memória que estarão na MPU
* Escrever um programa em Assembly que execute as duas operações:
	* De forma concorrente (utilizando as interrupções)
	* 1. Ordenação de um vetor de 64 elementos inteiros e aleatórios
		* Não utilizar algoritmo recursivo!
	* 2. Operação matricial na MPU:
		* Z[] = k1*X[] x Y[] + k2*Z[]
			* k1 e k2 são inteiros, X, Y e Z são matrizes, todos aleatórios

## Autores

[Lucas Cicconi](https://github.com/LucasCicconi)

[Murilo Croce](https://github.com/KingMars01)
