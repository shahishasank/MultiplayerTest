package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.minlog.Log;
import com.mygdx.game.buffers.BufferPlayerList;
import com.mygdx.game.buffers.BufferPlayerReady;
import com.mygdx.game.listeners.ClientLobbyListener;

import java.io.IOException;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by Shasank on 4/17/2018.
 */

public class ClientLobby extends Stage {

    Menu main;
    Skin skin;
    Client client;
    Player clientPlayer;
    TextButton msgBox,readyButton;
    ArrayList<Player> playersList=new ArrayList<Player>();
    ClientLobbyListener clientLobbyListener;
    private final int UDPPORT=54779,TCPPORT=54777;
    ClientLobby(Menu main, Skin skin, Viewport viewport) {
        setViewport(viewport);
        this.main=main;
        this.skin=skin;
        Gdx.input.setInputProcessor(this);
        msgBox=new TextButton("FIND",skin,"small");
        readyButton=new TextButton("ready",skin,"small");
        msgBox.setBounds(450f,350f,500,80f);
        readyButton.setBounds(450f,250f,500,80f);
        msgBox.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                findHost();
            }
        });
        addActor(msgBox);

    }

    private void findHost(){
        Gdx.app.log("find","host");

        client=new Client();
        InetAddress hostIP=null;
        try {

            hostIP = client.discoverHost(UDPPORT, 1000);
        }catch(Exception e){
            e.printStackTrace();
        }
        Kryo kryo = client.getKryo();
        KryoHelper.registerClasses(kryo);
        client.start();

        try {

            client.connect(3000, hostIP, TCPPORT,UDPPORT);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(!client.isConnected()){
            main.changeStage(new MainMenuStage(main,skin));
        }



        clientLobbyListener=new ClientLobbyListener(this);
        client.addListener(clientLobbyListener);
        readyButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.log("ready","ready");
                readyButton.setText("Wait...");
                clientPlayer=new Player(client.getID());
                clientPlayer.ready=true;
                BufferPlayerReady bpr=new BufferPlayerReady();
                client.sendUDP(bpr);
            }
        });
        addActor(readyButton);

    }

    @Override
    public void addActor(Actor actor) {
        super.addActor(actor);
    }

    public void UpdatePlayerList(BufferPlayerList bpl){
        playersList.clear();
        playersList.addAll(bpl.getPlayersList());
        msgBox.setText(Integer.toString(playersList.size()));
    }

    public void startGame(){
        Gdx.app.log("Game Started",":-)");
        msgBox.setText("z");
    }


    public void Disconnected(){

    }

}
