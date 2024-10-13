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
	HALT

TROCA:
	ST R11,R2,R0		;;Grava na posição de memória contida em R2 o conteúdo de R11
	ST R10,R3,R0		;;Grava na posição de memória contida em R3 o conteúdo de R10
	JMPD #VOLTATROCA	;;Salta incondicionalmente
.ENDCODE

.DATA
TAMV3:	DB	#7FH
.ORG #0500H
V3:	DB	#00H
.ENDDATA