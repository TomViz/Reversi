package reversi.game.basic.com.tom.reversi.network;

import java.io.IOException;

/**
 * Dummy connection for single player mode.
 */
public class DummyConnection implements IConnection
{
    @Override
    public void start() throws IOException
    {
        // Intentionally blank.
    }

    @Override
    public void sendCoordinates(int row, int column)
    {
        // Intentionally blank.
    }

    @Override
    public void close() throws IOException
    {
        // Intentionally blank.
    }
}
