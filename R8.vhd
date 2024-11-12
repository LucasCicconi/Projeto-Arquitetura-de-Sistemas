
--------------------------------------------------------------------------
library IEEE;
use IEEE.Std_Logic_1164.all;

package R8 is  
    
  subtype reg16  is std_logic_vector(15 downto 0);
  subtype reg4   is std_logic_vector(3 downto 0);
   
  -- instruction indicates the decoded by the control unit  -- 28 INSTRUCTIONS
  -- the 15 conditional jumps are abstracted as just 3 instruction classes: jumpR, jump, jumpD
  type instruction is  
  ( add, sub, and_i, or_i, xor_i, addi, subi, ldl, ldh, ld, st, sl0, sl1, sr0, sr1,
    notA, nop, halt, ldsp, rts, pop, push, jumpR, jump, jumpD, jsrr, jsr, jsrd, intr, intr_rs1);

  
  type microinstruction is record
     mpc:   std_logic_vector(1 downto 0);  -- PC input mux control
     msp:   std_logic;                     -- SP input mux control (stack-pointer)
     mad:   std_logic_vector(1 downto 0);  -- mux to select source for the memory address
     mreg:  std_logic;                     -- mux to select source for the register bank input
     ms2:   std_logic;                     -- register bank source2 mux control
     ma:    std_logic;                     -- ALU A operand mux control
     mb:    std_logic_vector(1 downto 0);  -- ALU B operand mux control
     wpc:   std_logic;                     -- PC write enable
     wsp:   std_logic;                     -- SP write enable
     wir:   std_logic;                     -- IR write enable
     wab:   std_logic;                     -- RegA and RegB write enable
     walu:  std_logic;                     -- RALU write enable
     wreg:  std_logic;                     -- Register Bank write enable  
     wnz:   std_logic;                     -- N and Z flags write enable
     wcv:   std_logic;                     -- C and V flags write enable
     ce,rw: std_logic;                     -- Chip enable and R_W controls
     intr_in: std_logic;                    -- Sinal decode para interrupção
     alu:   instruction;                   -- ALU operation specification
  end record;
         
  -- 16-bit register, with asynchronous reset and write enable
  component register16
       port( ck,rst,ce:in std_logic;
             D:in  reg16;
             Q:out reg16 );
  end component;
  
  procedure addAB  (  signal A,B: in  reg16;   signal Cin: in STD_LOGIC;
                       signal S:   out reg16;   signal Cout, Ov:out STD_LOGIC);
                       
  function is_zero  (  signal A:  in  reg16  ) return std_logic ;
  
end R8;

package body R8 is
 
  procedure addAB  (  signal A,B: in  reg16;   signal Cin: in STD_LOGIC;
                      signal S:   out reg16;   signal Cout, Ov:out STD_LOGIC) is             
     variable cy_prev, carry : STD_LOGIC;
  begin    
             for w in 0 to 15 loop
                  if w=0 then carry:=Cin;  end if;
                  S(w) <= A(w) xor B(w) xor carry;
                  cy_prev  := carry;
                  carry := (A(w) and B(w)) or (A(w) and carry) or (B(w) and carry);
             end loop;
             Cout <= carry;
             Ov <= cy_prev xor carry;
  end addAB;
 
  function is_zero  (  signal A:  in  reg16  ) return std_logic is           
      begin     
             if A=x"0000" then return '1'; else return '0';  end if;
  end is_zero;
  
end R8;

library IEEE;
use IEEE.Std_Logic_1164.all;
use work.R8.all;

entity register16 is
    port(
        ck, rst, ce : in std_logic;
        D : in reg16;
        Q : out reg16
    );
end register16;

architecture register16 of register16 is
    -- Declara intr_reg como um sinal interno
    signal intr_reg : reg16;
begin
    process (ck, rst)
    begin
        if rst = '1' then
            intr_reg <= (others => '0');
        elsif ck'event and ck = '0' then
            if ce = '1' then
                intr_reg <= D;  -- Salva o valor de A no registrador de interrupção
            end if;
        end if;
    end process;

    Q <= intr_reg;  -- Atribui o valor de intr_reg à saída Q
end register16;


library IEEE;
use IEEE.Std_Logic_1164.all;
use ieee.STD_LOGIC_UNSIGNED.all;   
use work.R8.all;

entity reg_bank is
      port(  ck, rst, wreg, rs2: in   std_logic;
             ir, inREG:          in   reg16;
             source1, source2:   out  reg16     );
end entity;

architecture reg_bank of reg_bank is   
   type bank is array (0 to 15) of reg16;
   signal reg : bank;
   signal wen:   reg16;
   signal destB: std_logic_vector(3 downto 0);
begin            
     
    r1:for i in 0 to 15 generate   
        wen(i) <= '1' when  ir(11 downto 8)=i  and wreg='1'  else '0';
        rx: register16 PORT MAP(ck=>ck, rst=>rst, ce=>wen(i), d=>inREG, q=>reg(i));               
    end generate r1;       
  
    -- source1 selection 
    source1 <=  reg( CONV_INTEGER(ir(7 downto 4)) );

    -- source2 selection 
    destB <= ir(3 downto 0) when rs2 = '0' else ir(11 downto 8);
    source2 <=   reg( CONV_INTEGER(destB) );
   
end reg_bank;

library IEEE;
use IEEE.Std_Logic_1164.all;
use IEEE.Std_Logic_unsigned.all;
use work.R8.all;
   
entity datapath is
      port( uins: in microinstruction;
            ck, rst: in std_logic;
            instruction, address : out reg16;
            dataIN:  in  reg16;
            dataOUT: out reg16;

            flag: out reg4 );
end datapath;

architecture datapath of datapath is
	
    component reg_bank
          port( ck, rst, wreg, rs2: in std_logic;
                ir, inREG:           in reg16;
                source1, source2:    out reg16 );
    end component;
		
	 type type_state  is (Sidle, Sfetch);
    signal dtreg, dtpc, dtsp, s1, s2, outalu, pc, sp, ir, rA, rB, ralu, 
           opA, opB, addA, addB, add: reg16;
    signal cin, cout, overflow: std_logic;
    signal intr_reg: reg16;  -- Registrador para salvar o valor de A durante a interrupção
	 
	 signal intr_in           : std_logic;                     -- Entrada para sinal de interrupção externa
    signal interrupt_enabled : std_logic := '0';              -- Flag para habilitar/desabilitar interrupções
    signal interrupt_address : reg16;                         -- Endereço de destino da interrupção
    signal return_address    : reg16;
	 signal EA, PE :  type_state;

begin

  --  IR register to instruction output signal
  instruction <= ir;

  --
  --  datapath registers: register bank, pc, sp, ir, rA, rB, ralu
  --
  REGS: reg_bank port map( ck=>ck, rst=>rst, wreg=>uins.wreg, rs2=>uins.ms2,
                         ir=>ir, inREG=> dtreg, source1=>s1, source2=>s2);

  RPC:      register16 port map(ck=>ck, rst=>rst, ce=>uins.wpc,   d=>dtpc,   q=>pc );
                                                                                           
  RSP:      register16 port map(ck=>ck, rst=>rst, ce=>uins.wsp,   d=>dtsp,   q=>sp );

  RIR:      register16 port map(ck=>ck, rst=>rst, ce=>uins.wir,   d=>dataIN, q=>ir );

  REG_A:    register16 port map(ck=>ck, rst=>rst, ce=>uins.wab,   d=>s1,     q=>rA );

  REG_B:    register16 port map(ck=>ck, rst=>rst, ce=>uins.wab,   d=>s2,     q=>rB );

  REG_alu:  register16 port map(ck=>ck, rst=>rst, ce=>uins.walu,  d=>outalu, q=>ralu );
  
  -- status flags
process (ck, rst)
begin
    if rst = '1' then
        EA <= Sidle;
    elsif rising_edge(ck) then
        if intr_in = '1' and interrupt_enabled = '1' then
            return_address <= pc;         -- Salva o PC atual para retorno
            pc <= interrupt_address;      -- Salta para o endereço de interrupção
            PE <= Sfetch;                 -- Volta para buscar a instrução de interrupção
        else
            if uins.wnz = '1' then
                flag(0) <= outalu(15);    -- negative flag
                flag(1) <= is_zero(outalu);
            end if;

            if uins.wcv = '1' then
                flag(2) <= cout;
                flag(3) <= overflow;
            end if;
        end if;
    end if;
end process;


  --
  --  multiplexers
  --
  dtpc <= intr_reg when uins.mpc = "10" else   -- Adiciona intr_reg como uma opção de entrada para o PC
            ralu    when uins.mpc = "01" else   
            dataIN  when uins.mpc = "00" else
            pc + 1;                             -- Default: PC incrementado
                                    -- by default the PC is incremented;

  dtsp <= sp-1 when uins.msp = '1' else        -- operand selection for SP register
          ralu;

  address  <= ralu  when uins.mad = "00" else  -- selection of who addresses the external RAM
               pc    when uins.mad = "01" else
               sp;

  opA <= ir     when uins.ma = '1' else ra;     -- A operand for the ALU   
      
  opB <= sp     when uins.mb = "01" else    -- B operand for the ALU, or memory
         pc     when uins.mb = "10" else 
         rb;    

  dtreg <= dataIN when uins.mreg = '1' else ralu; 
	  -- register bank writing contents selection
      
  -- moraes 21/March - multiplexer for the output data - bug corrected
  dataOUT <=  s2 when ir(15 downto 12)="1010" else opB;    
  
  ---
  ---  ALU - operation depend only on the current instruction 
  ---       (decoded in the control unit)
  ---

  addA <= "00000000" & opA(7 downto 0)       when uins.alu=addi else
           not ("00000000" & opA(7 downto 0)) when uins.alu=subi else           
           opA(11) & opA(11) & opA(11) & opA(11) & opA(11 downto 0)             when  uins.alu=jsrd else
           opA(9) & opA(9) & opA(9) & opA(9) & opA(9) & opA(9)& opA(9 downto 0) when uins.alu=jumpD else
           opA; -- add, sub, ld, st
           
  addB <= not opB  when uins.alu=sub  else
           opB; 
           
  cin <= '1' when uins.alu=sub or uins.alu=subi else '0';
  
  addAB( addA, addB, cin, add, cout, overflow); 
  
  outalu <= opA and opB                        when uins.alu = and_i else  
            opA or  opB                        when uins.alu = or_i  else   
            opA xor opB                        when uins.alu = xor_i else  
            opB(15 downto 8) & opA(7 downto 0) when uins.alu = ldl   else
            opA(7 downto 0)  & opB(7 downto 0) when uins.alu = ldh   else
            opA(14 downto 0) & '0'             when uins.alu = sl0   else
            opA(14 downto 0) & '1'             when uins.alu = sl1   else
            '0' & opA(15 downto 1)             when uins.alu = sr0   else
            '1' & opA(15 downto 1)             when uins.alu = sr1   else
             not opA                           when uins.alu = notA  else 
             opB + 1                           when uins.alu = rts or uins.alu=pop else  
             RA                                when uins.alu = jump or uins.alu=jsr  or uins.alu=ldsp else      
             add;     -- by default the ALU operation is add!!
  
    
end datapath;

library IEEE;
use IEEE.Std_Logic_1164.all;
use IEEE.Std_Logic_unsigned.all;
use work.R8.all;
entity control_unit is
      port (uins:    out microinstruction;
            rst,ck,intr_in: in std_logic;
            flag:   in reg4;
            ir:     in reg16 );
end control_unit;

architecture control_unit of control_unit is

  type type_state  is (Sidle, Sfetch, Srreg, Shalt, Salu,Sint_r,
  Srts, Spop, Sldsp, Sld, Sst, Swbk, Sjmp, Ssbrt, Spush);
  -- 13 states
  signal EA, PE :  type_state;

  signal fn, fz, fc, fv, inst_la1, inst_la2:  std_logic;
  signal i : instruction;
  signal interrupt_enabled : std_logic := '0';  -- Default disabled
  signal interrupt_address : reg16; 
begin
   
   fn <= flag(0);          -- status flags
   fz <= flag(1);
   fc <= flag(2);
   fv <= flag(3);
   
  i <= add   when ir(15 downto 12)=0  else
       sub   when ir(15 downto 12)=1  else
       and_i when ir(15 downto 12)=2  else
       or_i  when ir(15 downto 12)=3  else
       xor_i when ir(15 downto 12)=4  else
       addi  when ir(15 downto 12)=5  else
       subi  when ir(15 downto 12)=6  else
       ldl   when ir(15 downto 12)=7  else
       ldh   when ir(15 downto 12)=8  else
       ld    when ir(15 downto 12)=9  else
       st    when ir(15 downto 12)=10 else
       sl0   when ir(15 downto 12)=11 and ir(3 downto 0)=0  else
       sl1   when ir(15 downto 12)=11 and ir(3 downto 0)=1  else
       sr0   when ir(15 downto 12)=11 and ir(3 downto 0)=2  else
       sr1   when ir(15 downto 12)=11 and ir(3 downto 0)=3  else
       notA  when ir(15 downto 12)=11 and ir(3 downto 0)=4  else
       nop   when ir(15 downto 12)=11 and ir(3 downto 0)=5  else
       halt  when ir(15 downto 12)=11 and ir(3 downto 0)=6  else
       ldsp  when ir(15 downto 12)=11 and ir(3 downto 0)=7  else
       rts   when ir(15 downto 12)=11 and ir(3 downto 0)=8  else
       pop   when ir(15 downto 12)=11 and ir(3 downto 0)=9  else
       push  when ir(15 downto 12)=11 and ir(3 downto 0)=10 else 
       intr  when ir(15 downto 12)=11 and ir(3 downto 0)= 11 else
		 intr_rs1 when ir(15 downto 12) = 12 and ir(3 downto 0) = 11 else
               
       -- jump instructions ** It is here that the status flags are tested to jump or not to jump *************

       jumpR when ir(15 downto 12)=12 and  ( ir(3 downto 0)=0 or 
                   (ir(3 downto 0)=1 and fn='1') or (ir(3 downto 0)=2 and fz='1') or 
                   (ir(3 downto 0)=3 and fc='1') or (ir(3 downto 0)=4 and fv='1') )   else 
                   
       jump  when ir(15 downto 12)=12 and  ( ir(3 downto 0)=5 or 
                   (ir(3 downto 0)=6 and fn='1') or (ir(3 downto 0)=7 and fz='1') or 
                   (ir(3 downto 0)=8 and fc='1') or (ir(3 downto 0)=9 and fv='1') )   else 

                   
       jumpD  when ir(15 downto 12)=13 or ( ir(15 downto 12)=14 and  
                   ( (ir(11 downto 10)=0 and fn='1') or (ir(11 downto 10)=1 and fz='1') or 
                     (ir(11 downto 10)=2 and fc='1') or (ir(11 downto 10)=3 and fv='1') ) )  else 
                   
       jsrr  when ir(15 downto 12)=12 and ir(3 downto 0)=10 else
       jsr   when ir(15 downto 12)=12 and ir(3 downto 0)=11 else
       jsrd  when ir(15 downto 12)=15 else
       
       nop ;  -- IMPORTANT: default condition in case flag values are '1';
        
  uins.alu <= i;       -- operation that the alu is about to execute

  -- logic and arithmetic instructions (la) of type 1:
  inst_la1 <= '1' when i=add or i=sub or i=and_i or i=or_i or i=xor_i or i=notA or i=sl0 or i=sr0 or i=sl1 or i=sr1 else
              '0';

  -- logic and arithmetic instructions (la) of type 2 (rt on the right side of the expression):
  inst_la2 <= '1' when i=addi or i=subi or i=ldl or i=ldh else
              '0'; 
              

  uins.mpc <= "10" when EA=Sfetch else
              "00" when EA=Srts  else
              "01";                     -- alu mux

  uins.msp <= '1' when  i=jsrr or i=jsr or i=jsrd or i=push else  '0';
  -- the SP register employs post-decrement for push / pre-increment for pop
              

  uins.mad <= "10" when EA=Spush or  EA=Ssbrt else  -- in a subroutine mem is addressed by SP
              "01" when EA=Sfetch else              -- in an instruction fetch mem is addressed by the PC
              "00";                                 -- default used is for LD/ST instructions

  -- in which register to write contents just arrived from memory, if any
  uins.mreg <= '1' when i=ld or i=pop else '0';
       
  -- the second source operand (source2) receives the destination register address
  -- when the instruction in question is an arithmetic/logic type 2, a push or memory write,
  -- since register contents must be sent to external memory
  uins.ms2 <= '1' when inst_la2='1' or i=push or EA=Sst else '0';
 
  -- first alu operand is the IR register when instruction is an 
  -- arithmetic/logic type 2 or a jump/jsr with short displacement 
  uins.ma <= '1' when inst_la2='1' or i=jumpD or i=jsrd else '0';   

  uins.mb <= "01"  when  i=rts or i=pop   else      -- to increment the SP register
             "10"  when  i=jumpR or i=jump or i=jumpD or i=jsrr or i=jsr or i=jsrd  else 
             	-- in jumps and jsr instructions, the PC resgister is the ALU second operand
             "00" ;      

  
  uins.wpc  <= '1' when EA=Sfetch or EA=Sjmp  or EA=Ssbrt or EA=Srts               else '0';
  uins.wsp  <= '1' when EA=Sldsp  or EA=Srts   or EA=Ssbrt or EA=Spush or EA=Spop  else '0';
  uins.wir  <= '1' when EA=Sfetch                                                  else '0';
  uins.wab  <= '1' when EA=Srreg                                                   else '0';
  uins.walu <= '1' when EA=Salu                                                    else '0';
  uins.wreg <= '1' when EA=Swbk  or EA=Sld   or EA=Spop                            else '0';
  uins.wnz  <= '1' when EA=Salu and (inst_la1='1' or  i=addi or i=subi)            else '0';
  uins.wcv  <= '1' when EA=Salu and (i=add or i=addi or i=sub or i=subi)           else '0';
  
      ---  IMPORTANT !!!!!!!!!!!!!
  uins.ce<='1' when rst='0' and (EA=Sfetch or EA=Srts or EA=Spop or EA=Sld or EA=Ssbrt or EA=Spush or EA=Sst) else '0';
  uins.rw<='1' when EA=Sfetch or EA=Srts or EA=Spop or EA=Sld else '0';
  
  process(rst, ck)
   begin
      if rst='1' then 
		  EA <= Sidle; -- SIDLE is the state the machine stays while processor is being reset
       elsif ck'event and ck='1' then
          if EA=Sidle then
                EA <= Sfetch;
          else
                EA <= PE;
          end if;
     end if;
  end process;
  
  process(EA,i)     -- NEXT state: depends on the PRESENT state and on the current instruction
   begin

    case EA is
                
         when Sidle => 
				PE <=Sidle; -- reset being active, the processor do nothing!       
     --
     -- first clock cycle after reset and after each instruction ends execution
     --
 when Sfetch =>
            -- First cycle after reset or end of instruction execution
            if i = halt then
                PE <= Shalt;
            elsif i = intr_rs1 then
                if ir(7 downto 4) /= "0000" then  -- Se Rs1 != 0
                    interrupt_enabled <= '1';     -- Enable interrupts
                    interrupt_address <= reg(CONV_INTEGER(ir(7 downto 4))); -- MEM[Rs1]
                else
                    interrupt_enabled <= '0';     -- Disable interrupts
                end if;
                PE <= Sfetch;
            else
                PE <= Srreg;  -- Proceed with regular instruction processing
            end if;

when halt =>
    PE <= Shalt;

when others =>
    PE <= Srreg;


     --
     -- second clock cycle of every instruction
     --
     when Shalt  =>  PE <= Shalt;    -- When instruction is HALT lock processor until external reset occurs

     when Srreg =>   PE <= Salu;

     when Sint_r =>
			interrupt_enabled <= '1';
         return_address <= address;  -- Salva endereço de retorno
         PE <= Sfetch;               -- Retorna para buscar nova instrução

     --
     -- third clock cycle of every instruction - there are 9 distinct destinations from here
     --
     when Salu => if i=pop                                 then   PE <= Spop;
                    elsif i=rts                            then   PE <= Srts;
                    elsif i=ldsp                           then   PE <= Sldsp;
                    elsif i=ld                             then   PE <= Sld;
                    elsif i=st                             then   PE <= Sst;
                    elsif inst_la1='1' or inst_la2='1'     then   PE <= Swbk;
                    elsif i=jumpR or i=jump or i=jumpD     then   PE <= Sjmp;
                    elsif i=jsrr or i=jsr or i=jsrd        then   PE <= Ssbrt;
                    elsif i=push                           then   PE <= Spush;
                    else  PE <= Sfetch;   -- nop and jumps with flag=0 execute in just 3 clock cycles ** ATTENTION **
                   end if;

     --
     -- fourth clock cycle of every instruction - GO BACK TO FETCH
     -- 
     when Spop | Srts | Sldsp | Sint_r| Sld | Sst | Swbk | Sjmp | Ssbrt | Spush =>  PE <= Sfetch;
  
   end case;

  end process;

end control_unit;

library IEEE;
use IEEE.Std_Logic_1164.all;
use work.R8.all;

entity processor is
      port( ck,rst,intr_in: in std_logic;
            dataIN:  in  reg16;
            dataOUT: out reg16;
            address: out reg16;
            ce,rw: out std_logic );
end processor;

architecture processor of processor is

      component control_unit
            port( uins:   out microinstruction;
                  ck,rst,intr_in: in std_logic;
                  flag:   in reg4;
                  Rs1:     in std_logic;
                  ir:     in reg16 );
      end component;

      component datapath
            port( uins: in microinstruction;
                  ck, rst,intr_in: in std_logic;
                  instruction, address : out reg16;
                  dataIN:  in  reg16;
                  dataOUT: out reg16;
                  flag: out reg4 );
      end component;

      signal flag: reg4;
      signal uins: microinstruction;
      signal ir: reg16;

begin

    dp: datapath   port map(uins=>uins, ck=>ck, rst=>rst, intr_in => intr_in, instruction=>ir,
                            address=>address,dataIN=>dataIN, dataOUT=>dataOUT, flag=>flag);

    ctrl: control_unit port map(uins => uins, ck => ck, rst => rst, intr_in => intr_in, flag => flag, ir => ir);

    ce <= uins.ce;
    rw <= uins.rw;

end processor;
