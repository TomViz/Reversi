package reversi.game.basic.com.tom.reversi.network;

import reversi.game.basic.com.tom.reversi.utility.BroadcastRouter;

/**
 * Enum to be used with ConnectionHandler factory method.
 */
public enum ConnectionTypes
{
    HOST(BroadcastRouter.START_FIRST), JOIN(BroadcastRouter.START_SECOND);

    private String broadcastTarget;

    ConnectionTypes(String broadcastTarget)
    {
        this.broadcastTarget = broadcastTarget;
    }

    public String getBroadcastTarget()
    {
        return broadcastTarget;
    }
}
