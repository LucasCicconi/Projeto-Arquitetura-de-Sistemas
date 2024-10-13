.code
		ldli  r6,#aux		; Carrega em r6 o endereço de aux
		ldhi  r6,#aux		;
		xor   r0,r0,r0		; r0 <= zero (constante no programa)
		ldli  r1,n1		; carrega em r1 a 1ª posição do vetor n1
		ldhi  r1,n1		;
		push  r1		; coloca o conteudo de n1 na pilha
main_loop:	pop   r1		; r1 recebe o valor da pilha 
		push  r1		; e coloca novamente na pilha
		ldli  r2,cmp		; r2 recebe o valor inicial para comparações
		ldhi  r2,cmp		;
		stmsk #3		; seta a máscara para salto incondicional
		jpmi  #compara		; vai para subrotina compara
		stmsk #3		; seta a máscara para salto incondicional
		jpmi  #main_loop	; vai para subrotina compara
		rts


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
; Subrotina que verifica se o número é correspondente à uma operação
; matemática (adição, subtração, multiplicação ou divisão) ou se é
; um operando. Esta subrotina é utilizada apenas na 1ª vez que o programa
; é processado.
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

compara:	stmsk #20h		; seta a máscara para salto Z=1
		comp  r1,r2		; verifica se a subtração de r1-r2 dá Z=1		
		jpmi  #adicao		; se Z=1 então vai para a subrotina adicao

		addi  r2,#01h		; se Z=0 entao r2++ 
		comp  r1,r2		; verifica se a subtração de r1-r2 dá Z=1		
		jpmi  #subtracao	; se Z=1 então vai para a subrotina subtracao

		addi  r2,#01h		; se Z=0 entao r2++ 
		comp  r1,r2		; verifica se a subtração de r1-r2 dá Z=1		
		jpmi  #multiplicacao	; se Z=1 então vai para a subrotina multiplicacao

		addi  r2,#01h		; se Z=0 entao r2++ 
		comp  r1,r2		; verifica se a subtração de r1-r2 dá Z=1		
		jpmi  #divisao		; se Z=1 então vai para a subrotina divisão

		stmsk #3		; se Z=0 entao seta a máscara para salto incondicional
		jpmi  #null		; salta para a subrotina null
		rts

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
; Subrotina que calcula a soma de dois números, desempilha a operação
; e os operandos, após completada a soma, coloca na pilha o resultado
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

adicao:		ldli  r4,aux		; carrega aux em r4 
		ldhi  r4,aux		;
		addi  r4,#01h		; anda uma posição no vetor
		ld    r1,r4,r0		; r1 recebe a posição de memória de r4
		stmsk #3		; seta a máscara para salto incondicional
		jsrmi #compara2		; chama subrotina compara2				
		push  r1		; como r1 não é um operando, coloca r1 na pilha
		ldli  r4,aux		; carrega aux em r4 
		ldhi  r4,aux		;
		addi  r4,#02h		; anda uma posição no vetor
		ld    r1,r4,r0		; r1 recebe a posição de memória de r4
		stmsk #3		; seta a máscara para salto incondicional
		jsrmi #compara2		; chama subrotina compara2		
		push  r1		; como r1 não é um operando, coloca r1 na pilha		
		pop   r2		; retira o 2º operando da pilha e coloca em r2
		pop   r3		; retira o 1º operando da pilha e coloca em r3
		pop   r1		; retira o operador da pilha e coloca em r1
		add   r1,r2,r3		; soma os 2 operandos e coloca em r1
		push  r1		; retorna o resultado em r1 para a pilha
		stmsk #3		; seta a máscara para salto incondicional
		jpmi  #main_loop	; volta para main_loop


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
; Subrotina que calcula a subtração de dois números
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

subtracao:		ldli  r4,aux		; carrega aux em r4 
		ldhi  r4,aux		;
		addi  r4,#01h		; anda uma posição no vetor
		ld    r1,r4,r0		; r1 recebe a posição de memória de r4
		stmsk #3		; seta a máscara para salto incondicional
		jsrmi #compara2		; chama subrotina compara2				
		push  r1		; como r1 não é um operando, coloca r1 na pilha
		ldli  r4,aux		; carrega aux em r4 
		ldhi  r4,aux		;
		addi  r4,#02h		; anda uma posição no vetor
		ld    r1,r4,r0		; r1 recebe a posição de memória de r4
		stmsk #3		; seta a máscara para salto incondicional
		jsrmi #compara2		; chama subrotina compara2		
		push  r1		; como r1 não é um operando, coloca r1 na pilha		
		pop   r2		; retira o 2º operando da pilha e coloca em r2
		pop   r3		; retira o 1º operando da pilha e coloca em r3
		pop   r1		; retira o operador da pilha e coloca em r1
		sub   r1,r2,r3		; soma os 2 operandos e coloca em r1
		push  r1		; retorna o resultado em r1 para a pilha
		stmsk #3		; seta a máscara para salto incondicional
		jpmi  #main_loop	; volta para main_loop


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
; Subrotina que calcula a multiplicação de dois números
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

multiplicacao:	ldli  r4,aux		; carrega aux em r4 
		ldhi  r4,aux		;
		addi  r4,#01h		; anda uma posição no vetor
		ld    r1,r4,r0		; r1 recebe a posição de memória de r4
		stmsk #3		; seta a máscara para salto incondicional
		jsrmi #compara2		; chama subrotina compara2				
		push  r1		; como r1 não é um operando, coloca r1 na pilha
		ldli  r4,aux		; carrega aux em r4 
		ldhi  r4,aux		;
		addi  r4,#02h		; anda uma posição no vetor
		ld    r1,r4,r0		; r1 recebe a posição de memória de r4
		stmsk #3		; seta a máscara para salto incondicional
		jsrmi #compara2		; chama subrotina compara2		
		push  r1		; como r1 não é um operando, coloca r1 na pilha
		pop   r2		; r2 recebe o operando 1
		stmsk #20h 		; seta a máscara para salto se Z=1
		jsrmi #close1		; caso o operando seja 0 entao vai para close1
		pop   r3		; r3 recebe o operando 2
		pop   r1		; r1 recebe a operação multiplicação
		stmsk #20h 		; seta a máscara para salto se Z=1
		jsrmi #close1		; caso o operando seja 0 entao vai para close1
		xor   r1,r1,r1		; zera r1
		add   r1,r1,r3		; soma em r1 + r3 e guarda em r1
		subi  r2,#00ffh		; decrementa 1 de r2
loop2:		stmsk #20h 		; seta a máscara para salto se Z=1
		jsrmi #close		; se o r2 for zero então acabou, vai para o fim
		add   r1,r1,r3		; se r2 nao for zero soma r1 + r3 e guarda em r1
		subi  r2,#00ffh		; decrementa 1 de r2
		jpmi  #loop2		; volta para o loop
close:		push  r1		; adiciona na pilha o resultado
		stmsk #3		; seta a máscara para salto incondicional
		jpmi  #main_loop	; volta para main_loop
close1:		push  r0		; adiciona na pilha zero pois um dos operandos é zero
		stmsk #3		; seta a máscara para salto incondicional
		jpmi  #main_loop	; volta para main_loop


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
; Subrotina que calcula a divisão de dois números
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

divisao:	ldli  r4,aux		; carrega aux em r4 
		ldhi  r4,aux		;
		addi  r4,#01h		; anda uma posição no vetor
		ld    r1,r4,r0		; r1 recebe a posição de memória de r4
		stmsk #3		; seta a máscara para salto incondicional
		jsrmi #compara2		; chama subrotina compara2				
		push  r1		; como r1 não é um operando, coloca r1 na pilha
		ldli  r4,aux		; carrega aux em r4 
		ldhi  r4,aux		;
		addi  r4,#02h		; anda uma posição no vetor
		ld    r1,r4,r0		; r1 recebe a posição de memória de r4
		stmsk #3		; seta a máscara para salto incondicional
		jsrmi #compara2		; chama subrotina compara2		
		push  r1		; como r1 não é um operando, coloca r1 na pilha		
		pop   r3		; retira o número a ser dividido e coloca em r3
		pop   r2		; retira o divisor e coloca em r2
		pop   r1		; resultado vai estar em r1	
		xor   r1,r1,r1		; r1 = 0
loop:   	sub   r3,r3,r2		; subtrai r2 de r3 e coloca em r3
		stmsk #80h		; seta a máscara para salto se n =1
		jpmi  #fim		; se n = 1 jump fim
		stmsk #03h		; seta a máscara para salto incondicional
		jpmi  #inc		; vai para inc
fim:  		stmsk #03h		; jump incondicional
		jpmi  #exit		; vai para exit
inc:		addi  r1,#01h		; incrementa 1 no resultado que está em r1
		stmsk #03h		; seta a máscara para salto incondicional
		jpmi  #loop		; volta para loop
exit:		push  r1		; retorna o resultado em r1 para a pilha
		stmsk #3		; seta a máscara para salto incondicional
		jpmi  #main_loop	; volta para main_loop		
		

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
; Subrotina que verifica se o número é correspondente à uma operação
; matemática (adição, subtração, multiplicação ou divisão) ou se é
; um operando. A diferença entre essa subrotina e a #compara é que 
; a #compara é executada apenas na 1ª interação do programa enquanto
; esta é executada todas as futuras vezes que for chamada.
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

compara2:	stmsk #10h		; seta a máscara para salto Z=0
		comp  r1,r2		; verifica se a subtração de r1-r2 dá Z=0		
		jpmi  #continua		; se Z=1 então vai para a subrotina adicao
		st    r6,r1
		jpmi  #adicao
continua:
		addi  r2,#01h		; se Z=0 entao r2++ 
		comp  r1,r2		; verifica se a subtração de r1-r2 dá Z=0		
		jpmi  #continua2	; se Z=1 então vai para a subrotina adicao
		st    r6,r1
		jpmi  #subtracao	; se Z=1 então vai para a subrotina subtracao
continua2:
		addi  r2,#01h		; se Z=0 entao r2++ 
		comp  r1,r2		; verifica se a subtração de r1-r2 dá Z=0		
		jpmi  #continua3	; se Z=1 então vai para a subrotina adicao
		st    r6,r1
		jpmi  #multiplicacao	; se Z=1 então vai para a subrotina multiplicacao
continua3:
		addi  r2,#01h		; se Z=0 entao r2++ 
		comp  r1,r2		; verifica se a subtração de r1-r2 dá Z=0		
		jpmi  #continua4	; se Z=1 então vai para a subrotina adicao
		st    r6,r1
		jpmi  #divisao		; se Z=1 então vai para a subrotina divisão
continua4:
		stmsk #3		; se Z=0 entao seta a máscara para salto incondicional
		jpmi  #null		; salta para a subrotina null
		rts




;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
; Subrotina que verifica se existe apenas um elemento na pilha. 
; caso afirmativo, finaliza o programa.
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

null:		stmsk #20h		;seta a máscara para salto se overflow
		movsp r5		;carrega o valor de sp para r5
		addi  r5,#02h		;soma 2 unidades com r5
		comp  r5,r0
		jpmi  #end		;se V=1 entao na pilha só tem 1 elemento
					;entao vai para o fim do algorítmo
		rts


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
; Finaliza a aplicação com o resultado da expressão sendo o único 
; elemento da pilha e o resultado é armazenado na variável resultado.
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

end:		pop   r0		;pega o resultado da expressão calculada e coloca em r0
		ldli  r3,#resultado	
		ldhi  r3,#resultado	;r3 recebe o endereço da variavel resultado
		st    r3,r0		;coloca na variavel resultado o resultado da expressão calculada
		push  r0		;coloca o resultado na pilha novamente
		halt			;acaba o programa com o resultado na pilha e na variável
.endcode

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
; area de dados
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
.data
n1:		db	#0AACh,#05h,n2  ;expresão: o 1º elemento é a operação e o 2º e 3º elementos são os operandos
n2:		db	#0AAAh,#02h,#01h	
resultado:	db	#00h		;variavel que armazena o resultado
cmp:		db	#0AAAh		;variavel padrão de comparação
aux:		db      #00h	
.enddata