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


    end entity MPU;

    architecture reg of MPU is
        signal MATRIX : std_logic_vector(1023 downto 0);
        signal AUX : std_logic_vector(255 downto 0);
        signal MAC_RESULT : std_logic_vector(255 downto 0);  -- Resultado temporário para MAC
        signal com : std_logic_vector(255 downto 0);
        --1023 -> 768   : A
        --767 -> 512    : B
        --511 -> 256    : C
        --255 -> 0      : CONTROLE
    


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
    
    begin
            case address is
                when    "000000"=>
                        data <= MAT(1023 downto 1008);
                when    "000001"=>
                        data <= MAT(1007 downto 992);
                when    "000010"=>  
                        data <= MAT(991 downto 976);
                when    "000011"=>
                        data <= MAT(975 downto 960);
                when    "000100"=>
                        data <= MAT(959 downto 944);
                when    "000101"=>
                        data <= MAT(943 downto 928);
                when    "000110"=>
                        data <= MAT(927 downto 912);
                when    "000111"=>
                        data <= MAT(911 downto 896);
                when    "001000"=>
                        data <= MAT(895 downto 880);
                when    "001001"=>
                        data <= MAT(879 downto 864);
                when    "001010"=>
                        data <= MAT(863 downto 848);
                when    "001011"=>
                        data <= MAT(847 downto 832);
                when    "001100"=>
                        data <= MAT(831 downto 816);
                when    "001101"=>
                        data <= MAT(815 downto 800);
                when    "001110"=>
                        data <= MAT(799 downto 784);
                when    "001111"=>
                        data <= MAT(783 downto 768);
                when    "010000"=>
                        data <= MAT(767 downto 752);
                when    "010001"=>
                        data <= MAT(751 downto 736);
                when    "010010"=>
                        data <= MAT(735 downto 720);
                when    "010011"=>
                        data <= MAT(719 downto 704);
                when    "010100"=>
                        data <= MAT(703 downto 688);
                when    "010101"=>
                        data <= MAT(687 downto 672);
                when    "010110"=>
                        data <= MAT(671 downto 656);
                when    "010111"=>
                        data <= MAT(655 downto 640);
                when    "011000"=>
                        data <= MAT(639 downto 624);
                when    "011001"=>
                        data <= MAT(623 downto 608);
                when    "011010"=>
                        data <= MAT(607 downto 592);
                when    "011011"=>
                        data <= MAT(591 downto 576);
                when    "011100"=>
                        data <= MAT(575 downto 560);
                when    "011101"=>
                        data <= MAT(559 downto 544);
                when    "011110"=>
                        data <= MAT(543 downto 528);
                when    "011111"=>
                        data <= MAT(527 downto 512);
                when    "100000"=>
                        data <= MAT(511 downto 496);
                when    "100001"=>
                        data <= MAT(495 downto 480);
                when    "100010"=>
                        data <= MAT(479 downto 464);
                when    "100011"=>
                        data <= MAT(463 downto 448);
                when    "100100"=>
                        data <= MAT(447 downto 432);
                when    "100101"=>
                        data <= MAT(431 downto 416);
                when    "100110"=>
                        data <= MAT(415 downto 400);
                when    "100111"=>
                        data <= MAT(399 downto 384);
                when    "101000"=>
                        data <= MAT(383 downto 368);
                when    "101001"=>
                        data <= MAT(367 downto 352);
                when    "101010"=>
                        data <= MAT(351 downto 336);
                when    "101011"=>
                        data <= MAT(335 downto 320);
                when    "101100"=>
                        data <= MAT(319 downto 304);
                when    "101101"=>
                        data <= MAT(303 downto 288);
                when    "101110"=>
                        data <= MAT(287 downto 272);
                when    "101111"=>
                        data <= MAT(271 downto 256);
                when    "110000"=>
                        data <= MAT(255 downto 240);
                when    "110001"=>
                        data <= MAT(239 downto 224);
                when    "110010"=>
                        data <= MAT(223 downto 208);
                when    "110011"=>
                        data <= MAT(207 downto 192);
                when    "110100"=>
                        data <= MAT(191 downto 176);
                when    "110101"=>
                        data <= MAT(175 downto 160);
                when    "110110"=>
                        data <= MAT(159 downto 144);
                when    "110111"=>
                        data <= MAT(143 downto 128);
                when    "111000"=>
                        data <= MAT(127 downto 112);
                when    "111001"=>
                        data <= MAT(111 downto 96);
                when    "111010"=>
                        data <= MAT(95 downto 80);
                when    "111011"=>
                        data <= MAT(79 downto 64);
                when    "111100"=>
                        data <= MAT(63 downto 48);
                when    "111101"=>
                        data <= MAT(47 downto 32);
                when    "111110"=>
                        data <= MAT(31 downto 16);
                when    "111111"=>
                        data <= MAT(15 downto 0);
                when others =>
            end case;

    end READ;

    --write modificado para algo menor porem nao testado
    procedure WRITE (
                    signal address : in std_logic_vector(5 downto 0);
                    signal data : in std_logic_vector(15 downto 0);
                    signal MAT : out std_logic_vector(1023 downto 0)
                    ) is
    begin
            case address is
                when    "000000"=>
                        MAT(1023 downto 1008) <= data;
                when    "000001"=>
                        MAT(1007 downto 992) <= data;
                when    "000010"=>  
                        MAT(991 downto 976) <= data;
                when    "000011"=>
                        MAT(975 downto 960) <= data;
                when    "000100"=>
                        MAT(959 downto 944) <= data;
                when    "000101"=>
                        MAT(943 downto 928) <= data;
                when    "000110"=>
                        MAT(927 downto 912) <= data;
                when    "000111"=>
                        MAT(911 downto 896) <= data;
                when    "001000"=>
                        MAT(895 downto 880) <= data;
                when    "001001"=>
                        MAT(879 downto 864) <= data;
                when    "001010"=>
                        MAT(863 downto 848) <= data;
                when    "001011"=>
                        MAT(847 downto 832) <= data;
                when    "001100"=>
                        MAT(831 downto 816) <= data;
                when    "001101"=>
                        MAT(815 downto 800) <= data;
                when    "001110"=>
                        MAT(799 downto 784) <= data;
                when    "001111"=>
                        MAT(783 downto 768) <= data;
                when    "010000"=>
                        MAT(767 downto 752) <= data;
                when    "010001"=>
                        MAT(751 downto 736) <= data;
                when    "010010"=>
                        MAT(735 downto 720) <= data;
                when    "010011"=>
                        MAT(719 downto 704) <= data;
                when    "010100"=>
                        MAT(703 downto 688) <= data;
                when    "010101"=>
                        MAT(687 downto 672) <= data;
                when    "010110"=>
                        MAT(671 downto 656) <= data;
                when    "010111"=>
                        MAT(655 downto 640) <= data;
                when    "011000"=>
                        MAT(639 downto 624) <= data;
                when    "011001"=>
                        MAT(623 downto 608) <= data;
                when    "011010"=>
                        MAT(607 downto 592) <= data;
                when    "011011"=>
                        MAT(591 downto 576) <= data;
                when    "011100"=>
                        MAT(575 downto 560) <= data;
                when    "011101"=>
                        MAT(559 downto 544) <= data;
                when    "011110"=>
                        MAT(543 downto 528) <= data;
                when    "011111"=>
                        MAT(527 downto 512) <= data;
                when    "100000"=>
                        MAT(511 downto 496) <= data;
                when    "100001"=>
                        MAT(495 downto 480) <= data;
                when    "100010"=>
                        MAT(479 downto 464) <= data;
                when    "100011"=>
                        MAT(463 downto 448) <= data;
                when    "100100"=>
                        MAT(447 downto 432) <= data;
                when    "100101"=>
                        MAT(431 downto 416) <= data;
                when    "100110"=>
                        MAT(415 downto 400) <= data;
                when    "100111"=>
                        MAT(399 downto 384) <= data;
                when    "101000"=>
                        MAT(383 downto 368) <= data;
                when    "101001"=>
                        MAT(367 downto 352) <= data;
                when    "101010"=>
                        MAT(351 downto 336) <= data;
                when    "101011"=>
                        MAT(335 downto 320) <= data;
                when    "101100"=>
                        MAT(319 downto 304) <= data;
                when    "101101"=>
                        MAT(303 downto 288) <= data;
                when    "101110"=>
                        MAT(287 downto 272) <= data;
                when    "101111"=>
                        MAT(271 downto 256) <= data;
                when    "110000"=>
                        MAT(255 downto 240) <= data;
                when    "110001"=>
                        MAT(239 downto 224) <= data;
                when    "110010"=>
                        MAT(223 downto 208) <= data;
                when    "110011"=>
                        MAT(207 downto 192) <= data;
                when    "110100"=>
                        MAT(191 downto 176) <= data;
                when    "110101"=>
                        MAT(175 downto 160) <= data;
                when    "110110"=>
                        MAT(159 downto 144) <= data;
                when    "110111"=>
                        MAT(143 downto 128) <= data;
                when    "111000"=>
                        MAT(127 downto 112) <= data;
                when    "111001"=>
                        MAT(111 downto 96) <= data;
                when    "111010"=>
                        MAT(95 downto 80) <= data;
                when    "111011"=>
                        MAT(79 downto 64) <= data;
                when    "111100"=>
                        MAT(63 downto 48) <= data;
                when    "111101"=>
                        MAT(47 downto 32) <= data;
                when    "111110"=>
                        MAT(31 downto 16) <= data;
                when    "111111"=>
                        MAT(15 downto 0) <= data;
                when others =>
            end case;

    end WRITE;

    begin
    data <= (others => 'Z');
    
	   process(MATRIX, AUX, oe_n, ce_n, we_n, clk, rst, data, address, com)
begin
	
    if rising_edge(clk) then
        -- Reseta o sinal de interrupção no início do ciclo
        intr <= '0';
        
        case com(15 downto 0) is
            when "1111111111111111" =>
                if we_n = '0' then 
                    WRITE(address(5 downto 0), data, MATRIX);
                    report "Escrevendo em MATRIX" severity note;
                elsif oe_n = '0' then
                    READ(address(5 downto 0), data, MATRIX);
                    report "Lendo de MATRIX" severity note;
                end if;
                intr <= '1'; -- Ativa a interrupção após a operação de leitura/escrita

            when "0000000000000011" => -- Soma A e B, armazena em C
                SOMA(MATRIX(1023 downto 768), MATRIX(767 downto 512), MATRIX(511 downto 256));
                report "Soma executada: A + B armazenado em C" severity note;
                intr <= '1'; -- Ativa a interrupção após a operação de soma

            when "0000000000000101" => -- Multiplicação
                MUL(MATRIX(1023 downto 768), MATRIX(767 downto 512), MATRIX(511 downto 256));
                report "Multiplicação executada: C = A * B" severity note;
                intr <= '1'; -- Ativa a interrupção após a operação de multiplicação

            when "0000000000000111" => -- C = C + A * B
                MUL(MATRIX(1023 downto 768), MATRIX(767 downto 512), AUX);                      
                SOMA(MATRIX(511 downto 256), AUX, MATRIX(511 downto 256));
                report "C atualizado com a soma: C = C + A * B" severity note;
                intr <= '1'; -- Ativa a interrupção após a operação de soma e multiplicação

            when others =>
                report "Operação não reconhecida" severity warning;
                intr <= '0'; -- Desativa interrupção para operações não reconhecidas
        end case;
    end if;
end process;

        

end architecture reg;