;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;       BUBBLE V1, MERGE V1 e V2 -> V5 e MERGE V5 e V6 -> V7
;; Conteúdo: 
;;	1.Ordena um vetor V1 através do algoritmo de Bubble Sort
;;	2. Espera o processador P2 ordenar o vetor V2
;;	3. Aplica o algoritmo de Merge nos vetores V1 e V2 armazenando o resultado em V5.
;;	4. Espera o processador P3 aplicar o algoritmo de Merge nos vetor V3 e V4 e
;;	   armazenar o resultado em V6.
;;	5. Aplica o algoritmo de Merge nos vetores V5 e V6 armazenando o resultado em V7.
;;	           
;; Autor(a): Aline Vieira de Mello
;; Data de Criação:            08/11/2002
;; Data da Última Atualização: 08/11/2002
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
.CODE
INICIO:
	LDL R1,TAMV1		;;R1 contem o tamanho do vetor V1
	LDH R1,TAMV1		
	SUBI R1,#01H		;;Subtrai 1 de TAMV1
	LDL R5,#01H		;;R5 recebe #0001H
	LDH R5,#00H
LOOP:
	ADD R1,R1,R0		;;Aciona o flag de Zero quando R1 é zero 
	JMPZD #FIMBUBBLE	;;Salta quando o flag de Zero está ativo
	LDL R2,#V1		;;R2 contem o endereco inicial do vetor V1
	LDH R2,#V1		
	LDL R3,#V1		;;R3 contem o endereco inicial do vetor V1 adicionado de 1
	LDH R3,#V1		
	ADDI R3,#01H		
	LDL R4,TAMV1		;;R4 contem o tamanho do vetor V1
	LDH R4,TAMV1		
	SUB R4,R4,R5		;;Subtrai R4 de R5
LOOPINTERNO:
	ADD R4,R4,R0		;;Aciona o flag de Zero quando R4 é zero
	JMPZD #FIMLOOPINTERNO	;;Salta quando o flag de Zero está ativo
	LD R10,R2,R0		;;R10 recebe V1[R2]
	LD R11,R3,R0		;;R11 recebe V2[R3]
	SUB R12,R11,R10		;;Se R10 > R11 entao TROCA
	JMPND #TROCA		
VOLTATROCA:
	ADDI R2,#01H		;;Adiciona 1 em R2
	ADDI R3,#01H		;;Adiciona 1 em R3
	SUBI R4,#01H		;;Subtrai 1 de R4
	JMPD #LOOPINTERNO	;;Salta incondicionalmente 	
FIMLOOPINTERNO:
	SUBI R1,#01H		;;Subtrai 1 de R1
	ADDI R5,#01H		;;Adiciona 1 em R5
	JMPD #LOOP		;;Salta incondicionalmente
FIMBUBBLE: 			;;Termina a execução do algoritmo de bubble
	LDL R15,#FEH		;;Carrega o FFFE para R15 - FFFE indica WAIT
	LDH R15,#0FFH
	LDL R14,#05H		;;Carrega 05 para R14 - 05 é o addressC de quem se esta esperando o NOTIFY
	LDH R14,#00H
	ST R14,R0,R15		;;WAIT por core 05 (P2)
MERGEV1V2:			;;Comeca a execução do algoritmo de Merge de V1 e V2
	XOR R1,R0,R0		;;R1 contem o indice i que percorre o vetor V1
	XOR R2,R0,R0		;;R2 contem o indice j que percorre o vetor V2
	XOR R3,R0,R0		;;R3 contem o indice k que percorre o vetor V3
	LDL R4,TAMV1		;;R4 contem o tamanho do vetor V1 subtraido de 1
	LDH R4,TAMV1
	LDL R5,TAMV2		;;R5 contem o tamanho do vetor V2 subtraido de 1
	LDH R5,TAMV2
	LDL R6,#V1		;;R6 contem o endereço inicial do vetor V1
	LDH R6,#V1
	LDL R7,#V2		;;R7 contem o endereço inicial do vetor V2
	LDH R7,#V2
	LDL R8,#V5		;;R8 contem o endereço inicial do vetor V5
	LDH R8,#V5
FOR1:
	SUB R15,R4,R1		;;Se i(R1) < TAMV1(R4) continua 
	JMPZD #WHILEV1		;;Senao vai para WHILEV1
	SUB R15,R5,R2		;;Se j(R2) < TAMV2(R5) continua
	JMPZD #WHILEV1		;;Senao vai para WHILEV1
	LD R9,R1,R6		;;R9 recebe V1[endereco inicial(R6)+i(R1)]
	LD R10,R2,R7		;;R10 recebe V2[endereco inicial(R7)+j(R2)]
	SUB R15,R9,R10		;;Se R9<R10 entao vai para MENORV1
	JMPND #MENORV1
	JMPD #MENORV2		;;Senao vai para MENORV2
RETORNOFOR1:
	ADDI R3,#01H		;;k++
	JMPD #FOR1		;;Retorna a FOR1
WHILEV1:
	SUB R15,R4,R1		;;Se i(R1) < TAMV1(R4) continua
	JMPZD #WHILEV2		;;Senao vai para WHILEV2
	LD R9,R1,R6		;;R9 recebe V1[endereco inicial(R6)+i(R1)]
	ST R9,R3,R8		;;V5[endereco inicial(R8) + k(R3)] recebe R9
	ADDI R1,#01H		;;i++
	ADDI R3,#01H		;;k++
	JMPD #WHILEV1		;;Retorna a WHILEV1
WHILEV2:
	SUB R15,R5,R2		;;Se j(R2) < TAMV2(R5) continua
	JMPZD #FIMMERGEV1V2	;;Senao vai para FIMMERGEV1V2
	LD R10,R2,R7		;;R10 recebe V2[endereco inicial(R7)+j(R2)]
	ST R10,R3,R8		;;V5[endereco inicial(R8) + k(R3)] recebe R10
	ADDI R2,#01H		;;j++
	ADDI R3,#01H		;;k++
	JMPD #WHILEV2		;;Retorna a WHILEV2
FIMMERGEV1V2: 			;;Termina a execução do algoritmo de merge em V1 e V2
	LDL R15,#FEH		;;Carrega o FFFE para R15 - FFFE indica WAIT
	LDH R15,#0FFH
	LDL R14,#07H		;;Carrega 07 para R14 - 07 é o addressC de quem se esta esperando o NOTIFY
	LDH R14,#00H
	ST R14,R0,R15		;;WAIT por core 07 (P3)
MERGEV5V6:			;;Comeca a execução do algoritmo de Merge de V5 e V6
	XOR R1,R0,R0		;;R1 contem o indice i que percorre o vetor V5
	XOR R2,R0,R0		;;R2 contem o indice j que percorre o vetor V6
	XOR R3,R0,R0		;;R3 contem o indice k que percorre o vetor V7
	LDL R4,TAMV5		;;R4 contem o tamanho do vetor V5 subtraido de 1
	LDH R4,TAMV5
	LDL R5,TAMV6		;;R5 contem o tamanho do vetor V6 subtraido de 1
	LDH R5,TAMV6
	LDL R6,#V5		;;R6 contem o endereço inicial do vetor V5
	LDH R6,#V5
	LDL R7,#V6		;;R7 contem o endereço inicial do vetor V6
	LDH R7,#V6
	LDL R8,#V7		;;R8 contem o endereço inicial do vetor V7
	LDH R8,#V7
FOR2:
	SUB R15,R4,R1		;;Se i(R1) < TAMV5(R4) continua 
	JMPZD #WHILEV5		;;Senao vai para WHILEV5
	SUB R15,R5,R2		;;Se j(R2) < TAMV6(R5) continua
	JMPZD #WHILEV5		;;Senao vai para WHILEV5
	LD R9,R1,R6		;;R9 recebe V5[endereco inicial(R6)+i(R1)]
	LD R10,R2,R7		;;R10 recebe V6[endereco inicial(R7)+j(R2)]
	SUB R15,R9,R10		;;Se R9<R10 entao vai para MENORV5
	JMPND #MENORV5
	JMPD #MENORV6		;;Senao vai para MENORV6
RETORNOFOR2:
	ADDI R3,#01H		;;k++
	JMPD #FOR2		;;Retorna a FOR2
WHILEV5:
	SUB R15,R4,R1		;;Se i(R1) < TAMV5(R4) continua
	JMPZD #WHILEV6		;;Senao vai para WHILEV6
	LD R9,R1,R6		;;R9 recebe V5[endereco inicial(R6)+i(R1)]
	ST R9,R3,R8		;;V7[endereco inicial(R8) + k(R3)] recebe R9
	ADDI R1,#01H		;;i++
	ADDI R3,#01H		;;k++
	JMPD #WHILEV5		;;Retorna a WHILEV5
WHILEV6:
	SUB R15,R5,R2		;;Se j(R2) < TAMV6(R5) continua
	JMPZD #FIM		;;Senao vai para FIM
	LD R10,R2,R7		;;R10 recebe V6[endereco inicial(R7)+j(R2)]
	ST R10,R3,R8		;;V7[endereco inicial(R8) + k(R3)] recebe R10
	ADDI R2,#01H		;;j++
	ADDI R3,#01H		;;k++
	JMPD #WHILEV6		;;Retorna a WHILEV6
FIM:
	HALT

TROCA:
	ST R11,R2,R0		;;Grava na posição de memória contida em R2 o conteúdo de R11
	ST R10,R3,R0		;;Grava na posição de memória contida em R3 o conteúdo de R10
	JMPD #VOLTATROCA	;;Salta incondicionalmente
MENORV1:
	ST R9,R3,R8		;;V5[endereco inicial(R8) + k(R3)] recebe R9
	ADDI R1,#01H		;;i++
	JMPD #RETORNOFOR1	;;Vai para RETORNOFOR1
MENORV2:
	ST R10,R3,R8		;;V5[endereco inicial(R8) + k(R3)] recebe R10
	ADDI R2,#01H		;;j++
	JMPD #RETORNOFOR1	;;Vai para RETORNOFOR1
MENORV5:
	ST R9,R3,R8		;;V7[endereco inicial(R8) + k(R3)] recebe R9
	ADDI R1,#01H		;;i++
	JMPD #RETORNOFOR2	;;Vai para RETORNOFOR2
MENORV6:
	ST R10,R3,R8		;;V7[endereco inicial(R8) + k(R3)] recebe R10
	ADDI R2,#01H		;;j++
	JMPD #RETORNOFOR2	;;Vai para RETORNOFOR2


.ENDCODE
.DATA
TAMV1:	DB	#7FH
TAMV2:	DB	#7FH
TAMV3:	DB	#7FH
TAMV4:	DB	#7FH
TAMV5:	DB	#FFH
TAMV6:	DB	#FFH
.ORG #0400H
V1:	DB	#00H
.ORG #0480H
V2:	DB	#00H
.ORG #0500H
V3:	DB	#00H
.ORG #0580H
V4:	DB	#00H
.ORG #0600H
V5:	DB	#00H
.ORG #0700H
V6:	DB	#00H
.ORG #0800H
V7:	DB	#00H
.ENDDATA