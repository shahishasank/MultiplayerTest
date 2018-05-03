package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Server;
import com.esotericsoftware.minlog.Log;
import com.mygdx.game.buffers.BufferPlayerList;
import com.mygdx.game.buffers.BufferStartGame;
import com.mygdx.game.listeners.ServerLobbyListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Shasank on 4/17/2018.
 */

public class ServerLobby extends Stage {

    ArrayList<Player> playersList= new ArrayList<Player>();
    Menu main;
    Skin skin;
    Server server;
    Player serverPlayer;
    TextButton msgBox,readyButton;
    private final int UDPPORT=54779,TCPPORT=54777;
    ServerLobbyListener serverLobbyListener;


    public ServerLobby(Menu main, Skin skin, Viewport viewport) {
        super(viewport);
        Gdx.input.setInputProcessor(this);
        this.main=main;
        serverPlayer=new Player(10);//set new connection id for server
        playersList.add(serverPlayer);
        msgBox=new TextButton("WAITING",skin,"small");
        msgBox.setBounds(510.0f, 270.0f, 680.0f, 90.0f);
        readyButton=new TextButton("Ready",skin,"small");
        readyButton.setBounds(510f,170f,680f,90f);
        readyButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                serverPlayer.ready=true;
                playerReady(10);
            }
        });
        server=new Server();
        Kryo kryo = server.getKryo();
        KryoHelper.registerClasses(kryo);
        server.start();
        try {
            server.bind(TCPPORT,UDPPORT);
        }catch (IOException e){
            e.printStackTrace();
        }
        serverLobbyListener=new ServerLobbyListener(this);
        server.addListener(serverLobbyListener);
        addActor(msgBox);
        addActor(readyButton);
    }

    public void addPlayer(int id){
        if(server.getConnections().length==1) {
            playersList.add(new Player(id));
        }
        /*BufferPlayerList bpl=new BufferPlayerList();
        bpl.setPlayersList(playersList);
        server.sendToAllUDP(bpl);*/

        msgBox.setText(Integer.toString(playersList.size()));
    }

    public void removePlayer(int id){
        for (Player player : playersList) {
            if(player.ID==id){
                playersList.remove(player);
                break;
            }
        }
        //BufferPlayerList bpl=new BufferPlayerList();
        //bpl.setPlayersList(playersList);
        //server.sendToAllUDP(bpl);
        msgBox.setText(Integer.toString(playersList.size()));

    }

    @Override
    public void addActor(Actor actor) {
        super.addActor(actor);
    }

    public void playerReady(int id) {
        boolean flag=true;
        for(Player player:playersList){
            if(player.ID==id){
                player.ready=true;
            }
            if(!player.ready)
                flag=false;
        }

        //BufferPlayerList bpl=new BufferPlayerList();
        //bpl.setPlayersList(playersList);
        //server.sendToAllUDP(bpl);
        if(flag)
            startGame();
    }

    void startGame(){
        Gdx.app.log("Game started"," :-)");
        BufferStartGame bsg=new BufferStartGame();
        server.sendToAllUDP(bsg);
    }
}
