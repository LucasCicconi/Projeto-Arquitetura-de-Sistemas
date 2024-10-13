; Jose Antonio A. Tavares Fo.
; jtavares@inf.pucrs.br
;
; Elementos do vetor 1 que nao estao no 2 serao inseridos nele.
; vet2 = {20, 5, 16, 3, 18, 10, 9, 4, 19, 15, 1, 6, 8, 14, 12}
; vet1 = {19, 1, 17, 2, 13, 3, 11, 5, 7}
; 5 elementos serao inseridos no vet2!

;R0 = desloc vet1
;R1 = tamanho vet1
;R2 = base vet1
;R3 = desloc vet2
;R4 = tamanho vet2
;R5 = base vet2

;R6 = elem operando vet1
;R7 = elem operando vet2

;R9 = tamanho total do vet2 para posicao em que serah inserido o novo elemento

.CODE
               XOR R0, R0, R0      ; inicializa R0 com 0
               LDL R1, #n1
               LDH R1, #n1         ; coloca em R1 o endereco de n1
               LD R1, R1, R0       ; atribui a R1 o conteudo de n1 + o conteudo de R0 que eh zero
               LDL R2, #vet1
               LDH R2, #vet1       ; primeira posicao do vetor 1 

               XOR R3, R3, R3      ; inicializa R3 com 0
               LDL R4, #n2
               LDH R4, #n2         ; coloca em R4 o endereco de n2
               LD R9, R4, R3       ; atribui a R9 o conteudo de n2 + o conteudo de R3 que eh zero
               LD R4, R4, R3       ; atribui a R4 o conteudo de n2 + o conteudo de R3 que eh zero
               LDL R5, #vet2
               LDH R5, #vet2       ; primeira posicao do vetor 2

               XOR R10, R10, R10   ; usado somente para o store da quantidade de elementos do vet2 quando incrementada

loop:          LD R6, R2, R0       ; primeiro operando
               LD R7, R5, R3       ; segundo operando
               SUB R8, R6, R7      ; se o resultado for zero, os elementos sao iguais, descarta elem de vet1
               JMPZD #proximo_vet1

         
proximo_vet2:  ADDI R3, #01H
               SUBI R4, #01H
               JMPZD #insere
               JMPD #loop

proximo_vet1:  XOR R3, R3, R3      ; inicializa R3 com 0
               LDL R4, #n2
               LDH R4, #n2         ; busca a posicao da memoria onde estah o tamanho do vet2
               LD R4, R4, R3       ;; coloca em R4 o tamanho atual do vet2
               ADDI R0, #01H       ;;; vai para o proximo elemento do vetor 1
               SUBI R1, #01H       ;;;
               JMPZD #fim
               JMPD #loop

insere:        ST R6, R5, R9       ; grava R6 depois do vetor2, (R5=base)+(R9=tam)
               ADDI R9, #01H       ; aumenta a quantidade de elementos que existem no vetor 2
               LDL R4, #n2
               LDH R4, #n2         ; busca a posicao da memoria onde estah o tamanho do vet2
               ST R9, R4, R10      ; grava novo tamanho do vet2 na memoria
               JMPD #proximo_vet1

fim:           HALT


.org #0100H

.DATA

   n1:   DB #0009H
   vet1: DB #0014H, #0001H, #0012H, #0002H, #000DH, #0003H, #000BH, #0005H, #0007H
   n2:   DB #000FH
   vet2: DB #0015H, #0005H, #0011H, #0003H, #0013H, #000AH, #0009H, #0004H, #0014H, #000FH, #0001H, #0006H, #0008H, #000EH, #000CH

.ENDDATA
