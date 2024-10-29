library IEEE;
use IEEE.std_logic_1164.all;
use IEEE.numeric_std.all;

entity MPU_TB is
end entity MPU_TB;

architecture TB of MPU_TB is
    -- Declaração dos sinais
    signal ce_n, we_n, oe_n : std_logic := '1';
    signal intr : std_logic;
    signal clk : std_logic := '0';
    signal rst : std_logic := '1';
    signal address : std_logic_vector(15 downto 0) := (others => '0');
    signal data : std_logic_vector(15 downto 0) := (others => '0');

    -- Instância do sinal MATRIX para facilitar a verificação dos resultados
    signal MATRIX : std_logic_vector(1023 downto 0);
    signal com : std_logic_vector(15 downto 0) := (others => '0');

begin
    -- Geração do Clock
    clk_process : process
    begin
        clk <= not clk;
        wait for 10 ns;
    end process;

    -- Instância da Unidade Sob Teste (UUT)
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

    -- Processo de Teste
    test_process: process
    begin
        -- Inicialização: Reset do sistema
        rst <= '1';
        wait for 20 ns;
        rst <= '0';
        wait for 20 ns;

        -- Teste de FILL em A com valor específico
        data <= x"0001"; -- Valor para preencher

        -- FILL A
        com <= "0000000000000000";
        wait for 20 ns;

        -- Verifica se o conteúdo de A está correto
        assert MATRIX(1023 downto 768) = (others => '0')  -- Ajustar valor esperado
            report "FILL A falhou" severity error;

        -- Teste de SOMA (C = A + B)
        com <= "0000000000000011";
        wait for 20 ns;

        -- Verifica o resultado da soma
        assert MATRIX(511 downto 256) = x"0002"  -- Valor esperado
            report "SOMA A + B falhou" severity error;

        -- Outros testes podem ser adicionados similarmente para SUB, MUL, MAC

        -- Final do teste
        wait;
    end process;
end architecture TB;
