package reversi.game.basic.com.tom.reversi.network;

import java.io.Closeable;
import java.io.IOException;

/**
 * Note: Should extend AutoCloseable instead of having "close".
 */
public interface IConnection extends Closeable
{
    void start() throws IOException;

    void sendCoordinates(int row, int column);
}
