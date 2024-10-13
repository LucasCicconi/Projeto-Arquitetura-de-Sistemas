architecture Behavioral of R8 is
    -- Sinais para conectar a MPU
    signal ce_n, we_n, oe_n : std_logic;
    signal intr : std_logic;
    signal address : reg16;
    signal data : reg16;

    component MPU is
        port (
            ce_n, we_n, oe_n : in std_logic;
            intr : out std_logic;
            address : in reg16;
            data : inout reg16
        );
    end component;

begin
    -- Instância da MPU
    mpu_inst: MPU
        port map (
            ce_n => ce_n,
            we_n => we_n,
            oe_n => oe_n,
            intr => intr,
            address => address,
            data => data
        );

    -- Lógica de controle da comunicação entre CPU e MPU aqui

end Behavioral;
