;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Teste de todas as instruções do Processador R8 ;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
.CODE
	LDL	R1,#A			;;Carrega a parte baixa do endereco de A
	LDH	R1,#A			;;Carrega a parte alta do endereço de A
	LDL	R2,#W			;;Carrega a parte baixa do endereço de B
	LDH	R2,#W			;;Carrega a parte alta do endereço de B
	LDL	R3,#C			;;Carrega a parte baixa do endereço de C
	LDH	R3,#C			;;Carrega a parte alta do endereço de C
	LDL	R4,#D			;;Carrega a parte baixa do endereço de D
	LDH	R4,#D			;;Carrega a parte alta do endereço de D

	LD	R5,R0,R1		;;Carrega o conteúdo de A	
	LD	R6,R0,R2		;;Carrega o conteúdo de B
	LD	R7,R0,R3		;;Carrega o conteúdo de C
	LD	R8,R0,R4		;;Carrega o conteúdo de D

	ADD	R0,R0,R0		;;Adiciona zeros ->> Flag de Zero
	SUB	R9,R5,R6		;;Subtrai A de B ->> Flag de Negativo
	AND	R10,R5,R8		;;AND de A com D ->> Igual a A
	OR	R11,R5,R6		;;OR de A com B  ->> Igual a C
	XOR	R12,R5,R7		;;XOR de A com C ->> Igual a B
	ADD	R13,R0,R6		;;Carrega o conteúdo de B
	ADDI	R13,#FFh		;;Adiciona o valor imediato FFh ao conteúdo de B	
	SUBI	R13,#01H		;;Subtrai 1 do conteúdo de B ->> Flag de Zero

	ST	R9,R0,R1		;;Armazena Em A[R0+R1] <- R9[A-B] (D)
	ST	R10,R0,R2		;;Armazena Em B[R0+R2] <- R10[A AND D] (A)
	ST	R11,R0,R3		;;Armazena Em C[R0+R3] <- R11[A OR B] (C)
	ST	R12,R0,R4		;;Armazena Em B[R0+R4] <- R12[A XOR C] (B)
	
	NOT	R15,R5			;;NOT R5
	NOP				;;Pára por 3 Ciclos

	LDL	R9,#Ffh			;;Carrega FFh para a parte baixa do registrador R9
	LDH	R9,#00H			;;Carrega 00H para a parte baixa do registrador R9
	LDSP	R9			;;Carrega para o SP R9 [00FF]
	PUSH	R5			;;Coloca R5 na pilha 
	POP	R10			;;Carrega o topo da pilha [R5]

	LDL	R11,#Shifts		;;Carrega a parte baixa do endereço da subrotina Shifts
	LDH	R11,#Shifts		;;Carrega a parte alta do endereço da subrotina Shifts
	JSR	R11			;;Salta para a subrotina shifts

	NOP				;;Nenhuma ação

	JSRD	#shifts			;;Salta para a subrotina shifts

	NOP				;;Nenhuma ação

	LDL 	R15,#testajmps		;;Carrega a parte baixa do endereço da subrotina testajmps
	LDH 	R15,#testajmps		;;Carrega a parte alta do endereço da subrotina testajmps
	JSR 	R15 			;;Salta para a subrotina testajmps

	LDL 	R15,#testajmpds		;;Carrega a parte baixa do endereço da subrotina testajmpds
	LDH 	R15,#testajmpds		;;Carrega a parte alta do endereço da subrotina testajmpds
	JSR 	R15 			;;Salta para a subrotina testajmpds
	
;	LDL 	R15,#TeSTaJMPrs		;;Carrega a parte baixa do endereço da subrotina testajmpds
;	LDH 	R15,#TeSTaJMPrs		;;Carrega a parte alta do endereço da subrotina testajmpds
;	JMP 	R15 			;;Salta para a subrotina testajmprs

fim:
	HALT				;;Termina a execução
	
shifts:
	SL0 	R14,R8			;;Shift left concatenando 0 [FFFE] ->> Flag Negativo
	SL1 	R14,R14			;;Shift left concatenando 1 [FFFD] ->> Flag Negativo
	SR0 	R14,R14			;;Shift right concatenando 0 [7FFE]
	SR1 	R14,R14			;;Shift right concatenando 1 [BFFF] ->> Flag Negativo
	RTS				;;Retorno da subrotina

testajmpds:
testajmpzd:
;	ADD	R0,R0,R0		;; Aciona o flag Zero
	JMPZD 	#ff9
;	ADD	R8,R0,R8		;; Aciona o flag Negativo
	JMPND 	#ff10
;	ADD	R8,R8,R8		;; Aciona o flag Carry
	JMPCD 	#ff11
;	ADD	R8,R8,R8		;; Aciona o flag Overflow
	JMPVD 	#ff12
	JMPD	#fimtestajmpd
ff9:
	ADDI	R9,#FFh			;;Coloca 00FFh no registrador
	JMPD	#fimtestajmpd		;;Salta para o testajmpnd
ff10:
	ADDI	R10,#FFh		;;Coloca 00FFh no registrador
	JMPD	#fimtestajmpd		;;Salta para o testajmpnd
ff11:
	ADDI	R11,#FFh		;;Coloca 00FFh no registrador
	JMPD	#fimtestajmpd		;;Salta para o testajmpnd
ff12:
	ADDI	R12,#FFh		;;Coloca 00FFh no registrador
	JMPD	#fimtestajmpd		;;Salta para o testajmpnd
	
fimtestajmpd:
	RTS


testajmps:	
testajmpz:
	LDL	R12,#zerar9		;;Carrega a parte baixa do endereço do rótulo Zerar9
	LDH	R12,#zerar9		;;Carrega a parte alta do endereço do rótulo Zerar9
	ADD	R0,R0,R0		;;Aciona o flag Zero
	JMPZ	R12
testajmpn:
	LDL	R12,#zerar10		;;Carrega a parte baixa do endereço do rótulo Zerar10
	LDH	R12,#zerar10		;;Carrega a parte alta do endereço do rótulo Zerar10
	ADD	R8,R0,R8		;;Aciona o flag Negativo
	JMPN	R12
testajmpc:
	LDL	R12,#zerar11		;;Carrega a parte baixa do endereço do rótulo Zerar11
	LDH	R12,#zerar11		;;Carrega a parte alta do endereço do rótulo Zerar11
	ADD	R8,R8,R8		;;Aciona o flag Carry
	JMPC	R12
testajmpv:
	LDL	R12,#zerar12		;;Carrega a parte baixa do endereço do rótulo Zerar12
	LDH	R12,#zerar12		;;Carrega a parte alta do endereço do rótulo Zerar12
	ADD	R8,R8,R8		;;Aciona o flag Overflow
	JMPV	R12
fimtestajmps:
	RTS

testajmprs:

testajmpzr:
	LDL	R12,#Ech		;;Carrega a parte baixa do endereço do rótulo zerar9
	LDH	R12,#Ffh		;;Carrega a parte alta do endereço do rótulo zerar9
	ADD	R0,R0,R0		;;Aciona o flag Zero
	JMPZR	R12
fimtestajmprs:
	LDL	R12,#Fim		;;Carrega a parte baixa do endereço do rótulo zerar12
	LDH	R12,#Fim		;;Carrega a parte alta do endereço do rótulo zerar12
	JMP	R12

zerar9:
	ADD	R9,R0,R0		;;Zera o registrador
	LDL	R15,#testajmpn		;;Carrega a parte baixa do endereço do rótulo testajmpn
	LDH	R15,#testajmpn		;;Carrega a parte alta do endereço do rótulo testajmpn
	JMP	R15			;;Salta para o R15 [testajmpn]
zerar10:
	ADD	R10,R0,R0		;;Zera o registrador
	LDL	R15,#testajmpc		;;Carrega a parte baixa do endereço do rótulo testajmpc
	LDH	R15,#testajmpc		;;Carrega a parte alta do endereço do rótulo testajmpc
	JMP	R15			;;Salta para o R15 [testajmpc]
zerar11:
	ADD	R11,R0,R0		;;Zera o registrador
	LDL	R15,#testajmpv		;;Carrega a parte baixa do endereço do rótulo testajmpv
	LDH	R15,#testajmpv		;;Carrega a parte alta do endereço do rótulo testajmpv
	JMP	R15			;;Salta para o R15 [testajmpv]
zerar12:
	ADD	R12,R0,R0		;;Zera o registrador
	LDL	R15,#fimtestajmps	;;Carrega a parte baixa do endereço do rótulo fimtestajmps
	LDH	R15,#fimtestajmps	;;Carrega a parte alta do endereço do rótulo fimtestajmps
	JMP	R15			;;Salta para o R15 [#fimtestajmp]

.ENDCODE

.DATA
A: 	DB 	#0001H
W: 	DB 	#0002H
C: 	DB 	#0003H
D: 	DB 	#FFFFh
.ENDDATA