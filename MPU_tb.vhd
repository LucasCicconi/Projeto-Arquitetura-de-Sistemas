library IEEE;
use IEEE.STD_LOGIC_1164.ALL;
use IEEE.STD_LOGIC_ARITH.ALL;
use IEEE.STD_LOGIC_UNSIGNED.ALL;

entity tb_MPU is
end tb_MPU;

architecture Behavioral of tb_MPU is

    -- Component declaration for MPU
    component MPU is
        port (
            ce_n, we_n, oe_n : in std_logic;
            intr : out std_logic;
            address : in std_logic_vector(15 downto 0);
            data : inout std_logic_vector(15 downto 0)
        );
    end component;

    -- Signals for connecting to MPU
    signal ce_n, we_n, oe_n : std_logic := '1';
    signal intr : std_logic;
    signal address : std_logic_vector(15 downto 0);
    signal data : std_logic_vector(15 downto 0);
    
    -- Clock signal (optional, used for edge-triggered behaviors)
    signal clk : std_logic := '0';

    -- Auxiliary signals
    signal command : std_logic_vector(15 downto 0);
    signal result : std_logic_vector(15 downto 0);

    constant CLK_PERIOD : time := 10 ns;
    
    -- Matrices to hold test values
    constant matrix_a : array(0 to 3, 0 to 3) of std_logic_vector(15 downto 0) := (
        (x"0001", x"0002", x"0003", x"0004"),
        (x"0005", x"0006", x"0007", x"0008"),
        (x"0009", x"000A", x"000B", x"000C"),
        (x"000D", x"000E", x"000F", x"0010")
    );
    
    constant matrix_b : array(0 to 3, 0 to 3) of std_logic_vector(15 downto 0) := (
        (x"0010", x"000F", x"000E", x"000D"),
        (x"000C", x"000B", x"000A", x"0009"),
        (x"0008", x"0007", x"0006", x"0005"),
        (x"0004", x"0003", x"0002", x"0001")
    );

begin

    -- Instantiate the MPU component
    uut: MPU port map (
        ce_n => ce_n,
        we_n => we_n,
        oe_n => oe_n,
        intr => intr,
        address => address,
        data => data
    );

    -- Clock process
    clk_process : process
    begin
        while true loop
            clk <= '0';
            wait for CLK_PERIOD / 2;
            clk <= '1';
            wait for CLK_PERIOD / 2;
        end loop;
    end process;

    -- Stimulus process
    stim_proc : process
    begin
        -- Reset MPU control signals
        ce_n <= '1';
        we_n <= '1';
        oe_n <= '1';
        wait for 100 ns;

        -- Enable the MPU
        ce_n <= '0';
        
        -- Write matrix A to MPU
        for i in 0 to 3 loop
            for j in 0 to 3 loop
                address <= std_logic_vector(to_unsigned(16#0020# + i * 4 + j, 16));
                data <= matrix_a(i, j);
                we_n <= '0';
                wait for 20 ns;
                we_n <= '1';
                wait for 20 ns;
            end loop;
        end loop;

        -- Write matrix B to MPU
        for i in 0 to 3 loop
            for j in 0 to 3 loop
                address <= std_logic_vector(to_unsigned(16#0030# + i * 4 + j, 16));
                data <= matrix_b(i, j);
                we_n <= '0';
                wait for 20 ns;
                we_n <= '1';
                wait for 20 ns;
            end loop;
        end loop;

        -- Comando: Adição (A + B -> C)
        address <= x"0000";
        data <= x"0001"; -- CMD_ADD
        we_n <= '0';
        wait for 20 ns;
        we_n <= '1';
        -- Espera pela interrupção
        wait until intr = '1';
        wait for 20 ns;
        intr <= '0';

        -- Leitura de C (Resultado da adição)
        for i in 0 to 3 loop
            for j in 0 to 3 loop
                address <= std_logic_vector(to_unsigned(16#0010# + i * 4 + j, 16));
                oe_n <= '0';
                wait for 20 ns;
                result <= data;
                oe_n <= '1';
                wait for 20 ns;
            end loop;
        end loop;

        -- Comando: Subtração (A - B -> C)
        address <= x"0000";
        data <= x"0002"; -- CMD_SUB
        we_n <= '0';
        wait for 20 ns;
        we_n <= '1';
        -- Espera pela interrupção
        wait until intr = '1';
        wait for 20 ns;
        intr <= '0';

        -- Leitura de C (Resultado da subtração)
        for i in 0 to 3 loop
            for j in 0 to 3 loop
                address <= std_logic_vector(to_unsigned(16#0010# + i * 4 + j, 16));
                oe_n <= '0';
                wait for 20 ns;
                result <= data;
                oe_n <= '1';
                wait for 20 ns;
            end loop;
        end loop;

        -- Comando: Multiplicação (A * B -> C)
        address <= x"0000";
        data <= x"0003"; -- CMD_MUL
        we_n <= '0';
        wait for 20 ns;
        we_n <= '1';
        -- Espera pela interrupção
        wait until intr = '1';
        wait for 20 ns;
        intr <= '0';

        -- Leitura de C (Resultado da multiplicação)
        for i in 0 to 3 loop
            for j in 0 to 3 loop
                address <= std_logic_vector(to_unsigned(16#0010# + i * 4 + j, 16));
                oe_n <= '0';
                wait for 20 ns;
                result <= data;
                oe_n <= '1';
                wait for 20 ns;
            end loop;
        end loop;

        -- Comando: MAC (C = C + A * B)
        address <= x"0000";
        data <= x"0004"; -- CMD_MAC
        we_n <= '0';
        wait for 20 ns;
        we_n <= '1';
        -- Espera pela interrupção
        wait until intr = '1';
        wait for 20 ns;
        intr <= '0';

        -- Leitura de C (Resultado da operação MAC)
        for i in 0 to 3 loop
            for j in 0 to 3 loop
                address <= std_logic_vector(to_unsigned(16#0010# + i * 4 + j, 16));
                oe_n <= '0';
                wait for 20 ns;
                result <= data;
                oe_n <= '1';
                wait for 20 ns;
            end loop;
        end loop;

        wait;
    end process;
    
end Behavioral;
