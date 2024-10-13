;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;       		BUBBLE V3, MERGE V3 e V4 -> V6 
;; Conteúdo: 
;;	1.Ordena um vetor V3 através do algoritmo de Bubble Sort
;;	2. Espera o processador P4 ordenar o vetor V4
;;	3. Aplica o algoritmo de Merge nos vetores V3 e V4 armazenando o resultado em V6.
;;	4. Envia Notify para o processador P1.
;;	           
;; Autor(a): Aline Vieira de Mello
;; Data de Criação:            03/12/2002
;; Data da Última Atualização: 03/12/2002
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
.CODE
INICIO:
	LDL R1,TAMV3		;;R1 contem o tamanho do vetor V3
	LDH R1,TAMV3		
	SUBI R1,#01H		;;Subtrai 1 de TAMV3
	LDL R5,#01H		;;R5 recebe #0001H
	LDH R5,#00H
LOOP:
	ADD R1,R1,R0		;;Aciona o flag de Zero quando R1 é zero 
	JMPZD #FIMBUBBLE	;;Salta quando o flag de Zero está ativo
	LDL R2,#V3		;;R2 contem o endereco inicial do vetor V3
	LDH R2,#V3		
	LDL R3,#V3		;;R3 contem o endereco inicial do vetor V3 adicionado de 1
	LDH R3,#V3		
	ADDI R3,#01H		
	LDL R4,TAMV3		;;R4 contem o tamanho do vetor V3
	LDH R4,TAMV3		
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
	LDL R14,#09H		;;Carrega 09 para R14 - 09 é o addressC de quem se esta esperando o NOTIFY
	LDH R14,#00H
	ST R14,R0,R15		;;WAIT por core 09 (P4)
MERGEV3V4:			;;Comeca a execução do algoritmo de Merge de V3 e V4
	XOR R1,R0,R0		;;R1 contem o indice i que percorre o vetor V3
	XOR R2,R0,R0		;;R2 contem o indice j que percorre o vetor V4
	XOR R3,R0,R0		;;R3 contem o indice k que percorre o vetor V6
	LDL R4,TAMV3		;;R4 contem o tamanho do vetor V1 subtraido de 1
	LDH R4,TAMV3
	LDL R5,TAMV4		;;R5 contem o tamanho do vetor V2 subtraido de 1
	LDH R5,TAMV4
	LDL R6,#V3		;;R6 contem o endereço inicial do vetor V3
	LDH R6,#V3
	LDL R7,#V4		;;R7 contem o endereço inicial do vetor V4
	LDH R7,#V4
	LDL R8,#V6		;;R8 contem o endereço inicial do vetor V6
	LDH R8,#V6
FOR1:
	SUB R15,R4,R1		;;Se i(R1) < TAMV3(R4) continua 
	JMPZD #WHILEV3		;;Senao vai para WHILEV3
	SUB R15,R5,R2		;;Se j(R2) < TAMV4(R5) continua
	JMPZD #WHILEV3		;;Senao vai para WHILEV3
	LD R9,R1,R6		;;R9 recebe V3[endereco inicial(R6)+i(R1)]
	LD R10,R2,R7		;;R10 recebe V4[endereco inicial(R7)+j(R2)]
	SUB R15,R9,R10		;;Se R9<R10 entao vai para MENORV3
	JMPND #MENORV3
	JMPD #MENORV4		;;Senao vai para MENORV4
RETORNOFOR1:
	ADDI R3,#01H		;;k++
	JMPD #FOR1		;;Retorna a FOR1
WHILEV3:
	SUB R15,R4,R1		;;Se i(R1) < TAMV3(R4) continua
	JMPZD #WHILEV4		;;Senao vai para WHILEV4
	LD R9,R1,R6		;;R9 recebe V3[endereco inicial(R6)+i(R1)]
	ST R9,R3,R8		;;V6[endereco inicial(R8) + k(R3)] recebe R9
	ADDI R1,#01H		;;i++
	ADDI R3,#01H		;;k++
	JMPD #WHILEV3		;;Retorna a WHILEV3
WHILEV4:
	SUB R15,R5,R2		;;Se j(R2) < TAMV2(R5) continua
	JMPZD #FIMMERGEV3V4	;;Senao vai para FIMMERGEV3V4
	LD R10,R2,R7		;;R10 recebe V4[endereco inicial(R7)+j(R2)]
	ST R10,R3,R8		;;V6[endereco inicial(R8) + k(R3)] recebe R10
	ADDI R2,#01H		;;j++
	ADDI R3,#01H		;;k++
	JMPD #WHILEV4		;;Retorna a WHILEV4
FIMMERGEV3V4: 			;;Termina a execução do algoritmo de merge em V3 e V4
	LDL R15,#FDH		;;Carrega FFFD em R15 - FFFD indica NOTIFY
	LDH R15,#FFH
	LDL R14,#01H		;;Carrega 01 em R14 - indica que NOTIFY sera enviado ao core com address 01
	LDH R14,#00H
	ST R14,R0,R15		;;Envia notify a addressC 01(P1)
FIM:
	HALT

TROCA:
	ST R11,R2,R0		;;Grava na posição de memória contida em R2 o conteúdo de R11
	ST R10,R3,R0		;;Grava na posição de memória contida em R3 o conteúdo de R10
	JMPD #VOLTATROCA	;;Salta incondicionalmente
MENORV3:
	ST R9,R3,R8		;;V5[endereco inicial(R8) + k(R3)] recebe R9
	ADDI R1,#01H		;;i++
	JMPD #RETORNOFOR1	;;Vai para RETORNOFOR1
MENORV4:
	ST R10,R3,R8		;;V5[endereco inicial(R8) + k(R3)] recebe R10
	ADDI R2,#01H		;;j++
	JMPD #RETORNOFOR1	;;Vai para RETORNOFOR1
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