package com.mygdx.game.listeners;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.mygdx.game.ClientLobby;
import com.mygdx.game.buffers.BufferPlayerList;
import com.mygdx.game.buffers.BufferStartGame;

/**
 * client lobby listener
 */

public class ClientLobbyListener extends Listener {
    ClientLobby clientLobby;
    public ClientLobbyListener(ClientLobby clientLobby){
        this.clientLobby=clientLobby;
    }

    @Override
    public void received(Connection connection,Object object){
        if(object instanceof BufferStartGame){
            clientLobby.startGame();
        }
        /*if(object instanceof BufferPlayerList){
            BufferPlayerList bpl=(BufferPlayerList)object;
            clientLobby.UpdatePlayerList(bpl);
        }*/

        super.received(connection,object);
    }
    @Override
    public void connected(Connection connection){

        super.connected(connection);}

    @Override
    public void disconnected(Connection connection){
        clientLobby.Disconnected();
        super.disconnected(connection);}
}
