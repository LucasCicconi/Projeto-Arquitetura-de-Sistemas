;Aline
;
;  Agora que estamos usando de verdade o montador, est�o aparecendo v�rios
;problemas.
;
; Mais um: na R8 o LDSP n�o est� setando o SP como devia. Ver o exemplo
;em attach.
;
; importante: supor que a pilhe inicie no endere�o #2000h
;
;
;

.CODE

.ORG #0000H
        LDL R0, #00H
        LDH R0, #10H    
        JMP R0             ;  SALTA PARA O IN�CIO DO C�DIGO DO USU�RIO NO ENDERE�O #1000H

.ORG #0003H                ;  rotina para tratamento das chamdas de interrup��o
        PUSH R0            
        PUSH R1            ;  empilha os registradores
        XOR  R0, R0, R0
        XOR  R1, R1, R1
        LDL  R1, #0FH      ;  R1 <- 000F - mascara para endere�o da interrup��o
        
        
        ; INP  R0,R0,R0
        LDL  R0, #05H      ;  atendimento simulado da rotina de interrup��o n�mero 5 ()
        
        AND  R0, R0, R1
        LDL  R1, #10H
        LD   R0, R0, R1
        JSR  R0            ;  salta para a rotinda de atendimento de interrup��o
        
        POP R1
        POP R0
        RTS                ;  deveria ser RTI
        


;  ROTINA PARA TRATAR A INTERRUP��O NUMERO 5 
;  esta rotina dever� utilizar os comando INP e OUTP para acessar o perif�rico
.ORG #0500H

    PUSH R5
    LDL R5, #55H
    LDH R5, #55H           
    POP R5
    RTS
 
 
 
;  IN�CIO DO PROGRAMA DO USU�RIO NO ENDERE�O #1000H
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
        JSR R5        ;  salta para o rotina de tratamento de interrup��o
                      ;  ou seja, como n�o temos perif�rico estamos simulando
                      ;  uma chamada de interrup��o
                      
        HALT   
         
.ENDCODE


; endere�o das rotinas de tratamento de interup��o
.DATA
.ORG #10H
IR0:     DB      #0000H   ; n�o estamos atribuindo endere�o de interrup��o � IR0
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
 