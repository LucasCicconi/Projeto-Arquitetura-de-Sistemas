;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;                      BUBBLE P1
;; Conte�do: Ordena um vetor
;; Autor(a): Aline Vieira de Mello
;; Data de Cria��o:            08/11/2002
;; Data da �ltima Atualiza��o: 08/11/2002
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
.CODE
INICIO:
	LDL R1,TAMV1		;;R1 contem o tamanho do vetor V1
	LDH R1,TAMV1		
	INIT #01H		;;TESTE BRIAO
	SUBI R1,#01H		;;Subtrai 1 de TAMV1
	RDR R2,R5		;;TESTE BRIAO
	LDL R5,#01H		;;R5 recebe #0001H
	WRR R1,R2		;;TESTE BRIAO
	LDH R5,#00H
	SELR #12H		;;TESTE BRIAO
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
	HALT

TROCA:
	ST R11,R2,R0		;;Grava na posi��o de mem�ria contida em R2 o conte�do de R11
	ST R10,R3,R0		;;Grava na posi��o de mem�ria contida em R3 o conte�do de R10
	JMPD #VOLTATROCA	;;Salta incondicionalmente
.ENDCODE
.DATA
TAMV1:	DB	#00FFH
.ORG #0800H
V1:	DB	#00H
.ENDDATA