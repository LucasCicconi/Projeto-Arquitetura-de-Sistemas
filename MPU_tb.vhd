library IEEE;
use IEEE.Std_Logic_1164.all;
use IEEE.Numeric_Std.all;

entity MPU_tb is
end entity MPU_tb;

architecture behavior of MPU_tb is
    -- Component declaration of the Unit Under Test (UUT)
    component MPU
        port(
            ce_n, we_n, oe_n: in std_logic;
            intr: out std_logic;
            clk:  in  std_logic;
            rst:  in  std_logic;
            address: in std_logic_vector(15 downto 0);
            data: inout std_logic_vector(15 downto 0)
        );
    end component;

    -- Signals to connect to the UUT
    signal ce_n, we_n, oe_n: std_logic := '1';
    signal intr: std_logic;
    signal clk: std_logic := '0';
    signal rst: std_logic;
    signal address: std_logic_vector(15 downto 0) := (others => '0');
    signal data: std_logic_vector(15 downto 0);

    -- Matrices A, B and C as signals
    signal A : std_logic_vector(255 downto 0);
    signal B : std_logic_vector(255 downto 0);
    signal C : std_logic_vector(255 downto 0);

begin
    -- Instantiate the Unit Under Test (UUT)
    uut: MPU
        port map (
            ce_n => ce_n,
            we_n => we_n,
            oe_n => oe_n,  --Output enable, ligado quando usando o data como saída, else Z
            intr => intr,
            clk  => clk,
            rst  => rst,
            address => address,
            data => data
        );
    -- Test process
    clk_process: process
    begin
        clk <= not(clk);
        wait for 2 ns;
    end process;

    stimulus_process: process
        begin


    end process;
end architecture behavior;
