package com.chewielouie.textadventure;

import static org.junit.Assert.*;

//import java.util.ArrayList;
//import java.util.List;
//import org.jmock.*;
//import org.jmock.integration.junit4.JMock;
import org.junit.Test;
//import org.junit.runner.RunWith;

//@RunWith(JMock.class)
public class NormalItemActionFactoryTests {

    //private Mockery mockery = new Mockery();

    @Test
    public void creates_ChangeDescriptionItemActions() {
        NormalItemActionFactory factory = new NormalItemActionFactory();

        ItemAction action = factory.create( "change item description:new description" );

        assertTrue( action instanceof ChangeItemDescriptionItemAction );
    }
}

