package com.chewielouie.textadventure.action;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import com.chewielouie.textadventure.TextAdventureModel;
import com.chewielouie.textadventure.UserInventory;
import org.jmock.*;
import org.jmock.integration.junit4.JMock;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(JMock.class)
public class UserActionFactoryTests {

    private Mockery mockery = new Mockery();

    @Test
    public void create_show_inventory_action_makes_right_action() {
        final UserInventory inventory = mockery.mock( UserInventory.class );
        final TextAdventureModel model = mockery.mock( TextAdventureModel.class );
        UserActionFactory factory = new UserActionFactory();

        Action action = factory.createShowInventoryAction( inventory, model );

        assertThat( action, is( instanceOf( ShowInventory.class ) ) );
    }
}

