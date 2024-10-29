library IEEE;
use IEEE.std_logic_1164.all;
use IEEE.numeric_std.all;

entity MPU_TB is
end entity;

architecture TB of MPU_TB is
    signal ce_n, we_n, oe_n: std_logic := '1';
    signal intr: std_logic;
    signal clk: std_logic := '0';
    signal rst: std_logic := '1';
    signal address: std_logic_vector(15 downto 0) := (others => '0');
    signal data: std_logic_vector(15 downto 0) := (others => '0');
    
    -- Instancia o sinal MATRIX para facilitar a verificação dos resultados
    signal MATRIX : std_logic_vector(1023 downto 0);

    -- Sinal para o opcode `com`
    signal com : std_logic_vector(255 downto 0) := (others => '0');

begin
    -- Clock Generation
    clk_proc: process
    begin
        clk <= not clk after 10 ns;  -- Gera um clock de 50 MHz
        wait for 10 ns;
    end process;

    -- Instância da UUT (Unidade Sob Teste)
    UUT: entity work.MPU
        port map (
            ce_n => ce_n,
            we_n => we_n,
            oe_n => oe_n,
            intr => intr,
            clk => clk,
            rst => rst,
            address => address,
            data => data
        );

    -- Test Process
    test_process: process
    begin
        -- Inicialização: Reinicializar o sistema
        rst <= '1';
        wait for 20 ns;
        rst <= '0';
        wait for 20 ns;
        
        -- Teste de FILL em A, B e C com valor específico
        com <= (others => '0');
        data <= x"0001";  -- Valor de teste para preencher

        -- FILL A
        com(15 downto 0) <= "0000000000000000";
        wait for 20 ns;
        
        -- Verifica se o conteúdo de A está correto
        assert MATRIX(1023 downto 768) = (others => '0000000000000001')
            report "FILL A falhou" severity error;

        -- FILL B
        com(15 downto 0) <= "0000000000000001";
        wait for 20 ns;

        -- Verifica se o conteúdo de B está correto
        assert MATRIX(767 downto 512) = (others => '0000000000000001')
            report "FILL B falhou" severity error;

        -- FILL C
        com(15 downto 0) <= "0000000000000010";
        wait for 20 ns;

        -- Verifica se o conteúdo de C está correto
        assert MATRIX(511 downto 256) = (others => '0000000000000001')
            report "FILL C falhou" severity error;

        -- Teste da SOMA (C = A + B)
        com(15 downto 0) <= "0000000000000011";
        wait for 20 ns;

        -- Verifica o resultado da soma
        assert MATRIX(511 downto 256) = x"0002"  -- Resultado esperado
            report "SOMA A + B falhou" severity error;

        -- Teste da SUB (C = A - B)
        com(15 downto 0) <= "0000000000000100";
        wait for 20 ns;

        -- Verifica o resultado da subtração
        assert MATRIX(511 downto 256) = x"0000"  -- Resultado esperado
            report "SUB A - B falhou" severity error;

        -- Teste da MUL (C = A * B)
        com(15 downto 0) <= "0000000000000101";
        wait for 20 ns;

        -- Verifica o resultado da multiplicação
        assert MATRIX(511 downto 256) = x"0001"  -- Ajuste o valor esperado com base na sua lógica
            report "MUL A * B falhou" severity error;

        -- Teste de MAC (C = C + A * B)
        com(15 downto 0) <= "0000000000000111";
        wait for 20 ns;

        -- Verifica o resultado do MAC
        assert MATRIX(511 downto 256) = x"0002"  -- Ajuste o valor esperado com base na sua lógica
            report "MAC C = C + A * B falhou" severity error;

        -- Encerrar o teste
        wait;
    end process;
end architecture;
