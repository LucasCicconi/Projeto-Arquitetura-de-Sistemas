library IEEE;
use IEEE.STD_LOGIC_1164.ALL;
use IEEE.STD_LOGIC_ARITH.ALL;
use IEEE.STD_LOGIC_UNSIGNED.ALL;

entity R8 is
    Port (
        clk : in std_logic;
        reset : in std_logic;
        -- Outros sinais do processador
        -- Sinais para conectar a MPU
        ce_n, we_n, oe_n : out std_logic;
        intr : in std_logic;
        address : out reg16;
        data : inout reg16
    );
end R8;

architecture Behavioral of R8 is
    -- Declaração de sinais locais
    signal ce_n_internal, we_n_internal, oe_n_internal : std_logic;
    signal address_internal : reg16;
    signal data_internal : reg16;
    
    -- Component da MPU
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
            ce_n => ce_n_internal,
            we_n => we_n_internal,
            oe_n => oe_n_internal,
            intr => intr,
            address => address_internal,
            data => data_internal
        );

    -- Lógica de controle de comunicação entre CPU e MPU
    process (clk, reset)
    begin
        if reset = '1' then
            mpu_status <= x"0000";  -- Resetar status para idle
            intr <= '0';  -- Inicializar o sinal de interrupção
            A_matrix <= (others => (others => '0'));
            B_matrix <= (others => (others => '0'));
            C_matrix <= (others => (others => '0'));
        elsif rising_edge(clk) then
            if operation_done = '1' then
                intr <= '1';  -- Ativar interrupção quando operação for concluída
            else
                intr <= '0';  -- Limpar interrupção após a CPU ler o resultado
            end if;

            if we_n = '0' then  -- Escrita
                case address is
                    when 0 =>  -- Região de comandos
                        -- Escrita de comando
                        case data is
                            when CMD_ADD =>
                                -- Iniciar a operação ADD
                                mpu_status <= x"0001";  -- Busy
                                -- (Executar a lógica de ADD)
                                mpu_status <= x"0010";  -- Done ao terminar
                            when CMD_SUB =>
                                -- Iniciar a operação SUB
                                mpu_status <= x"0001";  -- Busy
                                -- (Executar a lógica de SUB)
                                mpu_status <= x"0010";  -- Done ao terminar
                            when CMD_MUL =>
                                -- Iniciar a operação MUL
                                mpu_status <= x"0001";  -- Busy
                                -- (Executar a lógica de MUL)
                                mpu_status <= x"0010";  -- Done ao terminar
                            when others =>
                                -- Comandos não reconhecidos
                        end case;
                    when 1 to 15 =>
                        -- Outros comandos ou controles
                    when 16 to 31 =>
                        C_matrix(address - 16) <= data;  -- Escreve no índice correto da matriz C
                    when 32 to 47 =>
                        A_matrix(address - 32) <= data;  -- Escreve no índice correto da matriz A
                    when 48 to 63 =>
                        B_matrix(address - 48) <= data;  -- Escreve no índice correto da matriz B
                    when others =>
                        -- Endereços inválidos
                end case;
            elsif oe_n = '0' then  -- Leitura
                case address is
                    when 0 =>  -- Leitura de status
                        data <= mpu_status;  -- Retorna o status atual da MPU
                    when 16 to 31 =>
                        data <= C_matrix(address - 16);  -- Retorna o valor da matriz C
                    when 32 to 47 =>
                        data <= A_matrix(address - 32);  -- Retorna o valor da matriz A
                    when 48 to 63 =>
                        data <= B_matrix(address - 48);  -- Retorna o valor da matriz B
                    when others =>
                        -- Endereços inválidos
                end case;
            end if;
        end if;
    end process;
    

    -- Lógica de tratamento de interrupção da MPU
    process (clk)
    begin
        if rising_edge(clk) then
            if intr = '1' and status = "10" then
                -- Após a leitura dos dados, a CPU pode preparar a próxima operação
                -- Envia um novo comando para a MPU
                data <= "0011_0001_00000010";  -- Exemplo de comando para multiplicar A por B
                we_n <= '0';  -- Habilita a escrita
                ce_n <= '0';  -- Ativa a MPU para começar a próxima operação
            end if;
            if intr = '1' then
                -- Após processar a interrupção, a CPU limpa o sinal
                intr <= '0';  -- Limpa o sinal de interrupção
            end if;
        end if;
    end process;

    -- Mapeamento de sinais internos para externos
    ce_n <= ce_n_internal;
    we_n <= we_n_internal;
    oe_n <= oe_n_internal;
    address <= address_internal;
    data <= data_internal;

end Behavioral;
