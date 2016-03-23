package reversi.game.basic.com.tom.reversi.network;

import java.io.Closeable;
import java.io.IOException;

/**
 * Note: Should extend AutoCloseable instead of having "close". (Not supported below API 19, application written for API 16)
 */
public interface IConnection extends Closeable
{
    void start() throws IOException;

    void sendCoordinates(int row, int column);
}
