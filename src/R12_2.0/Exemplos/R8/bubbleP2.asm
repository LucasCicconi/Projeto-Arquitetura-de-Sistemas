;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;                      BUBBLE P2
;; Conte�do:
;;	1.Ordena o vetor V2
;;	2.Envia NOTIFY para P1 - addressC 01
;; Autor(a): Aline Vieira de Mello
;; Data de Cria��o:            08/11/2002
;; Data da �ltima Atualiza��o: 08/11/2002
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
.CODE
INICIO:
	LDL R1,TAMV2		;;R1 contem o tamanho do vetor V1
	LDH R1,TAMV2		
	SUBI R1,#01H		;;Subtrai 1 de TAMV2
	LDL R5,#01H		;;R5 recebe #0001H
	LDH R5,#00H
LOOP:
	ADD R1,R1,R0		;;Aciona o flag de Zero quando R1 � zero 
	JMPZD #FIMBUBBLE	;;Salta quando o flag de Zero est� ativo
	LDL R2,#V2		;;R2 contem o endereco inicial do vetor V2
	LDH R2,#V2		
	LDL R3,#V2		;;R3 contem o endereco inicial do vetor V2 adicionado de 1
	LDH R3,#V2		
	ADDI R3,#01H		
	LDL R4,TAMV2		;;R4 contem o tamanho do vetor V2
	LDH R4,TAMV2		
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
	LDL R15,#FDH		;;Carrega FFFD em R15 - FFFD indica NOTIFY
	LDH R15,#FFH
	LDL R14,#01H		;;Carrega 01 em R14 - indica que NOTIFY sera enviado ao core com address 01
	LDH R14,#00H
	ST R14,R0,R15
FIM:
	HALT

TROCA:
	ST R11,R2,R0		;;Grava na posi��o de mem�ria contida em R2 o conte�do de R11
	ST R10,R3,R0		;;Grava na posi��o de mem�ria contida em R3 o conte�do de R10
	JMPD #VOLTATROCA		;;Salta incondicionalmente
.ENDCODE
.DATA
TAMV2:	DB	#7FH
.ORG #0480H
V2:	DB	#00H
.ENDDATA