;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; ARQUITETURA R11 - teste de algimas instruções
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

.code
                LDLI    R1, #0ABH
                LDHI    R1, #01H     ; endereço 01AB para início do topo da pilha
                LDSP    R1
                

                ;; teste de algumas instruções
                LDLI    R1,  #0F3H
                LDHI    R1,  #23H


                LDLI    R8,  #52H
                LDHI    R8,  #0E2H     ; flag de negativo

                LDLI    R15, #00H     ; flag de zero
                LDHI    R15, #8FH     ; flag de negativo

 
                ADD     R3, R1, R8     ; R3 <- 23F3 + E252 = (1)0645
                SUB     R4, R1, R8     ; R4 <- 23F3 - E252 =    41A1  
                SUB     R5, R1, R1     ; R5 <- 0  
                AND     R6, R8, R1     ; R6 <- E252 and 23F3 = 2252
                OR      R7, R1, R8     ; R7 <- 23F3 or  E252 = E3F3
                
                ADDI    R1, #0ABH
                SUBI    R1, #0ABH
                
                
                STMSK   #0FFH             ;  salto incondicional para a subrotina somavet (n/n'/z/z'/c/c'/z/z')
                JSRMI   #somavet

                NOP
                NOP
                NOP
                NOP
                NOP
                
                STMSK   #0FFH             ;  salto incondicional para a subrotina testeshift (n/n'/z/z'/c/c'/z/z')
                JSRMI   #testeshift
                
                HALT
                
                
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; um exemplo útil - soma de dois vetores
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

somavet:        PUSH  R1          ; empilha os registradores utilizados 
                PUSH  R2          ; na subrotina
                PUSH  R3
                PUSH  R4
                PUSH  R5
                PUSH  R6
                PUSH  R7

                XOR     R5, R5, R5     ; R5 <- 0

                LDLI    R1,  #end1
                LDHI    R1,  #end1
                LDLI    R2,  #end2
                LDHI    R2,  #end2       
                LDLI    R3,  #end3
                LDHI    R3,  #end3        ; carrega os ponteiro para os três vetores
                
                LDLI    R4   #n
                LDHI    R4,  #n           
                LD      R4, R4, R5        ; carrega o número de elementos
                
                
loop:           LD      R6, R1, R5
                LD      R7, R2, R5
                ADD     R7, R6, R7
                ST      R3, R7           ;  *end3 <- *end1 + *end2      
                
                ADDI    R5, #01H         ;  incrementa o deslocamento para buscar *end1 e *end2
                ADDI    R3, #01H         ;  incrementa o endereço do vetor *end3
                
                SUBI    R4, #01H
                STMSK   #10H             ;  z' (n/n'/z/z'/c/c'/z/z')
                JPMI    #loop
                
                POP  R7
                POP  R6
                POP  R5
                POP  R4
                POP  R3
                POP  R2
                POP  R1
                
                RTS                      ; final da subrotina



;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; teste de shifts
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

testeshift:     PUSH  R1          ; empilha os registradores utilizados 
                PUSH  R2          ; na subrotina
                PUSH  R3

                LDLI    R1,  #55H
                LDHI    R1,  #AAH   ;  R1 <- AA55 = 10101010 01010101
            
                NOT      R1, R1      ;  R1 <- 01010101 10101010 = 55AA  
                
                SL       R2, R1      ;  R2 <- 10101011 01010100 = AB54  (55AA*2)
                SL       R2, R2      ;  R2 <- (1)56A8   
                SLC      R2, R2      ;  R2 <- (1)AD50    (gera carry e entra '0' à esquerda)
  
                SR       R3, R1      ;  R3 <- 00101010 11010101  = 2AD5
                SR       R3, R3      ;  R3 <- 00010101 01101010  = 156A
                SRC      R3, R3      ;  R3 <- 00001010 10110101  = 0AB5        
                
                POP  R3
                POP  R2
                POP  R1               ;  recupera o contexto
 
                RTS                   ; final da subrotina
               
.endcode

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
; area de dados
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

.data

n:    db #08H

end1: db  #03H, #18H, #35H, #0ABH, #0CDH, #77H, #53H, #45H
  

end2: db  #67H, #34H, #21H, #0BFH, #0FH, #0FDH, #11H, #01H

end3: db  #00H  ; resultado

.enddata