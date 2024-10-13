; 
; program to add two vectors
;
.CODE
        XOR R0, R0, R0  ; initialize register R0 with the constant 0
        LDL R1,#N       
        LDH R1,#N       ; get the address where the size of the vectors is kept (N)
        LD  R1, R1,R0   ; get the size of the vectors   
        LDL R2,#vect1       
        LDH R2,#vect1   
        LDL R3,#vect2       
        LDH R3,#vect2   
        LDL R4,#vect3       
        LDH R4,#vect3   ; define pointer to source vectors (vect1, vect2) and destination vector (vect3)
loop:   LD R5,R0,R2       
        LD R6,R0,R3     ; fetch an element of each source vector
        ADD R6, R6,R5   ; add these elements
        ST  R6,R0,R4    ; store in memory the obtained sum
        ADDI R0,#01H    ; increment the displacement for all vectors
        SUBI R1,#01H    ; decrement the number of remaining elements to process
        JMPZD  #end
        JMPD   #loop       
end:    HALT


.org #0100H

.DATA
N:    DB #0005H
vect1: DB #0001H, #2305H, #AB3EH, #6159H, #3334H
vect2: DB #5001H, #00FFH, #FFFFH, #3840H, #2221H
vect3: DB #0000H
.ENDDATA