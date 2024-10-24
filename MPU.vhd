library IEEE;
use IEEE.STD_LOGIC_1164.ALL;
use IEEE.STD_LOGIC_ARITH.ALL;
use IEEE.STD_LOGIC_UNSIGNED.ALL;

entity MPU is
    port (
        ce_n, we_n, oe_n : in std_logic;  -- Sinais de controle
        intr : out std_logic;             -- Interrupção para CPU
        address : in reg16;               -- Endereço de acesso
        data : inout reg16                -- Dado de entrada/saída
    );
end MPU;

architecture Behavioral of MPU is
    -- Matriz A, B, C (4x4) de inteiros de 16 bits
    type matrix_type is array(0 to 3, 0 to 3) of signed(15 downto 0);
    signal A, B, C : matrix_type;

    -- Sinal para comando atual
    signal command : std_logic_vector(15 downto 0);
    signal busy : std_logic := '0';  -- Indica se a MPU está ocupada
    
    -- Constantes para comandos
    constant CMD_ADD : std_logic_vector(15 downto 0) := "0000000000000001";
    constant CMD_SUB : std_logic_vector(15 downto 0) := "0000000000000010";
    constant CMD_MUL : std_logic_vector(15 downto 0) := "0000000000000011";
    constant CMD_MAC : std_logic_vector(15 downto 0) := "0000000000000100";
    constant CMD_FILL : std_logic_vector(15 downto 12) := "0001";
    constant CMD_IDENTITY : std_logic_vector(15 downto 12) := "0010";
    
    -- Variáveis auxiliares
    signal fill_value : signed(15 downto 0);  -- Valor para preencher a matriz
    signal selected_matrix : integer;  -- Seleção da matriz (0=A, 1=B, 2=C)
    
begin
    process (ce_n, we_n, oe_n, address, data)
    begin
        if ce_n = '0' then  -- Chip enable ativo
            if we_n = '0' then  -- Escrita na MPU
                if address = x"0000" then  -- Região de comandos
                    command <= data;  -- Receber o comando
                    busy <= '1';  -- MPU está ocupada
                elsif address >= x"0010" and address <= x"001F" then
                    C((to_integer(address - x"0010") / 4), to_integer(address - x"0010") mod 4) <= signed(data);
                elsif address >= x"0020" and address <= x"002F" then
                    A((to_integer(address - x"0020") / 4), to_integer(address - x"0020") mod 4) <= signed(data);
                elsif address >= x"0030" and address <= x"003F" then
                    B((to_integer(address - x"0030") / 4), to_integer(address - x"0030") mod 4) <= signed(data);
                end if;
            elsif oe_n = '0' then  -- Leitura da MPU
                if address = x"0000" then  -- Estado da MPU
                    data <= std_logic_vector(to_unsigned(busy, 16));  -- Enviar estado da MPU (0=ocioso, 1=ocupado)
                elsif address >= x"0010" and address <= x"001F" then
                    data <= std_logic_vector(C((to_integer(address - x"0010") / 4), to_integer(address - x"0010") mod 4));
                elsif address >= x"0020" and address <= x"002F" then
                    data <= std_logic_vector(A((to_integer(address - x"0020") / 4), to_integer(address - x"0020") mod 4));
                elsif address >= x"0030" and address <= x"003F" then
                    data <= std_logic_vector(B((to_integer(address - x"0030") / 4), to_integer(address - x"0030") mod 4));
                end if;
            end if;
        end if;
    end process;

    -- Processo para execução dos comandos
    process (command)
    begin
        if busy = '1' then
            case command is
                when CMD_ADD =>
                    for i in 0 to 3 loop
                        for j in 0 to 3 loop
                            C(i, j) <= A(i, j) + B(i, j);
                        end loop;
                    end loop;
                    busy <= '0';
                    intr <= '1';  -- Levantar a interrupção
                when CMD_SUB =>
                    for i in 0 to 3 loop
                        for j in 0 to 3 loop
                            C(i, j) <= A(i, j) - B(i, j);
                        end loop;
                    end loop;
                    busy <= '0';
                    intr <= '1';
                when CMD_MUL =>
                    for i in 0 to 3 loop
                        for j in 0 to 3 loop
                            C(i, j) <= (others => '0');
                            for k in 0 to 3 loop
                                C(i, j) <= C(i, j) + A(i, k) * B(k, j);
                            end loop;
                        end loop;
                    end loop;
                    busy <= '0';
                    intr <= '1';
                when CMD_MAC =>
                    for i in 0 to 3 loop
                        for j in 0 to 3 loop
                            for k in 0 to 3 loop
                                C(i, j) <= C(i, j) + A(i, k) * B(k, j);
                            end loop;
                        end loop;
                    end loop;
                    busy <= '0';
                    intr <= '1';
                when others =>
                    if command(15 downto 12) = CMD_FILL then
                        fill_value <= signed(command(11 downto 0));  -- Extrair o valor de preenchimento
                        selected_matrix <= to_integer(command(3 downto 0));
                        for i in 0 to 3 loop
                            for j in 0 to 3 loop
                                if selected_matrix = 0 then
                                    A(i, j) <= fill_value;
                                elsif selected_matrix = 1 then
                                    B(i, j) <= fill_value;
                                else
                                    C(i, j) <= fill_value;
                                end if;
                            end loop;
                        end loop;
                        busy <= '0';
                        intr <= '1';
                    elsif command(15 downto 12) = CMD_IDENTITY then
                        fill_value <= signed(command(11 downto 0));  -- Extrair valor de identidade
                        selected_matrix <= to_integer(command(3 downto 0));
                        for i in 0 to 3 loop
                            for j in 0 to 3 loop
                                if selected_matrix = 0 then
                                    A(i, j) <= (others => '0');
                                    if i = j then A(i, j) <= fill_value; end if;
                                elsif selected_matrix = 1 then
                                    B(i, j) <= (others => '0');
                                    if i = j then B(i, j) <= fill_value; end if;
                                else
                                    C(i, j) <= (others => '0');
                                    if i = j then C(i, j) <= fill_value; end if;
                                end if;
                            end loop;
                        end loop;
                        busy <= '0';
                        intr <= '1';
                    end if;
            end case;
        end if;
    end process;
    
        process(clk)  -- Se houver um clock no sistema
    begin
        if rising_edge(clk) then
            if intr = '1' then
                intr <= '0';  -- Limpar a interrupção após um ciclo de clock
            end if;
        end if;
    end process;
end Behavioral;
