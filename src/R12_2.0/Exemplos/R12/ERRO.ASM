.code
		ldli  r6,#aux		; Carrega em r6 o endere�o de aux
		ldhi  r6,#aux		;
		xor   r0,r0,r0		; r0 <= zero (constante no programa)
		ldli  r1,n1		; carrega em r1 a 1� posi��o do vetor n1
		ldhi  r1,n1		;
		push  r1		; coloca o conteudo de n1 na pilha
main_loop:	pop   r1		; r1 recebe o valor da pilha 
		push  r1		; e coloca novamente na pilha
		ldli  r2,cmp		; r2 recebe o valor inicial para compara��es
		ldhi  r2,cmp		;
		stmsk #3		; seta a m�scara para salto incondicional
		jpmi  #compara		; vai para subrotina compara
		stmsk #3		; seta a m�scara para salto incondicional
		jpmi  #main_loop	; vai para subrotina compara
		rts


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
; Subrotina que verifica se o n�mero � correspondente � uma opera��o
; matem�tica (adi��o, subtra��o, multiplica��o ou divis�o) ou se �
; um operando. Esta subrotina � utilizada apenas na 1� vez que o programa
; � processado.
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

compara:	stmsk #20h		; seta a m�scara para salto Z=1
		comp  r1,r2		; verifica se a subtra��o de r1-r2 d� Z=1		
		jpmi  #adicao		; se Z=1 ent�o vai para a subrotina adicao

		addi  r2,#01h		; se Z=0 entao r2++ 
		comp  r1,r2		; verifica se a subtra��o de r1-r2 d� Z=1		
		jpmi  #subtracao	; se Z=1 ent�o vai para a subrotina subtracao

		addi  r2,#01h		; se Z=0 entao r2++ 
		comp  r1,r2		; verifica se a subtra��o de r1-r2 d� Z=1		
		jpmi  #multiplicacao	; se Z=1 ent�o vai para a subrotina multiplicacao

		addi  r2,#01h		; se Z=0 entao r2++ 
		comp  r1,r2		; verifica se a subtra��o de r1-r2 d� Z=1		
		jpmi  #divisao		; se Z=1 ent�o vai para a subrotina divis�o

		stmsk #3		; se Z=0 entao seta a m�scara para salto incondicional
		jpmi  #null		; salta para a subrotina null
		rts

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
; Subrotina que calcula a soma de dois n�meros, desempilha a opera��o
; e os operandos, ap�s completada a soma, coloca na pilha o resultado
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

adicao:		ldli  r4,aux		; carrega aux em r4 
		ldhi  r4,aux		;
		addi  r4,#01h		; anda uma posi��o no vetor
		ld    r1,r4,r0		; r1 recebe a posi��o de mem�ria de r4
		stmsk #3		; seta a m�scara para salto incondicional
		jsrmi #compara2		; chama subrotina compara2				
		push  r1		; como r1 n�o � um operando, coloca r1 na pilha
		ldli  r4,aux		; carrega aux em r4 
		ldhi  r4,aux		;
		addi  r4,#02h		; anda uma posi��o no vetor
		ld    r1,r4,r0		; r1 recebe a posi��o de mem�ria de r4
		stmsk #3		; seta a m�scara para salto incondicional
		jsrmi #compara2		; chama subrotina compara2		
		push  r1		; como r1 n�o � um operando, coloca r1 na pilha		
		pop   r2		; retira o 2� operando da pilha e coloca em r2
		pop   r3		; retira o 1� operando da pilha e coloca em r3
		pop   r1		; retira o operador da pilha e coloca em r1
		add   r1,r2,r3		; soma os 2 operandos e coloca em r1
		push  r1		; retorna o resultado em r1 para a pilha
		stmsk #3		; seta a m�scara para salto incondicional
		jpmi  #main_loop	; volta para main_loop


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
; Subrotina que calcula a subtra��o de dois n�meros
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

subtracao:		ldli  r4,aux		; carrega aux em r4 
		ldhi  r4,aux		;
		addi  r4,#01h		; anda uma posi��o no vetor
		ld    r1,r4,r0		; r1 recebe a posi��o de mem�ria de r4
		stmsk #3		; seta a m�scara para salto incondicional
		jsrmi #compara2		; chama subrotina compara2				
		push  r1		; como r1 n�o � um operando, coloca r1 na pilha
		ldli  r4,aux		; carrega aux em r4 
		ldhi  r4,aux		;
		addi  r4,#02h		; anda uma posi��o no vetor
		ld    r1,r4,r0		; r1 recebe a posi��o de mem�ria de r4
		stmsk #3		; seta a m�scara para salto incondicional
		jsrmi #compara2		; chama subrotina compara2		
		push  r1		; como r1 n�o � um operando, coloca r1 na pilha		
		pop   r2		; retira o 2� operando da pilha e coloca em r2
		pop   r3		; retira o 1� operando da pilha e coloca em r3
		pop   r1		; retira o operador da pilha e coloca em r1
		sub   r1,r2,r3		; soma os 2 operandos e coloca em r1
		push  r1		; retorna o resultado em r1 para a pilha
		stmsk #3		; seta a m�scara para salto incondicional
		jpmi  #main_loop	; volta para main_loop


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
; Subrotina que calcula a multiplica��o de dois n�meros
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

multiplicacao:	ldli  r4,aux		; carrega aux em r4 
		ldhi  r4,aux		;
		addi  r4,#01h		; anda uma posi��o no vetor
		ld    r1,r4,r0		; r1 recebe a posi��o de mem�ria de r4
		stmsk #3		; seta a m�scara para salto incondicional
		jsrmi #compara2		; chama subrotina compara2				
		push  r1		; como r1 n�o � um operando, coloca r1 na pilha
		ldli  r4,aux		; carrega aux em r4 
		ldhi  r4,aux		;
		addi  r4,#02h		; anda uma posi��o no vetor
		ld    r1,r4,r0		; r1 recebe a posi��o de mem�ria de r4
		stmsk #3		; seta a m�scara para salto incondicional
		jsrmi #compara2		; chama subrotina compara2		
		push  r1		; como r1 n�o � um operando, coloca r1 na pilha
		pop   r2		; r2 recebe o operando 1
		stmsk #20h 		; seta a m�scara para salto se Z=1
		jsrmi #close1		; caso o operando seja 0 entao vai para close1
		pop   r3		; r3 recebe o operando 2
		pop   r1		; r1 recebe a opera��o multiplica��o
		stmsk #20h 		; seta a m�scara para salto se Z=1
		jsrmi #close1		; caso o operando seja 0 entao vai para close1
		xor   r1,r1,r1		; zera r1
		add   r1,r1,r3		; soma em r1 + r3 e guarda em r1
		subi  r2,#00ffh		; decrementa 1 de r2
loop2:		stmsk #20h 		; seta a m�scara para salto se Z=1
		jsrmi #close		; se o r2 for zero ent�o acabou, vai para o fim
		add   r1,r1,r3		; se r2 nao for zero soma r1 + r3 e guarda em r1
		subi  r2,#00ffh		; decrementa 1 de r2
		jpmi  #loop2		; volta para o loop
close:		push  r1		; adiciona na pilha o resultado
		stmsk #3		; seta a m�scara para salto incondicional
		jpmi  #main_loop	; volta para main_loop
close1:		push  r0		; adiciona na pilha zero pois um dos operandos � zero
		stmsk #3		; seta a m�scara para salto incondicional
		jpmi  #main_loop	; volta para main_loop


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
; Subrotina que calcula a divis�o de dois n�meros
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

divisao:	ldli  r4,aux		; carrega aux em r4 
		ldhi  r4,aux		;
		addi  r4,#01h		; anda uma posi��o no vetor
		ld    r1,r4,r0		; r1 recebe a posi��o de mem�ria de r4
		stmsk #3		; seta a m�scara para salto incondicional
		jsrmi #compara2		; chama subrotina compara2				
		push  r1		; como r1 n�o � um operando, coloca r1 na pilha
		ldli  r4,aux		; carrega aux em r4 
		ldhi  r4,aux		;
		addi  r4,#02h		; anda uma posi��o no vetor
		ld    r1,r4,r0		; r1 recebe a posi��o de mem�ria de r4
		stmsk #3		; seta a m�scara para salto incondicional
		jsrmi #compara2		; chama subrotina compara2		
		push  r1		; como r1 n�o � um operando, coloca r1 na pilha		
		pop   r3		; retira o n�mero a ser dividido e coloca em r3
		pop   r2		; retira o divisor e coloca em r2
		pop   r1		; resultado vai estar em r1	
		xor   r1,r1,r1		; r1 = 0
loop:   	sub   r3,r3,r2		; subtrai r2 de r3 e coloca em r3
		stmsk #80h		; seta a m�scara para salto se n =1
		jpmi  #fim		; se n = 1 jump fim
		stmsk #03h		; seta a m�scara para salto incondicional
		jpmi  #inc		; vai para inc
fim:  		stmsk #03h		; jump incondicional
		jpmi  #exit		; vai para exit
inc:		addi  r1,#01h		; incrementa 1 no resultado que est� em r1
		stmsk #03h		; seta a m�scara para salto incondicional
		jpmi  #loop		; volta para loop
exit:		push  r1		; retorna o resultado em r1 para a pilha
		stmsk #3		; seta a m�scara para salto incondicional
		jpmi  #main_loop	; volta para main_loop		
		

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
; Subrotina que verifica se o n�mero � correspondente � uma opera��o
; matem�tica (adi��o, subtra��o, multiplica��o ou divis�o) ou se �
; um operando. A diferen�a entre essa subrotina e a #compara � que 
; a #compara � executada apenas na 1� intera��o do programa enquanto
; esta � executada todas as futuras vezes que for chamada.
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

compara2:	stmsk #10h		; seta a m�scara para salto Z=0
		comp  r1,r2		; verifica se a subtra��o de r1-r2 d� Z=0		
		jpmi  #continua		; se Z=1 ent�o vai para a subrotina adicao
		st    r6,r1
		jpmi  #adicao
continua:
		addi  r2,#01h		; se Z=0 entao r2++ 
		comp  r1,r2		; verifica se a subtra��o de r1-r2 d� Z=0		
		jpmi  #continua2	; se Z=1 ent�o vai para a subrotina adicao
		st    r6,r1
		jpmi  #subtracao	; se Z=1 ent�o vai para a subrotina subtracao
continua2:
		addi  r2,#01h		; se Z=0 entao r2++ 
		comp  r1,r2		; verifica se a subtra��o de r1-r2 d� Z=0		
		jpmi  #continua3	; se Z=1 ent�o vai para a subrotina adicao
		st    r6,r1
		jpmi  #multiplicacao	; se Z=1 ent�o vai para a subrotina multiplicacao
continua3:
		addi  r2,#01h		; se Z=0 entao r2++ 
		comp  r1,r2		; verifica se a subtra��o de r1-r2 d� Z=0		
		jpmi  #continua4	; se Z=1 ent�o vai para a subrotina adicao
		st    r6,r1
		jpmi  #divisao		; se Z=1 ent�o vai para a subrotina divis�o
continua4:
		stmsk #3		; se Z=0 entao seta a m�scara para salto incondicional
		jpmi  #null		; salta para a subrotina null
		rts




;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
; Subrotina que verifica se existe apenas um elemento na pilha. 
; caso afirmativo, finaliza o programa.
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

null:		stmsk #20h		;seta a m�scara para salto se overflow
		movsp r5		;carrega o valor de sp para r5
		addi  r5,#02h		;soma 2 unidades com r5
		comp  r5,r0
		jpmi  #end		;se V=1 entao na pilha s� tem 1 elemento
					;entao vai para o fim do algor�tmo
		rts


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
; Finaliza a aplica��o com o resultado da express�o sendo o �nico 
; elemento da pilha e o resultado � armazenado na vari�vel resultado.
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

end:		pop   r0		;pega o resultado da express�o calculada e coloca em r0
		ldli  r3,#resultado	
		ldhi  r3,#resultado	;r3 recebe o endere�o da variavel resultado
		st    r3,r0		;coloca na variavel resultado o resultado da express�o calculada
		push  r0		;coloca o resultado na pilha novamente
		halt			;acaba o programa com o resultado na pilha e na vari�vel
.endcode

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
; area de dados
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
.data
n1:		db	#0AACh,#05h,n2  ;expres�o: o 1� elemento � a opera��o e o 2� e 3� elementos s�o os operandos
n2:		db	#0AAAh,#02h,#01h	
resultado:	db	#00h		;variavel que armazena o resultado
cmp:		db	#0AAAh		;variavel padr�o de compara��o
aux:		db      #00h	
.enddata