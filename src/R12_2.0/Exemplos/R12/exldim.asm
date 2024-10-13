.code
ldli r1,#9h
ldhi r1,#0h
ldli r2,n
ldhi r2,n
st r2,r1
ldim r5,r1,#02h
halt
.endcode
.data
n: db #0Ah
.enddata