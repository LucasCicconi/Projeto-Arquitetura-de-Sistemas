;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; ARQUITETURA R11 - BUSCA UMA PALAVRA DENTRO DO ARQUIVO
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

.code
;;CARREGA Ia POS. DE ARQUIVO EM R1, E SOMA COM TAMANHO DO VETOR, PARA SOMAR E CARREGAR NA PILHA O INVERSO DO VETOR

		LDLI	R1, #ARQUIVO	
		LDHI	R1, #00H
		ADDI	R1, #0FH	;;SETA POS. FINAL DO ARQUIVO EM R1
					
		LDLI    R2, #0FFH	;;SETA INICIO DA PILHA PARA A ULTIMA POSICAO DE DADOS
                LDHI    R2, #0FFH
                LDSP    R2
					
		LDLI	R2, #ARQUIVO	;;SETA POS. INICIAL DO ARQUIVO EM R2
		LDHI	R2, #00H

		LDLI 	R3, #01H	;;SETA R3 COM 1 PARA SUBTRAIR DE R1 MAIS TARDE
		LDHI	R3, #00H

		LDLI 	R4, #00H	;;SETA R4 COM 0 PARA BUSCAR O DADO
		LDHI	R4, #00H
		
CARREGA:	STMSK	#20H		;SETA MASCARA PARA ZERO ATIVO
		COMP	R1, R2		;SE POS. FINAL MENOS POS. INICIAL IGUAL A ZERO
		JPMI	#CONTINUACAO 	
					;;POE NA PILHA
		LD	R5, R1, R4
		PUSH	R5
		SUB	R1, R1, R3
		STMSK	#0FFH
		JPMI	#CARREGA

CONTINUACAO:	HALT
.endcode

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
; area de dados
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

.data

AUX:	    db #00H

PALAVRA:    db #50H	;0  -> P
            db #45H	;1  -> E
	    db #54H	;2  -> T
	    db #49H	;3  -> I
	    db #4EH	;4  -> N
            db #46H	;5  -> F
	    db #00H	;6  -> 0 (MARCA FIM DA STRING)

I:	    db #10H 	;INDICE DO ARQUIVO

ARQUIVO:    db #42H	;0  -> B
            db #46H	;1  -> F
	    db #47H	;2  -> G
	    db #52H	;3  -> R
	    db #50H	;4  -> P
            db #45H	;5  -> E
	    db #54H	;6  -> T
	    db #49H	;7  -> I
	    db #4EH	;8  -> N
            db #46H	;9  -> F
	    db #4FH	;10 -> O
	    db #52H	;11 -> R
	    db #47H	;12 -> G
	    db #46H	;13 -> F
            db #49H	;14 -> I
	    db #4DH	;15 -> M
.enddata