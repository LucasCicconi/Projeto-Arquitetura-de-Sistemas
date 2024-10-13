;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;                      MERGE
;; Conteúdo: Aplicação do algoritmo de MERGE nos vetores V1 e V2, 
;;           armazenando o resultado no vetor V3.
;; Autor(a): Aline Vieira de Mello
;; Data de Criação:            07/11/2002
;; Data da Última Atualização: 07/11/2002
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
.CODE
INICIO:
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
	LDL R8,#V3		;;R8 contem o endereço inicial do vetor V3
	LDH R8,#V3
FOR:
	SUB R15,R4,R1		;;se i(R1) < TAMV1(R4) continua 
	JMPZD #WHILEV1		;;senao vai para WHILEV1
	SUB R15,R5,R2		;;se j(R2) < TAMV2(R5) continua
	JMPZD #WHILEV1		;;senao vai para WHILEV1
	LD R9,R1,R6		;;R9 recebe V1[endereco inicial(R6)+i(R1)]
	LD R10,R2,R7		;;R10 recebe V2[endereco inicial(R7)+j(R2)]
	SUB R15,R9,R10		;;se R9<R10 entao vai para MENORV1
	JMPND #MENORV1
	JMPD #MENORV2		;;senao vai para MENORV2
RETORNOFOR:
	ADDI R3,#01H		;;k++
	JMPD #FOR		;;retorn a FOR
WHILEV1:
	SUB R15,R4,R1		;;se i(R1) < TAMV1(R4) continua
	JMPZD #WHILEV2		;;senao vai para WHILEV2
	LD R9,R1,R6		;;R9 recebe V1[endereco inicial(R6)+i(R1)]
	ST R9,R3,R8		;;V3[endereco inicial(R8) + k(R3)] recebe R9
	ADDI R1,#01H		;;i++
	ADDI R3,#01H		;;k++
	JMPD #WHILEV1		;;retorna a WHILEV1
WHILEV2:
	SUB R15,R5,R2		;;se j(R2) < TAMV2(R5) continua
	JMPZD #FIM		;;senao vai para FIM
	LD R10,R2,R7		;;R10 recebe V2[endereco inicial(R7)+j(R2)]
	ST R10,R3,R8		;;V3[endereco inicial(R8) + k(R3)] recebe R10
	ADDI R2,#01H		;;j++
	ADDI R3,#01H		;;k++
	JMPD #WHILEV2		;;retorna a WHILEV2
FIM:
	HALT
MENORV1:
	ST R9,R3,R8		;;V3[endereco inicial(R8) + k(R3)] recebe R9
	ADDI R1,#01H		;;i++
	JMPD #RETORNOFOR	;;vai para RETORNOFOR
MENORV2:
	ST R10,R3,R8		;;V3[endereco inicial(R8) + k(R3)] recebe R10
	ADDI R2,#01H		;;j++
	JMPD #RETORNOFOR	;;vai para RETORNOFOR
.ENDCODE
.DATA
TAMV1:	DB	#07H
TAMV2:	DB	#07H
V1:	DB	#01H,#02H,#04H,#05H,#08H,#0DH,#11H
V2:	DB	#00H,#03H,#04H,#07H,#0CH,#0FH,#12H
V3:	DB	#00H
.ENDDATA	