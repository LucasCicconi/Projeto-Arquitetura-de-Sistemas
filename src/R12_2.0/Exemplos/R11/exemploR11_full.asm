;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; ARQUITETURA R11 - teste de todas as instruções (não exaustivo)
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
.code
		LDLI	R1,#0ABH	; flags NZCV=0000
		LDHI	R1,#01H		; endereço 01ABh para topo da pilha
		LDSP	R1		; 
		;; teste de algumas instruções
		LDLI	R1,#0F3H	;
		LDHI	R1,#23H		; R1<=23F3h
		LDLI	R8,#52H		;
		LDHI	R8,#0E2H	; R8<=E252, N=1
		LDLI	R15, #00H	; R15<=0, Z=1
		LDHI	R15, #8FH	; R15<=8F00h, Z=0, N=1
		ADD	R3,R1,R8	; R3<=23F3h+E252h=0645h, N=0, C=1
		SUB	R4,R1,R8	; R4<=23F3h-E252h=41A1h, C=0  
		SUB	R5,R1,R1	; R5<=0, Z=1  
		AND	R6,R8,R1	; R6<=E252h and 23F3h=2252h
		OR	R7,R1,R8	; R7<=23F3h or E252h=E3F3h, N=1
		ADDI	R1, #0ABH	; R1<=23F3h+00ABh=249Eh, N=0
		SUBI	R1, #0ABH	; R1<=249Eh-00ABh=23F3h
		STMSK	#0FFH		; gera condição de salto incondicional
		JSRMI	#somavet	; salto p/ subrotina somavet SP<=01AAh
		NOP			;
		NOP			;
		NOP			;
		NOP			;
		NOP			;
		STMSK	#0FFH		; salto incondicional para a subrotina testeshift (n/n'/z/z'/c/c'/z/z')
		JSRMI	#testeshift	;
		HALT			;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; um exemplo útil - soma de dois vetores
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
somavet:	PUSH	R1		; salva regs alterados aqui
		PUSH	R2		;
		PUSH	R3		;
		PUSH	R4		;
		PUSH	R5		;
		PUSH	R6		;
		PUSH	R7		;
		XOR	R5,R5,R5	; R5 <- 0
		LDLI	R1,#end1	;
		LDHI	R1,#end1	;
		LDLI	R2,#end2	;
		LDHI	R2,#end2	;
		LDLI	R3,#end3	;
		LDHI	R3,#end3	; carrega os ponteiro para os três vetores		
		LDLI	R4,#n		;
		LDHI	R4,#n		;
		LD	R4,R4,R5	; carrega o número de elementos		
loop:		LD	R6,R1,R5	;
		LD	R7,R2,R5	;
		ADD	R7,R6,R7	;
		ST	R3,R7		; *end3 <- *end1 + *end2	  
		ADDI	R5,#01H		; incrementa o deslocamento para buscar *end1 e *end2
		ADDI	R3,#01H		; incrementa o endereço do vetor *end3
		SUBI	R4,#01H		;
		STMSK   #10H		; z' (n/n'/z/z'/c/c'/z/z')
		JPMI	#loop		;
		POP	R7		;
		POP	R6		;
		POP	R5		;
		POP	R4		;
		POP	R3		;
		POP	R2		;
		POP	R1		;		
testeJSRGR:	RTS			; final da subrotina e priemira e última de outra
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; teste de shifts e outras instruções
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
testeshift:	PUSH	R1		; empilha os registradores utilizados
		PUSH	R2		; na subrotina
		PUSH	R3
		LDLI	R1,#55H		;
		LDHI	R1,#AAH		; R1<= AA55h = 1010 1010 0101 0101
		NOT	R1,R1		; R1<= 0101 0101 1010 1010 = 55AAh
		SL	R2,R1		; R2<= 1010 1011 0101 0100 = AB54h
		SL	R2,R2		; R2<= 56A8h, bit mais à esq perdido
		SLC	R2,R2		; R2<= AD50h, gera Z=0, entra C=0 à esq
		SR	R3,R1		; R3<= 0010 1010 1101 0101 = 2AD5h
		SR	R3,R3		; R3<= 0001 0101 0110 1010 = 156Ah
		SRC	R3,R3		; R3<= 0000 1010 1011 0101 = 0AB5h
		COMP	R3,R2		; NZCV=0000 indica (0AB5>AD50h) em compl 2 
		RL	R3,R2		; R3<= 0101 1010 1010 0001 = 5AA1h (AD50 rot)
		RR	R3,R2		; R3<= 0101 0110 1010 1000 = 56A8h (AD50 rot)
		RLC	R2,R3		; R2<= 1010 1101 0101 0000 = AD50h, C=0, N=1
		RRC	R2,R3		; R2<= 0010 1011 0101 0100 = 2B54h, C=0, N=0
		LDLI	R12,#2		;
		LDHI	R12,#0		; carrega R12 com 0002
		STMSK	#0C0H		; gera condição de salto incondicional
		JPRGR	R12		; pula as duas próximas instruções
		HALT			; nunca deve ser executada
		HALT			; nunca deve ser executada
		LDLI	R13,#testeJSRGR	;
		LDHI	R13,#testeJSRGR	; carrega R13 com endereço de testeJSRGR
		SUBI	R13,#testePC 	; obtém diferença entre valor do PC na hora
					; da chamada e endereço da subrotina a chamar
		STMSK	#03H		; gera condição de salto incondicional
		JSRGR	R13		; salto incondicional para testeJSRGR
testePC:	LDLI	R11,#testeJSRGR	;
		LDHI	R11,#testeJSRGR	; carrega R11 com endereço de testeJSRGR
		LDHI	R10,#80h	; rótulo aqui dá valor do PC na hora da chamada
					; constante na linha JSRGR R13
		LDLI	R10,#0		; 
		SUBI	R10,#1		;
		STMSK	#1		; máscara de salto se overflow reset
		JPRG	R11		; salto não deve funcionar
		STMSK	#2		; máscara de salto se overflow set
		JSRG	R11		; agora salto funciona
		POP	R3		;
		POP	R2		;
		POP	R1		; recupera o contexto
		RTS			; final da subrotina
.endcode
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
; area de dados
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
.data
n:		DB	#08H
end1:		db	#03H,#18H,#35H,#0ABH,#0CDH,#77H,#53H,#45H
end2:		db	#67H,#34H,#21H,#0BFH,#0FH,#0FDH,#11H,#01H
end3:		db	#00H		; resultado
.enddata