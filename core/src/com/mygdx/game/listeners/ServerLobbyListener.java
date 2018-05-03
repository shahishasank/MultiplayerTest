package com.mygdx.game.listeners;

import com.badlogic.gdx.Gdx;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.minlog.Log;
import com.mygdx.game.ServerLobby;
import com.mygdx.game.buffers.BufferPlayerReady;

/**
 * Created by Shasank on 4/18/2018.
 */

public class ServerLobbyListener extends Listener {

    ServerLobby serverLobby;
    public ServerLobbyListener(ServerLobby serverLobby){
        this.serverLobby=serverLobby;
    }
    @Override
    public void connected(Connection connection){
        Gdx.app.log("client","connected");
        serverLobby.addPlayer(connection.getID());
        super.connected(connection);
    }

    @Override
    public void received(Connection connection,Object object){
        if(object instanceof BufferPlayerReady){
            serverLobby.playerReady(connection.getID());
        }
        super.received(connection,object);
    }

    @Override
    public void disconnected(Connection connection){
        serverLobby.removePlayer(connection.getID());
        super.disconnected(connection);
    }
}
