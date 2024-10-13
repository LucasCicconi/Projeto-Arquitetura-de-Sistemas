;Aline
;
;  Agora que estamos usando de verdade o montador, estão aparecendo vários
;problemas.
;
; Mais um: na R8 o LDSP não está setando o SP como devia. Ver o exemplo
;em attach.
;
; importante: supor que a pilhe inicie no endereço #2000h
;
;
;

.CODE

.ORG #0000H
        LDL R0, #00H
        LDH R0, #10H    
        JMP R0             ;  SALTA PARA O INÍCIO DO CÓDIGO DO USUÁRIO NO ENDEREÇO #1000H

.ORG #0003H                ;  rotina para tratamento das chamdas de interrupção
        PUSH R0            
        PUSH R1            ;  empilha os registradores
        XOR  R0, R0, R0
        XOR  R1, R1, R1
        LDL  R1, #0FH      ;  R1 <- 000F - mascara para endereço da interrupção
        
        
        ; INP  R0,R0,R0
        LDL  R0, #05H      ;  atendimento simulado da rotina de interrupção número 5 ()
        
        AND  R0, R0, R1
        LDL  R1, #10H
        LD   R0, R0, R1
        JSR  R0            ;  salta para a rotinda de atendimento de interrupção
        
        POP R1
        POP R0
        RTS                ;  deveria ser RTI
        


;  ROTINA PARA TRATAR A INTERRUPÇÃO NUMERO 5 
;  esta rotina deverá utilizar os comando INP e OUTP para acessar o periférico
.ORG #0500H

    PUSH R5
    LDL R5, #55H
    LDH R5, #55H           
    POP R5
    RTS
 
 
 
;  INÍCIO DO PROGRAMA DO USUÁRIO NO ENDEREÇO #1000H
.ORG #1000H

        LDL  R0, #00H
        LDH  R0, #20H    
        LDSP R0          ; inicializa a pilha com 2000H

        LDL  R1, #AAH
        LDH  R1, #BBH    
        LDL  R2, #CCH
        LDH  R2, #DDH   ;  do nothing  
        
        LDL R5, #03H
        LDH R5, #00H           
        JSR R5        ;  salta para o rotina de tratamento de interrupção
                      ;  ou seja, como não temos periférico estamos simulando
                      ;  uma chamada de interrupção
                      
        HALT   
         
.ENDCODE


; endereço das rotinas de tratamento de interupção
.DATA
.ORG #10H
IR0:     DB      #0000H   ; não estamos atribuindo endereço de interrupção à IR0
IR1:     DB      #0100H
IR2:     DB      #0200H
IR3:     DB      #0300H
IR4:     DB      #0400H
IR5:     DB      #0500H
IR6:     DB      #0600H
IR7:     DB      #0700H
IR8:     DB      #0800H
IR9:     DB      #0900H
IRA:     DB      #0A00H
IRB:     DB      #0B00H
IRC:     DB      #0C00H
IRD:     DB      #0D00H
IRE:     DB      #0E00H
IRF:     DB      #0F00H
.ENDDATA
 