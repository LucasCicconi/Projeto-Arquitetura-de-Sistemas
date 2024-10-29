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

        -- Exemplo de escrita em matriz A
        we_n <= '0'; -- Habilitando escrita
        address <= "0000000000000000"; -- Endereço para matriz A
        data <= "0000000000000001"; -- Dado a ser escrito
        wait for 20 ns;

        -- Exemplo de leitura de matriz A
        we_n <= '1'; -- Desabilitando escrita
        oe_n <= '0'; -- Habilitando leitura
        wait for 20 ns;

        -- Exemplo de execução de uma operação (por exemplo, soma)
        address <= "0000000000000011"; -- Exemplo de opcode para soma
        wait for 20 ns;

        -- Exibir os resultados
        wait for 50 ns;
        -- Você pode adicionar mais estímulos aqui conforme necessário

        -- Finalizar simulação
        wait;
    end process;
end architecture test;
