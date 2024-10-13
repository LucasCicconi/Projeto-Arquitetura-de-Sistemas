;;0000    4000    XOR R0,R0,R0
;;0001    7A00    LDL R10,OPA
;;0002    8A80    LDH R10,OPA
;;0003    7400    LDL R4,OPB
;;0004    8480    LDH R4,OPB
;;0005    7A0C    LDL R10,#OPA
;;0006    8A00    LDH R10,#OPA
;;0007    9AA0    LD R10,R10,R0
;;0008    7403    LDL R4,#OPB
;;0009    8403    LDH R4,#OPB
;;000A    9440    LD R4,R4,R0
;;000B    B006    HALT
;;000C    8001    OPA
;;000D    8001    OPB

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
        
        LDL R4,#oPB     
        LDH R4,#oPB     
        LD  R4,R4,R0
      
        
        HALT                

.ENDCODE
.DATA
OPA:    DB      #8001H
oPB:    DB      #8001H
.ENDDATA