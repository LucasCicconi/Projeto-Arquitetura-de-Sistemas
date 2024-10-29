library IEEE;
use IEEE.std_logic_1164.all;
use IEEE.numeric_std.all;

entity MPU_tb is
end entity MPU_tb;

architecture test of MPU_tb is
    -- Sinais de teste
    signal ce_n   : std_logic := '1';
    signal we_n   : std_logic := '1';
    signal oe_n   : std_logic := '1';
    signal clk    : std_logic := '0';
    signal rst    : std_logic := '1';
    signal address : std_logic_vector(15 downto 0) := (others => '0');
    signal data    : std_logic_vector(15 downto 0) := (others => '0');
    signal intr    : std_logic;

    -- Instância da MPU
    component MPU is
        port(
            ce_n : in std_logic;
            we_n : in std_logic;
            oe_n : in std_logic;
            intr : out std_logic;
            clk  : in std_logic;
            rst  : in std_logic;
            address : in std_logic_vector(15 downto 0);
            data : inout std_logic_vector(15 downto 0)
        );
    end component;

begin
    -- Instância do MPU
    UUT: MPU
        port map(
            ce_n   => ce_n,
            we_n   => we_n,
            oe_n   => oe_n,
            intr   => intr,
            clk    => clk,
            rst    => rst,
            address => address,
            data   => data
        );

    -- Geração do clock
    clk_process : process
    begin
        while true loop
            clk <= '1';
            wait for 10 ns;  -- Ciclo alto
            clk <= '0';
            wait for 10 ns;  -- Ciclo baixo
        end loop;
    end process;

    -- Processo de estímulo
    stimulus_process: process
    begin
        -- Reset
        rst <= '1';
        wait for 20 ns;
        rst <= '0';  -- Desativando o reset
        wait for 20 ns;

        -- Teste 1: Operação de Soma
        we_n <= '0'; -- Habilitando leitura/escrita
        oe_n <= '1'; -- Habilitando leitura
        address <= "0000000000000011"; -- Opcode para soma
        wait for 20 ns;

        -- Preenchendo A e B com dados
        we_n <= '0'; 
        oe_n <= '1'; 
        address <= "0000000000000000"; -- Endereço para matriz A
        data <= "0000000000000010"; -- Dado para A (2)
        wait for 20 ns;

        address <= "0000000000000001"; -- Endereço para matriz B
        data <= "0000000000000011"; -- Dado para B (3)
        wait for 20 ns;

        -- Teste 2: Operação de Multiplicação
        address <= "0000000000000101"; -- Opcode para multiplicação
        wait for 20 ns;

        -- Teste 3: Operação de MAC
        address <= "0000000000000111"; -- Opcode para MAC
        wait for 20 ns;

        -- Teste 4: Leitura do resultado
        we_n <= '1'; -- Desabilitando escrita
        oe_n <= '0'; -- Habilitando leitura
        address <= "0000000000000000"; -- Endereço para leitura de A
        wait for 20 ns;

        -- Finalizar simulação
        wait for 50 ns;
        wait;
    end process;

end architecture test;
