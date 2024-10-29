library IEEE;
use IEEE.std_logic_1164.all;
use IEEE.numeric_std.ALL;
use work.R8.all;

entity MPU is
    port(
        ce_n, we_n, oe_n: in std_logic;
        intr: out std_logic;
        clk:    in std_logic;
        rst:    in std_logic;
        address: in std_logic_vector(15 downto 0);
        data: inout std_logic_vector(15 downto 0)
    );

    --ce_n chip enable = chip disponível, se estiver realizando operação, está 1
    --write enable chip write, 0 quando pode escrever e 1 quando não
    --output enable == REad
    end entity MPU;

    architecture reg of MPU is
    -- signal A 	: std_logic_vector(255 downto 0);
    -- signal B 	: std_logic_vector(255 downto 0);
    -- signal C 	: std_logic_vector(255 downto 0);
    signal MATRIX :std_logic_vector(1023 downto 0);
    --1023 -> 768   : A
    --767 -> 512    : B
    --511 -> 256    : C
    --255 -> 0      : CONTROLE
    signal AUX  : std_logic_vector(255 downto 0);
    signal com  : std_logic_vector(255 downto 0);


    --soma otimizado
    procedure SOMA  ( 
                    signal MAT1 : in std_logic_vector(255 downto 0);
                    signal MAT2 : in std_logic_vector(255 downto 0);
                    signal MATR : out std_logic_vector(255 downto 0)
                    ) is
    begin
        for i in 0 to 15 loop
            MATR((255 - (i * 16)) downto (240 - (i * 16))) <= 
                std_logic_vector(signed(MAT1((255 - (i * 16)) downto (240 - (i * 16)))) + 
                                 signed(MAT2((255 - (i * 16)) downto (240 - (i * 16)))));
        end loop;
    end SOMA;

    --sub otimizado
    procedure SUB  ( 
                signal MAT1 	:   in  std_logic_vector(255 downto 0);
                signal MAT2 	:   in  std_logic_vector(255 downto 0);
                signal MATR 	:   out std_logic_vector(255 downto 0)
                ) is
    begin
        for i in 0 to 15 loop
            MATR((255 - i*16) downto (240 - i*16)) <= 
                std_logic_vector(signed(MAT1((255 - i*16) downto (240 - i*16))) - 
                                 signed(MAT2((255 - i*16) downto (240 - i*16))));
        end loop;
    end SUB;


    procedure FILL (
                    signal MAT  :   out std_logic_vector(255 downto 0);
                    signal data :   in  std_logic_vector(15 downto 0)
                    ) is
        begin
            for i in 0 to 15 loop
                MAT(255 - (i * 16) downto 240 - (i * 16)) <= data;
            end loop;
    end FILL;



        --mul otimizado
    procedure MUL  (
                signal A 	:   in  std_logic_vector(255 downto 0);
                signal B 	:   in  std_logic_vector(255 downto 0);
                signal C 	:   out std_logic_vector(255 downto 0)
               ) is
    type temp_array is array (0 to 15) of std_logic_vector(31 downto 0);
    variable temp_sums : temp_array;
    begin
    for i in 0 to 15 loop
        -- Calcule o valor temporário para cada bloco de 16 bits
        temp_sums(i) := std_logic_vector(
            (signed(A(255 - i*16 downto 240 - i*16)) * signed(B(255 - i*16 downto 240 - i*16))) +
            (signed(A(239 - i*16 downto 224 - i*16)) * signed(B(191 - i*16 downto 176 - i*16))) +
            (signed(A(223 - i*16 downto 208 - i*16)) * signed(B(127 - i*16 downto 112 - i*16))) +
            (signed(A(207 - i*16 downto 192 - i*16)) * signed(B(63 - i*16 downto 48 - i*16)))
        );
        -- Atribui a cada bloco de saída os 16 bits mais significativos do cálculo temporário
        C(255 - i*16 downto 240 - i*16) <= temp_sums(i)(31 downto 16);
    end loop;
    end MUL;

    procedure MAC  (
                signal A  : in std_logic_vector(255 downto 0);
                signal B  : in std_logic_vector(255 downto 0);
                signal C  : inout std_logic_vector(255 downto 0)
               ) is
    variable AUX : std_logic_vector(255 downto 0);
    begin

        MUL(A, B, AUX);
       
        SOMA(C, AUX, C);

    end MAC;


    procedure ID   (
                    signal MAT 	:   out  std_logic_vector(255 downto 0);
                    signal data :   in std_logic_vector(15 downto 0)
                   ) is
        begin
        MAT(255 downto 240) <= data;
        MAT(239 downto 224) <= "0000000000000000";
        MAT(223 downto 208) <= "0000000000000000";
        MAT(207 downto 192) <= "0000000000000000";
        MAT(191 downto 176) <= "0000000000000000";
        MAT(175 downto 160) <= data;
        MAT(159 downto 144) <= "0000000000000000";
        MAT(143 downto 128) <= "0000000000000000";
        MAT(127 downto 112) <= "0000000000000000";
        MAT(111 downto 96)  <= "0000000000000000";
        MAT(95  downto 80)  <= data;
        MAT(79  downto 64)  <= "0000000000000000";
        MAT(63  downto 48)  <= "0000000000000000";
        MAT(47  downto 32)  <= "0000000000000000";
        MAT(31  downto 16)  <= "0000000000000000";
        MAT(15  downto 0)   <= data;

    end ID;

    --read 
    procedure READ (
        signal address : in std_logic_vector(5 downto 0);
        signal data : out std_logic_vector(15 downto 0);
        signal MAT : in std_logic_vector(1023 downto 0)
        ) is
    variable start_pos : integer;
    begin
    -- Calcula a posição inicial do intervalo em `MAT` com base no valor de `address`
    start_pos := to_integer(unsigned(address)) * 16;

    -- Atribui o intervalo correspondente de `MAT` a `data`
    data <= MAT(start_pos + 15 downto start_pos);
    end READ;

    --write modificado para algo menor porem nao testado
    procedure WRITE (
                    signal address : in std_logic_vector(5 downto 0);
                    signal data : in std_logic_vector(15 downto 0);
                    signal MAT : out std_logic_vector(1023 downto 0)
                    ) is
    variable start_pos : integer;
    begin
    -- Calcula a posição inicial do intervalo em `MAT` com base no valor de `address`
    start_pos := to_integer(unsigned(address)) * 16;
    
    -- Atribui `data` ao intervalo correspondente em `MAT`
    MAT(start_pos + 15 downto start_pos) <= data;
    end WRITE;

    begin
    data <= (others => 'Z');
    
	    -- Processamento da ULA baseado no opcode
    process(MATRIX, AUX, oe_n, ce_n, we_n, clk, rst, data, address)
    begin
        if rising_edge(clk) then
            case com(15 downto 0) is  --Se com na posição addressess for igual a:
                when "1111111111111111"=>                              --fill A com data
                    if we_n = '0' then 
                        WRITE(address(5 downto 0), data, MATRIX);
                    elsif oe_n = '0' then
                        READ(address(5 downto 0), data, MATRIX);
                    
                    end if;
                when "1000000000000000"=>

                when "0100000000000000"=>
                    --Passa pra data
                when "0010000000000000"=>
                    --Passa pra data
                when "0000000000000000"=>                              --fill A com data
                    FILL(MATRIX(1023 downto 768), data);
                when "0000000000000001"=>                              --fill B com data
                    FILL(MATRIX(767 downto 512), data);
                when "0000000000000010"=>                              --fill C com data
                    FILL(MATRIX(511 downto 256), data);
                when "0000000000000011"=>                               --Soma A, B, Armazena em C
                    SOMA(MATRIX(1023 downto 768), MATRIX(767 downto 512), MATRIX(511 downto 256));
                when "0000000000000100"=>                               --Sub A, B, Armazena em C
                    SUB(MATRIX(1023 downto 768), MATRIX(767 downto 512), MATRIX(511 downto 256));
                when "0000000000000101"=>                               --Multiplicação C = A X B
                    MUL(MATRIX(1023 downto 768), MATRIX(767 downto 512), MATRIX(511 downto 256));
                when "0000000000000111" =>                              -- MAC: C = C + A x B
                    MAC(MATRIX(1023 downto 768), MATRIX(767 downto 512), MATRIX(511 downto 256));
                when "0000000000001000"=>                              --Identidade A, B e C igual a data respectivamente
                    ID(MATRIX(1023 downto 768), data);                                         
                when "0000000000001001"=>
                    ID(MATRIX(767 downto 512), data);
                when "0000000000001010"=>
                    ID(MATRIX(511 downto 256), data);
                        

                when others =>        
            end case;
        end if;
    end process;

end architecture reg;


