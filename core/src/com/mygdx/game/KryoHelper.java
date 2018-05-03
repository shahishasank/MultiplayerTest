package com.mygdx.game;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.serializers.CollectionSerializer;
import com.mygdx.game.buffers.BufferPlayerList;
import com.mygdx.game.buffers.BufferPlayerReady;
import com.mygdx.game.buffers.BufferStartGame;

import java.util.ArrayList;

/**
 * Created by Shasank on 4/18/2018.
 */

public class KryoHelper {
    public static void registerClasses(Kryo kryo){
        //register buffer classes
        kryo.register(BufferPlayerReady.class);
        kryo.register(BufferStartGame.class);
        kryo.register(BufferPlayerList.class);
        kryo.register(Player[].class);
        kryo.register(ArrayList.class,new CollectionSerializer());
        kryo.register(int[].class);

    }
}
