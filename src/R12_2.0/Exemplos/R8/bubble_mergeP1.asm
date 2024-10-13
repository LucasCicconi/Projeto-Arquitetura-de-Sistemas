;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;                      BUBBLE E MERGE P1
;; Conte�do: Ordena um vetor V1 atrav�s do algoritmo de Bubble Sort
;;	     espera o outro processador ordenar o vetor V2 e ap�s
;;           aplica o algoritmo de Merge nos vetores V1 e V2,
;;	     armazenando o resultado em V3.
;; Autor(a): Aline Vieira de Mello
;; Data de Cria��o:            08/11/2002
;; Data da �ltima Atualiza��o: 08/11/2002
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
.CODE
INICIO:
	LDL R1,TAMV1		;;R1 contem o tamanho do vetor V1
	LDH R1,TAMV1		
	SUBI R1,#01H		;;Subtrai 1 de TAMV1
	LDL R5,#01H		;;R5 recebe #0001H
	LDH R5,#00H
LOOP:
	ADD R1,R1,R0		;;Aciona o flag de Zero quando R1 � zero 
	JMPZD #FIMBUBBLE	;;Salta quando o flag de Zero est� ativo
	LDL R2,#V1		;;R2 contem o endereco inicial do vetor V1
	LDH R2,#V1		
	LDL R3,#V1		;;R3 contem o endereco inicial do vetor V1 adicionado de 1
	LDH R3,#V1		
	ADDI R3,#01H		
	LDL R4,TAMV1		;;R4 contem o tamanho do vetor V1
	LDH R4,TAMV1		
	SUB R4,R4,R5		;;Subtrai R4 de R5
LOOPINTERNO:
	ADD R4,R4,R0		;;Aciona o flag de Zero quando R4 � zero
	JMPZD #FIMLOOPINTERNO	;;Salta quando o flag de Zero est� ativo
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
FIMBUBBLE: 			;;Termina a execu��o do algoritmo de bubble
	LDL R15,#FEH		;;Carrega o FFFE para R15 - FFFE indica WAIT
	LDH R15,#0FFH
	LDL R14,#05H		;;Carrega 05 para R14 - 05 � o addressC de quem se esta esperando o NOTIFY
	LDH R14,#00H
	ST R14,R0,R15		;;WAIT por core 05
MERGE:				;;Comeca a execu��o do algoritmo de Merge
	XOR R1,R0,R0		;;R1 contem o indice i que percorre o vetor V1
	XOR R2,R0,R0		;;R2 contem o indice j que percorre o vetor V2
	XOR R3,R0,R0		;;R3 contem o indice k que percorre o vetor V3
	LDL R4,TAMV1		;;R4 contem o tamanho do vetor V1 subtraido de 1
	LDH R4,TAMV1
	LDL R5,TAMV2		;;R5 contem o tamanho do vetor V2 subtraido de 1
	LDH R5,TAMV2
	LDL R6,#V1		;;R6 contem o endere�o inicial do vetor V1
	LDH R6,#V1
	LDL R7,#V2		;;R7 contem o endere�o inicial do vetor V2
	LDH R7,#V2
	LDL R8,#V3		;;R8 contem o endere�o inicial do vetor V3
	LDH R8,#V3
FOR:
	SUB R15,R4,R1		;;Se i(R1) < TAMV1(R4) continua 
	JMPZD #WHILEV1		;;Senao vai para WHILEV1
	SUB R15,R5,R2		;;Se j(R2) < TAMV2(R5) continua
	JMPZD #WHILEV1		;;Senao vai para WHILEV1
	LD R9,R1,R6		;;R9 recebe V1[endereco inicial(R6)+i(R1)]
	LD R10,R2,R7		;;R10 recebe V2[endereco inicial(R7)+j(R2)]
	SUB R15,R9,R10		;;Se R9<R10 entao vai para MENORV1
	JMPND #MENORV1
	JMPD #MENORV2		;;Senao vai para MENORV2
RETORNOFOR:
	ADDI R3,#01H		;;k++
	JMPD #FOR		;;Retorna a FOR
WHILEV1:
	SUB R15,R4,R1		;;Se i(R1) < TAMV1(R4) continua
	JMPZD #WHILEV2		;;Senao vai para WHILEV2
	LD R9,R1,R6		;;R9 recebe V1[endereco inicial(R6)+i(R1)]
	ST R9,R3,R8		;;V3[endereco inicial(R8) + k(R3)] recebe R9
	ADDI R1,#01H		;;i++
	ADDI R3,#01H		;;k++
	JMPD #WHILEV1		;;Retorna a WHILEV1
WHILEV2:
	SUB R15,R5,R2		;;Se j(R2) < TAMV2(R5) continua
	JMPZD #FIM		;;Senao vai para FIM
	LD R10,R2,R7		;;R10 recebe V2[endereco inicial(R7)+j(R2)]
	ST R10,R3,R8		;;V3[endereco inicial(R8) + k(R3)] recebe R10
	ADDI R2,#01H		;;j++
	ADDI R3,#01H		;;k++
	JMPD #WHILEV2		;;Retorna a WHILEV2
FIM:
	HALT

TROCA:
	ST R11,R2,R0		;;Grava na posi��o de mem�ria contida em R2 o conte�do de R11
	ST R10,R3,R0		;;Grava na posi��o de mem�ria contida em R3 o conte�do de R10
	JMPD #VOLTATROCA		;;Salta incondicionalmente

MENORV1:
	ST R9,R3,R8		;;V3[endereco inicial(R8) + k(R3)] recebe R9
	ADDI R1,#01H		;;i++
	JMPD #RETORNOFOR	;;Vai para RETORNOFOR
MENORV2:
	ST R10,R3,R8		;;V3[endereco inicial(R8) + k(R3)] recebe R10
	ADDI R2,#01H		;;j++
	JMPD #RETORNOFOR	;;Vai para RETORNOFOR
.ENDCODE
.DATA
TAMV1:	DB	#FFH
TAMV2:	DB	#FFH
.ORG #0400H
V1:	DB	#00H
.ORG #0500H
V2:	DB	#00H
.ORG #0600H
V3:	DB	#00H
.ENDDATA