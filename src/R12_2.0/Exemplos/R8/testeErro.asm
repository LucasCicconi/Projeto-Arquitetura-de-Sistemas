.CODE
INICIO:
        XOR R0,R0,R0   
                       
        LDL R10,OPA    
        LDH R10,OPA    
        
        
        LDL R4,OPB     
        LDH R4,OPB     
        
        LDL R10,#OPA    
        LDH R10,#OPA    
        LD  R10,R10,R0
        
        LDL R4,#OPB     
        LDH R4,#OPB     
        LD  R4,R4,R0
      
        
        HALT                

.ENDCODE
.DATA
OPA:    DB      #8001H
OPB:    DB      #8001H
.ENDDATA

